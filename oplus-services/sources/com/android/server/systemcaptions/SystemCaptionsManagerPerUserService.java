package com.android.server.systemcaptions;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.infra.AbstractPerUserSystemService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SystemCaptionsManagerPerUserService extends AbstractPerUserSystemService<SystemCaptionsManagerPerUserService, SystemCaptionsManagerService> {
    private static final String TAG = "SystemCaptionsManagerPerUserService";

    @GuardedBy({"mLock"})
    private RemoteSystemCaptionsManagerService mRemoteService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemCaptionsManagerPerUserService(SystemCaptionsManagerService systemCaptionsManagerService, Object obj, boolean z, int i) {
        super(systemCaptionsManagerService, obj, i);
    }

    @Override // com.android.server.infra.AbstractPerUserSystemService
    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        try {
            return AppGlobals.getPackageManager().getServiceInfo(componentName, 128L, this.mUserId);
        } catch (RemoteException unused) {
            throw new PackageManager.NameNotFoundException("Could not get service for " + componentName);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void initializeLocked() {
        if (((SystemCaptionsManagerService) this.mMaster).verbose) {
            Slog.v(TAG, "initialize()");
        }
        if (getRemoteServiceLocked() == null && ((SystemCaptionsManagerService) this.mMaster).verbose) {
            Slog.v(TAG, "initialize(): Failed to init remote server");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mLock"})
    public void destroyLocked() {
        if (((SystemCaptionsManagerService) this.mMaster).verbose) {
            Slog.v(TAG, "destroyLocked()");
        }
        RemoteSystemCaptionsManagerService remoteSystemCaptionsManagerService = this.mRemoteService;
        if (remoteSystemCaptionsManagerService != null) {
            remoteSystemCaptionsManagerService.destroy();
            this.mRemoteService = null;
        }
    }

    @GuardedBy({"mLock"})
    private RemoteSystemCaptionsManagerService getRemoteServiceLocked() {
        if (this.mRemoteService == null) {
            String componentNameLocked = getComponentNameLocked();
            if (componentNameLocked == null) {
                if (!((SystemCaptionsManagerService) this.mMaster).verbose) {
                    return null;
                }
                Slog.v(TAG, "getRemoteServiceLocked(): Not set");
                return null;
            }
            this.mRemoteService = new RemoteSystemCaptionsManagerService(getContext(), ComponentName.unflattenFromString(componentNameLocked), this.mUserId, ((SystemCaptionsManagerService) this.mMaster).verbose);
            if (((SystemCaptionsManagerService) this.mMaster).verbose) {
                Slog.v(TAG, "getRemoteServiceLocked(): initialize for user " + this.mUserId);
            }
            this.mRemoteService.initialize();
        }
        return this.mRemoteService;
    }
}
