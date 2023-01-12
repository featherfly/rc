
/*
 * All rights Reserved, Designed By zhongj
 * @Title: ConfigurationUtils.java
 * @Package cn.featherfly.rc
 * @Description: ConfigurationUtils
 * @author: zhongj
 * @date: 2022-12-09 16:24:09
 * @Copyright: 2022 www.featherfly.cn Inc. All rights reserved.
 */
package cn.featherfly.rc;

import cn.featherfly.rc.annotation.Configurations;

/**
 * ConfigurationUtils.
 *
 * @author zhongj
 */
public class ConfigurationUtils {

    public static Configurations getConfigurations(Class<?> configType) {
        Configurations cd = configType.getAnnotation(Configurations.class);
        if (cd == null) {
            throw new ConfigurationException(String.format("there is no annotation[%s] in type[%s]",
                    Configurations.class.getName(), configType.getName()));
        }
        return cd;
    }
}
