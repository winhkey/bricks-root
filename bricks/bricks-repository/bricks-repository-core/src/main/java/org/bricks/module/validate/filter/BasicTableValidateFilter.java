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

package org.bricks.module.validate.filter;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.of;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.module.constants.Constants.PoiConstants.EXCEL_ERR_UNIQUE;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.module.bean.ColumnConfig;
import org.bricks.module.bean.TableConfig;
import org.springframework.stereotype.Service;

/**
 * 基本表级校验
 *
 * @author fuzy
 */
@Service
public class BasicTableValidateFilter extends AbstractTableValidateFilter
{

    /**
     * 唯一性检查，唯一性字段全都相同返回false
     *
     * @param row 行号
     * @param index 索引
     * @param dataList 数据列表
     * @param config 表配置
     * @return 结果
     */
    @Override
    protected boolean validate(int row, int index, List<Map<Integer, String>> dataList, TableConfig config)
    {
        boolean rtn = rowValidate(row, index, dataList, config);
        boolean flag = rowsValidate(row, index, dataList, config);
        rtn = flag && rtn;
        if (!rtn)
        {
            addDuplicate(row, index);
        }
        return rtn;
    }

    @Override
    protected boolean rowValidate(int row, int index, List<Map<Integer, String>> dataList, TableConfig config)
    {
        boolean rtn = true;
        Map<Integer, String> dataMap1 = dataList.get(row);
        Map<Integer, String> dataMap2 = dataList.get(index);
        Map<Integer, ColumnConfig> columnConfig = config.getColumnMap();
        for (Entry<Integer, ColumnConfig> entry : columnConfig.entrySet())
        {
            if (entry.getValue()
                    .isUnique())
            {
                int column = entry.getKey();
                String value1 = dataMap1.get(column);
                String value2 = dataMap2.get(column);
                if (isNotBlank(value1) && value1.equals(value2))
                {
                    setErrorMessage(dataList.get(row), format(EXCEL_ERR_UNIQUE, index + 2, column + 1));
                    rtn = false;
                }
            }
        }
        return rtn;
    }

    private boolean rowsValidate(int row, int index, List<Map<Integer, String>> dataList, TableConfig config)
    {
        List<int[]> uniques = config.getUniques();
        if (isEmpty(uniques))
        {
            return true;
        }
        boolean rtn = true;
        Map<Integer, String> dataMap1 = dataList.get(row);
        Map<Integer, String> dataMap2 = dataList.get(index);
        for (int[] u : uniques)
        {
            if (isEmpty(u))
            {
                continue;
            }
            String value1 = of(u).mapToObj(dataMap1::get)
                    .collect(joining("\0"));
            String value2 = of(u).mapToObj(dataMap2::get)
                    .collect(joining("\0"));
            if (isNotBlank(value1) && value1.equals(value2))
            {
                setErrorMessage(dataList.get(row),
                        format(EXCEL_ERR_UNIQUE, index + 2,
                                u.length == 1 ? String.valueOf(u[0])
                                        : Arrays.toString(of(u).map(i -> i + 1)
                                                .toArray())));
                rtn = false;
            }
        }
        return rtn;
    }

}
