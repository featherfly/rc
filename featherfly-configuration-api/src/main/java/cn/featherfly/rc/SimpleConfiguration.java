
package cn.featherfly.rc;

import java.io.Serializable;

/**
 * <p>
 * SimpleConfiguration
 * </p>
 * 
 * @author 钟冀
 */
public class SimpleConfiguration implements Configuration{
    
    private String name;
    
    private ConfigurationValuePersistence configurationValuePersistence;
    
    /**
     * @param name 配置定义名称
     * @param configurationValuePersistence 持久化支持
     */
    public SimpleConfiguration(String name, ConfigurationValuePersistence configurationValuePersistence) {
        super();
        this.name = name;
        this.configurationValuePersistence = configurationValuePersistence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V extends Serializable> V get(String key, Class<V> type) {
        return configurationValuePersistence.get(name, key, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V extends Serializable> Configuration set(String key, V value) {
        configurationValuePersistence.set(name, key, value);
        return this;
    }
}
