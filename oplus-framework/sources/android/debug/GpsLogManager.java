package android.debug;

import android.content.Context;
import android.location.OplusLocationManager;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
public class GpsLogManager extends IDebugLogManager {
    private static final String TAG = TAG_BASE + GpsLogManager.class.getSimpleName();
    private static volatile GpsLogManager sInstance = null;
    private Context mContext;
    private OplusLocationManager mLocationManager;

    public GpsLogManager(Context context) {
        this.mLocationManager = null;
        this.mContext = context;
        this.mLocationManager = new OplusLocationManager();
    }

    public static GpsLogManager getInstance(Context context) {
        GpsLogManager gpsLogManager;
        synchronized (GpsLogManager.class) {
            if (sInstance == null) {
                sInstance = new GpsLogManager(context);
            }
            gpsLogManager = sInstance;
        }
        return gpsLogManager;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) throws RemoteException {
        Log.v(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param);
        this.mLocationManager.setDebugOn();
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() throws RemoteException {
        Log.v(TAG, "setLogOff: ");
        this.mLocationManager.setDebugOff();
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() throws RemoteException {
        Log.v(TAG, "setLogDump: ");
        this.mLocationManager.setDebugDump();
    }
}
