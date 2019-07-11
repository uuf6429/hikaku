package de.codecentric.hikaku.converters.spring.extensions

import de.codecentric.hikaku.endpoints.schemas.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure


internal fun KParameter.toSchema(): SchemaInterface? =
    this.type.jvmErasure.toSchema(this.type.arguments.map { it.type?.jvmErasure?.toSchema() })

internal fun KClass<*>.toSchema(variantArgs: List<SchemaInterface?>? = null): SchemaInterface? {
    return when {
        this.java == Boolean::class.java ->
            BoolSchema()
        this.java == Int::class.java ->
            IntegerSchema(null, null)
        this.java == Float::class.java ->
            FloatSchema(null, null)
        this.java == String::class.java ->
            StringSchema(null, null)
        this.isData ->
            ObjectSchema(
                this.memberProperties
                    .map { prop ->
                        prop.name to prop.returnType.jvmErasure.toSchema(
                            prop.returnType.arguments.mapNotNull { type -> type.type?.jvmErasure?.toSchema() }
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
                null,
                null
            )
        else ->
            null
    }
}