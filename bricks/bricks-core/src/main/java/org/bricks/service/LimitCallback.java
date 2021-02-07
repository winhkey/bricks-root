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

package org.bricks.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 限量回调
 *
 * @author fuzy
 */
@Service
public class LimitCallback {

    /**
     *
     * @param collection 集合
     * @param limit 分批
     * @param callback 回调
     * @param <T> 源
     * @param <V> 目标
     * @return 列表
     */
    public <T, V> List<V> limit(Collection<T> collection, int limit, Callback<List<T>, List<V>> callback) {
        List<V> vList = newArrayList();
        if (isNotEmpty(collection)) {
            List<T> list = newArrayList(collection);
            int length = collection.size();
            int page = length / limit;
            int mod = length % limit;
            int i = 0;
            List<V> subList;
            try {
                for (; i < page; i++) {
                    subList = callback.call(list.subList(i * limit, (i + 1) * limit));
                    addSubList(vList, subList);
                }
                if (mod > 0) {
                    subList = callback.call(list.subList(i * limit, length));
                    addSubList(vList, subList);
                }
            } finally {
                list.clear();
            }
        }
        return vList;
    }

    /**
     * 添加子列表
     *
     * @param list 列表
     * @param subList 子列表
     * @param <V> 目标类型
     */
    private <V> void addSubList(List<V> list, List<V> subList) {
        if (isNotEmpty(subList)) {
            list.addAll(subList);
        }
    }

}
