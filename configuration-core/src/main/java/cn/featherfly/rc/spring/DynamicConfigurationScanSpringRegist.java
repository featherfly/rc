package cn.featherfly.rc.spring;

import java.util.Set;

import cn.featherfly.rc.ConfigurationManager;

/**
 * <p>
 * 自动注册配置信息到spring context
 * </p>
 *
 * @author 钟冀
 */
public class DynamicConfigurationScanSpringRegist extends DynamicConfigurationSpringRegist {

    /**
     * @param basePackages                           basePackages
     * @param configurationValuePersistenceReference configurationValuePersistenceReference
     */
    public DynamicConfigurationScanSpringRegist(Set<String> basePackages,
            String configurationValuePersistenceReference) {
        this(basePackages, configurationValuePersistenceReference, null);
    }

    /**
     * Instantiates a new dynamic configuration scan spring regist.
     *
     * @param basePackages                           basePackages
     * @param configurationValuePersistenceReference configurationValuePersistenceReference
     * @param classLoader                            the class loader
     */
    public DynamicConfigurationScanSpringRegist(Set<String> basePackages, String configurationValuePersistenceReference,
            ClassLoader classLoader) {
        super(ConfigurationManager.getInstance().scanConfigurations(basePackages.toArray(new String[] {})),
                configurationValuePersistenceReference, classLoader);
    }
}
