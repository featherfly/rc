
package cn.featherfly.rc;

import java.io.Serializable;

/**
 * <p>
 * ConfigurationPersistence
 * </p>
 * 
 * @author 钟冀
 */
public interface ConfigurationValuePersistence {
    /**
     * <p>
     * 设置指定配置的指定key
     * </p>
     * @param configName 配置定义名称
     * @param name 配置名称
     * @param value 值
     * @param <V> 泛型
     */
    <V extends Serializable> V set(String configName, String name, V value);
    /**
     * <p>
     * 获取指定配置的指定key
     * </p>
     * @param configName 配置定义名称
     * @param name 配置名称
     * @param type 值类型
     * @param <V> 泛型
     * @return 值
     */
    <V extends Serializable> V get(String configName, String name, Class<V> type);
}
