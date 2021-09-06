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

package org.bricks.statemachine.module.entity;

/**
 * 状态记录
 *
 * @author fuzy
 *
 * @param <S> 状态
 */
public interface StateRecordEntity<S>
{

    /**
     * @return 前状态
     */
    S getSource();

    /**
     * @return 后状态
     */
    S getTarget();

}
