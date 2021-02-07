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

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.bricks.module.bean.ResultData;
import org.bricks.module.converter.EntityMapConverter;
import org.bricks.service.LimitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实体类crud操作service
 *
 * @author fuzy
 *
 * @param <T> 实体类型
 * @param <I> ID类型
 */
public abstract class AbstractEntityService<T, I> implements EntityService<T, I> {

    /**
     * 日志
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 属性条件正则
     */
    protected static final Pattern FIELD_PATTERN = Pattern.compile("(\\w+)@?(\\w+)?");

    /**
     * 限流回调
     */
    @Resource
    protected LimitCallback limitCallback;

    /**
     * 导入导出接口
     */
    @Autowired(required = false)
    protected ExcelService<T> excelService;

    /**
     * 实体转map
     */
    @Resource
    protected EntityMapConverter entityMapConverter;

    /**
     * 实体类型
     */
    protected Class<T> entityClass;

    /**
     * 构造方法
     */
    @SuppressWarnings("unchecked")
    public AbstractEntityService() {
        List<Class<?>> classList = getComponentClassList(getClass(), EntityService.class);
        entityClass = (Class<T>) classList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData importFile(InputStream inputStream) {
        ResultData data = new ResultData();
        List<Map<Integer, String>> errorList = newArrayList();
        try {
            List<T> excelList = excelService.importExcel(inputStream, 0, errorList);
            if (isNotEmpty(excelList)) {
                List<T> list = excelList.stream()
                        .filter(this::beforeImport)
                        .collect(toList());
                saveBatch(list);
                data.setSuccessSize(list.size());
            } else {
                data.setSuccessSize(0);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        data.setError(errorList);
        return data;
    }

    /**
     * 导入之前的数据处理,如校验，设置创建时间等一些默认值
     *
     * @param t 实体对象
     * @return 校验结果
     */
    protected boolean beforeImport(T t) {
        return true;
    }

    /**
     * 导入前数据处理和校验过滤（可混合session内容校验）
     *
     * @param t 实体对象
     * @return 校验结果
     */
    public String beforeImportValidate(T t) {
        return "";
    }

    @Override
    public void export(List<List<T>> list, OutputStream outputStream, boolean chart, List<String[]> fields) {
        if (isNotEmpty(list)) {
            try {
                excelService.export(list, outputStream, chart, fields);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(T t) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T update(T t) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(Collection<T> list) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(I id) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEntity(T t) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Collection<I> ids) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteList(Collection<T> list) {

    }

    @Override
    public T findOne(I id) {
        return null;
    }

    @Override
    public T findOne(Map<String, Object> condition, boolean clear, List<String> fieldList, Order... orders) {
        return null;
    }

    @Override
    public List<T> findByIds(Collection<I> ids) {
        return null;
    }

    @Override
    public List<T> findAll(Map<String, Object> condition, boolean clear, List<String> fieldList, Order... orders) {
        return null;
    }

    @Override
    public Page<T> findPage(Map<String, Object> condition, boolean clear, List<String> fieldList, Pageable pageable) {
        return null;
    }

    @Override
    public int deleteAll(Map<String, Object> condition, boolean clear) {
        return 0;
    }

    @Override
    public int updateAll(T t, Map<String, Object> condition, boolean clear) {
        return 0;
    }

    @Override
    public long count(Map<String, Object> condition, boolean clear) {
        return 0;
    }

    /**
     * 拼接条件sql
     *
     * @param builder builder
     * @param condition 条件
     * @param key 字段名
     * @param argument 参数
     * @param appendSql sql
     */
    protected void appendCondition(StringBuilder builder, Map<String, Object> condition, String key, Object argument,
            String appendSql) {
        ofNullable(argument).ifPresent(arg -> {
            condition.put(key, arg);
            ofNullable(appendSql).ifPresent(builder::append);
        });
    }

    /**
     * 拼接条件sql
     *
     * @param builder builder
     * @param condition 条件
     * @param key 字段名
     * @param argument 参数
     * @param appendSql sql
     */
    protected void appendCondition(StringBuilder builder, Map<String, Object> condition, String key, String argument,
            String appendSql) {
        if (isNotBlank(argument)) {
            condition.put(key, argument);
            ofNullable(appendSql).ifPresent(builder::append);
        }
    }

    /**
     * 清除条件
     *
     * @param condition 条件map
     * @param clear 是否清除
     */
    protected void clearCondition(Map<String, Object> condition, boolean clear) {
        if (clear && isNotEmpty(condition)) {
            condition.clear();
        }
    }

}
