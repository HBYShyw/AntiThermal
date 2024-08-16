package com.android.server.credentials;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.credentials.Credential;
import android.credentials.CredentialProviderInfo;
import android.credentials.ui.ProviderData;
import android.credentials.ui.ProviderPendingIntentResponse;
import android.os.ICancellationSignal;
import android.os.RemoteException;
import android.util.Slog;
import com.android.server.credentials.RemoteCredentialService;
import com.android.server.credentials.metrics.ProviderSessionMetric;
import java.util.UUID;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class ProviderSession<T, R> implements RemoteCredentialService.ProviderCallbacks<R> {
    private static final String TAG = "ProviderSession";
    protected final ProviderInternalCallback mCallbacks;
    protected final ComponentName mComponentName;
    protected final Context mContext;
    protected Credential mFinalCredentialResponse;
    protected ICancellationSignal mProviderCancellationSignal;
    protected final T mProviderRequest;
    protected R mProviderResponse;
    protected final ProviderSessionMetric mProviderSessionMetric;
    private int mProviderSessionUid;
    protected final RemoteCredentialService mRemoteCredentialService;
    protected final int mUserId;
    protected Status mStatus = Status.NOT_STARTED;
    protected Boolean mProviderResponseSet = Boolean.FALSE;
    protected final CredentialProviderInfo mProviderInfo = null;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    enum CredentialsSource {
        REMOTE_PROVIDER,
        REGISTRY,
        AUTH_ENTRY
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface ProviderInternalCallback<V> {
        void onFinalErrorReceived(ComponentName componentName, String str, String str2);

        void onFinalResponseReceived(ComponentName componentName, V v);

        void onProviderStatusChanged(Status status, ComponentName componentName, CredentialsSource credentialsSource);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum Status {
        NOT_STARTED,
        PENDING,
        CREDENTIALS_RECEIVED,
        SERVICE_DEAD,
        SAVE_ENTRIES_RECEIVED,
        CANCELED,
        EMPTY_RESPONSE,
        NO_CREDENTIALS_FROM_AUTH_ENTRY,
        COMPLETE
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void invokeSession();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void onUiEntrySelected(String str, String str2, ProviderPendingIntentResponse providerPendingIntentResponse);

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: prepareUiData */
    public abstract ProviderData mo3151prepareUiData();

    public static boolean isUiInvokingStatus(Status status) {
        return status == Status.CREDENTIALS_RECEIVED || status == Status.SAVE_ENTRIES_RECEIVED || status == Status.NO_CREDENTIALS_FROM_AUTH_ENTRY;
    }

    public static boolean isStatusWaitingForRemoteResponse(Status status) {
        return status == Status.PENDING;
    }

    public static boolean isTerminatingStatus(Status status) {
        return status == Status.CANCELED || status == Status.SERVICE_DEAD;
    }

    public static boolean isCompletionStatus(Status status) {
        return status == Status.COMPLETE || status == Status.EMPTY_RESPONSE;
    }

    public ProviderSessionMetric getProviderSessionMetric() {
        return this.mProviderSessionMetric;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public ProviderSession(Context context, T t, ProviderInternalCallback providerInternalCallback, ComponentName componentName, int i, RemoteCredentialService remoteCredentialService) {
        this.mContext = context;
        this.mProviderRequest = t;
        this.mCallbacks = providerInternalCallback;
        this.mUserId = i;
        this.mComponentName = componentName;
        this.mRemoteCredentialService = remoteCredentialService;
        this.mProviderSessionUid = MetricUtilities.getPackageUid(context, componentName);
        this.mProviderSessionMetric = new ProviderSessionMetric(((RequestSession) providerInternalCallback).mRequestSessionMetric.getSessionIdTrackTwo());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public Credential getFinalCredentialResponse() {
        return this.mFinalCredentialResponse;
    }

    public void cancelProviderRemoteSession() {
        try {
            ICancellationSignal iCancellationSignal = this.mProviderCancellationSignal;
            if (iCancellationSignal != null) {
                iCancellationSignal.cancel();
            }
            setStatus(Status.CANCELED);
        } catch (RemoteException e) {
            Slog.e(TAG, "Issue while cancelling provider session: ", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setStatus(Status status) {
        this.mStatus = status;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Status getStatus() {
        return this.mStatus;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    protected RemoteCredentialService getRemoteCredentialService() {
        return this.mRemoteCredentialService;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateStatusAndInvokeCallback(Status status, CredentialsSource credentialsSource) {
        setStatus(status);
        CredentialProviderInfo credentialProviderInfo = this.mProviderInfo;
        this.mProviderSessionMetric.collectCandidateMetricUpdate(isTerminatingStatus(status) || isStatusWaitingForRemoteResponse(status), isCompletionStatus(status) || isUiInvokingStatus(status), this.mProviderSessionUid, credentialsSource == CredentialsSource.AUTH_ENTRY, credentialProviderInfo != null && credentialProviderInfo.isPrimary());
        this.mCallbacks.onProviderStatusChanged(status, this.mComponentName, credentialsSource);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startCandidateMetrics() {
        this.mProviderSessionMetric.collectCandidateMetricSetupViaInitialMetric(((RequestSession) this.mCallbacks).mRequestSessionMetric.getInitialPhaseMetric());
    }

    protected T getProviderRequest() {
        return this.mProviderRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Boolean isProviderResponseSet() {
        return Boolean.valueOf(this.mProviderResponse != null || this.mProviderResponseSet.booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void invokeCallbackWithError(String str, String str2) {
        this.mCallbacks.onFinalErrorReceived(this.mComponentName, str, str2);
    }

    protected R getProviderResponse() {
        return this.mProviderResponse;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean enforceRemoteEntryRestrictions(ComponentName componentName) {
        if (!this.mComponentName.equals(componentName)) {
            Slog.w(TAG, "Remote entry being dropped as it is not from the service configured by the OEM.");
            return false;
        }
        try {
            ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(this.mComponentName.getPackageName(), PackageManager.ApplicationInfoFlags.of(1048576L));
            if (applicationInfo != null) {
                if (this.mContext.checkPermission("android.permission.PROVIDE_REMOTE_CREDENTIALS", -1, applicationInfo.uid) == 0) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException | SecurityException e) {
            Slog.e(TAG, "Error getting info for " + this.mComponentName.flattenToString(), e);
            return false;
        }
    }
}
