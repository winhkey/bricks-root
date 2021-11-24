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
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.bricks.module.bean.BatchData;
import org.bricks.module.bean.TableConfig;
import org.bricks.module.converter.EntityDataMapConverter;
import org.bricks.module.enums.DataType;
import org.bricks.module.service.ExcelService;
import org.bricks.module.service.TableConfigLoader;
import org.bricks.module.validate.factory.FilterManagerFactory;
import org.bricks.module.validate.manager.ValidateFilterManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Excel导入导出接口
 *
 * @author fuzy
 *
 */
@Component
public class ExcelServiceImpl implements ExcelService
{

    /**
     * 开启导入事务
     */
    @Value("${bricks.poi.transaction:true}")
    private boolean transaction;

    /**
     * 加载表配置
     */
    @Resource
    private TableConfigLoader configLoader;

    /**
     * 实体类转换
     */
    @Resource
    private EntityDataMapConverter entityDataMapConverter;

    /**
     * excel批量接口
     */
    @Resource
    private ExcelBatchService excelBatchService;

    /**
     * 过滤管理器工厂
     */
    @Resource
    private FilterManagerFactory filterManagerFactory;

    @SuppressWarnings(UNCHECKED)
    @Override
    public <T> List<T> importExcel(InputStream is, int sheetNum, List<Map<Integer, String>> errorList, Class<T> clazz)
    {
        TableConfig config = configLoader.load(clazz);
        BatchData data = excelBatchService.getData(is, sheetNum, config.getStartRow());
        ValidateFilterManager validateManager = filterManagerFactory.build(config);
        List<Map<Integer, String>> dataList = data.getDataMap();
        boolean rtn = validateManager.validate(dataList, config);
        if (transaction && !rtn)
        {
            errorList.addAll(dataList);
            return null;
        }
        return (List<T>) entityDataMapConverter.reverseConvertList(dataList,
                new Object[] {clazz, config.getColumnMap(), errorList});
    }

    @Override
    public void exportData(List<List<Map<Integer, String>>> list, OutputStream os, List<String[]> fields,
            Class<?> clazz)
    {
        Map<String, List<Map<Integer, String>>> dataMap = newHashMap();
        fillDataMap(list, dataMap);
        TableConfig config = configLoader.load(clazz);
        export(config, dataMap, os, fields);
    }

    @Override
    public void export(List<List<Object>> list, OutputStream os, List<String[]> fields, Class<?> clazz)
    {
        TableConfig config = configLoader.load(clazz);
        Map<String, List<Map<Integer, String>>> dataMap = newHashMap();
        fillObjectDataMap(list, config, dataMap, fields);
        export(config, dataMap, os, fields);
    }

    private void export(TableConfig config, Map<String, List<Map<Integer, String>>> dataMap, OutputStream os,
            List<String[]> fields)
    {
        // 封装title
        Map<String, List<String>> titleMap = newHashMap();
        Map<String, List<DataType>> dataTypeMap = newHashMap();
        buildTitleMap(dataMap.size(), config, titleMap, dataTypeMap, fields);
        excelBatchService.write(dataMap, titleMap, dataTypeMap, os);
    }

    /**
     * 标题对应的map
     * 
     * @param size sheet页数
     * @param config 配置参数
     * @param titleMap 标题map
     * @param dataTypeMap 数据类型map
     * @param fields 字段列表
     */
    protected void buildTitleMap(int size, TableConfig config, Map<String, List<String>> titleMap,
            Map<String, List<DataType>> dataTypeMap, List<String[]> fields)
    {
        for (int i = 0; i < size; i++)
        {
            String[] field = isNotEmpty(fields) ? fields.get(i) : null;
            titleMap.put("Sheet" + i, config.getTitles(field));
            dataTypeMap.put("Sheet" + i, config.getDataTypes(field));
        }
    }

    /**
     * 填充数据
     * 
     * @param list 数据
     * @param config 配置参数
     * @param dataMap 数据map
     * @param fields 字段
     */
    protected void fillObjectDataMap(List<List<Object>> list, TableConfig config,
            Map<String, List<Map<Integer, String>>> dataMap, List<String[]> fields)
    {
        int i = 0;
        boolean hasTitles = isNotEmpty(fields);
        for (List<Object> subList : list)
        {
            List<Map<Integer, String>> mapList = entityDataMapConverter.convertList(subList,
                    new Object[] {config.getFields(hasTitles ? fields.get(i) : null)});
            dataMap.put("Sheet" + i++, mapList);
        }
    }

    private void fillDataMap(List<List<Map<Integer, String>>> list, Map<String, List<Map<Integer, String>>> dataMap)
    {
        int i = 0;
        for (List<Map<Integer, String>> subList : list)
        {
            dataMap.put("Sheet" + i++, subList);
        }
    }

}
