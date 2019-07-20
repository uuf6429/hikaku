package de.codecentric.hikaku.converters.spring.pathparameters

import de.codecentric.hikaku.converters.spring.SpringConverter
import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import de.codecentric.hikaku.endpoints.PathParameter
import de.codecentric.hikaku.endpoints.schemas.Array
import de.codecentric.hikaku.endpoints.schemas.Boolean
import de.codecentric.hikaku.endpoints.schemas.Integer
import de.codecentric.hikaku.endpoints.schemas.Object
import de.codecentric.hikaku.endpoints.schemas.String
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.MediaType.TEXT_HTML_VALUE
import kotlin.test.assertFailsWith

class SpringConverterPathParameterTest {

    @Nested
    @WebMvcTest(PathParameterNamedByVariableController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class PathParameterNamedByVariableTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `path parameter name defined by variable name`() {
            //given
            val specification: Set<Endpoint> = setOf(
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = GET,
                            pathParameters = setOf(
                                PathParameter("id")
                            )
                    ),
                    Endpoint("/todos/{id}", OPTIONS),
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = HEAD,
                            pathParameters = setOf(
                                PathParameter("id")
                            )
                    )
            )

            //when
            val implementation = SpringConverter(context)

            //then
            assertThat(implementation.conversionResult).containsExactlyInAnyOrderElementsOf(specification)
        }
    }

    @Nested
    @WebMvcTest(PathParameterNamedByValueAttributeController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class PathParameterNamedByValueAttributeTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `path parameter name defined by 'value' attribute`() {
            //given
            val specification: Set<Endpoint> = setOf(
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = GET,
                            pathParameters = setOf(
                                    PathParameter("id")
                            )
                    ),
                    Endpoint("/todos/{id}", OPTIONS),
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = HEAD,
                            pathParameters = setOf(
                                    PathParameter("id")
                            )
                    )
            )

            //when
            val implementation = SpringConverter(context)

            //then
            assertThat(implementation.conversionResult).containsExactlyInAnyOrderElementsOf(specification)
        }
    }

    @Nested
    @WebMvcTest(PathParameterNamedByNameAttributeController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class PathParameterNamedByNameAttributeTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `path parameter name defined by 'name' attribute`() {
            //given
            val specification: Set<Endpoint> = setOf(
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = GET,
                            pathParameters = setOf(
                                    PathParameter("id")
                            )
                    ),
                    Endpoint("/todos/{id}", OPTIONS),
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = HEAD,
                            pathParameters = setOf(
                                    PathParameter("id")
                            )
                    )
            )

            //when
            val implementation = SpringConverter(context)

            //then
            assertThat(implementation.conversionResult).containsExactlyInAnyOrderElementsOf(specification)
        }
    }

    @Nested
    @WebMvcTest(PathParameterHavingBothValueAndNameAttributeController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class PathParameterHavingBothValueAndNameAttributeTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `path parameter name defined by both 'value' and 'name' attribute`() {
            assertFailsWith<IllegalStateException> {
                SpringConverter(context).conversionResult
            }
        }
    }

    @Nested
    @WebMvcTest(PathParameterSupportedForOptionsIfExplicitlyDefinedController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class PathParameterSupportedForOptionsIfExplicitlyDefinedTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `path parameter are supported for OPTIONS if defined explicitly`() {
            //given
            val specification: Set<Endpoint> = setOf(
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = OPTIONS,
                            pathParameters = setOf(
                                    PathParameter("id")
                            )
                    ),
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = HEAD,
                            pathParameters = setOf(
                                    PathParameter("id")
                            )
                    )
            )

            //when
            val implementation = SpringConverter(context)

            //then
            assertThat(implementation.conversionResult).containsExactlyInAnyOrderElementsOf(specification)
        }
    }

    @Nested
    @WebMvcTest(PathParameterOnDefaultErrorEndpointController::class)
    inner class PathParameterOnDefaultErrorEndpointTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `path parameters are not added to default error endpoint`() {
            //given
            val specification: Set<Endpoint> = setOf(
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = GET,
                            pathParameters = setOf(
                                    PathParameter("id")
                            )
                    ),
                    Endpoint("/todos/{id}", OPTIONS),
                    Endpoint(
                            path = "/todos/{id}",
                            httpMethod = HEAD,
                            pathParameters = setOf(
                                    PathParameter("id")
                            )
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = GET,
                            produces = mapOf(APPLICATION_JSON_UTF8_VALUE to Object(mapOf(
                                    "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                    "keys" to Array(Object()),
                                    "size" to Integer(),
                                    "values" to Array(Object())
                            )))
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = POST,
                            produces = mapOf(APPLICATION_JSON_UTF8_VALUE to Object(mapOf(
                                    "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                    "keys" to Array(Object()),
                                    "size" to Integer(),
                                    "values" to Array(Object())
                            )))
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = HEAD,
                            produces = mapOf(APPLICATION_JSON_UTF8_VALUE to null)
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = PUT,
                            produces = mapOf(APPLICATION_JSON_UTF8_VALUE to Object(mapOf(
                                    "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                    "keys" to Array(Object()),
                                    "size" to Integer(),
                                    "values" to Array(Object())
                            )))
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = PATCH,
                            produces = mapOf(APPLICATION_JSON_UTF8_VALUE to Object(mapOf(
                                    "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                    "keys" to Array(Object()),
                                    "size" to Integer(),
                                    "values" to Array(Object())
                            )))
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = DELETE,
                            produces = mapOf(APPLICATION_JSON_UTF8_VALUE to Object(mapOf(
                                    "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                    "keys" to Array(Object()),
                                    "size" to Integer(),
                                    "values" to Array(Object())
                            )))
                    ),
                    Endpoint("/error", OPTIONS),
                    Endpoint(
                            path = "/error",
                            httpMethod = GET,
                            produces = mapOf(TEXT_HTML_VALUE to Object(mapOf(
                                    "view" to Object(),
                                    "model" to Object(mapOf(
                                            "size" to Integer(),
                                            "values" to Array(Object()),
                                            "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                            "keys" to Array(String())
                                    )),
                                    "status" to Object(mapOf(
                                            "value" to Integer(),
                                            "reasonPhrase" to String(),
                                            "name" to String(),
                                            "ordinal" to Integer()
                                    )),
                                    "cleared" to Boolean()
                            )))
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = POST,
                            produces = mapOf(TEXT_HTML_VALUE to Object(mapOf(
                                    "view" to Object(),
                                    "model" to Object(mapOf(
                                            "size" to Integer(),
                                            "values" to Array(Object()),
                                            "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                            "keys" to Array(String())
                                    )),
                                    "status" to Object(mapOf(
                                            "value" to Integer(),
                                            "reasonPhrase" to String(),
                                            "name" to String(),
                                            "ordinal" to Integer()
                                    )),
                                    "cleared" to Boolean()
                            )))
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = HEAD,
                            produces = mapOf(TEXT_HTML_VALUE to null)
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = PUT,
                            produces = mapOf(TEXT_HTML_VALUE to Object(mapOf(
                                    "view" to Object(),
                                    "model" to Object(mapOf(
                                            "size" to Integer(),
                                            "values" to Array(Object()),
                                            "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                            "keys" to Array(String())
                                    )),
                                    "status" to Object(mapOf(
                                            "value" to Integer(),
                                            "reasonPhrase" to String(),
                                            "name" to String(),
                                            "ordinal" to Integer()
                                    )),
                                    "cleared" to Boolean()
                            )))
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = PATCH,
                            produces = mapOf(TEXT_HTML_VALUE to Object(mapOf(
                                    "view" to Object(),
                                    "model" to Object(mapOf(
                                            "size" to Integer(),
                                            "values" to Array(Object()),
                                            "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                            "keys" to Array(String())
                                    )),
                                    "status" to Object(mapOf(
                                            "value" to Integer(),
                                            "reasonPhrase" to String(),
                                            "name" to String(),
                                            "ordinal" to Integer()
                                    )),
                                    "cleared" to Boolean()
                            )))
                    ),
                    Endpoint(
                            path = "/error",
                            httpMethod = DELETE,
                            produces = mapOf(TEXT_HTML_VALUE to Object(mapOf(
                                    "view" to Object(),
                                    "model" to Object(mapOf(
                                            "size" to Integer(),
                                            "values" to Array(Object()),
                                            "entries" to Array(Object(mapOf("key" to Object(), "value" to Object()))),
                                            "keys" to Array(String())
                                    )),
                                    "status" to Object(mapOf(
                                            "value" to Integer(),
                                            "reasonPhrase" to String(),
                                            "name" to String(),
                                            "ordinal" to Integer()
                                    )),
                                    "cleared" to Boolean()
                            )))
                    ),
                    Endpoint("/error", OPTIONS)
            )

            //when
            val implementation = SpringConverter(context)

            //then
            assertThat(implementation.conversionResult).containsExactlyInAnyOrderElementsOf(specification)
        }
    }
}