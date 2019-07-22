package de.codecentric.hikaku.temp

import de.codecentric.hikaku.converters.spring.SpringConverter
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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.Min
import kotlin.test.assertEquals

@SpringBootApplication
open class DummyApp

data class TodoInput(val name: kotlin.String, val tags: List<kotlin.String>)
data class TodoOutput(@Min(1) val id: Int, val name: kotlin.String, val tags: List<kotlin.String>)
data class Todo3Output(val name: kotlin.String, val content: Todo3ContentOutput, val meta: List<Todo3MetaOutput>?)
data class Todo3ContentOutput(val content: kotlin.String, val excerpt: kotlin.String?)
data class Todo3MetaOutput(val name: kotlin.String, val value: kotlin.String)

@RestController
open class ControllerUnderTest {
    @GetMapping("/todos1")
    fun todos1(): ResponseEntity<List<TodoOutput>> = ResponseEntity(HttpStatus.OK)

    @GetMapping("/todos2")
    fun todos2(): List<TodoOutput> = listOf(TodoOutput(1, "test", emptyList()))

    @GetMapping("/todos3")
    fun todos3(): Todo3Output? = null

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
    fun `test fancy serialization`() {
        assertEquals("String(maxLength = 2)", String(maxLength = 2).toString())
        assertEquals("Array(items = String())", Array(items = String()).toString())
        assertEquals("Object(properties = mapOf(\"name\" to String()))", Object(properties = mapOf("name" to String())).toString())
    }

    @Test
    fun `test spring schema`() {
        //given
        val specification: Set<Endpoint> = setOf(
                Endpoint(
                        path = "/todos1",
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
                        path = "/todos1",
                        httpMethod = HEAD,
                        produces = mapOf(APPLICATION_JSON_UTF8_VALUE to null)
                ),
                Endpoint("/todos1", OPTIONS),

                Endpoint(
                        path = "/todos2",
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
                        path = "/todos2",
                        httpMethod = HEAD,
                        produces = mapOf(APPLICATION_JSON_UTF8_VALUE to null)
                ),
                Endpoint("/todos2", OPTIONS),

                Endpoint(
                        path = "/todos3",
                        httpMethod = GET,
                        produces = mapOf(APPLICATION_JSON_UTF8_VALUE to Object(mapOf(
                                "content" to Object(mapOf("content" to String(), "excerpt" to String(nullable = true))),
                                "meta" to Array(Object(mapOf("name" to String(), "value" to String())), nullable = true),
                                "name" to String()
                        ), nullable = true))
                ),
                Endpoint(
                        path = "/todos3",
                        httpMethod = HEAD,
                        produces = mapOf(APPLICATION_JSON_UTF8_VALUE to null)
                ),
                Endpoint("/todos3", OPTIONS),

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
                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        consumes = mapOf(APPLICATION_JSON_UTF8_VALUE to null)
                ),
                Endpoint("/todos", OPTIONS)
        )

        //when
        val implementation = SpringConverter(context)

        //then
        assertThat(implementation.conversionResult).containsExactlyInAnyOrderElementsOf(specification)
    }
}
