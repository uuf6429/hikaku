package de.codecentric.hikaku.endpoints.schemas

class FloatSchema(
        val minValue: Float?,
        val maxValue: Float?
): SchemaInterface {
    override val type: String
        get() = "float"

    override fun toString(): String = "float(${minValue ?: "*"}..${maxValue ?: "*"})"

    override fun equals(other: Any?): Boolean =
            other is FloatSchema
                    && other.minValue == this.minValue
                    && other.maxValue == this.maxValue
}