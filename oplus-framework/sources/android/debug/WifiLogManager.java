package android.debug;

import android.content.Context;
import android.net.wifi.OplusWifiManager;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
public class WifiLogManager extends IDebugLogManager {
    private static final String TAG = TAG_BASE + WifiLogManager.class.getSimpleName();
    private static volatile WifiLogManager sInstance = null;
    private Context mContext;
    private OplusWifiManager mWifiManager;

    public WifiLogManager(Context context) {
        this.mWifiManager = null;
        this.mContext = context;
        this.mWifiManager = new OplusWifiManager(context);
    }

    public static WifiLogManager getInstance(Context context) {
        WifiLogManager wifiLogManager;
        synchronized (WifiLogManager.class) {
            if (sInstance == null) {
                sInstance = new WifiLogManager(context);
            }
            wifiLogManager = sInstance;
        }
        return wifiLogManager;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) throws RemoteException {
        Log.v(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param);
        this.mWifiManager.setLogOn(maxSize, param);
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() throws RemoteException {
        Log.v(TAG, "setLogOff: ");
        this.mWifiManager.setLogOff();
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() throws RemoteException {
        Log.v(TAG, "setLogDump: ");
        this.mWifiManager.setLogDump();
    }
}
