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
