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

import cn.featherfly.common.exception.StandardSysException;
import cn.featherfly.common.io.ClassPathScanningProvider;
import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.rc.annotation.ConfigurationDifinition;
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
        
    private Set<String> basePackages = new HashSet<>();
    
    private DynamicConfigurationFacotry dynamicConfigurationFacotry = DynamicConfigurationFacotry.getInstance(); 
    
    private String configurationValuePersistenceReference;

    /**
     * @param basePackages
     * @param configurationValuePersistenceReference
     */
    public DynamicConfigurationSpringRegist(Set<String> basePackages,
            String configurationValuePersistenceReference) {
        super();
        this.basePackages = basePackages;
        this.configurationValuePersistenceReference = configurationValuePersistenceReference;
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
    public void postProcessBeanDefinitionRegistry(
            BeanDefinitionRegistry registry) throws BeansException {
        logger.debug("start regist config to spring");
        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider().findMetadata(
                basePackages.toArray(new String[] {}));
        for (MetadataReader metadataReader : metadataReaders) {
            if (metadataReader.getAnnotationMetadata().hasAnnotation(ConfigurationDifinition.class.getName())) {
                try {
                    Class<?> type = ClassUtils.forName(metadataReader.getClassMetadata().getClassName());
                    String configName = ((ConfigurationDifinition) type.getAnnotation(
                            ConfigurationDifinition.class)).name();
                    String dynamicImplName = dynamicConfigurationFacotry.create(type);
                    logger.debug("create class {} for {}", dynamicImplName, metadataReader.getClass().getName());
                    logger.debug("regist -> {} for config named {}", dynamicImplName, configName);
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ClassUtils.forName(dynamicImplName));
                    builder.addConstructorArgValue(configName);
                    builder.addConstructorArgReference(configurationValuePersistenceReference);
                    builder.setScope(BeanDefinition.SCOPE_SINGLETON);
                    registry.registerBeanDefinition(type.getName(), builder.getBeanDefinition());
                } catch (NotFoundException | CannotCompileException e) {
                    throw new StandardSysException(e);
                }
            }
        }
        logger.debug("end regist constant to spring");                
    }
}
