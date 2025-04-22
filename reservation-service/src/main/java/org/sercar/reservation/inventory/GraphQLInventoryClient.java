package org.sercar.reservation.inventory;


import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLClientApi(configKey = "inventory")
public interface GraphQLInventoryClient extends InventoryClient {
    @Query("cars")
    List<Car> allCars();
}


// import static io.smallrye.graphql.client.core.Document.document;
// import static io.smallrye.graphql.client.core.Field.field;
// import static io.smallrye.graphql.client.core.Operation.operation;
//Document cars = document(
//        operation("cars",
//                field("id"),
//                field("licensePlateNumber"),
//                field("manufacturer"),
//                field("model")
//        )
//);

//DynamicGraphQLClient client = ...; // obtain a client instance
//Response response = client.executeSync(cars);
//JsonArray carsAsJson = response.getData().asJsonArray();
//// or, automatically deserialize a List of cars:
//List<Car> cars = response.getList(Car.class, "cars");