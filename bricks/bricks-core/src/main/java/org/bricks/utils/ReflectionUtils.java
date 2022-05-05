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
import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.lang.Thread.currentThread;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Proxy.newProxyInstance;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.utils.FunctionUtils.apply;
import static org.springframework.core.ResolvableType.forClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import org.bricks.bean.Bean;
import org.springframework.core.ResolvableType;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 反射工具类
 * 
 * @author fuzy
 *
 */
@Slf4j
@UtilityClass
public class ReflectionUtils
{

    /**
     * 获取指定的方法
     *
     * @param clazz 类
     * @param methodName 方法名
     * @param includeParent 查找父类
     * @param parameterTypes 方法参数类型
     * @return 方法对象
     */
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, boolean includeParent,
            Class<?>... parameterTypes)
    {
        return ofNullable(clazz).filter(c -> c != Object.class)
                .map(apply(c -> c.getDeclaredMethod(methodName, parameterTypes),
                        (c, e) -> includeParent
                                ? getDeclaredMethod(c.getSuperclass(), methodName, includeParent, parameterTypes)
                                : null,
                        null, log, null))
                .orElse(null);
    }

    /**
     * 调用对象方法
     *
     * @param object 对象
     * @param methodName 方法名
     * @param includeParent 查找父类
     * @param parameterTypes 方法参数类型
     * @param parameters 方法参数
     * @return 方法的执行结果
     */
    public static Object invokeMethod(Object object, String methodName, boolean includeParent,
            Class<?>[] parameterTypes, Object... parameters)
    {
        return ofNullable(object).map(o -> getDeclaredMethod(o.getClass(), methodName, includeParent, parameterTypes))
                .map(apply(method ->
                {
                    if (!method.isAccessible())
                    {
                        makeAccessible(method);
                    }
                    return method.invoke(object, parameters);
                }, null, null, log, null))
                .orElse(null);
    }

    /**
     * 获取指定的属性
     *
     * @param clazz 类
     * @param fieldName 属性名
     * @param includeParent 查找父类
     * @return 属性对象
     */
    public static Field getDeclaredField(Class<?> clazz, String fieldName, boolean includeParent)
    {
        return ofNullable(clazz).filter(c -> c != Object.class)
                .map(apply(c -> c.getDeclaredField(fieldName),
                        (c, e) -> getDeclaredField(c.getSuperclass(), fieldName, includeParent), null, null, null))
                .orElse(null);
    }

    /**
     * 添加属性列表
     *
     * @param clazz 类
     * @param list 属性列表
     * @param includeParent 是否包含父类
     * @param containsStatic 是否包含静态字段
     * @param includeClass 包含类型
     */
    public static void addDeclaredFields(Class<?> clazz, List<Field> list, boolean includeParent,
            boolean containsStatic, Class<?>... includeClass)
    {
        ofNullable(clazz).filter(c -> c != Object.class)
                .ifPresent(c ->
                {
                    list.addAll(
                            of(c.getDeclaredFields()).filter(field -> containsStatic || !isStatic(field.getModifiers()))
                                    .filter(field -> isEmpty(includeClass)
                                            || of(includeClass).anyMatch(i -> i.isAssignableFrom(field.getType())))
                                    .collect(toList()));
                    if (includeParent)
                    {
                        addDeclaredFields(c.getSuperclass(), list, true, containsStatic, includeClass);
                    }
                });
    }

    /**
     * 添加方法列表
     *
     * @param clazz 类
     * @param list 方法列表
     * @param includeParent 是否包含父类
     * @param containsStatic 是否包含静态方法
     */
    public static void addDeclaredMethods(Class<?> clazz, List<Method> list, boolean includeParent,
            boolean containsStatic)
    {
        ofNullable(clazz).filter(c -> c != Object.class)
                .ifPresent(c ->
                {
                    list.addAll(of(c.getDeclaredMethods())
                            .filter(field -> containsStatic || !isStatic(field.getModifiers()))
                            .collect(toList()));
                    if (includeParent)
                    {
                        addDeclaredMethods(c.getSuperclass(), list, true, containsStatic);
                    }
                });
    }

    /**
     * 获取指定的注解
     *
     * @param <A> 注解类型
     * @param element 注解元素
     * @param annotationClass 注解类型
     * @return 注解对象
     */
    public static <A extends Annotation> A getAnnotation(AnnotatedElement element, Class<A> annotationClass)
    {
        return ofNullable(element).map(e -> e.getAnnotation(annotationClass))
                .orElse(null);
    }

    /**
     * 获取指定的注解
     *
     * @param <A> 注解类型
     * @param element 注解元素
     * @param annotationClass 注解类型
     * @return 注解对象
     */
    public static <A extends Annotation> A getDeclaredAnnotation(AnnotatedElement element, Class<A> annotationClass)
    {
        return ofNullable(element).map(e -> e.getDeclaredAnnotation(annotationClass))
                .orElse(null);
    }

    /**
     * 根据注解获取字段
     *
     * @param <A> 注解类型
     * @param clazz 类
     * @param annotationClass 注解类型
     * @param includeParent 是否包含父类
     * @return 字段
     */
    public static <A extends Annotation> Field getFieldByAnnotation(Class<?> clazz, Class<A> annotationClass,
            boolean includeParent)
    {
        List<Field> list = newArrayList();
        addDeclaredFields(clazz, list, includeParent, false);
        return list.stream()
                .filter(field -> getDeclaredAnnotation(field, annotationClass) != null)
                .findFirst()
                .orElse(null);
    }

    /**
     * 添加注解列表
     *
     * @param element 注解元素
     * @param list 注解列表
     */
    public static void getDeclaredAnnotations(AnnotatedElement element, List<Annotation> list)
    {
        ofNullable(element).ifPresent(e -> list.addAll(of(e.getDeclaredAnnotations()).collect(toList())));
    }

    /**
     * 获取嵌套类数组
     *
     * @param clazz 类型
     * @return 嵌套类数组
     */
    public static Type[] getNestedTypes(Class<?> clazz)
    {
        Set<Type> set = newLinkedHashSet();
        set.add(clazz);
        List<Field> list = newArrayList();
        addDeclaredFields(clazz, list, true, false, Bean.class);
        set.addAll(list.stream()
                .map(Field::getType)
                .collect(toList()));
        return set.toArray(new Type[0]);
    }

    /**
     * 获取泛型参数类型列表
     *
     * @param selfClass 自身class
     * @param superClass 父class
     * @return 泛型参数类型列表
     */
    public static List<Class<?>> getComponentClassList(Class<?> selfClass, Class<?> superClass)
    {
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
    public static Class<?> getComponentClass(Class<?> selfClass, Class<?> superClass, int index)
    {
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
    public static ResolvableType getResolvableType(Class<?> selfClass, Class<?> superClass)
    {
        return forClass(selfClass).as(superClass);
    }

    /**
     * 动态代理对象
     *
     * @param clazz 接口类型
     * @param handler 代理方法
     * @param <T> 接口类型
     * @return 代理对象
     */
    @SuppressWarnings(UNCHECKED)
    public static <T> T newProxy(Class<T> clazz, InvocationHandler handler)
    {
        return (T) newProxyInstance(currentThread().getContextClassLoader(), new Class[] {clazz}, handler);
    }

}
