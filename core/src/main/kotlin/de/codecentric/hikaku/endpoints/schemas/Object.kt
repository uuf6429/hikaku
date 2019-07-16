package de.codecentric.hikaku.endpoints.schemas

data class Object(
        val properties: Map<kotlin.String, Schema> = emptyMap()
) : Schema {
    override fun toString() =
            "Object(properties = mapOf(${properties.map { "\"${it.key}\" to ${it.value}" }.joinToString(", ")}))"
}