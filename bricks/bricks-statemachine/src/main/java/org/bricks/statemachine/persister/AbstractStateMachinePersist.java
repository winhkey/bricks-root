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

package org.bricks.statemachine.persister;

import static java.util.Optional.ofNullable;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.bricks.event.BricksEvent;
import org.bricks.event.publish.BricksEventPublish;
import org.bricks.exception.BaseException;
import org.bricks.statemachine.module.entity.StateEntity;
import org.bricks.statemachine.module.entity.StateRecordEntity;
import org.bricks.statemachine.module.service.StateEntityService;
import org.bricks.statemachine.module.service.StateRecordEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * 状态存储
 *
 * @author fuzy
 *
 * @param <S> 状态
 * @param <E> 事件
 * @param <I> 主键
 * @param <T> 状态对象
 * @param <R> 状态记录对象
 */
public abstract class AbstractStateMachinePersist<S, E, I, T extends StateEntity<S, T>, R extends StateRecordEntity<S>>
        implements BricksStateMachinePersist<S, E, I>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 状态机环境缓存
     */
    private final Map<I, StateMachineContext<S, E>> contextMap = new ConcurrentHashMap<>();

    /**
     * 状态枚举类型
     */
    protected Class<S> stateClass;

    /**
     * 事件枚举类型
     */
    protected Class<E> eventClass;

    /**
     * 状态实体
     */
    @Autowired
    protected StateEntityService<I, S, T> entityService;

    /**
     * 状态记录实体
     */
    @Autowired(required = false)
    protected StateRecordEntityService<I, S, R> recordEntityService;

    /**
     * 发布事件
     */
    @Resource
    protected BricksEventPublish bricksEventPublish;

    /**
     * 初始化获取状态机id
     */
    @PostConstruct
    @SuppressWarnings(UNCHECKED)
    public void init()
    {
        List<Class<?>> classList = getComponentClassList(getClass(), StateMachinePersist.class);
        stateClass = (Class<S>) classList.get(0);
        eventClass = (Class<E>) classList.get(1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void write(StateMachineContext<S, E> stateMachineContext, I id)
    {
        S state = stateMachineContext.getState();
        S stored = ofNullable(getExtendedState(stateMachineContext)).orElseGet(() -> entityService.findOne(id)
                .getState());
        log.debug("{} -> {}", stored, state);
        if (!state.equals(stored))
        {
            if (write())
            {
                if (entityService.updateState(id, state) <= 0)
                {
                    log.warn("未更新数据库");
                    bricksEventPublish.publish(notChangedEvent(id, stored, state));
                }
                else
                {
                    saveRecord(id, stored, state);
                    bricksEventPublish.publish(changedEvent(id, stored, state));
                }
            }
            setExtendedState(stateMachineContext.getExtendedState(), state);
            contextMap.put(id, stateMachineContext);
        }
    }

    /**
     * @return 是否持久化
     */
    protected boolean write()
    {
        return true;
    }

    @Override
    public StateMachineContext<S, E> read(I id)
    {
        return contextMap.computeIfAbsent(id,
                k -> ofNullable(entityService.findOne(id))
                        .map(t -> new DefaultStateMachineContext<S, E>(t.getState(), null, null,
                                setExtendedState(t.getState()), null, stateMachineId()))
                        .orElseThrow(() -> new BaseException("data not exists")));
    }

    /**
     * 保存状态变更记录
     *
     * @param id 业务id
     * @param source 变更前记录
     * @param target 变更后记录
     */
    protected void saveRecord(I id, S source, S target)
    {
        recordEntityService.saveRecord(id, source, target, null);
    }

    /**
     * 设置扩张状态
     *
     * @param state 状态
     * @return 扩展状态
     */
    protected ExtendedState setExtendedState(S state)
    {
        ExtendedState extendedState = new DefaultExtendedState();
        setExtendedState(extendedState, state);
        return extendedState;
    }

    /**
     * 设置扩展状态
     *
     * @param extendedState 扩展状态
     * @param state 状态
     */
    protected void setExtendedState(ExtendedState extendedState, S state)
    {
        extendedState.getVariables()
                .put("state", state);
    }

    /**
     * 获取扩展状态
     *
     * @param stateMachineContext 状态机环境
     * @return 扩展状态
     */
    protected S getExtendedState(StateMachineContext<S, E> stateMachineContext)
    {
        return stateMachineContext.getExtendedState()
                .get("state", stateClass);
    }

    /**
     * 状态变更事件
     *
     * @param id 业务id
     * @param source 变更前记录
     * @param target 变更后记录
     * @return 状态变更事件
     */
    protected abstract BricksEvent<?> changedEvent(I id, S source, S target);

    /**
     * 状态未变更事件
     *
     * @param id 业务id
     * @param source 变更前记录
     * @param target 变更后记录
     * @return 状态未变更事件
     */
    protected abstract BricksEvent<?> notChangedEvent(I id, S source, S target);

}
