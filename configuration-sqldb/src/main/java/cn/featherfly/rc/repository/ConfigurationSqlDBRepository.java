package cn.featherfly.rc.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import cn.featherfly.common.lang.Lang;
import cn.featherfly.conversion.string.ToStringConversionPolicys;
import cn.featherfly.conversion.string.ToStringTypeConversion;
import cn.featherfly.hammer.Hammer;
import cn.featherfly.hammer.sqldb.SqldbHammer;
import cn.featherfly.hammer.sqldb.jdbc.Jdbc;
import cn.featherfly.rc.Configuration;
import cn.featherfly.rc.ConfigurationException;
import cn.featherfly.rc.ConfigurationRepository;
import cn.featherfly.rc.ConfigurationUtils;
import cn.featherfly.rc.ConfigurationValue;
import cn.featherfly.rc.SimpleConfiguration;
import cn.featherfly.rc.SimpleConfigurationValue;

/**
 * ConfigurationPersistenceService.
 *
 * @author 钟冀
 */
public class ConfigurationSqlDBRepository implements ConfigurationRepository {

    /** The logger. */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Instantiates a new configuration sql DB repository.
     */
    public ConfigurationSqlDBRepository() {
        super();
    }

    private Hammer hammer;

    private Jdbc jdbc;

    private ToStringTypeConversion conversion = new ToStringTypeConversion(
        ToStringConversionPolicys.getFormatConversionPolicy());

    private static final String CONFIGURATION_DIFINITION_TABLE_NAME = "RC_CONFIGURATION_DIFINITION";

    private static final String CONFIGURATION_VALUE_TABLE_NAME = "RC_CONFIGURATION_VALUE";

    private static final String CONFIG_NAME = "config_name";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String DESCP = "descp";

    /**
     * {@inheritDoc}
     */
    @Override
    @CachePut(value = { "configurationCache" }, key = "'config:'+ #configName + ':' + #name")
    @Transactional
    public <V extends Object> V set(String configName, String name, V value) {
        if (hammer.update(CONFIGURATION_VALUE_TABLE_NAME).set(valueColumn(), value).where()
            .eq(configNameColumn(), configName) //
            .and().eq(nameColumn(), name) //
            .execute() < 1) {
            ConfigurationException.throwConfigNotInit(configName, name);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ConfigurationRepository set(String configName, Map<String, Object> configNameValueMap) {
        configNameValueMap.entrySet().forEach(e -> this.set(configName, e.getKey(), e.getValue()));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(value = { "configurationCache" }, key = "'config:'+ #configName + ':' + #name")
    public <V extends Object> V get(String configName, String name, Class<V> type) {
        String valueStr = hammer.query(CONFIGURATION_VALUE_TABLE_NAME).field(valueColumn()) //
            .where() //
            .eq(configNameColumn(), configName) //
            .and().eq(nameColumn(), name) //
            .string();
        return conversion.targetToSource(valueStr, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getConfigurations() {
        List<Map<String, Serializable>> configs = hammer.query(CONFIGURATION_DIFINITION_TABLE_NAME).sort()
            .asc(nameColumn()).list();
        List<Configuration> configurations = new ArrayList<>();
        for (Map<String, Serializable> configMap : configs) {
            configurations.add(create(configMap));
        }
        return configurations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(String name) {
        Map<String, Serializable> map = hammer.query(CONFIGURATION_DIFINITION_TABLE_NAME).where().eq(nameColumn(), name)
            .single();
        return create(map);
    }

    private Configuration create(Map<String, Serializable> configMap) {
        if (Lang.isEmpty(configMap)) {
            return null;
        }

        Object objDescp = configMap.get(descpColumn());
        String descp = objDescp == null ? null : objDescp.toString();
        return new SimpleConfiguration(configMap.get(nameColumn()).toString(), descp, this);
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
    public List<ConfigurationValue<?>> getConfigurations(String configName) {
        return hammer.query(CONFIGURATION_VALUE_TABLE_NAME).where() //
            .eq(configNameColumn(), configName) //
            .sort() //
            .asc(nameColumn()) //
            .list(SimpleConfigurationValue.class).stream().map(v -> (ConfigurationValue<?>) v)
            .collect(Collectors.toList());
        //        String sql = "select * from rc_configuration_difinition where name like '%" + configName + "%' order by NAME";
        //        return jdbcPersistence.findList(sql);
    }

    /**
     * Gets the config values.
     *
     * @param configName the config name
     * @return the config values
     */
    public List<Map<String, Serializable>> getConfigValues(String configName) {
        return hammer.query(CONFIGURATION_VALUE_TABLE_NAME) //
            .fields("*", configNameColumn() + " configName") //
            .where() //
            .co(configNameColumn(), configName) //
            .sort() //
            .asc(configNameColumn(), nameColumn()) //
            .list();
        //        String sql = "select *,config_name configName from rc_configuration_value where config_name = ? order by config_name, NAME";
        //        return jdbcPersistence.findList(sql, new Object[] { configName });
    }

    /**
     * 返回hammer.
     *
     * @return hammer
     */
    public Hammer getHammer() {
        return hammer;
    }

    /**
     * 设置hammer.
     *
     * @param hammer hammer
     */
    public void setHammer(Hammer hammer) {
        this.hammer = hammer;
        jdbc = ((SqldbHammer) hammer).getJdbc();
    }

    /**
     * 返回conversion.
     *
     * @return conversion
     */
    public ToStringTypeConversion getConversion() {
        return conversion;
    }

    /**
     * 设置conversion.
     *
     * @param conversion conversion
     */
    public void setConversion(ToStringTypeConversion conversion) {
        this.conversion = conversion;
    }

    private String nameColumn() {
        return jdbc.getDialect().convertTableOrColumnName(NAME);
    }

    private String valueColumn() {
        return jdbc.getDialect().convertTableOrColumnName(VALUE);
    }

    private String configNameColumn() {
        return jdbc.getDialect().convertTableOrColumnName(CONFIG_NAME);
    }

    private String descpColumn() {
        return jdbc.getDialect().convertTableOrColumnName(DESCP);
    }
}
