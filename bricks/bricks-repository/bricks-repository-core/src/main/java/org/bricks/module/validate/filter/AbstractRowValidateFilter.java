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

import static org.apache.commons.collections4.MapUtils.isNotEmpty;

import java.util.Map;
import java.util.Map.Entry;

import org.bricks.module.bean.ColumnConfig;
import org.bricks.module.bean.TableConfig;

/**
 * 抽象行级校验过滤器
 *
 * @author fuzy
 */
public abstract class AbstractRowValidateFilter extends AbstractValidateFilter<Map<Integer, String>>
        implements RowValidateFilter
{

    @Override
    public boolean validate(Map<Integer, String> dataMap, TableConfig config)
    {
        boolean result = true;
        if (isNotEmpty(dataMap))
        {
            for (Entry<Integer, ColumnConfig> entry : config.getColumnMap()
                    .entrySet())
            {
                boolean flag = validate(config.getCurrentRow(), entry.getKey(), entry.getValue(), dataMap,
                        config.getEntityClass());
                result = flag && result;
            }
        }
        return result;
    }

    /**
     * 验证单列数据
     *
     * @param row 行序号
     * @param column 列序号
     * @param columnConfig 列配置
     * @param dataMap 行数据
     * @param entityClass 实体类
     * @return 结果
     */
    protected abstract boolean validate(int row, int column, ColumnConfig columnConfig, Map<Integer, String> dataMap,
            Class<?> entityClass);

}
