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

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Character.isWhitespace;
import static java.lang.System.getProperty;
import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.US;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.bricks.constants.Constants.NumberConstants.ASCII_MAX;
import static org.bricks.constants.Constants.NumberConstants.DBC_65248;
import static org.bricks.constants.Constants.NumberConstants.SBC_65280;
import static org.bricks.constants.Constants.NumberConstants.SBC_65375;
import static org.bricks.constants.Constants.PatternConstants.FIRST_UPPER_PATTERN;
import static org.bricks.constants.Constants.PatternConstants.HUMP_PATTERN;
import static org.bricks.constants.Constants.PatternConstants.LINE_PATTERN;
import static org.bricks.constants.Constants.StringConstants.HEX;
import static org.bricks.constants.Constants.StringConstants.HEX_ARRAY;
import static org.bricks.constants.Constants.StringConstants.LONG_CHAR;
import static org.bricks.constants.Constants.StringConstants.SHORT_CHAR;
import static org.bricks.utils.RegexUtils.regularGroupReplaceAll;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.nio.charset.Charset;
import java.util.List;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 字符串工具类
 *
 * @author fuzy
 *
 */
@Slf4j
@UtilityClass
public class StringUtils
{

    /**
     * 判断字符串为空
     *
     * @param text text
     * @return boolean
     */
    public static boolean isEmpty(String text)
    {
        return !ofNullable(text).map(s -> s.chars()
                .filter(c -> !isWhitespace(c))
                .findFirst()
                .isPresent())
                .orElse(false);
    }

    /**
     * 判断字符不为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * 字符串分割
     *
     * @param str 字符串
     * @param split 分割字符串
     * @param allowEmpty 允许空串
     * @return 分割结果
     */
    public static String[] split(String str, String split, boolean allowEmpty)
    {
        if (str == null)
        {
            return null;
        }
        if (split == null || split.length() == 0)
        {
            return new String[] {str};
        }
        int index = str.indexOf(split);
        if (index == -1)
        {
            return new String[] {str};
        }
        List<String> list = newArrayList();
        int fromIndex = 0;
        int signLen = split.length();
        int strLen = str.length();
        String subStr;
        while (fromIndex < strLen)
        {
            index = str.indexOf(split, fromIndex);
            if (index == -1)
            {
                list.add(str.substring(fromIndex));
                break;
            }
            subStr = str.substring(fromIndex, index);
            if (allowEmpty || !isEmpty(subStr))
            {
                list.add(subStr);
            }
            fromIndex = index + signLen;
        }
        return list.toArray(new String[0]);
    }

    /**
     * 拆分成列表
     *
     * @param str 字符串
     * @param split 分割字符串
     * @return 列表
     */
    public static List<String> splitToList(String str, String split)
    {
        return of(split(str, split, false)).collect(toList());
    }

