package de.codecentric.hikaku.endpoints.schemas

class IntegerSchema(
        val minValue: Int?,
        val maxValue: Int?
): SchemaInterface {
    override val type: String
        get() = "integer"

    override fun toString(): String = "integer(${minValue ?: "*"}..${maxValue ?: "*"})"

    override fun equals(other: Any?): Boolean =
            other is IntegerSchema
                    && other.minValue == this.minValue
                    && other.maxValue == this.maxValue
}