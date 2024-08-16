package com.android.server.credentials;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.credentials.CredentialOption;
import android.credentials.CredentialProviderInfo;
import android.credentials.GetCredentialException;
import android.credentials.GetCredentialRequest;
import android.credentials.GetCredentialResponse;
import android.credentials.ui.AuthenticationEntry;
import android.credentials.ui.Entry;
import android.credentials.ui.GetCredentialProviderData;
import android.credentials.ui.ProviderPendingIntentResponse;
import android.os.ICancellationSignal;
import android.os.Parcelable;
import android.service.credentials.Action;
import android.service.credentials.BeginGetCredentialOption;
import android.service.credentials.BeginGetCredentialRequest;
import android.service.credentials.BeginGetCredentialResponse;
import android.service.credentials.CallingAppInfo;
import android.service.credentials.CredentialEntry;
import android.service.credentials.RemoteEntry;
import android.util.Pair;
import android.util.Slog;
import com.android.server.credentials.ProviderGetSession;
import com.android.server.credentials.ProviderSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ProviderGetSession extends ProviderSession<BeginGetCredentialRequest, BeginGetCredentialResponse> {
    public static final String ACTION_ENTRY_KEY = "action_key";
    public static final String AUTHENTICATION_ACTION_ENTRY_KEY = "authentication_action_key";
    public static final String CREDENTIAL_ENTRY_KEY = "credential_key";
    public static final String REMOTE_ENTRY_KEY = "remote_entry_key";
    private static final String TAG = "ProviderGetSession";
    private final Map<String, CredentialOption> mBeginGetOptionToCredentialOptionMap;
    private final CallingAppInfo mCallingAppInfo;
    private final GetCredentialRequest mCompleteRequest;
    private GetCredentialException mProviderException;
    private final ProviderResponseDataHandler mProviderResponseDataHandler;

    /* JADX WARN: Multi-variable type inference failed */
    public static ProviderGetSession createNewSession(Context context, int i, CredentialProviderInfo credentialProviderInfo, GetRequestSession getRequestSession, RemoteCredentialService remoteCredentialService) {
        GetCredentialRequest filterOptions = filterOptions(credentialProviderInfo.getCapabilities(), (GetCredentialRequest) getRequestSession.mClientRequest, credentialProviderInfo);
        if (filterOptions != null) {
            HashMap hashMap = new HashMap();
            return new ProviderGetSession(context, credentialProviderInfo, getRequestSession, i, remoteCredentialService, constructQueryPhaseRequest(filterOptions, getRequestSession.mClientAppInfo, ((GetCredentialRequest) getRequestSession.mClientRequest).alwaysSendAppInfoToProvider(), hashMap), filterOptions, getRequestSession.mClientAppInfo, hashMap, getRequestSession.mHybridService);
        }
        Slog.i(TAG, "Unable to create provider session for: " + credentialProviderInfo.getComponentName());
        return null;
    }

    private static BeginGetCredentialRequest constructQueryPhaseRequest(GetCredentialRequest getCredentialRequest, CallingAppInfo callingAppInfo, boolean z, final Map<String, CredentialOption> map) {
        final BeginGetCredentialRequest.Builder builder = new BeginGetCredentialRequest.Builder();
        getCredentialRequest.getCredentialOptions().forEach(new Consumer() { // from class: com.android.server.credentials.ProviderGetSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ProviderGetSession.lambda$constructQueryPhaseRequest$0(builder, map, (CredentialOption) obj);
            }
        });
        if (z) {
            builder.setCallingAppInfo(callingAppInfo);
        }
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$constructQueryPhaseRequest$0(BeginGetCredentialRequest.Builder builder, Map map, CredentialOption credentialOption) {
        String generateUniqueId = ProviderSession.generateUniqueId();
        builder.addBeginGetCredentialOption(new BeginGetCredentialOption(generateUniqueId, credentialOption.getType(), credentialOption.getCandidateQueryData()));
        map.put(generateUniqueId, credentialOption);
    }

    private static GetCredentialRequest filterOptions(List<String> list, GetCredentialRequest getCredentialRequest, CredentialProviderInfo credentialProviderInfo) {
        Slog.i(TAG, "Filtering request options for: " + credentialProviderInfo.getComponentName());
        ArrayList arrayList = new ArrayList();
        for (CredentialOption credentialOption : getCredentialRequest.getCredentialOptions()) {
            if (list.contains(credentialOption.getType()) && isProviderAllowed(credentialOption, credentialProviderInfo) && checkSystemProviderRequirement(credentialOption, credentialProviderInfo.isSystemProvider())) {
                Slog.i(TAG, "Option of type: " + credentialOption.getType() + " meets all filteringconditions");
                arrayList.add(credentialOption);
            }
        }
        if (!arrayList.isEmpty()) {
            return new GetCredentialRequest.Builder(getCredentialRequest.getData()).setCredentialOptions(arrayList).build();
        }
        Slog.i(TAG, "No options filtered");
        return null;
    }

    private static boolean isProviderAllowed(CredentialOption credentialOption, CredentialProviderInfo credentialProviderInfo) {
        if (credentialProviderInfo.isSystemProvider() || credentialOption.getAllowedProviders().isEmpty() || credentialOption.getAllowedProviders().contains(credentialProviderInfo.getComponentName())) {
            return true;
        }
        Slog.i(TAG, "Provider allow list specified but does not contain this provider");
        return false;
    }

    private static boolean checkSystemProviderRequirement(CredentialOption credentialOption, boolean z) {
        if (!credentialOption.isSystemProviderRequired() || z) {
            return true;
        }
        Slog.i(TAG, "System provider required, but this service is not a system provider");
        return false;
    }

    public ProviderGetSession(Context context, CredentialProviderInfo credentialProviderInfo, ProviderSession.ProviderInternalCallback<GetCredentialResponse> providerInternalCallback, int i, RemoteCredentialService remoteCredentialService, BeginGetCredentialRequest beginGetCredentialRequest, GetCredentialRequest getCredentialRequest, CallingAppInfo callingAppInfo, Map<String, CredentialOption> map, String str) {
        super(context, beginGetCredentialRequest, providerInternalCallback, credentialProviderInfo.getComponentName(), i, remoteCredentialService);
        this.mCompleteRequest = getCredentialRequest;
        this.mCallingAppInfo = callingAppInfo;
        setStatus(ProviderSession.Status.PENDING);
        this.mBeginGetOptionToCredentialOptionMap = new HashMap(map);
        this.mProviderResponseDataHandler = new ProviderResponseDataHandler(ComponentName.unflattenFromString(str));
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseSuccess(BeginGetCredentialResponse beginGetCredentialResponse) {
        Slog.i(TAG, "Remote provider responded with a valid response: " + this.mComponentName);
        onSetInitialRemoteResponse(beginGetCredentialResponse);
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseFailure(int i, Exception exc) {
        if (exc instanceof GetCredentialException) {
            GetCredentialException getCredentialException = (GetCredentialException) exc;
            this.mProviderException = getCredentialException;
            this.mProviderSessionMetric.collectCandidateFrameworkException(getCredentialException.getType());
        }
        this.mProviderSessionMetric.collectCandidateExceptionStatus(true);
        updateStatusAndInvokeCallback(ProviderSession.Status.CANCELED, ProviderSession.CredentialsSource.REMOTE_PROVIDER);
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderServiceDied(RemoteCredentialService remoteCredentialService) {
        if (remoteCredentialService.getComponentName().equals(this.mComponentName)) {
            updateStatusAndInvokeCallback(ProviderSession.Status.SERVICE_DEAD, ProviderSession.CredentialsSource.REMOTE_PROVIDER);
        } else {
            Slog.w(TAG, "Component names different in onProviderServiceDied - this should not happen");
        }
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderCancellable(ICancellationSignal iCancellationSignal) {
        this.mProviderCancellationSignal = iCancellationSignal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.ProviderSession
    public void onUiEntrySelected(String str, String str2, ProviderPendingIntentResponse providerPendingIntentResponse) {
        Slog.i(TAG, "onUiEntrySelected with entryType: " + str + ", and entryKey: " + str2);
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 1110515801:
                if (str.equals(REMOTE_ENTRY_KEY)) {
                    c = 0;
                    break;
                }
                break;
            case 1182857469:
                if (str.equals(AUTHENTICATION_ACTION_ENTRY_KEY)) {
                    c = 1;
                    break;
                }
                break;
            case 1208398455:
                if (str.equals(CREDENTIAL_ENTRY_KEY)) {
                    c = 2;
                    break;
                }
                break;
            case 1852195030:
                if (str.equals(ACTION_ENTRY_KEY)) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (this.mProviderResponseDataHandler.getRemoteEntry(str2) != null) {
                    onRemoteEntrySelected(providerPendingIntentResponse);
                    return;
                } else {
                    Slog.i(TAG, "Unexpected remote entry key");
                    invokeCallbackOnInternalInvalidState();
                    return;
                }
            case 1:
                Action authenticationAction = this.mProviderResponseDataHandler.getAuthenticationAction(str2);
                this.mProviderSessionMetric.createAuthenticationBrowsingMetric();
                if (authenticationAction == null) {
                    Slog.i(TAG, "Unexpected authenticationEntry key");
                    invokeCallbackOnInternalInvalidState();
                    return;
                } else {
                    if (onAuthenticationEntrySelected(providerPendingIntentResponse)) {
                        Slog.i(TAG, "Additional content received - removing authentication entry");
                        this.mProviderResponseDataHandler.removeAuthenticationAction(str2);
                        if (this.mProviderResponseDataHandler.isEmptyResponse()) {
                            return;
                        }
                        updateStatusAndInvokeCallback(ProviderSession.Status.CREDENTIALS_RECEIVED, ProviderSession.CredentialsSource.AUTH_ENTRY);
                        return;
                    }
                    Slog.i(TAG, "Additional content not received from authentication entry");
                    this.mProviderResponseDataHandler.updateAuthEntryWithNoCredentialsReceived(str2);
                    updateStatusAndInvokeCallback(ProviderSession.Status.NO_CREDENTIALS_FROM_AUTH_ENTRY, ProviderSession.CredentialsSource.AUTH_ENTRY);
                    return;
                }
            case 2:
                if (this.mProviderResponseDataHandler.getCredentialEntry(str2) == null) {
                    Slog.i(TAG, "Unexpected credential entry key");
                    invokeCallbackOnInternalInvalidState();
                    return;
                } else {
                    onCredentialEntrySelected(providerPendingIntentResponse);
                    return;
                }
            case 3:
                if (this.mProviderResponseDataHandler.getActionEntry(str2) == null) {
                    Slog.i(TAG, "Unexpected action entry key");
                    invokeCallbackOnInternalInvalidState();
                    return;
                } else {
                    onActionEntrySelected(providerPendingIntentResponse);
                    return;
                }
            default:
                Slog.i(TAG, "Unsupported entry type selected");
                invokeCallbackOnInternalInvalidState();
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.credentials.ProviderSession
    public void invokeSession() {
        if (this.mRemoteCredentialService != null) {
            startCandidateMetrics();
            this.mRemoteCredentialService.setCallback(this);
            this.mRemoteCredentialService.onBeginGetCredential((BeginGetCredentialRequest) this.mProviderRequest);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<String> getCredentialEntryTypes() {
        return this.mProviderResponseDataHandler.getCredentialEntryTypes();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.ProviderSession
    /* renamed from: prepareUiData, reason: merged with bridge method [inline-methods] */
    public GetCredentialProviderData mo3151prepareUiData() throws IllegalArgumentException {
        if (!ProviderSession.isUiInvokingStatus(getStatus())) {
            Slog.i(TAG, "No data for UI from: " + this.mComponentName.flattenToString());
            return null;
        }
        if (this.mProviderResponse == 0 || this.mProviderResponseDataHandler.isEmptyResponse()) {
            return null;
        }
        return this.mProviderResponseDataHandler.toGetCredentialProviderData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent setUpFillInIntentWithFinalRequest(String str) {
        if (this.mBeginGetOptionToCredentialOptionMap.get(str) == null) {
            Slog.w(TAG, "Id from Credential Entry does not resolve to a valid option");
            return new Intent();
        }
        return new Intent().putExtra("android.service.credentials.extra.GET_CREDENTIAL_REQUEST", new android.service.credentials.GetCredentialRequest(this.mCallingAppInfo, List.of(this.mBeginGetOptionToCredentialOptionMap.get(str))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent setUpFillInIntentWithQueryRequest() {
        Intent intent = new Intent();
        intent.putExtra("android.service.credentials.extra.BEGIN_GET_CREDENTIAL_REQUEST", (Parcelable) this.mProviderRequest);
        return intent;
    }

    private void onRemoteEntrySelected(ProviderPendingIntentResponse providerPendingIntentResponse) {
        onCredentialEntrySelected(providerPendingIntentResponse);
    }

    private void onCredentialEntrySelected(ProviderPendingIntentResponse providerPendingIntentResponse) {
        if (providerPendingIntentResponse == null) {
            invokeCallbackOnInternalInvalidState();
            return;
        }
        GetCredentialException maybeGetPendingIntentException = maybeGetPendingIntentException(providerPendingIntentResponse);
        if (maybeGetPendingIntentException != null) {
            invokeCallbackWithError(maybeGetPendingIntentException.getType(), maybeGetPendingIntentException.getMessage());
            return;
        }
        GetCredentialResponse extractGetCredentialResponse = PendingIntentResultHandler.extractGetCredentialResponse(providerPendingIntentResponse.getResultData());
        if (extractGetCredentialResponse != null) {
            this.mCallbacks.onFinalResponseReceived(this.mComponentName, extractGetCredentialResponse);
        } else {
            Slog.i(TAG, "Pending intent response contains no credential, or error for a credential entry");
            invokeCallbackOnInternalInvalidState();
        }
    }

    private GetCredentialException maybeGetPendingIntentException(ProviderPendingIntentResponse providerPendingIntentResponse) {
        if (providerPendingIntentResponse == null) {
            return null;
        }
        if (PendingIntentResultHandler.isValidResponse(providerPendingIntentResponse)) {
            GetCredentialException extractGetCredentialException = PendingIntentResultHandler.extractGetCredentialException(providerPendingIntentResponse.getResultData());
            if (extractGetCredentialException != null) {
                return extractGetCredentialException;
            }
            return null;
        }
        if (PendingIntentResultHandler.isCancelledResponse(providerPendingIntentResponse)) {
            return new GetCredentialException("android.credentials.GetCredentialException.TYPE_USER_CANCELED");
        }
        return new GetCredentialException("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL");
    }

    private boolean onAuthenticationEntrySelected(ProviderPendingIntentResponse providerPendingIntentResponse) {
        if (providerPendingIntentResponse == null) {
            return false;
        }
        GetCredentialException maybeGetPendingIntentException = maybeGetPendingIntentException(providerPendingIntentResponse);
        if (maybeGetPendingIntentException != null) {
            this.mProviderSessionMetric.collectAuthenticationExceptionStatus(true);
            invokeCallbackWithError(maybeGetPendingIntentException.getType(), maybeGetPendingIntentException.getMessage());
            return true;
        }
        BeginGetCredentialResponse extractResponseContent = PendingIntentResultHandler.extractResponseContent(providerPendingIntentResponse.getResultData());
        this.mProviderSessionMetric.collectCandidateEntryMetrics(extractResponseContent, true, null);
        if (extractResponseContent == null || this.mProviderResponseDataHandler.isEmptyResponse(extractResponseContent)) {
            return false;
        }
        addToInitialRemoteResponse(extractResponseContent, false);
        return true;
    }

    private void addToInitialRemoteResponse(BeginGetCredentialResponse beginGetCredentialResponse, boolean z) {
        if (beginGetCredentialResponse == null) {
            return;
        }
        this.mProviderResponseDataHandler.addResponseContent(beginGetCredentialResponse.getCredentialEntries(), beginGetCredentialResponse.getActions(), beginGetCredentialResponse.getAuthenticationActions(), beginGetCredentialResponse.getRemoteCredentialEntry(), z);
    }

    private void onActionEntrySelected(ProviderPendingIntentResponse providerPendingIntentResponse) {
        Slog.i(TAG, "onActionEntrySelected");
        onCredentialEntrySelected(providerPendingIntentResponse);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void onSetInitialRemoteResponse(BeginGetCredentialResponse beginGetCredentialResponse) {
        this.mProviderResponse = beginGetCredentialResponse;
        addToInitialRemoteResponse(beginGetCredentialResponse, true);
        if (this.mProviderResponseDataHandler.isEmptyResponse(beginGetCredentialResponse)) {
            this.mProviderSessionMetric.collectCandidateEntryMetrics(beginGetCredentialResponse, false, null);
            updateStatusAndInvokeCallback(ProviderSession.Status.EMPTY_RESPONSE, ProviderSession.CredentialsSource.REMOTE_PROVIDER);
        } else {
            this.mProviderSessionMetric.collectCandidateEntryMetrics(beginGetCredentialResponse, false, null);
            updateStatusAndInvokeCallback(ProviderSession.Status.CREDENTIALS_RECEIVED, ProviderSession.CredentialsSource.REMOTE_PROVIDER);
        }
    }

    private void invokeCallbackOnInternalInvalidState() {
        this.mCallbacks.onFinalErrorReceived(this.mComponentName, "android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL", null);
    }

    public void updateAuthEntriesStatusFromAnotherSession() {
        this.mProviderResponseDataHandler.updateAuthEntryWithNoCredentialsReceived(null);
    }

    public boolean containsEmptyAuthEntriesOnly() {
        return this.mProviderResponseDataHandler.mUiCredentialEntries.isEmpty() && this.mProviderResponseDataHandler.mUiRemoteEntry == null && this.mProviderResponseDataHandler.mUiAuthenticationEntries.values().stream().allMatch(new Predicate() { // from class: com.android.server.credentials.ProviderGetSession$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$containsEmptyAuthEntriesOnly$1;
                lambda$containsEmptyAuthEntriesOnly$1 = ProviderGetSession.lambda$containsEmptyAuthEntriesOnly$1((Pair) obj);
                return lambda$containsEmptyAuthEntriesOnly$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$containsEmptyAuthEntriesOnly$1(Pair pair) {
        return ((AuthenticationEntry) pair.second).getStatus() == 1 || ((AuthenticationEntry) pair.second).getStatus() == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ProviderResponseDataHandler {
        private final ComponentName mExpectedRemoteEntryProviderService;
        private final Map<String, Pair<CredentialEntry, Entry>> mUiCredentialEntries = new HashMap();
        private final Map<String, Pair<Action, Entry>> mUiActionsEntries = new HashMap();
        private final Map<String, Pair<Action, AuthenticationEntry>> mUiAuthenticationEntries = new HashMap();
        private final Set<String> mCredentialEntryTypes = new HashSet();
        private Pair<String, Pair<RemoteEntry, Entry>> mUiRemoteEntry = null;

        ProviderResponseDataHandler(ComponentName componentName) {
            this.mExpectedRemoteEntryProviderService = componentName;
        }

        public void addResponseContent(List<CredentialEntry> list, List<Action> list2, List<Action> list3, RemoteEntry remoteEntry, boolean z) {
            list.forEach(new Consumer() { // from class: com.android.server.credentials.ProviderGetSession$ProviderResponseDataHandler$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ProviderGetSession.ProviderResponseDataHandler.this.addCredentialEntry((CredentialEntry) obj);
                }
            });
            list2.forEach(new Consumer() { // from class: com.android.server.credentials.ProviderGetSession$ProviderResponseDataHandler$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ProviderGetSession.ProviderResponseDataHandler.this.addAction((Action) obj);
                }
            });
            list3.forEach(new Consumer() { // from class: com.android.server.credentials.ProviderGetSession$ProviderResponseDataHandler$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ProviderGetSession.ProviderResponseDataHandler.this.lambda$addResponseContent$0((Action) obj);
                }
            });
            if (remoteEntry == null && z) {
                return;
            }
            setRemoteEntry(remoteEntry);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addResponseContent$0(Action action) {
            addAuthenticationAction(action, 0);
        }

        public void addCredentialEntry(CredentialEntry credentialEntry) {
            String generateUniqueId = ProviderSession.generateUniqueId();
            this.mUiCredentialEntries.put(generateUniqueId, new Pair<>(credentialEntry, new Entry(ProviderGetSession.CREDENTIAL_ENTRY_KEY, generateUniqueId, credentialEntry.getSlice(), ProviderGetSession.this.setUpFillInIntentWithFinalRequest(credentialEntry.getBeginGetCredentialOptionId()))));
            this.mCredentialEntryTypes.add(credentialEntry.getType());
        }

        public void addAction(Action action) {
            String generateUniqueId = ProviderSession.generateUniqueId();
            this.mUiActionsEntries.put(generateUniqueId, new Pair<>(action, new Entry(ProviderGetSession.ACTION_ENTRY_KEY, generateUniqueId, action.getSlice(), ProviderGetSession.this.setUpFillInIntentWithQueryRequest())));
        }

        public void addAuthenticationAction(Action action, int i) {
            String generateUniqueId = ProviderSession.generateUniqueId();
            this.mUiAuthenticationEntries.put(generateUniqueId, new Pair<>(action, new AuthenticationEntry(ProviderGetSession.AUTHENTICATION_ACTION_ENTRY_KEY, generateUniqueId, action.getSlice(), i, ProviderGetSession.this.setUpFillInIntentWithQueryRequest())));
        }

        public void removeAuthenticationAction(String str) {
            this.mUiAuthenticationEntries.remove(str);
        }

        public void setRemoteEntry(RemoteEntry remoteEntry) {
            if (!ProviderGetSession.this.enforceRemoteEntryRestrictions(this.mExpectedRemoteEntryProviderService)) {
                Slog.w(ProviderGetSession.TAG, "Remote entry being dropped as it does not meet the restriction checks.");
            } else if (remoteEntry == null) {
                this.mUiRemoteEntry = null;
            } else {
                String generateUniqueId = ProviderSession.generateUniqueId();
                this.mUiRemoteEntry = new Pair<>(generateUniqueId, new Pair(remoteEntry, new Entry(ProviderGetSession.REMOTE_ENTRY_KEY, generateUniqueId, remoteEntry.getSlice(), ProviderGetSession.this.setUpFillInIntentForRemoteEntry())));
            }
        }

        public GetCredentialProviderData toGetCredentialProviderData() {
            return new GetCredentialProviderData.Builder(ProviderGetSession.this.mComponentName.flattenToString()).setActionChips(prepareActionEntries()).setCredentialEntries(prepareCredentialEntries()).setAuthenticationEntries(prepareAuthenticationEntries()).setRemoteEntry(prepareRemoteEntry()).build();
        }

        private List<Entry> prepareActionEntries() {
            ArrayList arrayList = new ArrayList();
            Iterator<String> it = this.mUiActionsEntries.keySet().iterator();
            while (it.hasNext()) {
                arrayList.add((Entry) this.mUiActionsEntries.get(it.next()).second);
            }
            return arrayList;
        }

        private List<AuthenticationEntry> prepareAuthenticationEntries() {
            ArrayList arrayList = new ArrayList();
            Iterator<String> it = this.mUiAuthenticationEntries.keySet().iterator();
            while (it.hasNext()) {
                arrayList.add((AuthenticationEntry) this.mUiAuthenticationEntries.get(it.next()).second);
            }
            return arrayList;
        }

        private List<Entry> prepareCredentialEntries() {
            ArrayList arrayList = new ArrayList();
            Iterator<String> it = this.mUiCredentialEntries.keySet().iterator();
            while (it.hasNext()) {
                arrayList.add((Entry) this.mUiCredentialEntries.get(it.next()).second);
            }
            return arrayList;
        }

        private Entry prepareRemoteEntry() {
            Object obj;
            Pair<String, Pair<RemoteEntry, Entry>> pair = this.mUiRemoteEntry;
            if (pair == null || pair.first == null || (obj = pair.second) == null) {
                return null;
            }
            return (Entry) ((Pair) obj).second;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmptyResponse() {
            return this.mUiCredentialEntries.isEmpty() && this.mUiActionsEntries.isEmpty() && this.mUiAuthenticationEntries.isEmpty() && this.mUiRemoteEntry == null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmptyResponse(BeginGetCredentialResponse beginGetCredentialResponse) {
            return beginGetCredentialResponse.getCredentialEntries().isEmpty() && beginGetCredentialResponse.getActions().isEmpty() && beginGetCredentialResponse.getAuthenticationActions().isEmpty() && beginGetCredentialResponse.getRemoteCredentialEntry() == null;
        }

        public Set<String> getCredentialEntryTypes() {
            return this.mCredentialEntryTypes;
        }

        public Action getAuthenticationAction(String str) {
            if (this.mUiAuthenticationEntries.get(str) == null) {
                return null;
            }
            return (Action) this.mUiAuthenticationEntries.get(str).first;
        }

        public Action getActionEntry(String str) {
            if (this.mUiActionsEntries.get(str) == null) {
                return null;
            }
            return (Action) this.mUiActionsEntries.get(str).first;
        }

        public RemoteEntry getRemoteEntry(String str) {
            Object obj;
            if (!((String) this.mUiRemoteEntry.first).equals(str) || (obj = this.mUiRemoteEntry.second) == null) {
                return null;
            }
            return (RemoteEntry) ((Pair) obj).first;
        }

        public CredentialEntry getCredentialEntry(String str) {
            if (this.mUiCredentialEntries.get(str) == null) {
                return null;
            }
            return (CredentialEntry) this.mUiCredentialEntries.get(str).first;
        }

        public void updateAuthEntryWithNoCredentialsReceived(String str) {
            if (str == null) {
                updatePreviousMostRecentAuthEntry();
            } else {
                updatePreviousMostRecentAuthEntry();
                updateMostRecentAuthEntry(str);
            }
        }

        private void updateMostRecentAuthEntry(String str) {
            AuthenticationEntry authenticationEntry = (AuthenticationEntry) this.mUiAuthenticationEntries.get(str).second;
            this.mUiAuthenticationEntries.put(str, new Pair<>((Action) this.mUiAuthenticationEntries.get(str).first, copyAuthEntryAndChangeStatus(authenticationEntry, 2)));
        }

        private void updatePreviousMostRecentAuthEntry() {
            Optional<Map.Entry<String, Pair<Action, AuthenticationEntry>>> findFirst = this.mUiAuthenticationEntries.entrySet().stream().filter(new Predicate() { // from class: com.android.server.credentials.ProviderGetSession$ProviderResponseDataHandler$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$updatePreviousMostRecentAuthEntry$1;
                    lambda$updatePreviousMostRecentAuthEntry$1 = ProviderGetSession.ProviderResponseDataHandler.lambda$updatePreviousMostRecentAuthEntry$1((Map.Entry) obj);
                    return lambda$updatePreviousMostRecentAuthEntry$1;
                }
            }).findFirst();
            if (findFirst.isEmpty()) {
                return;
            }
            String key = findFirst.get().getKey();
            this.mUiAuthenticationEntries.remove(key);
            this.mUiAuthenticationEntries.put(key, new Pair<>((Action) findFirst.get().getValue().first, copyAuthEntryAndChangeStatus((AuthenticationEntry) findFirst.get().getValue().second, 1)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$updatePreviousMostRecentAuthEntry$1(Map.Entry entry) {
            return ((AuthenticationEntry) ((Pair) entry.getValue()).second).getStatus() == 2;
        }

        private AuthenticationEntry copyAuthEntryAndChangeStatus(AuthenticationEntry authenticationEntry, Integer num) {
            return new AuthenticationEntry(ProviderGetSession.AUTHENTICATION_ACTION_ENTRY_KEY, authenticationEntry.getSubkey(), authenticationEntry.getSlice(), num.intValue(), authenticationEntry.getFrameworkExtrasIntent());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent setUpFillInIntentForRemoteEntry() {
        return new Intent().putExtra("android.service.credentials.extra.GET_CREDENTIAL_REQUEST", new android.service.credentials.GetCredentialRequest(this.mCallingAppInfo, this.mCompleteRequest.getCredentialOptions()));
    }
}
