package de.codecentric.hikaku.endpoints.schemas

data class Decimal(
        val minValue: Double? = null,
        val maxValue: Double? = null,
        val nullable: kotlin.Boolean = false
) : Schema {
    override fun toString() =
            "Decimal(${this.stringifyAssignedProperties(
                    this::minValue to null,
                    this::maxValue to null,
                    this::nullable to false
            )})"
}