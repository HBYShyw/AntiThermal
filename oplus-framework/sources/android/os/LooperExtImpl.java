package android.os;

import android.app.ActivityThread;
import android.app.OplusActivityManager;
import android.util.Log;

/* loaded from: classes.dex */
public class LooperExtImpl implements ILooperExt {
    private static final String ANR_LOG = "ANR_LOG";
    private static final long LOOPER_DISPATCH_TIMEOUT = 1500;
    private static final long LOOPER_MONITOR_TIMEOUT = 100;
    private Looper mBase;
    private Message mCurrentMsg;
    private boolean mDebug;
    private long mDebugWallTime;
    private long mOPlusWallTime;
    private OplusActivityManager mOplusActivityManager;
    private long mTimeFirst;
    private LooperMsgTimeTracker mMsgTimeTracker = null;
    private LooperMessageSuperviser mLooperMessageSuperviser = null;
    private OplusLooperMsgDispatcher mMsgDispatcher = null;
    private OplusLooperEntry mEntry = new OplusLooperEntry();
    private int mCpuload = 0;
    private int mLevel = 1;
    private long mOplusTime = 0;

    public LooperExtImpl(Object base) {
        this.mBase = (Looper) base;
    }

    public void initLoop(String threadName) {
        boolean mainThread = "main".equals(threadName);
        if (mainThread) {
            this.mLevel = SystemProperties.getInt("persist.sys.looper.level", 1);
            this.mDebug = SystemProperties.getBoolean("persist.sys.looper.dump", false);
            if (this.mOplusActivityManager == null) {
                this.mOplusActivityManager = new OplusActivityManager();
            }
            this.mLooperMessageSuperviser = new LooperMessageSuperviser();
            try {
                this.mCpuload = this.mOplusActivityManager.getLoopCpuLoad();
            } catch (RemoteException e) {
                Log.w("Looper", "unable to ", e);
            }
            OplusSigProtector.init();
            TheiaSigprotector.initSigprotector(this.mBase);
        }
    }

    public void startLooperMessageMonitor(Message msg, int pid, boolean threadExist) {
        if (this.mLooperMessageSuperviser != null && threadExist) {
            this.mEntry.upDataLooperEntry(msg);
            this.mTimeFirst = SystemClock.uptimeMillis();
            this.mLooperMessageSuperviser.setForebackStatus();
            this.mLooperMessageSuperviser.beginLooperMessage(this.mEntry, msg, pid, this.mLevel, this.mCpuload);
        }
    }

    public void stopLooperMessageMonitor(Message msg, int pid, boolean threadExist) {
        if (this.mLooperMessageSuperviser != null && threadExist) {
            this.mOplusTime = SystemClock.uptimeMillis();
            this.mEntry.updateLoopTime();
            if (this.mEntry.mWalltime > LOOPER_DISPATCH_TIMEOUT || this.mDebug) {
                Log.e(ANR_LOG, this.mEntry.toString());
            }
            this.mLooperMessageSuperviser.setForebackStatus();
            this.mLooperMessageSuperviser.endLooperMessage(this.mEntry, msg, this.mTimeFirst, pid, this.mLevel, this.mCpuload);
        }
    }

    public void dumpMessage() {
        Looper looper = this.mBase;
        if (looper == null) {
            return;
        }
        MessageQueue messageQueue = looper.getQueue();
        this.mLooperMessageSuperviser.dumpMessage(messageQueue);
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x006f -> B:19:0x0077). Please report as a decompilation issue!!! */
    public void updatOplusMessage(long timeEnd) {
        if (this.mEntry == null) {
            return;
        }
        this.mOPlusWallTime = timeEnd - this.mOplusTime;
        String pkg = ActivityThread.currentPackageName();
        StringBuilder buffer = new StringBuilder();
        buffer.append(pkg);
        buffer.append(",");
        buffer.append(this.mEntry.getTarget());
        buffer.append(",");
        buffer.append(this.mEntry.getWhen());
        if (this.mDebug) {
            this.mDebugWallTime = SystemProperties.getInt("persist.sys.looper.Walltime", 0);
        }
        long j = this.mOPlusWallTime;
        if (j > LOOPER_MONITOR_TIMEOUT || (this.mDebug && j > this.mDebugWallTime)) {
            try {
                OplusActivityManager oplusActivityManager = this.mOplusActivityManager;
                if (oplusActivityManager != null) {
                    oplusActivityManager.addOplusLoopLoadTime(j, buffer.toString());
                } else {
                    OplusActivityManager oplusActivityManager2 = new OplusActivityManager();
                    this.mOplusActivityManager = oplusActivityManager2;
                    oplusActivityManager2.addOplusLoopLoadTime(this.mOPlusWallTime, buffer.toString());
                }
            } catch (RemoteException e) {
                Log.w("Looper", "unable to ", e);
            }
        }
    }

    public void onLooperMsg(Message msg) {
        if (StrictMode.mStrictModeExt.isPerfMonitorEnable() && msg.callback != null && msg.callback.getClass() != null) {
            String claseName = msg.callback.getClass().getName();
            if (!claseName.contains("android.os.StrictMode") && !claseName.contains("android.view.Choreographer") && !claseName.contains("android.graphics.HardwareRendererObserver")) {
                StrictMode.mStrictModeExt.onLooperMsg(msg.toString());
            }
        }
    }

    public void showSlowLog(String msg) {
        if (StrictMode.mStrictModeExt.isPerfMonitorEnable()) {
            StrictMode.noteSlowCall(msg);
            if (StrictMode.mStrictModeExt.isCustomSlowCallEnable()) {
                ActivityThread.currentActivityThread();
                String processName = ActivityThread.currentProcessName();
                StringBuilder append = new StringBuilder().append("local:");
                IStrictModeExt iStrictModeExt = StrictMode.mStrictModeExt;
                StringBuilder append2 = append.append("strictmode").append(",").append(processName).append(",");
                IStrictModeExt iStrictModeExt2 = StrictMode.mStrictModeExt;
                StringBuilder append3 = append2.append(1).append(",");
                IStrictModeExt iStrictModeExt3 = StrictMode.mStrictModeExt;
                Log.p("Quality", append3.append(64).append(",").append(msg.toString()).toString());
                IStrictModeExt iStrictModeExt4 = StrictMode.mStrictModeExt;
                StrictModeExtImpl.writeAtomValue(1, 2, "strictmode", processName, msg.toString(), false);
            }
        }
    }

    public boolean isPerfMonitorEnable() {
        return StrictMode.mStrictModeExt.isPerfMonitorEnable();
    }

    public void dumpMergedQueue() {
        Log.d("Looper", "dumpMergedQueue");
        OplusLooperMsgDispatcher.getInstance().dumpMsgWhenAnr();
    }
}
