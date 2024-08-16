package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.content.AttributionSource;
import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import com.mediatek.bt.BluetoothLeCallControl;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public final class BluetoothAdapterSocExtImpl {
    private static final boolean DBG;
    public static final String TAG = "BluetoothAdapterSocExtImpl";

    static {
        DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
    }

    public void closeProfileProxy(int profile, BluetoothProfile proxy) {
        switch (profile) {
            case 27:
                BluetoothLeCallControl tbs = (BluetoothLeCallControl) proxy;
                tbs.close();
                return;
            default:
                return;
        }
    }

    public boolean getProfileProxy(Context context, BluetoothProfile.ServiceListener listener, int profile) {
        if (context == null || listener == null || profile != 27) {
            return false;
        }
        new BluetoothLeCallControl(context, listener);
        return true;
    }

    public boolean isBroadcastActive(IBluetooth service, ReentrantReadWriteLock serviceLock, AttributionSource attributionSource) {
        Log.w(TAG, "mtk not support api isBroadcastActive");
        return false;
    }
}
