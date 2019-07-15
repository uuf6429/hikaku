package de.codecentric.hikaku.endpoints.schemas

interface Schema {
    val type: kotlin.String

    override fun toString(): kotlin.String
}