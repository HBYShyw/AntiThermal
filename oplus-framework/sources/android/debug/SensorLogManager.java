package android.debug;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
public class SensorLogManager extends IDebugLogManager {
    private static final String TAG = TAG_BASE + SensorLogManager.class.getSimpleName();
    private static volatile SensorLogManager sInstance = null;
    private Context mContext;

    public SensorLogManager(Context context) {
        this.mContext = context;
    }

    public static SensorLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SensorLogManager.class) {
                if (sInstance == null) {
                    sInstance = new SensorLogManager(context);
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
