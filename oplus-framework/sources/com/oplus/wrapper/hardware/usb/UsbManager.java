package com.oplus.wrapper.hardware.usb;

import android.net.wifi.OplusWifiManager;

/* loaded from: classes.dex */
public class UsbManager {
    private final android.hardware.usb.UsbManager mUsbManager;
    public static final long FUNCTION_NONE = getFunctionNone();
    public static final long FUNCTION_ADB = getFunctionAdb();
    public static final long FUNCTION_AUDIO_SOURCE = getFunctionAudioSource();
    public static final long FUNCTION_ACCESSORY = getFunctionAccessory();
    public static final long FUNCTION_MTP = getFunctionMtp();
    public static final long FUNCTION_PTP = getFunctionPtp();
    public static final long FUNCTION_RNDIS = getFunctionRndis();
    public static final long FUNCTION_MIDI = getFunctionMidi();
    public static final long FUNCTION_NCM = getFunctionNcm();

    public UsbManager(android.hardware.usb.UsbManager usbManager) {
        this.mUsbManager = usbManager;
    }

    public void setCurrentFunctions(long functions) {
        this.mUsbManager.setCurrentFunctions(functions);
    }

    public void setScreenUnlockedFunctions(long functions) {
        this.mUsbManager.setScreenUnlockedFunctions(functions);
    }

    private static long getFunctionNone() {
        return 0L;
    }

    private static long getFunctionAdb() {
        return 1L;
    }

    private static long getFunctionMtp() {
        return 4L;
    }

    private static long getFunctionPtp() {
        return 16L;
    }

    private static long getFunctionAudioSource() {
        return 64L;
    }

    private static long getFunctionRndis() {
        return 32L;
    }

    private static long getFunctionMidi() {
        return 8L;
    }

    private static long getFunctionNcm() {
        return OplusWifiManager.OPLUS_WIFI_FEATURE_IOTConnect;
    }

    private static long getFunctionAccessory() {
        return 2L;
    }
}
