package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.schemas.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

internal fun KParameter.toSchema(): SchemaInterface? =
    this.type.jvmErasure.toSchema(
        this.annotations,
        this.type.arguments.map { it.toSchema() }
    )

internal fun KTypeProjection.toSchema(): SchemaInterface? =
    this.type?.jvmErasure?.toSchema(
        this.type?.annotations.orEmpty(),
        this.type?.arguments?.map { it.toSchema() }
    )

internal fun KClass<*>.toSchema(annotations: List<Annotation>, variantArgs: List<SchemaInterface?>?): SchemaInterface? {
    return when {
        this.java == Boolean::class.java ->
            BoolSchema()
        this.java == Int::class.java ->
            IntegerSchema(annotations.min(), annotations.max())
        this.java == Float::class.java ->
            FloatSchema(annotations.min(), annotations.max())
        this.java == String::class.java ->
            StringSchema(annotations.min(), annotations.max())
        this.isData ->
            ObjectSchema(
                this.memberProperties
                    .map { prop ->
                        prop.name to prop.returnType.jvmErasure.toSchema(
                            prop.annotations,
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
            ArraySchema(
                checkNotNull(variantArgs?.getOrNull(0)) {
                    "Could not determine type of array"
                },
                annotations.min(),
                annotations.max()
            )
        else ->
            null
    }
}

internal fun <T> List<Annotation>.min(): T? = null // TODO this.firstOrNull{ it.annotationClass.qualifiedName == "javax.validation.constraints.Min" }

internal fun <T> List<Annotation>.max(): T? = null // TODO
