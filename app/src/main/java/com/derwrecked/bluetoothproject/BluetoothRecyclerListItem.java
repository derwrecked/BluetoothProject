package com.derwrecked.bluetoothproject;

/**
 * Created by Derek on 3/18/2017.
 */

public class BluetoothRecyclerListItem {
    private String deviceName;
    private String deviceAddress;

    public BluetoothRecyclerListItem(String deviceName, String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
