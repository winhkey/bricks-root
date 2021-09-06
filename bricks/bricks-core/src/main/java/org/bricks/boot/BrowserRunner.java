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

package org.bricks.boot;

import static java.lang.Runtime.getRuntime;
import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.bricks.utils.ContextHolder.isWindows;

import org.bricks.annotation.NoLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动打开浏览器
 *
 * @author fuzy
 *
 */
@Slf4j
@NoLog
@Component
public class BrowserRunner implements CommandLineRunner
{

    /**
     * 端口号
     */
    @Value("${server.port:}")
    private String port;

    /**
     * 首页
     */
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    /**
     * 自动打开浏览器
     */
    @Value("${bricks.autoBrowser:false}")
    private boolean autoBrowser;

    @Override
    public void run(String... args)
    {
        if (autoBrowser && isNotBlank(contextPath) && isWindows())
        {
            String command = format("cmd /c start http://localhost:{0}{1}", port, contextPath);
            try
            {
                getRuntime().exec(command);
            }
            catch (Throwable e)
            {
                log.error(e.getMessage(), e);
            }
        }
    }

}
