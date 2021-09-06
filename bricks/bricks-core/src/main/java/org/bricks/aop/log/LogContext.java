package org.bricks.aop.log;

import org.bricks.bean.AbstractBean;
import org.slf4j.Logger;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 日志切面线程变量
 *
 * @author fuzy
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class LogContext extends AbstractBean
{

    /**
     * 打印日志
     */
    private boolean doLog;

    /**
     * 执行开始时间
     */
    private long start;

    /**
     * 日志对象
     */
    private Logger logger;

}
