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

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;

import org.bricks.annotation.NoLog;

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

    @Override
    @NoLog
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
        if (ctx.getParent() == null && ROOT.equals(name) || className.startsWith("Annotation") || className.equals("GenericWebApplicationContext"))
        {
            doInitFinished();
        }
    }

    /**
     * 监听后执行的方法
     */
    @Async("executor")
    protected abstract void doInitFinished();

}
