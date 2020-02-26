/*
  Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.bricks.function;

/**
 * 抛异常的Function
 *
 * @param <T> 入参
 * @param <R> 返回
 * @param <E> 异常
 * @author fuzy
 */
@FunctionalInterface
public interface ThrowableFunction<T, R, E extends Throwable> {

    /**
     * 操作
     *
     * @param t 入参
     * @return 返回
     * @throws E 异常
     */
    R apply(T t) throws E;

}
