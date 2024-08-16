package com.oplus.wrapper.os.storage;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StorageManager {
    private final android.os.storage.StorageManager mStorageManager;

    public StorageManager(android.os.storage.StorageManager storageManager) {
        this.mStorageManager = storageManager;
    }

    public void registerListener(StorageEventListener storageEventListener) {
        this.mStorageManager.registerListener(storageEventListener.getStorageEventListener());
    }

    public void unregisterListener(StorageEventListener storageEventListener) {
        this.mStorageManager.unregisterListener(storageEventListener.getStorageEventListener());
    }

    public android.os.storage.StorageVolume[] getVolumeList() {
        return this.mStorageManager.getVolumeList();
    }

    public String getVolumeState(String mountPoint) {
        return this.mStorageManager.getVolumeState(mountPoint);
    }

    public static boolean isFileEncryptedNativeOnly() {
        return android.os.storage.StorageManager.isFileEncryptedNativeOnly();
    }

    public List<VolumeInfo> getVolumes() {
        List<android.os.storage.VolumeInfo> volumes = this.mStorageManager.getVolumes();
        List<VolumeInfo> volumeInfoList = new ArrayList<>(volumes.size());
        for (android.os.storage.VolumeInfo volumeInfo : volumes) {
            if (volumeInfo != null) {
                volumeInfoList.add(new VolumeInfo(volumeInfo));
            }
        }
        return volumeInfoList;
    }

    public String[] getVolumePaths() {
        return this.mStorageManager.getVolumePaths();
    }

    public long getPrimaryStorageSize() {
        return this.mStorageManager.getPrimaryStorageSize();
    }
}
