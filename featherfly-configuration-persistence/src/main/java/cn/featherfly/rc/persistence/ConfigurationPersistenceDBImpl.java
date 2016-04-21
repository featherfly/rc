package cn.featherfly.rc.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ConfigurationPersistenceDBImpl implements ConfigurationPersistence, ConfigurationValuePersistence{
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 
     */
    public ConfigurationPersistenceDBImpl() {
        super();
    }

    public static final String CONFIGURATION_DIFINITION_TABLE_NAME = "RC_CONFIGURATION_DIFINITION";
    
    public static final String CONFIGURATION_VALUE_TABLE_NAME = "RC_CONFIGURATION_VALUE";
    
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
    
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    @CachePut(value = "configurationCache", key = "'config:'+ #configName + ':' + #name"
//    , cacheManager = "configurationCacheManager")
//    public <V extends Serializable> V init(String configName, String name,
//            V value, String descp) {
//        if (jdbcPersistence.findForString(String.format("select name from %s where config_name = ? and name = ?"
//                , CONFIGURATION_VALUE_TABLE_NAME), new Object[] {configName, name}) == null) {
//            logger.debug("init configuration({}) value -> name : {}, value : {}, descp : {}"
//                    , configName, name, value, descp);
//            String sql = String.format("insert %s (config_name, name, value, descp) values (?, ?, ?, ?)"
//                    , CONFIGURATION_VALUE_TABLE_NAME);
//            jdbcPersistence.execute(sql, new Object[] {configName, name, value, descp});            
//        } else {
//            logger.debug("configuration（{}.{}） already inited", configName, name);
//        }
//        return value;
//    }

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

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void init(String name, String descp) {
//        if (jdbcPersistence.findForString(String.format("select name from %s where name = ?"
//                , CONFIGURATION_DIFINITION_TABLE_NAME), new Object[] {name}) == null) {
//            logger.debug("init configuration difinition -> name : {} , descp : {}", name, descp);
//            String sql = String.format("insert %s (name, descp) values (?, ?)"
//                    , CONFIGURATION_DIFINITION_TABLE_NAME);
//            jdbcPersistence.execute(sql, new Object[] {name , descp});
//        } else {
//            logger.debug("configuration difinition -> name : {} already inited", name);
//        }
//    }    
}
