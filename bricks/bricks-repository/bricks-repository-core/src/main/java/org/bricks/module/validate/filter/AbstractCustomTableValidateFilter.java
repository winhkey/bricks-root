package org.bricks.module.validate.filter;

import java.util.List;
import java.util.Map;

import org.bricks.module.bean.TableConfig;

/**
 * 自定义表级过滤器
 *
 * @author fuzy
 *
 */
public abstract class AbstractCustomTableValidateFilter extends AbstractTableValidateFilter
{

    @Override
    protected boolean rowValidate(int row, int index, List<Map<Integer, String>> dataList, TableConfig config)
    {
        return rowValidate(row, index, dataList);
    }

    /**
     * 验证
     *
     * @param row 行
     * @param index 对比行
     * @param dataList 数据
     * @return 结果
     */
    protected abstract boolean rowValidate(int row, int index, List<Map<Integer, String>> dataList);

}
