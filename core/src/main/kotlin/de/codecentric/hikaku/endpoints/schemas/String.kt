package de.codecentric.hikaku.endpoints.schemas

data class String(
        val minLength: Int? = null,
        val maxLength: Int? = null
) : Schema {
    override fun toString() =
            "String(${this.stringifyAssignedProperties(
                    this::minLength to null,
                    this::maxLength to null
            )})"
}