
package cn.featherfly.rc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 配置组定义的注解.
 * </p>
 *
 * @author 钟冀
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Configurations {
    /**
     * <p>
     * 该配置组定义的名称.
     * </p>
     *
     * @return 名称
     */
    String name();

    /**
     * <p>
     * 该配置组定义的描述.
     * </p>
     *
     * @return 描述
     */
    String descp();
}
