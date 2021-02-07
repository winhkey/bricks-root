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
import static java.nio.file.Files.newInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import lombok.experimental.UtilityClass;

/**
 * md5工具
 *
 * @author fuzy
 *
 */
@UtilityClass
public class MD5Utils
{

    /**
     * 生成字符串的md5校验值
     *
     * @param s 字符串
     * @return md5结果
     */
    public static String getMD5String(String s)
    {
        return getMD5String(s.getBytes(UTF_8));
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file 文件
     * @return md5结果
     */
    public static String getMD5String(File file)
    {
        byte[] buffer = new byte[1024];
        try (InputStream fis = newInputStream(file.toPath()))
        {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            int numRead;
            while ((numRead = fis.read(buffer)) > 0)
            {
                messagedigest.update(buffer, 0, numRead);
            }
            return bufferToHex(messagedigest.digest());
        }
        catch (IOException | NoSuchAlgorithmException e)
        {
            return null;
        }
    }

    /**
     * 生成字节数组的md5校验值
     *
     * @param bytes 字节数组
     * @return md5结果
     */
    public static String getMD5String(byte[] bytes)
    {
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            return bufferToHex(messagedigest.digest(bytes));
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        }
    }

    /**
     * 将字节数组转成16进制字符串
     *
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    private static String bufferToHex(byte[] bytes)
    {
        return new String(new Hex().encode(bytes), UTF_8);
    }

}
