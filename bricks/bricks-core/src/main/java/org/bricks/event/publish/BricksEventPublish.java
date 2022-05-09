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

import static java.util.Optional.ofNullable;
import static org.bricks.utils.ValidationUtils.validate;

import javax.annotation.Resource;

import org.bricks.event.BricksEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 发布事件
 * 
 * @author fuzy
 *
 */
@Slf4j
@Service
public class BricksEventPublish
{

    /**
     * 发布者
     */
    @Resource
    protected ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发布
     * 
     * @param <E> 事件
     * @param event 事件
     */
    public <E extends BricksEvent<?>> void publish(E event)
    {
        ofNullable(event).ifPresent(e ->
        {
            validate(e);
            applicationEventPublisher.publishEvent(e);
        });
    }

}
