package de.codecentric.hikaku.endpoints.schemas

data class Float(
        val minValue: kotlin.Float? = null,
        val maxValue: kotlin.Float? = null
) : Schema {
    override fun toString() =
            "Float(${this.stringifyAssignedProperties(
                this::minValue to null,
                this::maxValue to null
            )})"
}