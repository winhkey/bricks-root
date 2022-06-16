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

package org.bricks.module.bean;

import org.bricks.bean.AbstractBean;
import org.bricks.module.enums.DataType;
import org.bricks.module.validate.filter.RowValidateFilter;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 列配置
 *
 * @author fuzy
 */
@Setter
@Getter
@Accessors(chain = true)
public class ColumnConfig extends AbstractBean
{

    /**
     * 列序号
     */
    private Integer column;

    /**
     * 标题
     */
    private String title;

    /**
     * 字段
     */
    private String field;

    /**
     * 字段类型
     */
    private DataType dataType;

    /**
     * 格式化
     */
    private String format;

    /**
     * 必填
     */
    private boolean mandatory;

    /**
     * 唯一
     */
    private boolean unique;

    /**
     * 最长
     */
    private int maxLength;

    /**
     * 校验
     */
    private String regex;

    /**
     * 自定义过滤器
     */
    private String filterName;

    /**
     * 自定义过滤器
     */
    private Class<? extends RowValidateFilter> filterClass;

}
