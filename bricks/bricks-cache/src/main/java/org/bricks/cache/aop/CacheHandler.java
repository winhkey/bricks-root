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

package org.bricks.cache.aop;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.enums.Default.NULL;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.bricks.aop.AfterReturningHandler;
import org.bricks.aop.AroundHandler;
import org.bricks.cache.annotation.CacheAdd;
import org.bricks.cache.annotation.CacheClear;
import org.bricks.cache.annotation.CacheRequired;
import org.bricks.cache.enums.CacheType;
import org.bricks.cache.service.CacheService;
import org.bricks.utils.FunctionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

/**
 * 读取缓存
 *
 * @author fuzy
 *
 */
@Component
public class CacheHandler implements AroundHandler, AfterReturningHandler
{

    /**
     * 本地缓存
     */
    @Resource(name = "caffeineService")
    protected CacheService caffeineService;

    /**
     * 二级缓存
     */
    @Qualifier("redisService")
    @Autowired(required = false)
    protected CacheService redisService;

    /**
     * 键生成器
     */
    @Resource
    protected KeyGenerator keyGenerator;

    @Override
    public Object around(ProceedingJoinPoint pjp)
    {
        Object target = pjp.getTarget();
        Class<?> clazz = target.getClass();
        String classCacheName = clazz.getSimpleName();
        CacheRequired cacheRequired = clazz.getAnnotation(CacheRequired.class);
        CacheType type = cacheRequired.type();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        CacheAdd cacheAdd = method.getAnnotation(CacheAdd.class);
        String name = ofNullable(cacheAdd).filter(c -> isNotBlank(c.value()))
                .map(CacheAdd::value)
                .orElse(classCacheName);
        String key = ofNullable(cacheAdd).filter(c -> isNotBlank(c.key()))
                .map(CacheAdd::key)
                .orElseGet(() -> (String) keyGenerator.generate(target, method, args));
        Object rtn = ofNullable(cacheAdd).map(c -> get(type, name, key))
                .orElseGet(FunctionUtils.get(() ->
                {
                    Object proceed = pjp.proceed();
                    put(type, name, key, ofNullable(proceed).orElse(NULL));
                    return proceed;
                }, null, null, null));
        return NULL.equals(rtn) || NULL.name()
                .equals(rtn) ? null : rtn;
    }

    @Override
    public void afterReturning(JoinPoint joinPoint, Object result)
    {
        Object target = joinPoint.getTarget();
        Class<?> clazz = target.getClass();
        CacheRequired cacheRequired = clazz.getAnnotation(CacheRequired.class);
        CacheType type = cacheRequired.type();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheClear cacheClear = method.getAnnotation(CacheClear.class);
        String[] names = ofNullable(cacheClear).map(CacheClear::value)
                .orElse(null);
        clear(type, clazz.getSimpleName(), names);
    }

    private Object get(CacheType type, String name, String key)
    {
        Object proceed = null;
        switch (type)
        {
            case LOCAL:
                proceed = caffeineService.get(name, key);
                break;
            case BOTH:
                proceed = caffeineService.get(name, key); // fall through
            case REDIS:
                if (proceed == null && redisService != null)
                {
                    proceed = redisService.get(name, key);
                    if (proceed != null)
                    {
                        caffeineService.put(name, key, proceed);
                        redisService.sync(name, key, proceed);
                    }
                }
                break;
            default:
                break;
        }
        return proceed;
    }

    private void put(CacheType type, String name, String key, Object value)
    {
        switch (type)
        {
            case LOCAL:
                caffeineService.put(name, key, value);
                break;
            case BOTH:
                caffeineService.put(name, key, value); // fall through
            case REDIS:
                if (redisService != null)
                {
                    redisService.put(name, key, value);
                    redisService.sync(name, key, value);
                }
                break;
            default:
                break;
        }
    }

    private void clear(CacheType type, String classCacheName, String[] names)
    {
        switch (type)
        {
            case LOCAL:
                clear(caffeineService, classCacheName, names, false);
                break;
            case BOTH:
                if (redisService != null)
                {
                    clear(redisService, classCacheName, names, true);
                }
                clear(caffeineService, classCacheName, names, false);
                break;
            case REDIS:
                if (redisService != null)
                {
                    clear(redisService, classCacheName, names, true);
                }
                break;
            default:
                break;
        }
    }

    private void clear(CacheService cacheService, String className, String[] names, boolean sync)
    {
        cacheService.clear(className, null);
        if (sync)
        {
            cacheService.sync(className, null, null);
        }
        if (isNotEmpty(names))
        {
            stream(names).filter(StringUtils::isNotBlank)
                    .forEach(n ->
                    {
                        cacheService.clear(n, null);
                        if (sync)
                        {
                            cacheService.sync(n, null, null);
                        }
                    });
        }
    }

}
