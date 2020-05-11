package org.bricks.service;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;
import static java.util.stream.IntStream.range;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.bricks.utils.FunctionUtils.accept;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 根据对象进行accept操作，抽象类
 * 
 * @author fuzy
 * 
 */
public abstract class AbstractConsumerService {

    /**
     * 根据不同类型构造消费者map再调用accept方法, 默认包含null 数组 集合 map接口
     * 
     * @param object 对象
     */
    public void consumer(Object object) {
        Map<Class<?>, Consumer<Object>> consumerMap = newLinkedHashMap();
        consumerMap.put(Void.class, o -> nullConsumer());
        consumerMap.put(Object[].class, o -> arrayConsumer(o));
        consumerMap.put(Collection.class, o -> collectionConsumer(o));
        consumerMap.put(Map.class, o -> mapConsumer(o));
        otherConsumers(consumerMap);
        accept(object, consumerMap);
    }

    /**
     * 处理数组
     * 
     * @param object 数组对象
     */
    protected void arrayConsumer(Object object) {
        int n = getLength(object);
        if (n > 0) {
            arrayConsumer(n, object);
        }
    }

    /**
     * 处理集合
     * 
     * @param object 集合对象
     */
    protected void collectionConsumer(Object object) {
        Collection<?> collection = (Collection<?>) object;
        if (isNotEmpty(collection)) {
            collectionConsumer(collection);
        }
    }

    /**
     * 处理map
     * 
     * @param object map对象
     */
    protected void mapConsumer(Object object) {
        Map<?, ?> map = (Map<?, ?>) object;
        if (isNotEmpty(map)) {
            mapConsumer(map);
        }
    }

    /**
     * 处理数组
     * 
     * @param n 数组个数
     * @param object 数组对象
     */
    protected void arrayConsumer(int n, Object object) {
        range(0, n).forEach(i -> elementConsumer(i, get(object, i)));
    }
    
    protected void collectionConsumer(Collection<?> collection) {
        collection.forEach(t -> elementConsumer(t));
    }

    protected void mapConsumer(Map<?, ?> map) {
        map.forEach((key, value) -> entryConsumer(key, value));
    }

    /**
     * 处理空指针
     */
    protected abstract void nullConsumer();

    /**
     * 处理数组元素
     * 
     * @param i 序号
     * @param object 元素
     */
    protected abstract void elementConsumer(int i, Object object);

    /**
     * 处理列表元素
     * 
     * @param object 元素
     */
    protected abstract void elementConsumer(Object object);

    /**
     * 处理entry
     * 
     * @param key 键
     * @param value 值
     */
    protected abstract void entryConsumer(Object key, Object value);

    /**
     * 处理其它类型
     * 
     * @param consumerMap 消费者map
     */
    protected abstract void otherConsumers(Map<Class<?>, Consumer<Object>> consumerMap);

}
