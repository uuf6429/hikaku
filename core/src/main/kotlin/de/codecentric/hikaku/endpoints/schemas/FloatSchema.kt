package de.codecentric.hikaku.endpoints.schemas

class FloatSchema(
        val minValue: Float?,
        val maxValue: Float?
): SchemaInterface {
    override val type: String
        get() = "float"
}