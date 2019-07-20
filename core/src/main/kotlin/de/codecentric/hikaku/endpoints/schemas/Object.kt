package de.codecentric.hikaku.endpoints.schemas

data class Object(
        val properties: Map<kotlin.String, Schema> = emptyMap()
) : Schema {
    override fun toString() =
            "Object(${this.stringifyAssignedProperties(
                    this::properties to emptyMap<String, Schema>()
            )})"
}