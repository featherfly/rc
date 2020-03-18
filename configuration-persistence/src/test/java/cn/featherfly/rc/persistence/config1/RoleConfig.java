
package cn.featherfly.rc.persistence.config1;

import cn.featherfly.rc.annotation.ConfigurationDifinition;

/**
 * <p>
 * UserConfig
 * </p>
 * 
 * @author zhongj
 */
@ConfigurationDifinition(name = "RoleConfig", descp = "role config")
public interface RoleConfig {

    String getName();

    RoleConfig setName(String name);
}
