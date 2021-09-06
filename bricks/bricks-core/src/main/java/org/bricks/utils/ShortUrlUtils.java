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

import static org.bricks.utils.MD5Utils.getMD5String;
import static org.bricks.utils.StringUtils.generateUUID;

import lombok.experimental.UtilityClass;

/**
 * 根据长链生成短地址
 *
 * @author fuzy
 *
 */
@UtilityClass
public class ShortUrlUtils
{

    /**
     * 字符数组
     */
    private static final String[] CHARS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};

    /**
     * @param url 长链接
     * @return 返回4组短地址
     */
    public static String[] shortUrl(String url)
    {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        // 对传入网址进行 MD5 加密
        String hex = getMD5String(generateUUID().concat(url), false);
        String[] resUrl = new String[4];
        for (int i = 0; i < resUrl.length; i++)
        {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++)
            {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars = outChars.concat(CHARS[(int) index]);
                // 每次循环按位右移 5 位
                lHexLong >>= 5;
            }
            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl;
    }

}
