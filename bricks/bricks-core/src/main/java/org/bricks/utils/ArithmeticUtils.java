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

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.text.MessageFormat.format;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * 数字运算
 *
 * @author fuzy
 *
 */
@UtilityClass
public class ArithmeticUtils
{

    /**
     * 0正则
     */
    private static final Pattern ZERO_PATTERN = Pattern.compile("0(\\.0+)?");

    /**
     * 默认运算精度
     */
    private static final int DEF_SCALE = 2;

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2)
    {
        return v1.add(v2);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1, Number v2)
    {
        return add(v1, new BigDecimal(String.valueOf(v2)));
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2)
    {
        return add(valueOf(v1), valueOf(v2)).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2)
    {
        return v1.subtract(v2);
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, Number v2)
    {
        return sub(v1, new BigDecimal(String.valueOf(v2)));
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2)
    {
        return sub(valueOf(v1), valueOf(v2)).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2)
    {
        return v1.multiply(v2);
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, Number v2)
    {
        return mul(v1, new BigDecimal(String.valueOf(v2)));
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2)
    {
        return mul(valueOf(v1), valueOf(v2)).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2)
    {
        return div(v1, v2, DEF_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, Number v2)
    {
        return div(v1, new BigDecimal(String.valueOf(v2)));
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后2位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2)
    {
        return div(v1, v2, DEF_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale)
    {
        if (RegexUtils.matches(ZERO_PATTERN, String.valueOf(v2)))
        {
            throw new IllegalArgumentException("The divisor cannot be zero");
        }
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v1.divide(v2, scale, HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale)
    {
        return div(valueOf(v1), valueOf(v2), scale).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal round(BigDecimal v, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return v.setScale(scale, HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale)
    {
        return round(valueOf(v), scale).doubleValue();
    }

    /**
     * 保留默认小数精度
     * 
     * @param number 数字
     * @return 结果
     */
    public static double retain(double number)
    {
        return retain(number, DEF_SCALE);
    }

    /**
     * 保留小数精度
     * 
     * @param number 数字
     * @param precision 精度
     * @return 结果
     */
    public static double retain(double number, int precision)
    {
        String result = String.format(format("%.{0}f", String.valueOf(precision)), number);
        return Double.parseDouble(result);
    }

    /**
     * 整数的n次幂
     * 
     * @param a 整数
     * @param n 幂
     * @return 结果
     */
    public static long power(long a, int n)
    {
        int rtn = 1;
        while (n >= 1)
        {
            if ((n & 1) == 1)
            {
                rtn *= a;
            }
            a *= a;
            n = n >> 1;
        }
        return rtn;
    }

}
