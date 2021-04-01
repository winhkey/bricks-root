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

package org.bricks.async.bean;

import static org.bricks.constants.Constants.NumberConstants.NUMBER_2000;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 线程池配置
 *
 * @author fuzy
 *
 */
@Setter
@Getter
@Accessors(chain = true)
public class ExecutorConfigItem extends AbstractAsyncConfigItem
{

    /**
     * 线程池初始化个数
     */
    private int corePoolSize = (int) (Runtime.getRuntime()
            .availableProcessors() / (1 - 0.9));

    /**
     * 队列容量
     */
    private int queueCapacity = NUMBER_2000;

}
