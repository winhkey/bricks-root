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

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.stereotype.Service;

/**
 * 限流回调
 *
 * @author fuzy
 */
@Service
public class LimitCallback
{

    /**
     * 限流回调
     * 
     * @param <T> 源
     * @param <V> 目标
     * @param collection 集合
     * @param limit 分批
     * @param function function
     * @return 列表
     */
    public <T, V> List<V> limitFunction(Collection<T> collection, int limit, Function<List<T>, List<V>> function)
    {
        return limitFunction(collection, limit, function, 0);
    }

    /**
     * 限流回调
     * 
     * @param <T> 源
     * @param <V> 目标
     * @param collection 集合
     * @param limit 分批
     * @param function function
     * @param delaySeconds 每批间隔
     * @return 列表
     */
    public <T, V> List<V> limitFunction(Collection<T> collection, int limit, Function<List<T>, List<V>> function,
            int delaySeconds)
    {
        List<V> vList = newArrayList();
        if (isNotEmpty(collection))
        {
            List<T> list = newArrayList(collection);
            int length = collection.size();
            int page = length / limit;
            int mod = length % limit;
            int i = 0;
            List<V> subList;
            try
            {
                for (; i < page; i++)
                {
                    subList = function.apply(list.subList(i * limit, (i + 1) * limit));
                    addSubList(vList, subList);
                    delay(delaySeconds);
                }
                if (mod > 0)
                {
                    subList = function.apply(list.subList(i * limit, length));
                    addSubList(vList, subList);
                }
            }
            finally
            {
                list.clear();
            }
        }
        return vList;
    }

    /**
     * 限流回调
     * 
     * @param <T> 源
     * @param <V> 目标
     * @param collection 集合
     * @param limit 分批
     * @param consumer consumer
     */
    public <T, V> void limit(Collection<T> collection, int limit, Consumer<List<T>> consumer)
    {
        limit(collection, limit, consumer, 0);
    }

    /**
     * 限流回调
     * 
     * @param <T> 源
     * @param <V> 目标
     * @param collection 集合
     * @param limit 分批
     * @param consumer consumer
     * @param delaySeconds 每批间隔
     */
    public <T, V> void limit(Collection<T> collection, int limit, Consumer<List<T>> consumer, int delaySeconds)
    {
        if (isNotEmpty(collection))
        {
            List<T> list = newArrayList(collection);
            int length = collection.size();
            int page = length / limit;
            int mod = length % limit;
            int i = 0;
            try
            {
                for (; i < page; i++)
                {
                    consumer.accept(list.subList(i * limit, (i + 1) * limit));
                    delay(delaySeconds);
                }
                if (mod > 0)
                {
                    consumer.accept(list.subList(i * limit, length));
                }
            }
            finally
            {
                list.clear();
            }
        }
    }

    private <V> void addSubList(List<V> list, List<V> subList)
    {
        if (isNotEmpty(subList))
        {
            list.addAll(subList);
        }
    }

    private void delay(int delaySeconds)
    {
        if (delaySeconds > 0)
        {
            try
            {
                Thread.sleep(delaySeconds * 1000L);
            }
            catch (InterruptedException e)
            {
                // do nth.
            }
        }
    }

}
