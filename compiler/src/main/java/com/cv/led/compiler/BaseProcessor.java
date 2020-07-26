package com.cv.led.compiler;

import com.cv.led.annotation.Const;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by jzj on 2018/3/23.
 */

public abstract class BaseProcessor extends AbstractProcessor {

    protected Filer filer;
    protected Types types;
    protected Elements elements;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        types = processingEnvironment.getTypeUtils();
        elements = processingEnvironment.getElementUtils();
    }

    /**
     * 从字符串获取TypeElement对象
     */
    public TypeElement typeElement(String className) {
        return elements.getTypeElement(className);
    }

    /**
     * 从字符串获取TypeMirror对象
     */
    public TypeMirror typeMirror(String className) {
        return typeElement(className).asType();
    }

    /**
     * 从字符串获取ClassName对象
     */
    public ClassName className(String className) {
        return ClassName.get(typeElement(className));
    }

    /**
     * 从字符串获取TypeName对象，包含Class的泛型信息
     */
    public TypeName typeName(String className) {
        return TypeName.get(typeMirror(className));
    }

    public static String getClassName(TypeMirror typeMirror) {
        return typeMirror == null ? "" : typeMirror.toString();
    }

    public boolean isSubType(TypeMirror type, String className) {
        return type != null && types.isSubtype(type, typeMirror(className));
    }

    public boolean isSubType(Element element, String className) {
        return element != null && isSubType(element.asType(), className);
    }

    public boolean isSubType(Element element, TypeMirror typeMirror) {
        return element != null && types.isSubtype(element.asType(), typeMirror);
    }

    /**
     * 非抽象类
     */
    public boolean isConcreteType(Element element) {
        return element instanceof TypeElement && !element.getModifiers().contains(
                Modifier.ABSTRACT);
    }

    /**
     * 非抽象子类
     */
    public boolean isConcreteSubType(Element element, String className) {
        return isConcreteType(element) && isSubType(element, className);
    }

    /**
     * 非抽象子类
     */
    public boolean isConcreteSubType(Element element, TypeMirror typeMirror) {
        return isConcreteType(element) && isSubType(element, typeMirror);
    }

    public static String randomHash() {
        return hash(UUID.randomUUID().toString());
    }

    public static String hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(str.hashCode());
        }
    }

    /**
     * 创建Handler。格式：<code>"com.demo.TestActivity"</code> 或 <code>new TestHandler()</code>
     */
    public CodeBlock buildHandler(boolean isActivity, Symbol.ClassSymbol cls) {
        CodeBlock.Builder b = CodeBlock.builder();
        if (isActivity) {
            b.add("$S", cls.className());
        } else {
            b.add("new $T()", cls);
        }
        return b.build();
    }

    /**
     * 辅助工具类，用于生成ServiceInitClass，格式如下：
     * <pre>
     * package com.sankuai.waimai.router.generated.service;
     *
     * import com.sankuai.waimai.router.service.ServiceLoader;
     *
     * public class &lt;ClassName&gt; {
     *     public static void init() {
     *         ServiceLoader.put(com.xxx.interface1.class, "key1", com.xxx.implementsA.class, false);
     *         ServiceLoader.put(com.xxx.interface2.class, "key2", com.xxx.implementsB.class, false);
     *     }
     * }
     * </pre>
     */
    public class ServiceInitClassBuilder {

        private final String className;
        private final CodeBlock.Builder builder;
        private final ClassName serviceLoaderClass;

        public ServiceInitClassBuilder(String className) {
            this.className = className;
            this.builder = CodeBlock.builder();
            this.serviceLoaderClass = className(Const.SERVICE_LOADER_CLASS);
        }

        public ServiceInitClassBuilder put(String interfaceName, String key, String implementName, boolean singleton) {
            builder.addStatement("$T.put($T.class, $S, $T.class, $L)",
                    serviceLoaderClass,
                    className(interfaceName),
                    key,
                    className(implementName),
                    singleton);
            return this;
        }

        public ServiceInitClassBuilder putDirectly(String interfaceName, String key, String implementName, boolean singleton) {
            // implementName是注解生成的类，直接用$L拼接原始字符串
            builder.addStatement("$T.put($T.class, $S, $L.class, $L)",
                    serviceLoaderClass,
                    className(interfaceName),
                    key,
                    implementName,
                    singleton);
            return this;
        }

        public void build() {
            MethodSpec methodSpec = MethodSpec.methodBuilder(Const.INIT_METHOD)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addCode(this.builder.build())
                    .build();

            TypeSpec typeSpec = TypeSpec.classBuilder(this.className)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodSpec)
                    .build();
            try {
                JavaFile.builder(Const.GEN_PKG_SERVICE, typeSpec)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
