/*
  Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.bricks.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 *
 * @author fuzy
 */
@Component
@Aspect
public class CommonLogAspect {

    /**
     * service切入点
     */
    @Pointcut("execution(* org.bricks..service..*.*(..))")
    public void serviceLog() {
    }

    /**
     * commons切入点
     */
    @Pointcut("execution(* org.bricks..*.*(..))")
    public void commonsLog() {
    }

    /**
     * 切面打印日志，环绕
     *
     * @param pjp 切入点
     * @return 返回对象
     * @throws Throwable Throwable
     */
    @Around("serviceLog() || commonsLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return AopLogger.around(pjp);
    }

}
