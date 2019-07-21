package de.codecentric.hikaku.endpoints.schemas

data class Object(
        val properties: Map<kotlin.String, Schema> = emptyMap(),
        val nullable: kotlin.Boolean = false
) : Schema {
    override fun toString() =
            "Object(${this.stringifyAssignedProperties(
                    this::properties to emptyMap<String, Schema>(),
                    this::nullable to false
            )})"
}