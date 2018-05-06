package com.example.waleed.audioandmore;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class DLNADevicesActivity extends AppCompatActivity {
    Button btnDiscoverDLNA;
    TextView txtDiscoveredDevices;
    Context context = this;
    Activity thisActivity = DLNADevicesActivity.this;

    private String[] devicesArray;
    private ArrayList<Device> devicesList = new ArrayList<>();

    private boolean permissionToWIFI = false;
    private String [] permissions = {Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,Manifest.permission.ACCESS_WIFI_STATE};

    private static final int REQUEST_WIFI_PERMISSION = 553;
    private static final int REQUEST_CHANGE_MULTICAST_WIFI = 455;
    private static final int REQUEST_INTERNET = 400;

    //RecyclerView and its components
    private RecyclerView reViewDevices;
    private static RecyclerView.Adapter devicesAdapter;
    private RecyclerView.LayoutManager devicesLayoutManager;
    private Button btnRefresh;

    private Device weeds;

    String LOG_TAG = "WebSocketClass";
    OkHttpClient client;
    String hostURL = "ws://192.168.137.124:54480/sony/audio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlnadevices);

        checkThePermissionFirst();
        // prepare the recyclerview
        reViewDevices = findViewById(R.id.reViewDevices);
        reViewDevices.setHasFixedSize(true);

        // use a linear layout manager
        devicesLayoutManager = new LinearLayoutManager(this);
        reViewDevices.setLayoutManager(devicesLayoutManager);

        // specify an adapter
        devicesAdapter = new DevicesAdapter(devicesList);
        reViewDevices.setAdapter(devicesAdapter);

        btnDiscoverDLNA = findViewById(R.id.btnDiscoverDLNA);
        btnDiscoverDLNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Button Clicked",Toast.LENGTH_SHORT).show();
//                devicesArray = UPnPDiscovery.discoverDevices(context);
//                Log.e("DLNA", String.valueOf (devicesArray.length));

                DLNAClass dlnaScan = new DLNAClass(getApplicationContext());
                String result = dlnaScan.Discover();
                Log.e("DLNA Discover" , result);
//                devicesList.add(result);
//                for(int i = 0; i< devicesArray.length; i++){
//                    devicesList.add(devicesArray[i]);
//                    Log.e("DLNA", devicesArray[i]);
//
//                }
            }
        });
        client = new OkHttpClient();
        start();

        btnRefresh = findViewById(R.id.refreshBtn);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        weeds = new Device();
        weeds.setLabel("ZR5-Weeds");
        weeds.setMaxVolume(50);
        weeds.setMinVolume(0);
//        weeds.setVolume(10);

        devicesList.add(weeds);

    }

    public void checkThePermissionFirst(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission Status Not Granted", Toast.LENGTH_LONG).show();
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.ACCESS_WIFI_STATE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                        REQUEST_WIFI_PERMISSION);
            }
        } else {
            // Permission has already been granted
            Toast.makeText(context, "Permisssion Has Already been granted", Toast.LENGTH_LONG).show();
        }

        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.CHANGE_WIFI_MULTICAST_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission Status Not Granted", Toast.LENGTH_LONG).show();
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.CHANGE_WIFI_MULTICAST_STATE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.CHANGE_WIFI_MULTICAST_STATE},
                        REQUEST_CHANGE_MULTICAST_WIFI);
            }
        } else {
            // Permission has already been granted
            Toast.makeText(context, "Permisssion Has Already been granted", Toast.LENGTH_LONG).show();
        }

        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission Status Not Granted", Toast.LENGTH_LONG).show();
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.INTERNET},
                        REQUEST_INTERNET);
            }
        } else {
            // Permission has already been granted
            Toast.makeText(context, "Permisssion Has Already been granted", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CHANGE_MULTICAST_WIFI: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(context, "Permission granted YAY", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case REQUEST_WIFI_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(context, "Permission granted YAY", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(context, "Permission granted YAY", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
//
//    public void onStop() {
//        super.onStop();
//        if (mRecorder != null) {
//            mRecorder.release();
//            mRecorder = null;
//        }
//
//        if (mPlayer != null) {
//            mPlayer.release();
//            mPlayer = null;
//        }
//    }

    public final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        private TextView output;
        @Override
        public void onOpen(okhttp3.WebSocket webSocket, Response response) {

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
//            webSocket.send(setEqualizer);
//            webSocket.send(getEqualizer);
            Log.e(LOG_TAG, requestObject.toString());
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));
            webSocket.send(getVolumeInformation);
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");


        }

        @Override
        public void onMessage(okhttp3.WebSocket webSocket, String text) {
            output(text);
        }
        @Override
        public void onMessage(okhttp3.WebSocket webSocket, ByteString bytes) {
            output2("Receiving bytes : " + bytes.hex());
        }
        public void onMessage(okhttp3.WebSocket webSocket, JSONObject result){
//            output(result);
        }
        @Override
        public void onClosing(okhttp3.WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output2("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
            output2("Error : " + t.getMessage());
        }


    }

    private void start() {
        Request request = new Request.Builder().url(hostURL).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        okhttp3.WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    private void output2(String theResult){
        Log.e("Result DLNA WebSocket", theResult);
    }

    private void output (final String theRespone){
        try {
            JSONObject jsonObject = new JSONObject(theRespone);
            JSONArray result =  jsonObject.getJSONArray("result");
            JSONArray internalResult = result.getJSONArray(0);
            JSONObject volumeInfo = internalResult.getJSONObject(0);
            int volumeLevel = volumeInfo.getInt("volume");
            int minVolumeLevel = volumeInfo.getInt("minVolume");
            int maxVolumeLevel = volumeInfo.getInt("maxVolume");
            weeds.setMinVolume(minVolumeLevel);
            weeds.setMaxVolume(maxVolumeLevel);
            weeds.setVolume(volumeLevel);
        }catch (JSONException e){
            Log.e("WebSocketClass - Error", e.toString());
        }

    }

}
