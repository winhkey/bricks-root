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

import java.util.List;

import org.bricks.cache.bean.CacheConfigItem;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 缓存容器配置
 *
 * @author fuzy
 *
 */
@Setter
@Getter
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "bricks.cache")
public class CacheConfigItemService
{

    /**
     * 缓存名
     */
    private String cacheNames;

    /**
     * 默认超时时间
     */
    private long expire = 300;

    /**
     * 默认刷新时间
     */
    private long refresh = 100;

    /**
     * 配置列表
     */
    private List<CacheConfigItem> items;

}
