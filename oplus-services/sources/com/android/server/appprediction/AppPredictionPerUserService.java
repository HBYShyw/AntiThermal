package com.android.server.appprediction;

import android.app.AppGlobals;
import android.app.prediction.AppPredictionContext;
import android.app.prediction.AppPredictionSessionId;
import android.app.prediction.AppTargetEvent;
import android.app.prediction.IPredictionCallback;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.DeviceConfig;
import android.service.appprediction.IPredictionService;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.AbstractRemoteService;
import com.android.server.LocalServices;
import com.android.server.appprediction.AppPredictionPerUserService;
import com.android.server.appprediction.RemoteAppPredictionService;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.AbstractPerUserSystemService;
import com.android.server.people.PeopleServiceInternal;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppPredictionPerUserService extends AbstractPerUserSystemService<AppPredictionPerUserService, AppPredictionManagerService> implements RemoteAppPredictionService.RemoteAppPredictionServiceCallbacks {
    private static final String PREDICT_USING_PEOPLE_SERVICE_PREFIX = "predict_using_people_service_";
    private static final String REMOTE_APP_PREDICTOR_KEY = "remote_app_predictor";
    private static final String TAG = AppPredictionPerUserService.class.getSimpleName();

    @GuardedBy({"mLock"})
    private RemoteAppPredictionService mRemoteService;

    @GuardedBy({"mLock"})
    private final ArrayMap<AppPredictionSessionId, AppPredictionSessionInfo> mSessionInfos;

    @GuardedBy({"mLock"})
    private boolean mZombie;

    /* JADX INFO: Access modifiers changed from: protected */
    public AppPredictionPerUserService(AppPredictionManagerService appPredictionManagerService, Object obj, int i) {
        super(appPredictionManagerService, obj, i);
        this.mSessionInfos = new ArrayMap<>();
    }

    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        try {
            return AppGlobals.getPackageManager().getServiceInfo(componentName, 128L, ((AbstractPerUserSystemService) this).mUserId);
        } catch (RemoteException unused) {
            throw new PackageManager.NameNotFoundException("Could not get service for " + componentName);
        }
    }

    @GuardedBy({"mLock"})
    protected boolean updateLocked(boolean z) {
        boolean updateLocked = super.updateLocked(z);
        if (updateLocked && !isEnabledLocked()) {
            this.mRemoteService = null;
        }
        return updateLocked;
    }

    @GuardedBy({"mLock"})
    public void onCreatePredictionSessionLocked(final AppPredictionContext appPredictionContext, final AppPredictionSessionId appPredictionSessionId, IBinder iBinder) {
        boolean z = (appPredictionContext.getExtras() != null && appPredictionContext.getExtras().getBoolean(REMOTE_APP_PREDICTOR_KEY, false) && DeviceConfig.getBoolean("systemui", "dark_launch_remote_prediction_service_enabled", false)) ? false : DeviceConfig.getBoolean("systemui", PREDICT_USING_PEOPLE_SERVICE_PREFIX + appPredictionContext.getUiSurface(), false);
        if (!resolveService(appPredictionSessionId, true, z, new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda5
            public final void run(IInterface iInterface) {
                ((IPredictionService) iInterface).onCreatePredictionSession(appPredictionContext, appPredictionSessionId);
            }
        }) || this.mSessionInfos.containsKey(appPredictionSessionId)) {
            return;
        }
        AppPredictionSessionInfo appPredictionSessionInfo = new AppPredictionSessionInfo(appPredictionSessionId, appPredictionContext, z, iBinder, new IBinder.DeathRecipient() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda6
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                AppPredictionPerUserService.this.lambda$onCreatePredictionSessionLocked$1(appPredictionSessionId);
            }
        });
        if (appPredictionSessionInfo.linkToDeath()) {
            this.mSessionInfos.put(appPredictionSessionId, appPredictionSessionInfo);
        } else {
            onDestroyPredictionSessionLocked(appPredictionSessionId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreatePredictionSessionLocked$1(AppPredictionSessionId appPredictionSessionId) {
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            onDestroyPredictionSessionLocked(appPredictionSessionId);
        }
    }

    @GuardedBy({"mLock"})
    public void notifyAppTargetEventLocked(final AppPredictionSessionId appPredictionSessionId, final AppTargetEvent appTargetEvent) {
        AppPredictionSessionInfo appPredictionSessionInfo = this.mSessionInfos.get(appPredictionSessionId);
        if (appPredictionSessionInfo == null) {
            return;
        }
        resolveService(appPredictionSessionId, false, appPredictionSessionInfo.mUsesPeopleService, new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda1
            public final void run(IInterface iInterface) {
                ((IPredictionService) iInterface).notifyAppTargetEvent(appPredictionSessionId, appTargetEvent);
            }
        });
    }

    @GuardedBy({"mLock"})
    public void notifyLaunchLocationShownLocked(final AppPredictionSessionId appPredictionSessionId, final String str, final ParceledListSlice parceledListSlice) {
        AppPredictionSessionInfo appPredictionSessionInfo = this.mSessionInfos.get(appPredictionSessionId);
        if (appPredictionSessionInfo == null) {
            return;
        }
        resolveService(appPredictionSessionId, false, appPredictionSessionInfo.mUsesPeopleService, new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda3
            public final void run(IInterface iInterface) {
                ((IPredictionService) iInterface).notifyLaunchLocationShown(appPredictionSessionId, str, parceledListSlice);
            }
        });
    }

    @GuardedBy({"mLock"})
    public void sortAppTargetsLocked(final AppPredictionSessionId appPredictionSessionId, final ParceledListSlice parceledListSlice, final IPredictionCallback iPredictionCallback) {
        AppPredictionSessionInfo appPredictionSessionInfo = this.mSessionInfos.get(appPredictionSessionId);
        if (appPredictionSessionInfo == null) {
            return;
        }
        resolveService(appPredictionSessionId, true, appPredictionSessionInfo.mUsesPeopleService, new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda8
            public final void run(IInterface iInterface) {
                ((IPredictionService) iInterface).sortAppTargets(appPredictionSessionId, parceledListSlice, iPredictionCallback);
            }
        });
    }

    @GuardedBy({"mLock"})
    public void registerPredictionUpdatesLocked(final AppPredictionSessionId appPredictionSessionId, final IPredictionCallback iPredictionCallback) {
        AppPredictionSessionInfo appPredictionSessionInfo = this.mSessionInfos.get(appPredictionSessionId);
        if (appPredictionSessionInfo != null && resolveService(appPredictionSessionId, true, appPredictionSessionInfo.mUsesPeopleService, new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda2
            public final void run(IInterface iInterface) {
                ((IPredictionService) iInterface).registerPredictionUpdates(appPredictionSessionId, iPredictionCallback);
            }
        })) {
            appPredictionSessionInfo.addCallbackLocked(iPredictionCallback);
        }
    }

    @GuardedBy({"mLock"})
    public void unregisterPredictionUpdatesLocked(final AppPredictionSessionId appPredictionSessionId, final IPredictionCallback iPredictionCallback) {
        AppPredictionSessionInfo appPredictionSessionInfo = this.mSessionInfos.get(appPredictionSessionId);
        if (appPredictionSessionInfo != null && resolveService(appPredictionSessionId, false, appPredictionSessionInfo.mUsesPeopleService, new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda0
            public final void run(IInterface iInterface) {
                ((IPredictionService) iInterface).unregisterPredictionUpdates(appPredictionSessionId, iPredictionCallback);
            }
        })) {
            appPredictionSessionInfo.removeCallbackLocked(iPredictionCallback);
        }
    }

    @GuardedBy({"mLock"})
    public void requestPredictionUpdateLocked(final AppPredictionSessionId appPredictionSessionId) {
        AppPredictionSessionInfo appPredictionSessionInfo = this.mSessionInfos.get(appPredictionSessionId);
        if (appPredictionSessionInfo == null) {
            return;
        }
        resolveService(appPredictionSessionId, true, appPredictionSessionInfo.mUsesPeopleService, new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda7
            public final void run(IInterface iInterface) {
                ((IPredictionService) iInterface).requestPredictionUpdate(appPredictionSessionId);
            }
        });
    }

    @GuardedBy({"mLock"})
    public void onDestroyPredictionSessionLocked(final AppPredictionSessionId appPredictionSessionId) {
        if (isDebug()) {
            Slog.d(TAG, "onDestroyPredictionSessionLocked(): sessionId=" + appPredictionSessionId);
        }
        AppPredictionSessionInfo remove = this.mSessionInfos.remove(appPredictionSessionId);
        if (remove == null) {
            return;
        }
        resolveService(appPredictionSessionId, false, remove.mUsesPeopleService, new AbstractRemoteService.AsyncRequest() { // from class: com.android.server.appprediction.AppPredictionPerUserService$$ExternalSyntheticLambda4
            public final void run(IInterface iInterface) {
                ((IPredictionService) iInterface).onDestroyPredictionSession(appPredictionSessionId);
            }
        });
        remove.destroy();
    }

    @Override // com.android.server.appprediction.RemoteAppPredictionService.RemoteAppPredictionServiceCallbacks
    public void onFailureOrTimeout(boolean z) {
        if (isDebug()) {
            Slog.d(TAG, "onFailureOrTimeout(): timed out=" + z);
        }
    }

    @Override // com.android.server.appprediction.RemoteAppPredictionService.RemoteAppPredictionServiceCallbacks
    public void onConnectedStateChanged(boolean z) {
        if (isDebug()) {
            Slog.d(TAG, "onConnectedStateChanged(): connected=" + z);
        }
        if (z) {
            synchronized (((AbstractPerUserSystemService) this).mLock) {
                if (this.mZombie) {
                    if (this.mRemoteService == null) {
                        Slog.w(TAG, "Cannot resurrect sessions because remote service is null");
                    } else {
                        this.mZombie = false;
                        resurrectSessionsLocked();
                    }
                }
            }
        }
    }

    public void onServiceDied(RemoteAppPredictionService remoteAppPredictionService) {
        if (isDebug()) {
            Slog.w(TAG, "onServiceDied(): service=" + remoteAppPredictionService);
        }
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            this.mZombie = true;
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
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            this.mZombie = true;
        }
        RemoteAppPredictionService remoteServiceLocked = getRemoteServiceLocked();
        this.mRemoteService = remoteServiceLocked;
        if (remoteServiceLocked != null) {
            if (isDebug()) {
                Slog.d(TAG, "Rebinding to the new remote service.");
            }
            this.mRemoteService.reconnect();
        }
    }

    private void resurrectSessionsLocked() {
        int size = this.mSessionInfos.size();
        if (isDebug()) {
            Slog.d(TAG, "Resurrecting remote service (" + this.mRemoteService + ") on " + size + " sessions.");
        }
        for (AppPredictionSessionInfo appPredictionSessionInfo : this.mSessionInfos.values()) {
            appPredictionSessionInfo.resurrectSessionLocked(this, appPredictionSessionInfo.mToken);
        }
    }

    @GuardedBy({"mLock"})
    protected boolean resolveService(AppPredictionSessionId appPredictionSessionId, boolean z, boolean z2, AbstractRemoteService.AsyncRequest<IPredictionService> asyncRequest) {
        if (z2) {
            IPredictionService iPredictionService = (IPredictionService) LocalServices.getService(PeopleServiceInternal.class);
            if (iPredictionService != null) {
                try {
                    asyncRequest.run(iPredictionService);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to invoke service:" + iPredictionService, e);
                }
            }
            return iPredictionService != null;
        }
        RemoteAppPredictionService remoteServiceLocked = getRemoteServiceLocked();
        if (remoteServiceLocked != null) {
            if (z) {
                remoteServiceLocked.executeOnResolvedService(asyncRequest);
            } else {
                remoteServiceLocked.scheduleOnResolvedService(asyncRequest);
            }
        }
        return remoteServiceLocked != null;
    }

    @GuardedBy({"mLock"})
    private RemoteAppPredictionService getRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            String componentNameLocked = getComponentNameLocked();
            if (componentNameLocked == null) {
                if (!((AbstractMasterSystemService) ((AppPredictionManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose) {
                    return null;
                }
                Slog.v(TAG, "getRemoteServiceLocked(): not set");
                return null;
            }
            this.mRemoteService = new RemoteAppPredictionService(getContext(), "android.service.appprediction.AppPredictionService", ComponentName.unflattenFromString(componentNameLocked), ((AbstractPerUserSystemService) this).mUserId, this, ((AppPredictionManagerService) ((AbstractPerUserSystemService) this).mMaster).isBindInstantServiceAllowed(), ((AbstractMasterSystemService) ((AppPredictionManagerService) ((AbstractPerUserSystemService) this).mMaster)).verbose);
        }
        return this.mRemoteService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AppPredictionSessionInfo {
        private static final boolean DEBUG = false;
        private final RemoteCallbackList<IPredictionCallback> mCallbacks = new RemoteCallbackList<>();
        final IBinder.DeathRecipient mDeathRecipient;
        private final AppPredictionContext mPredictionContext;
        private final AppPredictionSessionId mSessionId;
        final IBinder mToken;
        private final boolean mUsesPeopleService;

        AppPredictionSessionInfo(AppPredictionSessionId appPredictionSessionId, AppPredictionContext appPredictionContext, boolean z, IBinder iBinder, IBinder.DeathRecipient deathRecipient) {
            this.mSessionId = appPredictionSessionId;
            this.mPredictionContext = appPredictionContext;
            this.mUsesPeopleService = z;
            this.mToken = iBinder;
            this.mDeathRecipient = deathRecipient;
        }

        void addCallbackLocked(IPredictionCallback iPredictionCallback) {
            this.mCallbacks.register(iPredictionCallback);
        }

        void removeCallbackLocked(IPredictionCallback iPredictionCallback) {
            this.mCallbacks.unregister(iPredictionCallback);
        }

        boolean linkToDeath() {
            try {
                this.mToken.linkToDeath(this.mDeathRecipient, 0);
                return true;
            } catch (RemoteException unused) {
                return false;
            }
        }

        void destroy() {
            IBinder iBinder = this.mToken;
            if (iBinder != null) {
                iBinder.unlinkToDeath(this.mDeathRecipient, 0);
            }
            this.mCallbacks.kill();
        }

        void resurrectSessionLocked(final AppPredictionPerUserService appPredictionPerUserService, IBinder iBinder) {
            this.mCallbacks.getRegisteredCallbackCount();
            appPredictionPerUserService.onCreatePredictionSessionLocked(this.mPredictionContext, this.mSessionId, iBinder);
            this.mCallbacks.broadcast(new Consumer() { // from class: com.android.server.appprediction.AppPredictionPerUserService$AppPredictionSessionInfo$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppPredictionPerUserService.AppPredictionSessionInfo.this.lambda$resurrectSessionLocked$0(appPredictionPerUserService, (IPredictionCallback) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resurrectSessionLocked$0(AppPredictionPerUserService appPredictionPerUserService, IPredictionCallback iPredictionCallback) {
            appPredictionPerUserService.registerPredictionUpdatesLocked(this.mSessionId, iPredictionCallback);
        }
    }
}
