package com.example.testing;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("person")
public final class PersonResource {

    private final PersonService personService;

    @Inject
    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @POST
    public Response createPerson(Person person) {
        return Response.status(Response.Status.OK)
            .entity(personService.create(person))
            .build();
    }
}
