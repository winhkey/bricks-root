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
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 树操作
 *
 * @author fuzy
 *
 * @param <I> id
 * @param <D> 树
 */
@Service
public class TreeService<I, D extends Tree<I>>
{

    /**
     * 从数据列表里生成树列表
     *
     * @param dataList 数据列表
     * @return 树列表
     */
    public List<TreeNode<I, D>> getTrees(List<D> dataList)
    {
        return ofNullable(dataList).map(list ->
        {
            List<TreeNode<I, D>> treeList = new ArrayList<>();
            while (!list.isEmpty())
            {
                treeList.add(getTree(list));
            }
            return treeList;
        })
                .orElse(null);
    }

    private TreeNode<I, D> getTree(List<D> dataList)
    {
        TreeNode<I, D> rootNode = getRoot(dataList);
        ofNullable(rootNode).ifPresent(root -> children(dataList, root.getChildren()));
        return rootNode;
    }

    private TreeNode<I, D> getRoot(List<D> dataList)
    {
        TreeNode<I, D> rootNode = null;
        if (isNotEmpty(dataList))
        {
            rootNode = new TreeNode<>();
            D node = dataList.get(0);
            D root = getRoot(dataList, node);
            dataList.remove(root);
            rootNode.setTree(root);
            rootNode.convert(dataList);
        }
        return rootNode;
    }

    private D getRoot(List<D> dataList, D node)
    {
        return dataList.stream()
                .filter(data -> data.getId()
                        .equals(node.getParentId()))
                .findFirst()
                .map(n -> getRoot(dataList, n))
                .orElse(node);
    }

    private void children(List<D> dataList, List<TreeNode<I, D>> children)
    {
        List<TreeNode<I, D>> nodeList = children.stream()
                .map(treeNode ->
                {
                    treeNode.convert(dataList);
                    return treeNode.getChildren();
                })
                .flatMap(Collection::stream)
                .collect(toList());
        if (isNotEmpty(nodeList))
        {
            children(dataList, nodeList);
        }
    }

}
