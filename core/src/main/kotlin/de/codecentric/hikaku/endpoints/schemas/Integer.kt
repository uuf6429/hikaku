package de.codecentric.hikaku.endpoints.schemas

data class Integer(
        val minValue: Long? = null,
        val maxValue: Long? = null,
        val nullable: kotlin.Boolean = false
) : Schema {
    override fun toString() =
            "Integer(${this.stringifyAssignedProperties(
                    this::minValue to null,
                    this::maxValue to null,
                    this::nullable to false
            )})"
}