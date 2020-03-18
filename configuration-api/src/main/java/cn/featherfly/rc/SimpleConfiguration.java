
package cn.featherfly.rc;

import java.io.Serializable;

import cn.featherfly.common.lang.AssertIllegalArgument;

/**
 * <p>
 * SimpleConfiguration
 * </p>
 *
 * @author 钟冀
 */
public class SimpleConfiguration implements Configuration {

    private String name;

    private String descp;

    private ConfigurationValuePersistence configurationValuePersistence;

    /**
     * @param name                          配置定义名称
     * @param descp                         配置定义描述
     * @param configurationValuePersistence 持久化支持
     */
    public SimpleConfiguration(String name, String descp, ConfigurationValuePersistence configurationValuePersistence) {
        super();
        AssertIllegalArgument.isNotNull(name, "name");
        AssertIllegalArgument.isNotNull(configurationValuePersistence, "configurationValuePersistence");
        this.name = name;
        this.descp = descp;
        this.configurationValuePersistence = configurationValuePersistence;
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
    public String getDescp() {
        return descp;
    }
}
