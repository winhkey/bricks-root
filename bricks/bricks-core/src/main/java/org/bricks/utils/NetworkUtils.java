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

import static java.net.NetworkInterface.getNetworkInterfaces;
import static java.text.MessageFormat.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.bricks.constants.Constants.NetworkConstants.DEFAULT_DNS;
import static org.bricks.constants.Constants.NumberConstants.NUMBER_1;
import static org.bricks.utils.FunctionUtils.test;
import static org.bricks.utils.StreamUtils.toStream;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.util.Enumeration;
import java.util.List;

import com.google.common.collect.Lists;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 网络工具类
 *
 * @author fuzy
 *
 */
@Slf4j
@UtilityClass
public class NetworkUtils
{

    /**
     * 基本链接
     * 
     * @param uri uri
     * @return 基本链接
     */
    public static String base(URI uri)
    {
        return format("{0}://{1}", uri.getScheme(), uri.getAuthority());
    }

    /**
     * 基本链接
     *
     * @param url 链接
     * @return 基本链接
     */
    @SneakyThrows
    public static String base(String url)
    {
        return base(new URI(url));
    }

    /**
     * 字节数组ip
     *
     * @param bytes 字节数组
     * @return ip
     */
    public static String byteToIp(byte[] bytes)
    {
        return toStream(bytes).map(b -> b & 0xFF)
                .map(String::valueOf)
                .collect(joining("."));
    }

    /**
     * 检测端口号连通性
     *
     * @param host 主机
     * @param port 端口
     * @return 是否连通
     */
    public static boolean check(String host, int port)
    {
        try (Socket connect = new Socket())
        {
            connect.connect(new InetSocketAddress(host, port), 100);
            return connect.isConnected();
        }
        catch (IOException e)
        {
            log.error("{}:{} {}", host, port, e.getMessage());
        }
        return false;
    }

    /**
     * @return 本地ip
     */
    public static Inet4Address getLocalIp4Address()
    {
        List<Inet4Address> list = getIpByNetworkInterface();
        if (list.size() != NUMBER_1)
        {
            return ofNullable(getIpBySocket()).orElse(null);
        }
        return list.get(0);
    }

    /**
     * @return 从网卡获取本地ip列表
     */
    public static List<Inet4Address> getIpByNetworkInterface()
    {
        return ofNullable(getNetInterfaces())
                .map(netInterfaces -> toStream(netInterfaces, false)
                        .filter(test(NetworkUtils::isValidInterface, null, log, null))
                        .flatMap(i -> toStream(i.getInetAddresses(), false).filter(NetworkUtils::isValidAddress))
                        .map(a -> (Inet4Address) a)
                        .collect(toList()))
                .orElseGet(Lists::newArrayList);
    }

    /**
     * @return 获取所有网卡接口
     */
    public static Enumeration<NetworkInterface> getNetInterfaces()
    {
        try
        {
            return getNetworkInterfaces();
        }
        catch (SocketException e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
     * 
     * @param netInterface 网卡
     * @return 如果满足要求则true，否则false
     * @throws SocketException socket异常
     */
    private static boolean isValidInterface(NetworkInterface netInterface) throws SocketException
    {
        String name = netInterface.getName();
        return !netInterface.isLoopback() && !netInterface.isPointToPoint() && netInterface.isUp()
                && !netInterface.isVirtual() && (name.startsWith("eth") || name.startsWith("en"));
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址
     *
     * @param address InetAddress
     * @return 结果
     */
    private static boolean isValidAddress(InetAddress address)
    {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

    /**
     * @return 通过连接外网获取本地ip
     */
    private static Inet4Address getIpBySocket()
    {
        try (DatagramSocket socket = new DatagramSocket())
        {
            socket.connect(InetAddress.getByName(DEFAULT_DNS), 10_002);
            if (socket.getLocalAddress() instanceof Inet4Address)
            {
                return (Inet4Address) socket.getLocalAddress();
            }
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        return null;
    }

}
