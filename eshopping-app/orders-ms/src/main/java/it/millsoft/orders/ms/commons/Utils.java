package it.millsoft.orders.ms.commons;

import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventDataBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.millsoft.orders.ms.write.events.OrderPlacedEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.UUID;

public class Utils {

    public static Pair<UUID, String> enrichErrorMessage(String message) {
        UUID errorUUID = UUID.randomUUID();
        String enrichedErrorMessage = String.format(message + " ----- Error UUID: %s",
                                                    errorUUID);
        return Pair.of(errorUUID, enrichedErrorMessage);
    }

    public static EventData toEventData(ObjectMapper mapper,
                                         OrderPlacedEvent event) {
        try {
            byte[] serializedEvent = mapper.writeValueAsBytes(event);
            EventData eventData = EventDataBuilder.json(event.getEventType().name(),
                                                        serializedEvent).build();

            return eventData;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
