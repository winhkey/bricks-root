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

import static java.util.stream.IntStream.range;
import static org.bricks.constants.Constants.StringConstants.ALL_CHARS;
import static org.bricks.constants.Constants.StringConstants.ALL_NUMBERS;
import static org.bricks.constants.Constants.StringConstants.ENGLISH_CHARS;

import java.security.SecureRandom;
import java.util.Random;

import lombok.experimental.UtilityClass;

/**
 * 随机字符串生成
 *
 * @author fuzy
 *
 */
@UtilityClass
public class RandomUtils
{

    /**
     * 随机种子
     */
    private static final Random DEFAULT_RANDOM = new SecureRandom();

    /**
     * 数字随机串
     *
     * @param length 长度
     * @return 随机串
     */
    public static String randomNumber(int length)
    {
        return random(ALL_NUMBERS, length);
    }

    /**
     * 随机串
     *
     * @param length 长度
     * @return 随机串
     */
    public static String randomString(int length)
    {
        return random(ALL_CHARS, length);
    }

    /**
     * 字母随机串
     *
     * @param length 长度
     * @return 随机串
     */
    public static String randomLetter(int length)
    {
        return random(ENGLISH_CHARS, length);
    }

    /**
     * 随机串
     *
     * @param source 源
     * @param length 长度
     * @return 随机串
     */
    private static String random(String source, int length)
    {
        StringBuilder builder = new StringBuilder();
        range(0, length).forEach(i -> builder.append(source.charAt(DEFAULT_RANDOM.nextInt(source.length()))));
        return builder.toString();
    }

}
