package de.codecentric.hikaku.converters.openapi.extractors

import de.codecentric.hikaku.converters.openapi.extensions.referencedSchema
import de.codecentric.hikaku.converters.openapi.extensions.toSchema
import de.codecentric.hikaku.endpoints.schemas.Schema
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation

internal class ProducesExtractor(private val openApi: OpenAPI) {

    operator fun invoke(operation: Operation?) =
        operation?.responses
                ?.flatMap {
                    it.value
                            ?.content
                            ?.map {
                                it.key to it.value.toSchema(openApi)
                            }
                            .orEmpty()
                }
                .orEmpty()
                .union(extractResponsesFromComponents(operation))
                .toMap()

    private fun extractResponsesFromComponents(operation: Operation?) =
        operation?.responses
                ?.mapNotNull { it.value.referencedSchema }
                ?.map {
                    Regex("#/components/responses/(?<key>.+)")
                            .find(it)
                            ?.groups
                            ?.get("key")
                            ?.value
                }
                ?.flatMap {
                    openApi.components
                            .responses[it]
                            ?.content
                            ?.map {
                                it.key to it.value.toSchema(openApi)
                            }
                            .orEmpty()
                }
                .orEmpty()
}