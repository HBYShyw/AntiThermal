package com.android.server.wm;

import android.app.ActivityManagerInternal;
import android.content.pm.ApplicationInfo;
import android.util.ArrayMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class VisibleActivityProcessTracker {
    final ActivityTaskManagerService mAtms;

    @GuardedBy({"mProcMap"})
    private final ArrayMap<WindowProcessController, CpuTimeRecord> mProcMap = new ArrayMap<>();
    final Executor mBgExecutor = BackgroundThread.getExecutor();

    /* JADX INFO: Access modifiers changed from: package-private */
    public VisibleActivityProcessTracker(ActivityTaskManagerService activityTaskManagerService) {
        this.mAtms = activityTaskManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAnyActivityVisible(WindowProcessController windowProcessController) {
        CpuTimeRecord cpuTimeRecord = new CpuTimeRecord(windowProcessController);
        synchronized (this.mProcMap) {
            this.mProcMap.put(windowProcessController, cpuTimeRecord);
        }
        if (windowProcessController.hasResumedActivity()) {
            cpuTimeRecord.mShouldGetCpuTime = true;
            this.mBgExecutor.execute(cpuTimeRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAllActivitiesInvisible(WindowProcessController windowProcessController) {
        CpuTimeRecord removeProcess = removeProcess(windowProcessController);
        if (removeProcess == null || !removeProcess.mShouldGetCpuTime) {
            return;
        }
        this.mBgExecutor.execute(removeProcess);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityResumedWhileVisible(WindowProcessController windowProcessController) {
        CpuTimeRecord cpuTimeRecord;
        synchronized (this.mProcMap) {
            cpuTimeRecord = this.mProcMap.get(windowProcessController);
        }
        if (cpuTimeRecord == null || cpuTimeRecord.mShouldGetCpuTime) {
            return;
        }
        cpuTimeRecord.mShouldGetCpuTime = true;
        this.mBgExecutor.execute(cpuTimeRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasResumedActivity(int i) {
        return match(i, new Predicate() { // from class: com.android.server.wm.VisibleActivityProcessTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((WindowProcessController) obj).hasResumedActivity();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasVisibleActivity(int i) {
        return match(i, null);
    }

    private boolean match(int i, Predicate<WindowProcessController> predicate) {
        synchronized (this.mProcMap) {
            for (int size = this.mProcMap.size() - 1; size >= 0; size--) {
                WindowProcessController keyAt = this.mProcMap.keyAt(size);
                if (keyAt.mUid == i && (predicate == null || predicate.test(keyAt))) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CpuTimeRecord removeProcess(WindowProcessController windowProcessController) {
        CpuTimeRecord remove;
        synchronized (this.mProcMap) {
            remove = this.mProcMap.remove(windowProcessController);
        }
        return remove;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str + "VisibleActivityProcess:[");
        synchronized (this.mProcMap) {
            for (int size = this.mProcMap.size() - 1; size >= 0; size += -1) {
                printWriter.print(" " + this.mProcMap.keyAt(size));
            }
        }
        printWriter.println("]");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class CpuTimeRecord implements Runnable {
        private long mCpuTime;
        private boolean mHasStartCpuTime;
        private final WindowProcessController mProc;
        boolean mShouldGetCpuTime;

        CpuTimeRecord(WindowProcessController windowProcessController) {
            this.mProc = windowProcessController;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mProc.getPid() == 0) {
                return;
            }
            if (!this.mHasStartCpuTime) {
                this.mHasStartCpuTime = true;
                this.mCpuTime = this.mProc.getCpuTime();
                return;
            }
            long cpuTime = this.mProc.getCpuTime() - this.mCpuTime;
            if (cpuTime > 0) {
                ActivityManagerInternal activityManagerInternal = VisibleActivityProcessTracker.this.mAtms.mAmInternal;
                ApplicationInfo applicationInfo = this.mProc.mInfo;
                activityManagerInternal.updateForegroundTimeIfOnBattery(applicationInfo.packageName, applicationInfo.uid, cpuTime);
            }
        }
    }
}
