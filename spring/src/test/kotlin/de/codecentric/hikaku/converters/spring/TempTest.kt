package de.codecentric.hikaku.converters.spring

import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import de.codecentric.hikaku.endpoints.schemas.ObjectSchema
import de.codecentric.hikaku.endpoints.schemas.StringSchema
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@SpringBootApplication
open class DummyApp

data class TodoInput(val name: String)
data class TodoOutput(val id: Int, val name: String)

@Controller
open class ControllerUnderTest {
    @GetMapping("/todos")
    fun todos(): List<TodoOutput> = listOf(TodoOutput(1, "test"))

    @PostMapping("/todos")
    fun todos(
            @Suppress("UNUSED_PARAMETER")
            @RequestBody todo: TodoInput
    ) { }
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
                        httpMethod = GET,
                        consumes = emptyMap()
                ),
                Endpoint(
                        path = "/todos",
                        httpMethod = POST,
                        consumes = mapOf(APPLICATION_JSON_UTF8_VALUE to ObjectSchema(mapOf("name" to StringSchema(null, null))))
                ),
                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        consumes = mapOf(APPLICATION_JSON_UTF8_VALUE to ObjectSchema(mapOf("name" to StringSchema(null, null))))
                ),
                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        consumes = emptyMap()
                ),
                Endpoint("/todos", OPTIONS)
        )

        //when
        val implementation = SpringConverter(context)

        //then
        assertThat(implementation.conversionResult).containsExactlyInAnyOrderElementsOf(specification)
    }
}