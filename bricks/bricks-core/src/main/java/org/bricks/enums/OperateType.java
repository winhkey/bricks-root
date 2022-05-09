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

package org.bricks.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型
 *
 * @author fuzy
 *
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OperateType implements ValueEnum<String>
{

    /**
     * 查询
     */
    QUERY("查询"),

    /**
     * 新增
     */
    INSERT("新增"),

    /**
     * 修改
     */
    UPDATE("修改"),

    /**
     * 删除
     */
    DELETE("删除"),

    /**
     * 授权
     */
    GRANT("授权"),

    /**
     * 导出
     */
    EXPORT("导出"),

    /**
     * 导入
     */
    IMPORT("导入"),

    /**
     * 清空数据
     */
    CLEAN("清空"),

    /**
     * 生成代码
     */
    CODE("生成代码");

    /**
     * 名称
     */
    private final String value;

}
