package it.millsoft.orders.ms.write.services;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

public interface OrdersWriteServices {

    UUID placeOrder(URI lraId,
                    UUID orderId,
                    String customerEmail,
                    Map<String,Integer> bookedProducts);
    UUID compensatePlaceOrder(URI lraId);
    UUID completePlaceOrder(URI lraId);
}
