package android.debug;

import android.bluetooth.BluetoothAdapterExtImpl;
import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public class BluetoothLogManager extends IDebugLogManager {
    private static final String BT_LOG_ALWAYS_ON_PROP = "persist.sys.btlog.alwayson";
    private static final String BT_LOG_PROP = "persist.sys.btlog.enable";
    private static final String TAG = TAG_BASE + BluetoothLogManager.class.getSimpleName();
    private static volatile BluetoothLogManager sInstance = null;
    private BluetoothAdapterExtImpl mBluetoothAdapterExt;
    private Context mContext;

    public BluetoothLogManager(Context context) {
        this.mBluetoothAdapterExt = null;
        this.mContext = context;
        this.mBluetoothAdapterExt = BluetoothAdapterExtImpl.getInstance();
    }

    public static BluetoothLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BluetoothLogManager.class) {
                if (sInstance == null) {
                    sInstance = new BluetoothLogManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) {
        Log.v(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param);
        SystemProperties.set(BT_LOG_PROP, "1");
        SystemProperties.set(BT_LOG_ALWAYS_ON_PROP, "true");
        this.mBluetoothAdapterExt.oplusEnableVerboseLogging(true);
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() {
        Log.v(TAG, "setLogOff.");
        SystemProperties.set(BT_LOG_PROP, "0");
        SystemProperties.set(BT_LOG_ALWAYS_ON_PROP, "false");
        this.mBluetoothAdapterExt.oplusEnableVerboseLogging(false);
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() {
    }
}
