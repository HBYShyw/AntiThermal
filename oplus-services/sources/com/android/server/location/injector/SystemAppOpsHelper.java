package com.android.server.location.injector;

import android.app.AppOpsManager;
import android.content.Context;
import android.location.util.identity.CallerIdentity;
import android.os.Binder;
import com.android.internal.util.Preconditions;
import com.android.server.FgThread;
import com.android.server.location.common.OplusLbsFactory;
import com.android.server.location.interfaces.IOplusLBSMainClass;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SystemAppOpsHelper extends AppOpsHelper {
    private AppOpsManager mAppOps;
    private final Context mContext;

    public SystemAppOpsHelper(Context context) {
        this.mContext = context;
    }

    public void onSystemReady() {
        if (this.mAppOps != null) {
            return;
        }
        AppOpsManager appOpsManager = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
        Objects.requireNonNull(appOpsManager);
        this.mAppOps = appOpsManager;
        appOpsManager.startWatchingMode(0, (String) null, 1, new AppOpsManager.OnOpChangedListener() { // from class: com.android.server.location.injector.SystemAppOpsHelper$$ExternalSyntheticLambda1
            @Override // android.app.AppOpsManager.OnOpChangedListener
            public final void onOpChanged(String str, String str2) {
                SystemAppOpsHelper.this.lambda$onSystemReady$1(str, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0(String str) {
        notifyAppOpChanged(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$1(String str, final String str2) {
        if (str2 == null) {
            FgThread.getHandler().post(new Runnable() { // from class: com.android.server.location.injector.SystemAppOpsHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SystemAppOpsHelper.this.lambda$onSystemReady$0(str2);
                }
            });
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public boolean startOpNoThrow(int i, CallerIdentity callerIdentity) {
        Preconditions.checkState(this.mAppOps != null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mAppOps.startOpNoThrow(i, callerIdentity.getUid(), callerIdentity.getPackageName(), false, callerIdentity.getAttributionTag(), callerIdentity.getListenerId()) == 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public void finishOp(int i, CallerIdentity callerIdentity) {
        Preconditions.checkState(this.mAppOps != null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mAppOps.finishOp(i, callerIdentity.getUid(), callerIdentity.getPackageName(), callerIdentity.getAttributionTag());
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public boolean checkOpNoThrow(int i, CallerIdentity callerIdentity) {
        Preconditions.checkState(this.mAppOps != null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((IOplusLBSMainClass) OplusLbsFactory.getInstance().getFeature(IOplusLBSMainClass.DEFAULT, this.mContext)).checkOpNoThrow(this.mAppOps, i, callerIdentity, clearCallingIdentity);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public boolean noteOp(int i, CallerIdentity callerIdentity) {
        Preconditions.checkState(this.mAppOps != null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mAppOps.noteOp(i, callerIdentity.getUid(), callerIdentity.getPackageName(), callerIdentity.getAttributionTag(), callerIdentity.getListenerId()) == 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public boolean noteOpNoThrow(int i, CallerIdentity callerIdentity) {
        Preconditions.checkState(this.mAppOps != null);
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mAppOps.noteOpNoThrow(i, callerIdentity.getUid(), callerIdentity.getPackageName(), callerIdentity.getAttributionTag(), callerIdentity.getListenerId()) == 0;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
