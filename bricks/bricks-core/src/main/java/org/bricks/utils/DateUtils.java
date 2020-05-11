/*
 * Copyright 2020 fuzy(winhkey) (https://github.com/winhkey/bricks)
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
import static java.time.Duration.between;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.US;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.bricks.constants.Constants.FormatConstants.DATETIME_FORMAT;
import static org.bricks.constants.Constants.FormatConstants.DATE_FORMAT;
import static org.bricks.constants.Constants.FormatConstants.TIME_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * 日期工具类
 *
 * @author fuzhiying
 *
 */
@UtilityClass
public class DateUtils {

    /**
     * 定时任务时间规则
     */
    private static final String CRON_PATTERN = "ss mm HH dd MM ? yyyy";

    /**
     * 日期格式
     */
    public static final Map<String, DateTimeFormatter> FORMAT_MAP;

    static {
        FORMAT_MAP = newHashMap();
        FORMAT_MAP.put(CRON_PATTERN, ofPattern(CRON_PATTERN, US));
        FORMAT_MAP.put(DATETIME_FORMAT, ofPattern(DATETIME_FORMAT, US));
        FORMAT_MAP.put(DATE_FORMAT, ofPattern(DATE_FORMAT, US));
        FORMAT_MAP.put(TIME_FORMAT, ofPattern(TIME_FORMAT, US));
    }

    /**
     * 根据格式获取格式对象
     *
     * @param formatter 格式字符串
     * @return 格式对象
     */
    public static DateTimeFormatter get(String formatter) {
        return FORMAT_MAP.get(formatter);
    }

    /**
     * 格式化日期
     *
     * @param dateTime Date
     * @param formatStr FormatStr
     * @return string.
     */
    public static String format(LocalDateTime dateTime, String formatStr) {
        if (dateTime == null)
        {
            return "";
        }
        String pattern = isEmpty(formatStr) ? DATETIME_FORMAT : formatStr;
        DateTimeFormatter formatter = getDateTimeFormatter(pattern);
        return dateTime.format(formatter);
    }

    /**
     * 格式化
     *
     * @param dateTime 日期时间
     * @return 字符串
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DATETIME_FORMAT);
    }

    /**
     * 把字符串格式化日期
     *
     * @param dateStr date
     * @param formatStr formatStr
     * @return Date.
     */
    public static LocalDateTime parse(String dateStr, String formatStr) {
        if (isEmpty(dateStr))
        {
            return null;
        }
        DateTimeFormatter formatter = getDateTimeFormatter(formatStr);
        return LocalDateTime.parse(dateStr, formatter);
    }

    /**
     * 把时间字符串智能格式化日期
     *
     * @param dateStr 时间字符串
     * @return Date 时间对象
     */
    public static LocalDateTime parse(String dateStr) {
        if (isEmpty(dateStr))
        {
            return null;
        }
        String parse = changeDate(dateStr);
        return parse(dateStr, parse);
    }

    /**
     * 把字符串格式化日期
     *
     * @param datePattern 时间正则
     * @param dateStr 时间字符串
     * @return 日期
     */
    public static LocalDateTime parse(Pattern datePattern, String dateStr) {
        return ofNullable(datePattern).map(pattern -> {
            LocalDateTime result = null;
            if (isNotEmpty(dateStr)) {
                Matcher matcher = datePattern.matcher(dateStr);
                if (matcher.find()) {
                    DateTimeFormatter formatter = getDateTimeFormatter(dateStr, matcher);
                    result = ofNullable(formatter).map(f -> LocalDateTime.parse(dateStr, formatter))
                            .orElse(null);
                }
            }
            return result;
        })
                .orElse(null);
    }

