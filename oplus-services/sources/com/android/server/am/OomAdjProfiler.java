package com.android.server.am;

import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import com.android.internal.os.ProcessCpuTracker;
import com.android.internal.util.RingBuffer;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class OomAdjProfiler {
    private static final int MSG_UPDATE_CPU_TIME = 42;

    @GuardedBy({"this"})
    private boolean mLastScheduledOnBattery;

    @GuardedBy({"this"})
    private boolean mLastScheduledScreenOff;

    @GuardedBy({"this"})
    private long mLastSystemServerCpuTimeMs;

    @GuardedBy({"this"})
    private boolean mOnBattery;

    @GuardedBy({"this"})
    private CpuTimes mOomAdjRunTime;

    @GuardedBy({"this"})
    private long mOomAdjStartTimeUs;

    @GuardedBy({"this"})
    private boolean mOomAdjStarted;

    @GuardedBy({"this"})
    private boolean mScreenOff;

    @GuardedBy({"this"})
    private CpuTimes mSystemServerCpuTime;

    @GuardedBy({"this"})
    private boolean mSystemServerCpuTimeUpdateScheduled;

    @GuardedBy({"this"})
    private int mTotalOomAdjCalls;

    @GuardedBy({"this"})
    private long mTotalOomAdjRunTimeUs;
    private final ProcessCpuTracker mProcessCpuTracker = new ProcessCpuTracker(false);

    @GuardedBy({"this"})
    final RingBuffer<CpuTimes> mOomAdjRunTimesHist = new RingBuffer<>(CpuTimes.class, 10);

    @GuardedBy({"this"})
    final RingBuffer<CpuTimes> mSystemServerCpuTimesHist = new RingBuffer<>(CpuTimes.class, 10);

    public OomAdjProfiler() {
        this.mOomAdjRunTime = new CpuTimes();
        this.mSystemServerCpuTime = new CpuTimes();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void batteryPowerChanged(boolean z) {
        synchronized (this) {
            scheduleSystemServerCpuTimeUpdate();
            this.mOnBattery = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWakefulnessChanged(int i) {
        synchronized (this) {
            scheduleSystemServerCpuTimeUpdate();
            boolean z = true;
            if (i == 1) {
                z = false;
            }
            this.mScreenOff = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void oomAdjStarted() {
        synchronized (this) {
            this.mOomAdjStartTimeUs = SystemClock.currentThreadTimeMicro();
            this.mOomAdjStarted = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void oomAdjEnded() {
        synchronized (this) {
            if (this.mOomAdjStarted) {
                long currentThreadTimeMicro = SystemClock.currentThreadTimeMicro() - this.mOomAdjStartTimeUs;
                this.mOomAdjRunTime.addCpuTimeUs(currentThreadTimeMicro);
                this.mTotalOomAdjRunTimeUs += currentThreadTimeMicro;
                this.mTotalOomAdjCalls++;
            }
        }
    }

    private void scheduleSystemServerCpuTimeUpdate() {
        synchronized (this) {
            if (this.mSystemServerCpuTimeUpdateScheduled) {
                return;
            }
            this.mLastScheduledOnBattery = this.mOnBattery;
            this.mLastScheduledScreenOff = this.mScreenOff;
            this.mSystemServerCpuTimeUpdateScheduled = true;
            Message obtainMessage = PooledLambda.obtainMessage(new QuadConsumer() { // from class: com.android.server.am.OomAdjProfiler$$ExternalSyntheticLambda0
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                    ((OomAdjProfiler) obj).updateSystemServerCpuTime(((Boolean) obj2).booleanValue(), ((Boolean) obj3).booleanValue(), ((Boolean) obj4).booleanValue());
                }
            }, this, Boolean.valueOf(this.mLastScheduledOnBattery), Boolean.valueOf(this.mLastScheduledScreenOff), Boolean.TRUE);
            obtainMessage.setWhat(42);
            BackgroundThread.getHandler().sendMessage(obtainMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSystemServerCpuTime(boolean z, boolean z2, boolean z3) {
        long cpuTimeForPid = this.mProcessCpuTracker.getCpuTimeForPid(Process.myPid());
        synchronized (this) {
            if (z3) {
                if (!this.mSystemServerCpuTimeUpdateScheduled) {
                    return;
                }
            }
            this.mSystemServerCpuTime.addCpuTimeMs(cpuTimeForPid - this.mLastSystemServerCpuTimeMs, z, z2);
            this.mLastSystemServerCpuTimeMs = cpuTimeForPid;
            this.mSystemServerCpuTimeUpdateScheduled = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        synchronized (this) {
            if (this.mSystemServerCpuTime.isEmpty()) {
                return;
            }
            this.mOomAdjRunTimesHist.append(this.mOomAdjRunTime);
            this.mSystemServerCpuTimesHist.append(this.mSystemServerCpuTime);
            this.mOomAdjRunTime = new CpuTimes();
            this.mSystemServerCpuTime = new CpuTimes();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        synchronized (this) {
            if (this.mSystemServerCpuTimeUpdateScheduled) {
                BackgroundThread.getHandler().removeMessages(42);
                updateSystemServerCpuTime(this.mLastScheduledOnBattery, this.mLastScheduledScreenOff, false);
            } else {
                updateSystemServerCpuTime(this.mOnBattery, this.mScreenOff, false);
            }
            printWriter.println("System server and oomAdj runtimes (ms) in recent battery sessions (most recent first):");
            if (!this.mSystemServerCpuTime.isEmpty()) {
                printWriter.print("  ");
                printWriter.print("system_server=");
                printWriter.print(this.mSystemServerCpuTime);
                printWriter.print("  ");
                printWriter.print("oom_adj=");
                printWriter.println(this.mOomAdjRunTime);
            }
            CpuTimes[] cpuTimesArr = (CpuTimes[]) this.mSystemServerCpuTimesHist.toArray();
            CpuTimes[] cpuTimesArr2 = (CpuTimes[]) this.mOomAdjRunTimesHist.toArray();
            for (int length = cpuTimesArr2.length - 1; length >= 0; length--) {
                printWriter.print("  ");
                printWriter.print("system_server=");
                printWriter.print(cpuTimesArr[length]);
                printWriter.print("  ");
                printWriter.print("oom_adj=");
                printWriter.println(cpuTimesArr2[length]);
            }
            if (this.mTotalOomAdjCalls != 0) {
                printWriter.println("System server total oomAdj runtimes (us) since boot:");
                printWriter.print("  cpu time spent=");
                printWriter.print(this.mTotalOomAdjRunTimeUs);
                printWriter.print("  number of calls=");
                printWriter.print(this.mTotalOomAdjCalls);
                printWriter.print("  average=");
                printWriter.println(this.mTotalOomAdjRunTimeUs / this.mTotalOomAdjCalls);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class CpuTimes {
        private long mOnBatteryScreenOffTimeUs;
        private long mOnBatteryTimeUs;

        private CpuTimes() {
        }

        public void addCpuTimeMs(long j) {
            addCpuTimeUs(j * 1000, OomAdjProfiler.this.mOnBattery, OomAdjProfiler.this.mScreenOff);
        }

        public void addCpuTimeMs(long j, boolean z, boolean z2) {
            addCpuTimeUs(j * 1000, z, z2);
        }

        public void addCpuTimeUs(long j) {
            addCpuTimeUs(j, OomAdjProfiler.this.mOnBattery, OomAdjProfiler.this.mScreenOff);
        }

        public void addCpuTimeUs(long j, boolean z, boolean z2) {
            if (z) {
                this.mOnBatteryTimeUs += j;
                if (z2) {
                    this.mOnBatteryScreenOffTimeUs += j;
                }
            }
        }

        public boolean isEmpty() {
            return this.mOnBatteryTimeUs == 0 && this.mOnBatteryScreenOffTimeUs == 0;
        }

        public String toString() {
            return "[" + (this.mOnBatteryTimeUs / 1000) + "," + (this.mOnBatteryScreenOffTimeUs / 1000) + "]";
        }
    }
}
