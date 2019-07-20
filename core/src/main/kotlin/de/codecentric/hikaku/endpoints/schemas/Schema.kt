package de.codecentric.hikaku.endpoints.schemas

import java.lang.RuntimeException
import kotlin.reflect.KProperty

interface Schema {
    override fun toString(): kotlin.String
}

internal fun Schema.stringifyAssignedProperties(vararg properties: Pair<KProperty<*>, Any?>) =
        properties
                .map {
                    Triple(it.first, it.second, it.first.call())
                }
                .filter {
                    it.second != it.third
                }.joinToString(", ") {
                    "${it.first.name} = ${it.third.toCode()}"
                }

internal fun Any?.toCode(): kotlin.String =
        if (this == null)
            "null"
        else
            when (this) {
                is kotlin.String ->
                    "\"$this\""
                is kotlin.Boolean ->
                    if (this) "true" else "false"
                is Number ->
                    this.toString()
                is Map<*, *> ->
                    if (this.isEmpty())
                        "emptyMap()"
                    else
                        "mapOf(${this.map { "${it.key.toCode()} to ${it.value.toCode()}" }.joinToString(", ")})"
                is List<*> ->
                    if (this.isEmpty())
                        "emptyList()"
                    else
                        "listOf(${this.joinToString(", ") { it.toCode() }})"
                is Schema ->
                    this.toString()
                else ->
                    throw RuntimeException("Unsupported type: " + this::class.qualifiedName)
            }
