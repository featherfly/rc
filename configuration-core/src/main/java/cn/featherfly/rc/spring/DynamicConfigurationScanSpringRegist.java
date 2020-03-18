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
        super(ConfigurationManager.getInstance().scanConfigurations(basePackages.toArray(new String[] {})),
                configurationValuePersistenceReference);
    }

}
