package com.example.waleed.audioandmore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    Button btnRecordActivity, btnDLNAActivity;
    private Button start;
    private TextView output;
    private OkHttpClient client;
    private static final String LOG_TAG = "Main";
    public static String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path = getExternalCacheDir().getAbsolutePath();

        btnRecordActivity = findViewById(R.id.btnRecordActivity);
        btnDLNAActivity = findViewById(R.id.btnDLNAActivity);
        btnRecordActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent (MainActivity.this, RecordActivity.class);
                startActivity(myIntent);
            }
        });
        btnDLNAActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent (MainActivity.this, DLNADevicesActivity.class);
                startActivity(myIntent);
            }
        });


        start = (Button) findViewById(R.id.btnWebSocket);
        output = (TextView) findViewById(R.id.txtOutput);
        client = new OkHttpClient();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

    }

    public final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        private TextView output;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            JSONObject requestObject = new JSONObject();
            JSONArray params = new JSONArray();

            try {
                requestObject.put("method", "getPowerStatus");
                requestObject.put("id", "50");
                requestObject.put("version", "1.1");
                requestObject.put("params", params);

            }catch (JSONException jsonException){
                Log.e(LOG_TAG,jsonException.toString());
            }

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

//            webSocket.send(getEqualizer);
            webSocket.send(getVolumeInformation);
//            webSocket.send(getEqualizer);
            Log.e(LOG_TAG, requestObject.toString());
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));

            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");



        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving : " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        public void onMessage(WebSocket webSocket, JSONObject result){
            output("Receiving Json: " + result.toString());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }

    }

    private void start() {
        Request request = new Request.Builder().url("ws://192.168.137.124:54480/sony/audio").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }
}
