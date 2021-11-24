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

package org.bricks.module.validate.manager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bricks.module.validate.filter.RowValidateFilter;
import org.bricks.module.validate.filter.TableValidateFilter;

/**
 * 验证过滤管理器
 *
 * @author fuzy
 *
 */
public interface ValidateFilterManager extends ValidateManager<List<Map<Integer, String>>>
{

    /**
     * 增加行级验证过滤器
     *
     * @param filters 过滤器
     */
    void addRowFilters(Collection<RowValidateFilter> filters);

    /**
     * 增加表级验证过滤器
     *
     * @param filters 过滤器
     */
    void addTableFilters(Collection<TableValidateFilter> filters);

}
