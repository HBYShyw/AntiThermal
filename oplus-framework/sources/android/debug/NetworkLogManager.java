package android.debug;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
public class NetworkLogManager extends IDebugLogManager {
    private static final String TAG = TAG_BASE + NetworkLogManager.class.getSimpleName();
    private static volatile NetworkLogManager sInstance = null;
    private Context mContext;

    public NetworkLogManager(Context context) {
        this.mContext = context;
    }

    public static NetworkLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (NetworkLogManager.class) {
                if (sInstance == null) {
                    sInstance = new NetworkLogManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) throws RemoteException {
        Log.v(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param);
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() throws RemoteException {
        Log.v(TAG, "setLogOff: ");
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() throws RemoteException {
        Log.v(TAG, "setLogDump: ");
    }
}
