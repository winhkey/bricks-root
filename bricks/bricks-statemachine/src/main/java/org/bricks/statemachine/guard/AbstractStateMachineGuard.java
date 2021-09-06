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

package org.bricks.statemachine.guard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.guard.Guard;

/**
 * 状态判断
 *
 * @author fuzy
 *
 * @param <S> 状态
 * @param <E> 事件
 */
public abstract class AbstractStateMachineGuard<S, E> implements Guard<S, E>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

}
