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

package org.bricks.statemachine.service;

import static java.text.MessageFormat.format;
import static java.util.Optional.ofNullable;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

import javax.annotation.PostConstruct;

import org.bricks.annotation.NoLog;
import org.bricks.exception.BaseException;
import org.bricks.statemachine.builder.StateMachineBuilder;
import org.bricks.statemachine.persister.BricksStateMachinePersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.region.Region;

/**
 * 改变状态抽象类
 *
 * @author fuzy
 *
 * @param <I> 主键
 * @param <S> 状态
 * @param <E> 事件
 */
public abstract class AbstractStateChangeService<I, S, E> implements StateChangeService<I, S, E>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 状态机
     */
    @Autowired
    private StateMachineBuilder<S, E> builder;

    /**
     * 存储
     */
    @Autowired
    private BricksStateMachinePersist<S, E, I> stateMachinePersist;

    /**
     * 存储服务
     */
    private StateMachinePersister<S, E, I> persister;

    /**
     * 初始化
     */
    @PostConstruct
    public void init()
    {
        persister = new DefaultStateMachinePersister<>(stateMachinePersist);
    }

    @NoLog
    @Override
    public boolean changeState(I id, E event)
    {
        String key = format("{0}:{1}", event, id).intern();
        synchronized (key)
        {
            boolean result;
            StateMachine<S, E> machine = null;
            try
            {
                machine = builder.build(true);
                persister.restore(machine, id);
                result = machine.sendEvent(withPayload(event).setHeader(stateMachinePersist.stateMachineId(), id)
                        .build());
                log.debug("-> {} {}", event, result ? "成功" : "失败");
                if (result)
                {
                    persister.persist(machine, id);
                }
            }
            catch (Exception e)
            {
                throw new BaseException(e);
            }
            finally
            {
                ofNullable(machine).ifPresent(Region::stop);
            }
            return result;
        }
    }

    @Override
    public S currentState(I id)
    {
        synchronized (format("{1}", id).intern())
        {
            S state;
            StateMachine<S, E> machine = null;
            try
            {
                machine = builder.build(true);
                persister.restore(machine, id);
                state = machine.getState()
                        .getId();
            }
            catch (Exception e)
            {
                throw new BaseException(e);
            }
            finally
            {
                ofNullable(machine).ifPresent(Region::stop);
            }
            return state;
        }
    }

}
