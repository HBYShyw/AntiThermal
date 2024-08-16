package com.android.server.credentials;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.credentials.CreateCredentialRequest;
import android.credentials.CreateCredentialResponse;
import android.credentials.CredentialProviderInfo;
import android.credentials.ICreateCredentialCallback;
import android.credentials.ui.ProviderData;
import android.credentials.ui.RequestInfo;
import android.credentials.ui.UserSelectionDialogResult;
import android.os.CancellationSignal;
import android.os.RemoteException;
import android.service.credentials.CallingAppInfo;
import android.service.credentials.PermissionUtils;
import android.util.Slog;
import com.android.server.credentials.CredentialManagerUi;
import com.android.server.credentials.ProviderSession;
import com.android.server.credentials.RequestSession;
import com.android.server.credentials.metrics.ProviderStatusForMetrics;
import java.util.ArrayList;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CreateRequestSession extends RequestSession<CreateCredentialRequest, ICreateCredentialCallback, CreateCredentialResponse> implements ProviderSession.ProviderInternalCallback<CreateCredentialResponse> {
    private static final String TAG = "CreateRequestSession";
    private final Set<String> mPrimaryProviders;

    @Override // com.android.server.credentials.RequestSession
    public /* bridge */ /* synthetic */ void addProviderSession(ComponentName componentName, ProviderSession providerSession) {
        super.addProviderSession(componentName, providerSession);
    }

    @Override // com.android.server.credentials.RequestSession, com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public /* bridge */ /* synthetic */ void onUiSelection(UserSelectionDialogResult userSelectionDialogResult) {
        super.onUiSelection(userSelectionDialogResult);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CreateRequestSession(Context context, RequestSession.SessionLifetime sessionLifetime, Object obj, int i, int i2, CreateCredentialRequest createCredentialRequest, ICreateCredentialCallback iCreateCredentialCallback, CallingAppInfo callingAppInfo, Set<ComponentName> set, Set<String> set2, CancellationSignal cancellationSignal, long j) {
        super(context, sessionLifetime, obj, i, i2, createCredentialRequest, iCreateCredentialCallback, "android.credentials.ui.TYPE_CREATE", callingAppInfo, set, cancellationSignal, j);
        this.mRequestSessionMetric.collectCreateFlowInitialMetricInfo(createCredentialRequest.getOrigin() != null, createCredentialRequest);
        this.mPrimaryProviders = set2;
    }

    @Override // com.android.server.credentials.RequestSession
    public ProviderSession initiateProviderSession(CredentialProviderInfo credentialProviderInfo, RemoteCredentialService remoteCredentialService) {
        ProviderCreateSession createNewSession = ProviderCreateSession.createNewSession(this.mContext, this.mUserId, credentialProviderInfo, this, remoteCredentialService);
        if (createNewSession != null) {
            Slog.i(TAG, "Provider session created and being added for: " + credentialProviderInfo.getComponentName());
            this.mProviders.put(createNewSession.getComponentName().flattenToString(), createNewSession);
        }
        return createNewSession;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.credentials.RequestSession
    protected void launchUiWithProviderData(ArrayList<ProviderData> arrayList) {
        this.mRequestSessionMetric.collectUiCallStartTime(System.nanoTime());
        this.mCredentialManagerUi.setStatus(CredentialManagerUi.UiStatus.USER_INTERACTION);
        cancelExistingPendingIntent();
        try {
            PendingIntent createPendingIntent = this.mCredentialManagerUi.createPendingIntent(RequestInfo.newCreateRequestInfo(this.mRequestId, (CreateCredentialRequest) this.mClientRequest, this.mClientAppInfo.getPackageName(), PermissionUtils.hasPermission(this.mContext, this.mClientAppInfo.getPackageName(), "android.permission.CREDENTIAL_MANAGER_SET_ALLOWED_PROVIDERS"), new ArrayList(this.mPrimaryProviders)), arrayList);
            this.mPendingIntent = createPendingIntent;
            ((ICreateCredentialCallback) this.mClientCallback).onPendingIntent(createPendingIntent);
        } catch (RemoteException unused) {
            this.mRequestSessionMetric.collectUiReturnedFinalPhase(false);
            this.mCredentialManagerUi.setStatus(CredentialManagerUi.UiStatus.TERMINATED);
            respondToClientWithErrorAndFinish("android.credentials.CreateCredentialException.TYPE_UNKNOWN", "Unable to invoke selector");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.RequestSession
    public void invokeClientCallbackSuccess(CreateCredentialResponse createCredentialResponse) throws RemoteException {
        ((ICreateCredentialCallback) this.mClientCallback).onResponse(createCredentialResponse);
    }

    @Override // com.android.server.credentials.RequestSession
    protected void invokeClientCallbackError(String str, String str2) throws RemoteException {
        ((ICreateCredentialCallback) this.mClientCallback).onError(str, str2);
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalResponseReceived(ComponentName componentName, CreateCredentialResponse createCredentialResponse) {
        Slog.i(TAG, "Final credential received from: " + componentName.flattenToString());
        this.mRequestSessionMetric.collectUiResponseData(true, System.nanoTime());
        this.mRequestSessionMetric.updateMetricsOnResponseReceived(this.mProviders, componentName, isPrimaryProviderViaProviderInfo(componentName));
        if (createCredentialResponse != null) {
            this.mRequestSessionMetric.collectChosenProviderStatus(ProviderStatusForMetrics.FINAL_SUCCESS.getMetricCode());
            respondToClientWithResponseAndFinish(createCredentialResponse);
        } else {
            this.mRequestSessionMetric.collectChosenProviderStatus(ProviderStatusForMetrics.FINAL_FAILURE.getMetricCode());
            this.mRequestSessionMetric.collectFrameworkException("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS");
            respondToClientWithErrorAndFinish("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS", "Invalid response");
        }
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalErrorReceived(ComponentName componentName, String str, String str2) {
        respondToClientWithErrorAndFinish(str, str2);
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiCancellation(boolean z) {
        String str;
        String str2;
        if (z) {
            str = "android.credentials.CreateCredentialException.TYPE_USER_CANCELED";
            str2 = "User cancelled the selector";
        } else {
            str = "android.credentials.CreateCredentialException.TYPE_INTERRUPTED";
            str2 = "The UI was interrupted - please try again.";
        }
        this.mRequestSessionMetric.collectFrameworkException(str);
        respondToClientWithErrorAndFinish(str, str2);
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiSelectorInvocationFailure() {
        this.mRequestSessionMetric.collectFrameworkException("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS");
        respondToClientWithErrorAndFinish("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS", "No create options available.");
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onProviderStatusChanged(ProviderSession.Status status, ComponentName componentName, ProviderSession.CredentialsSource credentialsSource) {
        Slog.i(TAG, "Provider status changed: " + status + ", and source: " + credentialsSource);
        if (isAnyProviderPending()) {
            return;
        }
        if (isUiInvocationNeeded()) {
            Slog.i(TAG, "Provider status changed - ui invocation is needed");
            getProviderDataAndInitiateUi();
        } else {
            this.mRequestSessionMetric.collectFrameworkException("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS");
            respondToClientWithErrorAndFinish("android.credentials.CreateCredentialException.TYPE_NO_CREATE_OPTIONS", "No create options available.");
        }
    }
}
