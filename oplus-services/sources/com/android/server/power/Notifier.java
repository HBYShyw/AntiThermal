package com.android.server.power;

import android.app.ActivityManagerInternal;
import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.app.trust.TrustManager;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.hardware.display.DisplayManagerInternal;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IWakeLockCallback;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManagerInternal;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.WorkSource;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.EventLog;
import android.util.Slog;
import android.util.SparseArray;
import android.view.WindowManagerPolicyConstants;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.EventLogTags;
import com.android.server.LocalServices;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.input.InputManagerInternal;
import com.android.server.inputmethod.InputMethodManagerInternal;
import com.android.server.location.gnss.hal.GnssNative;
import com.android.server.pm.DumpState;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.oplus.android.internal.util.OplusFrameworkStatsLog;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import system.ext.loader.core.ExtLoader;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PROTECTED)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Notifier {
    private static final int[] CHARGING_VIBRATION_AMPLITUDE;
    private static final VibrationEffect CHARGING_VIBRATION_EFFECT;
    private static final long[] CHARGING_VIBRATION_TIME;
    private static final boolean DEBUG = false;
    static boolean DEBUG_PANIC = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES;
    private static final int INTERACTIVE_STATE_ASLEEP = 2;
    private static final int INTERACTIVE_STATE_AWAKE = 1;
    private static final int INTERACTIVE_STATE_UNKNOWN = 0;
    private static final int MSG_BROADCAST = 2;
    private static final int MSG_BROADCAST_ENHANCED_PREDICTION = 4;
    private static final int MSG_PROFILE_TIMED_OUT = 5;
    private static final int MSG_SCREEN_POLICY = 7;
    private static final int MSG_USER_ACTIVITY = 1;
    private static final int MSG_WIRED_CHARGING_STARTED = 6;
    private static final int MSG_WIRELESS_CHARGING_STARTED = 3;
    private static final String TAG = "PowerManagerNotifier";
    private final AppOpsManager mAppOps;
    private final Executor mBackgroundExecutor;
    private final IBatteryStats mBatteryStats;
    private boolean mBroadcastInProgress;
    private long mBroadcastStartTime;
    private int mBroadcastedInteractiveState;
    private final Context mContext;
    private final FaceDownDetector mFaceDownDetector;
    private final NotifierHandler mHandler;
    private boolean mPendingGoToSleepBroadcast;
    private int mPendingInteractiveState;
    private boolean mPendingWakeUpBroadcast;
    private final WindowManagerPolicy mPolicy;
    private final Intent mScreenOffIntent;
    private final Intent mScreenOnIntent;
    private final Bundle mScreenOnOffOptions;
    private final ScreenUndimDetector mScreenUndimDetector;
    private final boolean mShowWirelessChargingAnimationConfig;
    private final SuspendBlocker mSuspendBlocker;
    private final boolean mSuspendWhenScreenOffDueToProximityConfig;
    private final TrustManager mTrustManager;
    private boolean mUserActivityPending;
    private final Vibrator mVibrator;
    private final WakeLockLog mWakeLockLog;
    private final Object mLock = new Object();
    private final SparseArray<Interactivity> mInteractivityByGroupId = new SparseArray<>();
    private Interactivity mGlobalInteractivity = new Interactivity();
    private final AtomicBoolean mIsPlayingChargingStartedFeedback = new AtomicBoolean(false);
    private final IIntentReceiver mWakeUpBroadcastDone = new IIntentReceiver.Stub() { // from class: com.android.server.power.Notifier.2
        public void performReceive(Intent intent, int i, String str, Bundle bundle, boolean z, boolean z2, int i2) {
            if (Notifier.DEBUG_PANIC) {
                Slog.d(Notifier.TAG, "mWakeUpBroadcastDone - sendNextBroadcast");
            }
            EventLog.writeEvent(2726, 1, Long.valueOf(SystemClock.uptimeMillis() - Notifier.this.mBroadcastStartTime), 1);
            Notifier.this.sendNextBroadcast();
        }
    };
    private final IIntentReceiver mGoToSleepBroadcastDone = new IIntentReceiver.Stub() { // from class: com.android.server.power.Notifier.3
        public void performReceive(Intent intent, int i, String str, Bundle bundle, boolean z, boolean z2, int i2) {
            if (Notifier.DEBUG_PANIC) {
                Slog.d(Notifier.TAG, "mGoToSleepBroadcastDone - sendNextBroadcast");
            }
            EventLog.writeEvent(2726, 0, Long.valueOf(SystemClock.uptimeMillis() - Notifier.this.mBroadcastStartTime), 1);
            Notifier.this.sendNextBroadcast();
        }
    };
    private NotifierWrapper mNotifierWrapper = new NotifierWrapper();
    private INotifierExt mNotifierExt = (INotifierExt) ExtLoader.type(INotifierExt.class).base(this).create();
    private final ActivityManagerInternal mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
    private final InputManagerInternal mInputManagerInternal = (InputManagerInternal) LocalServices.getService(InputManagerInternal.class);
    private final InputMethodManagerInternal mInputMethodManagerInternal = (InputMethodManagerInternal) LocalServices.getService(InputMethodManagerInternal.class);
    private final StatusBarManagerInternal mStatusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
    private final DisplayManagerInternal mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);

    static {
        long[] jArr = {40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40};
        CHARGING_VIBRATION_TIME = jArr;
        int[] iArr = {1, 4, 11, 25, 44, 67, 91, HdmiCecKeycode.CEC_KEYCODE_F2_RED, 123, HdmiCecKeycode.CEC_KEYCODE_TUNE_FUNCTION, 79, 55, 34, 17, 7, 2};
        CHARGING_VIBRATION_AMPLITUDE = iArr;
        CHARGING_VIBRATION_EFFECT = VibrationEffect.createWaveform(jArr, iArr, -1);
        HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(50);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Interactivity {
        public int changeReason;
        public long changeStartTime;
        public boolean isChanging;
        public boolean isInteractive;

        private Interactivity() {
            this.isInteractive = true;
        }
    }

    public Notifier(Looper looper, Context context, IBatteryStats iBatteryStats, SuspendBlocker suspendBlocker, WindowManagerPolicy windowManagerPolicy, FaceDownDetector faceDownDetector, ScreenUndimDetector screenUndimDetector, Executor executor) {
        this.mContext = context;
        this.mBatteryStats = iBatteryStats;
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mSuspendBlocker = suspendBlocker;
        this.mPolicy = windowManagerPolicy;
        this.mFaceDownDetector = faceDownDetector;
        this.mScreenUndimDetector = screenUndimDetector;
        this.mTrustManager = (TrustManager) context.getSystemService(TrustManager.class);
        this.mVibrator = (Vibrator) context.getSystemService(Vibrator.class);
        this.mHandler = new NotifierHandler(looper);
        this.mBackgroundExecutor = executor;
        Intent intent = new Intent("android.intent.action.SCREEN_ON");
        this.mScreenOnIntent = intent;
        intent.addFlags(1344274432);
        intent.addFlags(DumpState.DUMP_KNOWN_PACKAGES);
        Intent intent2 = new Intent("android.intent.action.SCREEN_OFF");
        this.mScreenOffIntent = intent2;
        intent2.addFlags(1344274432);
        intent2.addFlags(DumpState.DUMP_KNOWN_PACKAGES);
        this.mScreenOnOffOptions = createScreenOnOffBroadcastOptions();
        this.mSuspendWhenScreenOffDueToProximityConfig = context.getResources().getBoolean(17891863);
        this.mShowWirelessChargingAnimationConfig = context.getResources().getBoolean(17891811);
        this.mWakeLockLog = new WakeLockLog();
        try {
            iBatteryStats.noteInteractive(true);
        } catch (RemoteException unused) {
        }
        FrameworkStatsLog.write(33, 1);
    }

    private Bundle createScreenOnOffBroadcastOptions() {
        BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
        makeBasic.setDeliveryGroupPolicy(1);
        makeBasic.setDeliveryGroupMatchingKey(UUID.randomUUID().toString(), "android.intent.action.SCREEN_ON");
        makeBasic.setDeferralPolicy(2);
        return makeBasic.toBundle();
    }

    public void onWakeLockAcquired(int i, String str, String str2, int i2, int i3, WorkSource workSource, String str3, IWakeLockCallback iWakeLockCallback) {
        notifyWakeLockListener(iWakeLockCallback, str, true);
        int batteryStatsWakeLockMonitorType = getBatteryStatsWakeLockMonitorType(i);
        if (batteryStatsWakeLockMonitorType >= 0) {
            boolean z = i2 == 1000 && (1073741824 & i) != 0;
            try {
                if (workSource != null) {
                    this.mBatteryStats.noteStartWakelockFromSource(workSource, i3, str, str3, batteryStatsWakeLockMonitorType, z);
                } else {
                    this.mBatteryStats.noteStartWakelock(i2, i3, str, str3, batteryStatsWakeLockMonitorType, z);
                    this.mAppOps.startOpNoThrow(40, i2, str2);
                }
            } catch (RemoteException unused) {
            }
        }
        this.mWakeLockLog.onWakeLockAcquired(str, i2, i);
    }

    public void onLongPartialWakeLockStart(String str, int i, WorkSource workSource, String str2) {
        try {
            if (workSource != null) {
                this.mBatteryStats.noteLongPartialWakelockStartFromSource(str, str2, workSource);
                FrameworkStatsLog.write(11, workSource, str, str2, 1);
            } else {
                this.mBatteryStats.noteLongPartialWakelockStart(str, str2, i);
                FrameworkStatsLog.write_non_chained(11, i, (String) null, str, str2, 1);
            }
        } catch (RemoteException unused) {
        }
    }

    public void onLongPartialWakeLockFinish(String str, int i, WorkSource workSource, String str2) {
        try {
            if (workSource != null) {
                this.mBatteryStats.noteLongPartialWakelockFinishFromSource(str, str2, workSource);
                FrameworkStatsLog.write(11, workSource, str, str2, 0);
            } else {
                this.mBatteryStats.noteLongPartialWakelockFinish(str, str2, i);
                FrameworkStatsLog.write_non_chained(11, i, (String) null, str, str2, 0);
            }
        } catch (RemoteException unused) {
        }
    }

    public void onWakeLockChanging(int i, String str, String str2, int i2, int i3, WorkSource workSource, String str3, IWakeLockCallback iWakeLockCallback, int i4, String str4, String str5, int i5, int i6, WorkSource workSource2, String str6, IWakeLockCallback iWakeLockCallback2) {
        int batteryStatsWakeLockMonitorType = getBatteryStatsWakeLockMonitorType(i);
        int batteryStatsWakeLockMonitorType2 = getBatteryStatsWakeLockMonitorType(i4);
        if (workSource != null && workSource2 != null && batteryStatsWakeLockMonitorType >= 0 && batteryStatsWakeLockMonitorType2 >= 0) {
            try {
                this.mBatteryStats.noteChangeWakelockFromSource(workSource, i3, str, str3, batteryStatsWakeLockMonitorType, workSource2, i6, str4, str6, batteryStatsWakeLockMonitorType2, i5 == 1000 && (1073741824 & i4) != 0);
            } catch (RemoteException unused) {
            }
        } else if (!PowerManagerService.isSameCallback(iWakeLockCallback, iWakeLockCallback2)) {
            onWakeLockReleased(i, str, str2, i2, i3, workSource, str3, null);
            onWakeLockAcquired(i4, str4, str5, i5, i6, workSource2, str6, iWakeLockCallback2);
        } else {
            onWakeLockReleased(i, str, str2, i2, i3, workSource, str3, iWakeLockCallback);
            onWakeLockAcquired(i4, str4, str5, i5, i6, workSource2, str6, iWakeLockCallback2);
        }
    }

    public void onWakeLockReleased(int i, String str, String str2, int i2, int i3, WorkSource workSource, String str3, IWakeLockCallback iWakeLockCallback) {
        notifyWakeLockListener(iWakeLockCallback, str, false);
        int batteryStatsWakeLockMonitorType = getBatteryStatsWakeLockMonitorType(i);
        if (batteryStatsWakeLockMonitorType >= 0) {
            try {
                if (workSource != null) {
                    this.mBatteryStats.noteStopWakelockFromSource(workSource, i3, str, str3, batteryStatsWakeLockMonitorType);
                } else {
                    this.mBatteryStats.noteStopWakelock(i2, i3, str, str3, batteryStatsWakeLockMonitorType);
                    this.mAppOps.finishOp(40, i2, str2);
                }
            } catch (RemoteException unused) {
            }
        }
        this.mWakeLockLog.onWakeLockReleased(str, i2);
    }

    private int getBatteryStatsWakeLockMonitorType(int i) {
        int i2 = i & GnssNative.GNSS_AIDING_TYPE_ALL;
        if (i2 == 1) {
            return 0;
        }
        if (i2 == 6 || i2 == 10) {
            return 1;
        }
        return i2 != 32 ? i2 != 128 ? -1 : 18 : this.mSuspendWhenScreenOffDueToProximityConfig ? -1 : 0;
    }

    public void onGlobalWakefulnessChangeStarted(final int i, int i2, long j) {
        boolean isInteractive = PowerManagerInternal.isInteractive(i);
        this.mNotifierExt.notifyOnWakefulnessChangeStartedEnter(isInteractive, i2);
        this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier.1
            @Override // java.lang.Runnable
            public void run() {
                Notifier.this.mActivityManagerInternal.onWakefulnessChanged(i);
                Notifier.this.mNotifierExt.noteSysStateChanged(PowerManagerInternal.isInteractive(i) ? 1 : 0, 1);
                Notifier.this.mNotifierExt.onWakefulnessChanged(i);
            }
        });
        Interactivity interactivity = this.mGlobalInteractivity;
        if (interactivity.isInteractive != isInteractive) {
            if (interactivity.isChanging) {
                handleLateGlobalInteractiveChange();
            }
            if (this.mNotifierExt.isNeedActiveInput()) {
                this.mInputManagerInternal.setInteractive(isInteractive);
            }
            this.mInputMethodManagerInternal.setInteractive(isInteractive);
            try {
                this.mBatteryStats.noteInteractive(isInteractive);
            } catch (RemoteException unused) {
            }
            FrameworkStatsLog.write(33, isInteractive ? 1 : 0);
            Interactivity interactivity2 = this.mGlobalInteractivity;
            interactivity2.isInteractive = isInteractive;
            interactivity2.isChanging = true;
            interactivity2.changeReason = i2;
            interactivity2.changeStartTime = j;
            handleEarlyGlobalInteractiveChange();
        }
    }

    public void onWakefulnessChangeFinished() {
        INotifierExt iNotifierExt = this.mNotifierExt;
        InputManagerInternal inputManagerInternal = this.mInputManagerInternal;
        Interactivity interactivity = this.mGlobalInteractivity;
        iNotifierExt.notifyOnWakefulnessChangeFinishedEnter(inputManagerInternal, interactivity.isInteractive, interactivity.isChanging);
        for (int i = 0; i < this.mInteractivityByGroupId.size(); i++) {
            int keyAt = this.mInteractivityByGroupId.keyAt(i);
            Interactivity valueAt = this.mInteractivityByGroupId.valueAt(i);
            if (valueAt.isChanging) {
                valueAt.isChanging = false;
                handleLateInteractiveChange(keyAt);
            }
        }
        Interactivity interactivity2 = this.mGlobalInteractivity;
        if (interactivity2.isChanging) {
            interactivity2.isChanging = false;
            handleLateGlobalInteractiveChange();
        }
    }

    private void handleEarlyInteractiveChange(final int i) {
        synchronized (this.mLock) {
            Interactivity interactivity = this.mInteractivityByGroupId.get(i);
            if (interactivity == null) {
                Slog.e(TAG, "no Interactivity entry for groupId:" + i);
                return;
            }
            final int i2 = interactivity.changeReason;
            if (interactivity.isInteractive) {
                this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        Notifier.this.lambda$handleEarlyInteractiveChange$0(i, i2);
                    }
                });
            } else {
                this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        Notifier.this.lambda$handleEarlyInteractiveChange$1(i, i2);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEarlyInteractiveChange$0(int i, int i2) {
        this.mPolicy.startedWakingUp(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEarlyInteractiveChange$1(int i, int i2) {
        this.mPolicy.startedGoingToSleep(i, i2);
    }

    private void handleEarlyGlobalInteractiveChange() {
        synchronized (this.mLock) {
            Interactivity interactivity = this.mGlobalInteractivity;
            if (interactivity.isInteractive) {
                final int i = interactivity.changeReason;
                this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        Notifier.this.lambda$handleEarlyGlobalInteractiveChange$2(i);
                    }
                });
                this.mPendingInteractiveState = 1;
                if (this.mNotifierExt.handleEarlyInteractiveChangeInActive()) {
                    return;
                }
                this.mPendingWakeUpBroadcast = true;
                updatePendingBroadcastLocked();
            } else {
                final int i2 = interactivity.changeReason;
                if (DEBUG_PANIC) {
                    Slog.d(TAG, "startedGoingToSleep: offReason=" + WindowManagerPolicyConstants.offReasonToString(WindowManagerPolicyConstants.translateSleepReasonToOffReason(i2)) + ", SleepReason = " + i2);
                }
                this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        Notifier.this.lambda$handleEarlyGlobalInteractiveChange$3(i2);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEarlyGlobalInteractiveChange$2(int i) {
        this.mDisplayManagerInternal.onEarlyInteractivityChange(true);
        this.mPolicy.startedWakingUpGlobal(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleEarlyGlobalInteractiveChange$3(int i) {
        this.mDisplayManagerInternal.onEarlyInteractivityChange(false);
        this.mPolicy.startedGoingToSleepGlobal(i);
    }

    private void handleLateGlobalInteractiveChange() {
        synchronized (this.mLock) {
            long uptimeMillis = SystemClock.uptimeMillis();
            Interactivity interactivity = this.mGlobalInteractivity;
            final int i = (int) (uptimeMillis - interactivity.changeStartTime);
            if (interactivity.isInteractive) {
                this.mNotifierExt.handleLateInteractiveChangeInActive();
                this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Notifier.this.lambda$handleLateGlobalInteractiveChange$4(i);
                    }
                });
            } else {
                if (this.mUserActivityPending) {
                    this.mUserActivityPending = false;
                    this.mHandler.removeMessages(1);
                }
                final int translateSleepReasonToOffReason = WindowManagerPolicyConstants.translateSleepReasonToOffReason(this.mGlobalInteractivity.changeReason);
                this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Notifier.this.lambda$handleLateGlobalInteractiveChange$5(translateSleepReasonToOffReason, i);
                    }
                });
                this.mPendingInteractiveState = 2;
                if (this.mNotifierExt.handleLateInteractiveChangeUnActive()) {
                    return;
                }
                this.mPendingGoToSleepBroadcast = true;
                updatePendingBroadcastLocked();
            }
            long currentTimeMillis = System.currentTimeMillis();
            Interactivity interactivity2 = this.mGlobalInteractivity;
            OplusFrameworkStatsLog.write(100009, currentTimeMillis, interactivity2.isInteractive, i, interactivity2.changeReason);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLateGlobalInteractiveChange$4(int i) {
        LogMaker logMaker = new LogMaker(198);
        logMaker.setType(1);
        logMaker.setSubtype(WindowManagerPolicyConstants.translateWakeReasonToOnReason(this.mGlobalInteractivity.changeReason));
        logMaker.setLatency(i);
        logMaker.addTaggedData(1694, Integer.valueOf(this.mGlobalInteractivity.changeReason));
        MetricsLogger.action(logMaker);
        EventLogTags.writePowerScreenState(1, 0, 0L, 0, i);
        this.mPolicy.finishedWakingUpGlobal(this.mGlobalInteractivity.changeReason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLateGlobalInteractiveChange$5(int i, int i2) {
        LogMaker logMaker = new LogMaker(198);
        logMaker.setType(2);
        logMaker.setSubtype(i);
        logMaker.setLatency(i2);
        logMaker.addTaggedData(1695, Integer.valueOf(this.mGlobalInteractivity.changeReason));
        MetricsLogger.action(logMaker);
        EventLogTags.writePowerScreenState(0, i, 0L, 0, i2);
        this.mPolicy.finishedGoingToSleepGlobal(this.mGlobalInteractivity.changeReason);
    }

    private void handleLateInteractiveChange(final int i) {
        synchronized (this.mLock) {
            Interactivity interactivity = this.mInteractivityByGroupId.get(i);
            if (interactivity == null) {
                Slog.e(TAG, "no Interactivity entry for groupId:" + i);
                return;
            }
            final int i2 = interactivity.changeReason;
            if (interactivity.isInteractive) {
                this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        Notifier.this.lambda$handleLateInteractiveChange$6(i, i2);
                    }
                });
            } else {
                this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        Notifier.this.lambda$handleLateInteractiveChange$7(i, i2);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLateInteractiveChange$6(int i, int i2) {
        this.mPolicy.finishedWakingUp(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleLateInteractiveChange$7(int i, int i2) {
        this.mPolicy.finishedGoingToSleep(i, i2);
    }

    public void onGroupWakefulnessChangeStarted(int i, int i2, int i3, long j) {
        boolean z;
        boolean isInteractive = PowerManagerInternal.isInteractive(i2);
        Interactivity interactivity = this.mInteractivityByGroupId.get(i);
        if (interactivity == null) {
            interactivity = new Interactivity();
            this.mInteractivityByGroupId.put(i, interactivity);
            z = true;
        } else {
            z = false;
        }
        if (z || interactivity.isInteractive != isInteractive) {
            if (interactivity.isChanging) {
                handleLateInteractiveChange(i);
            }
            interactivity.isInteractive = isInteractive;
            interactivity.changeReason = i3;
            interactivity.changeStartTime = j;
            interactivity.isChanging = true;
            handleEarlyInteractiveChange(i);
        }
    }

    public void onGroupRemoved(int i) {
        this.mInteractivityByGroupId.remove(i);
    }

    public void onUserActivity(int i, int i2, int i3) {
        try {
            this.mBatteryStats.noteUserActivity(i3, i2);
        } catch (RemoteException unused) {
        }
        synchronized (this.mLock) {
            if (!this.mUserActivityPending) {
                this.mUserActivityPending = true;
                Message obtainMessage = this.mHandler.obtainMessage(1);
                obtainMessage.arg1 = i;
                obtainMessage.arg2 = i2;
                obtainMessage.setAsynchronous(true);
                this.mHandler.sendMessage(obtainMessage);
            }
        }
    }

    public void onWakeUp(int i, String str, int i2, String str2, int i3) {
        try {
            this.mBatteryStats.noteWakeUp(str, i2);
            if (str2 != null) {
                this.mAppOps.noteOpNoThrow(61, i3, str2);
            }
        } catch (RemoteException unused) {
        }
        FrameworkStatsLog.write(282, i, i2);
    }

    public void onProfileTimeout(int i) {
        Message obtainMessage = this.mHandler.obtainMessage(5);
        obtainMessage.setAsynchronous(true);
        obtainMessage.arg1 = i;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void onWirelessChargingStarted(int i, int i2) {
        this.mSuspendBlocker.acquire();
        Message obtainMessage = this.mHandler.obtainMessage(3);
        obtainMessage.setAsynchronous(true);
        obtainMessage.arg1 = i;
        obtainMessage.arg2 = i2;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void onWiredChargingStarted(int i) {
        this.mSuspendBlocker.acquire();
        Message obtainMessage = this.mHandler.obtainMessage(6);
        obtainMessage.setAsynchronous(true);
        obtainMessage.arg1 = i;
        this.mHandler.sendMessage(obtainMessage);
    }

    public void onScreenPolicyUpdate(int i, int i2) {
        synchronized (this.mLock) {
            Message obtainMessage = this.mHandler.obtainMessage(7);
            obtainMessage.arg1 = i;
            obtainMessage.arg2 = i2;
            obtainMessage.setAsynchronous(true);
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public void dump(PrintWriter printWriter) {
        WakeLockLog wakeLockLog = this.mWakeLockLog;
        if (wakeLockLog != null) {
            wakeLockLog.dump(printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePendingBroadcastLocked() {
        int i;
        if (DEBUG_PANIC) {
            Slog.d(TAG, "updatePendingBroadcastLocked mBroadcastInProgress = " + this.mBroadcastInProgress + ", mPendingInteractiveState = " + this.mPendingInteractiveState + ", mPendingWakeUpBroadcast = " + this.mPendingWakeUpBroadcast + ", mPendingGoToSleepBroadcast = " + this.mPendingGoToSleepBroadcast + ", mBroadcastedInteractiveState = " + this.mBroadcastedInteractiveState);
        }
        if (this.mBroadcastInProgress || (i = this.mPendingInteractiveState) == 0) {
            return;
        }
        if (this.mPendingWakeUpBroadcast || this.mPendingGoToSleepBroadcast || i != this.mBroadcastedInteractiveState) {
            this.mBroadcastInProgress = true;
            this.mSuspendBlocker.acquire();
            Message obtainMessage = this.mHandler.obtainMessage(2);
            obtainMessage.setAsynchronous(true);
            this.mHandler.sendMessage(obtainMessage);
            this.mNotifierExt.updatePendingBroadcastLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishPendingBroadcastLocked() {
        if (DEBUG_PANIC) {
            Slog.d(TAG, "finishPendingBroadcastLocked");
        }
        this.mBroadcastInProgress = false;
        this.mSuspendBlocker.release();
        this.mNotifierExt.finishPendingBroadcastLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendUserActivity(int i, int i2) {
        synchronized (this.mLock) {
            if (this.mUserActivityPending) {
                this.mUserActivityPending = false;
                ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).notifyUserActivity();
                this.mInputManagerInternal.notifyUserActivity();
                this.mPolicy.userActivity(i, i2);
                this.mFaceDownDetector.userActivity(i2);
                this.mScreenUndimDetector.userActivity(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postEnhancedDischargePredictionBroadcast(long j) {
        this.mHandler.sendEmptyMessageDelayed(4, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEnhancedDischargePredictionBroadcast() {
        this.mContext.sendBroadcastAsUser(new Intent("android.os.action.ENHANCED_DISCHARGE_PREDICTION_CHANGED").addFlags(1073741824), UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendNextBroadcast() {
        synchronized (this.mLock) {
            if (DEBUG_PANIC) {
                Slog.d(TAG, "sendNextBroadcast, mBroadcastedInteractiveState=" + this.mBroadcastedInteractiveState + ", mPendingInteractiveState=" + this.mPendingInteractiveState + ", mPendingWakeUpBroadcast=" + this.mPendingWakeUpBroadcast + ", mPendingGoToSleepBroadcast=" + this.mPendingGoToSleepBroadcast + ", mSkipWakeUpBroadcast=" + this.mNotifierExt.isSkipWakeupBroadcast() + ", mSkipGoToSleepBroadcast=" + this.mNotifierExt.isSkipGotoSleepBroadcast());
            }
            int i = this.mBroadcastedInteractiveState;
            if (i == 0) {
                if (this.mPendingInteractiveState == 2) {
                    this.mPendingGoToSleepBroadcast = false;
                    this.mBroadcastedInteractiveState = 2;
                } else {
                    this.mPendingWakeUpBroadcast = false;
                    this.mBroadcastedInteractiveState = 1;
                }
            } else if (i == 1) {
                if (!this.mPendingWakeUpBroadcast && !this.mPendingGoToSleepBroadcast && this.mPendingInteractiveState != 2) {
                    finishPendingBroadcastLocked();
                    return;
                }
                this.mPendingGoToSleepBroadcast = false;
                this.mBroadcastedInteractiveState = 2;
            } else {
                if (!this.mPendingWakeUpBroadcast && !this.mPendingGoToSleepBroadcast && (this.mPendingInteractiveState != 1 || this.mNotifierExt.isSkipWakeupBroadcast())) {
                    finishPendingBroadcastLocked();
                    return;
                }
                this.mPendingWakeUpBroadcast = false;
                this.mBroadcastedInteractiveState = 1;
            }
            this.mBroadcastStartTime = SystemClock.uptimeMillis();
            int i2 = this.mBroadcastedInteractiveState;
            EventLog.writeEvent(2725, 1);
            if (i2 == 1) {
                sendWakeUpBroadcast();
            } else {
                sendGoToSleepBroadcast();
            }
        }
    }

    private void sendWakeUpBroadcast() {
        if (DEBUG_PANIC) {
            Slog.d(TAG, "Sending wake up broadcast.");
        }
        if (this.mActivityManagerInternal.isSystemReady()) {
            this.mNotifierExt.notifyScreenOnOff(true);
            this.mActivityManagerInternal.broadcastIntent(this.mScreenOnIntent, this.mWakeUpBroadcastDone, (String[]) null, !this.mActivityManagerInternal.isModernQueueEnabled(), -1, (int[]) null, (BiFunction) null, this.mScreenOnOffOptions);
        } else {
            EventLog.writeEvent(2727, 2, 1);
            sendNextBroadcast();
        }
    }

    private void sendGoToSleepBroadcast() {
        if (DEBUG_PANIC) {
            Slog.d(TAG, "Sending go to sleep broadcast.");
        }
        if (this.mActivityManagerInternal.isSystemReady()) {
            this.mNotifierExt.notifyScreenOnOff(false);
            this.mActivityManagerInternal.broadcastIntent(this.mScreenOffIntent, this.mGoToSleepBroadcastDone, (String[]) null, !this.mActivityManagerInternal.isModernQueueEnabled(), -1, (int[]) null, (BiFunction) null, this.mScreenOnOffOptions);
        } else {
            EventLog.writeEvent(2727, 3, 1);
            sendNextBroadcast();
        }
    }

    private void playChargingStartedFeedback(final int i, final boolean z) {
        if (!this.mNotifierExt.playChargingStartedFeedback() && isChargingFeedbackEnabled(i) && this.mIsPlayingChargingStartedFeedback.compareAndSet(false, true)) {
            this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Notifier.this.lambda$playChargingStartedFeedback$8(i, z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playChargingStartedFeedback$8(int i, boolean z) {
        Ringtone ringtone;
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "charging_vibration_enabled", 1, i) != 0) {
            this.mVibrator.vibrate(CHARGING_VIBRATION_EFFECT, HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES);
        }
        Uri parse = Uri.parse("file://" + Settings.Global.getString(this.mContext.getContentResolver(), z ? "wireless_charging_started_sound" : "charging_started_sound"));
        if (parse != null && (ringtone = RingtoneManager.getRingtone(this.mContext, parse)) != null) {
            ringtone.setStreamType(1);
            ringtone.play();
        }
        this.mIsPlayingChargingStartedFeedback.set(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWirelessChargingStarted(int i, int i2) {
        StatusBarManagerInternal statusBarManagerInternal;
        playChargingStartedFeedback(i2, true);
        if (this.mShowWirelessChargingAnimationConfig && (statusBarManagerInternal = this.mStatusBarManagerInternal) != null) {
            statusBarManagerInternal.showChargingAnimation(i);
        }
        this.mSuspendBlocker.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWiredChargingStarted(int i) {
        playChargingStartedFeedback(i, false);
        this.mSuspendBlocker.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void screenPolicyChanging(int i, int i2) {
        this.mScreenUndimDetector.recordScreenPolicy(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lockProfile(int i) {
        this.mTrustManager.setDeviceLockedForUser(i, true);
    }

    private boolean isChargingFeedbackEnabled(int i) {
        return (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "charging_sounds_enabled", 1, i) != 0) && (Settings.Global.getInt(this.mContext.getContentResolver(), "zen_mode", 1) == 0);
    }

    private void notifyWakeLockListener(final IWakeLockCallback iWakeLockCallback, final String str, final boolean z) {
        if (iWakeLockCallback != null) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.power.Notifier$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    Notifier.lambda$notifyWakeLockListener$9(iWakeLockCallback, z, str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyWakeLockListener$9(IWakeLockCallback iWakeLockCallback, boolean z, String str) {
        try {
            iWakeLockCallback.onStateChanged(z);
        } catch (RemoteException e) {
            Slog.e(TAG, "Wakelock.mCallback [" + str + "] is already dead.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class NotifierHandler extends Handler {
        public NotifierHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Notifier.this.sendUserActivity(message.arg1, message.arg2);
                    return;
                case 2:
                    Notifier.this.sendNextBroadcast();
                    return;
                case 3:
                    Notifier.this.showWirelessChargingStarted(message.arg1, message.arg2);
                    return;
                case 4:
                    removeMessages(4);
                    Notifier.this.sendEnhancedDischargePredictionBroadcast();
                    return;
                case 5:
                    Notifier.this.lockProfile(message.arg1);
                    return;
                case 6:
                    Notifier.this.showWiredChargingStarted(message.arg1);
                    return;
                case 7:
                    Notifier.this.screenPolicyChanging(message.arg1, message.arg2);
                    return;
                default:
                    return;
            }
        }
    }

    public INotifierWrapper getWrapper() {
        return this.mNotifierWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class NotifierWrapper implements INotifierWrapper {
        private NotifierWrapper() {
        }

        @Override // com.android.server.power.INotifierWrapper
        public void setPendingWakeUpBroadcast(boolean z) {
            Notifier.this.mPendingWakeUpBroadcast = z;
        }

        @Override // com.android.server.power.INotifierWrapper
        public Object getLock() {
            return Notifier.this.mLock;
        }

        @Override // com.android.server.power.INotifierWrapper
        public void updatePendingBroadcastLocked() {
            Notifier.this.updatePendingBroadcastLocked();
        }

        @Override // com.android.server.power.INotifierWrapper
        public void finishPendingBroadcastLocked() {
            Notifier.this.finishPendingBroadcastLocked();
        }
    }
}
