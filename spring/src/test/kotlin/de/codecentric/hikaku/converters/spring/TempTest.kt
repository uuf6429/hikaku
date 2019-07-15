package de.codecentric.hikaku.converters.spring

import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import de.codecentric.hikaku.endpoints.schemas.Array
import de.codecentric.hikaku.endpoints.schemas.Integer
import de.codecentric.hikaku.endpoints.schemas.Object
import de.codecentric.hikaku.endpoints.schemas.String
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Min

@SpringBootApplication
open class DummyApp

data class TodoInput(val name: kotlin.String, val tags: List<kotlin.String>)
data class TodoOutput(@Min(1) val id: Int, val name: kotlin.String, val tags: List<kotlin.String>)

@RestController
open class ControllerUnderTest {
    @GetMapping("/todos")
    fun todos(): List<TodoOutput> = listOf(TodoOutput(1, "test", emptyList()))

    @PostMapping("/todos")
    fun todos(
            @Suppress("UNUSED_PARAMETER")
            @RequestBody todos: @Min(1) List<TodoInput>
    ) = null
}

@WebMvcTest(ControllerUnderTest::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
class SpringSchemaTest {
    @Autowired
    lateinit var context: ConfigurableApplicationContext

    @Test
    fun `exhaustive-ish test`() {
        //given
        val specification: Set<Endpoint> = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        produces = mapOf(APPLICATION_JSON_UTF8_VALUE to null)
                ),
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        produces = mapOf(APPLICATION_JSON_UTF8_VALUE to Array(
                                Object(
                                        mapOf(
                                                "id" to Integer(1, null),
                                                "name" to String(null, null),
                                                "tags" to Array(String(null, null), null, null)
                                        )
                                ),
                                null,
                                null
                        ))
                ),

                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        consumes = mapOf(APPLICATION_JSON_UTF8_VALUE to null)
                ),
                Endpoint(
                        path = "/todos",
                        httpMethod = POST,
                        consumes = mapOf(APPLICATION_JSON_UTF8_VALUE to Array(
                                Object(
                                        mapOf(
                                                "name" to String(null, null),
                                                "tags" to Array(String(null, null), null, null)
                                        )
                                ),
                                1,
                                null
                        ))
                ),

                Endpoint("/todos", OPTIONS)
        )

        //when
        val implementation = SpringConverter(context)

        //then
        assertThat(implementation.conversionResult).containsExactlyInAnyOrderElementsOf(specification)
    }
}