package com.android.server.bluetooth;

import android.annotation.RequiresPermission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothAirplaneModeListener {
    public static final String APM_BT_ENABLED_NOTIFICATION = "apm_bt_enabled_notification";
    public static final String APM_BT_NOTIFICATION = "apm_bt_notification";
    public static final String APM_ENHANCEMENT = "apm_enhancement_enabled";
    public static final String APM_USER_TOGGLED_BLUETOOTH = "apm_user_toggled_bluetooth";
    public static final String APM_WIFI_BT_NOTIFICATION = "apm_wifi_bt_notification";
    public static final String BLUETOOTH_APM_STATE = "bluetooth_apm_state";

    @VisibleForTesting
    static final int MAX_TOAST_COUNT = 10;
    private static final int MSG_AIRPLANE_MODE_CHANGED = 0;
    public static final int NOTIFICATION_NOT_SHOWN = 0;
    public static final int NOTIFICATION_SHOWN = 1;
    private static final String TAG = "BluetoothAirplaneModeListener";

    @VisibleForTesting
    static final String TOAST_COUNT = "bluetooth_airplane_toast_count";
    public static final int UNUSED = 0;
    public static final int USED = 1;
    public static final String WIFI_APM_STATE = "wifi_apm_state";
    private BluetoothModeChangeHelper mAirplaneHelper;
    private final ContentObserver mAirplaneModeObserver;
    private final BluetoothManagerService mBluetoothManager;
    private final Context mContext;
    private final BluetoothAirplaneModeHandler mHandler;
    private BluetoothNotificationManager mNotificationManager;
    private boolean mIsBluetoothOnBeforeApmToggle = false;
    private boolean mIsBluetoothOnAfterApmToggle = false;
    private boolean mUserToggledBluetoothDuringApm = false;
    private boolean mUserToggledBluetoothDuringApmWithinMinute = false;
    private boolean mIsMediaProfileConnectedBeforeApmToggle = false;
    private long mApmEnabledTime = 0;

    @VisibleForTesting
    int mToastCount = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothAirplaneModeListener(BluetoothManagerService bluetoothManagerService, Looper looper, Context context, BluetoothNotificationManager bluetoothNotificationManager) {
        ContentObserver contentObserver = new ContentObserver(null) { // from class: com.android.server.bluetooth.BluetoothAirplaneModeListener.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                BluetoothAirplaneModeListener.this.mHandler.sendMessage(BluetoothAirplaneModeListener.this.mHandler.obtainMessage(0));
            }
        };
        this.mAirplaneModeObserver = contentObserver;
        this.mBluetoothManager = bluetoothManagerService;
        this.mNotificationManager = bluetoothNotificationManager;
        this.mContext = context;
        this.mHandler = new BluetoothAirplaneModeHandler(looper);
        context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("airplane_mode_on"), true, contentObserver);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class BluetoothAirplaneModeHandler extends Handler {
        BluetoothAirplaneModeHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 0) {
                BluetoothAirplaneModeListener.this.handleAirplaneModeChange();
                return;
            }
            Log.e(BluetoothAirplaneModeListener.TAG, "Invalid message: " + message.what);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void start(BluetoothModeChangeHelper bluetoothModeChangeHelper) {
        Log.i(TAG, "start");
        this.mAirplaneHelper = bluetoothModeChangeHelper;
        this.mToastCount = bluetoothModeChangeHelper.getSettingsInt(TOAST_COUNT);
    }

    @VisibleForTesting
    boolean shouldPopToast() {
        int i = this.mToastCount;
        if (i >= 10) {
            return false;
        }
        int i2 = i + 1;
        this.mToastCount = i2;
        this.mAirplaneHelper.setSettingsInt(TOAST_COUNT, i2);
        return true;
    }

    @RequiresPermission("android.permission.BLUETOOTH_PRIVILEGED")
    @VisibleForTesting
    void handleAirplaneModeChange() {
        BluetoothModeChangeHelper bluetoothModeChangeHelper = this.mAirplaneHelper;
        if (bluetoothModeChangeHelper == null) {
            return;
        }
        if (bluetoothModeChangeHelper.isAirplaneModeOn()) {
            this.mApmEnabledTime = SystemClock.elapsedRealtime();
            this.mIsBluetoothOnBeforeApmToggle = this.mAirplaneHelper.isBluetoothOn();
            this.mIsBluetoothOnAfterApmToggle = shouldSkipAirplaneModeChange();
            this.mIsMediaProfileConnectedBeforeApmToggle = this.mAirplaneHelper.isMediaProfileConnected();
            if (this.mIsBluetoothOnAfterApmToggle) {
                Log.i(TAG, "Ignore airplane mode change");
                this.mAirplaneHelper.setSettingsInt("bluetooth_on", 2);
                if (!isApmEnhancementEnabled() || !isBluetoothToggledOnApm()) {
                    if (shouldPopToast()) {
                        this.mAirplaneHelper.showToastMessage();
                        return;
                    }
                    return;
                } else if (isWifiEnabledOnApm() && isFirstTimeNotification(APM_WIFI_BT_NOTIFICATION)) {
                    try {
                        sendApmNotification("bluetooth_and_wifi_stays_on_title", "bluetooth_and_wifi_stays_on_message", APM_WIFI_BT_NOTIFICATION);
                        return;
                    } catch (Exception unused) {
                        Log.e(TAG, "APM enhancement BT and Wi-Fi stays on notification not shown");
                        return;
                    }
                } else {
                    if (isWifiEnabledOnApm() || !isFirstTimeNotification(APM_BT_NOTIFICATION)) {
                        return;
                    }
                    try {
                        sendApmNotification("bluetooth_stays_on_title", "bluetooth_stays_on_message", APM_BT_NOTIFICATION);
                        return;
                    } catch (Exception unused2) {
                        Log.e(TAG, "APM enhancement BT stays on notification not shown");
                        return;
                    }
                }
            }
        } else {
            this.mUserToggledBluetoothDuringApm = false;
            this.mUserToggledBluetoothDuringApmWithinMinute = false;
        }
        this.mAirplaneHelper.onAirplaneModeChanged(this.mBluetoothManager);
    }

    @VisibleForTesting
    boolean shouldSkipAirplaneModeChange() {
        boolean z = isApmEnhancementEnabled() && isBluetoothToggledOnApm();
        if (!z && this.mAirplaneHelper.isBluetoothOn() && this.mAirplaneHelper.isMediaProfileConnected()) {
            return true;
        }
        if (z && this.mAirplaneHelper.isBluetoothOn() && this.mAirplaneHelper.isBluetoothOnAPM()) {
            return true;
        }
        return isApmEnhancementEnabled() && !isBluetoothToggledOnApm() && this.mAirplaneHelper.isBluetoothOn() && this.mAirplaneHelper.isBluetoothOnAPM();
    }

    private boolean isApmEnhancementEnabled() {
        return this.mAirplaneHelper.getSettingsInt(APM_ENHANCEMENT) == 1;
    }

    private boolean isBluetoothToggledOnApm() {
        return this.mAirplaneHelper.getSettingsSecureInt(APM_USER_TOGGLED_BLUETOOTH, 0) == 1;
    }

    private boolean isWifiEnabledOnApm() {
        return this.mAirplaneHelper.getSettingsInt("wifi_on") != 0 && this.mAirplaneHelper.getSettingsSecureInt(WIFI_APM_STATE, 0) == 1;
    }

    private boolean isFirstTimeNotification(String str) {
        return this.mAirplaneHelper.getSettingsSecureInt(str, 0) == 0;
    }

    public void sendApmNotification(String str, String str2, String str3) throws PackageManager.NameNotFoundException {
        String bluetoothPackageName = this.mAirplaneHelper.getBluetoothPackageName();
        if (bluetoothPackageName == null) {
            Log.e(TAG, "Unable to find Bluetooth package name with APM notification resources");
            return;
        }
        Resources resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication(bluetoothPackageName);
        this.mNotificationManager.sendApmNotification(resourcesForApplication.getString(resourcesForApplication.getIdentifier(str, "string", bluetoothPackageName)), resourcesForApplication.getString(resourcesForApplication.getIdentifier(str2, "string", bluetoothPackageName)));
        this.mAirplaneHelper.setSettingsSecureInt(str3, 1);
    }

    public void updateBluetoothToggledTime() {
        if (!this.mUserToggledBluetoothDuringApm) {
            this.mUserToggledBluetoothDuringApmWithinMinute = SystemClock.elapsedRealtime() - this.mApmEnabledTime < 60000;
        }
        this.mUserToggledBluetoothDuringApm = true;
    }
}
