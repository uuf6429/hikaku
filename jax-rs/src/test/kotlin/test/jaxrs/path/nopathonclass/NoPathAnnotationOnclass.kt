package test.jaxrs.path.nopathonclass

import javax.ws.rs.GET
import javax.ws.rs.Path

@Suppress("unused")
class NoPathAnnotationOnClass {

    @GET
    @Path("/todos")
    fun todo() { }
}