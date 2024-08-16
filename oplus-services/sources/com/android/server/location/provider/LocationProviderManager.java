package com.android.server.location.provider;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.BroadcastOptions;
import android.app.PendingIntent;
import android.app.compat.CompatChanges;
import android.content.Context;
import android.content.Intent;
import android.location.ILocationCallback;
import android.location.ILocationListener;
import android.location.LastLocationRequest;
import android.location.Location;
import android.location.LocationManagerInternal;
import android.location.LocationRequest;
import android.location.LocationResult;
import android.location.provider.IProviderRequestListener;
import android.location.provider.ProviderProperties;
import android.location.provider.ProviderRequest;
import android.location.util.identity.CallerIdentity;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IRemoteCallback;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.EventLog;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.listeners.ListenerExecutor;
import com.android.internal.util.ConcurrentUtils;
import com.android.internal.util.Preconditions;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.job.controllers.JobStatus;
import com.android.server.location.LocationManagerService;
import com.android.server.location.LocationPermissions;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.eventlog.LocationEventLog;
import com.android.server.location.fudger.LocationFudger;
import com.android.server.location.injector.AlarmHelper;
import com.android.server.location.injector.AppForegroundHelper;
import com.android.server.location.injector.AppOpsHelper;
import com.android.server.location.injector.Injector;
import com.android.server.location.injector.LocationPermissionsHelper;
import com.android.server.location.injector.LocationPowerSaveModeHelper;
import com.android.server.location.injector.LocationUsageLogger;
import com.android.server.location.injector.PackageResetHelper;
import com.android.server.location.injector.ScreenInteractiveHelper;
import com.android.server.location.injector.SettingsHelper;
import com.android.server.location.injector.UserInfoHelper;
import com.android.server.location.interfaces.ILocationFreezeProc;
import com.android.server.location.interfaces.IOplusLBSMainClass;
import com.android.server.location.listeners.ListenerMultiplexer;
import com.android.server.location.listeners.RemovableListenerRegistration;
import com.android.server.location.provider.AbstractLocationProvider;
import com.android.server.location.provider.LocationProviderManager;
import com.android.server.location.settings.LocationSettings;
import com.android.server.location.settings.LocationUserSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocationProviderManager extends ListenerMultiplexer<Object, LocationTransport, Registration, ProviderRequest> implements AbstractLocationProvider.Listener {
    private static final float FASTEST_INTERVAL_JITTER_PERCENTAGE = 0.1f;
    private static final long MAX_CURRENT_LOCATION_AGE_MS = 30000;
    private static final int MAX_FASTEST_INTERVAL_JITTER_MS = 30000;
    private static final long MAX_GET_CURRENT_LOCATION_TIMEOUT_MS = 30000;
    private static final long MAX_HIGH_POWER_INTERVAL_MS = 300000;
    private static final long MIN_COARSE_INTERVAL_MS = 600000;
    private static final long MIN_REQUEST_DELAY_MS = 30000;
    private static final int STATE_STARTED = 0;
    private static final int STATE_STOPPED = 2;
    private static final int STATE_STOPPING = 1;
    private static final long TEMPORARY_APP_ALLOWLIST_DURATION_MS = 10000;
    private static final String WAKELOCK_TAG = "*location*";
    private static final long WAKELOCK_TIMEOUT_MS = 30000;
    private static ILocationFreezeProc mLocationFreeze;
    private static IOplusLBSMainClass mOplusLbsClass;
    private final SettingsHelper.GlobalSettingChangedListener mAdasPackageAllowlistChangedListener;
    protected final AlarmHelper mAlarmHelper;
    private final AppForegroundHelper.AppForegroundListener mAppForegroundChangedListener;
    protected final AppForegroundHelper mAppForegroundHelper;
    protected final AppOpsHelper mAppOpsHelper;
    private final SettingsHelper.GlobalSettingChangedListener mBackgroundThrottleIntervalChangedListener;
    private final SettingsHelper.GlobalSettingChangedListener mBackgroundThrottlePackageWhitelistChangedListener;
    protected final Context mContext;

    @GuardedBy({"mMultiplexerLock"})
    private AlarmManager.OnAlarmListener mDelayedRegister;

    @GuardedBy({"mMultiplexerLock"})
    private final SparseBooleanArray mEnabled;

    @GuardedBy({"mMultiplexerLock"})
    private final ArrayList<LocationManagerInternal.ProviderEnabledListener> mEnabledListeners;
    private final SettingsHelper.GlobalSettingChangedListener mIgnoreSettingsPackageWhitelistChangedListener;

    @GuardedBy({"mMultiplexerLock"})
    private final SparseArray<LastLocation> mLastLocations;
    private final SettingsHelper.UserSettingChangedListener mLocationEnabledChangedListener;
    protected final LocationFudger mLocationFudger;
    protected final LocationManagerInternal mLocationManagerInternal;
    private final SettingsHelper.UserSettingChangedListener mLocationPackageBlacklistChangedListener;
    protected final LocationPermissionsHelper mLocationPermissionsHelper;
    private final LocationPermissionsHelper.LocationPermissionsListener mLocationPermissionsListener;
    private final LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener mLocationPowerSaveModeChangedListener;
    protected final LocationPowerSaveModeHelper mLocationPowerSaveModeHelper;
    private ILocationProviderManagerWrapper mLocationProviderManagerWrapper;
    protected final LocationSettings mLocationSettings;
    protected final LocationUsageLogger mLocationUsageLogger;
    private final LocationSettings.LocationUserSettingsListener mLocationUserSettingsListener;
    protected final String mName;
    private final PackageResetHelper mPackageResetHelper;
    private final PackageResetHelper.Responder mPackageResetResponder;
    private final PassiveLocationProviderManager mPassiveManager;
    protected final MockableLocationProvider mProvider;
    private final CopyOnWriteArrayList<IProviderRequestListener> mProviderRequestListeners;
    private final Collection<String> mRequiredPermissions;
    private final ScreenInteractiveHelper.ScreenInteractiveChangedListener mScreenInteractiveChangedListener;
    protected final ScreenInteractiveHelper mScreenInteractiveHelper;
    protected final SettingsHelper mSettingsHelper;

    @GuardedBy({"mMultiplexerLock"})
    private int mState;

    @GuardedBy({"mMultiplexerLock"})
    private StateChangedListener mStateChangedListener;
    private final UserInfoHelper.UserListener mUserChangedListener;
    protected final UserInfoHelper mUserHelper;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface LocationTransport {
        void deliverOnFlushComplete(int i) throws Exception;

        void deliverOnLocationChanged(LocationResult locationResult, IRemoteCallback iRemoteCallback) throws Exception;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ProviderTransport {
        void deliverOnProviderEnabledChanged(String str, boolean z) throws Exception;
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface State {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface StateChangedListener {
        void onStateChanged(String str, AbstractLocationProvider.State state, AbstractLocationProvider.State state2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onLocationPowerSaveModeChanged$9(Registration registration) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onScreenInteractiveChanged$8(Registration registration) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$stopManager$0(Object obj) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class LocationListenerTransport implements LocationTransport, ProviderTransport {
        private final ILocationListener mListener;

        LocationListenerTransport(ILocationListener iLocationListener) {
            Objects.requireNonNull(iLocationListener);
            this.mListener = iLocationListener;
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnLocationChanged(LocationResult locationResult, IRemoteCallback iRemoteCallback) throws RemoteException {
            try {
                this.mListener.onLocationChanged(locationResult.asList(), iRemoteCallback);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnFlushComplete(int i) throws RemoteException {
            try {
                this.mListener.onFlushComplete(i);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.ProviderTransport
        public void deliverOnProviderEnabledChanged(String str, boolean z) throws RemoteException {
            try {
                this.mListener.onProviderEnabledChanged(str, z);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class LocationPendingIntentTransport implements LocationTransport, ProviderTransport {
        private final Context mContext;
        private final PendingIntent mPendingIntent;

        public LocationPendingIntentTransport(Context context, PendingIntent pendingIntent) {
            this.mContext = context;
            this.mPendingIntent = pendingIntent;
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnLocationChanged(LocationResult locationResult, final IRemoteCallback iRemoteCallback) throws PendingIntent.CanceledException {
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setDontSendToRestrictedApps(true);
            makeBasic.setTemporaryAppAllowlist(10000L, 0, 312, "");
            Intent putExtra = new Intent().putExtra("location", locationResult.getLastLocation());
            if (locationResult.size() > 1) {
                putExtra.putExtra("locations", (Parcelable[]) locationResult.asList().toArray(new Location[0]));
            }
            PendingIntentSender.send(this.mPendingIntent, this.mContext, putExtra, iRemoteCallback != null ? new Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$LocationPendingIntentTransport$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LocationProviderManager.LocationPendingIntentTransport.lambda$deliverOnLocationChanged$0(iRemoteCallback);
                }
            } : null, makeBasic.toBundle());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$deliverOnLocationChanged$0(IRemoteCallback iRemoteCallback) {
            try {
                iRemoteCallback.sendResult((Bundle) null);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnFlushComplete(int i) throws PendingIntent.CanceledException {
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setDontSendToRestrictedApps(true);
            makeBasic.setPendingIntentBackgroundActivityLaunchAllowed(false);
            this.mPendingIntent.send(this.mContext, 0, new Intent().putExtra("flushComplete", i), null, null, null, makeBasic.toBundle());
        }

        @Override // com.android.server.location.provider.LocationProviderManager.ProviderTransport
        public void deliverOnProviderEnabledChanged(String str, boolean z) throws PendingIntent.CanceledException {
            BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
            makeBasic.setDontSendToRestrictedApps(true);
            this.mPendingIntent.send(this.mContext, 0, new Intent().putExtra("providerEnabled", z), null, null, null, makeBasic.toBundle());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class GetCurrentLocationTransport implements LocationTransport {
        private final ILocationCallback mCallback;

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnFlushComplete(int i) {
        }

        GetCurrentLocationTransport(ILocationCallback iLocationCallback) {
            Objects.requireNonNull(iLocationCallback);
            this.mCallback = iLocationCallback;
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationTransport
        public void deliverOnLocationChanged(LocationResult locationResult, IRemoteCallback iRemoteCallback) throws RemoteException {
            Preconditions.checkState(iRemoteCallback == null);
            try {
                if (locationResult != null) {
                    this.mCallback.onLocation(locationResult.getLastLocation());
                } else {
                    this.mCallback.onLocation((Location) null);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public abstract class Registration extends RemovableListenerRegistration<Object, LocationTransport> {
        private final LocationRequest mBaseRequest;

        @GuardedBy({"mMultiplexerLock"})
        private boolean mForeground;
        private final CallerIdentity mIdentity;

        @GuardedBy({"mMultiplexerLock"})
        private boolean mIsUsingHighPower;
        private Location mLastLocation;
        private final int mPermissionLevel;

        @GuardedBy({"mMultiplexerLock"})
        private boolean mPermitted;

        @GuardedBy({"mMultiplexerLock"})
        private LocationRequest mProviderLocationRequest;

        abstract ListenerExecutor.ListenerOperation<LocationTransport> acceptLocationChange(LocationResult locationResult);

        protected Registration(LocationRequest locationRequest, CallerIdentity callerIdentity, Executor executor, LocationTransport locationTransport, int i) {
            super(executor, locationTransport);
            this.mLastLocation = null;
            Preconditions.checkArgument(callerIdentity.getListenerId() != null);
            Preconditions.checkArgument(i > 0);
            Preconditions.checkArgument(!locationRequest.getWorkSource().isEmpty());
            this.mBaseRequest = locationRequest;
            this.mIdentity = callerIdentity;
            this.mPermissionLevel = i;
            this.mProviderLocationRequest = locationRequest;
        }

        public final CallerIdentity getIdentity() {
            return this.mIdentity;
        }

        public final LocationRequest getRequest() {
            LocationRequest locationRequest;
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                locationRequest = this.mProviderLocationRequest;
            }
            return locationRequest;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.RemovableListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        public void onRegister() {
            super.onRegister();
            Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider added registration from " + getIdentity() + " -> " + getRequest() + " permissionLevel: " + this.mPermissionLevel + " permitted: " + LocationProviderManager.this.mLocationPermissionsHelper.hasLocationPermissions(this.mPermissionLevel, getIdentity()));
            LocationEventLog locationEventLog = LocationEventLog.EVENT_LOG;
            locationEventLog.logProviderClientRegistered(LocationProviderManager.this.mName, getIdentity(), this.mBaseRequest);
            this.mPermitted = LocationProviderManager.this.mLocationPermissionsHelper.hasLocationPermissions(this.mPermissionLevel, getIdentity());
            this.mForeground = LocationProviderManager.this.mAppForegroundHelper.isAppForeground(getIdentity().getUid());
            if (LocationProviderManager.mOplusLbsClass != null) {
                LocationProviderManager.mOplusLbsClass.startRequesting(getIdentity(), LocationProviderManager.this.mName, getRequest(), this.mForeground, "" + getKey().hashCode());
            }
            this.mProviderLocationRequest = calculateProviderLocationRequest();
            this.mIsUsingHighPower = isUsingHighPower();
            if (this.mForeground) {
                locationEventLog.logProviderClientForeground(LocationProviderManager.this.mName, getIdentity());
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        public void onUnregister() {
            LocationEventLog.EVENT_LOG.logProviderClientUnregistered(LocationProviderManager.this.mName, getIdentity());
            if (LocationProviderManager.mOplusLbsClass != null) {
                LocationProviderManager.mOplusLbsClass.stopRequesting(getIdentity(), LocationProviderManager.this.mName, getRequest(), "" + getKey().hashCode());
            }
            Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider removed registration from " + getIdentity());
            super.onUnregister();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        public void onActive() {
            LocationEventLog.EVENT_LOG.logProviderClientActive(LocationProviderManager.this.mName, getIdentity());
            if (!getRequest().isHiddenFromAppOps()) {
                LocationProviderManager.this.mAppOpsHelper.startOpNoThrow(41, getIdentity());
            }
            onHighPowerUsageChanged();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        public void onInactive() {
            onHighPowerUsageChanged();
            if (!getRequest().isHiddenFromAppOps()) {
                LocationProviderManager.this.mAppOpsHelper.finishOp(41, getIdentity());
            }
            LocationEventLog.EVENT_LOG.logProviderClientInactive(LocationProviderManager.this.mName, getIdentity());
        }

        @GuardedBy({"mMultiplexerLock"})
        final void setLastDeliveredLocation(Location location) {
            this.mLastLocation = location;
        }

        public final Location getLastDeliveredLocation() {
            Location location;
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                location = this.mLastLocation;
            }
            return location;
        }

        public int getPermissionLevel() {
            int i;
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                i = this.mPermissionLevel;
            }
            return i;
        }

        public final boolean isForeground() {
            boolean z;
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                z = this.mForeground;
            }
            return z;
        }

        public final boolean isPermitted() {
            boolean z;
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                z = this.mPermitted;
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$flush$1(final int i) {
            executeOperation(new ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.provider.LocationProviderManager$Registration$$ExternalSyntheticLambda0
                public final void operate(Object obj) {
                    ((LocationProviderManager.LocationTransport) obj).deliverOnFlushComplete(i);
                }
            });
        }

        public final void flush(final int i) {
            LocationProviderManager.this.mProvider.getController().flush(new Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$Registration$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LocationProviderManager.Registration.this.lambda$flush$1(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.RemovableListenerRegistration
        public final ListenerMultiplexer<Object, ? super LocationTransport, ?, ?> getOwner() {
            return LocationProviderManager.this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean onProviderPropertiesChanged() {
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                onHighPowerUsageChanged();
            }
            return false;
        }

        @GuardedBy({"mMultiplexerLock"})
        private void onHighPowerUsageChanged() {
            boolean isUsingHighPower = isUsingHighPower();
            if (isUsingHighPower != this.mIsUsingHighPower) {
                this.mIsUsingHighPower = isUsingHighPower;
                if (getRequest().isHiddenFromAppOps()) {
                    return;
                }
                if (this.mIsUsingHighPower) {
                    LocationProviderManager.this.mAppOpsHelper.startOpNoThrow(42, getIdentity());
                } else {
                    LocationProviderManager.this.mAppOpsHelper.finishOp(42, getIdentity());
                }
            }
        }

        private boolean isUsingHighPower() {
            ProviderProperties properties = LocationProviderManager.this.getProperties();
            return properties != null && isActive() && getRequest().getIntervalMillis() < LocationProviderManager.MAX_HIGH_POWER_INTERVAL_MS && properties.getPowerUsage() == 3;
        }

        final boolean onLocationPermissionsChanged(String str) {
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                if (str != null) {
                    if (!getIdentity().getPackageName().equals(str)) {
                        return false;
                    }
                }
                return onLocationPermissionsChanged();
            }
        }

        final boolean onLocationPermissionsChanged(int i) {
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                if (getIdentity().getUid() != i) {
                    return false;
                }
                return onLocationPermissionsChanged();
            }
        }

        @GuardedBy({"mMultiplexerLock"})
        private boolean onLocationPermissionsChanged() {
            boolean hasLocationPermissions = LocationProviderManager.this.mLocationPermissionsHelper.hasLocationPermissions(this.mPermissionLevel, getIdentity());
            if (hasLocationPermissions == this.mPermitted) {
                return false;
            }
            if (LocationManagerService.D) {
                Log.v(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider package " + getIdentity().getPackageName() + " permitted = " + hasLocationPermissions);
            }
            this.mPermitted = hasLocationPermissions;
            if (hasLocationPermissions) {
                LocationEventLog.EVENT_LOG.logProviderClientPermitted(LocationProviderManager.this.mName, getIdentity());
                return true;
            }
            LocationEventLog.EVENT_LOG.logProviderClientUnpermitted(LocationProviderManager.this.mName, getIdentity());
            return true;
        }

        final boolean onAdasGnssLocationEnabledChanged(int i) {
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                if (getIdentity().getUserId() != i) {
                    return false;
                }
                return onProviderLocationRequestChanged();
            }
        }

        final boolean onForegroundChanged(int i, boolean z) {
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                if (getIdentity().getUid() != i || z == this.mForeground) {
                    return false;
                }
                Log.v(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider uid " + i + " foreground = " + z);
                this.mForeground = z;
                if (LocationProviderManager.mOplusLbsClass != null) {
                    LocationProviderManager.mOplusLbsClass.updateForeground(getIdentity().getPackageName(), LocationProviderManager.this.mName, this.mForeground);
                }
                if (this.mForeground) {
                    LocationEventLog.EVENT_LOG.logProviderClientForeground(LocationProviderManager.this.mName, getIdentity());
                } else {
                    LocationEventLog.EVENT_LOG.logProviderClientBackground(LocationProviderManager.this.mName, getIdentity());
                }
                return onProviderLocationRequestChanged() || LocationProviderManager.this.mLocationPowerSaveModeHelper.getLocationPowerSaveMode() == 3;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final boolean onProviderLocationRequestChanged() {
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                LocationRequest calculateProviderLocationRequest = calculateProviderLocationRequest();
                if (this.mProviderLocationRequest.equals(calculateProviderLocationRequest)) {
                    return false;
                }
                LocationRequest locationRequest = this.mProviderLocationRequest;
                this.mProviderLocationRequest = calculateProviderLocationRequest;
                onHighPowerUsageChanged();
                LocationProviderManager.this.updateService();
                return locationRequest.isBypass() != calculateProviderLocationRequest.isBypass();
            }
        }

        @GuardedBy({"mMultiplexerLock"})
        private LocationRequest calculateProviderLocationRequest() {
            LocationRequest.Builder builder = new LocationRequest.Builder(this.mBaseRequest);
            if (this.mPermissionLevel < 2) {
                builder.setQuality(HdmiCecKeycode.CEC_KEYCODE_SELECT_MEDIA_FUNCTION);
                if (this.mBaseRequest.getIntervalMillis() < 600000) {
                    builder.setIntervalMillis(600000L);
                }
                if (this.mBaseRequest.getMinUpdateIntervalMillis() < 600000) {
                    builder.setMinUpdateIntervalMillis(600000L);
                }
            }
            boolean isLocationSettingsIgnored = this.mBaseRequest.isLocationSettingsIgnored();
            boolean z = false;
            if (isLocationSettingsIgnored) {
                if (!LocationProviderManager.this.mSettingsHelper.getIgnoreSettingsAllowlist().contains(getIdentity().getPackageName(), getIdentity().getAttributionTag()) && !LocationProviderManager.this.mLocationManagerInternal.isProvider((String) null, getIdentity())) {
                    isLocationSettingsIgnored = false;
                }
                builder.setLocationSettingsIgnored(isLocationSettingsIgnored);
            }
            boolean isAdasGnssBypass = this.mBaseRequest.isAdasGnssBypass();
            if (isAdasGnssBypass) {
                if (!"gps".equals(LocationProviderManager.this.mName)) {
                    Log.e(LocationManagerService.TAG, "adas gnss bypass request received in non-gps provider");
                } else if (LocationProviderManager.this.mUserHelper.isCurrentUserId(getIdentity().getUserId()) && LocationProviderManager.this.mLocationSettings.getUserSettings(getIdentity().getUserId()).isAdasGnssLocationEnabled() && LocationProviderManager.this.mSettingsHelper.getAdasAllowlist().contains(getIdentity().getPackageName(), getIdentity().getAttributionTag())) {
                    z = isAdasGnssBypass;
                }
                builder.setAdasGnssBypass(z);
            }
            if (!isLocationSettingsIgnored && !isThrottlingExempt()) {
                if (!this.mForeground) {
                    builder.setIntervalMillis(Math.max(this.mBaseRequest.getIntervalMillis(), LocationProviderManager.this.mSettingsHelper.getBackgroundThrottleIntervalMs()));
                }
                if ("gps".equals(LocationProviderManager.this.mName) && LocationProviderManager.mOplusLbsClass.checkRequestBlocked(LocationProviderManager.this.mName, getIdentity().getPackageName())) {
                    builder.setIntervalMillis(JobStatus.NO_LATEST_RUNTIME).setMinUpdateIntervalMillis(JobStatus.NO_LATEST_RUNTIME);
                }
            }
            if ("fused".equals(LocationProviderManager.this.mName) && LocationProviderManager.mOplusLbsClass.isFlpReqLimited(getIdentity().getPackageName())) {
                builder.setQuality(HdmiCecKeycode.CEC_KEYCODE_SELECT_MEDIA_FUNCTION);
            }
            return builder.build();
        }

        private boolean isThrottlingExempt() {
            if (LocationProviderManager.this.mSettingsHelper.getBackgroundThrottlePackageWhitelist().contains(getIdentity().getPackageName())) {
                return true;
            }
            return LocationProviderManager.this.mLocationManagerInternal.isProvider((String) null, getIdentity());
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(getIdentity());
            ArraySet arraySet = new ArraySet(2);
            if (!isForeground()) {
                arraySet.add("bg");
            }
            if (!isPermitted()) {
                arraySet.add("na");
            }
            if (!arraySet.isEmpty()) {
                sb.append(" ");
                sb.append(arraySet);
            }
            if (this.mPermissionLevel == 1) {
                sb.append(" (COARSE)");
            }
            sb.append(" ");
            sb.append(getRequest());
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public abstract class LocationRegistration extends Registration implements AlarmManager.OnAlarmListener, LocationManagerInternal.ProviderEnabledListener {

        @GuardedBy({"mMultiplexerLock"})
        private long mExpirationRealtimeMs;

        @GuardedBy({"mMultiplexerLock"})
        private int mNumLocationsDelivered;
        private volatile ProviderTransport mProviderTransport;
        final PowerManager.WakeLock mWakeLock;
        final ExternalWakeLockReleaser mWakeLockReleaser;

        /* JADX INFO: Access modifiers changed from: protected */
        public abstract void onProviderOperationFailure(ListenerExecutor.ListenerOperation<ProviderTransport> listenerOperation, Exception exc);

        protected <TTransport extends LocationTransport & ProviderTransport> LocationRegistration(LocationRequest locationRequest, CallerIdentity callerIdentity, Executor executor, TTransport ttransport, int i) {
            super(locationRequest, callerIdentity, executor, ttransport, i);
            this.mNumLocationsDelivered = 0;
            this.mExpirationRealtimeMs = JobStatus.NO_LATEST_RUNTIME;
            this.mProviderTransport = ttransport;
            PowerManager powerManager = (PowerManager) LocationProviderManager.this.mContext.getSystemService(PowerManager.class);
            Objects.requireNonNull(powerManager);
            PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, LocationProviderManager.WAKELOCK_TAG);
            this.mWakeLock = newWakeLock;
            newWakeLock.setReferenceCounted(true);
            newWakeLock.setWorkSource(locationRequest.getWorkSource());
            this.mWakeLockReleaser = new ExternalWakeLockReleaser(callerIdentity, newWakeLock);
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        protected void onListenerUnregister() {
            this.mProviderTransport = null;
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onRegister() {
            super.onRegister();
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long expirationRealtimeMs = getRequest().getExpirationRealtimeMs(elapsedRealtime);
            this.mExpirationRealtimeMs = expirationRealtimeMs;
            if (expirationRealtimeMs <= elapsedRealtime) {
                onAlarm();
            } else if (expirationRealtimeMs < JobStatus.NO_LATEST_RUNTIME) {
                LocationProviderManager.this.mAlarmHelper.setDelayedAlarm(expirationRealtimeMs - elapsedRealtime, this, null);
            }
            LocationProviderManager.this.addEnabledListener(this);
            int userId = getIdentity().getUserId();
            if (LocationProviderManager.this.isEnabled(userId)) {
                return;
            }
            onProviderEnabledChanged(LocationProviderManager.this.mName, userId, false);
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onUnregister() {
            LocationProviderManager.this.removeEnabledListener(this);
            if (this.mExpirationRealtimeMs < JobStatus.NO_LATEST_RUNTIME) {
                LocationProviderManager.this.mAlarmHelper.cancel(this);
            }
            super.onUnregister();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onActive() {
            Location lastLocationUnsafe;
            super.onActive();
            if (CompatChanges.isChangeEnabled(73144566L, getIdentity().getUid())) {
                long intervalMillis = getRequest().getIntervalMillis();
                Location lastDeliveredLocation = getLastDeliveredLocation();
                if (lastDeliveredLocation != null) {
                    intervalMillis = Math.min(intervalMillis, lastDeliveredLocation.getElapsedRealtimeAgeMillis() - 1);
                }
                long j = intervalMillis;
                if (j <= 30000 || (lastLocationUnsafe = LocationProviderManager.this.getLastLocationUnsafe(getIdentity().getUserId(), getPermissionLevel(), getRequest().isBypass(), j)) == null) {
                    return;
                }
                executeOperation(acceptLocationChange(LocationResult.wrap(new Location[]{lastLocationUnsafe})));
            }
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            if (LocationManagerService.D) {
                Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " expired at " + TimeUtils.formatRealtime(this.mExpirationRealtimeMs));
            }
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                this.mExpirationRealtimeMs = JobStatus.NO_LATEST_RUNTIME;
                remove();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration
        @GuardedBy({"mMultiplexerLock"})
        ListenerExecutor.ListenerOperation<LocationTransport> acceptLocationChange(LocationResult locationResult) {
            if (LocationProviderManager.mOplusLbsClass != null && !LocationProviderManager.mOplusLbsClass.shouldReportFlpAsGps(locationResult.getLastLocation(), getIdentity().getPackageName())) {
                return null;
            }
            if (SystemClock.elapsedRealtime() >= this.mExpirationRealtimeMs) {
                if (LocationManagerService.D) {
                    Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " expired at " + TimeUtils.formatRealtime(this.mExpirationRealtimeMs));
                }
                remove();
                return null;
            }
            LocationResult permittedLocationResult = LocationProviderManager.this.getPermittedLocationResult(locationResult, getPermissionLevel());
            Objects.requireNonNull(permittedLocationResult);
            final LocationResult filter = permittedLocationResult.filter(new Predicate<Location>() { // from class: com.android.server.location.provider.LocationProviderManager.LocationRegistration.1
                private Location mPreviousLocation;

                {
                    this.mPreviousLocation = LocationRegistration.this.getLastDeliveredLocation();
                }

                @Override // java.util.function.Predicate
                public boolean test(Location location) {
                    if (this.mPreviousLocation != null) {
                        if (location.getElapsedRealtimeMillis() - this.mPreviousLocation.getElapsedRealtimeMillis() < LocationRegistration.this.getRequest().getMinUpdateIntervalMillis() - Math.min(((float) LocationRegistration.this.getRequest().getIntervalMillis()) * LocationProviderManager.FASTEST_INTERVAL_JITTER_PERCENTAGE, 30000L)) {
                            if (LocationManagerService.D) {
                                Log.v(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + LocationRegistration.this.getIdentity() + " dropped delivery - too fast");
                            }
                            return false;
                        }
                        double minUpdateDistanceMeters = LocationRegistration.this.getRequest().getMinUpdateDistanceMeters();
                        if (minUpdateDistanceMeters > 0.0d && location.distanceTo(this.mPreviousLocation) <= minUpdateDistanceMeters) {
                            if (LocationManagerService.D) {
                                Log.v(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + LocationRegistration.this.getIdentity() + " dropped delivery - too close");
                            }
                            return false;
                        }
                    }
                    this.mPreviousLocation = location;
                    return true;
                }
            });
            if (filter == null) {
                return null;
            }
            if (!LocationProviderManager.this.mAppOpsHelper.noteOpNoThrow(LocationPermissions.asAppOp(getPermissionLevel()), getIdentity())) {
                if (LocationManagerService.D) {
                    Log.w(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " noteOp denied");
                }
                return null;
            }
            final boolean z = getRequest().getIntervalMillis() != JobStatus.NO_LATEST_RUNTIME;
            return new ListenerExecutor.ListenerOperation<LocationTransport>() { // from class: com.android.server.location.provider.LocationProviderManager.LocationRegistration.2
                public void onPreExecute() {
                    LocationRegistration.this.setLastDeliveredLocation(filter.getLastLocation());
                    if (z) {
                        LocationRegistration.this.mWakeLock.acquire(30000L);
                    }
                }

                public void operate(LocationTransport locationTransport) throws Exception {
                    LocationResult locationResult2;
                    if (LocationRegistration.this.getIdentity().getPid() == Process.myPid()) {
                        locationResult2 = filter.deepCopy();
                    } else {
                        locationResult2 = filter;
                    }
                    locationTransport.deliverOnLocationChanged(locationResult2, z ? LocationRegistration.this.mWakeLockReleaser : null);
                    LocationEventLog.EVENT_LOG.logProviderDeliveredLocations(LocationProviderManager.this.mName, filter.size(), LocationRegistration.this.getIdentity());
                }

                public void onPostExecute(boolean z2) {
                    if (!z2 && z) {
                        LocationRegistration.this.mWakeLock.release();
                    }
                    if (z2) {
                        if (LocationProviderManager.mOplusLbsClass != null) {
                            IOplusLBSMainClass iOplusLBSMainClass = LocationProviderManager.mOplusLbsClass;
                            LocationRegistration locationRegistration = LocationRegistration.this;
                            iOplusLBSMainClass.checkLocationHasChanged(LocationProviderManager.this.mName, locationRegistration.getIdentity().getPackageName(), 0);
                        }
                        LocationRegistration locationRegistration2 = LocationRegistration.this;
                        int i = locationRegistration2.mNumLocationsDelivered + 1;
                        locationRegistration2.mNumLocationsDelivered = i;
                        if (i >= LocationRegistration.this.getRequest().getMaxUpdates()) {
                            if (LocationManagerService.D) {
                                Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + LocationRegistration.this.getIdentity() + " finished after " + LocationRegistration.this.mNumLocationsDelivered + " updates");
                            }
                            LocationRegistration.this.remove();
                        }
                    }
                }
            };
        }

        public void onProviderEnabledChanged(String str, int i, final boolean z) {
            Preconditions.checkState(LocationProviderManager.this.mName.equals(str));
            Log.d(LocationManagerService.TAG, "onProviderEnabledChanged name: " + LocationProviderManager.this.mName + " enabled: " + z);
            if (i != getIdentity().getUserId()) {
                return;
            }
            executeSafely(getExecutor(), new Supplier() { // from class: com.android.server.location.provider.LocationProviderManager$LocationRegistration$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    LocationProviderManager.ProviderTransport lambda$onProviderEnabledChanged$0;
                    lambda$onProviderEnabledChanged$0 = LocationProviderManager.LocationRegistration.this.lambda$onProviderEnabledChanged$0();
                    return lambda$onProviderEnabledChanged$0;
                }
            }, new ListenerExecutor.ListenerOperation() { // from class: com.android.server.location.provider.LocationProviderManager$LocationRegistration$$ExternalSyntheticLambda1
                public final void operate(Object obj) {
                    LocationProviderManager.LocationRegistration.this.lambda$onProviderEnabledChanged$1(z, (LocationProviderManager.ProviderTransport) obj);
                }
            }, new ListenerExecutor.FailureCallback() { // from class: com.android.server.location.provider.LocationProviderManager$LocationRegistration$$ExternalSyntheticLambda2
                public final void onFailure(ListenerExecutor.ListenerOperation listenerOperation, Exception exc) {
                    LocationProviderManager.LocationRegistration.this.onProviderOperationFailure(listenerOperation, exc);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ ProviderTransport lambda$onProviderEnabledChanged$0() {
            return this.mProviderTransport;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProviderEnabledChanged$1(boolean z, ProviderTransport providerTransport) throws Exception {
            providerTransport.deliverOnProviderEnabledChanged(LocationProviderManager.this.mName, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LocationListenerRegistration extends LocationRegistration implements IBinder.DeathRecipient {
        LocationListenerRegistration(LocationRequest locationRequest, CallerIdentity callerIdentity, LocationListenerTransport locationListenerTransport, int i) {
            super(locationRequest, callerIdentity, callerIdentity.isMyProcess() ? FgThread.getExecutor() : ConcurrentUtils.DIRECT_EXECUTOR, locationListenerTransport, i);
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration, com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onRegister() {
            super.onRegister();
            try {
                ((IBinder) getKey()).linkToDeath(this, 0);
            } catch (RemoteException unused) {
                remove();
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration, com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onUnregister() {
            try {
                ((IBinder) getKey()).unlinkToDeath(this, 0);
            } catch (NoSuchElementException e) {
                Log.w(getTag(), "failed to unregister binder death listener", e);
            }
            super.onUnregister();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration
        protected void onProviderOperationFailure(ListenerExecutor.ListenerOperation<ProviderTransport> listenerOperation, Exception exc) {
            onTransportFailure(exc);
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public void onOperationFailure(ListenerExecutor.ListenerOperation<LocationTransport> listenerOperation, Exception exc) {
            onTransportFailure(exc);
        }

        private void onTransportFailure(Exception exc) {
            if (exc instanceof RemoteException) {
                Log.w(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " removed", exc);
                remove();
                return;
            }
            throw new AssertionError(exc);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                if (LocationManagerService.D) {
                    Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " died");
                }
                if (LocationProviderManager.mLocationFreeze != null) {
                    try {
                        LocationProviderManager.mLocationFreeze.onBinderDied(getKey(), getIdentity().getUid(), getIdentity().getPackageName());
                    } catch (NullPointerException unused) {
                        Log.d(LocationManagerService.TAG, "onBinderDied getKey is null!");
                    }
                }
                remove();
            } catch (RuntimeException e) {
                throw new AssertionError(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LocationPendingIntentRegistration extends LocationRegistration implements PendingIntent.CancelListener {
        LocationPendingIntentRegistration(LocationRequest locationRequest, CallerIdentity callerIdentity, LocationPendingIntentTransport locationPendingIntentTransport, int i) {
            super(locationRequest, callerIdentity, ConcurrentUtils.DIRECT_EXECUTOR, locationPendingIntentTransport, i);
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration, com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onRegister() {
            super.onRegister();
            if (((PendingIntent) getKey()).addCancelListener(ConcurrentUtils.DIRECT_EXECUTOR, this)) {
                return;
            }
            remove();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration, com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onUnregister() {
            ((PendingIntent) getKey()).removeCancelListener(this);
            super.onUnregister();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.LocationRegistration
        protected void onProviderOperationFailure(ListenerExecutor.ListenerOperation<ProviderTransport> listenerOperation, Exception exc) {
            onTransportFailure(exc);
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public void onOperationFailure(ListenerExecutor.ListenerOperation<LocationTransport> listenerOperation, Exception exc) {
            onTransportFailure(exc);
        }

        private void onTransportFailure(Exception exc) {
            if (exc instanceof PendingIntent.CanceledException) {
                Log.w(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " removed", exc);
                remove();
                return;
            }
            throw new AssertionError(exc);
        }

        public void onCanceled(PendingIntent pendingIntent) {
            if (LocationManagerService.D) {
                Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " canceled");
            }
            if (LocationProviderManager.mLocationFreeze != null && pendingIntent != null) {
                LocationProviderManager.mLocationFreeze.onBinderDied(pendingIntent, getIdentity().getUid(), getIdentity().getPackageName());
            }
            remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class GetCurrentLocationListenerRegistration extends Registration implements IBinder.DeathRecipient, AlarmManager.OnAlarmListener {

        @GuardedBy({"mMultiplexerLock"})
        private long mExpirationRealtimeMs;

        GetCurrentLocationListenerRegistration(LocationRequest locationRequest, CallerIdentity callerIdentity, LocationTransport locationTransport, int i) {
            super(locationRequest, callerIdentity, callerIdentity.isMyProcess() ? FgThread.getExecutor() : ConcurrentUtils.DIRECT_EXECUTOR, locationTransport, i);
            this.mExpirationRealtimeMs = JobStatus.NO_LATEST_RUNTIME;
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onRegister() {
            super.onRegister();
            try {
                ((IBinder) getKey()).linkToDeath(this, 0);
            } catch (RemoteException unused) {
                remove();
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long expirationRealtimeMs = getRequest().getExpirationRealtimeMs(elapsedRealtime);
            this.mExpirationRealtimeMs = expirationRealtimeMs;
            if (expirationRealtimeMs <= elapsedRealtime) {
                onAlarm();
            } else if (expirationRealtimeMs < JobStatus.NO_LATEST_RUNTIME) {
                LocationProviderManager.this.mAlarmHelper.setDelayedAlarm(expirationRealtimeMs - elapsedRealtime, this, null);
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.RemovableListenerRegistration, com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onUnregister() {
            if (this.mExpirationRealtimeMs < JobStatus.NO_LATEST_RUNTIME) {
                LocationProviderManager.this.mAlarmHelper.cancel(this);
            }
            try {
                ((IBinder) getKey()).unlinkToDeath(this, 0);
            } catch (NoSuchElementException e) {
                Log.w(getTag(), "failed to unregister binder death listener", e);
            }
            super.onUnregister();
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onActive() {
            super.onActive();
            Location lastLocationUnsafe = LocationProviderManager.this.getLastLocationUnsafe(getIdentity().getUserId(), getPermissionLevel(), getRequest().isBypass(), 30000L);
            if (lastLocationUnsafe != null) {
                executeOperation(acceptLocationChange(LocationResult.wrap(new Location[]{lastLocationUnsafe})));
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration, com.android.server.location.listeners.ListenerRegistration
        @GuardedBy({"mMultiplexerLock"})
        protected void onInactive() {
            executeOperation(acceptLocationChange(null));
            super.onInactive();
        }

        @GuardedBy({"mMultiplexerLock"})
        void deliverNull() {
            executeOperation(acceptLocationChange(null));
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            if (LocationManagerService.D) {
                Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " expired at " + TimeUtils.formatRealtime(this.mExpirationRealtimeMs));
            }
            synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                this.mExpirationRealtimeMs = JobStatus.NO_LATEST_RUNTIME;
                executeOperation(acceptLocationChange(null));
            }
        }

        @Override // com.android.server.location.provider.LocationProviderManager.Registration
        @GuardedBy({"mMultiplexerLock"})
        ListenerExecutor.ListenerOperation<LocationTransport> acceptLocationChange(LocationResult locationResult) {
            LocationResult locationResult2 = null;
            if (SystemClock.elapsedRealtime() >= this.mExpirationRealtimeMs) {
                if (LocationManagerService.D) {
                    Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " expired at " + TimeUtils.formatRealtime(this.mExpirationRealtimeMs));
                }
                locationResult = null;
            }
            if (locationResult == null || LocationProviderManager.this.mAppOpsHelper.noteOpNoThrow(LocationPermissions.asAppOp(getPermissionLevel()), getIdentity())) {
                locationResult2 = locationResult;
            } else if (LocationManagerService.D) {
                Log.w(LocationManagerService.TAG, "noteOp denied for " + getIdentity());
            }
            if (locationResult2 != null) {
                locationResult2 = locationResult2.asLastLocationResult();
            }
            final LocationResult permittedLocationResult = LocationProviderManager.this.getPermittedLocationResult(locationResult2, getPermissionLevel());
            return new ListenerExecutor.ListenerOperation<LocationTransport>() { // from class: com.android.server.location.provider.LocationProviderManager.GetCurrentLocationListenerRegistration.1
                public void operate(LocationTransport locationTransport) throws Exception {
                    LocationResult locationResult3;
                    LocationResult locationResult4;
                    if (GetCurrentLocationListenerRegistration.this.getIdentity().getPid() == Process.myPid() && (locationResult4 = permittedLocationResult) != null) {
                        locationResult3 = locationResult4.deepCopy();
                    } else {
                        locationResult3 = permittedLocationResult;
                    }
                    locationTransport.deliverOnLocationChanged(locationResult3, null);
                    LocationEventLog locationEventLog = LocationEventLog.EVENT_LOG;
                    String str = LocationProviderManager.this.mName;
                    LocationResult locationResult5 = permittedLocationResult;
                    locationEventLog.logProviderDeliveredLocations(str, locationResult5 != null ? locationResult5.size() : 0, GetCurrentLocationListenerRegistration.this.getIdentity());
                }

                public void onPostExecute(boolean z) {
                    if (z) {
                        GetCurrentLocationListenerRegistration.this.remove();
                    }
                }
            };
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public void onOperationFailure(ListenerExecutor.ListenerOperation<LocationTransport> listenerOperation, Exception exc) {
            if (exc instanceof RemoteException) {
                Log.w(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " removed", exc);
                remove();
                return;
            }
            throw new AssertionError(exc);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                if (LocationManagerService.D) {
                    Log.d(LocationManagerService.TAG, LocationProviderManager.this.mName + " provider registration " + getIdentity() + " died");
                }
                remove();
            } catch (RuntimeException e) {
                throw new AssertionError(e);
            }
        }
    }

    public LocationProviderManager(Context context, Injector injector, String str, PassiveLocationProviderManager passiveLocationProviderManager) {
        this(context, injector, str, passiveLocationProviderManager, Collections.emptyList());
    }

    public LocationProviderManager(Context context, Injector injector, String str, PassiveLocationProviderManager passiveLocationProviderManager, Collection<String> collection) {
        this.mUserChangedListener = new UserInfoHelper.UserListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda15
            @Override // com.android.server.location.injector.UserInfoHelper.UserListener
            public final void onUserChanged(int i, int i2) {
                LocationProviderManager.this.onUserChanged(i, i2);
            }
        };
        this.mLocationUserSettingsListener = new LocationSettings.LocationUserSettingsListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda17
            @Override // com.android.server.location.settings.LocationSettings.LocationUserSettingsListener
            public final void onLocationUserSettingsChanged(int i, LocationUserSettings locationUserSettings, LocationUserSettings locationUserSettings2) {
                LocationProviderManager.this.onLocationUserSettingsChanged(i, locationUserSettings, locationUserSettings2);
            }
        };
        this.mLocationEnabledChangedListener = new SettingsHelper.UserSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda18
            @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
            public final void onSettingChanged(int i) {
                LocationProviderManager.this.onLocationEnabledChanged(i);
            }
        };
        this.mBackgroundThrottlePackageWhitelistChangedListener = new SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda19
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                LocationProviderManager.this.onBackgroundThrottlePackageWhitelistChanged();
            }
        };
        this.mLocationPackageBlacklistChangedListener = new SettingsHelper.UserSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda20
            @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
            public final void onSettingChanged(int i) {
                LocationProviderManager.this.onLocationPackageBlacklistChanged(i);
            }
        };
        this.mLocationPermissionsListener = new LocationPermissionsHelper.LocationPermissionsListener() { // from class: com.android.server.location.provider.LocationProviderManager.1
            @Override // com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener
            public void onLocationPermissionsChanged(String str2) {
                LocationProviderManager.this.onLocationPermissionsChanged(str2);
            }

            @Override // com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener
            public void onLocationPermissionsChanged(int i) {
                LocationProviderManager.this.onLocationPermissionsChanged(i);
            }
        };
        this.mAppForegroundChangedListener = new AppForegroundHelper.AppForegroundListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda21
            @Override // com.android.server.location.injector.AppForegroundHelper.AppForegroundListener
            public final void onAppForegroundChanged(int i, boolean z) {
                LocationProviderManager.this.onAppForegroundChanged(i, z);
            }
        };
        this.mBackgroundThrottleIntervalChangedListener = new SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda22
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                LocationProviderManager.this.onBackgroundThrottleIntervalChanged();
            }
        };
        this.mAdasPackageAllowlistChangedListener = new SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda23
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                LocationProviderManager.this.onAdasAllowlistChanged();
            }
        };
        this.mIgnoreSettingsPackageWhitelistChangedListener = new SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda24
            @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
            public final void onSettingChanged() {
                LocationProviderManager.this.onIgnoreSettingsWhitelistChanged();
            }
        };
        this.mLocationPowerSaveModeChangedListener = new LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda25
            @Override // com.android.server.location.injector.LocationPowerSaveModeHelper.LocationPowerSaveModeChangedListener
            public final void onLocationPowerSaveModeChanged(int i) {
                LocationProviderManager.this.onLocationPowerSaveModeChanged(i);
            }
        };
        this.mScreenInteractiveChangedListener = new ScreenInteractiveHelper.ScreenInteractiveChangedListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda16
            @Override // com.android.server.location.injector.ScreenInteractiveHelper.ScreenInteractiveChangedListener
            public final void onScreenInteractiveChanged(boolean z) {
                LocationProviderManager.this.onScreenInteractiveChanged(z);
            }
        };
        this.mPackageResetResponder = new PackageResetHelper.Responder() { // from class: com.android.server.location.provider.LocationProviderManager.2
            @Override // com.android.server.location.injector.PackageResetHelper.Responder
            public void onPackageReset(String str2) {
                LocationProviderManager.this.onPackageReset(str2);
            }

            @Override // com.android.server.location.injector.PackageResetHelper.Responder
            public boolean isResetableForPackage(String str2) {
                return LocationProviderManager.this.isResetableForPackage(str2);
            }
        };
        this.mLocationProviderManagerWrapper = new LocationProviderManagerWrapper();
        this.mContext = context;
        Objects.requireNonNull(str);
        this.mName = str;
        this.mPassiveManager = passiveLocationProviderManager;
        this.mState = 2;
        this.mEnabled = new SparseBooleanArray(2);
        this.mLastLocations = new SparseArray<>(2);
        this.mRequiredPermissions = collection;
        this.mEnabledListeners = new ArrayList<>();
        this.mProviderRequestListeners = new CopyOnWriteArrayList<>();
        LocationManagerInternal locationManagerInternal = (LocationManagerInternal) LocalServices.getService(LocationManagerInternal.class);
        Objects.requireNonNull(locationManagerInternal);
        this.mLocationManagerInternal = locationManagerInternal;
        this.mLocationSettings = injector.getLocationSettings();
        SettingsHelper settingsHelper = injector.getSettingsHelper();
        this.mSettingsHelper = settingsHelper;
        this.mUserHelper = injector.getUserInfoHelper();
        this.mAlarmHelper = injector.getAlarmHelper();
        this.mAppOpsHelper = injector.getAppOpsHelper();
        this.mLocationPermissionsHelper = injector.getLocationPermissionsHelper();
        this.mAppForegroundHelper = injector.getAppForegroundHelper();
        this.mLocationPowerSaveModeHelper = injector.getLocationPowerSaveModeHelper();
        this.mScreenInteractiveHelper = injector.getScreenInteractiveHelper();
        this.mLocationUsageLogger = injector.getLocationUsageLogger();
        this.mLocationFudger = new LocationFudger(settingsHelper.getCoarseLocationAccuracyM());
        this.mPackageResetHelper = injector.getPackageResetHelper();
        MockableLocationProvider mockableLocationProvider = new MockableLocationProvider(this.mMultiplexerLock);
        this.mProvider = mockableLocationProvider;
        mockableLocationProvider.getController().setListener(this);
    }

    public void startManager(StateChangedListener stateChangedListener) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState == 2);
            this.mState = 0;
            this.mStateChangedListener = stateChangedListener;
            this.mUserHelper.addListener(this.mUserChangedListener);
            this.mLocationSettings.registerLocationUserSettingsListener(this.mLocationUserSettingsListener);
            this.mSettingsHelper.addOnLocationEnabledChangedListener(this.mLocationEnabledChangedListener);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mProvider.getController().start();
                onUserStarted(-1);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void stopManager() {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState == 0);
            this.mState = 1;
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                onEnabledChanged(-1);
                removeRegistrationIf(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda29
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$stopManager$0;
                        lambda$stopManager$0 = LocationProviderManager.lambda$stopManager$0(obj);
                        return lambda$stopManager$0;
                    }
                });
                this.mProvider.getController().stop();
                Binder.restoreCallingIdentity(clearCallingIdentity);
                this.mUserHelper.removeListener(this.mUserChangedListener);
                this.mLocationSettings.unregisterLocationUserSettingsListener(this.mLocationUserSettingsListener);
                this.mSettingsHelper.removeOnLocationEnabledChangedListener(this.mLocationEnabledChangedListener);
                Preconditions.checkState(this.mEnabledListeners.isEmpty());
                this.mProviderRequestListeners.clear();
                this.mEnabled.clear();
                this.mLastLocations.clear();
                this.mStateChangedListener = null;
                this.mState = 2;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
    }

    public String getName() {
        return this.mName;
    }

    public AbstractLocationProvider.State getState() {
        return this.mProvider.getState();
    }

    public CallerIdentity getProviderIdentity() {
        return this.mProvider.getState().identity;
    }

    public ProviderProperties getProperties() {
        return this.mProvider.getState().properties;
    }

    public boolean hasProvider() {
        return this.mProvider.getProvider() != null;
    }

    public boolean isEnabled(int i) {
        boolean valueAt;
        if (i == -10000) {
            return false;
        }
        if (i == -2) {
            return isEnabled(this.mUserHelper.getCurrentUserId());
        }
        Preconditions.checkArgument(i >= 0);
        synchronized (this.mMultiplexerLock) {
            int indexOfKey = this.mEnabled.indexOfKey(i);
            if (indexOfKey < 0) {
                Log.w(LocationManagerService.TAG, this.mName + " provider saw user " + i + " unexpectedly");
                onEnabledChanged(i);
                indexOfKey = this.mEnabled.indexOfKey(i);
            }
            valueAt = this.mEnabled.valueAt(indexOfKey);
        }
        return valueAt;
    }

    @SuppressLint({"AndroidFrameworkRequiresPermission"})
    public boolean isVisibleToCaller() {
        Iterator<String> it = this.mRequiredPermissions.iterator();
        while (it.hasNext()) {
            if (this.mContext.checkCallingOrSelfPermission(it.next()) != 0) {
                return false;
            }
        }
        return true;
    }

    public void addEnabledListener(LocationManagerInternal.ProviderEnabledListener providerEnabledListener) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            this.mEnabledListeners.add(providerEnabledListener);
        }
    }

    public void removeEnabledListener(LocationManagerInternal.ProviderEnabledListener providerEnabledListener) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            this.mEnabledListeners.remove(providerEnabledListener);
        }
    }

    public void addProviderRequestListener(IProviderRequestListener iProviderRequestListener) {
        this.mProviderRequestListeners.add(iProviderRequestListener);
    }

    public void removeProviderRequestListener(IProviderRequestListener iProviderRequestListener) {
        this.mProviderRequestListeners.remove(iProviderRequestListener);
    }

    public void setRealProvider(AbstractLocationProvider abstractLocationProvider) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mProvider.setRealProvider(abstractLocationProvider);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void setMockProvider(MockLocationProvider mockLocationProvider) {
        synchronized (this.mMultiplexerLock) {
            boolean z = true;
            Preconditions.checkState(this.mState != 2);
            LocationEventLog locationEventLog = LocationEventLog.EVENT_LOG;
            String str = this.mName;
            if (mockLocationProvider == null) {
                z = false;
            }
            locationEventLog.logProviderMocked(str, z);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mProvider.setMockProvider(mockLocationProvider);
                if (mockLocationProvider == null) {
                    int size = this.mLastLocations.size();
                    for (int i = 0; i < size; i++) {
                        this.mLastLocations.valueAt(i).clearMock();
                    }
                    this.mLocationFudger.resetOffsets();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void setMockProviderAllowed(boolean z) {
        synchronized (this.mMultiplexerLock) {
            if (!this.mProvider.isMock()) {
                throw new IllegalArgumentException(this.mName + " provider is not a test provider");
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mProvider.setMockProviderAllowed(z);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void setMockProviderLocation(Location location) {
        synchronized (this.mMultiplexerLock) {
            if (!this.mProvider.isMock()) {
                throw new IllegalArgumentException(this.mName + " provider is not a test provider");
            }
            String provider = location.getProvider();
            if (!TextUtils.isEmpty(provider) && !this.mName.equals(provider)) {
                EventLog.writeEvent(1397638484, "33091107", Integer.valueOf(Binder.getCallingUid()), this.mName + "!=" + provider);
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                this.mProvider.setMockProviderLocation(location);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public Location getLastLocation(LastLocationRequest lastLocationRequest, CallerIdentity callerIdentity, int i) {
        LastLocationRequest calculateLastLocationRequest = calculateLastLocationRequest(lastLocationRequest, callerIdentity);
        if (!isActive(calculateLastLocationRequest.isBypass(), callerIdentity) || !this.mAppOpsHelper.noteOpNoThrow(LocationPermissions.asAppOp(i), callerIdentity)) {
            return null;
        }
        Location permittedLocation = getPermittedLocation(getLastLocationUnsafe(callerIdentity.getUserId(), i, calculateLastLocationRequest.isBypass(), JobStatus.NO_LATEST_RUNTIME), i);
        return (permittedLocation == null || callerIdentity.getPid() != Process.myPid()) ? permittedLocation : new Location(permittedLocation);
    }

    private LastLocationRequest calculateLastLocationRequest(LastLocationRequest lastLocationRequest, CallerIdentity callerIdentity) {
        LastLocationRequest.Builder builder = new LastLocationRequest.Builder(lastLocationRequest);
        boolean isLocationSettingsIgnored = lastLocationRequest.isLocationSettingsIgnored();
        boolean z = false;
        if (isLocationSettingsIgnored) {
            if (!this.mSettingsHelper.getIgnoreSettingsAllowlist().contains(callerIdentity.getPackageName(), callerIdentity.getAttributionTag()) && !this.mLocationManagerInternal.isProvider((String) null, callerIdentity)) {
                isLocationSettingsIgnored = false;
            }
            builder.setLocationSettingsIgnored(isLocationSettingsIgnored);
        }
        boolean isAdasGnssBypass = lastLocationRequest.isAdasGnssBypass();
        if (isAdasGnssBypass) {
            if (!"gps".equals(this.mName)) {
                Log.e(LocationManagerService.TAG, "adas gnss bypass request received in non-gps provider");
            } else if (this.mUserHelper.isCurrentUserId(callerIdentity.getUserId()) && this.mLocationSettings.getUserSettings(callerIdentity.getUserId()).isAdasGnssLocationEnabled() && this.mSettingsHelper.getAdasAllowlist().contains(callerIdentity.getPackageName(), callerIdentity.getAttributionTag())) {
                z = isAdasGnssBypass;
            }
            builder.setAdasGnssBypass(z);
        }
        return builder.build();
    }

    public Location getLastLocationUnsafe(int i, int i2, boolean z, long j) {
        Location location;
        Location location2 = null;
        if (i == -1) {
            for (int i3 : this.mUserHelper.getRunningUserIds()) {
                Location lastLocationUnsafe = getLastLocationUnsafe(i3, i2, z, j);
                if (location2 == null || (lastLocationUnsafe != null && lastLocationUnsafe.getElapsedRealtimeNanos() > location2.getElapsedRealtimeNanos())) {
                    location2 = lastLocationUnsafe;
                }
            }
            return location2;
        }
        if (i == -2) {
            return getLastLocationUnsafe(this.mUserHelper.getCurrentUserId(), i2, z, j);
        }
        Preconditions.checkArgument(i >= 0);
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            LastLocation lastLocation = this.mLastLocations.get(i);
            location = lastLocation == null ? null : lastLocation.get(i2, z);
        }
        if (location != null && location.getElapsedRealtimeAgeMillis() <= j) {
            return location;
        }
        return null;
    }

    public void injectLastLocation(Location location, int i) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            if (getLastLocationUnsafe(i, 2, false, JobStatus.NO_LATEST_RUNTIME) == null) {
                setLastLocation(location, i);
            }
        }
    }

    private void setLastLocation(Location location, int i) {
        if (i == -1) {
            for (int i2 : this.mUserHelper.getRunningUserIds()) {
                setLastLocation(location, i2);
            }
            return;
        }
        if (i == -2) {
            setLastLocation(location, this.mUserHelper.getCurrentUserId());
            return;
        }
        Preconditions.checkArgument(i >= 0);
        synchronized (this.mMultiplexerLock) {
            LastLocation lastLocation = this.mLastLocations.get(i);
            if (lastLocation == null) {
                lastLocation = new LastLocation();
                this.mLastLocations.put(i, lastLocation);
            }
            if (isEnabled(i)) {
                lastLocation.set(location);
            }
            lastLocation.setBypass(location);
        }
    }

    public ICancellationSignal getCurrentLocation(LocationRequest locationRequest, CallerIdentity callerIdentity, int i, final ILocationCallback iLocationCallback) {
        if (locationRequest.getDurationMillis() > 30000) {
            locationRequest = new LocationRequest.Builder(locationRequest).setDurationMillis(30000L).build();
        }
        final GetCurrentLocationListenerRegistration getCurrentLocationListenerRegistration = new GetCurrentLocationListenerRegistration(locationRequest, callerIdentity, new GetCurrentLocationTransport(iLocationCallback), i);
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                putRegistration(iLocationCallback.asBinder(), getCurrentLocationListenerRegistration);
                if (!getCurrentLocationListenerRegistration.isActive()) {
                    getCurrentLocationListenerRegistration.deliverNull();
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        ICancellationSignal createTransport = CancellationSignal.createTransport();
        CancellationSignal.fromTransport(createTransport).setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda14
            @Override // android.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                LocationProviderManager.this.lambda$getCurrentLocation$2(iLocationCallback, getCurrentLocationListenerRegistration);
            }
        });
        return createTransport;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCurrentLocation$2(ILocationCallback iLocationCallback, GetCurrentLocationListenerRegistration getCurrentLocationListenerRegistration) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                removeRegistration(iLocationCallback.asBinder(), getCurrentLocationListenerRegistration);
            } catch (RuntimeException e) {
                FgThread.getExecutor().execute(new Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        LocationProviderManager.lambda$getCurrentLocation$1(e);
                    }
                });
                throw e;
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getCurrentLocation$1(RuntimeException runtimeException) {
        throw new AssertionError(runtimeException);
    }

    public void sendExtraCommand(int i, int i2, String str, Bundle bundle) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mProvider.getController().sendExtraCommand(i, i2, str, bundle);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void registerLocationRequest(LocationRequest locationRequest, CallerIdentity callerIdentity, int i, ILocationListener iLocationListener) {
        LocationListenerRegistration locationListenerRegistration = new LocationListenerRegistration(locationRequest, callerIdentity, new LocationListenerTransport(iLocationListener), i);
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                IOplusLBSMainClass iOplusLBSMainClass = mOplusLbsClass;
                if (iOplusLBSMainClass != null) {
                    iOplusLBSMainClass.getProviderStatus(this.mName, this.mProvider.getState().allowed, isEnabled(callerIdentity.getUserId()), true, callerIdentity.getUserId());
                }
                putRegistration(iLocationListener.asBinder(), locationListenerRegistration);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void registerLocationRequest(LocationRequest locationRequest, CallerIdentity callerIdentity, int i, PendingIntent pendingIntent) {
        LocationPendingIntentRegistration locationPendingIntentRegistration = new LocationPendingIntentRegistration(locationRequest, callerIdentity, new LocationPendingIntentTransport(this.mContext, pendingIntent), i);
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                putRegistration(pendingIntent, locationPendingIntentRegistration);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void flush(ILocationListener iLocationListener, final int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (updateRegistration(iLocationListener.asBinder(), new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$flush$3;
                    lambda$flush$3 = LocationProviderManager.lambda$flush$3(i, (LocationProviderManager.Registration) obj);
                    return lambda$flush$3;
                }
            })) {
            } else {
                throw new IllegalArgumentException("unregistered listener cannot be flushed");
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$flush$3(int i, Registration registration) {
        registration.flush(i);
        return false;
    }

    public void flush(PendingIntent pendingIntent, final int i) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (updateRegistration(pendingIntent, new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$flush$4;
                    lambda$flush$4 = LocationProviderManager.lambda$flush$4(i, (LocationProviderManager.Registration) obj);
                    return lambda$flush$4;
                }
            })) {
            } else {
                throw new IllegalArgumentException("unregistered pending intent cannot be flushed");
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$flush$4(int i, Registration registration) {
        registration.flush(i);
        return false;
    }

    public void unregisterLocationRequest(ILocationListener iLocationListener) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                removeRegistration((LocationProviderManager) iLocationListener.asBinder());
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void unregisterLocationRequest(PendingIntent pendingIntent) {
        synchronized (this.mMultiplexerLock) {
            Preconditions.checkState(this.mState != 2);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                removeRegistration((LocationProviderManager) pendingIntent);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    protected void onRegister() {
        this.mSettingsHelper.addOnBackgroundThrottleIntervalChangedListener(this.mBackgroundThrottleIntervalChangedListener);
        this.mSettingsHelper.addOnBackgroundThrottlePackageWhitelistChangedListener(this.mBackgroundThrottlePackageWhitelistChangedListener);
        this.mSettingsHelper.addOnLocationPackageBlacklistChangedListener(this.mLocationPackageBlacklistChangedListener);
        this.mSettingsHelper.addAdasAllowlistChangedListener(this.mAdasPackageAllowlistChangedListener);
        this.mSettingsHelper.addIgnoreSettingsAllowlistChangedListener(this.mIgnoreSettingsPackageWhitelistChangedListener);
        this.mLocationPermissionsHelper.addListener(this.mLocationPermissionsListener);
        this.mAppForegroundHelper.addListener(this.mAppForegroundChangedListener);
        this.mLocationPowerSaveModeHelper.addListener(this.mLocationPowerSaveModeChangedListener);
        this.mScreenInteractiveHelper.addListener(this.mScreenInteractiveChangedListener);
        this.mPackageResetHelper.register(this.mPackageResetResponder);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    protected void onUnregister() {
        this.mSettingsHelper.removeOnBackgroundThrottleIntervalChangedListener(this.mBackgroundThrottleIntervalChangedListener);
        this.mSettingsHelper.removeOnBackgroundThrottlePackageWhitelistChangedListener(this.mBackgroundThrottlePackageWhitelistChangedListener);
        this.mSettingsHelper.removeOnLocationPackageBlacklistChangedListener(this.mLocationPackageBlacklistChangedListener);
        this.mSettingsHelper.removeAdasAllowlistChangedListener(this.mAdasPackageAllowlistChangedListener);
        this.mSettingsHelper.removeIgnoreSettingsAllowlistChangedListener(this.mIgnoreSettingsPackageWhitelistChangedListener);
        this.mLocationPermissionsHelper.removeListener(this.mLocationPermissionsListener);
        this.mAppForegroundHelper.removeListener(this.mAppForegroundChangedListener);
        this.mLocationPowerSaveModeHelper.removeListener(this.mLocationPowerSaveModeChangedListener);
        this.mScreenInteractiveHelper.removeListener(this.mScreenInteractiveChangedListener);
        this.mPackageResetHelper.unregister(this.mPackageResetResponder);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    public void onRegistrationAdded(Object obj, Registration registration) {
        this.mLocationUsageLogger.logLocationApiUsage(0, 1, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), this.mName, registration.getRequest(), obj instanceof PendingIntent, obj instanceof IBinder, null, registration.isForeground());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    public void onRegistrationReplaced(Object obj, Registration registration, Object obj2, Registration registration2) {
        registration2.setLastDeliveredLocation(registration.getLastDeliveredLocation());
        super.onRegistrationReplaced((Registration) obj, (Object) registration, (Registration) obj2, (Object) registration2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    public void onRegistrationRemoved(Object obj, Registration registration) {
        this.mLocationUsageLogger.logLocationApiUsage(1, 1, registration.getIdentity().getPackageName(), registration.getIdentity().getAttributionTag(), this.mName, registration.getRequest(), obj instanceof PendingIntent, obj instanceof IBinder, null, registration.isForeground());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    public boolean registerWithService(ProviderRequest providerRequest, Collection<Registration> collection) {
        if (providerRequest.isActive()) {
            return reregisterWithService(ProviderRequest.EMPTY_REQUEST, providerRequest, collection);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    public boolean reregisterWithService(ProviderRequest providerRequest, final ProviderRequest providerRequest2, Collection<Registration> collection) {
        long calculateRequestDelayMillis = ((providerRequest.isBypass() || !providerRequest2.isBypass()) && providerRequest2.getIntervalMillis() <= providerRequest.getIntervalMillis()) ? calculateRequestDelayMillis(providerRequest2.getIntervalMillis(), collection) : 0L;
        Preconditions.checkState(calculateRequestDelayMillis >= 0 && calculateRequestDelayMillis <= providerRequest2.getIntervalMillis());
        if (calculateRequestDelayMillis < 30000) {
            setProviderRequest(providerRequest2);
        } else {
            if (LocationManagerService.D) {
                Log.d(LocationManagerService.TAG, this.mName + " provider delaying request update " + providerRequest2 + " by " + TimeUtils.formatDuration(calculateRequestDelayMillis));
            }
            AlarmManager.OnAlarmListener onAlarmListener = this.mDelayedRegister;
            if (onAlarmListener != null) {
                this.mAlarmHelper.cancel(onAlarmListener);
                this.mDelayedRegister = null;
            }
            AlarmManager.OnAlarmListener onAlarmListener2 = new AlarmManager.OnAlarmListener() { // from class: com.android.server.location.provider.LocationProviderManager.3
                @Override // android.app.AlarmManager.OnAlarmListener
                public void onAlarm() {
                    synchronized (((ListenerMultiplexer) LocationProviderManager.this).mMultiplexerLock) {
                        if (LocationProviderManager.this.mDelayedRegister == this) {
                            LocationProviderManager.this.mDelayedRegister = null;
                            LocationProviderManager.this.setProviderRequest(providerRequest2);
                        }
                    }
                }
            };
            this.mDelayedRegister = onAlarmListener2;
            this.mAlarmHelper.setDelayedAlarm(calculateRequestDelayMillis, onAlarmListener2, null);
        }
        return true;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    protected void unregisterWithService() {
        setProviderRequest(ProviderRequest.EMPTY_REQUEST);
    }

    @GuardedBy({"mMultiplexerLock"})
    void setProviderRequest(final ProviderRequest providerRequest) {
        AlarmManager.OnAlarmListener onAlarmListener = this.mDelayedRegister;
        if (onAlarmListener != null) {
            this.mAlarmHelper.cancel(onAlarmListener);
            this.mDelayedRegister = null;
        }
        LocationEventLog.EVENT_LOG.logProviderUpdateRequest(this.mName, providerRequest);
        if (LocationManagerService.D) {
            Log.d(LocationManagerService.TAG, this.mName + " provider request changed to " + providerRequest);
        }
        this.mProvider.getController().setRequest(providerRequest);
        FgThread.getHandler().post(new Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LocationProviderManager.this.lambda$setProviderRequest$5(providerRequest);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setProviderRequest$5(ProviderRequest providerRequest) {
        Iterator<IProviderRequestListener> it = this.mProviderRequestListeners.iterator();
        while (it.hasNext()) {
            IProviderRequestListener next = it.next();
            try {
                next.onProviderRequestChanged(this.mName, providerRequest);
            } catch (RemoteException unused) {
                this.mProviderRequestListeners.remove(next);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x002d, code lost:
    
        if (r0 != 4) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x003f, code lost:
    
        if ("gps".equals(r4.mName) == false) goto L28;
     */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean isActive(Registration registration) {
        if (!registration.isPermitted()) {
            return false;
        }
        boolean isBypass = registration.getRequest().isBypass();
        if (!isActive(isBypass, registration.getIdentity())) {
            return false;
        }
        if (!isBypass) {
            int locationPowerSaveMode = this.mLocationPowerSaveModeHelper.getLocationPowerSaveMode();
            if (locationPowerSaveMode != 1) {
                if (locationPowerSaveMode != 2) {
                    if (locationPowerSaveMode == 3) {
                        if (!registration.isForeground()) {
                            return false;
                        }
                    }
                }
                if (!this.mScreenInteractiveHelper.isInteractive()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isActive(boolean z, CallerIdentity callerIdentity) {
        return callerIdentity.isSystemServer() ? z || isEnabled(this.mUserHelper.getCurrentUserId()) : (z || (isEnabled(callerIdentity.getUserId()) && this.mUserHelper.isVisibleUserId(callerIdentity.getUserId()))) && !this.mSettingsHelper.isLocationPackageBlacklisted(callerIdentity.getUserId(), callerIdentity.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    @GuardedBy({"mMultiplexerLock"})
    public ProviderRequest mergeRegistrations(Collection<Registration> collection) {
        long j;
        Iterator<Registration> it = collection.iterator();
        int i = HdmiCecKeycode.CEC_KEYCODE_SELECT_MEDIA_FUNCTION;
        boolean z = false;
        long j2 = Long.MAX_VALUE;
        long j3 = Long.MAX_VALUE;
        boolean z2 = true;
        boolean z3 = false;
        while (it.hasNext()) {
            LocationRequest request = it.next().getRequest();
            if (request.getIntervalMillis() != JobStatus.NO_LATEST_RUNTIME) {
                j2 = Math.min(request.getIntervalMillis(), j2);
                i = Math.min(request.getQuality(), i);
                j3 = Math.min(request.getMaxUpdateDelayMillis(), j3);
                z |= request.isAdasGnssBypass();
                z3 |= request.isLocationSettingsIgnored();
                z2 &= request.isLowPower();
            }
        }
        if (j2 == JobStatus.NO_LATEST_RUNTIME) {
            return ProviderRequest.EMPTY_REQUEST;
        }
        if (j3 / 2 < j2) {
            j3 = 0;
        }
        try {
            j = Math.multiplyExact(Math.addExact(j2, 1000L) / 2, 3);
        } catch (ArithmeticException unused) {
            j = 9223372036854775806L;
        }
        WorkSource workSource = new WorkSource();
        for (Registration registration : collection) {
            if (registration.getRequest().getIntervalMillis() <= j) {
                workSource.add(registration.getRequest().getWorkSource());
            }
        }
        return new ProviderRequest.Builder().setIntervalMillis(j2).setQuality(i).setMaxUpdateDelayMillis(j3).setAdasGnssBypass(z).setLocationSettingsIgnored(z3).setLowPower(z2).setWorkSource(workSource).build();
    }

    @GuardedBy({"mMultiplexerLock"})
    protected long calculateRequestDelayMillis(long j, Collection<Registration> collection) {
        for (Registration registration : collection) {
            long j2 = 0;
            if (j == 0) {
                break;
            }
            LocationRequest request = registration.getRequest();
            Location lastDeliveredLocation = registration.getLastDeliveredLocation();
            if (lastDeliveredLocation == null && !request.isLocationSettingsIgnored()) {
                lastDeliveredLocation = getLastLocationUnsafe(registration.getIdentity().getUserId(), registration.getPermissionLevel(), false, request.getIntervalMillis());
            }
            if (lastDeliveredLocation != null) {
                j2 = Math.max(0L, request.getIntervalMillis() - lastDeliveredLocation.getElapsedRealtimeAgeMillis());
            }
            j = Math.min(j, j2);
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserChanged(final int i, int i2) {
        synchronized (this.mMultiplexerLock) {
            if (this.mState == 2) {
                return;
            }
            if (i2 != 1) {
                if (i2 == 2) {
                    onUserStarted(i);
                } else if (i2 == 3) {
                    onUserStopped(i);
                } else if (i2 != 4) {
                }
            }
            updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onUserChanged$6;
                    lambda$onUserChanged$6 = LocationProviderManager.lambda$onUserChanged$6(i, (LocationProviderManager.Registration) obj);
                    return lambda$onUserChanged$6;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onUserChanged$6(int i, Registration registration) {
        return registration.getIdentity().getUserId() == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationUserSettingsChanged(final int i, LocationUserSettings locationUserSettings, LocationUserSettings locationUserSettings2) {
        if (locationUserSettings.isAdasGnssLocationEnabled() != locationUserSettings2.isAdasGnssLocationEnabled()) {
            updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda13
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onLocationUserSettingsChanged$7;
                    lambda$onLocationUserSettingsChanged$7 = LocationProviderManager.lambda$onLocationUserSettingsChanged$7(i, (LocationProviderManager.Registration) obj);
                    return lambda$onLocationUserSettingsChanged$7;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onLocationUserSettingsChanged$7(int i, Registration registration) {
        return registration.onAdasGnssLocationEnabledChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationEnabledChanged(int i) {
        synchronized (this.mMultiplexerLock) {
            if (this.mState == 2) {
                return;
            }
            onEnabledChanged(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScreenInteractiveChanged(boolean z) {
        int locationPowerSaveMode = this.mLocationPowerSaveModeHelper.getLocationPowerSaveMode();
        if (locationPowerSaveMode != 1) {
            if (locationPowerSaveMode != 2 && locationPowerSaveMode != 4) {
                return;
            }
        } else if (!"gps".equals(this.mName)) {
            return;
        }
        updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onScreenInteractiveChanged$8;
                lambda$onScreenInteractiveChanged$8 = LocationProviderManager.lambda$onScreenInteractiveChanged$8((LocationProviderManager.Registration) obj);
                return lambda$onScreenInteractiveChanged$8;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBackgroundThrottlePackageWhitelistChanged() {
        updateRegistrations(new LocationProviderManager$$ExternalSyntheticLambda1());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBackgroundThrottleIntervalChanged() {
        updateRegistrations(new LocationProviderManager$$ExternalSyntheticLambda1());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPowerSaveModeChanged(int i) {
        updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onLocationPowerSaveModeChanged$9;
                lambda$onLocationPowerSaveModeChanged$9 = LocationProviderManager.lambda$onLocationPowerSaveModeChanged$9((LocationProviderManager.Registration) obj);
                return lambda$onLocationPowerSaveModeChanged$9;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onAppForegroundChanged$10(int i, boolean z, Registration registration) {
        return registration.onForegroundChanged(i, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAppForegroundChanged(final int i, final boolean z) {
        updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda30
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onAppForegroundChanged$10;
                lambda$onAppForegroundChanged$10 = LocationProviderManager.lambda$onAppForegroundChanged$10(i, z, (LocationProviderManager.Registration) obj);
                return lambda$onAppForegroundChanged$10;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAdasAllowlistChanged() {
        updateRegistrations(new LocationProviderManager$$ExternalSyntheticLambda1());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onIgnoreSettingsWhitelistChanged() {
        updateRegistrations(new LocationProviderManager$$ExternalSyntheticLambda1());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onLocationPackageBlacklistChanged$11(int i, Registration registration) {
        return registration.getIdentity().getUserId() == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPackageBlacklistChanged(final int i) {
        updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda26
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onLocationPackageBlacklistChanged$11;
                lambda$onLocationPackageBlacklistChanged$11 = LocationProviderManager.lambda$onLocationPackageBlacklistChanged$11(i, (LocationProviderManager.Registration) obj);
                return lambda$onLocationPackageBlacklistChanged$11;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPermissionsChanged(final String str) {
        updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onLocationPermissionsChanged$12;
                lambda$onLocationPermissionsChanged$12 = LocationProviderManager.lambda$onLocationPermissionsChanged$12(str, (LocationProviderManager.Registration) obj);
                return lambda$onLocationPermissionsChanged$12;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onLocationPermissionsChanged$12(String str, Registration registration) {
        return registration.onLocationPermissionsChanged(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onLocationPermissionsChanged$13(int i, Registration registration) {
        return registration.onLocationPermissionsChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPermissionsChanged(final int i) {
        updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda33
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onLocationPermissionsChanged$13;
                lambda$onLocationPermissionsChanged$13 = LocationProviderManager.lambda$onLocationPermissionsChanged$13(i, (LocationProviderManager.Registration) obj);
                return lambda$onLocationPermissionsChanged$13;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageReset(final String str) {
        updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onPackageReset$14;
                lambda$onPackageReset$14 = LocationProviderManager.lambda$onPackageReset$14(str, (LocationProviderManager.Registration) obj);
                return lambda$onPackageReset$14;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onPackageReset$14(String str, Registration registration) {
        if (!registration.getIdentity().getPackageName().equals(str)) {
            return false;
        }
        registration.remove();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isResetableForPackage(final String str) {
        return findRegistration(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$isResetableForPackage$15;
                lambda$isResetableForPackage$15 = LocationProviderManager.lambda$isResetableForPackage$15(str, (LocationProviderManager.Registration) obj);
                return lambda$isResetableForPackage$15;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isResetableForPackage$15(String str, Registration registration) {
        return registration.getIdentity().getPackageName().equals(str);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
    @GuardedBy({"mMultiplexerLock"})
    public void onStateChanged(final AbstractLocationProvider.State state, final AbstractLocationProvider.State state2) {
        if (state.allowed != state2.allowed) {
            onEnabledChanged(-1);
        }
        if (!Objects.equals(state.properties, state2.properties)) {
            updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda27
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((LocationProviderManager.Registration) obj).onProviderPropertiesChanged();
                }
            });
        }
        final StateChangedListener stateChangedListener = this.mStateChangedListener;
        if (stateChangedListener != null) {
            FgThread.getExecutor().execute(new Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    LocationProviderManager.this.lambda$onStateChanged$16(stateChangedListener, state, state2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStateChanged$16(StateChangedListener stateChangedListener, AbstractLocationProvider.State state, AbstractLocationProvider.State state2) {
        stateChangedListener.onStateChanged(this.mName, state, state2);
    }

    @Override // com.android.server.location.provider.AbstractLocationProvider.Listener
    @GuardedBy({"mMultiplexerLock"})
    public void onReportLocation(LocationResult locationResult) {
        final LocationResult locationResult2;
        Location lastLocationUnsafe;
        if (this.mPassiveManager != null) {
            locationResult2 = locationResult.filter(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda31
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onReportLocation$17;
                    lambda$onReportLocation$17 = LocationProviderManager.this.lambda$onReportLocation$17((Location) obj);
                    return lambda$onReportLocation$17;
                }
            });
            if (locationResult2 == null) {
                return;
            } else {
                LocationEventLog.EVENT_LOG.logProviderReceivedLocations(this.mName, locationResult2.size());
            }
        } else {
            locationResult2 = locationResult;
        }
        if (this.mPassiveManager != null && (lastLocationUnsafe = getLastLocationUnsafe(-2, 2, true, JobStatus.NO_LATEST_RUNTIME)) != null && locationResult.get(0).getElapsedRealtimeNanos() < lastLocationUnsafe.getElapsedRealtimeNanos()) {
            Log.e(LocationManagerService.TAG, "non-monotonic location received from " + this.mName + " provider");
        }
        setLastLocation(locationResult2.getLastLocation(), -1);
        IOplusLBSMainClass iOplusLBSMainClass = mOplusLbsClass;
        if (iOplusLBSMainClass != null) {
            iOplusLBSMainClass.handleLocationChanged(locationResult2, LocationManagerService.D);
        }
        deliverToListeners(new Function() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda32
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                ListenerExecutor.ListenerOperation lambda$onReportLocation$18;
                lambda$onReportLocation$18 = LocationProviderManager.lambda$onReportLocation$18(locationResult2, (LocationProviderManager.Registration) obj);
                return lambda$onReportLocation$18;
            }
        });
        PassiveLocationProviderManager passiveLocationProviderManager = this.mPassiveManager;
        if (passiveLocationProviderManager != null) {
            passiveLocationProviderManager.updateLocation(locationResult2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onReportLocation$17(Location location) {
        if (!location.isMock() && location.getLatitude() == 0.0d && location.getLongitude() == 0.0d) {
            Log.e(LocationManagerService.TAG, "blocking 0,0 location from " + this.mName + " provider");
            return false;
        }
        if (location.isComplete()) {
            return true;
        }
        Log.e(LocationManagerService.TAG, "blocking incomplete location from " + this.mName + " provider");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ ListenerExecutor.ListenerOperation lambda$onReportLocation$18(LocationResult locationResult, Registration registration) {
        return registration.acceptLocationChange(locationResult);
    }

    @GuardedBy({"mMultiplexerLock"})
    private void onUserStarted(int i) {
        if (i == -10000) {
            return;
        }
        if (i == -1) {
            this.mEnabled.clear();
            onEnabledChanged(-1);
        } else {
            Preconditions.checkArgument(i >= 0);
            this.mEnabled.delete(i);
            onEnabledChanged(i);
        }
    }

    @GuardedBy({"mMultiplexerLock"})
    private void onUserStopped(int i) {
        if (i == -10000) {
            return;
        }
        if (i == -1) {
            this.mEnabled.clear();
            this.mLastLocations.clear();
        } else {
            Preconditions.checkArgument(i >= 0);
            this.mEnabled.delete(i);
            this.mLastLocations.remove(i);
        }
    }

    @GuardedBy({"mMultiplexerLock"})
    private void onEnabledChanged(final int i) {
        LastLocation lastLocation;
        if (i == -10000) {
            return;
        }
        if (i == -1) {
            for (int i2 : this.mUserHelper.getRunningUserIds()) {
                onEnabledChanged(i2);
            }
            return;
        }
        Preconditions.checkArgument(i >= 0);
        final boolean z = this.mState == 0 && this.mProvider.getState().allowed && this.mSettingsHelper.isLocationEnabled(i);
        int indexOfKey = this.mEnabled.indexOfKey(i);
        Boolean valueOf = indexOfKey < 0 ? null : Boolean.valueOf(this.mEnabled.valueAt(indexOfKey));
        if (valueOf == null || valueOf.booleanValue() != z) {
            this.mEnabled.put(i, z);
            if (valueOf != null || z) {
                if (LocationManagerService.D) {
                    Log.d(LocationManagerService.TAG, "[u" + i + "] " + this.mName + " provider enabled = " + z);
                }
                LocationEventLog.EVENT_LOG.logProviderEnabled(this.mName, i, z);
            }
            if (!z && (lastLocation = this.mLastLocations.get(i)) != null) {
                lastLocation.clearLocations();
            }
            IOplusLBSMainClass iOplusLBSMainClass = mOplusLbsClass;
            if (iOplusLBSMainClass != null) {
                iOplusLBSMainClass.updateSettings(this.mName, i);
            }
            if (valueOf != null) {
                if (!"passive".equals(this.mName)) {
                    this.mContext.sendBroadcastAsUser(new Intent("android.location.PROVIDERS_CHANGED").putExtra("android.location.extra.PROVIDER_NAME", this.mName).putExtra("android.location.extra.PROVIDER_ENABLED", z).addFlags(1073741824).addFlags(268435456), UserHandle.of(i));
                }
                if (!this.mEnabledListeners.isEmpty()) {
                    final LocationManagerInternal.ProviderEnabledListener[] providerEnabledListenerArr = (LocationManagerInternal.ProviderEnabledListener[]) this.mEnabledListeners.toArray(new LocationManagerInternal.ProviderEnabledListener[0]);
                    FgThread.getHandler().post(new Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            LocationProviderManager.this.lambda$onEnabledChanged$19(providerEnabledListenerArr, i, z);
                        }
                    });
                }
            }
            updateRegistrations(new Predicate() { // from class: com.android.server.location.provider.LocationProviderManager$$ExternalSyntheticLambda11
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onEnabledChanged$20;
                    lambda$onEnabledChanged$20 = LocationProviderManager.lambda$onEnabledChanged$20(i, (LocationProviderManager.Registration) obj);
                    return lambda$onEnabledChanged$20;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnabledChanged$19(LocationManagerInternal.ProviderEnabledListener[] providerEnabledListenerArr, int i, boolean z) {
        for (LocationManagerInternal.ProviderEnabledListener providerEnabledListener : providerEnabledListenerArr) {
            providerEnabledListener.onProviderEnabledChanged(this.mName, i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onEnabledChanged$20(int i, Registration registration) {
        return registration.getIdentity().getUserId() == i;
    }

    Location getPermittedLocation(Location location, int i) {
        if (i != 1) {
            if (i == 2) {
                return location;
            }
            throw new AssertionError();
        }
        IOplusLBSMainClass iOplusLBSMainClass = mOplusLbsClass;
        if (iOplusLBSMainClass == null) {
            if (location != null) {
                return this.mLocationFudger.createCoarse(location);
            }
            return null;
        }
        if (location != null) {
            return iOplusLBSMainClass.addCoarseLocationExtra(this.mLocationFudger.createCoarse(location));
        }
        return null;
    }

    LocationResult getPermittedLocationResult(LocationResult locationResult, int i) {
        if (i != 1) {
            if (i == 2) {
                return locationResult;
            }
            throw new AssertionError();
        }
        IOplusLBSMainClass iOplusLBSMainClass = mOplusLbsClass;
        if (iOplusLBSMainClass == null) {
            if (locationResult != null) {
                return this.mLocationFudger.createCoarse(locationResult);
            }
            return null;
        }
        if (locationResult != null) {
            return iOplusLBSMainClass.addCoarseLocationExtra(this.mLocationFudger.createCoarse(locationResult));
        }
        return null;
    }

    public void dump(FileDescriptor fileDescriptor, IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        synchronized (this.mMultiplexerLock) {
            indentingPrintWriter.print(this.mName);
            indentingPrintWriter.print(" provider");
            if (this.mProvider.isMock()) {
                indentingPrintWriter.print(" [mock]");
            }
            indentingPrintWriter.println(":");
            indentingPrintWriter.increaseIndent();
            super.dump(fileDescriptor, (PrintWriter) indentingPrintWriter, strArr);
            int[] runningUserIds = this.mUserHelper.getRunningUserIds();
            for (int i : runningUserIds) {
                if (runningUserIds.length != 1) {
                    indentingPrintWriter.print("user ");
                    indentingPrintWriter.print(i);
                    indentingPrintWriter.println(":");
                    indentingPrintWriter.increaseIndent();
                }
                indentingPrintWriter.print("last location=");
                indentingPrintWriter.println(getLastLocationUnsafe(i, 2, false, JobStatus.NO_LATEST_RUNTIME));
                indentingPrintWriter.print("enabled=");
                indentingPrintWriter.println(isEnabled(i));
                if (runningUserIds.length != 1) {
                    indentingPrintWriter.decreaseIndent();
                }
            }
        }
        this.mProvider.dump(fileDescriptor, indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public String getServiceState() {
        return this.mProvider.getCurrentRequest().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class LastLocation {
        private Location mCoarseBypassLocation;
        private Location mCoarseLocation;
        private Location mFineBypassLocation;
        private Location mFineLocation;

        LastLocation() {
        }

        public void clearMock() {
            Location location = this.mFineLocation;
            if (location != null && location.isMock()) {
                this.mFineLocation = null;
            }
            Location location2 = this.mCoarseLocation;
            if (location2 != null && location2.isMock()) {
                this.mCoarseLocation = null;
            }
            Location location3 = this.mFineBypassLocation;
            if (location3 != null && location3.isMock()) {
                this.mFineBypassLocation = null;
            }
            Location location4 = this.mCoarseBypassLocation;
            if (location4 == null || !location4.isMock()) {
                return;
            }
            this.mCoarseBypassLocation = null;
        }

        public void clearLocations() {
            this.mFineLocation = null;
            this.mCoarseLocation = null;
        }

        public Location get(int i, boolean z) {
            if (i == 1) {
                if (z) {
                    return this.mCoarseBypassLocation;
                }
                return this.mCoarseLocation;
            }
            if (i != 2) {
                throw new AssertionError();
            }
            if (z) {
                return this.mFineBypassLocation;
            }
            return this.mFineLocation;
        }

        public void set(Location location) {
            this.mFineLocation = calculateNextFine(this.mFineLocation, location);
            this.mCoarseLocation = calculateNextCoarse(this.mCoarseLocation, location);
        }

        public void setBypass(Location location) {
            this.mFineBypassLocation = calculateNextFine(this.mFineBypassLocation, location);
            this.mCoarseBypassLocation = calculateNextCoarse(this.mCoarseBypassLocation, location);
        }

        private Location calculateNextFine(Location location, Location location2) {
            return (location != null && location2.getElapsedRealtimeNanos() <= location.getElapsedRealtimeNanos()) ? location : location2;
        }

        private Location calculateNextCoarse(Location location, Location location2) {
            return (location != null && location2.getElapsedRealtimeMillis() - 600000 <= location.getElapsedRealtimeMillis()) ? location : location2;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class PendingIntentSender {
        private PendingIntentSender() {
        }

        public static void send(PendingIntent pendingIntent, Context context, Intent intent, Runnable runnable, Bundle bundle) throws PendingIntent.CanceledException {
            PendingIntent.OnFinished onFinished;
            GatedCallback gatedCallback = null;
            byte b = 0;
            if (runnable != null) {
                final GatedCallback gatedCallback2 = new GatedCallback(runnable);
                onFinished = new PendingIntent.OnFinished() { // from class: com.android.server.location.provider.LocationProviderManager$PendingIntentSender$$ExternalSyntheticLambda0
                    @Override // android.app.PendingIntent.OnFinished
                    public final void onSendFinished(PendingIntent pendingIntent2, Intent intent2, int i, String str, Bundle bundle2) {
                        LocationProviderManager.PendingIntentSender.GatedCallback.this.run();
                    }
                };
                gatedCallback = gatedCallback2;
            } else {
                onFinished = null;
            }
            pendingIntent.send(context, 0, intent, onFinished, null, null, bundle);
            if (gatedCallback != null) {
                gatedCallback.allow();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static class GatedCallback implements Runnable {

            @GuardedBy({"this"})
            private Runnable mCallback;

            @GuardedBy({"this"})
            private boolean mGate;

            @GuardedBy({"this"})
            private boolean mRun;

            private GatedCallback(Runnable runnable) {
                this.mCallback = runnable;
            }

            public void allow() {
                Runnable runnable;
                Runnable runnable2;
                synchronized (this) {
                    this.mGate = true;
                    runnable = null;
                    if (this.mRun && (runnable2 = this.mCallback) != null) {
                        this.mCallback = null;
                        runnable = runnable2;
                    }
                }
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                Runnable runnable;
                Runnable runnable2;
                synchronized (this) {
                    this.mRun = true;
                    runnable = null;
                    if (this.mGate && (runnable2 = this.mCallback) != null) {
                        this.mCallback = null;
                        runnable = runnable2;
                    }
                }
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ExternalWakeLockReleaser extends IRemoteCallback.Stub {
        private final CallerIdentity mIdentity;
        private final PowerManager.WakeLock mWakeLock;

        ExternalWakeLockReleaser(CallerIdentity callerIdentity, PowerManager.WakeLock wakeLock) {
            this.mIdentity = callerIdentity;
            Objects.requireNonNull(wakeLock);
            this.mWakeLock = wakeLock;
        }

        public void sendResult(Bundle bundle) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    this.mWakeLock.release();
                } catch (RuntimeException e) {
                    if (e.getClass() == RuntimeException.class) {
                        Log.e(LocationManagerService.TAG, "wakelock over-released by " + this.mIdentity, e);
                    } else {
                        FgThread.getExecutor().execute(new Runnable() { // from class: com.android.server.location.provider.LocationProviderManager$ExternalWakeLockReleaser$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                LocationProviderManager.ExternalWakeLockReleaser.lambda$sendResult$0(e);
                            }
                        });
                        throw e;
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$sendResult$0(RuntimeException runtimeException) {
            throw new AssertionError(runtimeException);
        }
    }

    public ILocationProviderManagerWrapper getWrapper() {
        return this.mLocationProviderManagerWrapper;
    }

    public static void oplusSystemReady(Context context) {
        if (mOplusLbsClass == null) {
            mOplusLbsClass = (IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, context);
        }
        mLocationFreeze = (ILocationFreezeProc) OplusLbsFactory.getInstance().getFeature(ILocationFreezeProc.DEFAULT, context);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class LocationProviderManagerWrapper implements ILocationProviderManagerWrapper {
        private LocationProviderManagerWrapper() {
        }

        @Override // com.android.server.location.provider.ILocationProviderManagerWrapper
        public void backgroundThrottleIntervalChanged() {
            LocationProviderManager.this.onBackgroundThrottleIntervalChanged();
        }
    }
}
