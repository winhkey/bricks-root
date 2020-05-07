package org.bricks.converter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * 对象转换
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
 * @param <P> 参数
 */
public interface Converter<M, N, P> extends Function<M, N> {

    /**
     * M转为N
     *
     * @param m M对象
     * @param p 参数
     * @return N对象
     */
    N convert(M m, P p);

    /**
     * 转换
     *
     * @param m 源对象
     * @return 目标对象
     */
    default N convert(M m) {
        return convert(m, null);
    }

    /**
     * M列表转为N列表
     *
     * @param mList M列表
     * @param p 参数
     * @return N列表
     */
    List<N> convertList(Collection<M> mList, P p);

    /**
     * 列表转换
     *
     * @param mList m列表
     * @return n列表
     */
    default List<N> convertList(Collection<M> mList) {
        return convertList(mList, null);
    }

    /**
     * N转为M
     *
     * @param n N对象
     * @param p 参数
     * @return M对象
     */
    M reverseConvert(N n, P p);

    /**
     * 反转
     *
     * @param n n对象
     * @return m对象
     */
    default M reverseConvert(N n) {
        return reverseConvert(n, null);
    }

    /**
     * N列表转为M列表
     *
     * @param nList M列表
     * @param p 参数
     * @return M列表
     */
    List<M> reverseConvertList(Collection<N> nList, P p);

    /**
     * 列表反转
     *
     * @param nList n列表
     * @return m列表
     */
    default List<M> reverseConvertList(Collection<N> nList) {
        return reverseConvertList(nList, null);
    }

    /**
     * M类型
     *
     * @return M类型
     */
    Class<M> getSource();

    /**
     * N类型
     *
     * @return N类型
     */
    Class<N> getTarget();

}
