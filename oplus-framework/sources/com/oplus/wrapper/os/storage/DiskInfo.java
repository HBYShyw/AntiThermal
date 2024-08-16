package com.oplus.wrapper.os.storage;

/* loaded from: classes.dex */
public class DiskInfo {
    private final android.os.storage.DiskInfo mDiskInfo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DiskInfo(android.os.storage.DiskInfo diskInfo) {
        this.mDiskInfo = diskInfo;
    }

    public boolean isSd() {
        return this.mDiskInfo.isSd();
    }
}
