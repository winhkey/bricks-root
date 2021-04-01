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

package org.bricks.data.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import org.bricks.annotation.NoLog;
import org.bricks.data.utils.JacksonUtils;
import org.bricks.exception.BaseException;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jackson实现抽象类
 *
 * @author fuzy
 *
 */
public abstract class AbstractJacksonService extends AbstractJsonDataService
{

    /**
     * 转换器
     */
    protected ObjectMapper objectMapper;

    @NoLog
    public ObjectMapper getObjectMapper()
    {
        return objectMapper;
    }

    @Override
    protected <T> T readFrom(String json, Type... type)
    {
        try
        {
            return objectMapper.readValue(trim(json), getJavaType(type));
        }
        catch (IOException e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected <T> T readFrom(File file, Type... type)
    {
        try
        {
            return objectMapper.readValue(file, getJavaType(type));
        }
        catch (IOException e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected <T> T readFrom(InputStream stream, Type... type)
    {
        try
        {
            return objectMapper.readValue(stream, getJavaType(type));
        }
        catch (IOException e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected <T> T convertFrom(Object object, Type... type)
    {
        return objectMapper.convertValue(object, getJavaType(type));
    }

    @Override
    protected String toString(Object object, Type... type)
    {
        try
        {
            return objectMapper.writeValueAsString(object);
        }
        catch (IOException e)
        {
            throw new BaseException(e);
        }
    }

    /**
     * java类型转JavaType
     *
     * @param types java类型
     * @return JavaType
     */
    protected JavaType getJavaType(Type... types)
    {
        return JacksonUtils.getJavaType(types[0]);
    }

}
