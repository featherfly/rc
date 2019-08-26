package cn.featherfly.rc.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.reflect.GenericClass;
import cn.featherfly.conversion.core.ConversionPolicysJdk8;
import cn.featherfly.conversion.core.TypeConversion;
import cn.featherfly.rc.Configuration;
import cn.featherfly.rc.ConfigurationPersistence;
import cn.featherfly.rc.ConfigurationValuePersistence;
import cn.featherfly.rc.SimpleConfiguration;

/**
 * <p>
 * ConfigurationPersistenceService
 * </p>
 *
 * @author 钟冀
 */
public class ConfigurationPersistenceFileImpl
        implements ConfigurationPersistence, ConfigurationValuePersistence {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private PropertiesFileConfigurationConfigurator configurator;

    private TypeConversion conversion;

    /**
     * 
     * @param configurator
     */
    public ConfigurationPersistenceFileImpl(
            PropertiesFileConfigurationConfigurator configurator) {
        this(configurator, new TypeConversion(
                ConversionPolicysJdk8.getBasicConversionPolicy()));
    }

    /**
     * 
     * @param configurator
     * @param conversion
     */
    public ConfigurationPersistenceFileImpl(
            PropertiesFileConfigurationConfigurator configurator,
            TypeConversion conversion) {
        this.configurator = configurator;
        this.conversion = conversion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V extends Object> V set(String configName, String name, V value) {
        configurator.setConfig(configName, name, conversion.toString(value,
                new GenericClass<>(value.getClass())));
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <V extends Object> V get(String configName, String name,
            Class<V> type) {
        String valueStr = configurator.getConfig(configName, name);
        return (V) conversion.toObject(valueStr, new GenericClass<>(type));
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
        SimpleConfiguration simpleConfiguration = new SimpleConfiguration(name,
                this);
        return simpleConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends Configuration> C getConfiguration(String name,
            Class<C> type) {
        C configuration = ClassUtils.newInstance(type,
                new Object[] { name, this });
        return configuration;
    }
}
