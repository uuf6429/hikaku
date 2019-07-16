package de.codecentric.hikaku.converters.openapi

import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.DELETE
import de.codecentric.hikaku.endpoints.HttpMethod.GET
import de.codecentric.hikaku.endpoints.schemas.Array
import de.codecentric.hikaku.endpoints.schemas.String
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class OpenApiConverterProducesTest {

    @Test
    fun `inline declaration`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/produces_inline.yaml").toURI())
        val implementation = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        produces = mapOf("application/json" to Array(String()))
                )
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }

    @Test
    fun `no content-type`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/produces_no_content_type.yaml").toURI())
        val implementation = setOf(
                Endpoint("/todos", DELETE)
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }

    @Test
    fun `response is declared in components section`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/produces_response_in_components.yaml").toURI())
        val implementation = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        produces = mapOf("application/xml" to Array(Array(String())))
                )
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }

    @Test
    fun `produces having a default value`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/produces_with_default.yaml").toURI())
        val implementation = setOf(
                Endpoint(
                        path = "/todos/query",
                        httpMethod = GET,
                        produces = mapOf("application/json" to Array(String()), "text/plain" to String())
                )
        )

        //when
        val specification = OpenApiConverter(file)

        //then
        assertThat(specification.conversionResult).containsExactlyInAnyOrderElementsOf(implementation)
    }
}