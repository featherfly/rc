package cn.featherfly.rc.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.reflect.GenericClass;
import cn.featherfly.conversion.core.ConversionPolicys;
import cn.featherfly.conversion.core.TypeConversion;
import cn.featherfly.hammer.Hammer;
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

    private Hammer hammer;

    private TypeConversion conversion = new TypeConversion(ConversionPolicys.getFormatConversionPolicy());

    private static final String CONFIGURATION_DIFINITION_TABLE_NAME = "RC_CONFIGURATION_DIFINITION";

    private static final String CONFIGURATION_VALUE_TABLE_NAME = "RC_CONFIGURATION_VALUE";

    /**
     * {@inheritDoc}
     */
    @Override
    @CachePut(value = { "configurationCache" }, key = "'config:'+ #configName + ':' + #name")
    @Transactional
    public <V extends Object> ConfigurationValuePersistence set(String configName, String name, V value) {
        //        String sql = String.format("update %s set value = ? where config_name = ? and name = ?",
        //                CONFIGURATION_VALUE_TABLE_NAME);
        //        if (jdbcPersistence.execute(sql, new Object[] { value, configName, name }) < 1) {
        //            ConfigurationException.throwConfigNotInit(configName, name);
        //        }
        if (hammer.update(CONFIGURATION_VALUE_TABLE_NAME).set("value", value).where().eq("config_name", configName)
                .and().eq("name", name).execute() < 1) {
            ConfigurationException.throwConfigNotInit(configName, name);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> ConfigurationValuePersistence set(String configName, Map<String, V> configNameValueMap) {
        configNameValueMap.entrySet().forEach(e -> set(configName, e.getKey(), e.getValue()));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    @Cacheable(value = { "configurationCache" }, key = "'config:'+ #configName + ':' + #name")
    public <V extends Object> V get(String configName, String name, Class<V> type) {
        //        String sql = String.format("select value from %s where config_name = ? and name = ?",
        //                CONFIGURATION_VALUE_TABLE_NAME);
        //        String valueStr = jdbcPersistence.findForString(sql, new Object[] { configName, name });
        String valueStr = hammer.query(CONFIGURATION_VALUE_TABLE_NAME).property("value").where()
                .eq("config_name", configName).and().eq("name", name).string();
        return (V) conversion.toObject(valueStr, new GenericClass<>(type));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Configuration> getConfigurations() {
        //        String sql = String.format("select name from %s order by NAME", CONFIGURATION_DIFINITION_TABLE_NAME);
        //        List<String> names = jdbcPersistence.findForList(sql, String.class, new Object[] {});
        //        List<Configuration> configurations = new ArrayList<>();
        //        for (String name : names) {
        //            configurations.add(getConfiguration(name));
        //        }
        List<Map<String, Object>> configs = hammer.query(CONFIGURATION_DIFINITION_TABLE_NAME).sort().asc("name").list();
        List<Configuration> configurations = new ArrayList<>();
        for (Map<String, Object> configMap : configs) {
            configurations.add(create(configMap));
        }
        return configurations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(String name) {
        Map<String, Object> map = hammer.query(CONFIGURATION_DIFINITION_TABLE_NAME).where().eq("name", name).single();
        return create(map);
    }

    private Configuration create(Map<String, Object> configMap) {
        Object objDescp = configMap.get("descp");
        String descp = "";
        if (descp != null) {
            descp = objDescp.toString();
        }
        return new SimpleConfiguration(configMap.get("name").toString(), descp, this);
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
        return hammer.query(CONFIGURATION_DIFINITION_TABLE_NAME).where().co("name", configName).sort().asc("name")
                .list();
        //        String sql = "select * from rc_configuration_difinition where name like '%" + configName + "%' order by NAME";
        //        return jdbcPersistence.findList(sql);
    }

    public List<Map<String, Object>> getConfigValues(String configName) {
        return hammer.query(CONFIGURATION_VALUE_TABLE_NAME).property("*", "config_name configName").where()
                .co("config_name", configName).sort().asc("config_name", "name").list();
        //        String sql = "select *,config_name configName from rc_configuration_value where config_name = ? order by config_name, NAME";
        //        return jdbcPersistence.findList(sql, new Object[] { configName });
    }

    /**
     * 返回hammer
     *
     * @return hammer
     */
    public Hammer getHammer() {
        return hammer;
    }

    /**
     * 设置hammer
     *
     * @param hammer hammer
     */
    public void setHammer(Hammer hammer) {
        this.hammer = hammer;
    }

    /**
     * 返回conversion
     *
     * @return conversion
     */
    public TypeConversion getConversion() {
        return conversion;
    }

    /**
     * 设置conversion
     *
     * @param conversion conversion
     */
    public void setConversion(TypeConversion conversion) {
        this.conversion = conversion;
    }
}
