
package cn.featherfly.rc;

import static org.testng.Assert.assertEquals;

import javax.annotation.Resource;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

import cn.featherfly.common.lang.ClassLoaderUtils;

/**
 * <p>
 * Test
 * </p>
 * 
 * @author zhongj
 */
@ContextConfiguration(locations = { "classpath:app2.xml" })
public class Test extends AbstractTestNGSpringContextTests {
    @Resource
    private TestConfig testConfig;

    @BeforeClass
    public void before() {
        DOMConfigurator.configure(ClassLoaderUtils.getResource("log4j.xml",
                AbstractTestNGSpringContextTests.class));
    }

    @org.testng.annotations.Test
    public void test() {
        Integer age = 18;
        String name = "yi";
        testConfig.setAge(age);
        testConfig.setName(name);

        System.out.println(testConfig.getName());
        System.out.println(testConfig.getAge());

        assertEquals(testConfig.getAge(), age);
        assertEquals(testConfig.getName(), name);
    }
}
