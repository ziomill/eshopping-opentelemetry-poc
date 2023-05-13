package it.millsoft.orders.ms.write.test.integration;

import com.eventstore.dbclient.WrongExpectedVersionException;
import it.millsoft.orders.ms.write.exceptions.CantAppendEventToStreamException;
import it.millsoft.orders.ms.write.services.OrdersWriteServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@Testcontainers
public class OrdersWriteService_IntegrationTest {

    private static int EVENTSTOREDB_EXT_TCP_PORT = 1113;
    private static int EVENTSTOREDB_HTTP_PORT = 2113;

    @Container
    public static GenericContainer eventStoreDBContainer = new GenericContainer(DockerImageName.parse("eventstore/eventstore:22.10.1-buster-slim"))
            .withCommand("--insecure", "--run-projections=All", "--enable-atom-pub-over-http", "--mem-db")
            .withExposedPorts(EVENTSTOREDB_EXT_TCP_PORT,
                              EVENTSTOREDB_HTTP_PORT)
            .waitingFor(Wait.forHealthcheck());
//            .waitingFor(Wait.forHttp("/gossip")  // curl -X GET http://localhost:2113/gossip
//                                .forStatusCode(200));
//            .withStartupTimeout(Duration.of(120, ChronoUnit.SECONDS));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        Objects.requireNonNull(eventStoreDBContainer);
        registry.add("eventstoredb.uri", () -> {
            String result = String.format("esdb://%s:%d?tls=false",
                                          "localhost",
                                          eventStoreDBContainer.getMappedPort(EVENTSTOREDB_HTTP_PORT));
            return result;
        });
    }

    @Autowired
    private OrdersWriteServices ordersWriteServices;

    @Test
    public void placeOrder_AppendEventToNotExistingStream_ShouldReturnTheAppendedEventId() {
        Assertions.assertDoesNotThrow(
                () -> {
                    // Given
                    URI lraId = URI.create(UUID.randomUUID().toString());
                    UUID orderId = UUID.randomUUID();
                    String customerEmail = "jhon.doe@gmail.com";
                    Map<String, Integer> bookedProducts = Map.of("PRD000001", 1,
                                                                 "PRD000002", 3);
                    // When
                    UUID eventId = ordersWriteServices.placeOrder(lraId,
                                                                  orderId,
                                                                  customerEmail,
                                                                  bookedProducts);

                    // Then
                    Assertions.assertNotNull(orderId);
                }
        );
    }

    @Test
    public void placeOrder_AppendEventToExistingStream_ShouldThrowAnException() {
        Assertions.assertThrows(CantAppendEventToStreamException.class,
                                () -> {
                                    // Given
                                    URI lraId = URI.create(UUID.randomUUID().toString());
                                    UUID orderId = UUID.randomUUID();
                                    String customerEmail = "jhon.doe@gmail.com";
                                    Map<String, Integer> bookedProducts = Map.of("PRD000001", 1,
                                                                                 "PRD000002", 3);
                                    // When
                                    ordersWriteServices.placeOrder(lraId,
                                                                   orderId,
                                                                   customerEmail,
                                                                   bookedProducts);

                                    ordersWriteServices.placeOrder(lraId,
                                                                   orderId,
                                                                   customerEmail,
                                                                   bookedProducts);
                                }
        );
    }

}
