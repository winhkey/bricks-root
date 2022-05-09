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

package org.bricks.bean;

import org.bricks.enums.MessageEnum;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 返回格式
 *
 * @author fuzy
 *
 * @param <D> 数据
 */
@Setter
@Getter
@Accessors(chain = true)
public class ResponseWrapper<D> extends AbstractBean
{

    /**
     * 返回码
     */
    private String code;

    /**
     * 描述信息
     */
    @JsonAlias({"message", "msg"})
    private String message;

    /**
     * 业务数据
     */
    private D data;

    /**
     * 列表总数
     */
    private Long count;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 默认构造方法
     */
    public ResponseWrapper()
    {
        super();
    }

    /**
     * 构造方法
     *
     * @param code 返回码
     * @param message 返回描述
     */
    public ResponseWrapper(String code, String message)
    {
        this();
        this.code = code;
        this.message = message;
    }

    /**
     * @return message
     */
    public String getMsg()
    {
        return message;
    }

    /**
     * message
     *
     * @param message message
     */
    public void setMsg(String message)
    {
        this.message = message;
    }

    /**
     * 包含数据的成功结果
     *
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> ok()
    {
        return build("200", "success", true, null);
    }

    /**
     * 包含数据的成功结果
     *
     * @param data 数据
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> ok(T data)
    {
        return build("200", "success", true, data);
    }

    /**
     * 包含数据的错误结果
     *
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> error()
    {
        return error("-1", "error");
    }

    /**
     * 包含数据的错误结果
     * 
     * @param <T> 数据类型
     * @param code 返回码
     * @param message 返回消息
     * @return 结果
     */
    public static <T> ResponseWrapper<T> error(String code, String message)
    {
        return build(code, message, false, null);
    }

    /**
     * 构建
     *
     * @param code 返回码
     * @param message 返回消息
     * @param success 是否成功
     * @param data 数据
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> build(String code, String message, Boolean success, T data)
    {
        return new ResponseWrapper<T>(code, message).setSuccess(success)
                .setData(data);
    }

    /**
     * 构建
     *
     * @param messageEnum 消息枚举
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> build(MessageEnum messageEnum)
    {
        return build(messageEnum.getCode(), messageEnum.getMessage(), false, null);
    }

}
