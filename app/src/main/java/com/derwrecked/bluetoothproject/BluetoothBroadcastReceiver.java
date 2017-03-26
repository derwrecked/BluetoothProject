package com.derwrecked.bluetoothproject;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by pittsd3 on 3/16/2017.
 */

public class BluetoothBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF){
                Toast.makeText(context, "Bluetooth off.", Toast.LENGTH_SHORT).show();
            }else if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON){
                Toast.makeText(context, "Bluetooth on.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Bluetooth unknown state.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
