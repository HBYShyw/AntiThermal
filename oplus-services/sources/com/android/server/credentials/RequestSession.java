package com.android.server.credentials;

import android.R;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.credentials.CredentialProviderInfo;
import android.credentials.ui.ProviderData;
import android.credentials.ui.UserSelectionDialogResult;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Binder;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.credentials.CallingAppInfo;
import android.util.Slog;
import com.android.server.credentials.CredentialManagerUi;
import com.android.server.credentials.metrics.ApiName;
import com.android.server.credentials.metrics.ApiStatus;
import com.android.server.credentials.metrics.ProviderSessionMetric;
import com.android.server.credentials.metrics.ProviderStatusForMetrics;
import com.android.server.credentials.metrics.RequestSessionMetric;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class RequestSession<T, U, V> implements CredentialManagerUi.CredentialManagerUiCallback {
    private static final String TAG = "RequestSession";
    private final int mCallingUid;
    protected final CancellationSignal mCancellationSignal;
    protected final CallingAppInfo mClientAppInfo;
    protected final U mClientCallback;
    protected final T mClientRequest;
    protected final Context mContext;
    protected final CredentialManagerUi mCredentialManagerUi;
    private final Set<ComponentName> mEnabledProviders;
    protected final String mHybridService;
    protected final Object mLock;
    protected PendingIntent mPendingIntent;
    protected final RequestSessionMetric mRequestSessionMetric;
    protected final String mRequestType;
    protected final SessionLifetime mSessionCallback;
    protected final int mUniqueSessionInteger;
    protected final int mUserId;
    protected final Map<String, ProviderSession> mProviders = new ConcurrentHashMap();
    protected RequestSessionStatus mRequestSessionStatus = RequestSessionStatus.IN_PROGRESS;
    protected final Handler mHandler = new Handler(Looper.getMainLooper(), null, true);
    protected final IBinder mRequestId = new Binder();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum RequestSessionStatus {
        IN_PROGRESS,
        CANCELLED,
        COMPLETE
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface SessionLifetime {
        void onFinishRequestSession(int i, IBinder iBinder);
    }

    public abstract ProviderSession initiateProviderSession(CredentialProviderInfo credentialProviderInfo, RemoteCredentialService remoteCredentialService);

    protected abstract void invokeClientCallbackError(String str, String str2) throws RemoteException;

    protected abstract void invokeClientCallbackSuccess(V v) throws RemoteException;

    protected abstract void launchUiWithProviderData(ArrayList<ProviderData> arrayList);

    /* JADX INFO: Access modifiers changed from: protected */
    public RequestSession(Context context, SessionLifetime sessionLifetime, Object obj, int i, int i2, T t, U u, String str, CallingAppInfo callingAppInfo, Set<ComponentName> set, CancellationSignal cancellationSignal, long j) {
        this.mContext = context;
        this.mLock = obj;
        this.mSessionCallback = sessionLifetime;
        this.mUserId = i;
        this.mCallingUid = i2;
        this.mClientRequest = t;
        this.mClientCallback = u;
        this.mRequestType = str;
        this.mClientAppInfo = callingAppInfo;
        this.mEnabledProviders = set;
        this.mCancellationSignal = cancellationSignal;
        this.mCredentialManagerUi = new CredentialManagerUi(context, i, this, set);
        this.mHybridService = context.getResources().getString(R.string.config_mobile_hotspot_provision_response);
        int highlyUniqueInteger = MetricUtilities.getHighlyUniqueInteger();
        this.mUniqueSessionInteger = highlyUniqueInteger;
        RequestSessionMetric requestSessionMetric = new RequestSessionMetric(highlyUniqueInteger, MetricUtilities.getHighlyUniqueInteger());
        this.mRequestSessionMetric = requestSessionMetric;
        requestSessionMetric.collectInitialPhaseMetricInfo(j, i2, ApiName.getMetricCodeFromRequestInfo(str));
        setCancellationListener();
    }

    private void setCancellationListener() {
        this.mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: com.android.server.credentials.RequestSession$$ExternalSyntheticLambda0
            @Override // android.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                RequestSession.this.lambda$setCancellationListener$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCancellationListener$0() {
        finishSession(!maybeCancelUi());
    }

    private boolean maybeCancelUi() {
        if (this.mCredentialManagerUi.getStatus() != CredentialManagerUi.UiStatus.USER_INTERACTION) {
            return false;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mContext.startActivityAsUser(this.mCredentialManagerUi.createCancelIntent(this.mRequestId, this.mClientAppInfo.getPackageName()).addFlags(AudioFormat.EVRC), UserHandle.of(this.mUserId));
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return true;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    public void addProviderSession(ComponentName componentName, ProviderSession providerSession) {
        this.mProviders.put(componentName.flattenToString(), providerSession);
    }

    @Override // com.android.server.credentials.CredentialManagerUi.CredentialManagerUiCallback
    public void onUiSelection(UserSelectionDialogResult userSelectionDialogResult) {
        if (this.mRequestSessionStatus == RequestSessionStatus.COMPLETE) {
            Slog.w(TAG, "Request has already been completed. This is strange.");
            return;
        }
        if (isSessionCancelled()) {
            finishSession(true);
            return;
        }
        ProviderSession providerSession = this.mProviders.get(userSelectionDialogResult.getProviderId());
        if (providerSession == null) {
            Slog.w(TAG, "providerSession not found in onUiSelection. This is strange.");
            return;
        }
        ProviderSessionMetric providerSessionMetric = providerSession.mProviderSessionMetric;
        int size = providerSessionMetric.getBrowsedAuthenticationMetric().size();
        this.mRequestSessionMetric.collectMetricPerBrowsingSelect(userSelectionDialogResult, providerSession.mProviderSessionMetric.getCandidatePhasePerProviderMetric());
        providerSession.onUiEntrySelected(userSelectionDialogResult.getEntryKey(), userSelectionDialogResult.getEntrySubkey(), userSelectionDialogResult.getPendingIntentProviderResponse());
        int size2 = providerSessionMetric.getBrowsedAuthenticationMetric().size();
        if (size2 - size == 1) {
            this.mRequestSessionMetric.logAuthEntry(providerSession.mProviderSessionMetric.getBrowsedAuthenticationMetric().get(size2 - 1));
        }
    }

    protected void finishSession(boolean z) {
        Slog.i(TAG, "finishing session with propagateCancellation " + z);
        if (z) {
            this.mProviders.values().forEach(new Consumer() { // from class: com.android.server.credentials.RequestSession$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((ProviderSession) obj).cancelProviderRemoteSession();
                }
            });
        }
        this.mRequestSessionStatus = RequestSessionStatus.COMPLETE;
        this.mProviders.clear();
        clearRequestSessionLocked();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelExistingPendingIntent() {
        PendingIntent pendingIntent = this.mPendingIntent;
        if (pendingIntent != null) {
            try {
                pendingIntent.cancel();
                this.mPendingIntent = null;
            } catch (Exception e) {
                Slog.e(TAG, "Unable to cancel existing pending intent", e);
            }
        }
    }

    private void clearRequestSessionLocked() {
        synchronized (this.mLock) {
            this.mSessionCallback.onFinishRequestSession(this.mUserId, this.mRequestId);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAnyProviderPending() {
        Iterator<ProviderSession> it = this.mProviders.values().iterator();
        while (it.hasNext()) {
            if (ProviderSession.isStatusWaitingForRemoteResponse(it.next().getStatus())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isSessionCancelled() {
        return this.mCancellationSignal.isCanceled();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUiInvocationNeeded() {
        for (ProviderSession providerSession : this.mProviders.values()) {
            if (ProviderSession.isUiInvokingStatus(providerSession.getStatus())) {
                return true;
            }
            if (ProviderSession.isStatusWaitingForRemoteResponse(providerSession.getStatus())) {
                break;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getProviderDataAndInitiateUi() {
        ArrayList<ProviderData> providerDataForUi = getProviderDataForUi();
        if (providerDataForUi.isEmpty()) {
            return;
        }
        launchUiWithProviderData(providerDataForUi);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ArrayList<ProviderData> getProviderDataForUi() {
        Slog.i(TAG, "For ui, provider data size: " + this.mProviders.size());
        ArrayList<ProviderData> arrayList = new ArrayList<>();
        this.mRequestSessionMetric.logCandidatePhaseMetrics(this.mProviders);
        if (isSessionCancelled()) {
            finishSession(true);
            return arrayList;
        }
        Iterator<ProviderSession> it = this.mProviders.values().iterator();
        while (it.hasNext()) {
            ProviderData mo3151prepareUiData = it.next().mo3151prepareUiData();
            if (mo3151prepareUiData != null) {
                arrayList.add(mo3151prepareUiData);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void respondToClientWithResponseAndFinish(V v) {
        this.mRequestSessionMetric.logCandidateAggregateMetrics(this.mProviders);
        this.mRequestSessionMetric.collectFinalPhaseProviderMetricStatus(false, ProviderStatusForMetrics.FINAL_SUCCESS);
        if (this.mRequestSessionStatus == RequestSessionStatus.COMPLETE) {
            Slog.w(TAG, "Request has already been completed. This is strange.");
            return;
        }
        if (isSessionCancelled()) {
            this.mRequestSessionMetric.logApiCalledAtFinish(ApiStatus.CLIENT_CANCELED.getMetricCode());
            finishSession(true);
            return;
        }
        try {
            invokeClientCallbackSuccess(v);
            this.mRequestSessionMetric.logApiCalledAtFinish(ApiStatus.SUCCESS.getMetricCode());
        } catch (RemoteException e) {
            this.mRequestSessionMetric.collectFinalPhaseProviderMetricStatus(true, ProviderStatusForMetrics.FINAL_FAILURE);
            Slog.e(TAG, "Issue while responding to client with a response : " + e);
            this.mRequestSessionMetric.logApiCalledAtFinish(ApiStatus.FAILURE.getMetricCode());
        }
        finishSession(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void respondToClientWithErrorAndFinish(String str, String str2) {
        this.mRequestSessionMetric.logCandidateAggregateMetrics(this.mProviders);
        this.mRequestSessionMetric.collectFinalPhaseProviderMetricStatus(true, ProviderStatusForMetrics.FINAL_FAILURE);
        if (this.mRequestSessionStatus == RequestSessionStatus.COMPLETE) {
            Slog.w(TAG, "Request has already been completed. This is strange.");
            return;
        }
        if (isSessionCancelled()) {
            this.mRequestSessionMetric.logApiCalledAtFinish(ApiStatus.CLIENT_CANCELED.getMetricCode());
            finishSession(true);
            return;
        }
        try {
            invokeClientCallbackError(str, str2);
        } catch (RemoteException e) {
            Slog.e(TAG, "Issue while responding to client with error : " + e);
        }
        this.mRequestSessionMetric.logFailureOrUserCancel(str.contains(MetricUtilities.USER_CANCELED_SUBSTRING));
        finishSession(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isPrimaryProviderViaProviderInfo(ComponentName componentName) {
        CredentialProviderInfo credentialProviderInfo;
        ProviderSession providerSession = this.mProviders.get(componentName.flattenToString());
        return (providerSession == null || (credentialProviderInfo = providerSession.mProviderInfo) == null || !credentialProviderInfo.isPrimary()) ? false : true;
    }
}
