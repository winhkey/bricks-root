package org.bricks.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 环绕
 *
 * @author fuzy
 *
 */
public interface AroundHandler
{

    /**
     * 环绕
     *
     * @param pjp pjp
     * @return 结果
     * @throws Throwable 异常
     */
    Object around(ProceedingJoinPoint pjp) throws Throwable;

}
