
package cn.featherfly.rc;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
/**
 * <p>
 * AppRun
 * </p>
 * 
 * @author 钟冀
 */
//@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:app.xml"})
//@ActiveProfiles("dev")
//@TransactionConfiguration(defaultRollback = true)
public class AppRun extends AbstractTestNGSpringContextTests {
    
    @Resource
    private TestConfig testConfig;
    
    @Test
    public void test() {
        testConfig.setAge(18);
        testConfig.setName("yufei");
        System.out.println(testConfig.getName());
        System.out.println(testConfig.getAge());
    }
    @Test
    public void test2() {
        
    }
}
