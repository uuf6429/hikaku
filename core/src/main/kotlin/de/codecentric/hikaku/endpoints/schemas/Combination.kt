package de.codecentric.hikaku.endpoints.schemas

data class Combination(
        val schemas: List<Schema> = emptyList(),
        val nullable: kotlin.Boolean = false
) : Schema {
    override fun toString() =
            "Combination(${this.stringifyAssignedProperties(
                    this::schemas to emptyList<Schema>(),
                    this::nullable to false
            )})"
}