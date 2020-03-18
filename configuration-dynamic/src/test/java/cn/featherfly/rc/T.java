//
//package cn.featherfly.rc;
//
//import java.io.Serializable;
//
//import cn.featherfly.common.lang.ClassUtils;
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;
//import javassist.NotFoundException;
//
///**
// * <p>
// * T
// * 类的说明放这里
// * </p>
// *
// * @author zhongj
// */
//public class T {
//
//    private String name;
//
//    public T() {
//
//    }
//
//    private Object get(String name, Class type) {
//        return new String[] {"a", "b"};
//    }
//
//    public String[] getStr() {
//        return (java.lang.String[]) get(name, java.lang.String[].class);
//    }
//
//    public static void main(String[] args) throws NotFoundException {
//        System.out.println(ClassUtils.isParent(Serializable.class, String[].class));
//        System.out.println(ClassUtils.isParent(Serializable.class, Long[].class));
//        System.out.println(ClassUtils.isParent(Serializable.class, Object.class));
//        System.out.println(ClassUtils.isParent(Object.class, String[].class));
//        System.out.println(ClassUtils.isParent(Object.class, Long[].class));
//
//        ClassPool pool = ClassPool.getDefault();
//        CtClass ctc = pool.getCtClass(T.class.getName());
//        CtMethod ctMethod = ctc.getDeclaredMethod("getStr");
//        System.out.println(ctMethod.getGenericSignature());
//        System.out.println(ctMethod.getSignature());
//
//        ctc = pool.getCtClass(ConfigurationValuePersistence.class.getName());
//
//        CtMethod[] ms = ctc.getDeclaredMethods();
//        for (CtMethod m : ms) {
//            System.out.println(m.getName());
//            for (CtClass p : m.getParameterTypes()) {
//                System.out.println(p.getName());
//            }
//        }
//    }
//
//    /**
//     * 返回name
//     * @return name
//     */
//    public String getName() {
//        return name;
//    }
//
//    /**
//     * 设置name
//     * @param name name
//     */
//    public void setName(String name) {
//        this.name = name;
//    }
//}
