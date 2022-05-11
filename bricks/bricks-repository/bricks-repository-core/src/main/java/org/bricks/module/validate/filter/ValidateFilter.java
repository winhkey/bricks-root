package org.bricks.module.validate.filter;

import org.bricks.module.bean.TableConfig;

/**
 * 校验过滤器
 *
 * @author fuzhiying
 *
 * @param <D> 数据
 */
public interface ValidateFilter<D>
{

    /**
     * 过滤验证数据
     *
     * @param data 数据
     * @param config 表配置
     * @return 结果
     */
    boolean validate(D data, TableConfig config);

}
