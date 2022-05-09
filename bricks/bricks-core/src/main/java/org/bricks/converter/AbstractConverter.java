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
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.bricks.annotation.NoLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * 对象转换抽象类
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
 */
@NoLog
public abstract class AbstractConverter<M, N> implements Converter<M, N>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 源类型
     */
    @Getter
    protected Class<M> sourceClass;

    /**
     * 目标类型
     */
    @Getter
    protected Class<N> targetClass;

    /**
     * 构造方法
     */
    @SuppressWarnings(UNCHECKED)
    protected AbstractConverter()
    {
        List<Class<?>> classList = getComponentClassList(getClass(), Converter.class);
        sourceClass = (Class<M>) classList.get(0);
        targetClass = (Class<N>) classList.get(1);
    }

    @Override
    public N convert(@NotNull M m)
    {
        return check(m) ? from(m) : null;
    }

    @Override
    public List<N> convertList(List<M> mList)
    {
        return ofNullable(mList).map(list -> convertStream(list.stream()))
                .orElseGet(Collections::emptyList);
    }

    @Override
    public List<N> convertStream(Stream<M> stream)
    {
        return ofNullable(stream).map(s -> s.filter(this::check)
                .map(this::convert)
                .filter(Objects::nonNull)
                .collect(toList()))
                .orElseGet(Collections::emptyList);
    }

    @Override
    public M reverseConvert(N n)
    {
        return reverseCheck(n) ? reverseFrom(n) : null;
    }

    @Override
    public List<M> reverseConvertList(List<N> nList)
    {
        return ofNullable(nList).map(list -> reverseConvertStream(list.stream()))
                .orElseGet(Collections::emptyList);
    }

    @Override
    public List<M> reverseConvertStream(Stream<N> stream)
    {
        return ofNullable(stream).map(s -> s.filter(this::reverseCheck)
                .map(this::reverseConvert)
                .filter(Objects::nonNull)
                .collect(toList()))
                .orElseGet(Collections::emptyList);
    }

    /**
     * 转换条件
     *
     * @param m M对象
     * @return 是否转换
     */
    protected boolean check(M m)
    {
        return m != null;
    }

    /**
     * 转换条件
     *
     * @param n N对象
     * @return 是否转换
     */
    protected boolean reverseCheck(N n)
    {
        return n != null;
    }

    /**
     * M转为N
     *
     * @param m M对象
     * @return N对象
     */
    protected abstract N from(M m);

    /**
     * N转为n
     *
     * @param n n对象
     * @return N对象
     */
    protected abstract M reverseFrom(N n);

}
