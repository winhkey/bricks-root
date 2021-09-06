package org.bricks.aop;

import org.aspectj.lang.JoinPoint;

/**
 * 返回
 *
 * @author fuzy
 *
 */
public interface AfterReturningHandler
{

    /**
     * 返回
     *
     * @param joinPoint joinPoint
     * @param result 结果
     */
    void afterReturning(JoinPoint joinPoint, Object result);

}
