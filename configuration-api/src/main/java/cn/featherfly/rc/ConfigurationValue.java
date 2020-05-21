
package cn.featherfly.rc;

/**
 * <p>
 * ConfigurationValue
 * </p>
 * .
 *
 * @author zhongj
 * @param <V> the value type
 */
public interface ConfigurationValue<V> {

    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the descp.
     *
     * @return the descp
     */
    String getDescp();

    /**
     * Gets the value.
     *
     * @return the value
     */
    V getValue();
}
