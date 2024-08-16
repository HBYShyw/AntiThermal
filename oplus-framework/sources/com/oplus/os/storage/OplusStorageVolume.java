package com.oplus.os.storage;

import android.os.storage.StorageVolume;

/* loaded from: classes.dex */
public class OplusStorageVolume {
    public static final int VOLUME_TYPE_BLOCK_EXCEPTION = 2;
    public static final int VOLUME_TYPE_DEFAULT = 0;
    public static final int VOLUME_TYPE_READONLY_DIRECTLY = 1;
    private StorageVolume mStorageVolume;

    public OplusStorageVolume(StorageVolume storageVolume) {
        this.mStorageVolume = storageVolume;
    }

    public int getOplusReadOnlyType() {
        return this.mStorageVolume.mStorageVolumeExt.getReadOnlyType();
    }
}
