package org.bricks.aop;

import org.aspectj.lang.JoinPoint;

/**
 * 异常
 *
 * @author fuzy
 *
 */
public interface AfterThrowingHandler
{

    /**
     * 异常
     *
     * @param joinPoint joinPoint
     * @param t 异常
     */
    void afterThrowing(JoinPoint joinPoint, Throwable t);

}
