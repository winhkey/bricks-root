package org.bricks.module.exception;

import org.bricks.exception.BaseException;

/**
 * dao层异常基类
 *
 * @author fuzhiying
 *
 */
public class RepositoryException extends BaseException
{

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     *
     * @param t 异常
     */
    public RepositoryException(Throwable t)
    {
        super(t);
    }

    /**
     * 构造方法
     *
     * @param message 描述
     */
    public RepositoryException(String message)
    {
        super(message);
    }

    /**
     * 构造方法
     *
     * @param message 描述
     * @param t 异常
     */
    public RepositoryException(String message, Throwable t)
    {
        super(message, t);
    }

}
