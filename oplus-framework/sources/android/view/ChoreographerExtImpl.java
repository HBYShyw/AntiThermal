package android.view;

import android.app.ActivityThread;
import android.app.OplusActivityThreadExtImpl;
import android.common.OplusFeatureCache;
import android.net.wifi.OplusWifiManager;
import android.os.Handler;
import android.os.IStrictModeExt;
import android.os.JankFactorTracker;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.StrictMode;
import android.os.StrictModeExtImpl;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.view.Choreographer;
import android.view.DisplayEventReceiver;
import android.view.animation.AnimationUtils;
import android.view.debug.IOplusViewDebugManager;
import com.oplus.android.internal.util.OplusFrameworkStatsLog;
import com.oplus.deepthinker.OplusDeepThinkerManager;
import com.oplus.dynamicframerate.DynamicFrameRateController;
import com.oplus.dynamicframerate.util.FramerateUtil;
import com.oplus.scrolloptim.ScrOptController;
import com.oplus.uifirst.IOplusUIFirstManager;
import com.oplus.view.IJankManager;
import com.oplus.view.OplusChoreographerHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ChoreographerExtImpl implements IChoreographerExt {
    private static final int BASE_MULTIPLE = 3;
    private static final int BASE_MULTIPLE_UNIT = 1000000;
    private static boolean DEBUG_LOG = false;
    private static final long DEFAULT_FRAME_INTERVAL = 13000000;
    private static final int DEFAULT_UNITS = 1000;
    private static final long DELAY_TIME_LIMIT = 25000000;
    private static final int FIRST_FRAME = 2;
    private static final long FLAG_CAN_FORCE_DRAW = 256;
    private static final long FRAME_INTERVAL_60HZ_NANOS = 16666666;
    private static final int FRAME_INTERVAL_COMPARE_GAP = 500000;
    private static final long FRAME_STAGE_TIMEOUT = 5000000;
    private static final long HIGH_RATE_INTERVAL_NANO = 11111111;
    private static final int INVALID_POINTER = -1;
    private static final int MSG_DO_ANIM_CALLBACK = 103;
    private static final int MSG_DO_FRAME_INSERT = 104;
    private static final int NUM_10 = 10;
    private static final int NUM_5 = 5;
    private static final int OPLUS_FILED_NOT_USED = -1;
    private static final int PLUS_MULTIPLE = 4;
    private static final String SCREEN_ON = "sys.oplus.healthinfo.screen";
    private static final long SCROLL_STATE_FAKE_FLING = 2;
    private static final long SCROLL_STATE_FLING = 1;
    private static final long SCROLL_STATE_IDLE = 0;
    private static final long SCROLL_STATE_OVER_UP = 3;
    private static final long SENCOND_IN_NANO = 1000000000;
    private static final int SKIP_STATSD_LOG_THRESHOLD = 50;
    private static final int THRESHOLD_OF_IME_SHOW = 600000000;
    private static final long THRESHOLD_OF_UX_LOADING_SLOW = 100000000;
    private static final float floatFaultTolarance = 1.0E-6f;
    private DynamicFrameRateController mADFRController;
    private long mActivityLaunchTime;
    private long mActivityPauseEndTime;
    private long mActivityPauseStartTime;
    private long mActivityResumeTime;
    private long mAnimEndTime;
    private long mAnimStartTime;
    private Choreographer mChoreographer;
    private int mDrawCount;
    private Choreographer.FrameData mFrameData;
    private long mFrameRateThresholdInterval;
    private boolean mFrameScheduled;
    private long mFrameStartNanos;
    private Handler mHandler;
    private long mInputEventTime;
    private long mInputProcessTime;
    private long mLayoutTimeConsume;
    private Object mLock;
    private long mMeasureTimeConsume;
    private long mOpFlags;
    private long mPauseLifecycleEndTime;
    private int mRelayoutCount;
    private ScrOptController mScrOptController;
    private long mTaskAnimEndTimeStamp;
    private long mTaskAnimStartTimeStamp;
    private long mTempFrameNanos;
    private VelocityTracker mVelocityTracker;
    private int mViewCount;
    private static final int SKIPPED_FRAME_THRESHOLD = SystemProperties.getInt("debug.skip_frame_threshold", 1);
    private static final int SKIPPED_MONITOR_FRAME_THRESHOLD = SystemProperties.getInt("persist.sys.skip_frame_threshold", 1);
    private static final boolean mDropFirstFrameStatistics = SystemProperties.getBoolean("persist.sys.oplus.drop_first_frame", false);
    private static final boolean TRACK_FRAME_SKIP = SystemProperties.getBoolean("debug.track_frame_skip", false);
    private static final int SKIPPED_FRAME_ANIM_TRACK_THRESHOLD = SystemProperties.getInt("debug.track_frame_skip.threshold_anim", 4);
    private static final int SKIPPED_FRAME_TRACK_THRESHOLD = SystemProperties.getInt("debug.track_frame_skip.threshold", 15);
    private DisplayEventReceiver.VsyncEventData mEmptyVsyncEventData = new DisplayEventReceiver.VsyncEventData();
    private boolean mIsSFChoregrapher = false;
    private long mTraceMoreFrames = SCROLL_STATE_IDLE;
    private int mDoFrameIndex = 0;
    private long mFrameIntervalNanos = FRAME_INTERVAL_60HZ_NANOS;
    private boolean mJankTrackerEnable = false;
    private long mCallShowSoftInputTime = SCROLL_STATE_IDLE;
    private long mPreShowInputJankCount = SCROLL_STATE_IDLE;
    private long mShowSoftInputTime = SCROLL_STATE_IDLE;
    private int mActivePointerId = -1;
    private long mScrollState = SCROLL_STATE_IDLE;
    private boolean mTaskAnimStarted = false;
    private long mTaskAnimJankInterval = SCROLL_STATE_IDLE;
    private boolean mHasPendingFramerateChange = false;
    private int mAfterPendingFramerateChangeCount = 0;
    private List<OplusChoreographerHelper.IOplusVsyncCallback> mVsyncCallbacks = new ArrayList();
    private final IOplusUIFirstManager mUIFirstMgr = (IOplusUIFirstManager) OplusFeatureCache.getOrCreate(IOplusUIFirstManager.DEFAULT, new Object[0]);
    private final IJankManager mJankManager = (IJankManager) OplusFeatureCache.getOrCreate(IJankManager.DEFAULT, new Object[0]);

    /* loaded from: classes.dex */
    private enum SkipFrameType {
        GENERAL_DROP,
        BIG_DROP
    }

    static {
        boolean z = true;
        if (!SystemProperties.getBoolean("persist.sys.assert.panic", false) && !SystemProperties.get("ro.build.version.ota", "na").contains("PRE_")) {
            z = false;
        }
        DEBUG_LOG = z;
    }

    public ChoreographerExtImpl(Object base) {
        this.mChoreographer = (Choreographer) base;
    }

    public void recordSkippedFrames(long skippedFrames, boolean animation, boolean traversal, long startNanos, long jitterNanos) {
        long fixJitterNanos;
        long fixSkippedFrames;
        boolean reportLog;
        float cost;
        String activityName;
        long fixSkippedFrames2;
        String processName;
        int threshold;
        ScrOptController scrOptController = this.mScrOptController;
        if (scrOptController != null && scrOptController.isInsertingFrame()) {
            return;
        }
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        if ((skippedFrames >= SKIPPED_FRAME_THRESHOLD || skippedFrames >= SKIPPED_MONITOR_FRAME_THRESHOLD) && isFocusApp(activityThread)) {
            long j = this.mActivityResumeTime;
            if (SCROLL_STATE_IDLE == j) {
                fixJitterNanos = jitterNanos;
                fixSkippedFrames = skippedFrames;
            } else {
                long fixJitterNanos2 = Math.min(startNanos - j, jitterNanos);
                long fixSkippedFrames3 = fixJitterNanos2 / this.mFrameIntervalNanos;
                fixJitterNanos = fixJitterNanos2;
                fixSkippedFrames = fixSkippedFrames3;
            }
            boolean animationWithoutTraversal = animation && !traversal;
            boolean hasImportMessage = activityThread.mOplusActivityThreadExt.hasImportMessage();
            String processName2 = ActivityThread.currentProcessName();
            String activityName2 = activityThread.mOplusActivityThreadExt.getCurrentActivityName();
            if (mDropFirstFrameStatistics) {
                if (hasImportMessage) {
                    this.mDoFrameIndex++;
                }
                if (this.mDoFrameIndex > 2 || !hasImportMessage) {
                    this.mDoFrameIndex = 0;
                    activityThread.mOplusActivityThreadExt.setImportMessage(false);
                    reportLog = true;
                } else {
                    reportLog = false;
                }
            } else {
                reportLog = true;
            }
            float cost2 = ((float) fixJitterNanos) * floatFaultTolarance;
            if (!reportLog) {
                cost = cost2;
                activityName = activityName2;
                fixSkippedFrames2 = fixSkippedFrames;
                processName = processName2;
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("Skipped: ").append(animationWithoutTraversal);
                builder.append(" ").append(fixSkippedFrames);
                builder.append(" cost ").append(cost2);
                builder.append(" refreshRate ").append(this.mFrameIntervalNanos);
                builder.append(" bit ").append(OplusActivityThreadExtImpl.is64Bit());
                builder.append(" processName ").append(processName2);
                Log.p("Quality", builder.toString());
                cost = cost2;
                processName = processName2;
                activityName = activityName2;
                fixSkippedFrames2 = fixSkippedFrames;
                writeStatsdLog(SkipFrameType.GENERAL_DROP, animationWithoutTraversal, fixSkippedFrames, cost, processName);
                if (StrictMode.mStrictModeExt.isPerfMonitorEnable()) {
                    StrictMode.noteSlowCall(processName + " SkippedFrames:" + builder.toString());
                    if (StrictMode.mStrictModeExt.isCustomSlowCallEnable()) {
                        StringBuilder append = new StringBuilder().append("local:");
                        IStrictModeExt iStrictModeExt = StrictMode.mStrictModeExt;
                        StringBuilder append2 = append.append("strictmode").append(",").append(processName).append(",");
                        IStrictModeExt iStrictModeExt2 = StrictMode.mStrictModeExt;
                        StringBuilder append3 = append2.append(2).append(",");
                        IStrictModeExt iStrictModeExt3 = StrictMode.mStrictModeExt;
                        Log.p("Quality", append3.append(2048).append(",").append(builder.toString()).toString());
                        IStrictModeExt iStrictModeExt4 = StrictMode.mStrictModeExt;
                        StrictModeExtImpl.writeAtomValue(1, 2, "strictmode", processName, builder.toString(), true);
                    }
                }
            }
            if (fixSkippedFrames2 > 50) {
                writeStatsdLog(SkipFrameType.BIG_DROP, animationWithoutTraversal, fixSkippedFrames2, cost, processName);
            }
            Trace.traceCounter(8L, "skippedFrames", (int) fixSkippedFrames2);
            this.mTraceMoreFrames = fixSkippedFrames2;
            if (TRACK_FRAME_SKIP) {
                if (animationWithoutTraversal) {
                    threshold = SKIPPED_FRAME_ANIM_TRACK_THRESHOLD;
                } else {
                    threshold = SKIPPED_FRAME_TRACK_THRESHOLD;
                }
                if (fixSkippedFrames2 >= threshold && !FrameSkipReporter.checkDuplicate(startNanos, jitterNanos) && activityThread != null) {
                    FrameSkipReporter.setForebackStatus(activityThread.mOplusActivityThreadExt.isTopApp());
                    FrameSkipReporter.report(animationWithoutTraversal, fixSkippedFrames2, ActivityThread.currentProcessName(), activityName);
                }
            }
            activityThread.mOplusActivityThreadExt.asyncReportFrames(skippedFrames);
        }
    }

    boolean isFocusApp(ActivityThread activityThread) {
        return isScreenOn() && activityThread.mOplusActivityThreadExt.isTopApp() && Process.myPid() == Process.myTid();
    }

    private boolean isScreenOn() {
        int state = SystemProperties.getInt("debug.tracing.screen_state", 2);
        return state == 2;
    }

    public void traceBeginForSkippedFrames() {
        Trace.traceBegin(8L, "Choreographer#skippedFrames" + (this.mTraceMoreFrames > SCROLL_STATE_IDLE ? " " + this.mTraceMoreFrames : ""));
        this.mTraceMoreFrames = SCROLL_STATE_IDLE;
    }

    public void traceEndForSkippedFrames() {
        Trace.traceEnd(8L);
    }

    public void setIsSFChoregrapher(boolean isSFChoregrapher) {
        this.mIsSFChoregrapher = isSFChoregrapher;
    }

    public void doFrameStartHook(long frameStartNanos) {
        this.mFrameStartNanos = -1L;
        this.mUIFirstMgr.ofbBoostHint(Process.myPid(), 0, 0, 0, 200, 0, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE);
        if (this.mFrameIntervalNanos <= HIGH_RATE_INTERVAL_NANO) {
            this.mFrameStartNanos = frameStartNanos;
        }
    }

    public void doFrameHook() {
        if (this.mFrameStartNanos >= SCROLL_STATE_IDLE && System.nanoTime() - this.mFrameStartNanos > FRAME_STAGE_TIMEOUT) {
            this.mUIFirstMgr.ofbBoostHint(Process.myPid(), 0, 0, 0, 212, 0, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE, SCROLL_STATE_IDLE);
            this.mFrameStartNanos = -1L;
        }
    }

    public void adjustFrameTimeNanos(long frameTimeNanos, long lastFrameTimeNanos, long startNanos) {
        ScrOptController scrOptController = this.mScrOptController;
        if (scrOptController != null) {
            scrOptController.updateFrameTimeNanos(frameTimeNanos, lastFrameTimeNanos, startNanos);
        }
    }

    public boolean isTimeBackward() {
        ScrOptController scrOptController = this.mScrOptController;
        if (scrOptController != null) {
            return scrOptController.isTimeBackward();
        }
        return false;
    }

    public boolean isScrollOptEnabled() {
        ScrOptController scrOptController = this.mScrOptController;
        if (scrOptController != null) {
            return scrOptController.getOptimConfig().checkOptEnable();
        }
        return false;
    }

    public void traceBeginForOptimizeSlidingEffect() {
        ScrOptController scrOptController = this.mScrOptController;
        if (scrOptController != null) {
            scrOptController.traceBeginForOptimizeSlidingEffect();
        }
    }

    public long getAnimationFrameTimeNanos(Choreographer.FrameData frameData) {
        frameData.setInCallback(true);
        this.mTempFrameNanos = frameData.getFrameTimeNanos();
        frameData.setInCallback(false);
        this.mFrameData = frameData;
        if (this.mScrOptController != null && isScrollOptEnabled()) {
            if (this.mScrOptController.isNeedForceDraw()) {
                this.mChoreographer.mFrameInfo.addFlags(256L);
                this.mScrOptController.setNeedForceDraw(false);
            }
            if (this.mScrOptController.isInsertingFrame()) {
                synchronized (this.mLock) {
                    this.mChoreographer.getWrapper().setFrameScheduled(this.mFrameScheduled);
                }
            }
            if (this.mScrOptController.isInAnimAheadState()) {
                long animationFrameTimeNanos = this.mScrOptController.getLastFrameAnimOptTimeNanos();
                this.mChoreographer.getWrapper().setFrameTimeNanosForFrameData(frameData, animationFrameTimeNanos);
                return animationFrameTimeNanos;
            }
            long frameTimeNanos = this.mScrOptController.getFrameTimeNanos();
            this.mChoreographer.getWrapper().setFrameTimeNanosForFrameData(frameData, frameTimeNanos);
            return frameTimeNanos;
        }
        return this.mTempFrameNanos;
    }

    public void afterDoCallBacks(Choreographer.FrameData frameData) {
        if (this.mScrOptController != null && isScrollOptEnabled()) {
            this.mChoreographer.getWrapper().setFrameTimeNanosForFrameData(frameData, this.mTempFrameNanos);
        }
    }

    public void traceEndForOptimizeSlidingEffect() {
        ScrOptController scrOptController = this.mScrOptController;
        if (scrOptController != null) {
            scrOptController.traceEndForOptimizeSlidingEffect();
        }
    }

    public void onChoreographerInit() {
        if (Looper.myLooper() == Looper.getMainLooper() && Choreographer.getMainThreadInstance() == null) {
            this.mJankTrackerEnable = SystemProperties.getBoolean("persist.sys.janktracker.enable", false);
            this.mHandler = this.mChoreographer.getWrapper().getHandler();
            this.mLock = this.mChoreographer.getWrapper().getLock();
            this.mScrOptController = ScrOptController.getInstance();
        }
    }

    public void onDoFrameFinished() {
        ScrOptController scrOptController;
        ScrOptController scrOptController2 = this.mScrOptController;
        if (scrOptController2 != null) {
            scrOptController2.onDoFrameFinished();
        }
        if (this.mADFRController == null) {
            this.mADFRController = DynamicFrameRateController.getInstance();
        }
        boolean hasFramerateChange = false;
        if (this.mADFRController.isEnable() && (scrOptController = this.mScrOptController) != null) {
            hasFramerateChange = this.mADFRController.onDoFrameFinished(this.mFrameIntervalNanos, scrOptController.isPreDraw());
            if (this.mADFRController.isHighCapability()) {
                this.mFrameRateThresholdInterval = FramerateUtil.get60HzIntervalNanos();
            } else {
                this.mFrameRateThresholdInterval = FramerateUtil.get120HzIntervalNanos();
            }
            this.mHasPendingFramerateChange = hasFramerateChange && this.mScrOptController.isPreDraw();
        }
        if (this.mHasPendingFramerateChange && this.mScrOptController != null) {
            synchronized (this.mLock) {
                this.mChoreographer.getWrapper().scheduleFrameLocked(this.mScrOptController.getLastFrameTimeNanos());
            }
            this.mScrOptController.setAnimAheadState(false);
            return;
        }
        int i = this.mAfterPendingFramerateChangeCount;
        if (i > 0) {
            this.mAfterPendingFramerateChangeCount = i - 1;
        }
        if (this.mScrOptController != null && this.mAfterPendingFramerateChangeCount <= 0) {
            scrollOptAfterDoFrame(hasFramerateChange);
        }
    }

    void scrollOptAfterDoFrame(boolean hasFramerateChange) {
        long frameTimeNanos = this.mScrOptController.getFrameTimeNanos();
        long lastFrameTimeNanos = this.mScrOptController.getLastFrameTimeNanos();
        if (this.mScrOptController.isScrollOptEnabledScene()) {
            if (this.mScrOptController.isInAnimAheadState() && !this.mScrOptController.isFling()) {
                synchronized (this.mLock) {
                    this.mChoreographer.getWrapper().scheduleFrameLocked(lastFrameTimeNanos);
                }
            }
            this.mScrOptController.setAnimAheadState(false);
            if (this.mScrOptController.isFling()) {
                dispatchScrollOpt(hasFramerateChange, frameTimeNanos, lastFrameTimeNanos);
            }
            this.mFrameData = null;
        }
    }

    private void dispatchScrollOpt(boolean hasFramerateChange, long frameTimeNanos, long lastFrameTimeNanos) {
        long nowFrameTimeNanos;
        if (this.mScrOptController.isInsertingFrame()) {
            this.mScrOptController.setNeedForceDraw(true);
        }
        long now = System.nanoTime();
        long intendedNextFrameTimeNanos = lastFrameTimeNanos + this.mFrameIntervalNanos;
        boolean hasScheduleNextFrame = false;
        long nowFrameTimeNanos2 = this.mScrOptController.getOriginalFrameTimeNanos();
        long gapFrameTimeNanos = now - nowFrameTimeNanos2;
        long j = this.mFrameIntervalNanos;
        if (gapFrameTimeNanos < j) {
            nowFrameTimeNanos = nowFrameTimeNanos2;
        } else {
            nowFrameTimeNanos = now - (gapFrameTimeNanos % j);
        }
        if (this.mScrOptController.checkFrameInsertEnable() && !this.mScrOptController.isInsertingFrame() && !this.mScrOptController.reachInsertCountThreshold(nowFrameTimeNanos, intendedNextFrameTimeNanos) && isPreDrawFramerate(this.mFrameIntervalNanos)) {
            long intendedNextFrameTimeNanos2 = this.mScrOptController.getNextFrameTimeNanos(intendedNextFrameTimeNanos, now, lastFrameTimeNanos);
            this.mScrOptController.setFrameInsertTimeNanos(intendedNextFrameTimeNanos2);
            this.mScrOptController.setNeedForceDraw(true);
            synchronized (this.mLock) {
                Message msg = this.mHandler.obtainMessage(104);
                this.mFrameScheduled = this.mChoreographer.getWrapper().getFrameScheduled();
                this.mChoreographer.getWrapper().setFrameScheduled(true);
                this.mHandler.sendMessageAtFrontOfQueue(msg);
            }
            hasScheduleNextFrame = true;
            intendedNextFrameTimeNanos = intendedNextFrameTimeNanos2;
        }
        if (!this.mScrOptController.checkAnimAheadEnable() || hasScheduleNextFrame || this.mScrOptController.isInsertingFrame()) {
            return;
        }
        if (2000000 + now <= intendedNextFrameTimeNanos && !hasFramerateChange) {
            Message msg2 = this.mHandler.obtainMessage(103);
            msg2.obj = this.mFrameData;
            this.mScrOptController.setLastFrameAnimOptTimeNanos(intendedNextFrameTimeNanos);
            this.mHandler.sendMessageAtFrontOfQueue(msg2);
            return;
        }
        synchronized (this.mLock) {
            this.mChoreographer.getWrapper().scheduleFrameLocked(lastFrameTimeNanos);
        }
    }

    public void doFrameInsert(long obj, int arg1) {
        if (this.mScrOptController.isScrollOptDebugEnable()) {
            Trace.traceBegin(8L, "MSG_DO_FRAME_INSERT: " + (obj / 1000000));
        }
        this.mScrOptController.setInsertingFrame(true);
        this.mEmptyVsyncEventData.frameInterval = this.mScrOptController.getVsyncTimeSetter().getFrameIntervalNanos();
        this.mChoreographer.doFrame(obj, arg1, this.mEmptyVsyncEventData);
        this.mScrOptController.setInsertingFrame(false);
        if (this.mScrOptController.isScrollOptDebugEnable()) {
            Trace.traceEnd(8L);
        }
    }

    public void doAnimAheadCallback(Choreographer.FrameData frameData) {
        markOpAnimationStart();
        long lastFrameAnimOptTimeNano = this.mScrOptController.getLastFrameAnimOptTimeNanos();
        if (this.mScrOptController.isScrollOptDebugEnable()) {
            Trace.traceBegin(8L, "scrollOpt-preanimation " + (lastFrameAnimOptTimeNano / 1000000));
        }
        this.mScrOptController.setAnimAheadState(true);
        synchronized (this.mLock) {
            this.mChoreographer.getWrapper().scheduleFrameLocked(lastFrameAnimOptTimeNano);
        }
        frameData.setInCallback(true);
        long tmp = frameData.getFrameTimeNanos();
        this.mChoreographer.getWrapper().setFrameTimeNanosForFrameData(frameData, lastFrameAnimOptTimeNano);
        try {
            AnimationUtils.lockAnimationClock(lastFrameAnimOptTimeNano / 1000000);
            this.mChoreographer.doCallbacks(1, this.mScrOptController.getVsyncTimeSetter().getFrameIntervalNanos());
            this.mScrOptController.setLastFrameAnimOptTimeNanos(lastFrameAnimOptTimeNano);
            AnimationUtils.unlockAnimationClock();
            this.mChoreographer.getWrapper().setFrameTimeNanosForFrameData(frameData, tmp);
            frameData.setInCallback(false);
            if (this.mScrOptController.isScrollOptDebugEnable()) {
                Trace.traceEnd(8L);
            }
            markOpAnimationEnd();
        } catch (Throwable th) {
            AnimationUtils.unlockAnimationClock();
            this.mChoreographer.getWrapper().setFrameTimeNanosForFrameData(frameData, tmp);
            frameData.setInCallback(false);
            throw th;
        }
    }

    public void updateFrameIntervalNanos(long frameIntervalNanos) {
        JankFactorTracker.getInstance().consumeVsync();
        if (frameIntervalNanos == SCROLL_STATE_IDLE) {
            return;
        }
        if (this.mFrameIntervalNanos != frameIntervalNanos) {
            this.mFrameIntervalNanos = frameIntervalNanos;
        }
        ScrOptController scrOptController = this.mScrOptController;
        if (scrOptController != null) {
            scrOptController.updateFrameInterval(frameIntervalNanos);
        }
        if (this.mADFRController != null) {
            FramerateUtil.updateFrameIntervalNanos(frameIntervalNanos);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.view.ChoreographerExtImpl$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$view$ChoreographerExtImpl$SkipFrameType;

        static {
            int[] iArr = new int[SkipFrameType.values().length];
            $SwitchMap$android$view$ChoreographerExtImpl$SkipFrameType = iArr;
            try {
                iArr[SkipFrameType.GENERAL_DROP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$view$ChoreographerExtImpl$SkipFrameType[SkipFrameType.BIG_DROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private void writeStatsdLog(SkipFrameType skipFrameType, boolean animationWithoutTraversal, long skippedFrames, float cost, String processName) {
        switch (AnonymousClass1.$SwitchMap$android$view$ChoreographerExtImpl$SkipFrameType[skipFrameType.ordinal()]) {
            case 1:
                OplusFrameworkStatsLog.write(100027, System.currentTimeMillis(), OplusActivityThreadExtImpl.getPid(), animationWithoutTraversal, skippedFrames, cost, this.mFrameIntervalNanos, processName, OplusActivityThreadExtImpl.is64Bit());
                return;
            case 2:
                OplusFrameworkStatsLog.write(OplusDeepThinkerManager.EVENTFOUNTAIN_RANGE_START, OplusActivityThreadExtImpl.getUid(), OplusActivityThreadExtImpl.getPid(), animationWithoutTraversal, skippedFrames, cost, this.mFrameIntervalNanos, processName, ActivityThread.currentPackageName(), OplusActivityThreadExtImpl.getPackageVersionCode(), OplusActivityThreadExtImpl.is64Bit());
                return;
            default:
                return;
        }
    }

    public boolean checkJankTrackerEnable() {
        return this.mJankTrackerEnable;
    }

    public void handleInputEvent(InputEvent event, int maximumVelocity, int minimumVelocity) {
        DynamicFrameRateController dynamicFrameRateController = this.mADFRController;
        if (dynamicFrameRateController != null) {
            dynamicFrameRateController.getAnimationSpeedAware().onDeliverInputEvent(event);
        }
        if (!this.mJankTrackerEnable) {
            return;
        }
        long flag = SCROLL_STATE_IDLE;
        if (event instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) event;
            if (motionEvent.isFromSource(4098)) {
                int action = motionEvent.getActionMasked();
                if (action == 0) {
                    flag = 4;
                    this.mInputEventTime = motionEvent.getEventTimeNano();
                    this.mInputProcessTime = System.nanoTime();
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    if (velocityTracker == null) {
                        this.mVelocityTracker = VelocityTracker.obtain();
                    } else {
                        velocityTracker.clear();
                    }
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mScrollState = SCROLL_STATE_IDLE;
                } else if (action == 2) {
                    flag = 8;
                    this.mInputEventTime = motionEvent.getEventTimeNano();
                    int i = this.mActivePointerId;
                    if (i >= 0 && motionEvent.findPointerIndex(i) >= 0) {
                        this.mVelocityTracker.addMovement(motionEvent);
                    }
                    this.mScrollState = SCROLL_STATE_IDLE;
                } else if (action == 1) {
                    flag = 16;
                    this.mInputEventTime = motionEvent.getEventTimeNano();
                    this.mInputProcessTime = System.nanoTime();
                    int i2 = this.mActivePointerId;
                    if (i2 >= 0 && motionEvent.findPointerIndex(i2) >= 0) {
                        this.mVelocityTracker.addMovement(motionEvent);
                        this.mVelocityTracker.computeCurrentVelocity(1000, maximumVelocity);
                        boolean isYv = Math.abs(this.mVelocityTracker.getYVelocity(this.mActivePointerId)) > ((float) minimumVelocity);
                        boolean isXv = Math.abs(this.mVelocityTracker.getXVelocity(this.mActivePointerId)) > ((float) minimumVelocity);
                        if (isYv || isXv) {
                            this.mScrollState = 2L;
                        } else {
                            this.mScrollState = SCROLL_STATE_OVER_UP;
                        }
                    }
                    this.mActivePointerId = -1;
                } else if (action == 3) {
                    this.mScrollState = SCROLL_STATE_IDLE;
                    this.mActivePointerId = -1;
                }
                if (action != 2) {
                    Trace.traceBegin(8L, "ID:" + event.getId() + " time:" + event.getEventTime() + " action:" + action + " scrollStage:" + this.mScrollState);
                    Trace.traceEnd(8L);
                }
            }
        } else if (event instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.getAction() == 1 && keyEvent.getKeyCode() == 4) {
                flag = 256;
            }
        }
        this.mOpFlags |= flag;
    }

    private void markOpAnimationStart() {
        this.mAnimStartTime = System.nanoTime();
    }

    private void markOpAnimationEnd() {
        this.mAnimEndTime = System.nanoTime();
    }

    public void populateMeasureCost(long cost, View view, String reason) {
        this.mMeasureTimeConsume += cost;
        if (this.mJankTrackerEnable && cost >= THRESHOLD_OF_UX_LOADING_SLOW) {
            OplusFrameworkStatsLog.write(100083, System.currentTimeMillis(), "uxMeasureTime", cost, view.getClass().getSimpleName(), reason, OplusActivityThreadExtImpl.getPid());
        }
    }

    public void populateLayoutCost(long cost, View view) {
        this.mLayoutTimeConsume += cost;
        if (this.mJankTrackerEnable && cost >= THRESHOLD_OF_UX_LOADING_SLOW) {
            OplusFrameworkStatsLog.write(100083, System.currentTimeMillis(), "uxLayoutTime", cost, view.getClass().getSimpleName(), (String) null, OplusActivityThreadExtImpl.getPid());
        }
    }

    public void markDrawingCacheFlag() {
        this.mOpFlags |= 32;
    }

    public void markPerformClickFlag() {
        this.mOpFlags |= OplusWifiManager.OPLUS_WIFI_FEATURE_NETSHARE;
    }

    public void markRelayout() {
        this.mRelayoutCount++;
    }

    public void markDrawStart() {
        this.mDrawCount++;
    }

    public void syncViewCount(int viewCount) {
        this.mViewCount = viewCount;
    }

    public void resetFrameCount() {
        resetLaunchCount();
        this.mOpFlags = SCROLL_STATE_IDLE;
        this.mAnimStartTime = SCROLL_STATE_IDLE;
        this.mAnimEndTime = SCROLL_STATE_IDLE;
        this.mInputEventTime = SCROLL_STATE_IDLE;
        this.mInputProcessTime = SCROLL_STATE_IDLE;
        this.mMeasureTimeConsume = SCROLL_STATE_IDLE;
        this.mLayoutTimeConsume = SCROLL_STATE_IDLE;
        this.mRelayoutCount = 0;
        this.mViewCount = 0;
        this.mDrawCount = 0;
        this.mShowSoftInputTime = SCROLL_STATE_IDLE;
        JankFactorTracker.getInstance().stopTracker();
    }

    private void resetLaunchCount() {
        if (this.mDrawCount > 0) {
            this.mActivityLaunchTime = SCROLL_STATE_IDLE;
            this.mActivityResumeTime = SCROLL_STATE_IDLE;
            this.mActivityPauseStartTime = SCROLL_STATE_IDLE;
            this.mActivityPauseEndTime = SCROLL_STATE_IDLE;
            this.mPauseLifecycleEndTime = SCROLL_STATE_IDLE;
        }
    }

    public void populateAndResetFrameInfo(long[] frameInfoArray, boolean appVisible) {
        if (!this.mJankTrackerEnable) {
            return;
        }
        if (appVisible) {
            this.mOpFlags |= 2;
        }
        if (this.mPauseLifecycleEndTime != SCROLL_STATE_IDLE) {
            this.mOpFlags |= OplusWifiManager.OPLUS_WIFI_FEATURE_Passpoint;
        }
        if (this.mActivityLaunchTime != SCROLL_STATE_IDLE) {
            this.mOpFlags |= 512;
        }
        if (this.mActivityResumeTime != SCROLL_STATE_IDLE) {
            this.mOpFlags |= OplusWifiManager.OPLUS_WIFI_FEATURE_IOTConnect;
        }
        frameInfoArray[0] = this.mOpFlags;
        frameInfoArray[1] = JankFactorTracker.getInstance().getTextureDestroyedTime();
        frameInfoArray[2] = this.mAnimEndTime;
        frameInfoArray[3] = this.mInputEventTime;
        frameInfoArray[4] = this.mInputProcessTime;
        frameInfoArray[5] = this.mScrollState;
        frameInfoArray[6] = this.mMeasureTimeConsume;
        frameInfoArray[7] = this.mLayoutTimeConsume;
        frameInfoArray[8] = this.mRelayoutCount;
        frameInfoArray[9] = JankFactorTracker.getInstance().getBinderTime();
        frameInfoArray[10] = this.mDrawCount;
        frameInfoArray[11] = this.mActivityLaunchTime;
        frameInfoArray[12] = this.mActivityResumeTime;
        frameInfoArray[13] = this.mActivityPauseStartTime;
        frameInfoArray[14] = this.mActivityPauseEndTime;
        frameInfoArray[15] = this.mPauseLifecycleEndTime;
        frameInfoArray[16] = this.mJankManager.getSceneType();
        frameInfoArray[17] = this.mJankManager.getScene();
        frameInfoArray[18] = this.mJankManager.getScenePolicy();
        long j = this.mShowSoftInputTime;
        if (j == SCROLL_STATE_IDLE) {
            j = this.mJankManager.getSceneBeginTime();
        }
        frameInfoArray[19] = j;
        frameInfoArray[20] = this.mJankManager.getLatencyThresholdNs();
        frameInfoArray[21] = this.mJankManager.getSkippedFrameThreshold();
        frameInfoArray[22] = this.mJankManager.getSkippedFrame();
        if (DEBUG_LOG) {
            Trace.traceBegin(8L, "binder:" + JankFactorTracker.getInstance().getBinderTime() + " vd:" + JankFactorTracker.getInstance().getTextureDestroyedTime() + " mea:" + this.mMeasureTimeConsume + " lay:" + this.mLayoutTimeConsume + " input:" + this.mInputEventTime + " inputProcess:" + this.mInputProcessTime + " re:" + this.mRelayoutCount + " Scroll:" + this.mScrOptController.isFling() + " flingState:" + getFlingState() + " drawCount:" + this.mDrawCount + " launch:" + this.mActivityLaunchTime + " ru:" + this.mActivityResumeTime + " p1:" + this.mActivityPauseStartTime + " p2:" + this.mActivityPauseEndTime + " p3:" + this.mPauseLifecycleEndTime + " vs:" + appVisible + " isLa:" + (this.mOpFlags & OplusWifiManager.OPLUS_WIFI_FEATURE_IOTConnect) + " isPa:" + (this.mOpFlags & OplusWifiManager.OPLUS_WIFI_FEATURE_Passpoint) + " st:" + this.mJankManager.getSceneType() + " sc:" + this.mJankManager.getScene() + " sl:" + this.mJankManager.getLatencyThresholdNs() + " sft:" + this.mJankManager.getSkippedFrameThreshold() + " sf:" + this.mJankManager.getSkippedFrame() + " ime:" + this.mShowSoftInputTime + " Enable:" + this.mJankTrackerEnable + " Flag:" + this.mOpFlags);
            Trace.traceEnd(8L);
        }
        JankFactorTracker.getInstance().endTracker();
    }

    private long getFlingState() {
        if (this.mScrOptController.isFling()) {
            return 1L;
        }
        return this.mScrollState;
    }

    public void setLifecycleState(int state, boolean haveOnCreate, long activityLaunchTime, long activityResumeTime, long activityPauseTime) {
        switch (state) {
            case 3:
                this.mActivityLaunchTime = activityLaunchTime;
                this.mActivityResumeTime = activityResumeTime;
                long j = this.mOpFlags | OplusWifiManager.OPLUS_WIFI_FEATURE_IOTConnect;
                this.mOpFlags = j;
                if (haveOnCreate) {
                    this.mOpFlags = j | 512;
                    return;
                }
                return;
            case 4:
                this.mActivityPauseStartTime = activityPauseTime;
                this.mActivityPauseEndTime = System.nanoTime();
                this.mOpFlags |= OplusWifiManager.OPLUS_WIFI_FEATURE_Passpoint;
                return;
            default:
                return;
        }
    }

    public void makePauseActivityEnd() {
        this.mPauseLifecycleEndTime = System.nanoTime();
    }

    public void showSoftInput(boolean fromIme) {
        if (DEBUG_LOG) {
            Trace.traceCounter(8L, "showSoftInput", 1);
        }
        if (fromIme) {
            this.mShowSoftInputTime = System.nanoTime();
            this.mJankTrackerEnable = true;
        } else {
            this.mCallShowSoftInputTime = System.nanoTime();
        }
    }

    public void hideSoftInput(boolean fromIme) {
        if (DEBUG_LOG) {
            Trace.traceCounter(8L, "hideSoftInput", 1);
        }
        if (fromIme) {
            this.mJankTrackerEnable = false;
        }
    }

    public void showInsetAnim(int types, boolean fromIme) {
        if (fromIme && types == 8 && this.mCallShowSoftInputTime != SCROLL_STATE_IDLE) {
            long preImeTime = System.nanoTime() - this.mCallShowSoftInputTime;
            this.mCallShowSoftInputTime = SCROLL_STATE_IDLE;
            this.mPreShowInputJankCount++;
            if (preImeTime > 600000000) {
                Trace.traceCounter(8L, "slow_show_ime", 1);
                OplusFrameworkStatsLog.write(100026, System.currentTimeMillis(), "pre_show_ime_time", OplusActivityThreadExtImpl.getPid(), Process.myTid(), preImeTime, this.mPreShowInputJankCount);
                this.mPreShowInputJankCount = SCROLL_STATE_IDLE;
            }
        }
    }

    public void onAnimationStart(boolean needsEarlyWakeup, long duration) {
        if (needsEarlyWakeup && duration > SCROLL_STATE_IDLE) {
            this.mTaskAnimStarted = true;
            long j = this.mFrameIntervalNanos;
            this.mTaskAnimJankInterval = j * (j >= DEFAULT_FRAME_INTERVAL ? SCROLL_STATE_OVER_UP : 4L);
            long currentTimeStamp = System.nanoTime();
            long j2 = this.mTaskAnimJankInterval;
            this.mTaskAnimStartTimeStamp = currentTimeStamp + j2;
            this.mTaskAnimEndTimeStamp = ((1000000 * duration) + currentTimeStamp) - j2;
            this.mJankManager.ssAnimSceneBegin(null, 1, IJankManager.SsAnimScene.TASK_OPERATION_ANIMATION_DESC, j2, 0);
        }
    }

    public void dumpAnimationDropInfo(long startNanos) {
        if (this.mIsSFChoregrapher && this.mTaskAnimStarted) {
            long currentTime = System.nanoTime();
            if (currentTime >= this.mTaskAnimStartTimeStamp && currentTime < this.mTaskAnimEndTimeStamp) {
                this.mJankManager.flushFrame(1, this.mFrameIntervalNanos, currentTime - startNanos);
            }
        }
    }

    public void onAnimationEnd(boolean needsEarlyWakeup) {
        if (needsEarlyWakeup) {
            this.mJankManager.ssAnimSceneEnd(1);
            this.mTaskAnimStarted = false;
        }
    }

    public void setScheduleVsync() {
        JankFactorTracker.getInstance().setScheduleVsync();
    }

    /* loaded from: classes.dex */
    private static class GfxFrameInfo {
        private static final int ACTIVITY_PAUSE_END_TIME = 14;
        private static final int ANIM_END_TIME = 2;
        private static final int APP_SCENE = 17;
        private static final int APP_SCENE_TYPE = 16;
        private static final int BINDER_TIME = 9;
        private static final int DRAW_COUNT = 10;
        private static final long FLAG_ACTIVITY_CREATE = 512;
        private static final long FLAG_ACTIVITY_PAUSE = 2048;
        private static final long FLAG_ACTIVITY_RESUME = 1024;
        private static final long FLAG_BACK_DOWN_EVENT = 128;
        private static final long FLAG_BACK_UP_EVENT = 256;
        private static final long FLAG_BUILD_DRAW_CACHE = 32;
        private static final long FLAG_DOWN_FRAME = 4;
        private static final long FLAG_FLING_STATE = 64;
        private static final long FLAG_JANK_TRACKER_ENABLE = 2;
        private static final long FLAG_MOVE_FRAME = 8;
        private static final long FLAG_PERFORM_CLICK = 4096;
        private static final long FLAG_UP_FRAME = 16;
        private static final int FLING_STATE = 5;
        private static final int INPUT_EVENT_TIME = 3;
        private static final int INPUT_PROCESS_TIME = 4;
        private static final int LATENCY_THRESHOLD = 20;
        private static final int LAUNCH_START_TIME = 11;
        private static final int LAYOUT_TIME_CONSUME = 7;
        private static final int MEASURE_TIME_CONSUME = 6;
        private static final int OP_FLAGS = 0;
        private static final int PAUSE_END_TIME = 15;
        private static final int PAUSE_START_TIME = 13;
        private static final int RELAYOUT_COUNT = 8;
        private static final int RESUME_START_TIME = 12;
        private static final int SCENE_BEGIN_TIME = 19;
        private static final int SCENE_POLICY = 18;
        private static final int SKIPPED_FRAME = 22;
        private static final int SKIPPED_FRAME_THRESHOLD = 21;
        private static final int VIEW_DETACH_TIME = 1;

        private GfxFrameInfo() {
        }
    }

    public boolean hasPendingFramerateChange() {
        return this.mHasPendingFramerateChange;
    }

    public void handlePendingFramerateChange(long startNanos) {
        ScrOptController scrOptController = this.mScrOptController;
        if (scrOptController != null && scrOptController.getFrameTimeNanos() - 500000 <= startNanos) {
            this.mHasPendingFramerateChange = false;
            if (this.mADFRController == null) {
                this.mADFRController = DynamicFrameRateController.getInstance();
            }
            this.mADFRController.handlePendingFramerateChange();
            this.mAfterPendingFramerateChangeCount = 2;
        }
    }

    private boolean isPreDrawFramerate(long frameIntervalNanos) {
        DynamicFrameRateController dynamicFrameRateController = this.mADFRController;
        return dynamicFrameRateController == null || !dynamicFrameRateController.isEnable() || frameIntervalNanos - 500000 < this.mFrameRateThresholdInterval;
    }

    public String markOnVsync(DisplayEventReceiver.VsyncEventData vsyncEventData, long timestampNanos, long lastFrameTimeNanos) {
        return getViewDebugManager().markOnVsync(vsyncEventData, timestampNanos, lastFrameTimeNanos);
    }

    public String markOnDoframe(DisplayEventReceiver.VsyncEventData vsyncEventData, long timestampNanos, long lastFrameTimeNanos) {
        return getViewDebugManager().markOnDoframe(vsyncEventData, timestampNanos, lastFrameTimeNanos);
    }

    public IOplusViewDebugManager getViewDebugManager() {
        return (IOplusViewDebugManager) OplusFeatureCache.getOrCreate(IOplusViewDebugManager.mDefault, new Object[0]);
    }

    public void setAnimating(boolean animating) {
        StrictMode.mStrictModeExt.setAnimating(animating);
    }

    public void dispatchVsyncData(long timestampNanos, DisplayEventReceiver.VsyncEventData vsyncEventData) {
        for (int i = 0; i < this.mVsyncCallbacks.size(); i++) {
            try {
                this.mVsyncCallbacks.get(i).onVsync(timestampNanos, vsyncEventData.preferredFrameTimeline().vsyncId);
            } catch (Exception e) {
                Log.e("ChoreographerExtImpl", "Failed to dispatch oplus Vsync data");
                return;
            }
        }
    }

    public void addOplusVsyncCallback(Object object) {
        if (object instanceof OplusChoreographerHelper.IOplusVsyncCallback) {
            OplusChoreographerHelper.IOplusVsyncCallback callback = (OplusChoreographerHelper.IOplusVsyncCallback) object;
            synchronized (this) {
                this.mVsyncCallbacks.add(callback);
            }
        }
    }

    public void removeOplusVsyncCallback(Object object) {
        if (object instanceof OplusChoreographerHelper.IOplusVsyncCallback) {
            OplusChoreographerHelper.IOplusVsyncCallback callback = (OplusChoreographerHelper.IOplusVsyncCallback) object;
            synchronized (this) {
                if (this.mVsyncCallbacks.contains(callback)) {
                    this.mVsyncCallbacks.remove(callback);
                }
            }
        }
    }
}
