package de.codecentric.hikaku.endpoints.schemas

data class Boolean(val unused: Nothing? = null) : Schema {
    override val type
        get() = "bool"

    override fun toString() =
            "{type: bit}"
}