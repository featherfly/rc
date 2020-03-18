package cn.featherfly.rc.persistence;

import java.util.Set;

import cn.featherfly.common.io.ClassPathScanningProvider;

/**
 * <p>
 * PropertiesFileScanConfigurationConfigurator
 * </p>
 *
 * @author 钟冀
 */
public class PropertiesFileScanConfigurationConfigurator extends PropertiesFileConfigurationConfigurator {

    /**
     * @param projectName  projectName
     * @param basePackages basePackages
     */
    public PropertiesFileScanConfigurationConfigurator(String projectName, Set<String> basePackages) {
        super(projectName, new ClassPathScanningProvider().findMetadata(basePackages.toArray(new String[] {})));
    }

}
