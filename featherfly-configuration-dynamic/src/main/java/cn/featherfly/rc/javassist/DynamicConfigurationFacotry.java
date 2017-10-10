
package cn.featherfly.rc.javassist;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.CollectionUtils;
import cn.featherfly.common.lang.matcher.MethodNameRegexMatcher;
import cn.featherfly.rc.ConfigurationValuePersistence;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 * <p>
 * DynamicConfigFacotry
 * </p>
 *
 * @author 钟冀
 */
public class DynamicConfigurationFacotry {

    private Set<Class<?>> types = new HashSet<>();

    private static final DynamicConfigurationFacotry INSTANCE = new DynamicConfigurationFacotry();

    /**
     *
     */
    public DynamicConfigurationFacotry() {
        super();
    }

    /**
     * <p>
     * 方法的说明
     * </p>
     *
     * @return
     */
    public static DynamicConfigurationFacotry getInstance() {
        return INSTANCE;
    }

    public String create(Class<?> type) throws NotFoundException, CannotCompileException {
        String dynamicClassName = type.getPackage().getName() + "._" + type.getSimpleName() + "DynamicImpl";
        if (!types.contains(type)) {
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(this.getClass()));
            CtClass dynamicImplClass = pool.makeClass(dynamicClassName);
            dynamicImplClass.setInterfaces(new CtClass[] {pool.getCtClass(type.getName())});
            CtField nameField = new CtField(pool.getCtClass(String.class.getName()), "name", dynamicImplClass);
            nameField.setModifiers(Modifier.PRIVATE);
            dynamicImplClass.addField(nameField);
            CtField configurationValuePersistenceField = new CtField(
                    pool.getCtClass(ConfigurationValuePersistence.class.getName()), "configurationValuePersistence",
                    dynamicImplClass);
            configurationValuePersistenceField.setModifiers(Modifier.PRIVATE);
            dynamicImplClass.addField(configurationValuePersistenceField);
            CtConstructor constraConstructor = new CtConstructor(new CtClass[] {pool.getCtClass(String.class.getName()),
                    pool.getCtClass(ConfigurationValuePersistence.class.getName())}, dynamicImplClass);
            constraConstructor.setModifiers(Modifier.PUBLIC);
            constraConstructor.setBody("{this.name=$1;this.configurationValuePersistence=$2;}");
            dynamicImplClass.addConstructor(constraConstructor);

            Collection<Method> getMethods = ClassUtils.findMethods(type, new MethodNameRegexMatcher("get.+"));
            Collection<Method> setMethods = ClassUtils.findMethods(type, new MethodNameRegexMatcher("set.+"));
            for (Method getMethod : getMethods) {
                CtMethod ctMethod = new CtMethod(pool.getCtClass(getMethod.getReturnType().getTypeName()),
                        getMethod.getName(), new CtClass[] {}, dynamicImplClass);
                ctMethod.setBody(String.format("{return (%2$s) configurationValuePersistence.get(name, \"%s\", %2$s.class);}"
                        , ClassUtils.getPropertyName(getMethod), getMethod.getReturnType().getTypeName()));
                ctMethod.setModifiers(Modifier.PUBLIC);
                dynamicImplClass.addMethod(ctMethod);
            }
            for (Method setMethod : setMethods) {
                Collection<CtClass> params = new ArrayList<>();
                for (Class<?> paramType : setMethod.getParameterTypes()) {
                    params.add(pool.getCtClass(paramType.getName()));
                }
                CtMethod ctMethod = new CtMethod(pool.getCtClass(setMethod.getReturnType().getTypeName()),
                        setMethod.getName(), CollectionUtils.toArray(params, CtClass.class), dynamicImplClass);
                ctMethod.setBody(String.format("{configurationValuePersistence.set(name, \"%s\", $1);return this;}",
                        ClassUtils.getPropertyName(setMethod)));
                ctMethod.setModifiers(Modifier.PUBLIC);
                dynamicImplClass.addMethod(ctMethod);
            }
            dynamicImplClass.toClass();
            dynamicImplClass.detach();
            types.add(type);
        }
        return dynamicClassName;
    }
}
