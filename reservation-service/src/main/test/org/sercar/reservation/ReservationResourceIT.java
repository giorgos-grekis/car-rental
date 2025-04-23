package org.sercar.reservation;

import io.quarkus.test.junit.QuarkusIntegrationTest;

// To have native mode tests also execute, we have to execute.
// ./mvnw verify -Pnative

@QuarkusIntegrationTest
public class ReservationResourceIT extends ReservationResourceTest {
}
