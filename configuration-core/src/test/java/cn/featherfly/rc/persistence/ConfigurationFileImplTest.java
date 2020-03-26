
package cn.featherfly.rc.persistence;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.featherfly.common.io.ClassPathScanningProvider;
import cn.featherfly.common.io.FileUtils;
import cn.featherfly.common.lang.LangUtils;
import cn.featherfly.rc.repository.ConfigurationFileRepository;
import cn.featherfly.rc.repository.PropertiesFileConfigurator;

/**
 * <p>
 * ConfigurationFileImplTest
 * </p>
 *
 * @author zhongj
 */
public class ConfigurationFileImplTest {

    private ConfigurationFileRepository persistence;

    private String projectName = "featherfly-configuration-persistence-test";

    @BeforeClass
    public void before() {
        // 删除配置目录
        LangUtils.ifExists(new File(
                org.apache.commons.io.FileUtils.getUserDirectoryPath() + "/.featherfly_configuration/" + projectName),
                f -> FileUtils.deleteDir(f), f -> FileUtils.makeDirectory(f));

        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider()
                .findMetadata(new String[] { "cn.featherfly" });
        PropertiesFileConfigurator configurator = new PropertiesFileConfigurator(projectName, metadataReaders);
        persistence = new ConfigurationFileRepository(configurator);

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

        Integer sex = 1;
        persistence.set("test", "sex", sex);

        assertEquals(persistence.get("test", "sex", Integer.class), sex);

    }

}
