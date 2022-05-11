package org.bricks.module.validate.filter;

import static org.bricks.module.constants.Constants.PoiConstants.ERR_COL;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * 抽象校验过滤器
 *
 * @author fuzhiying
 *
 * @param <D> 数据
 */
public abstract class AbstractValidateFilter<D> implements ValidateFilter<D>
{

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 国际化
     */
    protected Map<String, MessageSourceAccessor> messageSourceAccessorMap;

    /**
     * 添加错误信息
     *
     * @param dataMap dataMap
     * @param errMessage errMessage
     */
    public void setErrorMessage(Map<Integer, String> dataMap, String errMessage)
    {
        String message = dataMap.get(-1);
        dataMap.put(ERR_COL, isNotBlank(message) ? message + "," + errMessage : errMessage);
    }

}
