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
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;
import java.util.Map;

import org.bricks.bean.AbstractBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 导入结果
 *
 * @author fuzy
 *
 */
@Setter
@Getter
@Accessors(chain = true)
public class ResultData extends AbstractBean
{

    /**
     * 错误信息(包含数据)
     */
    private List<Map<Integer, String>> error = newArrayList();

    /**
     * 错误描述
     */
    private List<String> errorList = newArrayList();

    /**
     * 成功总数
     */
    private Integer successSize;

    /**
     * 错误信息
     *
     * @param error 错误信息
     */
    public void setError(List<Map<Integer, String>> error)
    {
        // 设置错误信息
        if (isNotEmpty(error))
        {
            error.forEach(map -> errorList.add(map.get(-1)));
            this.error = error;
        }
    }

}
