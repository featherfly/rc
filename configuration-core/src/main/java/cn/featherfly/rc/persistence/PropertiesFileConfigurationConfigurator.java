package cn.featherfly.rc.persistence;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.classreading.MetadataReader;

import cn.featherfly.common.lang.AssertIllegalArgument;
import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.LangUtils;
import cn.featherfly.rc.ConfigurationException;
import cn.featherfly.rc.annotation.ConfigurationDifinition;

/**
 * <p>
 * PropertiesFileConfigurationConfigurator
 * </p>
 *
 * @author 钟冀
 */
public class PropertiesFileConfigurationConfigurator {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // private static final String CONFIG_FILE_NAME =
    // "featherfly_configuration.properties";

    private Map<String, File> configFileMap = new HashMap<>();
    private Map<String, Properties> propertiesMap = new HashMap<>();

    public enum FilePolicy {
        EACH_FILE_FOR_DEFINE,
        EACH_FILE_FOR_DEFINE_IN_PACKAGE
        //        , ALL_IN_ONE_FILE
    }

    public enum DirPolicy {
        USER_DIR
    }

    private static final String DIR_NAME = ".featherfly_configuration";

    private FilePolicy filePolicy = FilePolicy.EACH_FILE_FOR_DEFINE;

    private DirPolicy dirPolicy = DirPolicy.USER_DIR;

    private Set<MetadataReader> metadataReaders = new HashSet<>();

    private File storeDir;

    private String projectName;

    /**
     * @param projectName     projectName
     * @param metadataReaders metadataReaders
     */
    public PropertiesFileConfigurationConfigurator(String projectName, Set<MetadataReader> metadataReaders) {
        this(projectName, FilePolicy.EACH_FILE_FOR_DEFINE, DirPolicy.USER_DIR, metadataReaders);
    }

    /**
     * @param projectName     projectName
     * @param filePolicy      filePolicy
     * @param dirPolicy       dirPolicy
     * @param metadataReaders metadataReaders
     */
    public PropertiesFileConfigurationConfigurator(String projectName, FilePolicy filePolicy, DirPolicy dirPolicy,
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
                if (metadataReader.getAnnotationMetadata().hasAnnotation(ConfigurationDifinition.class.getName())) {
                    Class<?> type = ClassUtils.forName(metadataReader.getClassMetadata().getClassName());
                    String configName = type.getAnnotation(ConfigurationDifinition.class).name();
                    String descp = type.getAnnotation(ConfigurationDifinition.class).descp();
                    File file;
                    if (filePolicy == FilePolicy.EACH_FILE_FOR_DEFINE_IN_PACKAGE) {
                        file = new File(storeDir.getAbsoluteFile() + "/" + ClassUtils.packageToDir(type) + "/"
                                + type.getSimpleName() + ".properties");
                    } else {
                        file = new File(storeDir.getAbsoluteFile() + "/" + configName + ".properties");
                    }
                    Properties properties = new Properties();
                    if (!file.exists()) {
                        createFile(file, configName);
                        configFileMap.put(configName, file);
                        try {
                            properties.setProperty(type.getName(), configName);
                            if (LangUtils.isNotEmpty(descp)) {
                                properties.setProperty(getConfigNameDescpKey(configName), descp);
                            }
                            properties.store(new FileWriterWithEncoding(file, StandardCharsets.UTF_8), configName);
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

    public File getConfigFile(String config) {
        return configFileMap.get(config);
    }

    private String getConfigNameDescpKey(String configName) {
        return "ConfigName." + getDescpKey(configName);
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
     * get metadataReaders
     *
     * @return metadataReaders
     */
    public Set<MetadataReader> getMetadataReaders() {
        return metadataReaders;
    }

    /**
     * get filePolicy
     *
     * @return filePolicy
     */
    public FilePolicy getFilePolicy() {
        return filePolicy;
    }

    /**
     * set filePolicy
     *
     * @param filePolicy filePolicy
     */
    public void setFilePolicy(FilePolicy filePolicy) {
        this.filePolicy = filePolicy;
    }

    /**
     * get dirPolicy
     *
     * @return dirPolicy
     */
    public DirPolicy getDirPolicy() {
        return dirPolicy;
    }

    /**
     * set dirPolicy
     *
     * @param dirPolicy dirPolicy
     */
    public void setDirPolicy(DirPolicy dirPolicy) {
        this.dirPolicy = dirPolicy;
    }

    public PropertiesFileConfigurationConfigurator setConfig(String configName, Config... configs) {
        Properties properties = loadConfig(configName);
        for (Config config : configs) {
            properties.setProperty(config.getName(), config.getValue());
            if (LangUtils.isNotEmpty(config.getDescp())) {
                properties.setProperty(getDescpKey(config.getName()), config.getDescp());
            }
            try {
                properties.store(new FileWriterWithEncoding(configFileMap.get(configName), StandardCharsets.UTF_8),
                        configName);
                propertiesMap.put(configName, properties);
            } catch (IOException e) {
                throw new ConfigurationException(String.format("为%s的%s设置值%s时发生错误%s", configName, config.getName(),
                        config.getValue(), e.getMessage()));
            }
        }
        return this;
    }

    public Config getConfig(String configName, String name) {
        Properties properties = propertiesMap.get(configName);
        Config config = new Config();
        config.setName(name);
        config.setValue(properties.getProperty(name));
        config.setDescp(properties.getProperty(getDescpKey(name)));
        return config;
    }

    public Config getConfigDifinition(String configName) {
        Config config = new Config();
        Properties properties = propertiesMap.get(configName);
        config.setConfigName(configName);
        config.setConfigDescp(properties.getProperty(getConfigNameDescpKey(configName)));
        return config;
    }

    private Properties loadConfig(String configName) {
        File file = configFileMap.get(configName);
        if (file == null) {
            // TODO 这里后续要加入更精确的异常
            throw new ConfigurationException(String.format("%s的配置文件未找到", configName));
        }
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            return properties;
        } catch (IOException e) {
            throw new ConfigurationException(String.format("加载%s的配置文件%s报错", configName, file.getPath()));
        }
    }

    public Set<String> getConfigNames() {
        return configFileMap.keySet();
    }

    private String getDescpKey(String name) {
        return name + ".descp";
    }

}
