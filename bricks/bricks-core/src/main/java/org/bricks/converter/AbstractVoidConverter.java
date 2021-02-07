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

/**
 * 无参对象转换抽象类
 *
 * @author fuzy
 *
 * @param <M> 源对象
 * @param <N> 目标对象
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
