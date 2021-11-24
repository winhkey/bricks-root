package org.bricks.module.constants;

import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * 常量
 * 
 * @author fuzhiyign
 *
 */
@UtilityClass
public class Constants
{

    /**
     * 查询常量
     */
    public static class QueryConstants
    {

        /**
         * 属性条件正则
         */
        public static final Pattern FIELD_PATTERN = Pattern.compile("((?<connector>OR|AND)@)?(?<field>[\\w.]+)(@(?<operator>((?!@?(INNER|LEFT|RIGHT)).)+))?(@(?<g>INNER|LEFT|RIGHT))?");

    }

    /**
     * POI常量
     */
    public static class PoiConstants
    {

        /**
         * 错误字段号
         */
        public static final int ERR_COL = -1;

        /**
         * Excel文件字段非空提示
         */
        public static final String EXCEL_IS_NULL = "第{0}行第{1}列必填";

        /**
         * Excel文件字段超长提示
         */
        public static final String EXCEL_TOO_LONG = "第{0}行第{1}列超出最大长度";

        /**
         * Excel文件字段错误格式提示
         */
        public static final String EXCEL_ERR_FOMAT = "第{0}行第{1}列格式不对";

        /**
         * Excel文件字段错误值提示
         */
        public static final String EXCEL_ERR_VALUE = "第{0}行第{1}列值错误";

        /**
         * Excel文件字段唯一错误提示
         */
        public static final String EXCEL_ERR_UNIQUE = "第{0}行第{1}列值存在重复";

    }

}
