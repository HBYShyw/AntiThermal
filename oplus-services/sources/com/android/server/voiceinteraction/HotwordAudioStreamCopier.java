package com.android.server.voiceinteraction;

import android.app.AppOpsManager;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.service.voice.HotwordAudioStream;
import android.service.voice.HotwordDetectedResult;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class HotwordAudioStreamCopier {

    @VisibleForTesting
    static final int DEFAULT_COPY_BUFFER_LENGTH_BYTES = 32768;

    @VisibleForTesting
    static final int MAX_COPY_BUFFER_LENGTH_BYTES = 65536;
    private static final String OP_MESSAGE = "Streaming hotword audio to VoiceInteractionService";
    private static final String TAG = "HotwordAudioStreamCopier";
    private static final String TASK_ID_PREFIX = "HotwordDetectedResult@";
    private static final String THREAD_NAME_PREFIX = "Copy-";
    private final AppOpsManager mAppOpsManager;
    private final int mDetectorType;
    private final ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private final String mVoiceInteractorAttributionTag;
    private final String mVoiceInteractorPackageName;
    private final int mVoiceInteractorUid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HotwordAudioStreamCopier(AppOpsManager appOpsManager, int i, int i2, String str, String str2) {
        this.mAppOpsManager = appOpsManager;
        this.mDetectorType = i;
        this.mVoiceInteractorUid = i2;
        this.mVoiceInteractorPackageName = str;
        this.mVoiceInteractorAttributionTag = str2;
    }

    public HotwordDetectedResult startCopyingAudioStreams(HotwordDetectedResult hotwordDetectedResult) throws IOException {
        int i;
        List<HotwordAudioStream> audioStreams = hotwordDetectedResult.getAudioStreams();
        if (audioStreams.isEmpty()) {
            HotwordMetricsLogger.writeAudioEgressEvent(this.mDetectorType, 7, this.mVoiceInteractorUid, 0, 0, 0);
            return hotwordDetectedResult;
        }
        int size = audioStreams.size();
        ArrayList arrayList = new ArrayList(audioStreams.size());
        ArrayList arrayList2 = new ArrayList(audioStreams.size());
        char c = 0;
        int i2 = 0;
        int i3 = 0;
        for (HotwordAudioStream hotwordAudioStream : audioStreams) {
            ParcelFileDescriptor[] createReliablePipe = ParcelFileDescriptor.createReliablePipe();
            ParcelFileDescriptor parcelFileDescriptor = createReliablePipe[c];
            ParcelFileDescriptor parcelFileDescriptor2 = createReliablePipe[1];
            arrayList.add(hotwordAudioStream.buildUpon().setAudioStreamParcelFileDescriptor(parcelFileDescriptor).build());
            PersistableBundle metadata = hotwordAudioStream.getMetadata();
            int parcelableSize = i2 + HotwordDetectedResult.getParcelableSize(metadata);
            if (metadata.containsKey("android.service.voice.key.AUDIO_STREAM_COPY_BUFFER_LENGTH_BYTES")) {
                i = metadata.getInt("android.service.voice.key.AUDIO_STREAM_COPY_BUFFER_LENGTH_BYTES", -1);
                if (i < 1 || i > 65536) {
                    HotwordMetricsLogger.writeAudioEgressEvent(this.mDetectorType, 9, this.mVoiceInteractorUid, 0, 0, size);
                    Slog.w(TAG, "Attempted to set an invalid copy buffer length (" + i + ") for: " + hotwordAudioStream);
                } else {
                    i3 += hotwordAudioStream.getInitialAudio().length;
                    arrayList2.add(new CopyTaskInfo(hotwordAudioStream.getAudioStreamParcelFileDescriptor(), parcelFileDescriptor2, i));
                    i2 = parcelableSize;
                    c = 0;
                }
            }
            i = 32768;
            i3 += hotwordAudioStream.getInitialAudio().length;
            arrayList2.add(new CopyTaskInfo(hotwordAudioStream.getAudioStreamParcelFileDescriptor(), parcelFileDescriptor2, i));
            i2 = parcelableSize;
            c = 0;
        }
        this.mExecutorService.execute(new HotwordDetectedResultCopyTask(TASK_ID_PREFIX + System.identityHashCode(hotwordDetectedResult), arrayList2, i2, i3));
        return hotwordDetectedResult.buildUpon().setAudioStreams(arrayList).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class CopyTaskInfo {
        private final int mCopyBufferLength;
        private final ParcelFileDescriptor mSink;
        private final ParcelFileDescriptor mSource;

        CopyTaskInfo(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, int i) {
            this.mSource = parcelFileDescriptor;
            this.mSink = parcelFileDescriptor2;
            this.mCopyBufferLength = i;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class HotwordDetectedResultCopyTask implements Runnable {
        private final List<CopyTaskInfo> mCopyTaskInfos;
        private final ExecutorService mExecutorService = Executors.newCachedThreadPool();
        private final String mResultTaskId;
        private final int mTotalInitialAudioSizeBytes;
        private final int mTotalMetadataSizeBytes;

        HotwordDetectedResultCopyTask(String str, List<CopyTaskInfo> list, int i, int i2) {
            this.mResultTaskId = str;
            this.mCopyTaskInfos = list;
            this.mTotalMetadataSizeBytes = i;
            this.mTotalInitialAudioSizeBytes = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            Thread.currentThread().setName(HotwordAudioStreamCopier.THREAD_NAME_PREFIX + this.mResultTaskId);
            int size = this.mCopyTaskInfos.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                CopyTaskInfo copyTaskInfo = this.mCopyTaskInfos.get(i);
                arrayList.add(new SingleAudioStreamCopyTask(this.mResultTaskId + "@" + i, copyTaskInfo.mSource, copyTaskInfo.mSink, copyTaskInfo.mCopyBufferLength, HotwordAudioStreamCopier.this.mDetectorType, HotwordAudioStreamCopier.this.mVoiceInteractorUid));
            }
            try {
                if (HotwordAudioStreamCopier.this.mAppOpsManager.startOpNoThrow("android:record_audio_hotword", HotwordAudioStreamCopier.this.mVoiceInteractorUid, HotwordAudioStreamCopier.this.mVoiceInteractorPackageName, HotwordAudioStreamCopier.this.mVoiceInteractorAttributionTag, HotwordAudioStreamCopier.OP_MESSAGE) == 0) {
                    try {
                        HotwordMetricsLogger.writeAudioEgressEvent(HotwordAudioStreamCopier.this.mDetectorType, 1, HotwordAudioStreamCopier.this.mVoiceInteractorUid, this.mTotalInitialAudioSizeBytes, this.mTotalMetadataSizeBytes, size);
                        this.mExecutorService.invokeAll(arrayList);
                        int i2 = this.mTotalInitialAudioSizeBytes;
                        Iterator it = arrayList.iterator();
                        int i3 = i2;
                        while (it.hasNext()) {
                            i3 += ((SingleAudioStreamCopyTask) it.next()).mTotalCopiedBytes;
                        }
                        Slog.i(HotwordAudioStreamCopier.TAG, this.mResultTaskId + ": Task was completed. Total bytes egressed: " + i3 + " (including " + this.mTotalInitialAudioSizeBytes + " bytes NOT streamed), total metadata bundle size bytes: " + this.mTotalMetadataSizeBytes);
                        HotwordMetricsLogger.writeAudioEgressEvent(HotwordAudioStreamCopier.this.mDetectorType, 2, HotwordAudioStreamCopier.this.mVoiceInteractorUid, i3, this.mTotalMetadataSizeBytes, size);
                    } catch (InterruptedException e) {
                        int i4 = this.mTotalInitialAudioSizeBytes;
                        Iterator it2 = arrayList.iterator();
                        int i5 = i4;
                        while (it2.hasNext()) {
                            i5 += ((SingleAudioStreamCopyTask) it2.next()).mTotalCopiedBytes;
                        }
                        HotwordMetricsLogger.writeAudioEgressEvent(HotwordAudioStreamCopier.this.mDetectorType, 3, HotwordAudioStreamCopier.this.mVoiceInteractorUid, i5, this.mTotalMetadataSizeBytes, size);
                        Slog.i(HotwordAudioStreamCopier.TAG, this.mResultTaskId + ": Task was interrupted. Total bytes egressed: " + i5 + " (including " + this.mTotalInitialAudioSizeBytes + " bytes NOT streamed), total metadata bundle size bytes: " + this.mTotalMetadataSizeBytes);
                        bestEffortPropagateError(e.getMessage());
                    }
                    return;
                }
                HotwordMetricsLogger.writeAudioEgressEvent(HotwordAudioStreamCopier.this.mDetectorType, 4, HotwordAudioStreamCopier.this.mVoiceInteractorUid, 0, 0, size);
                bestEffortPropagateError("Failed to obtain RECORD_AUDIO_HOTWORD permission for voice interactor with uid=" + HotwordAudioStreamCopier.this.mVoiceInteractorUid + " packageName=" + HotwordAudioStreamCopier.this.mVoiceInteractorPackageName + " attributionTag=" + HotwordAudioStreamCopier.this.mVoiceInteractorAttributionTag);
            } finally {
                HotwordAudioStreamCopier.this.mAppOpsManager.finishOp("android:record_audio_hotword", HotwordAudioStreamCopier.this.mVoiceInteractorUid, HotwordAudioStreamCopier.this.mVoiceInteractorPackageName, HotwordAudioStreamCopier.this.mVoiceInteractorAttributionTag);
            }
        }

        private void bestEffortPropagateError(String str) {
            try {
                for (CopyTaskInfo copyTaskInfo : this.mCopyTaskInfos) {
                    copyTaskInfo.mSource.closeWithError(str);
                    copyTaskInfo.mSink.closeWithError(str);
                }
                HotwordMetricsLogger.writeAudioEgressEvent(HotwordAudioStreamCopier.this.mDetectorType, 10, HotwordAudioStreamCopier.this.mVoiceInteractorUid, 0, 0, this.mCopyTaskInfos.size());
            } catch (IOException e) {
                Slog.e(HotwordAudioStreamCopier.TAG, this.mResultTaskId + ": Failed to propagate error", e);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class SingleAudioStreamCopyTask implements Callable<Void> {
        private final ParcelFileDescriptor mAudioSink;
        private final ParcelFileDescriptor mAudioSource;
        private final int mCopyBufferLength;
        private final int mDetectorType;
        private final String mStreamTaskId;
        private volatile int mTotalCopiedBytes = 0;
        private final int mUid;

        SingleAudioStreamCopyTask(String str, ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, int i, int i2, int i3) {
            this.mStreamTaskId = str;
            this.mAudioSource = parcelFileDescriptor;
            this.mAudioSink = parcelFileDescriptor2;
            this.mCopyBufferLength = i;
            this.mDetectorType = i2;
            this.mUid = i3;
        }

        /* JADX WARN: Code restructure failed: missing block: B:32:0x00bf, code lost:
        
            if (r3 != null) goto L31;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00c9  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x00ce  */
        /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.Thread] */
        /* JADX WARN: Type inference failed for: r2v1, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r2v2 */
        /* JADX WARN: Type inference failed for: r2v3 */
        /* JADX WARN: Type inference failed for: r2v4, types: [java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r2v5, types: [android.os.ParcelFileDescriptor$AutoCloseInputStream, java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r3v12 */
        /* JADX WARN: Type inference failed for: r3v2 */
        /* JADX WARN: Type inference failed for: r3v4, types: [java.io.OutputStream] */
        @Override // java.util.concurrent.Callable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Void call() throws Exception {
            ?? r3;
            ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream;
            ?? currentThread = Thread.currentThread();
            StringBuilder sb = new StringBuilder();
            sb.append(HotwordAudioStreamCopier.THREAD_NAME_PREFIX);
            String str = this.mStreamTaskId;
            sb.append(str);
            ?? sb2 = sb.toString();
            currentThread.setName(sb2);
            InputStream inputStream = null;
            try {
                try {
                    sb2 = new ParcelFileDescriptor.AutoCloseInputStream(this.mAudioSource);
                    try {
                        autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(this.mAudioSink);
                    } catch (IOException e) {
                        e = e;
                        autoCloseOutputStream = null;
                    } catch (Throwable th) {
                        th = th;
                        str = null;
                        inputStream = sb2;
                        r3 = str;
                        if (inputStream != null) {
                        }
                        if (r3 != 0) {
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    e = e2;
                    sb2 = 0;
                    autoCloseOutputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                    r3 = 0;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (r3 != 0) {
                        r3.close();
                    }
                    throw th;
                }
                try {
                    byte[] bArr = new byte[this.mCopyBufferLength];
                    while (true) {
                        if (Thread.interrupted()) {
                            Slog.e(HotwordAudioStreamCopier.TAG, this.mStreamTaskId + ": SingleAudioStreamCopyTask task was interrupted");
                            break;
                        }
                        int read = sb2.read(bArr);
                        if (read < 0) {
                            Slog.i(HotwordAudioStreamCopier.TAG, this.mStreamTaskId + ": Reached end of audio stream");
                            break;
                        }
                        if (read > 0) {
                            autoCloseOutputStream.write(bArr, 0, read);
                            this.mTotalCopiedBytes += read;
                        }
                    }
                    sb2.close();
                } catch (IOException e3) {
                    e = e3;
                    this.mAudioSource.closeWithError(e.getMessage());
                    this.mAudioSink.closeWithError(e.getMessage());
                    Slog.i(HotwordAudioStreamCopier.TAG, this.mStreamTaskId + ": Failed to copy audio stream", e);
                    HotwordMetricsLogger.writeAudioEgressEvent(this.mDetectorType, 10, this.mUid, 0, 0, 0);
                    if (sb2 != 0) {
                        sb2.close();
                    }
                }
                autoCloseOutputStream.close();
                return null;
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }
}
