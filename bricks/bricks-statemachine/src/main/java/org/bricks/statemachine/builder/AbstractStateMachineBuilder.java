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

package org.bricks.statemachine.builder;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;
import static org.bricks.utils.ReflectionUtils.getDeclaredAnnotation;
import static org.springframework.statemachine.config.StateMachineBuilder.builder;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bricks.annotation.NoLog;
import org.bricks.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 状态机构建抽象类
 *
 * @author fuzy
 *
 * @param <S> 状态
 * @param <E> 事件
 */
public abstract class AbstractStateMachineBuilder<S, E> implements StateMachineBuilder<S, E>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 状态枚举类型
     */
    protected Class<S> stateClass;

    /**
     * 事件枚举类型
     */
    protected Class<E> eventClass;

    /**
     * 状态机id
     */
    private String machineId;

    /**
     * spring工厂
     */
    @Resource
    protected BeanFactory beanFactory;

    /**
     * 初始化获取状态机id
     */
    @PostConstruct
    @SuppressWarnings(UNCHECKED)
    public void init()
    {
        Class<?> clazz = getClass();
        machineId = ofNullable(getDeclaredAnnotation(clazz, Service.class)).map(Service::value)
                .orElseGet(() -> ofNullable(getDeclaredAnnotation(clazz, Component.class)).map(Component::value)
                        .orElseThrow(() -> new BaseException("缺少@Service或@Component注解")));
        if (isBlank(machineId))
        {
            throw new BaseException("@Service或@Component注解缺少value");
        }
        List<Class<?>> classList = getComponentClassList(getClass(), StateMachineBuilder.class);
        stateClass = (Class<S>) classList.get(0);
        eventClass = (Class<E>) classList.get(1);
    }

    @NoLog
    @Override
    public StateMachine<S, E> build(boolean autostart)
    {
        StateMachine<S, E> machine = null;
        Builder<S, E> builder = builder();
        try
        {
            builder.configureConfiguration()
                    .withConfiguration()
                    .machineId(machineId)
                    .beanFactory(beanFactory)
                    .listener(new StateMachineListenerAdapter<S, E>()
                    {

                        @Override
                        public void stateChanged(State<S, E> from, State<S, E> to)
                        {
                            log.debug("state changed from {} to {}", ofNullable(from).map(State::getId)
                                    .orElse(null), to.getId());
                        }

                    });
            StateConfigurer<S, E> stateConfigurer = builder.configureStates()
                    .withStates();
            configureStates(stateConfigurer);
            configureTransitions(builder.configureTransitions());
            machine = builder.build();
            if (autostart)
            {
                machine.start();
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return machine;
    }

    /**
     * 配置状态
     *
     * @param stateConfigurer 状态配置器
     */
    protected abstract void configureStates(StateConfigurer<S, E> stateConfigurer);

    /**
     * 配置状态与事件的流程
     *
     * @param transitionConfigurer 配置器
     */
    protected abstract void configureTransitions(StateMachineTransitionConfigurer<S, E> transitionConfigurer);

}
