package com.android.server.vibrator;

import android.R;
import android.app.ActivityManager;
import android.app.UidObserver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManagerInternal;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManagerInternal;
import android.os.PowerSaveState;
import android.os.RemoteException;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.vibrator.VibrationConfig;
import android.provider.Settings;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.companion.virtual.VirtualDeviceManagerInternal;
import com.android.server.utils.PriorityDump;
import com.android.server.vibrator.Vibration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VibrationSettings {
    private static final String TAG = "VibrationSettings";
    private static final int VIBRATE_ON_DISABLED_USAGE_ALLOWED = 66;

    @GuardedBy({"mLock"})
    private AudioManager mAudioManager;

    @GuardedBy({"mLock"})
    private boolean mBatterySaverMode;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private SparseIntArray mCurrentVibrationIntensities;
    private final SparseArray<VibrationEffect> mFallbackEffects;

    @GuardedBy({"mLock"})
    private final List<OnVibratorSettingsChanged> mListeners;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private PowerManagerInternal mPowerManagerInternal;

    @GuardedBy({"mLock"})
    private int mRingerMode;

    @VisibleForTesting
    final SettingsBroadcastReceiver mSettingChangeReceiver;

    @VisibleForTesting
    final SettingsContentObserver mSettingObserver;
    private final String mSystemUiPackage;

    @VisibleForTesting
    final MyUidObserver mUidObserver;

    @GuardedBy({"mLock"})
    private boolean mVibrateInputDevices;

    @GuardedBy({"mLock"})
    private boolean mVibrateOn;
    private final VibrationConfig mVibrationConfig;
    private final IVibrationSettingsWrapper mVibrationSettingsWrapper;
    final VirtualDeviceListener mVirtualDeviceListener;
    private static final Set<Integer> BACKGROUND_PROCESS_USAGE_ALLOWLIST = new HashSet(Arrays.asList(33, 17, 49, 65, 50, 34));
    private static final Set<Integer> BATTERY_SAVER_USAGE_ALLOWLIST = new HashSet(Arrays.asList(33, 17, 65, 34, 50));
    private static final Set<Integer> SYSTEM_VIBRATION_SCREEN_OFF_USAGE_ALLOWLIST = new HashSet(Arrays.asList(18, 66, 34, 50));
    private static final Set<Integer> POWER_MANAGER_SLEEP_REASON_ALLOWLIST = new HashSet(Arrays.asList(9, 2));
    private static final IntentFilter USER_SWITCHED_INTENT_FILTER = new IntentFilter("android.intent.action.USER_SWITCHED");
    private static final IntentFilter INTERNAL_RINGER_MODE_CHANGED_INTENT_FILTER = new IntentFilter("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface OnVibratorSettingsChanged {
        void onChange();
    }

    private int toIntensity(int i, int i2) {
        return (i < 0 || i > 3) ? i2 : i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VibrationSettings(Context context, Handler handler) {
        this(context, handler, new VibrationConfig(context.getResources()));
    }

    @VisibleForTesting
    VibrationSettings(Context context, Handler handler, VibrationConfig vibrationConfig) {
        this.mLock = new Object();
        this.mListeners = new ArrayList();
        this.mCurrentVibrationIntensities = new SparseIntArray();
        VibrationSettingsWrapper vibrationSettingsWrapper = new VibrationSettingsWrapper();
        this.mVibrationSettingsWrapper = vibrationSettingsWrapper;
        this.mContext = context;
        this.mVibrationConfig = vibrationConfig;
        this.mSettingObserver = new SettingsContentObserver(handler);
        this.mUidObserver = new MyUidObserver();
        this.mSettingChangeReceiver = new SettingsBroadcastReceiver();
        this.mVirtualDeviceListener = new VirtualDeviceListener();
        this.mSystemUiPackage = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).getSystemUiServiceComponent().getPackageName();
        VibrationEffect createEffectFromResource = createEffectFromResource(17236165);
        VibrationEffect createEffectFromResource2 = createEffectFromResource(R.array.config_notificationFallbackVibePattern);
        VibrationEffect createEffectFromResource3 = createEffectFromResource(R.array.config_vvmSmsFilterRegexes);
        VibrationEffect createEffectFromResource4 = createEffectFromResource(R.array.config_disableApksUnlessMatchedSku_apk_list);
        SparseArray<VibrationEffect> sparseArray = new SparseArray<>();
        this.mFallbackEffects = sparseArray;
        sparseArray.put(0, createEffectFromResource);
        sparseArray.put(1, createEffectFromResource2);
        sparseArray.put(2, createEffectFromResource4);
        sparseArray.put(5, createEffectFromResource3);
        sparseArray.put(21, VibrationEffect.get(2, false));
        vibrationSettingsWrapper.getExtImpl().init(context);
        update();
    }

    public void onSystemReady() {
        PowerManagerInternal powerManagerInternal = (PowerManagerInternal) LocalServices.getService(PowerManagerInternal.class);
        AudioManager audioManager = (AudioManager) this.mContext.getSystemService(AudioManager.class);
        int ringerModeInternal = audioManager.getRingerModeInternal();
        synchronized (this.mLock) {
            this.mPowerManagerInternal = powerManagerInternal;
            this.mAudioManager = audioManager;
            this.mRingerMode = ringerModeInternal;
        }
        try {
            ActivityManager.getService().registerUidObserver(this.mUidObserver, 3, -1, (String) null);
        } catch (RemoteException unused) {
        }
        powerManagerInternal.registerLowPowerModeObserver(new PowerManagerInternal.LowPowerModeListener() { // from class: com.android.server.vibrator.VibrationSettings.1
            public int getServiceType() {
                return 2;
            }

            public void onLowPowerModeChanged(PowerSaveState powerSaveState) {
                boolean z;
                synchronized (VibrationSettings.this.mLock) {
                    z = powerSaveState.batterySaverEnabled != VibrationSettings.this.mBatterySaverMode;
                    VibrationSettings.this.mBatterySaverMode = powerSaveState.batterySaverEnabled;
                }
                if (z) {
                    VibrationSettings.this.notifyListeners();
                }
            }
        });
        VirtualDeviceManagerInternal virtualDeviceManagerInternal = (VirtualDeviceManagerInternal) LocalServices.getService(VirtualDeviceManagerInternal.class);
        if (virtualDeviceManagerInternal != null) {
            virtualDeviceManagerInternal.registerVirtualDisplayListener(this.mVirtualDeviceListener);
            virtualDeviceManagerInternal.registerAppsOnVirtualDeviceListener(this.mVirtualDeviceListener);
        }
        registerSettingsChangeReceiver(USER_SWITCHED_INTENT_FILTER);
        registerSettingsChangeReceiver(INTERNAL_RINGER_MODE_CHANGED_INTENT_FILTER);
        registerSettingsObserver(Settings.System.getUriFor("vibrate_input_devices"));
        registerSettingsObserver(Settings.System.getUriFor("vibrate_on"));
        registerSettingsObserver(Settings.System.getUriFor("haptic_feedback_enabled"));
        registerSettingsObserver(Settings.System.getUriFor("alarm_vibration_intensity"));
        registerSettingsObserver(Settings.System.getUriFor("haptic_feedback_intensity"));
        registerSettingsObserver(Settings.System.getUriFor("hardware_haptic_feedback_intensity"));
        registerSettingsObserver(Settings.System.getUriFor("media_vibration_intensity"));
        registerSettingsObserver(Settings.System.getUriFor("notification_vibration_intensity"));
        registerSettingsObserver(Settings.System.getUriFor("ring_vibration_intensity"));
        this.mVibrationSettingsWrapper.getExtImpl().onSystemReady();
        update();
    }

    public void addListener(OnVibratorSettingsChanged onVibratorSettingsChanged) {
        synchronized (this.mLock) {
            if (!this.mListeners.contains(onVibratorSettingsChanged)) {
                this.mListeners.add(onVibratorSettingsChanged);
            }
        }
    }

    public void removeListener(OnVibratorSettingsChanged onVibratorSettingsChanged) {
        synchronized (this.mLock) {
            this.mListeners.remove(onVibratorSettingsChanged);
        }
    }

    public int getRampStepDuration() {
        return this.mVibrationConfig.getRampStepDurationMs();
    }

    public int getRampDownDuration() {
        return this.mVibrationConfig.getRampDownDurationMs();
    }

    public int getDefaultIntensity(int i) {
        return this.mVibrationConfig.getDefaultVibrationIntensity(i);
    }

    public int getCurrentIntensity(int i) {
        int i2;
        int defaultIntensity = getDefaultIntensity(i);
        synchronized (this.mLock) {
            i2 = this.mCurrentVibrationIntensities.get(i, defaultIntensity);
        }
        return i2;
    }

    public VibrationEffect getFallbackEffect(int i) {
        return this.mFallbackEffects.get(i);
    }

    public boolean shouldVibrateInputDevices() {
        return this.mVibrateInputDevices;
    }

    public Vibration.Status shouldIgnoreVibration(Vibration.CallerInfo callerInfo) {
        int usage = callerInfo.attrs.getUsage();
        synchronized (this.mLock) {
            if (!this.mUidObserver.isUidForeground(callerInfo.uid) && !BACKGROUND_PROCESS_USAGE_ALLOWLIST.contains(Integer.valueOf(usage))) {
                return Vibration.Status.IGNORED_BACKGROUND;
            }
            if (this.mVirtualDeviceListener.isAppOrDisplayOnAnyVirtualDevice(callerInfo.uid, callerInfo.displayId)) {
                return Vibration.Status.IGNORED_FROM_VIRTUAL_DEVICE;
            }
            if (this.mBatterySaverMode && !BATTERY_SAVER_USAGE_ALLOWLIST.contains(Integer.valueOf(usage))) {
                return Vibration.Status.IGNORED_FOR_POWER;
            }
            if (!callerInfo.attrs.isFlagSet(2)) {
                if (!this.mVibrateOn && 66 != usage) {
                    return Vibration.Status.IGNORED_FOR_SETTINGS;
                }
                if (getCurrentIntensity(usage) == 0 && !this.mVibrationSettingsWrapper.getExtImpl().shouldIgnoreVibration(usage, this.mRingerMode)) {
                    return Vibration.Status.IGNORED_FOR_SETTINGS;
                }
            }
            if (callerInfo.attrs.isFlagSet(1) || shouldVibrateForRingerModeLocked(usage)) {
                return null;
            }
            return Vibration.Status.IGNORED_FOR_RINGER_MODE;
        }
    }

    public boolean shouldCancelVibrationOnScreenOff(Vibration.CallerInfo callerInfo, long j) {
        PowerManagerInternal powerManagerInternal;
        synchronized (this.mLock) {
            powerManagerInternal = this.mPowerManagerInternal;
        }
        if (powerManagerInternal != null) {
            PowerManager.SleepData lastGoToSleep = powerManagerInternal.getLastGoToSleep();
            if (lastGoToSleep.goToSleepUptimeMillis < j || POWER_MANAGER_SLEEP_REASON_ALLOWLIST.contains(Integer.valueOf(lastGoToSleep.goToSleepReason))) {
                Slog.d(TAG, "Ignoring screen off event triggered at uptime " + lastGoToSleep.goToSleepUptimeMillis + " for reason " + PowerManager.sleepReasonToString(lastGoToSleep.goToSleepReason));
                return false;
            }
        }
        if (!SYSTEM_VIBRATION_SCREEN_OFF_USAGE_ALLOWLIST.contains(Integer.valueOf(callerInfo.attrs.getUsage()))) {
            return true;
        }
        int i = callerInfo.uid;
        return (i == 1000 || i == 0 || this.mSystemUiPackage.equals(callerInfo.opPkg)) ? false : true;
    }

    @GuardedBy({"mLock"})
    private boolean shouldVibrateForRingerModeLocked(int i) {
        if (i == 33 || i == 49) {
            return this.mVibrationSettingsWrapper.getExtImpl().shouldVibrateForRingerModeLocked(getCurrentIntensity(i), this.mRingerMode);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void update() {
        updateSettings();
        updateRingerMode();
        notifyListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettings() {
        synchronized (this.mLock) {
            boolean z = true;
            this.mVibrateInputDevices = loadSystemSetting("vibrate_input_devices", 0) > 0;
            if (loadSystemSetting("vibrate_on", 1) <= 0) {
                z = false;
            }
            this.mVibrateOn = z;
            int intensity = toIntensity(loadSystemSetting("alarm_vibration_intensity", -1), getDefaultIntensity(17));
            int defaultIntensity = getDefaultIntensity(18);
            int intensity2 = toIntensity(loadSystemSetting("haptic_feedback_intensity", -1), defaultIntensity);
            int positiveIntensity = toPositiveIntensity(intensity2, defaultIntensity);
            int intensity3 = toIntensity(loadSystemSetting("hardware_haptic_feedback_intensity", -1), positiveIntensity);
            int intensity4 = toIntensity(loadSystemSetting("media_vibration_intensity", -1), getDefaultIntensity(19));
            int defaultIntensity2 = getDefaultIntensity(49);
            int intensity5 = toIntensity(loadSystemSetting("notification_vibration_intensity", -1), defaultIntensity2);
            int positiveIntensity2 = toPositiveIntensity(intensity5, defaultIntensity2);
            int intensity6 = toIntensity(loadSystemSetting("ring_vibration_intensity", -1), getDefaultIntensity(33));
            this.mCurrentVibrationIntensities.clear();
            this.mCurrentVibrationIntensities.put(17, intensity);
            this.mCurrentVibrationIntensities.put(49, intensity5);
            this.mCurrentVibrationIntensities.put(19, intensity4);
            this.mCurrentVibrationIntensities.put(0, intensity4);
            this.mCurrentVibrationIntensities.put(33, intensity6);
            this.mCurrentVibrationIntensities.put(65, positiveIntensity2);
            this.mCurrentVibrationIntensities.put(50, intensity3);
            this.mCurrentVibrationIntensities.put(34, intensity3);
            this.mCurrentVibrationIntensities.put(18, intensity2);
            this.mCurrentVibrationIntensities.put(66, positiveIntensity);
            this.mVibrationSettingsWrapper.getExtImpl().updateSettings();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRingerMode() {
        synchronized (this.mLock) {
            AudioManager audioManager = this.mAudioManager;
            this.mRingerMode = audioManager == null ? 0 : audioManager.getRingerModeInternal();
        }
    }

    public String toString() {
        String str;
        synchronized (this.mLock) {
            StringBuilder sb = new StringBuilder("{");
            for (int i = 0; i < this.mCurrentVibrationIntensities.size(); i++) {
                int keyAt = this.mCurrentVibrationIntensities.keyAt(i);
                int valueAt = this.mCurrentVibrationIntensities.valueAt(i);
                sb.append(VibrationAttributes.usageToString(keyAt));
                sb.append("=(");
                sb.append(intensityToString(valueAt));
                sb.append(",default:");
                sb.append(intensityToString(getDefaultIntensity(keyAt)));
                sb.append("), ");
            }
            sb.append('}');
            str = "VibrationSettings{mVibratorConfig=" + this.mVibrationConfig + ", mVibrateInputDevices=" + this.mVibrateInputDevices + ", mBatterySaverMode=" + this.mBatterySaverMode + ", mVibrateOn=" + this.mVibrateOn + ", mVibrationIntensities=" + ((Object) sb) + ", mProcStatesCache=" + this.mUidObserver.mProcStatesCache + '}';
        }
        return str;
    }

    public void dumpProto(ProtoOutputStream protoOutputStream) {
        synchronized (this.mLock) {
            protoOutputStream.write(1133871366168L, this.mVibrateOn);
            protoOutputStream.write(1133871366150L, this.mBatterySaverMode);
            protoOutputStream.write(1120986464274L, getCurrentIntensity(17));
            protoOutputStream.write(1120986464275L, getDefaultIntensity(17));
            protoOutputStream.write(1120986464278L, getCurrentIntensity(50));
            protoOutputStream.write(1120986464279L, getDefaultIntensity(50));
            protoOutputStream.write(1120986464263L, getCurrentIntensity(18));
            protoOutputStream.write(1120986464264L, getDefaultIntensity(18));
            protoOutputStream.write(1120986464276L, getCurrentIntensity(19));
            protoOutputStream.write(1120986464277L, getDefaultIntensity(19));
            protoOutputStream.write(1120986464265L, getCurrentIntensity(49));
            protoOutputStream.write(1120986464266L, getDefaultIntensity(49));
            protoOutputStream.write(1120986464267L, getCurrentIntensity(33));
            protoOutputStream.write(1120986464268L, getDefaultIntensity(33));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListeners() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mListeners);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((OnVibratorSettingsChanged) it.next()).onChange();
        }
    }

    private static String intensityToString(int i) {
        if (i == 0) {
            return "OFF";
        }
        if (i == 1) {
            return "LOW";
        }
        if (i == 2) {
            return "MEDIUM";
        }
        if (i == 3) {
            return PriorityDump.PRIORITY_ARG_HIGH;
        }
        return "UNKNOWN INTENSITY " + i;
    }

    private int toPositiveIntensity(int i, int i2) {
        return i == 0 ? i2 : toIntensity(i, i2);
    }

    private boolean loadBooleanSetting(String str) {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), str, 0, -2) != 0;
    }

    private int loadSystemSetting(String str, int i) {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), str, i, -2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSettingsObserver(Uri uri) {
        this.mContext.getContentResolver().registerContentObserver(uri, true, this.mSettingObserver, -1);
    }

    private void registerSettingsChangeReceiver(IntentFilter intentFilter) {
        this.mContext.registerReceiver(this.mSettingChangeReceiver, intentFilter, 2);
    }

    private VibrationEffect createEffectFromResource(int i) {
        return createEffectFromTimings(getLongIntArray(this.mContext.getResources(), i));
    }

    private static VibrationEffect createEffectFromTimings(long[] jArr) {
        if (jArr == null || jArr.length == 0) {
            return null;
        }
        if (jArr.length == 1) {
            return VibrationEffect.createOneShot(jArr[0], -1);
        }
        return VibrationEffect.createWaveform(jArr, -1);
    }

    private static long[] getLongIntArray(Resources resources, int i) {
        int[] intArray = resources.getIntArray(i);
        if (intArray == null) {
            return null;
        }
        long[] jArr = new long[intArray.length];
        for (int i2 = 0; i2 < intArray.length; i2++) {
            jArr[i2] = intArray[i2];
        }
        return jArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class SettingsContentObserver extends ContentObserver {
        SettingsContentObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            VibrationSettings.this.updateSettings();
            VibrationSettings.this.notifyListeners();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class SettingsBroadcastReceiver extends BroadcastReceiver {
        SettingsBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.USER_SWITCHED".equals(action)) {
                VibrationSettings.this.update();
            } else if ("android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION".equals(action)) {
                VibrationSettings.this.updateRingerMode();
                VibrationSettings.this.notifyListeners();
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class MyUidObserver extends UidObserver {
        private final SparseArray<Integer> mProcStatesCache = new SparseArray<>();

        MyUidObserver() {
        }

        public boolean isUidForeground(int i) {
            return this.mProcStatesCache.get(i, 6).intValue() <= 6;
        }

        public void onUidGone(int i, boolean z) {
            this.mProcStatesCache.delete(i);
        }

        public void onUidStateChanged(int i, int i2, long j, int i3) {
            this.mProcStatesCache.put(i, Integer.valueOf(i2));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class VirtualDeviceListener implements VirtualDeviceManagerInternal.VirtualDisplayListener, VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener {

        @GuardedBy({"mLock"})
        private final Set<Integer> mVirtualDisplays = new HashSet();

        @GuardedBy({"mLock"})
        private final Set<Integer> mAppsOnVirtualDevice = new HashSet();

        VirtualDeviceListener() {
        }

        public void onVirtualDisplayCreated(int i) {
            synchronized (VibrationSettings.this.mLock) {
                this.mVirtualDisplays.add(Integer.valueOf(i));
            }
        }

        public void onVirtualDisplayRemoved(int i) {
            synchronized (VibrationSettings.this.mLock) {
                this.mVirtualDisplays.remove(Integer.valueOf(i));
            }
        }

        public void onAppsOnAnyVirtualDeviceChanged(Set<Integer> set) {
            synchronized (VibrationSettings.this.mLock) {
                this.mAppsOnVirtualDevice.clear();
                this.mAppsOnVirtualDevice.addAll(set);
            }
        }

        public boolean isAppOrDisplayOnAnyVirtualDevice(int i, int i2) {
            if (i2 == 0) {
                return false;
            }
            synchronized (VibrationSettings.this.mLock) {
                if (i2 == -1) {
                    return this.mAppsOnVirtualDevice.contains(Integer.valueOf(i));
                }
                return this.mVirtualDisplays.contains(Integer.valueOf(i2));
            }
        }
    }

    public IVibrationSettingsWrapper getWrapper() {
        return this.mVibrationSettingsWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class VibrationSettingsWrapper implements IVibrationSettingsWrapper {
        private final IVibrationSettingsExt mVibrationSettingsExt;

        private VibrationSettingsWrapper() {
            this.mVibrationSettingsExt = (IVibrationSettingsExt) ExtLoader.type(IVibrationSettingsExt.class).base(VibrationSettings.this).create();
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public IVibrationSettingsExt getExtImpl() {
            return this.mVibrationSettingsExt;
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public void registerSettingsObserverExt(Uri uri) {
            VibrationSettings.this.registerSettingsObserver(uri);
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public void updateTouchUsageVibrationIntensity(int i) {
            VibrationSettings.this.mCurrentVibrationIntensities.put(18, i);
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public void updateNotificationUsageVibrationIntensity(int i) {
            VibrationSettings.this.mCurrentVibrationIntensities.put(49, i);
        }

        @Override // com.android.server.vibrator.IVibrationSettingsWrapper
        public void updateRingtoneUsageVibrationIntensity(int i) {
            VibrationSettings.this.mCurrentVibrationIntensities.put(33, i);
            VibrationSettings.this.mCurrentVibrationIntensities.put(17, i);
        }
    }
}
