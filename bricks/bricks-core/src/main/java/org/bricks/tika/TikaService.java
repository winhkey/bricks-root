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

package org.bricks.tika;

import static java.nio.file.Files.newInputStream;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.tika.Tika;
import org.bricks.exception.BaseException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * 文件类型
 *
 * @author fuzhiying
 *
 */
@Getter
@Setter
@Service
@ConfigurationProperties(prefix = "bricks.mime")
public class TikaService
{

    /**
     * mime type
     */
    private Map<String, String> map;

    /**
     * tika
     */
    @Resource
    private Tika tika;

    /**
     * 判断文件类型
     *
     * @param name 文件名
     * @param stream 文件流
     * @return 类型
     */
    @SneakyThrows
    public String getMimeType(String name, InputStream stream)
    {
        return tika.detect(stream, name);
    }

    /**
     * 判断文件类型
     *
     * @param file 文件
     * @return 类型
     */
    public String getMimeType(File file)
    {
        try (InputStream stream = newInputStream(file.toPath()))
        {
            return getMimeType(file.getName(), stream);
        }
        catch (Exception e)
        {
            throw new BaseException(e);
        }
    }

}
