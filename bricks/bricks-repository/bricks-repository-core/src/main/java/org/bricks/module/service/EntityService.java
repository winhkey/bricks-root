package org.bricks.module.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import org.bricks.module.bean.ResultData;

/**
 * 实体类crud操作service
 *
 * @author fuzhiying
 *
 * @param <I> ID类型
 * @param <T> 实体类型
 */
public interface EntityService<I, T>
{

    /**
     * 按条件计数
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @return 个数
     */
    long count(Map<String, Object> condition, boolean clear);

    /**
     * 根据id删除
     *
     * @param id id
     */
    void delete(I id);

    /**
     * 根据条件物理删除
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @return 删除条数
     */
    int deleteAll(Map<String, Object> condition, boolean clear);

    /**
     * 根据id列表批量删除
     *
     * @param ids id列表
     */
    void deleteBatch(Collection<I> ids);

    /**
     * 删除实体对象
     *
     * @param t 实体对象
     */
    void deleteEntity(T t);

    /**
     * 删除对象列表
     *
     * @param list 对象列表
     */
    void deleteList(Collection<T> list);

    /**
     * 导出数据
     *
     * @param list 数据列表
     * @param outputStream 输出流
     * @param fields 定制字段列表
     */
    void export(List<List<Object>> list, OutputStream outputStream, List<String[]> fields);

    /**
     * 导出数据
     *
     * @param list 数据列表
     * @param outputStream 输出流
     * @param fields 定制字段列表
     */
    void exportData(List<List<Map<Integer, String>>> list, OutputStream outputStream, List<String[]> fields);

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
     * 根据条件查询
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @param orders 排序字段
     * @return 列表
     */
    List<T> findAll(Map<String, Object> condition, boolean clear, Order... orders);

    /**
     * 查询
     *
     * @param orders 排序字段
     * @return 列表
     */
    List<T> findAll(Order... orders);

    /**
     * 根据id列表查找对象列表
     *
     * @param ids id列表
     * @return 对象列表
     */
    List<T> findByIds(Collection<I> ids);

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
     * @param fieldList 字段列表
     * @param orders 排序字段
     * @return 单条记录
     */
    T findOne(Map<String, Object> condition, boolean clear, List<String> fieldList, Order... orders);

    /**
     * 按条件查询
     *
     * @param condition 条件
     * @param clear 是否清除条件
     * @param orders 排序字段
     * @return 单条记录
     */
    T findOne(Map<String, Object> condition, boolean clear, Order... orders);

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
    Page<T> findPage(Map<String, Object> condition, boolean clear, Pageable pageable);

    /**
     * 分页查询
     *
     * @param pageable 分页
     * @return 列表
     */
    Page<T> findPage(Pageable pageable);

    /**
     * @return 实体类型
     */
    Class<T> getEntityClass();

    /**
     * 导入数据文件
     *
     * @param inputStream 输入流
     * @return 数据列表
     */
    ResultData importFile(InputStream inputStream);

    /**
     * 逻辑删除
     *
     * @param t 对象
     */
    void logicDelete(T t);

    /**
     * 逻辑批量删除
     *
     * @param coll 对象列表
     */
    void logicDeleteCollection(Collection<T> coll);

    /**
     * 逻辑删除
     *
     * @param id id
     */
    void logicDeleteId(I id);

    /**
     * 逻辑批量删除
     *
     * @param ids id列表
     */
    void logicDeleteIds(Collection<I> ids);

    /**
     * 新增
     *
     * @param t 实体对象
     * @return 实体对象
     */
    T save(T t);

    /**
     * 批量新增
     *
     * @param collection 实体列表
     * @return 结果
     */
    List<T> saveBatch(Collection<T> collection);

    /**
     * 更新
     *
     * @param t 实体对象
     * @return 实体对象
     */
    T update(T t);

    /**
     * 批量更新
     *
     * @param list 实体列表
     * @return 结果
     */
    List<T> updateBatch(Collection<T> list);

    /**
     * 根据条件更新
     *
     * @param t 更新值
     * @param condition 条件
     * @param clear 是否清除条件
     * @return 更新条数
     */
    int updateAll(T t, Map<String, Object> condition, boolean clear);

}
