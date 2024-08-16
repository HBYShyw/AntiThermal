package com.android.server.os;

import android.content.Context;
import com.android.server.LocalServices;
import com.android.server.SystemService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NativeTombstoneManagerService extends SystemService {
    private static final String TAG = "NativeTombstoneManagerService";
    private NativeTombstoneManager mManager;

    public NativeTombstoneManagerService(Context context) {
        super(context);
    }

    public void onStart() {
        NativeTombstoneManager nativeTombstoneManager = new NativeTombstoneManager(getContext());
        this.mManager = nativeTombstoneManager;
        LocalServices.addService(NativeTombstoneManager.class, nativeTombstoneManager);
    }

    public void onBootPhase(int i) {
        if (i == 550) {
            this.mManager.onSystemReady();
        }
    }
}
