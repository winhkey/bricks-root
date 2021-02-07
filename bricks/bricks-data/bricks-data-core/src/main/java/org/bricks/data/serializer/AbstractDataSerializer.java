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

package org.bricks.data.serializer;

import static org.bricks.utils.ReflectionUtils.getComponentClassList;

import java.util.List;

import javax.annotation.PostConstruct;

/**
 * 数据序列化
 *
 * @author fuzy
 *
 * @param <D> 数据类
 */
public class AbstractDataSerializer<D> implements DataSerializer<D>
{

    /**
     * 数据类型
     */
    private Class<D> clazz;

    /**
     * 初始化获取参数类型
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init()
    {
        List<Class<?>> classList = getComponentClassList(getClass(), DataSerializer.class);
        clazz = (Class<D>) classList.get(0);
    }

    @Override
    public Class<D> getClazz()
    {
        return clazz;
    }

}
