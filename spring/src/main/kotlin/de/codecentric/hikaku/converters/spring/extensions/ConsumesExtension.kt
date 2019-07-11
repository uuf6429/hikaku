package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.schemas.*
import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

internal fun Map.Entry<RequestMappingInfo, HandlerMethod>.consumes(): Map<String, SchemaInterface?> {
    val requestBodyParameter = this.value
            .method
            .kotlinFunction
            ?.parameters
            ?.firstOrNull {
                it.annotations
                        .filterIsInstance<RequestBody>()
                        .any()
            }

    if (requestBodyParameter === null) {
        return emptyMap()
    }

    val consumes = this.key
            .consumesCondition
            .expressions
            .map { it.mediaType.toString() to requestBodyParameter.toSchema() }

    if (consumes.isNotEmpty()) {
        return consumes.toMap()
    }

    val isParameterString = requestBodyParameter
            .type
            .jvmErasure
            .let {
                it.java == java.lang.String::class.java
            }

    return if (isParameterString) {
        mapOf(ALL_VALUE to requestBodyParameter.toSchema() )
    } else {
        mapOf(APPLICATION_JSON_UTF8_VALUE to requestBodyParameter.toSchema() )
    }
}