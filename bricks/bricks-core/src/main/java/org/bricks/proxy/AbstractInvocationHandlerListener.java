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

package org.bricks.proxy;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;

import org.bricks.listener.AbstractInitFinishedListener;

/**
 * 处理动态代理监听器
 *
 * @author fuzy
 *
 */
public abstract class AbstractInvocationHandlerListener extends AbstractInitFinishedListener
{

    @Override
    protected void doAsyncInitFinished()
    {
        List<Class<?>> list = InvocationHandlerUtils.getClass(getKey());
        if (isNotEmpty(list))
        {
            list.forEach(this::process);
        }
    }

    /**
     * @return 动态代理注解名
     */
    protected abstract String getKey();

    /**
     * 处理动态代理
     *
     * @param clazz 动态代理
     */
    protected abstract void process(Class<?> clazz);

}
