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

import static org.bricks.bean.ResponseWrapper.build;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bricks.bean.ResponseWrapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 命令行工具类
 *
 * @author fuzy
 *
 */
@Slf4j
@UtilityClass
public class CommandUtils
{

    /**
     * 执行本地进程
     *
     * @param command 例如：/bin/sh
     * @param resultCharset 执行返回结果字符集
     * @param timeout 执行超时时间
     * @param commandArgs 例如：new String[]{"-c","echo 'test'"}
     * @return ResponseWrapper
     */
    public static ResponseWrapper<Void> execCommand(String command, String resultCharset, long timeout,
            String... commandArgs)
    {
        ResponseWrapper<Void> wrapper = null;
        CommandLine commandLine = new CommandLine(command);
        commandLine.addArguments(commandArgs, false);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
        DefaultExecutor exec = new DefaultExecutor();
        exec.setWatchdog(watchdog);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
            exec.setStreamHandler(pumpStreamHandler);
            int code = exec.execute(commandLine);
            String result = outputStream.toString(resultCharset);
            wrapper = build(String.valueOf(code), result, code == 0);
        }
        catch (Throwable e)
        {
            log.error(e.getMessage(), e);
        }
        return wrapper;
    }

}
