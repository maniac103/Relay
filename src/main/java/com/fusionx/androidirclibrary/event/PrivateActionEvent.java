package com.fusionx.androidirclibrary.event;

public class PrivateActionEvent extends PrivateEvent {
    public PrivateActionEvent(String action, String recipientNick, final boolean isNew) {
        super(action, recipientNick, isNew);
    }
}
