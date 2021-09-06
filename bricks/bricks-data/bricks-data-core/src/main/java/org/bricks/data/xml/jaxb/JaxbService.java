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

import static com.google.common.collect.Maps.newConcurrentMap;
import static java.lang.String.join;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newInputStream;
import static java.util.Arrays.copyOf;
import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;
import static javax.xml.bind.JAXBContext.newInstance;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.utils.FunctionUtils.apply;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import lombok.SneakyThrows;

/**
 * JAXB转换XML和java bean
 *
 * @author fuzy
 *
 */
@Service("jaxbService")
public class JaxbService extends AbstractDataService implements XmlDataService
{

    /**
     * 路径列表
     */
    @Value("${bricks.jaxb.context-paths:}")
    private List<String> contextPaths;

    /**
     * JAXBContext缓存
     */
    private static final Map<String, JAXBContext> JAXB_CONTEXT_MAP = newConcurrentMap();

    /**
     * 全局JAXBContext
     */
    private JAXBContext jaxbContext;

    /**
     * 初始化
     */
    @PostConstruct
    @SneakyThrows
    public void init()
    {
        if (isNotEmpty(contextPaths))
        {
            jaxbContext = newInstance(join(":", contextPaths));
        }
    }

    @Override
    protected <T> T readFrom(String content, Type... type)
    {
        try (Reader reader = new StringReader(content))
        {
            Unmarshaller unmarshaller = buildUnmarshaller(parse(type));
            return unmarshal(unmarshaller, reader);
        }
        catch (Throwable e)
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
        catch (Throwable e)
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
        catch (Throwable e)
        {
            throw new BaseException(e);
        }
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    protected <T> T convertFrom(Object object, Type... type)
    {
        try
        {
            Unmarshaller unmarshaller = buildUnmarshaller(parse(type));
            return unmarshaller.unmarshal((Node) object, (Class<T>) type[0])
                    .getValue();
        }
        catch (Throwable e)
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
        catch (Throwable e)
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
        Type[] t;
        if (isEmpty(types))
        {
            t = new Type[] {MapElementFactory.class};
        }
        else
        {
            t = copyOf(types, types.length + 1);
            t[t.length - 1] = MapElementFactory.class;
        }
        return of(t).map(type -> (Class<?>) type)
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

    @SuppressWarnings(UNCHECKED)
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
        catch (Throwable e)
        {
            throw new BaseException(e);
        }
    }

    private JAXBContext getJaxbContext(Class<?>... classes)
    {
        return ofNullable(jaxbContext).orElseGet(() -> JAXB_CONTEXT_MAP.computeIfAbsent(getCacheKey(classes),
                apply(k -> newInstance(classes), null, null, log, null)));
    }

}
