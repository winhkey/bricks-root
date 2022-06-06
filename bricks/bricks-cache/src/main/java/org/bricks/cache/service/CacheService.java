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

package org.bricks.cache.service;

/**
 * 缓存
 *
 * @author fuzy
 *
 */
public interface CacheService
{

    /**
     * 缓存对象
     *
     * @param cacheName 缓存名
     * @param key 键
     * @param value 对象
     */
    void put(String cacheName, String key, Object value);

    /**
     * 获取缓存对象
     *
     * @param cacheName 缓存名
     * @param key 键
     * @param <T> 类型
     * @return 对象
     */
    <T> T get(String cacheName, String key);

    /**
     * 删除keys
     * 
     * @param cacheName 缓存名
     * @param key 键
     */
    void delete(String cacheName, String key);

    /**
     * 清空缓冲区
     *
     * @param cacheName 缓存名
     * @param key 键
     */
    void clear(String cacheName, String key);

    /**
     * 同步分布式缓存
     *
     * @param cacheName 缓存名
     * @param key 键
     * @param value 值
     */
    void sync(String cacheName, String key, Object value);

}
