package com.android.server.os;

import android.content.Context;
import android.os.IBinder;
import com.android.server.SystemService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BugreportManagerService extends SystemService {
    private static final String TAG = "BugreportManagerService";
    private BugreportManagerServiceImpl mService;

    public BugreportManagerService(Context context) {
        super(context);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.os.BugreportManagerServiceImpl, android.os.IBinder] */
    public void onStart() {
        ?? bugreportManagerServiceImpl = new BugreportManagerServiceImpl(getContext());
        this.mService = bugreportManagerServiceImpl;
        publishBinderService("bugreport", (IBinder) bugreportManagerServiceImpl);
    }
}
