//package cn.featherfly.rc.apt;
//
//import java.util.Set;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.Messager;
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedOptions;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//
//import com.sun.source.tree.Tree;
//import com.sun.tools.javac.api.JavacTrees;
//import com.sun.tools.javac.code.Flags;
//import com.sun.tools.javac.processing.JavacProcessingEnvironment;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
//import com.sun.tools.javac.tree.TreeMaker;
//import com.sun.tools.javac.tree.TreeTranslator;
//import com.sun.tools.javac.util.Context;
//import com.sun.tools.javac.util.List;
//import com.sun.tools.javac.util.Name;
//import com.sun.tools.javac.util.Names;
//
//import cn.featherfly.rc.annotation.ConfigurationDifinition;
// TODO 动态编译实现了,但是IDE没有代码提示...估计要写ide插件，暂时搁置吧
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes({ "cn.featherfly.rc.annotation.ConfigurationDifinition" })
//@SupportedOptions("debug")
//public class ConfigurationProcessor extends AbstractProcessor {
//
//    private Messager messager;
//    private JavacTrees trees;
//    private TreeMaker treeMaker;
//    private Names names;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        messager = processingEnv.getMessager();
//        trees = JavacTrees.instance(processingEnv);
//        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
//        treeMaker = TreeMaker.instance(context);
//        names = Names.instance(context);
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
//        Messager messager = processingEnv.getMessager();
//        messager.printMessage(Diagnostic.Kind.NOTE, "start generate get and set method");
//        for (Element element : roundEnvironment.getElementsAnnotatedWith(ConfigurationDifinition.class)) {
//            //            TypeElement typeElem = (TypeElement) element;
//            String name = element.getSimpleName().toString();
//            messager.printMessage(Diagnostic.Kind.NOTE, "-" + name);
//            JCTree jcTree = trees.getTree(element);
//            System.out.println("jcTree.getClass().getName(): " + jcTree.getClass().getName());
//
//            jcTree.accept(new TreeTranslator() {
//                @Override
//                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
//                    List<JCTree.JCMethodDecl> jcMethodDeclList = List.nil();
//                    for (JCTree tree : jcClassDecl.defs) {
//                        if (tree.getKind().equals(Tree.Kind.METHOD)) {
//                            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) tree;
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE, tree.toString());
//                            messager.printMessage(Diagnostic.Kind.NOTE, "name: " + jcMethodDecl.name);
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE,
//                            //                                    "getModifiers: " + jcMethodDecl.getModifiers() + "");
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE, "restype: " + jcMethodDecl.restype + "");
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE,
//                            //                                    "returnType: " + jcMethodDecl.getReturnType() + "");
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE, "body: " + jcMethodDecl.body + "");
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE,
//                            //                                    "defaultValue: " + jcMethodDecl.defaultValue + "");
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE,
//                            //                                    "getReceiverParameter: " + jcMethodDecl.getReceiverParameter() + "");
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE,
//                            //                                    "getParameters: " + jcMethodDecl.getParameters() + "");
//                            //                            messager.printMessage(Diagnostic.Kind.NOTE,
//                            //                                    "getTypeParameters: " + jcMethodDecl.getTypeParameters().toString() + "");
//                            jcMethodDeclList = jcMethodDeclList.append(jcMethodDecl);
//                        }
//                    }
//                    messager.printMessage(Diagnostic.Kind.NOTE, "****************************");
//                    messager.printMessage(Diagnostic.Kind.NOTE, jcMethodDeclList.size() + "");
//                    jcMethodDeclList.forEach(jcMethodDecl -> {
//                        messager.printMessage(Diagnostic.Kind.NOTE, jcMethodDecl.name + " has been processed");
//                        JCMethodDecl setter = makeGetterMethodDecl(jcMethodDecl);
//                        messager.printMessage(Diagnostic.Kind.NOTE, " setter name: " + setter.name);
//                        jcClassDecl.defs = jcClassDecl.defs.prepend(setter);
//                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeSetterMethodDecl(jcMethodDecl));
//                    });
//                    super.visitClassDef(jcClassDecl);
//                }
//            });
//        }
//        messager.printMessage(Diagnostic.Kind.NOTE, "end generate get and set method ");
//        System.out.println("test end");
//        return true;
//    }
//
//    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCMethodDecl jcMethodDecl) {
//        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.InterfaceMethodFlags),
//                getGetterName(jcMethodDecl.getName()), jcMethodDecl.restype, List.nil(), List.nil(), List.nil(), null,
//                null);
//    }
//
//    /*
//    public JCMethodDecl MethodDef(JCModifiers mods,
//            Name name,
//            JCExpression restype,
//            List<JCTypeParameter> typarams,
//            List<JCVariableDecl> params,
//            List<JCExpression> thrown,
//            JCBlock body,
//            JCExpression defaultValue)
//    */
//    private JCTree.JCMethodDecl makeSetterMethodDecl(JCTree.JCMethodDecl jcMethodDecl) {
//        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.InterfaceMethodFlags),
//                getSetterName(jcMethodDecl.getName()), null, // return type
//                jcMethodDecl.typarams, jcMethodDecl.params, List.nil(), null, null);
//    }
//
//    private Name getGetterName(Name name) {
//        String s = name.toString();
//        return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
//    }
//
//    private Name getSetterName(Name name) {
//        String s = name.toString();
//        return names.fromString("set" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
//    }
//
//    private void log(String msg) {
//        if (processingEnv.getOptions().containsKey("debug")) {
//            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
//        }
//    }
//
//}