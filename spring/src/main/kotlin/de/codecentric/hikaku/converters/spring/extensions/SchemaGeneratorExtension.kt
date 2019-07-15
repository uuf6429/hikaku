package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.schemas.*
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import kotlin.reflect.*
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

internal fun KParameter.toSchema(): Schema? =
        this.type.jvmErasure.toSchema(
                this.type,
                this.type.arguments.map { it.toSchema() }
        )

internal fun KTypeProjection.toSchema(): Schema? =
        this.type?.jvmErasure?.toSchema(
                this.type,
                this.type?.arguments?.map { it.toSchema() }
        )

internal fun KType.toSchema(): Schema? =
        this.jvmErasure.toSchema(
                this,
                this.arguments.map { it.toSchema() }
        )

internal fun KClass<*>.toSchema(annotations: KAnnotatedElement?, variantArgs: List<Schema?>?): Schema? {
    return when {
        this.java == Boolean::class.java ->
            Boolean()
        this.java == Int::class.java ->
            Integer(annotations.minInt, annotations.maxInt)
        this.java == Float::class.java ->
            Float(annotations.minDec, annotations.maxDec)
        this.java == String::class.java ->
            String(annotations.minInt, annotations.maxInt)
        this.isData ->
            Object(
                    memberProperties
                            .map { prop ->
                                prop.name to prop.returnType.jvmErasure.toSchema(
                                        prop,
                                        prop.returnType.arguments.mapNotNull { type -> type.toSchema() }
                                )
                            }
                            .filter {
                                it.second != null
                            }
                            .map {
                                it.first to it.second!!
                            }
                            .toMap()
            )
        this.isSubclassOf(Iterable::class) ->
            Array(
                    checkNotNull(variantArgs?.getOrNull(0)) {
                        "Could not determine type of array"
                    },
                    annotations.minInt,
                    annotations.maxInt
            )
        else ->
            null
    }
}

internal val KAnnotatedElement?.minInt
    get() =
        this?.annotation<Min>()?.value?.toInt()

internal val KAnnotatedElement?.maxInt
    get() =
        this?.annotation<Max>()?.value?.toInt()

internal val KAnnotatedElement?.minDec
    get() =
        this?.annotation<DecimalMin>()?.value?.toFloat()

internal val KAnnotatedElement?.maxDec
    get() =
        this?.annotation<DecimalMax>()?.value?.toFloat()

internal val KAnnotatedElement?.allAnnotations
    get() =
        this?.annotations.orEmpty()
                .union(
                        if (this is KProperty<*>) this.javaField?.annotations?.toList().orEmpty() else emptyList()
                )
                .union(
                        if (this is KProperty1<*, *>) this.javaField?.annotations?.toList().orEmpty() else emptyList()
                )
                .union(
                        if (this is KProperty2<*, *, *>) this.javaField?.annotations?.toList().orEmpty() else emptyList()
                )

internal inline fun <reified T : Annotation> KAnnotatedElement.annotation(): T? =
        @Suppress("UNCHECKED_CAST")
        this.allAnnotations.firstOrNull { it is T } as T?