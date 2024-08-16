package android.view;

import android.app.ActivityThread;
import android.app.OplusActivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.SystemProperties;
import android.util.Log;
import java.lang.reflect.Method;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FrameSkipReporter {
    private static final int PROCESS_STATUS_BACKGROUND = 0;
    private static final int PROCESS_STATUS_FOREGROUND = 1;
    private static final int PROCESS_STATUS_UNKNOW = -1;
    private static final String TAG = "Choreographer#FrameSkipReporter";
    private static OplusActivityManager mOAms;
    private static Handler mPerfDataReporterHandler;
    private static HandlerThread mPerfDataReporterThread;
    private static long mLastSkipTime = 0;
    private static int foregroundFlag = -1;
    private static Method methodLogP = null;

    /* renamed from: -$$Nest$smisForegroundApp, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m258$$Nest$smisForegroundApp() {
        return isForegroundApp();
    }

    FrameSkipReporter() {
    }

    static {
        mPerfDataReporterHandler = null;
        mPerfDataReporterThread = null;
        mOAms = null;
        HandlerThread handlerThread = new HandlerThread("PerfDataReporter");
        mPerfDataReporterThread = handlerThread;
        handlerThread.start();
        mPerfDataReporterHandler = mPerfDataReporterThread.getThreadHandler();
        mOAms = new OplusActivityManager();
    }

    public static boolean checkDuplicate(long thisSkipTime, long diff) {
        long j = mLastSkipTime;
        if (j > 0 && thisSkipTime < j + diff) {
            return true;
        }
        mLastSkipTime = thisSkipTime;
        return false;
    }

    public static void report(final long skippedFrames) {
        final long currentTime = System.currentTimeMillis();
        mPerfDataReporterHandler.post(new Runnable() { // from class: android.view.FrameSkipReporter.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Log.d(FrameSkipReporter.TAG, "Process " + ActivityThread.currentProcessName() + "(pid " + Process.myPid() + ") reported " + skippedFrames + " frame(s) skipped");
                    FrameSkipReporter.mOAms.reportSkippedFrames(currentTime, skippedFrames);
                } catch (Exception e) {
                    Log.w(FrameSkipReporter.TAG, ActivityThread.currentProcessName() + " failed to report skipped frames, error " + e.toString());
                }
            }
        });
    }

    public static void report(final boolean isAnimation, final long skippedFrames) {
        final long currentTime = System.currentTimeMillis();
        mPerfDataReporterHandler.post(new Runnable() { // from class: android.view.FrameSkipReporter.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    boolean isForeground = FrameSkipReporter.m258$$Nest$smisForegroundApp();
                    Log.d(FrameSkipReporter.TAG, "Process " + ActivityThread.currentProcessName() + "(pid " + Process.myPid() + ") reported " + skippedFrames + " frame(s) skipped(anim:" + isAnimation + ", fore:" + isForeground + ")");
                    FrameSkipReporter.mOAms.reportSkippedFrames(currentTime, isAnimation, isForeground, skippedFrames);
                } catch (Exception e) {
                    Log.w(FrameSkipReporter.TAG, ActivityThread.currentProcessName() + " failed to report skipped frames, error " + e.toString());
                }
            }
        });
    }

    public static void report(final boolean isAnimation, final long skippedFrames, final String processName) {
        final long currentTime = System.currentTimeMillis();
        mPerfDataReporterHandler.post(new Runnable() { // from class: android.view.FrameSkipReporter.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    boolean isForeground = FrameSkipReporter.m258$$Nest$smisForegroundApp();
                    Log.d(FrameSkipReporter.TAG, "Process " + processName + "(pid " + Process.myPid() + ") reported " + skippedFrames + " frame(s) skipped(anim:" + isAnimation + ", fore:" + isForeground + ")");
                    FrameSkipReporter.mOAms.reportSkippedFrames(currentTime, isAnimation, isForeground, skippedFrames, processName);
                } catch (Exception e) {
                    Log.w(FrameSkipReporter.TAG, processName + " failed to report skipped frames, error " + e.toString());
                }
            }
        });
    }

    public static void report(final boolean isAnimation, final long skippedFrames, final String processName, final String activityName) {
        final long currentTime = System.currentTimeMillis();
        mPerfDataReporterHandler.post(new Runnable() { // from class: android.view.FrameSkipReporter.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    boolean isForeground = FrameSkipReporter.m258$$Nest$smisForegroundApp();
                    Log.d(FrameSkipReporter.TAG, "Process " + processName + "(pid " + Process.myPid() + ") activityName " + activityName + " reported " + skippedFrames + " frame(s) skipped(anim:" + isAnimation + ", fore:" + isForeground + ")");
                    FrameSkipReporter.mOAms.reportSkippedFrames(currentTime, isAnimation, isForeground, skippedFrames, processName, activityName);
                } catch (Exception e) {
                    Log.w(FrameSkipReporter.TAG, processName + " failed to report skipped frames, error " + e.toString());
                }
            }
        });
    }

    public static void setForebackStatus(boolean z) {
        foregroundFlag = z ? 1 : 0;
    }

    private static boolean isForegroundApp() {
        int i = foregroundFlag;
        return i != -1 && i == 1;
    }

    /* loaded from: classes.dex */
    static class FramePerfReport {
        private static final String PROP_FORE_PID = "debug.junk.process.pid";
        private static final String SCREEN_ON = "sys.oplus.healthinfo.screen";
        private static final int SKIP_TYPE_CONTINUOUSLY_SKIPPED_LOW = 1;
        private static final int SKIP_TYPE_CONTINUOUSLY_SKIPPED_MID = 16;
        private static final int SKIP_TYPE_DISCRETE_SKIP = 256;
        private static final int SKIP_TYPE_NONE = 0;
        private static final String TAG = "FramePerfReport";
        private static final String THRESHOLD_CONTINUOUSLY_SKIPPED_LOW_LIMIT_PROP = "sys.oplus.healthinfo.con_skip";
        private static final String THRESHOLD_DISCRETE_SKIPPED_LOW_LIMIT_PROP = "sys.oplus.healthinfo.dis_skip";
        private static final String THRESHOLD_DISCRETE_SPLIT_PROP = "sys.oplus.healthinfo.dis_split";
        private static final String THRESHOLD_MIN_REPORT_SKIPPED_PERCENT_PROP = "sys.oplus.healthinfo.percent";
        private static final int THRESHOLD_TIMEOUT_FORCE_SPLIT = 10000;
        private int discreteSkipped;
        private long endTime;
        private float mFrameIntervalMs;
        private long startTime;
        public static final boolean DEBUG = !SystemProperties.getBoolean("ro.build.release_type", false);
        private static long sDiscreteSplitMs = 200;
        private static long sContinuouslySkippedLowLimitMs = 700;
        private static long sDiscreteSkippedLowLimitMs = 1000;
        private static float sMinReportSkippedPercent = 0.6f;
        private boolean inSkipping = false;
        private int skipType = 0;
        private boolean collected = false;

        public FramePerfReport(long mFrameIntervalNanos) {
            this.mFrameIntervalMs = ((float) mFrameIntervalNanos) * 1.0E-6f;
            sDiscreteSplitMs = SystemProperties.getLong(THRESHOLD_DISCRETE_SPLIT_PROP, sDiscreteSplitMs);
            sContinuouslySkippedLowLimitMs = SystemProperties.getLong(THRESHOLD_CONTINUOUSLY_SKIPPED_LOW_LIMIT_PROP, sContinuouslySkippedLowLimitMs);
            sDiscreteSkippedLowLimitMs = SystemProperties.getLong(THRESHOLD_DISCRETE_SKIPPED_LOW_LIMIT_PROP, sDiscreteSkippedLowLimitMs);
            sMinReportSkippedPercent = SystemProperties.getInt(THRESHOLD_MIN_REPORT_SKIPPED_PERCENT_PROP, (int) (sMinReportSkippedPercent * 100.0f)) / 100.0f;
        }

        void resetStatus() {
            this.inSkipping = false;
            debugI("Skipped2.0 clear");
        }

        void discreteDetect(long skipped) {
            long curT = System.currentTimeMillis();
            if (!isForeground()) {
                if (this.inSkipping) {
                    reportDone();
                    return;
                }
                return;
            }
            if (skipped > 0) {
                if (this.inSkipping) {
                    float f = ((float) skipped) * this.mFrameIntervalMs;
                    long j = this.endTime;
                    if (f > ((float) (curT - j))) {
                        if (DEBUG) {
                            debugI("Skipped2.0 exception " + skipped + " " + (((float) skipped) * this.mFrameIntervalMs) + " > " + (curT - this.endTime));
                            return;
                        }
                        return;
                    }
                    long offsetToLastDraw = (curT - (((float) skipped) * r10)) - j;
                    boolean split = offsetToLastDraw > sDiscreteSplitMs;
                    boolean split2 = split || curT - this.startTime > 10000;
                    if (!split2) {
                        validSkip((int) skipped);
                        this.discreteSkipped += (int) skipped;
                        this.endTime = curT;
                        if (DEBUG) {
                            debugI("Skipped2.0 append " + skipped + " start " + this.startTime + " end " + this.endTime);
                        }
                    } else {
                        if (DEBUG) {
                            debugI("Skipped2.0 inskipped split " + offsetToLastDraw);
                        }
                        reportDone();
                    }
                }
                boolean split3 = this.inSkipping;
                if (!split3) {
                    validSkip((int) skipped);
                    this.discreteSkipped = (int) skipped;
                    this.startTime = curT - (this.mFrameIntervalMs * ((float) skipped));
                    this.endTime = curT;
                    this.skipType = 0;
                    this.collected = false;
                    this.inSkipping = true;
                    if (DEBUG) {
                        debugI("Skipped2.0 begin " + skipped + " start " + this.startTime + " end " + this.endTime);
                    }
                }
                this.skipType = needReport() ? this.skipType : reportNecessity(skipped);
                if (DEBUG) {
                    debugI("Skipped skipType " + this.skipType);
                }
                reportCollect();
                return;
            }
            if (this.inSkipping && curT - this.endTime > sDiscreteSplitMs) {
                reportDone();
            }
        }

        void validSkip(int frames) {
            FrameSkipReporter.logP("Quality", "Skipped2.0: skip " + frames);
        }

        int reportNecessity(long skipped) {
            float f = this.mFrameIntervalMs;
            if (((float) skipped) * f >= ((float) sContinuouslySkippedLowLimitMs)) {
                return 16;
            }
            int lostTime = (int) (this.discreteSkipped * f);
            long duration = this.endTime - this.startTime;
            double skippedTimePercent = lostTime / duration;
            if (DEBUG) {
                debugI("Skipped discrete check " + duration + " " + lostTime + " " + skippedTimePercent);
            }
            if (lostTime > sDiscreteSkippedLowLimitMs && skippedTimePercent > sMinReportSkippedPercent) {
                return 256;
            }
            return 0;
        }

        boolean needReport() {
            return this.skipType != 0;
        }

        void reportCollect() {
            if (needReport() && !this.collected) {
                FrameSkipReporter.logP("Quality", "Skipped2.0: collect " + this.startTime + " " + this.skipType);
                this.collected = true;
            }
        }

        void reportDone() {
            if (needReport()) {
                long duration = this.endTime - this.startTime;
                int lostTime = (int) (this.discreteSkipped * this.mFrameIntervalMs);
                FrameSkipReporter.logP("Quality", "Skipped2.0: done " + this.startTime + " " + duration + " " + lostTime + " " + this.discreteSkipped);
            }
            resetStatus();
        }

        boolean isForeground() {
            return SystemProperties.getInt(PROP_FORE_PID, -999) == Process.myPid() && Process.myPid() == Process.myTid() && isScreenOn();
        }

        boolean isScreenOn() {
            return SystemProperties.getBoolean(SCREEN_ON, true);
        }

        public static void debugI(String logContent) {
            if (DEBUG) {
                Log.i(TAG, logContent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logP(String tag, String content) {
        if (methodLogP == null) {
            try {
                Class cls = Class.forName("android.util.Log");
                Method declaredMethod = cls.getDeclaredMethod("p", String.class, String.class);
                methodLogP = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (ClassNotFoundException e) {
                Log.i(TAG, "ClassNotFoundException : " + e.getMessage());
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                Log.i(TAG, "NoSuchMethodException : " + e2.getMessage());
                e2.printStackTrace();
            } catch (SecurityException e3) {
                e3.printStackTrace();
            }
        }
        Method method = methodLogP;
        if (method != null) {
            try {
                method.invoke(null, tag, content);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }
}
