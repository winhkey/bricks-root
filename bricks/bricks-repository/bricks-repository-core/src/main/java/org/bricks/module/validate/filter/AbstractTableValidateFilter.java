package org.bricks.module.validate.filter;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;
import java.util.Map;

import org.bricks.module.bean.TableConfig;

/**
 * 抽象表级校验过滤器
 *
 * @author fuzhiying
 */
public abstract class AbstractTableValidateFilter extends AbstractValidateFilter<List<Map<Integer, String>>>
        implements TableValidateFilter
{

    /**
     * threadLocal
     */
    private final ThreadLocal<Map<Integer, List<Integer>>> threadLocal = new ThreadLocal<>();

    @Override
    public boolean validate(List<Map<Integer, String>> dataList, TableConfig config)
    {
        boolean result = true;
        Map<Integer, List<Integer>> duplicateMap = newHashMap();
        threadLocal.set(duplicateMap);
        if (isNotEmpty(dataList))
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                boolean flag = validate(i, dataList, config);
                result = flag && result;
            }
        }
        threadLocal.remove();
        return result;
    }

    /**
     * 验证
     *
     * @param row 当前行
     * @param dataList 数据
     * @param config 配置
     * @return 结果
     */
    protected boolean validate(int row, List<Map<Integer, String>> dataList, TableConfig config)
    {
        boolean result = true;
        if (isNotEmpty(dataList))
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                if (i > row && notContains(i) && notContains(row))
                {
                    boolean flag = validate(row, i, dataList, config);
                    result = flag && result;
                }
            }
        }
        return result;
    }

    /**
     * 验证
     *
     * @param row 行
     * @param index 对比行
     * @param dataList 数据
     * @param config 配置
     * @return 结果
     */
    protected boolean validate(int row, int index, List<Map<Integer, String>> dataList, TableConfig config)
    {
        boolean rtn = rowValidate(row, index, dataList, config);
        if (!rtn)
        {
            addDuplicate(row, index);
        }
        return rtn;
    }

    /**
     * 将重复的行记录加进重复列表
     *
     * @param row 行号
     * @param dupIndex 重复索引
     */
    protected void addDuplicate(int row, int dupIndex)
    {
        List<Integer> list = threadLocal.get()
                .computeIfAbsent(row, k -> newArrayList());
        list.add(dupIndex);
    }

    /**
     * 行记录在重复列表中是否存在，包括key和value
     *
     * @param index 索引
     * @return 是否存在
     */
    protected boolean notContains(int index)
    {
        return threadLocal.get()
                .values()
                .stream()
                .noneMatch(s -> s.contains(index));
    }

    /**
     * 行验证
     *
     * @param row 行
     * @param index 对比行
     * @param dataList 数据
     * @param config 配置
     * @return 结果
     */
    protected abstract boolean rowValidate(int row, int index, List<Map<Integer, String>> dataList, TableConfig config);

}
