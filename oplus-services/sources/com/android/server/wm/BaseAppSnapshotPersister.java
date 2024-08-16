package com.android.server.wm;

import android.window.TaskSnapshot;
import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
class BaseAppSnapshotPersister {
    static final String BITMAP_EXTENSION = ".jpg";
    static final String LOW_RES_FILE_POSTFIX = "_reduced";
    static final String PROTO_EXTENSION = ".proto";
    protected final Object mLock;
    protected final PersistInfoProvider mPersistInfoProvider;
    protected final SnapshotPersistQueue mSnapshotPersistQueue;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public interface DirectoryResolver {
        File getSystemDirectoryForUser(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAppSnapshotPersister(SnapshotPersistQueue snapshotPersistQueue, PersistInfoProvider persistInfoProvider) {
        this.mSnapshotPersistQueue = snapshotPersistQueue;
        this.mPersistInfoProvider = persistInfoProvider;
        this.mLock = snapshotPersistQueue.getLock();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void persistSnapshot(int i, int i2, TaskSnapshot taskSnapshot) {
        synchronized (this.mLock) {
            SnapshotPersistQueue snapshotPersistQueue = this.mSnapshotPersistQueue;
            snapshotPersistQueue.sendToQueueLocked(snapshotPersistQueue.createStoreWriteQueueItem(i, i2, taskSnapshot, this.mPersistInfoProvider));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeSnap(int i, int i2) {
        synchronized (this.mLock) {
            SnapshotPersistQueue snapshotPersistQueue = this.mSnapshotPersistQueue;
            snapshotPersistQueue.sendToQueueLocked(snapshotPersistQueue.createDeleteWriteQueueItem(i, i2, this.mPersistInfoProvider));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class PersistInfoProvider {
        private final String mDirName;
        protected final DirectoryResolver mDirectoryResolver;
        private final boolean mEnableLowResSnapshots;
        private final float mLowResScaleFactor;
        private final boolean mUse16BitFormat;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PersistInfoProvider(DirectoryResolver directoryResolver, String str, boolean z, float f, boolean z2) {
            this.mDirectoryResolver = directoryResolver;
            this.mDirName = str;
            this.mEnableLowResSnapshots = z;
            this.mLowResScaleFactor = f;
            this.mUse16BitFormat = z2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public File getDirectory(int i) {
            return new File(this.mDirectoryResolver.getSystemDirectoryForUser(i), this.mDirName);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean use16BitFormat() {
            return this.mUse16BitFormat;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean createDirectory(int i) {
            File directory = getDirectory(i);
            return directory.exists() || directory.mkdir();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public File getProtoFile(int i, int i2) {
            return new File(getDirectory(i2), i + BaseAppSnapshotPersister.PROTO_EXTENSION);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public File getLowResolutionBitmapFile(int i, int i2) {
            return new File(getDirectory(i2), i + BaseAppSnapshotPersister.LOW_RES_FILE_POSTFIX + BaseAppSnapshotPersister.BITMAP_EXTENSION);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public File getHighResolutionBitmapFile(int i, int i2) {
            return new File(getDirectory(i2), i + BaseAppSnapshotPersister.BITMAP_EXTENSION);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean enableLowResSnapshots() {
            return this.mEnableLowResSnapshots;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public float lowResScaleFactor() {
            return this.mLowResScaleFactor;
        }
    }
}
