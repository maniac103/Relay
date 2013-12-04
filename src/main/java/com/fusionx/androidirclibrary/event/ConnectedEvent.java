package com.fusionx.androidirclibrary.event;

import com.fusionx.androidirclibrary.misc.InterfaceHolders;

public class ConnectedEvent extends ServerEvent {

    public ConnectedEvent(final String serverUrl) {
        super(InterfaceHolders.getEventResponses().getOnConnectedMessage(serverUrl));
        //super(String.format(context.getString(R.string.parser_connected), serverUrl));
    }
}