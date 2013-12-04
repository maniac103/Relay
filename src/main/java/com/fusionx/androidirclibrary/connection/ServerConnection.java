/*
HoloIRC - an IRC client for Android

Copyright 2013 Lalit Maganti

This file is part of HoloIRC.

HoloIRC is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

HoloIRC is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with HoloIRC. If not, see <http://www.gnu.org/licenses/>.
*/

package com.fusionx.androidirclibrary.connection;

import com.fusionx.androidirclibrary.Server;
import com.fusionx.androidirclibrary.ServerConfiguration;
import com.fusionx.androidirclibrary.misc.InterfaceHolders;

import android.os.Handler;

public class ServerConnection extends Thread {

    private final Server mServer;

    private final BaseConnection mConnection;

    private final Handler mHandler;

    ServerConnection(final ServerConfiguration configuration, final Handler handler) {
        mServer = new Server(configuration.getTitle(), this);
        mConnection = new BaseConnection(configuration, mServer);
        mHandler = handler;
    }

    @Override
    public void run() {
        try {
            mConnection.connectToServer();
        } catch (final Exception ex) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    public void disconnectFromServer() {
        final String status = mServer.getStatus();

        if (status.equals(InterfaceHolders.getEventResponses().getConnectedStatus())) {
            mConnection.onDisconnect();
        } else if (isAlive()) {
            interrupt();
            mConnection.closeSocket();
        }
    }

    public Server getServer() {
        return mServer;
    }
}