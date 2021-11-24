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

package org.bricks.module.service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.utils.ReflectionUtils.addDeclaredFields;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.bricks.module.annotation.Excel;
import org.bricks.module.annotation.ExcelColumn;
import org.bricks.module.annotation.Unique;
import org.bricks.module.bean.ColumnConfig;
import org.bricks.module.bean.TableConfig;
import org.bricks.module.enums.DataType;
import org.springframework.stereotype.Service;

/**
 * 加载表配置
 *
 * @author fuzy
 *
 */
@Service
public class TableConfigLoader
{

    /**
     * 缓存
     */
    private static final Map<String, TableConfig> CACHE = newHashMap();

    /**
     * 根据类加载表配置
     *
     * @param clazz 类
     * @return 表配置
     */
    public TableConfig load(Class<?> clazz)
    {
        if (CACHE.containsKey(clazz.getName()))
        {
            return CACHE.get(clazz.getName());
        }
        List<Field> fieldList = newArrayList();
        addDeclaredFields(clazz, fieldList, true, false);
        if (isEmpty(fieldList))
        {
            return null;
        }
        List<String> fList = newArrayList();
        Map<Integer, ColumnConfig> columnMap = newLinkedHashMap();
        Map<String, String> fieldTitleMap = newLinkedHashMap();
        Map<String, DataType> fieldDataTypeMap = newLinkedHashMap();
        TableConfig tableConfig = new TableConfig().setEntityClass(clazz)
                .setStartRow(1)
                .setColumnMap(columnMap)
                .setFieldList(fList)
                .setFieldTitleMap(fieldTitleMap)
                .setFieldDataTypeMap(fieldDataTypeMap);
        fieldList.stream()
                .filter(field -> field.getAnnotation(ExcelColumn.class) != null)
                .forEach(field ->
                {
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                    ColumnConfig cfg = new ColumnConfig();
                    if (excelColumn.column() > -1)
                    {
                        cfg.setColumn(excelColumn.column());
                        columnMap.put(cfg.getColumn(), cfg);
                    }
                    String name = field.getName();
                    cfg.setField(isNotBlank(excelColumn.field()) ? excelColumn.field() : name)
                            .setTitle(isNotBlank(excelColumn.title()) ? excelColumn.title() : name)
                            .setDataType(excelColumn.dataType())
                            .setFilterName(excelColumn.filterName())
                            .setFilterClass(excelColumn.filterClass())
                            .setMandatory(excelColumn.mandatory())
                            .setMaxLength(excelColumn.maxLength())
                            .setRegex(excelColumn.regex())
                            .setUnique(excelColumn.unique());
                    fList.add(cfg.getField());
                    fieldTitleMap.put(cfg.getField(), cfg.getTitle());
                    fieldDataTypeMap.put(cfg.getField(), cfg.getDataType());
                    if (cfg.isUnique())
                    {
                        tableConfig.setUnique(true);
                    }
                });
        ofNullable(clazz.getAnnotation(Excel.class)).ifPresent(excel -> tableConfig.setStartRow(excel.startRow())
                .setUniques(of(excel.uniques()).map(Unique::columns)
                        .filter(ArrayUtils::isNotEmpty)
                        .collect(toList()))
                .setFilterName(excel.filterName())
                .setFilterClass(excel.filterClass()));
        if (isNotEmpty(tableConfig.getUniques()))
        {
            tableConfig.setUnique(true);
        }
        CACHE.put(clazz.getName(), tableConfig);
        return tableConfig;
    }

}
