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

package org.bricks.async.work.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象线程
 *
 * @author fuzy
 *
 */
public abstract class AbstractThread implements Runnable {

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        try {
            doRun();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 执行线程
     */
    protected abstract void doRun();

}
