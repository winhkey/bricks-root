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

package org.bricks.proxy;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newConcurrentMap;

import java.util.List;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * 动态代理缓存
 *
 * @author fuzy
 *
 */
@UtilityClass
public class InvocationHandlerUtils
{

    /**
     * 分组，代理注解名-代理类列表
     */
    private static Map<String, List<Class<?>>> map = newConcurrentMap();

    /**
     * 添加代理类
     *
     * @param key 代理注解名
     * @param clazz 代理类
     */
    public static void addClass(String key, Class<?> clazz)
    {
        map.computeIfAbsent(key, k -> newArrayList())
                .add(clazz);
    }

    /**
     * 获取代理列表
     *
     * @param key 代理注解名
     * @return 代理列表
     */
    public static List<Class<?>> getClass(String key)
    {
        return map.get(key);
    }

}
