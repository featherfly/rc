
package cn.featherfly.rc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.SocketUtils;
import org.testng.annotations.Test;

import cn.featherfly.common.lang.ClassUtils;
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
        for (Method method : TestConfig.class.getMethods()) {
            System.out.println(method.isDefault());
            if (method.isDefault()) {
            }
        }
        
    }
}
