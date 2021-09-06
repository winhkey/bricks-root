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

package org.bricks.aop.log;

import org.bricks.bean.AbstractBean;
import org.slf4j.Logger;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 日志切面线程变量
 *
 * @author fuzy
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class LogContext extends AbstractBean
{

    /**
     * 打印日志
     */
    private boolean doLog;

    /**
     * 执行开始时间
     */
    private long start;

    /**
     * 日志对象
     */
    private Logger logger;

}
