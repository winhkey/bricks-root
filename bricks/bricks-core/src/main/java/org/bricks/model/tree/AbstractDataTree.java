/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks-root)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bricks.model.tree;

import java.util.List;

import org.bricks.bean.AbstractBean;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 数据树
 *
 * @author fuzy
 *
 * @param <I> id
 * @param <D> 数据
 * @param <T> 子类
 */
@Getter
@Accessors(chain = true)
public abstract class AbstractDataTree<I, D, T extends AbstractDataTree<I, D, T>> extends AbstractBean
{

    /**
     * 主键
     */
    private I id;

    /**
     * 父id
     */
    private I parentId;

    /**
     * 子节点
     */
    private List<T> children;

    /**
     * 是否有父节点
     */
    private boolean hasParent;

    /**
     * 是否有子节点
     */
    private boolean hasChild;

    /**
     * 数据
     */
    private D data;

    /**
     * 设置id
     *
     * @param id id
     * @return 子类
     */
    public T setId(I id)
    {
        this.id = id;
        return self();
    }

    /**
     * 设置父id
     *
     * @param parentId 父id
     * @return 子类
     */
    public T setParentId(I parentId)
    {
        this.parentId = parentId;
        return self();
    }

    /**
     * 设置子节点列表
     *
     * @param children 子节点列表
     * @return 子类
     */
    public T setChildren(List<T> children)
    {
        this.children = children;
        return self();
    }

    /**
     * 是否有父节点
     *
     * @param hasParent 是否有父节点
     * @return 子类
     */
    public T setHasParent(boolean hasParent)
    {
        this.hasParent = hasParent;
        return self();
    }

    /**
     * 是否有子节点
     *
     * @param hasChild 是否有子节点
     * @return 子类
     */
    public T setHasChild(boolean hasChild)
    {
        this.hasChild = hasChild;
        return self();
    }

    /**
     * 设置数据
     *
     * @param data 数据
     * @return 子类
     */
    public T setData(D data)
    {
        this.data = data;
        return self();
    }

}
