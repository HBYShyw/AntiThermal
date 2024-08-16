package com.android.server.credentials;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.credentials.CredentialProviderInfo;
import android.service.credentials.CredentialProviderInfoFactory;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.infra.AbstractPerUserSystemService;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CredentialManagerServiceImpl extends AbstractPerUserSystemService<CredentialManagerServiceImpl, CredentialManagerService> {
    private static final String TAG = "CredManSysServiceImpl";

    @GuardedBy({"mLock"})
    private CredentialProviderInfo mInfo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CredentialManagerServiceImpl(CredentialManagerService credentialManagerService, Object obj, int i, String str) throws PackageManager.NameNotFoundException {
        super(credentialManagerService, obj, i);
        Slog.i(TAG, "CredentialManagerServiceImpl constructed for: " + str);
        synchronized (((AbstractPerUserSystemService) this).mLock) {
            newServiceInfoLocked(ComponentName.unflattenFromString(str));
        }
    }

    @GuardedBy({"mLock"})
    public ComponentName getComponentName() {
        return this.mInfo.getServiceInfo().getComponentName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CredentialManagerServiceImpl(CredentialManagerService credentialManagerService, Object obj, int i, CredentialProviderInfo credentialProviderInfo) {
        super(credentialManagerService, obj, i);
        Slog.i(TAG, "CredentialManagerServiceImpl constructed for: " + credentialProviderInfo.getServiceInfo().getComponentName().flattenToString());
        this.mInfo = credentialProviderInfo;
    }

    @GuardedBy({"mLock"})
    protected ServiceInfo newServiceInfoLocked(ComponentName componentName) throws PackageManager.NameNotFoundException {
        if (this.mInfo != null) {
            Slog.i(TAG, "newServiceInfoLocked, mInfo not null : " + this.mInfo.getServiceInfo().getComponentName().flattenToString() + " , " + componentName.flattenToString());
        } else {
            Slog.i(TAG, "newServiceInfoLocked, mInfo null, " + componentName.flattenToString());
        }
        CredentialProviderInfo create = CredentialProviderInfoFactory.create(getContext(), componentName, ((AbstractPerUserSystemService) this).mUserId, false);
        this.mInfo = create;
        return create.getServiceInfo();
    }

    @GuardedBy({"mLock"})
    public ProviderSession initiateProviderSessionForRequestLocked(RequestSession requestSession, List<String> list) {
        if (!list.isEmpty() && !isServiceCapableLocked(list)) {
            Slog.i(TAG, "Service does not have the required capabilities");
            return null;
        }
        if (this.mInfo == null) {
            Slog.w(TAG, "Initiating provider session for request but mInfo is null. This shouldn't happen");
            return null;
        }
        return requestSession.initiateProviderSession(this.mInfo, new RemoteCredentialService(getContext(), this.mInfo.getServiceInfo().getComponentName(), ((AbstractPerUserSystemService) this).mUserId));
    }

    @GuardedBy({"mLock"})
    boolean isServiceCapableLocked(List<String> list) {
        if (this.mInfo == null) {
            return false;
        }
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (this.mInfo.hasCapability(it.next())) {
                return true;
            }
        }
        return false;
    }

    @GuardedBy({"mLock"})
    public CredentialProviderInfo getCredentialProviderInfo() {
        return this.mInfo;
    }

    @GuardedBy({"mLock"})
    protected void handlePackageUpdateLocked(String str) {
        CredentialProviderInfo credentialProviderInfo = this.mInfo;
        if (credentialProviderInfo == null || credentialProviderInfo.getServiceInfo() == null || !this.mInfo.getServiceInfo().getComponentName().getPackageName().equals(str)) {
            return;
        }
        try {
            newServiceInfoLocked(this.mInfo.getServiceInfo().getComponentName());
        } catch (PackageManager.NameNotFoundException e) {
            Slog.e(TAG, "Issue while updating serviceInfo: " + e.getMessage());
        }
    }
}
