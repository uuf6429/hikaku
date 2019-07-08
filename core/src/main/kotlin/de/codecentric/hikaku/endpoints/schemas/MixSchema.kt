package de.codecentric.hikaku.endpoints.schemas

class MixSchema(
        val schemas: List<SchemaInterface>
): SchemaInterface {
    override val type: String
        get() = "mix"
}