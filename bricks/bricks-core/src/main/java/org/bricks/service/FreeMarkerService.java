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

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.newBufferedWriter;
import static java.text.MessageFormat.format;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.annotation.Resource;

import org.bricks.annotation.NoLog;
import org.bricks.exception.BaseException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * freemarker服务
 *
 * @author fuzy
 *
 */
@Slf4j
@Service
@Scope("prototype")
public class FreeMarkerService
{

    /**
     * 配置信息
     */
    @Resource
    private Configuration configuration;

    /**
     * 模板文件
     */
    private String templateFile;

    @NoLog
    public void setTemplateFile(String templateFile)
    {
        this.templateFile = templateFile;
    }

    /**
     * 构建文件
     *
     * @param fileName 文件路径
     * @param rootMap 数据
     * @param clear 是否清除数据
     */
    public void build(String fileName, Map<String, Object> rootMap, boolean clear)
    {
        if (isBlank(fileName))
        {
            return;
        }
        Path path = Paths.get(fileName);
        Path dir = path.getParent();
        if (!Files.exists(dir))
        {
            try
            {
                dir = Files.createDirectories(dir);
                if (Files.exists(dir))
                {
                    throw new BaseException(format("{0} can not be created.", dir));
                }
            }
            catch (IOException e)
            {
                throw new BaseException(format("{0} can not be created.", dir), e);
            }
        }
        try (Writer writer = newBufferedWriter(path, forName(configuration.getDefaultEncoding())))
        {
            build(writer, rootMap, clear);
        }
        catch (Throwable e)
        {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 将数据写入流
     *
     * @param writer writer
     * @param rootMap 数据
     * @param clear 是否清除数据
     */
    public void build(Writer writer, Map<String, Object> rootMap, boolean clear)
    {
        if (writer != null)
        {
            try (Writer w = writer)
            {
                Template boardTemplate = configuration.getTemplate(templateFile);
                boardTemplate.process(rootMap, w);
                w.flush();
            }
            catch (TemplateNotFoundException e)
            {
                log.error("未找到模板文件: {}", templateFile, e);
            }
            catch (Throwable e)
            {
                log.error("调用模板失败: {}", e.getMessage(), e);
            }
        }
        clear(rootMap, clear);
    }

    /**
     * 生成字符串
     *
     * @param rootMap 数据
     * @param clear 是否清除数据
     * @return 结果
     */
    public String buildString(Map<String, Object> rootMap, boolean clear)
    {
        StringWriter writer = new StringWriter();
        build(writer, rootMap, clear);
        return writer.toString();
    }

    private void clear(Map<String, Object> rootMap, boolean clear)
    {
        if (clear && isNotEmpty(rootMap))
        {
            rootMap.clear();
        }
    }

}
