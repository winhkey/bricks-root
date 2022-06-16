package org.bricks.module.service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.bricks.module.bean.BatchData;

/**
 * 批量接口
 *
 * @author fuzy
 */
public interface BatchService
{

    /**
     * 导入
     *
     * @param stream 输入流
     * @return 批量数据
     */
    BatchData getData(InputStream stream);

    /**
     * 将数据写入文件
     *
     * @param dataList 行数据列表
     * @param titleList 标题列表
     * @param file 文件
     */
    void write(List<Map<Integer, String>> dataList, List<String> titleList, File file);

    /**
     * 将数据写入流
     *
     * @param dataList 行数据列表
     * @param titleList 标题列表
     * @param os 输出流
     */
    void write(List<Map<Integer, String>> dataList, List<String> titleList, OutputStream os);

}
