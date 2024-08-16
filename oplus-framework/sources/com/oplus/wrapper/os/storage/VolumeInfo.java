package com.oplus.wrapper.os.storage;

import java.io.File;

/* loaded from: classes.dex */
public class VolumeInfo {
    public static final int STATE_MOUNTED = getStateMounted();
    private final android.os.storage.VolumeInfo mVolumeInfo;

    private static int getStateMounted() {
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VolumeInfo(android.os.storage.VolumeInfo volumeInfo) {
        this.mVolumeInfo = volumeInfo;
    }

    public android.os.storage.VolumeInfo getVolumeInfo() {
        return this.mVolumeInfo;
    }

    public void setStringPath(String path) {
        this.mVolumeInfo.path = path;
    }

    public String getStringPath() {
        return this.mVolumeInfo.path;
    }

    public DiskInfo getDisk() {
        if (this.mVolumeInfo.getDisk() == null) {
            return null;
        }
        return new DiskInfo(this.mVolumeInfo.getDisk());
    }

    public String getId() {
        return this.mVolumeInfo.getId();
    }

    public File getPath() {
        return this.mVolumeInfo.getPath();
    }

    public String getFsUuid() {
        return this.mVolumeInfo.getFsUuid();
    }

    public String getFsType() {
        return this.mVolumeInfo.fsType;
    }

    public boolean isMountedReadable() {
        return this.mVolumeInfo.isMountedReadable();
    }

    public int getType() {
        return this.mVolumeInfo.getType();
    }
}
