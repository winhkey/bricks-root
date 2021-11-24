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

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.module.constants.Constants.PoiConstants.ERR_COL;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象校验过滤器
 *
 * @author fuzy
 *
 * @param <D> 数据
 */
public abstract class AbstractValidateFilter<D> implements ValidateFilter<D>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 添加错误信息
     *
     * @param dataMap dataMap
     * @param errMessage errMessage
     */
    public void setErrorMessage(Map<Integer, String> dataMap, String errMessage)
    {
        String message = dataMap.get(-1);
        dataMap.put(ERR_COL, isNotBlank(message) ? message + "," + errMessage : errMessage);
    }

}
