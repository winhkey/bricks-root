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

package org.bricks.pattern;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import org.bricks.annotation.NoLog;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 正则缓存
 *
 * @author fuzy
 *
 */
@Slf4j
@Setter
@Getter
@Service
@ConfigurationProperties(prefix = "bricks.pattern")
public class PatternService
{

    /**
     * 正则缓存
     */
    private Map<String, String> patternMap;

    /**
     * 正则缓存
     */
    private static final Map<String, Pattern> PATTERN_MAP = newHashMap();

    /**
     * 初始化加载配置
     */
    @PostConstruct
    public void init()
    {
        if (isNotEmpty(patternMap))
        {
            patternMap.forEach((key, value) ->
            {
                log.info("{}-{}", key, value);
                PATTERN_MAP.put(key, Pattern.compile(value));
            });
        }
    }

    /**
     * 根据id加载正则对象
     *
     * @param id id
     * @return 正则对象
     */
    @NoLog
    public Pattern loadPatternById(String id)
    {
        return PATTERN_MAP.get(id);
    }

}

