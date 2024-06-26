
package cn.featherfly.rc.repository;

/**
 * Config.
 *
 * @author zhongj
 */
public class Config {

    private String configName;

    private String configDescp;

    private String name;

    private String descp;

    private String value;

    /**
     * Instantiates a new config.
     */
    public Config() {
        super();
    }

    /**
     * Instantiates a new config.
     *
     * @param name  the name
     * @param value the value
     */
    public Config(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * 返回name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name.
     *
     * @param  name name
     * @return      the config
     */
    public Config setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 返回descp.
     *
     * @return descp
     */
    public String getDescp() {
        return descp;
    }

    /**
     * 设置descp.
     *
     * @param  descp descp
     * @return       the config
     */
    public Config setDescp(String descp) {
        this.descp = descp;
        return this;
    }

    /**
     * 返回value.
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置value.
     *
     * @param  value value
     * @return       the config
     */
    public Config setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 返回configName.
     *
     * @return configName
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * 设置configName.
     *
     * @param  configName configName
     * @return            the config
     */
    public Config setConfigName(String configName) {
        this.configName = configName;
        return this;
    }

    /**
     * 返回configDescp.
     *
     * @return configDescp
     */
    public String getConfigDescp() {
        return configDescp;
    }

    /**
     * 设置configDescp.
     *
     * @param  configDescp configDescp
     * @return             the config
     */
    public Config setConfigDescp(String configDescp) {
        this.configDescp = configDescp;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Config [configName=" + configName + ", configDescp=" + configDescp + ", name=" + name + ", descp="
            + descp + ", value=" + value + "]";
    }

}
