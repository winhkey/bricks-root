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

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 缓存
 *
 * @author fuzy
 *
 */
public abstract class AbstractCacheService implements CacheService
{

    /**
     * 默认缓存名
     */
    protected static final String DEFAULT_CACHE = "defaultCache";

    /**
     * 缓存管理器
     */
    protected CacheManager cacheManager;

    @Override
    public void put(String cacheName, String key, Object value)
    {
        if (key != null && value != null)
        {
            ofNullable(getCache(cacheName)).ifPresent(cache -> cache.put(key, value));
        }
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public <T> T get(String cacheName, String key)
    {
        return ofNullable(key).map(k -> getCache(cacheName))
                .map(cache -> cache.get(key))
                .map(wrapper -> (T) wrapper.get())
                .orElse(null);
    }

    @Override
    public void delete(String cacheName, String key)
    {
        ofNullable(getCache(cacheName)).ifPresent(cache -> cache.evict(key));
    }

    @Override
    public void clear(String cacheName, String key)
    {
        Cache cache = getCache(cacheName);
        if (cache != null)
        {
            if (isBlank(key))
            {
                cache.clear();
            }
            else
            {
                cache.evict(key);
            }
        }
    }

    /**
     * 获取缓存库
     *
     * @param cacheName 缓存名
     * @return 缓存库
     */
    protected Cache getCache(String cacheName)
    {
        return cacheManager.getCache(isNotBlank(cacheName) ? cacheName : DEFAULT_CACHE);
    }

}
