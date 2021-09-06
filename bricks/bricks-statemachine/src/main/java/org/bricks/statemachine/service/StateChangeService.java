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

/**
 * 改变状态接口
 *
 * @author fuzy
 *
 * @param <I> 主键
 * @param <S> 状态
 * @param <E> 事件
 */
public interface StateChangeService<I, S, E>
{

    /**
     * 改变状态
     *
     * @param id 主键
     * @param event 事件
     * @return 结果
     */
    boolean changeState(I id, E event);

    /**
     * 当前状态
     *
     * @param id 主键
     * @return 状态
     */
    S currentState(I id);

}
