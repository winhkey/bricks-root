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

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 切面抽象类
 *
 * @author fuzy
 *
 */
public abstract class AbstractAspect
{

    /**
     * 前置
     */
    protected BeforeHandler beforeHandler;

    /**
     * 后置
     */
    protected AfterHandler afterHandler;

    /**
     * 环绕
     */
    protected AroundHandler aroundHandler;

    /**
     * 返回
     */
    protected AfterReturningHandler afterReturningHandler;

    /**
     * 异常
     */
    protected AfterThrowingHandler afterThrowingHandler;

    /**
     * 前置
     *
     * @param joinPoint joinPoint
     */
    public void before(JoinPoint joinPoint)
    {
        beforeHandler.before(joinPoint);
    }

    /**
     * 环绕
     *
     * @param pjp pjp
     * @return 结果
     * @throws Throwable 异常
     */
    public Object around(ProceedingJoinPoint pjp) throws Throwable
    {
        return aroundHandler.around(pjp);
    }

    /**
     * 返回
     *
     * @param joinPoint joinPoint
     * @param result 结果
     */
    public void afterReturning(JoinPoint joinPoint, Object result)
    {
        afterReturningHandler.afterReturning(joinPoint, result);
    }

    /**
     * 返回
     *
     * @param joinPoint joinPoint
     */
    public void afterReturning(JoinPoint joinPoint)
    {
        afterReturningHandler.afterReturning(joinPoint, null);
    }

    /**
     * 抛异常
     *
     * @param joinPoint joinPoint
     * @param t 异常
     */
    public void afterThrowing(JoinPoint joinPoint, Throwable t)
    {
        afterThrowingHandler.afterThrowing(joinPoint, t);
    }

    /**
     * 后置
     *
     * @param joinPoint joinPoint
     */
    public void after(JoinPoint joinPoint)
    {
        afterHandler.after(joinPoint);
    }

}
