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

package org.bricks.async.work.service;

import static java.util.Optional.ofNullable;

import javax.annotation.Resource;

import org.bricks.async.work.task.BrickTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 执行工作抽象类
 *
 * @author fuzy
 *
 */
public abstract class AbstractBrickJobService implements BrickJobService {

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * traceExecutor
     */
    @Resource
    protected ThreadPoolTaskExecutor executor;

    /**
     * 同步执行单个任务
     *
     * @param task 任务
     * @param target 任务参数
     * @param <T> 任务参数
     */
    protected <T> void syncExecuteTask(BrickTask<T> task, T target) {
        ofNullable(task).ifPresent(t -> task.task(target));
    }

    /**
     * 异步执行单个任务
     *
     * @param task 任务
     * @param target 任务参数
     * @param <T> 任务参数
     */
    protected <T> void asyncExecuteTask(BrickTask<T> task, T target) {
        ofNullable(task).ifPresent(t -> executor.execute(() -> task.task(target)));
    }

}
