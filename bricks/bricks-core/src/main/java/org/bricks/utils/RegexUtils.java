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
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * 正则工具
 *
 * @author fuzy
 *
 */
@UtilityClass
public class RegexUtils
{

    /**
     * 正则捕获
     *
     * @param expression 正则表达式
     * @param flag 正则flag
     * @param input 目标字符串
     * @return 捕获字符串
     */
    public static String regularGroup(String expression, int flag, CharSequence input)
    {
        Pattern pattern = Pattern.compile(expression, flag);
        return regularGroup(pattern, input);
    }

    /**
     * 正则捕获
     *
     * @param pattern 正则对象
     * @param input 目标字符串
     * @return 捕获字符串
     */
    public static String regularGroup(Pattern pattern, CharSequence input)
    {
        return regularGroup(pattern, input, 0);
    }

    /**
     * 正则捕获
     *
     * @param pattern 正则对象
     * @param input 目标字符串
     * @param group 分组号
     * @return 捕获字符串
     */
    public static String regularGroup(Pattern pattern, CharSequence input, int group)
    {
        List<String> list = regularGroups(pattern, input, group);
        return list.isEmpty() ? "" : list.get(0);
    }

    /**
     * 正则捕获
     *
     * @param pattern 正则对象
     * @param input 目标字符串
     * @return 捕获列表
     */
    public static List<String> regularGroups(Pattern pattern, CharSequence input)
    {
        return regularGroups(pattern, input, 0);
    }

    /**
     * 正则捕获
     *
     * @param pattern 正则对象
     * @param input 目标字符串
     * @param group 分组
     * @return 捕获列表
     */
    public static List<String> regularGroups(Pattern pattern, CharSequence input, int group)
    {
        List<String> list = newArrayList();
        Matcher matcher = pattern.matcher(input);
        int groupCount = matcher.groupCount();
        while (matcher.find())
        {
            // 返回捕获的数据
            if (group <= groupCount)
            {
                list.add(matcher.group(group));
            }
        }
        return list;
    }

    /**
     * 正则捕获
     *
     * @param pattern 正则对象
     * @param input 目标字符串
     * @param groups 分组号
     * @return 捕获列表
     */
    public static Map<Integer, String> regularGroupMap(Pattern pattern, CharSequence input, int... groups)
    {
        List<Map<Integer, String>> list = regularGroupsMap(pattern, input, groups);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 正则捕获
     *
     * @param pattern 正则对象
     * @param input 目标字符串
     * @param groups 分组号
     * @return 捕获列表
     */
    public static List<Map<Integer, String>> regularGroupsMap(Pattern pattern, CharSequence input, int... groups)
    {
        List<Map<Integer, String>> list = newArrayList();
        if (isNotEmpty(groups))
        {
            Matcher matcher = pattern.matcher(input);
            int groupCount = matcher.groupCount();
            while (matcher.find())
            {
                Map<Integer, String> map = newHashMap();
                stream(groups).forEach(i -> map.put(i, i <= groupCount ? matcher.group(i) : ""));
                list.add(map);
            }
        }
        else
        {
            Map<Integer, String> map = newLinkedHashMap();
            map.put(0, regularGroup(pattern, input));
            list.add(map);
        }
        return list;
    }

    /**
     * 正则验证
     *
     * @param pattern 正则对象
     * @param input 目标字符串
     * @return boolean 验证结果
     */
    public static boolean matches(Pattern pattern, CharSequence input)
    {
        Matcher m = pattern.matcher(input);
        return m.matches();
    }

    /**
     * 正则替换
     *
     * @param pattern 正则对象
     * @param source 源字符串
     * @param target 替换字符串
     * @return 替换结果
     */
    public static String replaceAll(Pattern pattern, String source, String target)
    {
        Matcher m = pattern.matcher(source);
        return m.replaceAll(target);
    }

    /**
     * 正则替换
     *
     * @param pattern 正则对象
     * @param source 源字符串
     * @param target 替换字符串
     * @return 替换结果
     */
    public static String replaceFirst(Pattern pattern, String source, String target)
    {
        Matcher m = pattern.matcher(source);
        return m.replaceFirst(target);
    }

    /**
     * 正则捕获替换
     *
     * @param pattern 正则对象
     * @param content 源字符串
     * @param source 捕获字符串
     * @param target 替换字符串
     * @return 替换结果
     */
    public static String regularGroupReplaceAll(Pattern pattern, String content, String source, String target)
    {
        Matcher matcher = pattern.matcher(content);
        StringBuffer result = new StringBuffer(content.length());
        while (matcher.find())
        {
            matcher.appendReplacement(result, "");
            result.append(matcher.group()
                    .replaceAll(source, target));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * 正则分割字符串
     *
     * @param pattern 正则对象
     * @param content 源字符串
     * @return 分割后的数组
     */
    public String[] split(Pattern pattern, String content)
    {
        return pattern.split(content);
    }

    /**
     * 正则查找
     *
     * @param pattern 正则对象
     * @param content 源字符串
     * @return 是否包含
     */
    public static boolean contains(Pattern pattern, String content)
    {
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

}
