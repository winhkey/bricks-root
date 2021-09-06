package org.bricks.aop.log;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newConcurrentMap;
import static com.google.common.collect.Maps.newHashMap;
import static java.text.MessageFormat.format;
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
import org.bricks.aop.AbstractAroundHandler;
import org.slf4j.Logger;

/**
 * 日志环绕
 *
 * @author fuzy
 *
 */
public class LogAroundHandler extends AbstractAroundHandler<Map<String, LogContext>>
{

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
    private static final Map<Class<?>, Logger> LOGGER_MAP = newConcurrentMap();

    @Override
    protected void before(ProceedingJoinPoint pjp)
    {
        Object obj = pjp.getTarget();
        Class<?> clazz = obj.getClass();
        Signature signature = pjp.getSignature();
        String methodName = signature.getName();
        Logger logger = LOGGER_MAP.computeIfAbsent(clazz, k ->
        {
            Object object = ofNullable(getFieldValue(obj, "log")).orElseGet(() -> getFieldValue(obj, "logger"));
            return (Logger) ofNullable(object).filter(o -> o instanceof Logger)
                    .orElse(null);
        });
        boolean doLog = ofNullable(logger).filter(log -> clazz.getAnnotation(NoLog.class) == null)
                .map(log -> ofNullable(((MethodSignature) signature).getMethod())
                        .filter(method -> method.getAnnotation(NoLog.class) == null)
                        .isPresent())
                .orElse(false);
        List<Object> argList = newArrayList();
        StringBuilder s = new StringBuilder();
        String entryLog;
        if (doLog)
        {
            argList.add(methodName);
            Object[] arguments = pjp.getArgs();
            if (isNotEmpty(arguments))
            {
                StringBuilder sb = new StringBuilder();
                for (Object arg : arguments)
                {
                    buildString(s, arg);
                    argList.add(s.toString());
                    s.delete(0, s.length());
                    sb.append("{}, ");
                }
                entryLog = ENTER_LOG_TEMP.replace(":arg", sb.substring(0, sb.length() - 2));
            }
            else
            {
                entryLog = ENTER_LOG_TEMP.replace(", args::arg", "");
            }
            logger.debug(entryLog, argList.toArray());
        }
        long start = System.currentTimeMillis();
        if (threadLocal.get() == null)
        {
            threadLocal.set(newHashMap());
        }
        threadLocal.get()
                .computeIfAbsent(format("{0}.{1}", clazz.getName(), methodName), k -> new LogContext().setDoLog(doLog)
                        .setStart(start)
                        .setLogger(logger));
    }

    @Override
    protected void after(ProceedingJoinPoint pjp, Object result)
    {
        long end = System.currentTimeMillis();
        Object obj = pjp.getTarget();
        Class<?> clazz = obj.getClass();
        Signature signature = pjp.getSignature();
        String methodName = signature.getName();
        String key = format("{0}.{1}", clazz.getName(), methodName);
        ofNullable(threadLocal.get()
                .get(key)).filter(LogContext::isDoLog)
                        .ifPresent(logContext ->
                        {
                            StringBuilder s = new StringBuilder();
                            List<Object> returnList = newArrayList();
                            returnList.add(methodName);
                            buildString(s, result);
                            returnList.add(s.toString());
                            returnList.add(end - logContext.getStart());
                            logContext.getLogger()
                                    .debug(EXIT_LOG, returnList.toArray());
                            threadLocal.get()
                                    .remove(key);
                        });
    }

}
