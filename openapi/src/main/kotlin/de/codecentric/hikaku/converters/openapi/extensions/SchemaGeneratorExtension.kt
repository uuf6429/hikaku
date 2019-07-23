package de.codecentric.hikaku.converters.openapi.extensions

import de.codecentric.hikaku.endpoints.schemas.*
import de.codecentric.hikaku.endpoints.schemas.Array
import de.codecentric.hikaku.endpoints.schemas.Boolean
import de.codecentric.hikaku.endpoints.schemas.String
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.ArraySchema as OpenApiArraySchema
import io.swagger.v3.oas.models.media.BooleanSchema as OpenApiBooleanSchema
import io.swagger.v3.oas.models.media.IntegerSchema as OpenApiIntegerSchema
import io.swagger.v3.oas.models.media.MediaType as OpenApiMediaType
import io.swagger.v3.oas.models.media.NumberSchema as OpenApiNumberSchema
import io.swagger.v3.oas.models.media.ObjectSchema as OpenApiObjectSchema
import io.swagger.v3.oas.models.media.Schema as OpenApiSchema
import io.swagger.v3.oas.models.media.StringSchema as OpenApiStringSchema

internal fun OpenApiMediaType?.toSchema(openApi: OpenAPI): Schema? =
        this?.schema?.toSchema(openApi)

internal fun OpenApiSchema<*>.toSchema(openApi: OpenAPI): Schema? =
        if (!this.`$ref`.isNullOrEmpty())
            findReferencedSchema(openApi, this.`$ref`)?.toSchema(openApi)
        else
            when (this) {
                is OpenApiBooleanSchema ->
                    Boolean(this.nullable ?: false)
                is OpenApiIntegerSchema ->
                    Integer(this.minimum?.toLong(), this.maximum?.toLong(), this.nullable ?: false)
                is OpenApiNumberSchema ->
                    Decimal(this.minimum?.toDouble(), this.maximum?.toDouble(), this.nullable ?: false)
                is OpenApiStringSchema ->
                    String(this.minLength, this.maxLength, this.nullable ?: false)
                is OpenApiArraySchema ->
                    Array(this.items.toSchema(openApi), this.minItems, this.maxItems, this.nullable ?: false)
                is OpenApiObjectSchema ->
                    Object(
                            this.properties
                                    .map { it.key to it.value.toSchema(openApi)!! }
                                    .toMap(),
                            this.nullable ?: false
                    )
                else ->
                    null // TODO throw exception
            }


internal fun OpenApiSchema<*>.findReferencedSchema(openApi: OpenAPI, ref: kotlin.String): OpenApiSchema<*>? =
        openApi.components
                .schemas[
                Regex("#/components/schemas/(?<key>.+)")
                        .find(ref)
                        ?.groups
                        ?.get("key")
                        ?.value
        ]