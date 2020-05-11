package org.bricks.constants;

import lombok.experimental.UtilityClass;

/**
 * 常量类
 *
 * @author fuzy
 *
 */
@UtilityClass
public class Constants {

    /**
     * 数字常量
     */
    public static final class NumberConstants {

        /**
         * 0
         */
        public static final int NUMBER_0 = 0;

        /**
         * 1
         */
        public static final int NUMBER_1 = 1;

        /**
         * 2
         */
        public static final int NUMBER_2 = 2;

        /**
         * 3
         */
        public static final int NUMBER_3 = 3;

        /**
         * 48
         */
        public static final int NUMBER_48 = 48;

        /**
         * 60
         */
        public static final int NUMBER_60 = 60;

        /**
         * 100
         */
        public static final int NUMBER_100 = 100;

        /**
         * 2000
         */
        public static final int NUMBER_2000 = 2000;

    }

    /**
     * 时间格式
     */
    public static final class FormatConstants {

        /**
         * yyyy-MM-dd HH:mm:ss
         */
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

        /**
         * yyyy-MM-dd
         */
        public static final String DATE_FORMAT = "yyyy-MM-dd";

        /**
         * HH:mm:ss
         */
        public static final String TIME_FORMAT = "HH:mm:ss";

    }

    /**
     * 网络常量
     */
    public static final class NetworkConstants {

        /**
         * 本机地址
         */
        public static final String LOCALHOST = "localhost";

        /**
         * ipv4本机地址
         */
        public static final String LOCALHOST_IPV4 = "127.0.0.1";

        /**
         * ipv6本机地址
         */
        public static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

        /**
         * IP头列表
         */
        public static final String[] IP_HEADERS = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

    }

}
