package com.android.server.bluetooth;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothSatelliteModeListener {
    private static final int MSG_SATELLITE_MODE_CHANGED = 0;
    private static final String TAG = "BluetoothSatelliteModeListener";
    private final BluetoothManagerService mBluetoothManagerService;
    private final BluetoothSatelliteModeHandler mHandler;
    private final ContentObserver mSatelliteModeObserver;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothSatelliteModeListener(BluetoothManagerService bluetoothManagerService, Looper looper, Context context) {
        ContentObserver contentObserver = new ContentObserver(null) { // from class: com.android.server.bluetooth.BluetoothSatelliteModeListener.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                BluetoothSatelliteModeListener.this.mHandler.sendEmptyMessage(0);
            }
        };
        this.mSatelliteModeObserver = contentObserver;
        Log.d(TAG, " BluetoothSatelliteModeListener");
        this.mBluetoothManagerService = bluetoothManagerService;
        this.mHandler = new BluetoothSatelliteModeHandler(looper);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("satellite_mode_radios"), false, contentObserver);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("satellite_mode_enabled"), false, contentObserver);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BluetoothSatelliteModeHandler extends Handler {
        BluetoothSatelliteModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 0) {
                Log.e(BluetoothSatelliteModeListener.TAG, "Invalid message: " + message.what);
                return;
            }
            BluetoothSatelliteModeListener.this.handleSatelliteModeChange();
        }
    }

    @VisibleForTesting
    public void handleSatelliteModeChange() {
        this.mBluetoothManagerService.onSatelliteModeChanged();
    }
}
