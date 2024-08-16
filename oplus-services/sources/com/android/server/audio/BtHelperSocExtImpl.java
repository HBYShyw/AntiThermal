package com.android.server.audio;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothLeAudio;
import android.content.Intent;
import android.util.Log;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BtHelperSocExtImpl implements IBtHelperSocExt {
    private static final String TAG = "BtHelperSocExtImpl";
    private BtHelper mBtHelper;

    public BtHelperSocExtImpl(Object obj) {
        this.mBtHelper = (BtHelper) obj;
    }

    @Override // com.android.server.audio.IBtHelperSocExt
    public boolean isLeAudioDevice(Intent intent) {
        String stringExtra = intent.getStringExtra("android.bluetooth.device.extra.NAME");
        if (stringExtra == null || !"fake_hfp_broadcast".equals(stringExtra)) {
            return false;
        }
        Log.d(TAG, "Fake HFP active device broadcast,return");
        return true;
    }

    @Override // com.android.server.audio.IBtHelperSocExt
    public boolean isNextBtActiveDeviceAvailableForMusic(BluetoothA2dp bluetoothA2dp, BluetoothLeAudio bluetoothLeAudio) {
        boolean z;
        if (bluetoothA2dp != null) {
            z = bluetoothA2dp.isFallbackDeviceUsable();
            if (AudioService.DEBUG_DEVICES) {
                Log.i(TAG, "A2DP isFallbackDeviceUsable()=" + z);
            }
            if (z) {
                return z;
            }
        } else {
            z = false;
        }
        if (bluetoothLeAudio != null) {
            z = bluetoothLeAudio.isFallbackDeviceUsable();
            if (AudioService.DEBUG_DEVICES) {
                Log.i(TAG, "mLeAudio isFallbackDeviceUsable()=" + z);
            }
        }
        return z;
    }

    @Override // com.android.server.audio.IBtHelperSocExt
    public boolean isBluetoothScoOn() {
        return this.mBtHelper.isBluetoothScoOn();
    }
}
