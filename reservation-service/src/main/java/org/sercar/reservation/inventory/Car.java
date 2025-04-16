package org.sercar.reservation.inventory;

import lombok.Data;


@Data
public class Car {

//    public Long id;
//    public String licensePlateNumber;
//    public String manufacturer;
//    public String model;

    private Long id;
    private String licensePlateNumber;
    private String manufacturer;
    private String model;



    public Car(Long id, String licensePlateNumber,
               String manufacturer, String model) {
        this.id= id;
        this.licensePlateNumber = licensePlateNumber;
        this.manufacturer = manufacturer;
        this.model = model;
    }
}
