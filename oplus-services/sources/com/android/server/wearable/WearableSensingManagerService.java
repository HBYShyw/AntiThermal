package com.android.server.wearable;

import android.R;
import android.app.wearable.IWearableSensingManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManagerInternal;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.ResultReceiver;
import android.os.SharedMemory;
import android.os.ShellCallback;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.FrameworkResourcesServiceNameResolver;
import java.io.FileDescriptor;
import java.util.Objects;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WearableSensingManagerService extends AbstractMasterSystemService<WearableSensingManagerService, WearableSensingManagerPerUserService> {
    private static final boolean DEFAULT_SERVICE_ENABLED = true;
    private static final String KEY_SERVICE_ENABLED = "service_enabled";
    public static final int MAX_TEMPORARY_SERVICE_DURATION_MS = 30000;
    private static final String TAG = "WearableSensingManagerService";
    private final Context mContext;
    volatile boolean mIsServiceEnabled;

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 30000;
    }

    public WearableSensingManagerService(Context context) {
        super(context, new FrameworkResourcesServiceNameResolver(context, R.string.config_signalXPath), null, 68);
        this.mContext = context;
    }

    public void onStart() {
        publishBinderService("wearable_sensing", new WearableSensingManagerInternal());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onBootPhase(int i) {
        if (i == 500) {
            DeviceConfig.addOnPropertiesChangedListener("wearable_sensing", getContext().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.wearable.WearableSensingManagerService$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    WearableSensingManagerService.this.lambda$onBootPhase$0(properties);
                }
            });
            this.mIsServiceEnabled = DeviceConfig.getBoolean("wearable_sensing", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0(DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    private void onDeviceConfigChange(Set<String> set) {
        if (set.contains(KEY_SERVICE_ENABLED)) {
            this.mIsServiceEnabled = DeviceConfig.getBoolean("wearable_sensing", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public WearableSensingManagerPerUserService newServiceLocked(int i, boolean z) {
        return new WearableSensingManagerPerUserService(this, this.mLock, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceRemoved(WearableSensingManagerPerUserService wearableSensingManagerPerUserService, int i) {
        Slog.d(TAG, "onServiceRemoved");
        wearableSensingManagerPerUserService.destroyLocked();
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageRestartedLocked(int i) {
        Slog.d(TAG, "onServicePackageRestartedLocked.");
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int i) {
        Slog.d(TAG, "onServicePackageUpdatedLocked.");
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", TAG);
    }

    public static boolean isDetectionServiceConfigured() {
        boolean z = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getKnownPackageNames(19, 0).length != 0;
        Slog.i(TAG, "Wearable sensing service configured: " + z);
        return z;
    }

    public ComponentName getComponentName(int i) {
        synchronized (this.mLock) {
            WearableSensingManagerPerUserService serviceForUserLocked = getServiceForUserLocked(i);
            if (serviceForUserLocked == null) {
                return null;
            }
            return serviceForUserLocked.getComponentName();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void provideDataStream(int i, ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) {
        synchronized (this.mLock) {
            WearableSensingManagerPerUserService serviceForUserLocked = getServiceForUserLocked(i);
            if (serviceForUserLocked != null) {
                serviceForUserLocked.onProvideDataStream(parcelFileDescriptor, remoteCallback);
            } else {
                Slog.w(TAG, "Service not available.");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void provideData(int i, PersistableBundle persistableBundle, SharedMemory sharedMemory, RemoteCallback remoteCallback) {
        synchronized (this.mLock) {
            WearableSensingManagerPerUserService serviceForUserLocked = getServiceForUserLocked(i);
            if (serviceForUserLocked != null) {
                serviceForUserLocked.onProvidedData(persistableBundle, sharedMemory, remoteCallback);
            } else {
                Slog.w(TAG, "Service not available.");
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class WearableSensingManagerInternal extends IWearableSensingManager.Stub {
        final WearableSensingManagerPerUserService mService;

        private WearableSensingManagerInternal() {
            this.mService = (WearableSensingManagerPerUserService) WearableSensingManagerService.this.getServiceForUserLocked(UserHandle.getCallingUserId());
        }

        public void provideDataStream(ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) {
            Slog.i(WearableSensingManagerService.TAG, "WearableSensingManagerInternal provideDataStream.");
            Objects.requireNonNull(parcelFileDescriptor);
            Objects.requireNonNull(remoteCallback);
            WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", WearableSensingManagerService.TAG);
            if (!WearableSensingManagerService.this.mIsServiceEnabled) {
                Slog.w(WearableSensingManagerService.TAG, "Service not available.");
                WearableSensingManagerPerUserService.notifyStatusCallback(remoteCallback, 3);
            } else {
                this.mService.onProvideDataStream(parcelFileDescriptor, remoteCallback);
            }
        }

        public void provideData(PersistableBundle persistableBundle, SharedMemory sharedMemory, RemoteCallback remoteCallback) {
            Slog.i(WearableSensingManagerService.TAG, "WearableSensingManagerInternal provideData.");
            Objects.requireNonNull(persistableBundle);
            Objects.requireNonNull(remoteCallback);
            WearableSensingManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_WEARABLE_SENSING_SERVICE", WearableSensingManagerService.TAG);
            if (!WearableSensingManagerService.this.mIsServiceEnabled) {
                Slog.w(WearableSensingManagerService.TAG, "Service not available.");
                WearableSensingManagerPerUserService.notifyStatusCallback(remoteCallback, 3);
            } else {
                this.mService.onProvidedData(persistableBundle, sharedMemory, remoteCallback);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new WearableSensingShellCommand(WearableSensingManagerService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }
    }
}
