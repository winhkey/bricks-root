package org.bricks.module.validate.manager;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.bricks.module.bean.TableConfig;
import org.bricks.module.validate.filter.RowValidateFilter;
import org.bricks.module.validate.filter.TableValidateFilter;

/**
 * 验证过滤管理器实现类
 *
 * @author fuzhiying
 *
 */
@Scope("prototype")
@Component
public class FilterManagerImpl implements ValidateFilterManager
{

    /**
     * 行验证过滤器列表
     */
    private final List<RowValidateFilter> rowFilterList;

    /**
     * 表验证过滤器列表
     */
    private final List<TableValidateFilter> tableFilterList;

    /**
     * 构造方法
     */
    public FilterManagerImpl()
    {
        // 初始化表验证过滤器
        tableFilterList = newArrayList();
        // 初始化行验证过滤器
        rowFilterList = newArrayList();
    }

    @Override
    public void addRowFilters(Collection<RowValidateFilter> filters)
    {
        rowFilterList.addAll(filters);
    }

    @Override
    public void addTableFilters(Collection<TableValidateFilter> filters)
    {
        tableFilterList.addAll(filters);
    }

    @Override
    public boolean validate(List<Map<Integer, String>> dataList, TableConfig config)
    {
        boolean result = tableValidate(dataList, config);
        int row = 1;
        for (Map<Integer, String> dataMap : dataList)
        {
            config.setCurrentRow(row++);
            boolean flag = rowValidate(dataMap, config);
            result = flag && result;
        }
        return result;
    }

    /**
     * 验证表数据
     *
     * @param dataList 表数据
     * @param config 配置参数
     * @return 结果
     */
    private boolean tableValidate(List<Map<Integer, String>> dataList, TableConfig config)
    {
        boolean result = true;
        for (TableValidateFilter filter : tableFilterList)
        {
            boolean flag = filter.validate(dataList, config);
            result = flag && result;
        }
        return result;
    }

    /**
     * 验证单行数据
     *
     * @param dataMap 单行数据
     * @param config 配置参数
     * @return 结果
     */
    private boolean rowValidate(Map<Integer, String> dataMap, TableConfig config)
    {
        boolean result = true;
        for (RowValidateFilter filter : rowFilterList)
        {
            boolean flag = filter.validate(dataMap, config);
            result = flag && result;
        }
        return result;
    }

}
