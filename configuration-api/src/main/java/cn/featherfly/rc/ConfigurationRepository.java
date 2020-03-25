
package cn.featherfly.rc;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * ConfigurationRepository
 * </p>
 *
 * @author 钟冀
 */
public interface ConfigurationRepository {
    /**
     * <p>
     * 获取配置信息对象集合
     * </p>
     *
     * @return 配置信息对象集合
     */
    Collection<Configuration> getConfigurations();

    /**
     * <p>
     * 获取指定名称的配置
     * </p>
     *
     * @param configName 配置定义名称
     * @return 配置对象
     */
    Configuration getConfiguration(String configName);

    /**
     * <p>
     * 获取指定名称的配置
     * </p>
     *
     * @param configName 配置定义名称
     * @param type       配置定义类型
     * @param <C>        配置对象泛型
     * @return 配置对象
     */
    <C extends Configuration> C getConfiguration(String configName, Class<C> type);

    /**
     * <p>
     * 设置指定配置定义的指定配置项的值，如果配置未初始化，会抛出异常.
     * </p>
     *
     * @param configName 配置定义名称
     * @param name       配置名称
     * @param value      值
     * @param <V>        泛型
     */
    <V extends Object> ConfigurationRepository set(String configName, String name, V value);

    /**
     * <p>
     * 设置指定配置定义的指定配置项的值，如果配置未初始化，会抛出异常.
     * </p>
     *
     * @param configName   配置定义名称
     * @param nameValueMap 多个配置名，值对组成的Map
     * @param <V>          泛型
     */
    <V extends Object> ConfigurationRepository set(String configName, Map<String, V> nameValueMap);

    /**
     * <p>
     * 获取指定配置定义的指定配置的值
     * </p>
     *
     * @param configName 配置定义名称
     * @param name       配置名称
     * @param type       值类型
     * @param <V>        泛型
     * @return 值
     */
    <V extends Object> V get(String configName, String name, Class<V> type);
}
