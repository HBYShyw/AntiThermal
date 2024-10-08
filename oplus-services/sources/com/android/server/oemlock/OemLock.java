package com.android.server.oemlock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class OemLock {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract String getLockName();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isOemUnlockAllowedByCarrier();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean isOemUnlockAllowedByDevice();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void setOemUnlockAllowedByCarrier(boolean z, byte[] bArr);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void setOemUnlockAllowedByDevice(boolean z);
}
