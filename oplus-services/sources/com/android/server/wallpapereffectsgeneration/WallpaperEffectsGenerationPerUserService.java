package com.android.server.wallpapereffectsgeneration;

import android.app.AppGlobals;
import android.app.wallpapereffectsgeneration.CinematicEffectRequest;
import android.app.wallpapereffectsgeneration.CinematicEffectResponse;
import android.app.wallpapereffectsgeneration.ICinematicEffectListener;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.IInterface;
import android.os.RemoteException;
import android.service.wallpapereffectsgeneration.IWallpaperEffectsGenerationService;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.AbstractRemoteService;
import com.android.server.infra.AbstractPerUserSystemService;
import com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WallpaperEffectsGenerationPerUserService extends AbstractPerUserSystemService<WallpaperEffectsGenerationPerUserService, WallpaperEffectsGenerationManagerService> implements RemoteWallpaperEffectsGenerationService.RemoteWallpaperEffectsGenerationServiceCallback {
    private static final String TAG = WallpaperEffectsGenerationPerUserService.class.getSimpleName();

    @GuardedBy({"mLock"})
    private CinematicEffectListenerWrapper mCinematicEffectListenerWrapper;

    @GuardedBy({"mLock"})
    private RemoteWallpaperEffectsGenerationService mRemoteService;

    /* JADX INFO: Access modifiers changed from: protected */
    public WallpaperEffectsGenerationPerUserService(WallpaperEffectsGenerationManagerService wallpaperEffectsGenerationManagerService, Object obj, int i) {
        super(wallpaperEffectsGenerationManagerService, obj, i);
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        try {
            ServiceInfo serviceInfo = AppGlobals.getPackageManager().getServiceInfo(componentName, 128L, this.mUserId);
            if ("android.permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE".equals(serviceInfo.permission)) {
                return serviceInfo;
            }
            Slog.w(TAG, "WallpaperEffectsGenerationService from '" + serviceInfo.packageName + "' does not require permission android.permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE");
            throw new SecurityException("Service does not require permission android.permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE");
        } catch (RemoteException unused) {
            throw new PackageManager.NameNotFoundException("Could not get service for " + componentName);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractPerUserSystemService
    @GuardedBy({"mLock"})
    public boolean updateLocked(boolean z) {
        boolean updateLocked = super.updateLocked(z);
        updateRemoteServiceLocked();
        return updateLocked;
    }

    @GuardedBy({"mLock"})
    public void onGenerateCinematicEffectLocked(final CinematicEffectRequest cinematicEffectRequest, ICinematicEffectListener iCinematicEffectListener) {
        String taskId = cinematicEffectRequest.getTaskId();
        CinematicEffectListenerWrapper cinematicEffectListenerWrapper = this.mCinematicEffectListenerWrapper;
        if (cinematicEffectListenerWrapper != null) {
            if (cinematicEffectListenerWrapper.mTaskId.equals(taskId)) {
                invokeCinematicListenerAndCleanup(new CinematicEffectResponse.Builder(3, taskId).build());
                return;
            } else {
                invokeCinematicListenerAndCleanup(new CinematicEffectResponse.Builder(4, taskId).build());
                return;
            }
        }
        RemoteWallpaperEffectsGenerationService ensureRemoteServiceLocked = ensureRemoteServiceLocked();
        if (ensureRemoteServiceLocked != null) {
            ensureRemoteServiceLocked.executeOnResolvedService(new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.wallpapereffectsgeneration.WallpaperEffectsGenerationPerUserService$$ExternalSyntheticLambda0
                public final void run(IInterface iInterface) {
                    ((IWallpaperEffectsGenerationService) iInterface).onGenerateCinematicEffect(cinematicEffectRequest);
                }
            });
            this.mCinematicEffectListenerWrapper = new CinematicEffectListenerWrapper(taskId, iCinematicEffectListener);
            return;
        }
        if (isDebug()) {
            Slog.d(TAG, "Remote service not found");
        }
        try {
            iCinematicEffectListener.onCinematicEffectGenerated(createErrorCinematicEffectResponse(taskId));
        } catch (RemoteException unused) {
            if (isDebug()) {
                Slog.d(TAG, "Failed to invoke cinematic effect listener for task [" + taskId + "]");
            }
        }
    }

    @GuardedBy({"mLock"})
    public void onReturnCinematicEffectResponseLocked(CinematicEffectResponse cinematicEffectResponse) {
        invokeCinematicListenerAndCleanup(cinematicEffectResponse);
    }

    public boolean isCallingUidAllowed(int i) {
        return getServiceUidLocked() == i;
    }

    @GuardedBy({"mLock"})
    private void updateRemoteServiceLocked() {
        RemoteWallpaperEffectsGenerationService remoteWallpaperEffectsGenerationService = this.mRemoteService;
        if (remoteWallpaperEffectsGenerationService != null) {
            remoteWallpaperEffectsGenerationService.destroy();
            this.mRemoteService = null;
        }
        CinematicEffectListenerWrapper cinematicEffectListenerWrapper = this.mCinematicEffectListenerWrapper;
        if (cinematicEffectListenerWrapper != null) {
            invokeCinematicListenerAndCleanup(createErrorCinematicEffectResponse(cinematicEffectListenerWrapper.mTaskId));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageUpdatedLocked() {
        if (isDebug()) {
            Slog.v(TAG, "onPackageUpdatedLocked()");
        }
        destroyAndRebindRemoteService();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageRestartedLocked() {
        if (isDebug()) {
            Slog.v(TAG, "onPackageRestartedLocked()");
        }
        destroyAndRebindRemoteService();
    }

    private void destroyAndRebindRemoteService() {
        if (this.mRemoteService == null) {
            return;
        }
        if (isDebug()) {
            Slog.d(TAG, "Destroying the old remote service.");
        }
        this.mRemoteService.destroy();
        this.mRemoteService = null;
        RemoteWallpaperEffectsGenerationService ensureRemoteServiceLocked = ensureRemoteServiceLocked();
        this.mRemoteService = ensureRemoteServiceLocked;
        if (ensureRemoteServiceLocked != null) {
            if (isDebug()) {
                Slog.d(TAG, "Rebinding to the new remote service.");
            }
            this.mRemoteService.reconnect();
        }
        CinematicEffectListenerWrapper cinematicEffectListenerWrapper = this.mCinematicEffectListenerWrapper;
        if (cinematicEffectListenerWrapper != null) {
            invokeCinematicListenerAndCleanup(createErrorCinematicEffectResponse(cinematicEffectListenerWrapper.mTaskId));
        }
    }

    private CinematicEffectResponse createErrorCinematicEffectResponse(String str) {
        return new CinematicEffectResponse.Builder(0, str).build();
    }

    @GuardedBy({"mLock"})
    private void invokeCinematicListenerAndCleanup(CinematicEffectResponse cinematicEffectResponse) {
        try {
            try {
                CinematicEffectListenerWrapper cinematicEffectListenerWrapper = this.mCinematicEffectListenerWrapper;
                if (cinematicEffectListenerWrapper != null && cinematicEffectListenerWrapper.mListener != null) {
                    this.mCinematicEffectListenerWrapper.mListener.onCinematicEffectGenerated(cinematicEffectResponse);
                } else if (isDebug()) {
                    Slog.w(TAG, "Cinematic effect listener not found for task[" + this.mCinematicEffectListenerWrapper.mTaskId + "]");
                }
            } catch (RemoteException unused) {
                if (isDebug()) {
                    Slog.w(TAG, "Error invoking cinematic effect listener for task[" + this.mCinematicEffectListenerWrapper.mTaskId + "]");
                }
            }
        } finally {
            this.mCinematicEffectListenerWrapper = null;
        }
    }

    @GuardedBy({"mLock"})
    private RemoteWallpaperEffectsGenerationService ensureRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            ComponentName updateServiceInfoLocked = updateServiceInfoLocked();
            if (updateServiceInfoLocked == null) {
                if (!((WallpaperEffectsGenerationManagerService) this.mMaster).verbose) {
                    return null;
                }
                Slog.v(TAG, "ensureRemoteServiceLocked(): not set");
                return null;
            }
            this.mRemoteService = new RemoteWallpaperEffectsGenerationService(getContext(), updateServiceInfoLocked, this.mUserId, this, ((WallpaperEffectsGenerationManagerService) this.mMaster).isBindInstantServiceAllowed(), ((WallpaperEffectsGenerationManagerService) this.mMaster).verbose);
        }
        return this.mRemoteService;
    }

    public void onServiceDied(RemoteWallpaperEffectsGenerationService remoteWallpaperEffectsGenerationService) {
        Slog.w(TAG, "remote wallpaper effects generation service died");
        updateRemoteServiceLocked();
    }

    @Override // com.android.server.wallpapereffectsgeneration.RemoteWallpaperEffectsGenerationService.RemoteWallpaperEffectsGenerationServiceCallback
    public void onConnectedStateChanged(boolean z) {
        if (z) {
            return;
        }
        Slog.w(TAG, "remote wallpaper effects generation service disconnected");
        updateRemoteServiceLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class CinematicEffectListenerWrapper {
        private final ICinematicEffectListener mListener;
        private final String mTaskId;

        CinematicEffectListenerWrapper(String str, ICinematicEffectListener iCinematicEffectListener) {
            this.mTaskId = str;
            this.mListener = iCinematicEffectListener;
        }
    }
}
