package cn.featherfly.rc.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.reflect.GenericClass;
import cn.featherfly.conversion.core.ConversionPolicysJdk8;
import cn.featherfly.conversion.core.TypeConversion;
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
public class ConfigurationPersistenceSqlDBImpl implements ConfigurationPersistence, ConfigurationValuePersistence {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     */
    public ConfigurationPersistenceSqlDBImpl() {
        super();
    }

    @Resource(name = "jdbcPersistence")
    private JdbcPersistence jdbcPersistence;

    private TypeConversion conversion = new TypeConversion(ConversionPolicysJdk8.getFormatConversionPolicy());

    private static final String CONFIGURATION_DIFINITION_TABLE_NAME = "RC_CONFIGURATION_DIFINITION";

    private static final String CONFIGURATION_VALUE_TABLE_NAME = "RC_CONFIGURATION_VALUE";

    /**
     * {@inheritDoc}
     */
    @Override
    @CachePut(value = { "configurationCache" }, key = "'config:'+ #configName + ':' + #name")
    @Transactional
    public <V extends Object> V set(String configName, String name, V value) {
        String sql = String.format("update %s set value = ? where config_name = ? and name = ?",
                CONFIGURATION_VALUE_TABLE_NAME);
        if (jdbcPersistence.execute(sql, new Object[] { value, configName, name }) < 1) {
            ConfigurationException.throwConfigNotInit(configName, name);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    @Cacheable(value = { "configurationCache" }, key = "'config:'+ #configName + ':' + #name")
    public <V extends Object> V get(String configName, String name, Class<V> type) {
        String sql = String.format("select value from %s where config_name = ? and name = ?",
                CONFIGURATION_VALUE_TABLE_NAME);
        String valueStr = jdbcPersistence.findForString(sql, new Object[] { configName, name });
        return (V) conversion.toObject(valueStr, new GenericClass<>(type));
        //        V value = jdbcPersistence.findForType(sql, type, new Object[]{configName, name});
        //        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getConfigurations() {
        String sql = String.format("select name from %s order by NAME", CONFIGURATION_DIFINITION_TABLE_NAME);
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
    public <C extends Configuration> C getConfiguration(String name, Class<C> type) {
        C configuration = ClassUtils.newInstance(type, new Object[] { name, this });
        return configuration;
    }

    public List<Map<String, Object>> getConfigurations(String configName) {
        String sql = "select * from rc_configuration_difinition where name like '%" + configName + "%' order by NAME";
        return jdbcPersistence.findList(sql);
    }

    public List<Map<String, Object>> getConfigValues(String configName) {
        String sql = "select *,config_name configName from rc_configuration_value where config_name = ? order by config_name, NAME";
        return jdbcPersistence.findList(sql, new Object[] { configName });
    }
}
