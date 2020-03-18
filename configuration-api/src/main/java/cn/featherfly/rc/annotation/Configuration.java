
package cn.featherfly.rc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 配置项的注解.
 * </p>
 *
 * @author 钟冀
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Configuration {
    /**
     * <p>
     * 该配置项的名称.
     * </p>
     *
     * @return 名称
     */
    String name() default "";

    /**
     * <p>
     * 该配置项的描述.
     * </p>
     *
     * @return 描述
     */
    String descp() default "";
}
