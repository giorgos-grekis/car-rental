package org.sercar.reservation.reservation;


import lombok.Data;

import java.time.LocalDate;

@Data
public class Reservation {

    private Long id;
    private Long carId;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Check if the given duration overlaps with this reservation
     * @return true if the dates overlap with the reservation, false
     * otherwise
     */

    public boolean isReserved(LocalDate startDate, LocalDate endDate) {
        return (!this.endDate.isBefore(startDate) ||

                !this.startDate.isAfter(endDate));
    }

}
