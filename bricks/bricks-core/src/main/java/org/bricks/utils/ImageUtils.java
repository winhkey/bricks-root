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

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.utils.RegexUtils.regularGroupMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 图片工具类
 *
 * @author fuzy
 *
 */
@Slf4j
@UtilityClass
public class ImageUtils
{

    /**
     * base64正则
     */
    private static final Pattern BASE64IMG_PATTERN = Pattern.compile("data:image/(.*);base64,(.*)");

    /**
     * 将图片base64编码
     *
     * @param path 图片路径
     * @return base64编码
     */
    public static String base64Encode(String path)
    {
        try (InputStream in = newInputStream(Paths.get(path)))
        {
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            return Base64.getEncoder()
                    .encodeToString(bytes);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将base64编码的图片解码
     *
     * @param base64 base64编码
     * @param path 保存路径
     * @return 解码结果
     */
    public static String base64Decode(String base64, String path)
    {
        String filePath = path;
        if (isNotBlank(base64) && isNotBlank(filePath))
        {
            String base = base64;
            Map<Integer, String> map = regularGroupMap(BASE64IMG_PATTERN, base, 3);
            if (map != null)
            {
                base = map.get(2);
                filePath = path.replace(path.substring(path.lastIndexOf('.') + 1), map.get(1));
            }
            File dir = new File(filePath).getParentFile();
            if (!dir.exists() && dir.mkdirs())
            {
                log.error("create directory failed: {}", dir.getAbsolutePath());
            }
            try (OutputStream out = newOutputStream(Paths.get(filePath)))
            {
                byte[] bytes = Base64.getDecoder()
                        .decode(base);
                for (int i = 0; i < bytes.length; ++i)
                {
                    if (bytes[i] < 0)
                    {
                        bytes[i] += 256;
                    }
                }
                out.write(bytes);
                out.flush();
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return filePath;
    }

}
