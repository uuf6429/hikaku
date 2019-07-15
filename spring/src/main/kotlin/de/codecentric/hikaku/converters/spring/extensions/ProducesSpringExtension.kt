package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.HttpMethod
import de.codecentric.hikaku.endpoints.schemas.Schema
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import java.lang.reflect.Method
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

internal fun HttpMethod.produces(entry: Map.Entry<RequestMappingInfo, HandlerMethod>): Map<String, Schema?> {
    val isNotErrorPath = !entry.key.patternsCondition.patterns.contains("/error")
    val hasNoResponseBodyAnnotation = !entry.value.providesResponseBodyAnnotation()
    val hasNoRestControllerAnnotation = !entry.value.providesRestControllerAnnotation()
    val hasServletResponseParam = entry.value.hasHttpServletResponseParam()
    val methodReturnType = entry.value.method.kotlinFunction?.returnType

    if (isNotErrorPath && (hasNoResponseBodyAnnotation && hasNoRestControllerAnnotation)) {
        return emptyMap()
    }

    if (isNotErrorPath && (entry.value.method.hasNoReturnType() && !hasServletResponseParam)) {
        return emptyMap()
    }

    val schema: Schema? =
            when {
                this.name == "HEAD" ->
                    null
                (methodReturnType?.classifier as KClass<*>).isSubclassOf(ResponseEntity::class) ->
                    methodReturnType.arguments.singleOrNull()?.toSchema()
                else ->
                    methodReturnType.toSchema()
            }

    val produces = entry.key
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

private fun HandlerMethod.hasHttpServletResponseParam() =
        this.methodParameters.any { it.parameterType is HttpServletResponse }