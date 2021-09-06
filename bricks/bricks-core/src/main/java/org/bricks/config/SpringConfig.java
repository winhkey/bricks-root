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
import static javax.validation.Validation.buildDefaultValidatorFactory;

import javax.annotation.PostConstruct;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import org.bricks.factory.YamlPropertySourceFactory;

import brave.sampler.Sampler;

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
    public ApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor executor)
    {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(executor);
        return eventMulticaster;
    }

}
