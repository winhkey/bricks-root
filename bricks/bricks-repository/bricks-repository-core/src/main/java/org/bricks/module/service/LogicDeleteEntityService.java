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

package org.bricks.module.service;

import java.util.Collection;

/**
 * 逻辑删除实体接口
 *
 * @author fuzy
 *
 * @param <T> 实体类型
 * @param <I> ID类型
 */
public interface LogicDeleteEntityService<T, I> extends EntityService<T, I>
{

    /**
     * 逻辑删除
     *
     * @param id id
     */
    void logicDeleteId(I id);

    /**
     * 逻辑批量删除
     *
     * @param ids id列表
     */
    void logicDeleteIds(Collection<I> ids);

    /**
     * 逻辑删除
     *
     * @param t 对象
     */
    void logicDelete(T t);

    /**
     * 逻辑批量删除
     *
     * @param coll 对象列表
     */
    void logicDeleteCollection(Collection<T> coll);

}
