package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.schemas.*
import de.codecentric.hikaku.endpoints.schemas.Array
import de.codecentric.hikaku.endpoints.schemas.Boolean
import de.codecentric.hikaku.endpoints.schemas.Decimal
import de.codecentric.hikaku.endpoints.schemas.String
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
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

internal fun KClass<*>.toSchema(annotations: KAnnotatedElement?, variantArgs: List<Schema?>?): Schema =
    when {
        this.java == kotlin.Boolean::class.java ->
            Boolean()
        this.java == kotlin.Int::class.java ->
            Integer(annotations.minLng, annotations.maxLng)
        this.java == kotlin.Float::class.java ->
            Decimal(annotations.minDec, annotations.maxDec)
        this.java == kotlin.String::class.java ->
            String(annotations.minInt, annotations.maxInt)
        this.isSubclassOf(Iterable::class) ->
            Array(
                checkNotNull(variantArgs?.getOrNull(0)) {
                    "Could not determine type of array"
                },
                annotations.minInt,
                annotations.maxInt
            )
        else ->
            Object(
                    this.memberProperties
                            .map { prop ->
                                prop.name to prop.returnType.jvmErasure.toSchema(
                                        AnnotationExtractor(
                                            prop,
                                            this.primaryConstructor?.parameters?.find { it.name == prop.name }
                                        ),
                                        prop.returnType.arguments.mapNotNull { type -> type.toSchema() }
                                )
                            }
                            .map {
                                it.first to it.second
                            }
                            .toMap()
            )
    }

internal val KAnnotatedElement?.minLng
    get() =
        this?.findAnnotation<Min>()?.value

internal val KAnnotatedElement?.maxLng
    get() =
        this?.findAnnotation<Max>()?.value

internal val KAnnotatedElement?.minInt
    get() =
        this?.findAnnotation<Min>()?.value?.toInt()

internal val KAnnotatedElement?.maxInt
    get() =
        this?.findAnnotation<Max>()?.value?.toInt()

internal val KAnnotatedElement?.minDec
    get() =
        this?.findAnnotation<DecimalMin>()?.value?.toDouble()

internal val KAnnotatedElement?.maxDec
    get() =
        this?.findAnnotation<DecimalMax>()?.value?.toDouble()

data class AnnotationExtractor(override val annotations: List<Annotation>) : KAnnotatedElement {
    constructor(vararg providers: KAnnotatedElement?) :
        this(providers.map { it?.annotations.orEmpty() }.flatten())
}