
package cn.featherfly.rc.persistence;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.DelegatingSmartContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.featherfly.common.db.SqlExecutor;
import cn.featherfly.common.lang.ClassLoaderUtils;
import cn.featherfly.rc.Configuration;
import cn.featherfly.rc.ConfigurationRepository;
import cn.featherfly.rc.ConfigurationValue;
import cn.featherfly.rc.persistence.config1.UserConfig2;

/**
 * <p>
 * AppRun
 * </p>
 *
 * @author 钟冀
 */
@ContextConfiguration(loader = DelegatingSmartContextLoader.class, classes = SqldbConfig.class)
public class SqldbTest extends AbstractTestNGSpringContextTests {

    @Resource
    private UserConfig2 userConfig;

    @Resource
    private ConfigurationRepository repository;

    @Resource
    private DataSource dataSource;

    final int newAge = 18;
    final String newName = "yufei_update";

    @BeforeClass
    public void init() throws IOException {
        SqlExecutor sqlExecutor = new SqlExecutor(dataSource);
        sqlExecutor.execute(new File(ClassLoaderUtils.getResource("test.mysql.sql", this.getClass()).getFile()));
    }

    @Test
    public void test() {
        userConfig.setAge(newAge);
        userConfig.setName(newName);
        System.out.println(userConfig.getName());
        System.out.println(userConfig.getAge());

        assertTrue(userConfig.getAge() == newAge);
    }

    @Test
    public void test2() {
        System.out.println(userConfig.getName());
        System.out.println(userConfig.getAge());
        assertEquals(userConfig.getAge().intValue(), newAge);
        assertEquals(userConfig.getName(), newName);
    }

    @Test
    public void test3() {
        Configuration c = repository.getConfiguration("UserConfig2");
        assertEquals(c.getName(), "UserConfig2");
        assertNull(c.getDescp());
        List<ConfigurationValue<?>> configs = repository.getConfigurations("UserConfig2");
        for (ConfigurationValue<?> v : configs) {
            if (v.getName().equals("name")) {
                assertEquals(v.getValue(), newName);
                assertEquals(v.getDescp(), "name");
            }
            if (v.getName().equals("age")) {
                assertEquals(v.getValue(), newAge + "");
                assertEquals(v.getDescp(), "age");
            }
        }
        //        System.out.println(repository.getConfigurations("UserConfig2"));
    }
}
