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

package org.bricks.async.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 异步调用
 *
 * @author fuzy
 *
 */
public interface AsyncService
{

    /**
     * 异步执行单个线程
     *
     * @param runnable 异步任务
     */
    void async(Runnable runnable);

    /**
     * 单个异步请求
     *
     * @param <T> 请求类型
     * @param <V> 结果类型
     * @param t 请求
     * @param callback 回调
     * @param timeout 超时
     * @param timeUnit 单位
     * @param defaultValue 默认值
     * @return 结果
     */
    <T, V> V async(T t, Function<T, V> callback, long timeout, TimeUnit timeUnit, V defaultValue);

    /**
     * 一个异步请求多个结果
     *
     * @param t 请求
     * @param list 回调列表
     * @param <T> 请求类型
     * @param <V> 结果类型
     * @param timeout 超时
     * @param timeUnit 单位
     * @param defaultValue 默认值
     * @return 结果列表
     */
    <T, V> List<V> async(T t, List<Function<T, V>> list, long timeout, TimeUnit timeUnit, V defaultValue);

    /**
     * 批量异步请求，合并结果
     *
     * @param <T> 请求类型
     * @param <V> 结果类型
     * @param collection 请求列表
     * @param callback 回调
     * @param timeout 超时
     * @param timeUnit 单位
     * @param defaultValue 默认值
     * @return 结果列表
     */
    <T, V> List<V> asyncList(Collection<T> collection, Function<T, V> callback, long timeout, TimeUnit timeUnit,
            V defaultValue);

    /**
     * 批量异步请求，合并结果
     *
     * @param <T> 请求类型
     * @param <V> 结果类型
     * @param stream 请求流
     * @param callback 回调
     * @param timeout 超时
     * @param timeUnit 单位
     * @param defaultValue 默认值
     * @return 结果列表
     */
    <T, V> List<V> asyncStream(Stream<T> stream, Function<T, V> callback, long timeout, TimeUnit timeUnit,
            V defaultValue);

}
