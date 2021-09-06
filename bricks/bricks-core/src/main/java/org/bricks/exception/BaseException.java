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

package org.bricks.exception;

import static java.text.MessageFormat.format;

import org.bricks.enums.MessageEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 封装基础异常
 *
 * @author fuzy
 */
@Setter
@Getter
@Accessors(chain = true)
public class BaseException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 数据
     */
    private Object data;

    /**
     * 默认构造
     */
    public BaseException()
    {
        super();
    }

    /**
     * 构造方法
     *
     * @param message 描述
     */
    public BaseException(String message)
    {
        super(message);
    }

    /**
     * 构造方法
     *
     * @param errorCode 错误码
     * @param pattern 描述
     * @param args 参数
     */
    public BaseException(String errorCode, String pattern, Object... args)
    {
        super(format(pattern, args));
        this.errorCode = errorCode;
    }

    /**
     * 构造方法
     *
     * @param t 异常
     * @param errorCode 错误码
     * @param pattern 描述
     * @param args 参数
     */
    public BaseException(Throwable t, String errorCode, String pattern, Object... args)
    {
        super(format(pattern, args), t);
        this.errorCode = errorCode;
    }

    /**
     * 构造方法
     *
     * @param t 异常
     */
    public BaseException(Throwable t)
    {
        super(t);
    }

    /**
     * 构造方法
     *
     * @param message 描述
     * @param t 异常
     */
    public BaseException(String message, Throwable t)
    {
        super(message, t);
    }

    /**
     * 构造方法
     * 
     * @param messageEnum 消息枚举
     */
    public BaseException(MessageEnum messageEnum)
    {
        this(messageEnum.getCode(), messageEnum.getMessage());
    }

    /**
     * 构造方法
     * 
     * @param messageEnum 消息枚举
     * @param t 异常
     */
    public BaseException(MessageEnum messageEnum, Throwable t)
    {
        this(t, messageEnum.getCode(), messageEnum.getMessage());
    }

}
