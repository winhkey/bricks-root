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

package org.bricks.poi.service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.bricks.module.bean.BatchData;
import org.bricks.module.enums.DataType;
import org.bricks.module.service.BatchService;

/**
 * excel批量接口
 *
 * @author fuzy
 *
 */
public interface ExcelBatchService extends BatchService
{

    /**
     * 按excel导入数据
     *
     * @param stream 输入流
     * @param sheetIndex sheet序号
     * @param startRow 开始行号
     * @return 批量数据
     */
    BatchData getData(InputStream stream, int sheetIndex, int startRow);

    /**
     * 将数据写入文件(支持多sheet)
     *
     * @param dataMap Map&lt;sheetName, List&lt;Map&lt;Integer, String&gt;&gt;&gt;
     * @param titleMap Map&lt;sheetName, List&lt;String&gt;&gt;
     * @param file 文件
     */
    void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap, File file);

    /**
     * 将数据写入流
     *
     * @param dataMap Map&lt;sheetName, List&lt;Map&lt;Integer, String&gt;&gt;&gt;
     * @param titleMap Map&lt;sheetName, List&lt;String&gt;&gt;
     * @param os 输出流
     */
    void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap, OutputStream os);

    /**
     * 将数据写入excel
     *
     * @param dataMap 数据列表
     * @param titleMap 标题
     * @param dataTypeMap 数据类型
     * @param file 文件
     */
    void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap,
            Map<String, List<DataType>> dataTypeMap, File file);

    /**
     * 将数据写入excel
     *
     * @param dataMap 数据列表
     * @param titleMap 标题
     * @param dataTypeMap 数据类型
     * @param os 输出流
     */
    void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap,
            Map<String, List<DataType>> dataTypeMap, OutputStream os);

}
