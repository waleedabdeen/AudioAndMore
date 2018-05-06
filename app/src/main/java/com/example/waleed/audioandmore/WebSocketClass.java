package com.example.waleed.audioandmore;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by Waleed on 06/05/2018.
 */

public class WebSocketClass {
    String LOG_TAG = "WebSocketClass";
    OkHttpClient client;
    String hostURL = "ws://192.168.137.124:54480/sony/audio";
    int param1;
    String requestType;

    public WebSocketClass(String hostURL, String requestType, int param1){
        client = new OkHttpClient();
        this.hostURL = hostURL;
        this.requestType = requestType;
        this.param1 = param1;
    }


    public final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        private TextView output;
        private String requestType;

        public EchoWebSocketListener(String requestType){
            this.requestType = requestType;
        }
        @Override
        public void onOpen(okhttp3.WebSocket webSocket, Response response) {

//            JSONObject requestObject = new JSONObject();
//            JSONArray params = new JSONArray();
//
//            try {
//                requestObject.put("method", "getPowerStatus");
//                requestObject.put("id", "50");
//                requestObject.put("version", "1.1");
//                requestObject.put("params", params);
//
//            }catch (JSONException jsonException){
//                Log.e(LOG_TAG,jsonException.toString());
//            }

            String getInfo = "{\n" +
                    " \"method\":\"getPowerStatus\",\n" +
                    " \"id\":50,\n" +
                    " \"params\":[],\n" +
                    " \"version\":\"1.1\"\n" +
                    "}";
            String getEqualizer  = "{\n" +
                    " \"method\":\"getCustomEqualizerSettings\",\n" +
                    " \"id\":11,\n" +
                    " \"params\":[\n" +
                    "  {\n" +
                    "   \"target\":\"10000HzBandLevel\"\n" +
                    "  }\n" +
                    " ],\n" +
                    " \"version\":\"1.0\"\n" +
                    "}";

            String setEqualizer = "{\n" +
                    " \"method\":\"setCustomEqualizerSettings\",\n" +
                    " \"id\":15,\n" +
                    " \"params\":[\n" +
                    "  {\n" +
                    "   \"settings\":[\n" +
                    "    {\n" +
                    "     \"value\":\"10\",\n" +
                    "     \"target\":\"10000HzBandLevel\"\n" +
                    "    }\n" +
                    "   ]\n" +
                    "  }\n" +
                    " ],\n" +
                    " \"version\":\"1.0\"\n" +
                    "}";

            String getVolumeInformation = "{\n" +
                    " \"method\":\"getVolumeInformation\",\n" +
                    " \"id\":33,\n" +
                    " \"params\":[\n" +
                    "  {\n" +
                    "   \"output\":\"extOutput:zone?zone=1\"\n" +
                    "  }\n" +
                    " ],\n" +
                    " \"version\":\"1.1\"\n" +
                    "}";

            String setAudioVolume = "{\n" +
                    " \"method\":\"setAudioVolume\",\n" +
                    " \"id\":98,\n" +
                    " \"params\":[\n" +
                    "  {\n" +
                    "   \"volume\":\" " + param1 + " \",\n" +
                    "   \"output\":\"extOutput:zone?zone=2\"\n" +
                    "  }\n" +
                    " ],\n" +
                    " \"version\":\"1.1\"\n" +
                    "}";

            switch (requestType){
                case "getVolume":
                    webSocket.send(getVolumeInformation);
                    break;
                case "setVolume":
                    webSocket.send(setAudioVolume);
                    break;
            }
//            webSocket.send(getEqualizer);
//            webSocket.send(setEqualizer);
//            webSocket.send(getEqualizer);
//            Log.e(LOG_TAG, requestObject.toString());
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));
//            webSocket.send(getVolumeInformation);
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");



        }

        @Override
        public void onMessage(okhttp3.WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }
        @Override
        public void onMessage(okhttp3.WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }
        public void onMessage(okhttp3.WebSocket webSocket, JSONObject result){
            output("Receiving Json: " + result.toString());
        }

        @Override
        public void onClosing(okhttp3.WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }


    }

    public void start() {
        Request request = new Request.Builder().url(hostURL).build();
        EchoWebSocketListener listener = new EchoWebSocketListener(requestType);
        okhttp3.WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                output.setText(output.getText().toString() + "\n\n" + txt);
//            }
//        });
    }
    private void output (final JSONObject jsonObject){
        try {
            JSONArray result =  jsonObject.getJSONArray("result");
            JSONArray internalResult = result.getJSONArray(0);
            JSONObject volumeInfo = internalResult.getJSONObject(0);
            int volumeLevel = volumeInfo.getInt("volume");
            int minVolumeLevel = volumeInfo.getInt("minVolume");
            int maxVolumeLevel = volumeInfo.getInt("maxVolume");

        }catch (JSONException e){
            Log.e("WebSocketClass - Error", e.toString());
        }

    }
}
