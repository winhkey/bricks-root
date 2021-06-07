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

package org.bricks.event.publish;

import javax.annotation.Resource;

import org.bricks.event.BricksEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 发布事件
 * 
 * @author fuzy
 *
 * @param <E> 事件源
 */
public abstract class AbstractBricksEventPublish<E extends BricksEvent<?>> implements BricksEventPublish<E>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 发布者
     */
    @Resource
    protected ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(E event)
    {
        applicationEventPublisher.publishEvent(event);
    }

}
