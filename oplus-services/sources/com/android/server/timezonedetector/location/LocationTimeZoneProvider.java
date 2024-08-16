package com.android.server.timezonedetector.location;

import android.os.SystemClock;
import android.service.timezone.TimeZoneProviderEvent;
import android.service.timezone.TimeZoneProviderStatus;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.timezonedetector.ConfigurationInternal;
import com.android.server.timezonedetector.Dumpable;
import com.android.server.timezonedetector.ReferenceWithHistory;
import com.android.server.timezonedetector.location.ThreadingDomain;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class LocationTimeZoneProvider implements Dumpable {
    private final ThreadingDomain.SingleRunnableQueue mInitializationTimeoutQueue;
    ProviderListener mProviderListener;
    private final ProviderMetricsLogger mProviderMetricsLogger;
    final String mProviderName;
    private final boolean mRecordStateChanges;
    final Object mSharedLock;
    final ThreadingDomain mThreadingDomain;
    private final TimeZoneProviderEventPreProcessor mTimeZoneProviderEventPreProcessor;

    @GuardedBy({"mSharedLock"})
    private final ArrayList<ProviderState> mRecordedStates = new ArrayList<>(0);

    @GuardedBy({"mSharedLock"})
    final ReferenceWithHistory<ProviderState> mCurrentState = new ReferenceWithHistory<>(10);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ProviderListener {
        void onProviderStateChange(ProviderState providerState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ProviderMetricsLogger {
        void onProviderStateChanged(int i);
    }

    @GuardedBy({"mSharedLock"})
    abstract void onDestroy();

    @GuardedBy({"mSharedLock"})
    abstract boolean onInitialize();

    @GuardedBy({"mSharedLock"})
    void onSetCurrentState(ProviderState providerState) {
    }

    abstract void onStartUpdates(Duration duration, Duration duration2);

    abstract void onStopUpdates();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ProviderState {
        static final int PROVIDER_STATE_DESTROYED = 6;
        static final int PROVIDER_STATE_PERM_FAILED = 5;
        static final int PROVIDER_STATE_STARTED_CERTAIN = 2;
        static final int PROVIDER_STATE_STARTED_INITIALIZING = 1;
        static final int PROVIDER_STATE_STARTED_UNCERTAIN = 3;
        static final int PROVIDER_STATE_STOPPED = 4;
        static final int PROVIDER_STATE_UNKNOWN = 0;
        public final ConfigurationInternal currentUserConfiguration;
        public final TimeZoneProviderEvent event;
        private final String mDebugInfo;
        private final long mStateEntryTimeMillis;
        public final LocationTimeZoneProvider provider;
        public final int stateEnum;

        @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        @interface ProviderStateEnum {
        }

        private ProviderState(LocationTimeZoneProvider locationTimeZoneProvider, int i, TimeZoneProviderEvent timeZoneProviderEvent, ConfigurationInternal configurationInternal, String str) {
            Objects.requireNonNull(locationTimeZoneProvider);
            this.provider = locationTimeZoneProvider;
            this.stateEnum = i;
            this.event = timeZoneProviderEvent;
            this.currentUserConfiguration = configurationInternal;
            this.mStateEntryTimeMillis = SystemClock.elapsedRealtime();
            this.mDebugInfo = str;
        }

        static ProviderState createStartingState(LocationTimeZoneProvider locationTimeZoneProvider) {
            return new ProviderState(locationTimeZoneProvider, 0, null, null, "Initial state");
        }

        ProviderState newState(int i, TimeZoneProviderEvent timeZoneProviderEvent, ConfigurationInternal configurationInternal, String str) {
            switch (this.stateEnum) {
                case 0:
                    if (i != 4) {
                        throw new IllegalArgumentException("Must transition from " + prettyPrintStateEnum(0) + " to " + prettyPrintStateEnum(4));
                    }
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    break;
                case 5:
                case 6:
                    throw new IllegalArgumentException("Illegal transition out of " + prettyPrintStateEnum(this.stateEnum));
                default:
                    throw new IllegalArgumentException("Invalid this.stateEnum=" + this.stateEnum);
            }
            switch (i) {
                case 0:
                    throw new IllegalArgumentException("Cannot transition to " + prettyPrintStateEnum(0));
                case 1:
                case 2:
                case 3:
                    if (configurationInternal == null) {
                        throw new IllegalArgumentException("Started state: currentUserConfig must not be null");
                    }
                    break;
                case 4:
                    if (timeZoneProviderEvent != null || configurationInternal != null) {
                        throw new IllegalArgumentException("Stopped state: event and currentUserConfig must be null, event=" + timeZoneProviderEvent + ", currentUserConfig=" + configurationInternal);
                    }
                case 5:
                case 6:
                    if (timeZoneProviderEvent != null || configurationInternal != null) {
                        throw new IllegalArgumentException("Terminal state: event and currentUserConfig must be null, newStateEnum=" + i + ", event=" + timeZoneProviderEvent + ", currentUserConfig=" + configurationInternal);
                    }
                default:
                    throw new IllegalArgumentException("Unknown newStateEnum=" + i);
            }
            return new ProviderState(this.provider, i, timeZoneProviderEvent, configurationInternal, str);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isStarted() {
            int i = this.stateEnum;
            return i == 1 || i == 2 || i == 3;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isTerminated() {
            int i = this.stateEnum;
            return i == 5 || i == 6;
        }

        public int getProviderStatus() {
            switch (this.stateEnum) {
                case 1:
                    return 2;
                case 2:
                    return 3;
                case 3:
                    return 4;
                case 4:
                case 6:
                    return 2;
                case 5:
                    return 1;
                default:
                    throw new IllegalStateException("Unknown state enum:" + prettyPrintStateEnum(this.stateEnum));
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public TimeZoneProviderStatus getReportedStatus() {
            TimeZoneProviderEvent timeZoneProviderEvent = this.event;
            if (timeZoneProviderEvent == null) {
                return null;
            }
            return timeZoneProviderEvent.getTimeZoneProviderStatus();
        }

        public String toString() {
            return "ProviderState{stateEnum=" + prettyPrintStateEnum(this.stateEnum) + ", event=" + this.event + ", currentUserConfiguration=" + this.currentUserConfiguration + ", mStateEntryTimeMillis=" + this.mStateEntryTimeMillis + ", mDebugInfo=" + this.mDebugInfo + '}';
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ProviderState providerState = (ProviderState) obj;
            return this.stateEnum == providerState.stateEnum && Objects.equals(this.event, providerState.event) && Objects.equals(this.currentUserConfiguration, providerState.currentUserConfiguration);
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.stateEnum), this.event, this.currentUserConfiguration);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String prettyPrintStateEnum(int i) {
            switch (i) {
                case 1:
                    return "Started initializing (1)";
                case 2:
                    return "Started certain (2)";
                case 3:
                    return "Started uncertain (3)";
                case 4:
                    return "Stopped (4)";
                case 5:
                    return "Perm failure (5)";
                case 6:
                    return "Destroyed (6)";
                default:
                    return "Unknown (" + i + ")";
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocationTimeZoneProvider(ProviderMetricsLogger providerMetricsLogger, ThreadingDomain threadingDomain, String str, TimeZoneProviderEventPreProcessor timeZoneProviderEventPreProcessor, boolean z) {
        Objects.requireNonNull(threadingDomain);
        this.mThreadingDomain = threadingDomain;
        Objects.requireNonNull(providerMetricsLogger);
        this.mProviderMetricsLogger = providerMetricsLogger;
        this.mInitializationTimeoutQueue = threadingDomain.createSingleRunnableQueue();
        this.mSharedLock = threadingDomain.getLockObject();
        Objects.requireNonNull(str);
        this.mProviderName = str;
        Objects.requireNonNull(timeZoneProviderEventPreProcessor);
        this.mTimeZoneProviderEventPreProcessor = timeZoneProviderEventPreProcessor;
        this.mRecordStateChanges = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void initialize(ProviderListener providerListener) {
        String str;
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            if (this.mProviderListener != null) {
                throw new IllegalStateException("initialize already called");
            }
            Objects.requireNonNull(providerListener);
            this.mProviderListener = providerListener;
            ProviderState newState = ProviderState.createStartingState(this).newState(4, null, null, "initialize");
            boolean z = false;
            setCurrentState(newState, false);
            try {
                str = "onInitialize() returned false";
                z = onInitialize();
            } catch (RuntimeException e) {
                LocationTimeZoneManagerService.warnLog("Unable to initialize the provider due to exception", e);
                str = "onInitialize() threw exception:" + e.getMessage();
            }
            if (!z) {
                setCurrentState(newState.newState(5, null, null, "Failed to initialize: " + str), true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void destroy() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            ProviderState providerState = this.mCurrentState.get();
            if (!providerState.isTerminated()) {
                setCurrentState(providerState.newState(6, null, null, "destroy"), false);
                onDestroy();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void clearRecordedStates() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            this.mRecordedStates.clear();
            this.mRecordedStates.trimToSize();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<ProviderState> getRecordedStates() {
        ArrayList arrayList;
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            arrayList = new ArrayList(this.mRecordedStates);
        }
        return arrayList;
    }

    private void setCurrentState(ProviderState providerState, boolean z) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            ProviderState providerState2 = this.mCurrentState.get();
            this.mCurrentState.set(providerState);
            onSetCurrentState(providerState);
            if (!Objects.equals(providerState, providerState2)) {
                this.mProviderMetricsLogger.onProviderStateChanged(providerState.stateEnum);
                if (this.mRecordStateChanges) {
                    this.mRecordedStates.add(providerState);
                }
                if (z) {
                    this.mProviderListener.onProviderStateChange(providerState);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ProviderState getCurrentState() {
        ProviderState providerState;
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            providerState = this.mCurrentState.get();
        }
        return providerState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String getName() {
        this.mThreadingDomain.assertCurrentThread();
        return this.mProviderName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void startUpdates(ConfigurationInternal configurationInternal, Duration duration, Duration duration2, Duration duration3) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            assertCurrentState(4);
            setCurrentState(this.mCurrentState.get().newState(1, null, configurationInternal, "startUpdates"), false);
            this.mInitializationTimeoutQueue.runDelayed(new Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LocationTimeZoneProvider.this.handleInitializationTimeout();
                }
            }, duration.plus(duration2).toMillis());
            onStartUpdates(duration, duration3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitializationTimeout() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            ProviderState providerState = this.mCurrentState.get();
            if (providerState.stateEnum == 1) {
                setCurrentState(providerState.newState(3, null, providerState.currentUserConfiguration, "handleInitializationTimeout"), true);
            } else {
                LocationTimeZoneManagerService.warnLog("handleInitializationTimeout: Initialization timeout triggered when in an unexpected state=" + providerState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void stopUpdates() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            assertIsStarted();
            setCurrentState(this.mCurrentState.get().newState(4, null, null, "stopUpdates"), false);
            cancelInitializationTimeoutIfSet();
            onStopUpdates();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00fc, code lost:
    
        r5 = 3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void handleTimeZoneProviderEvent(TimeZoneProviderEvent timeZoneProviderEvent) {
        this.mThreadingDomain.assertCurrentThread();
        Objects.requireNonNull(timeZoneProviderEvent);
        TimeZoneProviderEvent preProcess = this.mTimeZoneProviderEventPreProcessor.preProcess(timeZoneProviderEvent);
        synchronized (this.mSharedLock) {
            LocationTimeZoneManagerService.debugLog("handleTimeZoneProviderEvent: mProviderName=" + this.mProviderName + ", timeZoneProviderEvent=" + preProcess);
            ProviderState providerState = this.mCurrentState.get();
            int type = preProcess.getType();
            int i = 2;
            switch (providerState.stateEnum) {
                case 1:
                case 2:
                case 3:
                    if (type == 1) {
                        String str = "handleTimeZoneProviderEvent: Failure event=" + preProcess + " received for provider=" + this.mProviderName + " in state=" + ProviderState.prettyPrintStateEnum(providerState.stateEnum) + ", entering permanently failed state";
                        LocationTimeZoneManagerService.warnLog(str);
                        setCurrentState(providerState.newState(5, null, null, str), true);
                        cancelInitializationTimeoutIfSet();
                        return;
                    }
                    if (type != 2 && type != 3) {
                        throw new IllegalStateException("Unknown eventType=" + preProcess);
                    }
                    setCurrentState(providerState.newState(i, preProcess, providerState.currentUserConfiguration, "handleTimeZoneProviderEvent"), true);
                    cancelInitializationTimeoutIfSet();
                    return;
                case 4:
                    if (type == 1) {
                        String str2 = "handleTimeZoneProviderEvent: Failure event=" + preProcess + " received for stopped provider=" + this.mProviderName + ", entering permanently failed state";
                        LocationTimeZoneManagerService.warnLog(str2);
                        setCurrentState(providerState.newState(5, null, null, str2), true);
                        cancelInitializationTimeoutIfSet();
                        return;
                    }
                    if (type != 2 && type != 3) {
                        throw new IllegalStateException("Unknown eventType=" + preProcess);
                    }
                    LocationTimeZoneManagerService.warnLog("handleTimeZoneProviderEvent: event=" + preProcess + " received for stopped provider=" + this + ", ignoring");
                    return;
                case 5:
                case 6:
                    LocationTimeZoneManagerService.warnLog("handleTimeZoneProviderEvent: Event=" + preProcess + " received for provider=" + this + " when in terminated state");
                    return;
                default:
                    throw new IllegalStateException("Unknown providerType=" + providerState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0012. Please report as an issue. */
    public final void handleTemporaryFailure(String str) {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            ProviderState providerState = this.mCurrentState.get();
            switch (providerState.stateEnum) {
                case 1:
                case 2:
                case 3:
                    setCurrentState(providerState.newState(3, null, providerState.currentUserConfiguration, "handleTemporaryFailure: reason=" + str + ", currentState=" + ProviderState.prettyPrintStateEnum(providerState.stateEnum)), true);
                    cancelInitializationTimeoutIfSet();
                    break;
                case 4:
                    LocationTimeZoneManagerService.debugLog("handleProviderLost reason=" + str + ", mProviderName=" + this.mProviderName + ", currentState=" + providerState + ": No state change required, provider is stopped.");
                    break;
                case 5:
                case 6:
                    LocationTimeZoneManagerService.debugLog("handleProviderLost reason=" + str + ", mProviderName=" + this.mProviderName + ", currentState=" + providerState + ": No state change required, provider is terminated.");
                    break;
                default:
                    throw new IllegalStateException("Unknown currentState=" + providerState);
            }
        }
    }

    @GuardedBy({"mSharedLock"})
    private void assertIsStarted() {
        ProviderState providerState = this.mCurrentState.get();
        if (providerState.isStarted()) {
            return;
        }
        throw new IllegalStateException("Required a started state, but was " + providerState);
    }

    @GuardedBy({"mSharedLock"})
    private void assertCurrentState(int i) {
        ProviderState providerState = this.mCurrentState.get();
        if (providerState.stateEnum == i) {
            return;
        }
        throw new IllegalStateException("Required stateEnum=" + i + ", but was " + providerState);
    }

    @VisibleForTesting
    boolean isInitializationTimeoutSet() {
        boolean hasQueued;
        synchronized (this.mSharedLock) {
            hasQueued = this.mInitializationTimeoutQueue.hasQueued();
        }
        return hasQueued;
    }

    @GuardedBy({"mSharedLock"})
    private void cancelInitializationTimeoutIfSet() {
        if (this.mInitializationTimeoutQueue.hasQueued()) {
            this.mInitializationTimeoutQueue.cancel();
        }
    }

    @VisibleForTesting
    Duration getInitializationTimeoutDelay() {
        Duration ofMillis;
        synchronized (this.mSharedLock) {
            ofMillis = Duration.ofMillis(this.mInitializationTimeoutQueue.getQueuedDelayMillis());
        }
        return ofMillis;
    }
}
