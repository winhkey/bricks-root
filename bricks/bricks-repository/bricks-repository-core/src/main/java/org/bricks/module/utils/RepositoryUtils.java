package org.bricks.module.utils;

import static org.bricks.module.constants.Constants.QueryConstants.FIELD_PATTERN;
import static org.bricks.utils.RegexUtils.regularGroupMap;
import static org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus;

import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * 数据库工具类
 *
 * @author fuzy
 *
 */
@UtilityClass
public class RepositoryUtils
{

    /**
     * 分析查询条件
     * 
     * @param key 条件key
     * @return 分析结果
     */
    public static Map<String, String> parse(String key)
    {
        return regularGroupMap(FIELD_PATTERN, key, "field", "operator", "connector", "g");
    }

    /**
     * 手动回滚
     */
    public static void rollback()
    {
        currentTransactionStatus().setRollbackOnly();
    }

}
