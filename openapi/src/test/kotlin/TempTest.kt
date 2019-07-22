package de.codecentric.hikaku.temp

import de.codecentric.hikaku.converters.openapi.OpenApiConverter
import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import de.codecentric.hikaku.endpoints.schemas.Array
import de.codecentric.hikaku.endpoints.schemas.Integer
import de.codecentric.hikaku.endpoints.schemas.Object
import de.codecentric.hikaku.endpoints.schemas.String
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class OpenApiSchemaTest {
    @Test
    fun `test openapi schema`() { // TODO
        //given
        val content = """
            openapi: 3.0.2
            info:
              version: 1.0.0
              title: Todo List
            paths:
              /todos:
                post:
                  description: ''
                  requestBody:
                    content:
                      'application/xml':
                        schema:
                          ${"$"}ref: '#/components/schemas/Todo'
                  responses:
                    '200':
                      description: OK
            components:
              schemas:
                Todo:
                  type: array
                  items:
                    type: string
        """.trimIndent()
        val implementation = setOf(
            Endpoint(
                path = "/todos",
                httpMethod = POST,
                consumes = mapOf("application/xml" to Array(String()))
            )
        )

        //when
        val specification = OpenApiConverter(content)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }
}