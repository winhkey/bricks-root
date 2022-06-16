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

package org.bricks.module.entity;

import java.time.LocalDateTime;

/**
 * 创建时间
 *
 * @author fuzy
 *
 * @param <T> 子类
 */
public interface TimeEntity<T>
{

    /**
     * @return 创建时间
     */
    LocalDateTime getCreateTime();

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     * @return 对象
     */
    T setCreateTime(LocalDateTime createTime);

    /**
     * @return 修改时间
     */
    LocalDateTime getUpdateTime();

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     * @return 对象
     */
    T setUpdateTime(LocalDateTime updateTime);

}
