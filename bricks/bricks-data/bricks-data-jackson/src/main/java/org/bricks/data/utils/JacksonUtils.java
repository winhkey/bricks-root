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

package org.bricks.data.utils;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.PropertyAccessor.ALL;
import static com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.google.common.collect.Maps.newConcurrentMap;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Locale.US;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.bricks.constants.Constants.FormatConstants.DATETIME_FORMAT;
import static org.bricks.constants.Constants.FormatConstants.DATE_FORMAT;
import static org.bricks.constants.Constants.FormatConstants.TIME_FORMAT;
import static org.bricks.utils.MD5Utils.getMD5String;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import lombok.experimental.UtilityClass;

/**
 * json工具类
 *
 * @author fuzy
 *
 */
@UtilityClass
public class JacksonUtils
{

    /**
     * 类型缓存
     */
    private static final Map<String, JavaType> JAVA_TYPE_MAP = newConcurrentMap();

    /**
     * @return ObjectMapper
     */
    @SuppressWarnings("deprecation")
    public static ObjectMapper createJsonMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        configure(objectMapper);
        objectMapper.setSerializationInclusion(NON_NULL);
        // 设置数字按字符串处理
        objectMapper.enable(WRITE_NUMBERS_AS_STRINGS);
        // 允许字段名不含双引号
        objectMapper.enable(ALLOW_UNQUOTED_FIELD_NAMES);
        // 允许单引号
        objectMapper.enable(ALLOW_SINGLE_QUOTES);
        // 非法转义字符
        objectMapper.enable(ALLOW_UNQUOTED_CONTROL_CHARS);
        objectMapper.disable(FAIL_ON_EMPTY_BEANS);
        objectMapper.setVisibility(ALL, ANY);
        return objectMapper;
    }

    /**
     * 构建XmlMapper
     *
     * @return XmlMapper
     */
    public static ObjectMapper createXmlMapper()
    {
        XmlMapper xmlMapper = new XmlMapper();
        configure(xmlMapper);
        return xmlMapper;
    }

    private void configure(ObjectMapper objectMapper)
    {
        objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        // json转换对象忽略找不到属性的对象
        objectMapper.enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // 设置日期处理格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", US));
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ofPattern(DATETIME_FORMAT, US)));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(ofPattern(DATETIME_FORMAT, US)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(ofPattern(DATE_FORMAT, US)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(ofPattern(DATE_FORMAT, US)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(ofPattern(TIME_FORMAT, US)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(ofPattern(TIME_FORMAT, US)));
        objectMapper.registerModule(javaTimeModule);
    }

    /**
     * 处理类型
     *
     * @param type 类型
     * @return 结果
     */
    public static JavaType getJavaType(Type type)
    {
        return JAVA_TYPE_MAP.computeIfAbsent(getCacheKey(type), k ->
        {
            TypeFactory typeFactory = TypeFactory.defaultInstance();
            Class<?> clazz;
            JavaType[] javaTypes;
            if (type instanceof ParameterizedType)
            {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                clazz = (Class<?>) ((ParameterizedType) type).getRawType();
                javaTypes = stream(actualTypeArguments).map(JacksonUtils::getJavaType)
                        .toArray(JavaType[]::new);
            }
            else
            {
                clazz = (Class<?>) type;
                javaTypes = new JavaType[0];
            }
            return typeFactory.constructParametricType(clazz, javaTypes);
        });
    }

    /**
     * 根据类型生成缓存的key
     *
     * @param types 类型列表
     * @return key
     */
    private String getCacheKey(Type... types)
    {
        return isEmpty(types) ? null
                : getMD5String(stream(types).map(Type::getTypeName)
                        .sorted()
                        .collect(joining("|")), false);
    }

}
