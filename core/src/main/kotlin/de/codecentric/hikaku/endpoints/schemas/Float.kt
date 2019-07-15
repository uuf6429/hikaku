package de.codecentric.hikaku.endpoints.schemas

data class Float(
        val minValue: kotlin.Float?,
        val maxValue: kotlin.Float?
) : Schema {
    override val type
        get() = "float"

    override fun toString() =
            "{type: dec, min: ${minValue ?: "*"}, max: ${maxValue ?: "*"}}"
}