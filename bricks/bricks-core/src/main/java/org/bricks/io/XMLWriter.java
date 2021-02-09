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

package org.bricks.io;

import java.io.BufferedWriter;
import java.io.Closeable;

import org.dom4j.io.OutputFormat;

/**
 * 封装dom4j的XMLWriter添加Closeable接口
 * 
 * @author fuzy
 *
 */
public class XMLWriter extends org.dom4j.io.XMLWriter implements Closeable
{

    /**
     * 构造方法
     * 
     * @param writer writer
     * @param format format
     */
    public XMLWriter(BufferedWriter writer, OutputFormat format)
    {
        super(writer, format);
    }

}
