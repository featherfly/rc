
package cn.featherfly.rc.persistence.config1;

import cn.featherfly.rc.annotation.Configurations;

/**
 * <p>
 * UserConfig
 * </p>
 * 
 * @author zhongj
 */
@Configurations(name = "RoleConfig", descp = "role config")
public interface RoleConfig {

    String getName();

    RoleConfig setName(String name);
}
