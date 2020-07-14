package cn.featherfly.rc.spring;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.type.classreading.MetadataReader;

import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.Lang;
import cn.featherfly.rc.ConfigurationException;
import cn.featherfly.rc.annotation.Configurations;
import cn.featherfly.rc.javassist.DynamicConfigurationFacotry;
import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * <p>
 * 自动注册配置信息到spring context
 * </p>
 *
 * @author 钟冀
 */
public class DynamicConfigurationSpringRegist implements BeanDefinitionRegistryPostProcessor {

    /**
     * logger
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Set<MetadataReader> metadataReaders = new HashSet<>();

    private DynamicConfigurationFacotry dynamicConfigurationFacotry = DynamicConfigurationFacotry.getInstance();

    private String configurationValuePersistenceReference;

    private ClassLoader classLoader;

    /**
     * Instantiates a new dynamic configuration spring regist.
     *
     * @param metadataReaders                        metadataReaders
     * @param configurationValuePersistenceReference configurationValuePersistenceReference
     */
    public DynamicConfigurationSpringRegist(Set<MetadataReader> metadataReaders,
            String configurationValuePersistenceReference) {
        this(metadataReaders, configurationValuePersistenceReference, null);
    }

    /**
     * Instantiates a new dynamic configuration spring regist.
     *
     * @param metadataReaders                        metadataReaders
     * @param configurationValuePersistenceReference configurationValuePersistenceReference
     * @param classLoader                            the class loader
     */
    public DynamicConfigurationSpringRegist(Set<MetadataReader> metadataReaders,
            String configurationValuePersistenceReference, ClassLoader classLoader) {
        super();
        this.metadataReaders = metadataReaders;
        this.configurationValuePersistenceReference = configurationValuePersistenceReference;
        this.classLoader = classLoader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (Lang.isEmpty(metadataReaders)) {
            logger.debug("metadataReaders is empty");
        }
        logger.debug("start regist configuration to spring");
        for (MetadataReader metadataReader : metadataReaders) {
            if (metadataReader.getAnnotationMetadata().hasAnnotation(Configurations.class.getName())) {
                try {
                    Class<?> type = ClassUtils.forName(metadataReader.getClassMetadata().getClassName());
                    Configurations cd = type.getAnnotation(Configurations.class);
                    if (cd == null) {
                        throw new ConfigurationException(String.format("there is no annotation[%s] in type[%s]",
                                Configurations.class.getName(), type.getName()));
                    }
                    String configName = cd.name();
                    String dynamicImplName = dynamicConfigurationFacotry.create(type, classLoader);
                    logger.debug("create class {} for {}", dynamicImplName, type.getName());
                    logger.debug("regist -> {} for config named {}", dynamicImplName, configName);
                    Class<?> newType;
                    if (classLoader == null) {
                        newType = ClassUtils.forName(dynamicImplName);
                    } else {
                        newType = classLoader.loadClass(dynamicImplName);
                    }
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(newType);
                    builder.addConstructorArgValue(configName);
                    builder.addConstructorArgReference(configurationValuePersistenceReference);
                    builder.setScope(BeanDefinition.SCOPE_SINGLETON);
                    // registry.registerBeanDefinition(type.getName(),
                    // builder.getBeanDefinition());
                    registry.registerBeanDefinition(configName, builder.getBeanDefinition());
                } catch (NotFoundException | CannotCompileException | ClassNotFoundException e) {
                    throw new ConfigurationException(e);
                }
            }
        }
        logger.debug("end regist configuration to spring");
    }

    /**
     * 返回metadataReaders
     *
     * @return metadataReaders
     */
    public Set<MetadataReader> getMetadataReaders() {
        return metadataReaders;
    }

}
