package de.codecentric.hikaku.endpoints.schemas

class MixSchema(
        val schemas: List<SchemaInterface>
): SchemaInterface {
    override val type: String
        get() = "mix"

    override fun toString(): String = schemas.joinToString(" | ") { it.toString() }

    override fun equals(other: Any?): Boolean =
            other is MixSchema
                    && other.schemas == this.schemas
}