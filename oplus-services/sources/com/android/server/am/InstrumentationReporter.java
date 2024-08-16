package com.android.server.am;

import android.app.IInstrumentationWatcher;
import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.Process;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class InstrumentationReporter {
    static final boolean DEBUG = false;
    static final int REPORT_TYPE_FINISHED = 1;
    static final int REPORT_TYPE_STATUS = 0;
    static final String TAG = "ActivityManager";
    final Object mLock = new Object();
    ArrayList<Report> mPendingReports;
    Thread mThread;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class MyThread extends Thread {
        public MyThread() {
            super("InstrumentationReporter");
        }

        /* JADX WARN: Code restructure failed: missing block: B:13:0x001b, code lost:
        
            r1 = 0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x0020, code lost:
        
            if (r1 >= r4.size()) goto L41;
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x0022, code lost:
        
            r2 = r4.get(r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x002a, code lost:
        
            if (r2.mType != 0) goto L19;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x002c, code lost:
        
            r2.mWatcher.instrumentationStatus(r2.mName, r2.mResultCode, r2.mResults);
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0038, code lost:
        
            r2.mWatcher.instrumentationFinished(r2.mName, r2.mResultCode, r2.mResults);
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x0044, code lost:
        
            android.util.Slog.i("ActivityManager", "Failure reporting to instrumentation watcher: comp=" + r2.mName + " results=" + r2.mResults);
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x006b, code lost:
        
            r8.this$0.mLock.wait(com.android.server.IDeviceIdleControllerExt.ADVANCE_TIME);
         */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            boolean z;
            Process.setThreadPriority(0);
            while (true) {
                z = false;
                while (true) {
                    synchronized (InstrumentationReporter.this.mLock) {
                        InstrumentationReporter instrumentationReporter = InstrumentationReporter.this;
                        ArrayList<Report> arrayList = instrumentationReporter.mPendingReports;
                        instrumentationReporter.mPendingReports = null;
                        if (arrayList != null && !arrayList.isEmpty()) {
                            break;
                        }
                        InstrumentationReporter.this.mThread = null;
                        return;
                    }
                    z = true;
                }
            }
            z = true;
            int i = i + 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class Report {
        final ComponentName mName;
        final int mResultCode;
        final Bundle mResults;
        final int mType;
        final IInstrumentationWatcher mWatcher;

        Report(int i, IInstrumentationWatcher iInstrumentationWatcher, ComponentName componentName, int i2, Bundle bundle) {
            this.mType = i;
            this.mWatcher = iInstrumentationWatcher;
            this.mName = componentName;
            this.mResultCode = i2;
            this.mResults = bundle;
            Binder.allowBlocking(iInstrumentationWatcher.asBinder());
        }
    }

    public void reportStatus(IInstrumentationWatcher iInstrumentationWatcher, ComponentName componentName, int i, Bundle bundle) {
        report(new Report(0, iInstrumentationWatcher, componentName, i, bundle));
    }

    public void reportFinished(IInstrumentationWatcher iInstrumentationWatcher, ComponentName componentName, int i, Bundle bundle) {
        report(new Report(1, iInstrumentationWatcher, componentName, i, bundle));
    }

    private void report(Report report) {
        synchronized (this.mLock) {
            if (this.mThread == null) {
                MyThread myThread = new MyThread();
                this.mThread = myThread;
                myThread.start();
            }
            if (this.mPendingReports == null) {
                this.mPendingReports = new ArrayList<>();
            }
            this.mPendingReports.add(report);
            this.mLock.notifyAll();
        }
    }
}
