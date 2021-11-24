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

import static java.util.Optional.ofNullable;

import java.util.Map;

import org.bricks.module.bean.ColumnConfig;
import org.springframework.stereotype.Service;

/**
 * 自定义行级过滤器
 * 
 * @author fuzy
 *
 */
public abstract class AbstractCustomRowValidateFilter extends AbstractRowValidateFilter
{

    @Override
    protected boolean validate(int row, int column, ColumnConfig columnConfig, Map<Integer, String> dataMap,
            Class<?> entityClass)
    {
        return ofNullable(getClass().getAnnotation(Service.class)).filter(service -> columnConfig.getFilterName()
                .contains(service.value()))
                .map(service -> validate(row, column, dataMap))
                .orElse(true);
    }

    /**
     * 验证
     * 
     * @param row 行
     * @param column 列
     * @param dataMap 数据
     * @return 结果
     */
    protected abstract boolean validate(int row, int column, Map<Integer, String> dataMap);

}
