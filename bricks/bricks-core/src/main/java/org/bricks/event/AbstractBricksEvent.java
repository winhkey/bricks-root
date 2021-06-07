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

package org.bricks.event;

import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 事件抽象类
 * 
 * @author fuzy
 *
 * @param <S> 事件源
 */
@Getter
@Setter
public class AbstractBricksEvent<S> implements BricksEvent<S>
{

    private static final long serialVersionUID = 1L;

    /**
     * 事件源
     */
    private S source;

    private Class<S> sourceClass;

    @SuppressWarnings(UNCHECKED)
    public AbstractBricksEvent(S source)
    {
        this.source = source;
        List<Class<?>> classList = getComponentClassList(getClass(), BricksEvent.class);
        sourceClass = (Class<S>) classList.get(0);
    }

}
