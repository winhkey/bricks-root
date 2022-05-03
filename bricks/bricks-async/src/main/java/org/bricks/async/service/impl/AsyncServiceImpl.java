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

package org.bricks.async.service.impl;

import static java.text.MessageFormat.format;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.bricks.async.service.AsyncService;
import org.bricks.listener.AbstractInitFinishedListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
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
public class AsyncServiceImpl extends AbstractInitFinishedListener implements AsyncService
{

    /**
     * 定时器
     */
    private final ScheduledExecutorService schedulerExecutor = newScheduledThreadPool(10);

    /**
     * beanFactory
     */
    @Resource
    private BeanFactory beanFactory;

    /**
     * 线程池
     */
    @Resource
    private ThreadPoolTaskExecutor executor;

    /**
     * 自定义线程池
     */
    @Resource
    private ThreadPoolTaskExecutor asyncExecutor;

    /**
     * 线程池
     */
    private ExecutorService executorService;

    /**
     * 大线程池
     */
    @Value("${bricks.async.largePool:false}")
    private boolean largePool;

    @Override
    public void async(Runnable runnable)
    {
        executor.execute(runnable);
    }

    @Override
    public <T, V> V async(T t, Function<T, V> callback, long timeout, TimeUnit timeUnit, V defaultValue)
    {
        return future(t, callback, timeout, timeUnit, defaultValue).join();
    }

    @Override
    public <T, V> List<V> async(T t, List<Function<T, V>> list, long timeout, TimeUnit timeUnit, V defaultValue)
    {
        return list.stream()
                .map(callback -> future(t, callback, timeout, timeUnit, defaultValue))
                .map(CompletableFuture::join)
                .collect(toList());
    }

    @Override
    public <T, V> List<V> asyncList(Collection<T> collection, Function<T, V> callback, long timeout, TimeUnit timeUnit,
            V defaultValue)
    {
        return asyncStream(collection.stream(), callback, timeout, timeUnit, defaultValue);
    }

    @Override
    public <T, V> List<V> asyncStream(Stream<T> stream, Function<T, V> callback, long timeout, TimeUnit timeUnit,
            V defaultValue)
    {
        return stream.map(t -> future(t, callback, timeout, timeUnit, defaultValue))
                .collect(toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    private <T, V> CompletableFuture<V> future(T t, Function<T, V> callback, long timeout, TimeUnit timeUnit,
            V defaultValue)
    {
        return supplyAsync(() -> callback.apply(t), timeout, timeUnit, defaultValue).exceptionally(e ->
        {
            log.error(format("{0}", t), e);
            return defaultValue;
        });
    }

    private <V> CompletableFuture<V> supplyAsync(final Supplier<V> supplier, long timeout, TimeUnit timeUnit,
            V defaultValue)
    {
        final CompletableFuture<V> completableFuture = new CompletableFuture<>();
        Runnable runnable = () ->
        {
            try
            {
                completableFuture.complete(supplier.get());
            }
            catch (Throwable e)
            {
                completableFuture.completeExceptionally(e);
            }
        };
        Future<?> future = largePool ? executorService.submit(runnable) : asyncExecutor.submit(runnable);
        schedulerExecutor.schedule(() ->
        {
            if (!completableFuture.isDone())
            {
                completableFuture.complete(defaultValue);
                future.cancel(true);
            }
        }, timeout, timeUnit);
        return completableFuture;
    }

    @Override
    protected void doAsyncInitFinished()
    {
        if (largePool)
        {
            executorService = new TraceableExecutorService(beanFactory, newCachedThreadPool());
        }
    }

}
