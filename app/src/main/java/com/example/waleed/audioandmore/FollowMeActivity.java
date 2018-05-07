package com.example.waleed.audioandmore;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FollowMeActivity extends AppCompatActivity {

    Button btnScan, btnStop;

    Context context = this;
    Activity thisActivity = FollowMeActivity.this;
    TextView textView1, txtFoundBluetoothDevices;
    private String LOG_TAG = "FollowMeBluetooth"; // Just for logging purposes. Could be anything. Set to app_name
    private int REQUEST_ENABLE_BT = 99; // Any positive integer should work.
    private BluetoothAdapter mBluetoothAdapter;


    private static final int MY_PERMISSIONS_BLUETOOTH_ADMIN = 997;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 86;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_me);

        setTitle("Follow me");

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);


        LOG_TAG = getResources().getString(R.string.app_name);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        txtFoundBluetoothDevices = findViewById(R.id.txtFoundBluetoothDevices);

        btnScan = (Button) findViewById(R.id.btnScanBT);
        btnScan.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Toast.makeText(context, "button clicked", Toast.LENGTH_LONG).show();
                try {

                    checkThePermissionFirst();
                    // Start the process here
                    enableBluetoothOnDevice();
                    mBluetoothAdapter.startDiscovery();
                    displayListOfFoundDevices();
                }catch (Exception e)
                {
                    Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStop = findViewById(R.id.btnStopBT);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.cancelDiscovery();
                Toast.makeText(context, "Discovery Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkThePermissionFirst(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission Status Not Granted", Toast.LENGTH_SHORT).show();
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.BLUETOOTH_ADMIN)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        MY_PERMISSIONS_BLUETOOTH_ADMIN);

                // MY_PERMISSIONS_BLUETOOTH_ADMIN is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Toast.makeText(context, "Permisssion Has Already been granted", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission Status Not Granted", Toast.LENGTH_SHORT).show();
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_BLUETOOTH_ADMIN is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Toast.makeText(context, "Permisssion Has Already been granted", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_BLUETOOTH_ADMIN: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(context, "Permission granted YAY", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(context, "Permission granted YAY", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TextView rssi_msg = (TextView) findViewById(R.id.textView2);
            rssi_msg.setText(rssi_msg.getText() + ":\n");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                rssi_msg.setText(rssi_msg.getText() + deviceName + "-" + deviceHardwareAddress + " => " + rssi + "dBm\n");

                if(BluetoothDevice.ACTION_FOUND.equals(action)) {

                    String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

                }
            }
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    private void enableBluetoothOnDevice()
    {
        if (mBluetoothAdapter == null)
        {
            Log.e(LOG_TAG, "This device does not have a bluetooth adapter");
            finish();
            // If the android device does not have bluetooth, just return and get out.
            // There's nothing the app can do in this case. Closing app.
        }else{
            Log.e(LOG_TAG, "bluetooth adapter found");
        }

        // Check to see if bluetooth is enabled. Prompt to enable it
        if( !mBluetoothAdapter.isEnabled())
        {
            Log.e(LOG_TAG, "bluetooth adapter disabled");
            Toast.makeText(context,"bluetooth adapter disabled",Toast.LENGTH_LONG).show();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else{
            Log.e(LOG_TAG, "bluetooth adapter Enabled");
            Toast.makeText(context,"bluetooth adapter Enabled",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT)
        {
            if (resultCode == 0)
            {
                // If the resultCode is 0, the user selected "No" when prompt to
                // allow the app to enable bluetooth.
                // You may want to display a dialog explaining what would happen if
                // the user doesn't enable bluetooth.
                Toast.makeText(this, "The user decided to deny bluetooth access", Toast.LENGTH_LONG).show();
            }
            else
                Log.i(LOG_TAG, "User allowed bluetooth access!");
        }
    }

    private void displayListOfFoundDevices()
    {
//        arrayOfFoundBTDevices = new ArrayList<BluetoothObject>();

        // start looking for bluetooth devices
        mBluetoothAdapter.startDiscovery();
        Toast.makeText(context, "Scanning...",Toast.LENGTH_LONG).show();
        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.e(LOG_TAG, "found a device");
                Toast.makeText(context,"Found a Device",Toast.LENGTH_LONG).show();

                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {

                    // Get the bluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // Get the "RSSI" to get the signal strength as integer,
                    // but should be displayed in "dBm" units
                    int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                    txtFoundBluetoothDevices.setText(txtFoundBluetoothDevices.getText() + "\n" + device.getName());
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }


}
