package org.sercar.reservation;


import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sercar.reservation.reservation.Reservation;
import org.sercar.reservation.reservation.ReservationsRepository;

import java.time.LocalDate;

//@DisabledOnIntegrationTest(forArtifactTypes = DisabledOnIntegrationTest.ArtifactType.NATIVE_BINARY)
@QuarkusTest
public class ReservationRepositoryTest {

    @Inject
    ReservationsRepository repository;

    @Test
    public void testCreateReservation() {
        Reservation reservation = new Reservation();
        var startDay = LocalDate.now().plusDays(5);
        reservation.setStartDate(startDay);

        var endDay = LocalDate.now().plusDays(12);
        reservation.setEndDate(endDay);

        reservation.setCarId(384L);

        repository.save(reservation);

        Assertions.assertNotNull(reservation.getId());
        Assertions.assertTrue(repository.findAll().contains(reservation));

    }
}
