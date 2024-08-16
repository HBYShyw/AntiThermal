package com.android.server.pm;

import android.app.admin.SecurityLog;
import android.content.Context;
import android.content.pm.ApkChecksum;
import android.content.pm.IOnChecksumsReadyListener;
import android.content.pm.PackageManagerInternal;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.os.BackgroundThread;
import com.android.server.oplus.osense.OsenseConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ProcessLoggingHandler extends Handler {
    private static final int CHECKSUM_TYPE = 8;
    private static final String TAG = "ProcessLoggingHandler";
    private final Executor mExecutor;
    private final ArrayMap<String, LoggingInfo> mLoggingInfo;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class LoggingInfo {
        public String apkHash = null;
        public List<Bundle> pendingLogEntries = new ArrayList();

        LoggingInfo() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessLoggingHandler() {
        super(BackgroundThread.getHandler().getLooper());
        this.mExecutor = new HandlerExecutor(this);
        this.mLoggingInfo = new ArrayMap<>();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logAppProcessStart(Context context, PackageManagerInternal packageManagerInternal, String str, String str2, String str3, int i, String str4, int i2) {
        boolean z;
        final LoggingInfo loggingInfo;
        Bundle bundle = new Bundle();
        bundle.putLong("startTimestamp", System.currentTimeMillis());
        bundle.putString("processName", str3);
        bundle.putInt("uid", i);
        bundle.putString("seinfo", str4);
        bundle.putInt(OsenseConstants.KEY_INTEGER_PID, i2);
        if (str == null) {
            enqueueSecurityLogEvent(bundle, "No APK");
            return;
        }
        synchronized (this.mLoggingInfo) {
            LoggingInfo loggingInfo2 = this.mLoggingInfo.get(str);
            z = loggingInfo2 == null;
            if (z) {
                loggingInfo2 = new LoggingInfo();
                this.mLoggingInfo.put(str, loggingInfo2);
            }
            loggingInfo = loggingInfo2;
        }
        synchronized (loggingInfo) {
            if (!TextUtils.isEmpty(loggingInfo.apkHash)) {
                enqueueSecurityLogEvent(bundle, loggingInfo.apkHash);
                return;
            }
            loggingInfo.pendingLogEntries.add(bundle);
            if (z) {
                try {
                    packageManagerInternal.requestChecksums(str2, false, 0, 8, (List) null, new IOnChecksumsReadyListener.Stub() { // from class: com.android.server.pm.ProcessLoggingHandler.1
                        public void onChecksumsReady(List<ApkChecksum> list) throws RemoteException {
                            ProcessLoggingHandler.this.processChecksums(loggingInfo, list);
                        }
                    }, context.getUserId(), this.mExecutor, this);
                } catch (Throwable th) {
                    Slog.e(TAG, "requestChecksums() failed", th);
                    enqueueProcessChecksum(loggingInfo, null);
                }
            }
        }
    }

    void processChecksums(LoggingInfo loggingInfo, List<ApkChecksum> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ApkChecksum apkChecksum = list.get(i);
            if (apkChecksum.getType() == 8) {
                processChecksum(loggingInfo, apkChecksum.getValue());
                return;
            }
        }
        Slog.e(TAG, "requestChecksums() failed to return SHA256, see logs for details.");
        processChecksum(loggingInfo, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enqueueProcessChecksum$0(LoggingInfo loggingInfo) {
        processChecksum(loggingInfo, null);
    }

    void enqueueProcessChecksum(final LoggingInfo loggingInfo, byte[] bArr) {
        post(new Runnable() { // from class: com.android.server.pm.ProcessLoggingHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ProcessLoggingHandler.this.lambda$enqueueProcessChecksum$0(loggingInfo);
            }
        });
    }

    void processChecksum(LoggingInfo loggingInfo, byte[] bArr) {
        String str;
        if (bArr != null) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bArr) {
                sb.append(String.format("%02x", Byte.valueOf(b)));
            }
            str = sb.toString();
        } else {
            str = "Failed to count APK hash";
        }
        synchronized (loggingInfo) {
            if (TextUtils.isEmpty(loggingInfo.apkHash)) {
                loggingInfo.apkHash = str;
                List<Bundle> list = loggingInfo.pendingLogEntries;
                loggingInfo.pendingLogEntries = null;
                if (list != null) {
                    Iterator<Bundle> it = list.iterator();
                    while (it.hasNext()) {
                        lambda$enqueueSecurityLogEvent$1(it.next(), str);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidateBaseApkHash(String str) {
        synchronized (this.mLoggingInfo) {
            this.mLoggingInfo.remove(str);
        }
    }

    void enqueueSecurityLogEvent(final Bundle bundle, final String str) {
        post(new Runnable() { // from class: com.android.server.pm.ProcessLoggingHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ProcessLoggingHandler.this.lambda$enqueueSecurityLogEvent$1(bundle, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: logSecurityLogEvent, reason: merged with bridge method [inline-methods] */
    public void lambda$enqueueSecurityLogEvent$1(Bundle bundle, String str) {
        long j = bundle.getLong("startTimestamp");
        String string = bundle.getString("processName");
        int i = bundle.getInt("uid");
        SecurityLog.writeEvent(210005, new Object[]{string, Long.valueOf(j), Integer.valueOf(i), Integer.valueOf(bundle.getInt(OsenseConstants.KEY_INTEGER_PID)), bundle.getString("seinfo"), str});
    }
}
