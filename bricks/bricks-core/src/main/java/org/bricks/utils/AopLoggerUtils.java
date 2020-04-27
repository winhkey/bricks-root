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
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.bricks.utils.ObjectUtils.buildString;
import static org.bricks.utils.ObjectUtils.getFieldValue;

import java.util.List;
import java.util.Map;

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
     * 日志缓存
     */
    private static final Map<Class<?>, Logger> LOGGER_MAP = newHashMap();

    /**
     * 切面打印日志，环绕
     *
     * @param pjp 切入点
     * @return 返回对象
     * @throws Throwable Throwable
     */
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = pjp.getTarget();
        Signature signature = pjp.getSignature();
        List<Object> argList = newArrayList();
        List<Object> returnList = newArrayList();
        StringBuilder s = new StringBuilder();
        String entryLog;
        Class<?> clazz = obj.getClass();
        Logger logger;
        if (LOGGER_MAP.containsKey(clazz)) {
            logger = LOGGER_MAP.get(clazz);
        } else {
            Object object = ofNullable(getFieldValue(obj, "log")).orElseGet(() -> getFieldValue(obj, "logger"));
            logger = (Logger) ofNullable(object).filter(o -> o instanceof Logger)
                    .orElse(null);
            LOGGER_MAP.put(clazz, logger);
        }
        boolean doLog = ofNullable(logger).filter(log -> obj.getClass().getAnnotation(NoLog.class) == null)
                .map(log -> ofNullable(((MethodSignature) signature).getMethod())
                        .filter(method -> method.getAnnotation(NoLog.class) == null).isPresent())
                .orElse(false);
        if (doLog) {
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
            logger.debug(entryLog, argList.toArray());
        }
        long start = System.currentTimeMillis();
        Object returnObject = pjp.proceed();
        long end = System.currentTimeMillis();
        if (doLog) {
            buildString(s, returnObject);
            returnList.add(s.toString());
            returnList.add(end - start);
            logger.debug(EXIT_LOG, returnList.toArray());
        }
        return returnObject;
    }

}
