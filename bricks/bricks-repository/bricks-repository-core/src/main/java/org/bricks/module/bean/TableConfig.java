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

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.util.List;
import java.util.Map;

import org.bricks.bean.AbstractBean;
import org.bricks.module.enums.DataType;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 表配置
 *
 * @author fuzy
 *
 */
@Setter
@Getter
@Accessors(chain = true)
public class TableConfig extends AbstractBean {

    /**
     * 实体类
     */
    private Class<?> entityClass;

    /**
     * 读取起始行
     */
    private int startRow;

    /**
     * 读取当前行
     */
    private int currentRow;

    /**
     * Map&lt;列序号, 列配置&gt;
     */
    private Map<Integer, ColumnConfig> columnMap;

    /**
     * 字段标题表
     */
    private Map<String, String> fieldTitleMap;

    /**
     * 字段类型表
     */
    private Map<String, DataType> fieldDataTypeMap;

    /**
     * 字段列表
     */
    private List<String> fieldList;

    /**
     * 根据自选字段获取标题列表
     *
     * @param fields 字段列表
     * @return 标题列表
     */
    public List<String> getTitles(String... fields) {
        return getList(fieldTitleMap, fields);
    }

    /**
     * 获取自选字段列表
     *
     * @param fields 自选字段
     * @return 字段列表
     */
    public List<String> getFields(String... fields) {
        return isNotEmpty(fields) ? newArrayList(fields) : fieldList;
    }

    /**
     * 根据自选字段获取数据类型列表
     *
     * @param fields 自选字段
     * @return 数据类型列表
     */
    public List<DataType> getDataTypes(String... fields)
    {
        return getList(fieldDataTypeMap, fields);
    }

    private <T> List<T> getList(Map<String, T> map, String... fields) {
        List<T> list = newArrayList();
        if (isNotEmpty(fields)) {
            of(fields).forEach(field -> list.add(map.get(field)));
        } else {
            list.addAll(map.values());
        }
        return list;
    }

}
