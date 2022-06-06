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

package org.bricks.cache.bean;

import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;

import java.util.Optional;

/**
 * 缓存key
 *
 * @author fuzy
 *
 */
public class CacheKey
{

    /**
     * 类型
     */
    private final Class<?> clazz;

    /**
     * 方法名
     */
    private final String method;

    /**
     * 参数列表
     */
    private final Object[] args;

    /**
     * hash code
     */
    private final int hash;

    /**
     * 构造方法
     *
     * @param clazz 类
     * @param method 方法
     * @param args 参数
     */
    public CacheKey(Class<?> clazz, String method, Object... args)
    {
        this.clazz = clazz;
        this.method = method;
        this.args = Optional.ofNullable(args)
            .map(array ->
            {
                Object[] arr = new Object[args.length];
                System.arraycopy(args, 0, arr, 0, args.length);
                return arr;
            })
            .orElse(null);
        int code = deepHashCode(args);
        code = 31 * code + clazz.hashCode();
        code = 31 * code + method.hashCode();
        hash = code;
    }

    @Override
    public int hashCode()
    {
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof CacheKey))
        {
            return false;
        }
        CacheKey other = (CacheKey) obj;
        if (hash != other.hash)
        {
            return false;
        }
        return clazz.equals(other.clazz) && method.equals(other.method) && deepEquals(args, other.args);
    }

}
