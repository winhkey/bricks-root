package org.bricks.converter;

/**
 * 无参对象转换抽象类
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
 *
 */
public abstract class AbstractVoidConverter<M, N> extends AbstractConverter<M, N, Void> {

    @Override
    protected N from(M m, Void v) {
        return from(m);
    }

    /**
     * 转换
     *
     * @param m 源对象
     * @return 目标对象
     */
    protected abstract N from(M m);

}
