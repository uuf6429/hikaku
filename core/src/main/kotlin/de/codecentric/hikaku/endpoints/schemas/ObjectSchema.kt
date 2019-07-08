package de.codecentric.hikaku.endpoints.schemas

import java.util.*

class ObjectSchema(
        val properties: Dictionary<String, SchemaInterface>
): SchemaInterface {
    override val type: String
        get() = "object"
}