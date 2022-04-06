
package cn.featherfly.rc;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import cn.featherfly.common.exception.LocalizedException;

/**
 * <p>
 * ConfigurationException
 * </p>
 *
 * @author 钟冀
 */
public class ConfigurationException extends LocalizedException {

    private static final long serialVersionUID = -4694242174379739049L;

    /**
     * throwConfigNotInit
     *
     * @param configName
     * @param name
     */
    public static void throwConfigNotInit(String configName, String name) {
        throw new ConfigurationException("#config.not.init", new Object[] { configName, name })
                .setCharset(StandardCharsets.UTF_8);
    }

    /**
     * throwConfigDuplicateKey
     *
     * @param configName
     * @param className1
     * @param className2
     */
    public static void throwConfigDuplicateKey(String configName, String className1, String className2) {
        throw new ConfigurationException("#config.duplicate.name", new Object[] { configName, className1, className2 })
                .setCharset(StandardCharsets.UTF_8);
    }

    /**
     * @param message
     * @param locale
     * @param ex
     */
    public ConfigurationException(String message, Locale locale, Throwable ex) {
        super(message, locale, ex);
    }

    /**
     * @param message
     * @param locale
     */
    public ConfigurationException(String message, Locale locale) {
        super(message, locale);
    }

    /**
     * @param message
     * @param argus
     * @param locale
     * @param ex
     */
    public ConfigurationException(String message, Object[] argus, Locale locale, Throwable ex) {
        super(message, argus, locale, ex);
    }

    /**
     * @param message
     * @param argus
     * @param locale
     */
    public ConfigurationException(String message, Object[] argus, Locale locale) {
        super(message, argus, locale);
    }

    /**
     * @param message
     * @param argus
     * @param ex
     */
    public ConfigurationException(String message, Object[] argus, Throwable ex) {
        super(message, argus, ex);
    }

    /**
     * @param message
     * @param argus
     */
    public ConfigurationException(String message, Object[] argus) {
        super(message, argus);
    }

    /**
     * @param message
     * @param ex
     */
    public ConfigurationException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * @param message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * @param ex
     */
    public ConfigurationException(Throwable ex) {
        super(ex);
    }
}
