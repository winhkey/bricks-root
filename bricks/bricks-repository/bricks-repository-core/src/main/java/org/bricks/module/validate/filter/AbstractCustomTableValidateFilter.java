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

import java.util.List;
import java.util.Map;

import org.bricks.module.bean.TableConfig;

/**
 * 自定义表级过滤器
 *
 * @author fuzy
 *
 */
public abstract class AbstractCustomTableValidateFilter extends AbstractTableValidateFilter
{

    @Override
    protected boolean rowValidate(int row, int index, List<Map<Integer, String>> dataList, TableConfig config)
    {
        return rowValidate(row, index, dataList);
    }

    /**
     * 验证
     *
     * @param row 行
     * @param index 对比行
     * @param dataList 数据
     * @return 结果
     */
    protected abstract boolean rowValidate(int row, int index, List<Map<Integer, String>> dataList);

}
