
package cn.featherfly.rc;

import java.lang.reflect.Method;

/**
 * <p>
 * T
 * </p>
 *
 * @author zhongj
 */
public class T {
    static UserConfig uc;

    public static void main(String[] args) {
        for (Method m : UserConfig.class.getMethods()) {
            System.out.println(m.getName());
        }
    }
}
