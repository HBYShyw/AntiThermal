package com.android.server.usb;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class OplusUsbDeviceFinishBootInfo {
    private String mContentStr;

    public OplusUsbDeviceFinishBootInfo(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, String str, boolean z7) {
        this.mContentStr = null;
        this.mContentStr = "UsbBootInfo[connected:" + z + ", bootComplete:" + z2 + ", curUsbFunRec:" + z3 + ", systemReady:" + z4 + ", pendingBoot:" + z5 + ", screenLock:" + z6 + ", screenUnlock:" + str + ", adbEnable:" + z7;
    }

    public String toString() {
        return this.mContentStr;
    }
}
