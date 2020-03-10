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

package org.bricks.utils;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.of;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.bricks.utils.FunctionUtils.accept;
import static org.bricks.utils.FunctionUtils.apply;
import static org.springframework.aop.support.AopUtils.isAopProxy;
import static org.springframework.aop.support.AopUtils.isJdkDynamicProxy;
import static org.springframework.beans.BeanUtils.instantiateClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.bricks.enums.ValueEnum;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.core.ResolvableType;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class EntityUtils {

    /**
     * 获取对象的 DeclaredMethod
     *
     * @param clazz 子类
     * @param methodName 父类中的方法名
     * @param parameterTypes 父类中的方法参数类型
     * @return 父类中的方法对象
     */
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        return ofNullable(clazz).filter(c -> c != Object.class)
                .map(apply(c -> c.getDeclaredMethod(methodName, parameterTypes), c -> getDeclaredMethod(c.getSuperclass(), methodName, parameterTypes), log, null))
                .orElse(null);
    }

    /**
     * 调用对象方法
     *
     * @param object 子类对象
     * @param methodName 父类中的方法名
     * @param parameterTypes 父类中的方法参数类型
     * @param parameters 父类中的方法参数
     * @return 父类中方法的执行结果
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        return ofNullable(object).map(o -> getDeclaredMethod(o.getClass(), methodName, parameterTypes))
                .map(apply(method -> {
                    method.setAccessible(true);
                    return method.invoke(object, parameters);
                }, null, log, null))
                .orElse(null);
    }

    /**
     * 获取对象的 DeclaredField
     *
     * @param clazz 子类
     * @param fieldName 父类中的属性名
     * @return 父类中的属性对象
     */
    public static Field getDeclaredField(Class<?> clazz, String fieldName) {
        return ofNullable(clazz).filter(c -> c != Object.class)
                .map(apply(c -> c.getDeclaredField(fieldName), c -> getDeclaredField(c.getSuperclass(), fieldName), null, null))
                .orElse(null);
    }

    /**
     * 获取对象的 DeclaredField列表
     *
     * @param clazz 子类
     * @param list 列表
     * @param containsStatic 是否包含静态字段
     */
    public static void getDeclaredFields(Class<?> clazz, List<Field> list, boolean containsStatic) {
        ofNullable(clazz).filter(c -> c != Object.class)
                .ifPresent(c -> {
                    list.addAll(of(c.getDeclaredFields()).filter(field -> containsStatic || !isStatic(field.getModifiers()))
                            .collect(toList()));
                    getDeclaredFields(c.getSuperclass(), list, containsStatic);
                });
    }

    /**
     * 直接读取对象的属性值
     *
     * @param object 子类对象
     * @param fieldName 父类中的属性名
     * @param <T> 对象类型
     * @return 父类中的属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, String fieldName) {
        return ofNullable(getDeclaredField(object.getClass(), fieldName)).map(apply(field -> {
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
    public static void setFieldValue(Object object, String fieldName, Object value) {
        ofNullable(object).map(o -> getDeclaredField(o.getClass(), fieldName))
                .ifPresent(accept(field -> {
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
    public static void copy(Object src, Object dest) {
        List<Field> fieldList = newArrayList();
        getDeclaredFields(src.getClass(), fieldList, false);
        fieldList.forEach(field -> {
            String name = field.getName();
            ofNullable(getFieldValue(src, name)).ifPresent(value -> setFieldValue(dest, name, value));
        });
    }

    /**
     * 将map数据转换成对应的实体类对象
     *
     * @param dataList 数据
     * @param clazz 实体类
     * @param <T> 实体类
     * @return 对象
     */
    public static <T> List<T> convertMapList(List<Map<String, Object>> dataList, Class<T> clazz) {
        return isNotEmpty(dataList) ? dataList.stream()
                .map(dataMap -> convertMap(dataMap, clazz))
                .collect(toList()) : null;
    }

    /**
     * 将map中的值赋给对象
     *
     * @param dataMap 数据
     * @param clazz 实体类
     * @param <T> 实体类
     * @return 对象
     */
    public static <T> T convertMap(Map<String, Object> dataMap, Class<T> clazz) {
        T t = instantiateClass(clazz);
        List<Field> list = newArrayList();
        getDeclaredFields(clazz, list, false);
        list.forEach(field -> {
            String fieldName = field.getName();
            if (dataMap.containsKey(fieldName)) {
                setFieldValue(t, fieldName, dataMap.get(fieldName));
            }
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
    public static Map<String, Object> convertData(Object object, String... excludes) {
        return ofNullable(object).map(o -> {
            Map<String, Object> dataMap = newHashMap();
            List<Field> list = newArrayList();
            getDeclaredFields(o.getClass(), list, false);
            list.stream()
                    .filter(field -> isEmpty(excludes) || !contains(excludes, field.getName()))
                    .forEach(accept(field -> {
                        field.setAccessible(true);
                        ofNullable(field.get(o)).ifPresent(value -> dataMap.put(field.getName(), value));
                    }, null, log, null));
            return dataMap;
        })
                .orElse(null);
    }

    /**
     * 集合toString
     *
     * @param builder StringBuilder
     * @param col 集合
     * @param <T> 任意类型
     */
    public static <T> void buildString(StringBuilder builder, Collection<T> col) {
        if (isNotEmpty(col)) {
            builder.append('[');
            col.forEach(t -> buildValue(builder, t));
            builder.delete(builder.lastIndexOf(", "), builder.length());
            builder.append(']');
        } else {
            builder.append(col);
        }
    }

    /**
     * map toString
     *
     * @param builder StringBuilder
     * @param map Map
     * @param <K> key
     * @param <V> value
     */
    public static <K, V> void buildString(StringBuilder builder, Map<K, V> map) {
        if (isNotEmpty(map)) {
            builder.append('{');
            map.forEach((key, value) -> {
                builder.append(key)
                        .append('=');
                buildValue(builder, value);
            });
            builder.delete(builder.lastIndexOf(", "), builder.length());
            builder.append('}');
        } else {
            builder.append(map);
        }
    }

    /**
     * toString
     *
     * @param builder StringBuilder
     * @param object 对象
     */
    public static void buildString(StringBuilder builder, Object object) {
        if (object == null) {
            builder.append("null");
        } else {
            Class<?> clazz = object.getClass();
            if (clazz.isArray()) {
                builder.append('[');
                int n = getLength(object);
                if (n > 0) {
                    range(0, n).forEach(i -> {
                        buildString(builder, get(object, i));
                        builder.append(", ");
                    });
                    builder.delete(builder.lastIndexOf(", "), builder.length());
                }
                builder.append(']');
            } else {
                builder.append(object);
            }
        }
    }

    /**
     * 获取枚举对象
     *
     * @param clazz 枚举类
     * @param value 枚举值
     * @return 枚举对象
     */
    @SuppressWarnings("unchecked")
    public static Object getEnumValue(Class<?> clazz, Object value) {
        Stream<?> stream = ValueEnum.class.isAssignableFrom(clazz) ? of(((Class<ValueEnum<?>>) clazz).getEnumConstants()).filter(v -> v.getValue()
                .equals(value)) : of(clazz.getEnumConstants()).filter(v -> v.equals(value));
        return stream.findAny()
                .orElse(null);
    }

    private void buildValue(StringBuilder builder, Object value) {
        if (value instanceof Collection) {
            buildString(builder, (Collection<?>) value);
        }
        else if (value instanceof Map) {
            buildString(builder, (Map<?, ?>) value);
        } else {
            buildString(builder, value);
        }
        builder.append(", ");
    }

    /**
     * 获取泛型参数类型列表
     *
     * @param selfClass 自身class
     * @param superClass 父class
     * @return 泛型参数类型列表
     */
    public static List<Class<?>> getComponentClassList(Class<?> selfClass, Class<?> superClass) {
        return of(getResolvableType(selfClass, superClass).getGenerics()).map(ResolvableType::resolve)
                .collect(toList());
    }

    /**
     * 获取泛型参数类型
     *
     * @param selfClass 自身class
     * @param superClass 父class
     * @param index 泛型参数索引
     * @return 泛型参数类型
     */
    public static Class<?> getComponentClass(Class<?> selfClass, Class<?> superClass, int index) {
        return getResolvableType(selfClass, superClass).getGeneric(index)
                .resolve();
    }

    /**
     * 获取ResolvableType
     *
     * @param selfClass 自身class
     * @param superClass 父class
     * @return ResolvableType
     */
    public ResolvableType getResolvableType(Class<?> selfClass, Class<?> superClass) {
        return ResolvableType.forClass(selfClass)
                .as(superClass);
    }

    /**
     * 获取代理对象的真实对象
     *
     * @param proxy 代理对象
     * @return 真实对象
     */
    public static Object getTarget(Object proxy) {
        return isAopProxy(proxy) ? getProxyTargetObject(proxy, isJdkDynamicProxy(proxy) ? "h" : "CGLIB$CALLBACK_0") : proxy;
    }

    private static Object getProxyTargetObject(Object proxy, String proxyField) {
        Object target = null;
        try {
            Field h = proxy.getClass()
                    .getDeclaredField(proxyField);
            h.setAccessible(true);
            Object obj = h.get(proxy);
            Field advised = obj.getClass()
                    .getDeclaredField("advised");
            advised.setAccessible(true);
            target = ((AdvisedSupport) advised.get(obj)).getTargetSource()
                    .getTarget();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return target;
    }

}
