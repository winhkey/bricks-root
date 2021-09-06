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

package org.bricks.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 环绕抽象类
 *
 * @author fuzy
 *
 * @param <C> 线程变量类型
 */
public abstract class AbstractAroundHandler<C> implements AroundHandler
{

    /**
     * 线程变量
     */
    protected ThreadLocal<C> threadLocal = new ThreadLocal<>();

    @Override
    public Object around(ProceedingJoinPoint pjp) throws Throwable
    {
        before(pjp);
        Object result = pjp.proceed();
        after(pjp, result);
        return result;
    }

    /**
     * 前置
     *
     * @param pjp pjp
     */
    protected abstract void before(ProceedingJoinPoint pjp);

    /**
     * 后置
     *
     * @param pjp pjp
     * @param result 结果
     */
    protected abstract void after(ProceedingJoinPoint pjp, Object result);

}
