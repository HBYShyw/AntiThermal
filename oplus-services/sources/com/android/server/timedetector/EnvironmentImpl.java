package com.android.server.timedetector;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Slog;
import com.android.server.AlarmManagerInternal;
import com.android.server.LocalServices;
import com.android.server.SystemClockTime;
import com.android.server.timedetector.TimeDetectorStrategyImpl;
import com.android.server.timezonedetector.StateChangeListener;
import java.io.PrintWriter;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class EnvironmentImpl implements TimeDetectorStrategyImpl.Environment {
    private static final String LOG_TAG = "time_detector";
    private final AlarmManagerInternal mAlarmManagerInternal;
    private final Handler mHandler;
    private final ServiceConfigAccessor mServiceConfigAccessor;
    private final PowerManager.WakeLock mWakeLock;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EnvironmentImpl(Context context, Handler handler, ServiceConfigAccessor serviceConfigAccessor) {
        Objects.requireNonNull(handler);
        this.mHandler = handler;
        Objects.requireNonNull(serviceConfigAccessor);
        this.mServiceConfigAccessor = serviceConfigAccessor;
        PowerManager.WakeLock newWakeLock = ((PowerManager) context.getSystemService(PowerManager.class)).newWakeLock(1, LOG_TAG);
        Objects.requireNonNull(newWakeLock);
        this.mWakeLock = newWakeLock;
        AlarmManagerInternal alarmManagerInternal = (AlarmManagerInternal) LocalServices.getService(AlarmManagerInternal.class);
        Objects.requireNonNull(alarmManagerInternal);
        this.mAlarmManagerInternal = alarmManagerInternal;
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void setConfigurationInternalChangeListener(final StateChangeListener stateChangeListener) {
        this.mServiceConfigAccessor.addConfigurationInternalChangeListener(new StateChangeListener() { // from class: com.android.server.timedetector.EnvironmentImpl$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                EnvironmentImpl.this.lambda$setConfigurationInternalChangeListener$0(stateChangeListener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setConfigurationInternalChangeListener$0(StateChangeListener stateChangeListener) {
        Handler handler = this.mHandler;
        Objects.requireNonNull(stateChangeListener);
        handler.post(new EnvironmentImpl$$ExternalSyntheticLambda1(stateChangeListener));
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public ConfigurationInternal getCurrentUserConfigurationInternal() {
        return this.mServiceConfigAccessor.getCurrentUserConfigurationInternal();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void acquireWakeLock() {
        if (this.mWakeLock.isHeld()) {
            Slog.wtf(LOG_TAG, "WakeLock " + this.mWakeLock + " already held");
        }
        this.mWakeLock.acquire();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public long elapsedRealtimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public long systemClockMillis() {
        return System.currentTimeMillis();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public int systemClockConfidence() {
        return SystemClockTime.getTimeConfidence();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void setSystemClock(long j, int i, String str) {
        checkWakeLockHeld();
        this.mAlarmManagerInternal.setTime(j, i, str);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void setSystemClockConfidence(int i, String str) {
        checkWakeLockHeld();
        SystemClockTime.setConfidence(i, str);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void releaseWakeLock() {
        checkWakeLockHeld();
        this.mWakeLock.release();
    }

    private void checkWakeLockHeld() {
        if (this.mWakeLock.isHeld()) {
            return;
        }
        Slog.wtf(LOG_TAG, "WakeLock " + this.mWakeLock + " not held");
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void addDebugLogEntry(String str) {
        SystemClockTime.addDebugLogEntry(str);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void dumpDebugLog(PrintWriter printWriter) {
        SystemClockTime.dump(printWriter);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategyImpl.Environment
    public void runAsync(Runnable runnable) {
        this.mHandler.post(runnable);
    }
}
