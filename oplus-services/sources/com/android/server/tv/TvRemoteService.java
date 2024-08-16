package com.android.server.tv;

import android.content.Context;
import com.android.server.SystemService;
import com.android.server.Watchdog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TvRemoteService extends SystemService implements Watchdog.Monitor {
    private static final boolean DEBUG = false;
    private static final String TAG = "TvRemoteService";
    private final Object mLock;
    private final TvRemoteProviderWatcher mWatcher;

    public void onStart() {
    }

    public TvRemoteService(Context context) {
        super(context);
        Object obj = new Object();
        this.mLock = obj;
        this.mWatcher = new TvRemoteProviderWatcher(context, obj);
        Watchdog.getInstance().addMonitor(this);
    }

    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    public void onBootPhase(int i) {
        if (i == 600) {
            this.mWatcher.start();
        }
    }
}
