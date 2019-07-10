package de.codecentric.hikaku.endpoints.schemas

interface SchemaInterface {
    val type: String

    override fun toString(): String

    override operator fun equals(other: Any?): Boolean
}