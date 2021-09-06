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
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.newInputStream;
import static java.text.MessageFormat.format;
import static java.util.Locale.US;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.bricks.constants.Constants.FileTypeConstants.ImageTypeConstants.BMP;
import static org.bricks.constants.Constants.NumberConstants.NUMBER_2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import org.bricks.exception.BaseException;

import lombok.experimental.UtilityClass;

/**
 * 文件处理
 *
 * @author fuzy
 *
 */
@UtilityClass
public class FileUtils
{

    /**
     * 头文件类型
     */
    private static final Map<String, String> FILE_TYPE_MAP = newHashMap();

    /**
     * 错误提示
     */
    private static final String MESSAGE = "{0} can not be created.";

    static
    {
        // JPEG (jpg)
        FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg");
        // PNG (png)
        FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png");
        // GIF (gif)
        FILE_TYPE_MAP.put("47494638396126026f01", "gif");
        // TIFF (tif)
        FILE_TYPE_MAP.put("49492a00227105008037", "tif");
        // 16色位图(bmp)
        FILE_TYPE_MAP.put("424d228c010000000000", BMP);
        // 24位位图(bmp)
        FILE_TYPE_MAP.put("424d8240090000000000", BMP);
        // 256色位图(bmp)
        FILE_TYPE_MAP.put("424d8e1b030000000000", BMP);
        // CAD (dwg)
        FILE_TYPE_MAP.put("41433130313500000000", "dwg");
        // HTML (html)
        FILE_TYPE_MAP.put("3c21444f435459504520", "html");
        // HTM (htm)
        FILE_TYPE_MAP.put("3c21646f637479706520", "htm");
        // css
        FILE_TYPE_MAP.put("48544d4c207b0d0a0942", "css");
        // js
        FILE_TYPE_MAP.put("696b2e71623d696b2e71", "js");
        // Rich Text Format (rtf)
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf");
        // Photoshop (psd)
        FILE_TYPE_MAP.put("38425053000100000000", "psd");
        // Email [Outlook Express 6] (eml)
        FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml");
        // doc
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc");
        // MS Access (mdb)
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb");
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
        // Adobe Acrobat (pdf)
        FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf");
        // rmvb/rm相同
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb");
        // flv与f4v相同
        FILE_TYPE_MAP.put("464c5601050000000900", "flv");
        FILE_TYPE_MAP.put("00000020667479706d70", "mp4");
        FILE_TYPE_MAP.put("49443303000000002176", "mp3");
        FILE_TYPE_MAP.put("000001ba210001000180", "mpg");
        // wmv与asf相同
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv");
        // Wave (wav)
        FILE_TYPE_MAP.put("52494646e27807005741", "wav");
        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");
        // MIDI (mid)
        FILE_TYPE_MAP.put("4d546864000000060001", "mid");
        FILE_TYPE_MAP.put("504b0304140000000800", "zip");
        FILE_TYPE_MAP.put("526172211a0700cf9073", "rar");
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
        FILE_TYPE_MAP.put("504b03040a0000000000", "jar");
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe");
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf");
        FILE_TYPE_MAP.put("3c3f786d6c2076657273", "xml");
        FILE_TYPE_MAP.put("494e5345525420494e54", "sql");
        FILE_TYPE_MAP.put("7061636b616765207765", "java");
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat");
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz");
        FILE_TYPE_MAP.put("6c6f67346a2e726f6f74", "properties");
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");
        FILE_TYPE_MAP.put("49545346030000006000", "chm");
        FILE_TYPE_MAP.put("04000000010000001300", "mxp");
        FILE_TYPE_MAP.put("504b0304140006000800", "docx");
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");
        // Quicktime (mov)
        FILE_TYPE_MAP.put("6D6F6F76", "mov");
        // WordPerfect (wpd)
        FILE_TYPE_MAP.put("FF575043", "wpd");
        // Outlook Express (dbx)
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx");
        // Outlook (pst)
        FILE_TYPE_MAP.put("2142444E", "pst");
        // Quicken (qdf)
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf");
        // Windows Password (pwl)
        FILE_TYPE_MAP.put("E3828596", "pwl");
        // Real Audio (ram)
        FILE_TYPE_MAP.put("2E7261FD", "ram");
    }

    /**
     * 获取文件头
     *
     * @param file 文件
     * @return 文件头
     */
    public static String getFileType(File file)
    {
        byte[] b = new byte[10];
        try (InputStream is = newInputStream(file.toPath()))
        {
            return is.read(b, 0, b.length) > -1 ? getFileTypeByByte(b) : null;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    /**
     * 获取文件头
     *
     * @param bytes 文件字节
     * @return 文件头
     */
    public static String getFileTypeByByte(byte[] bytes)
    {
        return ofNullable(bytesToHexString(bytes)).map(fileCode ->
        {
            final String code = fileCode.toLowerCase(US);
            // 这种方法在字典的头代码不够位数的时候可以用但是速度相对慢一点
            return FILE_TYPE_MAP.entrySet()
                    .stream()
                    .filter(entry ->
                    {
                        String key = entry.getKey()
                                .toLowerCase(US);
                        return key.startsWith(code) || code.startsWith(key);
                    })
                    .findFirst()
                    .map(Entry::getValue)
                    .orElse(null);
        })
                .orElse(null);
    }

    /**
     * 得到上传文件的文件头
     *
     * @param src 文件字节
     * @return 文件头
     */
    private static String bytesToHexString(byte[] src)
    {
        if (isNotEmpty(src))
        {
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : src)
            {
                int v = b & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < NUMBER_2)
                {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        }
        return null;
    }

    /**
     * 创建目录
     *
     * @param dir 目录
     */
    public static void createDirectory(Path dir)
    {
        if (!exists(dir))
        {
            try
            {
                Path createdDir = createDirectories(dir);
                if (!exists(createdDir))
                {
                    throw new BaseException(format(MESSAGE, dir));
                }
            }
            catch (IOException e)
            {
                throw new BaseException(format(MESSAGE, dir), e);
            }
        }
    }

}
