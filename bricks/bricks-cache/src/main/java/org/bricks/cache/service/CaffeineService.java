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

import javax.annotation.Resource;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Caffeine接口
 *
 * @author fuzy
 *
 */
@Service("caffeineService")
public class CaffeineService extends AbstractCacheService
{

    /**
     * 注入缓存管理器
     *
     * @param caffeineCacheManager 缓存管理器
     */
    @Resource(name = "caffeineCacheManager")
    public void setCacheManager(CacheManager caffeineCacheManager)
    {
        cacheManager = caffeineCacheManager;
    }

    @Override
    public void sync(String cacheName, String key, Object value)
    {
        // do nth.
    }

}
