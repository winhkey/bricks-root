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

package org.bricks.async.controller;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import javax.annotation.Resource;

import org.bricks.async.bean.TaskRequest;
import org.bricks.async.work.task.BrickTask;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手动触发定时任务
 *
 * @author fuzy
 *
 */
@RestController
public class TaskController
{

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private ThreadPoolTaskExecutor traceExecutor;

    @PostMapping("async/task")
    public void task(@RequestBody TaskRequest request)
    {
        if (isNotEmpty(request.getTasks()))
        {
            request.getTasks()
                    .stream()
                    .map(taskId -> applicationContext.getBean(taskId, BrickTask.class))
                    .forEach(task -> traceExecutor.execute(() -> task.task(request.getParam())));
        }
    }

}
