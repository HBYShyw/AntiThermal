package com.android.server.display;

import android.content.Context;
import android.database.ContentObserver;
import android.hardware.display.BrightnessInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.MathUtils;
import android.util.Slog;
import android.util.TimeUtils;
import android.view.SurfaceControlHdrLayerInfoListener;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.display.DisplayDeviceConfig;
import com.android.server.display.DisplayManagerService;
import com.android.server.display.HighBrightnessModeController;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HighBrightnessModeController {
    private static final boolean DEBUG = false;
    private static final float DEFAULT_MAX_DESIRED_HDR_SDR_RATIO = 1.0f;

    @VisibleForTesting
    static final float HBM_TRANSITION_POINT_INVALID = Float.POSITIVE_INFINITY;
    private static final String TAG = "HighBrightnessModeController";
    private float mAmbientLux;
    private float mBrightness;
    private final float mBrightnessMax;
    private final float mBrightnessMin;
    private final DisplayManagerService.Clock mClock;
    private final Context mContext;
    private int mDisplayStatsId;
    private final Handler mHandler;
    private final Runnable mHbmChangeCallback;
    private DisplayDeviceConfig.HighBrightnessModeData mHbmData;
    private int mHbmMode;
    private int mHbmStatsState;
    private HdrBrightnessDeviceConfig mHdrBrightnessCfg;
    private HdrListener mHdrListener;
    private int mHeight;
    private HighBrightnessModeMetadata mHighBrightnessModeMetadata;
    private final Injector mInjector;
    private boolean mIsAutoBrightnessEnabled;
    private boolean mIsAutoBrightnessOffByState;
    private boolean mIsBlockedByLowPowerMode;
    private boolean mIsHdrLayerPresent;
    private boolean mIsInAllowedAmbientRange;
    private boolean mIsTimeAvailable;
    private float mMaxDesiredHdrSdrRatio;
    private final Runnable mRecalcRunnable;
    private IBinder mRegisteredDisplayToken;
    private final SettingsObserver mSettingsObserver;
    private int mThrottlingReason;
    private float mUnthrottledBrightness;
    private int mWidth;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface HdrBrightnessDeviceConfig {
        float getHdrBrightnessFromSdr(float f, float f2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HighBrightnessModeController(Handler handler, int i, int i2, IBinder iBinder, String str, float f, float f2, DisplayDeviceConfig.HighBrightnessModeData highBrightnessModeData, HdrBrightnessDeviceConfig hdrBrightnessDeviceConfig, Runnable runnable, HighBrightnessModeMetadata highBrightnessModeMetadata, Context context) {
        this(new Injector(), handler, i, i2, iBinder, str, f, f2, highBrightnessModeData, hdrBrightnessDeviceConfig, runnable, highBrightnessModeMetadata, context);
    }

    @VisibleForTesting
    HighBrightnessModeController(Injector injector, Handler handler, int i, int i2, IBinder iBinder, String str, float f, float f2, DisplayDeviceConfig.HighBrightnessModeData highBrightnessModeData, HdrBrightnessDeviceConfig hdrBrightnessDeviceConfig, Runnable runnable, HighBrightnessModeMetadata highBrightnessModeMetadata, Context context) {
        this.mIsInAllowedAmbientRange = false;
        this.mIsTimeAvailable = false;
        this.mIsAutoBrightnessEnabled = false;
        this.mIsAutoBrightnessOffByState = false;
        this.mThrottlingReason = 0;
        this.mHbmMode = 0;
        this.mIsHdrLayerPresent = false;
        this.mMaxDesiredHdrSdrRatio = 1.0f;
        this.mIsBlockedByLowPowerMode = false;
        this.mHbmStatsState = 1;
        this.mHighBrightnessModeMetadata = null;
        this.mInjector = injector;
        this.mContext = context;
        this.mClock = injector.getClock();
        this.mHandler = handler;
        this.mBrightness = f;
        this.mBrightnessMin = f;
        this.mBrightnessMax = f2;
        this.mHbmChangeCallback = runnable;
        this.mHighBrightnessModeMetadata = highBrightnessModeMetadata;
        this.mSettingsObserver = new SettingsObserver(handler);
        this.mRecalcRunnable = new Runnable() { // from class: com.android.server.display.HighBrightnessModeController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                HighBrightnessModeController.this.recalculateTimeAllowance();
            }
        };
        this.mHdrListener = new HdrListener();
        resetHbmData(i, i2, iBinder, str, highBrightnessModeData, hdrBrightnessDeviceConfig);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAutoBrightnessEnabled(int i) {
        boolean z = i == 1;
        this.mIsAutoBrightnessOffByState = i == 3;
        if (!deviceSupportsHbm() || z == this.mIsAutoBrightnessEnabled) {
            return;
        }
        this.mIsAutoBrightnessEnabled = z;
        this.mIsInAllowedAmbientRange = false;
        recalculateTimeAllowance();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCurrentBrightnessMin() {
        return this.mBrightnessMin;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCurrentBrightnessMax() {
        if (!deviceSupportsHbm() || isCurrentlyAllowed()) {
            return this.mBrightnessMax;
        }
        return this.mHbmData.transitionPoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getNormalBrightnessMax() {
        return deviceSupportsHbm() ? this.mHbmData.transitionPoint : this.mBrightnessMax;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getHdrBrightnessValue() {
        HdrBrightnessDeviceConfig hdrBrightnessDeviceConfig = this.mHdrBrightnessCfg;
        if (hdrBrightnessDeviceConfig != null) {
            float hdrBrightnessFromSdr = hdrBrightnessDeviceConfig.getHdrBrightnessFromSdr(this.mBrightness, this.mMaxDesiredHdrSdrRatio);
            if (hdrBrightnessFromSdr != -1.0f) {
                return hdrBrightnessFromSdr;
            }
        }
        return MathUtils.map(getCurrentBrightnessMin(), getCurrentBrightnessMax(), this.mBrightnessMin, this.mBrightnessMax, this.mBrightness);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAmbientLuxChange(float f) {
        this.mAmbientLux = f;
        if (deviceSupportsHbm() && this.mIsAutoBrightnessEnabled) {
            boolean z = f >= this.mHbmData.minimumLux;
            if (z != this.mIsInAllowedAmbientRange) {
                this.mIsInAllowedAmbientRange = z;
                recalculateTimeAllowance();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBrightnessChanged(float f, float f2, int i) {
        if (deviceSupportsHbm()) {
            this.mBrightness = f;
            this.mUnthrottledBrightness = f2;
            this.mThrottlingReason = i;
            long runningStartTimeMillis = this.mHighBrightnessModeMetadata.getRunningStartTimeMillis();
            boolean z = runningStartTimeMillis != -1;
            boolean z2 = this.mBrightness > this.mHbmData.transitionPoint && !this.mIsHdrLayerPresent;
            if (z != z2) {
                long uptimeMillis = this.mClock.uptimeMillis();
                if (z2) {
                    this.mHighBrightnessModeMetadata.setRunningStartTimeMillis(uptimeMillis);
                } else {
                    this.mHighBrightnessModeMetadata.addHbmEvent(new HbmEvent(runningStartTimeMillis, uptimeMillis));
                    this.mHighBrightnessModeMetadata.setRunningStartTimeMillis(-1L);
                }
            }
            recalculateTimeAllowance();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getHighBrightnessMode() {
        return this.mHbmMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getTransitionPoint() {
        return deviceSupportsHbm() ? this.mHbmData.transitionPoint : HBM_TRANSITION_POINT_INVALID;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() {
        registerHdrListener(null);
        this.mSettingsObserver.stopObserving();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHighBrightnessModeMetadata(HighBrightnessModeMetadata highBrightnessModeMetadata) {
        this.mHighBrightnessModeMetadata = highBrightnessModeMetadata;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetHbmData(int i, int i2, IBinder iBinder, String str, DisplayDeviceConfig.HighBrightnessModeData highBrightnessModeData, HdrBrightnessDeviceConfig hdrBrightnessDeviceConfig) {
        this.mWidth = i;
        this.mHeight = i2;
        this.mHbmData = highBrightnessModeData;
        this.mHdrBrightnessCfg = hdrBrightnessDeviceConfig;
        this.mDisplayStatsId = str.hashCode();
        unregisterHdrListener();
        this.mSettingsObserver.stopObserving();
        if (deviceSupportsHbm()) {
            registerHdrListener(iBinder);
            recalculateTimeAllowance();
            if (this.mHbmData.allowInLowPowerMode) {
                return;
            }
            this.mIsBlockedByLowPowerMode = false;
            this.mSettingsObserver.startObserving();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(final PrintWriter printWriter) {
        this.mHandler.runWithScissors(new Runnable() { // from class: com.android.server.display.HighBrightnessModeController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                HighBrightnessModeController.this.lambda$dump$0(printWriter);
            }
        }, 1000L);
    }

    @VisibleForTesting
    HdrListener getHdrListener() {
        return this.mHdrListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$0(PrintWriter printWriter) {
        String str;
        printWriter.println("HighBrightnessModeController:");
        printWriter.println("  mBrightness=" + this.mBrightness);
        printWriter.println("  mUnthrottledBrightness=" + this.mUnthrottledBrightness);
        printWriter.println("  mThrottlingReason=" + BrightnessInfo.briMaxReasonToString(this.mThrottlingReason));
        printWriter.println("  mCurrentMin=" + getCurrentBrightnessMin());
        printWriter.println("  mCurrentMax=" + getCurrentBrightnessMax());
        StringBuilder sb = new StringBuilder();
        sb.append("  mHbmMode=");
        sb.append(BrightnessInfo.hbmToString(this.mHbmMode));
        if (this.mHbmMode == 2) {
            str = "(" + getHdrBrightnessValue() + ")";
        } else {
            str = "";
        }
        sb.append(str);
        printWriter.println(sb.toString());
        printWriter.println("  mHbmStatsState=" + hbmStatsStateToString(this.mHbmStatsState));
        printWriter.println("  mHbmData=" + this.mHbmData);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("  mAmbientLux=");
        sb2.append(this.mAmbientLux);
        sb2.append(this.mIsAutoBrightnessEnabled ? "" : " (old/invalid)");
        printWriter.println(sb2.toString());
        printWriter.println("  mIsInAllowedAmbientRange=" + this.mIsInAllowedAmbientRange);
        printWriter.println("  mIsAutoBrightnessEnabled=" + this.mIsAutoBrightnessEnabled);
        printWriter.println("  mIsAutoBrightnessOffByState=" + this.mIsAutoBrightnessOffByState);
        printWriter.println("  mIsHdrLayerPresent=" + this.mIsHdrLayerPresent);
        printWriter.println("  mBrightnessMin=" + this.mBrightnessMin);
        printWriter.println("  mBrightnessMax=" + this.mBrightnessMax);
        printWriter.println("  remainingTime=" + calculateRemainingTime(this.mClock.uptimeMillis()));
        printWriter.println("  mIsTimeAvailable= " + this.mIsTimeAvailable);
        if (this.mHighBrightnessModeMetadata != null) {
            printWriter.println("  mRunningStartTimeMillis=" + TimeUtils.formatUptime(this.mHighBrightnessModeMetadata.getRunningStartTimeMillis()));
        }
        printWriter.println("  mIsBlockedByLowPowerMode=" + this.mIsBlockedByLowPowerMode);
        printWriter.println("  width*height=" + this.mWidth + "*" + this.mHeight);
        printWriter.println("  mEvents=");
        long uptimeMillis = this.mClock.uptimeMillis();
        HighBrightnessModeMetadata highBrightnessModeMetadata = this.mHighBrightnessModeMetadata;
        if (highBrightnessModeMetadata != null) {
            long runningStartTimeMillis = highBrightnessModeMetadata.getRunningStartTimeMillis();
            if (runningStartTimeMillis != -1) {
                uptimeMillis = dumpHbmEvent(printWriter, new HbmEvent(runningStartTimeMillis, uptimeMillis));
            }
            Iterator<HbmEvent> it = this.mHighBrightnessModeMetadata.getHbmEventQueue().iterator();
            while (it.hasNext()) {
                HbmEvent next = it.next();
                if (uptimeMillis > next.getEndTimeMillis()) {
                    printWriter.println("    event: [normal brightness]: " + TimeUtils.formatDuration(uptimeMillis - next.getEndTimeMillis()));
                }
                uptimeMillis = dumpHbmEvent(printWriter, next);
            }
        }
    }

    private long dumpHbmEvent(PrintWriter printWriter, HbmEvent hbmEvent) {
        printWriter.println("    event: [" + TimeUtils.formatUptime(hbmEvent.getStartTimeMillis()) + ", " + TimeUtils.formatUptime(hbmEvent.getEndTimeMillis()) + "] (" + TimeUtils.formatDuration(hbmEvent.getEndTimeMillis() - hbmEvent.getStartTimeMillis()) + ")");
        return hbmEvent.getStartTimeMillis();
    }

    private boolean isCurrentlyAllowed() {
        return !this.mIsHdrLayerPresent && this.mIsAutoBrightnessEnabled && this.mIsTimeAvailable && this.mIsInAllowedAmbientRange && !this.mIsBlockedByLowPowerMode;
    }

    private boolean deviceSupportsHbm() {
        return this.mHbmData != null;
    }

    private long calculateRemainingTime(long j) {
        long j2;
        if (!deviceSupportsHbm()) {
            return 0L;
        }
        long runningStartTimeMillis = this.mHighBrightnessModeMetadata.getRunningStartTimeMillis();
        if (runningStartTimeMillis > 0) {
            if (runningStartTimeMillis > j) {
                Slog.e(TAG, "Start time set to the future. curr: " + j + ", start: " + runningStartTimeMillis);
                this.mHighBrightnessModeMetadata.setRunningStartTimeMillis(j);
                runningStartTimeMillis = j;
            }
            j2 = j - runningStartTimeMillis;
        } else {
            j2 = 0;
        }
        long j3 = j - this.mHbmData.timeWindowMillis;
        Iterator<HbmEvent> it = this.mHighBrightnessModeMetadata.getHbmEventQueue().iterator();
        while (it.hasNext()) {
            HbmEvent next = it.next();
            if (next.getEndTimeMillis() < j3) {
                it.remove();
            } else {
                j2 += next.getEndTimeMillis() - Math.max(next.getStartTimeMillis(), j3);
            }
        }
        return Math.max(0L, this.mHbmData.timeMaxMillis - j2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recalculateTimeAllowance() {
        long j;
        long uptimeMillis = this.mClock.uptimeMillis();
        long calculateRemainingTime = calculateRemainingTime(uptimeMillis);
        DisplayDeviceConfig.HighBrightnessModeData highBrightnessModeData = this.mHbmData;
        boolean z = true;
        boolean z2 = calculateRemainingTime >= highBrightnessModeData.timeMinMillis;
        boolean z3 = !z2 && calculateRemainingTime > 0 && this.mBrightness > highBrightnessModeData.transitionPoint;
        if (!z2 && !z3) {
            z = false;
        }
        this.mIsTimeAvailable = z;
        ArrayDeque<HbmEvent> hbmEventQueue = this.mHighBrightnessModeMetadata.getHbmEventQueue();
        if (this.mBrightness > this.mHbmData.transitionPoint) {
            j = uptimeMillis + calculateRemainingTime;
        } else if (this.mIsTimeAvailable || hbmEventQueue.size() <= 0) {
            j = -1;
        } else {
            long j2 = uptimeMillis - this.mHbmData.timeWindowMillis;
            j = (uptimeMillis + ((Math.max(j2, hbmEventQueue.peekLast().getStartTimeMillis()) + this.mHbmData.timeMinMillis) - j2)) - calculateRemainingTime;
        }
        if (j != -1) {
            this.mHandler.removeCallbacks(this.mRecalcRunnable);
            this.mHandler.postAtTime(this.mRecalcRunnable, j + 1);
        }
        updateHbmMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHbmMode() {
        int calculateHighBrightnessMode = calculateHighBrightnessMode();
        updateHbmStats(calculateHighBrightnessMode);
        if (this.mHbmMode != calculateHighBrightnessMode) {
            this.mHbmMode = calculateHighBrightnessMode;
            this.mHbmChangeCallback.run();
        }
    }

    private void updateHbmStats(int i) {
        int i2 = 3;
        int i3 = (i != 2 || getHdrBrightnessValue() <= this.mHbmData.transitionPoint) ? (i != 1 || this.mBrightness <= this.mHbmData.transitionPoint) ? 1 : 3 : 2;
        int i4 = this.mHbmStatsState;
        if (i3 == i4) {
            return;
        }
        boolean z = i4 == 3;
        boolean z2 = i3 == 3;
        if (z && !z2) {
            boolean z3 = this.mIsAutoBrightnessEnabled;
            if (!z3 && this.mIsAutoBrightnessOffByState) {
                i2 = 6;
            } else if (!z3) {
                i2 = 7;
            } else if (!this.mIsInAllowedAmbientRange) {
                i2 = 1;
            } else if (!this.mIsTimeAvailable) {
                i2 = 2;
            } else if (!isThermalThrottlingActive()) {
                if (this.mIsHdrLayerPresent) {
                    i2 = 4;
                } else if (this.mIsBlockedByLowPowerMode) {
                    i2 = 5;
                } else if (this.mBrightness <= this.mHbmData.transitionPoint) {
                    i2 = 9;
                }
            }
            this.mInjector.reportHbmStateChange(this.mDisplayStatsId, i3, i2);
            this.mHbmStatsState = i3;
        }
        i2 = 0;
        this.mInjector.reportHbmStateChange(this.mDisplayStatsId, i3, i2);
        this.mHbmStatsState = i3;
    }

    @VisibleForTesting
    boolean isThermalThrottlingActive() {
        float f = this.mUnthrottledBrightness;
        float f2 = this.mHbmData.transitionPoint;
        return f > f2 && this.mBrightness <= f2 && this.mThrottlingReason == 1;
    }

    private String hbmStatsStateToString(int i) {
        return i != 1 ? i != 2 ? i != 3 ? String.valueOf(i) : "HBM_ON_SUNLIGHT" : "HBM_ON_HDR" : "HBM_OFF";
    }

    private int calculateHighBrightnessMode() {
        if (!deviceSupportsHbm()) {
            return 0;
        }
        if (this.mIsHdrLayerPresent) {
            return 2;
        }
        return isCurrentlyAllowed() ? 1 : 0;
    }

    private void registerHdrListener(IBinder iBinder) {
        if (this.mRegisteredDisplayToken == iBinder) {
            return;
        }
        unregisterHdrListener();
        this.mRegisteredDisplayToken = iBinder;
        if (iBinder != null) {
            this.mHdrListener.register(iBinder);
        }
    }

    private void unregisterHdrListener() {
        IBinder iBinder = this.mRegisteredDisplayToken;
        if (iBinder != null) {
            this.mHdrListener.unregister(iBinder);
            this.mIsHdrLayerPresent = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class HdrListener extends SurfaceControlHdrLayerInfoListener {
        HdrListener() {
        }

        public void onHdrInfoChanged(IBinder iBinder, final int i, final int i2, final int i3, int i4, final float f) {
            HighBrightnessModeController.this.mHandler.post(new Runnable() { // from class: com.android.server.display.HighBrightnessModeController$HdrListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HighBrightnessModeController.HdrListener.this.lambda$onHdrInfoChanged$0(i, i2, i3, f);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHdrInfoChanged$0(int i, int i2, int i3, float f) {
            HighBrightnessModeController highBrightnessModeController = HighBrightnessModeController.this;
            highBrightnessModeController.mIsHdrLayerPresent = i > 0 && ((float) (i2 * i3)) >= ((float) (highBrightnessModeController.mWidth * HighBrightnessModeController.this.mHeight)) * HighBrightnessModeController.this.mHbmData.minimumHdrPercentOfScreen;
            if (!HighBrightnessModeController.this.mIsHdrLayerPresent || HighBrightnessModeController.this.mHdrBrightnessCfg == null) {
                f = 1.0f;
            }
            if (f >= 1.0f) {
                HighBrightnessModeController.this.mMaxDesiredHdrSdrRatio = f;
            } else {
                Slog.w(HighBrightnessModeController.TAG, "Ignoring invalid desired HDR/SDR Ratio: " + f);
                HighBrightnessModeController.this.mMaxDesiredHdrSdrRatio = 1.0f;
            }
            HighBrightnessModeController highBrightnessModeController2 = HighBrightnessModeController.this;
            highBrightnessModeController2.onBrightnessChanged(highBrightnessModeController2.mBrightness, HighBrightnessModeController.this.mUnthrottledBrightness, HighBrightnessModeController.this.mThrottlingReason);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SettingsObserver extends ContentObserver {
        private final Uri mLowPowerModeSetting;
        private boolean mStarted;

        SettingsObserver(Handler handler) {
            super(handler);
            this.mLowPowerModeSetting = Settings.Global.getUriFor("low_power");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            updateLowPower();
        }

        void startObserving() {
            if (this.mStarted) {
                return;
            }
            HighBrightnessModeController.this.mContext.getContentResolver().registerContentObserver(this.mLowPowerModeSetting, false, this, -1);
            this.mStarted = true;
            updateLowPower();
        }

        void stopObserving() {
            HighBrightnessModeController.this.mIsBlockedByLowPowerMode = false;
            if (this.mStarted) {
                HighBrightnessModeController.this.mContext.getContentResolver().unregisterContentObserver(this);
                this.mStarted = false;
            }
        }

        private void updateLowPower() {
            boolean isLowPowerMode = isLowPowerMode();
            if (isLowPowerMode == HighBrightnessModeController.this.mIsBlockedByLowPowerMode) {
                return;
            }
            HighBrightnessModeController.this.mIsBlockedByLowPowerMode = isLowPowerMode;
            HighBrightnessModeController.this.updateHbmMode();
        }

        private boolean isLowPowerMode() {
            return Settings.Global.getInt(HighBrightnessModeController.this.mContext.getContentResolver(), "low_power", 0) != 0;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        public DisplayManagerService.Clock getClock() {
            return new DisplayManagerService.Clock() { // from class: com.android.server.display.HighBrightnessModeController$Injector$$ExternalSyntheticLambda0
                @Override // com.android.server.display.DisplayManagerService.Clock
                public final long uptimeMillis() {
                    return SystemClock.uptimeMillis();
                }
            };
        }

        public void reportHbmStateChange(int i, int i2, int i3) {
            FrameworkStatsLog.write(FrameworkStatsLog.DISPLAY_HBM_STATE_CHANGED, i, i2, i3);
        }
    }
}
