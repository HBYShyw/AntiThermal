package com.android.server.wm;

import android.graphics.Bitmap;
import android.os.Process;
import android.os.SystemClock;
import android.util.AtomicFile;
import android.util.Slog;
import android.window.TaskSnapshot;
import com.android.internal.annotations.GuardedBy;
import com.android.server.LocalServices;
import com.android.server.pm.UserManagerInternal;
import com.android.server.wm.BaseAppSnapshotPersister;
import com.android.server.wm.nano.WindowManagerProtos;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SnapshotPersistQueue {
    private static final int COMPRESS_QUALITY = 95;
    private static final long DELAY_MS = 100;
    private static final int MAX_STORE_QUEUE_DEPTH = 2;
    private static final String TAG = "WindowManager";

    @GuardedBy({"mLock"})
    private boolean mPaused;

    @GuardedBy({"mLock"})
    private boolean mQueueIdling;
    private boolean mStarted;

    @GuardedBy({"mLock"})
    private final ArrayDeque<WriteQueueItem> mWriteQueue = new ArrayDeque<>();

    @GuardedBy({"mLock"})
    private final ArrayDeque<StoreWriteQueueItem> mStoreQueueItems = new ArrayDeque<>();
    private final Object mLock = new Object();
    private final Thread mPersister = new Thread("TaskSnapshotPersister") { // from class: com.android.server.wm.SnapshotPersistQueue.1
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            WriteQueueItem writeQueueItem;
            boolean z;
            Process.setThreadPriority(10);
            while (true) {
                synchronized (SnapshotPersistQueue.this.mLock) {
                    if (SnapshotPersistQueue.this.mPaused) {
                        writeQueueItem = null;
                    } else {
                        writeQueueItem = (WriteQueueItem) SnapshotPersistQueue.this.mWriteQueue.poll();
                        if (writeQueueItem != null) {
                            if (writeQueueItem.isReady()) {
                                writeQueueItem.onDequeuedLocked();
                                z = true;
                            } else {
                                SnapshotPersistQueue.this.mWriteQueue.addLast(writeQueueItem);
                            }
                        }
                    }
                    z = false;
                }
                if (writeQueueItem != null) {
                    if (z) {
                        writeQueueItem.write();
                    }
                    SystemClock.sleep(SnapshotPersistQueue.DELAY_MS);
                }
                synchronized (SnapshotPersistQueue.this.mLock) {
                    boolean isEmpty = SnapshotPersistQueue.this.mWriteQueue.isEmpty();
                    if (isEmpty || SnapshotPersistQueue.this.mPaused) {
                        try {
                            SnapshotPersistQueue.this.mQueueIdling = isEmpty;
                            SnapshotPersistQueue.this.mLock.wait();
                            SnapshotPersistQueue.this.mQueueIdling = false;
                        } catch (InterruptedException unused) {
                        }
                    }
                }
            }
        }
    };
    private final UserManagerInternal mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getLock() {
        return this.mLock;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady() {
        start();
    }

    void start() {
        if (this.mStarted) {
            return;
        }
        this.mStarted = true;
        this.mPersister.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPaused(boolean z) {
        synchronized (this.mLock) {
            this.mPaused = z;
            if (!z) {
                this.mLock.notifyAll();
            }
        }
    }

    void waitForQueueEmpty() {
        while (true) {
            synchronized (this.mLock) {
                if (this.mWriteQueue.isEmpty() && this.mQueueIdling) {
                    return;
                }
            }
            SystemClock.sleep(DELAY_MS);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void sendToQueueLocked(WriteQueueItem writeQueueItem) {
        this.mWriteQueue.offer(writeQueueItem);
        writeQueueItem.onQueuedLocked();
        ensureStoreQueueDepthLocked();
        if (this.mPaused) {
            return;
        }
        this.mLock.notifyAll();
    }

    @GuardedBy({"mLock"})
    private void ensureStoreQueueDepthLocked() {
        while (this.mStoreQueueItems.size() > 2) {
            StoreWriteQueueItem poll = this.mStoreQueueItems.poll();
            this.mWriteQueue.remove(poll);
            Slog.i(TAG, "Queue is too deep! Purged item with index=" + poll.mId);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deleteSnapshot(int i, int i2, BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
        File protoFile = persistInfoProvider.getProtoFile(i, i2);
        File lowResolutionBitmapFile = persistInfoProvider.getLowResolutionBitmapFile(i, i2);
        protoFile.delete();
        if (lowResolutionBitmapFile.exists()) {
            lowResolutionBitmapFile.delete();
        }
        File highResolutionBitmapFile = persistInfoProvider.getHighResolutionBitmapFile(i, i2);
        if (highResolutionBitmapFile.exists()) {
            highResolutionBitmapFile.delete();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class WriteQueueItem {
        protected final BaseAppSnapshotPersister.PersistInfoProvider mPersistInfoProvider;

        boolean isReady() {
            return true;
        }

        void onDequeuedLocked() {
        }

        void onQueuedLocked() {
        }

        abstract void write();

        /* JADX INFO: Access modifiers changed from: package-private */
        public WriteQueueItem(BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
            this.mPersistInfoProvider = persistInfoProvider;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StoreWriteQueueItem createStoreWriteQueueItem(int i, int i2, TaskSnapshot taskSnapshot, BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
        return new StoreWriteQueueItem(i, i2, taskSnapshot, persistInfoProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class StoreWriteQueueItem extends WriteQueueItem {
        private final int mId;
        private final TaskSnapshot mSnapshot;
        private final int mUserId;

        StoreWriteQueueItem(int i, int i2, TaskSnapshot taskSnapshot, BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
            super(persistInfoProvider);
            this.mId = i;
            this.mUserId = i2;
            this.mSnapshot = taskSnapshot;
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        @GuardedBy({"mLock"})
        void onQueuedLocked() {
            SnapshotPersistQueue.this.mStoreQueueItems.offer(this);
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        @GuardedBy({"mLock"})
        void onDequeuedLocked() {
            SnapshotPersistQueue.this.mStoreQueueItems.remove(this);
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        boolean isReady() {
            return SnapshotPersistQueue.this.mUserManagerInternal.isUserUnlocked(this.mUserId);
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        void write() {
            if (!this.mPersistInfoProvider.createDirectory(this.mUserId)) {
                Slog.e(SnapshotPersistQueue.TAG, "Unable to create snapshot directory for user dir=" + this.mPersistInfoProvider.getDirectory(this.mUserId));
            }
            if (writeBuffer() ? !writeProto() : true) {
                SnapshotPersistQueue.this.deleteSnapshot(this.mId, this.mUserId, this.mPersistInfoProvider);
            }
        }

        boolean writeProto() {
            FileOutputStream fileOutputStream;
            WindowManagerProtos.TaskSnapshotProto taskSnapshotProto = new WindowManagerProtos.TaskSnapshotProto();
            taskSnapshotProto.orientation = this.mSnapshot.getOrientation();
            taskSnapshotProto.rotation = this.mSnapshot.getRotation();
            taskSnapshotProto.taskWidth = this.mSnapshot.getTaskSize().x;
            taskSnapshotProto.taskHeight = this.mSnapshot.getTaskSize().y;
            taskSnapshotProto.insetLeft = this.mSnapshot.getContentInsets().left;
            taskSnapshotProto.insetTop = this.mSnapshot.getContentInsets().top;
            taskSnapshotProto.insetRight = this.mSnapshot.getContentInsets().right;
            taskSnapshotProto.insetBottom = this.mSnapshot.getContentInsets().bottom;
            taskSnapshotProto.letterboxInsetLeft = this.mSnapshot.getLetterboxInsets().left;
            taskSnapshotProto.letterboxInsetTop = this.mSnapshot.getLetterboxInsets().top;
            taskSnapshotProto.letterboxInsetRight = this.mSnapshot.getLetterboxInsets().right;
            taskSnapshotProto.letterboxInsetBottom = this.mSnapshot.getLetterboxInsets().bottom;
            taskSnapshotProto.isRealSnapshot = this.mSnapshot.isRealSnapshot();
            taskSnapshotProto.windowingMode = this.mSnapshot.getWindowingMode();
            taskSnapshotProto.appearance = this.mSnapshot.getAppearance();
            taskSnapshotProto.isTranslucent = this.mSnapshot.isTranslucent();
            taskSnapshotProto.topActivityComponent = this.mSnapshot.getTopActivityComponent().flattenToString();
            taskSnapshotProto.id = this.mSnapshot.getId();
            byte[] byteArray = WindowManagerProtos.TaskSnapshotProto.toByteArray(taskSnapshotProto);
            File protoFile = this.mPersistInfoProvider.getProtoFile(this.mId, this.mUserId);
            AtomicFile atomicFile = new AtomicFile(protoFile);
            try {
                fileOutputStream = atomicFile.startWrite();
                try {
                    fileOutputStream.write(byteArray);
                    atomicFile.finishWrite(fileOutputStream);
                    return true;
                } catch (IOException e) {
                    e = e;
                    atomicFile.failWrite(fileOutputStream);
                    Slog.e(SnapshotPersistQueue.TAG, "Unable to open " + protoFile + " for persisting. " + e);
                    return false;
                }
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = null;
            }
        }

        boolean writeBuffer() {
            if (this.mSnapshot.getSnapshot().isDestroyed()) {
                Slog.e(SnapshotPersistQueue.TAG, "grahpicbuffer has been destroyed");
                return false;
            }
            if (AbsAppSnapshotController.isInvalidHardwareBuffer(this.mSnapshot.getHardwareBuffer())) {
                Slog.e(SnapshotPersistQueue.TAG, "Invalid task snapshot hw buffer, taskId=" + this.mId);
                return false;
            }
            Bitmap wrapHardwareBuffer = Bitmap.wrapHardwareBuffer(this.mSnapshot.getHardwareBuffer(), this.mSnapshot.getColorSpace());
            if (wrapHardwareBuffer == null) {
                Slog.e(SnapshotPersistQueue.TAG, "Invalid task snapshot hw bitmap");
                return false;
            }
            if (wrapHardwareBuffer.getWidth() <= 0 || wrapHardwareBuffer.getHeight() <= 0) {
                wrapHardwareBuffer.recycle();
                return false;
            }
            Bitmap copy = wrapHardwareBuffer.copy(Bitmap.Config.ARGB_8888, false);
            if (copy == null) {
                Slog.e(SnapshotPersistQueue.TAG, "Bitmap conversion from (config=" + wrapHardwareBuffer.getConfig() + ", isMutable=" + wrapHardwareBuffer.isMutable() + ") to (config=ARGB_8888, isMutable=false) failed.");
                return false;
            }
            wrapHardwareBuffer.recycle();
            if (copy.getWidth() <= 0 || copy.getHeight() <= 0) {
                copy.recycle();
                return false;
            }
            File highResolutionBitmapFile = this.mPersistInfoProvider.getHighResolutionBitmapFile(this.mId, this.mUserId);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(highResolutionBitmapFile);
                copy.compress(Bitmap.CompressFormat.JPEG, SnapshotPersistQueue.COMPRESS_QUALITY, fileOutputStream);
                fileOutputStream.close();
                if (!this.mPersistInfoProvider.enableLowResSnapshots()) {
                    copy.recycle();
                    return true;
                }
                int width = (int) (wrapHardwareBuffer.getWidth() * this.mPersistInfoProvider.lowResScaleFactor());
                int height = (int) (wrapHardwareBuffer.getHeight() * this.mPersistInfoProvider.lowResScaleFactor());
                if (width <= 0 || height <= 0) {
                    copy.recycle();
                    return false;
                }
                Bitmap createScaledBitmap = Bitmap.createScaledBitmap(copy, width, height, true);
                copy.recycle();
                File lowResolutionBitmapFile = this.mPersistInfoProvider.getLowResolutionBitmapFile(this.mId, this.mUserId);
                try {
                    FileOutputStream fileOutputStream2 = new FileOutputStream(lowResolutionBitmapFile);
                    createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, SnapshotPersistQueue.COMPRESS_QUALITY, fileOutputStream2);
                    fileOutputStream2.close();
                    createScaledBitmap.recycle();
                    return true;
                } catch (IOException e) {
                    Slog.e(SnapshotPersistQueue.TAG, "Unable to open " + lowResolutionBitmapFile + " for persisting.", e);
                    return false;
                }
            } catch (IOException e2) {
                Slog.e(SnapshotPersistQueue.TAG, "Unable to open " + highResolutionBitmapFile + " for persisting.", e2);
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeleteWriteQueueItem createDeleteWriteQueueItem(int i, int i2, BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
        return new DeleteWriteQueueItem(i, i2, persistInfoProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class DeleteWriteQueueItem extends WriteQueueItem {
        private final int mId;
        private final int mUserId;

        DeleteWriteQueueItem(int i, int i2, BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
            super(persistInfoProvider);
            this.mId = i;
            this.mUserId = i2;
        }

        @Override // com.android.server.wm.SnapshotPersistQueue.WriteQueueItem
        void write() {
            SnapshotPersistQueue.this.deleteSnapshot(this.mId, this.mUserId, this.mPersistInfoProvider);
        }
    }
}
