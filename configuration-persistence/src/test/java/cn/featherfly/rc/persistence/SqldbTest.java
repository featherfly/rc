
package cn.featherfly.rc.persistence;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.DelegatingSmartContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.featherfly.common.db.SqlExecutor;
import cn.featherfly.common.lang.ClassLoaderUtils;
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
    private DataSource dataSource;

    @BeforeClass
    public void init() throws IOException {
        SqlExecutor sqlExecutor = new SqlExecutor(dataSource);
        sqlExecutor.execute(new File(ClassLoaderUtils.getResource("test.mysql.sql.txt", this.getClass()).getFile()));
    }

    @Test
    public void test() {
        userConfig.setAge(28);
        userConfig.setName("yufei");
        System.out.println(userConfig.getName());
        System.out.println(userConfig.getAge());
    }

    @Test
    public void test2() {
        System.out.println(userConfig.getName());
        System.out.println(userConfig.getAge());
        assertTrue(userConfig.getAge() == 18);
        assertNull(userConfig.getName());

    }
}
