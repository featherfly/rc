
package cn.featherfly.rc.persistence;

import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.featherfly.common.io.ClassPathScanningProvider;
import cn.featherfly.common.io.FileUtils;

/**
 * <p>
 * ConfigurationFileImplTest
 * </p>
 *
 * @author zhongj
 */
public class ConfigurationFileImplTest {

    private ConfigurationPersistenceFileImpl persistence;

    private String projectName = "featherfly-configuration-persistence-test";

    @BeforeClass
    public void before() {
        // 删除配置目录
        FileUtils.deleteDir(
                org.apache.commons.io.FileUtils.getUserDirectoryPath() + "/.featherfly_configuration/" + projectName);

        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider()
                .findMetadata(new String[] { "cn.featherfly" });
        PropertiesFileConfigurationConfigurator configurator = new PropertiesFileConfigurationConfigurator(projectName,
                metadataReaders);
        persistence = new ConfigurationPersistenceFileImpl(configurator);

    }

    @Test
    public void test() {
        String name = "yufei";

        System.out.println("userconfig name " + persistence.get("UserConfig", "name", String.class));

        persistence.set("UserConfig", "name", name);

        assertEquals(persistence.get("UserConfig", "name", String.class), name);

        name = "admin";
        persistence.set("RoleConfig", "name", name);
        assertEquals(persistence.get("RoleConfig", "name", String.class), name);

    }

}
