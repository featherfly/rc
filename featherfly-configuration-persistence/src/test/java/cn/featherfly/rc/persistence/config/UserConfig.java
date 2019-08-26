
package cn.featherfly.rc.persistence.config;

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

    Integer getAge();

    UserConfig setAge(Integer age);
}
