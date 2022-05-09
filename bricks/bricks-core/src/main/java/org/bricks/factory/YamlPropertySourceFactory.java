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

package org.bricks.factory;

import static java.util.Optional.ofNullable;

import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

/**
 * 加载yml文件
 *
 * @author fuzy
 *
 */
public class YamlPropertySourceFactory implements PropertySourceFactory
{

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource)
    {
        Properties ymlProperties = ymlProperties(resource);
        String sourceName = ofNullable(name).orElseGet(() -> ofNullable(resource.getResource()
                .getFilename()).orElseThrow(IllegalArgumentException::new));
        return new PropertiesPropertySource(sourceName, ymlProperties);
    }

    private Properties ymlProperties(EncodedResource resource)
    {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

}
