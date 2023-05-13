package it.millsoft.orders.ms.write.events;

import lombok.Getter;
import lombok.ToString;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString
public class OrderPlacedEvent extends OrderEvent {

    private URI lraId;
    private UUID orderId;
    private String customerEmail;
    private Map<String, Integer> bookedProducts;

    //    @Builder
    public OrderPlacedEvent(EventType eventType,
                            URI lraId,
                            UUID orderId,
                            String customerEmail,
                            Map<String, Integer> bookedProducts) {
        super(eventType);
        this.lraId = lraId;
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.bookedProducts = bookedProducts;
    }

}
