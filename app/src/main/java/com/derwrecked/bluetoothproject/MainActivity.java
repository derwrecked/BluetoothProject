package com.derwrecked.bluetoothproject;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        BluetoothDiscoveryBroadcastReceiver.BroadcastReceiverAddDevice {
    int REQUEST_ENABLE_BT = 1;
    RecyclerView recyclerView;
    BluetoothRecyclerAdapter bluetoothRecyclerAdapter;
    BluetoothBroadcastReceiver br;
    BluetoothDiscoveryBroadcastReceiver discoverBCR;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check permissions
        checkPermissions();

        Button startbutton = (Button) findViewById(R.id.start_discovery_button);
        Button stop = (Button) findViewById(R.id.stop_discovery_button);


        ArrayList<String> list = new ArrayList<>();
        String k;
        for (int i = 0; i < 10; i++) {
            k = "Bluetooth device: " + Integer.toString(i);
            list.add(k);
        }

        recyclerView = (RecyclerView) findViewById(R.id.bluetooth_discovery_recyclerview);
        bluetoothRecyclerAdapter = new BluetoothRecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bluetoothRecyclerAdapter);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                    // register broadcast receivers to find devices.
                    registerReceivers(
                            new BluetoothDiscoveryBroadcastReceiver(MainActivity.this),
                            new BluetoothBroadcastReceiver());
                    // start discovery
                    BluetoothAdapter.getDefaultAdapter().startDiscovery();
                } else {
                    Toast.makeText(MainActivity.this, "Already Discovering", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unregisterReceivers();
                if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                } else {
                    Toast.makeText(MainActivity.this, "Currently not discovering, nothing to stop.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateOrAddDevice(String string) {
        bluetoothRecyclerAdapter = (BluetoothRecyclerAdapter) recyclerView.getAdapter();
        bluetoothRecyclerAdapter.updateOrAddItem(string);
    }

    private void unregisterReceivers() {
        if (discoverBCR != null) {
            unregisterReceiver(discoverBCR);
        }
        if(br != null){
            unregisterReceiver(br);
        }
        discoverBCR = null;
        br = null;
        filter = null;
    }

    private void registerReceivers(BluetoothDiscoveryBroadcastReceiver discoveryBCR, BluetoothBroadcastReceiver bluetoothStatusBCR) {
        discoverBCR = discoveryBCR;
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoverBCR, filter);

        br = bluetoothStatusBCR;
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(br, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceivers();
    }

    private void checkPermissions() {
        final int CODE = 5; // app defined constant used for onRequestPermissionsResult

        String[] permissionsToRequest =
                {
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

        boolean allPermissionsGranted = true;

        for (String permission : permissionsToRequest) {
            allPermissionsGranted = allPermissionsGranted && (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, CODE);
        }
    }
}
