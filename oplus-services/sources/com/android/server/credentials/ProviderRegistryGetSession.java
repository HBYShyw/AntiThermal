package com.android.server.credentials;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.credentials.CredentialOption;
import android.credentials.GetCredentialException;
import android.credentials.GetCredentialResponse;
import android.credentials.ui.Entry;
import android.credentials.ui.GetCredentialProviderData;
import android.credentials.ui.ProviderData;
import android.credentials.ui.ProviderPendingIntentResponse;
import android.os.ICancellationSignal;
import android.service.credentials.CallingAppInfo;
import android.service.credentials.CredentialEntry;
import android.service.credentials.GetCredentialRequest;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.credentials.CredentialDescriptionRegistry;
import com.android.server.credentials.ProviderSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ProviderRegistryGetSession extends ProviderSession<CredentialOption, Set<CredentialDescriptionRegistry.FilterResult>> {

    @VisibleForTesting
    static final String CREDENTIAL_ENTRY_KEY = "credential_key";
    private static final String TAG = "ProviderRegistryGetSession";
    private final CallingAppInfo mCallingAppInfo;
    private final CredentialDescriptionRegistry mCredentialDescriptionRegistry;

    @VisibleForTesting
    List<CredentialEntry> mCredentialEntries;
    private final String mCredentialProviderPackageName;
    private final Set<String> mElementKeys;
    private final Map<String, CredentialEntry> mUiCredentialEntries;

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderCancellable(ICancellationSignal iCancellationSignal) {
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseFailure(int i, Exception exc) {
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderResponseSuccess(Set<CredentialDescriptionRegistry.FilterResult> set) {
    }

    @Override // com.android.server.credentials.RemoteCredentialService.ProviderCallbacks
    public void onProviderServiceDied(RemoteCredentialService remoteCredentialService) {
    }

    public static ProviderRegistryGetSession createNewSession(Context context, int i, GetRequestSession getRequestSession, CallingAppInfo callingAppInfo, String str, CredentialOption credentialOption) {
        return new ProviderRegistryGetSession(context, i, getRequestSession, callingAppInfo, str, credentialOption);
    }

    public static ProviderRegistryGetSession createNewSession(Context context, int i, PrepareGetRequestSession prepareGetRequestSession, CallingAppInfo callingAppInfo, String str, CredentialOption credentialOption) {
        return new ProviderRegistryGetSession(context, i, prepareGetRequestSession, callingAppInfo, str, credentialOption);
    }

    protected ProviderRegistryGetSession(Context context, int i, GetRequestSession getRequestSession, CallingAppInfo callingAppInfo, String str, CredentialOption credentialOption) {
        super(context, credentialOption, getRequestSession, new ComponentName(str, UUID.randomUUID().toString()), i, null);
        this.mUiCredentialEntries = new HashMap();
        this.mCredentialDescriptionRegistry = CredentialDescriptionRegistry.forUser(i);
        this.mCallingAppInfo = callingAppInfo;
        this.mCredentialProviderPackageName = str;
        this.mElementKeys = new HashSet(credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS"));
        this.mStatus = ProviderSession.Status.PENDING;
    }

    protected ProviderRegistryGetSession(Context context, int i, PrepareGetRequestSession prepareGetRequestSession, CallingAppInfo callingAppInfo, String str, CredentialOption credentialOption) {
        super(context, credentialOption, prepareGetRequestSession, new ComponentName(str, UUID.randomUUID().toString()), i, null);
        this.mUiCredentialEntries = new HashMap();
        this.mCredentialDescriptionRegistry = CredentialDescriptionRegistry.forUser(i);
        this.mCallingAppInfo = callingAppInfo;
        this.mCredentialProviderPackageName = str;
        this.mElementKeys = new HashSet(credentialOption.getCredentialRetrievalData().getStringArrayList("android.credentials.GetCredentialOption.SUPPORTED_ELEMENT_KEYS"));
        this.mStatus = ProviderSession.Status.PENDING;
    }

    private List<Entry> prepareUiCredentialEntries(List<CredentialEntry> list) {
        ArrayList arrayList = new ArrayList();
        for (CredentialEntry credentialEntry : list) {
            String generateUniqueId = ProviderSession.generateUniqueId();
            this.mUiCredentialEntries.put(generateUniqueId, credentialEntry);
            arrayList.add(new Entry("credential_key", generateUniqueId, credentialEntry.getSlice(), setUpFillInIntent()));
        }
        return arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Intent setUpFillInIntent() {
        Intent intent = new Intent();
        intent.putExtra("android.service.credentials.extra.GET_CREDENTIAL_REQUEST", new GetCredentialRequest(this.mCallingAppInfo, List.of((CredentialOption) this.mProviderRequest)));
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.ProviderSession
    /* renamed from: prepareUiData */
    public ProviderData mo3151prepareUiData() {
        if (!ProviderSession.isUiInvokingStatus(getStatus())) {
            Slog.i(TAG, "No date for UI coming from: " + this.mComponentName.flattenToString());
            return null;
        }
        if (this.mProviderResponse == 0) {
            Slog.w(TAG, "response is null when preparing ui data. This is strange.");
            return null;
        }
        GetCredentialProviderData.Builder builder = new GetCredentialProviderData.Builder(this.mComponentName.flattenToString());
        List list = Collections.EMPTY_LIST;
        return builder.setActionChips(list).setAuthenticationEntries(list).setCredentialEntries(prepareUiCredentialEntries((List) ((Set) this.mProviderResponse).stream().flatMap(new Function() { // from class: com.android.server.credentials.ProviderRegistryGetSession$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Stream lambda$prepareUiData$0;
                lambda$prepareUiData$0 = ProviderRegistryGetSession.lambda$prepareUiData$0((CredentialDescriptionRegistry.FilterResult) obj);
                return lambda$prepareUiData$0;
            }
        }).collect(Collectors.toList()))).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Stream lambda$prepareUiData$0(CredentialDescriptionRegistry.FilterResult filterResult) {
        return filterResult.mCredentialEntries.stream();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.ProviderSession
    public void onUiEntrySelected(String str, String str2, ProviderPendingIntentResponse providerPendingIntentResponse) {
        str.hashCode();
        if (str.equals("credential_key")) {
            CredentialEntry credentialEntry = this.mUiCredentialEntries.get(str2);
            if (credentialEntry == null) {
                Slog.i(TAG, "Unexpected credential entry key");
                return;
            } else {
                onCredentialEntrySelected(credentialEntry, providerPendingIntentResponse);
                return;
            }
        }
        Slog.i(TAG, "Unsupported entry type selected");
    }

    private void onCredentialEntrySelected(CredentialEntry credentialEntry, ProviderPendingIntentResponse providerPendingIntentResponse) {
        if (providerPendingIntentResponse != null) {
            GetCredentialException maybeGetPendingIntentException = maybeGetPendingIntentException(providerPendingIntentResponse);
            if (maybeGetPendingIntentException != null) {
                invokeCallbackWithError(maybeGetPendingIntentException.getType(), maybeGetPendingIntentException.getMessage());
                return;
            }
            GetCredentialResponse extractGetCredentialResponse = PendingIntentResultHandler.extractGetCredentialResponse(providerPendingIntentResponse.getResultData());
            if (extractGetCredentialResponse != null) {
                ProviderSession.ProviderInternalCallback providerInternalCallback = this.mCallbacks;
                if (providerInternalCallback != null) {
                    ((GetRequestSession) providerInternalCallback).onFinalResponseReceived(this.mComponentName, extractGetCredentialResponse);
                    return;
                }
                return;
            }
        }
        Slog.w(TAG, "CredentialEntry does not have a credential or a pending intent result");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v1, types: [R, java.util.Set] */
    @Override // com.android.server.credentials.ProviderSession
    public void invokeSession() {
        startCandidateMetrics();
        ?? filteredResultForProvider = this.mCredentialDescriptionRegistry.getFilteredResultForProvider(this.mCredentialProviderPackageName, this.mElementKeys);
        this.mProviderResponse = filteredResultForProvider;
        this.mCredentialEntries = (List) ((Set) filteredResultForProvider).stream().flatMap(new Function() { // from class: com.android.server.credentials.ProviderRegistryGetSession$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Stream lambda$invokeSession$1;
                lambda$invokeSession$1 = ProviderRegistryGetSession.lambda$invokeSession$1((CredentialDescriptionRegistry.FilterResult) obj);
                return lambda$invokeSession$1;
            }
        }).collect(Collectors.toList());
        updateStatusAndInvokeCallback(ProviderSession.Status.CREDENTIALS_RECEIVED, ProviderSession.CredentialsSource.REGISTRY);
        this.mProviderSessionMetric.collectCandidateEntryMetrics(this.mCredentialEntries);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Stream lambda$invokeSession$1(CredentialDescriptionRegistry.FilterResult filterResult) {
        return filterResult.mCredentialEntries.stream();
    }

    protected GetCredentialException maybeGetPendingIntentException(ProviderPendingIntentResponse providerPendingIntentResponse) {
        if (providerPendingIntentResponse == null) {
            return null;
        }
        if (PendingIntentResultHandler.isValidResponse(providerPendingIntentResponse)) {
            GetCredentialException extractGetCredentialException = PendingIntentResultHandler.extractGetCredentialException(providerPendingIntentResponse.getResultData());
            if (extractGetCredentialException == null) {
                return null;
            }
            Slog.i(TAG, "Pending intent contains provider exception");
            return extractGetCredentialException;
        }
        if (PendingIntentResultHandler.isCancelledResponse(providerPendingIntentResponse)) {
            return new GetCredentialException("android.credentials.GetCredentialException.TYPE_USER_CANCELED");
        }
        return new GetCredentialException("android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL");
    }
}
