package com.android.server.ambientcontext;

import android.R;
import android.app.PendingIntent;
import android.app.ambientcontext.AmbientContextEvent;
import android.app.ambientcontext.AmbientContextEventRequest;
import android.app.ambientcontext.IAmbientContextManager;
import android.app.ambientcontext.IAmbientContextObserver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManagerInternal;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.util.DumpUtils;
import com.android.server.LocalServices;
import com.android.server.ambientcontext.AmbientContextManagerPerUserService;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.FrameworkResourcesServiceNameResolver;
import com.google.android.collect.Sets;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AmbientContextManagerService extends AbstractMasterSystemService<AmbientContextManagerService, AmbientContextManagerPerUserService> {
    private static final Set<Integer> DEFAULT_EVENT_SET = Sets.newHashSet(new Integer[]{1, 2, 3});
    private static final boolean DEFAULT_SERVICE_ENABLED = true;
    private static final String KEY_SERVICE_ENABLED = "service_enabled";
    public static final int MAX_TEMPORARY_SERVICE_DURATION_MS = 30000;
    private static final String TAG = "AmbientContextManagerService";
    private final Context mContext;
    private Set<ClientRequest> mExistingClientRequests;
    boolean mIsServiceEnabled;
    boolean mIsWearableServiceEnabled;

    protected int getMaximumTemporaryServiceDurationMs() {
        return 30000;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: newServiceLocked, reason: merged with bridge method [inline-methods] */
    public AmbientContextManagerPerUserService m1546newServiceLocked(int i, boolean z) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class ClientRequest {
        private final IAmbientContextObserver mObserver;
        private final String mPackageName;
        private final AmbientContextEventRequest mRequest;
        private final int mUserId;

        ClientRequest(int i, AmbientContextEventRequest ambientContextEventRequest, String str, IAmbientContextObserver iAmbientContextObserver) {
            this.mUserId = i;
            this.mRequest = ambientContextEventRequest;
            this.mPackageName = str;
            this.mObserver = iAmbientContextObserver;
        }

        String getPackageName() {
            return this.mPackageName;
        }

        AmbientContextEventRequest getRequest() {
            return this.mRequest;
        }

        IAmbientContextObserver getObserver() {
            return this.mObserver;
        }

        boolean hasUserId(int i) {
            return this.mUserId == i;
        }

        boolean hasUserIdAndPackageName(int i, String str) {
            return i == this.mUserId && str.equals(getPackageName());
        }
    }

    public AmbientContextManagerService(Context context) {
        super(context, new FrameworkResourcesServiceNameResolver(context, R.array.config_displayWhiteBalanceAmbientColorTemperatures, true), (String) null, 68);
        this.mContext = context;
        this.mExistingClientRequests = new ArraySet();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onStart() {
        publishBinderService("ambient_context", new AmbientContextManagerInternal());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onBootPhase(int i) {
        if (i == 500) {
            DeviceConfig.addOnPropertiesChangedListener("ambient_context_manager_service", getContext().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.ambientcontext.AmbientContextManagerService$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    AmbientContextManagerService.this.lambda$onBootPhase$0(properties);
                }
            });
            this.mIsServiceEnabled = DeviceConfig.getBoolean("ambient_context_manager_service", KEY_SERVICE_ENABLED, true);
            this.mIsWearableServiceEnabled = DeviceConfig.getBoolean("wearable_sensing", KEY_SERVICE_ENABLED, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0(DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void newClientAdded(int i, AmbientContextEventRequest ambientContextEventRequest, String str, IAmbientContextObserver iAmbientContextObserver) {
        Slog.d(TAG, "New client added: " + str);
        this.mExistingClientRequests.removeAll(findExistingRequests(i, str));
        this.mExistingClientRequests.add(new ClientRequest(i, ambientContextEventRequest, str, iAmbientContextObserver));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clientRemoved(int i, String str) {
        Slog.d(TAG, "Remove client: " + str);
        this.mExistingClientRequests.removeAll(findExistingRequests(i, str));
    }

    private Set<ClientRequest> findExistingRequests(int i, String str) {
        ArraySet arraySet = new ArraySet();
        for (ClientRequest clientRequest : this.mExistingClientRequests) {
            if (clientRequest.hasUserIdAndPackageName(i, str)) {
                arraySet.add(clientRequest);
            }
        }
        return arraySet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IAmbientContextObserver getClientRequestObserver(int i, String str) {
        for (ClientRequest clientRequest : this.mExistingClientRequests) {
            if (clientRequest.hasUserIdAndPackageName(i, str)) {
                return clientRequest.getObserver();
            }
        }
        return null;
    }

    private void onDeviceConfigChange(Set<String> set) {
        if (set.contains(KEY_SERVICE_ENABLED)) {
            this.mIsServiceEnabled = DeviceConfig.getBoolean("ambient_context_manager_service", KEY_SERVICE_ENABLED, true);
            this.mIsWearableServiceEnabled = DeviceConfig.getBoolean("wearable_sensing", KEY_SERVICE_ENABLED, true);
        }
    }

    protected List<AmbientContextManagerPerUserService> newServiceListLocked(int i, boolean z, String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            Slog.i(TAG, "serviceNames sent in newServiceListLocked is null, or empty");
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        if (strArr.length == 2) {
            Slog.i(TAG, "Not using default services, services provided for testing should be exactly two services.");
            if (!isDefaultService(strArr[0]) && !isDefaultWearableService(strArr[1])) {
                arrayList.add(new DefaultAmbientContextManagerPerUserService(this, ((AbstractMasterSystemService) this).mLock, i, AmbientContextManagerPerUserService.ServiceType.DEFAULT, strArr[0]));
                arrayList.add(new WearableAmbientContextManagerPerUserService(this, ((AbstractMasterSystemService) this).mLock, i, AmbientContextManagerPerUserService.ServiceType.WEARABLE, strArr[1]));
            }
            return arrayList;
        }
        Slog.i(TAG, "Incorrect number of services provided for testing.");
        for (String str : strArr) {
            Slog.d(TAG, "newServicesListLocked with service name: " + str);
            AmbientContextManagerPerUserService.ServiceType serviceType = getServiceType(str);
            AmbientContextManagerPerUserService.ServiceType serviceType2 = AmbientContextManagerPerUserService.ServiceType.WEARABLE;
            if (serviceType == serviceType2) {
                arrayList.add(new WearableAmbientContextManagerPerUserService(this, ((AbstractMasterSystemService) this).mLock, i, serviceType2, str));
            } else {
                arrayList.add(new DefaultAmbientContextManagerPerUserService(this, ((AbstractMasterSystemService) this).mLock, i, AmbientContextManagerPerUserService.ServiceType.DEFAULT, str));
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceRemoved(AmbientContextManagerPerUserService ambientContextManagerPerUserService, int i) {
        Slog.d(TAG, "onServiceRemoved");
        ambientContextManagerPerUserService.destroyLocked();
    }

    protected void onServicePackageRestartedLocked(int i) {
        Slog.d(TAG, "Restoring remote request. Reason: Service package restarted.");
        restorePreviouslyEnabledClients(i);
    }

    protected void onServicePackageUpdatedLocked(int i) {
        Slog.d(TAG, "Restoring remote request. Reason: Service package updated.");
        restorePreviouslyEnabledClients(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", TAG);
    }

    public static boolean isDetectionServiceConfigured() {
        boolean z = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getKnownPackageNames(18, 0).length != 0;
        Slog.i(TAG, "Detection service configured: " + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startDetection(int i, AmbientContextEventRequest ambientContextEventRequest, String str, IAmbientContextObserver iAmbientContextObserver) {
        Context context = this.mContext;
        String str2 = TAG;
        context.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", str2);
        synchronized (((AbstractMasterSystemService) this).mLock) {
            AmbientContextManagerPerUserService ambientContextManagerPerUserServiceForEventTypes = getAmbientContextManagerPerUserServiceForEventTypes(i, ambientContextEventRequest.getEventTypes());
            if (ambientContextManagerPerUserServiceForEventTypes != null) {
                ambientContextManagerPerUserServiceForEventTypes.startDetection(ambientContextEventRequest, str, iAmbientContextObserver);
            } else {
                Slog.i(str2, "service not available for user_id: " + i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopAmbientContextEvent(int i, String str) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", TAG);
        synchronized (((AbstractMasterSystemService) this).mLock) {
            for (ClientRequest clientRequest : this.mExistingClientRequests) {
                String str2 = TAG;
                Slog.i(str2, "Looping through clients");
                if (clientRequest.hasUserIdAndPackageName(i, str)) {
                    Slog.i(str2, "we have an existing client");
                    AmbientContextManagerPerUserService ambientContextManagerPerUserServiceForEventTypes = getAmbientContextManagerPerUserServiceForEventTypes(i, clientRequest.getRequest().getEventTypes());
                    if (ambientContextManagerPerUserServiceForEventTypes != null) {
                        ambientContextManagerPerUserServiceForEventTypes.stopDetection(str);
                    } else {
                        Slog.i(str2, "service not available for user_id: " + i);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void queryServiceStatus(int i, String str, int[] iArr, RemoteCallback remoteCallback) {
        Context context = this.mContext;
        String str2 = TAG;
        context.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", str2);
        synchronized (((AbstractMasterSystemService) this).mLock) {
            AmbientContextManagerPerUserService ambientContextManagerPerUserServiceForEventTypes = getAmbientContextManagerPerUserServiceForEventTypes(i, intArrayToIntegerSet(iArr));
            if (ambientContextManagerPerUserServiceForEventTypes != null) {
                ambientContextManagerPerUserServiceForEventTypes.onQueryServiceStatus(iArr, str, remoteCallback);
            } else {
                Slog.i(str2, "query service not available for user_id: " + i);
            }
        }
    }

    private void restorePreviouslyEnabledClients(int i) {
        synchronized (((AbstractMasterSystemService) this).mLock) {
            for (AmbientContextManagerPerUserService ambientContextManagerPerUserService : getServiceListForUserLocked(i)) {
                for (ClientRequest clientRequest : this.mExistingClientRequests) {
                    if (clientRequest.hasUserId(i)) {
                        Slog.d(TAG, "Restoring detection for " + clientRequest.getPackageName());
                        ambientContextManagerPerUserService.startDetection(clientRequest.getRequest(), clientRequest.getPackageName(), clientRequest.getObserver());
                    }
                }
            }
        }
    }

    public ComponentName getComponentName(int i, AmbientContextManagerPerUserService.ServiceType serviceType) {
        synchronized (((AbstractMasterSystemService) this).mLock) {
            AmbientContextManagerPerUserService serviceForType = getServiceForType(i, serviceType);
            if (serviceForType == null) {
                return null;
            }
            return serviceForType.getComponentName();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AmbientContextManagerPerUserService getAmbientContextManagerPerUserServiceForEventTypes(int i, Set<Integer> set) {
        if (isWearableEventTypesOnly(set)) {
            return getServiceForType(i, AmbientContextManagerPerUserService.ServiceType.WEARABLE);
        }
        return getServiceForType(i, AmbientContextManagerPerUserService.ServiceType.DEFAULT);
    }

    private AmbientContextManagerPerUserService.ServiceType getServiceType(String str) {
        String string = this.mContext.getResources().getString(R.string.config_signalXPath);
        if (string != null && string.equals(str)) {
            return AmbientContextManagerPerUserService.ServiceType.WEARABLE;
        }
        return AmbientContextManagerPerUserService.ServiceType.DEFAULT;
    }

    private boolean isDefaultService(String str) {
        String string = this.mContext.getResources().getString(R.string.config_keyguardComponent);
        return string != null && string.equals(str);
    }

    private boolean isDefaultWearableService(String str) {
        String string = this.mContext.getResources().getString(R.string.config_signalXPath);
        return string != null && string.equals(str);
    }

    private AmbientContextManagerPerUserService getServiceForType(int i, AmbientContextManagerPerUserService.ServiceType serviceType) {
        String str = TAG;
        Slog.d(str, "getServiceForType with userid: " + i + " service type: " + serviceType.name());
        synchronized (((AbstractMasterSystemService) this).mLock) {
            List<AmbientContextManagerPerUserService> serviceListForUserLocked = getServiceListForUserLocked(i);
            StringBuilder sb = new StringBuilder();
            sb.append("Services that are available: ");
            sb.append(serviceListForUserLocked == null ? "null services" : serviceListForUserLocked.size() + " number of services");
            Slog.d(str, sb.toString());
            if (serviceListForUserLocked == null) {
                return null;
            }
            for (AmbientContextManagerPerUserService ambientContextManagerPerUserService : serviceListForUserLocked) {
                if (ambientContextManagerPerUserService.getServiceType() == serviceType) {
                    return ambientContextManagerPerUserService;
                }
            }
            return null;
        }
    }

    private boolean isWearableEventTypesOnly(Set<Integer> set) {
        if (set.isEmpty()) {
            Slog.d(TAG, "empty event types.");
            return false;
        }
        Iterator<Integer> it = set.iterator();
        while (it.hasNext()) {
            if (it.next().intValue() < 100000) {
                Slog.d(TAG, "Not all events types are wearable events.");
                return false;
            }
        }
        Slog.d(TAG, "only wearable events.");
        return true;
    }

    private boolean isWearableEventTypesOnly(int[] iArr) {
        return isWearableEventTypesOnly(new HashSet(Arrays.asList(intArrayToIntegerArray(iArr))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean containsMixedEvents(int[] iArr) {
        if (isWearableEventTypesOnly(iArr)) {
            return false;
        }
        for (int i : iArr) {
            if (!DEFAULT_EVENT_SET.contains(Integer.valueOf(i))) {
                Slog.w(TAG, "Received mixed event types, this is not supported.");
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] integerSetToIntArray(Set<Integer> set) {
        int[] iArr = new int[set.size()];
        Iterator<Integer> it = set.iterator();
        int i = 0;
        while (it.hasNext()) {
            iArr[i] = it.next().intValue();
            i++;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<Integer> intArrayToIntegerSet(int[] iArr) {
        HashSet hashSet = new HashSet();
        for (int i : iArr) {
            hashSet.add(Integer.valueOf(i));
        }
        return hashSet;
    }

    private static Integer[] intArrayToIntegerArray(int[] iArr) {
        Integer[] numArr = new Integer[iArr.length];
        int length = iArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            numArr[i2] = Integer.valueOf(iArr[i]);
            i++;
            i2++;
        }
        return numArr;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class AmbientContextManagerInternal extends IAmbientContextManager.Stub {
        private AmbientContextManagerInternal() {
        }

        public void registerObserver(AmbientContextEventRequest ambientContextEventRequest, final PendingIntent pendingIntent, final RemoteCallback remoteCallback) {
            Objects.requireNonNull(ambientContextEventRequest);
            Objects.requireNonNull(pendingIntent);
            Objects.requireNonNull(remoteCallback);
            final AmbientContextManagerPerUserService ambientContextManagerPerUserServiceForEventTypes = AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(UserHandle.getCallingUserId(), ambientContextEventRequest.getEventTypes());
            registerObserverWithCallback(ambientContextEventRequest, pendingIntent.getCreatorPackage(), new IAmbientContextObserver.Stub() { // from class: com.android.server.ambientcontext.AmbientContextManagerService.AmbientContextManagerInternal.1
                public void onEvents(List<AmbientContextEvent> list) throws RemoteException {
                    ambientContextManagerPerUserServiceForEventTypes.sendDetectionResultIntent(pendingIntent, list);
                }

                public void onRegistrationComplete(int i) throws RemoteException {
                    ambientContextManagerPerUserServiceForEventTypes.sendStatusCallback(remoteCallback, i);
                }
            });
        }

        public void registerObserverWithCallback(AmbientContextEventRequest ambientContextEventRequest, String str, IAmbientContextObserver iAmbientContextObserver) {
            Slog.i(AmbientContextManagerService.TAG, "AmbientContextManagerService registerObserverWithCallback.");
            Objects.requireNonNull(ambientContextEventRequest);
            Objects.requireNonNull(str);
            Objects.requireNonNull(iAmbientContextObserver);
            AmbientContextManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", AmbientContextManagerService.TAG);
            AmbientContextManagerService.this.assertCalledByPackageOwner(str);
            AmbientContextManagerPerUserService ambientContextManagerPerUserServiceForEventTypes = AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(UserHandle.getCallingUserId(), ambientContextEventRequest.getEventTypes());
            if (ambientContextManagerPerUserServiceForEventTypes == null) {
                Slog.w(AmbientContextManagerService.TAG, "onRegisterObserver unavailable user_id: " + UserHandle.getCallingUserId());
                return;
            }
            int checkStatusCode = checkStatusCode(ambientContextManagerPerUserServiceForEventTypes, AmbientContextManagerService.integerSetToIntArray(ambientContextEventRequest.getEventTypes()));
            if (checkStatusCode == 1) {
                ambientContextManagerPerUserServiceForEventTypes.onRegisterObserver(ambientContextEventRequest, str, iAmbientContextObserver);
            } else {
                ambientContextManagerPerUserServiceForEventTypes.completeRegistration(iAmbientContextObserver, checkStatusCode);
            }
        }

        public void unregisterObserver(String str) {
            AmbientContextManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", AmbientContextManagerService.TAG);
            AmbientContextManagerService.this.assertCalledByPackageOwner(str);
            synchronized (((AbstractMasterSystemService) AmbientContextManagerService.this).mLock) {
                for (ClientRequest clientRequest : AmbientContextManagerService.this.mExistingClientRequests) {
                    if (clientRequest != null && clientRequest.getPackageName().equals(str)) {
                        AmbientContextManagerPerUserService ambientContextManagerPerUserServiceForEventTypes = AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(UserHandle.getCallingUserId(), clientRequest.getRequest().getEventTypes());
                        if (ambientContextManagerPerUserServiceForEventTypes != null) {
                            ambientContextManagerPerUserServiceForEventTypes.onUnregisterObserver(str);
                        } else {
                            Slog.w(AmbientContextManagerService.TAG, "onUnregisterObserver unavailable user_id: " + UserHandle.getCallingUserId());
                        }
                    }
                }
            }
        }

        public void queryServiceStatus(int[] iArr, String str, RemoteCallback remoteCallback) {
            Objects.requireNonNull(iArr);
            Objects.requireNonNull(str);
            Objects.requireNonNull(remoteCallback);
            AmbientContextManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", AmbientContextManagerService.TAG);
            AmbientContextManagerService.this.assertCalledByPackageOwner(str);
            synchronized (((AbstractMasterSystemService) AmbientContextManagerService.this).mLock) {
                AmbientContextManagerPerUserService ambientContextManagerPerUserServiceForEventTypes = AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(UserHandle.getCallingUserId(), AmbientContextManagerService.this.intArrayToIntegerSet(iArr));
                if (ambientContextManagerPerUserServiceForEventTypes == null) {
                    Slog.w(AmbientContextManagerService.TAG, "queryServiceStatus unavailable user_id: " + UserHandle.getCallingUserId());
                    return;
                }
                int checkStatusCode = checkStatusCode(ambientContextManagerPerUserServiceForEventTypes, iArr);
                if (checkStatusCode == 1) {
                    ambientContextManagerPerUserServiceForEventTypes.onQueryServiceStatus(iArr, str, remoteCallback);
                } else {
                    ambientContextManagerPerUserServiceForEventTypes.sendStatusCallback(remoteCallback, checkStatusCode);
                }
            }
        }

        public void startConsentActivity(int[] iArr, String str) {
            Objects.requireNonNull(iArr);
            Objects.requireNonNull(str);
            AmbientContextManagerService.this.assertCalledByPackageOwner(str);
            AmbientContextManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_AMBIENT_CONTEXT_EVENT", AmbientContextManagerService.TAG);
            if (AmbientContextManagerService.this.containsMixedEvents(iArr)) {
                Slog.d(AmbientContextManagerService.TAG, "AmbientContextEventRequest contains mixed events, this is not supported.");
                return;
            }
            AmbientContextManagerPerUserService ambientContextManagerPerUserServiceForEventTypes = AmbientContextManagerService.this.getAmbientContextManagerPerUserServiceForEventTypes(UserHandle.getCallingUserId(), AmbientContextManagerService.this.intArrayToIntegerSet(iArr));
            if (ambientContextManagerPerUserServiceForEventTypes != null) {
                ambientContextManagerPerUserServiceForEventTypes.onStartConsentActivity(iArr, str);
                return;
            }
            Slog.w(AmbientContextManagerService.TAG, "startConsentActivity unavailable user_id: " + UserHandle.getCallingUserId());
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(AmbientContextManagerService.this.mContext, AmbientContextManagerService.TAG, printWriter)) {
                synchronized (((AbstractMasterSystemService) AmbientContextManagerService.this).mLock) {
                    AmbientContextManagerService.this.dumpLocked("", printWriter);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new AmbientContextShellCommand(AmbientContextManagerService.this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        private int checkStatusCode(AmbientContextManagerPerUserService ambientContextManagerPerUserService, int[] iArr) {
            if (ambientContextManagerPerUserService.getServiceType() == AmbientContextManagerPerUserService.ServiceType.DEFAULT && !AmbientContextManagerService.this.mIsServiceEnabled) {
                Slog.d(AmbientContextManagerService.TAG, "Service not enabled.");
                return 3;
            }
            if (ambientContextManagerPerUserService.getServiceType() == AmbientContextManagerPerUserService.ServiceType.WEARABLE && !AmbientContextManagerService.this.mIsWearableServiceEnabled) {
                Slog.d(AmbientContextManagerService.TAG, "Wearable Service not available.");
                return 3;
            }
            if (!AmbientContextManagerService.this.containsMixedEvents(iArr)) {
                return 1;
            }
            Slog.d(AmbientContextManagerService.TAG, "AmbientContextEventRequest contains mixed events, this is not supported.");
            return 2;
        }
    }
}
