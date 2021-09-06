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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * w3c xml解析工具类
 *
 * @author fuzy
 *
 */
@Slf4j
@UtilityClass
public class W3cXmlUtils
{

    /**
     * 线程安全的builder
     */
    private static final ThreadLocal<DocumentBuilder> DOC_BUILDER_INS = ThreadLocal.withInitial(() ->
    {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try
        {
            return domFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    });

    /**
     * 线程安全的xpath对象
     */
    private static final ThreadLocal<XPath> XPATH_INS = ThreadLocal.withInitial(() -> XPathFactory.newInstance()
            .newXPath());

    /**
     * 根据uri得到doc
     *
     * @param uri 网址
     * @return Document
     */
    public static Document parseDocument(String uri)
    {
        Document doc = null;
        if (uri == null)
        {
            log.error("uri is null");
        }
        else
        {
            try
            {
                doc = DOC_BUILDER_INS.get()
                        .parse(uri);
            }
            catch (Throwable e)
            {
                log.error("Can't parse the uri's response as XML.", e);
            }
        }
        return doc;
    }

    /**
     * 根据输入流得到doc
     *
     * @param stream 输入流
     * @return Document
     * @throws SAXException 解析异常
     * @throws IOException IO异常
     */
    public static Document parseDocument(InputStream stream) throws SAXException, IOException
    {
        Document doc = null;
        if (stream == null)
        {
            log.error("stream is null");
        }
        else
        {
            doc = DOC_BUILDER_INS.get()
                    .parse(stream);
        }
        return doc;
    }

    /**
     * 根据文件得到doc
     *
     * @param file xml文件
     * @return Document
     */
    public static Document parseDocument(File file)
    {
        Document doc = null;
        if (file == null || !file.exists() || !file.isFile())
        {
            log.error("file is null or error");
        }
        else
        {
            try
            {
                doc = DOC_BUILDER_INS.get()
                        .parse(file);
            }
            catch (Throwable e)
            {
                log.error(MessageFormat.format("Can't parse the file's content as XML. File is[{0}]",
                        file.getAbsolutePath()), e);
            }
        }
        return doc;
    }

    /**
     * 根据xml字符串得到doc
     *
     * @param xml xml字符串
     * @param encoding 编码
     * @return Document
     */
    public static Document parseXMLDocument(String xml, String encoding)
    {
        Document doc = null;
        if (isNotBlank(xml))
        {
            try (ByteArrayInputStream stream = new ByteArrayInputStream(
                    isNotBlank(encoding) ? xml.getBytes(encoding) : xml.getBytes(UTF_8.name())))
            {
                doc = parseDocument(stream);
            }
            catch (Throwable e)
            {
                log.error("Can't parse the string as XML.{}, {}", xml, encoding, e);
            }
        }
        return doc;
    }

    /**
     * xpath获取节点列表
     *
     * @param node 节点
     * @param xpath xpath
     * @return Document
     */
    public static NodeList getNodeList(Node node, String xpath)
    {
        return (NodeList) evaluate(node, xpath, XPathConstants.NODESET);
    }

    /**
     * xpath获取节点
     *
     * @param node 节点
     * @param xpath xpath
     * @return Node
     */
    public static Node getNode(Node node, String xpath)
    {
        return (Node) evaluate(node, xpath, XPathConstants.NODE);
    }

    /**
     * xpath获取字符串值
     *
     * @param node 节点
     * @param xpath xpath
     * @return string
     */
    public static String getNodeValue(Node node, String xpath)
    {
        return (String) evaluate(node, xpath, XPathConstants.STRING);
    }

    /**
     * xpath计算
     *
     * @param node 节点
     * @param xPath xpath
     * @param returnType 返回类型
     * @return 返回值
     */
    private static Object evaluate(Node node, String xPath, QName returnType)
    {
        Object result = null;
        try
        {
            XPathExpression expr = XPATH_INS.get()
                    .compile(xPath);
            result = expr.evaluate(node, returnType);
        }
        catch (XPathExpressionException e)
        {
            log.error("xpath evaluate error.", e);
        }
        return result;
    }

    /**
     * xml字符串
     *
     * @param node 节点
     * @param encoding 编码
     * @return string
     */
    public static String asXML(Node node, String encoding)
    {
        String xml = null;
        if (node != null)
        {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream())
            {
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                transformer.transform(new DOMSource(node), new StreamResult(stream));
                xml = stream.toString(encoding);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return xml;
    }

}
