
package cn.featherfly.rc.persistence.config1;

import cn.featherfly.rc.annotation.Configurations;

/**
 * <p>
 * UserConfig
 * </p>
 *
 * @author zhongj
 */
@Configurations(name = "UserConfig2", descp = "user config")
public interface UserConfig2 {

    String getName();

    UserConfig2 setName(String name);

    default Integer getAge() {
        return 18;
    }

    UserConfig2 setAge(Integer age);

}
