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
import org.bricks.statemachine.module.entity.StateRecordEntity;

/**
 * 状态记录接口
 *
 * @author fuzy
 *
 * @param <I> 主键
 * @param <S> 状态
 * @param <T> 事件
 */
public interface StateRecordEntityService<I, S, T extends StateRecordEntity<S>> extends EntityService<I, T>
{

    /**
     * 状态记录入库
     *
     * @param id 主键
     * @param source 开始状态
     * @param target 结束状态
     * @return 记录
     */
    T saveRecord(I id, S source, S target);

}
