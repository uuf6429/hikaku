package de.codecentric.hikaku.endpoints.schemas

class ObjectSchema(
        val properties: Map<String, SchemaInterface>
): SchemaInterface {
    override val type: String
        get() = "object"

    override fun toString(): String = "{${properties.map { "${it.key}: ${it.value}" }.joinToString(", ")}}"

    override fun equals(other: Any?): Boolean =
            other is ObjectSchema
                    && other.properties == this.properties
}