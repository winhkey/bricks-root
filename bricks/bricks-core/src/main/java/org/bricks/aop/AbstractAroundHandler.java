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
