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

package org.bricks.async.work.task;

import static com.google.common.collect.Maps.newConcurrentMap;
import static java.text.MessageFormat.format;

import java.util.Map;

import javax.annotation.Resource;

import org.bricks.data.json.JsonDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务抽象类
 *
 * @author fuzy
 *
 * @param <T> 任务参数
 */
public abstract class AbstractBrickTask<T> implements BrickTask<T>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 运行标记
     */
    protected static final Map<String, String> RUNNING_MAP = newConcurrentMap();

    /**
     * jsonDataService
     */
    @Resource(name = "jacksonJsonService")
    protected JsonDataService jsonDataService;

    @Override
    public void task(T target)
    {
        String taskClass = getClass().getSimpleName();
        String key = format("{0}_{1}", taskClass, jsonDataService.output(target));
        String running = RUNNING_MAP.get(key);
        if (running != null)
        {
            log.debug("Task {} is still running, wait for the next time.", key);
            return;
        }
        RUNNING_MAP.put(key, "");
        try
        {
            doTask(target);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        RUNNING_MAP.remove(key);
    }

    /**
     * 查询条件
     *
     * @param condition 查询条件
     * @param paramMap 任务参数
     */
    protected void buildCondition(Map<String, Object> condition, Map<String, Object> paramMap)
    {

    }

    /**
     * 执行任务
     *
     * @param target 任务参数
     */
    protected abstract void doTask(T target);

}
