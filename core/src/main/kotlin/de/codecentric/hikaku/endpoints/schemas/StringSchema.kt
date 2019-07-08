package de.codecentric.hikaku.endpoints.schemas

class StringSchema(
        val minLength: Int?,
        val maxLength: Int?
): SchemaInterface {
    override val type: String
        get() = "string"
}