package com.android.server.job.controllers.idle;

import android.R;
import android.app.AlarmManager;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.server.AppSchedulingModuleThread;
import com.android.server.job.JobSchedulerService;
import com.android.server.job.controllers.IIdleControllerExt;
import com.android.server.job.controllers.IdleController;
import java.io.PrintWriter;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DeviceIdlenessTracker extends BroadcastReceiver implements IdlenessTracker {
    private static final boolean DEBUG;
    private static final String TAG = "JobScheduler.DeviceIdlenessTracker";
    private AlarmManager mAlarm;
    private boolean mDockIdle;
    private boolean mIdle;
    IIdleControllerExt mIdleControllerExt;
    private IdlenessListener mIdleListener;
    private long mIdleWindowSlop;
    private long mInactivityIdleThreshold;
    private PowerManager mPowerManager;
    private boolean mProjectionActive;
    private final UiModeManager.OnProjectionStateChangedListener mOnProjectionStateChangedListener = new UiModeManager.OnProjectionStateChangedListener() { // from class: com.android.server.job.controllers.idle.DeviceIdlenessTracker$$ExternalSyntheticLambda0
        public final void onProjectionStateChanged(int i, Set set) {
            DeviceIdlenessTracker.this.onProjectionStateChanged(i, set);
        }
    };
    private AlarmManager.OnAlarmListener mIdleAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.server.job.controllers.idle.DeviceIdlenessTracker$$ExternalSyntheticLambda1
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            DeviceIdlenessTracker.this.lambda$new$0();
        }
    };
    private boolean mScreenOn = true;

    static {
        DEBUG = JobSchedulerService.DEBUG || Log.isLoggable(TAG, 3);
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public boolean isIdle() {
        return this.mIdle;
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void startTracking(Context context, IdlenessListener idlenessListener) {
        this.mIdleListener = idlenessListener;
        this.mInactivityIdleThreshold = context.getResources().getInteger(R.integer.config_networkPolicyDefaultWarning);
        this.mIdleWindowSlop = context.getResources().getInteger(R.integer.config_networkNotifySwitchType);
        this.mAlarm = (AlarmManager) context.getSystemService("alarm");
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.DREAMING_STARTED");
        intentFilter.addAction("android.intent.action.DREAMING_STOPPED");
        intentFilter.addAction("com.android.server.ACTION_TRIGGER_IDLE");
        this.mIdleControllerExt = ((IdleController) idlenessListener).mIdleControllerExt;
        intentFilter.addAction(IIdleControllerExt.ACTION_FAST_IDLE_TRIGGER_INTENT);
        intentFilter.addAction("android.intent.action.DOCK_IDLE");
        intentFilter.addAction("android.intent.action.DOCK_ACTIVE");
        context.registerReceiver(this, intentFilter, null, AppSchedulingModuleThread.getHandler());
        ((UiModeManager) context.getSystemService(UiModeManager.class)).addOnProjectionStateChangedListener(-1, AppSchedulingModuleThread.getExecutor(), this.mOnProjectionStateChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProjectionStateChanged(int i, Set<String> set) {
        boolean z = i != 0;
        if (this.mProjectionActive == z) {
            return;
        }
        if (DEBUG) {
            Slog.v(TAG, "Projection state changed: " + z);
        }
        this.mProjectionActive = z;
        if (z) {
            cancelIdlenessCheck();
            if (this.mIdle) {
                this.mIdle = false;
                this.mIdleListener.reportNewIdleState(false);
            }
            this.mIdleControllerExt.updateFastIdleflag();
            return;
        }
        maybeScheduleIdlenessCheck("Projection ended");
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void dump(PrintWriter printWriter) {
        printWriter.print("  mIdle: ");
        printWriter.println(this.mIdle);
        printWriter.print("  mScreenOn: ");
        printWriter.println(this.mScreenOn);
        printWriter.print("  mDockIdle: ");
        printWriter.println(this.mDockIdle);
        printWriter.print("  mProjectionActive: ");
        printWriter.println(this.mProjectionActive);
    }

    @Override // com.android.server.job.controllers.idle.IdlenessTracker
    public void dump(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        long start2 = protoOutputStream.start(1146756268033L);
        protoOutputStream.write(1133871366145L, this.mIdle);
        protoOutputStream.write(1133871366146L, this.mScreenOn);
        protoOutputStream.write(1133871366147L, this.mDockIdle);
        protoOutputStream.write(1133871366149L, this.mProjectionActive);
        protoOutputStream.end(start2);
        protoOutputStream.end(start);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:30:0x0084. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0099 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00b8  */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        boolean z = DEBUG;
        if (z) {
            Slog.v(TAG, "Received action: " + action);
        }
        action.hashCode();
        char c = 65535;
        switch (action.hashCode()) {
            case -2128145023:
                if (action.equals("android.intent.action.SCREEN_OFF")) {
                    c = 0;
                    break;
                }
                break;
            case -1454123155:
                if (action.equals("android.intent.action.SCREEN_ON")) {
                    c = 1;
                    break;
                }
                break;
            case -1100466933:
                if (action.equals(IIdleControllerExt.ACTION_FAST_IDLE_TRIGGER_INTENT)) {
                    c = 2;
                    break;
                }
                break;
            case -905264325:
                if (action.equals("android.intent.action.DOCK_IDLE")) {
                    c = 3;
                    break;
                }
                break;
            case 244891622:
                if (action.equals("android.intent.action.DREAMING_STARTED")) {
                    c = 4;
                    break;
                }
                break;
            case 257757490:
                if (action.equals("android.intent.action.DREAMING_STOPPED")) {
                    c = 5;
                    break;
                }
                break;
            case 1456569541:
                if (action.equals("com.android.server.ACTION_TRIGGER_IDLE")) {
                    c = 6;
                    break;
                }
                break;
            case 1689632941:
                if (action.equals("android.intent.action.DOCK_ACTIVE")) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 3:
            case 4:
                if (action.equals("android.intent.action.DOCK_IDLE")) {
                    if (!this.mScreenOn) {
                        return;
                    } else {
                        this.mDockIdle = true;
                    }
                } else {
                    this.mScreenOn = false;
                    this.mDockIdle = false;
                }
                maybeScheduleIdlenessCheck(action);
                return;
            case 1:
                this.mScreenOn = true;
                this.mDockIdle = false;
                if (z) {
                    Slog.v(TAG, "exiting idle");
                }
                cancelIdlenessCheck();
                if (this.mIdle) {
                    this.mIdle = false;
                    this.mIdleListener.reportNewIdleState(false);
                }
                this.mIdleControllerExt.updateFastIdleflag();
                return;
            case 2:
                this.mIdleControllerExt.handleFastIdleTrigger(this.mScreenOn, this.mDockIdle, this.mProjectionActive);
                return;
            case 5:
                if (!this.mPowerManager.isInteractive()) {
                    return;
                }
                this.mScreenOn = true;
                this.mDockIdle = false;
                if (z) {
                }
                cancelIdlenessCheck();
                if (this.mIdle) {
                }
                this.mIdleControllerExt.updateFastIdleflag();
                return;
            case 6:
                lambda$new$0();
                return;
            case 7:
                if (!this.mScreenOn) {
                    return;
                }
                if (!this.mPowerManager.isInteractive()) {
                }
                this.mScreenOn = true;
                this.mDockIdle = false;
                if (z) {
                }
                cancelIdlenessCheck();
                if (this.mIdle) {
                }
                this.mIdleControllerExt.updateFastIdleflag();
                return;
            default:
                return;
        }
    }

    private void maybeScheduleIdlenessCheck(String str) {
        if ((!this.mScreenOn || this.mDockIdle) && !this.mProjectionActive) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            long j = millis + this.mInactivityIdleThreshold;
            if (DEBUG) {
                Slog.v(TAG, "Scheduling idle : " + str + " now:" + millis + " when=" + j);
            }
            this.mAlarm.setWindow(2, j, this.mIdleWindowSlop, "JS idleness", AppSchedulingModuleThread.getExecutor(), this.mIdleAlarmListener);
        }
    }

    private void cancelIdlenessCheck() {
        this.mAlarm.cancel(this.mIdleAlarmListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleIdleTrigger, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        if (!this.mIdle && ((!this.mScreenOn || this.mDockIdle) && !this.mProjectionActive)) {
            if (DEBUG) {
                Slog.v(TAG, "Idle trigger fired @ " + JobSchedulerService.sElapsedRealtimeClock.millis());
            }
            this.mIdle = true;
            this.mIdleListener.reportNewIdleState(true);
            return;
        }
        if (DEBUG) {
            Slog.v(TAG, "TRIGGER_IDLE received but not changing state; idle=" + this.mIdle + " screen=" + this.mScreenOn + " projection=" + this.mProjectionActive);
        }
    }
}
