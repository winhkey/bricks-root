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

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.lang.reflect.Array.get;
import static java.util.Optional.of;
import static java.util.stream.IntStream.range;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.bricks.utils.FunctionUtils.accept;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 根据对象进行accept操作，抽象类
 *
 * @author fuzy
 *
 * @param <T> 消费类型
 */
public abstract class AbstractConsumerService<T>
{

    /**
     * 根据不同类型构造消费者map再调用accept方法, 默认包含null 数组 集合 map接口
     *
     * @param t 消费对象
     * @param object 对象
     * @return 消费对象
     */
    public T consumer(T t, Object object)
    {
        Map<Class<?>, Consumer<Object>> consumerMap = newLinkedHashMap();
        consumerMap.put(Void.class, o -> nullConsumer(t));
        consumerMap.put(Object[].class, o -> arrayConsumer(t, o));
        consumerMap.put(Collection.class, o -> collectionConsumer(t, o));
        consumerMap.put(Map.class, o -> mapConsumer(t, o));
        otherConsumers(t, consumerMap);
        accept(object, consumerMap);
        return t;
    }

    /**
     * 处理数组
     *
     * @param t 消费对象
     * @param object 数组对象
     * @return 消费对象
     */
    protected T arrayConsumer(T t, Object object)
    {
        of(object).map(Array::getLength)
                .filter(n -> n > 0)
                .ifPresent(n -> arrayConsumer(t, n, object));
        return t;
    }

    /**
     * 处理数组
     *
     * @param t 消费对象
     * @param n 数组个数
     * @param object 数组对象
     * @return 消费对象
     */
    protected T arrayConsumer(T t, int n, Object object)
    {
        range(0, n).forEach(i -> elementConsumer(t, i, get(object, i)));
        return t;
    }

    /**
     * 处理集合
     *
     * @param t 消费对象
     * @param object 集合对象
     * @return 消费对象
     */
    protected T collectionConsumer(T t, Object object)
    {
        Collection<?> collection = (Collection<?>) object;
        if (isNotEmpty(collection))
        {
            collectionConsumer(t, collection);
        }
        return t;
    }

    /**
     * 处理集合
     *
     * @param t 消费对象
     * @param collection 集合
     * @return 消费对象
     */
    protected T collectionConsumer(T t, Collection<?> collection)
    {
        collection.forEach(o -> elementConsumer(t, o));
        return t;
    }

    /**
     * 处理map
     *
     * @param t 消费对象
     * @param object map对象
     * @return 消费对象
     */
    protected T mapConsumer(T t, Object object)
    {
        Map<?, ?> map = (Map<?, ?>) object;
        if (isNotEmpty(map))
        {
            mapConsumer(t, map);
        }
        return t;
    }

    /**
     * 处理map
     *
     * @param t 消费对象
     * @param map map
     * @return 消费对象
     */
    protected T mapConsumer(T t, Map<?, ?> map)
    {
        map.forEach((k, v) -> entryConsumer(t, k, v));
        return t;
    }

    /**
     * 处理null对象
     *
     * @param t 消费对象
     * @return 消费对象
     */
    protected abstract T nullConsumer(T t);

    /**
     * 处理数组元素
     *
     * @param t 消费对象
     * @param i 序号
     * @param object 元素
     * @return 消费对象
     */
    protected abstract T elementConsumer(T t, int i, Object object);

    /**
     * 处理集合元素
     *
     * @param t 消费对象
     * @param object 元素
     * @return 消费对象
     */
    protected abstract T elementConsumer(T t, Object object);

    /**
     * 处理map
     *
     * @param t 消费对象
     * @param key 键
     * @param value 值
     * @return 消费对象
     */
    protected abstract T entryConsumer(T t, Object key, Object value);

    /**
     * 处理其它对象
     *
     * @param t 消费对象
     * @param consumerMap 消费者map
     */
    protected abstract void otherConsumers(T t, Map<Class<?>, Consumer<Object>> consumerMap);

}
