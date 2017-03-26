package com.derwrecked.bluetoothproject;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by pittsd3 on 3/16/2017.
 */

public class BluetoothDiscoveryBroadcastReceiver extends BroadcastReceiver {
    private final static String TAG = BluetoothDiscoveryBroadcastReceiver.class.getSimpleName();
    private static BroadcastReceiverAddDevice mMainActivityInterface;

    public BluetoothDiscoveryBroadcastReceiver(){
        Log.d(TAG, "constructor called.");
    }
    public BluetoothDiscoveryBroadcastReceiver(MainActivity mainActivity){
        this.mMainActivityInterface = mainActivity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceieve called.");
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            Log.d(TAG, "Device found.");
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            if(deviceName == null){
                deviceName = "Device Name Null";
            }
            if(deviceHardwareAddress == null){
                deviceName = "Device Hardware Address Null";
            }
            String send = deviceName + "," + deviceHardwareAddress;
            //LocalBroadcastManager.getInstance(context);
            //context.sendBroadcast(sendIntent);
            //Log.d(TAG, send);
            mMainActivityInterface.updateOrAddDevice(send);
        }
    }

    public interface BroadcastReceiverAddDevice{
        void updateOrAddDevice(String string);
    }
}
