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

package org.bricks.async.config;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.bricks.async.bean.ExecutorConfigItem;
import org.bricks.async.bean.SchedulerConfigItem;
import org.bricks.async.service.AsyncFactory;
import org.bricks.async.service.AsyncItemService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import lombok.extern.slf4j.Slf4j;

/**
 * 异步配置
 *
 * @author fuzy
 *
 */
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig extends AsyncConfigurerSupport implements SchedulingConfigurer
{

    /**
     * 异步配置加载
     */
    @Resource
    private AsyncFactory asyncFactory;

    /**
     * @return 线程池
     */
    @Primary
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor executor()
    {
        ThreadPoolTaskExecutor executor = asyncFactory.buildExecutor();
        log.debug("create executor {}", executor.getCorePoolSize());
        return executor;
    }

    /**
     * @return 线程池
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor asyncExecutor()
    {
        ThreadPoolTaskExecutor executor = asyncFactory.buildExecutor();
        log.debug("create asyncExecutor {}", executor.getCorePoolSize());
        return executor;
    }

    /**
     * @return 任务池
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler scheduler()
    {
        return asyncFactory.buildScheduler();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        taskRegistrar.setScheduler(scheduler());
    }

}
