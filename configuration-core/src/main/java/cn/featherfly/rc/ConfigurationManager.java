
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
 * <p>
 * ConfigurationManager
 * </p>
 *
 * @author zhongj
 */
public class ConfigurationManager {

    private static final ConfigurationManager DEFAULT = new ConfigurationManager();

    public static ConfigurationManager getInstance() {
        return DEFAULT;
    }

    private Map<String, String> configNameTypeMap = new HashMap<>();

    /**
     * getConfigurations
     *
     * @param metadataReaders
     * @return
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
     * scanConfigurations
     *
     * @param basePackages
     * @return
     */
    public Set<MetadataReader> scanConfigurations(String... basePackages) {
        return getConfigurations(new ClassPathScanningProvider().findMetadata(basePackages));
    }

    private void check(MetadataReader metadataReader) {
        Class<?> type = ClassUtils.forName(metadataReader.getClassMetadata().getClassName());
        String configName = type.getAnnotation(Configurations.class).name();
        if (configNameTypeMap.containsKey(configName)) {
            ConfigurationException.throwConfigDuplicateKey(configName, configNameTypeMap.get(configName),
                    type.getName());
        }
        configNameTypeMap.put(configName, type.getName());
    }
}
