
package cn.featherfly.rc.config;

import cn.featherfly.common.lang.SystemPropertyUtils;
import cn.featherfly.rc.annotation.Configuration;
import cn.featherfly.rc.annotation.ConfigurationDifinition;

/**
 * <p>
 * UserConfig
 * </p>
 *
 * @author zhongj
 */
@ConfigurationDifinition(name = "UserConfig", descp = "user config")
public interface UserConfig {

    String getName();

    UserConfig setName(String name);

    @Configuration
    default int age() {
        return 18;
    }
    //    Integer getAge();
    //
    //    UserConfig setAge(Integer age);
}
