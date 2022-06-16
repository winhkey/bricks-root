package org.bricks.module.validate.filter;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.bricks.module.constants.Constants.PoiConstants.EXCEL_ERR_FOMAT;
import static org.bricks.module.constants.Constants.PoiConstants.EXCEL_ERR_VALUE;
import static org.bricks.module.constants.Constants.PoiConstants.EXCEL_IS_NULL;
import static org.bricks.module.constants.Constants.PoiConstants.EXCEL_TOO_LONG;
import static org.bricks.utils.ObjectUtils.getEnumValue;
import static org.bricks.utils.ReflectionUtils.addDeclaredFields;
import static org.bricks.utils.RegexUtils.matches;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import org.bricks.module.bean.ColumnConfig;
import org.bricks.module.enums.DataType;

/**
 * 基本行级校验
 *
 * @author fuzy
 */
@Service("basicRowValidateFilter")
public class BasicRowValidateFilter extends AbstractRowValidateFilter
{

    /**
     * 正则缓存
     */
    private final Map<String, Pattern> patternMap = newHashMap();

    @Override
    protected boolean validate(int row, int column, ColumnConfig columnConfig, Map<Integer, String> dataMap,
            Class<?> entityClass)
    {
        // 必填校验
        boolean result = mandatoryValidate(row, column, columnConfig, dataMap);
        // 长度校验
        boolean flag = lengthValidate(row, column, columnConfig, dataMap);
        result = flag && result;
        // 正则校验
        flag = regexValidate(row, column, columnConfig, dataMap);
        result = flag && result;
        // 枚举校验
        flag = enumValidate(row, column, columnConfig, dataMap, entityClass);
        result = flag && result;
        return result;
    }

    /**
     * 必填校验
     *
     * @param row 行序号
     * @param column 列序号
     * @param columnConfig 列配置
     * @param dataMap 行数据
     * @return 结果
     */
    private boolean mandatoryValidate(int row, int column, ColumnConfig columnConfig, Map<Integer, String> dataMap)
    {
        String value = dataMap.get(column);
        if (columnConfig.isMandatory() && isBlank(value))
        {
            setErrorMessage(dataMap, messageSourceAccessorMap.get(getLocale().toString())
                    .getMessage(EXCEL_IS_NULL, new Object[] {row + 1, column + 1}));
            return false;
        }
        return true;
    }

    /**
     * 长度校验
     *
     * @param row 行序号
     * @param column 列序号
     * @param columnConfig 列配置
     * @param dataMap 行数据
     * @return 结果
     */
    private boolean lengthValidate(int row, int column, ColumnConfig columnConfig, Map<Integer, String> dataMap)
    {
        String value = dataMap.get(column);
        int maxLength = columnConfig.getMaxLength();
        if (maxLength > 0 && value.length() > maxLength)
        {
            setErrorMessage(dataMap, messageSourceAccessorMap.get(getLocale().toString())
                    .getMessage(EXCEL_TOO_LONG, new Object[] {row + 1, column + 1}));
            return false;
        }
        return true;
    }

    /**
     * 正则校验
     *
     * @param row 行序号
     * @param column 列序号
     * @param columnConfig 列配置
     * @param dataMap 行数据
     * @return 结果
     */
    private boolean regexValidate(int row, int column, ColumnConfig columnConfig, Map<Integer, String> dataMap)
    {
        String value = dataMap.get(column);
        String regex = columnConfig.getRegex();
        if (isNotBlank(regex))
        {
            Pattern pattern = patternMap.get(regex);
            if (pattern == null)
            {
                pattern = Pattern.compile(regex);
                patternMap.put(regex, pattern);
            }
            if (isNotBlank(value) && !matches(pattern, value))
            {
                setErrorMessage(dataMap, messageSourceAccessorMap.get(getLocale().toString())
                        .getMessage(EXCEL_ERR_FOMAT, new Object[] {row + 1, column + 1}));
                return false;
            }
        }
        return true;
    }

    /**
     * 枚举校验
     *
     * @param row 行序号
     * @param column 列序号
     * @param columnConfig 列配置
     * @param dataMap 行数据
     * @param entityClass 实体类
     * @return 结果
     */
    private boolean enumValidate(int row, int column, ColumnConfig columnConfig, Map<Integer, String> dataMap,
            Class<?> entityClass)
    {
        boolean result = false;
        if (columnConfig.getDataType() == DataType.ENUM)
        {
            String value = dataMap.get(column);
            String fieldName = columnConfig.getField();
            List<Field> fieldList = newArrayList();
            addDeclaredFields(entityClass, fieldList, true, false);
            for (Field field : fieldList)
            {
                if (field.getName()
                        .equals(fieldName))
                {
                    result = enumValidate(value, field.getType());
                    break;
                }
            }
            if (!result)
            {
                setErrorMessage(dataMap, messageSourceAccessorMap.get(getLocale().toString())
                        .getMessage(EXCEL_ERR_VALUE, new Object[] {row + 1, column + 1}));
            }
        }
        else
        {
            result = true;
        }
        return result;
    }

    private boolean enumValidate(String value, Class<?> clazz)
    {
        if (isBlank(value))
        {
            return true;
        }
        // 处理枚举类型
        if (clazz.isEnum())
        {
            return getEnumValue(clazz, value) != null;
        }
        return false;
    }

}
