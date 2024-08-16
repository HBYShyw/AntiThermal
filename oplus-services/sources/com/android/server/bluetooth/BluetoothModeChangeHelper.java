package com.android.server.bluetooth;

import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHearingAid;
import android.bluetooth.BluetoothLeAudio;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothModeChangeHelper {
    private static final String TAG = "BluetoothModeChangeHelper";
    private volatile BluetoothA2dp mA2dp;
    private final BluetoothAdapter mAdapter;
    private String mBluetoothPackageName;
    private final Context mContext;
    private volatile BluetoothHearingAid mHearingAid;
    private volatile BluetoothLeAudio mLeAudio;
    private final BluetoothProfile.ServiceListener mProfileServiceListener;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothModeChangeHelper(Context context) {
        BluetoothProfile.ServiceListener serviceListener = new BluetoothProfile.ServiceListener() { // from class: com.android.server.bluetooth.BluetoothModeChangeHelper.1
            @Override // android.bluetooth.BluetoothProfile.ServiceListener
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                if (i == 2) {
                    BluetoothModeChangeHelper.this.mA2dp = (BluetoothA2dp) bluetoothProfile;
                } else if (i == 21) {
                    BluetoothModeChangeHelper.this.mHearingAid = (BluetoothHearingAid) bluetoothProfile;
                } else {
                    if (i != 22) {
                        return;
                    }
                    BluetoothModeChangeHelper.this.mLeAudio = (BluetoothLeAudio) bluetoothProfile;
                }
            }

            @Override // android.bluetooth.BluetoothProfile.ServiceListener
            public void onServiceDisconnected(int i) {
                if (i == 2) {
                    BluetoothModeChangeHelper.this.mA2dp = null;
                } else if (i == 21) {
                    BluetoothModeChangeHelper.this.mHearingAid = null;
                } else {
                    if (i != 22) {
                        return;
                    }
                    BluetoothModeChangeHelper.this.mLeAudio = null;
                }
            }
        };
        this.mProfileServiceListener = serviceListener;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mAdapter = defaultAdapter;
        this.mContext = context;
        defaultAdapter.getProfileProxy(context, serviceListener, 2);
        defaultAdapter.getProfileProxy(context, serviceListener, 21);
        defaultAdapter.getProfileProxy(context, serviceListener, 22);
    }

    @VisibleForTesting
    public boolean isMediaProfileConnected() {
        return isA2dpConnected() || isHearingAidConnected() || isLeAudioConnected() || isBroadcastActive();
    }

    @VisibleForTesting
    public boolean isBluetoothOn() {
        BluetoothAdapter bluetoothAdapter = this.mAdapter;
        if (bluetoothAdapter == null) {
            return false;
        }
        return bluetoothAdapter.isLeEnabled();
    }

    @VisibleForTesting
    public boolean isAirplaneModeOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
    }

    @RequiresPermission("android.permission.BLUETOOTH_PRIVILEGED")
    @VisibleForTesting
    public void onAirplaneModeChanged(BluetoothManagerService bluetoothManagerService) {
        bluetoothManagerService.onAirplaneModeChanged();
    }

    @VisibleForTesting
    public int getSettingsInt(String str) {
        return Settings.Global.getInt(this.mContext.getContentResolver(), str, 0);
    }

    @VisibleForTesting
    public void setSettingsInt(String str, int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), str, i);
    }

    public int getSettingsSecureInt(String str, int i) {
        return Settings.Secure.getInt(this.mContext.createContextAsUser(UserHandle.of(ActivityManager.getCurrentUser()), 0).getContentResolver(), str, i);
    }

    public void setSettingsSecureInt(String str, int i) {
        Settings.Secure.putInt(this.mContext.createContextAsUser(UserHandle.of(ActivityManager.getCurrentUser()), 0).getContentResolver(), str, i);
    }

    @VisibleForTesting
    public void showToastMessage() {
        Toast.makeText(this.mContext, this.mContext.getResources().getString(Resources.getSystem().getIdentifier("bluetooth_airplane_mode_toast", "string", "android")), 1).show();
    }

    private boolean isA2dpConnected() {
        BluetoothA2dp bluetoothA2dp = this.mA2dp;
        return bluetoothA2dp != null && bluetoothA2dp.getConnectedDevices().size() > 0;
    }

    private boolean isHearingAidConnected() {
        BluetoothHearingAid bluetoothHearingAid = this.mHearingAid;
        return bluetoothHearingAid != null && bluetoothHearingAid.getConnectedDevices().size() > 0;
    }

    private boolean isLeAudioConnected() {
        BluetoothLeAudio bluetoothLeAudio = this.mLeAudio;
        return bluetoothLeAudio != null && bluetoothLeAudio.getConnectedDevices().size() > 0;
    }

    public boolean isBluetoothOnAPM() {
        return Settings.Secure.getInt(this.mContext.createContextAsUser(UserHandle.of(ActivityManager.getCurrentUser()), 0).getContentResolver(), BluetoothAirplaneModeListener.BLUETOOTH_APM_STATE, 0) == 1;
    }

    public String getBluetoothPackageName() {
        String str = this.mBluetoothPackageName;
        if (str != null) {
            return str;
        }
        for (String str2 : this.mContext.getPackageManager().getPackagesForUid(1002)) {
            try {
                if (this.mContext.getPackageManager().getResourcesForApplication(str2).getIdentifier("bluetooth_and_wifi_stays_on_title", "string", str2) != 0) {
                    this.mBluetoothPackageName = str2;
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e(TAG, "Could not find package " + str2);
            } catch (Exception e) {
                Log.e(TAG, "Error while loading package" + e);
            }
        }
        return this.mBluetoothPackageName;
    }

    private boolean isBroadcastActive() {
        return this.mAdapter.isBroadcastActive();
    }
}
