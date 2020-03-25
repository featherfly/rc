package cn.featherfly.rc.repository;

import java.util.Set;

import cn.featherfly.common.io.ClassPathScanningProvider;

/**
 * <p>
 * PropertiesFileScanConfigurationConfigurator
 * </p>
 *
 * @author 钟冀
 */
public class PropertiesFileScanConfigurator extends PropertiesFileConfigurator {

    /**
     * @param projectName  projectName
     * @param basePackages basePackages
     */
    public PropertiesFileScanConfigurator(String projectName, Set<String> basePackages) {
        super(projectName, new ClassPathScanningProvider().findMetadata(basePackages.toArray(new String[] {})));
    }

}
