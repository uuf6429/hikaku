package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.schemas.*
import org.springframework.http.MediaType.ALL_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
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

internal fun KParameter.toSchema(): SchemaInterface? {
    return when {
        this.type.jvmErasure.java == Boolean::class.java ->
            BoolSchema()
        this.type.jvmErasure.java == Int::class.java ->
            IntegerSchema(null, null)
        this.type.jvmErasure.java == Float::class.java ->
            FloatSchema(null, null)
        this.type.jvmErasure.java == String::class.java ->
            StringSchema(null, null)
        this.type.jvmErasure.isData ->
            ObjectSchema(
                    this.type.jvmErasure.memberProperties
                            .map {
                                it.name to it.returnType.toSchema()
                            }
                            .filter {
                                it.second != null
                            }
                            .map {
                                it.first to it.second!!
                            }
                            .toMap()
            )
        // TODO handle arrays
//        this.type.jvmErasure.java == String::class.java ->
//            StringSchema(null, null)
        else ->
            null
    }
}
internal fun KType.toSchema(): SchemaInterface? {
    return when {
        this.jvmErasure.java == Boolean::class.java ->
            BoolSchema()
        this.jvmErasure.java == Int::class.java ->
            IntegerSchema(null, null)
        this.jvmErasure.java == Float::class.java ->
            FloatSchema(null, null)
        this.jvmErasure.java == String::class.java ->
            StringSchema(null, null)
//        this.jvmErasure.isData ->
//            ObjectSchema(
//                    this.jvmErasure.memberProperties
//                            .map {
//                                it.name to it.toSchema()
//                            }
//                            .toMap()
//            )
        // TODO handle arrays
//        this.type.jvmErasure.java == String::class.java ->
//            StringSchema(null, null)
        else ->
            null
    }
}