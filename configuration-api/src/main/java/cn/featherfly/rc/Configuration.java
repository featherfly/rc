
package cn.featherfly.rc;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * 配置接口.
 * </p>
 *
 * @author 钟冀
 */
public interface Configuration {

    /**
     * <p>
     * 获取配置对象名称
     * </p>
     * .
     *
     * @return 配置对象名称
     */
    String getName();

    /**
     * <p>
     * 获取当前配置对象的描述
     * </p>
     * .
     *
     * @return 配置对象描述
     */
    String getDescp();

    /**
     * <p>
     * 获取指定配置
     * </p>
     * .
     *
     * @param <V>  泛型
     * @param name 配置名称
     * @param type 值类型
     * @return 值
     */
    <V extends Serializable> V get(String name, Class<V> type);

    /**
     * <p>
     * 设置指定配置
     * </p>
     * .
     *
     * @param <V>   泛型
     * @param name  配置名称
     * @param value 值
     * @return this
     */
    <V extends Serializable> Configuration set(String name, V value);
}
