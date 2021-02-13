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

package org.bricks.statemachine.persist;

import java.util.Map;

import org.bricks.module.service.EntityService;
import org.bricks.statemachine.module.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;

/**
 * 状态存储
 *
 * @param <S>
 * @param <E>
 * @param <I>
 * @param <T>
 */
public abstract class AbstractStateMachinePersist<S, E, I, T extends StateEntity<S, I, T>>
        implements StateMachinePersist<S, E, Map<String, Object>>
{

    /**
     * 数据库操作
     */
    @Autowired
    protected EntityService<I, T> entityService;

    @Override
    public void write(StateMachineContext<S, E> stateMachineContext, Map<String, Object> condition)
    {
        if (write())
        {
            S state = stateMachineContext.getState();
            T stored = entityService.findOne(condition, false);
            if (!stored.getState()
                    .equals(state))
            {
                entityService.save(stored.setState(state));
            }
        }
    }

    @Override
    public StateMachineContext<S, E> read(Map<String, Object> condition)
    {
        return new DefaultStateMachineContext<>(entityService.findOne(condition, false)
                .getState(), null, null, null, null, stateMachineId());
    }

    /**
     * @return 是否持久化
     */
    protected abstract boolean write();

    /**
     * @return 状态机id
     */
    protected abstract String stateMachineId();

}
