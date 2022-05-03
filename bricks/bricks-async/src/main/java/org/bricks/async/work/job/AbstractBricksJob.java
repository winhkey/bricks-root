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

package org.bricks.async.work.job;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;

import java.util.List;

import javax.annotation.Resource;

import org.bricks.async.work.service.BricksJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 定时任务抽象类
 *
 * @author fuzy
 *
 * @param <R> 任务接口
 */
public abstract class AbstractBricksJob<R extends BricksJobService> implements BricksJob<R>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * applicationContext
     */
    @Resource
    protected ApplicationContext applicationContext;

    /**
     * environment
     */
    @Resource
    protected Environment environment;

    /**
     * 任务线程
     */
    private final Class<R> jobServiceClass;

    /**
     * 构造方法
     */
    @SuppressWarnings("unchecked")
    protected AbstractBricksJob()
    {
        List<Class<?>> classList = getComponentClassList(getClass(), BricksJob.class);
        jobServiceClass = (Class<R>) classList.get(0);
    }

    @Override
    public void execute()
    {
        execute(applicationContext);
    }

    /**
     * 执行任务
     *
     * @param applicationContext spring环境
     */
    protected void execute(ApplicationContext applicationContext)
    {
        if (!isEnabled())
        {
            log.warn("job off");
            return;
        }
        try
        {
            applicationContext.getBean(jobServiceClass)
                    .execute();
        }
        catch (Throwable e)
        {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @return 定时任务有效
     */
    protected boolean isEnabled()
    {
        return isBlank(enableKey()) || environment.getProperty(enableKey(), Boolean.class, true);
    }

    /**
     * @return 开关配置名
     */
    protected String enableKey()
    {
        return null;
    }

}
