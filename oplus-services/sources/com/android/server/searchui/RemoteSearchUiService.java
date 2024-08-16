package com.android.server.searchui;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.service.search.ISearchUiService;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;
import com.android.server.pm.DumpState;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RemoteSearchUiService extends AbstractMultiplePendingRequestsRemoteService<RemoteSearchUiService, ISearchUiService> {
    private static final String TAG = "RemoteSearchUiService";
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;
    private final RemoteSearchUiServiceCallbacks mCallback;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface RemoteSearchUiServiceCallbacks extends AbstractRemoteService.VultureCallback<RemoteSearchUiService> {
        void onConnectedStateChanged(boolean z);

        void onFailureOrTimeout(boolean z);
    }

    protected long getRemoteRequestMillis() {
        return TIMEOUT_REMOTE_REQUEST_MILLIS;
    }

    protected long getTimeoutIdleBindMillis() {
        return 0L;
    }

    public RemoteSearchUiService(Context context, String str, ComponentName componentName, int i, RemoteSearchUiServiceCallbacks remoteSearchUiServiceCallbacks, boolean z, boolean z2) {
        super(context, str, componentName, i, remoteSearchUiServiceCallbacks, context.getMainThreadHandler(), z ? DumpState.DUMP_CHANGES : 0, z2, 1);
        this.mCallback = remoteSearchUiServiceCallbacks;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ISearchUiService getServiceInterface(IBinder iBinder) {
        return ISearchUiService.Stub.asInterface(iBinder);
    }

    public void reconnect() {
        super.scheduleBind();
    }

    public void scheduleOnResolvedService(AbstractRemoteService.AsyncRequest<ISearchUiService> asyncRequest) {
        scheduleAsyncRequest(asyncRequest);
    }

    public void executeOnResolvedService(AbstractRemoteService.AsyncRequest<ISearchUiService> asyncRequest) {
        executeAsyncRequest(asyncRequest);
    }

    protected void handleOnConnectedStateChanged(boolean z) {
        RemoteSearchUiServiceCallbacks remoteSearchUiServiceCallbacks = this.mCallback;
        if (remoteSearchUiServiceCallbacks != null) {
            remoteSearchUiServiceCallbacks.onConnectedStateChanged(z);
        }
    }
}
