/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks-root)
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

import static java.util.Optional.ofNullable;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.bricks.function.ThrowableBiFunction;
import org.bricks.function.ThrowableConsumer;
import org.bricks.function.ThrowableFunction;
import org.bricks.function.ThrowablePredicate;
import org.bricks.function.ThrowableRunnable;
import org.bricks.function.ThrowableSupplier;
import org.slf4j.Logger;

import lombok.experimental.UtilityClass;

/**
 * 函数式处理工具
 *
 * @author fuzy
 *
 */
@UtilityClass
public class FunctionUtils
{

    /**
     * 处理对象
     *
     * @param object 对象
     * @param consumerMap 根据对象类型区分处理方法
     */
    public static void accept(Object object, Map<Class<?>, Consumer<Object>> consumerMap)
    {
        if (isNotEmpty(consumerMap))
        {
            if (object == null)
            {
                ofNullable(consumerMap.get(Void.class)).ifPresent(consumer -> consumer.accept(null));
            }
            else if (object.getClass()
                    .isArray())
            {
                ofNullable(consumerMap.get(Object[].class)).ifPresent(consumer -> consumer.accept(object));
            }
            else
            {
                consumerMap.entrySet()
                        .stream()
                        .filter(entry -> entry.getKey()
                                .isInstance(object))
                        .findFirst()
                        .ifPresent(entry -> entry.getValue()
                                .accept(object));
            }
        }
    }

    /**
     * 处理异常的consumer
     *
     * @param throwableConsumer 抛异常的consumer
     * @param catchConsumer 出现异常后执行的consumer
     * @param finallyRunnable finally执行
     * @param log 日志对象
     * @param exceptionFunction 异常转换
     * @param <T> 入参
     * @return consumer
     */
    public static <T> Consumer<T> accept(ThrowableConsumer<T, Throwable> throwableConsumer, Consumer<T> catchConsumer,
            Runnable finallyRunnable, Logger log, Function<Throwable, RuntimeException> exceptionFunction)
    {
        return t ->
        {
            try
            {
                throwableConsumer.accept(t);
            }
            catch (Throwable e)
            {
                ofNullable(catchConsumer).ifPresent(c -> c.accept(t));
                handle(e, log, exceptionFunction);
            }
            finally
            {
                handle(finallyRunnable);
            }
        };
    }

    /**
     * 处理异常的Supplier
     *
     * @param throwableSupplier 抛异常的Supplier
     * @param finallyRunnable finally执行
     * @param log 日志对象
     * @param exceptionFunction 异常转换
     * @param <T> 入参
     * @return Supplier
     */
    public static <T> Supplier<T> get(ThrowableSupplier<T, Throwable> throwableSupplier, Runnable finallyRunnable,
            Logger log, Function<Throwable, RuntimeException> exceptionFunction)
    {
        return () ->
        {
            T t = null;
            try
            {
                t = throwableSupplier.get();
            }
            catch (Throwable e)
            {
                handle(e, log, exceptionFunction);
            }
            finally
            {
                handle(finallyRunnable);
            }
            return t;
        };
    }

    /**
     * 处理异常的Predicate
     *
     * @param throwablePredicate 抛异常的Predicate
     * @param finallyRunnable finally执行
     * @param log 日志对象
     * @param exceptionFunction 异常转换
     * @param <T> 入参
     * @return Predicate
     */
    public static <T> Predicate<T> test(ThrowablePredicate<T, Throwable> throwablePredicate, Runnable finallyRunnable,
            Logger log, Function<Throwable, RuntimeException> exceptionFunction)
    {
        return t ->
        {
            boolean result = false;
            try
            {
                result = throwablePredicate.test(t);
            }
            catch (Throwable e)
            {
                handle(e, log, exceptionFunction);
            }
            finally
            {
                handle(finallyRunnable);
            }
            return result;
        };
    }

    /**
     * 处理异常的Function
     *
     * @param throwableFunction 抛异常的Function
     * @param catchFunction 出现异常后执行的function
     * @param finallyRunnable finally执行
     * @param log 日志对象
     * @param exceptionFunction 异常转换
     * @param <T> 入参
     * @param <R> 结果
     * @return Function
     */
    public static <T, R> Function<T, R> apply(ThrowableFunction<T, R, Throwable> throwableFunction,
            BiFunction<T, Throwable, R> catchFunction, Runnable finallyRunnable, Logger log,
            Function<Throwable, RuntimeException> exceptionFunction)
    {
        return t ->
        {
            R r;
            try
            {
                r = throwableFunction.apply(t);
            }
            catch (Throwable e)
            {
                r = ofNullable(catchFunction).map(f -> f.apply(t, e))
                        .orElse(null);
                handle(e, log, exceptionFunction);
            }
            finally
            {
                handle(finallyRunnable);
            }
            return r;
        };
    }

    /**
     * 处理异常的BiFunction
     *
     * @param throwableFunction 抛异常的Function
     * @param catchFunction 捕获异常后执行的function
     * @param finallyRunnable finally执行的Runnable
     * @param log 日志对象
     * @param exceptionFunction 异常转换
     * @param <T> 入参
     * @param <U> 入参
     * @param <R> 结果
     * @return BiFunction
     */
    public static <T, U, R> BiFunction<T, U, R> apply(ThrowableBiFunction<T, U, R, Throwable> throwableFunction,
            BiFunction<T, U, R> catchFunction, Runnable finallyRunnable, Logger log,
            Function<Throwable, RuntimeException> exceptionFunction)
    {
        return (t, u) ->
        {
            R r;
            try
            {
                r = throwableFunction.apply(t, u);
            }
            catch (Throwable e)
            {
                r = ofNullable(catchFunction).map(f -> f.apply(t, u))
                        .orElse(null);
                handle(e, log, exceptionFunction);
            }
            finally
            {
                handle(finallyRunnable);
            }
            return r;
        };
    }

    /**
     * 处理异常的Runnable
     *
     * @param throwableRunnable 抛异常的Runnable
     * @param catchRunnable 捕获异常执行的Runnable
     * @param finallyRunnable finally执行的Runnable
     * @param log 日志对象
     */
    public static void run(ThrowableRunnable<Throwable> throwableRunnable, Runnable catchRunnable,
            Runnable finallyRunnable, Logger log)
    {
        try
        {
            throwableRunnable.run();
        }
        catch (Throwable e)
        {
            handle(catchRunnable);
            handle(e, log, null);
        }
        finally
        {
            handle(finallyRunnable);
        }
    }

    private static void handle(Throwable e, Logger log, Function<Throwable, RuntimeException> function)
    {
        ofNullable(log).ifPresent(l -> l.error(e.getMessage(), e));
        if (function != null)
        {
            throw function.apply(e);
        }
    }

    private static void handle(Runnable runnable)
    {
        ofNullable(runnable).ifPresent(Runnable::run);
    }

}
