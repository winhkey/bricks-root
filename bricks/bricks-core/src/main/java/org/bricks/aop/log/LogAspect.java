package org.bricks.aop.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.bricks.aop.AbstractAspect;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 *
 * @author fuzy
 *
 */
@Aspect
@Component
public class LogAspect extends AbstractAspect
{

    /**
     * 构造方法
     */
    public LogAspect()
    {
        super();
        aroundHandler = new LogAroundHandler();
    }

    /**
     * commons切入点
     */
    @Pointcut("execution(* org..*.*(..))")
    public void commonsLog()
    {
        // 方法用于注解
    }

    /**
     * 切面打印日志，环绕
     *
     * @param pjp 切入点
     * @return 返回对象
     * @throws Throwable Throwable
     */
    @Around("commonsLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable
    {
        return super.around(pjp);
    }

}