    private static DateTimeFormatter getDateTimeFormatter(String dateStr, Matcher matcher) {
        DateTimeFormatter formatter = null;
        if (dateStr.equals(matcher.group(1))) {
            String parse = changeDate(dateStr);
            formatter = ofPattern(parse, US);
        } else if (dateStr.equals(matcher.group(6))) {
            String month = matcher.group(7);
            String parse = month == null ? dateStr : dateStr.replace(month, month.substring(0, 3));
            parse = parse.replaceFirst("(?<=\\d)st|nd|rd|th", "")
                    .replaceFirst("(?<=\\d)(am|pm|AM|PM)", " $1")
                    .replaceFirst("^[a-zA-Z]{3,8}([^a-zA-Z])", "MMM$1")
                    .replaceFirst("([^\\d])[\\d]{1,2}(st|nd|rd|th)?([^\\d])", "$1d ")
                    .replaceFirst("([^\\d])[\\d]{4}( ?)", "$1yyyy$2")
                    .replaceFirst("( ?)[\\d]{1,2}([^\\d])", "$1K$2")
                    .replaceFirst("([^\\d])[\\d]{1,2}([^\\d]?)", "$1m$2")
                    .replaceFirst("([^\\d])[\\d]{1,2}([^\\d]?)", "$1s$2")
                    .replaceFirst("(am|pm|AM|PM)", "a");
            formatter = ofPattern(parse, US);
        }
        return formatter;
    }

    private static DateTimeFormatter getDateTimeFormatter(String pattern) {
        return ofNullable(get(pattern)).orElseGet(() -> FORMAT_MAP.compute(pattern, (k, v) -> ofPattern(pattern, US)));
    }

    /**
     * LocalDateTime转Date
     *
     * @param dateTime LocalDateTime
     * @return date
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(systemDefault())
                .toInstant());
    }

    /**
     * LocalDate转Date
     *
     * @param date LocalDate
     * @return Date
     */
    public static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay()
                .atZone(systemDefault())
                .toInstant());
    }

    /**
     * Date转LocalDateTime
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), systemDefault());
    }

    /**
     * Date转LocalDate
     *
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    /**
     * Date转LocalTime
     *
     * @param date Date
     * @return LocalTime
     */
    public static LocalTime toLocalTime(Date date) {
        return toLocalDateTime(date).toLocalTime();
    }

    /**
     * 返回2个时间的相差时间
     *
     * @param dateTime1 时间1
     * @param dateTime2 时间2
     * @return 日期
     */
    public static String getdras(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return null;
        } else {
            long mills = between(dateTime1, dateTime2).toMillis();
            return calculate(mills);
        }
    }

    /**
     * 相差时间
     *
     * @param dras 毫秒
     * @return 相差字符串
     */
    private static String calculate(long dras) {
        long daylong = DAYS.toMillis(1);
        long hourlong = HOURS.toMillis(1);
        long minulong = MINUTES.toMillis(1);
        long secodlong = SECONDS.toMillis(1);
        String result;
        if (dras > daylong) {
            long days = dras / daylong;
            result = days + "天" + calculate(dras % daylong);
        } else if (dras > hourlong) {
            long hour = dras / hourlong;
            result = hour + "小时" + calculate(dras % hourlong);
        } else if (dras > minulong) {
            long minu = dras / minulong;
            result = minu + "分" + calculate(dras % minulong);
        } else {
            long second = dras / secodlong;
            result = second + "秒";
        }
        return result;
    }

    /**
     * 日期转换
     *
     * @param dateStr 日期字符串
     * @return 日期格式
     */
    private static String changeDate(String dateStr) {
        return dateStr.replaceFirst("^[\\d]{4}([^\\d])", "yyyy$1")
                .replaceFirst("^[\\d]{2}([^\\d])", "yy$1")
                .replaceFirst("([^\\d])[\\d]{1,2}([^\\d])", "$1MM$2")
                .replaceFirst("([^\\d])[\\d]{1,2}( ?)", "$1dd$2")
                .replaceFirst("( ?)[\\d]{1,2}([^\\d])", "$1HH$2")
                .replaceFirst("([^\\d])[\\d]{1,2}([^\\d]?)", "$1mm$2")
                .replaceFirst("([^\\d])[\\d]{1,2}([^\\d]?)", "$1ss$2");
    }

}
