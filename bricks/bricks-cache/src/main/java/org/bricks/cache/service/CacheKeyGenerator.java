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

package org.bricks.cache.service;

import static java.text.MessageFormat.format;
import static org.bricks.utils.MD5Utils.getMD5String;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.bricks.data.json.JacksonJsonService;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

/**
 * 缓存key生成器
 *
 * @author fuzy
 *
 */
@Component
public class CacheKeyGenerator implements KeyGenerator
{

    /**
     * json
     */
    @Resource
    private JacksonJsonService jacksonJsonService;

    @Override
    public Object generate(Object target, Method method, Object... params)
    {
        return format("{0}.{1}:{2}", target.getClass()
                .getSimpleName(), method.getName(), getMD5String(jacksonJsonService.output(params), false));
    }

}
