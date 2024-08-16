package android.net.util;

import android.net.INetd;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class NetdService {
    private static final long BASE_TIMEOUT_MS = 100;
    private static final long MAX_TIMEOUT_MS = 1000;
    private static final String TAG = "NetdService";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface NetdCommand {
        void run(INetd iNetd) throws RemoteException;
    }

    public static INetd getInstance() {
        INetd asInterface = INetd.Stub.asInterface(ServiceManager.getService("netd"));
        if (asInterface == null) {
            Log.w(TAG, "WARNING: returning null INetd instance.");
        }
        return asInterface;
    }

    public static INetd get(long j) {
        if (j == 0) {
            return getInstance();
        }
        long elapsedRealtime = j > 0 ? SystemClock.elapsedRealtime() + j : Long.MAX_VALUE;
        long j2 = 0;
        while (true) {
            INetd netdService = getInstance();
            if (netdService != null) {
                return netdService;
            }
            long elapsedRealtime2 = elapsedRealtime - SystemClock.elapsedRealtime();
            if (elapsedRealtime2 <= 0) {
                return null;
            }
            j2 = Math.min(Math.min(j2 + BASE_TIMEOUT_MS, MAX_TIMEOUT_MS), elapsedRealtime2);
            try {
                Thread.sleep(j2);
            } catch (InterruptedException unused) {
            }
        }
    }

    public static INetd get() {
        return get(-1L);
    }

    public static void run(NetdCommand netdCommand) {
        while (true) {
            try {
                netdCommand.run(get());
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "error communicating with netd: " + e);
            }
        }
    }
}
