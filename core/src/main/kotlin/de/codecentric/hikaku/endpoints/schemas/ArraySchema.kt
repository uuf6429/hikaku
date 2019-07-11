package de.codecentric.hikaku.endpoints.schemas

class ArraySchema(
        val items: SchemaInterface,
        val minItems: Int?,
        val maxItems: Int?
): SchemaInterface {
    override val type: String
        get() = "array"

    override fun toString(): String = "$items[${minItems ?: "*"}..${maxItems ?: "*"}]"

    override fun equals(other: Any?): Boolean =
            other is ArraySchema
                    && other.items == this.items
                    && other.minItems == this.minItems
                    && other.maxItems === this.maxItems
}