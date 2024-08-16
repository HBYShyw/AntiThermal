package com.android.server.hdmi;

import android.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.hdmi.HdmiControlManager;
import android.os.Environment;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ConcurrentUtils;
import com.android.server.pm.PackageManagerService;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HdmiCecConfig {
    private static final String CONFIG_FILE = "cec_config.xml";
    private static final String ETC_DIR = "etc";
    private static final String SHARED_PREFS_DIR = "shared_prefs";
    private static final String SHARED_PREFS_NAME = "cec_config.xml";
    private static final int STORAGE_GLOBAL_SETTINGS = 1;
    private static final int STORAGE_SHARED_PREFS = 2;
    private static final int STORAGE_SYSPROPS = 0;
    private static final String TAG = "HdmiCecConfig";
    private static final String VALUE_TYPE_INT = "int";
    private static final String VALUE_TYPE_STRING = "string";
    private final Context mContext;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private final ArrayMap<Setting, ArrayMap<SettingChangeListener, Executor>> mSettingChangeListeners;
    private LinkedHashMap<String, Setting> mSettings;
    private final StorageAdapter mStorageAdapter;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface SettingChangeListener {
        void onChange(@HdmiControlManager.SettingName String str);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface Storage {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private @interface ValueType {
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class VerificationException extends RuntimeException {
        public VerificationException(String str) {
            super(str);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class StorageAdapter {
        private final Context mContext;
        private final SharedPreferences mSharedPrefs;

        StorageAdapter(Context context) {
            this.mContext = context;
            this.mSharedPrefs = context.createDeviceProtectedStorageContext().getSharedPreferences(new File(new File(Environment.getDataSystemDirectory(), HdmiCecConfig.SHARED_PREFS_DIR), "cec_config.xml"), 0);
        }

        public String retrieveSystemProperty(String str, String str2) {
            return SystemProperties.get(str, str2);
        }

        public void storeSystemProperty(String str, String str2) {
            SystemProperties.set(str, str2);
        }

        public String retrieveGlobalSetting(String str, String str2) {
            String string = Settings.Global.getString(this.mContext.getContentResolver(), str);
            return string != null ? string : str2;
        }

        public void storeGlobalSetting(String str, String str2) {
            Settings.Global.putString(this.mContext.getContentResolver(), str, str2);
        }

        public String retrieveSharedPref(String str, String str2) {
            return this.mSharedPrefs.getString(str, str2);
        }

        public void storeSharedPref(String str, String str2) {
            this.mSharedPrefs.edit().putString(str, str2).apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class Value {
        private final Integer mIntValue;
        private final String mStringValue;

        Value(String str) {
            this.mStringValue = str;
            this.mIntValue = null;
        }

        Value(Integer num) {
            this.mStringValue = null;
            this.mIntValue = num;
        }

        String getStringValue() {
            return this.mStringValue;
        }

        Integer getIntValue() {
            return this.mIntValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class Setting {
        private final Context mContext;

        @HdmiControlManager.SettingName
        private final String mName;
        private final boolean mUserConfigurable;
        private Value mDefaultValue = null;
        private List<Value> mAllowedValues = new ArrayList();

        Setting(Context context, @HdmiControlManager.SettingName String str, int i) {
            this.mContext = context;
            this.mName = str;
            this.mUserConfigurable = context.getResources().getBoolean(i);
        }

        @HdmiControlManager.SettingName
        public String getName() {
            return this.mName;
        }

        @ValueType
        public String getValueType() {
            return getDefaultValue().getStringValue() != null ? HdmiCecConfig.VALUE_TYPE_STRING : HdmiCecConfig.VALUE_TYPE_INT;
        }

        public Value getDefaultValue() {
            Value value = this.mDefaultValue;
            if (value != null) {
                return value;
            }
            throw new VerificationException("Invalid CEC setup for '" + getName() + "' setting. Setting has no default value.");
        }

        public boolean getUserConfigurable() {
            return this.mUserConfigurable;
        }

        private void registerValue(Value value, int i, int i2) {
            if (this.mContext.getResources().getBoolean(i)) {
                this.mAllowedValues.add(value);
                if (this.mContext.getResources().getBoolean(i2)) {
                    if (this.mDefaultValue != null) {
                        Slog.e(HdmiCecConfig.TAG, "Failed to set '" + value + "' as a default for '" + getName() + "': Setting already has a default ('" + this.mDefaultValue + "').");
                        return;
                    }
                    this.mDefaultValue = value;
                }
            }
        }

        public void registerValue(String str, int i, int i2) {
            registerValue(new Value(str), i, i2);
        }

        public void registerValue(int i, int i2, int i3) {
            registerValue(new Value(Integer.valueOf(i)), i2, i3);
        }

        public List<Value> getAllowedValues() {
            return this.mAllowedValues;
        }
    }

    @VisibleForTesting
    HdmiCecConfig(Context context, StorageAdapter storageAdapter) {
        this.mLock = new Object();
        this.mSettingChangeListeners = new ArrayMap<>();
        this.mSettings = new LinkedHashMap<>();
        this.mContext = context;
        this.mStorageAdapter = storageAdapter;
        Setting registerSetting = registerSetting("hdmi_cec_enabled", R.bool.config_dozeAlwaysOnEnabled);
        registerSetting.registerValue(1, R.bool.config_dozeAfterScreenOffByDefault, R.bool.config_dozeAlwaysOnDisplayAvailable);
        registerSetting.registerValue(0, R.bool.config_dockedStackDividerFreeSnapMode, R.bool.config_dontPreferApn);
        Setting registerSetting2 = registerSetting("hdmi_cec_version", R.bool.config_dreamsActivatedOnSleepByDefault);
        registerSetting2.registerValue(5, R.bool.config_dozePulsePickup, R.bool.config_dozeSupportsAodWallpaper);
        registerSetting2.registerValue(6, R.bool.config_dozeWakeLockScreenSensorAvailable, R.bool.config_dreamsActivatedOnDockByDefault);
        Setting registerSetting3 = registerSetting("routing_control", R.bool.config_swipe_up_gesture_setting_available);
        registerSetting3.registerValue(1, R.bool.config_sustainedPerformanceModeSupported, R.bool.config_swipeDisambiguation);
        registerSetting3.registerValue(0, R.bool.config_supportsSystemDecorsOnSecondaryDisplays, R.bool.config_suspendWhenScreenOffDueToProximity);
        Setting registerSetting4 = registerSetting("soundbar_mode", R.bool.config_useAssistantVolume);
        registerSetting4.registerValue(1, R.bool.config_usbChargingMessage, R.bool.config_use16BitTaskSnapshotPixelFormat);
        registerSetting4.registerValue(0, R.bool.config_ui_enableFadingMarquee, R.bool.config_unplugTurnsOnScreen);
        Setting registerSetting5 = registerSetting("power_control_mode", R.bool.config_enableBurnInProtection);
        registerSetting5.registerValue("to_tv", R.bool.config_enableAppWidgetService, R.bool.config_enableAutoPowerModes);
        registerSetting5.registerValue("broadcast", R.bool.config_dreamsEnabledByDefault, R.bool.config_dreamsEnabledOnBattery);
        registerSetting5.registerValue("none", R.bool.config_dreamsSupported, R.bool.config_duplicate_port_omadm_wappush);
        registerSetting5.registerValue("to_tv_and_audio_system", R.bool.config_eap_sim_based_auth_supported, R.bool.config_enableActivityRecognitionHardwareOverlay);
        Setting registerSetting6 = registerSetting("power_state_change_on_active_source_lost", R.bool.config_enableGeofenceOverlay);
        registerSetting6.registerValue("none", R.bool.config_enableCarDockHomeLaunch, R.bool.config_enableCredentialFactoryResetProtection);
        registerSetting6.registerValue("standby_now", R.bool.config_enableFusedLocationOverlay, R.bool.config_enableGeocoderOverlay);
        Setting registerSetting7 = registerSetting("system_audio_control", R.bool.config_useRoundIcon);
        registerSetting7.registerValue(1, R.bool.config_useDevInputEventForAudioJack, R.bool.config_useFixedVolume);
        registerSetting7.registerValue(0, R.bool.config_useAttentionLight, R.bool.config_useDefaultFocusHighlight);
        Setting registerSetting8 = registerSetting("system_audio_mode_muting", R.bool.config_useWebViewPacProcessor);
        registerSetting8.registerValue(1, R.bool.config_useVideoPauseWorkaround, R.bool.config_useVolumeKeySounds);
        registerSetting8.registerValue(0, R.bool.config_useSmsAppService, R.bool.config_useSystemProvidedLauncherForSecondary);
        Setting registerSetting9 = registerSetting("volume_control_enabled", R.bool.config_windowEnableCircularEmulatorDisplayOverlay);
        registerSetting9.registerValue(1, R.bool.config_wimaxEnabled, R.bool.config_windowActionBarSupported);
        registerSetting9.registerValue(0, R.bool.config_wakeOnDpadKeyPress, R.bool.config_wifiDisplaySupportsProtectedBuffers);
        Setting registerSetting10 = registerSetting("tv_wake_on_one_touch_play", R.bool.config_wakeOnBackKeyPress);
        registerSetting10.registerValue(1, R.bool.config_volumeHushGestureEnabled, R.bool.config_wakeOnAssistKeyPress);
        registerSetting10.registerValue(0, R.bool.config_user_notification_of_restrictied_mobile_access, R.bool.config_voice_capable);
        Setting registerSetting11 = registerSetting("tv_send_standby_on_sleep", R.bool.config_use_voip_mode_for_ims);
        registerSetting11.registerValue(1, R.bool.config_use_strict_phone_number_comparation_for_kazakhstan, R.bool.config_use_strict_phone_number_comparation_for_russia);
        registerSetting11.registerValue(0, R.bool.config_use_sim_language_file, R.bool.config_use_strict_phone_number_comparation);
        Setting registerSetting12 = registerSetting("set_menu_language", R.bool.config_tintNotificationActionButtons);
        registerSetting12.registerValue(1, R.bool.config_tether_upstream_automatic, R.bool.config_timeZoneRulesUpdateTrackingEnabled);
        registerSetting12.registerValue(0, R.bool.config_switch_phone_on_voice_reg_state_change, R.bool.config_syncstorageengine_masterSyncAutomatically);
        Setting registerSetting13 = registerSetting("rc_profile_tv", R.bool.config_supportsSplitScreenMultiWindow);
        registerSetting13.registerValue(0, R.bool.config_supportMicNearUltrasound, R.bool.config_supportPreRebootSecurityLogs);
        registerSetting13.registerValue(2, R.bool.config_supportSpeakerNearUltrasound, R.bool.config_supportSystemNavigationKeys);
        registerSetting13.registerValue(6, R.bool.config_supportsMultiWindow, R.bool.config_supportsRoundedCornersOnWindows);
        registerSetting13.registerValue(10, R.bool.config_supportsInsecureLockScreen, R.bool.config_supportsMultiDisplay);
        registerSetting13.registerValue(14, R.bool.config_supportDoubleTapWake, R.bool.config_supportLongPressPowerWhenNonInteractive);
        Setting registerSetting14 = registerSetting("rc_profile_source_handles_root_menu", R.bool.config_smart_battery_available);
        registerSetting14.registerValue(1, R.bool.config_single_volume, R.bool.config_sip_wifi_only);
        registerSetting14.registerValue(0, R.bool.config_skipScreenOnBrightnessRamp, R.bool.config_skipSensorAvailable);
        Setting registerSetting15 = registerSetting("rc_profile_source_handles_setup_menu", R.bool.config_speed_up_audio_on_mt_calls);
        registerSetting15.registerValue(1, R.bool.config_sms_capable, R.bool.config_sms_decode_gsm_8bit_data);
        registerSetting15.registerValue(0, R.bool.config_sms_force_7bit_encoding, R.bool.config_sms_utf8_support);
        Setting registerSetting16 = registerSetting("rc_profile_source_handles_contents_menu", R.bool.config_showGesturalNavigationHints);
        registerSetting16.registerValue(1, R.bool.config_sf_limitedAlpha, R.bool.config_sf_slowBlur);
        registerSetting16.registerValue(0, R.bool.config_showAreaUpdateInfoSettings, R.bool.config_showBuiltinWirelessChargingAnim);
        Setting registerSetting17 = registerSetting("rc_profile_source_handles_top_menu", R.bool.config_supportBluetoothPersistedState);
        registerSetting17.registerValue(1, R.bool.config_stkNoAlphaUsrCnf, R.bool.config_strongAuthRequiredOnBoot);
        registerSetting17.registerValue(0, R.bool.config_supportAudioSourceUnprocessed, R.bool.config_supportAutoRotation);
        Setting registerSetting18 = registerSetting("rc_profile_source_handles_media_context_sensitive_menu", R.bool.config_silenceSensorAvailable);
        registerSetting18.registerValue(1, R.bool.config_showMenuShortcutsWhenKeyboardPresent, R.bool.config_showNavigationBar);
        registerSetting18.registerValue(0, R.bool.config_showSysuiShutdown, R.bool.config_showUserSwitcherByDefault);
        Setting registerSetting19 = registerSetting("query_sad_lpcm", R.bool.config_lockDayNightMode);
        registerSetting19.registerValue(1, R.bool.config_lidControlsSleep, R.bool.config_localDisplaysMirrorContent);
        registerSetting19.registerValue(0, R.bool.config_lidControlsDisplayFold, R.bool.config_lidControlsScreenLock);
        Setting registerSetting20 = registerSetting("query_sad_dd", R.bool.config_enable_puk_unlock_screen);
        registerSetting20.registerValue(1, R.bool.config_enableWifiDisplay, R.bool.config_enable_emergency_call_while_sim_locked);
        registerSetting20.registerValue(0, R.bool.config_enableWallpaperService, R.bool.config_enableWcgMode);
        Setting registerSetting21 = registerSetting("query_sad_mpeg1", R.bool.config_notificationHeaderClickableForExpand);
        registerSetting21.registerValue(1, R.bool.config_noHomeScreen, R.bool.config_notificationBadging);
        registerSetting21.registerValue(0, R.bool.config_networkSamplingWakesDevice, R.bool.config_nightDisplayAvailable);
        Setting registerSetting22 = registerSetting("query_sad_mp3", R.bool.config_navBarTapThrough);
        registerSetting22.registerValue(1, R.bool.config_navBarCanMove, R.bool.config_navBarNeedsScrim);
        registerSetting22.registerValue(0, R.bool.config_multiuserDelayUserDataLocking, R.bool.config_navBarAlwaysShowOnSideEdgeGesture);
        Setting registerSetting23 = registerSetting("query_sad_mpeg2", R.bool.config_pinnerCameraApp);
        registerSetting23.registerValue(1, R.bool.config_permissionsIndividuallyControlled, R.bool.config_pinnerAssistantApp);
        registerSetting23.registerValue(0, R.bool.config_overrideRemoteViewsActivityTransition, R.bool.config_pdp_reject_enable_retry);
        Setting registerSetting24 = registerSetting("query_sad_aac", R.bool.config_enableNetworkLocationOverlay);
        registerSetting24.registerValue(1, R.bool.config_enableLockScreenRotation, R.bool.config_enableMultiUserUI);
        registerSetting24.registerValue(0, R.bool.config_enableHapticTextHandle, R.bool.config_enableLockBeforeUnlockScreen);
        Setting registerSetting25 = registerSetting("query_sad_dts", R.bool.config_hearing_aid_profile_supported);
        registerSetting25.registerValue(1, R.bool.config_hasPermanentDpad, R.bool.config_hasRecents);
        registerSetting25.registerValue(0, R.bool.config_guestUserEphemeral, R.bool.config_handleVolumeKeysInWindowManager);
        Setting registerSetting26 = registerSetting("query_sad_atrac", R.bool.config_enableUpdateableTimeZoneRules);
        registerSetting26.registerValue(1, R.bool.config_enableScreenshotChord, R.bool.config_enableServerNotificationEffectsForAutomotive);
        registerSetting26.registerValue(0, R.bool.config_enableNewAutoSelectNetworkUI, R.bool.config_enableNightMode);
        Setting registerSetting27 = registerSetting("query_sad_onebitaudio", R.bool.config_quickSettingsSupported);
        registerSetting27.registerValue(1, R.bool.config_powerDecoupleInteractiveModeFromDisplay, R.bool.config_preferenceFragmentClipToPadding);
        registerSetting27.registerValue(0, R.bool.config_pinnerHomeApp, R.bool.config_powerDecoupleAutoSuspendModeFromDisplay);
        Setting registerSetting28 = registerSetting("query_sad_ddp", R.bool.config_focusScrollContainersInTouchMode);
        registerSetting28.registerValue(1, R.bool.config_fillMainBuiltInDisplayCutout, R.bool.config_fingerprintSupportsGestures);
        registerSetting28.registerValue(0, R.bool.config_expandLockScreenUserSwitcher, R.bool.config_faceAuthDismissesKeyguard);
        Setting registerSetting29 = registerSetting("query_sad_dtshd", R.bool.config_keyguardUserSwitcher);
        registerSetting29.registerValue(1, R.bool.config_intrusiveNotificationLed, R.bool.config_keepRestrictedProfilesInBackground);
        registerSetting29.registerValue(0, R.bool.config_hotswapCapable, R.bool.config_inflateSignalStrength);
        Setting registerSetting30 = registerSetting("query_sad_truehd", R.bool.config_reverseDefaultRotation);
        registerSetting30.registerValue(1, R.bool.config_restartRadioAfterProvisioning, R.bool.config_restart_radio_on_pdp_fail_regular_deactivation);
        registerSetting30.registerValue(0, R.bool.config_requireCallCapableAccountForHandle, R.bool.config_requireRadioPowerOffOnSimRefreshReset);
        Setting registerSetting31 = registerSetting("query_sad_dst", R.bool.config_goToSleepOnButtonPressTheaterMode);
        registerSetting31.registerValue(1, R.bool.config_forceWindowDrawsStatusBarBackground, R.bool.config_freeformWindowManagement);
        registerSetting31.registerValue(0, R.bool.config_forceShowSystemBars, R.bool.config_forceSystemPackagesQueryable);
        Setting registerSetting32 = registerSetting("query_sad_wmapro", R.bool.config_setColorTransformAcceleratedPerLayer);
        registerSetting32.registerValue(1, R.bool.config_sendAudioBecomingNoisy, R.bool.config_setColorTransformAccelerated);
        registerSetting32.registerValue(0, R.bool.config_safe_media_disable_on_volume_up, R.bool.config_safe_media_volume_enabled);
        Setting registerSetting33 = registerSetting("query_sad_max", R.bool.config_mobile_data_capable);
        registerSetting33.registerValue(1, R.bool.config_maskMainBuiltInDisplayCutout, R.bool.config_mms_content_disposition_support);
        registerSetting33.registerValue(0, R.bool.config_lockUiMode, R.bool.config_mainBuiltInDisplayIsRound);
        Setting registerSetting34 = registerSetting("earc_enabled", 17891646);
        registerSetting34.registerValue(1, 17891649, 17891650);
        registerSetting34.registerValue(0, 17891647, 17891648);
        verifySettings();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HdmiCecConfig(Context context) {
        this(context, new StorageAdapter(context));
    }

    private Setting registerSetting(@HdmiControlManager.SettingName String str, int i) {
        Setting setting = new Setting(this.mContext, str, i);
        this.mSettings.put(str, setting);
        return setting;
    }

    private void verifySettings() {
        for (Setting setting : this.mSettings.values()) {
            setting.getDefaultValue();
            getStorage(setting);
            getStorageKey(setting);
        }
    }

    private Setting getSetting(String str) {
        if (this.mSettings.containsKey(str)) {
            return this.mSettings.get(str);
        }
        return null;
    }

    @Storage
    private int getStorage(Setting setting) {
        String name = setting.getName();
        name.hashCode();
        char c = 65535;
        switch (name.hashCode()) {
            case -2072577869:
                if (name.equals("hdmi_cec_version")) {
                    c = 0;
                    break;
                }
                break;
            case -1788790343:
                if (name.equals("system_audio_mode_muting")) {
                    c = 1;
                    break;
                }
                break;
            case -1618836197:
                if (name.equals("set_menu_language")) {
                    c = 2;
                    break;
                }
                break;
            case -1275604441:
                if (name.equals("rc_profile_source_handles_media_context_sensitive_menu")) {
                    c = 3;
                    break;
                }
                break;
            case -1253675651:
                if (name.equals("rc_profile_source_handles_top_menu")) {
                    c = 4;
                    break;
                }
                break;
            case -1188289112:
                if (name.equals("rc_profile_source_handles_root_menu")) {
                    c = 5;
                    break;
                }
                break;
            case -1157203295:
                if (name.equals("query_sad_atrac")) {
                    c = 6;
                    break;
                }
                break;
            case -1154431553:
                if (name.equals("query_sad_dtshd")) {
                    c = 7;
                    break;
                }
                break;
            case -1146252564:
                if (name.equals("query_sad_mpeg1")) {
                    c = '\b';
                    break;
                }
                break;
            case -1146252563:
                if (name.equals("query_sad_mpeg2")) {
                    c = '\t';
                    break;
                }
                break;
            case -1081575217:
                if (name.equals("earc_enabled")) {
                    c = '\n';
                    break;
                }
                break;
            case -971363478:
                if (name.equals("query_sad_truehd")) {
                    c = 11;
                    break;
                }
                break;
            case -910325648:
                if (name.equals("rc_profile_source_handles_contents_menu")) {
                    c = '\f';
                    break;
                }
                break;
            case -890678558:
                if (name.equals("query_sad_wmapro")) {
                    c = '\r';
                    break;
                }
                break;
            case -412334364:
                if (name.equals("routing_control")) {
                    c = 14;
                    break;
                }
                break;
            case -314100402:
                if (name.equals("query_sad_lpcm")) {
                    c = 15;
                    break;
                }
                break;
            case -293445547:
                if (name.equals("rc_profile_source_handles_setup_menu")) {
                    c = 16;
                    break;
                }
                break;
            case -219770232:
                if (name.equals("power_state_change_on_active_source_lost")) {
                    c = 17;
                    break;
                }
                break;
            case -25374657:
                if (name.equals("power_control_mode")) {
                    c = 18;
                    break;
                }
                break;
            case 18371678:
                if (name.equals("soundbar_mode")) {
                    c = 19;
                    break;
                }
                break;
            case 73184058:
                if (name.equals("volume_control_enabled")) {
                    c = 20;
                    break;
                }
                break;
            case 261187356:
                if (name.equals("hdmi_cec_enabled")) {
                    c = 21;
                    break;
                }
                break;
            case 791759782:
                if (name.equals("rc_profile_tv")) {
                    c = 22;
                    break;
                }
                break;
            case 799280879:
                if (name.equals("query_sad_onebitaudio")) {
                    c = 23;
                    break;
                }
                break;
            case 1577324768:
                if (name.equals("query_sad_dd")) {
                    c = 24;
                    break;
                }
                break;
            case 1629183631:
                if (name.equals("tv_wake_on_one_touch_play")) {
                    c = 25;
                    break;
                }
                break;
            case 1652424675:
                if (name.equals("query_sad_aac")) {
                    c = 26;
                    break;
                }
                break;
            case 1652427664:
                if (name.equals("query_sad_ddp")) {
                    c = 27;
                    break;
                }
                break;
            case 1652428133:
                if (name.equals("query_sad_dst")) {
                    c = 28;
                    break;
                }
                break;
            case 1652428163:
                if (name.equals("query_sad_dts")) {
                    c = 29;
                    break;
                }
                break;
            case 1652436228:
                if (name.equals("query_sad_max")) {
                    c = 30;
                    break;
                }
                break;
            case 1652436624:
                if (name.equals("query_sad_mp3")) {
                    c = 31;
                    break;
                }
                break;
            case 2055627683:
                if (name.equals("tv_send_standby_on_sleep")) {
                    c = ' ';
                    break;
                }
                break;
            case 2118236132:
                if (name.equals("system_audio_control")) {
                    c = '!';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case PackageManagerService.MIN_INSTALLABLE_TARGET_SDK /* 23 */:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_ENTRY_MODE /* 29 */:
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_11 /* 30 */:
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_12 /* 31 */:
            case ' ':
            case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_1 /* 33 */:
                return 2;
            default:
                throw new VerificationException("Invalid CEC setting '" + setting.getName() + "' storage.");
        }
    }

    private String getStorageKey(Setting setting) {
        String name = setting.getName();
        name.hashCode();
        char c = 65535;
        switch (name.hashCode()) {
            case -2072577869:
                if (name.equals("hdmi_cec_version")) {
                    c = 0;
                    break;
                }
                break;
            case -1788790343:
                if (name.equals("system_audio_mode_muting")) {
                    c = 1;
                    break;
                }
                break;
            case -1618836197:
                if (name.equals("set_menu_language")) {
                    c = 2;
                    break;
                }
                break;
            case -1275604441:
                if (name.equals("rc_profile_source_handles_media_context_sensitive_menu")) {
                    c = 3;
                    break;
                }
                break;
            case -1253675651:
                if (name.equals("rc_profile_source_handles_top_menu")) {
                    c = 4;
                    break;
                }
                break;
            case -1188289112:
                if (name.equals("rc_profile_source_handles_root_menu")) {
                    c = 5;
                    break;
                }
                break;
            case -1157203295:
                if (name.equals("query_sad_atrac")) {
                    c = 6;
                    break;
                }
                break;
            case -1154431553:
                if (name.equals("query_sad_dtshd")) {
                    c = 7;
                    break;
                }
                break;
            case -1146252564:
                if (name.equals("query_sad_mpeg1")) {
                    c = '\b';
                    break;
                }
                break;
            case -1146252563:
                if (name.equals("query_sad_mpeg2")) {
                    c = '\t';
                    break;
                }
                break;
            case -1081575217:
                if (name.equals("earc_enabled")) {
                    c = '\n';
                    break;
                }
                break;
            case -971363478:
                if (name.equals("query_sad_truehd")) {
                    c = 11;
                    break;
                }
                break;
            case -910325648:
                if (name.equals("rc_profile_source_handles_contents_menu")) {
                    c = '\f';
                    break;
                }
                break;
            case -890678558:
                if (name.equals("query_sad_wmapro")) {
                    c = '\r';
                    break;
                }
                break;
            case -412334364:
                if (name.equals("routing_control")) {
                    c = 14;
                    break;
                }
                break;
            case -314100402:
                if (name.equals("query_sad_lpcm")) {
                    c = 15;
                    break;
                }
                break;
            case -293445547:
                if (name.equals("rc_profile_source_handles_setup_menu")) {
                    c = 16;
                    break;
                }
                break;
            case -219770232:
                if (name.equals("power_state_change_on_active_source_lost")) {
                    c = 17;
                    break;
                }
                break;
            case -25374657:
                if (name.equals("power_control_mode")) {
                    c = 18;
                    break;
                }
                break;
            case 18371678:
                if (name.equals("soundbar_mode")) {
                    c = 19;
                    break;
                }
                break;
            case 73184058:
                if (name.equals("volume_control_enabled")) {
                    c = 20;
                    break;
                }
                break;
            case 261187356:
                if (name.equals("hdmi_cec_enabled")) {
                    c = 21;
                    break;
                }
                break;
            case 791759782:
                if (name.equals("rc_profile_tv")) {
                    c = 22;
                    break;
                }
                break;
            case 799280879:
                if (name.equals("query_sad_onebitaudio")) {
                    c = 23;
                    break;
                }
                break;
            case 1577324768:
                if (name.equals("query_sad_dd")) {
                    c = 24;
                    break;
                }
                break;
            case 1629183631:
                if (name.equals("tv_wake_on_one_touch_play")) {
                    c = 25;
                    break;
                }
                break;
            case 1652424675:
                if (name.equals("query_sad_aac")) {
                    c = 26;
                    break;
                }
                break;
            case 1652427664:
                if (name.equals("query_sad_ddp")) {
                    c = 27;
                    break;
                }
                break;
            case 1652428133:
                if (name.equals("query_sad_dst")) {
                    c = 28;
                    break;
                }
                break;
            case 1652428163:
                if (name.equals("query_sad_dts")) {
                    c = 29;
                    break;
                }
                break;
            case 1652436228:
                if (name.equals("query_sad_max")) {
                    c = 30;
                    break;
                }
                break;
            case 1652436624:
                if (name.equals("query_sad_mp3")) {
                    c = 31;
                    break;
                }
                break;
            case 2055627683:
                if (name.equals("tv_send_standby_on_sleep")) {
                    c = ' ';
                    break;
                }
                break;
            case 2118236132:
                if (name.equals("system_audio_control")) {
                    c = '!';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return setting.getName();
            case 1:
                return setting.getName();
            case 2:
                return setting.getName();
            case 3:
                return setting.getName();
            case 4:
                return setting.getName();
            case 5:
                return setting.getName();
            case 6:
                return setting.getName();
            case 7:
                return setting.getName();
            case '\b':
                return setting.getName();
            case '\t':
                return setting.getName();
            case '\n':
                return setting.getName();
            case 11:
                return setting.getName();
            case '\f':
                return setting.getName();
            case '\r':
                return setting.getName();
            case 14:
                return setting.getName();
            case 15:
                return setting.getName();
            case 16:
                return setting.getName();
            case 17:
                return setting.getName();
            case 18:
                return setting.getName();
            case 19:
                return setting.getName();
            case 20:
                return setting.getName();
            case 21:
                return setting.getName();
            case 22:
                return setting.getName();
            case PackageManagerService.MIN_INSTALLABLE_TARGET_SDK /* 23 */:
                return setting.getName();
            case 24:
                return setting.getName();
            case 25:
                return setting.getName();
            case 26:
                return setting.getName();
            case 27:
                return setting.getName();
            case 28:
                return setting.getName();
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_ENTRY_MODE /* 29 */:
                return setting.getName();
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_11 /* 30 */:
                return setting.getName();
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_12 /* 31 */:
                return setting.getName();
            case ' ':
                return setting.getName();
            case HdmiCecKeycode.CEC_KEYCODE_NUMBERS_1 /* 33 */:
                return setting.getName();
            default:
                throw new VerificationException("Invalid CEC setting '" + setting.getName() + "' storage key.");
        }
    }

    protected String retrieveValue(Setting setting, String str) {
        int storage = getStorage(setting);
        String storageKey = getStorageKey(setting);
        if (storage == 0) {
            HdmiLogger.debug("Reading '" + storageKey + "' sysprop.", new Object[0]);
            return this.mStorageAdapter.retrieveSystemProperty(storageKey, str);
        }
        if (storage == 1) {
            HdmiLogger.debug("Reading '" + storageKey + "' global setting.", new Object[0]);
            return this.mStorageAdapter.retrieveGlobalSetting(storageKey, str);
        }
        if (storage != 2) {
            return null;
        }
        HdmiLogger.debug("Reading '" + storageKey + "' shared preference.", new Object[0]);
        return this.mStorageAdapter.retrieveSharedPref(storageKey, str);
    }

    protected void storeValue(Setting setting, String str) {
        int storage = getStorage(setting);
        String storageKey = getStorageKey(setting);
        if (storage == 0) {
            HdmiLogger.debug("Setting '" + storageKey + "' sysprop.", new Object[0]);
            this.mStorageAdapter.storeSystemProperty(storageKey, str);
            return;
        }
        if (storage == 1) {
            HdmiLogger.debug("Setting '" + storageKey + "' global setting.", new Object[0]);
            this.mStorageAdapter.storeGlobalSetting(storageKey, str);
            return;
        }
        if (storage == 2) {
            HdmiLogger.debug("Setting '" + storageKey + "' shared pref.", new Object[0]);
            this.mStorageAdapter.storeSharedPref(storageKey, str);
            notifySettingChanged(setting);
        }
    }

    protected void notifySettingChanged(final Setting setting) {
        synchronized (this.mLock) {
            ArrayMap<SettingChangeListener, Executor> arrayMap = this.mSettingChangeListeners.get(setting);
            if (arrayMap == null) {
                return;
            }
            for (Map.Entry<SettingChangeListener, Executor> entry : arrayMap.entrySet()) {
                final SettingChangeListener key = entry.getKey();
                entry.getValue().execute(new Runnable() { // from class: com.android.server.hdmi.HdmiCecConfig.1
                    @Override // java.lang.Runnable
                    public void run() {
                        key.onChange(setting.getName());
                    }
                });
            }
        }
    }

    public void registerChangeListener(@HdmiControlManager.SettingName String str, SettingChangeListener settingChangeListener) {
        registerChangeListener(str, settingChangeListener, ConcurrentUtils.DIRECT_EXECUTOR);
    }

    public void registerChangeListener(@HdmiControlManager.SettingName String str, SettingChangeListener settingChangeListener, Executor executor) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        int storage = getStorage(setting);
        if (storage != 1 && storage != 2) {
            throw new IllegalArgumentException("Change listeners for setting '" + str + "' not supported.");
        }
        synchronized (this.mLock) {
            if (!this.mSettingChangeListeners.containsKey(setting)) {
                this.mSettingChangeListeners.put(setting, new ArrayMap<>());
            }
            this.mSettingChangeListeners.get(setting).put(settingChangeListener, executor);
        }
    }

    public void removeChangeListener(@HdmiControlManager.SettingName String str, SettingChangeListener settingChangeListener) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        synchronized (this.mLock) {
            if (this.mSettingChangeListeners.containsKey(setting)) {
                ArrayMap<SettingChangeListener, Executor> arrayMap = this.mSettingChangeListeners.get(setting);
                arrayMap.remove(settingChangeListener);
                if (arrayMap.isEmpty()) {
                    this.mSettingChangeListeners.remove(setting);
                }
            }
        }
    }

    @HdmiControlManager.SettingName
    public List<String> getAllSettings() {
        return new ArrayList(this.mSettings.keySet());
    }

    @HdmiControlManager.SettingName
    public List<String> getUserSettings() {
        ArrayList arrayList = new ArrayList();
        for (Setting setting : this.mSettings.values()) {
            if (setting.getUserConfigurable()) {
                arrayList.add(setting.getName());
            }
        }
        return arrayList;
    }

    public boolean isStringValueType(@HdmiControlManager.SettingName String str) {
        if (getSetting(str) == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        return getSetting(str).getValueType().equals(VALUE_TYPE_STRING);
    }

    public boolean isIntValueType(@HdmiControlManager.SettingName String str) {
        if (getSetting(str) == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        return getSetting(str).getValueType().equals(VALUE_TYPE_INT);
    }

    public List<String> getAllowedStringValues(@HdmiControlManager.SettingName String str) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_STRING)) {
            throw new IllegalArgumentException("Setting '" + str + "' is not a string-type setting.");
        }
        ArrayList arrayList = new ArrayList();
        Iterator<Value> it = setting.getAllowedValues().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getStringValue());
        }
        return arrayList;
    }

    public List<Integer> getAllowedIntValues(@HdmiControlManager.SettingName String str) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_INT)) {
            throw new IllegalArgumentException("Setting '" + str + "' is not a string-type setting.");
        }
        ArrayList arrayList = new ArrayList();
        Iterator<Value> it = setting.getAllowedValues().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getIntValue());
        }
        return arrayList;
    }

    public String getDefaultStringValue(@HdmiControlManager.SettingName String str) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_STRING)) {
            throw new IllegalArgumentException("Setting '" + str + "' is not a string-type setting.");
        }
        return getSetting(str).getDefaultValue().getStringValue();
    }

    public int getDefaultIntValue(@HdmiControlManager.SettingName String str) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_INT)) {
            throw new IllegalArgumentException("Setting '" + str + "' is not a string-type setting.");
        }
        return getSetting(str).getDefaultValue().getIntValue().intValue();
    }

    public String getStringValue(@HdmiControlManager.SettingName String str) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_STRING)) {
            throw new IllegalArgumentException("Setting '" + str + "' is not a string-type setting.");
        }
        HdmiLogger.debug("Getting CEC setting value '" + str + "'.", new Object[0]);
        return retrieveValue(setting, setting.getDefaultValue().getStringValue());
    }

    public int getIntValue(@HdmiControlManager.SettingName String str) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_INT)) {
            throw new IllegalArgumentException("Setting '" + str + "' is not a int-type setting.");
        }
        HdmiLogger.debug("Getting CEC setting value '" + str + "'.", new Object[0]);
        return Integer.parseInt(retrieveValue(setting, Integer.toString(setting.getDefaultValue().getIntValue().intValue())));
    }

    public void setStringValue(@HdmiControlManager.SettingName String str, String str2) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        if (!setting.getUserConfigurable()) {
            throw new IllegalArgumentException("Updating CEC setting '" + str + "' prohibited.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_STRING)) {
            throw new IllegalArgumentException("Setting '" + str + "' is not a string-type setting.");
        }
        if (!getAllowedStringValues(str).contains(str2)) {
            throw new IllegalArgumentException("Invalid CEC setting '" + str + "' value: '" + str2 + "'.");
        }
        HdmiLogger.debug("Updating CEC setting '" + str + "' to '" + str2 + "'.", new Object[0]);
        storeValue(setting, str2);
    }

    public void setIntValue(@HdmiControlManager.SettingName String str, int i) {
        Setting setting = getSetting(str);
        if (setting == null) {
            throw new IllegalArgumentException("Setting '" + str + "' does not exist.");
        }
        if (!setting.getUserConfigurable()) {
            throw new IllegalArgumentException("Updating CEC setting '" + str + "' prohibited.");
        }
        if (!setting.getValueType().equals(VALUE_TYPE_INT)) {
            throw new IllegalArgumentException("Setting '" + str + "' is not a int-type setting.");
        }
        if (!getAllowedIntValues(str).contains(Integer.valueOf(i))) {
            throw new IllegalArgumentException("Invalid CEC setting '" + str + "' value: '" + i + "'.");
        }
        HdmiLogger.debug("Updating CEC setting '" + str + "' to '" + i + "'.", new Object[0]);
        storeValue(setting, Integer.toString(i));
    }
}
