
package cn.featherfly.rc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.persistence.jdbc.JdbcPersistence;
import cn.featherfly.rc.Configuration;
import cn.featherfly.rc.ConfigurationException;
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
public class ConfigurationService implements ConfigurationPersistence, ConfigurationValuePersistence{
    
    /**
     * 
     */
    public ConfigurationService() {
        super();
    }

    private static final String CONFIGURATION_DIFINITION_TABLE_NAME = "RC_CONFIGURATION_DIFINITION";
    
    private static final String CONFIGURATION_VALUE_TABLE_NAME = "RC_CONFIGURATION_VALUE";
    
    private JdbcPersistence jdbcPersistence;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @CachePut(value = "configurationCache", key = "'config:'+ #configName + ':' + #name"
        , cacheManager = "configurationCacheManager")
    public <V extends Serializable> V set(String configName, String name,
            V value) {
        String sql = String.format("update %s set value = ? where config_name = ? and name = ?"
                , CONFIGURATION_VALUE_TABLE_NAME);
        if (jdbcPersistence.execute(sql, new Object[] {value, configName, name}) < 1) {
            ConfigurationException.throwConfigNotInit(configName, name);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = "configurationCache", key = "'config:'+ #configName + ':' + #name"
        , cacheManager = "configurationCacheManager")
    public <V extends Serializable> V get(String configName, String name,
            Class<V> type) {
        String sql = String.format("select value from %s where config_name = ? and name = ?"
                , CONFIGURATION_VALUE_TABLE_NAME);
        V value = jdbcPersistence.findForType(sql, type, new Object[] {configName, name});
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getConfigurations() {
        String sql = String.format("select name from %s order by NAME"
                , CONFIGURATION_DIFINITION_TABLE_NAME);
        List<String> names = jdbcPersistence.findForList(sql, String.class, new Object[] {});
        List<Configuration> configurations = new ArrayList<>();
        for (String name : names) {
            configurations.add(getConfiguration(name));
        }
        return configurations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(String name) {
        SimpleConfiguration simpleConfiguration = new SimpleConfiguration(name, this);
        return simpleConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends Configuration> C getConfiguration(String name,
            Class<C> type) {
        C configuration = (C) ClassUtils.newInstance(type, new Object[] {name, this});        
        return configuration;
    }

    
}
