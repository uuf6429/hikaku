package de.codecentric.hikaku.endpoints.schemas

data class Float(
        val minValue: kotlin.Float? = null,
        val maxValue: kotlin.Float? = null
) : Schema {
    override val type
        get() = "float"

    override fun toString() =
            "{type: dec, min: ${minValue ?: "*"}, max: ${maxValue ?: "*"}}"
}