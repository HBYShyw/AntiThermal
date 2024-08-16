package com.android.server.timezonedetector.location;

import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.Preconditions;
import com.android.server.FgThread;
import com.android.server.SystemService;
import com.android.server.timezonedetector.Dumpable;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import com.android.server.timezonedetector.ServiceConfigAccessorImpl;
import com.android.server.timezonedetector.StateChangeListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Callable;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LocationTimeZoneManagerService extends Binder {
    private static final String ATTRIBUTION_TAG = "LocationTimeZoneService";
    private static final long BLOCKING_OP_WAIT_DURATION_MILLIS = Duration.ofSeconds(20).toMillis();
    static final String TAG = "LocationTZDetector";
    private final Context mContext;
    private final Handler mHandler;

    @GuardedBy({"mSharedLock"})
    private LocationTimeZoneProviderController mLocationTimeZoneProviderController;

    @GuardedBy({"mSharedLock"})
    private LocationTimeZoneProviderControllerEnvironmentImpl mLocationTimeZoneProviderControllerEnvironment;

    @GuardedBy({"mSharedLock"})
    private final ProviderConfig mPrimaryProviderConfig = new ProviderConfig(0, "primary", "android.service.timezone.PrimaryLocationTimeZoneProviderService");

    @GuardedBy({"mSharedLock"})
    private final ProviderConfig mSecondaryProviderConfig = new ProviderConfig(1, "secondary", "android.service.timezone.SecondaryLocationTimeZoneProviderService");
    private final ServiceConfigAccessor mServiceConfigAccessor;
    private final Object mSharedLock;
    private final ThreadingDomain mThreadingDomain;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Lifecycle extends SystemService {
        private LocationTimeZoneManagerService mService;
        private final ServiceConfigAccessor mServiceConfigAccessor;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Lifecycle(Context context) {
            super(context);
            Objects.requireNonNull(context);
            this.mServiceConfigAccessor = ServiceConfigAccessorImpl.getInstance(context);
        }

        public void onStart() {
            Context context = getContext();
            if (this.mServiceConfigAccessor.isGeoTimeZoneDetectionFeatureSupportedInConfig()) {
                LocationTimeZoneManagerService locationTimeZoneManagerService = new LocationTimeZoneManagerService(context, this.mServiceConfigAccessor);
                this.mService = locationTimeZoneManagerService;
                publishBinderService("location_time_zone_manager", locationTimeZoneManagerService);
                return;
            }
            Slog.d(LocationTimeZoneManagerService.TAG, "Geo time zone detection feature is disabled in config");
        }

        public void onBootPhase(int i) {
            if (this.mServiceConfigAccessor.isGeoTimeZoneDetectionFeatureSupportedInConfig()) {
                if (i == 500) {
                    this.mService.onSystemReady();
                } else if (i == 600) {
                    this.mService.onSystemThirdPartyAppsCanStart();
                }
            }
        }
    }

    LocationTimeZoneManagerService(Context context, ServiceConfigAccessor serviceConfigAccessor) {
        this.mContext = context.createAttributionContext(ATTRIBUTION_TAG);
        Handler handler = FgThread.getHandler();
        this.mHandler = handler;
        HandlerThreadingDomain handlerThreadingDomain = new HandlerThreadingDomain(handler);
        this.mThreadingDomain = handlerThreadingDomain;
        this.mSharedLock = handlerThreadingDomain.getLockObject();
        Objects.requireNonNull(serviceConfigAccessor);
        this.mServiceConfigAccessor = serviceConfigAccessor;
    }

    void onSystemReady() {
        this.mServiceConfigAccessor.addLocationTimeZoneManagerConfigListener(new StateChangeListener() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda4
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                LocationTimeZoneManagerService.this.handleServiceConfigurationChangedOnMainThread();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleServiceConfigurationChangedOnMainThread() {
        this.mThreadingDomain.post(new Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                LocationTimeZoneManagerService.this.restartIfRequiredOnDomainThread();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restartIfRequiredOnDomainThread() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            if (this.mLocationTimeZoneProviderController != null) {
                stopOnDomainThread();
                startOnDomainThread();
            }
        }
    }

    void onSystemThirdPartyAppsCanStart() {
        startInternal(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        enforceManageTimeZoneDetectorPermission();
        startInternal(true);
    }

    private void startInternal(boolean z) {
        Runnable runnable = new Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                LocationTimeZoneManagerService.this.startOnDomainThread();
            }
        };
        if (z) {
            this.mThreadingDomain.postAndWait(runnable, BLOCKING_OP_WAIT_DURATION_MILLIS);
        } else {
            this.mThreadingDomain.post(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startWithTestProviders(final String str, final String str2, final boolean z) {
        enforceManageTimeZoneDetectorPermission();
        if (str == null && str2 == null) {
            throw new IllegalArgumentException("One or both test package names must be provided.");
        }
        this.mThreadingDomain.postAndWait(new Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                LocationTimeZoneManagerService.this.lambda$startWithTestProviders$0(str, str2, z);
            }
        }, BLOCKING_OP_WAIT_DURATION_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startWithTestProviders$0(String str, String str2, boolean z) {
        synchronized (this.mSharedLock) {
            stopOnDomainThread();
            this.mServiceConfigAccessor.setTestPrimaryLocationTimeZoneProviderPackageName(str);
            this.mServiceConfigAccessor.setTestSecondaryLocationTimeZoneProviderPackageName(str2);
            this.mServiceConfigAccessor.setRecordStateChangesForTests(z);
            startOnDomainThread();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOnDomainThread() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            if (!this.mServiceConfigAccessor.isGeoTimeZoneDetectionFeatureSupported()) {
                debugLog("Not starting location_time_zone_manager: it is disabled in service config");
                return;
            }
            if (this.mLocationTimeZoneProviderController == null) {
                LocationTimeZoneProvider createProvider = this.mPrimaryProviderConfig.createProvider();
                LocationTimeZoneProvider createProvider2 = this.mSecondaryProviderConfig.createProvider();
                LocationTimeZoneProviderController locationTimeZoneProviderController = new LocationTimeZoneProviderController(this.mThreadingDomain, new RealControllerMetricsLogger(), createProvider, createProvider2, this.mServiceConfigAccessor.getRecordStateChangesForTests());
                LocationTimeZoneProviderControllerEnvironmentImpl locationTimeZoneProviderControllerEnvironmentImpl = new LocationTimeZoneProviderControllerEnvironmentImpl(this.mThreadingDomain, this.mServiceConfigAccessor, locationTimeZoneProviderController);
                locationTimeZoneProviderController.initialize(locationTimeZoneProviderControllerEnvironmentImpl, new LocationTimeZoneProviderControllerCallbackImpl(this.mThreadingDomain));
                this.mLocationTimeZoneProviderControllerEnvironment = locationTimeZoneProviderControllerEnvironmentImpl;
                this.mLocationTimeZoneProviderController = locationTimeZoneProviderController;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() {
        enforceManageTimeZoneDetectorPermission();
        this.mThreadingDomain.postAndWait(new Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                LocationTimeZoneManagerService.this.stopOnDomainThread();
            }
        }, BLOCKING_OP_WAIT_DURATION_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopOnDomainThread() {
        this.mThreadingDomain.assertCurrentThread();
        synchronized (this.mSharedLock) {
            LocationTimeZoneProviderController locationTimeZoneProviderController = this.mLocationTimeZoneProviderController;
            if (locationTimeZoneProviderController != null) {
                locationTimeZoneProviderController.destroy();
                this.mLocationTimeZoneProviderController = null;
                this.mLocationTimeZoneProviderControllerEnvironment.destroy();
                this.mLocationTimeZoneProviderControllerEnvironment = null;
                this.mServiceConfigAccessor.resetVolatileTestConfig();
            }
        }
    }

    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new LocationTimeZoneManagerShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearRecordedProviderStates() {
        enforceManageTimeZoneDetectorPermission();
        this.mThreadingDomain.postAndWait(new Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LocationTimeZoneManagerService.this.lambda$clearRecordedProviderStates$1();
            }
        }, BLOCKING_OP_WAIT_DURATION_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRecordedProviderStates$1() {
        synchronized (this.mSharedLock) {
            LocationTimeZoneProviderController locationTimeZoneProviderController = this.mLocationTimeZoneProviderController;
            if (locationTimeZoneProviderController != null) {
                locationTimeZoneProviderController.clearRecordedStates();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocationTimeZoneManagerServiceState getStateForTests() {
        enforceManageTimeZoneDetectorPermission();
        try {
            return (LocationTimeZoneManagerServiceState) this.mThreadingDomain.postAndWait(new Callable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneManagerService$$ExternalSyntheticLambda3
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    LocationTimeZoneManagerServiceState lambda$getStateForTests$2;
                    lambda$getStateForTests$2 = LocationTimeZoneManagerService.this.lambda$getStateForTests$2();
                    return lambda$getStateForTests$2;
                }
            }, BLOCKING_OP_WAIT_DURATION_MILLIS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ LocationTimeZoneManagerServiceState lambda$getStateForTests$2() throws Exception {
        synchronized (this.mSharedLock) {
            LocationTimeZoneProviderController locationTimeZoneProviderController = this.mLocationTimeZoneProviderController;
            if (locationTimeZoneProviderController == null) {
                return null;
            }
            return locationTimeZoneProviderController.getStateForTests();
        }
    }

    @Override // android.os.Binder
    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
            synchronized (this.mSharedLock) {
                indentingPrintWriter.println("LocationTimeZoneManagerService:");
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println("Primary provider config:");
                indentingPrintWriter.increaseIndent();
                this.mPrimaryProviderConfig.dump(indentingPrintWriter, strArr);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("Secondary provider config:");
                indentingPrintWriter.increaseIndent();
                this.mSecondaryProviderConfig.dump(indentingPrintWriter, strArr);
                indentingPrintWriter.decreaseIndent();
                LocationTimeZoneProviderController locationTimeZoneProviderController = this.mLocationTimeZoneProviderController;
                if (locationTimeZoneProviderController == null) {
                    indentingPrintWriter.println("{Stopped}");
                } else {
                    locationTimeZoneProviderController.dump(indentingPrintWriter, strArr);
                }
                indentingPrintWriter.decreaseIndent();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void debugLog(String str) {
        if (Log.isLoggable(TAG, 3)) {
            Slog.d(TAG, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void infoLog(String str) {
        if (Log.isLoggable(TAG, 4)) {
            Slog.i(TAG, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void warnLog(String str) {
        warnLog(str, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void warnLog(String str, Throwable th) {
        if (Log.isLoggable(TAG, 5)) {
            Slog.w(TAG, str, th);
        }
    }

    private void enforceManageTimeZoneDetectorPermission() {
        this.mContext.enforceCallingPermission("android.permission.MANAGE_TIME_AND_ZONE_DETECTION", "manage time and time zone detection");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ProviderConfig implements Dumpable {
        private final int mIndex;
        private final String mName;
        private final String mServiceAction;

        /* JADX WARN: Code restructure failed: missing block: B:4:0x0008, code lost:
        
            if (r2 <= 1) goto L8;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        ProviderConfig(int i, String str, String str2) {
            boolean z = i >= 0;
            Preconditions.checkArgument(z);
            this.mIndex = i;
            Objects.requireNonNull(str);
            this.mName = str;
            Objects.requireNonNull(str2);
            this.mServiceAction = str2;
        }

        LocationTimeZoneProvider createProvider() {
            RealProviderMetricsLogger realProviderMetricsLogger = new RealProviderMetricsLogger(this.mIndex);
            if (Objects.equals(getMode(), ServiceConfigAccessor.PROVIDER_MODE_DISABLED)) {
                return new DisabledLocationTimeZoneProvider(realProviderMetricsLogger, LocationTimeZoneManagerService.this.mThreadingDomain, this.mName, LocationTimeZoneManagerService.this.mServiceConfigAccessor.getRecordStateChangesForTests());
            }
            return new BinderLocationTimeZoneProvider(realProviderMetricsLogger, LocationTimeZoneManagerService.this.mThreadingDomain, this.mName, createBinderProxy(), LocationTimeZoneManagerService.this.mServiceConfigAccessor.getRecordStateChangesForTests());
        }

        @Override // com.android.server.timezonedetector.Dumpable
        public void dump(IndentingPrintWriter indentingPrintWriter, String[] strArr) {
            indentingPrintWriter.printf("getMode()=%s\n", new Object[]{getMode()});
            indentingPrintWriter.printf("getPackageName()=%s\n", new Object[]{getPackageName()});
        }

        private String getMode() {
            if (this.mIndex == 0) {
                return LocationTimeZoneManagerService.this.mServiceConfigAccessor.getPrimaryLocationTimeZoneProviderMode();
            }
            return LocationTimeZoneManagerService.this.mServiceConfigAccessor.getSecondaryLocationTimeZoneProviderMode();
        }

        private RealLocationTimeZoneProviderProxy createBinderProxy() {
            String str = this.mServiceAction;
            boolean isTestProvider = isTestProvider();
            return new RealLocationTimeZoneProviderProxy(LocationTimeZoneManagerService.this.mContext, LocationTimeZoneManagerService.this.mHandler, LocationTimeZoneManagerService.this.mThreadingDomain, str, getPackageName(), isTestProvider);
        }

        private boolean isTestProvider() {
            if (this.mIndex == 0) {
                return LocationTimeZoneManagerService.this.mServiceConfigAccessor.isTestPrimaryLocationTimeZoneProvider();
            }
            return LocationTimeZoneManagerService.this.mServiceConfigAccessor.isTestSecondaryLocationTimeZoneProvider();
        }

        private String getPackageName() {
            if (this.mIndex == 0) {
                return LocationTimeZoneManagerService.this.mServiceConfigAccessor.getPrimaryLocationTimeZoneProviderPackageName();
            }
            return LocationTimeZoneManagerService.this.mServiceConfigAccessor.getSecondaryLocationTimeZoneProviderPackageName();
        }
    }
}
