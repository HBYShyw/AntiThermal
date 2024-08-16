package com.android.server.backup.params;

import android.os.ParcelFileDescriptor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AdbRestoreParams extends AdbParams {
    public AdbRestoreParams(ParcelFileDescriptor parcelFileDescriptor) {
        this.fd = parcelFileDescriptor;
    }
}
