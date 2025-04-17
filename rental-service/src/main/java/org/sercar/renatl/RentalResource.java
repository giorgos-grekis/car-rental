package org.sercar.renatl;


import io.quarkus.logging.Log;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Path("/rental")
public class RentalResource {

    private final AtomicLong id = new AtomicLong(0);

    @Path("/start/{userId}/{reservationId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Rental start(String userId,
                        Long reservationId) {
        // You could also instantiate an org.jboss.logging.Logger instance instead, but using static methods is simpler
        Log.infof("Starting rental for user %s, reservation %d"
                , userId, reservationId);
        return new Rental (id.incrementAndGet(), userId, reservationId,
                LocalDate.now());
    }
}
