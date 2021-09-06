package org.bricks.aop;

import org.aspectj.lang.JoinPoint;

/**
 * 后置
 *
 * @author fuzy
 *
 */
public interface AfterHandler
{

    /**
     * 后置
     *
     * @param joinPoint joinPoint
     */
    void after(JoinPoint joinPoint);

}
