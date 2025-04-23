package org.sercar.reservation;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.sercar.reservation.reservation.Reservation;
import org.sercar.reservation.rest.ReservationResource;

import java.net.URL;
import java.time.LocalDate;

import static org.hamcrest.Matchers.notNullValue;

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

}
