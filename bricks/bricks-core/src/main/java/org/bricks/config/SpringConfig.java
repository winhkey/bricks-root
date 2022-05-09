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

package org.bricks.config;

import static java.util.TimeZone.getTimeZone;
import static java.util.TimeZone.setDefault;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.bricks.utils.FunctionUtils.apply;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.validation.Validator;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;

import org.bricks.factory.YamlPropertySourceFactory;

import brave.sampler.Sampler;
import lombok.SneakyThrows;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

/**
 * spring配置
 * 
 * @author fuzy
 * 
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:application-core.yml")
public class SpringConfig
{

    /**
     * 时区
     */
    @Value("${bricks.timezone:Asia/Shanghai}")
    private String timezone;

    /**
     * 初始化
     */
    @PostConstruct
    public void init()
    {
        setDefault(getTimeZone(timezone));
    }

/**
     * @return 系统信息
     */
    @Bean
    public SystemInfo systemInfo()
    {
        return new SystemInfo();
    }

    /**
     * @return 硬件
     */
    @Bean
    public HardwareAbstractionLayer hardware()
    {
        return systemInfo().getHardware();
    }

    /**
     * @return 操作系统
     */
    @Bean
    public OperatingSystem operatingSystem()
    {
        return systemInfo().getOperatingSystem();
    }

    /**
     * @return 校验器
     */
    @Bean
    public Validator validator()
    {
        return buildDefaultValidatorFactory().getValidator();
    }

    /**
     * @return 日志采样器
     */
    @Bean
    public Sampler mySampler()
    {
        return Sampler.ALWAYS_SAMPLE;
    }

    /**
     * 异步发布事件
     *
     * @param executor 线程池
     * @return 异步
     */
    @Bean
    @ConditionalOnProperty(name = "bricks.event.async", havingValue = "true")
    public ApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor executor)
    {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(executor);
        return eventMulticaster;
    }

    /**
     * @return tika
     */
    @Bean
    public Tika tika()
    {
        return new Tika();
    }

    /**
     * 构造rest template
     * 
     * @param builder 构造器
     * @return rest template
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder)
    {
        return builder.build();
    }

    /**
     * @return AntPathMatcher
     */
    @Bean
    public AntPathMatcher antPathMatcher()
    {
        return new AntPathMatcher();
    }

    /**
     * @return 国际化
     */
    @Bean
    @SneakyThrows
    public Map<String, MessageSourceAccessor> messageSourceAccessorMap()
    {
        return of(new PathMatchingResourcePatternResolver().getResources("classpath*:i18n/*.properties")).map(apply(r ->
        {
            String url = r.getURL()
                    .toExternalForm();
            return url.substring(0, url.lastIndexOf('.'));
        }, null, null, null, null))
                .collect(groupingBy(n -> n.contains("_") ? n.substring(n.indexOf('_') + 1) : "default"))
                .entrySet()
                .stream()
                .collect(toMap(Entry::getKey, o ->
                {
                    ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
                    source.setBasenames(o.getValue()
                            .toArray(new String[0]));
                    source.setDefaultEncoding("UTF-8");
                    return new MessageSourceAccessor(source);
                }));
    }

}
