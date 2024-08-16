package com.android.server.location.eventlog;

import android.location.LocationRequest;
import android.location.provider.ProviderRequest;
import android.location.util.identity.CallerIdentity;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.android.server.job.controllers.JobStatus;
import com.android.server.location.LocationManagerService;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.eventlog.LocalEventLog;
import com.android.server.location.interfaces.ILocationEventLogExt;
import com.android.server.policy.PhoneWindowManager;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.usb.descriptors.UsbDescriptor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocationEventLog extends LocalEventLog<Object> {
    public static final LocationEventLog EVENT_LOG = new LocationEventLog();

    @GuardedBy({"mAggregateStats"})
    private final ArrayMap<String, ArrayMap<CallerIdentity, AggregateStats>> mAggregateStats;
    private final ILocationEventLogWrapper mLocationWrapper;

    @GuardedBy({"this"})
    private final LocationsEventLog mLocationsLog;

    private static int getLogSize() {
        if (LocationManagerService.D) {
            return PhoneWindowManager.TOAST_WINDOW_ANIM_BUFFER;
        }
        return 300;
    }

    private static int getLocationsLogSize() {
        if (LocationManagerService.D) {
            return UsbDescriptor.USB_CONTROL_TRANSFER_TIMEOUT_MS;
        }
        return 100;
    }

    private LocationEventLog() {
        super(getLogSize(), Object.class);
        this.mLocationWrapper = new LocationEventLogWrapper();
        this.mAggregateStats = new ArrayMap<>(4);
        this.mLocationsLog = new LocationsEventLog(getLocationsLogSize());
    }

    public ArrayMap<String, ArrayMap<CallerIdentity, AggregateStats>> copyAggregateStats() {
        ArrayMap<String, ArrayMap<CallerIdentity, AggregateStats>> arrayMap;
        synchronized (this.mAggregateStats) {
            arrayMap = new ArrayMap<>(this.mAggregateStats);
            for (int i = 0; i < arrayMap.size(); i++) {
                arrayMap.setValueAt(i, new ArrayMap<>(arrayMap.valueAt(i)));
            }
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AggregateStats getAggregateStats(String str, CallerIdentity callerIdentity) {
        AggregateStats aggregateStats;
        synchronized (this.mAggregateStats) {
            ArrayMap<CallerIdentity, AggregateStats> arrayMap = this.mAggregateStats.get(str);
            if (arrayMap == null) {
                arrayMap = new ArrayMap<>(2);
                this.mAggregateStats.put(str, arrayMap);
            }
            CallerIdentity forAggregation = CallerIdentity.forAggregation(callerIdentity);
            aggregateStats = arrayMap.get(forAggregation);
            if (aggregateStats == null) {
                aggregateStats = new AggregateStats();
                arrayMap.put(forAggregation, aggregateStats);
            }
        }
        return aggregateStats;
    }

    public void logUserSwitched(int i, int i2) {
        addLog(new UserSwitchedEvent(i, i2));
    }

    public void logUserVisibilityChanged(int i, boolean z) {
        addLog(new UserVisibilityChangedEvent(i, z));
    }

    public void logLocationEnabled(int i, boolean z) {
        addLog(new LocationEnabledEvent(i, z));
    }

    public void logAdasLocationEnabled(int i, boolean z) {
        addLog(new LocationAdasEnabledEvent(i, z));
    }

    public void logProviderEnabled(String str, int i, boolean z) {
        addLog(new ProviderEnabledEvent(str, i, z));
    }

    public void logProviderMocked(String str, boolean z) {
        addLog(new ProviderMockedEvent(str, z));
    }

    public void logProviderClientRegistered(String str, CallerIdentity callerIdentity, LocationRequest locationRequest) {
        addLog(new ProviderClientRegisterEvent(str, true, callerIdentity, locationRequest));
        getAggregateStats(str, callerIdentity).markRequestAdded(locationRequest.getIntervalMillis());
    }

    public void logProviderClientUnregistered(String str, CallerIdentity callerIdentity) {
        addLog(new ProviderClientRegisterEvent(str, false, callerIdentity, null));
        getAggregateStats(str, callerIdentity).markRequestRemoved();
    }

    public void logProviderClientActive(String str, CallerIdentity callerIdentity) {
        getAggregateStats(str, callerIdentity).markRequestActive();
    }

    public void logProviderClientInactive(String str, CallerIdentity callerIdentity) {
        getAggregateStats(str, callerIdentity).markRequestInactive();
    }

    public void logProviderClientForeground(String str, CallerIdentity callerIdentity) {
        if (LocationManagerService.D) {
            addLog(new ProviderClientForegroundEvent(str, true, callerIdentity));
        }
        getAggregateStats(str, callerIdentity).markRequestForeground();
    }

    public void logProviderClientBackground(String str, CallerIdentity callerIdentity) {
        if (LocationManagerService.D) {
            addLog(new ProviderClientForegroundEvent(str, false, callerIdentity));
        }
        getAggregateStats(str, callerIdentity).markRequestBackground();
    }

    public void logProviderClientPermitted(String str, CallerIdentity callerIdentity) {
        if (LocationManagerService.D) {
            addLog(new ProviderClientPermittedEvent(str, true, callerIdentity));
        }
    }

    public void logProviderClientUnpermitted(String str, CallerIdentity callerIdentity) {
        if (LocationManagerService.D) {
            addLog(new ProviderClientPermittedEvent(str, false, callerIdentity));
        }
    }

    public void logProviderUpdateRequest(String str, ProviderRequest providerRequest) {
        addLog(new ProviderUpdateEvent(str, providerRequest));
    }

    public void logProviderReceivedLocations(String str, int i) {
        synchronized (this) {
            this.mLocationsLog.logProviderReceivedLocations(str, i);
        }
    }

    public void logProviderDeliveredLocations(String str, int i, CallerIdentity callerIdentity) {
        if (getExtLoader() != null && getExtLoader().enablePassiveDeliveredLocations(str)) {
            getAggregateStats(str, callerIdentity).markLocationDelivered();
            return;
        }
        synchronized (this) {
            this.mLocationsLog.logProviderDeliveredLocations(str, i, callerIdentity);
        }
        getAggregateStats(str, callerIdentity).markLocationDelivered();
    }

    public void logProviderStationaryThrottled(String str, boolean z, ProviderRequest providerRequest) {
        addLog(new ProviderStationaryThrottledEvent(str, z, providerRequest));
    }

    public void logLocationPowerSaveMode(int i) {
        addLog(new LocationPowerSaveModeEvent(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addLog(Object obj) {
        addLog(SystemClock.elapsedRealtime(), obj);
    }

    @Override // com.android.server.location.eventlog.LocalEventLog
    public synchronized void iterate(LocalEventLog.LogConsumer<? super Object> logConsumer) {
        LocalEventLog.iterate(logConsumer, this, this.mLocationsLog);
    }

    public void iterate(Consumer<String> consumer) {
        iterate(consumer, (String) null);
    }

    public void iterate(final Consumer<String> consumer, final String str) {
        final long currentTimeMillis = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        final StringBuilder sb = new StringBuilder();
        iterate(new LocalEventLog.LogConsumer() { // from class: com.android.server.location.eventlog.LocationEventLog$$ExternalSyntheticLambda0
            @Override // com.android.server.location.eventlog.LocalEventLog.LogConsumer
            public final void acceptLog(long j, Object obj) {
                LocationEventLog.lambda$iterate$0(str, sb, currentTimeMillis, consumer, j, obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$iterate$0(String str, StringBuilder sb, long j, Consumer consumer, long j2, Object obj) {
        if (str == null || ((obj instanceof ProviderEvent) && str.equals(((ProviderEvent) obj).mProvider))) {
            sb.setLength(0);
            sb.append(TimeUtils.logTimeOfDay(j2 + j));
            sb.append(": ");
            sb.append(obj);
            consumer.accept(sb.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class ProviderEvent {
        protected final String mProvider;

        ProviderEvent(String str) {
            this.mProvider = str;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ProviderEnabledEvent extends ProviderEvent {
        private final boolean mEnabled;
        private final int mUserId;

        ProviderEnabledEvent(String str, int i, boolean z) {
            super(str);
            this.mUserId = i;
            this.mEnabled = z;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mProvider);
            sb.append(" provider [u");
            sb.append(this.mUserId);
            sb.append("] ");
            sb.append(this.mEnabled ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ProviderMockedEvent extends ProviderEvent {
        private final boolean mMocked;

        ProviderMockedEvent(String str, boolean z) {
            super(str);
            this.mMocked = z;
        }

        public String toString() {
            if (this.mMocked) {
                return this.mProvider + " provider added mock provider override";
            }
            return this.mProvider + " provider removed mock provider override";
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ProviderClientRegisterEvent extends ProviderEvent {
        private final CallerIdentity mIdentity;
        private final LocationRequest mLocationRequest;
        private final boolean mRegistered;

        ProviderClientRegisterEvent(String str, boolean z, CallerIdentity callerIdentity, LocationRequest locationRequest) {
            super(str);
            this.mRegistered = z;
            this.mIdentity = callerIdentity;
            this.mLocationRequest = locationRequest;
        }

        public String toString() {
            if (this.mRegistered) {
                return this.mProvider + " provider +registration " + this.mIdentity + " -> " + this.mLocationRequest;
            }
            return this.mProvider + " provider -registration " + this.mIdentity;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ProviderClientForegroundEvent extends ProviderEvent {
        private final boolean mForeground;
        private final CallerIdentity mIdentity;

        ProviderClientForegroundEvent(String str, boolean z, CallerIdentity callerIdentity) {
            super(str);
            this.mForeground = z;
            this.mIdentity = callerIdentity;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mProvider);
            sb.append(" provider client ");
            sb.append(this.mIdentity);
            sb.append(" -> ");
            sb.append(this.mForeground ? "foreground" : "background");
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ProviderClientPermittedEvent extends ProviderEvent {
        private final CallerIdentity mIdentity;
        private final boolean mPermitted;

        ProviderClientPermittedEvent(String str, boolean z, CallerIdentity callerIdentity) {
            super(str);
            this.mPermitted = z;
            this.mIdentity = callerIdentity;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mProvider);
            sb.append(" provider client ");
            sb.append(this.mIdentity);
            sb.append(" -> ");
            sb.append(this.mPermitted ? "permitted" : "unpermitted");
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ProviderUpdateEvent extends ProviderEvent {
        private final ProviderRequest mRequest;

        ProviderUpdateEvent(String str, ProviderRequest providerRequest) {
            super(str);
            this.mRequest = providerRequest;
        }

        public String toString() {
            return this.mProvider + " provider request = " + this.mRequest;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ProviderReceiveLocationEvent extends ProviderEvent {
        private final int mNumLocations;

        ProviderReceiveLocationEvent(String str, int i) {
            super(str);
            this.mNumLocations = i;
        }

        public String toString() {
            return this.mProvider + " provider received location[" + this.mNumLocations + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class ProviderDeliverLocationEvent extends ProviderEvent {
        private final CallerIdentity mIdentity;
        private final int mNumLocations;

        ProviderDeliverLocationEvent(String str, int i, CallerIdentity callerIdentity) {
            super(str);
            this.mNumLocations = i;
            this.mIdentity = callerIdentity;
        }

        public String toString() {
            return this.mProvider + " provider delivered location[" + this.mNumLocations + "] to " + this.mIdentity;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class ProviderStationaryThrottledEvent extends ProviderEvent {
        private final ProviderRequest mRequest;
        private final boolean mStationaryThrottled;

        ProviderStationaryThrottledEvent(String str, boolean z, ProviderRequest providerRequest) {
            super(str);
            this.mStationaryThrottled = z;
            this.mRequest = providerRequest;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mProvider);
            sb.append(" provider stationary/idle ");
            sb.append(this.mStationaryThrottled ? "throttled" : "unthrottled");
            sb.append(", request = ");
            sb.append(this.mRequest);
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class LocationPowerSaveModeEvent {
        private final int mLocationPowerSaveMode;

        LocationPowerSaveModeEvent(int i) {
            this.mLocationPowerSaveMode = i;
        }

        public String toString() {
            int i = this.mLocationPowerSaveMode;
            return "location power save mode changed to " + (i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "UNKNOWN" : "THROTTLE_REQUESTS_WHEN_SCREEN_OFF" : "FOREGROUND_ONLY" : "ALL_DISABLED_WHEN_SCREEN_OFF" : "GPS_DISABLED_WHEN_SCREEN_OFF" : "NO_CHANGE");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class UserSwitchedEvent {
        private final int mUserIdFrom;
        private final int mUserIdTo;

        UserSwitchedEvent(int i, int i2) {
            this.mUserIdFrom = i;
            this.mUserIdTo = i2;
        }

        public String toString() {
            return "current user switched from u" + this.mUserIdFrom + " to u" + this.mUserIdTo;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class UserVisibilityChangedEvent {
        private final int mUserId;
        private final boolean mVisible;

        UserVisibilityChangedEvent(int i, boolean z) {
            this.mUserId = i;
            this.mVisible = z;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[u");
            sb.append(this.mUserId);
            sb.append("] ");
            sb.append(this.mVisible ? "visible" : "invisible");
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class LocationEnabledEvent {
        private final boolean mEnabled;
        private final int mUserId;

        LocationEnabledEvent(int i, boolean z) {
            this.mUserId = i;
            this.mEnabled = z;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("location [u");
            sb.append(this.mUserId);
            sb.append("] ");
            sb.append(this.mEnabled ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static final class LocationAdasEnabledEvent {
        private final boolean mEnabled;
        private final int mUserId;

        LocationAdasEnabledEvent(int i, boolean z) {
            this.mUserId = i;
            this.mEnabled = z;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("adas location [u");
            sb.append(this.mUserId);
            sb.append("] ");
            sb.append(this.mEnabled ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class LocationsEventLog extends LocalEventLog<Object> {
        LocationsEventLog(int i) {
            super(i, Object.class);
        }

        public void logProviderReceivedLocations(String str, int i) {
            addLog(new ProviderReceiveLocationEvent(str, i));
        }

        public void logProviderDeliveredLocations(String str, int i, CallerIdentity callerIdentity) {
            addLog(new ProviderDeliverLocationEvent(str, i, callerIdentity));
        }

        private void addLog(Object obj) {
            addLog(SystemClock.elapsedRealtime(), obj);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class AggregateStats {

        @GuardedBy({"this"})
        private int mActiveRequestCount;

        @GuardedBy({"this"})
        private long mActiveTimeLastUpdateRealtimeMs;

        @GuardedBy({"this"})
        private long mActiveTimeTotalMs;

        @GuardedBy({"this"})
        private int mAddedRequestCount;

        @GuardedBy({"this"})
        private long mAddedTimeLastUpdateRealtimeMs;

        @GuardedBy({"this"})
        private long mAddedTimeTotalMs;

        @GuardedBy({"this"})
        private int mDeliveredLocationCount;

        @GuardedBy({"this"})
        private int mForegroundRequestCount;

        @GuardedBy({"this"})
        private long mForegroundTimeLastUpdateRealtimeMs;

        @GuardedBy({"this"})
        private long mForegroundTimeTotalMs;

        @GuardedBy({"this"})
        private long mFastestIntervalMs = JobStatus.NO_LATEST_RUNTIME;

        @GuardedBy({"this"})
        private long mSlowestIntervalMs = 0;

        AggregateStats() {
        }

        synchronized void markRequestAdded(long j) {
            int i = this.mAddedRequestCount;
            this.mAddedRequestCount = i + 1;
            if (i == 0) {
                this.mAddedTimeLastUpdateRealtimeMs = SystemClock.elapsedRealtime();
            }
            this.mFastestIntervalMs = Math.min(j, this.mFastestIntervalMs);
            this.mSlowestIntervalMs = Math.max(j, this.mSlowestIntervalMs);
        }

        synchronized void markRequestRemoved() {
            updateTotals();
            boolean z = true;
            int i = this.mAddedRequestCount - 1;
            this.mAddedRequestCount = i;
            if (i < 0) {
                z = false;
            }
            Preconditions.checkState(z);
            this.mActiveRequestCount = Math.min(this.mAddedRequestCount, this.mActiveRequestCount);
            this.mForegroundRequestCount = Math.min(this.mAddedRequestCount, this.mForegroundRequestCount);
        }

        synchronized void markRequestActive() {
            Preconditions.checkState(this.mAddedRequestCount > 0);
            int i = this.mActiveRequestCount;
            this.mActiveRequestCount = i + 1;
            if (i == 0) {
                this.mActiveTimeLastUpdateRealtimeMs = SystemClock.elapsedRealtime();
            }
        }

        synchronized void markRequestInactive() {
            updateTotals();
            boolean z = true;
            int i = this.mActiveRequestCount - 1;
            this.mActiveRequestCount = i;
            if (i < 0) {
                z = false;
            }
            Preconditions.checkState(z);
        }

        synchronized void markRequestForeground() {
            Preconditions.checkState(this.mAddedRequestCount > 0);
            int i = this.mForegroundRequestCount;
            this.mForegroundRequestCount = i + 1;
            if (i == 0) {
                this.mForegroundTimeLastUpdateRealtimeMs = SystemClock.elapsedRealtime();
            }
        }

        synchronized void markRequestBackground() {
            updateTotals();
            boolean z = true;
            int i = this.mForegroundRequestCount - 1;
            this.mForegroundRequestCount = i;
            if (i < 0) {
                z = false;
            }
            Preconditions.checkState(z);
        }

        synchronized void markLocationDelivered() {
            this.mDeliveredLocationCount++;
        }

        public synchronized void updateTotals() {
            if (this.mAddedRequestCount > 0) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                this.mAddedTimeTotalMs += elapsedRealtime - this.mAddedTimeLastUpdateRealtimeMs;
                this.mAddedTimeLastUpdateRealtimeMs = elapsedRealtime;
            }
            if (this.mActiveRequestCount > 0) {
                long elapsedRealtime2 = SystemClock.elapsedRealtime();
                this.mActiveTimeTotalMs += elapsedRealtime2 - this.mActiveTimeLastUpdateRealtimeMs;
                this.mActiveTimeLastUpdateRealtimeMs = elapsedRealtime2;
            }
            if (this.mForegroundRequestCount > 0) {
                long elapsedRealtime3 = SystemClock.elapsedRealtime();
                this.mForegroundTimeTotalMs += elapsedRealtime3 - this.mForegroundTimeLastUpdateRealtimeMs;
                this.mForegroundTimeLastUpdateRealtimeMs = elapsedRealtime3;
            }
        }

        public synchronized String toString() {
            return "min/max interval = " + intervalToString(this.mFastestIntervalMs) + SliceClientPermissions.SliceAuthority.DELIMITER + intervalToString(this.mSlowestIntervalMs) + ", total/active/foreground duration = " + TimeUtils.formatDuration(this.mAddedTimeTotalMs) + SliceClientPermissions.SliceAuthority.DELIMITER + TimeUtils.formatDuration(this.mActiveTimeTotalMs) + SliceClientPermissions.SliceAuthority.DELIMITER + TimeUtils.formatDuration(this.mForegroundTimeTotalMs) + ", locations = " + this.mDeliveredLocationCount;
        }

        private static String intervalToString(long j) {
            if (j == JobStatus.NO_LATEST_RUNTIME) {
                return "passive";
            }
            return TimeUnit.MILLISECONDS.toSeconds(j) + "s";
        }
    }

    public ILocationEventLogWrapper getLocationWrapper() {
        return this.mLocationWrapper;
    }

    private ILocationEventLogExt getExtLoader() {
        return (ILocationEventLogExt) OplusLbsFactory.getInstance().getFeature(ILocationEventLogExt.DEFAULT, null);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class LocationEventLogWrapper implements ILocationEventLogWrapper {
        private LocationEventLogWrapper() {
        }

        @Override // com.android.server.location.eventlog.ILocationEventLogWrapper
        public void addExtLog(Object obj) {
            LocationEventLog.this.addLog(SystemClock.elapsedRealtime(), obj);
        }

        @Override // com.android.server.location.eventlog.ILocationEventLogWrapper
        public void updateEventsLocationSize(int i) {
            LocationEventLog.this.mLocationsLog.getLocalWrapper().updateEventsLogSize(i);
        }

        @Override // com.android.server.location.eventlog.ILocationEventLogWrapper
        public void addLogToProviderEvent(String str, CallerIdentity callerIdentity, Object obj, long j, boolean z) {
            LocationEventLog.this.addLog(obj);
            if (z) {
                LocationEventLog.this.getAggregateStats(str, callerIdentity).markRequestAdded(j);
            } else {
                LocationEventLog.this.getAggregateStats(str, callerIdentity).markRequestRemoved();
            }
        }
    }
}
