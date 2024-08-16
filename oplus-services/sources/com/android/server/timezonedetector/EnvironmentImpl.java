package com.android.server.timezonedetector;

import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import com.android.server.AlarmManagerInternal;
import com.android.server.LocalServices;
import com.android.server.SystemTimeZone;
import com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl;
import java.io.PrintWriter;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class EnvironmentImpl implements TimeZoneDetectorStrategyImpl.Environment {
    private static final String TIMEZONE_PROPERTY = "persist.sys.timezone";
    private final Handler mHandler;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EnvironmentImpl(Handler handler) {
        Objects.requireNonNull(handler);
        this.mHandler = handler;
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public String getDeviceTimeZone() {
        return SystemProperties.get(TIMEZONE_PROPERTY);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public int getDeviceTimeZoneConfidence() {
        return SystemTimeZone.getTimeZoneConfidence();
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public void setDeviceTimeZoneAndConfidence(String str, int i, String str2) {
        ((AlarmManagerInternal) LocalServices.getService(AlarmManagerInternal.class)).setTimeZone(str, i, str2);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public long elapsedRealtimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public void addDebugLogEntry(String str) {
        SystemTimeZone.addDebugLogEntry(str);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public void dumpDebugLog(PrintWriter printWriter) {
        SystemTimeZone.dump(printWriter);
    }

    @Override // com.android.server.timezonedetector.TimeZoneDetectorStrategyImpl.Environment
    public void runAsync(Runnable runnable) {
        this.mHandler.post(runnable);
    }
}
