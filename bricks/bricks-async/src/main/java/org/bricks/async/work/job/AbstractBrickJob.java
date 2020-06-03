/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
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

import static org.bricks.utils.ReflectionUtils.getComponentClassList;

import java.util.List;

import javax.annotation.Resource;

import org.bricks.async.work.service.BrickJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 定时任务抽象类
 *
 * @author fuzy
 *
 * @param <R> 任务接口
 */
public abstract class AbstractBrickJob<R extends BrickJobService> implements BrickJob<R> {

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
     * 任务线程
     */
    private final Class<R> jobServiceClass;

    /**
     * 构造方法
     */
    @SuppressWarnings("unchecked")
    protected AbstractBrickJob() {
        List<Class<?>> classList = getComponentClassList(getClass(), BrickJob.class);
        jobServiceClass = (Class<R>) classList.get(0);
    }

    @Override
    public void execute() {
        execute(applicationContext);
    }

    /**
     * 执行任务
     *
     * @param applicationContext spring环境
     */
    protected void execute(ApplicationContext applicationContext) {
        try {
            applicationContext.getBean(jobServiceClass)
                    .execute();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

}
