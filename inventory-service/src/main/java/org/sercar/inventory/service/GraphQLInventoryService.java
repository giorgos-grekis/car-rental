package org.sercar.inventory.service;

import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;
import org.sercar.inventory.database.CarInventory;
import org.sercar.inventory.model.Car;

import java.util.List;
import java.util.Optional;

@GraphQLApi
public class GraphQLInventoryService {

    @Inject
    CarInventory inventory;

    @Query
    public List<Car> cars() {
        return inventory.getCars();
    }

    @Mutation
    public Car register(Car car) {
        Long carId = CarInventory.ids.incrementAndGet();
        car.setId(carId);
        inventory.getCars().add(car);
        return car;
    }

    @Mutation
    public boolean remove(String licensePlateNumber) {
        List<Car> cars = inventory.getCars();
        Optional<Car> toBeRemoved = cars.stream()
                .filter(car -> car.getLicensePlateNumber()
                        .equals(licensePlateNumber))
                .findFirst();

//        if(toBeRemoved.isPresent()) {
//            return cars.remove(toBeRemoved.get());
//        }
//        else {
//            return false;
//        }
        return toBeRemoved.map(cars::remove).orElse(false);
    }
}
