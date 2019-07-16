package de.codecentric.hikaku.converters.openapi.extractors

import de.codecentric.hikaku.converters.openapi.extensions.referencedSchema
import de.codecentric.hikaku.converters.openapi.extensions.toSchema
import de.codecentric.hikaku.endpoints.schemas.Schema
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation

internal class ConsumesExtractor(private val openApi: OpenAPI) {

    operator fun invoke(operation: Operation?): Map<String, Schema?> =
        operation?.requestBody
                ?.content
                ?.map {
                    it.key to it.value.toSchema(openApi)
                }
                .orEmpty()
                .union(extractConsumesFromComponents(operation))
                .toMap()

    private fun extractConsumesFromComponents(operation: Operation?) =
        operation?.requestBody
                ?.referencedSchema
                ?.let {
                    Regex("#/components/requestBodies/(?<key>.+)")
                            .find(it)
                            ?.groups
                            ?.get("key")
                            ?.value
                }
                ?.let {
                    openApi.components
                            .requestBodies[it]
                            ?.content
                            ?.map {
                                it.key to it.value.toSchema(openApi)
                            }
                }
                .orEmpty()
}