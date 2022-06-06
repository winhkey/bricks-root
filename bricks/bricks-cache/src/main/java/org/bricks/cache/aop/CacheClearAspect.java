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

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.bricks.aop.AbstractAspect;
import org.springframework.stereotype.Component;

/**
 * 清理缓存
 *
 * @author fuzy
 *
 */
@Aspect
@Component
public class CacheClearAspect extends AbstractAspect
{

    /**
     * 设置缓存后置
     * @param cacheHandler 缓存后置
     */
    @Resource
    public void setAfterReturningHandler(CacheHandler cacheHandler)
    {
        afterReturningHandler = cacheHandler;
    }

    @Override
    @AfterReturning("@annotation(com.wgc.bricks.cache.annotation.CacheClear)")
    public void afterReturning(JoinPoint pjp)
    {
        super.afterReturning(pjp);
    }

}
