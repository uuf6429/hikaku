package de.codecentric.hikaku.endpoints.schemas

class StringSchema(
        val minLength: Int?,
        val maxLength: Int?
): SchemaInterface {
    override val type: String
        get() = "string"

    override fun toString(): String = "string(${minLength ?: "*"}..${maxLength ?: "*"})"

    override fun equals(other: Any?): Boolean =
            other is StringSchema
                    && other.minLength == this.minLength
                    && other.maxLength === this.maxLength
}