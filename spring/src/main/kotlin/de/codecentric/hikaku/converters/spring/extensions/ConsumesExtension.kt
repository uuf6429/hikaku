package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.HttpMethod
import de.codecentric.hikaku.endpoints.schemas.*
import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

internal fun HttpMethod.consumes(entry: Map.Entry<RequestMappingInfo, HandlerMethod>): Map<String, Schema?> {
    val requestBodyParameter = entry.value
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

    val schema: Schema? =
            if (this.name == "HEAD")
                null
            else
                requestBodyParameter.toSchema()

    val consumes = entry.key
            .consumesCondition
            .expressions
            .map { it.mediaType.toString() to schema }

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
        mapOf(ALL_VALUE to schema)
    } else {
        mapOf(APPLICATION_JSON_UTF8_VALUE to schema)
    }
}