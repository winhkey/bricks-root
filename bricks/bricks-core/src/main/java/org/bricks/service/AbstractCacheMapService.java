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

package org.bricks.service;

import static com.google.common.collect.Maps.newConcurrentMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象缓存
 *
 * @author fuzy
 *
 * @param <T> 缓存类型
 */
public abstract class AbstractCacheMapService<T>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 缓存
     */
    protected final Map<String, T> cacheMap = newConcurrentMap();

    /**
     * 添加多个
     *
     * @param map map
     */
    public void putAll(Map<String, T> map)
    {
        cacheMap.putAll(map);
    }

    /**
     * 添加缓存
     *
     * @param key 键
     * @param t 值
     */
    public void put(String key, T t)
    {
        if (isNotBlank(key) && t != null)
        {
            cacheMap.put(key, t);
        }
    }

    /**
     * 查询缓存
     *
     * @param key 键
     * @return 结果
     */
    public T get(String key)
    {
        return isBlank(key) ? null : cacheMap.get(key);
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 结果
     */
    public T remove(String key)
    {
        return isBlank(key) ? null : cacheMap.remove(key);
    }

}
