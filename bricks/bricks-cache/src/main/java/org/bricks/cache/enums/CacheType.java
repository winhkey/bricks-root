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

package org.bricks.cache.enums;

/**
 * 缓存类型
 *
 * @author fuzy
 *
 */
public enum CacheType
{

    /**
     * 无缓存
     */
    NONE,

    /**
     * 本地缓存
     */
    LOCAL,

    /**
     * redis
     */
    REDIS,

    /**
     * 同时开启
     */
    BOTH

}
