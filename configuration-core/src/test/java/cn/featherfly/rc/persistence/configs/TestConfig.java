
package cn.featherfly.rc.persistence.configs;

import cn.featherfly.rc.annotation.Configuration;
import cn.featherfly.rc.annotation.Configurations;

/**
 * <p>
 * Test 类的说明放这里
 * </p>
 *
 * @author 钟冀
 */
@Configurations(name = "test", descp = "描述")
public interface TestConfig {

    @Configuration(descp = "性别")
    default Integer sex() {
        return 18;
    }

    Integer getAge();

    TestConfig setAge(Integer age);

    String getName();

    TestConfig setName(String name);

}

class T implements TestConfig {

    @Override
    public Integer getAge() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestConfig setAge(Integer age) {
        // YUFEI_TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        // YUFEI_TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestConfig setName(String name) {
        // YUFEI_TODO Auto-generated method stub
        return null;
    }

}