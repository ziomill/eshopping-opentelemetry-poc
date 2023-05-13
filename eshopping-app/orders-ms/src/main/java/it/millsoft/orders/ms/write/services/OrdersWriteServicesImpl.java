package it.millsoft.orders.ms.write.services;

import com.eventstore.dbclient.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.millsoft.orders.ms.commons.Utils;
import it.millsoft.orders.ms.write.events.EventType;
import it.millsoft.orders.ms.write.events.OrderPlacedEvent;
import it.millsoft.orders.ms.write.exceptions.CantAppendEventToStreamException;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Service
public class OrdersWriteServicesImpl implements OrdersWriteServices {

    private static Logger LOGGER = LoggerFactory.getLogger(OrdersWriteServicesImpl.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EventStoreDBClient eventStoreDBClient;

    @Override
    public UUID placeOrder(URI lraId,
                           UUID orderId,
                           String customerEmail,
                           Map<String, Integer> bookedProducts) {
        // The Event
        OrderPlacedEvent event = new OrderPlacedEvent(EventType.ORDER_PLACED,
                                                      lraId,
                                                      orderId,
                                                      customerEmail,
                                                      bookedProducts);

        // Wrap the Event into the needed Wrapper
        EventData eventData = Utils.toEventData(mapper,
                                                event);

        // When appending events to a stream you can supply a STREAM STATE or STREAM REVISION.
        // https://developers.eventstore.com/clients/grpc/appending-events.html#handling-concurrency
        // Append the Event in the Store --> Check the STREAM STATE --> Append only if the current Stream doesn't exist (This must be the first Event for current Stream)
        String streamName = "SALES.ORDER." + orderId;
        AppendToStreamOptions options = AppendToStreamOptions.get()
                .expectedRevision(ExpectedRevision.noStream());
        try {
            WriteResult wResult = eventStoreDBClient.appendToStream(streamName,
                                                                    options,
                                                                    eventData).get();

            LOGGER.info(wResult.toString());

        } catch (Exception ex) {
            String msg = String.format("An ERROR has occurred while appending the EVENT %s to the Store. EVENT DATA: %s - ERROR MESSAGE: %s",
                                       event.getEventType().name(),
                                       event,
                                       ex.getMessage());
            Pair<UUID,String> errorData = Utils.enrichErrorMessage(msg);
            LOGGER.error(errorData.getRight(),ex);
            throw new CantAppendEventToStreamException(errorData.getRight(),
                                                       errorData.getLeft());
        }

        // Return the Event Id
        return eventData.getEventId();
    }



    @Override
    public UUID compensatePlaceOrder(URI lraId) {
//        // Get data from the MAIN EVENT
//        OrderEvent mainEvent = getMainEvent(lraId);
//        // Add the COMPENSATION EVENT
//        String eventId = addEvent(OrderEventType.ORDER_PLACING_COMPENSATED,
//                mainEvent.getStreamId(),
//                lraId,
//                mainEvent.getEventBody().getCustomerEmail(),
//                mainEvent.getEventBody().getBookedItems());
//        return eventId;
        return null;
    }

    @Override
    public UUID completePlaceOrder(URI lraId) {
//        // Get data from the MAIN EVENT
//        OrderEvent mainEvent = getMainEvent(lraId);
//        // Add the COMPLETION EVENT
//        String eventId = addEvent(OrderEventType.ORDER_PLACING_COMPLETED,
//                mainEvent.getStreamId(),
//                lraId,
//                mainEvent.getEventBody().getCustomerEmail(),
//                mainEvent.getEventBody().getBookedItems());
//        return eventId;
        return null;
    }

//    private OrderEvent getMainEvent(URI lraId) {
//        // Get data from the MAIN Event
//        Optional<OrderEvent> optMainEvent = orderEventsRepository.findOrderEventByEventTypeAndLraId(OrderEventType.ORDER_PLACING_COMPLETED,lraId);
//        if(optMainEvent.isEmpty()) {
//            // Main Event not found --> This is an Inconsistent Scenario that should never happen
//            UUID errorUUID = UUID.randomUUID();
//            String msg = String.format("The MAIN EVENT associated with the LRA_ID=%s has not been found. This scenario should never happen - Error UUID: %s",
//                    lraId,
//                    errorUUID);
//            LOGGER.warn(msg);
//            throw new EventNotFoundException(msg, errorUUID);
//        }
//        // Main Event
//        OrderEvent mainEvent = optMainEvent.get();
//        LOGGER.info("The MAIN EVENT having - {} - has been found",mainEvent);
//        return mainEvent;
//    }
//
//    private String addEvent(OrderEventType eventType,
//                            String orderCode,
//                            URI lraId,
//                            String customerEmail,
//                            Map<String, Integer> bookedProducts) {
//        // Build the Event
//        OrderEvent event = new OrderEvent(orderCode,
//                eventType,
//                OffsetDateTime.now(),
//                new OrderEventBody(lraId,customerEmail,bookedProducts));
//        try {
//            // Save the Event
//            event = orderEventsRepository.save(event);
//            LOGGER.info("The EVENT having - {} - has been added with success",event);
//        } catch (DuplicateKeyException ex) {
//            // Duplicated Event --> The Idempotency key "LRA_ID - EVENT_TYPE" has been violated
//            UUID errorUUID = UUID.randomUUID();
//            String msg = String.format("An EVENT of type=%s associated with the LRA_ID=%s already exists. The EVENT %s has not been added - Error UUID: %s",
//                    event.getEventType(),
//                    event.getEventBody().getLraId(),
//                    event,
//                    errorUUID);
//            LOGGER.warn(msg);
//            throw new DuplicatedEventException(msg, errorUUID);
//        }
//        return event.getEventId();
//    }

}
