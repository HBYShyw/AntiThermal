package com.android.server.systemcaptions;

import android.R;
import android.content.Context;
import com.android.server.infra.AbstractMasterSystemService;
import com.android.server.infra.FrameworkResourcesServiceNameResolver;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SystemCaptionsManagerService extends AbstractMasterSystemService<SystemCaptionsManagerService, SystemCaptionsManagerPerUserService> {
    public void onStart() {
    }

    public SystemCaptionsManagerService(Context context) {
        super(context, new FrameworkResourcesServiceNameResolver(context, R.string.config_retailDemoPackageSignature), null, 68);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public SystemCaptionsManagerPerUserService newServiceLocked(int i, boolean z) {
        SystemCaptionsManagerPerUserService systemCaptionsManagerPerUserService = new SystemCaptionsManagerPerUserService(this, this.mLock, z, i);
        systemCaptionsManagerPerUserService.initializeLocked();
        return systemCaptionsManagerPerUserService;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceRemoved(SystemCaptionsManagerPerUserService systemCaptionsManagerPerUserService, int i) {
        synchronized (this.mLock) {
            systemCaptionsManagerPerUserService.destroyLocked();
        }
    }
}
