package it.millsoft.orders.ms.write.exceptions;

import it.millsoft.orders.ms.commons.OrdersMsError;
import it.millsoft.orders.ms.commons.exceptions.OrdersMsException;

import java.util.UUID;

public class CantAppendEventToStreamException extends OrdersMsException {

    public CantAppendEventToStreamException(String message, UUID errorUUID) {
        super(message,
              OrdersMsError.CANT_APPEND_EVENT_TO_STREAM,
              errorUUID);
    }

}
