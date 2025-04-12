package com.tuanha.adapter.processor

import com.google.auto.service.AutoService
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.tuanha.adapter.annotation.ItemAdapter
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class AdapterProcessor : AbstractProcessor() {

    private var filer: Filer? = null
    private var messager: Messager? = null
    private var elements: Elements? = null
    private var classList: MutableList<ClassName>? = null

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        this.filer = processingEnvironment.filer
        this.messager = processingEnvironment.messager
        this.elements = processingEnvironment.elementUtils
        this.classList = ArrayList()
    }

    override fun process(set: Set<TypeElement?>, roundEnvironment: RoundEnvironment): Boolean {

        val elementList = roundEnvironment.getElementsAnnotatedWith(ItemAdapter::class.java)

        for (element in elementList) {

            if (element.kind != ElementKind.CLASS) {
                messager!!.printMessage(Diagnostic.Kind.ERROR, "@ItemAdapter should be on top of classes")
                return false
            }

            val className = ClassName.get(elements!!.getPackageOf(element).qualifiedName.toString(), element.simpleName.toString())

            classList!!.add(className)
        }


        val list = ClassName.get("java.util", "List")
        val arrayList = ClassName.get("java.util", "ArrayList")

        val navigationDeepLink = ClassName.get("java.lang", "Object")
        val listOfNavigationDeepLink: TypeName = ParameterizedTypeName.get(list, navigationDeepLink)

        var builder = MethodSpec.methodBuilder("all")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .returns(listOfNavigationDeepLink)
            .addStatement("\$T result = new \$T<>()", listOfNavigationDeepLink, arrayList)

        for (className in classList!!) {
            builder = builder.addStatement("result.add(new \$T())", className)
        }


        val startMethod = builder.addStatement("return result")
            .build()

        val keepAnnotation = AnnotationSpec.builder(ClassName.get("androidx.annotation", "Keep"))
            .build()


        val generatedClass = TypeSpec
            .classBuilder("AdapterProvider")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(keepAnnotation)
            .addMethod(startMethod)
            .build()

        try {
            JavaFile.builder("com.tuanha.adapter", generatedClass).build().writeTo(filer)
        } catch (_: IOException) {
        }

        return true
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(ItemAdapter::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}