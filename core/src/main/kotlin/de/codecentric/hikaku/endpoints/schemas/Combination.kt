package de.codecentric.hikaku.endpoints.schemas

data class Combination(
        val schemas: List<Schema>
) : Schema {
    override val type
        get() = "mix"

    override fun toString() =
            "{type: com, schemas: ${schemas.joinToString(" | ") { it.toString() }}"
}