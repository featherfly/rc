/*
 *
 */
package cn.featherfly.rc.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.classreading.MetadataReader;

import cn.featherfly.common.constant.Chars;
import cn.featherfly.common.io.Properties;
import cn.featherfly.common.io.Properties.Property;
import cn.featherfly.common.io.PropertiesImpl;
import cn.featherfly.common.lang.AssertIllegalArgument;
import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.Lang;
import cn.featherfly.common.lang.matcher.MethodNameRegexMatcher;
import cn.featherfly.rc.ConfigurationException;
import cn.featherfly.rc.annotation.Configuration;
import cn.featherfly.rc.annotation.Configurations;

/**
 * <p>
 * PropertiesFileConfigurationConfigurator
 * </p>
 * .
 *
 * @author 钟冀
 */
public class PropertiesFileConfigurator {

    /** The logger. */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // private static final String CONFIG_FILE_NAME =
    // "featherfly_configuration.properties";

    private Map<String, File> configFileMap = new HashMap<>();
    private Map<String, Properties> propertiesMap = new HashMap<>();

    /**
     * The Enum FilePolicy.
     */
    public enum FilePolicy {

        /** The each file for define. */
        EACH_FILE_FOR_DEFINE,
        /** The each file for define in package. */
        EACH_FILE_FOR_DEFINE_IN_PACKAGE
        //        , ALL_IN_ONE_FILE
    }

    /**
     * The Enum DirPolicy.
     */
    public enum DirPolicy {

        /** The user dir. */
        USER_DIR
    }

    private static final String DIR_NAME = ".featherfly_configuration";

    private FilePolicy filePolicy = FilePolicy.EACH_FILE_FOR_DEFINE;

    private DirPolicy dirPolicy = DirPolicy.USER_DIR;

    private Set<MetadataReader> metadataReaders = new HashSet<>();

    private File storeDir;

    private String projectName;

    /**
     * Instantiates a new properties file configurator.
     *
     * @param projectName     projectName
     * @param metadataReaders metadataReaders
     */
    public PropertiesFileConfigurator(String projectName, Set<MetadataReader> metadataReaders) {
        this(projectName, FilePolicy.EACH_FILE_FOR_DEFINE, DirPolicy.USER_DIR, metadataReaders);
    }

    /**
     * Instantiates a new properties file configurator.
     *
     * @param projectName     projectName
     * @param filePolicy      filePolicy
     * @param dirPolicy       dirPolicy
     * @param metadataReaders metadataReaders
     */
    public PropertiesFileConfigurator(String projectName, FilePolicy filePolicy, DirPolicy dirPolicy,
            Set<MetadataReader> metadataReaders) {
        super();
        AssertIllegalArgument.isNotNull(projectName, "projectName");
        this.projectName = projectName;
        this.filePolicy = filePolicy;
        this.dirPolicy = dirPolicy;
        this.metadataReaders = metadataReaders;
        init();
    }

    private void init() {
        if (dirPolicy == DirPolicy.USER_DIR) {
            storeDir = new File(
                    org.apache.commons.io.FileUtils.getUserDirectoryPath() + "/" + DIR_NAME + "/" + projectName);
        }
        if (filePolicy == FilePolicy.EACH_FILE_FOR_DEFINE || filePolicy == FilePolicy.EACH_FILE_FOR_DEFINE_IN_PACKAGE) {
            for (MetadataReader metadataReader : metadataReaders) {
                if (metadataReader.getAnnotationMetadata().hasAnnotation(Configurations.class.getName())) {
                    Class<?> type = ClassUtils.forName(metadataReader.getClassMetadata().getClassName());
                    String configName = type.getAnnotation(Configurations.class).name();
                    String descp = type.getAnnotation(Configurations.class).descp();
                    File file;
                    if (filePolicy == FilePolicy.EACH_FILE_FOR_DEFINE_IN_PACKAGE) {
                        file = new File(storeDir.getAbsoluteFile() + "/" + ClassUtils.packageToDir(type) + "/"
                                + type.getSimpleName() + ".properties");
                    } else {
                        file = new File(storeDir.getAbsoluteFile() + "/" + configName + ".properties");
                    }
                    final Properties properties;
                    if (!file.exists()) {
                        properties = new PropertiesImpl();
                        createFile(file, configName);
                        configFileMap.put(configName, file);
                        try {
                            properties.setProperty(type.getName(), configName, descp);

                            Collection<Method> getMethods = ClassUtils.findMethods(type,
                                    new MethodNameRegexMatcher("get.+"));
                            Collection<Method> setMethods = ClassUtils.findMethods(type,
                                    new MethodNameRegexMatcher("set.+"));

                            Map<String, Configuration> map = new HashMap<>();
                            for (Method setMethod : setMethods) {
                                Lang.ifNotEmpty(setMethod.getAnnotation(Configuration.class),
                                        a -> map.put(ClassUtils.getPropertyName(setMethod), a));
                            }
                            for (Method getMethod : getMethods) {
                                Lang.ifNotEmpty(getMethod.getAnnotation(Configuration.class),
                                        a -> map.put(ClassUtils.getPropertyName(getMethod), a));
                            }
                            map.entrySet()
                                    .forEach(e -> properties.setProperty(
                                            Lang.ifEmpty(e.getValue().name(), () -> e.getKey(), n -> n),
                                            e.getValue().value(), e.getValue().descp()));
                            properties.store(new FileOutputStream(file));

                            // configMap.put(configName, properties);
                            logger.debug("create file {} for {}", file.getAbsolutePath(), type.getName());
                        } catch (IOException e) {
                            // FIXME 需要更精确的异常描述
                            logger.error(e.getMessage());
                            ConfigurationException.throwConfigNotInit(configName, "*");
                        }
                    } else {
                        configFileMap.put(configName, file);
                        properties = loadConfig(configName);
                    }
                    propertiesMap.put(configName, properties);
                }
            }
        }
        // else if (filePolicy == FilePolicy.ALL_IN_ONE_FILE) {
        // File file = new File(
        // storeDir.getAbsolutePath() + "/" + CONFIG_FILE_NAME);
        // Properties properties = new Properties();
        // if (!file.exists()) {
        // createFile(file);
        // }
        // for (MetadataReader metadataReader : metadataReaders) {
        // Class<?> type = ClassUtils.forName(
        // metadataReader.getClassMetadata().getClassName());
        // String configName = type
        // .getAnnotation(ConfigurationDifinition.class).name();
        // String relativePath = ClassUtils.packageToDir(type);
        // properties.setProperty(type.getName(), configName);
        // configMap.put(configName, properties);
        // logger.debug("create file {} for {}", file.getAbsolutePath(),
        // type.getName());
        // }
        // try {
        // properties.store(
        // new FileWriterWithEncoding(file, Charset.UTF_8), "");
        // } catch (IOException e) {
        // // FIXME 需要更精确的异常描述
        // logger.error(e.getMessage());
        // throw new ConfigurationException(e);
        // }
        // }
    }

