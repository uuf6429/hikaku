package de.codecentric.hikaku.endpoints.schemas

data class String(
        val minLength: Int? = null,
        val maxLength: Int? = null
) : Schema {
    override val type
        get() = "string"

    override fun toString() =
            "{type: str, min: ${minLength ?: "*"}, max: ${maxLength ?: "*"}}"
}