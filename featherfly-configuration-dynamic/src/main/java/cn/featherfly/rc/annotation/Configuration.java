
package cn.featherfly.rc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 配置信息的注解.
 * </p>
 * @author 钟冀
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Configuration {
	/**
	 * <p>
	 * 该配置的描述.
	 * </p>
	 * @return 描述
	 */
	String value();
}
