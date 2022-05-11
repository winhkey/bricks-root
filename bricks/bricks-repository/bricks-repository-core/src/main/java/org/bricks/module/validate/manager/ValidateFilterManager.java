package org.bricks.module.validate.manager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bricks.module.validate.filter.RowValidateFilter;
import org.bricks.module.validate.filter.TableValidateFilter;

/**
 * 验证过滤管理器
 *
 * @author fuzhiying
 *
 */
public interface ValidateFilterManager extends ValidateManager<List<Map<Integer, String>>>
{

    /**
     * 增加行级验证过滤器
     *
     * @param filters 过滤器
     */
    void addRowFilters(Collection<RowValidateFilter> filters);

    /**
     * 增加表级验证过滤器
     *
     * @param filters 过滤器
     */
    void addTableFilters(Collection<TableValidateFilter> filters);

}
