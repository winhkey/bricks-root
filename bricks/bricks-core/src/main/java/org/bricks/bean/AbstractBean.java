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

package org.bricks.bean;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.lang.reflect.Field;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 抽象数据Bean
 *
 * @author fuzy
 * 
 */
public class AbstractBean implements Bean
{

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this, SHORT_PREFIX_STYLE)
        {

            @Override
            protected boolean accept(Field field)
            {
                return super.accept(field) && acceptField(field);
            }

        }.toString();
    }

    /**
     * 接受字段
     *
     * @param field 字段
     * @return 是否接受
     */
    protected boolean acceptField(Field field)
    {
        return !field.isAnnotationPresent(JsonIgnore.class);
    }

}
