package com.example.waleed.audioandmore;

import android.icu.util.Output;
import android.widget.TextView;

import org.w3c.dom.Text;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by Waleed on 03/05/2018.
 */

public final class WebSocketConn extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private TextView output;
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        webSocket.send("Hello, it's SSaurel !");
        webSocket.send("What's up ?");
        webSocket.send(ByteString.decodeHex("deadbeef"));
        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");

    }

//    @Override
//    public void onMessage(WebSocket webSocket, String text) {
//        output("Receiving : " + text);
//    }
//
//    @Override
//    public void onMessage(WebSocket webSocket, ByteString bytes) {
//        output("Receiving bytes : " + bytes.hex());
//    }
//
//    @Override
//    public void onClosing(WebSocket webSocket, int code, String reason) {
//        webSocket.close(NORMAL_CLOSURE_STATUS, null);
//        output("Closing : " + code + " / " + reason);
//    }
//
//    @Override
//    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//        output("Error : " + t.getMessage());
//    }
}
