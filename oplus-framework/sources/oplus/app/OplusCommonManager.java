package oplus.app;

import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Log;

/* loaded from: classes.dex */
public abstract class OplusCommonManager {
    protected final IBinder mRemote;

    public OplusCommonManager(String name) {
        this(ServiceManager.getService(name), name);
    }

    public OplusCommonManager(IBinder remote, String name) {
        if (remote == null) {
            Log.e("OplusCommonManager", "remote is null: " + name);
            this.mRemote = ServiceManager.getService(name);
            Log.d("OplusCommonManager", "Retry remote is null: " + (remote == null));
            return;
        }
        this.mRemote = remote;
    }
}
