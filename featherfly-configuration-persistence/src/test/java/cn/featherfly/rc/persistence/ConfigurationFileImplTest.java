
package cn.featherfly.rc.persistence;

import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.featherfly.common.io.ClassPathScanningProvider;

/**
 * <p>
 * ConfigurationFileImplTest
 * </p>
 * 
 * @author zhongj
 */
public class ConfigurationFileImplTest {

    private ConfigurationPersistenceFileImpl persistence;

    @BeforeClass
    public void before() {
        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider()
                .findMetadata(new String[] { "cn.featherfly" });
        PropertiesFileConfigurationConfigurator configurator = new PropertiesFileConfigurationConfigurator(
                "featherfly-configuration-persistence-test", metadataReaders);
        persistence = new ConfigurationPersistenceFileImpl(configurator);

    }

    @Test
    public void test() {
        String name = "yufei";
        persistence.set("UserConfig", "name", name);

        assertEquals(persistence.get("UserConfig", "name", String.class), name);

        name = "admin";
        persistence.set("RoleConfig", "name", name);
        assertEquals(persistence.get("RoleConfig", "name", String.class), name);

    }

}
