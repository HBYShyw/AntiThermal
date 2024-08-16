package com.android.server.usb;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class OplusUsbDeviceFunctionInfo {
    private String mContentStr;

    public OplusUsbDeviceFunctionInfo(String str, String str2, String str3, boolean z, boolean z2, String str4) {
        this.mContentStr = null;
        this.mContentStr = "UsbFunc[fun:" + str + ", oemFun:" + str2 + ", curFun:" + str3 + ", curFunApplied:" + z + ", forceRestart:" + z2 + ", curOemFun:" + str4;
    }

    public String toString() {
        return this.mContentStr;
    }
}
