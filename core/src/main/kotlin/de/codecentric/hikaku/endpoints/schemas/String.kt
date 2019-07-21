package de.codecentric.hikaku.endpoints.schemas

data class String(
        val minLength: Int? = null,
        val maxLength: Int? = null,
        val nullable: kotlin.Boolean = false
) : Schema {
    override fun toString() =
            "String(${this.stringifyAssignedProperties(
                    this::minLength to null,
                    this::maxLength to null,
                    this::nullable to false
            )})"
}