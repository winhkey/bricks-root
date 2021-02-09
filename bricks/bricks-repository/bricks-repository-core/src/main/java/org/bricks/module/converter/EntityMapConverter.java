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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.text.MessageFormat.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.utils.FunctionUtils.accept;
import static org.bricks.utils.ReflectionUtils.addDeclaredFields;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bricks.converter.AbstractVoidConverter;
import org.bricks.entity.Entity;
import org.springframework.stereotype.Component;

/**
 * entity转map
 *
 * @author fuzy
 *
 */
@Component
public class EntityMapConverter extends AbstractVoidConverter<Object, Map<String, Object>>
{

    @Override
    protected Map<String, Object> from(Object m)
    {
        Map<String, Object> conditionMap = newHashMap();
        buildCondition(m, "", conditionMap);
        return conditionMap;
    }

    /**
     * 构造条件
     *
     * @param object 对象
     * @param root root
     * @param conditionMap 条件map
     */
    protected void buildCondition(Object object, String root, Map<String, Object> conditionMap)
    {
        ofNullable(object).ifPresent(o ->
        {
            List<Field> fieldList = newArrayList();
            addDeclaredFields(object.getClass(), fieldList, true, false);
            fieldList.forEach(accept(field ->
            {
                field.setAccessible(true);
                String name = field.getName();
                if (isNotBlank(root))
                {
                    name = format("{0}.{1}", root, name);
                }
                final String n = name;
                ofNullable(field.get(object)).ifPresent(value ->
                {
                    if (value instanceof String)
                    {
                        if (isNotBlank((String) value))
                        {
                            conditionMap.put(n, value);
                        }
                    }
                    else if (value instanceof Collection && isNotEmpty((Collection<?>) value))
                    {
                        conditionMap.put(n, value);
                    }
                    else if (value instanceof Map && isNotEmpty((Map<?, ?>) value))
                    {
                        conditionMap.put(n, value);
                    }
                    else if (value instanceof Enum)
                    {
                        conditionMap.put(n, ((Enum<?>) value).ordinal());
                    }
                    else if (value instanceof Entity)
                    {
                        Entity<?> entity = (Entity<?>) value;
                        if (entity.getId() == null)
                        {
                            buildCondition(entity, n, conditionMap);
                        }
                        else
                        {
                            conditionMap.put(format("{0}.id", n), entity.getId());
                        }
                    }
                    else
                    {
                        conditionMap.put(n, value);
                    }
                });
            }, null, log, null));
        });
    }

}
