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

package org.bricks.statemachine.module.service;

import org.bricks.module.service.EntityService;
import org.bricks.statemachine.module.entity.StateEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * 状态机对象
 *
 * @param <I> 主键
 * @param <S> 状态
 * @param <T> 子类
 */
public interface StateEntityService<I, S, T extends StateEntity<S, T>> extends EntityService<I, T>
{

    /**
     * 更新状态
     *
     * @param id 主键
     * @param state 状态
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    int updateState(I id, S state);

}
