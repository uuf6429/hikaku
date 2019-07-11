package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.schemas.SchemaInterface
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import java.lang.reflect.Method
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

internal fun Map.Entry<RequestMappingInfo, HandlerMethod>.produces(): Map<String, SchemaInterface?> {
    val isNotErrorPath = !this.key.patternsCondition.patterns.contains("/error")
    val hasNoResponseBodyAnnotation = !this.value.providesResponseBodyAnnotation()
    val hasNoRestControllerAnnotation = !this.value.providesRestControllerAnnotation()
    val servletResponseParam = this.value.getHttpServletResponseParam()
    val methodReturnType = this.value.method.kotlinFunction?.returnType

    if (isNotErrorPath && (hasNoResponseBodyAnnotation && hasNoRestControllerAnnotation)) {
        return emptyMap()
    }

    if (isNotErrorPath && (this.value.method.hasNoReturnType() && servletResponseParam === null)) {
        return emptyMap()
    }

    val schema: SchemaInterface? = null
    // TODO fill schema
    val produces = this.key
            .producesCondition
            .expressions
            .map { it.mediaType.toString() to schema }

    if (produces.isNotEmpty()) {
        return produces.toMap()
    }

    val isParameterString = methodReturnType
        ?.jvmErasure
        .let {
            it?.java == java.lang.String::class.java
        }

    return if (isParameterString) {
        mapOf(TEXT_PLAIN_VALUE to schema)
    } else {
        mapOf(APPLICATION_JSON_UTF8_VALUE to schema)
    }
}

private fun Method.hasNoReturnType() = this.returnType.name == "void" || this.returnType.name == "java.lang.Void"

private fun HandlerMethod.providesRestControllerAnnotation() = this.method
        .kotlinFunction
        ?.instanceParameter
        ?.type
        ?.jvmErasure
        ?.findAnnotation<RestController>() != null

private fun HandlerMethod.providesResponseBodyAnnotation() = isResponseBodyAnnotationOnClass() || isResponseBodyAnnotationOnFunction()

private fun HandlerMethod.isResponseBodyAnnotationOnClass() = this.method
        .kotlinFunction
        ?.instanceParameter
        ?.type
        ?.jvmErasure
        ?.findAnnotation<ResponseBody>() != null

private fun HandlerMethod.isResponseBodyAnnotationOnFunction() = this.method
        .kotlinFunction
        ?.findAnnotation<ResponseBody>() != null

private val javaxServletResponseClass: Class<*>? =
        (try {
            Class.forName("javax.servlet.http.HttpServletResponse")
        } catch (ex: Throwable) {
            null
        })

private fun HandlerMethod.getHttpServletResponseParam() =
        this.methodParameters
            .firstOrNull {
                javaxServletResponseClass !== null
                    && it.parameterType.isAssignableFrom(javaxServletResponseClass)
            }
