package de.codecentric.hikaku.endpoints.schemas

class BoolSchema(): SchemaInterface {
    override val type: String
        get() = "bool"

    override fun toString(): String = "bool"

    override fun equals(other: Any?): Boolean =
            other is BoolSchema
}