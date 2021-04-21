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

package org.bricks.statemachine.listener;

import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.annotation.PostConstruct;

import org.bricks.exception.BaseException;
import org.bricks.statemachine.module.entity.StateEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * 状态监听抽象类
 *
 * @author fuzy
 *
 */
public abstract class AbstractStateListener<S, E, I, T extends StateEntity<S, I, T>>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init()
    {
        WithStateMachine withStateMachine = getClass().getAnnotation(WithStateMachine.class);
        if (withStateMachine == null)
        {
            throw new BaseException("缺少@WithStateMachine注解");
        }
        if (isBlank(withStateMachine.id()))
        {
            throw new BaseException("@WithStateMachine注解缺少id");
        }
    }

}
