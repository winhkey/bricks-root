package org.bricks.module.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.utils.ContextHolder.getBean;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

import org.bricks.annotation.NoLog;
import org.bricks.converter.EntityMapConverter;
import org.bricks.exception.BaseException;
import org.bricks.listener.AbstractInitFinishedListener;
import org.bricks.module.bean.ResultData;
import org.bricks.service.LimitCallback;

/**
 * 实体类crud操作service
 *
 * @author fuzy
 *
 * @param <I> ID类型
 * @param <T> 实体类型
 */
@Transactional(readOnly = true)
public abstract class AbstractEntityService<I, T> extends AbstractInitFinishedListener implements EntityService<I, T>
{

    /**
     * 日志
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 限流回调
     */
    @Resource
    protected LimitCallback limitCallback;

    /**
     * 自身
     */
    protected AbstractEntityService<I, T> selfService;

    /**
     * 导入导出接口
     */
    @Autowired(required = false)
    protected ExcelService excelService;

    /**
     * 实体转map
     */
    @Resource
    protected EntityMapConverter entityMapConverter;

    /**
     * 主键类型
     */
    protected Class<I> idClass;

    /**
     * 实体类型
     */
    protected Class<T> entityClass;

    /**
     * 实体类名
     */
    protected String entityName;

    /**
     * 构造方法
     */
    @SuppressWarnings(UNCHECKED)
    @PostConstruct
    public void init()
    {
        List<Class<?>> classList = getComponentClassList(getClass(), EntityService.class);
        idClass = (Class<I>) classList.get(0);
        entityClass = (Class<T>) classList.get(1);
        entityName = entityClass.getSimpleName();
    }

    @Override
    public long count(Map<String, Object> condition, boolean clear)
    {
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(I id)
    {
        //
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAll(Map<String, Object> condition, boolean clear)
    {
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Collection<I> ids)
    {
        //
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEntity(T t)
    {
        //
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteList(Collection<T> list)
    {
        //
    }

    @Override
    public void export(List<List<Object>> list, OutputStream outputStream, List<String[]> fields)
    {
        if (isNotEmpty(list))
        {
            try
            {
                excelService.export(list, outputStream, fields, entityClass);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void exportData(List<List<Map<Integer, String>>> list, OutputStream outputStream, List<String[]> fields)
    {
        if (isNotEmpty(list))
        {
            try
            {
                excelService.exportData(list, outputStream, fields, entityClass);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public List<T> findAll(Map<String, Object> condition, boolean clear, List<String> fieldList, Order... orders)
    {
        return null;
    }

    @Override
    public List<T> findAll(Map<String, Object> condition, boolean clear, Order... orders)
    {
        return findAll(condition, clear, emptyList(), orders);
    }

    @Override
    public List<T> findAll(Order... orders)
    {
        return findAll(emptyMap(), false, emptyList(), orders);
    }

    @Override
    public List<T> findByIds(Collection<I> ids)
    {
        return null;
    }

    @Override
    public T findOne(I id)
    {
        return null;
    }

    @Override
    public T findOne(Map<String, Object> condition, boolean clear, List<String> fieldList, Order... orders)
    {
        return null;
    }

    @Override
    public T findOne(Map<String, Object> condition, boolean clear, Order... orders)
    {
        return findOne(condition, clear, emptyList(), orders);
    }

    @Override
    public Page<T> findPage(Map<String, Object> condition, boolean clear, List<String> fieldList, Pageable pageable)
    {
        return null;
    }

    @Override
    public Page<T> findPage(Map<String, Object> condition, boolean clear, Pageable pageable)
    {
        return findPage(condition, clear, emptyList(), pageable);
    }

    @Override
    public Page<T> findPage(Pageable pageable)
    {
        return findPage(emptyMap(), false, emptyList(), pageable);
    }

    @NoLog
    @Override
    public Class<T> getEntityClass()
    {
        return entityClass;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultData importFile(InputStream inputStream)
    {
        ResultData data = new ResultData();
        List<Map<Integer, String>> errorList = newArrayList();
        List<T> excelList = excelService.importExcel(inputStream, 0, errorList, entityClass);
        excelList = beforeImport(excelList);
        if (isNotEmpty(excelList))
        {
            List<T> list = excelList.stream()
                    .filter(this::beforeImport)
                    .collect(toList());
            list = saveBatch(list);
            afterImport(list);
            data.setSuccessSize(list.size());
        }
        else
        {
            data.setSuccessSize(0);
        }
        data.setError(errorList);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(T t)
    {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> saveBatch(Collection<T> collection)
    {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T update(T t)
    {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> updateBatch(Collection<T> list)
    {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAll(T t, Map<String, Object> condition, boolean clear)
    {
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(T t)
    {
        //
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDeleteCollection(Collection<T> coll)
    {
        //
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDeleteId(I id)
    {
        //
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDeleteIds(Collection<I> ids)
    {
        //
    }

    /**
     * 导入后处理
     *
     * @param list 导入列表
     */
    protected void afterImport(List<T> list)
    {
        //
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
            String appendSql)
    {
        ofNullable(argument).ifPresent(arg ->
        {
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
            String appendSql)
    {
        if (isNotBlank(argument))
        {
            condition.put(key, argument);
            ofNullable(appendSql).ifPresent(builder::append);
        }
    }

    /**
     * 导入前处理
     *
     * @param list 实体列表
     * @return 实体列表
     */
    protected List<T> beforeImport(List<T> list)
    {
        return list;
    }

    /**
     * 导入之前的数据处理,如校验，设置创建时间等一些默认值
     *
     * @param t 实体对象
     * @return 校验结果
     */
    protected boolean beforeImport(T t)
    {
        return true;
    }

    /**
     * 清除条件
     *
     * @param condition 条件map
     * @param clear 是否清除
     */
    protected void clearCondition(Map<String, Object> condition, boolean clear)
    {
        if (clear && isNotEmpty(condition))
        {
            condition.clear();
        }
    }

    @SuppressWarnings(UNCHECKED)
    @Override
    protected void doSyncInitFinished()
    {
        selfService = ofNullable(getBean(this.getClass())).orElseThrow(BaseException::new);
    }

}
