package org.bricks.function;

/**
 * 抛异常的BiFunction
 * 
 * @author fuzy
 * 
 * @param <T> 入参
 * @param <U> 入参
 * @param <E> 异常
 */
public interface ThrowableBiConsumer<T, U, E extends Throwable>
{

    /**
     * 消费
     *
     * @param t 参数
     * @param u 参数
     * @throws E 异常
     */
    void accept(T t, U u) throws E;

}
