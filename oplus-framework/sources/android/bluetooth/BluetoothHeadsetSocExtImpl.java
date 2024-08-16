package android.bluetooth;

import android.content.AttributionSource;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public final class BluetoothHeadsetSocExtImpl implements IBluetoothHeadsetSocExt {
    private static final boolean DBG;
    public static final String TAG = "BluetoothHeadsetSocExtImpl";

    static {
        DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
    }

    public BluetoothHeadsetSocExtImpl(Object base) {
    }

    public void phoneStateChangedDsDa(IBluetoothHeadset headsetService, AttributionSource attributionSource, int numActive, int numHeld, int callState, String number, int type, String name) {
        Log.w(TAG, "mtk not support api phoneStateChangedDsDa");
    }

    public void clccResponseDsDa(IBluetoothHeadset headsetService, AttributionSource attributionSource, int index, int direction, int status, int mode, boolean mpty, String number, int type) {
        Log.w(TAG, "mtk not support api clccResponseDsDa");
    }
}
