package com.android.server.wallpapereffectsgeneration;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.service.wallpapereffectsgeneration.IWallpaperEffectsGenerationService;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService;
import com.android.server.pm.DumpState;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RemoteWallpaperEffectsGenerationService extends AbstractMultiplePendingRequestsRemoteService<RemoteWallpaperEffectsGenerationService, IWallpaperEffectsGenerationService> {
    private static final String TAG = RemoteWallpaperEffectsGenerationService.class.getSimpleName();
    private static final long TIMEOUT_IDLE_BIND_MILLIS = 120000;
    private static final long TIMEOUT_REMOTE_REQUEST_MILLIS = 2000;
    private final RemoteWallpaperEffectsGenerationServiceCallback mCallback;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface RemoteWallpaperEffectsGenerationServiceCallback extends AbstractRemoteService.VultureCallback<RemoteWallpaperEffectsGenerationService> {
        void onConnectedStateChanged(boolean z);
    }

    protected long getRemoteRequestMillis() {
        return TIMEOUT_REMOTE_REQUEST_MILLIS;
    }

    protected long getTimeoutIdleBindMillis() {
        return 120000L;
    }

    public RemoteWallpaperEffectsGenerationService(Context context, ComponentName componentName, int i, RemoteWallpaperEffectsGenerationServiceCallback remoteWallpaperEffectsGenerationServiceCallback, boolean z, boolean z2) {
        super(context, "android.service.wallpapereffectsgeneration.WallpaperEffectsGenerationService", componentName, i, remoteWallpaperEffectsGenerationServiceCallback, context.getMainThreadHandler(), z ? DumpState.DUMP_CHANGES : 0, z2, 1);
        this.mCallback = remoteWallpaperEffectsGenerationServiceCallback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IWallpaperEffectsGenerationService getServiceInterface(IBinder iBinder) {
        return IWallpaperEffectsGenerationService.Stub.asInterface(iBinder);
    }

    public void reconnect() {
        super.scheduleBind();
    }

    public void scheduleOnResolvedService(AbstractRemoteService.AsyncRequest<IWallpaperEffectsGenerationService> asyncRequest) {
        scheduleAsyncRequest(asyncRequest);
    }

    public void executeOnResolvedService(AbstractRemoteService.AsyncRequest<IWallpaperEffectsGenerationService> asyncRequest) {
        executeAsyncRequest(asyncRequest);
    }

    protected void handleOnConnectedStateChanged(boolean z) {
        RemoteWallpaperEffectsGenerationServiceCallback remoteWallpaperEffectsGenerationServiceCallback = this.mCallback;
        if (remoteWallpaperEffectsGenerationServiceCallback != null) {
            remoteWallpaperEffectsGenerationServiceCallback.onConnectedStateChanged(z);
        }
    }
}
