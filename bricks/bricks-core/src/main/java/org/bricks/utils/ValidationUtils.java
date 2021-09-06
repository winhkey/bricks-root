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

import static java.text.MessageFormat.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.bricks.constants.Constants.PatternConstants.LOWER_START_PATTERN;
import static org.bricks.utils.FunctionUtils.apply;
import static org.bricks.utils.RegexUtils.matches;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import org.bricks.exception.BaseException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * jsr参数校验
 *
 * @author fuzy
 * 
 */
@Slf4j
@UtilityClass
public class ValidationUtils
{

    /**
     * 验证器
     */
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory()
            .getValidator();

    /**
     * 执行验证器
     */
    private static final ExecutableValidator EXECUTABLE_VALIDATOR = VALIDATOR.forExecutables();

    /**
     * 注解验证方法
     *
     * @param object 对象
     * @param name 方法名
     * @param value 返回值
     * @param <T> 对象类型
     */
    public static <T> void validateMethods(T object, String name, Object value)
    {
        ofNullable(object).map(apply(t -> EXECUTABLE_VALIDATOR.validateReturnValue(t, t.getClass()
                .getMethod(name), value), null, null, log, null))
                .ifPresent(ValidationUtils::check);
    }

    /**
     * 注解验证参数
     *
     * @param t 参数
     * @param <T> 参数类型
     */
    public static <T> void validate(T t)
    {
        Set<ConstraintViolation<T>> set = VALIDATOR.validate(t);
        check(set);
    }

    /**
     * 注解验证参数
     *
     * @param collection 参数集合
     * @param <T> 参数
     */
    public static <T> void validateCollection(Collection<T> collection)
    {
        Set<ConstraintViolation<T>> set = collection.stream()
                .flatMap(t -> VALIDATOR.validate(t)
                        .stream())
                .collect(toSet());
        check(set);
    }

    /**
     * 注解验证参数
     *
     * @param map 参数map
     * @param <T> 键类型
     * @param <V> 值类型
     */
    public static <T, V> void validateMap(Map<T, V> map)
    {
        Set<ConstraintViolation<V>> set = map.entrySet()
                .stream()
                .flatMap(entry -> VALIDATOR.validate(entry.getValue())
                        .stream())
                .collect(toSet());
        check(set);
    }

    private static <T> void check(Set<ConstraintViolation<T>> set)
    {
        if (isNotEmpty(set))
        {
            String message = set.stream()
                    .map(c -> format("{0}{1}", matches(LOWER_START_PATTERN, c.getMessage()) ? "" : c.getPropertyPath(),
                            c.getMessage()))
                    .collect(joining(", "));
            throw new BaseException(message);
        }
    }

}
