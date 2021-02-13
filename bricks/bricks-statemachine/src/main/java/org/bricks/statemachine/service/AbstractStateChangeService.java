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

import static java.util.Optional.ofNullable;
import static org.springframework.messaging.support.MessageBuilder.withPayload;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.bricks.statemachine.builder.StateMachineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.region.Region;

/**
 * 改变状态抽象类
 *
 * @author fuzy
 *
 * @param <S> 状态
 * @param <E> 事件
 */
public abstract class AbstractStateChangeService<S, E> implements StateChangeService<S, E>
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
    private StateMachinePersist<S, E, Map<String, Object>> stateMachinePersist;

    /**
     * 存储服务
     */
    private StateMachinePersister<S, E, Map<String, Object>> persister;

    @PostConstruct
    public void init()
    {
        persister = new DefaultStateMachinePersister<>(stateMachinePersist);
    }

    @Override
    public boolean sendEvent(E event, String header, Map<String, Object> condition)
    {
        synchronized (String.valueOf(condition)
                .intern())
        {
            boolean result = false;
            StateMachine<S, E> machine = null;
            try
            {
                machine = builder.build(true);
                persister.restore(machine, condition);
                result = machine.sendEvent(withPayload(event).setHeader(header, condition)
                        .build());
                persister.persist(machine, condition);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
            finally
            {
                ofNullable(machine).ifPresent(Region::stop);
            }
            return result;
        }
    }

}
