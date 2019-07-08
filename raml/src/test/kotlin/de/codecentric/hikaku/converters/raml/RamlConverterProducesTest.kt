package de.codecentric.hikaku.converters.raml

import de.codecentric.hikaku.converters.EndpointConverterException
import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.test.assertFailsWith

class RamlConverterProducesTest {

    @Test
    fun `no media type`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/no_media_type.raml").toURI())

        //when
        assertFailsWith<EndpointConverterException> {
            RamlConverter(file).conversionResult
        }
    }

    @Test
    fun `single default media type`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/single_default_media_type.raml").toURI())

        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        produces = mapOf(
                                "application/json" to null
                        )
                )
        )

        //when
        val implementation = RamlConverter(file).conversionResult

        //then
        assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `multiple default media types`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/multiple_default_media_types.raml").toURI())

        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        produces = mapOf(
                                "application/json" to null,
                                "application/xml" to null
                        )
                )
        )

        //when
        val implementation = RamlConverter(file).conversionResult

        //then
        assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `single method declaration`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/single_method_declaration.raml").toURI())

        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        produces = mapOf(
                                "text/plain" to null
                        )
                )
        )

        //when
        val implementation = RamlConverter(file).conversionResult

        //then
        assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `multiple method declarations`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/multiple_method_declarations.raml").toURI())

        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        produces = mapOf(
                                "application/json" to null,
                                "application/xml" to null
                        )
                )
        )

        //when
        val implementation = RamlConverter(file).conversionResult

        //then
        assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `method declaration overwrites default`() {
        //given
        val file = Paths.get(this::class.java.classLoader.getResource("produces/method_declaration_overwrites_default.raml").toURI())

        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        produces = mapOf(
                                "text/plain" to null
                        )
                )
        )

        //when
        val implementation = RamlConverter(file).conversionResult

        //then
        assertThat(implementation).containsExactlyInAnyOrderElementsOf(specification)
    }
}