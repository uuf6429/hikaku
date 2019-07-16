package de.codecentric.hikaku.endpoints.schemas

data class Integer(
        val minValue: Int? = null,
        val maxValue: Int? = null
) : Schema {
    override fun toString() =
            "Integer(${this.stringifyAssignedProperties(
                this::minValue to null,
                this::maxValue to null
            )})"
}