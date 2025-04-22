package org.sercar.reservation.rest;


import io.quarkus.logging.Log;
import io.smallrye.graphql.client.GraphQLClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestQuery;
import org.sercar.reservation.inventory.Car;
import org.sercar.reservation.inventory.GraphQLInventoryClient;
import org.sercar.reservation.inventory.InventoryClient;
import org.sercar.reservation.rental.Rental;
import org.sercar.reservation.rental.RentalClient;
import org.sercar.reservation.reservation.Reservation;
import org.sercar.reservation.reservation.ReservationsRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationResource {

    private final ReservationsRepository reservationsRepository;
    private final InventoryClient inventoryClient;
    private final RentalClient rentalClient;

//    public ReservationResource(ReservationsRepository reservations,
//                               InventoryClient inventoryClient,
//                               @RestClient RentalClient rentalClient) {
//        this.reservationsRepository = reservations;
//        this.inventoryClient = inventoryClient;
//        this.rentalClient = rentalClient;
//    }

    public ReservationResource(ReservationsRepository reservations,
                               @GraphQLClient("inventory") GraphQLInventoryClient inventoryClient,
                               @RestClient RentalClient rentalClient) {
        this.reservationsRepository = reservations;
        this.inventoryClient = inventoryClient;
        this.rentalClient = rentalClient;
    }

    @GET
    @Path("availability")
    public Collection<Car> availability(@RestQuery LocalDate startDate,
                                        @RestQuery LocalDate endDate) {
        // obtain all cars from inventory
        List<Car> availableCars = inventoryClient.allCars();

        // create a map from id to car
        Map<Long, Car> carsById = new HashMap<>();
        for (Car car : availableCars) {
            carsById.put(car.getId(), car);
        }

        // get all current reservations
        List<Reservation> reservations = reservationsRepository.findAll();
        // for each reservation, remove the car from the map
        for (Reservation reservation : reservations) {
            if (reservation.isReserved(startDate, endDate)) {
                carsById.remove(reservation.getCarId());
            }
        }

        return carsById.values();
    }


    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Reservation make(Reservation reservation) {
        Reservation result = reservationsRepository.save(reservation);
        // this is just a dummy value
        String userId = "x";

        if (reservation.getStartDate().equals(LocalDate.now())) {
            Rental rental =
                    rentalClient.start(userId, result.getId());
            Log.info("Successfully started rental " + rental);
        }

        return result;
    }

}
