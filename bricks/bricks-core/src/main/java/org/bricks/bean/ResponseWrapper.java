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

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 返回格式
 *
 * @author fuzy
 *
 * @param <T> 数据
 *
 */
@Setter
@Getter
@Accessors(chain = true)
public class ResponseWrapper<T> extends AbstractBean {

    /**
     * 返回码
     */
    private String code;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 业务数据
     */
    private T data;

    /**
     * 列表总数
     */
    private Long count;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 默认构造方法
     */
    public ResponseWrapper() {
        super();
    }

    /**
     * 构造方法
     *
     * @param code 返回码
     * @param message 返回描述
     */
    public ResponseWrapper(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    /**
     * @return message
     */
    @JsonProperty("msg")
    public String getMsg() {
        return message;
    }

    /**
     * message
     *
     * @param message message
     */
    @JsonProperty("msg")
    public void setMsg(String message) {
        this.message = message;
    }

    /**
     * @return 无数据的成功结果
     */
    public static ResponseWrapper<Void> ok() {
        return ok(Void.class);
    }

    /**
     * 包含数据的成功结果
     *
     * @param clazz 数据类型
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> ok(Class<T> clazz) {
        return build("200", "success", true, clazz);
    }

    /**
     * @return 无数据的错误结果
     */
    public static ResponseWrapper<Void> error() {
        return error(Void.class);
    }

    /**
     * 包含数据的错误结果
     *
     * @param clazz 数据类型
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> error(Class<T> clazz) {
        return build("-1", "error", false, clazz);
    }

    /**
     * 构建
     *
     * @param code 返回码
     * @param message 返回消息
     * @param success 是否成功
     * @return 结果
     */
    public static ResponseWrapper<Void> build(String code, String message, Boolean success) {
        return build(code, message, success, Void.class);
    }

    /**
     * 构建
     *
     * @param code 返回码
     * @param message 返回消息
     * @param success 是否成功
     * @param clazz 数据类型
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> build(String code, String message, Boolean success, Class<T> clazz) {
        return new ResponseWrapper<T>(code, message).setSuccess(success);
    }

    /**
     * 构建
     *
     * @param messageEnum 消息枚举
     * @return 结果
     */
    public static ResponseWrapper<Void> build(MessageEnum messageEnum) {
        return build(messageEnum, Void.class);
    }

    /**
     * 构建
     *
     * @param messageEnum 消息枚举
     * @param clazz 数据类型
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> ResponseWrapper<T> build(MessageEnum messageEnum, Class<T> clazz) {
        return build(messageEnum.getCode(), messageEnum.getMessage(), false, clazz);
    }

}
