package com.android.server.appprediction;

import android.content.ComponentName;
import android.content.Context;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.IBinder;
import android.service.appprediction.IPredictionService;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RemoteAppPredictionService extends AbstractMultiplePendingRequestsRemoteService<RemoteAppPredictionService, IPredictionService> {
    private static final String TAG = "RemoteAppPredictionService";
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;
    private final RemoteAppPredictionServiceCallbacks mCallback;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface RemoteAppPredictionServiceCallbacks extends AbstractRemoteService.VultureCallback<RemoteAppPredictionService> {
        void onConnectedStateChanged(boolean z);

        void onFailureOrTimeout(boolean z);
    }

    protected long getRemoteRequestMillis() {
        return TIMEOUT_REMOTE_REQUEST_MILLIS;
    }

    protected long getTimeoutIdleBindMillis() {
        return 0L;
    }

    public RemoteAppPredictionService(Context context, String str, ComponentName componentName, int i, RemoteAppPredictionServiceCallbacks remoteAppPredictionServiceCallbacks, boolean z, boolean z2) {
        super(context, str, componentName, i, remoteAppPredictionServiceCallbacks, context.getMainThreadHandler(), z ? AudioDevice.OUT_SPEAKER_SAFE : 0, z2, 1);
        this.mCallback = remoteAppPredictionServiceCallbacks;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IPredictionService getServiceInterface(IBinder iBinder) {
        return IPredictionService.Stub.asInterface(iBinder);
    }

    public void reconnect() {
        super.scheduleBind();
    }

    public void scheduleOnResolvedService(AbstractRemoteService.AsyncRequest<IPredictionService> asyncRequest) {
        scheduleAsyncRequest(asyncRequest);
    }

    public void executeOnResolvedService(AbstractRemoteService.AsyncRequest<IPredictionService> asyncRequest) {
        executeAsyncRequest(asyncRequest);
    }

    protected void handleOnConnectedStateChanged(boolean z) {
        RemoteAppPredictionServiceCallbacks remoteAppPredictionServiceCallbacks = this.mCallback;
        if (remoteAppPredictionServiceCallbacks != null) {
            remoteAppPredictionServiceCallbacks.onConnectedStateChanged(z);
        }
    }
}
