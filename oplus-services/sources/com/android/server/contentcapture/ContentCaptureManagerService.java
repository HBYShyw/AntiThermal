package com.android.server.contentcapture;

import android.R;
import android.app.ActivityManagerInternal;
import android.app.ActivityThread;
import android.app.assist.ActivityId;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityPresentationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.service.contentcapture.ContentCaptureService;
import android.service.contentcapture.IDataShareCallback;
import android.service.contentcapture.IDataShareReadAdapter;
import android.service.voice.VoiceInteractionManagerInternal;
import android.util.ArraySet;
import android.util.LocalLog;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.contentcapture.ContentCaptureHelper;
import android.view.contentcapture.DataRemovalRequest;
import android.view.contentcapture.DataShareRequest;
import android.view.contentcapture.IContentCaptureManager;
import android.view.contentcapture.IContentCaptureOptionsCallback;
import android.view.contentcapture.IDataShareWriteAdapter;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.GlobalWhitelistState;
import com.android.internal.os.IResultReceiver;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.SyncResultReceiver;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.contentcapture.ContentCaptureManagerService;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.FrameworkResourcesServiceNameResolver;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ContentCaptureManagerService extends AbstractMasterSystemService<ContentCaptureManagerService, ContentCapturePerUserService> {
    private static final int DATA_SHARE_BYTE_BUFFER_LENGTH = 1024;
    private static final int EVENT__DATA_SHARE_ERROR_CONCURRENT_REQUEST = 14;
    private static final int EVENT__DATA_SHARE_ERROR_TIMEOUT_INTERRUPTED = 15;
    private static final int EVENT__DATA_SHARE_WRITE_FINISHED = 9;
    private static final int MAX_CONCURRENT_FILE_SHARING_REQUESTS = 10;
    private static final int MAX_DATA_SHARE_FILE_DESCRIPTORS_TTL_MS = 300000;
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    static final String RECEIVER_BUNDLE_EXTRA_SESSIONS = "sessions";
    private static final String TAG = "ContentCaptureManagerService";

    @GuardedBy({"mLock"})
    private ActivityManagerInternal mAm;
    private final RemoteCallbackList<IContentCaptureOptionsCallback> mCallbacks;
    private final ContentCaptureManagerServiceStub mContentCaptureManagerServiceStub;
    private final Executor mDataShareExecutor;

    @GuardedBy({"mLock"})
    boolean mDevCfgDisableFlushForViewTreeAppearing;

    @GuardedBy({"mLock"})
    int mDevCfgIdleFlushingFrequencyMs;

    @GuardedBy({"mLock"})
    int mDevCfgIdleUnbindTimeoutMs;

    @GuardedBy({"mLock"})
    int mDevCfgLogHistorySize;

    @GuardedBy({"mLock"})
    int mDevCfgLoggingLevel;

    @GuardedBy({"mLock"})
    int mDevCfgMaxBufferSize;

    @GuardedBy({"mLock"})
    int mDevCfgTextChangeFlushingFrequencyMs;

    @GuardedBy({"mLock"})
    private boolean mDisabledByDeviceConfig;

    @GuardedBy({"mLock"})
    private SparseBooleanArray mDisabledBySettings;
    final GlobalContentCaptureOptions mGlobalContentCaptureOptions;
    private final Handler mHandler;
    private final LocalService mLocalService;

    @GuardedBy({"mLock"})
    private final Set<String> mPackagesWithShareRequests;
    final LocalLog mRequestsHistory;

    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    public ContentCaptureManagerService(Context context) {
        super(context, new FrameworkResourcesServiceNameResolver(context, R.string.config_mms_user_agent_profile_url), "no_content_capture", 1);
        this.mLocalService = new LocalService();
        this.mContentCaptureManagerServiceStub = new ContentCaptureManagerServiceStub();
        this.mDataShareExecutor = Executors.newCachedThreadPool();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mPackagesWithShareRequests = new HashSet();
        this.mCallbacks = new RemoteCallbackList<>();
        this.mGlobalContentCaptureOptions = new GlobalContentCaptureOptions();
        DeviceConfig.addOnPropertiesChangedListener("content_capture", ActivityThread.currentApplication().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                ContentCaptureManagerService.this.lambda$new$0(properties);
            }
        });
        setDeviceConfigProperties();
        if (this.mDevCfgLogHistorySize > 0) {
            if (((AbstractMasterSystemService) this).debug) {
                Slog.d(TAG, "log history size: " + this.mDevCfgLogHistorySize);
            }
            this.mRequestsHistory = new LocalLog(this.mDevCfgLogHistorySize);
        } else {
            if (((AbstractMasterSystemService) this).debug) {
                Slog.d(TAG, "disabled log history because size is " + this.mDevCfgLogHistorySize);
            }
            this.mRequestsHistory = null;
        }
        List supportedUsers = getSupportedUsers();
        for (int i = 0; i < supportedUsers.size(); i++) {
            int i2 = ((UserInfo) supportedUsers.get(i)).id;
            if (!isEnabledBySettings(i2)) {
                Slog.i(TAG, "user " + i2 + " disabled by settings");
                if (this.mDisabledBySettings == null) {
                    this.mDisabledBySettings = new SparseBooleanArray(1);
                }
                this.mDisabledBySettings.put(i2, true);
            }
            this.mGlobalContentCaptureOptions.setServiceInfo(i2, ((AbstractMasterSystemService) this).mServiceNameResolver.getServiceName(i2), ((AbstractMasterSystemService) this).mServiceNameResolver.isTemporary(i2));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: newServiceLocked, reason: merged with bridge method [inline-methods] */
    public ContentCapturePerUserService m3096newServiceLocked(int i, boolean z) {
        return new ContentCapturePerUserService(this, ((AbstractMasterSystemService) this).mLock, z, i);
    }

    public boolean isUserSupported(SystemService.TargetUser targetUser) {
        return targetUser.isFull() || targetUser.isProfile();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onStart() {
        publishBinderService("content_capture", this.mContentCaptureManagerServiceStub);
        publishLocalService(ContentCaptureManagerInternal.class, this.mLocalService);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceRemoved(ContentCapturePerUserService contentCapturePerUserService, int i) {
        contentCapturePerUserService.destroyLocked();
    }

    protected void onServicePackageUpdatingLocked(int i) {
        ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) getServiceForUserLocked(i);
        if (contentCapturePerUserService != null) {
            contentCapturePerUserService.onPackageUpdatingLocked();
        }
    }

    protected void onServicePackageUpdatedLocked(int i) {
        ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) getServiceForUserLocked(i);
        if (contentCapturePerUserService != null) {
            contentCapturePerUserService.onPackageUpdatedLocked();
        }
    }

    protected void onServiceNameChanged(int i, String str, boolean z) {
        this.mGlobalContentCaptureOptions.setServiceInfo(i, str, z);
        super.onServiceNameChanged(i, str, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_CONTENT_CAPTURE", TAG);
    }

    protected void registerForExtraSettingsChanges(ContentResolver contentResolver, ContentObserver contentObserver) {
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("content_capture_enabled"), false, contentObserver, -1);
    }

    protected void onSettingsChanged(int i, String str) {
        str.hashCode();
        if (str.equals("content_capture_enabled")) {
            setContentCaptureFeatureEnabledBySettingsForUser(i, isEnabledBySettings(i));
            return;
        }
        Slog.w(TAG, "Unexpected property (" + str + "); updating cache instead");
    }

    protected boolean isDisabledLocked(int i) {
        return this.mDisabledByDeviceConfig || isDisabledBySettingsLocked(i) || super.isDisabledLocked(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void assertCalledByPackageOwner(String str) {
        try {
            super.assertCalledByPackageOwner(str);
        } catch (SecurityException e) {
            int callingUid = Binder.getCallingUid();
            VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity hotwordDetectionServiceIdentity = ((VoiceInteractionManagerInternal) LocalServices.getService(VoiceInteractionManagerInternal.class)).getHotwordDetectionServiceIdentity();
            if (callingUid != hotwordDetectionServiceIdentity.getIsolatedUid()) {
                super.assertCalledByPackageOwner(str);
                return;
            }
            String[] packagesForUid = getContext().getPackageManager().getPackagesForUid(hotwordDetectionServiceIdentity.getOwnerUid());
            if (packagesForUid != null) {
                for (String str2 : packagesForUid) {
                    if (str.equals(str2)) {
                        return;
                    }
                }
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDisabledBySettingsLocked(int i) {
        SparseBooleanArray sparseBooleanArray = this.mDisabledBySettings;
        return sparseBooleanArray != null && sparseBooleanArray.get(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isEnabledBySettings(int i) {
        return Settings.Secure.getIntForUser(getContext().getContentResolver(), "content_capture_enabled", 1, i) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0082 A[LOOP:0: B:2:0x0008->B:31:0x0082, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x009a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00a7 A[SYNTHETIC] */
    /* renamed from: onDeviceConfigChange, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$new$0(DeviceConfig.Properties properties) {
        for (String str : properties.getKeyset()) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2119665698:
                    if (str.equals("disable_flush_for_view_tree_appearing")) {
                        c = 0;
                    }
                    switch (c) {
                        case 0:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            setFineTuneParamsFromDeviceConfig();
                            return;
                        case 1:
                            setLoggingLevelFromDeviceConfig();
                            return;
                        case 2:
                            setDisabledByDeviceConfig(properties.getString(str, (String) null));
                            return;
                        default:
                            Slog.i(TAG, "Ignoring change on " + str);
                    }
                case -1970239836:
                    if (str.equals("logging_level")) {
                        c = 1;
                    }
                    switch (c) {
                    }
                case -302650995:
                    if (str.equals("service_explicitly_enabled")) {
                        c = 2;
                    }
                    switch (c) {
                    }
                case -148969820:
                    if (str.equals("text_change_flush_frequency")) {
                        c = 3;
                    }
                    switch (c) {
                    }
                case 227845607:
                    if (str.equals("log_history_size")) {
                        c = 4;
                    }
                    switch (c) {
                    }
                case 1119140421:
                    if (str.equals("max_buffer_size")) {
                        c = 5;
                    }
                    switch (c) {
                    }
                case 1568835651:
                    if (str.equals("idle_unbind_timeout")) {
                        c = 6;
                    }
                    switch (c) {
                    }
                case 2068460406:
                    if (str.equals("idle_flush_frequency")) {
                        c = 7;
                    }
                    switch (c) {
                    }
                default:
                    switch (c) {
                    }
            }
        }
    }

    private void setFineTuneParamsFromDeviceConfig() {
        synchronized (((AbstractMasterSystemService) this).mLock) {
            this.mDevCfgMaxBufferSize = DeviceConfig.getInt("content_capture", "max_buffer_size", 500);
            this.mDevCfgIdleFlushingFrequencyMs = DeviceConfig.getInt("content_capture", "idle_flush_frequency", 5000);
            this.mDevCfgTextChangeFlushingFrequencyMs = DeviceConfig.getInt("content_capture", "text_change_flush_frequency", 1000);
            this.mDevCfgLogHistorySize = DeviceConfig.getInt("content_capture", "log_history_size", 20);
            this.mDevCfgIdleUnbindTimeoutMs = DeviceConfig.getInt("content_capture", "idle_unbind_timeout", 0);
            this.mDevCfgDisableFlushForViewTreeAppearing = DeviceConfig.getBoolean("content_capture", "disable_flush_for_view_tree_appearing", false);
            if (((AbstractMasterSystemService) this).verbose) {
                Slog.v(TAG, "setFineTuneParamsFromDeviceConfig(): bufferSize=" + this.mDevCfgMaxBufferSize + ", idleFlush=" + this.mDevCfgIdleFlushingFrequencyMs + ", textFluxh=" + this.mDevCfgTextChangeFlushingFrequencyMs + ", logHistory=" + this.mDevCfgLogHistorySize + ", idleUnbindTimeoutMs=" + this.mDevCfgIdleUnbindTimeoutMs + ", disableFlushForViewTreeAppearing=" + this.mDevCfgDisableFlushForViewTreeAppearing);
            }
        }
    }

    private void setLoggingLevelFromDeviceConfig() {
        int i = DeviceConfig.getInt("content_capture", "logging_level", ContentCaptureHelper.getDefaultLoggingLevel());
        this.mDevCfgLoggingLevel = i;
        ContentCaptureHelper.setLoggingLevel(i);
        boolean z = ContentCaptureHelper.sVerbose;
        ((AbstractMasterSystemService) this).verbose = z;
        ((AbstractMasterSystemService) this).debug = ContentCaptureHelper.sDebug;
        if (z) {
            Slog.v(TAG, "setLoggingLevelFromDeviceConfig(): level=" + this.mDevCfgLoggingLevel + ", debug=" + ((AbstractMasterSystemService) this).debug + ", verbose=" + ((AbstractMasterSystemService) this).verbose);
        }
    }

    private void setDeviceConfigProperties() {
        setLoggingLevelFromDeviceConfig();
        setFineTuneParamsFromDeviceConfig();
        setDisabledByDeviceConfig(DeviceConfig.getProperty("content_capture", "service_explicitly_enabled"));
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setDisabledByDeviceConfig(String str) {
        boolean z;
        if (((AbstractMasterSystemService) this).verbose) {
            Slog.v(TAG, "setDisabledByDeviceConfig(): explicitlyEnabled=" + str);
        }
        List supportedUsers = getSupportedUsers();
        boolean z2 = str != null && str.equalsIgnoreCase("false");
        synchronized (((AbstractMasterSystemService) this).mLock) {
            if (this.mDisabledByDeviceConfig == z2) {
                if (((AbstractMasterSystemService) this).verbose) {
                    Slog.v(TAG, "setDisabledByDeviceConfig(): already " + z2);
                }
                return;
            }
            this.mDisabledByDeviceConfig = z2;
            Slog.i(TAG, "setDisabledByDeviceConfig(): set to " + this.mDisabledByDeviceConfig);
            for (int i = 0; i < supportedUsers.size(); i++) {
                int i2 = ((UserInfo) supportedUsers.get(i)).id;
                if (!this.mDisabledByDeviceConfig && !isDisabledBySettingsLocked(i2)) {
                    z = false;
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("setDisabledByDeviceConfig(): updating service for user ");
                    sb.append(i2);
                    sb.append(" to ");
                    sb.append(!z ? "'disabled'" : "'enabled'");
                    Slog.i(str2, sb.toString());
                    updateCachedServiceLocked(i2, z);
                }
                z = true;
                String str22 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setDisabledByDeviceConfig(): updating service for user ");
                sb2.append(i2);
                sb2.append(" to ");
                sb2.append(!z ? "'disabled'" : "'enabled'");
                Slog.i(str22, sb2.toString());
                updateCachedServiceLocked(i2, z);
            }
        }
    }

    private void setContentCaptureFeatureEnabledBySettingsForUser(int i, boolean z) {
        synchronized (((AbstractMasterSystemService) this).mLock) {
            if (this.mDisabledBySettings == null) {
                this.mDisabledBySettings = new SparseBooleanArray();
            }
            boolean z2 = true;
            if (!((!this.mDisabledBySettings.get(i)) ^ z)) {
                if (((AbstractMasterSystemService) this).debug) {
                    Slog.d(TAG, "setContentCaptureFeatureEnabledForUser(): already " + z);
                }
                return;
            }
            if (z) {
                Slog.i(TAG, "setContentCaptureFeatureEnabled(): enabling service for user " + i);
                this.mDisabledBySettings.delete(i);
            } else {
                Slog.i(TAG, "setContentCaptureFeatureEnabled(): disabling service for user " + i);
                this.mDisabledBySettings.put(i, true);
            }
            if (z && !this.mDisabledByDeviceConfig) {
                z2 = false;
            }
            updateCachedServiceLocked(i, z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroySessions(int i, IResultReceiver iResultReceiver) {
        Slog.i(TAG, "destroySessions() for userId " + i);
        enforceCallingPermissionForManagement();
        synchronized (((AbstractMasterSystemService) this).mLock) {
            if (i != -1) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) peekServiceForUserLocked(i);
                if (contentCapturePerUserService != null) {
                    contentCapturePerUserService.destroySessionsLocked();
                }
            } else {
                visitServicesLocked(new AbstractMasterSystemService.Visitor() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda0
                    public final void visit(Object obj) {
                        ((ContentCapturePerUserService) obj).destroySessionsLocked();
                    }
                });
            }
        }
        try {
            iResultReceiver.send(0, new Bundle());
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void listSessions(int i, IResultReceiver iResultReceiver) {
        Slog.i(TAG, "listSessions() for userId " + i);
        enforceCallingPermissionForManagement();
        Bundle bundle = new Bundle();
        final ArrayList<String> arrayList = new ArrayList<>();
        synchronized (((AbstractMasterSystemService) this).mLock) {
            if (i != -1) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) peekServiceForUserLocked(i);
                if (contentCapturePerUserService != null) {
                    contentCapturePerUserService.listSessionsLocked(arrayList);
                }
            } else {
                visitServicesLocked(new AbstractMasterSystemService.Visitor() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda2
                    public final void visit(Object obj) {
                        ((ContentCapturePerUserService) obj).listSessionsLocked(arrayList);
                    }
                });
            }
        }
        bundle.putStringArrayList(RECEIVER_BUNDLE_EXTRA_SESSIONS, arrayList);
        try {
            iResultReceiver.send(0, bundle);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateOptions(final String str, final ContentCaptureOptions contentCaptureOptions) {
        this.mCallbacks.broadcast(new BiConsumer() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ContentCaptureManagerService.lambda$updateOptions$3(str, contentCaptureOptions, (IContentCaptureOptionsCallback) obj, obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateOptions$3(String str, ContentCaptureOptions contentCaptureOptions, IContentCaptureOptionsCallback iContentCaptureOptionsCallback, Object obj) {
        if (obj.equals(str)) {
            try {
                iContentCaptureOptionsCallback.setContentCaptureOptions(contentCaptureOptions);
            } catch (RemoteException e) {
                Slog.w(TAG, "Unable to send setContentCaptureOptions(): " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ActivityManagerInternal getAmInternal() {
        synchronized (((AbstractMasterSystemService) this).mLock) {
            if (this.mAm == null) {
                this.mAm = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
            }
        }
        return this.mAm;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void assertCalledByServiceLocked(String str) {
        if (!isCalledByServiceLocked(str)) {
            throw new SecurityException("caller is not user's ContentCapture service");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    @GuardedBy({"mLock"})
    public boolean isCalledByServiceLocked(String str) {
        int callingUserId = UserHandle.getCallingUserId();
        int callingUid = Binder.getCallingUid();
        String serviceName = ((AbstractMasterSystemService) this).mServiceNameResolver.getServiceName(callingUserId);
        if (serviceName == null) {
            Slog.e(TAG, str + ": called by UID " + callingUid + ", but there's no service set for user " + callingUserId);
            return false;
        }
        ComponentName unflattenFromString = ComponentName.unflattenFromString(serviceName);
        if (unflattenFromString == null) {
            Slog.w(TAG, str + ": invalid service name: " + serviceName);
            return false;
        }
        try {
            int packageUidAsUser = getContext().getPackageManager().getPackageUidAsUser(unflattenFromString.getPackageName(), UserHandle.getCallingUserId());
            if (callingUid == packageUidAsUser) {
                return true;
            }
            Slog.e(TAG, str + ": called by UID " + callingUid + ", but service UID is " + packageUidAsUser);
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.w(TAG, str + ": could not verify UID for " + serviceName);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean throwsSecurityException(IResultReceiver iResultReceiver, Runnable runnable) {
        try {
            runnable.run();
            return false;
        } catch (SecurityException e) {
            try {
                iResultReceiver.send(-1, SyncResultReceiver.bundleFor(e.getMessage()));
                return true;
            } catch (RemoteException e2) {
                Slog.w(TAG, "Unable to send security exception (" + e + "): ", e2);
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean isDefaultServiceLocked(int i) {
        String defaultServiceName = ((AbstractMasterSystemService) this).mServiceNameResolver.getDefaultServiceName(i);
        if (defaultServiceName == null) {
            return false;
        }
        return defaultServiceName.equals(((AbstractMasterSystemService) this).mServiceNameResolver.getServiceName(i));
    }

    @GuardedBy({"mLock"})
    protected void dumpLocked(String str, PrintWriter printWriter) {
        super.dumpLocked(str, printWriter);
        String str2 = str + "  ";
        printWriter.print(str);
        printWriter.print("Users disabled by Settings: ");
        printWriter.println(this.mDisabledBySettings);
        printWriter.print(str);
        printWriter.println("DeviceConfig Settings: ");
        printWriter.print(str2);
        printWriter.print("disabled: ");
        printWriter.println(this.mDisabledByDeviceConfig);
        printWriter.print(str2);
        printWriter.print("loggingLevel: ");
        printWriter.println(this.mDevCfgLoggingLevel);
        printWriter.print(str2);
        printWriter.print("maxBufferSize: ");
        printWriter.println(this.mDevCfgMaxBufferSize);
        printWriter.print(str2);
        printWriter.print("idleFlushingFrequencyMs: ");
        printWriter.println(this.mDevCfgIdleFlushingFrequencyMs);
        printWriter.print(str2);
        printWriter.print("textChangeFlushingFrequencyMs: ");
        printWriter.println(this.mDevCfgTextChangeFlushingFrequencyMs);
        printWriter.print(str2);
        printWriter.print("logHistorySize: ");
        printWriter.println(this.mDevCfgLogHistorySize);
        printWriter.print(str2);
        printWriter.print("idleUnbindTimeoutMs: ");
        printWriter.println(this.mDevCfgIdleUnbindTimeoutMs);
        printWriter.print(str2);
        printWriter.print("disableFlushForViewTreeAppearing: ");
        printWriter.println(this.mDevCfgDisableFlushForViewTreeAppearing);
        printWriter.print(str);
        printWriter.println("Global Options:");
        this.mGlobalContentCaptureOptions.dump(str2, printWriter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ContentCaptureManagerServiceStub extends IContentCaptureManager.Stub {
        ContentCaptureManagerServiceStub() {
        }

        public void startSession(IBinder iBinder, IBinder iBinder2, ComponentName componentName, int i, int i2, IResultReceiver iResultReceiver) {
            Objects.requireNonNull(iBinder);
            Objects.requireNonNull(iBinder2);
            int callingUserId = UserHandle.getCallingUserId();
            ActivityPresentationInfo activityPresentationInfo = ContentCaptureManagerService.this.getAmInternal().getActivityPresentationInfo(iBinder);
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) ContentCaptureManagerService.this.getServiceForUserLocked(callingUserId);
                if (!ContentCaptureManagerService.this.isDefaultServiceLocked(callingUserId) && !ContentCaptureManagerService.this.isCalledByServiceLocked("startSession()")) {
                    ContentCaptureService.setClientState(iResultReceiver, 4, (IBinder) null);
                } else {
                    contentCapturePerUserService.startSessionLocked(iBinder, iBinder2, activityPresentationInfo, i, Binder.getCallingUid(), i2, iResultReceiver);
                }
            }
        }

        public void finishSession(int i) {
            int callingUserId = UserHandle.getCallingUserId();
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ((ContentCapturePerUserService) ContentCaptureManagerService.this.getServiceForUserLocked(callingUserId)).finishSessionLocked(i);
            }
        }

        public void getServiceComponentName(IResultReceiver iResultReceiver) {
            ComponentName serviceComponentName;
            int callingUserId = UserHandle.getCallingUserId();
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                serviceComponentName = ((ContentCapturePerUserService) ContentCaptureManagerService.this.getServiceForUserLocked(callingUserId)).getServiceComponentName();
            }
            try {
                iResultReceiver.send(0, SyncResultReceiver.bundleFor(serviceComponentName));
            } catch (RemoteException e) {
                Slog.w(ContentCaptureManagerService.TAG, "Unable to send service component name: " + e);
            }
        }

        public void removeData(DataRemovalRequest dataRemovalRequest) {
            Objects.requireNonNull(dataRemovalRequest);
            ContentCaptureManagerService.this.assertCalledByPackageOwner(dataRemovalRequest.getPackageName());
            int callingUserId = UserHandle.getCallingUserId();
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ((ContentCapturePerUserService) ContentCaptureManagerService.this.getServiceForUserLocked(callingUserId)).removeDataLocked(dataRemovalRequest);
            }
        }

        public void shareData(DataShareRequest dataShareRequest, IDataShareWriteAdapter iDataShareWriteAdapter) {
            Objects.requireNonNull(dataShareRequest);
            Objects.requireNonNull(iDataShareWriteAdapter);
            ContentCaptureManagerService.this.assertCalledByPackageOwner(dataShareRequest.getPackageName());
            int callingUserId = UserHandle.getCallingUserId();
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) ContentCaptureManagerService.this.getServiceForUserLocked(callingUserId);
                if (ContentCaptureManagerService.this.mPackagesWithShareRequests.size() < 10 && !ContentCaptureManagerService.this.mPackagesWithShareRequests.contains(dataShareRequest.getPackageName())) {
                    contentCapturePerUserService.onDataSharedLocked(dataShareRequest, new DataShareCallbackDelegate(dataShareRequest, iDataShareWriteAdapter, ContentCaptureManagerService.this));
                    return;
                }
                try {
                    ContentCaptureMetricsLogger.writeServiceEvent(14, ((AbstractMasterSystemService) ContentCaptureManagerService.this).mServiceNameResolver.getServiceName(callingUserId));
                    iDataShareWriteAdapter.error(2);
                } catch (RemoteException unused) {
                    Slog.e(ContentCaptureManagerService.TAG, "Failed to send error message to client");
                }
            }
        }

        public void isContentCaptureFeatureEnabled(IResultReceiver iResultReceiver) {
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                if (ContentCaptureManagerService.this.throwsSecurityException(iResultReceiver, new Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$ContentCaptureManagerServiceStub$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContentCaptureManagerService.ContentCaptureManagerServiceStub.this.lambda$isContentCaptureFeatureEnabled$0();
                    }
                })) {
                    return;
                }
                try {
                    iResultReceiver.send(!ContentCaptureManagerService.this.mDisabledByDeviceConfig && !ContentCaptureManagerService.this.isDisabledBySettingsLocked(UserHandle.getCallingUserId()) ? 1 : 2, (Bundle) null);
                } catch (RemoteException e) {
                    Slog.w(ContentCaptureManagerService.TAG, "Unable to send isContentCaptureFeatureEnabled(): " + e);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$isContentCaptureFeatureEnabled$0() {
            ContentCaptureManagerService.this.assertCalledByServiceLocked("isContentCaptureFeatureEnabled()");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getServiceSettingsActivity$1() {
            ContentCaptureManagerService.this.enforceCallingPermissionForManagement();
        }

        public void getServiceSettingsActivity(IResultReceiver iResultReceiver) {
            if (ContentCaptureManagerService.this.throwsSecurityException(iResultReceiver, new Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$ContentCaptureManagerServiceStub$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ContentCaptureManagerService.ContentCaptureManagerServiceStub.this.lambda$getServiceSettingsActivity$1();
                }
            })) {
                return;
            }
            int callingUserId = UserHandle.getCallingUserId();
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) ContentCaptureManagerService.this.getServiceForUserLocked(callingUserId);
                if (contentCapturePerUserService == null) {
                    return;
                }
                ComponentName serviceSettingsActivityLocked = contentCapturePerUserService.getServiceSettingsActivityLocked();
                try {
                    iResultReceiver.send(0, SyncResultReceiver.bundleFor(serviceSettingsActivityLocked));
                } catch (RemoteException e) {
                    Slog.w(ContentCaptureManagerService.TAG, "Unable to send getServiceSettingsIntent(): " + e);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getContentCaptureConditions$2(String str) {
            ContentCaptureManagerService.this.assertCalledByPackageOwner(str);
        }

        public void getContentCaptureConditions(final String str, IResultReceiver iResultReceiver) {
            ArrayList list;
            if (ContentCaptureManagerService.this.throwsSecurityException(iResultReceiver, new Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$ContentCaptureManagerServiceStub$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ContentCaptureManagerService.ContentCaptureManagerServiceStub.this.lambda$getContentCaptureConditions$2(str);
                }
            })) {
                return;
            }
            int callingUserId = UserHandle.getCallingUserId();
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) ContentCaptureManagerService.this.getServiceForUserLocked(callingUserId);
                list = contentCapturePerUserService == null ? null : ContentCaptureHelper.toList(contentCapturePerUserService.getContentCaptureConditionsLocked(str));
            }
            try {
                iResultReceiver.send(0, SyncResultReceiver.bundleFor(list));
            } catch (RemoteException e) {
                Slog.w(ContentCaptureManagerService.TAG, "Unable to send getServiceComponentName(): " + e);
            }
        }

        public void registerContentCaptureOptionsCallback(String str, IContentCaptureOptionsCallback iContentCaptureOptionsCallback) {
            ContentCaptureManagerService.this.assertCalledByPackageOwner(str);
            ContentCaptureManagerService.this.mCallbacks.register(iContentCaptureOptionsCallback, str);
            ContentCaptureOptions options = ContentCaptureManagerService.this.mGlobalContentCaptureOptions.getOptions(UserHandle.getCallingUserId(), str);
            if (options != null) {
                try {
                    iContentCaptureOptionsCallback.setContentCaptureOptions(options);
                } catch (RemoteException e) {
                    Slog.w(ContentCaptureManagerService.TAG, "Unable to send setContentCaptureOptions(): " + e);
                }
            }
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(ContentCaptureManagerService.this.getContext(), ContentCaptureManagerService.TAG, printWriter)) {
                boolean z = true;
                if (strArr != null) {
                    for (String str : strArr) {
                        str.hashCode();
                        if (str.equals("--no-history")) {
                            z = false;
                        } else {
                            if (str.equals("--help")) {
                                printWriter.println("Usage: dumpsys content_capture [--no-history]");
                                return;
                            }
                            Slog.w(ContentCaptureManagerService.TAG, "Ignoring invalid dump arg: " + str);
                        }
                    }
                }
                synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                    ContentCaptureManagerService.this.dumpLocked("", printWriter);
                }
                printWriter.print("Requests history: ");
                if (ContentCaptureManagerService.this.mRequestsHistory == null) {
                    printWriter.println("disabled by device config");
                } else {
                    if (z) {
                        printWriter.println();
                        ContentCaptureManagerService.this.mRequestsHistory.reverseDump(fileDescriptor, printWriter, strArr);
                        printWriter.println();
                        return;
                    }
                    printWriter.println();
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) throws RemoteException {
            new ContentCaptureManagerServiceShellCommand(ContentCaptureManagerService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        public void resetTemporaryService(int i) {
            ContentCaptureManagerService.this.resetTemporaryService(i);
        }

        public void setTemporaryService(int i, String str, int i2) {
            ContentCaptureManagerService.this.setTemporaryService(i, str, i2);
        }

        public void setDefaultServiceEnabled(int i, boolean z) {
            ContentCaptureManagerService.this.setDefaultServiceEnabled(i, z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class LocalService extends ContentCaptureManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public boolean isContentCaptureServiceForUser(int i, int i2) {
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) ContentCaptureManagerService.this.peekServiceForUserLocked(i2);
                if (contentCapturePerUserService == null) {
                    return false;
                }
                return contentCapturePerUserService.isContentCaptureServiceForUserLocked(i);
            }
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public boolean sendActivityAssistData(int i, IBinder iBinder, Bundle bundle) {
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) ContentCaptureManagerService.this.peekServiceForUserLocked(i);
                if (contentCapturePerUserService == null) {
                    return false;
                }
                return contentCapturePerUserService.sendActivityAssistDataLocked(iBinder, bundle);
            }
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public ContentCaptureOptions getOptionsForPackage(int i, String str) {
            return ContentCaptureManagerService.this.mGlobalContentCaptureOptions.getOptions(i, str);
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public void notifyActivityEvent(int i, ComponentName componentName, int i2, ActivityId activityId) {
            synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                ContentCapturePerUserService contentCapturePerUserService = (ContentCapturePerUserService) ContentCaptureManagerService.this.peekServiceForUserLocked(i);
                if (contentCapturePerUserService != null) {
                    contentCapturePerUserService.onActivityEventLocked(activityId, componentName, i2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class GlobalContentCaptureOptions extends GlobalWhitelistState {

        @GuardedBy({"mGlobalWhitelistStateLock"})
        private final SparseArray<String> mServicePackages = new SparseArray<>();

        @GuardedBy({"mGlobalWhitelistStateLock"})
        private final SparseBooleanArray mTemporaryServices = new SparseBooleanArray();

        GlobalContentCaptureOptions() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setServiceInfo(int i, String str, boolean z) {
            synchronized (((GlobalWhitelistState) this).mGlobalWhitelistStateLock) {
                if (z) {
                    this.mTemporaryServices.put(i, true);
                } else {
                    this.mTemporaryServices.delete(i);
                }
                if (str != null) {
                    ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
                    if (unflattenFromString == null) {
                        Slog.w(ContentCaptureManagerService.TAG, "setServiceInfo(): invalid name: " + str);
                        this.mServicePackages.remove(i);
                    } else {
                        this.mServicePackages.put(i, unflattenFromString.getPackageName());
                    }
                } else {
                    this.mServicePackages.remove(i);
                }
            }
        }

        @GuardedBy({"mGlobalWhitelistStateLock"})
        public ContentCaptureOptions getOptions(int i, String str) {
            ArraySet arraySet;
            ContentCaptureOptions contentCaptureOptions;
            synchronized (((GlobalWhitelistState) this).mGlobalWhitelistStateLock) {
                boolean isWhitelisted = isWhitelisted(i, str);
                if (isWhitelisted) {
                    arraySet = null;
                } else {
                    ArraySet whitelistedComponents = getWhitelistedComponents(i, str);
                    if (whitelistedComponents == null && str.equals(this.mServicePackages.get(i))) {
                        if (((AbstractMasterSystemService) ContentCaptureManagerService.this).verbose) {
                            Slog.v(ContentCaptureManagerService.TAG, "getOptionsForPackage() lite for " + str);
                        }
                        return new ContentCaptureOptions(ContentCaptureManagerService.this.mDevCfgLoggingLevel);
                    }
                    arraySet = whitelistedComponents;
                }
                if (Build.IS_USER && ((AbstractMasterSystemService) ContentCaptureManagerService.this).mServiceNameResolver.isTemporary(i) && !str.equals(this.mServicePackages.get(i))) {
                    Slog.w(ContentCaptureManagerService.TAG, "Ignoring package " + str + " while using temporary service " + this.mServicePackages.get(i));
                    return null;
                }
                if (!isWhitelisted && arraySet == null) {
                    if (((AbstractMasterSystemService) ContentCaptureManagerService.this).verbose) {
                        Slog.v(ContentCaptureManagerService.TAG, "getOptionsForPackage(" + str + "): not whitelisted");
                    }
                    return null;
                }
                synchronized (((AbstractMasterSystemService) ContentCaptureManagerService.this).mLock) {
                    ContentCaptureManagerService contentCaptureManagerService = ContentCaptureManagerService.this;
                    contentCaptureOptions = new ContentCaptureOptions(contentCaptureManagerService.mDevCfgLoggingLevel, contentCaptureManagerService.mDevCfgMaxBufferSize, contentCaptureManagerService.mDevCfgIdleFlushingFrequencyMs, contentCaptureManagerService.mDevCfgTextChangeFlushingFrequencyMs, contentCaptureManagerService.mDevCfgLogHistorySize, contentCaptureManagerService.mDevCfgDisableFlushForViewTreeAppearing, arraySet);
                    if (((AbstractMasterSystemService) ContentCaptureManagerService.this).verbose) {
                        Slog.v(ContentCaptureManagerService.TAG, "getOptionsForPackage(" + str + "): " + contentCaptureOptions);
                    }
                }
                return contentCaptureOptions;
            }
        }

        public void dump(String str, PrintWriter printWriter) {
            super.dump(str, printWriter);
            synchronized (((GlobalWhitelistState) this).mGlobalWhitelistStateLock) {
                if (this.mServicePackages.size() > 0) {
                    printWriter.print(str);
                    printWriter.print("Service packages: ");
                    printWriter.println(this.mServicePackages);
                }
                if (this.mTemporaryServices.size() > 0) {
                    printWriter.print(str);
                    printWriter.print("Temp services: ");
                    printWriter.println(this.mTemporaryServices);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class DataShareCallbackDelegate extends IDataShareCallback.Stub {
        private final IDataShareWriteAdapter mClientAdapter;
        private final DataShareRequest mDataShareRequest;
        private final AtomicBoolean mLoggedWriteFinish = new AtomicBoolean(false);
        private final ContentCaptureManagerService mParentService;

        DataShareCallbackDelegate(DataShareRequest dataShareRequest, IDataShareWriteAdapter iDataShareWriteAdapter, ContentCaptureManagerService contentCaptureManagerService) {
            this.mDataShareRequest = dataShareRequest;
            this.mClientAdapter = iDataShareWriteAdapter;
            this.mParentService = contentCaptureManagerService;
        }

        public void accept(final IDataShareReadAdapter iDataShareReadAdapter) {
            Slog.i(ContentCaptureManagerService.TAG, "Data share request accepted by Content Capture service");
            logServiceEvent(7);
            Pair<ParcelFileDescriptor, ParcelFileDescriptor> createPipe = createPipe();
            if (createPipe == null) {
                logServiceEvent(12);
                sendErrorSignal(this.mClientAdapter, iDataShareReadAdapter, 1);
                return;
            }
            final ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) createPipe.second;
            final ParcelFileDescriptor parcelFileDescriptor2 = (ParcelFileDescriptor) createPipe.first;
            Pair<ParcelFileDescriptor, ParcelFileDescriptor> createPipe2 = createPipe();
            if (createPipe2 == null) {
                logServiceEvent(13);
                bestEffortCloseFileDescriptors(parcelFileDescriptor, parcelFileDescriptor2);
                sendErrorSignal(this.mClientAdapter, iDataShareReadAdapter, 1);
                return;
            }
            final ParcelFileDescriptor parcelFileDescriptor3 = (ParcelFileDescriptor) createPipe2.second;
            final ParcelFileDescriptor parcelFileDescriptor4 = (ParcelFileDescriptor) createPipe2.first;
            synchronized (((AbstractMasterSystemService) this.mParentService).mLock) {
                this.mParentService.mPackagesWithShareRequests.add(this.mDataShareRequest.getPackageName());
            }
            if (!setUpSharingPipeline(this.mClientAdapter, iDataShareReadAdapter, parcelFileDescriptor, parcelFileDescriptor4)) {
                sendErrorSignal(this.mClientAdapter, iDataShareReadAdapter, 1);
                bestEffortCloseFileDescriptors(parcelFileDescriptor, parcelFileDescriptor2, parcelFileDescriptor3, parcelFileDescriptor4);
                synchronized (((AbstractMasterSystemService) this.mParentService).mLock) {
                    this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                }
                return;
            }
            bestEffortCloseFileDescriptors(parcelFileDescriptor, parcelFileDescriptor4);
            this.mParentService.mDataShareExecutor.execute(new Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$DataShareCallbackDelegate$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ContentCaptureManagerService.DataShareCallbackDelegate.this.lambda$accept$0(parcelFileDescriptor2, parcelFileDescriptor3, iDataShareReadAdapter);
                }
            });
            this.mParentService.mHandler.postDelayed(new Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$DataShareCallbackDelegate$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ContentCaptureManagerService.DataShareCallbackDelegate.this.lambda$accept$1(parcelFileDescriptor, parcelFileDescriptor2, parcelFileDescriptor3, parcelFileDescriptor4, iDataShareReadAdapter);
                }
            }, 300000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:119:0x00dd -> B:34:0x00ef). Please report as a decompilation issue!!! */
        public /* synthetic */ void lambda$accept$0(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, IDataShareReadAdapter iDataShareReadAdapter) {
            boolean z;
            boolean z2 = false;
            try {
                try {
                    try {
                        ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor);
                        try {
                            ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor2);
                            try {
                                byte[] bArr = new byte[1024];
                                z = false;
                                while (true) {
                                    try {
                                        int read = autoCloseInputStream.read(bArr);
                                        if (read == -1) {
                                            try {
                                                break;
                                            } catch (Throwable th) {
                                                th = th;
                                                z2 = z;
                                                try {
                                                    autoCloseInputStream.close();
                                                } catch (Throwable th2) {
                                                    th.addSuppressed(th2);
                                                }
                                                throw th;
                                            }
                                        }
                                        autoCloseOutputStream.write(bArr, 0, read);
                                        z = true;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        z2 = z;
                                        try {
                                            autoCloseOutputStream.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                        throw th;
                                    }
                                }
                                autoCloseOutputStream.close();
                                try {
                                    autoCloseInputStream.close();
                                    synchronized (((AbstractMasterSystemService) this.mParentService).mLock) {
                                        this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                                    }
                                } catch (IOException e) {
                                    e = e;
                                    z2 = z;
                                    Slog.e(ContentCaptureManagerService.TAG, "Failed to pipe client and service streams", e);
                                    logServiceEvent(10);
                                    sendErrorSignal(this.mClientAdapter, iDataShareReadAdapter, 1);
                                    synchronized (((AbstractMasterSystemService) this.mParentService).mLock) {
                                        this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                                    }
                                    if (z2) {
                                        if (!this.mLoggedWriteFinish.get()) {
                                            logServiceEvent(9);
                                            this.mLoggedWriteFinish.set(true);
                                        }
                                        try {
                                            this.mClientAdapter.finish();
                                        } catch (RemoteException e2) {
                                            Slog.e(ContentCaptureManagerService.TAG, "Failed to call finish() the client operation", e2);
                                        }
                                        iDataShareReadAdapter.finish();
                                        return;
                                    }
                                    logServiceEvent(11);
                                    sendErrorSignal(this.mClientAdapter, iDataShareReadAdapter, 1);
                                } catch (Throwable th5) {
                                    th = th5;
                                    z2 = z;
                                    synchronized (((AbstractMasterSystemService) this.mParentService).mLock) {
                                        this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                                    }
                                    if (z2) {
                                        if (!this.mLoggedWriteFinish.get()) {
                                            logServiceEvent(9);
                                            this.mLoggedWriteFinish.set(true);
                                        }
                                        try {
                                            this.mClientAdapter.finish();
                                        } catch (RemoteException e3) {
                                            Slog.e(ContentCaptureManagerService.TAG, "Failed to call finish() the client operation", e3);
                                        }
                                        try {
                                            iDataShareReadAdapter.finish();
                                        } catch (RemoteException e4) {
                                            Slog.e(ContentCaptureManagerService.TAG, "Failed to call finish() the service operation", e4);
                                        }
                                    } else {
                                        logServiceEvent(11);
                                        sendErrorSignal(this.mClientAdapter, iDataShareReadAdapter, 1);
                                    }
                                    throw th;
                                }
                            } catch (Throwable th6) {
                                th = th6;
                            }
                        } catch (Throwable th7) {
                            th = th7;
                            autoCloseInputStream.close();
                            throw th;
                        }
                    } catch (IOException e5) {
                        e = e5;
                    }
                } catch (RemoteException e6) {
                    Slog.e(ContentCaptureManagerService.TAG, "Failed to call finish() the service operation", e6);
                }
                if (z) {
                    if (!this.mLoggedWriteFinish.get()) {
                        logServiceEvent(9);
                        this.mLoggedWriteFinish.set(true);
                    }
                    try {
                        this.mClientAdapter.finish();
                    } catch (RemoteException e7) {
                        Slog.e(ContentCaptureManagerService.TAG, "Failed to call finish() the client operation", e7);
                    }
                    iDataShareReadAdapter.finish();
                    return;
                }
                logServiceEvent(11);
                sendErrorSignal(this.mClientAdapter, iDataShareReadAdapter, 1);
            } catch (Throwable th8) {
                th = th8;
            }
        }

        public void reject() {
            Slog.i(ContentCaptureManagerService.TAG, "Data share request rejected by Content Capture service");
            logServiceEvent(8);
            try {
                this.mClientAdapter.rejected();
            } catch (RemoteException e) {
                Slog.w(ContentCaptureManagerService.TAG, "Failed to call rejected() the client operation", e);
                try {
                    this.mClientAdapter.error(1);
                } catch (RemoteException e2) {
                    Slog.w(ContentCaptureManagerService.TAG, "Failed to call error() the client operation", e2);
                }
            }
        }

        private boolean setUpSharingPipeline(IDataShareWriteAdapter iDataShareWriteAdapter, IDataShareReadAdapter iDataShareReadAdapter, ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2) {
            try {
                iDataShareWriteAdapter.write(parcelFileDescriptor);
                try {
                    iDataShareReadAdapter.start(parcelFileDescriptor2);
                    return true;
                } catch (RemoteException e) {
                    Slog.e(ContentCaptureManagerService.TAG, "Failed to call start() the service operation", e);
                    logServiceEvent(13);
                    return false;
                }
            } catch (RemoteException e2) {
                Slog.e(ContentCaptureManagerService.TAG, "Failed to call write() the client operation", e2);
                logServiceEvent(12);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: enforceDataSharingTtl, reason: merged with bridge method [inline-methods] */
        public void lambda$accept$1(ParcelFileDescriptor parcelFileDescriptor, ParcelFileDescriptor parcelFileDescriptor2, ParcelFileDescriptor parcelFileDescriptor3, ParcelFileDescriptor parcelFileDescriptor4, IDataShareReadAdapter iDataShareReadAdapter) {
            synchronized (((AbstractMasterSystemService) this.mParentService).mLock) {
                this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                boolean z = (parcelFileDescriptor2.getFileDescriptor().valid() || parcelFileDescriptor3.getFileDescriptor().valid()) ? false : true;
                if (z) {
                    if (!this.mLoggedWriteFinish.get()) {
                        logServiceEvent(9);
                        this.mLoggedWriteFinish.set(true);
                    }
                    Slog.i(ContentCaptureManagerService.TAG, "Content capture data sharing session terminated successfully for package '" + this.mDataShareRequest.getPackageName() + "'");
                } else {
                    logServiceEvent(15);
                    Slog.i(ContentCaptureManagerService.TAG, "Reached the timeout of Content Capture data sharing session for package '" + this.mDataShareRequest.getPackageName() + "', terminating the pipe.");
                }
                bestEffortCloseFileDescriptors(parcelFileDescriptor, parcelFileDescriptor2, parcelFileDescriptor3, parcelFileDescriptor4);
                if (!z) {
                    sendErrorSignal(this.mClientAdapter, iDataShareReadAdapter, 3);
                }
            }
        }

        private Pair<ParcelFileDescriptor, ParcelFileDescriptor> createPipe() {
            try {
                ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
                if (createPipe.length != 2) {
                    Slog.e(ContentCaptureManagerService.TAG, "Failed to create a content capture data-sharing pipe, unexpected number of file descriptors");
                    return null;
                }
                if (!createPipe[0].getFileDescriptor().valid() || !createPipe[1].getFileDescriptor().valid()) {
                    Slog.e(ContentCaptureManagerService.TAG, "Failed to create a content capture data-sharing pipe, didn't receive a pair of valid file descriptors.");
                    return null;
                }
                return Pair.create(createPipe[0], createPipe[1]);
            } catch (IOException e) {
                Slog.e(ContentCaptureManagerService.TAG, "Failed to create a content capture data-sharing pipe", e);
                return null;
            }
        }

        private void bestEffortCloseFileDescriptor(ParcelFileDescriptor parcelFileDescriptor) {
            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
                Slog.e(ContentCaptureManagerService.TAG, "Failed to close a file descriptor", e);
            }
        }

        private void bestEffortCloseFileDescriptors(ParcelFileDescriptor... parcelFileDescriptorArr) {
            for (ParcelFileDescriptor parcelFileDescriptor : parcelFileDescriptorArr) {
                bestEffortCloseFileDescriptor(parcelFileDescriptor);
            }
        }

        private static void sendErrorSignal(IDataShareWriteAdapter iDataShareWriteAdapter, IDataShareReadAdapter iDataShareReadAdapter, int i) {
            try {
                iDataShareWriteAdapter.error(i);
            } catch (RemoteException e) {
                Slog.e(ContentCaptureManagerService.TAG, "Failed to call error() the client operation", e);
            }
            try {
                iDataShareReadAdapter.error(i);
            } catch (RemoteException e2) {
                Slog.e(ContentCaptureManagerService.TAG, "Failed to call error() the service operation", e2);
            }
        }

        private void logServiceEvent(int i) {
            ContentCaptureMetricsLogger.writeServiceEvent(i, ((AbstractMasterSystemService) this.mParentService).mServiceNameResolver.getServiceName(UserHandle.getCallingUserId()));
        }
    }
}
