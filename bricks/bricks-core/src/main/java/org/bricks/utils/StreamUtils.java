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

package org.bricks.utils;

import static com.google.common.primitives.Bytes.asList;
import static java.util.Spliterator.SORTED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

/**
 * Stream工具类
 *
 * @author fuzy
 *
 */
@UtilityClass
public class StreamUtils
{

    /**
     * 字节数组转Stream
     * 
     * @param bytes 字节数组
     * @return Stream
     */
    public static Stream<Byte> toStream(byte[] bytes)
    {
        return asList(bytes).stream();
    }

    /**
     * Iterable转Stream
     *
     * @param iterable iterable
     * @param parallel 是否并行
     * @param <T> 泛型
     * @return Stream
     */
    public static <T> Stream<T> toStream(Iterable<T> iterable, boolean parallel)
    {
        return stream(iterable.spliterator(), parallel);
    }

    /**
     * Iterator转Stream
     *
     * @param iterator Iterator
     * @param parallel 是否并行
     * @param <T> 泛型
     * @return Stream
     */
    public static <T> Stream<T> toStream(Iterator<T> iterator, boolean parallel)
    {
        return stream(spliteratorUnknownSize(iterator, SORTED), parallel);
    }

    /**
     * Enumeration转Stream
     * 
     * @param enumeration 枚举
     * @param parallel 是否并行
     * @param <T> 泛型
     * @return Stream
     */
    public static <T> Stream<T> toStream(Enumeration<T> enumeration, boolean parallel)
    {
        return toStream(new Iterator<T>()
        {

            @Override
            public T next()
            {
                return enumeration.nextElement();
            }

            @Override
            public boolean hasNext()
            {
                return enumeration.hasMoreElements();
            }

        }, parallel);
    }

}
