package org.bricks.aop;

import org.aspectj.lang.JoinPoint;

/**
 * 前置
 *
 * @author fuzy
 *
 */
public interface BeforeHandler
{

    /**
     * 前置
     *
     * @param joinPoint joinPoint
     */
    void before(JoinPoint joinPoint);

}
