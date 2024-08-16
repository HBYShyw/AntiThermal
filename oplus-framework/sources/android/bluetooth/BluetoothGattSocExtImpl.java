package android.bluetooth;

import android.content.AttributionSource;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public final class BluetoothGattSocExtImpl implements IBluetoothGattSocExt {
    private static final boolean DBG;
    public static final String TAG = "BluetoothGattSocExtImpl";

    static {
        DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
    }

    public BluetoothGattSocExtImpl(Object base) {
    }

    public boolean subrateModeRequest(IBluetoothGatt iGatt, int clientIf, String address, int subrateMode, AttributionSource attributionSource) {
        Log.w(TAG, "mtk not support api subrateModeRequest");
        return true;
    }

    public boolean leSubrateRequest(IBluetoothGatt iGatt, int clientIf, String address, int subrateMin, int subrateMax, int maxLatency, int contNumber, int supervisionTimeout, AttributionSource attributionSource) {
        Log.w(TAG, "mtk not support api leSubrateRequest");
        return true;
    }
}
