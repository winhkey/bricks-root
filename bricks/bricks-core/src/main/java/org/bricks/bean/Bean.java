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

package org.bricks.bean;

import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;

/**
 * 数据Bean接口
 *
 * @author fuzy
 *
 */
public interface Bean
{

    /**
     * 返回自对象
     * 
     * @param <T> 自类型
     * @return 自对象
     */
    @SuppressWarnings(UNCHECKED)
    default <T> T self()
    {
        return (T) this;
    }

}
