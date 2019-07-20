package de.codecentric.hikaku.endpoints.schemas

data class Decimal(
        val minValue: Double? = null,
        val maxValue: Double? = null
) : Schema {
    override fun toString() =
            "Decimal(${this.stringifyAssignedProperties(
                    this::minValue to null,
                    this::maxValue to null
            )})"
}