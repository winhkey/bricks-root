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

package org.bricks.converter;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bricks.annotation.NoLog;

/**
 * 对象转换
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
 * @param <P> 参数
 */
public interface Converter<M, N, P>
{

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
    @NoLog
    default N convert(M m)
    {
        return convert(m, null);
    }

    /**
     * 转换
     *
     * @param m 源对象
     * @param function 转换lambda
     * @param <M> 源类型
     * @param <N> 目标类型
     * @return 目标对象
     */
    static <M, N> N convertFunction(M m, Function<M, N> function)
    {
        return function.apply(m);
    }

    /**
     * 转换
     *
     * @param m 源对象
     * @param p 参数
     * @param function 转换lambda
     * @param <M> 源类型
     * @param <N> 目标类型
     * @param <P> 参数类型
     * @return 目标对象
     */
    static <M, N, P> N convertFunction(M m, P p, BiFunction<M, P, N> function)
    {
        return function.apply(m, p);
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
    default List<N> convertList(Collection<M> mList)
    {
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
    default M reverseConvert(N n)
    {
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
    default List<M> reverseConvertList(Collection<N> nList)
    {
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
