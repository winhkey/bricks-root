package org.bricks.module.converter;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.constants.Constants.FormatConstants.DATETIME_FORMAT;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.module.constants.Constants.PoiConstants.ERR_COL;
import static org.bricks.utils.DateUtils.format;
import static org.bricks.utils.DateUtils.parse;
import static org.bricks.utils.FunctionUtils.accept;
import static org.bricks.utils.ObjectUtils.getEnumValue;
import static org.bricks.utils.ObjectUtils.getFieldValue;
import static org.bricks.utils.ObjectUtils.setFieldValue;
import static org.bricks.utils.ReflectionUtils.getDeclaredField;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.converter.AbstractConverter;
import org.bricks.enums.ValueEnum;
import org.bricks.exception.BaseException;
import org.bricks.module.bean.ColumnConfig;
import org.bricks.module.enums.DataType;
import org.springframework.stereotype.Component;

/**
 * 对象转数据
 *
 * @author fuzy
 *
 */
@Component
public class EntityDataMapConverter extends AbstractConverter<Object, Map<Integer, String>, Object[]>
{

    @Override
    @SuppressWarnings(UNCHECKED)
    protected Map<Integer, String> from(Object m, Object[] objects)
    {
        Map<Integer, String> map = newLinkedHashMap();
        int i = 0;
        List<String> fieldList = (List<String>) objects[0];
        for (String field : fieldList)
        {
            Object value = getFieldValue(m, field);
            String v = "";
            if (value != null)
            {
                Class<?> clazz = value.getClass();
                if (clazz.isEnum() && ValueEnum.class.isAssignableFrom(clazz))
                {
                    value = ((ValueEnum<?>) value).getValue();
                }
                if (value instanceof LocalDateTime)
                {
                    v = format((LocalDateTime) value, DATETIME_FORMAT);
                }
                else
                {
                    v = String.valueOf(value);
                }
            }
            map.put(i++, v);
        }
        return map;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    protected Object reverseFrom(Map<Integer, String> m, Object[] objects)
    {
        Object t = null;
        if (isBlank(m.get(ERR_COL)))
        {
            try
            {
                t = ((Class<?>) objects[0]).newInstance();
                Map<Integer, ColumnConfig> columnMap = (Map<Integer, ColumnConfig>) objects[1];
                addEntry(m, t, columnMap);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                return null;
            }
        }
        else
        {
            List<Map<Integer, String>> errorList = (List<Map<Integer, String>>) objects[2];
            errorList.add(m);
        }
        return t;
    }

    /**
     * 向列表中增加对象
     *
     * @param dataMap 一行数据
     * @param obj 对象
     * @param columnMap 行配置
     */
    private void addEntry(Map<Integer, String> dataMap, Object obj, Map<Integer, ColumnConfig> columnMap)
    {
        dataMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey() != ERR_COL && isNotBlank(entry.getValue()))
                .forEach(accept(entry -> setEntryValues(obj, entry, columnMap), null, null, null,
                        t -> t instanceof BaseException ? (BaseException) t : new BaseException(t)));
    }

    /**
     * 根据对应字段设置值
     *
     * @param obj 对象
     * @param entry 字段值
     * @param configMap 行配置
     */
    private void setEntryValues(Object obj, Entry<Integer, String> entry, Map<Integer, ColumnConfig> configMap)
    {
        Integer key = entry.getKey();
        String strValue = entry.getValue();
        ColumnConfig configParam = configMap.get(key);
        String field = configParam.getField();
        Class<?> clazz;
        try
        {
            clazz = getDeclaredField(obj.getClass(), field, true).getType();
            Object value = clazz.isEnum() ? getEnumValue(clazz, strValue)
                    : getValue(configParam.getDataType(), strValue);
            setFieldValue(obj, field, value);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    /**
     * 根据字段类型转换值
     *
     * @param dataType 数据类型
     * @param value 值
     * @return 对象
     */
    private static Object getValue(DataType dataType, String value)
    {
        Object cellValue = null;
        if (isNotBlank(value))
        {
            switch (dataType)
            {
                case INTEGER:
                    cellValue = Integer.parseInt(value);
                    break;
                case DOUBLE:
                    cellValue = Double.parseDouble(value);
                    break;
                case BIGDECIMAL:
                    cellValue = new BigDecimal(value);
                    break;
                case DATE:
                    cellValue = parse(value, DATETIME_FORMAT);
                    break;
                case BOOLEAN:
                    // TODO: boolean类型
                    // break;
                case STRING:
                default:
                    cellValue = value;
                    break;
            }
        }
        return cellValue;
    }

}
