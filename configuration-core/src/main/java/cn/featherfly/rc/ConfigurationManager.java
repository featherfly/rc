
package cn.featherfly.rc;

/**
 * <p>
 * ConfigurationManager
 * </p>
 *
 * @author zhongj
 */
public class ConfigurationManager {

    private static final ConfigurationManager DEFAULT = new ConfigurationManager();

    public static ConfigurationManager getInstance() {
        return DEFAULT;
    }

}
