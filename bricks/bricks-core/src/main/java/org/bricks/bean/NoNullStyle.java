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

package org.bricks.bean;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 忽略null字段
 *
 * @author fuzy
 * 
 */
public class NoNullStyle extends ToStringStyle
{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     */
    public NoNullStyle()
    {
        super();
        this.setUseShortClassName(true);
        this.setUseIdentityHashCode(true);
    }

    @Override
    public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail)
    {
        if (value instanceof String)
        {
            if (isNotBlank((String) value))
            {
                super.append(buffer, fieldName, value, fullDetail);
            }
        }
        else if (value != null)
        {
            super.append(buffer, fieldName, value, fullDetail);
        }
    }

}
