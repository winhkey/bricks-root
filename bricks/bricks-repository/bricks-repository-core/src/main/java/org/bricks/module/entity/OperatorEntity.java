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

/**
 * 操作员
 *
 * @param <T> 子类
 */
public interface OperatorEntity<T> extends TenantEntity<T>
{

    /**
     * 设置创建者id
     *
     * @param operatorId 创建者id
     * @return 对象
     */
    T setCreator(Integer operatorId);

    /**
     * @return 创建者id
     */
    Integer getCreator();

    /**
     * 设置创建者
     *
     * @param operatorName 创建者
     * @return 对象
     */
    T setCreatorName(String operatorName);

    /**
     * @return 创建者
     */
    String getCreatorName();

    /**
     * 设置修改者id
     *
     * @param operatorId 修改者id
     * @return 对象
     */
    T setUpdater(Integer operatorId);

    /**
     * @return 修改者id
     */
    Integer getUpdater();

    /**
     * 设置修改者
     *
     * @param operatorName 修改者
     * @return 对象
     */
    T setUpdaterName(String operatorName);

    /**
     * @return 修改者
     */
    String getUpdaterName();

}
