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

package org.bricks.event.listener;

import org.bricks.event.BricksEvent;

/**
 * 事件监听接口
 * 
 * @author fuzy
 *
 * @param <S> 事件
 */
public interface BricksEventListener<S extends BricksEvent<?>>
{

    /**
     * 监听方法
     *
     * @param event 事件
     * @return 新事件
     */
    BricksEvent<?> onEvent(S event);

}
