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

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;

/**
 * 对象转换
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
 * @param <P> 参数
 */
public interface ParameterConverter<M, N, P> extends Converter<M, N>
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
     * M列表转为N列表
     *
     * @param mList M列表
     * @param p 参数
     * @return N列表
     */
    @InheritConfiguration(name = "convert")
    List<N> convertList(List<M> mList, P p);

    /**
     * N转为M
     *
     * @param n N对象
     * @param p 参数
     * @return M对象
     */
    @InheritInverseConfiguration(name = "convert")
    M reverseConvert(N n, P p);

    /**
     * N列表转为M列表
     *
     * @param nList M列表
     * @param p 参数
     * @return M列表
     */
    @InheritConfiguration(name = "reverseConvert")
    List<M> reverseConvertList(List<N> nList, P p);

}
