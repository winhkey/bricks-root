/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
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

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.bricks.utils.FunctionUtils.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.core.ResolvableType;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ReflectionUtils {

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
                .map(apply(c -> c.getDeclaredMethod(methodName, parameterTypes),
                        c -> getDeclaredMethod(c.getSuperclass(), methodName, parameterTypes), log, null))
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
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object... parameters) {
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
                .map(apply(c -> c.getDeclaredField(fieldName), c -> getDeclaredField(c.getSuperclass(), fieldName),
                        null, null))
                .orElse(null);
    }

    /**
     * 获取对象的 DeclaredField列表
     *
     * @param clazz 子类
     * @param list 列表
     * @param containsStatic 是否包含静态字段
     */
    public static void addDeclaredFields(Class<?> clazz, List<Field> list, boolean containsStatic) {
        ofNullable(clazz).filter(c -> c != Object.class)
                .ifPresent(c -> {
                    list.addAll(
                            of(c.getDeclaredFields()).filter(field -> containsStatic || !isStatic(field.getModifiers()))
                                    .collect(toList()));
                    addDeclaredFields(c.getSuperclass(), list, containsStatic);
                });
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
    public static ResolvableType getResolvableType(Class<?> selfClass, Class<?> superClass) {
        return ResolvableType.forClass(selfClass)
                .as(superClass);
    }

}
