package com.r2starbase.apo11o.coeuspinger;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Originally created by apo11o on 2/1/16.
 */
public class DeviceInfo {
    private String deviceName;
    private String deviceAddress;
    private int deviceStatus;
    private WifiP2pDevice device;

    public DeviceInfo(WifiP2pDevice device) {
        this.device = device;
        this.deviceName = device.deviceName;
        this.deviceAddress = device.deviceAddress;
        this.deviceStatus = device.status;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public int getDeviceStatus() {
        return this.device.status;
    }

    public String getDeviceStatusMsg() {
        return this.getStatusMsg(this.deviceStatus);
    }

    private String getStatusMsg(int status) {
        String msg;
        switch (status) {
            case WifiP2pDevice.AVAILABLE:
                msg = "Available";
                break;
            case WifiP2pDevice.UNAVAILABLE:
                msg = "Unavailable";
                break;
            case WifiP2pDevice.INVITED:
                msg = "Invited";
                break;
            case WifiP2pDevice.CONNECTED:
                msg = "Connected";
                break;
            case WifiP2pDevice.FAILED:
                msg = "Failed";
                break;
            default:
                msg = "Unknown";
        }
        return msg;
    }

    public WifiP2pDevice getDevice() {
        return this.device;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "deviceName='" + deviceName + '\'' +
                ", deviceAddress='" + deviceAddress + '\'' +
                ", deviceStatus=" + deviceStatus +
                ", device=" + device +
                '}';
    }
}
