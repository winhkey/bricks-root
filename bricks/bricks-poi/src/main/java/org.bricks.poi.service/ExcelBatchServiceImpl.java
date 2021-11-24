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

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.RED;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;
import static org.apache.poi.ss.usermodel.FillPatternType.SPARSE_DOTS;
import static org.apache.poi.ss.usermodel.WorkbookFactory.create;
import static org.bricks.constants.Constants.FormatConstants.DATETIME_FORMAT;
import static org.bricks.module.constants.Constants.PoiConstants.ERR_COL;
import static org.bricks.utils.DateUtils.format;
import static org.bricks.utils.DateUtils.toLocalDateTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bricks.module.bean.BatchData;
import org.bricks.module.enums.DataType;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * excel批量接口
 *
 * @author fuzy
 *
 */
@Slf4j
@Service
public class ExcelBatchServiceImpl implements ExcelBatchService
{

    @Override
    public BatchData getData(InputStream stream)
    {
        return getData(stream, 0, 1);
    }

    @Override
    public BatchData getData(InputStream stream, int sheetIndex, int startRow)
    {
        BatchData data = new BatchData();
        if (stream != null)
        {
            ZipSecureFile.setMinInflateRatio(-1.0);
            try (InputStream is = stream; Workbook workbook = create(is))
            {
                List<Map<Integer, String>> dataMapList = getDataList(workbook, sheetIndex, startRow);
                data.setDataMap(dataMapList);
                data.setTotal(dataMapList.size());
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return data;
    }

    @Override
    public void write(List<Map<Integer, String>> dataList, List<String> titleList, File file)
    {
        try (OutputStream os = newOutputStream(get(file.getAbsolutePath())))
        {
            write(dataList, titleList, os);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void write(List<Map<Integer, String>> dataList, List<String> titleList, OutputStream os)
    {
        Map<String, List<Map<Integer, String>>> dataMap = newHashMap();
        dataMap.put("Sheet1", dataList);
        Map<String, List<String>> titleMap = newHashMap();
        titleMap.put("Sheet1", titleList);
        write(dataMap, titleMap, os);
        dataMap.clear();
        titleMap.clear();
    }

    @Override
    public void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap, File file)
    {
        write(dataMap, titleMap, null, file);
    }

    @Override
    public void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap,
            OutputStream os)
    {
        write(dataMap, titleMap, null, os);
    }

    @Override
    public void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap,
            Map<String, List<DataType>> dataTypeMap, File file)
    {
        if (file == null)
        {
            return;
        }
        File dir = file.getParentFile();
        if (!dir.exists() && !dir.mkdirs())
        {
            log.warn("{} create failed.", dir.getAbsolutePath());
            return;
        }
        String fileName = file.getName()
                .toLowerCase(Locale.US);
        try (Workbook workbook = fileName.endsWith(".xls") ? new HSSFWorkbook() : new XSSFWorkbook();
                OutputStream os = newOutputStream(get(file.getAbsolutePath())))
        {
            write(dataMap, titleMap, os, workbook);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap,
            Map<String, List<DataType>> dataTypeMap, OutputStream stream)
    {
        try (Workbook workbook = new XSSFWorkbook(); OutputStream os = stream)
        {
            write(dataMap, titleMap, os, workbook);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    private void write(Map<String, List<Map<Integer, String>>> dataMap, Map<String, List<String>> titleMap,
            OutputStream stream, Workbook workbook) throws IOException
    {
        // 根据list生成sheet页
        createSheet(workbook, titleMap, dataMap);
        // 向excel写入数据
        workbook.write(stream);
        stream.flush();
    }

    /**
     * 解析excel文件得到数据集
     *
     * @param workbook 工作薄
     * @param sheetIndex sheet号
     * @param startRow 起始行
     * @return 行数据列表
     */
    public List<Map<Integer, String>> getDataList(Workbook workbook, int sheetIndex, int startRow)
    {
        if (workbook == null)
        {
            return null;
        }
        List<Map<Integer, String>> dataList = new ArrayList<>();
        addSheetData(workbook, sheetIndex, startRow, dataList);
        return dataList;
    }

    /**
     * 工作薄中一个sheet页的数据
     *
     * @param workbook 工作薄
     * @param sheetIndex sheet号
     * @param startRow 起始行
     * @param dataList 行数据列表
     */
    public void addSheetData(Workbook workbook, int sheetIndex, int startRow, List<Map<Integer, String>> dataList)
    {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        int cells = workbook.getSheetAt(sheetIndex)
                .getRow(0)
                .getLastCellNum();
        for (int i = startRow; i <= sheet.getLastRowNum(); i++)
        {
            addRowData(sheet, cells, i, dataList);
        }
    }

    /**
     * sheet页中一行的数据
     *
     * @param sheet sheet页
     * @param cells 列数
     * @param row 行号
     * @param dataList 行数据列表
     */
    private void addRowData(Sheet sheet, int cells, int row, List<Map<Integer, String>> dataList)
    {
        // 当前行
        Row line = sheet.getRow(row);
        if (line == null)
        {
            return;
        }
        // 初始化一行
        Map<Integer, String> dataMap = newLinkedHashMap();
        for (int i = 0; i < cells; i++)
        {
            addCellData(line, i, dataMap);
        }
        // 如果不是空行，就加进数据集
        if (!isAllEmpty(dataMap))
        {
            dataList.add(dataMap);
        }
    }

    /**
     * 添加一行中一个单元格的数据
     *
     * @param row 行
     * @param cel 列号
     * @param dataMap 行数据
     */
    private void addCellData(Row row, int cel, Map<Integer, String> dataMap)
    {
        String cellValue = "";
        Cell cell = row.getCell(cel);
        if (cell != null)
        {
            switch (cell.getCellType())
            {
                case NUMERIC:
                    if (isCellDateFormatted(cell))
                    {
                        Date date = cell.getDateCellValue();
                        cellValue = format(toLocalDateTime(date), DATETIME_FORMAT);
                    }
                    else
                    {
                        cell.setCellType(STRING);
                        cellValue = cell.getRichStringCellValue()
                                .getString()
                                .trim();
                    }
                    break;
                case STRING:
                    cellValue = cell.getRichStringCellValue()
                            .getString()
                            .trim();
                    break;
                case FORMULA:
                {
                    if (isCellDateFormatted(cell))
                    {
                        Date date = cell.getDateCellValue();
                        cellValue = format(toLocalDateTime(date), DATETIME_FORMAT);
                    }
                    else
                    {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case BLANK:
                default:
                    cellValue = "";
                    break;
            }
        }
        dataMap.put(cel, cellValue);
    }

    /**
     * 判断是否空行
     *
     * @param dataMap 行数据
     * @return 是否空行
     */
    private boolean isAllEmpty(Map<Integer, String> dataMap)
    {
        if (isNotEmpty(dataMap))
        {
            for (String value : dataMap.values())
            {
                if (isNotBlank(value))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 根据list生成sheet
     *
     * @param book 工作簿
     * @param titleMap Map&lt;sheetName, List&lt;title&gt;&gt;
     * @param dataMap 数据
     */
    private void createSheet(Workbook book, Map<String, List<String>> titleMap,
            Map<String, List<Map<Integer, String>>> dataMap)
    {
        if (isNotEmpty(titleMap))
        {
            for (Entry<String, List<String>> entry : titleMap.entrySet())
            {
                String key = entry.getKey();
                setSheet(book, key, entry.getValue(), dataMap.get(key));
            }
        }
    }

    private void setSheet(Workbook book, String sheetName, List<String> titles, List<Map<Integer, String>> list)
    {
        Sheet sheet = book.createSheet(sheetName);
        // 每页一个单元格样式
        CellStyle style = book.createCellStyle();
        // 设置标题
        setTitleValue(sheet, titles, style);
        // 错误信息单元格样式
        CellStyle etyle;
        Map<Integer, String> dataMap;
        boolean hasError;
        if (isNotEmpty(list))
        {
            for (int i = 0; i < list.size(); i++)
            {
                dataMap = list.get(i);
                hasError = isNotBlank(dataMap.get(ERR_COL));
                // 设置错误信息就将整行设为红色
                if (hasError)
                {
                    etyle = book.createCellStyle();
                    etyle.setFillForegroundColor(RED.getIndex());
                    etyle.setFillBackgroundColor(RED.getIndex());
                    etyle.setFillPattern(SPARSE_DOTS);
                    etyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    etyle.setAlignment(HorizontalAlignment.CENTER);
                    setRow(sheet, titles, i + 1, dataMap, etyle, true);
                }
                else
                {
                    setRow(sheet, titles, i + 1, dataMap, style, false);
                }
            }
        }
    }

    /**
     * 在当前sheet页的第一行写入标题
     *
     * @param sheet sheet页
     * @param titles 标题列表
     * @param cellStyle 样式
     */
    private void setTitleValue(Sheet sheet, List<String> titles, CellStyle cellStyle)
    {
        // 取第一行
        Row row = sheet.createRow(0);
        boolean hasTitle = isNotEmpty(titles);
        int i = 0;
        if (hasTitle)
        {
            for (int n = titles.size(); i < n; i++)
            {
                setCell(row, i, titles.get(i), cellStyle);
            }
        }
    }

    /**
     * 设置一行数据
     * 
     * @param sheet sheet
     * @param titles 标题列表
     * @param rowNumber 行号
     * @param dataMap 数据
     * @param cellStyle 样式
     * @param hasError 错误信息
     */
    private void setRow(Sheet sheet, List<String> titles, int rowNumber, Map<Integer, String> dataMap,
            CellStyle cellStyle, boolean hasError)
    {
        // 取得当前行
        Row row = sheet.createRow(rowNumber);
        int i = 0;
        int n = titles.size();
        String value;
        for (; i < n; i++)
        {
            value = dataMap.get(i);
            sheet.setColumnWidth(i, 5000);
            sheet.setAutobreaks(true);
            setCell(row, i, value, cellStyle);
        }
        if (hasError)
        {
            sheet.setColumnWidth(i, 8000);
            setCell(row, i, dataMap.get(ERR_COL), cellStyle);
        }
    }

    /**
     * 将数据插入当前行的单元格
     * 
     * @param row 行
     * @param colNumber 列号
     * @param value 数据
     * @param cellStyle 样式
     */
    private void setCell(Row row, int colNumber, String value, CellStyle cellStyle)
    {
        // 垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 自动换行
        cellStyle.setWrapText(true);
        // 设置单元格样式
        Cell cell = row.createCell(colNumber);
        cell.setCellStyle(cellStyle);
        // 设置单元格的值
        cell.setCellValue(value);
    }

}
