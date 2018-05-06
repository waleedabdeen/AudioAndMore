package com.example.waleed.audioandmore;

/**
 * Created by Waleed on 06/05/2018.
 */

public class Device {
    String status;
    String ipAddress;
    String label;
    int volume;
    int minVolume;
    int maxVolume;

    public Device() {
        status = "offline";
        ipAddress = "";
        label = "Unknown";
        volume = 10;
        minVolume = 0;
        maxVolume = 20;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(int minVolume) {
        this.minVolume = minVolume;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }
}
