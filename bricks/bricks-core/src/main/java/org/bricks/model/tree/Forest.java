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
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.IterableUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import org.bricks.bean.AbstractBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 森林
 *
 * @author fuzy
 * 
 * @param <I> 主键
 * @param <T> 树
 */
@Setter
@Getter
@Accessors(chain = true)
public class Forest<I, T extends Tree<I>> extends AbstractBean
{

    /**
     * 是否缓存节点
     */
    private boolean cacheNode;

    /**
     * 是否缓存父节点
     */
    private boolean cacheParent;

    /**
     * 是否缓存兄弟节点
     */
    private boolean cacheBrother;

    /**
     * 平铺树节点，data与树节点关系
     */
    private Map<T, TreeNode<I, T>> treeMap = new HashMap<>();

    /**
     * 平铺树节点，id与树节点关系
     */
    private Map<I, TreeNode<I, T>> idTreeMap = new HashMap<>();

    /**
     * 构造方法
     *
     * @param cacheNode 缓存本节点
     * @param cacheParent 缓存父节点
     * @param cacheBrother 缓存同级
     */
    public Forest(boolean cacheNode, boolean cacheParent, boolean cacheBrother)
    {
        super();
        this.cacheNode = cacheNode;
        this.cacheParent = cacheParent;
        this.cacheBrother = cacheBrother;
    }

    /**
     * 构建森林
     *
     * @param rootList 根节点列表
     * @return 森林
     */
    public Forest<I, T> build(List<TreeNode<I, T>> rootList)
    {
        ofNullable(rootList).ifPresent(list -> list.forEach(treeRoot -> pushStack(treeRoot, this::insertMap)));
        return this;
    }

    /**
     * 压栈
     *
     * @param node 节点
     * @param consumer 操作
     */
    public void pushStack(TreeNode<I, T> node, Consumer<TreeNode<I, T>> consumer)
    {
        TreeNode<I, T> tree;
        Stack<TreeNode<I, T>> stack = new Stack<>();
        ofNullable(node).ifPresent(stack::push);
        while (!stack.empty())
        {
            tree = stack.pop();
            consumer.accept(tree);
            tree.getChildren()
                    .forEach(stack::push);
        }
    }

    private void insertMap(TreeNode<I, T> treeNode)
    {
        idTreeMap.put(treeNode.getId(), treeNode);
        if (cacheNode)
        {
            treeMap.put(treeNode.getTree(), treeNode);
        }
    }

    private void updateMap(I oldTreeNodeId, T oldTreeNode, TreeNode<I, T> newTreeNode)
    {
        idTreeMap.remove(oldTreeNodeId);
        idTreeMap.put(newTreeNode.getId(), newTreeNode);
        if (cacheNode)
        {
            treeMap.remove(oldTreeNode);
            treeMap.put(newTreeNode.getTree(), newTreeNode);
        }
    }

    private void deleteMap(TreeNode<I, T> treeNode)
    {
        List<TreeNode<I, T>> childrenNode = treeNode.getChildren();
        if (isNotEmpty(childrenNode))
        {
            idTreeMap.remove(treeNode.getId());
            if (cacheNode)
            {
                treeMap.remove(treeNode.getTree());
            }
            childrenNode.forEach(this::deleteMap);
        }
    }

    /**
     * 根据ids获取treeList
     *
     * @param ids id列表
     * @return 树列表
     */
    public List<TreeNode<I, T>> getNodeList(List<I> ids)
    {
        List<TreeNode<I, T>> treeNodes = new ArrayList<>();
        ids.forEach(id -> treeNodes.add(idTreeMap.get(id)));
        return treeNodes;
    }

    /**
     * 插入子节点
     *
     * @param parentNode 父节点
     * @param tree 子树
     * @return 新节点
     */
    public TreeNode<I, T> insertTreeNode(TreeNode<I, T> parentNode, T tree)
    {
        TreeNode<I, T> treeNode = new TreeNode<>();
        treeNode.setTree(tree)
                .setParentIdTree(treeNode.initParent());
        // 更新子节点的同级id
        for (TreeNode<I, T> node : parentNode.getChildren())
        {
            node.getBrotherIdList()
                    .add(treeNode.getId());
            if (isEmpty(treeNode.getBrotherIdList()))
            {
                treeNode.setBrotherIdList(node.getBrotherIdList());
            }
        }
        // 添加的是叶子节点时，设置同级id
        if (treeNode.getBrotherIdList()
                .isEmpty())
        {
            treeNode.setBrotherIdList(Collections.singletonList(treeNode.getId()));
        }
        // 更新映射
        insertMap(treeNode);
        return treeNode;
    }

    /**
     * 根据id删除节点
     *
     * @param treeNodeId 节点id
     */
    public void deleteTreeNode(I treeNodeId)
    {
        TreeNode<I, T> tTreeNode = idTreeMap.get(treeNodeId);
        deleteTreeNode(tTreeNode);
    }

    /**
     * 删除节点
     *
     * @param treeNode 节点
     */
    public void deleteTreeNode(TreeNode<I, T> treeNode)
    {
        // 处理父节点引用
        List<I> parentNodes = treeNode.getParentIdTree();
        I parentId = parentNodes.get(parentNodes.size() - 1);
        TreeNode<I, T> parentNode = idTreeMap.get(parentId);
        // 删除节点
        parentNode.getChildren()
                .remove(treeNode);
        // 更新同级节点
        List<TreeNode<I, T>> peerNode = parentNode.getChildren();
        peerNode.forEach(m -> m.getBrotherIdList()
                .remove(treeNode.getId()));
        // 更新映射
        deleteMap(treeNode);
    }

    /**
     * 修改节点内容
     *
     * @param oldNodeId 节点id
     * @param newNode 新节点
     * @return 改后的节点
     */
    public TreeNode<I, T> updateTreeNode(I oldNodeId, TreeNode<I, T> newNode)
    {
        TreeNode<I, T> oldNode = idTreeMap.get(oldNodeId);
        final T oldNodeData = oldNode.getTree();
        // 节点参数
        oldNode.setTree(newNode.getTree());
        // 修改同级节点的id
        List<I> parentNodes = oldNode.getParentIdTree();
        TreeNode<I, T> parentNode = idTreeMap.get(parentNodes.get(parentNodes.size() - 1));
        parentNode.getChildren()
                .forEach(treeNode ->
                {
                    treeNode.getBrotherIdList()
                            .remove(oldNodeId);
                    treeNode.getBrotherIdList()
                            .add(newNode.getId());
                });

        // 修改子节点的追溯
        pushStack(oldNode, m ->
        {
            List<I> parent = m.getParentIdTree();
            int oldNodeIndex = parent.indexOf(oldNodeId);
            if (oldNodeIndex >= 0)
            {
                parent.set(oldNodeIndex, newNode.getId());
            }
        });
        // 更新映射
        updateMap(oldNodeId, oldNodeData, oldNode);
        return oldNode;
    }

    /**
     * 根据data从缓存获取树
     *
     * @param data 数据
     * @return 树节点
     */
    public TreeNode<I, T> getTreeNode(T data)
    {
        return cacheNode ? treeMap.get(data) : null;
    }

}
