package it.millsoft.orders.ms.write.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class OrderEvent {

    private EventType eventType;

}
