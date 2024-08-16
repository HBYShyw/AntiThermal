package com.android.server.am;

import android.app.ApplicationErrorReport;
import android.hardware.tv.hdmi.cec.CecDeviceType;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructTimeval;
import android.system.UnixSocketAddress;
import android.util.Slog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.InterruptedIOException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class NativeCrashListener extends Thread {
    static final boolean DEBUG = false;
    static final String DEBUGGERD_SOCKET_PATH = "/data/system/ndebugsocket";
    static final boolean MORE_DEBUG = false;
    static final long SOCKET_TIMEOUT_MILLIS = 10000;
    static final String TAG = "NativeCrashListener";
    final ActivityManagerService mAm;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class NativeCrashReporter extends Thread {
        ProcessRecord mApp;
        String mCrashReport;
        boolean mGwpAsanRecoverableCrash;
        int mSignal;

        NativeCrashReporter(ProcessRecord processRecord, int i, boolean z, String str) {
            super("NativeCrashReport");
            this.mApp = processRecord;
            this.mSignal = i;
            this.mGwpAsanRecoverableCrash = z;
            this.mCrashReport = str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                ApplicationErrorReport.CrashInfo crashInfo = new ApplicationErrorReport.CrashInfo();
                crashInfo.exceptionClassName = "Native crash";
                crashInfo.exceptionMessage = Os.strsignal(this.mSignal);
                crashInfo.throwFileName = "unknown";
                crashInfo.throwClassName = "unknown";
                crashInfo.throwMethodName = "unknown";
                crashInfo.stackTrace = this.mCrashReport;
                ActivityManagerService activityManagerService = NativeCrashListener.this.mAm;
                String str = this.mGwpAsanRecoverableCrash ? "native_recoverable_crash" : "native_crash";
                ProcessRecord processRecord = this.mApp;
                activityManagerService.handleApplicationCrashInner(str, processRecord, processRecord.processName, crashInfo);
            } catch (Exception e) {
                Slog.e(NativeCrashListener.TAG, "Unable to report native crash", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NativeCrashListener(ActivityManagerService activityManagerService) {
        this.mAm = activityManagerService;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        byte[] bArr = new byte[1];
        File file = new File(DEBUGGERD_SOCKET_PATH);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileDescriptor socket = Os.socket(OsConstants.AF_UNIX, OsConstants.SOCK_STREAM, 0);
            Os.bind(socket, UnixSocketAddress.createFileSystem(DEBUGGERD_SOCKET_PATH));
            Os.listen(socket, 1);
            Os.chmod(DEBUGGERD_SOCKET_PATH, 511);
            while (true) {
                FileDescriptor fileDescriptor = null;
                try {
                    try {
                        fileDescriptor = Os.accept(socket, null);
                        if (fileDescriptor != null) {
                            consumeNativeCrashData(fileDescriptor);
                        }
                    } catch (Exception e) {
                        Slog.w(TAG, "Error handling connection", e);
                        if (fileDescriptor != null) {
                            Os.write(fileDescriptor, bArr, 0, 1);
                        }
                    }
                    if (fileDescriptor != null) {
                        Os.write(fileDescriptor, bArr, 0, 1);
                        try {
                            Os.close(fileDescriptor);
                        } catch (ErrnoException unused) {
                        }
                    }
                } catch (Throwable th) {
                    if (fileDescriptor != null) {
                        try {
                            Os.write(fileDescriptor, bArr, 0, 1);
                        } catch (Exception unused2) {
                        }
                        try {
                            Os.close(fileDescriptor);
                        } catch (ErrnoException unused3) {
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e2) {
            Slog.e(TAG, "Unable to init native debug socket!", e2);
        }
    }

    static int unpackInt(byte[] bArr, int i) {
        return (bArr[i + 3] & CecDeviceType.INACTIVE) | ((bArr[i] & CecDeviceType.INACTIVE) << 24) | ((bArr[i + 1] & CecDeviceType.INACTIVE) << 16) | ((bArr[i + 2] & CecDeviceType.INACTIVE) << 8);
    }

    static int readExactly(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        int i3 = 0;
        while (i2 > 0) {
            int read = Os.read(fileDescriptor, bArr, i + i3, i2);
            if (read <= 0) {
                return -1;
            }
            i2 -= read;
            i3 += read;
        }
        return i3;
    }

    void consumeNativeCrashData(FileDescriptor fileDescriptor) {
        ProcessRecord processRecord;
        byte[] bArr = new byte[4096];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
        try {
            StructTimeval fromMillis = StructTimeval.fromMillis(10000L);
            Os.setsockoptTimeval(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_RCVTIMEO, fromMillis);
            Os.setsockoptTimeval(fileDescriptor, OsConstants.SOL_SOCKET, OsConstants.SO_SNDTIMEO, fromMillis);
            if (readExactly(fileDescriptor, bArr, 0, 9) != 9) {
                Slog.e(TAG, "Unable to read from debuggerd");
                return;
            }
            int unpackInt = unpackInt(bArr, 0);
            int unpackInt2 = unpackInt(bArr, 4);
            boolean z = bArr[8] != 0;
            if (unpackInt < 0) {
                Slog.e(TAG, "Bogus pid!");
                return;
            }
            synchronized (this.mAm.mPidsSelfLocked) {
                processRecord = this.mAm.mPidsSelfLocked.get(unpackInt);
            }
            if (processRecord == null) {
                Slog.w(TAG, "Couldn't find ProcessRecord for pid " + unpackInt);
                return;
            }
            if (processRecord.isPersistent()) {
                return;
            }
            while (true) {
                int read = Os.read(fileDescriptor, bArr, 0, 4096);
                if (read > 0) {
                    int i = read - 1;
                    if (bArr[i] == 0) {
                        byteArrayOutputStream.write(bArr, 0, i);
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                if (read <= 0) {
                    break;
                }
            }
            if (!z) {
                ActivityManagerService activityManagerService = this.mAm;
                ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        ActivityManagerGlobalLock activityManagerGlobalLock = this.mAm.mProcLock;
                        ActivityManagerService.boostPriorityForProcLockedSection();
                        synchronized (activityManagerGlobalLock) {
                            try {
                                processRecord.mErrorState.setCrashing(true);
                                processRecord.mErrorState.setForceCrashReport(true);
                            } catch (Throwable th) {
                                ActivityManagerService.resetPriorityAfterProcLockedSection();
                                throw th;
                            }
                        }
                        ActivityManagerService.resetPriorityAfterProcLockedSection();
                    } catch (Throwable th2) {
                        ActivityManagerService.resetPriorityAfterLockedSection();
                        throw th2;
                    }
                }
                ActivityManagerService.resetPriorityAfterLockedSection();
            }
            new NativeCrashReporter(processRecord, unpackInt2, z, new String(byteArrayOutputStream.toByteArray(), "UTF-8")).start();
        } catch (Exception e) {
            Slog.e(TAG, "Exception dealing with report", e);
        }
    }
}
