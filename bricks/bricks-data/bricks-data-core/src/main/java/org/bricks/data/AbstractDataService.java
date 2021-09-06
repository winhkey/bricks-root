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

package org.bricks.data;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.utils.MD5Utils.getMD5String;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.bricks.annotation.NoLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串转对象接口抽象类
 *
 * @author fuzy
 *
 */
public abstract class AbstractDataService implements DataService
{

    /**
     * 日志
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @NoLog
    @Override
    public <T> T input(String content, Type... type)
    {
        if (isNotBlank(content) && type != null)
        {
            try
            {
                return readFrom(simplify(content), type);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    @NoLog
    @Override
    public <T> T input(File file, Type... type)
    {
        if (file != null && file.exists() && file.isFile() && type != null)
        {
            try
            {
                return readFrom(file, type);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    @NoLog
    @Override
    public <T> T input(InputStream stream, Type... type)
    {
        if (stream != null && type != null)
        {
            try
            {
                return readFrom(stream, type);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    @NoLog
    @Override
    public String output(Object object, Type... type)
    {
        try
        {
            return toString(object, type);
        }
        catch (Throwable e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @NoLog
    @Override
    public <T> T convert(Object object, Type... type)
    {
        if (object != null)
        {
            try
            {
                return convertFrom(object, type);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public String simplify(String content)
    {
        return content;
    }

    /**
     * 根据类型生成缓存的key
     *
     * @param types 类型列表
     * @return key
     */
    protected String getCacheKey(Type... types)
    {
        return isEmpty(types) ? null
                : getMD5String(stream(types).map(Type::getTypeName)
                        .sorted()
                        .collect(joining("|")), false);
    }

    /**
     * 字符串转对象
     *
     * @param content 字符串
     * @param type 类型
     * @param <T> 类型
     * @return 对象
     */
    protected abstract <T> T readFrom(String content, Type... type);

    /**
     * 文件转对象
     *
     * @param file 文件
     * @param type 类型
     * @param <T> 类型
     * @return 对象
     */
    protected abstract <T> T readFrom(File file, Type... type);

    /**
     * 流转对象
     *
     * @param stream 流
     * @param type 类型
     * @param <T> 类型
     * @return 对象
     */
    protected abstract <T> T readFrom(InputStream stream, Type... type);

    /**
     * 未知json转对象
     *
     * @param object 未知json
     * @param type 类型
     * @param <T> 类型
     * @return 对象
     */
    protected abstract <T> T convertFrom(Object object, Type... type);

    /**
     * 对象转字符串
     *
     * @param object 对象
     * @param type 类型
     * @return 字符串
     */
    protected abstract String toString(Object object, Type... type);

}
