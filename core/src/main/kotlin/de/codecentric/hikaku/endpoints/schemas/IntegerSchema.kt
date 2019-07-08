package de.codecentric.hikaku.endpoints.schemas

class IntegerSchema(
        val minValue: Int?,
        val maxValue: Int?
): SchemaInterface {
    override val type: String
        get() = "integer"
}