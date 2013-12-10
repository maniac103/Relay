package com.fusionx.androidirclibrary.event;

public class DisconnectEvent extends ServerEvent {

    public boolean retryPending;

    public DisconnectEvent(String message, final boolean retryPending) {
        super(message);
        this.retryPending = retryPending;
    }
}
