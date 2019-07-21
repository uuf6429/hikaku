package de.codecentric.hikaku.endpoints.schemas

data class Array(
        val items: Schema? = null,
        val minItems: Int? = null,
        val maxItems: Int? = null,
        val nullable: kotlin.Boolean = false
) : Schema {
    override fun toString() =
            "Array(${this.stringifyAssignedProperties(
                    this::items to null,
                    this::minItems to null,
                    this::maxItems to null,
                    this::nullable to false
            )})"
}