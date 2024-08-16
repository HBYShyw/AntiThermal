package com.android.server;

import android.os.storage.VolumeInfo;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public interface IOplusStorageManagerCallback {
    byte[] exportSensitveFileKey(int i, int i2, boolean z);

    int getSystemUnlockedUserIdByIndexLocked(int i);

    VolumeInfo getVolumeInfoByIndexLocked(int i);

    void onCheckBeforeMount(String str);

    void onFsyncCtrl(String str);

    void oplusAbortIdleMaintenance();

    boolean oplusIsFuseEnabled();

    void unlockSensitiveFileKey(int i, int i2, byte[] bArr, byte[] bArr2, int i3);
}
