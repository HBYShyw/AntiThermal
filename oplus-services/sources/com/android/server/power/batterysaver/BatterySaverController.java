package com.android.server.power.batterysaver;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManagerInternal;
import android.os.BatterySaverPolicyConfig;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManagerInternal;
import android.os.UserHandle;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.EventLogTags;
import com.android.server.LocalServices;
import com.android.server.power.batterysaver.BatterySaverPolicy;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BatterySaverController implements BatterySaverPolicy.BatterySaverPolicyListener {
    static final boolean DEBUG = false;
    public static final int REASON_ADAPTIVE_DYNAMIC_POWER_SAVINGS_CHANGED = 11;
    public static final int REASON_DYNAMIC_POWER_SAVINGS_AUTOMATIC_OFF = 10;
    public static final int REASON_DYNAMIC_POWER_SAVINGS_AUTOMATIC_ON = 9;
    public static final int REASON_FULL_POWER_SAVINGS_CHANGED = 13;
    public static final int REASON_INTERACTIVE_CHANGED = 5;
    public static final int REASON_MANUAL_OFF = 3;
    public static final int REASON_MANUAL_ON = 2;
    public static final int REASON_PERCENTAGE_AUTOMATIC_OFF = 1;
    public static final int REASON_PERCENTAGE_AUTOMATIC_ON = 0;
    public static final int REASON_PLUGGED_IN = 7;
    public static final int REASON_POLICY_CHANGED = 6;
    public static final int REASON_SETTING_CHANGED = 8;
    public static final int REASON_STICKY_RESTORE = 4;
    public static final int REASON_TIMEOUT = 12;
    static final String TAG = "BatterySaverController";

    @GuardedBy({"mLock"})
    private boolean mAdaptiveEnabledRaw;
    private boolean mAdaptivePreviouslyEnabled;
    private final BatterySaverPolicy mBatterySaverPolicy;
    private final BatterySavingStats mBatterySavingStats;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private boolean mFullEnabledRaw;
    private boolean mFullPreviouslyEnabled;
    private final MyHandler mHandler;

    @GuardedBy({"mLock"})
    private boolean mIsInteractive;

    @GuardedBy({"mLock"})
    private boolean mIsPluggedIn;
    private final Object mLock;
    private PowerManager mPowerManager;
    private Optional<String> mPowerSaveModeChangedListenerPackage;

    @GuardedBy({"mLock"})
    private final ArrayList<PowerManagerInternal.LowPowerModeListener> mListeners = new ArrayList<>();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.server.power.batterysaver.BatterySaverController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            boolean z = true;
            char c = 65535;
            switch (action.hashCode()) {
                case -2128145023:
                    if (action.equals("android.intent.action.SCREEN_OFF")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1538406691:
                    if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1454123155:
                    if (action.equals("android.intent.action.SCREEN_ON")) {
                        c = 2;
                        break;
                    }
                    break;
                case 498807504:
                    if (action.equals("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED")) {
                        c = 3;
                        break;
                    }
                    break;
                case 870701415:
                    if (action.equals("android.os.action.DEVICE_IDLE_MODE_CHANGED")) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 2:
                    if (!BatterySaverController.this.isPolicyEnabled()) {
                        BatterySaverController.this.updateBatterySavingStats();
                        return;
                    } else {
                        BatterySaverController.this.mHandler.postStateChanged(false, 5);
                        return;
                    }
                case 1:
                    synchronized (BatterySaverController.this.mLock) {
                        BatterySaverController batterySaverController = BatterySaverController.this;
                        if (intent.getIntExtra("plugged", 0) == 0) {
                            z = false;
                        }
                        batterySaverController.mIsPluggedIn = z;
                        break;
                    }
                case 3:
                case 4:
                    break;
                default:
                    return;
            }
            BatterySaverController.this.updateBatterySavingStats();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String reasonToString(int i) {
        switch (i) {
            case 0:
                return "Percentage Auto ON";
            case 1:
                return "Percentage Auto OFF";
            case 2:
                return "Manual ON";
            case 3:
                return "Manual OFF";
            case 4:
                return "Sticky restore";
            case 5:
                return "Interactivity changed";
            case 6:
                return "Policy changed";
            case 7:
                return "Plugged in";
            case 8:
                return "Setting changed";
            case 9:
                return "Dynamic Warning Auto ON";
            case 10:
                return "Dynamic Warning Auto OFF";
            case 11:
                return "Adaptive Power Savings changed";
            case 12:
                return "timeout";
            case 13:
                return "Full Power Savings changed";
            default:
                return "Unknown reason: " + i;
        }
    }

    public BatterySaverController(Object obj, Context context, Looper looper, BatterySaverPolicy batterySaverPolicy, BatterySavingStats batterySavingStats) {
        this.mLock = obj;
        this.mContext = context;
        this.mHandler = new MyHandler(looper);
        this.mBatterySaverPolicy = batterySaverPolicy;
        batterySaverPolicy.addListener(this);
        this.mBatterySavingStats = batterySavingStats;
        PowerManager.invalidatePowerSaveModeCaches();
    }

    public void addListener(PowerManagerInternal.LowPowerModeListener lowPowerModeListener) {
        synchronized (this.mLock) {
            this.mListeners.add(lowPowerModeListener);
        }
    }

    public void systemReady() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        intentFilter.addAction("android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED");
        intentFilter.addCategory("oplusBrEx@android.intent.action.BATTERY_CHANGED@BATTERYSTATE=CHARGING_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        this.mHandler.postSystemReady();
    }

    private PowerManager getPowerManager() {
        if (this.mPowerManager == null) {
            PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
            Objects.requireNonNull(powerManager);
            this.mPowerManager = powerManager;
        }
        return this.mPowerManager;
    }

    @Override // com.android.server.power.batterysaver.BatterySaverPolicy.BatterySaverPolicyListener
    public void onBatterySaverPolicyChanged(BatterySaverPolicy batterySaverPolicy) {
        if (isPolicyEnabled()) {
            this.mHandler.postStateChanged(true, 6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class MyHandler extends Handler {
        private static final int ARG_DONT_SEND_BROADCAST = 0;
        private static final int ARG_SEND_BROADCAST = 1;
        private static final int MSG_STATE_CHANGED = 1;
        private static final int MSG_SYSTEM_READY = 2;

        public MyHandler(Looper looper) {
            super(looper);
        }

        void postStateChanged(boolean z, int i) {
            obtainMessage(1, z ? 1 : 0, i).sendToTarget();
        }

        public void postSystemReady() {
            obtainMessage(2, 0, 0).sendToTarget();
        }

        @Override // android.os.Handler
        public void dispatchMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            BatterySaverController.this.handleBatterySaverStateChanged(message.arg1 == 1, message.arg2);
        }
    }

    @VisibleForTesting
    public void enableBatterySaver(boolean z, int i) {
        synchronized (this.mLock) {
            if (getFullEnabledLocked() == z) {
                return;
            }
            setFullEnabledLocked(z);
            if (updatePolicyLevelLocked()) {
                this.mHandler.postStateChanged(true, i);
            }
        }
    }

    private boolean updatePolicyLevelLocked() {
        if (getFullEnabledLocked()) {
            return this.mBatterySaverPolicy.setPolicyLevel(2);
        }
        if (getAdaptiveEnabledLocked()) {
            return this.mBatterySaverPolicy.setPolicyLevel(1);
        }
        return this.mBatterySaverPolicy.setPolicyLevel(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BatterySaverPolicyConfig getPolicyLocked(int i) {
        return this.mBatterySaverPolicy.getPolicyLocked(i).toConfig();
    }

    public boolean isEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = getFullEnabledLocked() || (getAdaptiveEnabledLocked() && this.mBatterySaverPolicy.shouldAdvertiseIsEnabled());
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPolicyEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = getFullEnabledLocked() || getAdaptiveEnabledLocked();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFullEnabled() {
        boolean fullEnabledLocked;
        synchronized (this.mLock) {
            fullEnabledLocked = getFullEnabledLocked();
        }
        return fullEnabledLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setFullPolicyLocked(BatterySaverPolicyConfig batterySaverPolicyConfig, int i) {
        return setFullPolicyLocked(BatterySaverPolicy.Policy.fromConfig(batterySaverPolicyConfig), i);
    }

    boolean setFullPolicyLocked(BatterySaverPolicy.Policy policy, int i) {
        if (!this.mBatterySaverPolicy.setFullPolicyLocked(policy)) {
            return false;
        }
        this.mHandler.postStateChanged(true, i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAdaptiveEnabled() {
        boolean adaptiveEnabledLocked;
        synchronized (this.mLock) {
            adaptiveEnabledLocked = getAdaptiveEnabledLocked();
        }
        return adaptiveEnabledLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setAdaptivePolicyLocked(BatterySaverPolicyConfig batterySaverPolicyConfig, int i) {
        return setAdaptivePolicyLocked(BatterySaverPolicy.Policy.fromConfig(batterySaverPolicyConfig), i);
    }

    boolean setAdaptivePolicyLocked(BatterySaverPolicy.Policy policy, int i) {
        if (!this.mBatterySaverPolicy.setAdaptivePolicyLocked(policy)) {
            return false;
        }
        this.mHandler.postStateChanged(true, i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resetAdaptivePolicyLocked(int i) {
        if (!this.mBatterySaverPolicy.resetAdaptivePolicyLocked()) {
            return false;
        }
        this.mHandler.postStateChanged(true, i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setAdaptivePolicyEnabledLocked(boolean z, int i) {
        if (getAdaptiveEnabledLocked() == z) {
            return false;
        }
        setAdaptiveEnabledLocked(z);
        if (!updatePolicyLevelLocked()) {
            return false;
        }
        this.mHandler.postStateChanged(true, i);
        return true;
    }

    public boolean isInteractive() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsInteractive;
        }
        return z;
    }

    public BatterySaverPolicy getBatterySaverPolicy() {
        return this.mBatterySaverPolicy;
    }

    public boolean isLaunchBoostDisabled() {
        return isPolicyEnabled() && this.mBatterySaverPolicy.isLaunchBoostDisabled();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0028  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x003a A[Catch: all -> 0x00fe, TryCatch #0 {, blocks: (B:4:0x000b, B:6:0x0013, B:10:0x001d, B:13:0x0024, B:16:0x002b, B:19:0x0034, B:21:0x003a, B:22:0x0043, B:23:0x0065), top: B:3:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0033  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x002a  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void handleBatterySaverStateChanged(boolean z, int i) {
        int i2;
        byte b;
        PowerManagerInternal.LowPowerModeListener[] lowPowerModeListenerArr;
        boolean isInteractive = getPowerManager().isInteractive();
        synchronized (this.mLock) {
            if (!getFullEnabledLocked() && !getAdaptiveEnabledLocked()) {
                b = false;
                EventLogTags.writeBatterySaverMode(!this.mFullPreviouslyEnabled ? 1 : 0, !this.mAdaptivePreviouslyEnabled ? 1 : 0, !getFullEnabledLocked() ? 1 : 0, getAdaptiveEnabledLocked() ? 1 : 0, isInteractive ? 1 : 0, b == false ? this.mBatterySaverPolicy.toEventLogString() : "", i);
                this.mFullPreviouslyEnabled = getFullEnabledLocked();
                this.mAdaptivePreviouslyEnabled = getAdaptiveEnabledLocked();
                lowPowerModeListenerArr = (PowerManagerInternal.LowPowerModeListener[]) this.mListeners.toArray(new PowerManagerInternal.LowPowerModeListener[0]);
                this.mIsInteractive = isInteractive;
            }
            b = true;
            if (!this.mFullPreviouslyEnabled) {
            }
            if (!this.mAdaptivePreviouslyEnabled) {
            }
            if (!getFullEnabledLocked()) {
            }
            EventLogTags.writeBatterySaverMode(!this.mFullPreviouslyEnabled ? 1 : 0, !this.mAdaptivePreviouslyEnabled ? 1 : 0, !getFullEnabledLocked() ? 1 : 0, getAdaptiveEnabledLocked() ? 1 : 0, isInteractive ? 1 : 0, b == false ? this.mBatterySaverPolicy.toEventLogString() : "", i);
            this.mFullPreviouslyEnabled = getFullEnabledLocked();
            this.mAdaptivePreviouslyEnabled = getAdaptiveEnabledLocked();
            lowPowerModeListenerArr = (PowerManagerInternal.LowPowerModeListener[]) this.mListeners.toArray(new PowerManagerInternal.LowPowerModeListener[0]);
            this.mIsInteractive = isInteractive;
        }
        PowerManagerInternal powerManagerInternal = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);
        if (powerManagerInternal != null) {
            powerManagerInternal.setPowerMode(1, isEnabled());
        }
        updateBatterySavingStats();
        if (z) {
            Slog.i(TAG, "Sending broadcasts for mode: " + isEnabled());
            Intent intent = new Intent("android.os.action.POWER_SAVE_MODE_CHANGED");
            intent.addFlags(1073741824);
            this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
            if (getPowerSaveModeChangedListenerPackage().isPresent()) {
                this.mContext.sendBroadcastAsUser(new Intent("android.os.action.POWER_SAVE_MODE_CHANGED").setPackage(getPowerSaveModeChangedListenerPackage().get()).addFlags(285212672), UserHandle.ALL);
            }
            Intent intent2 = new Intent("android.os.action.POWER_SAVE_MODE_CHANGED_INTERNAL");
            intent2.addFlags(1073741824);
            this.mContext.sendBroadcastAsUser(intent2, UserHandle.ALL, "android.permission.DEVICE_POWER");
            for (PowerManagerInternal.LowPowerModeListener lowPowerModeListener : lowPowerModeListenerArr) {
                lowPowerModeListener.onLowPowerModeChanged(this.mBatterySaverPolicy.getBatterySaverPolicy(lowPowerModeListener.getServiceType()));
            }
        }
    }

    private Optional<String> getPowerSaveModeChangedListenerPackage() {
        Optional<String> empty;
        if (this.mPowerSaveModeChangedListenerPackage == null) {
            String string = this.mContext.getString(R.string.date_time);
            if (((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).isSystemPackage(string)) {
                empty = Optional.of(string);
            } else {
                empty = Optional.empty();
            }
            this.mPowerSaveModeChangedListenerPackage = empty;
        }
        return this.mPowerSaveModeChangedListenerPackage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBatterySavingStats() {
        int i;
        PowerManager powerManager = getPowerManager();
        if (powerManager == null) {
            Slog.wtf(TAG, "PowerManager not initialized");
            return;
        }
        boolean isInteractive = powerManager.isInteractive();
        int i2 = 2;
        int i3 = 1;
        if (powerManager.isDeviceIdleMode()) {
            i = 2;
        } else {
            i = powerManager.isLightDeviceIdleMode() ? 1 : 0;
        }
        synchronized (this.mLock) {
            BatterySavingStats batterySavingStats = this.mBatterySavingStats;
            if (getFullEnabledLocked()) {
                i2 = 1;
            } else if (!getAdaptiveEnabledLocked()) {
                i2 = 0;
            }
            int i4 = isInteractive ? 1 : 0;
            if (!this.mIsPluggedIn) {
                i3 = 0;
            }
            batterySavingStats.transitionState(i2, i4, i, i3);
        }
    }

    @GuardedBy({"mLock"})
    private void setFullEnabledLocked(boolean z) {
        if (this.mFullEnabledRaw == z) {
            return;
        }
        PowerManager.invalidatePowerSaveModeCaches();
        this.mFullEnabledRaw = z;
    }

    private boolean getFullEnabledLocked() {
        return this.mFullEnabledRaw;
    }

    @GuardedBy({"mLock"})
    private void setAdaptiveEnabledLocked(boolean z) {
        if (this.mAdaptiveEnabledRaw == z) {
            return;
        }
        PowerManager.invalidatePowerSaveModeCaches();
        this.mAdaptiveEnabledRaw = z;
    }

    private boolean getAdaptiveEnabledLocked() {
        return this.mAdaptiveEnabledRaw;
    }
}
