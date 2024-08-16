package com.oplus.wrapper.os.storage;

/* loaded from: classes.dex */
public class StorageEventListener {
    private final android.os.storage.StorageEventListener mStorageEventListener = new android.os.storage.StorageEventListener() { // from class: com.oplus.wrapper.os.storage.StorageEventListener.1
        public void onStorageStateChanged(String path, String oldState, String newState) {
            StorageEventListener.this.onStorageStateChanged(path, oldState, newState);
        }

        public void onVolumeStateChanged(android.os.storage.VolumeInfo volumeInfo, int oldState, int newState) {
            StorageEventListener.this.onVolumeStateChanged(new VolumeInfo(volumeInfo), oldState, newState);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public android.os.storage.StorageEventListener getStorageEventListener() {
        return this.mStorageEventListener;
    }

    public void onStorageStateChanged(String path, String oldState, String newState) {
    }

    public void onVolumeStateChanged(VolumeInfo vol, int oldState, int newState) {
    }
}
