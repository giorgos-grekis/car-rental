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

    /**
     *     Because we use the Car class as a return value from the
     *     service, Quarkus needs to be able to deserialize it from
     *     JSON documents and construct its instances.
     *     Therefore, it’s necessary to provide a public no-argument constructor in
     *     the Car class.
     */
    public Car() {
    }

    public Car(Long id, String licensePlateNumber,
               String manufacturer, String model) {
        this.id= id;
        this.licensePlateNumber = licensePlateNumber;
        this.manufacturer = manufacturer;
        this.model = model;
    }
}
