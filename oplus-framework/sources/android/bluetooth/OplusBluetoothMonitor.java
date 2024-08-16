package android.bluetooth;

import android.app.ActivityThread;
import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import java.util.List;

/* loaded from: classes.dex */
public final class OplusBluetoothMonitor {
    public static final String AVRCPCONTROL_MONIT_EVENT = "avrcp_control";
    public static final String BLUETOOTH_MANAGER_SERVICE = "bluetooth_manager";
    public static final String CODECSWITCH_MONIT_EVENT = "codec_switch";
    private static final boolean DBG;
    public static final String MEDIAAUDIO_MONIT_EVENT = "media_audio";
    public static final String OPPTRANS_MONIT_EVENT = "opp_trans";
    public static final String PAIRCONN_MONIT_EVENT = "pair_conn";
    public static final String PHONEAUDIO_MONIT_EVENT = "phone_audio";
    public static final String SAUUPDATE_MONIT_EVENT = "sau_update";
    public static final String SCAN_MONIT_EVENT = "scan";
    public static final String SWITCH_MONIT_EVENT = "swicth";
    private static final String TAG = "OplusBluetoothMonitor";
    public static final String UNKNOWN_MONIT_EVENT = "unknown_event";
    private static OplusBluetoothMonitor sBluetoothMonitor;
    private Context mContext;

    static {
        DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
        sBluetoothMonitor = null;
    }

    public static synchronized OplusBluetoothMonitor getDefaultBluetoothMonitor(Context context) {
        OplusBluetoothMonitor oplusBluetoothMonitor;
        synchronized (OplusBluetoothMonitor.class) {
            if (DBG) {
                Log.d(TAG, "enter getDefaultBluetoothMonitor()");
            }
            if (sBluetoothMonitor == null) {
                sBluetoothMonitor = new OplusBluetoothMonitor(context);
            }
            oplusBluetoothMonitor = sBluetoothMonitor;
        }
        return oplusBluetoothMonitor;
    }

    OplusBluetoothMonitor(Context context) {
        this.mContext = context;
    }

    public boolean openBtAbnomalMonitor(List<String> monitorEvents, OplusBluetoothMonitorCallback callback, int... params) {
        if (DBG) {
            Log.d(TAG, "enter openBtAbnomalMonitor");
        }
        String packageName = ActivityThread.currentPackageName();
        if (callback == null) {
            Log.w(TAG, "callback is null!");
            return false;
        }
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp == null) {
            return false;
        }
        return bluetoothAdapterExtImp.openBtAbnomalMonitor(packageName, monitorEvents, callback);
    }

    public boolean closeBtAbnomalMonitor(List<String> monitorEvents, OplusBluetoothMonitorCallback callback, int... params) {
        if (DBG) {
            Log.d(TAG, "enter closeSwitchMonitor");
        }
        String packageName = ActivityThread.currentPackageName();
        if (callback == null) {
            Log.w(TAG, "callback is null!");
            return false;
        }
        BluetoothAdapterExtImpl bluetoothAdapterExtImp = BluetoothAdapterExtImpl.getInstance();
        if (bluetoothAdapterExtImp == null) {
            return false;
        }
        return bluetoothAdapterExtImp.closeBtAbnomalMonitor(packageName, monitorEvents, callback);
    }
}
