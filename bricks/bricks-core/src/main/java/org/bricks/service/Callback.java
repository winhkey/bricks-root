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

package org.bricks.service;

/**
 * 回调接口
 *
 * @author fuzy
 *
 * @param <P> 请求
 * @param <R> 响应
 */
@FunctionalInterface
public interface Callback<P, R> {

    /**
     * 回调
     * 
     * @param param 参数
     * @return 返回值
     */
    R call(P param);

}
