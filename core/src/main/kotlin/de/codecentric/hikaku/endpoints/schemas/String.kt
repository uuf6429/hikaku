package de.codecentric.hikaku.endpoints.schemas

data class String(
        val minLength: Int?,
        val maxLength: Int?
) : Schema {
    override val type
        get() = "string"

    override fun toString() =
            "{type: str, min: ${minLength ?: "*"}, max: ${maxLength ?: "*"}}"
}