    /**
     * Gets the config file.
     *
     * @param config the config
     * @return the config file
     */
    public File getConfigFile(String config) {
        return configFileMap.get(config);
    }

    private void createFile(File file, String configName) {
        try {
            org.apache.commons.io.FileUtils.write(file, "", StandardCharsets.UTF_8);
        } catch (IOException e) {
            // FIXME 需要更精确的异常描述
            logger.error(e.getMessage());
            ConfigurationException.throwConfigNotInit(configName, "*");
        }
    }

    /**
     * get metadataReaders.
     *
     * @return metadataReaders
     */
    public Set<MetadataReader> getMetadataReaders() {
        return metadataReaders;
    }

    /**
     * get filePolicy.
     *
     * @return filePolicy
     */
    public FilePolicy getFilePolicy() {
        return filePolicy;
    }

    /**
     * set filePolicy.
     *
     * @param filePolicy filePolicy
     */
    public void setFilePolicy(FilePolicy filePolicy) {
        this.filePolicy = filePolicy;
    }

    /**
     * get dirPolicy.
     *
     * @return dirPolicy
     */
    public DirPolicy getDirPolicy() {
        return dirPolicy;
    }

    /**
     * set dirPolicy.
     *
     * @param dirPolicy dirPolicy
     */
    public void setDirPolicy(DirPolicy dirPolicy) {
        this.dirPolicy = dirPolicy;
    }

    /**
     * Sets the config.
     *
     * @param configName the config name
     * @param configs    the configs
     * @return the properties file configurator
     */
    public PropertiesFileConfigurator setConfig(String configName, Config... configs) {
        Properties properties = loadConfig(configName);
        for (Config config : configs) {
            properties.setProperty(config.getName(), config.getValue(), config.getDescp());
            //            if (Lang.isNotEmpty(config.getDescp())) {
            //                properties.setProperty(getDescpKey(config.getName()), config.getDescp());
            //            }
            try {
                properties.store(new FileOutputStream(configFileMap.get(configName)));
                propertiesMap.put(configName, properties);
            } catch (IOException e) {
                throw new ConfigurationException(String.format("为%s的%s设置值%s时发生错误%s", configName, config.getName(),
                        config.getValue(), e.getMessage()));
            }
        }
        return this;
    }

    /**
     * Gets the config.
     *
     * @param configName the config name
     * @param name       the name
     * @return the config
     */
    public Config getConfig(String configName, String name) {
        Properties properties = propertiesMap.get(configName);
        Property p = properties.getPropertyPart(name);
        Config config = new Config();
        config.setName(name);
        if (p != null) {
            config.setValue(p.getValue());
            //        config.setDescp(properties.getProperty(getDescpKey(name)));
            config.setDescp(p.getComment());
        }
        return config;
    }

    /**
     * Gets the config difinition.
     *
     * @param configName the config name
     * @return the config difinition
     */
    public Config getConfigDifinition(String configName) {
        Config config = new Config();
        Properties properties = propertiesMap.get(configName);
        config.setConfigName(configName);
        for (Property p : properties.getPropertyParts()) {
            if (p.getKey().contains(Chars.DOT)) {
                config.setConfigDescp(p.getComment());
                break;
            }
        }
        return config;
    }

    private Properties loadConfig(String configName) {
        File file = configFileMap.get(configName);
        if (file == null) {
            // TODO 这里后续要加入更精确的异常
            throw new ConfigurationException(String.format("%s的配置文件未找到", configName));
        }
        try {
            Properties properties = new PropertiesImpl();
            properties.load(new FileInputStream(file));
            return properties;
        } catch (IOException e) {
            throw new ConfigurationException(String.format("加载%s的配置文件%s报错", configName, file.getPath()));
        }
    }

    /**
     * Gets the config names.
     *
     * @return the config names
     */
    public Set<String> getConfigNames() {
        return configFileMap.keySet();
    }

    /**
     * Gets the configs.
     *
     * @param config the config
     * @return the configs
     */
    public List<Config> getConfigs(String config) {
        List<Config> configs = new ArrayList<>();
        Properties properties = propertiesMap.get(config);
        String descp = null;
        for (Property p : properties.getPropertyParts()) {
            if (p.getKey().contains(Chars.DOT)) {
                descp = p.getComment();
                continue;
            }
            Config c = new Config();
            c.setConfigName(config);
            c.setConfigDescp(descp);
            c.setName(p.getKey());
            c.setValue(p.getValue());
            c.setDescp(p.getComment());
            configs.add(c);
        }
        return configs;
    }

}
