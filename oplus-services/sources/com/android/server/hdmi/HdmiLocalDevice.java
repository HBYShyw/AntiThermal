package com.android.server.hdmi;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class HdmiLocalDevice {
    private static final String TAG = "HdmiLocalDevice";
    protected final int mDeviceType;
    protected final Object mLock;
    protected final HdmiControlService mService;

    /* JADX INFO: Access modifiers changed from: protected */
    public HdmiLocalDevice(HdmiControlService hdmiControlService, int i) {
        this.mService = hdmiControlService;
        this.mDeviceType = i;
        this.mLock = hdmiControlService.getServiceLock();
    }
}
