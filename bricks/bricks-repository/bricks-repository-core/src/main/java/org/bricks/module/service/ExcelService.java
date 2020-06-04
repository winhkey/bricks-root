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
 * @param <T> 实体类型
 */
public interface ExcelService<T> {

    /**
     * 从流导入excel
     *
     * @param is 输入流
     * @param sheetNum 页码
     * @param errorList 错误列表
     * @return 数据列表
     */
    List<T> importExcel(InputStream is, int sheetNum, List<Map<Integer, String>> errorList);

    /**
     * 导出excel
     *
     * @param list 数据列表
     * @param os 输出流
     * @param chart 是否导出图表
     * @param fields 导出的字段
     */
    void export(List<List<T>> list, OutputStream os, boolean chart, List<String[]> fields);

}
