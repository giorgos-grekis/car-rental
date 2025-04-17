package org.sercar.reservation.rental;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;
// ένα interface REST client, δηλαδή ότι θα χρησιμοποιηθεί για να καλέσει ένα εξωτερικό REST API.
@RegisterRestClient(baseUri = "http://localhost:8082")
@Path("/rental")
public interface RentalClient {
    @POST
    @Path("/start/{userId}/{reservationId}")
    Rental start(@RestPath String userId,
                 @RestPath Long reservationId);
}