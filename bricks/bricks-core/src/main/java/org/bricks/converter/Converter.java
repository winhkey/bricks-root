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

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapperConfig;

/**
 * 对象转换
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
 */
@MapperConfig
public interface Converter<M, N>
{

    /**
     * M转为N
     *
     * @param m M对象
     * @return N对象
     */
    N convert(M m);

    /**
     * M列表转为N列表
     *
     * @param mList M列表
     * @return N列表
     */
    @InheritConfiguration(name = "convert")
    List<N> convertList(List<M> mList);

    /**
     * M流转为N列表
     *
     * @param stream M流
     * @return N列表
     */
    List<N> convertStream(Stream<M> stream);

    /**
     * N转为M
     *
     * @param n N对象
     * @return M对象
     */
    @InheritInverseConfiguration(name = "convert")
    M reverseConvert(N n);

    /**
     * N列表转为M列表
     *
     * @param nList M列表
     * @return M列表
     */
    @InheritConfiguration(name = "reverseConvert")
    List<M> reverseConvertList(List<N> nList);

    /**
     * N流转为M列表
     *
     * @param stream N流
     * @return M列表
     */
    List<M> reverseConvertStream(Stream<N> stream);

}
