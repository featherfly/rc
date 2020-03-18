
package cn.featherfly.rc;

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
     * 设置指定配置定义的指定配置项的值，如果配置未初始化，会抛出异常.
     * </p>
     * 
     * @param configName 配置定义名称
     * @param name       配置名称
     * @param value      值
     * @param <V>        泛型
     */
    <V extends Object> ConfigurationValuePersistence set(String configName, String name, V value);

    //    /**
    //     * <p>
    //     * 设置指定配置定义的指定配置项的初始化值，如果已经存在，则不做任何事情.
    //     * </p>
    //     * @param configName 配置定义名称
    //     * @param name 配置名称
    //     * @param value 值
    //     * @param descp 描述信息
    //     * @param <V> 泛型
    //     */
    //    <V extends Serializable> V init(String configName, String name, V value, String descp);
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
