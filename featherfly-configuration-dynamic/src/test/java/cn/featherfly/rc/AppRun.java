
package cn.featherfly.rc;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import cn.featherfly.rc.javassist.DynamicConfigurationFacotry;
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

    public static void main(String[] args) throws Exception {
        //        for (Method method : TestConfig.class.getMethods()) {
        //            System.out.println(method.isDefault());
        //            if (method.isDefault()) {
        //            }
        //        }

        DynamicConfigurationFacotry.getInstance().create(WechatConfiguration.class);

    }
}
