package org.bricks.module.bean;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import org.bricks.bean.AbstractBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 批量数据
 * 
 * @author fuzy
 *
 */
@Setter
@Getter
@Accessors(chain = true)
public class BatchData extends AbstractBean
{

    /**
     * 数据列表
     */
    private List<Map<Integer, String>> dataMap = newArrayList();

    /**
     * 数据行数
     */
    private int total;

}
