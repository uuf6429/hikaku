package de.codecentric.hikaku.converters.spring.matrixparameters

import de.codecentric.hikaku.converters.spring.SpringConverter
import de.codecentric.hikaku.endpoints.Endpoint
import de.codecentric.hikaku.endpoints.HttpMethod.*
import de.codecentric.hikaku.endpoints.MatrixParameter
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

class SpringConverterMatrixParameterTest {

    @Nested
    @WebMvcTest(MatrixParameterNamedByVariableController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class MatrixParameterNamedByVariableTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `matrix parameter name defined by variable name`() {
            //given
            val specification: Set<Endpoint> = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        matrixParameters = setOf(
                            MatrixParameter("tag", true)
                        )
                ),
                Endpoint("/todos", OPTIONS),
                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        matrixParameters = setOf(
                                MatrixParameter("tag", true)
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
    @WebMvcTest(MatrixParameterNamedByValueAttributeController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class MatrixParameterNamedByValueAttributeTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `matrix parameter name defined by attribute 'value'`() {
            //given
            val specification: Set<Endpoint> = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        matrixParameters = setOf(
                            MatrixParameter("tag", true)
                        )
                ),
                Endpoint("/todos", OPTIONS),
                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        matrixParameters = setOf(
                            MatrixParameter("tag", true)
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
    @WebMvcTest(MatrixParameterNamedByNameAttributeController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class MatrixParameterNamedByNameAttributeTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `matrix parameter name defined by attribute 'name'`() {
            //given
            val specification: Set<Endpoint> = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        matrixParameters = setOf(
                            MatrixParameter("tag", true)
                        )
                ),
                Endpoint("/todos", OPTIONS),
                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        matrixParameters = setOf(
                            MatrixParameter("tag", true)
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
    @WebMvcTest(MatrixParameterHavingBothNameAndValueAttributeController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class MatrixParameterHavingBothNameAndValueAttributeTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `both 'value' and 'name' attribute defined for matrix parameter`() {
            assertFailsWith<IllegalStateException> {
                SpringConverter(context).conversionResult
            }
        }
    }

    @Nested
    @WebMvcTest(MatrixParameterOptionalController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class MatrixParameterOptionalTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `matrix parameter optional`() {
            //given
            val specification: Set<Endpoint> = setOf(
                    Endpoint(
                            path = "/todos",
                            httpMethod = GET,
                            matrixParameters = setOf(
                                MatrixParameter("tag", false)
                            )
                    ),
                    Endpoint("/todos", OPTIONS),
                    Endpoint(
                            path = "/todos",
                            httpMethod = HEAD,
                            matrixParameters = setOf(
                                MatrixParameter("tag", false)
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
    @WebMvcTest(MatrixParameterOptionalBecauseOfDefaultValueController::class, excludeAutoConfiguration = [ErrorMvcAutoConfiguration::class])
    inner class MatrixParameterOptionalBecauseOfDefaultValueTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `matrix parameter optional because of a default value`() {
            //given
            val specification: Set<Endpoint> = setOf(
                Endpoint(
                        path = "/todos",
                        httpMethod = GET,
                        matrixParameters = setOf(
                            MatrixParameter("tag", false)
                        )
                ),
                Endpoint("/todos", OPTIONS),
                Endpoint(
                        path = "/todos",
                        httpMethod = HEAD,
                        matrixParameters = setOf(
                            MatrixParameter("tag", false)
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
    @WebMvcTest(MatrixParameterOnDefaultErrorEndpointController::class)
    inner class MatrixParameterOnDefaultErrorEndpointTest {
        @Autowired
        lateinit var context: ConfigurableApplicationContext

        @Test
        fun `matrix parameter is not available in default error endpoints`() {
            //given
            val specification: Set<Endpoint> = setOf(
                    Endpoint(
                            path = "/todos",
                            httpMethod = GET,
                            matrixParameters = setOf(
                                    MatrixParameter("tag", true)
                            )
                    ),
                    Endpoint("/todos", OPTIONS),
                    Endpoint(
                            path = "/todos",
                            httpMethod = HEAD,
                            matrixParameters = setOf(
                                    MatrixParameter("tag", true)
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