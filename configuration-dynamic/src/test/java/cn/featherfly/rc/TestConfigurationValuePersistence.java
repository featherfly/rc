package cn.featherfly.rc;

import java.util.HashMap;
import java.util.Map;

public class TestConfigurationValuePersistence implements ConfigurationValuePersistence {

    private Map<String, Object> map = new HashMap<>();

    @Override
    public <V extends Object> ConfigurationValuePersistence set(String configName, String name, V value) {
        map.put(name, value);
        return this;
    }

    @Override
    public <V extends Object> V get(String configName, String name, Class<V> type) {
        return (V) map.get(name);
    }
}