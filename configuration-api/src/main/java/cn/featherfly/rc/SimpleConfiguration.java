
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

    private ConfigurationRepository configurationRepository;

    /**
     * @param name                    配置定义名称
     * @param descp                   配置定义描述
     * @param configurationRepository 存储支持
     */
    public SimpleConfiguration(String name, String descp, ConfigurationRepository configurationRepository) {
        super();
        AssertIllegalArgument.isNotNull(name, "name");
        AssertIllegalArgument.isNotNull(configurationRepository, "configurationRepository");
        this.name = name;
        this.descp = descp;
        this.configurationRepository = configurationRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V extends Serializable> V get(String key, Class<V> type) {
        return configurationRepository.get(name, key, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V extends Serializable> Configuration set(String key, V value) {
        configurationRepository.set(name, key, value);
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
