package com.android.server.power.batterysaver;

import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.BatterySaverPolicyConfig;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import com.android.server.EventLogTags;
import com.android.server.timezonedetector.ServiceConfigAccessor;
import java.io.PrintWriter;
import java.time.Duration;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BatterySaverStateMachine {
    private static final int ADAPTIVE_AUTO_DISABLE_BATTERY_LEVEL = 80;
    private static final long ADAPTIVE_CHANGE_TIMEOUT_MS = 86400000;
    private static final String BATTERY_SAVER_NOTIF_CHANNEL_ID = "battery_saver_channel";
    private static final boolean DEBUG = false;
    private static final int DYNAMIC_MODE_NOTIFICATION_ID = 1992;
    private static final String DYNAMIC_MODE_NOTIF_CHANNEL_ID = "dynamic_mode_notification";
    private static final int STATE_AUTOMATIC_ON = 3;
    private static final int STATE_MANUAL_ON = 2;
    private static final int STATE_OFF = 1;
    private static final int STATE_OFF_AUTOMATIC_SNOOZED = 4;
    private static final int STATE_PENDING_STICKY_ON = 5;
    private static final int STICKY_AUTO_DISABLED_NOTIFICATION_ID = 1993;
    private static final long STICKY_DISABLED_NOTIFY_TIMEOUT_MS = Duration.ofHours(12).toMillis();
    private static final String TAG = "BatterySaverStateMachine";

    @GuardedBy({"mLock"})
    private int mBatteryLevel;
    private final BatterySaverController mBatterySaverController;
    private final boolean mBatterySaverStickyBehaviourDisabled;

    @GuardedBy({"mLock"})
    private boolean mBatteryStatusSet;

    @GuardedBy({"mLock"})
    private boolean mBootCompleted;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private final int mDynamicPowerSavingsDefaultDisableThreshold;

    @GuardedBy({"mLock"})
    private int mDynamicPowerSavingsDisableThreshold;

    @GuardedBy({"mLock"})
    private boolean mDynamicPowerSavingsEnableBatterySaver;

    @GuardedBy({"mLock"})
    private boolean mIsBatteryLevelLow;

    @GuardedBy({"mLock"})
    private boolean mIsPowered;

    @GuardedBy({"mLock"})
    private long mLastAdaptiveBatterySaverChangedExternallyElapsed;

    @GuardedBy({"mLock"})
    private int mLastChangedIntReason;

    @GuardedBy({"mLock"})
    private String mLastChangedStrReason;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private int mSettingAutomaticBatterySaver;

    @GuardedBy({"mLock"})
    private boolean mSettingBatterySaverEnabled;

    @GuardedBy({"mLock"})
    private boolean mSettingBatterySaverEnabledSticky;

    @GuardedBy({"mLock"})
    private boolean mSettingBatterySaverStickyAutoDisableEnabled;

    @GuardedBy({"mLock"})
    private int mSettingBatterySaverStickyAutoDisableThreshold;

    @GuardedBy({"mLock"})
    private int mSettingBatterySaverTriggerThreshold;

    @GuardedBy({"mLock"})
    private boolean mSettingsLoaded;
    private final ContentObserver mSettingsObserver = new ContentObserver(null) { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            synchronized (BatterySaverStateMachine.this.mLock) {
                BatterySaverStateMachine.this.refreshSettingsLocked();
            }
        }
    };
    private final Runnable mThresholdChangeLogger = new Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            BatterySaverStateMachine.this.lambda$new$1();
        }
    };
    private BatterySaverStateMachineWrapper mBSSMWrapper = new BatterySaverStateMachineWrapper();
    private IBatterySaverStateMachineExt mBSSMExt = (IBatterySaverStateMachineExt) ExtLoader.type(IBatterySaverStateMachineExt.class).base(this).create();

    @GuardedBy({"mLock"})
    private int mState = 1;

    public BatterySaverStateMachine(Object obj, Context context, BatterySaverController batterySaverController) {
        this.mLock = obj;
        this.mContext = context;
        this.mBatterySaverController = batterySaverController;
        this.mBatterySaverStickyBehaviourDisabled = context.getResources().getBoolean(R.bool.config_built_in_sip_phone);
        this.mDynamicPowerSavingsDefaultDisableThreshold = context.getResources().getInteger(R.integer.config_minimumScreenOffTimeout);
        this.mBSSMExt.init(context);
    }

    private boolean isAutomaticModeActiveLocked() {
        return this.mSettingAutomaticBatterySaver == 0 && this.mSettingBatterySaverTriggerThreshold > 0;
    }

    private boolean isInAutomaticLowZoneLocked() {
        return this.mIsBatteryLevelLow;
    }

    private boolean isDynamicModeActiveLocked() {
        return this.mSettingAutomaticBatterySaver == 1 && this.mDynamicPowerSavingsEnableBatterySaver;
    }

    private boolean isInDynamicLowZoneLocked() {
        return this.mBatteryLevel <= this.mDynamicPowerSavingsDisableThreshold;
    }

    public void onBootCompleted() {
        putGlobalSetting("low_power", 0);
        runOnBgThread(new Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                BatterySaverStateMachine.this.lambda$onBootCompleted$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootCompleted$0() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("low_power"), false, this.mSettingsObserver, 0);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("low_power_sticky"), false, this.mSettingsObserver, 0);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("low_power_trigger_level"), false, this.mSettingsObserver, 0);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("automatic_power_save_mode"), false, this.mSettingsObserver, 0);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("dynamic_power_savings_enabled"), false, this.mSettingsObserver, 0);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("dynamic_power_savings_disable_threshold"), false, this.mSettingsObserver, 0);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("low_power_sticky_auto_disable_enabled"), false, this.mSettingsObserver, 0);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("low_power_sticky_auto_disable_level"), false, this.mSettingsObserver, 0);
        synchronized (this.mLock) {
            if (getGlobalSetting("low_power_sticky", 0) != 0) {
                this.mState = 5;
            }
            this.mBootCompleted = true;
            refreshSettingsLocked();
            doAutoBatterySaverLocked();
            this.mBSSMExt.onBootCompleted(this.mSettingBatterySaverEnabledSticky);
        }
    }

    @VisibleForTesting
    void runOnBgThread(Runnable runnable) {
        BackgroundThread.getHandler().post(runnable);
    }

    @VisibleForTesting
    void runOnBgThreadLazy(Runnable runnable, int i) {
        Handler handler = BackgroundThread.getHandler();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void refreshSettingsLocked() {
        boolean z = getGlobalSetting("low_power", 0) != 0;
        boolean z2 = getGlobalSetting("low_power_sticky", 0) != 0;
        boolean z3 = getGlobalSetting("dynamic_power_savings_enabled", 0) != 0;
        setSettingsLocked(z, z2, getGlobalSetting("low_power_trigger_level", 0), getGlobalSetting("low_power_sticky_auto_disable_enabled", 1) != 0, getGlobalSetting("low_power_sticky_auto_disable_level", 90), getGlobalSetting("automatic_power_save_mode", 0), z3, getGlobalSetting("dynamic_power_savings_disable_threshold", this.mDynamicPowerSavingsDefaultDisableThreshold));
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void setSettingsLocked(boolean z, boolean z2, int i, boolean z3, int i2, int i3, boolean z4, int i4) {
        this.mSettingsLoaded = true;
        int max = Math.max(i2, i);
        boolean z5 = this.mSettingBatterySaverEnabled != z;
        boolean z6 = this.mSettingBatterySaverEnabledSticky != z2;
        boolean z7 = this.mSettingBatterySaverTriggerThreshold != i;
        boolean z8 = this.mSettingBatterySaverStickyAutoDisableEnabled != z3;
        boolean z9 = this.mSettingBatterySaverStickyAutoDisableThreshold != max;
        boolean z10 = this.mSettingAutomaticBatterySaver != i3;
        boolean z11 = this.mDynamicPowerSavingsDisableThreshold != i4;
        boolean z12 = this.mDynamicPowerSavingsEnableBatterySaver != z4;
        if (z5 || z6 || z7 || z10 || z8 || z9 || z11 || z12) {
            this.mSettingBatterySaverEnabled = z;
            this.mSettingBatterySaverEnabledSticky = z2;
            this.mSettingBatterySaverTriggerThreshold = i;
            this.mSettingBatterySaverStickyAutoDisableEnabled = z3;
            this.mSettingBatterySaverStickyAutoDisableThreshold = max;
            this.mSettingAutomaticBatterySaver = i3;
            this.mDynamicPowerSavingsDisableThreshold = i4;
            this.mDynamicPowerSavingsEnableBatterySaver = z4;
            if (z7) {
                runOnBgThreadLazy(this.mThresholdChangeLogger, 2000);
            }
            if (!this.mSettingBatterySaverStickyAutoDisableEnabled) {
                hideStickyDisabledNotification();
            }
            if (z5) {
                enableBatterySaverLocked(z, true, 8, z ? "Global.low_power changed to 1" : "Global.low_power changed to 0");
            } else {
                doAutoBatterySaverLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        EventLogTags.writeBatterySaverSetting(this.mSettingBatterySaverTriggerThreshold);
    }

    public void setBatteryStatus(boolean z, int i, boolean z2) {
        synchronized (this.mLock) {
            boolean z3 = true;
            this.mBatteryStatusSet = true;
            boolean z4 = this.mIsPowered != z;
            boolean z5 = this.mBatteryLevel != i;
            if (this.mIsBatteryLevelLow == z2) {
                z3 = false;
            }
            if (z4 || z5 || z3) {
                this.mIsPowered = z;
                this.mBatteryLevel = i;
                this.mIsBatteryLevelLow = z2;
                doAutoBatterySaverLocked();
            }
        }
    }

    public BatterySaverPolicyConfig getFullBatterySaverPolicy() {
        BatterySaverPolicyConfig policyLocked;
        synchronized (this.mLock) {
            policyLocked = this.mBatterySaverController.getPolicyLocked(2);
        }
        return policyLocked;
    }

    public boolean setFullBatterySaverPolicy(BatterySaverPolicyConfig batterySaverPolicyConfig) {
        boolean fullPolicyLocked;
        synchronized (this.mLock) {
            fullPolicyLocked = this.mBatterySaverController.setFullPolicyLocked(batterySaverPolicyConfig, 13);
        }
        return fullPolicyLocked;
    }

    public boolean setAdaptiveBatterySaverEnabled(boolean z) {
        boolean adaptivePolicyEnabledLocked;
        synchronized (this.mLock) {
            this.mLastAdaptiveBatterySaverChangedExternallyElapsed = SystemClock.elapsedRealtime();
            adaptivePolicyEnabledLocked = this.mBatterySaverController.setAdaptivePolicyEnabledLocked(z, 11);
        }
        return adaptivePolicyEnabledLocked;
    }

    public boolean setAdaptiveBatterySaverPolicy(BatterySaverPolicyConfig batterySaverPolicyConfig) {
        boolean adaptivePolicyLocked;
        synchronized (this.mLock) {
            this.mLastAdaptiveBatterySaverChangedExternallyElapsed = SystemClock.elapsedRealtime();
            adaptivePolicyLocked = this.mBatterySaverController.setAdaptivePolicyLocked(batterySaverPolicyConfig, 11);
        }
        return adaptivePolicyLocked;
    }

    @GuardedBy({"mLock"})
    private void doAutoBatterySaverLocked() {
        if (this.mBSSMExt.isOplusFeatureDisalbed() && this.mBootCompleted && this.mSettingsLoaded && this.mBatteryStatusSet) {
            updateStateLocked(false, false);
            if (SystemClock.elapsedRealtime() - this.mLastAdaptiveBatterySaverChangedExternallyElapsed > 86400000) {
                this.mBatterySaverController.setAdaptivePolicyEnabledLocked(false, 12);
                this.mBatterySaverController.resetAdaptivePolicyLocked(12);
            } else {
                if (!this.mIsPowered || this.mBatteryLevel < 80) {
                    return;
                }
                this.mBatterySaverController.setAdaptivePolicyEnabledLocked(false, 7);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void updateStateLocked(boolean z, boolean z2) {
        if (z || (this.mBootCompleted && this.mSettingsLoaded && this.mBatteryStatusSet)) {
            int i = this.mState;
            if (i == 1) {
                if (this.mIsPowered) {
                    return;
                }
                if (z) {
                    if (!z2) {
                        Slog.e(TAG, "Tried to disable BS when it's already OFF");
                        return;
                    }
                    enableBatterySaverLocked(true, true, 2);
                    hideStickyDisabledNotification();
                    this.mState = 2;
                    return;
                }
                if (isAutomaticModeActiveLocked() && isInAutomaticLowZoneLocked()) {
                    enableBatterySaverLocked(true, false, 0);
                    hideStickyDisabledNotification();
                    this.mState = 3;
                    return;
                } else {
                    if (isDynamicModeActiveLocked() && isInDynamicLowZoneLocked()) {
                        enableBatterySaverLocked(true, false, 9);
                        hideStickyDisabledNotification();
                        this.mState = 3;
                        return;
                    }
                    return;
                }
            }
            if (i == 2) {
                if (z) {
                    if (z2) {
                        Slog.e(TAG, "Tried to enable BS when it's already MANUAL_ON");
                        return;
                    } else {
                        enableBatterySaverLocked(false, true, 3);
                        this.mState = 1;
                        return;
                    }
                }
                if (this.mIsPowered) {
                    enableBatterySaverLocked(false, false, 7);
                    if (this.mSettingBatterySaverEnabledSticky && !this.mBatterySaverStickyBehaviourDisabled) {
                        this.mState = 5;
                        return;
                    } else {
                        this.mState = 1;
                        return;
                    }
                }
                return;
            }
            if (i == 3) {
                if (this.mIsPowered) {
                    enableBatterySaverLocked(false, false, 7);
                    this.mState = 1;
                    return;
                }
                if (z) {
                    if (z2) {
                        Slog.e(TAG, "Tried to enable BS when it's already AUTO_ON");
                        return;
                    } else {
                        enableBatterySaverLocked(false, true, 3);
                        this.mState = 4;
                        return;
                    }
                }
                if (isAutomaticModeActiveLocked() && !isInAutomaticLowZoneLocked()) {
                    enableBatterySaverLocked(false, false, 1);
                    this.mState = 1;
                    return;
                } else if (isDynamicModeActiveLocked() && !isInDynamicLowZoneLocked()) {
                    enableBatterySaverLocked(false, false, 10);
                    this.mState = 1;
                    return;
                } else {
                    if (isAutomaticModeActiveLocked() || isDynamicModeActiveLocked()) {
                        return;
                    }
                    enableBatterySaverLocked(false, false, 8);
                    this.mState = 1;
                    return;
                }
            }
            if (i == 4) {
                if (z) {
                    if (!z2) {
                        Slog.e(TAG, "Tried to disable BS when it's already AUTO_SNOOZED");
                        return;
                    } else {
                        enableBatterySaverLocked(true, true, 2);
                        this.mState = 2;
                        return;
                    }
                }
                if (this.mIsPowered || ((isAutomaticModeActiveLocked() && !isInAutomaticLowZoneLocked()) || ((isDynamicModeActiveLocked() && !isInDynamicLowZoneLocked()) || !(isAutomaticModeActiveLocked() || isDynamicModeActiveLocked())))) {
                    this.mState = 1;
                    return;
                }
                return;
            }
            if (i != 5) {
                Slog.wtf(TAG, "Unknown state: " + this.mState);
                return;
            }
            if (z) {
                Slog.e(TAG, "Tried to manually change BS state from PENDING_STICKY_ON");
                return;
            }
            boolean z3 = this.mSettingBatterySaverStickyAutoDisableEnabled && this.mBatteryLevel >= this.mSettingBatterySaverStickyAutoDisableThreshold;
            if ((this.mBatterySaverStickyBehaviourDisabled || !this.mSettingBatterySaverEnabledSticky) || z3) {
                this.mState = 1;
                setStickyActive(false);
                triggerStickyDisabledNotification();
            } else {
                if (this.mIsPowered) {
                    return;
                }
                enableBatterySaverLocked(true, true, 4);
                this.mState = 2;
            }
        }
    }

    @VisibleForTesting
    int getState() {
        int i;
        synchronized (this.mLock) {
            i = this.mState;
        }
        return i;
    }

    public void setBatterySaverEnabledManually(boolean z) {
        synchronized (this.mLock) {
            if (!this.mBSSMExt.isOplusFeatureDisalbed()) {
                Slog.d(TAG, "setBatterySaverEnabledManually: onSetBatterySaverEnabledManually. enabled=" + z);
                this.mBSSMExt.onSetBatterySaverEnabledManually(z);
            } else {
                Slog.d(TAG, "setBatterySaverEnabledManually: updateStateLocked. enabled=" + z);
                updateStateLocked(true, z);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void enableBatterySaverLocked(boolean z, boolean z2, int i) {
        enableBatterySaverLocked(z, z2, i, BatterySaverController.reasonToString(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void enableBatterySaverLocked(boolean z, boolean z2, int i, String str) {
        if (this.mBatterySaverController.isFullEnabled() == z) {
            StringBuilder sb = new StringBuilder();
            sb.append("Already ");
            sb.append(z ? ServiceConfigAccessor.PROVIDER_MODE_ENABLED : ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
            Slog.d(TAG, sb.toString());
            return;
        }
        if (z && this.mIsPowered && this.mBSSMExt.isOplusFeatureDisalbed()) {
            return;
        }
        this.mLastChangedIntReason = i;
        this.mLastChangedStrReason = str;
        this.mSettingBatterySaverEnabled = z;
        putGlobalSetting("low_power", z ? 1 : 0);
        if (z2) {
            setStickyActive(!this.mBatterySaverStickyBehaviourDisabled && z);
        }
        this.mBatterySaverController.enableBatterySaver(z, i);
        Slog.d(TAG, "enableBatterySaver: Enabled=" + z + " manual=" + z2 + " reason=" + str + "(" + i + ")");
        if (i == 9 || i == 0) {
            triggerDynamicModeNotification();
        } else {
            if (z) {
                return;
            }
            hideDynamicModeNotification();
        }
    }

    @VisibleForTesting
    void triggerDynamicModeNotification() {
        runOnBgThread(new Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BatterySaverStateMachine.this.lambda$triggerDynamicModeNotification$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerDynamicModeNotification$2() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        ensureNotificationChannelExists(notificationManager, DYNAMIC_MODE_NOTIF_CHANNEL_ID, R.string.font_family_headline_material);
        notificationManager.notifyAsUser(TAG, DYNAMIC_MODE_NOTIFICATION_ID, buildNotification(DYNAMIC_MODE_NOTIF_CHANNEL_ID, R.string.font_family_subhead_material, R.string.font_family_menu_material, "android.settings.BATTERY_SAVER_SETTINGS", 0L), UserHandle.ALL);
    }

    @VisibleForTesting
    void triggerStickyDisabledNotification() {
        runOnBgThread(new Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BatterySaverStateMachine.this.lambda$triggerStickyDisabledNotification$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$triggerStickyDisabledNotification$3() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        ensureNotificationChannelExists(notificationManager, BATTERY_SAVER_NOTIF_CHANNEL_ID, R.string.bugreport_screenshot_failure_toast);
        notificationManager.notifyAsUser(TAG, STICKY_AUTO_DISABLED_NOTIFICATION_ID, buildNotification(BATTERY_SAVER_NOTIF_CHANNEL_ID, R.string.bugreport_title, R.string.bugreport_option_full_summary, "android.settings.BATTERY_SAVER_SETTINGS", STICKY_DISABLED_NOTIFY_TIMEOUT_MS), UserHandle.ALL);
    }

    private void ensureNotificationChannelExists(NotificationManager notificationManager, String str, int i) {
        NotificationChannel notificationChannel = new NotificationChannel(str, this.mContext.getText(i), 3);
        notificationChannel.setSound(null, null);
        notificationChannel.setBlockable(true);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private Notification buildNotification(String str, int i, int i2, String str2, long j) {
        Resources resources = this.mContext.getResources();
        Intent intent = new Intent(str2);
        intent.setFlags(268468224);
        PendingIntent activity = PendingIntent.getActivity(this.mContext, 0, intent, 201326592);
        String string = resources.getString(i);
        String string2 = resources.getString(i2);
        return new Notification.Builder(this.mContext, str).setSmallIcon(R.drawable.ic_cab_done_holo).setContentTitle(string).setContentText(string2).setContentIntent(activity).setStyle(new Notification.BigTextStyle().bigText(string2)).setOnlyAlertOnce(true).setAutoCancel(true).setTimeoutAfter(j).build();
    }

    private void hideDynamicModeNotification() {
        hideNotification(DYNAMIC_MODE_NOTIFICATION_ID);
    }

    private void hideStickyDisabledNotification() {
        hideNotification(STICKY_AUTO_DISABLED_NOTIFICATION_ID);
    }

    private void hideNotification(final int i) {
        runOnBgThread(new Runnable() { // from class: com.android.server.power.batterysaver.BatterySaverStateMachine$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                BatterySaverStateMachine.this.lambda$hideNotification$4(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideNotification$4(int i) {
        ((NotificationManager) this.mContext.getSystemService(NotificationManager.class)).cancelAsUser(TAG, i, UserHandle.ALL);
    }

    private void setStickyActive(boolean z) {
        this.mSettingBatterySaverEnabledSticky = z;
        putGlobalSetting("low_power_sticky", z ? 1 : 0);
    }

    @VisibleForTesting
    protected void putGlobalSetting(String str, int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), str, i);
    }

    @VisibleForTesting
    protected int getGlobalSetting(String str, int i) {
        return Settings.Global.getInt(this.mContext.getContentResolver(), str, i);
    }

    public void dump(PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println();
        indentingPrintWriter.println("Battery saver state machine:");
        indentingPrintWriter.increaseIndent();
        synchronized (this.mLock) {
            indentingPrintWriter.print("Enabled=");
            indentingPrintWriter.println(this.mBatterySaverController.isEnabled());
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print("full=");
            indentingPrintWriter.println(this.mBatterySaverController.isFullEnabled());
            indentingPrintWriter.print("adaptive=");
            indentingPrintWriter.print(this.mBatterySaverController.isAdaptiveEnabled());
            if (this.mBatterySaverController.isAdaptiveEnabled()) {
                indentingPrintWriter.print(" (advertise=");
                indentingPrintWriter.print(this.mBatterySaverController.getBatterySaverPolicy().shouldAdvertiseIsEnabled());
                indentingPrintWriter.print(")");
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.print("mState=");
            indentingPrintWriter.println(this.mState);
            indentingPrintWriter.print("mLastChangedIntReason=");
            indentingPrintWriter.println(this.mLastChangedIntReason);
            indentingPrintWriter.print("mLastChangedStrReason=");
            indentingPrintWriter.println(this.mLastChangedStrReason);
            indentingPrintWriter.print("mBootCompleted=");
            indentingPrintWriter.println(this.mBootCompleted);
            indentingPrintWriter.print("mSettingsLoaded=");
            indentingPrintWriter.println(this.mSettingsLoaded);
            indentingPrintWriter.print("mBatteryStatusSet=");
            indentingPrintWriter.println(this.mBatteryStatusSet);
            indentingPrintWriter.print("mIsPowered=");
            indentingPrintWriter.println(this.mIsPowered);
            indentingPrintWriter.print("mBatteryLevel=");
            indentingPrintWriter.println(this.mBatteryLevel);
            indentingPrintWriter.print("mIsBatteryLevelLow=");
            indentingPrintWriter.println(this.mIsBatteryLevelLow);
            indentingPrintWriter.print("mSettingAutomaticBatterySaver=");
            indentingPrintWriter.println(this.mSettingAutomaticBatterySaver);
            indentingPrintWriter.print("mSettingBatterySaverEnabled=");
            indentingPrintWriter.println(this.mSettingBatterySaverEnabled);
            indentingPrintWriter.print("mSettingBatterySaverEnabledSticky=");
            indentingPrintWriter.println(this.mSettingBatterySaverEnabledSticky);
            indentingPrintWriter.print("mSettingBatterySaverStickyAutoDisableEnabled=");
            indentingPrintWriter.println(this.mSettingBatterySaverStickyAutoDisableEnabled);
            indentingPrintWriter.print("mSettingBatterySaverStickyAutoDisableThreshold=");
            indentingPrintWriter.println(this.mSettingBatterySaverStickyAutoDisableThreshold);
            indentingPrintWriter.print("mSettingBatterySaverTriggerThreshold=");
            indentingPrintWriter.println(this.mSettingBatterySaverTriggerThreshold);
            indentingPrintWriter.print("mBatterySaverStickyBehaviourDisabled=");
            indentingPrintWriter.println(this.mBatterySaverStickyBehaviourDisabled);
            indentingPrintWriter.print("mDynamicPowerSavingsDefaultDisableThreshold=");
            indentingPrintWriter.println(this.mDynamicPowerSavingsDefaultDisableThreshold);
            indentingPrintWriter.print("mDynamicPowerSavingsDisableThreshold=");
            indentingPrintWriter.println(this.mDynamicPowerSavingsDisableThreshold);
            indentingPrintWriter.print("mDynamicPowerSavingsEnableBatterySaver=");
            indentingPrintWriter.println(this.mDynamicPowerSavingsEnableBatterySaver);
            indentingPrintWriter.print("mLastAdaptiveBatterySaverChangedExternallyElapsed=");
            indentingPrintWriter.println(this.mLastAdaptiveBatterySaverChangedExternallyElapsed);
        }
        indentingPrintWriter.decreaseIndent();
    }

    public void dumpProto(ProtoOutputStream protoOutputStream, long j) {
        synchronized (this.mLock) {
            long start = protoOutputStream.start(j);
            protoOutputStream.write(1133871366145L, this.mBatterySaverController.isEnabled());
            protoOutputStream.write(1159641169938L, this.mState);
            protoOutputStream.write(1133871366158L, this.mBatterySaverController.isFullEnabled());
            protoOutputStream.write(1133871366159L, this.mBatterySaverController.isAdaptiveEnabled());
            protoOutputStream.write(1133871366160L, this.mBatterySaverController.getBatterySaverPolicy().shouldAdvertiseIsEnabled());
            protoOutputStream.write(1133871366146L, this.mBootCompleted);
            protoOutputStream.write(1133871366147L, this.mSettingsLoaded);
            protoOutputStream.write(1133871366148L, this.mBatteryStatusSet);
            protoOutputStream.write(1133871366150L, this.mIsPowered);
            protoOutputStream.write(1120986464263L, this.mBatteryLevel);
            protoOutputStream.write(1133871366152L, this.mIsBatteryLevelLow);
            protoOutputStream.write(1159641169939L, this.mSettingAutomaticBatterySaver);
            protoOutputStream.write(1133871366153L, this.mSettingBatterySaverEnabled);
            protoOutputStream.write(1133871366154L, this.mSettingBatterySaverEnabledSticky);
            protoOutputStream.write(1120986464267L, this.mSettingBatterySaverTriggerThreshold);
            protoOutputStream.write(1133871366156L, this.mSettingBatterySaverStickyAutoDisableEnabled);
            protoOutputStream.write(1120986464269L, this.mSettingBatterySaverStickyAutoDisableThreshold);
            protoOutputStream.write(1120986464276L, this.mDynamicPowerSavingsDefaultDisableThreshold);
            protoOutputStream.write(1120986464277L, this.mDynamicPowerSavingsDisableThreshold);
            protoOutputStream.write(1133871366166L, this.mDynamicPowerSavingsEnableBatterySaver);
            protoOutputStream.write(1112396529681L, this.mLastAdaptiveBatterySaverChangedExternallyElapsed);
            protoOutputStream.end(start);
        }
    }

    public IBatterySaverStateMachineWrapper getWrapper() {
        return this.mBSSMWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class BatterySaverStateMachineWrapper implements IBatterySaverStateMachineWrapper {
        private BatterySaverStateMachineWrapper() {
        }

        @Override // com.android.server.power.batterysaver.IBatterySaverStateMachineWrapper
        public void enableBatterySaverLocked(boolean z, boolean z2, int i, String str) {
            BatterySaverStateMachine.this.enableBatterySaverLocked(z, z2, i, str);
        }
    }
}
