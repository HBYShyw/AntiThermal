package com.android.server.backup.params;

import android.app.backup.IFullBackupRestoreObserver;
import android.os.ParcelFileDescriptor;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AdbParams {
    public String curPassword;
    public String encryptPassword;
    public ParcelFileDescriptor fd;
    public final AtomicBoolean latch = new AtomicBoolean(false);
    public IFullBackupRestoreObserver observer;
}
