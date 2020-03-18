
package cn.featherfly.rc;

import cn.featherfly.rc.annotation.ConfigurationDifinition;

/**
 * <p>
 * Test 类的说明放这里
 * </p>
 *
 * @author 钟冀
 */
@ConfigurationDifinition(name = "test", descp = "描述")
public interface TestConfig {

    default Integer getAge() {
        return 18;
    }

    TestConfig setAge(Integer age);

    String getName();

    TestConfig setName(String name);
}
