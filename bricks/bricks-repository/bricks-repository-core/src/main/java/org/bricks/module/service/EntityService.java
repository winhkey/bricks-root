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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bricks.module.bean.ResultData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

/**
 * 实体类crud操作service
 *
 * @author fuzy
 *
 * @param <T> 实体类型
 * @param <I> ID类型
 */
public interface EntityService<I, T>
{

    /**
     * 错误描述
     */
    String ERR_MSG = "{0}.{1} error";

    /**
     * 新增
     *
     * @param t 实体对象
     * @return 实体对象
     */
    T save(T t);

    /**
     * 更新
     *
     * @param t 实体对象
     * @return 实体对象
     */
    T update(T t);

    /**
     * 批量新增
     *
     * @param list 实体列表
     */
    void saveBatch(Collection<T> list);

    /**
     * 根据id删除
     *
     * @param id id
     */
    void delete(I id);

    /**
     * 删除实体对象
     *
     * @param t 实体对象
     */
    void deleteEntity(T t);

    /**
     * 根据id列表批量删除
     *
     * @param ids id列表
     */
    void deleteBatch(Collection<I> ids);

    /**
     * 删除对象列表
     *
     * @param list 对象列表
     */
    void deleteList(Collection<T> list);

    /**
     * 根据id查找对象
     *
     * @param id id
     * @return 对象
     */
    T findOne(I id);

    /**
     * 按条件查询
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @param orders 排序字段
     * @return 单条记录
     */
    default T findOne(Map<String, Object> condition, boolean clear, Order... orders)
    {
        return findOne(condition, clear, emptyList(), orders);
    }

    /**
     * 按条件查询
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @param fieldList 字段列表
     * @param orders 排序字段
     * @return 单条记录
     */
    T findOne(Map<String, Object> condition, boolean clear, List<String> fieldList, Order... orders);

    /**
     * 根据id列表查找对象列表
     *
     * @param ids id列表
     * @return 对象列表
     */
    List<T> findByIds(Collection<I> ids);

    /**
     * 查询
     *
     * @param orders 排序字段
     * @return 列表
     */
    default List<T> findAll(Order... orders)
    {
        return findAll(emptyMap(), false, emptyList(), orders);
    }

    /**
     * 根据条件查询
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @param orders 排序字段
     * @return 列表
     */
    default List<T> findAll(Map<String, Object> condition, boolean clear, Order... orders)
    {
        return findAll(condition, clear, emptyList(), orders);
    }

    /**
     * 根据条件查询
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @param fieldList 字段列表
     * @param orders 排序字段
     * @return 列表
     */
    List<T> findAll(Map<String, Object> condition, boolean clear, List<String> fieldList, Order... orders);

    /**
     * 分页查询
     *
     * @param pageable 分页
     * @return 列表
     */
    default Page<T> findPage(Pageable pageable)
    {
        return findPage(emptyMap(), false, emptyList(), pageable);
    }

    /**
     * 分页查询
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @param fieldList 字段列表
     * @param pageable 分页
     * @return 列表
     */
    Page<T> findPage(Map<String, Object> condition, boolean clear, List<String> fieldList, Pageable pageable);

    /**
     * 分页查询
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @param pageable 分页
     * @return 列表
     */
    default Page<T> findPage(Map<String, Object> condition, boolean clear, Pageable pageable)
    {
        return findPage(condition, clear, emptyList(), pageable);
    }

    /**
     * 导入数据文件
     *
     * @param inputStream 输入流
     * @return 数据列表
     */
    ResultData importFile(InputStream inputStream);

    /**
     * 导出数据
     *
     * @param list 数据列表
     * @param outputStream 输出流
     * @param chart 是否导出图表
     * @param fields 定制字段列表
     */
    void export(List<List<T>> list, OutputStream outputStream, boolean chart, List<String[]> fields);

    /**
     * 根据条件物理删除
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @return 删除条数
     */
    int deleteAll(Map<String, Object> condition, boolean clear);

    /**
     * 根据条件更新
     *
     * @param t 更新值
     * @param condition 条件
     * @param clear 是否清除条件
     * @return 更新条数
     */
    int updateAll(T t, Map<String, Object> condition, boolean clear);

    /**
     * 按条件计数
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @return 个数
     */
    long count(Map<String, Object> condition, boolean clear);

}
