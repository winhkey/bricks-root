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

package org.bricks.async.service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import javax.annotation.Resource;

import org.bricks.async.bean.ExecutorConfigItem;
import org.bricks.async.bean.SchedulerConfigItem;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * 异步工厂
 *
 * @author fuzy
 *
 */
@Component
public class AsyncFactory
{

    /**
     * 异步配置加载
     */
    @Resource
    private AsyncItemService asyncItemService;

    /**
     * @return 线程池
     */
    public ThreadPoolTaskExecutor buildExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        ExecutorConfigItem item = asyncItemService.getExecutor();
        executor.setCorePoolSize(item.getCorePoolSize());
        executor.setMaxPoolSize(Math.max(item.getMaxPoolSize(), (int) (item.getCorePoolSize() * 1.5)));
        executor.setQueueCapacity(item.getQueueCapacity());
        String threadNamePrefix = item.getThreadNamePrefix();
        if (isNotBlank(threadNamePrefix))
        {
            executor.setThreadNamePrefix(threadNamePrefix.concat("-"));
        }
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * @return 定时器
     */
    public ThreadPoolTaskScheduler buildScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        SchedulerConfigItem item = asyncItemService.getScheduler();
        scheduler.setPoolSize(item.getMaxPoolSize());
        String taskNamePrefix = item.getThreadNamePrefix();
        if (isNotBlank(taskNamePrefix))
        {
            scheduler.setThreadNamePrefix(taskNamePrefix.concat("-"));
        }
        scheduler.setAwaitTerminationSeconds(item.getAwaitTerminationSeconds());
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setRejectedExecutionHandler(new CallerRunsPolicy());
        scheduler.initialize();
        return scheduler;
    }

}
