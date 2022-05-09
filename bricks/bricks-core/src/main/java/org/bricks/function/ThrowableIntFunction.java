package org.bricks.function;

/**
 * 抛异常的IntFunction
 *
 * @author fuzy
 *
 * @param <R> 返回
 * @param <E> 异常
 */
@FunctionalInterface
public interface ThrowableIntFunction<R, E extends Throwable> extends ThrowableFunction<Integer, R, E>
{

}

