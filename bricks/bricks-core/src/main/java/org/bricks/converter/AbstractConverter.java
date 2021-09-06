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

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.bricks.annotation.NoLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象转换抽象类
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
 * @param <P> 参数
 */
public abstract class AbstractConverter<M, N, P> implements Converter<M, N, P>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 源类型
     */
    protected Class<M> source;

    /**
     * 目标类型
     */
    protected Class<N> target;

    /**
     * 参数类型
     */
    protected Class<P> param;

    /**
     * 构造方法
     */
    @SuppressWarnings(UNCHECKED)
    protected AbstractConverter()
    {
        List<Class<?>> classList = getComponentClassList(getClass(), Converter.class);
        source = (Class<M>) classList.get(0);
        target = (Class<N>) classList.get(1);
        param = (Class<P>) classList.get(2);
    }

    @NoLog
    @Override
    public Class<M> getSource()
    {
        return source;
    }

    @NoLog
    @Override
    public Class<N> getTarget()
    {
        return target;
    }

    @NoLog
    @Override
    public N convert(@NotNull M m, @NotNull P p)
    {
        return check(m, p) ? from(m, p) : null;
    }

    @NoLog
    @Override
    public List<N> convertList(Collection<M> mList)
    {
        return convertList(mList, null);
    }

    @NoLog
    @Override
    public List<N> convertList(Collection<M> mList, P p)
    {
        return ofNullable(mList).map(list -> list.stream()
                .filter(m -> check(m, p))
                .map(m -> convert(m, p))
                .filter(Objects::nonNull)
                .collect(toList()))
                .orElseGet(Collections::emptyList);
    }

    @NoLog
    @Override
    public M reverseConvert(N n, P p)
    {
        return reverseCheck(n, p) ? reverseFrom(n, p) : null;
    }

    @NoLog
    @Override
    public List<M> reverseConvertList(Collection<N> nList, P p)
    {
        return nList.stream()
                .filter(n -> reverseCheck(n, p))
                .map(n -> reverseFrom(n, p))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    /**
     * 转换条件
     *
     * @param m M对象
     * @param p 参数
     * @return 是否转换
     */
    protected boolean check(M m, P p)
    {
        return m != null && (p != null || param.equals(Void.class));
    }

    /**
     * 转换条件
     *
     * @param n N对象
     * @param p 参数
     * @return 是否转换
     */
    protected boolean reverseCheck(N n, P p)
    {
        return n != null && (p != null || param.equals(Void.class));
    }

    /**
     * M转为N
     *
     * @param m M对象
     * @param p 参数
     * @return N对象
     */
    protected abstract N from(M m, P p);

    /**
     * N转为n
     *
     * @param n n对象
     * @param p 参数
     * @return N对象
     */
    protected M reverseFrom(N n, P p)
    {
        return null;
    }

}
