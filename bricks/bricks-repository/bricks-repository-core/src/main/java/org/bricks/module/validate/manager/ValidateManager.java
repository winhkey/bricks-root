package org.bricks.module.validate.manager;

import org.bricks.module.bean.TableConfig;

/**
 * 验证管理器
 *
 * @author fuzhiying
 *
 * @param <D> 数据
 */
public interface ValidateManager<D>
{

    /**
     * 校验
     *
     * @param data 数据
     * @param config 配置参数
     * @return 结果
     */
    boolean validate(D data, TableConfig config);

}
