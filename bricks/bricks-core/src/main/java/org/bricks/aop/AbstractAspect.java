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
