package android.bluetooth;

import android.content.AttributionSource;
import android.os.SystemProperties;

/* loaded from: classes.dex */
public final class BluetoothA2dpSocExtImpl implements IBluetoothA2dpSocExt {
    private static final boolean DBG;
    public static final String TAG = "BluetoothA2dpSocExtImpl";
    private BluetoothA2dp mBluetoothA2dp;

    static {
        DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
    }

    public BluetoothA2dpSocExtImpl(Object bluetoothA2dp) {
        this.mBluetoothA2dp = (BluetoothA2dp) bluetoothA2dp;
    }

    public boolean isFallbackDeviceUsable(IBluetoothLeAudio service, AttributionSource attributionSource) {
        return false;
    }
}
