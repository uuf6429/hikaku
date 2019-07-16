package de.codecentric.hikaku.endpoints.schemas

import kotlin.reflect.KProperty

interface Schema {
    override fun toString(): kotlin.String
}

internal fun  Schema.stringifyAssignedProperties(vararg properties: Pair<KProperty<*>, Any?>) =
        properties
            .map {
                Triple(it.first, it.second, it.first.call())
            }
            .filter {
                it.second != it.third
            }.joinToString(", ") {
                "${it.first.name} = ${it.third}"
            }