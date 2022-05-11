package org.bricks.module.enums;

import static org.bricks.constants.Constants.FormatConstants.DATETIME_FORMAT;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.bricks.enums.ValueEnum;
import org.bricks.utils.DateUtils;

/**
 * 数据类型
 *
 * @author fuzhiying
 *
 */
public enum DataType
{

    /**
     * boolean
     */
    BOOLEAN,

    /**
     * 字符串
     */
    STRING,

    /**
     * int
     */
    INTEGER
    {

        @Override
        public Object parse(String value, String format)
        {
            return Integer.parseInt(value);
        }

    },

    /**
     * double
     */
    DOUBLE
    {

        @Override
        public Object parse(String value, String format)
        {
            return Double.parseDouble(value);
        }

    },

    /**
     * 高精度
     */
    BIGDECIMAL
    {

        @Override
        public Object parse(String value, String format)
        {
            return new BigDecimal(value);
        }

    },

    /**
     * 日期时间
     */
    DATETIME
    {

        @Override
        public String format(Object value, String format)
        {
            return DateUtils.format((LocalDateTime) value, isBlank(format) ? DATETIME_FORMAT : format);
        }

        @Override
        public Object parse(String value, String format)
        {
            return DateUtils.parseDateTime(value, format);
        }

    },

    /**
     * 日期
     */
    DATE
    {

        @Override
        public String format(Object value, String format)
        {
            return DateUtils.format((LocalDate) value, isBlank(format) ? DATETIME_FORMAT : format);
        }

        @Override
        public Object parse(String value, String format)
        {
            return DateUtils.parseDate(value, format);
        }

    },

    /**
     * 时间
     */
    TIME
    {

        @Override
        public String format(Object value, String format)
        {
            return DateUtils.format((LocalTime) value, isBlank(format) ? DATETIME_FORMAT : format);
        }

        @Override
        public Object parse(String value, String format)
        {
            return DateUtils.parseTime(value, format);
        }

    },

    /**
     * 枚举
     */
    ENUM
    {

        @Override
        public String format(Object value, String format)
        {
            Object v = value;
            Class<?> clazz = v.getClass();
            if (clazz.isEnum() && ValueEnum.class.isAssignableFrom(clazz))
            {
                v = ((ValueEnum<?>) value).getValue();
            }
            return super.format(v, format);
        }

    };

    /**
     * 格式化字符串
     * 
     * @param value 值
     * @param format 格式
     * @return 结果
     */
    public String format(Object value, String format)
    {
        return String.valueOf(value);
    }

    /**
     * 字符串值转换
     * 
     * @param value 字符串
     * @param format 格式化
     * @return 结果
     */
    public Object parse(String value, String format)
    {
        return value;
    }

}
