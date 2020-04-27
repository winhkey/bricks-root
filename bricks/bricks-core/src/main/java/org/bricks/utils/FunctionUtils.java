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
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

import java.util.Map;
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
     * 处理对象
     *
     * @param object 对象
     * @param consumerMap 根据对象类型区分处理方法
     */
    public void accept(Object object, Map<Class<?>, Consumer<Object>> consumerMap)
    {
        if (isNotEmpty(consumerMap)) {
            if (object == null) {
                ofNullable(consumerMap.get(Void.class)).ifPresent(consumer -> consumer.accept(null));
            } else if (object.getClass().isArray()) {
                ofNullable(consumerMap.get(Object[].class)).ifPresent(consumer -> consumer.accept(null));
            } else {
                consumerMap.entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().isInstance(object))
                        .findFirst()
                        .ifPresent(entry -> entry.getValue().accept(object));
            }
        }
    }

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
    public <T> Consumer<T> accept(ThrowableConsumer<T, Throwable> throwableConsumer, Consumer<T> catchConsumer,
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
    public <T> Supplier<T> get(ThrowableSupplier<T, Throwable> throwableSupplier, Logger logger,
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
    public <T> Predicate<T> test(ThrowablePredicate<T, Throwable> throwablePredicate, Logger logger,
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
    public <T, R> Function<T, R> apply(ThrowableFunction<T, R, Throwable> throwableFunction,
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

    private void handle(Throwable e, Logger logger, Function<Throwable, RuntimeException> function) {
        ofNullable(logger).ifPresent(log -> log.error(e.getMessage(), e));
        if (function != null) {
            throw function.apply(e);
        }
    }

}
