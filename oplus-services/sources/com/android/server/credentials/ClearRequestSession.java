package com.android.server.credentials;

import android.content.ComponentName;
import android.content.Context;
import android.credentials.ClearCredentialStateRequest;
import android.credentials.CredentialProviderInfo;
import android.credentials.IClearCredentialStateCallback;
import android.credentials.ui.ProviderData;
import android.credentials.ui.UserSelectionDialogResult;
import android.os.CancellationSignal;
import android.os.RemoteException;
import android.service.credentials.CallingAppInfo;
import android.util.Slog;
import com.android.server.credentials.ProviderSession;
import com.android.server.credentials.RequestSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ClearRequestSession extends RequestSession<ClearCredentialStateRequest, IClearCredentialStateCallback, Void> implements ProviderSession.ProviderInternalCallback<Void> {
    private static final String TAG = "GetRequestSession";

    @Override // com.android.server.credentials.RequestSession
    protected void launchUiWithProviderData(ArrayList<ProviderData> arrayList) {
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalErrorReceived(ComponentName componentName, String str, String str2) {
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiCancellation(boolean z) {
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiSelectorInvocationFailure() {
    }

    @Override // com.android.server.credentials.RequestSession
    public /* bridge */ /* synthetic */ void addProviderSession(ComponentName componentName, ProviderSession providerSession) {
        super.addProviderSession(componentName, providerSession);
    }

    @Override // com.android.server.credentials.RequestSession, com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public /* bridge */ /* synthetic */ void onUiSelection(UserSelectionDialogResult userSelectionDialogResult) {
        super.onUiSelection(userSelectionDialogResult);
    }

    public ClearRequestSession(Context context, RequestSession.SessionLifetime sessionLifetime, Object obj, int i, int i2, IClearCredentialStateCallback iClearCredentialStateCallback, ClearCredentialStateRequest clearCredentialStateRequest, CallingAppInfo callingAppInfo, Set<ComponentName> set, CancellationSignal cancellationSignal, long j) {
        super(context, sessionLifetime, obj, i, i2, clearCredentialStateRequest, iClearCredentialStateCallback, "android.credentials.ui.TYPE_UNDEFINED", callingAppInfo, set, cancellationSignal, j);
    }

    @Override // com.android.server.credentials.RequestSession
    public ProviderSession initiateProviderSession(CredentialProviderInfo credentialProviderInfo, RemoteCredentialService remoteCredentialService) {
        ProviderClearSession createNewSession = ProviderClearSession.createNewSession(this.mContext, this.mUserId, credentialProviderInfo, this, remoteCredentialService);
        if (createNewSession != null) {
            Slog.i(TAG, "Provider session created and being added for: " + credentialProviderInfo.getComponentName());
            this.mProviders.put(createNewSession.getComponentName().flattenToString(), createNewSession);
        }
        return createNewSession;
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onProviderStatusChanged(ProviderSession.Status status, ComponentName componentName, ProviderSession.CredentialsSource credentialsSource) {
        Slog.i(TAG, "Provider changed with status: " + status + ", and source: " + credentialsSource);
        if (ProviderSession.isTerminatingStatus(status)) {
            Slog.i(TAG, "Provider terminating status");
            onProviderTerminated(componentName);
        } else if (ProviderSession.isCompletionStatus(status)) {
            Slog.i(TAG, "Provider has completion status");
            onProviderResponseComplete(componentName);
        }
    }

    @Override // com.android.server.credentials.ProviderSession.ProviderInternalCallback
    public void onFinalResponseReceived(ComponentName componentName, Void r5) {
        this.mRequestSessionMetric.collectUiResponseData(true, System.nanoTime());
        this.mRequestSessionMetric.updateMetricsOnResponseReceived(this.mProviders, componentName, isPrimaryProviderViaProviderInfo(componentName));
        respondToClientWithResponseAndFinish(null);
    }

    protected void onProviderResponseComplete(ComponentName componentName) {
        if (isAnyProviderPending()) {
            return;
        }
        onFinalResponseReceived(componentName, (Void) null);
    }

    protected void onProviderTerminated(ComponentName componentName) {
        if (isAnyProviderPending()) {
            return;
        }
        processResponses();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.credentials.RequestSession
    public void invokeClientCallbackSuccess(Void r1) throws RemoteException {
        ((IClearCredentialStateCallback) this.mClientCallback).onSuccess();
    }

    @Override // com.android.server.credentials.RequestSession
    protected void invokeClientCallbackError(String str, String str2) throws RemoteException {
        ((IClearCredentialStateCallback) this.mClientCallback).onError(str, str2);
    }

    private void processResponses() {
        Iterator<ProviderSession> it = this.mProviders.values().iterator();
        while (it.hasNext()) {
            if (it.next().isProviderResponseSet().booleanValue()) {
                respondToClientWithResponseAndFinish(null);
                return;
            }
        }
        this.mRequestSessionMetric.collectFrameworkException("android.credentials.ClearCredentialStateException.TYPE_UNKNOWN");
        respondToClientWithErrorAndFinish("android.credentials.ClearCredentialStateException.TYPE_UNKNOWN", "All providers failed");
    }
}
