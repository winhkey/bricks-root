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

package org.bricks.async.bean;


import org.bricks.bean.AbstractBean;

import lombok.Getter;

/**
 * 并发配置
 *
 * @author fuzy
 * 
 * @param <T> 自对象
 */
@Getter
public abstract class AbstractAsyncConfigItem<T extends AbstractAsyncConfigItem<T>> extends AbstractBean
{

    /**
     * 线程池最大容量
     */
    private int maxPoolSize = 10;

    /**
     * 线程名前缀
     */
    private String threadNamePrefix;

    /**
     * 设置最大容量
     * 
     * @param maxPoolSize 最大容量
     * @return 对象
     */
    public T setMaxPoolSize(int maxPoolSize)
    {
        this.maxPoolSize = maxPoolSize;
        return self();
    }

    /**
     * 设置线程名前缀
     * 
     * @param threadNamePrefix 前缀
     * @return 对象
     */
    public T setThreadNamePrefix(String threadNamePrefix)
    {
        this.threadNamePrefix = threadNamePrefix;
        return self();
    }

}
