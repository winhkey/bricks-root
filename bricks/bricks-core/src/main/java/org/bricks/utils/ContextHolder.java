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

import static java.util.Optional.ofNullable;
import static org.bricks.utils.FunctionUtils.apply;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * spring环境
 *
 * @author fuzy
 *
 */
@Component
public class ContextHolder implements ApplicationContextAware
{

    /**
     * ApplicationContext
     */
    private static ApplicationContext context;

    /**
     * Environment
     */
    private static Environment environment;

    /**
     * 根据id获取bean
     *
     * @param beanId bean id
     * @return bean
     */
    public static Object getBean(String beanId)
    {
        return ofNullable(context).map(ctx -> ctx.getBean(beanId))
                .orElse(null);
    }

    /**
     * 根据类型获取bean
     *
     * @param <T> 类型
     * @param clazz 类型
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz)
    {
        return ofNullable(context).map(ctx -> ctx.getBean(clazz))
                .orElse(null);
    }

    /**
     * 根据id获取bean
     *
     * @param beanId bean id
     * @param clazz 类型
     * @param <T> 类型
     * @return bean
     */
    public static <T> T getBean(String beanId, Class<T> clazz)
    {
        return ofNullable(context).map(apply(ctx -> ctx.getBean(beanId, clazz), null, null, null))
                .orElse(null);
    }

    /**
     * 注入applicationContext
     *
     * @param applicationContext spring环境
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext)
    {
        setContext(applicationContext);
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext spring环境
     */
    public static ApplicationContext getContext()
    {
        return context;
    }

    /**
     * 注入applicationContext
     *
     * @param applicationContext spring环境
     */
    public static void setContext(ApplicationContext applicationContext)
    {
        context = applicationContext;
        environment = applicationContext.getEnvironment();
    }

    /**
     * @return 环境
     */
    public static Environment getEnvironment()
    {
        return environment;
    }

    /**
     * 读取资源文件
     *
     * @param key key
     * @param locale 语言
     * @return value
     */
    public static String getMessage(String key, Locale locale)
    {
        return context.getMessage(key, null, null, locale);
    }

    /**
     * 读取资源文件
     *
     * @param key key
     * @param args 参数
     * @param defaultMessage 默认值
     * @param locale 语言
     * @return value
     */
    public static String getMessage(String key, Object[] args, String defaultMessage, Locale locale)
    {
        return context.getMessage(key, args, defaultMessage, locale);
    }

    /**
     * @return 是否windows系统
     */
    public static boolean isWindows()
    {
        return ofNullable(environment.getProperty("os.name")).map(osName -> osName.contains("Windows"))
                .orElse(false);
    }

    /**
     * @return 是否unix系统
     */
    public static boolean isUnix()
    {
        return ofNullable(environment.getProperty("os.name"))
                .map(osName -> osName.contains("unix") || osName.contains("linux") || osName.contains("Mac OS X"))
                .orElse(false);
    }

}
