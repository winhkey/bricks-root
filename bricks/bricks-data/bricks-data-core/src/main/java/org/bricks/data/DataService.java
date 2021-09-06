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

package org.bricks.data;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 字符串转对象接口
 *
 * @author fuzy
 *
 */
public interface DataService
{

    /**
     * 字符串转对象
     *
     * @param content 字符串
     * @param type 类型
     * @param <T> 类型
     * @return 对象
     */
    <T> T input(String content, Type... type);

    /**
     * 文件转对象
     *
     * @param file 文件
     * @param type 类型
     * @param <T> 类型
     * @return 对象
     */
    <T> T input(File file, Type... type);

    /**
     * 流转对象
     *
     * @param stream json流
     * @param type 类型
     * @param <T> 类型
     * @return 对象
     */
    <T> T input(InputStream stream, Type... type);

    /**
     * 未知类型(一般是map)转对象
     *
     * @param object 未知
     * @param type 类型
     * @param <T> 类型
     * @return 对象
     */
    <T> T convert(Object object, Type... type);

    /**
     * 对象转字符串
     *
     * @param object 对象
     * @param type 类型
     * @return 字符串
     */
    String output(Object object, Type... type);

    /**
     * 简化
     *
     * @param content 字符串
     * @return 结果
     */
    String simplify(String content);

}
