package de.codecentric.hikaku.endpoints.schemas

data class Integer(
        val minValue: Int? = null,
        val maxValue: Int? = null
) : Schema {
    override val type
        get() = "integer"

    override fun toString() =
            "{type: int, min: ${minValue ?: "*"}, max: ${maxValue ?: "*"}}"
}