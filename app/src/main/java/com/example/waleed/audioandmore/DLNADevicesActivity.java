package com.example.waleed.audioandmore;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;

public class DLNADevicesActivity extends AppCompatActivity {
    Button btnDiscoverDLNA;
    TextView txtDiscoveredDevices;
    Context context = this;
    Activity thisActivity = DLNADevicesActivity.this;

    private String[] devicesArray;
    private ArrayList<String> devicesList = new ArrayList<>();

    private boolean permissionToWIFI = false;
    private String [] permissions = {Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,Manifest.permission.ACCESS_WIFI_STATE};

    private static final int REQUEST_WIFI_PERMISSION = 553;
    private static final int REQUEST_CHANGE_MULTICAST_WIFI = 455;
    private static final int REQUEST_INTERNET = 400;

    //RecyclerView and its components
    private RecyclerView reViewDevices;
    private static RecyclerView.Adapter devicesAdapter;
    private RecyclerView.LayoutManager devicesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlnadevices);

//        ActivityCompat.requestPermissions(this, permissions, REQUEST_WIFI_PERMISSION);
//        ActivityCompat.requestPermissions(this, permissions, REQUEST_CHANGE_MULTICAST_WIFI);

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
//        txtDiscoveredDevices = findViewById(R.id.);

        btnDiscoverDLNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Button Clicked",Toast.LENGTH_SHORT).show();
//                devicesArray = UPnPDiscovery.discoverDevices(context);
//                Log.e("DLNA", String.valueOf (devicesArray.length));

                DLNAClass dlnaScan = new DLNAClass(getApplicationContext());
                String result = dlnaScan.Discover();
                Log.e("DLNA Discover" , result);
                devicesList.add(result);
//                for(int i = 0; i< devicesArray.length; i++){
//                    devicesList.add(devicesArray[i]);
//                    Log.e("DLNA", devicesArray[i]);
//
//                }
            }
        });


    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case REQUEST_WIFI_PERMISSION:
//                permissionToWIFI  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                break;
//            case REQUEST_CHANGE_MULTICAST_WIFI:
//                permissionToWIFI  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                break;
//        }
//        if (!permissionToWIFI ) finish();
//
//    }

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

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
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



}
