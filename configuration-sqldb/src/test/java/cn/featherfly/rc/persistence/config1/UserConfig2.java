
package cn.featherfly.rc.persistence.config1;

import cn.featherfly.rc.annotation.ConfigurationDifinition;

/**
 * <p>
 * UserConfig
 * </p>
 *
 * @author zhongj
 */
@ConfigurationDifinition(name = "UserConfig2", descp = "user config")
public interface UserConfig2 {

    String getName();

    UserConfig2 setName(String name);

    default Integer getAge() {
        return 18;
    }

    UserConfig2 setAge(Integer age);

}
