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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import lombok.Getter;

/**
 * 对象转换抽象类
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
 * @param <P> 参数对象
 */
public abstract class AbstractParameterConverter<M, N, P> extends AbstractConverter<M, N>
        implements ParameterConverter<M, N, P>
{

    /**
     * 参数类型
     */
    @Getter
    protected Class<P> parameterClass;

    /**
     * 构造方法
     */
    @SuppressWarnings(UNCHECKED)
    protected AbstractParameterConverter()
    {
        super();
        List<Class<?>> classList = getComponentClassList(getClass(), ParameterConverter.class);
        parameterClass = (Class<P>) classList.get(2);
    }

    @Override
    public N convert(@NotNull M m, @NotNull P p)
    {
        return check(m, p) ? from(m, p) : null;
    }

    @Override
    public List<N> convertList(List<M> mList, P p)
    {
        return ofNullable(mList).map(list -> list.stream()
                .filter(m -> check(m, p))
                .map(m -> convert(m, p))
                .filter(Objects::nonNull)
                .collect(toList()))
                .orElseGet(Collections::emptyList);
    }

    @Override
    public M reverseConvert(N n, P p)
    {
        return reverseCheck(n, p) ? reverseFrom(n, p) : null;
    }

    @Override
    public List<M> reverseConvertList(List<N> nList, P p)
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
        return m != null && (p != null || parameterClass.equals(Void.class));
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
        return n != null && (p != null || parameterClass.equals(Void.class));
    }

    @Override
    protected N from(M m)
    {
        return from(m, null);
    }

    /**
     * M转为N
     *
     * @param m M对象
     * @param p 参数
     * @return N对象
     */
    protected abstract N from(M m, P p);

    @Override
    protected M reverseFrom(N n)
    {
        return reverseFrom(n, null);
    }

    /**
     * N转为n
     *
     * @param n n对象
     * @param p 参数
     * @return N对象
     */
    protected abstract M reverseFrom(N n, P p);

}
