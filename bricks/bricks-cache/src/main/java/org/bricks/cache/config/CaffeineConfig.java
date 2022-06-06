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

package org.bricks.cache.config;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;

import javax.annotation.Resource;

import org.bricks.cache.service.CacheConfigItemService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Caffeine配置
 * 
 * @author fuzy
 *
 */
@Configuration
public class CaffeineConfig
{

    /**
     * 缓存配置
     */
    @Resource
    private CacheConfigItemService cacheConfigItemService;

    /**
     * CacheManager
     *
     * @param cacheLoader cacheLoader
     * @return CacheManager
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager(CacheLoader<Object, Object> cacheLoader)
    {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(10_000)
                .expireAfterAccess(cacheConfigItemService.getRefresh(), SECONDS);
        CaffeineCacheManager cacheManager = ofNullable(cacheConfigItemService.getCacheNames()).map(n -> n.split(","))
                .map(CaffeineCacheManager::new)
                .orElseGet(CaffeineCacheManager::new);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheLoader(cacheLoader);
        cacheManager.setAllowNullValues(true);
        return cacheManager;
    }

    /**
     * @return cacheLoader
     */
    @Bean
    public CacheLoader<Object, Object> cacheLoader()
    {
        return new CacheLoader<Object, Object>()
        {

            @Override
            public Object load(Object key)
            {
                return null;
            }

            @Override
            public Object reload(Object key, Object value)
            {
                return value;
            }

        };
    }

}
