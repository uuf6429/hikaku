package de.codecentric.hikaku.converters.jaxrs

import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.GET
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JaxRsConverterConsumesTest {

    @Test
    fun `single media type defined on class`() {
        // given
        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        consumes = mapOf(
                                "application/json" to null
                        )
                )
        )

        //when
        val result = JaxRsConverter("test.jaxrs.consumes.singlemediatypeonclass").conversionResult

        //then
        assertThat(result).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `single media type defined on function`() {
        // given
        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        consumes = mapOf(
                                "application/json" to null
                        )
                )
        )

        //when
        val result = JaxRsConverter("test.jaxrs.consumes.singlemediatypeonfunction").conversionResult

        //then
        assertThat(result).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `single media type without request body`() {
        // given
        val specification = setOf(
                Endpoint( "/todos", GET)
        )

        //when
        val result = JaxRsConverter("test.jaxrs.consumes.singlemediatypewithoutrequestbody").conversionResult

        //then
        assertThat(result).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `request body, but no annotation`() {
        // given
        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        consumes = mapOf(
                                "*/*" to null
                        )
                )
        )

        //when
        val result = JaxRsConverter("test.jaxrs.consumes.noannotation").conversionResult

        //then
        assertThat(result).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `no request body, but other annotated parameter`() {
        // given
        val specification = setOf(
                Endpoint("/todos", GET)
        )

        //when
        val result = JaxRsConverter("test.jaxrs.consumes.singlemediatypewithoutrequestbodybutotherannotatedparameter").conversionResult

        //then
        assertThat(result).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `multiple media type defined on class`() {
        // given
        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        consumes = mapOf(
                                "application/json" to null,
                                "application/xml" to null
                        )
                )
        )

        //when
        val result = JaxRsConverter("test.jaxrs.consumes.multiplemediatypesonclass").conversionResult

        //then
        assertThat(result).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `multiple media type defined on function`() {
        // given
        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        consumes = mapOf(
                                "application/json" to null,
                                "application/xml" to null
                        )
                )
        )

        //when
        val result = JaxRsConverter("test.jaxrs.consumes.multiplemediatypesonfunction").conversionResult

        //then
        assertThat(result).containsExactlyInAnyOrderElementsOf(specification)
    }

    @Test
    fun `function declaration overwrites class declaration`() {
        // given
        val specification = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        consumes = mapOf(
                                "application/json" to null,
                                "text/plain" to null
                        )
                )
        )

        //when
        val result = JaxRsConverter("test.jaxrs.consumes.functiondeclarationoverwritesclassdeclaration").conversionResult

        //then
        assertThat(result).containsExactlyInAnyOrderElementsOf(specification)
    }
}