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

package org.bricks.listener;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import javax.annotation.Resource;

import org.bricks.annotation.NoLog;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * spring初始化完成时监听
 *
 * @author fuzy
 *
 */
public abstract class AbstractInitFinishedListener implements ApplicationListener<ContextRefreshedEvent>
{

    /**
     * Root WebApplicationContext
     */
    private static final String ROOT = "Root WebApplicationContext";

    /**
     * 缓存
     */
    private static final Map<String, String> MAP = newHashMap();

    /**
     * spring环境
     */
    @Resource
    protected Environment environment;

    /**
     * 异步
     */
    @Resource
    protected ThreadPoolTaskExecutor executor;

    /**
     * 任务池
     */
    @Resource
    protected ThreadPoolTaskScheduler scheduler;

    @NoLog
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        ApplicationContext ctx = event.getApplicationContext();
        String name = ctx.getDisplayName();
        if (name.startsWith("FeignContext") || name.startsWith("SpringClientFactory"))
        {
            return;
        }
        String className = ctx.getClass()
                .getSimpleName();
        String beanName = getClass().getSimpleName();
        if (!MAP.containsKey(beanName) && (ctx.getParent() == null && ROOT.equals(name)
                || className.startsWith("Annotation") || className.equals("GenericWebApplicationContext")))
        {
            MAP.put(beanName, "");
            doSyncInitFinished();
            executor.execute(this::doAsyncInitFinished);
        }
    }

    /**
     * 监听后执行的方法
     */
    protected void doSyncInitFinished()
    {
        //
    }

    /**
     * 监听后执行的方法
     */
    protected void doAsyncInitFinished()
    {
        //
    }

}
