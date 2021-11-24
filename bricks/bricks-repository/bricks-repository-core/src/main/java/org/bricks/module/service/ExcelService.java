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

package org.bricks.module.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel导入导出接口
 *
 * @author fuzy
 *
 */
public interface ExcelService
{

    /**
     * 从流导入excel
     *
     * @param <T> 导入类型
     * @param is 输入流
     * @param sheetNum 页码
     * @param errorList 错误列表
     * @param clazz 导入类型
     * @return 数据列表
     */
    <T> List<T> importExcel(InputStream is, int sheetNum, List<Map<Integer, String>> errorList, Class<T> clazz);

    /**
     * 导出excel
     *
     * @param list 数据列表
     * @param os 输出流
     * @param fields 导出的字段
     * @param clazz 导入类型
     */
    void export(List<List<Object>> list, OutputStream os, List<String[]> fields, Class<?> clazz);

    /**
     * 导出excel
     *
     * @param list 数据列表
     * @param os 输出流
     * @param fields 导出的字段
     * @param clazz 导入类型
     */
    void exportData(List<List<Map<Integer, String>>> list, OutputStream os, List<String[]> fields, Class<?> clazz);

}
