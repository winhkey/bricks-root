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

package org.bricks.data.bean;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import org.bricks.bean.AbstractBean;
import org.bricks.data.xml.jaxb.MapAdapter;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * map封装
 *
 * @author fuzy
 *
 * @param <T> 子类
 */
@Getter
@Accessors(chain = true)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractJaxbMapWrapper<T extends AbstractJaxbMapWrapper<T>> extends AbstractBean
{

    /**
     * map
     */
    @XmlJavaTypeAdapter(MapAdapter.class)
    @XmlAnyElement
    private Map<String, String> map;

    /**
     * list
     */
    @XmlElementRef(name = "list")
    private List<JAXBElement<String>> list;

    /**
     * 设置map
     *
     * @param map map
     * @return 对象
     */
    public T setMap(Map<String, String> map)
    {
        this.map = map;
        return self();
    }

    /**
     * before marshaller
     * 
     * @param marshaller marshaller
     */
    public void beforeMarshal(Marshaller marshaller)
    {
        if (isNotEmpty(map))
        {
            list = map.entrySet()
                    .stream()
                    .map(entry -> new JAXBElement<>(new QName(entry.getKey()), String.class, entry.getValue()))
                    .collect(toList());
        }
    }

    /**
     * before marshaller
     * 
     * @param marshaller marshaller
     */
    public void afterMarshal(Marshaller marshaller)
    {
        MapAdapter.clear();
    }

}
