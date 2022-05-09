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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.bricks.constants.Constants.NumberConstants.NUMBER_3;
import static org.bricks.constants.Constants.NumberConstants.NUMBER_48;
import static org.bricks.utils.ContextHolder.getBean;
import static org.bricks.utils.MD5Utils.getMD5String;
import static org.bricks.utils.RandomUtils.randomString;
import static org.bricks.utils.RegexUtils.matches;

import org.bricks.exception.BaseException;
import org.bricks.pattern.PatternService;

import lombok.experimental.UtilityClass;

/**
 * 密码工具类
 *
 * @author fuzy
 *
 */
@UtilityClass
public class PasswordUtils
{

    /**
     * 密码加密
     *
     * @param password 明文
     * @return 密文
     */
    public static String encodePassword(String password)
    {
        return encodePassword(password, randomString(16));
    }

    /**
     * 加密
     *
     * @param password 明文
     * @param salt 盐
     * @return 密文
     */
    public static String encodePassword(String password, String salt)
    {
        PatternService patternService = getBean(PatternService.class);
        if (patternService != null && !matches(patternService.loadPatternById("password"), password))
        {
            throw new BaseException("error.password.format");
        }
        String md5 = getMD5String(password.concat(salt), false);
        char[] cs = new char[48];
        for (int i = 0; i < NUMBER_48; i += NUMBER_3)
        {
            cs[i] = md5.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = md5.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验
     *
     * @param inputPassword 输入的密码
     * @param storedPassword 存储的密码
     * @return 是否匹配
     */
    public static boolean verify(String inputPassword, String storedPassword)
    {
        if (isBlank(inputPassword) || isBlank(storedPassword))
        {
            return false;
        }
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < NUMBER_48; i += NUMBER_3)
        {
            cs1[i / 3 * 2] = storedPassword.charAt(i);
            cs1[i / 3 * 2 + 1] = storedPassword.charAt(i + 2);
            cs2[i / 3] = storedPassword.charAt(i + 1);
        }
        String salt = new String(cs2);
        return getMD5String(inputPassword.concat(salt), false).equals(new String(cs1));
    }

}
