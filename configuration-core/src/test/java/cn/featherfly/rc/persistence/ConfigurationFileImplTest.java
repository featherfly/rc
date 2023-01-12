
package cn.featherfly.rc.persistence;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.featherfly.common.io.ClassPathScanningProvider;
import cn.featherfly.common.io.FileUtils;
import cn.featherfly.common.lang.Lang;
import cn.featherfly.rc.Configuration;
import cn.featherfly.rc.ConfigurationValue;
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

    private ConfigurationFileRepository repository;

    private String projectName = "featherfly-configuration-persistence-test";

    @BeforeClass
    public void before() {
        // 删除配置目录
        Lang.ifExists(new File(
                org.apache.commons.io.FileUtils.getUserDirectoryPath() + "/.featherfly_configuration/" + projectName),
                f -> FileUtils.deleteDir(f), f -> FileUtils.makeDirectory(f));

        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider()
                .findMetadata(new String[] { "cn.featherfly" });
        PropertiesFileConfigurator configurator = new PropertiesFileConfigurator(projectName, metadataReaders);
        repository = new ConfigurationFileRepository(configurator);

    }

    @Test
    public void test() {
        String name = "yufei";

        System.out.println("userconfig name " + repository.get("UserConfig", "name", String.class));

        repository.set("UserConfig", "name", name);

        assertEquals(repository.get("UserConfig", "name", String.class), name);

        name = "admin";
        repository.set("RoleConfig", "name", name);
        assertEquals(repository.get("RoleConfig", "name", String.class), name);

        Integer sex = 1;
        repository.set("test", "sex", sex);

        assertEquals(repository.get("test", "sex", Integer.class), sex);

    }

    @Test
    public void test2() {
        Configuration c = repository.getConfiguration("test");
        assertEquals(c.getName(), "test");
        assertNull(c.getDescp());
        //        System.out.println(c);
        List<ConfigurationValue<?>> configs = repository.getConfigurations("test");
        for (ConfigurationValue<?> v : configs) {
            if (v.getName().equals("name")) {
                assertEquals(v.getValue(), "yufei");
                assertEquals(v.getDescp(), "姓名");
            }
            if (v.getName().equals("age")) {
                assertEquals(v.getValue(), "18");
                assertEquals(v.getDescp(), "年龄");
            }
            if (v.getName().equals("sex")) {
                assertEquals(v.getValue(), "1");
                assertNull(v.getDescp());
            }
        }
        //        System.out.println(repository.getConfigurations("test"));
    }

    @Test
    public void testAll() {
        Collection<Configuration> cs = repository.getConfigurations();
        for (Configuration configuration : cs) {
            System.out.println(configuration.getName());
            List<ConfigurationValue<?>> configs = repository.getConfigurations(configuration.getName());
            for (ConfigurationValue<?> v : configs) {
                System.out.println("    " + v.getName() + " " + v.getValue() + " " + v.getDescp());
                if (v.getName().equals("name")) {
                    assertEquals(v.getValue(), "yufei");
                    assertEquals(v.getDescp(), "姓名");
                }
                if (v.getName().equals("age")) {
                    assertEquals(v.getValue(), "18");
                    assertEquals(v.getDescp(), "年龄");
                }
                if (v.getName().equals("sex")) {
                    assertEquals(v.getValue(), "1");
                    assertNull(v.getDescp());
                }
            }
        }
    }

}
