package com.android.server.wm;

import android.content.Context;
import android.os.Environment;
import android.os.StrictMode;
import android.util.AtomicFile;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.wm.PersisterQueue;
import com.android.server.wm.nano.WindowManagerProtos;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class LetterboxConfigurationPersister {

    @VisibleForTesting
    static final String LETTERBOX_CONFIGURATION_FILENAME = "letterbox_config";
    private static final String TAG = "WindowManager";
    private final Consumer<String> mCompletionCallback;
    private final AtomicFile mConfigurationFile;
    private final Context mContext;
    private final Supplier<Integer> mDefaultBookModeReachabilitySupplier;
    private final Supplier<Integer> mDefaultHorizontalReachabilitySupplier;
    private final Supplier<Integer> mDefaultTabletopModeReachabilitySupplier;
    private final Supplier<Integer> mDefaultVerticalReachabilitySupplier;
    private volatile int mLetterboxPositionForBookModeReachability;
    private volatile int mLetterboxPositionForHorizontalReachability;
    private volatile int mLetterboxPositionForTabletopModeReachability;
    private volatile int mLetterboxPositionForVerticalReachability;
    private final PersisterQueue mPersisterQueue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LetterboxConfigurationPersister(Context context, Supplier<Integer> supplier, Supplier<Integer> supplier2, Supplier<Integer> supplier3, Supplier<Integer> supplier4) {
        this(context, supplier, supplier2, supplier3, supplier4, Environment.getDataSystemDirectory(), new PersisterQueue(), null);
    }

    @VisibleForTesting
    LetterboxConfigurationPersister(Context context, Supplier<Integer> supplier, Supplier<Integer> supplier2, Supplier<Integer> supplier3, Supplier<Integer> supplier4, File file, PersisterQueue persisterQueue, Consumer<String> consumer) {
        this.mContext = context.createDeviceProtectedStorageContext();
        this.mDefaultHorizontalReachabilitySupplier = supplier;
        this.mDefaultVerticalReachabilitySupplier = supplier2;
        this.mDefaultBookModeReachabilitySupplier = supplier3;
        this.mDefaultTabletopModeReachabilitySupplier = supplier4;
        this.mCompletionCallback = consumer;
        this.mConfigurationFile = new AtomicFile(new File(file, LETTERBOX_CONFIGURATION_FILENAME));
        this.mPersisterQueue = persisterQueue;
        runWithDiskReadsThreadPolicy(new Runnable() { // from class: com.android.server.wm.LetterboxConfigurationPersister$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LetterboxConfigurationPersister.this.readCurrentConfiguration();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        this.mPersisterQueue.startPersisting();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxPositionForHorizontalReachability(boolean z) {
        if (z) {
            return this.mLetterboxPositionForBookModeReachability;
        }
        return this.mLetterboxPositionForHorizontalReachability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLetterboxPositionForVerticalReachability(boolean z) {
        if (z) {
            return this.mLetterboxPositionForTabletopModeReachability;
        }
        return this.mLetterboxPositionForVerticalReachability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxPositionForHorizontalReachability(boolean z, int i) {
        if (z) {
            if (this.mLetterboxPositionForBookModeReachability != i) {
                this.mLetterboxPositionForBookModeReachability = i;
                updateConfiguration();
                return;
            }
            return;
        }
        if (this.mLetterboxPositionForHorizontalReachability != i) {
            this.mLetterboxPositionForHorizontalReachability = i;
            updateConfiguration();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLetterboxPositionForVerticalReachability(boolean z, int i) {
        if (z) {
            if (this.mLetterboxPositionForTabletopModeReachability != i) {
                this.mLetterboxPositionForTabletopModeReachability = i;
                updateConfiguration();
                return;
            }
            return;
        }
        if (this.mLetterboxPositionForVerticalReachability != i) {
            this.mLetterboxPositionForVerticalReachability = i;
            updateConfiguration();
        }
    }

    @VisibleForTesting
    void useDefaultValue() {
        this.mLetterboxPositionForHorizontalReachability = this.mDefaultHorizontalReachabilitySupplier.get().intValue();
        this.mLetterboxPositionForVerticalReachability = this.mDefaultVerticalReachabilitySupplier.get().intValue();
        this.mLetterboxPositionForBookModeReachability = this.mDefaultBookModeReachabilitySupplier.get().intValue();
        this.mLetterboxPositionForTabletopModeReachability = this.mDefaultTabletopModeReachabilitySupplier.get().intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x0047 -> B:11:0x004d). Please report as a decompilation issue!!! */
    public void readCurrentConfiguration() {
        if (!this.mConfigurationFile.exists()) {
            useDefaultValue();
            return;
        }
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    fileInputStream = this.mConfigurationFile.openRead();
                    WindowManagerProtos.LetterboxProto parseFrom = WindowManagerProtos.LetterboxProto.parseFrom(readInputStream(fileInputStream));
                    this.mLetterboxPositionForHorizontalReachability = parseFrom.letterboxPositionForHorizontalReachability;
                    this.mLetterboxPositionForVerticalReachability = parseFrom.letterboxPositionForVerticalReachability;
                    this.mLetterboxPositionForBookModeReachability = parseFrom.letterboxPositionForBookModeReachability;
                    this.mLetterboxPositionForTabletopModeReachability = parseFrom.letterboxPositionForTabletopModeReachability;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (Throwable th) {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            useDefaultValue();
                            Slog.e(TAG, "Error reading from LetterboxConfigurationPersister ", e);
                        }
                    }
                    throw th;
                }
            } catch (IOException e2) {
                Slog.e(TAG, "Error reading from LetterboxConfigurationPersister. Using default values!", e2);
                useDefaultValue();
                if (fileInputStream == null) {
                } else {
                    fileInputStream.close();
                }
            }
        } catch (IOException e3) {
            useDefaultValue();
            Slog.e(TAG, "Error reading from LetterboxConfigurationPersister ", e3);
        }
    }

    private void updateConfiguration() {
        this.mPersisterQueue.addItem(new UpdateValuesCommand(this.mConfigurationFile, this.mLetterboxPositionForHorizontalReachability, this.mLetterboxPositionForVerticalReachability, this.mLetterboxPositionForBookModeReachability, this.mLetterboxPositionForTabletopModeReachability, this.mCompletionCallback), true);
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = new byte[1024];
            int read = inputStream.read(bArr);
            while (read > 0) {
                byteArrayOutputStream.write(bArr, 0, read);
                read = inputStream.read(bArr);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            byteArrayOutputStream.close();
        }
    }

    private void runWithDiskReadsThreadPolicy(Runnable runnable) {
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitDiskReads().build());
        runnable.run();
        StrictMode.setThreadPolicy(threadPolicy);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class UpdateValuesCommand implements PersisterQueue.WriteQueueItem<UpdateValuesCommand> {
        private final int mBookModeReachability;
        private final AtomicFile mFileToUpdate;
        private final int mHorizontalReachability;
        private final Consumer<String> mOnComplete;
        private final int mTabletopModeReachability;
        private final int mVerticalReachability;

        UpdateValuesCommand(AtomicFile atomicFile, int i, int i2, int i3, int i4, Consumer<String> consumer) {
            this.mFileToUpdate = atomicFile;
            this.mHorizontalReachability = i;
            this.mVerticalReachability = i2;
            this.mBookModeReachability = i3;
            this.mTabletopModeReachability = i4;
            this.mOnComplete = consumer;
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            Consumer<String> consumer;
            WindowManagerProtos.LetterboxProto letterboxProto = new WindowManagerProtos.LetterboxProto();
            letterboxProto.letterboxPositionForHorizontalReachability = this.mHorizontalReachability;
            letterboxProto.letterboxPositionForVerticalReachability = this.mVerticalReachability;
            letterboxProto.letterboxPositionForBookModeReachability = this.mBookModeReachability;
            letterboxProto.letterboxPositionForTabletopModeReachability = this.mTabletopModeReachability;
            byte[] byteArray = WindowManagerProtos.LetterboxProto.toByteArray(letterboxProto);
            FileOutputStream fileOutputStream = null;
            try {
                try {
                    fileOutputStream = this.mFileToUpdate.startWrite();
                    fileOutputStream.write(byteArray);
                    this.mFileToUpdate.finishWrite(fileOutputStream);
                    consumer = this.mOnComplete;
                    if (consumer == null) {
                        return;
                    }
                } catch (IOException e) {
                    this.mFileToUpdate.failWrite(fileOutputStream);
                    Slog.e(LetterboxConfigurationPersister.TAG, "Error writing to LetterboxConfigurationPersister. Using default values!", e);
                    consumer = this.mOnComplete;
                    if (consumer == null) {
                        return;
                    }
                }
                consumer.accept("UpdateValuesCommand");
            } catch (Throwable th) {
                Consumer<String> consumer2 = this.mOnComplete;
                if (consumer2 != null) {
                    consumer2.accept("UpdateValuesCommand");
                }
                throw th;
            }
        }
    }
}
