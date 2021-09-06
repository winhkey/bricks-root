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

import org.springframework.statemachine.StateMachinePersist;

/**
 * 状态存储
 *
 * @author fuzy
 *
 * @param <S> 状态
 * @param <E> 事件
 * @param <I> 主键
 */
public interface BricksStateMachinePersist<S, E, I> extends StateMachinePersist<S, E, I>
{

    /**
     * @return 状态机id
     */
    String stateMachineId();

}
