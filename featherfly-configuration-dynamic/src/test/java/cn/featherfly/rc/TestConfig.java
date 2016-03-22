
package cn.featherfly.rc;

import cn.featherfly.rc.annotation.ConfigurationDifinition;

/**
 * <p>
 * Test
 * 类的说明放这里
 * </p>
 * 
 * @author 钟冀
 */
@ConfigurationDifinition(name = "test", descp = "描述")
public interface TestConfig {
    
    public Integer getAge();
    
    public TestConfig setAge(Integer age);
    
    public String getName();
    
    public TestConfig setName(String name);
}

