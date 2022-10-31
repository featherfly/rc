
package cn.featherfly.rc;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import cn.featherfly.common.exception.LocalizedException;

/**
 * <p>
 * ConfigurationException
 * </p>
 * .
 *
 * @author 钟冀
 */
public class ConfigurationException extends LocalizedException {

    private static final long serialVersionUID = -4694242174379739049L;

    /**
     * throwConfigNotInit.
     *
     * @param configName the config name
     * @param name       the name
     */
    public static void throwConfigNotInit(String configName, String name) {
        throw new ConfigurationException("#config.not.init", new Object[] { configName, name })
                .setCharset(StandardCharsets.UTF_8);
    }

    /**
     * throwConfigDuplicateKey.
     *
     * @param configName the config name
     * @param className1 the class name 1
     * @param className2 the class name 2
     */
    public static void throwConfigDuplicateKey(String configName, String className1, String className2) {
        throw new ConfigurationException("#config.duplicate.name", new Object[] { configName, className1, className2 })
                .setCharset(StandardCharsets.UTF_8);
    }

    /**
     * Instantiates a new configuration exception.
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param message the message
     * @param locale  the locale
     * @param ex      the ex
     */
    public ConfigurationException(String message, Locale locale, Throwable ex) {
        super(message, locale, ex);
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param message the message
     * @param locale  the locale
     */
    public ConfigurationException(String message, Locale locale) {
        super(message, locale);
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param message the message
     * @param argus   the argus
     * @param locale  the locale
     * @param ex      the ex
     */
    public ConfigurationException(String message, Object[] argus, Locale locale, Throwable ex) {
        super(message, argus, locale, ex);
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param message the message
     * @param argus   the argus
     * @param locale  the locale
     */
    public ConfigurationException(String message, Object[] argus, Locale locale) {
        super(message, argus, locale);
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param message the message
     * @param argus   the argus
     * @param ex      the ex
     */
    public ConfigurationException(String message, Object[] argus, Throwable ex) {
        super(message, argus, ex);
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param message the message
     * @param argus   the argus
     */
    public ConfigurationException(String message, Object[] argus) {
        super(message, argus);
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param message the message
     * @param ex      the ex
     */
    public ConfigurationException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param message the message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new configuration exception.
     *
     * @param ex the ex
     */
    public ConfigurationException(Throwable ex) {
        super(ex);
    }
}
