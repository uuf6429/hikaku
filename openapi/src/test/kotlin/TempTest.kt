package de.codecentric.hikaku.temp

import de.codecentric.hikaku.converters.openapi.OpenApiConverter
import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import de.codecentric.hikaku.endpoints.schemas.Array
import de.codecentric.hikaku.endpoints.schemas.Boolean
import de.codecentric.hikaku.endpoints.schemas.Integer
import de.codecentric.hikaku.endpoints.schemas.Object
import de.codecentric.hikaku.endpoints.schemas.String
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class OpenApiSchemaTest {
    @Test
    fun `test openapi schema`() {
        //given
        @Language("yaml")
        val content = """
            openapi: 3.0.2
            paths:
              /todos:
                get:
                  description: Get all todos
                  responses:
                    '200':
                      description: OK
                      content:
                        application/json:
                          schema:
                            type: array
                            items:
                              ${"$"}ref: '#/components/schemas/Todo'
                post:
                  description: Add a new todo
                  requestBody:
                    content:
                      application/json:
                        schema:
                          ${"$"}ref: '#/components/schemas/Todo'
                  responses:
                    '200':
                      description: OK
            components:
              schemas:
                Todo:
                  type: object
                  properties:
                    id:
                      type: integer
                    extra:
                      type: string
                      nullable: true
                    done:
                      type: boolean
        """.trimIndent()
        val implementation = setOf(
            Endpoint(
                path = "/todos",
                httpMethod = GET,
                produces = mapOf("application/json" to Array(Object(mapOf(
                    "id" to Integer(),
                    "extra" to String(nullable = true),
                    "done" to Boolean()
                ))))
            ),
            Endpoint(
                path = "/todos",
                httpMethod = POST,
                consumes = mapOf("application/json" to Object(mapOf(
                    "id" to Integer(),
                    "extra" to String(nullable = true),
                    "done" to Boolean()
                )))
            )
        )

        //when
        val specification = OpenApiConverter(content)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }
}