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

import java.io.IOException;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import lombok.experimental.UtilityClass;

/**
 * 国际化工具类
 *
 * @author fuzy
 *
 */
@UtilityClass
public class I18nUtils
{

    /**
     * 资源访问
     */
    private static MessageSourceAccessor accessor;

    /**
     * 国际化资源文件路径
     */
    private static final String PATH_PARENT = "classpath:i18n/";

    /**
     * 国际化资源文件后缀
     */
    private static final String SUFFIX = ".properties";

    /**
     * 路径规则解析
     */
    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    /**
     * 初始化资源文件的存储器 加载指定语言配置文件
     *
     * @param language 语言类型(文件名即为语言类型,eg: en_us 表明使用 美式英文 语言配置)
     * @throws IOException io异常
     */
    private static void initMessageSourceAccessor(String language) throws IOException
    {
        Resource resource = RESOURCE_PATTERN_RESOLVER.getResource(PATH_PARENT + language + SUFFIX);
        String fileName = resource.getURL()
                .toExternalForm();
        int lastIndex = fileName.lastIndexOf('.');
        String baseName = fileName.substring(0, lastIndex);
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename(baseName);
        reloadableResourceBundleMessageSource.setCacheSeconds(5);
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        accessor = new MessageSourceAccessor(reloadableResourceBundleMessageSource);
    }

    /**
     * 获取一条语言配置信息
     *
     * @param language 语言类型,zh_cn: 简体中文, en_us: 英文
     * @param message 配置信息属性名,eg: api.response.code.user.signUp
     * @param defaultMessage 默认信息,当无法从配置文件中读取到对应的配置信息时返回该信息
     * @return 配置信息
     * @throws IOException IOException
     */
    public static String getMessage(String language, String message, String defaultMessage) throws IOException
    {
        initMessageSourceAccessor(language);
        return accessor.getMessage(message, defaultMessage, LocaleContextHolder.getLocale());
    }

}
