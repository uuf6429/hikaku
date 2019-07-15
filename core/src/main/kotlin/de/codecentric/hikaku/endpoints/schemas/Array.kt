package de.codecentric.hikaku.endpoints.schemas

data class Array(
        val items: Schema,
        val minItems: Int?,
        val maxItems: Int?
) : Schema {
    override val type
        get() = "array"

    override fun toString() =
            "{type: arr, min: ${minItems ?: "*"}, max: ${maxItems ?: "*"}, items: $items}"
}