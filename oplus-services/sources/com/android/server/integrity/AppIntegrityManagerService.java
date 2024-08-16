package com.android.server.integrity;

import android.content.Context;
import android.os.IBinder;
import com.android.server.SystemService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AppIntegrityManagerService extends SystemService {
    private Context mContext;
    private AppIntegrityManagerServiceImpl mService;

    public AppIntegrityManagerService(Context context) {
        super(context);
        this.mContext = context;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.server.integrity.AppIntegrityManagerServiceImpl, android.os.IBinder] */
    public void onStart() {
        ?? create = AppIntegrityManagerServiceImpl.create(this.mContext);
        this.mService = create;
        publishBinderService("app_integrity", (IBinder) create);
    }
}
