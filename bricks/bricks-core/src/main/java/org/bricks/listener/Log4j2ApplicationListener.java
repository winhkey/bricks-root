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

package org.bricks.listener;

import static java.text.MessageFormat.format;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * 日志监听器
 *
 * @author fuzy
 */
public class Log4j2ApplicationListener implements GenericApplicationListener {

    /**
     * 排序
     */
    public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

    /**
     * 事件类型数组
     */
    private static final Class<?>[] EVENT_TYPES = {ApplicationStartingEvent.class, ApplicationEnvironmentPreparedEvent.class, ApplicationPreparedEvent.class, ContextClosedEvent.class, ApplicationFailedEvent.class};

    /**
     * 源类型数组
     */
    private static final Class<?>[] SOURCE_TYPES = {SpringApplication.class, ApplicationContext.class};

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            onApplicationEvent((ApplicationEnvironmentPreparedEvent) event);
        }
    }

    private void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        String path = format("{0}/{1}/{2}", environment.getProperty("spring.application.name"), getIp(environment), environment.getProperty("server.port"));
        MDC.put("app", path);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        return of(supportedTypes).anyMatch(supportedType -> supportedType.isAssignableFrom(type));
    }

    private String getIp(Environment environment) {
        String ip = environment.getProperty("eureka.local");
        if (isBlank(ip)) {
            try {
                InetAddress address = InetAddress.getLocalHost();
                ip = address.getHostAddress();
            } catch (UnknownHostException e) {
                ip = "";
            }
        }
        return ip;
    }

}
