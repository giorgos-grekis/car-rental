package org.sercar.inventory.grpc;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.sercar.inventiry.model.CarResponse;
import org.sercar.inventiry.model.InsertCarRequest;
import org.sercar.inventiry.model.RemoveCarRequest;
import org.sercar.inventory.database.CarInventory;
import org.sercar.inventory.model.Car;

import java.util.Optional;


/**
 * If your IDE cannot find the generated classes, you
 * need to mark the target/generated-sources/grpc directory
 * as sources (or generated sources).
 */


@GrpcService
public class GrpcInventoryService implements org.sercar.inventiry.model.InventoryService {

    @Inject
    CarInventory inventory;

    // add "Multi" because grpc add is stream
    @Override
    public Multi<CarResponse> add (Multi<InsertCarRequest> requests) {
        return requests
                .map( request -> {
                            Car car = new Car();
                            var licensePlateNumber = request.getLicensePlateNumber();
                            car.setLicensePlateNumber(licensePlateNumber);
                            var manufacturer = request.getManufacturer();
                            car.setManufacturer(manufacturer);
                            var model = request.getModel();
                            return  car;
                        })
                .onItem().invoke(car -> {
                    Log.info("Persisting " + car);
                    inventory.getCars().add(car);
                }).map(car -> CarResponse.newBuilder()
                        .setLicensePlateNumber(car.getLicensePlateNumber())
                        .setManufacturer(car.getManufacturer())
                        .setModel(car.getModel())
                        .setId(car.getId())
                        .build());
//        Car car = new Car();
//        var licensePlateNumber = request.getLicensePlateNumber();
//        car.setLicensePlateNumber(licensePlateNumber);
//
//        var manufacturer = request.getManufacturer();
//        car.setManufacturer(manufacturer);
//
//        var model = request.getModel();
//        car.setModel(model);
//
//        var id = CarInventory.ids.incrementAndGet();
//        car.setId(id);
//        Log.info("Persisting " + car);
//
//        inventory.getCars().add(car);
//
//
//        return null;
    }

    @Override
    public Uni<CarResponse> remove(RemoveCarRequest request) {
        Optional<Car> optionalCar = inventory.getCars().stream()
                .filter(car -> request.getLicensePlateNumber().equals(car.getLicensePlateNumber()))
                .findFirst();


        if (optionalCar.isPresent()) {
            Car removeCar = optionalCar.get();
            inventory.getCars().remove(removeCar);
            return Uni.createFrom().item(CarResponse.newBuilder()
                    .setLicensePlateNumber(removeCar.getLicensePlateNumber())
                    .setManufacturer(removeCar.getManufacturer())
                    .setModel(removeCar.getModel())
                    .setId(removeCar.getId())
                    .build()
            );
        }

        return Uni.createFrom().nullItem();
    }
}
