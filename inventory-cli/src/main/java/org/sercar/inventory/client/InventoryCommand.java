// https://quarkus.io/guides/command-mode-reference
package org.sercar.inventory.client;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

// in order to import those make sure to add  /target/generated-sources/grpc as "Generated source root"
import org.sercar.inventory.InsertCarRequest;
import org.sercar.inventory.InventoryService;
import org.sercar.inventory.RemoveCarRequest;


// Marks this class as the main class
@QuarkusMain
//  implements QuarkusApplication => Tells Quarkus that this is a Quarkus application
public class InventoryCommand implements QuarkusApplication {
    private static final String USAGE =
            "Usage: inventory <add>|<remove> " +
                    "<license plate number> <manufacturer> <model>";
    @GrpcClient("inventory")
    InventoryService inventory;

    // Overrides the main entry point for the Quarkus application
    @Override
    public int run(String... args) {

        String action =
                args.length > 0 ? args[0] : null;
        if ("add".equals(action) && args.length >= 4) {
            add(args[1], args[2], args[3]);
            return 0;
        } else if ("remove".equals(action) && args.length >= 2) {
            remove(args[1]);
            return 0;
        }
        System.err.println(USAGE);
        return 1;
    }
    public void add(String licensePlateNumber, String manufacturer,
                    String model) {
        inventory.add(InsertCarRequest.newBuilder()
                        .setLicensePlateNumber(licensePlateNumber)
                        .setManufacturer(manufacturer)
                        .setModel(model)
                        .build())
                .onItem().invoke(carResponse ->
                        System.out.println("Inserted new car " + carResponse))
                .await().indefinitely();
    }
    public void remove(String licensePlateNumber) {
        inventory.remove(RemoveCarRequest.newBuilder()
                        .setLicensePlateNumber(licensePlateNumber)
                        .build())
                .onItem().invoke(carResponse ->
                        System.out.println("Removed car " + carResponse))
                .await().indefinitely();
    }
}
