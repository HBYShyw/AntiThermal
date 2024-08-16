package com.android.server.pm;

import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.pm.ShortcutService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ShortcutBitmapSaver {
    private static final boolean ADD_DELAY_BEFORE_SAVE_FOR_TEST = false;
    private static final boolean DEBUG = false;
    private static final long SAVE_DELAY_MS_FOR_TEST = 1000;
    private static final String TAG = "ShortcutService";
    private final long SAVE_WAIT_TIMEOUT_MS = 5000;
    private final Executor mExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());

    @GuardedBy({"mPendingItems"})
    private final Deque<PendingItem> mPendingItems = new LinkedBlockingDeque();
    private final Runnable mRunnable = new Runnable() { // from class: com.android.server.pm.ShortcutBitmapSaver$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            ShortcutBitmapSaver.this.lambda$new$1();
        }
    };
    private final ShortcutService mService;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PendingItem {
        public final byte[] bytes;
        private final long mInstantiatedUptimeMillis;
        public final ShortcutInfo shortcut;

        private PendingItem(ShortcutInfo shortcutInfo, byte[] bArr) {
            this.shortcut = shortcutInfo;
            this.bytes = bArr;
            this.mInstantiatedUptimeMillis = SystemClock.uptimeMillis();
        }

        public String toString() {
            return "PendingItem{size=" + this.bytes.length + " age=" + (SystemClock.uptimeMillis() - this.mInstantiatedUptimeMillis) + "ms shortcut=" + this.shortcut.toInsecureString() + "}";
        }
    }

    public ShortcutBitmapSaver(ShortcutService shortcutService) {
        this.mService = shortcutService;
    }

    public boolean waitForAllSavesLocked() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        this.mExecutor.execute(new Runnable() { // from class: com.android.server.pm.ShortcutBitmapSaver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                countDownLatch.countDown();
            }
        });
        try {
            if (countDownLatch.await(5000L, TimeUnit.MILLISECONDS)) {
                return true;
            }
            this.mService.wtf("Timed out waiting on saving bitmaps.");
            return false;
        } catch (InterruptedException unused) {
            Slog.w(TAG, "interrupted");
            return false;
        }
    }

    public String getBitmapPathMayWaitLocked(ShortcutInfo shortcutInfo) {
        if (waitForAllSavesLocked() && shortcutInfo.hasIconFile()) {
            return shortcutInfo.getBitmapPath();
        }
        return null;
    }

    public void removeIcon(ShortcutInfo shortcutInfo) {
        shortcutInfo.setIconResourceId(0);
        shortcutInfo.setIconResName(null);
        shortcutInfo.setBitmapPath(null);
        shortcutInfo.setIconUri(null);
        shortcutInfo.clearFlags(35340);
    }

    public void saveBitmapLocked(ShortcutInfo shortcutInfo, int i, Bitmap.CompressFormat compressFormat, int i2) {
        Icon icon = shortcutInfo.getIcon();
        Objects.requireNonNull(icon);
        Bitmap bitmap = icon.getBitmap();
        if (bitmap == null) {
            Log.e(TAG, "Missing icon: " + shortcutInfo);
            return;
        }
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        try {
            try {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(threadPolicy).permitCustomSlowCalls().build());
                Bitmap shrinkBitmap = ShortcutService.shrinkBitmap(bitmap, i);
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(65536);
                    try {
                        if (!shrinkBitmap.compress(compressFormat, i2, byteArrayOutputStream)) {
                            Slog.wtf(TAG, "Unable to compress bitmap");
                        }
                        byteArrayOutputStream.flush();
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        byteArrayOutputStream.close();
                        byteArrayOutputStream.close();
                        StrictMode.setThreadPolicy(threadPolicy);
                        shortcutInfo.addFlags(2056);
                        if (icon.getType() == 5) {
                            shortcutInfo.addFlags(512);
                        }
                        PendingItem pendingItem = new PendingItem(shortcutInfo, byteArray);
                        synchronized (this.mPendingItems) {
                            this.mPendingItems.add(pendingItem);
                        }
                        this.mExecutor.execute(this.mRunnable);
                    } finally {
                    }
                } finally {
                    if (shrinkBitmap != bitmap) {
                        shrinkBitmap.recycle();
                    }
                }
            } catch (IOException | OutOfMemoryError | RuntimeException e) {
                Slog.wtf(TAG, "Unable to write bitmap to file", e);
                StrictMode.setThreadPolicy(threadPolicy);
            }
        } catch (Throwable th) {
            StrictMode.setThreadPolicy(threadPolicy);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        do {
        } while (processPendingItems());
    }

    private boolean processPendingItems() {
        ShortcutInfo shortcutInfo;
        Throwable th;
        File file = null;
        try {
            synchronized (this.mPendingItems) {
                if (this.mPendingItems.size() == 0) {
                    return false;
                }
                PendingItem pop = this.mPendingItems.pop();
                shortcutInfo = pop.shortcut;
                try {
                    if (!shortcutInfo.isIconPendingSave()) {
                        if (shortcutInfo.getBitmapPath() == null) {
                            removeIcon(shortcutInfo);
                        }
                        shortcutInfo.clearFlags(2048);
                        return true;
                    }
                    try {
                        ShortcutService.FileOutputStreamWithPath openIconFileForWrite = this.mService.openIconFileForWrite(shortcutInfo.getUserId(), shortcutInfo);
                        File file2 = openIconFileForWrite.getFile();
                        try {
                            openIconFileForWrite.write(pop.bytes);
                            IoUtils.closeQuietly(openIconFileForWrite);
                            shortcutInfo.setBitmapPath(file2.getAbsolutePath());
                            if (shortcutInfo.getBitmapPath() == null) {
                                removeIcon(shortcutInfo);
                            }
                            shortcutInfo.clearFlags(2048);
                            return true;
                        } catch (Throwable th2) {
                            IoUtils.closeQuietly(openIconFileForWrite);
                            throw th2;
                        }
                    } catch (IOException | RuntimeException e) {
                        Slog.e(TAG, "Unable to write bitmap to file", e);
                        if (0 != 0 && file.exists()) {
                            file.delete();
                        }
                        if (shortcutInfo.getBitmapPath() == null) {
                            removeIcon(shortcutInfo);
                        }
                        shortcutInfo.clearFlags(2048);
                        return true;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (shortcutInfo != null) {
                        if (shortcutInfo.getBitmapPath() == null) {
                            removeIcon(shortcutInfo);
                        }
                        shortcutInfo.clearFlags(2048);
                    }
                    throw th;
                }
            }
        } catch (Throwable th4) {
            shortcutInfo = null;
            th = th4;
        }
    }

    public void dumpLocked(PrintWriter printWriter, String str) {
        synchronized (this.mPendingItems) {
            int size = this.mPendingItems.size();
            printWriter.print(str);
            printWriter.println("Pending saves: Num=" + size + " Executor=" + this.mExecutor);
            for (PendingItem pendingItem : this.mPendingItems) {
                printWriter.print(str);
                printWriter.print("  ");
                printWriter.println(pendingItem);
            }
        }
    }
}
