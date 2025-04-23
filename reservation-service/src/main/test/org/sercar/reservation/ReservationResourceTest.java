// more details for quarkus test
// https://mng.bz/5ga1

package org.sercar.reservation;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.DisabledOnIntegrationTest;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sercar.reservation.inventory.Car;
import org.sercar.reservation.inventory.GraphQLInventoryClient;
import org.sercar.reservation.reservation.Reservation;
import org.sercar.reservation.rest.ReservationResource;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ReservationResourceTest {

    @TestHTTPEndpoint(ReservationResource.class) // Δηλώνει ότι θα τεστάρουμε το REST endpoint του ReservationResource.
    @TestHTTPResource // Κάνει inject το σωστό URL του endpoint σε μια μεταβλητή.
    URL reservationResource;
    @Test
    public void testReservationIds() {
        Reservation reservation = new Reservation();
        var cardId = 12345L;
        reservation.setId(cardId);

        var startDay = LocalDate.of(2025, 3, 20);
        reservation.setStartDate(startDay);

        var endDay = LocalDate.of(2025, 3, 2);
        reservation.setEndDate(endDay);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when()
                .post(reservationResource)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }


    @TestHTTPEndpoint(ReservationResource.class)
    @TestHTTPResource("availability") // {baseURL}/reservation/availability
    URL availability;;

    @DisabledOnIntegrationTest(forArtifactTypes =
        DisabledOnIntegrationTest.ArtifactType.NATIVE_BINARY)
    @Test
    public void testMakingReservationAndCheckAvailability() {
        GraphQLInventoryClient mock = Mockito.mock(GraphQLInventoryClient.class);
        Car peugeot = new Car(1L, "ABC123", "Peugeot", "406");

        Mockito.when(mock.allCars())
                .thenReturn(Collections.singletonList(peugeot));
        QuarkusMock.installMockForType(mock, GraphQLInventoryClient.class);


        String startDate = "2022-01-01";
        String endDate = "2022-01-10";

        // List available cars for our requested timeslot and choose one
        Car[] cars = RestAssured.given()
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .when().get(availability)
                .then().statusCode(200)
                .extract().as(Car[].class);

        Car car = cars[0];


        // Prepare a Reservation object
        Reservation reservation = new Reservation();
        reservation.setCarId(car.getId());
        reservation.setStartDate(LocalDate.parse(startDate));
        reservation.setEndDate(LocalDate.parse(endDate));




        // Submit the reservation
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post(reservationResource)
                .then().statusCode(200)
                .body("carId", is(car.getId().intValue()));

        // Verify that this car doesn't show as available anymore
        RestAssured.given()
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .when()
                .get(availability)
                .then()
                .statusCode(200)
                .body("findAll { car -> car.getId() == " + car.getId() + "}", hasSize(0));


    }

//    @TestHTTPEndpoint(ReservationResource.class)
//    @TestHTTPResource("availability")
//    URL availability;
//    @DisabledOnIntegrationTest(forArtifactTypes =
//            DisabledOnIntegrationTest.ArtifactType.NATIVE_BINARY)
//    @Test
//    public void testMakingAReservationAndCheckAvailability() {
//        GraphQLInventoryClient mock =
//                Mockito.mock(GraphQLInventoryClient.class);
//
//        Car peugeot = new Car(1L, "ABC123", "Peugeot", "406");
//        Mockito.when(mock.allCars())
//                .thenReturn(Collections.singletonList(peugeot));
//        QuarkusMock.installMockForType(mock,
//                GraphQLInventoryClient.class);
//        String startDate = "2022-01-01";
//        String endDate = "2022-01-10";
//// List available cars for our requested timeslot and choose one
//        Car[] cars = RestAssured.given()
//                .queryParam("startDate", startDate)
//                .queryParam("endDate", endDate)
//                .when().get(availability)
//                .then().statusCode(200)
//                .extract().as(Car[].class);
//        Car car = cars[0];
//// Prepare a Reservation object
//        Reservation reservation = new Reservation();
//        reservation.setCarId(car.getId());
//        reservation.setStartDate(LocalDate.parse(startDate));
//        reservation.setEndDate(LocalDate.parse(endDate));
//// Submit the reservation
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(reservation)
//                .when().post(reservationResource)
//                .then().statusCode(200)
//                .body("carId", is(car.getId().intValue()));
//// Verify that this car doesn't show as available anymore
//        RestAssured.given()
//                .queryParam("startDate", startDate)
//                .queryParam("endDate", endDate)
//                .when().get(availability)
//                .then().statusCode(200)
//                .body("findAll { car -> car.id == " + car.getId() + "}", hasSize(0));
//    }

}
