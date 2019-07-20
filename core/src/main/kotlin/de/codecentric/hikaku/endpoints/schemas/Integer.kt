package de.codecentric.hikaku.endpoints.schemas

data class Integer(
        val minValue: Long? = null,
        val maxValue: Long? = null
) : Schema {
    override fun toString() =
            "Integer(${this.stringifyAssignedProperties(
                    this::minValue to null,
                    this::maxValue to null
            )})"
}