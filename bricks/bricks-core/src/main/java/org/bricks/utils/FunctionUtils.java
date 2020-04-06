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

import static java.util.Optional.ofNullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.bricks.function.ThrowableConsumer;
import org.bricks.function.ThrowableFunction;
import org.bricks.function.ThrowablePredicate;
import org.bricks.function.ThrowableSupplier;
import org.slf4j.Logger;

import lombok.experimental.UtilityClass;

/**
 * 函数式处理工具
 *
 * @author fuzy
 */
@UtilityClass
public class FunctionUtils {

    /**
     * 处理异常的consumer
     *
     * @param throwableConsumer 抛异常的consumer
     * @param catchConsumer     出现异常后执行的consumer
     * @param logger            日志对象
     * @param exceptionFunction 异常转换
     * @param <T>               入参
     * @return consumer
     */
    public static <T> Consumer<T> accept(ThrowableConsumer<T, Throwable> throwableConsumer, Consumer<T> catchConsumer,
                                         Logger logger, Function<Throwable, RuntimeException> exceptionFunction) {
        return t -> {
            try {
                throwableConsumer.accept(t);
            } catch (Throwable e) {
                ofNullable(catchConsumer).ifPresent(c -> c.accept(t));
                handle(e, logger, exceptionFunction);
            }
        };
    }

    /**
     * 处理异常的Supplier
     *
     * @param throwableSupplier 抛异常的Supplier
     * @param logger            日志对象
     * @param exceptionFunction 异常转换
     * @param <T>               入参
     * @return Supplier
     */
    public static <T> Supplier<T> get(ThrowableSupplier<T, Throwable> throwableSupplier, Logger logger,
                                      Function<Throwable, RuntimeException> exceptionFunction) {
        return () -> {
            T t = null;
            try {
                t = throwableSupplier.get();
            } catch (Throwable e) {
                handle(e, logger, exceptionFunction);
            }
            return t;
        };
    }

    /**
     * 处理异常的Predicate
     *
     * @param throwablePredicate 抛异常的Predicate
     * @param logger             日志对象
     * @param exceptionFunction  异常转换
     * @param <T>                入参
     * @return Predicate
     */
    public static <T> Predicate<T> test(ThrowablePredicate<T, Throwable> throwablePredicate, Logger logger,
                                        Function<Throwable, RuntimeException> exceptionFunction) {
        return t -> {
            boolean result = false;
            try {
                result = throwablePredicate.test(t);
            } catch (Throwable e) {
                handle(e, logger, exceptionFunction);
            }
            return result;
        };
    }

    /**
     * 处理异常的Function
     *
     * @param throwableFunction 抛异常的Function
     * @param catchFunction     出现异常后执行的function
     * @param logger            日志对象
     * @param exceptionFunction 异常转换
     * @param <T>               入参
     * @param <R>               结果
     * @return Function
     */
    public static <T, R> Function<T, R> apply(ThrowableFunction<T, R, Throwable> throwableFunction,
                                              Function<T, R> catchFunction, Logger logger, Function<Throwable, RuntimeException> exceptionFunction) {
        return t -> {
            R r;
            try {
                r = throwableFunction.apply(t);
            } catch (Throwable e) {
                r = ofNullable(catchFunction).map(f -> f.apply(t)).orElse(null);
                handle(e, logger, exceptionFunction);
            }
            return r;
        };
    }

    private static void handle(Throwable e, Logger logger, Function<Throwable, RuntimeException> function) {
        ofNullable(logger).ifPresent(log -> log.error(e.getMessage(), e));
        if (function != null) {
            throw function.apply(e);
        }
    }

}
