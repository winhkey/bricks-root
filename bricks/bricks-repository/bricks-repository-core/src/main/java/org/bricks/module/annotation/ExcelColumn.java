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

package org.bricks.module.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

import org.bricks.module.bean.TableConfig;
import org.bricks.module.enums.DataType;
import org.bricks.module.validate.filter.RowValidateFilter;

/**
 * excel列注解
 *
 * @author fuzhiying
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface ExcelColumn
{

    /**
     * @return 列序号
     */
    int column() default -1;

    /**
     * @return 字段
     */
    String field() default "";

    /**
     * @return 列标题
     */
    String title() default "";

    /**
     * @return 数据类型
     */
    DataType dataType() default DataType.STRING;

    /**
     * @return 格式化
     */
    String format() default "";

    /**
     * @return 必填
     */
    boolean mandatory() default false;

    /**
     * @return 唯一
     */
    boolean unique() default false;

    /**
     * @return 正则
     */
    String regex() default "";

    /**
     * @return 最长
     */
    int maxLength() default 0;

    /**
     * @return 自定义过滤器
     */
    String filterName() default "";

    /**
     * @return 自定义过滤器
     */
    Class<? extends RowValidateFilter> filterClass() default UselessRowFilter.class;

    /**
     * 默认无用filter
     *
     * @author fuzhiying
     */
    final class UselessRowFilter implements RowValidateFilter
    {

        @Override
        public boolean validate(Map<Integer, String> data, TableConfig config)
        {
            return false;
        }

    }

}
