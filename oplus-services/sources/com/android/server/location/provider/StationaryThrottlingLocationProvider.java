package com.android.server.location.provider;

import android.location.Location;
import android.location.LocationResult;
import android.location.provider.ProviderRequest;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.Preconditions;
import com.android.server.DeviceIdleInternal;
import com.android.server.FgThread;
import com.android.server.job.controllers.JobStatus;
import com.android.server.location.LocationManagerService;
import com.android.server.location.eventlog.LocationEventLog;
import com.android.server.location.injector.DeviceIdleHelper;
import com.android.server.location.injector.DeviceStationaryHelper;
import com.android.server.location.injector.Injector;
import com.android.server.location.provider.AbstractLocationProvider;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class StationaryThrottlingLocationProvider extends DelegateLocationProvider implements DeviceIdleHelper.DeviceIdleListener, DeviceIdleInternal.StationaryListener {
    private static final long MAX_STATIONARY_LOCATION_AGE_MS = 30000;
    private static final long MIN_INTERVAL_MS = 1000;

    @GuardedBy({"mLock"})
    DeliverLastLocationRunnable mDeliverLastLocationCallback;

    @GuardedBy({"mLock"})
    private boolean mDeviceIdle;
    private final DeviceIdleHelper mDeviceIdleHelper;

    @GuardedBy({"mLock"})
    private boolean mDeviceStationary;
    private final DeviceStationaryHelper mDeviceStationaryHelper;

    @GuardedBy({"mLock"})
    private long mDeviceStationaryRealtimeMs;

    @GuardedBy({"mLock"})
    private ProviderRequest mIncomingRequest;

    @GuardedBy({"mLock"})
    Location mLastLocation;
    final Object mLock;
    private final String mName;

    @GuardedBy({"mLock"})
    private ProviderRequest mOutgoingRequest;

    @GuardedBy({"mLock"})
    long mThrottlingIntervalMs;

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider.Listener
    public /* bridge */ /* synthetic */ void onStateChanged(AbstractLocationProvider.State state, AbstractLocationProvider.State state2) {
        super.onStateChanged(state, state2);
    }

    public StationaryThrottlingLocationProvider(String str, Injector injector, AbstractLocationProvider abstractLocationProvider) {
        super(ConcurrentUtils.DIRECT_EXECUTOR, abstractLocationProvider);
        this.mLock = new Object();
        this.mDeviceIdle = false;
        this.mDeviceStationary = false;
        this.mDeviceStationaryRealtimeMs = Long.MIN_VALUE;
        ProviderRequest providerRequest = ProviderRequest.EMPTY_REQUEST;
        this.mIncomingRequest = providerRequest;
        this.mOutgoingRequest = providerRequest;
        this.mThrottlingIntervalMs = JobStatus.NO_LATEST_RUNTIME;
        this.mDeliverLastLocationCallback = null;
        this.mName = str;
        this.mDeviceIdleHelper = injector.getDeviceIdleHelper();
        this.mDeviceStationaryHelper = injector.getDeviceStationaryHelper();
        initializeDelegate();
    }

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider.Listener
    public void onReportLocation(LocationResult locationResult) {
        super.onReportLocation(locationResult);
        synchronized (this.mLock) {
            this.mLastLocation = locationResult.getLastLocation();
            onThrottlingChangedLocked(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider
    public void onStart() {
        this.mDelegate.getController().start();
        synchronized (this.mLock) {
            this.mDeviceIdleHelper.addListener(this);
            this.mDeviceIdle = this.mDeviceIdleHelper.isDeviceIdle();
            this.mDeviceStationaryHelper.addListener(this);
            this.mDeviceStationary = false;
            this.mDeviceStationaryRealtimeMs = Long.MIN_VALUE;
            onThrottlingChangedLocked(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider
    public void onStop() {
        synchronized (this.mLock) {
            this.mDeviceStationaryHelper.removeListener(this);
            this.mDeviceIdleHelper.removeListener(this);
            ProviderRequest providerRequest = ProviderRequest.EMPTY_REQUEST;
            this.mIncomingRequest = providerRequest;
            this.mOutgoingRequest = providerRequest;
            this.mThrottlingIntervalMs = JobStatus.NO_LATEST_RUNTIME;
            if (this.mDeliverLastLocationCallback != null) {
                FgThread.getHandler().removeCallbacks(this.mDeliverLastLocationCallback);
                this.mDeliverLastLocationCallback = null;
            }
            this.mLastLocation = null;
        }
        this.mDelegate.getController().stop();
    }

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider
    protected void onSetRequest(ProviderRequest providerRequest) {
        synchronized (this.mLock) {
            this.mIncomingRequest = providerRequest;
            onThrottlingChangedLocked(true);
        }
    }

    @Override // com.android.server.location.injector.DeviceIdleHelper.DeviceIdleListener
    public void onDeviceIdleChanged(boolean z) {
        synchronized (this.mLock) {
            if (z == this.mDeviceIdle) {
                return;
            }
            this.mDeviceIdle = z;
            onThrottlingChangedLocked(false);
        }
    }

    public void onDeviceStationaryChanged(boolean z) {
        synchronized (this.mLock) {
            if (this.mDeviceStationary == z) {
                return;
            }
            this.mDeviceStationary = z;
            if (z) {
                this.mDeviceStationaryRealtimeMs = SystemClock.elapsedRealtime();
            } else {
                this.mDeviceStationaryRealtimeMs = Long.MIN_VALUE;
            }
            onThrottlingChangedLocked(false);
        }
    }

    @GuardedBy({"mLock"})
    private void onThrottlingChangedLocked(boolean z) {
        ProviderRequest providerRequest;
        Location location;
        long max = (!this.mDeviceStationary || !this.mDeviceIdle || this.mIncomingRequest.isLocationSettingsIgnored() || (location = this.mLastLocation) == null || location.getElapsedRealtimeAgeMillis(this.mDeviceStationaryRealtimeMs) > MAX_STATIONARY_LOCATION_AGE_MS) ? Long.MAX_VALUE : Math.max(this.mIncomingRequest.getIntervalMillis(), 1000L);
        if (max != JobStatus.NO_LATEST_RUNTIME) {
            providerRequest = ProviderRequest.EMPTY_REQUEST;
        } else {
            providerRequest = this.mIncomingRequest;
        }
        if (!providerRequest.equals(this.mOutgoingRequest)) {
            this.mOutgoingRequest = providerRequest;
            this.mDelegate.getController().setRequest(this.mOutgoingRequest);
        }
        long j = this.mThrottlingIntervalMs;
        if (max == j) {
            return;
        }
        this.mThrottlingIntervalMs = max;
        if (max != JobStatus.NO_LATEST_RUNTIME) {
            if (j == JobStatus.NO_LATEST_RUNTIME) {
                Log.d(LocationManagerService.TAG, this.mName + " provider stationary throttled");
                LocationEventLog.EVENT_LOG.logProviderStationaryThrottled(this.mName, true, this.mOutgoingRequest);
            }
            if (this.mDeliverLastLocationCallback != null) {
                FgThread.getHandler().removeCallbacks(this.mDeliverLastLocationCallback);
            }
            this.mDeliverLastLocationCallback = new DeliverLastLocationRunnable();
            Preconditions.checkState(this.mLastLocation != null);
            if (z) {
                FgThread.getHandler().post(this.mDeliverLastLocationCallback);
                return;
            } else {
                FgThread.getHandler().postDelayed(this.mDeliverLastLocationCallback, this.mThrottlingIntervalMs - this.mLastLocation.getElapsedRealtimeAgeMillis());
                return;
            }
        }
        if (j != JobStatus.NO_LATEST_RUNTIME) {
            LocationEventLog.EVENT_LOG.logProviderStationaryThrottled(this.mName, false, this.mOutgoingRequest);
            Log.d(LocationManagerService.TAG, this.mName + " provider stationary unthrottled");
        }
        FgThread.getHandler().removeCallbacks(this.mDeliverLastLocationCallback);
        this.mDeliverLastLocationCallback = null;
    }

    @Override // com.android.server.location.provider.DelegateLocationProvider, com.android.server.location.provider.AbstractLocationProvider
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (this.mThrottlingIntervalMs != JobStatus.NO_LATEST_RUNTIME) {
            printWriter.println("stationary throttled=" + this.mLastLocation);
        } else {
            printWriter.print("stationary throttled=false");
            if (!this.mDeviceIdle) {
                printWriter.print(" (not idle)");
            }
            if (!this.mDeviceStationary) {
                printWriter.print(" (not stationary)");
            }
            printWriter.println();
        }
        this.mDelegate.dump(fileDescriptor, printWriter, strArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class DeliverLastLocationRunnable implements Runnable {
        DeliverLastLocationRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (StationaryThrottlingLocationProvider.this.mLock) {
                StationaryThrottlingLocationProvider stationaryThrottlingLocationProvider = StationaryThrottlingLocationProvider.this;
                if (stationaryThrottlingLocationProvider.mDeliverLastLocationCallback != this) {
                    return;
                }
                if (stationaryThrottlingLocationProvider.mLastLocation == null) {
                    return;
                }
                Location location = new Location(StationaryThrottlingLocationProvider.this.mLastLocation);
                location.setTime(System.currentTimeMillis());
                location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                if (location.hasSpeed()) {
                    location.removeSpeed();
                    if (location.hasSpeedAccuracy()) {
                        location.removeSpeedAccuracy();
                    }
                }
                if (location.hasBearing()) {
                    location.removeBearing();
                    if (location.hasBearingAccuracy()) {
                        location.removeBearingAccuracy();
                    }
                }
                StationaryThrottlingLocationProvider.this.mLastLocation = location;
                FgThread.getHandler().postDelayed(this, StationaryThrottlingLocationProvider.this.mThrottlingIntervalMs);
                StationaryThrottlingLocationProvider.this.reportLocation(LocationResult.wrap(new Location[]{location}));
            }
        }
    }
}
