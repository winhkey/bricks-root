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

package org.bricks.utils;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.text.MessageFormat.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.bricks.utils.FunctionUtils.accept;
import static org.bricks.utils.FunctionUtils.apply;
import static org.bricks.utils.ReflectionUtils.addDeclaredFields;
import static org.bricks.utils.ReflectionUtils.getDeclaredField;
import static org.springframework.aop.support.AopUtils.isAopProxy;
import static org.springframework.aop.support.AopUtils.isJdkDynamicProxy;
import static org.springframework.beans.BeanUtils.instantiateClass;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.bricks.enums.ValueEnum;
import org.bricks.service.AbstractConsumerService;
import org.springframework.aop.framework.AdvisedSupport;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象工具类
 *
 * @author fuzy
 *
 */
@Slf4j
@UtilityClass
public class ObjectUtils
{

    /**
     * 直接读取对象的属性值
     *
     * @param object 子类对象
     * @param fieldName 父类中的属性名
     * @param <T> 对象类型
     * @return 父类中的属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String fieldName)
    {
        return ofNullable(getDeclaredField(object.getClass(), fieldName, true)).map(apply(field ->
        {
            field.setAccessible(true);
            return (T) field.get(object);
        }, null, null, null))
                .orElse(null);
    }

    /**
     * 设置对象属性值
     *
     * @param object 子类对象
     * @param fieldName 父类中的属性名
     * @param value 将要设置的值
     */
    public static void setFieldValue(Object object, String fieldName, Object value)
    {
        ofNullable(object).map(o -> getDeclaredField(o.getClass(), fieldName, true))
                .ifPresent(accept(field ->
                {
                    field.setAccessible(true);
                    field.set(object, value);
                }, null, log, null));
    }

    /**
     * 对象拷贝 数据对象空值不拷贝到目标对象
     *
     * @param src 源对象
     * @param dest 目标对象
     */
    public static void copy(Object src, Object dest)
    {
        List<Field> fieldList = newArrayList();
        addDeclaredFields(src.getClass(), fieldList, true, false);
        fieldList.forEach(field ->
        {
            String name = field.getName();
            ofNullable(getFieldValue(src, name)).ifPresent(value -> setFieldValue(dest, name, value));
        });
    }

    /**
     * 比较对象的属性
     *
     * @param oldObj 旧对象
     * @param newObj 新对象
     * @return 不同的属性
     */
    public static Map<String, String> diff(Object oldObj, Object newObj)
    {
        Map<String, String> diffMap = newHashMap();
        Class<?> c1 = oldObj.getClass();
        Class<?> c2 = newObj.getClass();
        if (c1.equals(c2))
        {
            List<Field> fieldList = newArrayList();
            addDeclaredFields(c1, fieldList, true, false);
            fieldList.forEach(field ->
            {
                String name = field.getName();
                Object oldValue = getFieldValue(oldObj, name);
                Object newValue = getFieldValue(newObj, name);
                if (oldValue == null && newValue != null || oldValue != null && !oldValue.equals(newValue))
                {
                    diffMap.put(name, format("from {0} to {1}", oldValue, newValue));
                }
            });
        }
        return diffMap;
    }

    /**
     * 将map数据转换成对应的实体类对象
     *
     * @param <T> 实体类
     * @param dataList 数据
     * @param clazz 实体类
     * @return 对象
     */
    public static <T> List<T> convertMapList(List<Map<String, Object>> dataList, Class<T> clazz)
    {
        return isNotEmpty(dataList) ? dataList.stream()
                .map(dataMap -> convertMap(dataMap, clazz))
                .collect(toList()) : null;
    }

    /**
     * 将map中的值赋给对象
     *
     * @param <T> 实体类
     * @param dataMap 数据
     * @param clazz 实体类
     * @return 对象
     */
    public static <T> T convertMap(Map<String, Object> dataMap, Class<T> clazz)
    {
        T t = instantiateClass(clazz);
        List<Field> list = newArrayList();
        addDeclaredFields(clazz, list, true, false);
        list.forEach(field ->
        {
            String fieldName = field.getName();
            setFieldValue(t, fieldName, dataMap.get(fieldName));
        });
        return t;
    }

