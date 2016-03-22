
package cn.featherfly.rc;

import java.util.Collection;

/**
 * <p>
 * ConfigurationPersistence
 * </p>
 * 
 * @author 钟冀
 */
public interface ConfigurationPersistence {
    /**
     * <p>
     * 获取配置信息对象集合
     * </p>
     * @return 配置信息对象集合
     */
    Collection<Configuration> getConfigurations();
    /**
     * <p>
     * 获取指定名称的配置
     * </p>
     * @param name 配置名称
     * @return 配置对象 
     */
    Configuration getConfiguration(String name);
    /**
     * <p>
     * 获取指定名称的配置
     * </p>
     * @param name 配置名称
     * @param type 配置定义类型
     * @param <C> 配置对象泛型 
     * @return 配置对象 
     */
    <C extends Configuration> C getConfiguration(String name, Class<C> type);
}
