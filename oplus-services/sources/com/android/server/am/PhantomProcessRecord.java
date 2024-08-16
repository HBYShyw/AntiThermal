package com.android.server.am;

import android.os.Handler;
import android.os.Process;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Slog;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PhantomProcessRecord {
    static final String TAG = "ActivityManager";
    long mCurrentCputime;
    long mLastCputime;
    final Object mLock;
    final Consumer<PhantomProcessRecord> mOnKillListener;
    final int mPid;
    final FileDescriptor mPidFd;
    final int mPpid;
    final String mProcessName;
    final ActivityManagerService mService;
    String mStringName;
    final int mUid;
    int mUpdateSeq;
    boolean mZombie;
    static final long[] LONG_OUT = new long[1];
    static final int[] LONG_FORMAT = {8202};
    private Runnable mProcKillTimer = new Runnable() { // from class: com.android.server.am.PhantomProcessRecord.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (PhantomProcessRecord.this.mLock) {
                Slog.w("ActivityManager", "Process " + toString() + " is still alive after " + PhantomProcessRecord.this.mService.mConstants.mProcessKillTimeoutMs + "ms");
                PhantomProcessRecord phantomProcessRecord = PhantomProcessRecord.this;
                phantomProcessRecord.mZombie = true;
                phantomProcessRecord.onProcDied(false);
            }
        }
    };
    boolean mKilled = false;
    int mAdj = -1000;
    final long mKnownSince = SystemClock.elapsedRealtime();
    final Handler mKillHandler = ProcessList.sKillHandler;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PhantomProcessRecord(String str, int i, int i2, int i3, ActivityManagerService activityManagerService, Consumer<PhantomProcessRecord> consumer) throws IllegalStateException {
        this.mProcessName = str;
        this.mUid = i;
        this.mPid = i2;
        this.mPpid = i3;
        this.mService = activityManagerService;
        this.mLock = activityManagerService.mPhantomProcessList.mLock;
        this.mOnKillListener = consumer;
        if (Process.supportsPidFd()) {
            StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
            try {
                try {
                    FileDescriptor openPidFd = Process.openPidFd(i2, 0);
                    this.mPidFd = openPidFd;
                    if (openPidFd != null) {
                        return;
                    } else {
                        throw new IllegalStateException();
                    }
                } catch (IOException e) {
                    Slog.w("ActivityManager", "Unable to open process " + i2 + ", it might be gone");
                    IllegalStateException illegalStateException = new IllegalStateException();
                    illegalStateException.initCause(e);
                    throw illegalStateException;
                }
            } finally {
                StrictMode.setThreadPolicy(allowThreadDiskReads);
            }
        }
        this.mPidFd = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void killLocked(String str, boolean z) {
        if (this.mKilled) {
            return;
        }
        Trace.traceBegin(64L, "kill");
        if (z || this.mUid == this.mService.mCurOomAdjUid) {
            this.mService.reportUidInfoMessageLocked("ActivityManager", "Killing " + toString() + ": " + str, this.mUid);
        }
        int uidForPid = Process.getUidForPid(this.mPid);
        int i = this.mUid;
        if (uidForPid != i) {
            return;
        }
        if (this.mPid > 0) {
            EventLog.writeEvent(EventLogTags.AM_KILL, Integer.valueOf(UserHandle.getUserId(i)), Integer.valueOf(this.mPid), this.mProcessName, Integer.valueOf(this.mAdj), str);
            if (!Process.supportsPidFd()) {
                onProcDied(false);
            } else {
                this.mKillHandler.postDelayed(this.mProcKillTimer, this, this.mService.mConstants.mProcessKillTimeoutMs);
            }
            Process.killProcessQuiet(this.mPid);
            ProcessList.killProcessGroup(this.mUid, this.mPid);
        }
        this.mKilled = true;
        Trace.traceEnd(64L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void updateAdjLocked() {
        String str = "/proc/" + this.mPid + "/oom_score_adj";
        int[] iArr = LONG_FORMAT;
        long[] jArr = LONG_OUT;
        if (Process.readProcFile(str, iArr, null, jArr, null)) {
            this.mAdj = (int) jArr[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void onProcDied(boolean z) {
        if (z) {
            Slog.i("ActivityManager", "Process " + toString() + " died");
        }
        this.mKillHandler.removeCallbacks(this.mProcKillTimer, this);
        Consumer<PhantomProcessRecord> consumer = this.mOnKillListener;
        if (consumer != null) {
            consumer.accept(this);
        }
    }

    public String toString() {
        String str = this.mStringName;
        if (str != null) {
            return str;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("PhantomProcessRecord {");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.mPid);
        sb.append(':');
        sb.append(this.mPpid);
        sb.append(':');
        sb.append(this.mProcessName);
        sb.append('/');
        int i = this.mUid;
        if (i < 10000) {
            sb.append(i);
        } else {
            sb.append('u');
            sb.append(UserHandle.getUserId(this.mUid));
            int appId = UserHandle.getAppId(this.mUid);
            if (appId >= 10000) {
                sb.append('a');
                sb.append(appId - 10000);
            } else {
                sb.append('s');
                sb.append(appId);
            }
            if (appId >= 99000 && appId <= 99999) {
                sb.append('i');
                sb.append(appId - 99000);
            }
        }
        sb.append('}');
        String sb2 = sb.toString();
        this.mStringName = sb2;
        return sb2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        printWriter.print(str);
        printWriter.print("user #");
        printWriter.print(UserHandle.getUserId(this.mUid));
        printWriter.print(" uid=");
        printWriter.print(this.mUid);
        printWriter.print(" pid=");
        printWriter.print(this.mPid);
        printWriter.print(" ppid=");
        printWriter.print(this.mPpid);
        printWriter.print(" knownSince=");
        TimeUtils.formatDuration(this.mKnownSince, elapsedRealtime, printWriter);
        printWriter.print(" killed=");
        printWriter.println(this.mKilled);
        printWriter.print(str);
        printWriter.print("lastCpuTime=");
        printWriter.print(this.mLastCputime);
        if (this.mLastCputime > 0) {
            printWriter.print(" timeUsed=");
            TimeUtils.formatDuration(this.mCurrentCputime - this.mLastCputime, printWriter);
        }
        printWriter.print(" oom adj=");
        printWriter.print(this.mAdj);
        printWriter.print(" seq=");
        printWriter.println(this.mUpdateSeq);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean equals(String str, int i, int i2) {
        return this.mUid == i && this.mPid == i2 && TextUtils.equals(this.mProcessName, str);
    }
}
