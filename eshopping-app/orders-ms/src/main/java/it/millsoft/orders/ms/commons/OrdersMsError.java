package it.millsoft.orders.ms.commons;

import lombok.Getter;

@Getter
public enum OrdersMsError {

    CANT_APPEND_EVENT_TO_STREAM("/failure/cant-append-event-to-stream");

    private String errorDesc;

    OrdersMsError(String errorDesc) {
        this.errorDesc = errorDesc;
    }


}
