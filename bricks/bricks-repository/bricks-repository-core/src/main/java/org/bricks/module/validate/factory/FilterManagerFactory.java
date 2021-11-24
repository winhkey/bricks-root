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

package org.bricks.module.validate.factory;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.utils.ContextHolder.getBean;

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.bricks.module.annotation.Excel;
import org.bricks.module.annotation.ExcelColumn;
import org.bricks.module.bean.ColumnConfig;
import org.bricks.module.bean.TableConfig;
import org.bricks.module.validate.filter.BasicRowValidateFilter;
import org.bricks.module.validate.filter.BasicTableValidateFilter;
import org.bricks.module.validate.filter.RowValidateFilter;
import org.bricks.module.validate.filter.TableValidateFilter;
import org.bricks.module.validate.filter.ValidateFilter;
import org.bricks.module.validate.manager.ValidateFilterManager;
import org.springframework.stereotype.Component;

/**
 * 过滤管理器工厂
 *
 * @author fuzy
 *
 */
@Component
public class FilterManagerFactory
{

    /**
     * 基本行过滤
     */
    @Resource
    private BasicRowValidateFilter basicRowValidateFilter;

    /**
     * 基本表过滤
     */
    @Resource
    private BasicTableValidateFilter basicTableValidateFilter;

    /**
     * 构造过滤管理器
     *
     * @param config 表配置
     * @return 管理器
     */
    public ValidateFilterManager build(TableConfig config)
    {
        Set<RowValidateFilter> rowFilterSet = newLinkedHashSet();
        rowFilterSet.add(basicRowValidateFilter);
        Set<TableValidateFilter> tableFilterSet = newLinkedHashSet();
        if (config.isUnique())
        {
            tableFilterSet.add(basicTableValidateFilter);
        }
        String filterName = config.getFilterName();
        if (isNotBlank(filterName))
        {
            TableValidateFilter validateFilter = getBean(filterName, TableValidateFilter.class);
            addFilter(rowFilterSet, tableFilterSet, validateFilter);
        }
        Class<? extends TableValidateFilter> tableFilterClass = config.getFilterClass();
        if (tableFilterClass != null && tableFilterClass != Excel.UselessTableFilter.class)
        {
            TableValidateFilter validateFilter = getBean(tableFilterClass);
            addFilter(rowFilterSet, tableFilterSet, validateFilter);
        }
        for (Entry<Integer, ColumnConfig> entry : config.getColumnMap()
                .entrySet())
        {
            ColumnConfig cfg = entry.getValue();
            filterName = cfg.getFilterName();
            if (isNotBlank(filterName))
            {
                RowValidateFilter validateFilter = getBean(filterName, RowValidateFilter.class);
                addFilter(rowFilterSet, tableFilterSet, validateFilter);
            }
            Class<? extends RowValidateFilter> rowFilterClass = cfg.getFilterClass();
            if (rowFilterClass != null && rowFilterClass != ExcelColumn.UselessRowFilter.class)
            {
                RowValidateFilter validateFilter = getBean(rowFilterClass);
                addFilter(rowFilterSet, tableFilterSet, validateFilter);
            }
        }
        ValidateFilterManager filterManager = getBean(ValidateFilterManager.class);
        filterManager.addRowFilters(rowFilterSet);
        filterManager.addTableFilters(tableFilterSet);
        return filterManager;
    }

    private void addFilter(Set<RowValidateFilter> rowFilterSet, Set<TableValidateFilter> tableFilterSet,
            ValidateFilter<?> validateFilter)
    {
        if (validateFilter instanceof RowValidateFilter)
        {
            rowFilterSet.add((RowValidateFilter) validateFilter);
        }
        else if (validateFilter instanceof TableValidateFilter)
        {
            tableFilterSet.add((TableValidateFilter) validateFilter);
        }
    }

}
