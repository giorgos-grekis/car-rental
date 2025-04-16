package org.sercar.reservation.inventory;

import java.util.List;
import jakarta.inject.Singleton;

/**
* This annotation is part of the Context and Dependency Injection (CDI)
* specification. It allows us to use this class as the implementation of
* InventoryClient wherever the application needs such implementation
*/

@Singleton
public class InMemoryInventoryClient implements InventoryClient {

    private static final List<Car> ALL_CARS = List.of(
            new Car(1L, "ABC-123", "Toyota", "Corolla"),
            new Car(2L, "ABC-987", "Honda", "Jazz"),
            new Car(3L, "XYZ-123", "Renault", "Clio"),
            new Car(4L, "XYZ-987", "Ford", "Focus")
    );

    @Override
    public List<Car> allCars() {
        return ALL_CARS;
    }

}