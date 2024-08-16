package com.android.server.credentials;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.credentials.ClearCredentialStateRequest;
import android.credentials.CreateCredentialRequest;
import android.credentials.CredentialOption;
import android.credentials.CredentialProviderInfo;
import android.credentials.GetCredentialRequest;
import android.credentials.IClearCredentialStateCallback;
import android.credentials.ICreateCredentialCallback;
import android.credentials.ICredentialManager;
import android.credentials.IGetCredentialCallback;
import android.credentials.IPrepareGetCredentialCallback;
import android.credentials.ISetEnabledProvidersCallback;
import android.credentials.PrepareGetCredentialResponseInternal;
import android.credentials.RegisterCredentialDescriptionRequest;
import android.credentials.UnregisterCredentialDescriptionRequest;
import android.os.Binder;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.service.credentials.CallingAppInfo;
import android.service.credentials.CredentialProviderInfoFactory;
import android.service.credentials.PermissionUtils;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.SystemService;
import com.android.server.credentials.CredentialDescriptionRegistry;
import com.android.server.credentials.CredentialManagerService;
import com.android.server.credentials.RequestSession;
import com.android.server.credentials.metrics.ApiName;
import com.android.server.credentials.metrics.ApiStatus;
import com.android.server.credentials.metrics.InitialPhaseMetric;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.SecureSettingsServiceNameResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CredentialManagerService extends AbstractMasterSystemService<CredentialManagerService, CredentialManagerServiceImpl> {
    private static final String DEVICE_CONFIG_ENABLE_CREDENTIAL_MANAGER = "enable_credential_manager";
    private static final String PERMISSION_DENIED_ERROR = "permission_denied";
    private static final String PERMISSION_DENIED_WRITE_SECURE_SETTINGS_ERROR = "Caller is missing WRITE_SECURE_SETTINGS permission";
    private static final String TAG = "CredManSysService";
    private final Context mContext;

    @GuardedBy({"mLock"})
    private final SparseArray<Map<IBinder, RequestSession>> mRequestSessions;
    private final SessionManager mSessionManager;

    @GuardedBy({"mLock"})
    private final SparseArray<List<CredentialManagerServiceImpl>> mSystemServicesCacheList;

    public static boolean isCredentialDescriptionApiEnabled() {
        return true;
    }

    protected String getServiceSettingsProperty() {
        return "credential_service";
    }

    public CredentialManagerService(Context context) {
        super(context, new SecureSettingsServiceNameResolver(context, "credential_service", true), (String) null, 4);
        this.mSystemServicesCacheList = new SparseArray<>();
        this.mRequestSessions = new SparseArray<>();
        this.mSessionManager = new SessionManager();
        this.mContext = context;
    }

    @GuardedBy({"mLock"})
    private List<CredentialManagerServiceImpl> constructSystemServiceListLocked(final int i) {
        final ArrayList arrayList = new ArrayList();
        CredentialProviderInfoFactory.getAvailableSystemServices(this.mContext, i, false, new HashSet()).forEach(new Consumer() { // from class: com.android.server.credentials.CredentialManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CredentialManagerService.this.lambda$constructSystemServiceListLocked$0(arrayList, i, (CredentialProviderInfo) obj);
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$constructSystemServiceListLocked$0(List list, int i, CredentialProviderInfo credentialProviderInfo) {
        list.add(new CredentialManagerServiceImpl(this, ((AbstractMasterSystemService) this).mLock, i, credentialProviderInfo));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: newServiceLocked, reason: merged with bridge method [inline-methods] */
    public CredentialManagerServiceImpl m3141newServiceLocked(int i, boolean z) {
        Slog.w(TAG, "Should not be here - CredentialManagerService is configured to use multiple services");
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onStart() {
        publishBinderService("credential", new CredentialManagerServiceStub());
    }

    @GuardedBy({"mLock"})
    protected List<CredentialManagerServiceImpl> newServiceListLocked(int i, boolean z, String[] strArr) {
        getOrConstructSystemServiceListLock(i);
        if (strArr == null || strArr.length == 0) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList(strArr.length);
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str)) {
                try {
                    arrayList.add(new CredentialManagerServiceImpl(this, ((AbstractMasterSystemService) this).mLock, i, str));
                } catch (PackageManager.NameNotFoundException | SecurityException e) {
                    Slog.e(TAG, "Unable to add serviceInfo : ", e);
                }
            }
        }
        return arrayList;
    }

    @GuardedBy({"mLock"})
    protected void handlePackageRemovedMultiModeLocked(String str, int i) {
        List<CredentialManagerServiceImpl> peekServiceListForUserLocked = peekServiceListForUserLocked(i);
        if (peekServiceListForUserLocked == null) {
            return;
        }
        ArrayList<CredentialManagerServiceImpl> arrayList = new ArrayList();
        for (CredentialManagerServiceImpl credentialManagerServiceImpl : peekServiceListForUserLocked) {
            if (credentialManagerServiceImpl != null && str.equals(credentialManagerServiceImpl.getCredentialProviderInfo().getServiceInfo().getComponentName().getPackageName())) {
                arrayList.add(credentialManagerServiceImpl);
            }
        }
        for (CredentialManagerServiceImpl credentialManagerServiceImpl2 : arrayList) {
            removeServiceFromCache(credentialManagerServiceImpl2, i);
            removeServiceFromSystemServicesCache(credentialManagerServiceImpl2, i);
            removeServiceFromMultiModeSettings(credentialManagerServiceImpl2.getComponentName().flattenToString(), i);
            CredentialDescriptionRegistry.forUser(i).evictProviderWithPackageName(credentialManagerServiceImpl2.getServicePackageName());
        }
    }

    @GuardedBy({"mLock"})
    private void removeServiceFromSystemServicesCache(CredentialManagerServiceImpl credentialManagerServiceImpl, int i) {
        if (this.mSystemServicesCacheList.get(i) != null) {
            this.mSystemServicesCacheList.get(i).remove(credentialManagerServiceImpl);
        }
    }

    @GuardedBy({"mLock"})
    private List<CredentialManagerServiceImpl> getOrConstructSystemServiceListLock(int i) {
        List<CredentialManagerServiceImpl> list = this.mSystemServicesCacheList.get(i);
        if (list != null && list.size() != 0) {
            return list;
        }
        List<CredentialManagerServiceImpl> constructSystemServiceListLocked = constructSystemServiceListLocked(i);
        this.mSystemServicesCacheList.put(i, constructSystemServiceListLocked);
        return constructSystemServiceListLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasWriteSecureSettingsPermission() {
        return hasPermission("android.permission.WRITE_SECURE_SETTINGS");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verifyGetProvidersPermission() throws SecurityException {
        if (!hasPermission("android.permission.QUERY_ALL_PACKAGES") && !hasPermission("android.permission.LIST_ENABLED_CREDENTIAL_PROVIDERS")) {
            throw new SecurityException("Caller is missing permission: QUERY_ALL_PACKAGES or LIST_ENABLED_CREDENTIAL_PROVIDERS");
        }
    }

    private boolean hasPermission(String str) {
        boolean z = this.mContext.checkCallingOrSelfPermission(str) == 0;
        if (!z) {
            Slog.e(TAG, "Caller does not have permission: " + str);
        }
        return z;
    }

    private void runForUser(Consumer<CredentialManagerServiceImpl> consumer) {
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (((AbstractMasterSystemService) this).mLock) {
                Iterator<CredentialManagerServiceImpl> it = getCredentialProviderServicesLocked(callingUserId).iterator();
                while (it.hasNext()) {
                    consumer.accept(it.next());
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Set<String> getPrimaryProvidersForUserId(Context context, int i) {
        String[] readServiceNameList = new SecureSettingsServiceNameResolver(context, "credential_service_primary", true).readServiceNameList(ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, false, "getPrimaryProvidersForUserId", null));
        if (readServiceNameList == null) {
            return new HashSet();
        }
        return new HashSet(Arrays.asList(readServiceNameList));
    }

    @GuardedBy({"mLock"})
    private List<CredentialManagerServiceImpl> getCredentialProviderServicesLocked(int i) {
        ArrayList arrayList = new ArrayList();
        List serviceListForUserLocked = getServiceListForUserLocked(i);
        if (serviceListForUserLocked != null && !serviceListForUserLocked.isEmpty()) {
            arrayList.addAll(serviceListForUserLocked);
        }
        arrayList.addAll(getOrConstructSystemServiceListLock(i));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<ProviderSession> initiateProviderSessionsWithActiveContainers(GetRequestSession getRequestSession, Set<Pair<CredentialOption, CredentialDescriptionRegistry.FilterResult>> set) {
        ArrayList arrayList = new ArrayList();
        for (Pair<CredentialOption, CredentialDescriptionRegistry.FilterResult> pair : set) {
            ProviderRegistryGetSession createNewSession = ProviderRegistryGetSession.createNewSession(this.mContext, UserHandle.getCallingUserId(), getRequestSession, getRequestSession.mClientAppInfo, ((CredentialDescriptionRegistry.FilterResult) pair.second).mPackageName, (CredentialOption) pair.first);
            arrayList.add(createNewSession);
            getRequestSession.addProviderSession(createNewSession.getComponentName(), createNewSession);
        }
        return arrayList;
    }

    private List<ProviderSession> initiateProviderSessionsWithActiveContainers(PrepareGetRequestSession prepareGetRequestSession, Set<Pair<CredentialOption, CredentialDescriptionRegistry.FilterResult>> set) {
        ArrayList arrayList = new ArrayList();
        for (Pair<CredentialOption, CredentialDescriptionRegistry.FilterResult> pair : set) {
            ProviderRegistryGetSession createNewSession = ProviderRegistryGetSession.createNewSession(this.mContext, UserHandle.getCallingUserId(), prepareGetRequestSession, prepareGetRequestSession.mClientAppInfo, ((CredentialDescriptionRegistry.FilterResult) pair.second).mPackageName, (CredentialOption) pair.first);
            arrayList.add(createNewSession);
            prepareGetRequestSession.addProviderSession(createNewSession.getComponentName(), createNewSession);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set<Pair<CredentialOption, CredentialDescriptionRegistry.FilterResult>> getFilteredResultFromRegistry(List<CredentialOption> list) {
        Set<CredentialDescriptionRegistry.FilterResult> matchingProviders = CredentialDescriptionRegistry.forUser(UserHandle.getCallingUserId()).getMatchingProviders((Set) list.stream().map(new Function() { // from class: com.android.server.credentials.CredentialManagerService$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                HashSet lambda$getFilteredResultFromRegistry$1;
                lambda$getFilteredResultFromRegistry$1 = CredentialManagerService.lambda$getFilteredResultFromRegistry$1((CredentialOption) obj);
                return lambda$getFilteredResultFromRegistry$1;
            }
        }).collect(Collectors.toSet()));
        HashSet hashSet = new HashSet();
        for (CredentialDescriptionRegistry.FilterResult filterResult : matchingProviders) {
            for (CredentialOption credentialOption : list) {
                if (CredentialDescriptionRegistry.checkForMatch(filterResult.mElementKeys, new HashSet(credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS")))) {
                    hashSet.add(new Pair(credentialOption, filterResult));
                }
            }
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ HashSet lambda$getFilteredResultFromRegistry$1(CredentialOption credentialOption) {
        return new HashSet(credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<ProviderSession> initiateProviderSessions(final RequestSession requestSession, final List<String> list) {
        final ArrayList arrayList = new ArrayList();
        runForUser(new Consumer() { // from class: com.android.server.credentials.CredentialManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CredentialManagerService.this.lambda$initiateProviderSessions$2(requestSession, list, arrayList, (CredentialManagerServiceImpl) obj);
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initiateProviderSessions$2(RequestSession requestSession, List list, List list2, CredentialManagerServiceImpl credentialManagerServiceImpl) {
        synchronized (((AbstractMasterSystemService) this).mLock) {
            ProviderSession initiateProviderSessionForRequestLocked = credentialManagerServiceImpl.initiateProviderSessionForRequestLocked(requestSession, list);
            if (initiateProviderSessionForRequestLocked != null) {
                list2.add(initiateProviderSessionForRequestLocked);
            }
        }
    }

    @GuardedBy({"CredentialDescriptionRegistry.sLock"})
    public void onUserStopped(SystemService.TargetUser targetUser) {
        super.onUserStopped(targetUser);
        CredentialDescriptionRegistry.clearUserSession(targetUser.getUserIdentifier());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public CallingAppInfo constructCallingAppInfo(String str, int i, String str2) {
        try {
            return new CallingAppInfo(str, getContext().getPackageManager().getPackageInfoAsUser(str, PackageManager.PackageInfoFlags.of(134217728L), i).signingInfo, str2);
        } catch (PackageManager.NameNotFoundException e) {
            Slog.e(TAG, "Issue while retrieving signatureInfo : ", e);
            return new CallingAppInfo(str, null, str2);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    final class CredentialManagerServiceStub extends ICredentialManager.Stub {
        CredentialManagerServiceStub() {
        }

        public ICancellationSignal executeGetCredential(GetCredentialRequest getCredentialRequest, IGetCredentialCallback iGetCredentialCallback, String str) {
            long nanoTime = System.nanoTime();
            Slog.i(CredentialManagerService.TAG, "starting executeGetCredential with callingPackage: " + str);
            ICancellationSignal createTransport = CancellationSignal.createTransport();
            int callingUserId = UserHandle.getCallingUserId();
            int callingUid = Binder.getCallingUid();
            CredentialManagerService.this.enforceCallingPackage(str, callingUid);
            CredentialManagerService.this.validateGetCredentialRequest(getCredentialRequest);
            GetRequestSession getRequestSession = new GetRequestSession(CredentialManagerService.this.getContext(), CredentialManagerService.this.mSessionManager, ((AbstractMasterSystemService) CredentialManagerService.this).mLock, callingUserId, callingUid, iGetCredentialCallback, getCredentialRequest, CredentialManagerService.this.constructCallingAppInfo(str, callingUserId, getCredentialRequest.getOrigin()), getEnabledProvidersForUser(callingUserId), CancellationSignal.fromTransport(createTransport), nanoTime);
            CredentialManagerService.this.addSessionLocked(callingUserId, getRequestSession);
            List<ProviderSession> prepareProviderSessions = prepareProviderSessions(getCredentialRequest, getRequestSession);
            if (prepareProviderSessions.isEmpty()) {
                try {
                    iGetCredentialCallback.onError("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL", "No credentials available on this device.");
                } catch (RemoteException e) {
                    Slog.e(CredentialManagerService.TAG, "Issue invoking onError on IGetCredentialCallback callback: " + e.getMessage());
                }
            }
            invokeProviderSessions(prepareProviderSessions);
            return createTransport;
        }

        public ICancellationSignal executePrepareGetCredential(GetCredentialRequest getCredentialRequest, IPrepareGetCredentialCallback iPrepareGetCredentialCallback, IGetCredentialCallback iGetCredentialCallback, String str) {
            long nanoTime = System.nanoTime();
            ICancellationSignal createTransport = CancellationSignal.createTransport();
            if (getCredentialRequest.getOrigin() != null) {
                CredentialManagerService.this.mContext.enforceCallingPermission("android.permission.CREDENTIAL_MANAGER_SET_ORIGIN", null);
            }
            CredentialManagerService.this.enforcePermissionForAllowedProviders(getCredentialRequest);
            int callingUserId = UserHandle.getCallingUserId();
            int callingUid = Binder.getCallingUid();
            CredentialManagerService.this.enforceCallingPackage(str, callingUid);
            List<ProviderSession> prepareProviderSessions = prepareProviderSessions(getCredentialRequest, new PrepareGetRequestSession(CredentialManagerService.this.getContext(), CredentialManagerService.this.mSessionManager, ((AbstractMasterSystemService) CredentialManagerService.this).mLock, callingUserId, callingUid, iGetCredentialCallback, getCredentialRequest, CredentialManagerService.this.constructCallingAppInfo(str, callingUserId, getCredentialRequest.getOrigin()), getEnabledProvidersForUser(callingUserId), CancellationSignal.fromTransport(createTransport), nanoTime, iPrepareGetCredentialCallback));
            if (prepareProviderSessions.isEmpty()) {
                try {
                    iPrepareGetCredentialCallback.onResponse(new PrepareGetCredentialResponseInternal(PermissionUtils.hasPermission(CredentialManagerService.this.mContext, str, "android.permission.CREDENTIAL_MANAGER_QUERY_CANDIDATE_CREDENTIALS"), (Set) null, false, false, (PendingIntent) null));
                } catch (RemoteException e) {
                    Slog.e(CredentialManagerService.TAG, "Issue invoking onError on IGetCredentialCallback callback: " + e.getMessage());
                }
            }
            invokeProviderSessions(prepareProviderSessions);
            return createTransport;
        }

        private List<ProviderSession> prepareProviderSessions(GetCredentialRequest getCredentialRequest, GetRequestSession getRequestSession) {
            List<ProviderSession> initiateProviderSessions;
            if (CredentialManagerService.isCredentialDescriptionApiEnabled()) {
                List<CredentialOption> list = getCredentialRequest.getCredentialOptions().stream().filter(new Predicate() { // from class: com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$prepareProviderSessions$0;
                        lambda$prepareProviderSessions$0 = CredentialManagerService.CredentialManagerServiceStub.lambda$prepareProviderSessions$0((CredentialOption) obj);
                        return lambda$prepareProviderSessions$0;
                    }
                }).toList();
                List<CredentialOption> list2 = getCredentialRequest.getCredentialOptions().stream().filter(new Predicate() { // from class: com.android.server.credentials.CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$prepareProviderSessions$1;
                        lambda$prepareProviderSessions$1 = CredentialManagerService.CredentialManagerServiceStub.lambda$prepareProviderSessions$1((CredentialOption) obj);
                        return lambda$prepareProviderSessions$1;
                    }
                }).toList();
                CredentialManagerService credentialManagerService = CredentialManagerService.this;
                List initiateProviderSessionsWithActiveContainers = credentialManagerService.initiateProviderSessionsWithActiveContainers(getRequestSession, (Set<Pair<CredentialOption, CredentialDescriptionRegistry.FilterResult>>) credentialManagerService.getFilteredResultFromRegistry(list));
                List initiateProviderSessions2 = CredentialManagerService.this.initiateProviderSessions(getRequestSession, (List) list2.stream().map(new CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda3()).collect(Collectors.toList()));
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                linkedHashSet.addAll(initiateProviderSessions2);
                linkedHashSet.addAll(initiateProviderSessionsWithActiveContainers);
                initiateProviderSessions = new ArrayList<>(linkedHashSet);
            } else {
                initiateProviderSessions = CredentialManagerService.this.initiateProviderSessions(getRequestSession, (List) getCredentialRequest.getCredentialOptions().stream().map(new CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda3()).collect(Collectors.toList()));
            }
            finalizeAndEmitInitialPhaseMetric(getRequestSession);
            return initiateProviderSessions;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$prepareProviderSessions$0(CredentialOption credentialOption) {
            return credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS") != null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$prepareProviderSessions$1(CredentialOption credentialOption) {
            return credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS") == null;
        }

        private void invokeProviderSessions(List<ProviderSession> list) {
            list.forEach(new CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda0());
        }

        public ICancellationSignal executeCreateCredential(CreateCredentialRequest createCredentialRequest, ICreateCredentialCallback iCreateCredentialCallback, String str) {
            long nanoTime = System.nanoTime();
            Slog.i(CredentialManagerService.TAG, "starting executeCreateCredential with callingPackage: " + str);
            ICancellationSignal createTransport = CancellationSignal.createTransport();
            if (createCredentialRequest.getOrigin() != null) {
                CredentialManagerService.this.mContext.enforceCallingPermission("android.permission.CREDENTIAL_MANAGER_SET_ORIGIN", null);
            }
            int callingUserId = UserHandle.getCallingUserId();
            int callingUid = Binder.getCallingUid();
            CredentialManagerService.this.enforceCallingPackage(str, callingUid);
            CreateRequestSession createRequestSession = new CreateRequestSession(CredentialManagerService.this.getContext(), CredentialManagerService.this.mSessionManager, ((AbstractMasterSystemService) CredentialManagerService.this).mLock, callingUserId, callingUid, createCredentialRequest, iCreateCredentialCallback, CredentialManagerService.this.constructCallingAppInfo(str, callingUserId, createCredentialRequest.getOrigin()), getEnabledProvidersForUser(callingUserId), CredentialManagerService.getPrimaryProvidersForUserId(CredentialManagerService.this.getContext(), callingUserId), CancellationSignal.fromTransport(createTransport), nanoTime);
            CredentialManagerService.this.addSessionLocked(callingUserId, createRequestSession);
            processCreateCredential(createCredentialRequest, iCreateCredentialCallback, createRequestSession);
            return createTransport;
        }

        private void processCreateCredential(CreateCredentialRequest createCredentialRequest, ICreateCredentialCallback iCreateCredentialCallback, CreateRequestSession createRequestSession) {
            List initiateProviderSessions = CredentialManagerService.this.initiateProviderSessions(createRequestSession, List.of(createCredentialRequest.getType()));
            if (initiateProviderSessions.isEmpty()) {
                try {
                    iCreateCredentialCallback.onError("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS", "No create options available.");
                } catch (RemoteException e) {
                    Slog.e(CredentialManagerService.TAG, "Issue invoking onError on ICreateCredentialCallback callback: ", e);
                }
            }
            finalizeAndEmitInitialPhaseMetric(createRequestSession);
            initiateProviderSessions.forEach(new CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda0());
        }

        private void finalizeAndEmitInitialPhaseMetric(RequestSession requestSession) {
            try {
                InitialPhaseMetric initialPhaseMetric = requestSession.mRequestSessionMetric.getInitialPhaseMetric();
                initialPhaseMetric.setCredentialServiceBeginQueryTimeNanoseconds(System.nanoTime());
                MetricUtilities.logApiCalledInitialPhase(initialPhaseMetric, requestSession.mRequestSessionMetric.returnIncrementSequence());
            } catch (Exception e) {
                Slog.i(CredentialManagerService.TAG, "Unexpected error during metric logging: ", e);
            }
        }

        public void setEnabledProviders(List<String> list, List<String> list2, int i, ISetEnabledProvidersCallback iSetEnabledProvidersCallback) {
            int callingUid = Binder.getCallingUid();
            if (!CredentialManagerService.this.hasWriteSecureSettingsPermission()) {
                try {
                    MetricUtilities.logApiCalledSimpleV2(ApiName.SET_ENABLED_PROVIDERS, ApiStatus.FAILURE, callingUid);
                    iSetEnabledProvidersCallback.onError(CredentialManagerService.PERMISSION_DENIED_ERROR, CredentialManagerService.PERMISSION_DENIED_WRITE_SECURE_SETTINGS_ERROR);
                    return;
                } catch (RemoteException e) {
                    MetricUtilities.logApiCalledSimpleV2(ApiName.SET_ENABLED_PROVIDERS, ApiStatus.FAILURE, callingUid);
                    Slog.e(CredentialManagerService.TAG, "Issue with invoking response: ", e);
                    return;
                }
            }
            int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, false, "setEnabledProviders", null);
            HashSet hashSet = new HashSet(list2);
            hashSet.addAll(list);
            boolean putStringForUser = Settings.Secure.putStringForUser(CredentialManagerService.this.getContext().getContentResolver(), "credential_service", String.join(":", hashSet), handleIncomingUser);
            boolean putStringForUser2 = Settings.Secure.putStringForUser(CredentialManagerService.this.getContext().getContentResolver(), "credential_service_primary", String.join(":", list), handleIncomingUser);
            if (!putStringForUser || !putStringForUser2) {
                Slog.e(CredentialManagerService.TAG, "Failed to store setting containing enabled or primary providers");
                try {
                    MetricUtilities.logApiCalledSimpleV2(ApiName.SET_ENABLED_PROVIDERS, ApiStatus.FAILURE, callingUid);
                    iSetEnabledProvidersCallback.onError("failed_setting_store", "Failed to store setting containing enabled or primary providers");
                } catch (RemoteException e2) {
                    MetricUtilities.logApiCalledSimpleV2(ApiName.SET_ENABLED_PROVIDERS, ApiStatus.FAILURE, callingUid);
                    Slog.e(CredentialManagerService.TAG, "Issue with invoking error response: ", e2);
                    return;
                }
            }
            try {
                MetricUtilities.logApiCalledSimpleV2(ApiName.SET_ENABLED_PROVIDERS, ApiStatus.SUCCESS, callingUid);
                iSetEnabledProvidersCallback.onResponse();
            } catch (RemoteException e3) {
                MetricUtilities.logApiCalledSimpleV2(ApiName.SET_ENABLED_PROVIDERS, ApiStatus.FAILURE, callingUid);
                Slog.e(CredentialManagerService.TAG, "Issue with invoking response: ", e3);
            }
        }

        public boolean isEnabledCredentialProviderService(ComponentName componentName, String str) {
            Slog.i(CredentialManagerService.TAG, "isEnabledCredentialProviderService with componentName: " + componentName.flattenToString());
            int callingUserId = UserHandle.getCallingUserId();
            int callingUid = Binder.getCallingUid();
            CredentialManagerService.this.enforceCallingPackage(str, callingUid);
            synchronized (((AbstractMasterSystemService) CredentialManagerService.this).mLock) {
                for (CredentialManagerServiceImpl credentialManagerServiceImpl : CredentialManagerService.this.getServiceListForUserLocked(callingUserId)) {
                    if (credentialManagerServiceImpl.getServiceComponentName().equals(componentName)) {
                        if (!credentialManagerServiceImpl.getServicePackageName().equals(str)) {
                            MetricUtilities.logApiCalledSimpleV2(ApiName.IS_ENABLED_CREDENTIAL_PROVIDER_SERVICE, ApiStatus.FAILURE, callingUid);
                            Slog.w(CredentialManagerService.TAG, "isEnabledCredentialProviderService: Component name does not match package name.");
                            return false;
                        }
                        MetricUtilities.logApiCalledSimpleV2(ApiName.IS_ENABLED_CREDENTIAL_PROVIDER_SERVICE, ApiStatus.SUCCESS, callingUid);
                        return true;
                    }
                }
                return false;
            }
        }

        public List<CredentialProviderInfo> getCredentialProviderServices(int i, int i2) {
            CredentialManagerService.this.verifyGetProvidersPermission();
            MetricUtilities.logApiCalledSimpleV2(ApiName.GET_CREDENTIAL_PROVIDER_SERVICES, ApiStatus.SUCCESS, Binder.getCallingUid());
            return CredentialProviderInfoFactory.getCredentialProviderServices(CredentialManagerService.this.mContext, i, i2, getEnabledProvidersForUser(i), CredentialManagerService.getPrimaryProvidersForUserId(CredentialManagerService.this.mContext, i));
        }

        public List<CredentialProviderInfo> getCredentialProviderServicesForTesting(int i) {
            CredentialManagerService.this.verifyGetProvidersPermission();
            int callingUserId = UserHandle.getCallingUserId();
            return CredentialProviderInfoFactory.getCredentialProviderServicesForTesting(CredentialManagerService.this.mContext, callingUserId, i, getEnabledProvidersForUser(callingUserId), CredentialManagerService.getPrimaryProvidersForUserId(CredentialManagerService.this.mContext, callingUserId));
        }

        public boolean isServiceEnabled() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DeviceConfig.getBoolean("credential_manager", CredentialManagerService.DEVICE_CONFIG_ENABLE_CREDENTIAL_MANAGER, true);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private Set<ComponentName> getEnabledProvidersForUser(int i) {
            int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, false, "getEnabledProvidersForUser", null);
            HashSet hashSet = new HashSet();
            String stringForUser = Settings.Secure.getStringForUser(CredentialManagerService.this.mContext.getContentResolver(), "credential_service", handleIncomingUser);
            if (!TextUtils.isEmpty(stringForUser)) {
                for (String str : stringForUser.split(":")) {
                    ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
                    if (unflattenFromString != null) {
                        hashSet.add(unflattenFromString);
                    }
                }
            }
            return hashSet;
        }

        public ICancellationSignal clearCredentialState(ClearCredentialStateRequest clearCredentialStateRequest, IClearCredentialStateCallback iClearCredentialStateCallback, String str) {
            long nanoTime = System.nanoTime();
            Slog.i(CredentialManagerService.TAG, "starting clearCredentialState with callingPackage: " + str);
            int callingUserId = UserHandle.getCallingUserId();
            int callingUid = Binder.getCallingUid();
            CredentialManagerService.this.enforceCallingPackage(str, callingUid);
            ICancellationSignal createTransport = CancellationSignal.createTransport();
            ClearRequestSession clearRequestSession = new ClearRequestSession(CredentialManagerService.this.getContext(), CredentialManagerService.this.mSessionManager, ((AbstractMasterSystemService) CredentialManagerService.this).mLock, callingUserId, callingUid, iClearCredentialStateCallback, clearCredentialStateRequest, CredentialManagerService.this.constructCallingAppInfo(str, callingUserId, null), getEnabledProvidersForUser(callingUserId), CancellationSignal.fromTransport(createTransport), nanoTime);
            CredentialManagerService.this.addSessionLocked(callingUserId, clearRequestSession);
            List initiateProviderSessions = CredentialManagerService.this.initiateProviderSessions(clearRequestSession, List.of());
            if (initiateProviderSessions.isEmpty()) {
                try {
                    iClearCredentialStateCallback.onError("UNKNOWN", "No credentials available on this device");
                } catch (RemoteException e) {
                    Slog.e(CredentialManagerService.TAG, "Issue invoking onError on IClearCredentialStateCallback callback: ", e);
                }
            }
            finalizeAndEmitInitialPhaseMetric(clearRequestSession);
            initiateProviderSessions.forEach(new CredentialManagerService$CredentialManagerServiceStub$$ExternalSyntheticLambda0());
            return createTransport;
        }

        public void registerCredentialDescription(RegisterCredentialDescriptionRequest registerCredentialDescriptionRequest, String str) throws IllegalArgumentException, NonCredentialProviderCallerException {
            Slog.i(CredentialManagerService.TAG, "registerCredentialDescription with callingPackage: " + str);
            if (!CredentialManagerService.isCredentialDescriptionApiEnabled()) {
                throw new UnsupportedOperationException();
            }
            CredentialManagerService.this.enforceCallingPackage(str, Binder.getCallingUid());
            CredentialDescriptionRegistry.forUser(UserHandle.getCallingUserId()).executeRegisterRequest(registerCredentialDescriptionRequest, str);
        }

        public void unregisterCredentialDescription(UnregisterCredentialDescriptionRequest unregisterCredentialDescriptionRequest, String str) throws IllegalArgumentException {
            Slog.i(CredentialManagerService.TAG, "unregisterCredentialDescription with callingPackage: " + str);
            if (!CredentialManagerService.isCredentialDescriptionApiEnabled()) {
                throw new UnsupportedOperationException();
            }
            CredentialManagerService.this.enforceCallingPackage(str, Binder.getCallingUid());
            CredentialDescriptionRegistry.forUser(UserHandle.getCallingUserId()).executeUnregisterRequest(unregisterCredentialDescriptionRequest, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void validateGetCredentialRequest(GetCredentialRequest getCredentialRequest) {
        if (getCredentialRequest.getOrigin() != null) {
            this.mContext.enforceCallingPermission("android.permission.CREDENTIAL_MANAGER_SET_ORIGIN", null);
        }
        enforcePermissionForAllowedProviders(getCredentialRequest);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforcePermissionForAllowedProviders(GetCredentialRequest getCredentialRequest) {
        if (getCredentialRequest.getCredentialOptions().stream().anyMatch(new Predicate() { // from class: com.android.server.credentials.CredentialManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$enforcePermissionForAllowedProviders$3;
                lambda$enforcePermissionForAllowedProviders$3 = CredentialManagerService.lambda$enforcePermissionForAllowedProviders$3((CredentialOption) obj);
                return lambda$enforcePermissionForAllowedProviders$3;
            }
        })) {
            this.mContext.enforceCallingPermission("android.permission.CREDENTIAL_MANAGER_SET_ALLOWED_PROVIDERS", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$enforcePermissionForAllowedProviders$3(CredentialOption credentialOption) {
        return (credentialOption.getAllowedProviders() == null || credentialOption.getAllowedProviders().isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addSessionLocked(int i, RequestSession requestSession) {
        synchronized (((AbstractMasterSystemService) this).mLock) {
            this.mSessionManager.addSession(i, requestSession.mRequestId, requestSession);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceCallingPackage(String str, int i) {
        try {
            if (this.mContext.createContextAsUser(UserHandle.getUserHandleForUid(i), 0).getPackageManager().getPackageUid(str, PackageManager.PackageInfoFlags.of(0L)) == i) {
                return;
            }
            throw new SecurityException(str + " does not belong to uid " + i);
        } catch (PackageManager.NameNotFoundException unused) {
            throw new SecurityException(str + " not found");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class SessionManager implements RequestSession.SessionLifetime {
        private SessionManager() {
        }

        @Override // com.android.server.credentials.RequestSession.SessionLifetime
        @GuardedBy({"mLock"})
        public void onFinishRequestSession(int i, IBinder iBinder) {
            if (CredentialManagerService.this.mRequestSessions.get(i) != null) {
                ((Map) CredentialManagerService.this.mRequestSessions.get(i)).remove(iBinder);
            }
        }

        @GuardedBy({"mLock"})
        public void addSession(int i, IBinder iBinder, RequestSession requestSession) {
            if (CredentialManagerService.this.mRequestSessions.get(i) == null) {
                CredentialManagerService.this.mRequestSessions.put(i, new HashMap());
            }
            ((Map) CredentialManagerService.this.mRequestSessions.get(i)).put(iBinder, requestSession);
        }
    }
}
