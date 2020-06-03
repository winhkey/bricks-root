/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
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

package org.bricks.async.bean;

import java.util.List;

import org.bricks.bean.AbstractBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 任务请求
 *
 * @author fuzy
 *
 */
@Setter
@Getter
@Accessors(chain = true)
public class TaskRequest extends AbstractBean {

    /**
     * task列表（bean Id）
     */
    private List<String> tasks;

    /**
     * 任务参数
     */
    private Object param;

}
