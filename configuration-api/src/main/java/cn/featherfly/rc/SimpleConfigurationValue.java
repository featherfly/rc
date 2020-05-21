
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
public class SimpleConfigurationValue<V> implements ConfigurationValue<V> {

    private String name;

    private V value;

    private String descp;

    /**
     * Instantiates a new configuration value.
     */
    public SimpleConfigurationValue() {
    }

    /**
     * Instantiates a new configuration value.
     *
     * @param name  the name
     * @param value the value
     */
    public SimpleConfigurationValue(String name, V value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * Instantiates a new configuration value.
     *
     * @param name  the name
     * @param value the value
     * @param descp the descp
     */
    public SimpleConfigurationValue(String name, V value, String descp) {
        super();
        this.name = name;
        this.value = value;
        this.descp = descp;
    }

    /**
     * 返回name.
     *
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 设置name.
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 返回value.
     *
     * @return value
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * 设置value.
     *
     * @param value value
     */
    public void setValue(V value) {
        this.value = value;
    }

    /**
     * 返回descp.
     *
     * @return descp
     */
    @Override
    public String getDescp() {
        return descp;
    }

    /**
     * 设置descp.
     *
     * @param descp descp
     */
    public void setDescp(String descp) {
        this.descp = descp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SimpleConfigurationValue [name=" + name + ", value=" + value + ", descp=" + descp + "]";
    }
}
