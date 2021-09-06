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

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.bricks.bean.AbstractBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 树节点
 *
 * @author fuzy
 *
 * @param <I> 主键
 * @param <T> 树
 */
@Setter
@Getter
@Accessors(chain = true)
public class TreeNode<I, T extends Tree<I>> extends AbstractBean
{

    /**
     * 树实体
     */
    private T tree;

    /**
     * 直接子节点
     */
    private List<TreeNode<I, T>> children = new ArrayList<>();

    /**
     * 追溯父节点Id，0 -&gt; ...... -&gt; parentId
     */
    private List<I> parentIdTree = new ArrayList<>();

    /**
     * 同级节点Id
     */
    private List<I> brotherIdList = new ArrayList<>();

    /**
     * 获取子节点
     *
     * @param treeList 数据集合
     */
    public void convert(List<T> treeList)
    {
        List<T> collect = treeList.stream()
                .filter(tree -> this.tree.getId()
                        .equals(tree.getParentId()))
                .collect(toList());
        List<I> childIdList = collect.stream()
                .map(Tree::getId)
                .collect(toList());
        treeList.removeAll(collect);
        collect.forEach(t -> children.add(new TreeNode<I, T>().setTree(t)
                .setParentIdTree(initParent())
                .setBrotherIdList(childIdList)));
    }

    /**
     * @return 初始化父节点
     */
    public List<I> initParent()
    {
        return new ArrayList<>(parentIdTree);
    }

    /**
     * @return 节点id
     */
    public I getId()
    {
        return ofNullable(tree).map(Tree::getId)
                .orElse(null);
    }

}
