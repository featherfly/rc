package cn.featherfly.rc.persistence;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;

import cn.featherfly.common.io.ClassPathScanningProvider;

/**
 * <p>
 * ConfigurationPersistenceService
 * </p>
 *
 * @author 钟冀
 */
public class PropertiesFileScanConfigurationConfigurator extends PropertiesFileConfigurationConfigurator {

    /**
     * @param projectName
     * @param basePackages
     */
    public PropertiesFileScanConfigurationConfigurator(String projectName, Set<String> basePackages) {
        super(projectName, new HashSet<>());
        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider()
                .findMetadata(basePackages.toArray(new String[] {}));
        setMetadataReaders(metadataReaders);
        init();
    }

}
