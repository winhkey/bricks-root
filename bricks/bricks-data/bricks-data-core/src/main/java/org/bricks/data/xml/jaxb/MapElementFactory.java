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

package org.bricks.data.xml.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * Element工厂
 *
 * @author fuzy
 *
 */
@XmlRegistry
public class MapElementFactory
{

    /**
     * 创建
     *
     * @param key 键
     * @param value 值
     * @return element
     */
    @XmlElementDecl(name = "list")
    public JAXBElement<String> create(String key, String value)
    {
        return new JAXBElement<>(new QName(key), String.class, value);
    }

}