    /**
     * 对象转map
     *
     * @param object 对象
     * @param excludes 排除字段
     * @return map
     */
    public static Map<String, Object> convertData(Object object, String... excludes)
    {
        return ofNullable(object).map(o ->
        {
            Map<String, Object> map = newHashMap();
            List<Field> list = newArrayList();
            addDeclaredFields(o.getClass(), list, true, false);
            list.stream()
                    .filter(field -> isEmpty(excludes) || !contains(excludes, field.getName()))
                    .forEach(accept(field ->
                    {
                        field.setAccessible(true);
                        ofNullable(field.get(o)).ifPresent(value -> map.put(field.getName(), value));
                    }, null, log, null));
            return map;
        })
                .orElse(null);
    }

    /**
     * toString
     *
     * @param builder StringBuilder
     * @param object 对象
     * @return builder
     */
    public static StringBuilder buildString(StringBuilder builder, Object object)
    {
        return new AbstractConsumerService<StringBuilder>()
        {

            @Override
            public StringBuilder arrayConsumer(StringBuilder builder, Object object)
            {
                builder.append('[');
                return super.arrayConsumer(builder, object).append(']');
            }

            @Override
            protected StringBuilder arrayConsumer(StringBuilder builder, int n, Object object)
            {
                return super.arrayConsumer(builder, n, object).delete(builder.lastIndexOf(", "), builder.length());
            }

            @Override
            protected StringBuilder collectionConsumer(StringBuilder builder, Collection<?> collection)
            {
                return super.collectionConsumer(builder, collection).delete(builder.lastIndexOf(", "),
                        builder.length());
            }

            @Override
            public StringBuilder collectionConsumer(StringBuilder builder, Object object)
            {
                builder.append('[');
                return super.collectionConsumer(builder, object).append(']');
            }

            @Override
            protected StringBuilder mapConsumer(StringBuilder builder, Object object)
            {
                builder.append('{');
                return super.mapConsumer(builder, object).append('}');
            }

            @Override
            protected StringBuilder mapConsumer(StringBuilder builder, Map<?, ?> map)
            {
                return super.mapConsumer(builder, map).delete(builder.lastIndexOf(", "), builder.length());
            }

            @Override
            protected StringBuilder nullConsumer(StringBuilder builder)
            {
                return builder.append("null");
            }

            @Override
            protected StringBuilder elementConsumer(StringBuilder builder, Object object)
            {
                return buildValue(builder, object);
            }

            @Override
            protected StringBuilder elementConsumer(StringBuilder builder, int i, Object object)
            {
                return buildValue(builder, object);
            }

            @Override
            protected StringBuilder entryConsumer(StringBuilder builder, Object key, Object value)
            {
                buildValue(builder.append(key)
                        .append('='), value);
                return builder;
            }

            @Override
            protected void otherConsumers(StringBuilder builder, Map<Class<?>, Consumer<Object>> consumerMap)
            {
                consumerMap.put(Object.class, builder::append);
            }

        }.consumer(builder, object);
    }

    private StringBuilder buildValue(StringBuilder builder, Object value)
    {
        return buildString(builder, value).append(", ");
    }

    /**
     * 获取枚举对象
     *
     * @param clazz 枚举类
     * @param value 枚举值
     * @return 枚举对象
     */
    @SuppressWarnings("unchecked")
    public static Object getEnumValue(Class<?> clazz, Object value)
    {
        Stream<?> stream = ValueEnum.class.isAssignableFrom(
                clazz) ? of(((Class<ValueEnum<?>>) clazz).getEnumConstants()).filter(v -> v.getValue()
                        .equals(value)) : of(clazz.getEnumConstants()).filter(v -> v.equals(value));
        return stream.findAny()
                .orElse(null);
    }

    /**
     * 获取代理对象的真实对象
     *
     * @param proxy 代理对象
     * @return 真实对象
     */
    public static Object getTarget(Object proxy)
    {
        return isAopProxy(proxy) ? getProxyTargetObject(proxy, isJdkDynamicProxy(proxy) ? "h" : "CGLIB$CALLBACK_0")
                : proxy;
    }

    private static Object getProxyTargetObject(Object proxy, String proxyField)
    {
        Object target = null;
        try
        {
            Field h = proxy.getClass()
                    .getDeclaredField(proxyField);
            h.setAccessible(true);
            Object obj = h.get(proxy);
            Field advised = obj.getClass()
                    .getDeclaredField("advised");
            advised.setAccessible(true);
            target = ((AdvisedSupport) advised.get(obj)).getTargetSource()
                    .getTarget();
        }
        catch (Throwable e)
        {
            log.error(e.getMessage(), e);
        }
        return target;
    }

}
