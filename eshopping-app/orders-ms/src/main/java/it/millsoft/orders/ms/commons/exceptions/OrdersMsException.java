package it.millsoft.orders.ms.commons.exceptions;

import it.millsoft.orders.ms.commons.OrdersMsError;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class OrdersMsException extends RuntimeException {

    private OrdersMsError error;
    private UUID errorUUID;

    public OrdersMsException(String message,
                             OrdersMsError error,
                             UUID errorUUID) {
        super(message);
        this.error = error;
        this.errorUUID = errorUUID;
    }

}
