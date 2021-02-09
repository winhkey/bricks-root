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

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.bricks.service.Callback;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 异步调用
 *
 * @author fuzy
 *
 */
@Slf4j
@Service
public class AsyncService
{

    /**
     * 线程池
     */
    @Resource
    private ThreadPoolTaskExecutor executor;

    /**
     * 批量异步请求，合并结果
     *
     * @param <T> 请求类型
     * @param <V> 结果类型
     * @param collection 请求列表
     * @param callback 回调
     * @return 结果列表
     */
    public <T, V> List<V> asyncList(Collection<T> collection, Callback<T, V> callback)
    {
        return futureStream(collection, callback).map(CompletableFuture::join)
                .collect(toList());
    }

    /**
     * 单个异步请求
     *
     * @param <T> 请求类型
     * @param <V> 结果类型
     * @param t 请求
     * @param callback 回调
     * @return 结果
     */
    public <T, V> V async(T t, Callback<T, V> callback)
    {
        return future(t, callback).join();
    }

    /**
     * 异步请求，不同步结果
     *
     * @param <T> 请求类型
     * @param <V> 结果类型
     * @param t 请求
     * @param callback 回调
     * @return 结果
     */
    public <T, V> CompletableFuture<V> future(T t, Callback<T, V> callback)
    {
        return CompletableFuture.supplyAsync(() -> callback.call(t), executor)
                .exceptionally(e ->
                {
                    log.error(e.getMessage(), e);
                    return null;
                });
    }

    /**
     * 异步请求，不同步结果
     *
     * @param <T> 请求类型
     * @param <V> 结果类型
     * @param collection 请求
     * @param callback 回调
     * @return 结果流
     */
    public <T, V> Stream<CompletableFuture<V>> futureStream(Collection<T> collection, Callback<T, V> callback)
    {
        return collection.stream()
                .map(t -> future(t, callback))
                .collect(toList())
                .stream();
    }

}
