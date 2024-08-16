package com.android.server.hdmi;

import android.hardware.hdmi.HdmiPortInfo;
import android.util.SparseArray;
import com.android.internal.util.IndentingPrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HdmiMhlControllerStub {
    private static final int INVALID_DEVICE_ROLES = 0;
    private static final int INVALID_MHL_VERSION = 0;
    private static final int NO_SUPPORTED_FEATURES = 0;
    private static final SparseArray<HdmiMhlLocalDeviceStub> mLocalDevices = new SparseArray<>();
    private static final HdmiPortInfo[] EMPTY_PORT_INFO = new HdmiPortInfo[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiMhlLocalDeviceStub addLocalDevice(HdmiMhlLocalDeviceStub hdmiMhlLocalDeviceStub) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearAllLocalDevices() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
    }

    int getEcbusDeviceRoles(int i) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiMhlLocalDeviceStub getLocalDevice(int i) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiMhlLocalDeviceStub getLocalDeviceById(int i) {
        return null;
    }

    int getMhlVersion(int i) {
        return 0;
    }

    int getPeerMhlVersion(int i) {
        return 0;
    }

    int getSupportedFeatures(int i) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReady() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiMhlLocalDeviceStub removeLocalDevice(int i) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendVendorCommand(int i, int i2, int i3, byte[] bArr) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOption(int i, int i2) {
    }

    private HdmiMhlControllerStub(HdmiControlService hdmiControlService) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiMhlControllerStub create(HdmiControlService hdmiControlService) {
        return new HdmiMhlControllerStub(hdmiControlService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiPortInfo[] getPortInfos() {
        return EMPTY_PORT_INFO;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseArray<HdmiMhlLocalDeviceStub> getAllLocalDevices() {
        return mLocalDevices;
    }
}
