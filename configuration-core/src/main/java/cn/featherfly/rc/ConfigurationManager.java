
package cn.featherfly.rc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;

import cn.featherfly.common.io.ClassPathScanningProvider;
import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.rc.annotation.Configurations;

/**
 * ConfigurationManager.
 *
 * @author zhongj
 */
public class ConfigurationManager {

    private static final ConfigurationManager DEFAULT = new ConfigurationManager();

    /**
     * Instantiates a new configuration manager.
     */
    public ConfigurationManager() {
    }

    /**
     * Gets the single instance of ConfigurationManager.
     *
     * @return single instance of ConfigurationManager
     */
    public static ConfigurationManager getInstance() {
        return DEFAULT;
    }

    private Map<String, String> configNameTypeMap = new HashMap<>();

    /**
     * getConfigurations.
     *
     * @param  metadataReaders the metadata readers
     * @return                 the configurations
     */
    public Set<MetadataReader> getConfigurations(Set<MetadataReader> metadataReaders) {
        Set<MetadataReader> configurations = new HashSet<>();
        for (MetadataReader metadataReader : metadataReaders) {
            if (metadataReader.getAnnotationMetadata().hasAnnotation(Configurations.class.getName())) {
                check(metadataReader);
                configurations.add(metadataReader);
            }
        }
        return configurations;
    }

    /**
     * scanConfigurations.
     *
     * @param  basePackages the base packages
     * @return              the sets the
     */
    public Set<MetadataReader> scanConfigurations(String... basePackages) {
        return getConfigurations(new ClassPathScanningProvider().findMetadata(basePackages));
    }

    private void check(MetadataReader metadataReader) {
        Class<?> type = ClassUtils.forName(metadataReader.getClassMetadata().getClassName());
        String configName = type.getAnnotation(Configurations.class).name();
        String t = configNameTypeMap.get(configName);
        // 忽略同样的类反复注册
        if (t != null && !t.equals(type.getName())) {
            ConfigurationException.throwConfigDuplicateKey(configName, configNameTypeMap.get(configName),
                type.getName());
        }
        configNameTypeMap.put(configName, type.getName());
    }
}
