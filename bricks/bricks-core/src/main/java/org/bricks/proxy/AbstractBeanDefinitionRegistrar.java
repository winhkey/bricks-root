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

package org.bricks.proxy;

import static com.google.common.collect.Lists.newArrayList;
import static org.bricks.constants.Constants.GenericConstants.UNCHECKED;
import static org.bricks.proxy.InvocationHandlerUtils.addClass;
import static org.bricks.utils.FunctionUtils.accept;
import static org.bricks.utils.ReflectionUtils.getComponentClassList;
import static org.bricks.utils.StringUtils.firstToLowercase;
import static java.util.Arrays.asList;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.beans.factory.config.AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;

/**
 * 注册动态代理接口
 *
 * @param <E> 开关注解
 * @param <I> 接口注解
 * @param <F> 工厂Bean
 */
public abstract class AbstractBeanDefinitionRegistrar<E extends Annotation, I extends Annotation, F extends AbstractFactoryBean>
        implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware
{

    /**
     * 开关注解
     */
    private final Class<E> enableAnnotationClass;

    /**
     * 接口注解
     */
    private final Class<I> interfaceAnnotationClass;

    /**
     * 工厂Bean
     */
    private final Class<F> factoryClazz;

    /**
     * 环境
     */
    private Environment environment;

    /**
     * 资源加载
     */
    private ResourceLoader resourceLoader;

    /**
     * 构造方法
     */
    @SuppressWarnings(UNCHECKED)
    protected AbstractBeanDefinitionRegistrar()
    {
        List<Class<?>> classList = getComponentClassList(getClass(), AbstractBeanDefinitionRegistrar.class);
        enableAnnotationClass = (Class<E>) classList.get(0);
        interfaceAnnotationClass = (Class<I>) classList.get(1);
        factoryClazz = (Class<F>) classList.get(2);
    }

    @Override
    public void setEnvironment(@NonNull Environment environment)
    {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            @NonNull BeanDefinitionRegistry registry, @NonNull BeanNameGenerator importBeanNameGenerator)
    {
        String annotationName = enableAnnotationClass.getName();
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(annotationName);
        if (isNotEmpty(annotationAttributes))
        {
            List<String> packageList = newArrayList("org");
            String[] packages = (String[]) annotationAttributes.get("basePackages");
            if (isNotEmpty(packages))
            {
                packageList.addAll(asList(packages));
            }
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false, environment,
                    resourceLoader)
            {

                @Override
                protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)
                {
                    return beanDefinition.getMetadata()
                            .isInterface();
                }

            };
            scanner.addIncludeFilter(new AnnotationTypeFilter(interfaceAnnotationClass, true, true));
            packageList.stream()
                    .map(scanner::findCandidateComponents)
                    .flatMap(Collection::stream)
                    .forEach(accept(
                            beanDefinition -> registerBean(registry, Class.forName(beanDefinition.getBeanClassName())),
                            null, null, null, null));
        }
    }

    private void registerBean(BeanDefinitionRegistry registry, Class<?> clazz)
    {
        BeanDefinitionBuilder builder = genericBeanDefinition(clazz);
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        beanDefinition.getConstructorArgumentValues()
                .addGenericArgumentValue(clazz);
        beanDefinition.setBeanClass(factoryClazz);
        beanDefinition.setAutowireMode(AUTOWIRE_BY_TYPE);
        String beanName = getBeanName(clazz);
        if (isBlank(beanName))
        {
            beanName = firstToLowercase(clazz.getSimpleName());
        }
        registry.registerBeanDefinition(beanName, beanDefinition);
        addClass(interfaceAnnotationClass.getName(), clazz);
    }

    /**
     * 获取bean name
     *
     * @param clazz 接口
     * @return bean name
     */
    protected abstract String getBeanName(Class<?> clazz);

}
