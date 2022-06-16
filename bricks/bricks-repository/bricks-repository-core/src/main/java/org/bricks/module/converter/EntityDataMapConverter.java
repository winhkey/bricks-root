/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks-root)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bricks.module.converter;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.module.constants.Constants.PoiConstants.ERR_COL;
import static org.bricks.module.enums.DataType.STRING;
import static org.bricks.utils.FunctionUtils.accept;
import static org.bricks.utils.ObjectUtils.getEnumValue;
import static org.bricks.utils.ObjectUtils.getFieldValue;
import static org.bricks.utils.ObjectUtils.setFieldValue;
import static org.bricks.utils.ReflectionUtils.getDeclaredField;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import org.bricks.converter.AbstractParameterConverter;
import org.bricks.exception.BaseException;
import org.bricks.module.bean.ColumnConfig;
import org.bricks.module.enums.DataType;

/**
 * 对象转数据
 *
 * @author fuzy
 * 
 */
@Component
public class EntityDataMapConverter extends AbstractParameterConverter<Object, Map<Integer, String>, Object[]>
{

    @Override
    @SuppressWarnings(UNCHECKED)
    protected Map<Integer, String> from(Object m, Object[] objects)
    {
        Map<Integer, String> map = newLinkedHashMap();
        int i = 0;
        List<String> fieldList = (List<String>) objects[0];
        Map<Integer, ColumnConfig> columnMap = (Map<Integer, ColumnConfig>) objects[1];
        Map<String, ColumnConfig> fieldMap = columnMap.values()
                .stream()
                .collect(toMap(ColumnConfig::getField, c -> c, (o, n) -> n));
        for (String field : fieldList)
        {
            Object value = getFieldValue(m, field);
            ColumnConfig configParam = fieldMap.get(field);
            String v = "";
            if (value != null)
            {
                String format = ofNullable(configParam).map(ColumnConfig::getFormat)
                        .orElse(null);
                v = ofNullable(configParam).map(ColumnConfig::getDataType)
                        .orElse(STRING)
                        .format(value, format);
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
        ColumnConfig configParam = configMap.get(key);
        if (configParam != null)
        {
            String strValue = entry.getValue();
            String field = configParam.getField();
            try
            {
                Class<?> clazz = getDeclaredField(obj.getClass(), field, true).getType();
                Object value = clazz.isEnum() ? getEnumValue(clazz, strValue) : getValue(configParam, strValue);
                setFieldValue(obj, field, value);
            }
            catch (Exception e)
            {
                throw new BaseException(e);
            }
        }
    }

    /**
     * 根据字段类型转换值
     *
     * @param configParam 配置
     * @param value 值
     * @return 对象
     */
    private static Object getValue(ColumnConfig configParam, String value)
    {
        DataType dataType = configParam.getDataType();
        String format = configParam.getFormat();
        return dataType.parse(value, format);
    }

}
