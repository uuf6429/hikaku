package de.codecentric.hikaku.endpoints.schemas

data class Combination(
        val schemas: List<Schema> = emptyList()
) : Schema {
    override fun toString() =
            "Combination(${this.stringifyAssignedProperties(
                    this::schemas to emptyList<Schema>()
            )})"
}