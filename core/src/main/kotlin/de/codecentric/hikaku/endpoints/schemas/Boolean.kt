package de.codecentric.hikaku.endpoints.schemas

data class Boolean(
        val nullable: kotlin.Boolean = false
) : Schema {
    override fun toString() =
            "Boolean(${this.stringifyAssignedProperties(
                    this::nullable to false
            )})"
}