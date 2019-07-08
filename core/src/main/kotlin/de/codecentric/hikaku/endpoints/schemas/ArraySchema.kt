package de.codecentric.hikaku.endpoints.schemas

class ArraySchema(
        val items: SchemaInterface,
        val minItems: Int?,
        val maxItems: Int?
): SchemaInterface {
    override val type: String
        get() = "array"
}