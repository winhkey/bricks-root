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
        public static final Pattern FIELD_PATTERN = Pattern.compile(
                "((?<connector>OR|AND)@)?(?<field>[\\w.]+)(@(?<operator>((?!@?(INNER|LEFT|RIGHT)).)+))?(@(?<g>INNER|LEFT|RIGHT))?");

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
        public static final String EXCEL_IS_NULL = "error.excel.isNull";

        /**
         * Excel文件字段超长提示
         */
        public static final String EXCEL_TOO_LONG = "error.excel.tooLong";

        /**
         * Excel文件字段错误格式提示
         */
        public static final String EXCEL_ERR_FOMAT = "error.excel.format";

        /**
         * Excel文件字段错误值提示
         */
        public static final String EXCEL_ERR_VALUE = "error.excel.value";

        /**
         * Excel文件字段唯一错误提示
         */
        public static final String EXCEL_ERR_UNIQUE = "error.excel.unique";

    }

    /**
     * 租户常量
     */
    public static class TenantConstants
    {

        /**
         * 默认租户
         */
        public static final String DEFAULT_TENANT_ID = "default_tenant";

    }

}
