/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
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

package org.bricks.utils;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.bricks.utils.EntityUtils.buildString;
import static org.bricks.utils.EntityUtils.getFieldValue;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.bricks.annotation.NoLog;
import org.slf4j.Logger;

import lombok.experimental.UtilityClass;

/**
 * 切面日志
 *
 * @author fuzy
 */
@UtilityClass
public class AopLoggerUtils {

    /**
     * 入口日志
     */
    private static final String ENTER_LOG_TEMP = "Enter {}(), args::arg";

    /**
     * 出口日志
     */
    private static final String EXIT_LOG = "Exit {}(), return:{}, cost:{}ms";

    /**
     * 切面打印日志，环绕
     *
     * @param pjp 切入点
     * @return 返回对象
     * @throws Throwable Throwable
     */
    public static Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = pjp.getTarget();
        List<Object> argList = newArrayList();
        List<Object> returnList = newArrayList();
        StringBuilder s = new StringBuilder();
        String entryLog = null;
        boolean doLog = false;
        Logger log = getFieldValue(obj, "log");
        if (log != null) {
            Signature signature = pjp.getSignature();
            String methodName = signature.getName();
            argList.add(methodName);
            returnList.add(methodName);
            Object[] arguments = pjp.getArgs();
            if (isNotEmpty(arguments)) {
                StringBuilder sb = new StringBuilder();
                for (Object arg : arguments) {
                    buildString(s, arg);
                    argList.add(s.toString());
                    s.delete(0, s.length());
                    sb.append("{}, ");
                }
                entryLog = ENTER_LOG_TEMP.replace(":arg", sb.substring(0, sb.length() - 2));
            } else {
                entryLog = ENTER_LOG_TEMP.replace(", args::arg", "");
            }
            doLog = ofNullable(((MethodSignature) signature).getMethod())
                    .filter(method -> method.getAnnotation(NoLog.class) == null)
                    .isPresent();
        }
        if (doLog) {
            log.debug(entryLog, argList.toArray());
        }
        long start = System.currentTimeMillis();
        Object returnObject = pjp.proceed();
        long end = System.currentTimeMillis();
        if (doLog) {
            buildString(s, returnObject);
            returnList.add(s.toString());
            returnList.add(end - start);
            log.debug(EXIT_LOG, returnList.toArray());
        }
        return returnObject;
    }

}
