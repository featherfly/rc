
package cn.featherfly.rc.javassist;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.featherfly.common.lang.AssertIllegalArgument;
import cn.featherfly.common.lang.ClassLoaderUtils;
import cn.featherfly.common.lang.ClassUtils;
import cn.featherfly.common.lang.CollectionUtils;
import cn.featherfly.common.lang.matcher.MethodNameRegexMatcher;
import cn.featherfly.rc.ConfigurationException;
import cn.featherfly.rc.ConfigurationRepository;
import cn.featherfly.rc.annotation.Configurations;
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
 * DynamicConfigFacotry.
 *
 * @author zhongj
 */
public class DynamicConfigurationFacotry {

    private ClassLoader classLoader;

    private Set<Class<?>> types = new HashSet<>();

    private static final DynamicConfigurationFacotry INSTANCE = new DynamicConfigurationFacotry();

    private Map<Class<?>, Object> typeInstances = new HashMap<>();

    /**
     * Instantiates a new dynamic configuration facotry.
     */
    public DynamicConfigurationFacotry() {
        super();
    }

    /**
     * <p>
     * return default DynamicConfigurationFacotry instance
     * </p>
     * .
     *
     * @return DynamicConfigurationFacotry
     */
    public static DynamicConfigurationFacotry getInstance() {
        return INSTANCE;
    }

    /**
     * always return a new instance.
     *
     * @param <E>        the element type
     * @param type       configuration interface type
     * @param repository ConfigurationRepository
     * @return new instance
     */
    @SuppressWarnings("unchecked")
    public <E> E newInstance(Class<E> type, ConfigurationRepository repository) {
        try {
            AssertIllegalArgument.isNotNull(type, "type");
            AssertIllegalArgument.isNotNull(repository, "repository");
            Configurations cd = type.getAnnotation(Configurations.class);
            if (cd == null) {
                throw new ConfigurationException(String.format("there is no annotation[%s] in type[%s]",
                        Configurations.class.getName(), type.getName()));
            }
            String configName = cd.name();
            return (E) ClassUtils.forName(create(type)).getConstructor(String.class, ConfigurationRepository.class)
                    .newInstance(configName, repository);
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    /**
     * return a singleton instance, every type only new one instance.
     *
     * @param <E>        the element type
     * @param type       configuration interface type
     * @param repository ConfigurationRepository
     * @return new instance
     */
    @SuppressWarnings("unchecked")
    public <E> E instance(Class<E> type, ConfigurationRepository repository) {
        E e = null;
        e = (E) typeInstances.get(type);
        if (e == null) {
            e = newInstance(type, repository);
            typeInstances.put(type, e);
        }
        return e;
    }

    /**
     * create configuration interface implemented class.
     *
     * @param type configuration interface class
     * @return implemented class name
     * @throws NotFoundException      the not found exception
     * @throws CannotCompileException the cannot compile exception
     */
    public String create(Class<?> type) throws NotFoundException, CannotCompileException {
        return create(type, this.getClass().getClassLoader());
    }

    /**
     * create configuration interface implemented class.
     *
     * @param type        configuration interface class
     * @param classLoader the class loader
     * @return implemented class name
     * @throws NotFoundException      the not found exception
     * @throws CannotCompileException the cannot compile exception
     */
    public String create(Class<?> type, ClassLoader classLoader) throws NotFoundException, CannotCompileException {
        String dynamicClassName = type.getPackage().getName() + "._" + type.getSimpleName() + "DynamicImpl";
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        if (this.classLoader == null) {
            // 第一次加载
            this.classLoader = classLoader;
        }
        if (this.classLoader != classLoader) {
            // 表示使用的classLoader没了，使用新的classLoader重新加载，一般出现在热部署时，如springboot-dev-tool的RestartClassLoader
            clear();
            this.classLoader = classLoader;
        }
        if (!types.contains(type)) {
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(this.getClass()));
            CtClass dynamicImplClass = pool.makeClass(dynamicClassName);
            dynamicImplClass.setInterfaces(new CtClass[] { pool.getCtClass(type.getName()) });
            // name
            CtField nameField = new CtField(pool.getCtClass(String.class.getName()), "name", dynamicImplClass);
            nameField.setModifiers(Modifier.PRIVATE);
            dynamicImplClass.addField(nameField);
            // ConfigurationRepository
            CtField configurationRepositoryField = new CtField(pool.getCtClass(ConfigurationRepository.class.getName()),
                    "configurationRepository", dynamicImplClass);
            configurationRepositoryField.setModifiers(Modifier.PRIVATE);
            dynamicImplClass.addField(configurationRepositoryField);
            CtConstructor constraConstructor = new CtConstructor(new CtClass[] {
                    pool.getCtClass(String.class.getName()), pool.getCtClass(ConfigurationRepository.class.getName()) },
                    dynamicImplClass);
            constraConstructor.setModifiers(Modifier.PUBLIC);
            constraConstructor.setBody("{this.name=$1;this.configurationRepository=$2;}");
            dynamicImplClass.addConstructor(constraConstructor);

            Collection<Method> getMethods = ClassUtils.findMethods(type, new MethodNameRegexMatcher("get.+"));
            Collection<Method> setMethods = ClassUtils.findMethods(type, new MethodNameRegexMatcher("set.+"));
            for (Method getMethod : getMethods) {
                CtMethod ctMethod = new CtMethod(pool.getCtClass(getMethod.getReturnType().getTypeName()),
                        getMethod.getName(), new CtClass[] {}, dynamicImplClass);
                ctMethod.setBody(String.format("{return (%2$s) configurationRepository.get(name, \"%s\", %2$s.class);}",
                        ClassUtils.getPropertyName(getMethod), getMethod.getReturnType().getTypeName()));
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
                ctMethod.setBody(String.format("{configurationRepository.set(name, \"%s\", $1);return this;}",
                        ClassUtils.getPropertyName(setMethod)));
                ctMethod.setModifiers(Modifier.PUBLIC);
                dynamicImplClass.addMethod(ctMethod);
            }

            try {
                byte[] code = dynamicImplClass.toBytecode();
                Class<?> newType = ClassLoaderUtils.defineClass(classLoader, dynamicClassName, code,
                        type.getProtectionDomain());
                if (newType == null) {
                    pool.toClass(dynamicImplClass, type, classLoader,
                            dynamicImplClass.getClass().getProtectionDomain());
                }
            } catch (java.io.IOException e) {
                throw new CannotCompileException(e);
            }
            dynamicImplClass.detach();
            types.add(type);
        }
        return dynamicClassName;
    }

    private void clear() {
        types.clear();
        typeInstances.clear();
    }
}
