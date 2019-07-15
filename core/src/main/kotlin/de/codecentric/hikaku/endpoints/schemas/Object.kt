package de.codecentric.hikaku.endpoints.schemas

data class Object(
        val properties: Map<kotlin.String, Schema> = emptyMap()
) : Schema {
    override val type
        get() = "object"

    override fun toString() =
            "{type: obj, props: {${properties.map { "${it.key}: ${it.value}" }.joinToString(", ")}}}"
}