package com.android.server.hdmi;

import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.IHdmiControlCallback;
import com.android.server.location.gnss.hal.GnssNative;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HdmiMhlLocalDeviceStub {
    private static final HdmiDeviceInfo INFO = HdmiDeviceInfo.mhlDevice(GnssNative.GNSS_AIDING_TYPE_ALL, -1, -1, -1);
    private final int mPortId;
    private final HdmiControlService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBusOvercurrentDetected(boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDeviceRemoved() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendKeyEvent(int i, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendStandby() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBusMode(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceStatusChange(int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void turnOn(IHdmiControlCallback iHdmiControlCallback) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HdmiMhlLocalDeviceStub(HdmiControlService hdmiControlService, int i) {
        this.mService = hdmiControlService;
        this.mPortId = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiDeviceInfo getInfo() {
        return INFO;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPortId() {
        return this.mPortId;
    }
}
