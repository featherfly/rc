
package cn.featherfly.rc.spring;

import cn.featherfly.rc.Configuration;
import cn.featherfly.rc.ConfigurationPersistence;
import cn.featherfly.rc.SimpleConfiguration;

/**
 * <p>
 * FactoryBean
 * </p>
 * 
 * @author 钟冀
 */
public class ConfigurationFactoryBean implements org.springframework.beans.factory.FactoryBean<Configuration>{
    
    private ConfigurationPersistence configurationPersistence;
    
    private String name;
    
    /**
     * 
     */
    public ConfigurationFactoryBean() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getObject() throws Exception {
        return configurationPersistence.getConfiguration(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getObjectType() {
        return SimpleConfiguration.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 设置name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 设置configurationPersistence
     * @param configurationPersistence configurationPersistence
     */
    public void setConfigurationPersistence(
            ConfigurationPersistence configurationPersistence) {
        this.configurationPersistence = configurationPersistence;
    }
}
