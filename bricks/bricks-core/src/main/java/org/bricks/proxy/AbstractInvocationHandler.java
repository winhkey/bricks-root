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

import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.bricks.utils.ContextHolder.getEnvironment;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.core.env.Environment;

/**
 * 动态代理
 *
 * @author fuzy
 *
 */
public abstract class AbstractInvocationHandler implements InvocationHandler
{

    /**
     * 环境
     */
    protected static Environment environment;

    static
    {
        environment = getEnvironment();
    }

    /**
     * 方法注解
     *
     * @param method 方法
     * @return 注解列表
     */
    protected Annotation[] methodAnnotation(Method method)
    {
        return method.getDeclaredAnnotations();
    }

    /**
     * 参数注解
     *
     * @param method 方法
     * @return 注解map
     */
    protected Map<Integer, Annotation[]> argumentAnnotation(Method method)
    {
        Map<Integer, Annotation[]> map = newLinkedHashMap();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++)
        {
            map.put(i, annotations[i]);
        }
        return map;
    }

}
