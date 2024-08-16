package com.android.server.timedetector;

import android.app.time.ExternalTimeSuggestion;
import android.app.time.TimeCapabilities;
import android.app.time.TimeState;
import android.app.time.UnixEpochTime;
import android.app.timedetector.ManualTimeSuggestion;
import android.app.timedetector.TelephonyTimeSuggestion;
import android.content.Context;
import android.os.Handler;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.SystemClockTime;
import com.android.server.timezonedetector.ArrayMapWithHistory;
import com.android.server.timezonedetector.ReferenceWithHistory;
import com.android.server.timezonedetector.StateChangeListener;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class TimeDetectorStrategyImpl implements TimeDetectorStrategy {
    private static final boolean DBG = false;
    private static final int KEEP_SUGGESTION_HISTORY_SIZE = 10;
    private static final String LOG_TAG = "time_detector";

    @VisibleForTesting
    static final long MAX_SUGGESTION_TIME_AGE_MILLIS = 86400000;
    private static final long SYSTEM_CLOCK_PARANOIA_THRESHOLD_MILLIS = 2000;
    private static final int TELEPHONY_BUCKET_COUNT = 24;

    @VisibleForTesting
    static final int TELEPHONY_BUCKET_SIZE_MILLIS = 3600000;
    private static final int TELEPHONY_INVALID_SCORE = -1;

    @GuardedBy({"this"})
    private ConfigurationInternal mCurrentConfigurationInternal;
    private final Environment mEnvironment;

    @GuardedBy({"this"})
    private UnixEpochTime mLastAutoSystemClockTimeSet;

    @GuardedBy({"this"})
    private final ArrayMapWithHistory<Integer, TelephonyTimeSuggestion> mSuggestionBySlotIndex = new ArrayMapWithHistory<>(10);

    @GuardedBy({"this"})
    private final ReferenceWithHistory<NetworkTimeSuggestion> mLastNetworkSuggestion = new ReferenceWithHistory<>(10);

    @GuardedBy({"this"})
    private final ReferenceWithHistory<GnssTimeSuggestion> mLastGnssSuggestion = new ReferenceWithHistory<>(10);

    @GuardedBy({"this"})
    private final ReferenceWithHistory<ExternalTimeSuggestion> mLastExternalSuggestion = new ReferenceWithHistory<>(10);

    @GuardedBy({"this"})
    private final ArraySet<StateChangeListener> mNetworkTimeUpdateListeners = new ArraySet<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Environment {
        void acquireWakeLock();

        void addDebugLogEntry(String str);

        void dumpDebugLog(PrintWriter printWriter);

        long elapsedRealtimeMillis();

        ConfigurationInternal getCurrentUserConfigurationInternal();

        void releaseWakeLock();

        void runAsync(Runnable runnable);

        void setConfigurationInternalChangeListener(StateChangeListener stateChangeListener);

        void setSystemClock(long j, int i, String str);

        void setSystemClockConfidence(int i, String str);

        int systemClockConfidence();

        long systemClockMillis();
    }

    private static boolean isOriginAutomatic(int i) {
        return i != 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TimeDetectorStrategy create(Context context, Handler handler, ServiceConfigAccessor serviceConfigAccessor) {
        return new TimeDetectorStrategyImpl(new EnvironmentImpl(context, handler, serviceConfigAccessor));
    }

    @VisibleForTesting
    TimeDetectorStrategyImpl(Environment environment) {
        Objects.requireNonNull(environment);
        Environment environment2 = environment;
        this.mEnvironment = environment2;
        synchronized (this) {
            environment2.setConfigurationInternalChangeListener(new StateChangeListener() { // from class: com.android.server.timedetector.TimeDetectorStrategyImpl$$ExternalSyntheticLambda0
                @Override // com.android.server.timezonedetector.StateChangeListener
                public final void onChange() {
                    TimeDetectorStrategyImpl.this.handleConfigurationInternalChanged();
                }
            });
            this.mCurrentConfigurationInternal = environment2.getCurrentUserConfigurationInternal();
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void suggestExternalTime(ExternalTimeSuggestion externalTimeSuggestion) {
        Objects.requireNonNull(externalTimeSuggestion);
        if (validateAutoSuggestionTime(externalTimeSuggestion.getUnixEpochTime(), externalTimeSuggestion)) {
            this.mLastExternalSuggestion.set(externalTimeSuggestion);
            doAutoTimeDetection("External time suggestion received: suggestion=" + externalTimeSuggestion);
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void suggestGnssTime(GnssTimeSuggestion gnssTimeSuggestion) {
        Objects.requireNonNull(gnssTimeSuggestion);
        if (validateAutoSuggestionTime(gnssTimeSuggestion.getUnixEpochTime(), gnssTimeSuggestion)) {
            this.mLastGnssSuggestion.set(gnssTimeSuggestion);
            doAutoTimeDetection("GNSS time suggestion received: suggestion=" + gnssTimeSuggestion);
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized boolean suggestManualTime(int i, ManualTimeSuggestion manualTimeSuggestion, boolean z) {
        ConfigurationInternal configurationInternal = this.mCurrentConfigurationInternal;
        if (configurationInternal.getUserId() != i) {
            Slog.w(LOG_TAG, "Manual suggestion received but user != current user, userId=" + i + " suggestion=" + manualTimeSuggestion);
            return false;
        }
        Objects.requireNonNull(manualTimeSuggestion);
        String str = "Manual time suggestion received: suggestion=" + manualTimeSuggestion;
        TimeCapabilities capabilities = configurationInternal.createCapabilitiesAndConfig(z).getCapabilities();
        if (capabilities.getSetManualTimeCapability() != 40) {
            Slog.i(LOG_TAG, "User does not have the capability needed to set the time manually: capabilities=" + capabilities + ", suggestion=" + manualTimeSuggestion + ", cause=" + str);
            return false;
        }
        UnixEpochTime unixEpochTime = manualTimeSuggestion.getUnixEpochTime();
        if (!validateManualSuggestionTime(unixEpochTime, manualTimeSuggestion)) {
            return false;
        }
        return setSystemClockAndConfidenceIfRequired(2, unixEpochTime, str);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void suggestNetworkTime(NetworkTimeSuggestion networkTimeSuggestion) {
        Objects.requireNonNull(networkTimeSuggestion);
        if (validateAutoSuggestionTime(networkTimeSuggestion.getUnixEpochTime(), networkTimeSuggestion)) {
            NetworkTimeSuggestion networkTimeSuggestion2 = this.mLastNetworkSuggestion.get();
            if (networkTimeSuggestion2 == null || !networkTimeSuggestion2.equals(networkTimeSuggestion)) {
                this.mLastNetworkSuggestion.set(networkTimeSuggestion);
                notifyNetworkTimeUpdateListenersAsynchronously();
            }
            doAutoTimeDetection("New network time suggested. suggestion=" + networkTimeSuggestion);
        }
    }

    @GuardedBy({"this"})
    private void notifyNetworkTimeUpdateListenersAsynchronously() {
        Iterator<StateChangeListener> it = this.mNetworkTimeUpdateListeners.iterator();
        while (it.hasNext()) {
            StateChangeListener next = it.next();
            Environment environment = this.mEnvironment;
            Objects.requireNonNull(next);
            environment.runAsync(new EnvironmentImpl$$ExternalSyntheticLambda1(next));
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void addNetworkTimeUpdateListener(StateChangeListener stateChangeListener) {
        this.mNetworkTimeUpdateListeners.add(stateChangeListener);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized NetworkTimeSuggestion getLatestNetworkSuggestion() {
        return this.mLastNetworkSuggestion.get();
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void clearLatestNetworkSuggestion() {
        this.mLastNetworkSuggestion.set(null);
        notifyNetworkTimeUpdateListenersAsynchronously();
        doAutoTimeDetection("Network time cleared");
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized TimeState getTimeState() {
        return new TimeState(new UnixEpochTime(this.mEnvironment.elapsedRealtimeMillis(), this.mEnvironment.systemClockMillis()), this.mEnvironment.systemClockConfidence() < 100);
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void setTimeState(TimeState timeState) {
        Objects.requireNonNull(timeState);
        int i = timeState.getUserShouldConfirmTime() ? 0 : 100;
        this.mEnvironment.acquireWakeLock();
        try {
            setSystemClockAndConfidenceUnderWakeLock(2, timeState.getUnixEpochTime(), i, "setTimeZoneState()");
        } finally {
            this.mEnvironment.releaseWakeLock();
        }
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized boolean confirmTime(UnixEpochTime unixEpochTime) {
        boolean isTimeWithinConfidenceThreshold;
        Objects.requireNonNull(unixEpochTime);
        this.mEnvironment.acquireWakeLock();
        try {
            long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
            long systemClockMillis = this.mEnvironment.systemClockMillis();
            isTimeWithinConfidenceThreshold = isTimeWithinConfidenceThreshold(unixEpochTime, elapsedRealtimeMillis, systemClockMillis);
            if (isTimeWithinConfidenceThreshold) {
                int systemClockConfidence = this.mEnvironment.systemClockConfidence();
                if (systemClockConfidence < 100) {
                    this.mEnvironment.setSystemClockConfidence(100, "Confirm system clock time. confirmationTime=" + unixEpochTime + " newTimeConfidence=100 currentElapsedRealtimeMillis=" + elapsedRealtimeMillis + " currentSystemClockMillis=" + systemClockMillis + " (old) currentTimeConfidence=" + systemClockConfidence);
                }
            }
        } finally {
            this.mEnvironment.releaseWakeLock();
        }
        return isTimeWithinConfidenceThreshold;
    }

    @Override // com.android.server.timedetector.TimeDetectorStrategy
    public synchronized void suggestTelephonyTime(TelephonyTimeSuggestion telephonyTimeSuggestion) {
        if (telephonyTimeSuggestion.getUnixEpochTime() == null) {
            return;
        }
        if (validateAutoSuggestionTime(telephonyTimeSuggestion.getUnixEpochTime(), telephonyTimeSuggestion)) {
            if (storeTelephonySuggestion(telephonyTimeSuggestion)) {
                doAutoTimeDetection("New telephony time suggested. suggestion=" + telephonyTimeSuggestion);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleConfigurationInternalChanged() {
        ConfigurationInternal currentUserConfigurationInternal = this.mEnvironment.getCurrentUserConfigurationInternal();
        addDebugLogEntry("handleConfigurationInternalChanged: oldConfiguration=" + this.mCurrentConfigurationInternal + ", newConfiguration=" + currentUserConfigurationInternal);
        this.mCurrentConfigurationInternal = currentUserConfigurationInternal;
        if (currentUserConfigurationInternal.getAutoDetectionEnabledBehavior()) {
            doAutoTimeDetection("Auto time zone detection config changed.");
        } else {
            this.mLastAutoSystemClockTimeSet = null;
        }
    }

    private void addDebugLogEntry(String str) {
        this.mEnvironment.addDebugLogEntry(str);
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public synchronized void dump(IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        indentingPrintWriter.println("TimeDetectorStrategy:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mLastAutoSystemClockTimeSet=" + this.mLastAutoSystemClockTimeSet);
        indentingPrintWriter.println("mCurrentConfigurationInternal=" + this.mCurrentConfigurationInternal);
        indentingPrintWriter.println("[Capabilities=" + this.mCurrentConfigurationInternal.createCapabilitiesAndConfig(false) + "]");
        long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        indentingPrintWriter.printf("mEnvironment.elapsedRealtimeMillis()=%s (%s)\n", new Object[]{Duration.ofMillis(elapsedRealtimeMillis), Long.valueOf(elapsedRealtimeMillis)});
        long systemClockMillis = this.mEnvironment.systemClockMillis();
        indentingPrintWriter.printf("mEnvironment.systemClockMillis()=%s (%s)\n", new Object[]{Instant.ofEpochMilli(systemClockMillis), Long.valueOf(systemClockMillis)});
        indentingPrintWriter.println("mEnvironment.systemClockConfidence()=" + this.mEnvironment.systemClockConfidence());
        indentingPrintWriter.println("Time change log:");
        indentingPrintWriter.increaseIndent();
        SystemClockTime.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("Telephony suggestion history:");
        indentingPrintWriter.increaseIndent();
        this.mSuggestionBySlotIndex.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("Network suggestion history:");
        indentingPrintWriter.increaseIndent();
        this.mLastNetworkSuggestion.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("Gnss suggestion history:");
        indentingPrintWriter.increaseIndent();
        this.mLastGnssSuggestion.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("External suggestion history:");
        indentingPrintWriter.increaseIndent();
        this.mLastExternalSuggestion.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.decreaseIndent();
    }

    @GuardedBy({"this"})
    private boolean storeTelephonySuggestion(TelephonyTimeSuggestion telephonyTimeSuggestion) {
        UnixEpochTime unixEpochTime = telephonyTimeSuggestion.getUnixEpochTime();
        int slotIndex = telephonyTimeSuggestion.getSlotIndex();
        TelephonyTimeSuggestion telephonyTimeSuggestion2 = this.mSuggestionBySlotIndex.get(Integer.valueOf(slotIndex));
        if (telephonyTimeSuggestion2 != null) {
            if (telephonyTimeSuggestion2.getUnixEpochTime() == null) {
                Slog.w(LOG_TAG, "Previous suggestion is null or has a null time. previousSuggestion=" + telephonyTimeSuggestion2 + ", suggestion=" + telephonyTimeSuggestion);
                return false;
            }
            long elapsedRealtimeDifference = UnixEpochTime.elapsedRealtimeDifference(unixEpochTime, telephonyTimeSuggestion2.getUnixEpochTime());
            if (elapsedRealtimeDifference < 0) {
                Slog.w(LOG_TAG, "Out of order telephony suggestion received. referenceTimeDifference=" + elapsedRealtimeDifference + " previousSuggestion=" + telephonyTimeSuggestion2 + " suggestion=" + telephonyTimeSuggestion);
                return false;
            }
        }
        this.mSuggestionBySlotIndex.put(Integer.valueOf(slotIndex), telephonyTimeSuggestion);
        return true;
    }

    @GuardedBy({"this"})
    private boolean validateSuggestionCommon(UnixEpochTime unixEpochTime, Object obj) {
        long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        if (elapsedRealtimeMillis < unixEpochTime.getElapsedRealtimeMillis()) {
            Slog.w(LOG_TAG, "New elapsed realtime is in the future? Ignoring. elapsedRealtimeMillis=" + elapsedRealtimeMillis + ", suggestion=" + obj);
            return false;
        }
        if (unixEpochTime.getUnixEpochTimeMillis() <= this.mCurrentConfigurationInternal.getSuggestionUpperBound().toEpochMilli()) {
            return true;
        }
        Slog.w(LOG_TAG, "Suggested value is above max time supported by this device. suggestion=" + obj);
        return false;
    }

    @GuardedBy({"this"})
    private boolean validateAutoSuggestionTime(UnixEpochTime unixEpochTime, Object obj) {
        return validateSuggestionCommon(unixEpochTime, obj) && validateSuggestionAgainstLowerBound(unixEpochTime, obj, this.mCurrentConfigurationInternal.getAutoSuggestionLowerBound());
    }

    @GuardedBy({"this"})
    private boolean validateManualSuggestionTime(UnixEpochTime unixEpochTime, Object obj) {
        return validateSuggestionCommon(unixEpochTime, obj) && validateSuggestionAgainstLowerBound(unixEpochTime, obj, this.mCurrentConfigurationInternal.getManualSuggestionLowerBound());
    }

    @GuardedBy({"this"})
    private boolean validateSuggestionAgainstLowerBound(UnixEpochTime unixEpochTime, Object obj, Instant instant) {
        if (instant.toEpochMilli() <= unixEpochTime.getUnixEpochTimeMillis()) {
            return true;
        }
        Slog.w(LOG_TAG, "Suggestion points to time before lower bound, skipping it. suggestion=" + obj + ", lower bound=" + instant);
        return false;
    }

    @GuardedBy({"this"})
    private void doAutoTimeDetection(String str) {
        String str2;
        int[] autoOriginPriorities = this.mCurrentConfigurationInternal.getAutoOriginPriorities();
        for (int i : autoOriginPriorities) {
            UnixEpochTime unixEpochTime = null;
            if (i == 1) {
                TelephonyTimeSuggestion findBestTelephonySuggestion = findBestTelephonySuggestion();
                if (findBestTelephonySuggestion != null) {
                    unixEpochTime = findBestTelephonySuggestion.getUnixEpochTime();
                    str2 = "Found good telephony suggestion., bestTelephonySuggestion=" + findBestTelephonySuggestion + ", detectionReason=" + str;
                }
                str2 = null;
            } else if (i == 3) {
                NetworkTimeSuggestion findLatestValidNetworkSuggestion = findLatestValidNetworkSuggestion();
                if (findLatestValidNetworkSuggestion != null) {
                    unixEpochTime = findLatestValidNetworkSuggestion.getUnixEpochTime();
                    str2 = "Found good network suggestion., networkSuggestion=" + findLatestValidNetworkSuggestion + ", detectionReason=" + str;
                }
                str2 = null;
            } else if (i == 4) {
                GnssTimeSuggestion findLatestValidGnssSuggestion = findLatestValidGnssSuggestion();
                if (findLatestValidGnssSuggestion != null) {
                    unixEpochTime = findLatestValidGnssSuggestion.getUnixEpochTime();
                    str2 = "Found good gnss suggestion., gnssSuggestion=" + findLatestValidGnssSuggestion + ", detectionReason=" + str;
                }
                str2 = null;
            } else {
                if (i == 5) {
                    ExternalTimeSuggestion findLatestValidExternalSuggestion = findLatestValidExternalSuggestion();
                    if (findLatestValidExternalSuggestion != null) {
                        unixEpochTime = findLatestValidExternalSuggestion.getUnixEpochTime();
                        str2 = "Found good external suggestion., externalSuggestion=" + findLatestValidExternalSuggestion + ", detectionReason=" + str;
                    }
                } else {
                    Slog.w(LOG_TAG, "Unknown or unsupported origin=" + i + " in " + Arrays.toString(autoOriginPriorities) + ": Skipping");
                }
                str2 = null;
            }
            if (unixEpochTime != null) {
                if (this.mCurrentConfigurationInternal.getAutoDetectionEnabledBehavior()) {
                    setSystemClockAndConfidenceIfRequired(i, unixEpochTime, str2);
                    return;
                } else {
                    upgradeSystemClockConfidenceIfRequired(unixEpochTime, str2);
                    return;
                }
            }
        }
    }

    @GuardedBy({"this"})
    private TelephonyTimeSuggestion findBestTelephonySuggestion() {
        long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        TelephonyTimeSuggestion telephonyTimeSuggestion = null;
        int i = -1;
        for (int i2 = 0; i2 < this.mSuggestionBySlotIndex.size(); i2++) {
            Integer keyAt = this.mSuggestionBySlotIndex.keyAt(i2);
            TelephonyTimeSuggestion valueAt = this.mSuggestionBySlotIndex.valueAt(i2);
            if (valueAt == null) {
                Slog.w(LOG_TAG, "Latest suggestion unexpectedly null for slotIndex. slotIndex=" + keyAt);
            } else if (valueAt.getUnixEpochTime() == null) {
                Slog.w(LOG_TAG, "Latest suggestion unexpectedly empty.  candidateSuggestion=" + valueAt);
            } else {
                int scoreTelephonySuggestion = scoreTelephonySuggestion(elapsedRealtimeMillis, valueAt);
                if (scoreTelephonySuggestion != -1) {
                    if (telephonyTimeSuggestion == null || i < scoreTelephonySuggestion) {
                        i = scoreTelephonySuggestion;
                    } else if (i == scoreTelephonySuggestion) {
                        if (valueAt.getSlotIndex() >= telephonyTimeSuggestion.getSlotIndex()) {
                        }
                    }
                    telephonyTimeSuggestion = valueAt;
                }
            }
        }
        return telephonyTimeSuggestion;
    }

    private static int scoreTelephonySuggestion(long j, TelephonyTimeSuggestion telephonyTimeSuggestion) {
        UnixEpochTime unixEpochTime = telephonyTimeSuggestion.getUnixEpochTime();
        if (!validateSuggestionUnixEpochTime(j, unixEpochTime)) {
            Slog.w(LOG_TAG, "Existing suggestion found to be invalid elapsedRealtimeMillis=" + j + ", suggestion=" + telephonyTimeSuggestion);
            return -1;
        }
        int elapsedRealtimeMillis = (int) ((j - unixEpochTime.getElapsedRealtimeMillis()) / 3600000);
        if (elapsedRealtimeMillis >= TELEPHONY_BUCKET_COUNT) {
            return -1;
        }
        return TELEPHONY_BUCKET_COUNT - elapsedRealtimeMillis;
    }

    @GuardedBy({"this"})
    private NetworkTimeSuggestion findLatestValidNetworkSuggestion() {
        NetworkTimeSuggestion networkTimeSuggestion = this.mLastNetworkSuggestion.get();
        if (networkTimeSuggestion == null) {
            return null;
        }
        if (validateSuggestionUnixEpochTime(this.mEnvironment.elapsedRealtimeMillis(), networkTimeSuggestion.getUnixEpochTime())) {
            return networkTimeSuggestion;
        }
        return null;
    }

    @GuardedBy({"this"})
    private GnssTimeSuggestion findLatestValidGnssSuggestion() {
        GnssTimeSuggestion gnssTimeSuggestion = this.mLastGnssSuggestion.get();
        if (gnssTimeSuggestion == null) {
            return null;
        }
        if (validateSuggestionUnixEpochTime(this.mEnvironment.elapsedRealtimeMillis(), gnssTimeSuggestion.getUnixEpochTime())) {
            return gnssTimeSuggestion;
        }
        return null;
    }

    @GuardedBy({"this"})
    private ExternalTimeSuggestion findLatestValidExternalSuggestion() {
        ExternalTimeSuggestion externalTimeSuggestion = this.mLastExternalSuggestion.get();
        if (externalTimeSuggestion == null) {
            return null;
        }
        if (validateSuggestionUnixEpochTime(this.mEnvironment.elapsedRealtimeMillis(), externalTimeSuggestion.getUnixEpochTime())) {
            return externalTimeSuggestion;
        }
        return null;
    }

    @GuardedBy({"this"})
    private boolean setSystemClockAndConfidenceIfRequired(int i, UnixEpochTime unixEpochTime, String str) {
        if (isOriginAutomatic(i)) {
            if (!this.mCurrentConfigurationInternal.getAutoDetectionEnabledBehavior()) {
                return false;
            }
        } else if (this.mCurrentConfigurationInternal.getAutoDetectionEnabledBehavior()) {
            return false;
        }
        this.mEnvironment.acquireWakeLock();
        try {
            return setSystemClockAndConfidenceUnderWakeLock(i, unixEpochTime, 100, str);
        } finally {
            this.mEnvironment.releaseWakeLock();
        }
    }

    @GuardedBy({"this"})
    private void upgradeSystemClockConfidenceIfRequired(UnixEpochTime unixEpochTime, String str) {
        int systemClockConfidence = this.mEnvironment.systemClockConfidence();
        if (systemClockConfidence < 100) {
            this.mEnvironment.acquireWakeLock();
            try {
                long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
                long systemClockMillis = this.mEnvironment.systemClockMillis();
                if (isTimeWithinConfidenceThreshold(unixEpochTime, elapsedRealtimeMillis, systemClockMillis)) {
                    this.mEnvironment.setSystemClockConfidence(100, "Upgrade system clock confidence. autoDetectedUnixEpochTime=" + unixEpochTime + " newTimeConfidence=100 cause=" + str + " currentElapsedRealtimeMillis=" + elapsedRealtimeMillis + " currentSystemClockMillis=" + systemClockMillis + " currentTimeConfidence=" + systemClockConfidence);
                }
            } finally {
                this.mEnvironment.releaseWakeLock();
            }
        }
    }

    @GuardedBy({"this"})
    private boolean isTimeWithinConfidenceThreshold(UnixEpochTime unixEpochTime, long j, long j2) {
        return Math.abs(unixEpochTime.at(j).getUnixEpochTimeMillis() - j2) <= ((long) this.mCurrentConfigurationInternal.getSystemClockConfidenceThresholdMillis());
    }

    @GuardedBy({"this"})
    private boolean setSystemClockAndConfidenceUnderWakeLock(int i, UnixEpochTime unixEpochTime, int i2, String str) {
        UnixEpochTime unixEpochTime2;
        long elapsedRealtimeMillis = this.mEnvironment.elapsedRealtimeMillis();
        boolean isOriginAutomatic = isOriginAutomatic(i);
        long systemClockMillis = this.mEnvironment.systemClockMillis();
        if (isOriginAutomatic && (unixEpochTime2 = this.mLastAutoSystemClockTimeSet) != null) {
            long unixEpochTimeMillis = unixEpochTime2.at(elapsedRealtimeMillis).getUnixEpochTimeMillis();
            if (Math.abs(unixEpochTimeMillis - systemClockMillis) > SYSTEM_CLOCK_PARANOIA_THRESHOLD_MILLIS) {
                Slog.w(LOG_TAG, "System clock has not tracked elapsed real time clock. A clock may be inaccurate or something unexpectedly set the system clock. origin=" + TimeDetectorStrategy.originToString(i) + " elapsedRealtimeMillis=" + elapsedRealtimeMillis + " expectedTimeMillis=" + unixEpochTimeMillis + " actualTimeMillis=" + systemClockMillis + " cause=" + str);
            }
        }
        long unixEpochTimeMillis2 = unixEpochTime.at(elapsedRealtimeMillis).getUnixEpochTimeMillis();
        boolean z = Math.abs(unixEpochTimeMillis2 - systemClockMillis) >= ((long) this.mCurrentConfigurationInternal.getSystemClockUpdateThresholdMillis());
        int systemClockConfidence = this.mEnvironment.systemClockConfidence();
        boolean z2 = i2 != systemClockConfidence;
        if (!z) {
            if (!z2) {
                return true;
            }
            this.mEnvironment.setSystemClockConfidence(i2, "Set system clock confidence. origin=" + TimeDetectorStrategy.originToString(i) + " newTime=" + unixEpochTime + " newTimeConfidence=" + i2 + " cause=" + str + " elapsedRealtimeMillis=" + elapsedRealtimeMillis + " (old) actualSystemClockMillis=" + systemClockMillis + " newSystemClockMillis=" + unixEpochTimeMillis2 + " currentTimeConfidence=" + systemClockConfidence);
            return true;
        }
        this.mEnvironment.setSystemClock(unixEpochTimeMillis2, i2, "Set system clock & confidence. origin=" + TimeDetectorStrategy.originToString(i) + " newTime=" + unixEpochTime + " newTimeConfidence=" + i2 + " cause=" + str + " elapsedRealtimeMillis=" + elapsedRealtimeMillis + " (old) actualSystemClockMillis=" + systemClockMillis + " newSystemClockMillis=" + unixEpochTimeMillis2 + " currentTimeConfidence=" + systemClockConfidence);
        if (isOriginAutomatic(i)) {
            this.mLastAutoSystemClockTimeSet = unixEpochTime;
            return true;
        }
        this.mLastAutoSystemClockTimeSet = null;
        return true;
    }

    @VisibleForTesting
    public synchronized TelephonyTimeSuggestion findBestTelephonySuggestionForTests() {
        return findBestTelephonySuggestion();
    }

    @VisibleForTesting
    public synchronized NetworkTimeSuggestion findLatestValidNetworkSuggestionForTests() {
        return findLatestValidNetworkSuggestion();
    }

    @VisibleForTesting
    public synchronized GnssTimeSuggestion findLatestValidGnssSuggestionForTests() {
        return findLatestValidGnssSuggestion();
    }

    @VisibleForTesting
    public synchronized ExternalTimeSuggestion findLatestValidExternalSuggestionForTests() {
        return findLatestValidExternalSuggestion();
    }

    @VisibleForTesting
    public synchronized TelephonyTimeSuggestion getLatestTelephonySuggestion(int i) {
        return this.mSuggestionBySlotIndex.get(Integer.valueOf(i));
    }

    @VisibleForTesting
    public synchronized GnssTimeSuggestion getLatestGnssSuggestion() {
        return this.mLastGnssSuggestion.get();
    }

    @VisibleForTesting
    public synchronized ExternalTimeSuggestion getLatestExternalSuggestion() {
        return this.mLastExternalSuggestion.get();
    }

    private static boolean validateSuggestionUnixEpochTime(long j, UnixEpochTime unixEpochTime) {
        long elapsedRealtimeMillis = unixEpochTime.getElapsedRealtimeMillis();
        return elapsedRealtimeMillis <= j && j - elapsedRealtimeMillis <= 86400000;
    }
}
