package cn.featherfly.rc.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.lang.reflect.ClassType;
import cn.featherfly.conversion.string.ToStringConversionPolicys;
import cn.featherfly.conversion.string.ToStringTypeConversion;
import cn.featherfly.rc.Configuration;
import cn.featherfly.rc.ConfigurationRepository;
import cn.featherfly.rc.ConfigurationUtils;
import cn.featherfly.rc.ConfigurationValue;
import cn.featherfly.rc.SimpleConfiguration;
import cn.featherfly.rc.SimpleConfigurationValue;

/**
 * ConfigurationFileRepository.
 *
 * @author 钟冀
 */
public class ConfigurationFileRepository implements ConfigurationRepository {

    /** The logger. */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private PropertiesFileConfigurator configurator;

    private ToStringTypeConversion conversion;

    /**
     * Instantiates a new configuration file repository.
     *
     * @param configurator the configurator
     */
    public ConfigurationFileRepository(PropertiesFileConfigurator configurator) {
        this(configurator, new ToStringTypeConversion(ToStringConversionPolicys.getBasicConversionPolicy()));
    }

    /**
     * Instantiates a new configuration file repository.
     *
     * @param configurator the configurator
     * @param conversion   the conversion
     */
    public ConfigurationFileRepository(PropertiesFileConfigurator configurator, ToStringTypeConversion conversion) {
        this.configurator = configurator;
        this.conversion = conversion;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <V extends Object> V set(String configName, String name, V value) {
        Config config = new Config();
        config.setName(name);
        config.setValue(conversion.sourceToTarget(value, (Class<V>) value.getClass()));
        configurator.setConfig(configName, config);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public ConfigurationRepository set(String configName, Map<String, Object> configNameValueMap) {
        configurator.setConfig(configName,
            configNameValueMap.entrySet().stream()
                .map(e -> new Config(e.getKey(),
                    conversion.sourceToTarget(e.getValue(), (Class<Object>) e.getValue().getClass())))
                .toArray(value -> new Config[configNameValueMap.size()]));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V extends Object> V get(String configName, String name, Class<V> type) {
        Config c = configurator.getConfig(configName, name);
        String valueStr = null;
        if (c != null) {
            valueStr = c.getValue();
        }
        return conversion.targetToSource(valueStr, new ClassType<>(type));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getConfigurations() {
        List<Configuration> configurations = new ArrayList<>();
        for (String name : configurator.getConfigNames()) {
            configurations.add(getConfiguration(name));
        }
        return configurations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(String name) {
        Config config = configurator.getConfigDifinition(name);
        SimpleConfiguration simpleConfiguration = new SimpleConfiguration(name, config.getDescp(), this);
        return simpleConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(Class<?> type) {
        return getConfiguration(ConfigurationUtils.getConfigurations(type).name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConfigurationValue<?>> getConfigurations(String name) {
        return configurator.getConfigs(name).stream().map(c -> {
            SimpleConfigurationValue<Object> value = new SimpleConfigurationValue<>();
            value.setName(c.getName());
            value.setValue(c.getValue());
            value.setDescp(c.getDescp());
            return (ConfigurationValue<?>) value;
        }).collect(Collectors.toList());

    }
}
