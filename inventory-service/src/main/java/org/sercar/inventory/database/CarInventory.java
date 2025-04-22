package org.sercar.inventory.database;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.sercar.inventory.model.Car;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

// turns this class into a CDI bean that uses the ApplicationScoped
@ApplicationScoped
public class CarInventory {

    private List<Car> cars;

    public static final AtomicLong ids = new AtomicLong();

    @PostConstruct
    void initialize() {
        cars = new CopyOnWriteArrayList<>();
        initialData();
    }

    public List<Car> getCars() {
        return cars;
    }

    private void initialData() {
        Car mazda = new Car();
        mazda.setId(ids.incrementAndGet());
        mazda.setManufacturer("Mazda");
        mazda.setModel("Mazda");
        mazda.setLicensePlateNumber("ABC123");

        cars.add(mazda);

        Car ford = new Car();
        ford.setId(ids.incrementAndGet());
        ford.setManufacturer("Ford");
        ford.setModel("Mustang");
        ford.setLicensePlateNumber("XYZ987");

        cars.add(ford);
    }
}
