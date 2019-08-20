package cn.featherfly.rc.spring;

import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;

import cn.featherfly.common.io.ClassPathScanningProvider;

/**
 * <p>
 * 自动注册配置信息到spring context
 * </p>
 *
 * @author 钟冀
 */
public class DynamicConfigurationScanSpringRegist
        extends DynamicConfigurationSpringRegist {

    /**
     * @param basePackages
     *            basePackages
     * @param configurationValuePersistenceReference
     *            configurationValuePersistenceReference
     */
    public DynamicConfigurationScanSpringRegist(Set<String> basePackages,
            String configurationValuePersistenceReference) {
        super(configurationValuePersistenceReference);
        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider()
                .findMetadata(basePackages.toArray(new String[] {}));
        setMetadataReaders(metadataReaders);
    }

}