    /**
     * 半角转全角
     *
     * @param input 半角字符串
     * @return 全角字符串
     */
    public static String fullWidth(String input)
    {
        String content = input.replace(' ', '　');
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] < ASCII_MAX)
            {
                chars[i] = (char) (chars[i] + DBC_65248);
            }
        }
        return new String(chars);
    }

    /**
     * 全角转半角
     *
     * @param input 全角字符串
     * @return 半角
     */
    public static String halfWidth(String input)
    {
        String content = input.replace('　', ' ');
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] > SBC_65280 && chars[i] < SBC_65375)
            {
                chars[i] = (char) (chars[i] - DBC_65248);
            }
        }
        return new String(chars);
    }

    /**
     * 根据变量，进行中文简转繁或繁转简的操作
     *
     * @param str 被转换的源字符串
     * @param flag 转换标记，true：繁转简，false：简转繁
     * @return 转换后的字符串
     */
    public static String change(String str, boolean flag)
    {
        return flag ? toShort(str) : toLong(str);
    }

    /**
     * 中文简体转繁体
     *
     * @param str 被转换的源字符串
     * @return 转换后的字符串
     */
    public static String toLong(String str)
    {
        StringBuilder outputString = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            int index = LONG_CHAR.indexOf(c);
            outputString.append(index > -1 ? SHORT_CHAR.charAt(index) : c);
        }
        return outputString.toString();
    }

    /**
     * 中文繁体转简体
     *
     * @param str 被转换的源字符串
     * @return 转换后的字符串
     */
    public static String toShort(String str)
    {
        StringBuilder outputString = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            if (SHORT_CHAR.indexOf(str.charAt(i)) > -1)
            {
                outputString.append(LONG_CHAR.charAt(SHORT_CHAR.indexOf(str.charAt(i))));
            }
            else
            {
                outputString.append(str.charAt(i));
            }
        }
        return outputString.toString();
    }

    /**
     * uuid字符串
     *
     * @return uuid
     */
    public static String generateUUID()
    {
        return randomUUID().toString()
                .replace("-", "");
    }

    /**
     * encode UTF-8解码
     *
     * @param str 字符串
     * @return 编码
     */
    public static String urlEncode(String str)
    {
        return urlEncode(str, UTF_8);
    }

    /**
     * encode 转码
     *
     * @param str 字符串
     * @param charset 字符集
     * @return 编码
     */
    public static String urlEncode(String str, Charset charset)
    {
        String encode;
        try
        {
            encode = encode(str, charset.name());
        }
        catch (UnsupportedEncodingException e)
        {
            encode = "";
        }
        return encode;
    }

    /**
     * decode UTF-8解码
     *
     * @param str 字符串
     * @return 编码
     */
    public static String urlDecode(String str)
    {
        return urlDecode(str, UTF_8.name());
    }

    /**
     * encode 解码
     *
     * @param str 字符串
     * @param enc 字符集
     * @return 解码
     */
    public static String urlDecode(String str, String enc)
    {
        String decode;
        try
        {
            decode = decode(str, enc);
        }
        catch (UnsupportedEncodingException e)
        {
            decode = "";
        }
        return decode;
    }

    /**
     * 转换字符串编码
     *
     * @param before 转换前字符串
     * @param fromEncoding 原编码（为空用系统编码）
     * @param toEncoding 目标编码（为空用系统编码）
     * @return 转换后字符串
     */
    public static String convertEncoding(String before, String fromEncoding, String toEncoding)
    {
        String systemFileEncoding = getProperty("file.encoding");
        String from = isEmpty(fromEncoding) ? systemFileEncoding : fromEncoding;
        String to = isEmpty(toEncoding) ? systemFileEncoding : toEncoding;
        if (from.equals(to))
        {
            return before;
        }
        String after = null;
        try
        {
            after = new String(before.getBytes(from), to);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error(e.getMessage(), e);
        }
        return after;
    }

    /**
     * utf8转unicode
     * 
     * @param str utf8
     * @return unicode
     */
    public static String utf8ToUnicode(String str)
    {
        char[] myBuffer = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        int n = str.length();
        for (int i = 0; i < n; i++)
        {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN)
            {
                // 英文及数字等
                sb.append(myBuffer[i]);
            }
            else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
            {
                // 全角半角字符
                int j = (int) myBuffer[i] - DBC_65248;
                sb.append((char) j);
            }
            else
            {
                // 汉字
                String hexS = Integer.toHexString(myBuffer[i]);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase(US));
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param str 字符串
     * @return 结果
     */
    public static String humpToLine(String str)
    {
        return regularGroupReplaceAll(HUMP_PATTERN, str, matcher -> "_".concat(matcher.group(0)
                .toLowerCase(US)));
    }

    /**
     * 下划线转驼峰
     *
     * @param str 字符串
     * @return 结果
     */
    public static String lineToHump(String str)
    {
        return regularGroupReplaceAll(LINE_PATTERN, str, matcher -> matcher.group(1)
                .toUpperCase(US));
    }

    /**
     * 首字母转小写
     *
     * @param str 字符串
     * @return 结果
     */
    public static String firstToLowercase(String str)
    {
        return regularGroupReplaceAll(FIRST_UPPER_PATTERN, str, matcher -> matcher.group(0)
                .toLowerCase(US));
    }

    /**
     * 二进制字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hex 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexToBytes(String hex)
    {
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] chars = hex.toCharArray();
        for (int i = 0; i < len; i++)
        {
            int pos = i * 2;
            result[i] = (byte) ((byte) HEX.indexOf(chars[pos]) << 4 | (byte) HEX.indexOf(chars[pos + 1]));
        }
        return result;
    }

}
