package org.bricks.module.validate.filter;

import static java.util.Optional.ofNullable;

import java.util.Map;

import org.springframework.stereotype.Service;

import org.bricks.module.bean.ColumnConfig;

/**
 * 自定义行级过滤器
 * 
 * @author fuzhiying
 *
 */
public abstract class AbstractCustomRowValidateFilter extends AbstractRowValidateFilter
{

    @Override
    protected boolean validate(int row, int column, ColumnConfig columnConfig, Map<Integer, String> dataMap,
            Class<?> entityClass)
    {
        return ofNullable(getClass().getAnnotation(Service.class)).filter(service -> columnConfig.getFilterName()
                .contains(service.value()))
                .map(service -> validate(row, column, dataMap))
                .orElse(true);
    }

    /**
     * 验证
     * 
     * @param row 行
     * @param column 列
     * @param dataMap 数据
     * @return 结果
     */
    protected abstract boolean validate(int row, int column, Map<Integer, String> dataMap);

}
