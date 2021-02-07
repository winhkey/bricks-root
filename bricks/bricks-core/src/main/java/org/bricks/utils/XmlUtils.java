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

import static com.google.common.collect.Maps.newHashMap;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.utils.FunctionUtils.apply;
import static org.dom4j.DocumentHelper.parseText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.bricks.io.XMLWriter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.jaxen.XPathFunctionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import lombok.experimental.UtilityClass;

/**
 * xml工具类
 *
 * @author fuzy
 *
 */
@UtilityClass
public class XmlUtils
{

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(XmlUtils.class);

    static
    {
        XPathFunctionContext context = (XPathFunctionContext) XPathFunctionContext.getInstance();
        context.registerFunction(null, "matches", new MatchesFunction());
        context.registerFunction(null, "replace", new ReplaceFunction());
    }

    /**
     * 根据输入流得到doc
     *
     * @param stream 输入流
     * @return doc
     * @throws DocumentException DocumentException
     */
    public static Document getDocument(InputStream stream) throws DocumentException
    {
        return ofNullable(stream).map(apply(s -> new SAXReader().read(s), null, log, null))
                .orElse(null);
    }

    /**
     * 根据URL得到doc
     *
     * @param url url
     * @return doc doc
     * @throws DocumentException DocumentException
     */
    public static Document getDocument(URL url) throws DocumentException
    {
        return ofNullable(url).map(apply(u -> new SAXReader().read(u), null, log, null))
                .orElse(null);
    }

    /**
     * 根据文件得到doc
     *
     * @param xmlFile xml文件
     * @return doc
     */
    public static Document parseXMLFile(File xmlFile)
    {
        Document doc = null;
        if (xmlFile == null || !xmlFile.exists() || !xmlFile.isFile())
        {
            log.error("xmlFile is null or error");
        }
        else
        {
            try (InputStream in = newInputStream(xmlFile.toPath()))
            {
                doc = getDocument(in);
            }
            catch (FileNotFoundException e)
            {
                log.error("File does not exists.");
            }
            catch (IOException e)
            {
                log.error(e.getMessage(), e);
            }
            catch (DocumentException e)
            {
                log.error("Can't parse the content as XML. File is[{}]", xmlFile.getPath(), e);
            }
        }
        return doc;
    }

    /**
     * 根据xml字符串得到doc
     *
     * @param xml xml字符串
     * @return doc
     */
    public static Document parseXML(String xml)
    {
        return ofNullable(xml).map(apply(x -> parseText(x), null, log, null))
                .orElse(null);
    }

    /**
     * 将doc写入文件
     *
     * @param path 文件路径
     * @param doc doc
     * @param encoding 字符集
     */
    public static void writeToFile(String path, Document doc, String encoding)
    {
        if (path == null)
        {
            return;
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(encoding);
        try (BufferedWriter writer = newBufferedWriter(get(path), forName(encoding));
                XMLWriter xmlWriter = new XMLWriter(writer, format))
        {
            xmlWriter.write(doc);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 根据xpath得到元素列表
     *
     * @param node 节点
     * @param xpath xpath
     * @param namespaces 命名空间
     * @return 子节点列表
     */
    public static List<Node> selectNodes(Node node, String xpath, Namespace... namespaces)
    {
        return ofNullable(node).filter(n -> isNotBlank(xpath))
                .map(n -> isNotEmpty(namespaces) ? buildXPath(node, xpath, namespaces).selectNodes(node)
                        : node.selectNodes(xpath))
                .orElseGet(() -> emptyList());
    }

    /**
     * 根据xpath删除元素列表
     *
     * @param node 节点
     * @param xpath xpath
     * @param namespaces 命名空间
     */
    public static void removeNodes(Node node, String xpath, Namespace... namespaces)
    {
        List<Node> removes = selectNodes(node, xpath, namespaces);
        if (isNotEmpty(removes))
        {
            removes.forEach(remove -> remove.detach());
        }
    }

    /**
     * 根据xpath得到节点
     *
     * @param node 节点
     * @param xpath xpath
     * @param namespaces 命名空间
     * @return 子节点
     */
    public static Node selectSingleNode(Node node, String xpath, Namespace... namespaces)
    {
        return ofNullable(node).filter(n -> isNotBlank(xpath))
                .map(n -> isNotEmpty(namespaces) ? buildXPath(node, xpath, namespaces).selectSingleNode(node)
                        : node.selectSingleNode(xpath))
                .orElse(null);
    }

    /**
     * 根据xpath得到节点值
     *
     * @param node 节点
     * @param xpath xpath
     * @param namespaces 命名空间
     * @return 子节点值
     */
    public static String selectSingleNodeValue(Node node, String xpath, Namespace... namespaces)
    {
        return ofNullable(selectSingleNode(node, xpath, namespaces)).map(subNode -> subNode.getText())
                .filter(value -> isNotBlank(value))
                .orElse("");
    }

    /**
     * 得到节点的属性值
     *
     * @param node 节点
     * @param attrName 属性名
     * @param namespaces 命名空间
     * @return 属性值
     */
    public static String getAttributeValue(Node node, String attrName, Namespace... namespaces)
    {
        return ofNullable(node).filter(n -> isNotBlank(attrName))
                .map(n -> selectSingleNode(n, "@" + attrName, namespaces))
                .map(subNode -> subNode.getText())
                .filter(value -> isNotBlank(value))
                .orElse("");
    }

    /**
     * 得到节点的子节点值
     *
     * @param node 节点
     * @param nodeName 子节点
     * @param namespaces 命名空间
     * @return 子节点值
     */
    public static String getNodeValue(Node node, String nodeName, Namespace... namespaces)
    {
        return ofNullable(node).filter(n -> isNotBlank(nodeName))
                .map(n -> selectSingleNode(n, nodeName, namespaces))
                .map(subNode -> subNode.getText())
                .filter(value -> isNotBlank(value))
                .orElse("");
    }

    /**
     * 根据schema验证xml
     *
     * @param xsdUrl schema文件url
     * @param node xml节点
     * @return 验证结果
     */
    public static boolean validateXsd(URL xsdUrl, Node node)
    {
        return ofNullable(xsdUrl).filter(url -> node != null)
                .map(apply(url -> validateXsd(url, new DocumentSource(node)), null, log, null))
                .orElse(false);
    }

    /**
     * 根据schema验证xml
     *
     * @param xsdUrl schema文件url
     * @param source xml输入流
     * @return 验证结果
     * @throws SAXException 解析异常
     * @throws IOException IO异常
     */
    public static boolean validateXsd(URL xsdUrl, Source source) throws IOException, SAXException
    {
        boolean flag = false;
        if (xsdUrl == null || source == null)
        {
            log.error("xsdUrl or source is null");
        }
        else
        {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(xsdUrl);
            Validator validator = schema.newValidator();
            validator.validate(source);
            flag = true;
        }
        return flag;
    }

    /**
     * 构建xpath对象
     *
     * @param node 节点
     * @param xpath xpath
     * @param namespaces 命名空间
     * @return xpath对象
     */
    public static XPath buildXPath(Node node, String xpath, Namespace... namespaces)
    {
        XPath path = node.createXPath(xpath);
        if (isNotEmpty(namespaces))
        {
            Map<String, String> nsMap = newHashMap();
            of(namespaces).forEach(ns -> nsMap.put(ns.getPrefix(), ns.getUri()));
            path.setNamespaceURIs(nsMap);
        }
        return path;
    }

}
