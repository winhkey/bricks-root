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

import static java.lang.ThreadLocal.withInitial;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.w3c.dom.Element;

/**
 * map支持
 *
 * @author fuzy
 *
 */
public class MapAdapter extends XmlAdapter<Element, Map<String, String>>
{

    /**
     * 线程变量
     */
    private static final ThreadLocal<Map<String, String>> MAP_THREAD_LOCAL = withInitial(LinkedHashMap::new);

    /**
     * 清除线程变量
     */
    public static void clear()
    {
        MAP_THREAD_LOCAL.remove();
    }

    @Override
    public Map<String, String> unmarshal(Element v)
    {
        if (v == null)
        {
            return null;
        }
        Map<String, String> map = MAP_THREAD_LOCAL.get();
        map.put(v.getLocalName(), v.getTextContent());
        return map;
    }

    @Override
    public Element marshal(Map<String, String> v)
    {
        // 在同级的类中定义一个List<JAXBElement<String>>类型的属性作为marshal时Map的替代，而Map的marshal此时将返回null
        return null;
    }

}
