package de.codecentric.hikaku.endpoints.schemas

data class Integer(
        val minValue: Int?,
        val maxValue: Int?
) : Schema {
    override val type
        get() = "integer"

    override fun toString() =
            "{type: int, min: ${minValue ?: "*"}, max: ${maxValue ?: "*"}}"
}