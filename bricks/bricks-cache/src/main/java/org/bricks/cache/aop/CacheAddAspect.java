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

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.bricks.aop.AbstractAspect;
import org.springframework.stereotype.Component;

/**
 * 缓存切面
 *
 * @author fuzy
 *
 */
@Aspect
@Component
public class CacheAddAspect extends AbstractAspect
{

    /**
     * 设置缓存环绕
     *
     * @param cacheHandler 缓存环绕
     */
    @Resource
    public void setAroundHandler(CacheHandler cacheHandler)
    {
        aroundHandler = cacheHandler;
    }

    @Override
    @Around("@annotation(com.wgc.bricks.cache.annotation.CacheAdd)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable
    {
        return super.around(pjp);
    }

}
