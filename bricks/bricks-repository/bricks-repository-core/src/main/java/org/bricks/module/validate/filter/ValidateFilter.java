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

import org.bricks.module.bean.TableConfig;

/**
 * 校验过滤器
 *
 * @author fuzy
 *
 * @param <D> 数据
 */
public interface ValidateFilter<D>
{

    /**
     * 过滤验证数据
     *
     * @param data 数据
     * @param config 表配置
     * @return 结果
     */
    boolean validate(D data, TableConfig config);

}
