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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import org.bricks.module.bean.TableConfig;
import org.bricks.module.validate.filter.TableValidateFilter;

/**
 * excel表注解
 *
 * @author fuzy
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Excel
{

    /**
     * @return 起始行
     */
    int startRow() default 1;

    /**
     * @return 唯一列
     */
    Unique[] uniques() default {};

    /**
     * @return 自定义过滤器
     */
    String filterName() default "";

    /**
     * @return 自定义过滤器
     */
    Class<? extends TableValidateFilter> filterClass() default Excel.UselessTableFilter.class;

    /**
     * 默认无用filter
     *
     * @author fuzy
     */
    final class UselessTableFilter implements TableValidateFilter
    {

        @Override
        public boolean validate(List<Map<Integer, String>> data, TableConfig config)
        {
            return false;
        }

    }

}
