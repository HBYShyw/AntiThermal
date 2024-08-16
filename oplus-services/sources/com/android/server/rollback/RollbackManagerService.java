package com.android.server.rollback;

import android.content.Context;
import android.os.IBinder;
import com.android.server.LocalServices;
import com.android.server.SystemService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class RollbackManagerService extends SystemService {
    private RollbackManagerServiceImpl mService;

    public RollbackManagerService(Context context) {
        super(context);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.rollback.RollbackManagerServiceImpl, android.os.IBinder] */
    public void onStart() {
        ?? rollbackManagerServiceImpl = new RollbackManagerServiceImpl(getContext());
        this.mService = rollbackManagerServiceImpl;
        publishBinderService("rollback", (IBinder) rollbackManagerServiceImpl);
        LocalServices.addService(RollbackManagerInternal.class, this.mService);
    }

    public void onUserUnlocking(SystemService.TargetUser targetUser) {
        this.mService.onUnlockUser(targetUser.getUserIdentifier());
    }

    public void onBootPhase(int i) {
        if (i == 1000) {
            this.mService.onBootCompleted();
        }
    }
}
