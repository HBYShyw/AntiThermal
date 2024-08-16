package com.android.server.smartspace;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.service.smartspace.ISmartspaceService;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;
import com.android.server.pm.DumpState;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RemoteSmartspaceService extends AbstractMultiplePendingRequestsRemoteService<RemoteSmartspaceService, ISmartspaceService> {
    private static final String TAG = "RemoteSmartspaceService";
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;
    private final RemoteSmartspaceServiceCallbacks mCallback;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface RemoteSmartspaceServiceCallbacks extends AbstractRemoteService.VultureCallback<RemoteSmartspaceService> {
        void onConnectedStateChanged(boolean z);

        void onFailureOrTimeout(boolean z);
    }

    protected long getRemoteRequestMillis() {
        return TIMEOUT_REMOTE_REQUEST_MILLIS;
    }

    protected long getTimeoutIdleBindMillis() {
        return 0L;
    }

    public RemoteSmartspaceService(Context context, String str, ComponentName componentName, int i, RemoteSmartspaceServiceCallbacks remoteSmartspaceServiceCallbacks, boolean z, boolean z2) {
        super(context, str, componentName, i, remoteSmartspaceServiceCallbacks, context.getMainThreadHandler(), z ? DumpState.DUMP_CHANGES : 0, z2, 1);
        this.mCallback = remoteSmartspaceServiceCallbacks;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ISmartspaceService getServiceInterface(IBinder iBinder) {
        return ISmartspaceService.Stub.asInterface(iBinder);
    }

    public void reconnect() {
        super.scheduleBind();
    }

    public void scheduleOnResolvedService(AbstractRemoteService.AsyncRequest<ISmartspaceService> asyncRequest) {
        scheduleAsyncRequest(asyncRequest);
    }

    public void executeOnResolvedService(AbstractRemoteService.AsyncRequest<ISmartspaceService> asyncRequest) {
        executeAsyncRequest(asyncRequest);
    }

    protected void handleOnConnectedStateChanged(boolean z) {
        RemoteSmartspaceServiceCallbacks remoteSmartspaceServiceCallbacks = this.mCallback;
        if (remoteSmartspaceServiceCallbacks != null) {
            remoteSmartspaceServiceCallbacks.onConnectedStateChanged(z);
        }
    }
}
