package com.android.server;

import android.content.Context;
import android.util.Log;
import com.android.internal.util.ConcurrentUtils;
import com.android.server.SystemService;
import com.android.server.location.contexthub.ContextHubService;
import com.android.server.location.contexthub.IContextHubWrapper;
import java.util.concurrent.Future;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ContextHubSystemService extends SystemService {
    private static final String TAG = "ContextHubSystemService";
    private ContextHubService mContextHubService;
    private Future<?> mInit;

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    public ContextHubSystemService(final Context context) {
        super(context);
        this.mInit = SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.ContextHubSystemService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ContextHubSystemService.this.lambda$new$0(context);
            }
        }, "Init ContextHubSystemService");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Context context) {
        this.mContextHubService = new ContextHubService(context, IContextHubWrapper.getContextHubWrapper());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 500) {
            Log.d(TAG, "onBootPhase: PHASE_SYSTEM_SERVICES_READY");
            ConcurrentUtils.waitForFutureNoInterrupt(this.mInit, "Wait for ContextHubSystemService init");
            this.mInit = null;
            publishBinderService("contexthub", this.mContextHubService);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        this.mContextHubService.onUserChanged();
    }
}
