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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newConcurrentMap;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newInputStream;
import static java.util.Arrays.asList;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.bricks.data.AbstractDataService;
import org.bricks.data.xml.XmlDataService;
import org.bricks.exception.BaseException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * JAXB转换XML和java bean
 *
 * @author fuzy
 *
 */
@Service("jaxbService")
public class JaxbServiceImpl extends AbstractDataService implements XmlDataService
{

    /**
     * JAXBContext缓存
     */
    private static final Map<List<Class<?>>, JAXBContext> JAXB_CONTEXT_MAP = newConcurrentMap();

    @Override
    protected <T> T readFrom(String content, Type... type)
    {
        try (Reader reader = new StringReader(content))
        {
            Unmarshaller unmarshaller = buildUnmarshaller(parse(type));
            return unmarshal(unmarshaller, reader);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected <T> T readFrom(File file, Type... type)
    {
        try (InputStream stream = newInputStream(file.toPath()))
        {
            return readFrom(stream, type);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected <T> T readFrom(InputStream stream, Type... type)
    {
        try (Reader reader = new InputStreamReader(stream, UTF_8))
        {
            Unmarshaller unmarshaller = buildUnmarshaller(parse(type));
            return unmarshal(unmarshaller, reader);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T convertFrom(Object object, Type... type)
    {
        try
        {
            Unmarshaller unmarshaller = buildUnmarshaller(parse(type));
            return unmarshaller.unmarshal((Node) object, (Class<T>) type[0])
                    .getValue();
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    protected String toString(Object object, Type... type)
    {
        try
        {
            Marshaller marshaller = buildMarshaller(parse(type));
            StringWriter sw = new StringWriter();
            marshaller.marshal(object, sw);
            return sw.toString()
                    .replace(" standalone=\"yes\"", "");
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    /**
     * 类型强转
     *
     * @param types Type类型
     * @return class类型
     */
    private Class<?>[] parse(Type... types)
    {
        if (isEmpty(types))
        {
            throw new IllegalArgumentException("Unknown Type");
        }
        return of(types).map(type -> (Class<?>) type)
                .toArray(Class[]::new);
    }

    /**
     * 构建序列化
     *
     * @param clazz 类型列表
     * @return 序列化
     * @throws JAXBException JAXBException
     */
    private Marshaller buildMarshaller(Class<?>... clazz) throws JAXBException
    {
        JAXBContext jaxb = getJaxbContext(clazz);
        return jaxb.createMarshaller();
    }

    /**
     * 构建反序列化
     *
     * @param clazz 类型列表
     * @return 反序列化
     * @throws JAXBException JAXBException
     */
    private Unmarshaller buildUnmarshaller(Class<?>... clazz) throws JAXBException
    {
        JAXBContext jaxb = getJaxbContext(clazz);
        return jaxb.createUnmarshaller();
    }

    @SuppressWarnings("unchecked")
    private <T> T unmarshal(Unmarshaller unmarshaller, Reader reader)
    {
        try
        {
            SAXParserFactory sax = SAXParserFactory.newInstance();
            sax.setNamespaceAware(false);
            XMLReader xmlReader = sax.newSAXParser()
                    .getXMLReader();
            Source source = new SAXSource(xmlReader, new InputSource(reader));
            return (T) unmarshaller.unmarshal(source);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

    private JAXBContext getJaxbContext(Class<?>... classes) throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance(classes);
        JAXB_CONTEXT_MAP.putIfAbsent(newArrayList(asList(classes)), context);
        return context;
    }

}
