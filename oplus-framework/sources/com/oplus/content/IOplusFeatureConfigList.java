package com.oplus.content;

import com.android.internal.telephony.OplusTelephonyIntents;
import com.oplus.annotation.OplusFeature;
import com.oplus.annotation.OplusFeaturePermission;

/* loaded from: classes.dex */
public interface IOplusFeatureConfigList {

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String DISABLE_FEATURE_TORCH_HELPER = "oplus.software.powerkey_disbale_turnoff_torch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FAETURE_SIDO_SCREEN_ON_NO_45G_OPT = "oplus.software.radio.sido_screen_on_no_45g_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FAETURE_SIDO_SCREEN_ON_NO_SERVICE_OPT = "oplus.software.radio.sido_screen_on_no_service_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_1X_BSR_DDS_SWITCH = "oplus.software.radio.1x_bsr_dds_swtich";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_1X_INCALL_MMI_CODE_DISABLED = "oplus.software.radio.1X_incall_mmi_code_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_3G_NULL_APN = "oplus.software.radio.3g_null_apn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_5G_BAND_CFG = "oplus.software.radio.5g_band_cfg";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_5G_ICON_JP_REQUIREMENT = "oplus.software.5g_icon_jp_requirement";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_5G_SUPPORT = "oplus.software.radio.support_5g";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ACTIVITY_PAUSING_RESUME_BOOSTER = "oplus.software.activity_pausing_resume_booster";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ACTIVITY_PRELOAD = "oplus.software.activity_preload";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ACTIVITY_PRELOAD_ONLYHIGHPERF = "oplus.software.activity_preload_onlyhighperf";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ADD_MORE_MULTIAPP = "oplus.software.multiapp.add_more_multiapps";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ADFR_SUPPORT = "oplus.software.display.adfr";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AIR_INTERFACE_DETECT_SUPPORT = "oplus.software.radio.air_interface_detect_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ALERT_SLIDER = "oplus.software.audio.alert_slider";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ALIGN_ALARM_SUPPORT = "oplus.software.power.align_alarm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ALL_CLIENT = "oplus.software.radio.all_client";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ALWAYS_USE_LTE_LEVEL_FOR_NSA = "oplus.software.radio.always_use_lte_level_for_nsa";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ANTIROOT_DISABLED = "oplus.software.antiroot_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AOD_PREVENT_BURN_NEW_MECH = "oplus.software.display.aod_prevent_burn_new_mech";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AOD_PREVENT_BURN_SUPPORT = "oplus.software.display.aod_prevent_burn_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AOD_RAMLESS_SUPPORT = "oplus.software.display.aod_ramless_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AOD_SUPPORT = "oplus.software.display.aod_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AOD_TRANSPARENT = "oplus.software.display.aod_transparent";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AOL_SCENE_TO_MODEM = "oplus.software.radio.aol_scene_to_modem";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_ANSWERPHONE_SUPPORT = "oplus.software.aon_phone_answerphone";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_ANT_PEEP_DISABLE_SUPPORT = "oplus.software.aon_ant_peep_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_AOD_SUPPORT = "oplus.software.aon_aod_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_AUTO_NONE_SCREEN_OFF_DISABLE_SUPPORT = "oplus.software.aon_auto_none_screen_off_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_DISTANCE_DETECTION_SUPPORT = "oplus.software.aon_distance_detection_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_EXPLORER_SUPPORT = "oplus.software.aon_explorer_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_GESTUREUI_SUPPORT = "oplus.software.aon_gestureui_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_GESTURE_CONTROL_SUPPORT = "oplus.software.aon_gesture_control_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_GESTURE_PRESS_SUPPORT = "oplus.software.aon_gesture_press";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_HARDWARE_SUPPORT = "oplus.hardware.aon_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_LUMINANCE_CONTROL = "oplus.software.aon_luminance_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_PHONE_CAMERA_GESTURE_RECOGNITION_SUPPORT = "oplus.software.aon_phone_camera_gesture_recognition";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_PHONE_MUTE_SUPPORT = "oplus.software.aon_phone_mute";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_PHONE_SUPPORT = "oplus.software.aon_phone_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_POSE_DETECTION_SUPPORT = "oplus.software.aon_pose_detection_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_SENSORHUB_SUPPORT = "oplus.software.aon_sensorhub_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_SOFTWARE_SUPPORT = "oplus.software.aon_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AON_SUB_CAM_SUPPORT = "oplus.software.aon_sub_cam_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_APN_RECOVERY = "oplus.software.radio.apn_recovery";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_APN_RECOVERY_ADJUST_FOR_EXPORT = "oplus.software.radio.apn_recovery_for_export";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_APN_RUS_UPDATE = "oplus.software.radio.apn_rus_update_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.PERSIST_FEATURE)
    @OplusFeaturePermission("com.oplus.permission.OPLUS_FEATURE")
    public static final String FEATURE_APP_ACCESS_NFC = "oplus.software.nfc.app_access_nfc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_APP_REDUCE_BRIGHTNESS_DISABLE = "oplus.software.display.disable_app_reduce_brightness_for_engineering";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_APP_RESOLUTION_DEFAULT = "oplus.software.app_resolution_auto";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_APP_RESOLUTION_DEFAULT_CONFIG = "oplus.software.resolution_default_config";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_APP_RESOLUTION_SWITCH = "oplus.software.app_resolution_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_APTG_FORCE_ENABLE_ROAMING = "oplus.software.radio.aptg_force_enable_roaming";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ASSISTANT_SUPOORT = "oplus.software.notify_wellbeing";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUDIOEFFECT_SUPPORT = "oplus.software.audio.audioeffect_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUDIOX_SUPPORT = "oplus.software.audio.audiox_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUDIO_ROUTE = "oplus.software.audio.route_strategy";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTOBRIGHTCTL_ANIMATION_SUPPORT = "oplus.software.display.autobrightctl_animation_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTOLIGHT_PSENSOR_NOTSUPPORT = "oplus.software.display.autolight_psensor_notsupport";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTOMATIC_BRIGHTNESS_PROXIMITY_DISABLE = "oplus.software.display.automatic_brightness_proximity_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTOMATIC_NETWORK_MODE = "oplus.software.radio.automatic_network_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTOTIMEUPDATE_FORCE = "oplus.software.country.autotimeupdate_force";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTO_CHANGE_NETWORK_DEFAULT_VALUE_DISABLE_CN = "oplus.software.wlan.network_auto_change_default_value_disable_cn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTO_CHANGE_NETWORK_DEFAULT_VALUE_ENABLE_EX = "oplus.software.wlan.network_auto_change_default_value_enable_ex";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTO_DISABLE_SMART_WFC_SA_CONTROLLER = "oplus.software.radio.auto_disable_smart_wfc_sa_controller";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTO_ENABLE_SMART_WFC_SA_CONTROLLER = "oplus.software.radio.auto_enable_smart_wfc_sa_controller";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_AUTO_LAYOUT = "oplus.software.view.autolayout";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BACKGROUND_STREAM_SERVICE_ENABLED = "oplus.software.background_stream_tileservice_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BACK_TOUCH_FINGERPRINT = "oplus.software.fingeprint_back_touch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BENCHMARK_SUPPORT = "oplus.software.benchmark.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BIND_SMALL_CORE = "oplus.software.bind_small_core";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BIOMETRICS_PALMPRINT = "oplus.software.palmprint";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BIOMETRICS_PALMPRINT_NON_UNIFY = "oplus.software.palmprint_non_unify";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BIOMETRICS_PALMPRINT_UNIFY = "oplus.software.palmprint_unify";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BOOST_REFRESHRATE_FOR_ANIMATION_SUPPORT = "oplus.software.display.refreshrate_animation_boost";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BRIGHTNESS_LOWLEVEL_SCREEN = "oplus.software.display.brightness_lowlevel_screen";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BRIGHTNESS_MEMORY_RM = "oplus.software.display.brightness_memory_rm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BRIGHTNESS_MODE_AUTOMATIC = "oplus.software.display.brightness_mode_automatic";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BROWSER_HARMONYNET = "oplus.software.browser.harmonynet";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BTC_DYNAMIC_TDD = "oplus.software.btc.dynamic_tdd";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_ABNORMAL_DETECT_DISABLE = "oplus.software.bt.abnormal_detect_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_ABSVOLUME_SUPPORT = "oplus.software.bt.absvolume_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_ABSVOLUME_SUPPORT_DISABLE = "oplus.software.bt.absvolume_support_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_APCF_THRESHOLD = "oplus.software.bt.apcf_threshold";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_API_CUSTOMIZE_DISABLE = "oplus.software.bt.api_customize_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_AUDIO_GUARD_DISABLE = "oplus.software.bt.audio_guard_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_BINAURAL_RECORD = "oplus.software.bt.binaural_record";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_BLE_SCAN_STRATEGYMODE = "oplus.software.bt.ble_scan_strategymode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_CONNECT_HELP = "oplus.software.bt.connect.helper";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_DELAY_REPORT_DISABLE = "oplus.software.bt.delay_report_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_DYNAMIC_BLACKLIST_DWM_DISABLE = "oplus.software.bt.dynamic_blacklist_dwm_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_HCI_LOG_DISABLE = "oplus.software.bt.hcilog_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_HDT = "oplus.software.bt.ear_hdt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_IDENTIFICATION_DISABLE = "oplus.software.bt.identification_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_IGNORE_LOCATIONMODE_DISABLE = "oplus.software.bt.ignore_locationmode_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_IOT_ENABLELOGGING_DISABLE = "oplus.software.bt.iot_enablelogging_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_OIDT_DISABLE = "oplus.software.bt.oidt_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_ONLY_SUPPORT_CN = "oplus.software.bt.help.region_CN";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_PROFILE_TRAKCER_DISABLE = "oplus.software.bt.profile_tracker_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_QoS_DISABLE = "oplus.software.bt.qos.disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_RMTDEVICE_REPORT_DISABLE = "oplus.software.bt.rmtdevice_report_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_RSSI_RANGE_SUPPORT = "oplus.software.bt.rssi_range_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_RSSI_RANGE_SUPPORT_DISABLE = "oplus.software.bt.rssi_range_support_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_RUS_DISABLE = "oplus.software.bt.rus_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_SAR = "oplus.software.bt.sar_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    @Deprecated
    public static final String FEATURE_BT_SMART_ANTENNA = "oplus.software.bt.smart.antenna";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_SSR_DUMP_DISABLE = "oplus.software.bt.ssrdump_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_SWITCH_LOG_DISABLE = "oplus.software.bt.switchlog_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_TETHERING_DENIED = "oplus.software.connectivity.tethering_denied";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_VENDOR_AT_COMMAND_DISABLE = "oplus.software.bt.vendor_at_command_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BT_VIRTUAL_CALL_ACTION_DISABLE = "oplus.software.bt.virtual_call_action_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_BUSINESS_FLASHBACK = "oplus.business.flashback";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_C303_DEVICE = "oplus.software.carrier.c303_device";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_C427_DEVICE = "oplus.software.carrier.c427_device";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CALL_ASSISTANT_SUPPORT = "oplus.software.audio.call_assistant_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CALL_FORWARDING_NUM_EXT_DISABLED = "oplus.software.radio.call_forwarding_num_ext_disalbed";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CALL_TRANSLATOR_SUPPORT = "oplus.software.audio.call_translator_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CAMERA_DROP_DETECTION = "oplus.software.camera.dropdetection_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CAMERA_HEIC_SUPPORT = "oplus.software.camera.heic_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CAMERA_INF_DETECT = "oplus.software.radio.camera.inf.detect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CAMERA_PREOPEN = "oplus.software.preload.camera";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CAMERA_VOLUME_QUICK_LAUNCH = "oplus.software.camera_volume_quick_launch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CDMACW_FILTER_DISABLED = "oplus.software.radio.cdmacw_filter_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CHANGE_PACKAGES_STATE = "oplus.software.change_packages_state";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CHECK_COTA_MOUNT_WHILE_SETUPWIZARD = "oplus.software.cota.check_mount_setupwizard";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    @Deprecated
    public static final String FEATURE_CHECK_SERVER_SUPPORT = "oplus.software.radio.check_server_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CINEMA_MODE_SUPPORT = "oplus.software.display.cinema_mode_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CLIENT_HIDE_STORAGE = "oplus.all.client_hide_storage";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CLIPBOARD_NOTIFY_CONTROL_ENABLE = "oplus.software.clipboard_notify_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CLIR_DEFAULT_SUPPRESSION = "oplus.software.radio.clir_default_suppression";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CLOSEEYE_FACE = "oplus.software.face_closeeye_detect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CMCC_DM = "oplus.software.radio.cmcc_dm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COLLECT_PROX_USAGE_SUPPORT = "oplus.software.sensor.collect_prox_usage";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COLORFUL_MODE_SUPPORT = "oplus.software.display.colorful_mode_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COLORMODE_CALIBRATE_P3_D65_SUPPORT = "oplus.software.display.colormode_calibrate_p3_65_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COLORMODE_CALIBRATE_SUPPORT = "oplus.software.display.colormode_calibrate_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COLORMODE_OLED_SUPPORT = "oplus.software.display.colormode_oled_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COLORX_EDR_SUPPORT = "oplus.software.display.game.hdr3.0_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COLORX_SUPPORT = "oplus.software.colorx.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COMBINATION_KEY_GRAB_SYSTRACE = "oplus.software.combination.key.grab.systrace";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COMM_SCENE_SWITCH_ENABLE = "oplus.software.radio.comm_scene_switch_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CONGESTION_SUBWAY = "oplus.software.radio.congestion_subway";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CONNECTIVITY_REGION_CN = "oplus.software.connectivity.region_CN";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CONNECTIVITY_REGION_JP = "oplus.software.connectivity.region_JP";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CONTINUE_TO_USE_NEW_PACKAGE = "oplus.software.continue_to_use_new_package";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CONTROLL_DRSEARCH_DURING_CALL = "oplus.software.radio.controll_drsearch_during_call";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_COOLEFECT_COOLEX_SUPPORT = "oplus.software.coolex.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CRS_RUS_CONTROL = "oplus.software.radio.crs_rus_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CTA_SUPPORT = "oplus.software.cta.support";

    @OplusFeature(OplusFeature.OplusFeatureType.PERSIST_FEATURE)
    @OplusFeaturePermission(OplusTelephonyIntents.OPLUS_SAFE_SECURITY_PERMISSION)
    public static final String FEATURE_CUSTOM_DEVICE_OWNER_UNINSTALL_DISABLE = "oplus.software.custom_device_owner_uninstall_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CUSTOM_DWB_CURVE_SUPPORT = "oplus.software.display.custom_dwb_curve";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CUSTOM_ECCLIST_WRITABLE = "oplus.software.radio.custom_ecclist_writable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CUSTOM_MTK_OOS_CFG = "oplus.software.radio.custom_mtk_oos_cfg";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_CUSTOM_PLMN_FOR_SPECIAL_OPERATOR = "oplus.software.radio.custom_plmn_for_special_operator";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DATA_AVOID_DATA_ICON_DISABLED = "oplus.software.radio.data_avoid_data_icon_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DATA_BLOCK_NONDDS_IMS_PAKG = "oplus.software.radio.block_nondds_pck";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DATA_DEFAULT_AS_DUN_APN_DISABLED = "oplus.software.radio.data_default_as_dun_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DATA_DNS_SPEED_UP = "oplus.software.radio.dns_speed_up";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DATA_DYNAMIC_ENABLE = "oplus.software.radio.data_dynamic_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DATA_LINK_GAME = "oplus.software.radio.data_link_game";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DATA_NW_DIAGNOSIS = "oplus.software.radio.data_network_diagnosis";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DCBACKLIGHT_SUPPORT = "oplus.software.display.dcbacklight_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DDS_SWITCH = "oplus.software.radio.dds_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DEFAULTAPP_OPLUS_POLICY_ENABLED = "oplus.software.defaultapp.color_policy_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DEFAULTAPP_REMOVE_FORCE_LAUNCHER = "oplus.software.defaultapp.remove_force_launcher";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DEFAULTAPP_SET_CHROME_BROWSER = "oplus.software.defaultapp.set_chrome_browser";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DEFAULT_DATA_ROAMING = "oplus.software.radio.default_data_roaming";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DEFAULT_TOP_DISPLAY = "oplus.software.display.default_top_display";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DELAY_AML_SMS = "oplus.software.radio.delay_aml_sms";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DELAY_SMS_WHEN_BROADCASTING = "oplus.software.radio.delay_sms_when_broadcasting";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DETECT_VALID_WAP_PUSH_INDEX = "oplus.software.radio.detect_valid_wap_push_index";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DEVICEIDLE_NETWORK_OPT = "oplus.software.deviceidle_network_optimization";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DEVICE_CASE = "oplus.software.device_case";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DIABLE_DIRAC_FOR_ENGINEERING = "oplus.software.audio.disable_dirac_for_engineering";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DIABLE_DOLBY_FOR_ENGINEERING = "oplus.software.audio.disable_dolby_for_engineering";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DIAGNOSE_PARSE_NR_CELL_SUPPORT = "oplus.software.radio.diagnose_parse_nr_cell_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DIAGNOSIS_OLD_PLATFORM = "oplus.software.radio.diagnosis_old_platform";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DIRAC_A2DP_SUPPORT = "oplus.software.audio.dirac.a2dp.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DIRAC_BLACKLIST_SUPPORT = "oplus.software.audio.dirac.blacklist.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DIRAC_SUPPORT = "oplus.software.audio.dirac_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DIRAC_V2_SUPPORT = "oplus.software.audio.dirac_v2_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLED_CACHED_UID = "oplus.software.radio.disabled_cached_uid";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_ALL_SA_BACKOFF = "oplus.software.radio.disable_all_sa_backoff";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_AOD_ALL_DAY_MODE = "oplus.software.disable_aod_all_day_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_AUTO_NETWORK_SELECT = "oplus.software.radio.disable_auto_network_select";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_CLEAN_EU = "oplus.software.phonemanager_disable_clean_eu";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_CLEAN_JP = "oplus.software.phonemanager_disable_clean_jp";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_CN_GMS = "oplus.software.disable_cn_gms";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_DELAY_MCP_KILL = "oplus.software.ams.disable_delay_mcp_kill";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_EFS_NV_DUMP = "oplus.software.radio.disable_efs_nv_dump";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_FIRST_FEATURE_LOAD = "oplus.software.radio.disable_first_feature_load";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_JETPACK_WINDOW_LAYOUT_INFO = "oplus.software.disable_jetpack_window_layout_info";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_KEYLOG_PRINT = "oplus.software.radio.disable_keylog_print";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_MEETING_OPITMIZ = "oplus.software.radio.disable_online_meeting";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_NOTIFICATION_DRAW_CHECK = "oplus.software.wms.disable_notification_draw_check";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_NR5G_CAUSE_5G_DUMP = "oplus.software.radio.disable_5g_nr_5gdump";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_NW_LIMIT_NOTIFY = "oplus.software.radio.disable_nw_limit_notify";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_POWERSAVE = "oplus.software.power.disable_power_save";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_PRELOAD_SPLASH = "oplus.software.wms.disable_preload_splash";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_SCENE_MODE = "oplus.software.radio.disable_scene_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_SMALLWINDOW_LEATHER = "oplus.software.disable_smallwindow_leather";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISABLE_SPECIAL_DATA_ROAMING = "oplus.software.disable_special_data_roaming";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DISPLAY_PLMN_SPN = "oplus.software.radio.display_plmn_spn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DITO_CT_ATUO_IMS_REG = "oplus.software.radio.dito_ct_auto_ims_reg";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DOLBYVISION_MULTI_DISPLAY = "oplus.software.dv.multi_display";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DOLBY_ENHANCE_BRIGHTNESS_V1_RM_SUPPORT = "oplus.software.display.dolby_enhance_v1_rm_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DOLBY_ENHANCE_BRIGHTNESS_V1_SUPPORT = "oplus.software.display.dolby_enhance_v1_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DOLBY_SUPPORT = "oplus.software.audio.dolby_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DRM_SUPPORT = "oplus.software.video.drm_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DSDA_HOLD_NOT_SUPPORT = "oplus.software.radio.dsda_hold_not_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DUALHEADPHONE_SUPPORT = "oplus.software.audio.dualheadphone";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DUALHEADPHONE_SUPPORT_LITE = "oplus.software.audio.dualheadphone.lite";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DUAL_LIGHTSENSOR_SUPPORT = "oplus.software.display.dual_lightsensor_support";

    @OplusFeature(OplusFeature.OplusFeatureType.PERSIST_FEATURE)
    public static final String FEATURE_DUAL_NR_ENABLED = "oplus.software.radio.dual_nr_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.PERSIST_FEATURE)
    public static final String FEATURE_DUAL_NR_TOGGLE_ENABLED = "oplus.software.radio.dual_nr_toggle_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DUAL_PS = "oplus.software.radio.dual_ps";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DUAL_SIMS_SMART5G_ENABLED = "oplus.software.radio.dual_sims_smart5g_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DUAL_SPEAKER_SUPPORT = "oplus.software.audio.dualspk_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DUNNAME_SAMEAS_DEFAULT = "oplus.software.radio.dun_name_same_as_default";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DUPLICATE_PERMISSION_INSTALL = "oplus.software.duplicate_permission_install";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DYNAMIC_FPS_SWITCH = "oplus.software.display.dynamic_fps_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DYNAMIC_REPORT_RATE = "oplus.software.dynamic_report_rate";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_DYNAMIC_SINGLE_SIM_IMEI_COMPARE = "oplus.software.radio.dynamic_single_sim_imei_compare";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ECC_CSFB_UPDATE_RAT = "oplus.software.radio.ecc_csfb_update_rat";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ECC_SOLUTION_DISABLED = "oplus.software.radio.ecc_solution_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ELEVATOR_OOS_PLMN_SCAN = "oplus.software.radio.elev_oos_plmn_scan";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ELEV_AUTOMATION = "oplus.software.radio.elev_automation";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ELEV_GW_NDDS = "oplus.software.radio.elev_ndds";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_EMERGENCY_CALL_CHANNEL = "oplus.software.radio.emergency_call_channel";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_EMERGENCY_CALL_DATA_SHARE = "oplus.software.radio.emergency_call_data_share";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_EMERGENCY_CALL_ECC_APN = "oplus.software.radio.emergency_call_ecc_apn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ENABLE_DIALING_NULBER_CHECK = "oplus.software.radio.enable_cf_dialing_nulber_check";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ENABLE_OEM_LOG = "oplus.software.radio.enable_oem_log";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ENABLE_SA_WEAK_SIGNAL_BACKOFF = "oplus.software.radio.enable_weak_signal_backoff";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ENABLE_TELCEL_COSTOMIZE_NAME = "oplus.software.radio.enable_telcel_costomize_name";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ENDC_NR_SIGNAL_OPTIMIZATION = "oplus.software.radio.nsa_signal_optim";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ENHANCE_BRIGHTNESS_WITH_UIDIMMING = "oplus.software.display.enhance_brightness_with_uidimming";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ENVELOPE_ASSISTANT_ENABLE = "oplus.software.notification.envelope_assistant_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_EXIT_INFO_RECORD = "oplus.software.application_exit_info_detect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_EXP_ENG_CALL_TEST_UNSUPPORTED = "oplus.software.radio.exp_eng_call_test_unsupport";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_EXP_NOTSHOW_STK_BOOTWIZARD = "oplus.software.radio.EXP_NOTSHOW_STK_BOOTWIZARD";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_EXP_OPERATOR_SWITCH_ENABLE = "oplus.software.radio.exp_operator_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_EXTREME_INSERT_SUPPORT = "oplus.software.display.game.support.extreme_insert";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FAST_DORMANCY = "oplus.software.radio.fast_dormancy";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FBE_SDP = "oplus.software.fbe.sdp";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FCC_DISABLE_DC_SAR = "oplus.software.radio.fcc_disable_dc_sar";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FEMTOCELL_UI = "oplus.software.radio.usv_femtocell_ui";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FETCH_NUMBER_COUNTRY_NAME = "oplus.software.radio.fetch_number_country_name";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FINGERPRINT_DISABLE_DIMLAYER = "oplus.software.display.fingerprint_disable_dimlayer";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FINGERPRINT_SHUTTER = "oplus.software.fingerprint.shutter";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FIVE_SIGNAL_BAR = "oplus.software.five_signal_bar";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FIVE_SIGNAL_BAR_FOR_DIFF_BAND = "oplus.software.five_signal_bar_for_diff_band";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FLUID_REPLACE_NTF = "oplus.software.fluid_replace_ntf";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FOLD = "oplus.hardware.type.fold";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FOLDING_MODE_SUPPORT = "oplus.software.radio.folding_mode_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FOLD_REMAP_DISPLAY_DISABLED = "oplus.software.fold_remap_display_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FORBID_DELETE_AND_CLEAR_FAMILYGUARD = "oplus.software.forbid_delete_and_clear_familyguard";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FORCE_FCC_SAR_REGION = "oplus.software.radio.force_fcc_sar";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FORCE_SET_DENSITY_FOLDABLE_DISPLAY = "oplus.software.force_set_density_foldable_display";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FORWARDLY_FREEZE = "oplus.software.forwardly_freeze";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FRONT_PRESS_FINGERPRINT = "oplus.software.fingeprint_front_press";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FRONT_TOUCH_FINGERPRINT = "oplus.software.fingeprint_front_touch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FRONT_TPPROTECT_FINGERPRINT = "oplus.software.fingeprint_front_tpprotect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_FULLYSRGAME_SUPPORT = "oplus.software.display.game.sr.fully_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAMESPACE_MT_OPTIMIZATION = "oplus.software.radio.mt_optimization";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_ACCELERATE = "oplus.software.radio.game_accelerate";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_ANTI_MISTOUCH_SUPPORT = "oplus.intelligent.anti.touch.feature";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_BACKOFF_SA = "oplus.software.radio.game_backoff_sa";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_BOUNCE_SUPPORT = "oplus.software.game_bounce_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_COLD_START_SPEEDUP_SUPPORT = "oplus.software.game.cold.start.speedup.enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_DAPR_SUPPORT = "oplus.software.display.game.dapr_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_ENGINE_VIBRATE_V1_SUPPORT = "oplus.software.game_engine_vibrator_v1.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_HAPTIC_VIBRATE_V1_SUPPORT = "oplus.software.haptic_vibrator_v1.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_HAPTIC_VIBRATE_V2_SUPPORT = "oplus.software.haptic_vibrator_v2.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_IMS_REG = "oplus.software.radio.game_ims_reg";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_JOYSTICK_SUPPORT = "oplus.software.joystick.game_joystick_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_LOL_ENABLE = "oplus.software.game.lol_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_OPLUS_PLUS_V2_SUPPORT = "oplus.software.display.game_color_plus_v2_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_PEL_ENABLE = "oplus.software.game.pel_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_SCROFF_ACT_PRELOAD = "oplus.software.game_scroff_act_preload";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_SPACE_TOOL_BOX = "oplus.software.game_space_tool_box";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GAME_TOUCH_ADJUSTER_SUPPORT = "oplus.software.game.touch_adjuster_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GCP_IRIS_SDR2HDR_SUPPORT = "oplus.software.display.game_color_plus_iris_sdr2hdr_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GDVIBRATOR_SUPPORT = "oplus.software.vibrator_gdvibrator";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GEME_VOLUMEMUTE_SUPPORT = "oplus.software.audio.game_volumemute_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GENERAL_COOLING_BACK_CLIP_SUPPORT = "oplus.software.general.cooling.back.clip.enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GEOIPTIME = "oplus.software.radio.geoiptime_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GESTUREUI_FOR_SCREEN_OFF_SHORTHAND = "oplus.software.screen_off_shorthand";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GESTURE_PROXIMITYSENSOR = "oplus.software.gesture_proximitysensor";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GMS_ADP_SUPPORT = "oplus.software.gameopt.gms.adp_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GOOGLE_EXTENSION_EMBEDDING = "oplus.software.display.google_extension_embedding";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GOOGLE_EXTENSION_LAYOUT = "oplus.software.display.google_extension_layout";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GOOGLE_MESSAGE_CONFIG_ENABLE = "oplus.software.radio.google_message_config_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GPS_NO_SIM_LPPE_SUPPORTED = "oplus.software.gps.no_sim_lppe_supported";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GPS_UST_E911 = "oplus.software.gps.ust_e911";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GPU_CONTROLPANEL_SUPPORT = "oplus.gpu.controlpanel.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GT_MODE_SUPPORT_OPTIMIZE = "oplus.software.support.gt.mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GUARDELF_SHOW_DELUSIVE_MEMORY = "oplus.software.battery.show_confused_hardwareinfo";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_GUARDELF_SUPPORT = "oplus.software.power.guardelf";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HANDLE_NO_DATA = "oplus.software.radio.handle_no_data";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HANS_RESTRICTION = "oplus.software.hans_restriction";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HARDWARE_RC_SUPPORT = "oplus.software.display.enable_hardware_roundcorner";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HDR_ENHANCE_BRIGHTNESS = "oplus.software.display.hdr_enhance_brightness_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HEARING_HEALTH_SUPPORT = "oplus.software.audio.hearing_health_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HEYCAST_INTERNAL_RECORD_SUPPORT = "oplus.software.audio.heycast_internal_record_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HIDDEN_API_COLLECT = "oplus.software.enable_hidden_api_collect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HIDE_2G_MODE = "oplus.software.radio.hide_2G_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HIDE_ALERT_WINDOW_NOTIFICATION = "oplus.software.floatwindow_hide_alertwindow_notification";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HIDE_MERGE_BUTTON = "oplus.software.radio.hide_merge_button_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HQV_FRC_SUPPORT = "oplus.software.video.hqv_frc_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HQV_SUPPORT = "oplus.software.video.hqv_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HVOLTE_DISABLED = "oplus.software.radio.hvolte_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HW_MANUFACTURER_MTK = "oplus.software.hw.manufacturer.mtk";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HW_MANUFACTURER_QUALCOMM = "oplus.software.hw.manufacturer.qualcomm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_HW_MIPI_FAIL_SUPPORT = "oplus.software.radio.hw.mipi.fail.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ICON_LTEAD_5GE = "oplus.software.radio.icon_ltead_5ge";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ICON_NRAD_5GPLUS = "oplus.software.radio.icon_nrad_5gplus";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ICON_NRAD_5GUC = "oplus.software.radio.icon_nrad_5guc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ICON_NRAD_5GUW = "oplus.software.radio.icon_nrad_5guw";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_IGNORE_CODE_SIP_REDIRECTED = "oplus.software.radio.ignore_sip_redirected";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_IGNORE_OFF_VONR = "oplus.software.radio.ignore_turn_off_vonr";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_IGNORE_RETRICTED_ENABLED = "oplus.software.radio.ignore_dcnr_retricted_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_IGNORE_SIM_VM_NUMBER = "oplus.software.radio.ignore_sim_vm_number_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_IMS_GUARD = "oplus.software.radio.ims_guard";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INEXACT_ALARM_SUPPORT = "oplus.software.power.inexact_alarm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INIT_TMOBILE_PROPERTY = "oplus.software.radio.init_tmobile_property";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    @Deprecated
    public static final String FEATURE_INPUTMETHOD_BAIDU_DEFAULT = "oplus.software.inputmethod.baidu.default";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INPUTMETHOD_CN = "oplus.software.inputmethod.cn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    @Deprecated
    public static final String FEATURE_INPUTMETHOD_EMOJI_DEFAULT = "oplus.software.inputmethod.emoji.default";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    @Deprecated
    public static final String FEATURE_INPUTMETHOD_GBOARD_DEFAULT = "oplus.software.inputmethod.gboard.default";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    @Deprecated
    public static final String FEATURE_INPUTMETHOD_SOGOU_DEFAULT = "oplus.software.inputmethod.sogou.default";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INPUTMETHOD_UNRAISED = "oplus.software.inputmethod.unraised";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INPUT_DELOAD_SUPPORT = "oplus.software.input.game.deloading.enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INTELLIGENT_OPLUS_DUAL_SENSOR_SUPPORT = "oplus.software.display.dual_sensor_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INTELLIGENT_OPLUS_TEMPERATURE_SUPPORT = "oplus.software.display.intelligent_color_temperature_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INTELLIGENT_OPLUS_TEMPERATURE_SUPPORT_2_0 = "oplus.software.display.intelligent_color_temperature_2.0_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_INTERNAL_RECORD_SUPPORT = "oplus.software.audio.internal_record_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_IRIS_EDR_SUPPORT = "oplus.software.display.game.hdr2.0_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_IR_INFLUENCE_AVIOD = "oplus.software.ir_aviod";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_JOB_CUSTOMIZEFUNC_SUPPORT = "oplus.software.power.jobscheduler";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_JP_SIGNAL_STRENGTH = "oplus.software.radio.jp_signal_strength";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KARAOKE_V2_PITCHCHANGE = "oplus.software.audio.karaoke_v2.pitchchange";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KARAOKE_V2_SUPPORT = "oplus.software.audio.karaoke_v2.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KEEPALIVE = "oplus.software.keep_alive";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KEEPALIVE_EXT = "oplus.software.keep_alive_ext";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KEEP_GAME_SCREEN_ON_DURING_CALL_SUPPORT = "oplus.software.game.keep_screen_on_during_call";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KEYBOARD_RAISE = "oplus.software.country.keyboard_raise";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KEY_QUICKOPERATE_TORCH = "oplus.software.key_quickoperate_torch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KEY_SUPPORT_FIFO = "oplus.software.notification_alert_support_fifo";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_KEY_VIBRATION_RINGTONE = "oplus.software.vibration_ringtone";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LAB_TEST_MODE = "oplus.software.radio.lab_test_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LHDR_ONLY_DIMMING_SUPPORT = "oplus.software.display.lhdr_only_dimming_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LIBDIAG_VENDOR_SUPPORT = "oplus.software.radio.libdiag_vendor_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LIGHT_SMART5G_ENABLED = "oplus.software.radio.light_smart5g_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LISTEN_STATE_FROM_PLAYSTORE = "oplus.software.listen_state_from_playstore";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LIST_OPTIMIZE = "oplus.software.list_optimize";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LMVIBRATOR_SUPPORT = "oplus.software.vibrator_lmvibrator";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LOCAL_HBM_SUPPORT = "oplus.software.display.local_hbm_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LOCAL_RELEASE = "oplus.software.radio.local_release";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LOC_CONTROL_SWITCH = "oplus.software.radio.loc_control_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LOGKIT_HIDE_DESKTOP_ICON = "oplus.software.logkit.hide_desktop_icon";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LOG_GAME_ERR = "oplus.software.radio.game_err";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LOG_GAME_PAGING = "oplus.software.radio.game_paging";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LOW_FRAMERATE_PANEL_EXPENED = "oplus.software.low_framerate_panel_expened";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LOW_LATENCY_MODE_DE_3 = "oplus.software.radio.low_latency_mode_de3";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LTE_CAPABILITY = "oplus.software.radio.lte_ca_capbility";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LTE_INSTEND_OF_CA = "oplus.software.radio.lte_instend_of_ca";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_LTWCONNECT_DISABLED = "oplus.software.ltw.disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MAX_DEFAULT_VOLUME_SUPPORT = "oplus.software.audio.max_default_volume";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MAX_THREE_PDNS = "oplus.software.radio.max_three_pdns";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    @Deprecated
    public static final String FEATURE_MEETING_OPITMIZ = "oplus.software.radio.online_meeting";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MEMCGAME_INCREASE_FPS_LIMIT_SUPPORT = "oplus.software.display.game.memc_increase_fps_limit_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MEMCGAME_INCREASE_FPS_SUPPORT = "oplus.software.display.game.memc_increase_fps_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MEMCGAME_OPTIMISE_POWER_SUPPORT = "oplus.software.display.game.memc_optimise_power_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MEMCGAME_SUPPORT = "oplus.software.display.game.memc_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MEMC_SUPPORT = "oplus.software.display.memc_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.PERSIST_FEATURE)
    @OplusFeaturePermission(OplusTelephonyIntents.OPLUS_SAFE_SECURITY_PERMISSION)
    public static final String FEATURE_MIDAS_DISABLE = "oplus.software.midas.disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MIPC_CLEAR_FTAI = "oplus.software.radio.mipc_clear_ftai";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MIPC_UNBLOCK_PLMN = "oplus.software.radio.mipc_unblock_plmn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MIPI_OSC_FREQ_HOP_SUPPORT = "oplus.software.radio.mipi_osc_freq_hop_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MIX_SIM_NAME_LEN_DEFAULT_TEN = "oplus.software.radio.mix_sim_name_len_default_ten";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MMS_NOT_ALLOW_INTERNET = "oplus.software.radio.mms_not_allow_internet";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MOTOR_BACKCAMERA = "oplus.software.motor.backcamera";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MOTOR_BACKFLASH = "oplus.software.motor.backflash";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MOTOR_BREATHLED = "oplus.software.motor.breathled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MOTOR_DAT_ENABLED = "oplus.software.radio.motor_dat_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MOTOR_SUPPORT = "oplus.software.motor_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MTKOPLUSMODE_SUPPORT = "oplus.software.display.mtkcolormode_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MTK_HIGH_PRIORITY_QUEUE = "oplus.software.radio.mtk_high_priority_queue";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTIAPP_ERROR_MONITOR = "oplus.software.multiapp.error_monitor";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTIBITS_DIMMING_SUPPORT = "oplus.software.display.multibits_dimming_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_APP_DISABLED = "oplus.software.multi_app_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_APP_MAX_OPEN_NUMBER_LIMITED = "oplus.software.multiapp_max_open_number_limited";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_APP_SUPPORT_LESS = "oplus.software.multiapp_support_less";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_APP_SUPPORT_RLM = "oplus.software.multiapp_support_rlm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_APP_SUPPORT_RLM_EXT = "oplus.software.multiapp_support_rlm_ext";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_APP_VOLUME_ADJUST_SUPPORT = "oplus.software.multi_app.volume.adjust.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_DEVICE_DECREASE_REFRESH_RATE_SUPPORT = "oplus.software.display.multi_device_decrease_refresh_rate";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_LED_SUPPORT = "oplus.software.display.multi_led_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_USER_CLONE_PREINSTALLED_APPS = "oplus.software.multiuser_clone_preinstalled_apps";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_MULTI_USER_ENTRY_DISABLED = "oplus.software.multiuser_entry_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NETWORK_CYBERSENSE_LOCATION_ENABLED = "oplus.software.radio.cybersense_location_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NETWORK_CYBERSENSE_RESIDENCE_LOC_ENABLED = "oplus.software.radio.cybersense_residence_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NETWORK_FENCE = "oplus.software.radio.enable_network_fence";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NETWORK_QOS_PREDICTION_ENABLED = "oplus.software.radio.qos_prediction_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NEW_FEATURES_DISPLAYING = "oplus.software.new_features_displaying";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NEW_POLICIES_DISPLAYING = "oplus.software.new_policies_displaying";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NEXT_PRELOAD = "oplus.software.next_preload";

    @OplusFeature(OplusFeature.OplusFeatureType.PERSIST_FEATURE)
    @OplusFeaturePermission("com.oplus.permission.OPLUS_FEATURE")
    public static final String FEATURE_NFC_AID_OVERFLOW = "oplus.software.nfc.over_flow";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NFC_FELICA_SUPPORT = "oplus.software.nfc.felica_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NONE_DEVICE = "oplus.software.carrier.none_device";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NON_DDS_PDN_DISCONN = "oplus.software.radio.non_dds_pdn_diconnect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NON_STS_SIM_LOCK = "oplus.software.radio.non_sts_sim_lock";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOPROWERBLOCK_FACE = "oplus.software.face_nopowerblock";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOTIFICATION_CUSTOM_VERSION = "oplus.software.notification_custom_version";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOTIFY_DATA_CONNECTION = "oplus.software.radio.notify_data_connection";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOT_CHANGE_VIBRATION_IN_JP_REGION = "oplus.software.notification.is_version_for_japan_region";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOT_GRANT_CONTACTS = "oplus.software.disable_grant_calendar_contacts_permissions";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOT_SUPPORT_N28 = "oplus.software.radio.not_support_n28";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOT_SUPPORT_NETWORK_AUTO_CHANGE = "oplus.software.wlan.not_support_network_auto_change";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOT_SUPPORT_NRCA = "oplus.software.radio.not_support_nrca";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NOT_UNINSTALL_COMPANY_PACKAGES = "oplus.software.not_uninstall_company_package";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NO_BAND_SELECTIONS = "oplus.software.radio.no_band_selections";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NR_ALWAYS_SA_PRE = "oplus.software.radio.nr_always_sa_pre";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NR_BWP_RUS_CONTROL = "oplus.software.radio.nr_bwp_rus_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NR_ICON_OPTIMIZED_ENABLED = "oplus.software.radio.nr_icon_optimized_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NR_NRCA_RUS_CONTROL = "oplus.software.radio.nr_nrca_rus_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NSA_CALL_DISABLE_ENDC = "oplus.software.radio.nsa_call_disable_endc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NTP_PREFERRED = "oplus.software.radio.ntp_preferred";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NV_RECOVERY_UI_OFF = "oplus.software.radio.nv_recovery_ui_off";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_AMC = "oplus.software.radio.nwpower_amc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_APP_NETWORK_MONITOR_SWITCH_ENABLE = "oplus.software.radio.nwpower_app_network_monitor_switch_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_BG_APP_NET_CONTROL_SWITCH_ENABLE = "oplus.software.radio.nwpower_bg_app_net_control_switch_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_HEARTBEAT = "oplus.software.radio.nwpower_heartbeat";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_LINKPOWER = "oplus.software.radio.nwpower_linkpower";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_NETWORK_DISABLE_WHITE_LIST = "oplus.software.radio.nwpower_network_disable_white_list";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_POWER_RECOVERY_SWITCH_ENABLE = "oplus.software.radio.nwpower_power_recovery_switch_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_POWER_SUPPRESE_SIGNAL_REPORT_ENABLE = "oplus.software.radio.nwpower_supprese_signal_report_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_PROC_NET_CONTROL_SWITCH_ENABLE = "oplus.software.radio.nwpower_proc_net_control_switch_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_SCREENOFF_APP_NET_CONTROL_SWITCH_ENABLE = "oplus.software.radio.nwpower_screenoff_app_net_control_switch_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NWPOWER_SYS_NET_CONTROL_SWITCH_ENABLE = "oplus.software.radio.nwpower_sys_net_control_switch_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_BACKOFF_RECOVERY_DISABLED = "oplus.software.radio.nw_backoff_recovery_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_DATA_EVAL = "oplus.software.radio.data_eval";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_DATA_EVAL3 = "oplus.software.radio.data_eval3";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_DATA_EVAL4 = "oplus.software.radio.data_eval4";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_DATA_FAST_DORMANCY_DISABLED = "oplus.software.radio.data_fast_dormancy_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_DATA_FAST_RECOVERY = "oplus.software.radio.data_fast_recovery";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_DATA_LIMIT = "oplus.software.radio.data_limit";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_DATA_SMART_DATA_EVAL = "oplus.software.radio.smart_data_eval";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_DATA_STUCK_ANALYSE = "oplus.software.radio.stuck_analyse";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_NW_LOW_LATENCY = "oplus.software.radio.low_latency";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OFFHOTSPOT_SIMREFRESH = "oplus.software.radio.offhotspot_simrefresh";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OGUARD_ENABLE = "oplus.software.oguard.enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OHA_SUPPORT = "oplus.software.display.oha_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OMCF_MBN_UPGRADE = "oplus.software.radio.omcf_mbn_upgrade";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ONESHOT_SUPPORT = "oplus.software.speechassist.oneshot.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ONLY_GET_SIM_MSISDN = "oplus.software.radio.only_get_sim_msisdn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPENID_SUPPORT_OLD = "oplus.software.openid.support_old_project";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_CONNECTIVITY_BASIC_SWITCH = "oplus.software.connectivity.basic_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_COTA_COTA = "oplus.software.operator_service.cota_cota";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_SFR_FR = "oplus.software.operator_service.sfr_fr";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_THREAD_DISABLE = "oplus.software.operator.disable_domestic";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_TM_DE = "oplus.software.operator_service.tmobile_de";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_TM_NL = "oplus.software.operator_service.tmobile_nl";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_TM_PL = "oplus.software.operator_service.tmobile_pl";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_TM_RO = "oplus.software.operator_service.tmobile_ro";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_VF_DE = "oplus.software.operator_service.vodafone_de";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_VF_ES = "oplus.software.operator_service.vodafone_es";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_VF_GB = "oplus.software.operator_service.vodafone_gb";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_VF_IT = "oplus.software.operator_service.vodafone_it";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPERATOR_SERVICE_VF_PT = "oplus.software.operator_service.vodafone_pt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUSMODE_SUPPORT = "oplus.software.display.colormode_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUSPOWERSAVINGMODE_SUPPORT = "oplus.software.display.colormode_powersaving_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_BLUETOOTH_VOL_SYNC = "oplus.software.bluetooth_volume_sync";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_EMBEDDING = "oplus.software.display.oplus_activity_embedding";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_FIVE_STAGE_COLOR_TEMPERATURE_SUPPORT = "oplus.software.display.five_stage_color_temperature";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_LOCK_COLOR_TEMPERATURE_SUPPORT = "oplus.software.display.lock_color_temperature_in_drag_brightness_bar_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_MAINLINE_PREODEX_ENABLE = "oplus.software.pms_mainline_preodex_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_MEDIA_CONTROL = "oplus.software.audio.media_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_OTADEXOPT_DISABLED = "oplus.software.pms_oplus_otadexopt_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_PASSPPOINT_OFF = "oplus.software.wlan.opluspasspoint.off";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_PRIVACY_CALL_SUPPORT = "oplus.hardware.audio.dipole_speaker_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_QUICK_REPLY = "oplus.software.oplus_quick_reply";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_SPATIALIZER_SPEAKER = "oplus.software.spatializer_speaker";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_VOCAL_PROMINENCE_SUPPORT = "oplus.hardware.audio.voice_isolation_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPLUS_VOICE_ISOLATION_SUPPORT = "oplus.hardware.audio.voice_isolation_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OPTICAL_FINGERPRINT = "oplus.software.fingeprint_optical_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OP_SHARE_SHEET = "oplus.software.op_share_sheet";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OP_UXICON_EXP = "oplus.software.op_uxicon_exp";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ORIGINAL_SHUTDOWN_ANIMATION = "oplus.software.original.shutdown_animation";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ORIGIN_OTADEXOPT_ENABLE = "oplus.software.pms_origin_otadexopt_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ORIGIN_ROUNDCORNER_SUPPORT = "oplus.software.display.origin_roundcorner_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OSIE_AIPQ_SUPPORT = "oplus.software.display.osie_aipq_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OSIE_AISDR2HDR_SUPPORT = "oplus.software.display.osie_aisdr2hdr_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_OSIE_SUPPORT = "oplus.software.video.osie_support";

    @OplusFeature(OplusFeature.OplusFeatureType.MEMORY_FEATURE)
    @OplusFeaturePermission("com.oplus.permission.safe.UPDATE")
    public static final String FEATURE_OTA_UPDATE_RESULT = "oplus.software.deal_update_result";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PADCONNECT_SUPPORT = "oplus.software.padconnect.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PARALLEL_PRELOAD = "oplus.software.parallel_preload";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PA_AGING_FOR_LTE_B41_110M = "oplus.software.radio.pa_aging_for_lte_b41_110m";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PA_AGING_FOR_LTE_B41_120M = "oplus.software.radio.pa_aging_for_lte_b41_120m";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PA_AGING_FOR_LTE_B41_160M = "oplus.software.radio.pa_aging_for_lte_b41_160m";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PA_AGING_FOR_LTE_B41_LEGACY = "oplus.software.radio.pa_aging_for_lte_b41_legacy";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PA_AGING_FOR_N41_CHANNEL = "oplus.software.radio.pa_aging_for_n41_channel";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PA_AGING_FOR_NR_N41_160M = "oplus.software.radio.pa_aging_for_nr_n41_160m";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PCCONNECT_SUPPORT = "oplus.software.pcconnect.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PCTELEPHONE_SUPPORT = "oplus.software.pctelephone.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PEN_TEST_NOT_SUPPORT = "oplus.hardware.pen.not_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERCEPTIBLE_APP_KEEP_ALIVE = "oplus.software.perceptible_app_keep_alive";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERFORMANCE_SETTING_EXTENSION = "oplus.software.performance_setting_extension";
    public static final String FEATURE_PERMISSION_ACCOUNT_DIALOG_DISABLED = "oplus.software.permission_account_dialog_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERMISSION_ACTIVITY_START_MANAGER = "oplus.software.activity_start_manager";
    public static final String FEATURE_PERMISSION_BACKGROUND_REJECT_DISABLED = "oplus.software.permission_background_reject_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERMISSION_INTERCEPT = "oplus.software.permission_intercept_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERMISSION_INTERCEPT_LOCKSCREEN = "oplus.software.intercept_lockscreen_service";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERMISSION_PARSE = "oplus.software.permission_product_xml_parse";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERMISSION_STEALTH_SECURITY_MODE = "oplus.software.stealth_security_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERMISSION_VELOCITYTRACKER_STRATEGY_SUPPORT = "oplus.velocitytracker.strategy.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PERSONAL_ASSIST_ENABLE = "oplus.software.personal_assist_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PIXELWORKS_SUPPORT = "oplus.software.display.pixelworks_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PIXELWORKS_X5_SUPPORT = "oplus.software.display.pixelworks_x5_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PIXELWORKS_X7P_SUPPORT = "oplus.software.display.pixelworks_x7p_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PIXELWORKS_X7_SUPPORT = "oplus.software.display.pixelworks_x7_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PMS_ADB_SESSION_INSTALLER_INTERCEPT = "oplus.software.pms_adb_session_installer_intercept";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PMS_APP_FROZEN = "oplus.software.pms_app_frozen";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PMS_APP_LIST_INTERCEPT = "oplus.software.pms_app_list_intercept";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PMS_DEVICE_OWNER_UNINSTALL_POLICY = "oplus.software.pms_device_owner_uninstall_policy";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PMS_GP_VERIFICATION_DISABLED = "oplus.software.pms_gp_verification_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PMS_HIDE_LAUNCHER_ENABLE = "oplus.software.pms_hide_launcher_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PMS_INSTALL_PERMISSION_AUTOALLOWED = "oplus.software.install_permission_autoallowed";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PMS_INSTALL_STATISTICS_NO_SILENT = "oplus.software.pms_install_statistics_no_silent";

    @OplusFeature(OplusFeature.OplusFeatureType.PERSIST_FEATURE)
    public static final String FEATURE_PMS_SELLMODE = "oplus.software.pms_sellmode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_POGO_PIN_TEST_NOT_SUPPORT = "oplus.hardware.pogo_pin.not_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_POLICY_SALEMODE_DISABLED = "oplus.software.radio.policy_salemode_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_POWER_BUTTON_REDEFINE = "oplus.software.power_button_redefine";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_POWER_SAVE_DUAL_NETWORK_CONTROL = "oplus.software.radio.power_save_dual_network_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PREFERRED_APN_AS_INIT = "oplus.software.radio.preferred_apn_as_init";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PRELOAD_SPLAHS = "oplus.software.preload_splash";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PRESS_AREA_TAKESCREEN = "oplus.software.input.press_area_takescreen";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PROCESS_REGION_CHANGE = "oplus.software.radio.process_change_region";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PRODUCT_OH_LIGHT = "oplus.software.product.oh_light";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PULL_WIFI_CELL = "oplus.software.radio.pull_wifi_cell";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PUT_CARD_TYPE_TO_DB = "oplus.software.radio.put_card_type_to_db";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_PWMBACKLIGHT_SUPPORT = "oplus.software.display.pwm_switch.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QCOM_LMVIBRATOR_SUPPORT = "oplus.software.vibrator_qcom_lmvibrator";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QCOM_PIXELWORKS_X5_SUPPORT = "oplus.software.display.qcom_pixelworks_x5_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QCT_SMART_DATA_FLOW = "oplus.software.radio.qct_smart_data_flow";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QC_GSM_FBRX_NOT_CAL = "oplus.software.radio.gsm_fbrx_not_cal";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QC_NEW_PA_AGING_SUPPORT = "oplus.software.radio.new_pa_aging_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QC_PA_AGING_FOR_LTE_B28B = "oplus.software.radio.pa_aging_for_lte_b28b";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QC_PA_AGING_FOR_LTE_B41B = "oplus.software.radio.pa_aging_for_lte_b41b";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QC_PA_AGING_FOR_NR_B28B = "oplus.software.radio.pa_aging_for_nr_b28b";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QC_PA_AGING_SKIP_TX_TEST = "oplus.software.radio.pa_aging_skip_tx_test";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QC_PA_AGING_USE_FTM_NS = "oplus.software.radio.pa_aging_use_ftm_ns";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QOE_NETWORK_RECOVERY = "oplus.software.radio.qoe_network_recovery";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QR_RECOVERY = "oplus.software.radio.qr_recovery";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_QUICK_BOOT = "oplus.software.quick_boot";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_APMDMLOGPB_SUPPORT = "oplus.software.radio.apmdm_log_pb";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_ENABLE_SIM_DETECT = "oplus.software.radio.enable_sim_detect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_ENGNW_NVBACKUPUI_STS_INTERFACE = "oplus.software.radio.nvbackupui_sts_interface";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_ENGNW_STS_INTERFACE_SUPPORT = "oplus.software.radio.engnw_sts_interface_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_HEALTH_SERVICE_SUPPORT = "oplus.software.radio.health_service_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_LCMHOPPING_URC = "oplus.software.radio.lcm_hopping_urc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_NHS_RESTRICTED_DISABLED = "oplus.software.radio.nhs_disabled_restricted";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_OCLOUD_SUPPORT = "oplus.software.logkit.ocloud.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_SAR_VIA_STS_SUPPORT = "oplus.software.radio.sar_via_sts_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_STS_NO_SUPPORT_ENG = "oplus.software.radio.engnw_no_sts_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_VENDOR_DIAG_SUPPORT = "oplus.software.radio.vendor_diag";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_VIRTUALMODEM = "oplus.software.radio.virtualmodem_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_VIRTUALMODEM_ACCOUNTSKIP = "oplus.software.radio.vcomm_account_skip";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RADIO_VIRTUALMODEM_CONFERENCE = "oplus.software.radio.virtualmodem_conference";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RAT_BAND_SELECTIONS = "oplus.software.radio.rat_band_selections";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REALME_DISABLE_STARTUP_MANAGER = "oplus.software.disable_startup_manager";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REALME_ENABLE_STARTUP_MANAGER = "oplus.software.enable_startup_manager";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REAR_BREATHE_LIGHT_RM = "oplus.misc.lights.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RECORD_CHANNEL_SUPPORT = "oplus.software.audio.record_channel_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RECOVERY_NRMODE = "oplus.software.radio.recovery_nrmode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REDUCE_BRIGHTNESS_DELAY = "oplus.software.display.reduce_brightness_delay";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REDUCE_BRIGHTNESS_MANUAL = "oplus.software.display.reduce_brightness_manual";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REDUCE_BRIGHTNESS_RM = "oplus.software.display.reduce_brightness_rm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REDUCE_BRIGHTNESS_RM_MANUAL = "oplus.software.display.reduce_brightness_rm_manual";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REDUCE_TASK_SNAPSHOT = "oplus.software.reduce.task_snapshot";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REDUCE_TEMPERATURE_RM = "oplus.software.display.temperature_rm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REFRESHRATE_DEFAULT_SMART = "oplus.software.display.refreshrate_default_smart";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REFRESHRATE_DEFAULT_SMART_NOTIFICATION = "oplus.software.display.refreshrate_default_smart_notification";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REFRESHRATE_OVERRIDE_DISABLE = "oplus.software.display.disable_refreshrate_default_override";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REFRESHRATE_OVERRIDE_SUPPORT = "oplus.software.display.refreshrate_default_override";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REMOVE_ABNORMAL_SYNCJOB = "oplus.software.remove_abnormal_syncjob";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RESOLUTION_SWITCH = "oplus.software.display.resolution_switch_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RESOLUTION_SWITCH_DISABLEAUTO = "oplus.software.display.resolution_switch_disableauto_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RESOLUTION_SWITCH_HD = "oplus.software.display.1.5k_resolution_switch_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RESOLUTION_SWITCH_WM = "oplus.software.display.wm_size_resolution_switch_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RESOLVER_SHARE_EMAIL = "oplus.software.resolver_share_email";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RESOURCES_CACHE_APKASSET = "oplus.software.resources.cache_apkasset";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RETRY_OPEN_CHANNEL_FOR_TW = "oplus.software.radio.retry_open_channel";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REVISE_RECTANGLE_DISPLAY_ORIENTATION = "oplus.software.revise_rectangle_display_orientation";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_REVISE_SQUARE_DISPLAY_ORIENTATION = "oplus.software.revise_square_display_orientation";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RF_BAR_CELL = "oplus.software.radio.bar_cell";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RGB_BALL_SUPPORT = "oplus.software.display.rgb_ball_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RGB_NORMALIZE = "oplus.software.view.rgbnormalize";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RHYTHM_OPLUS_TEMPERATURE_HEALTH_SUPPORT = "oplus.software.display.smart_color_temperature_rhythm_health_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RICHTAP_SUPPORT = "oplus.software.vibrator_richctap";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RINGER_TOGGLE_CHORD_DISABLE = "oplus.software.ringer_toggle_chord_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RINGTONE_HAPTIC_CHANNEL = "oplus.software.audio.haptic_channel_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RING_LIGHTS_CONTROL_SUPPORT = "oplus.ring_lights_control.misc.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RM_BATTERY_CURVE = "oplus.software.rm_battery_curve";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_RSU_SIM_LOCK = "oplus.software.radio.rsu_sim_lock";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SALEPATEST_SUPPORT = "oplus.software.radio.eng_salepatest_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SAR_AIRINTERFACE_DETECT_NOTSUPPORT = "oplus.software.radio.sar.airinterface.detect.notsupport";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SAR_BACKOFF_DISABLED = "oplus.software.radio.sar_backoff_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SAR_BT_DETECT_SUPPORT = "oplus.software.radio.sar.bluetooth.detect.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SAR_EARPIECE_DETECT_SUPPORT = "oplus.software.radio.sar.earpiece.detect.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SAR_ENABLE = "oplus.software.radio.sar.sar_sensor_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SAR_FCC_DETECT_SUPPORT = "oplus.software.radio.sar.fcc.detect.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SAU_PRELOAD = "oplus.software.sau_preload";

    @OplusFeature(OplusFeature.OplusFeatureType.MEMORY_FEATURE)
    @OplusFeaturePermission("com.oplus.permission.safe.SAU")
    public static final String FEATURE_SAU_UPDATE_RESULT = "oplus.software.deal_sau_update_result";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_APM_CONTROL = "oplus.software.radio.sa_apm_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_BACKOFF_SA_CAUSE_NETWORK_REJECT = "oplus.software.radio.backoff_sa_cause_network_reject";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_BAND_CFG = "oplus.software.radio.sa_band_cfg";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_CALL_BACK0FF_CONTROL = "oplus.software.radio.sa_call_backoff_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_CALL_CONTROL_DISABLED = "oplus.software.radio.sa_call_control_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_DATA_CALL_CONTROL_DISABLED = "oplus.software.radio.data_call_control_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_DATA_RECOVERY = "oplus.software.radio.sa_data_recovery_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_IMS_CONTROL_DISABLED = "oplus.software.radio.sa_ims_control_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_MODE_ROAMING_CONTROL = "oplus.software.radio.sa_mode_roaming_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_PINGPONG_CONTROL_DISABLED = "oplus.software.radio.sa_pingpong_control_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_RAT_CONTROL = "oplus.software.radio.sa_rat_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_REG_TIMEOUT_CONTROL_ENABLED = "oplus.software.radio.sa_reg_timeout_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_SCREEN_OFF_DISABLE_SA_ENABLED = "oplus.software.radio.screen_off_disable_sa_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_TAC_CONTROL = "oplus.software.radio.sa_tac_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SA_TIME_CONTROL_DISABLED = "oplus.software.radio.sa_time_control_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    @Deprecated
    public static final String FEATURE_SCENE_MODE = "oplus.software.radio.scene_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_CALIBRATEAPL_SUPPORT = "oplus.software.display.screen_calibrate_100apl";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_DEFAULT_SMART_MODE_SUPPORT = "oplus.software.display.screen_defaultsmartmode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_GLOABLEHBM_SUPPORT = "oplus.software.display.screen_gloablehbm_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_HETEROMORPHISM = "oplus.software.display.screen_heteromorphism";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_NO_HBM_IN_MANUAL_MODE_SUPPORT = "oplus.software.display.no_hbm_in_manual_mode_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_OFF_NEVER_SUPPORT = "oplus.software.screen_off_never_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_PARTHBM_SUPPORT = "oplus.software.display.screen_parthbm_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_PEAK_BRIGHTNESS_SUPPORT = "oplus.software.display.peak_brightness_test_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SCREEN_SELECT = "oplus.software.display.screen_select";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SECONDARY_PROXIMITY_ENABLE = "oplus.software.display.secondary_proximity_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SECOND_MAX_BRIGHTNESS = "oplus.software.display.sec_max_brightness_rm";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SECURITYANALYSIS_SENDBROADCAST = "oplus.software.securityanalysis.sendbroadcast";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SENSOR_FUSIONLIGHT_SUPPORT = "oplus.sensor.fusionlight.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SEPARATE_WALLPAPER_FOR_MULTI_DISPLAY = "oplus.software.wallpaper.separate_wallpaper_for_multi_display";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SET_DEFAUT_MONTH_FOR_FIRST_DATE = "oplus.software.time.set_default_month";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SET_POL_ENTRY_COMPLETE_WITH_LAST_ITEM = "oplus.software.radio.set_pol_entry_complete_with_last_item";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SET_RAT_NO_DELAY = "oplus.software.radio.set_pref_rat_no_delay";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SET_SA_SILENCE = "oplus.software.radio.set_sa_silence";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SET_WIFI_SAR_ENABLED = "oplus.software.radio.set_wifi_sar_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SHARPNESS_SUPPORT = "oplus.software.display.sharpness_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SHOULDER_KEY = "oplus.software.shoulderkey_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SHOW_CONFUSED_HARDWARE_INFO = "oplus.software.show_confused_hardwareinfo";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SHOW_ESIM_ENGINEERMODE_ENABLED = "oplus.software.radio.esim_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SHOW_HD_PLUS = "oplus.software.radio.show_hd_plus";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SHOW_NOTIFICATION_TOAST = "oplus.software.notification.show_toast";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SHOW_NR5G_MODE = "oplus.software.radio.show_5g_nr_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SHUTDOWN_ANIMATION = "oplus.software.shutdown_animation";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDE_FINGERPRINT = "oplus.software.side_fingerprint";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_AIRPLANE_MODE_LOW_POWER_OPT = "oplus.software.radio.sido_airplane_mode_low_power_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_CELL_STRONG_INTERFERENCE_OPT = "oplus.software.radio.sido_cell_strong_interference_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_DISABLE = "oplus.software.radio.sido_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_DUAL_SIM_COOP_NO4G_NO5G_OPT = "oplus.software.radio.sido_dual_sim_coop_no4g_no5g_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_DUAL_SIM_COOP_NO_SERVICE_OPT = "oplus.software.radio.sido_dual_sim_coop_no_service_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_ELEVATOR_5G_OPT = "oplus.software.radio.sido_elevator_5g_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_ENABLE_SA_BACKOFF_ROLLBACK = "oplus.software.radio.enable_sa_backoff_rollback";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_GW_LTE_PINGPONG_LTE_POOR_SIGNAL_OPT = "oplus.software.radio.sido_gw_lte_pingpong_lte_poor_signal_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_GW_LTE_PINGPONG_LTE_STRONG_SIGNAL_OPT = "oplus.software.radio.sido_gw_lte_pingpong_lte_strong_signal_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_INTER_CELL_COOP_DUAL_CELL_STRONG_SIG_OPT = "oplus.software.radio.sido_inter_cell_coop_dual_cell_strong_sig_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_INTER_CELL_COOP_LTE_REJ_15_OPT = "oplus.software.radio.sido_inter_cell_coop_lte_rej_15_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_LTE_CELL_PINGPONG_OPT = "oplus.software.radio.sido_lte_cell_pingpong_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_LTE_FAST_RETURN_TO_NR_OPT = "oplus.software.radio.sido_lte_fast_return_to_nr_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_LTE_NR_PINGPONG_MITIGATION = "oplus.software.radio.sido_lte_nr_pingpong_mitigation";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_LTE_NR_PINGPONG_RSRP_PREFER_OPT = "oplus.software.radio.sido_lte_nr_pingpong_rsrp_prefer_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_LTE_SRV_POOR_SIGNAL_NGBR_STRONG_OPT = "oplus.software.radio.sido_lte_srv_poor_signal_ngbr_strong_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_MMR_REG_REQ_STUCK_OPT = "oplus.software.radio.sido_mmr_reg_req_stuck_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_NR_ICON_PINGPOING_OPT = "oplus.software.radio.sido_nr_icon_pingpong_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_RAT_ENHANCE_LTE_BACKOFF_OPT = "oplus.software.radio.sido_rat_enhance_lte_backoff_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_RAT_ENHANCE_NR_BACKOFF_OPT = "oplus.software.radio.sido_rat_enhance_nr_backoff_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_RF_DSDA_PREFER_OPT = "oplus.software.radio.sido_rf_dsda_prefer_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_RF_IDC_AVOIDANCE_OPT = "oplus.software.radio.sido_rf_idc_avoidance_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_ROAMING_FAST_SEARCH_OPT = "oplus.software.radio.sido_roaming_fast_search_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_SIM_RECOVERY_OPT = "oplus.software.radio.sido_sim_recovery_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_SIM_TAG_OPT = "oplus.software.radio.sido_sim_tag_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_SS_UPDATE_ABNORMAL_OPT = "oplus.software.radio.sido_ss_update_abnormal_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIDO_STAMP_DB = "oplus.software.radio.sido_stamp_db";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIGNAL_MAP = "oplus.software.radio.enable_signal_map";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIGNAL_RECOVERY = "oplus.software.radio.signal_recovery_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIGNAL_SMOOTH_DISABLED = "oplus.software.radio.signal_smooth_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.PERSIST_FEATURE)
    public static final String FEATURE_SILENT_REDIAL_ENABLED = "oplus.software.radio.silent_redial_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SILENT_REDIAL_SUPPORTED = "oplus.software.radio.silent_redial_supported";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM2_DETECT_NOT_SUPPORT = "oplus.software.radio.sim2_detect_not_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_CONTACTS_SUPPORT = "oplus.software.radio.sim_contacts_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_DETECT_STATUS_CHECK_SUPPORT = "oplus.software.radio.sim_detect_check_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_NAME_LENGTH_DEFAULT_TEN = "oplus.software.radio.sim_name_length_default_ten";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_OVERDUE_OPT = "oplus.software.radio.sim_overdue_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_CURRENT_REMOVE_PRE_APK = "oplus.software.carrier.sim_switch_current_remove_prev_apk";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_FIRST_EVERYTIME = "oplus.software.carrier.sim_switch_first_everytime";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_FIRST_ONETIME = "oplus.software.carrier.sim_switch_first_onetime";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_FIRST_ONLY_FIRST = "oplus.software.carrier.sim_switch_first_only_first";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_FIRST_REMOVE_PRE_APK = "oplus.software.carrier.sim_switch_first_remove_prev_apk";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_FORCE_REBOOT = "oplus.software.carrier.sim_switch_force_reboot";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_NOTIFY_REBOOT = "oplus.software.carrier.sim_switch_notify_reboot";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_WAIT_DATA_SLOT = "oplus.software.carrier.sim_switch_first_wait_data_slot";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_WAIT_MAIN_SLOT = "oplus.software.carrier.sim_switch_first_wait_main_slot";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIM_SWITCH_WAIT_PHYSIAL_SLOT = "oplus.software.carrier.sim_switch_first_wait_physial_slot";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SINGLEHAND_MODE = "oplus.software.singlehand_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SINGLE_PULSE_REFRESHRATE_144_NOT_SUPPORT = "oplus.software.display.single_pulse_refreshrate_144_not_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIP491_RESUME_FAILURE_DISABLED = "oplus.software.radio.sip491_resume_failure_disalbed";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SIX_SECTIONS_RM = "oplus.software.display.color_temperature_six_sections_rm_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SKIP_ALL_APPLICATIONS_READY = "oplus.software.radio.skip_all_applications_ready";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SKIP_CHECK_LOCKASSISTANT = "oplus.software.skip_check_lockassistant";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_EXP_CFG = "oplus.software.radio.smart5g_exp_cfg";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_FENCE_SA_DATA_SLOW = "oplus.software.radio.smart5g_fence_sa_data_slow_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_FORCE_TX = "oplus.software.radio.smart5g_force_tx";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_GAME_LATENCY_CHECK = "oplus.software.radio.smart5g_game_latency_check";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_GAME_LTE_PREFER = "oplus.software.radio.smart5g_game_lte_prefer";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_GMAE_LIST_DEPRIO_SA = "oplus.software.radio.smart5g_game_list_deprio_sa";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_HOTSPOT_ENABLE = "oplus.software.radio.smart5g_hotspot_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_LOCAL_RELEASE = "oplus.software.radio.smart5g_local_release";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_LOWER_POWEWR_CONTROL = "oplus.software.radio.smart5g_Low_Power_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_NR_RTT_CHECK = "oplus.software.radio.smart5g_nr_rtt_check";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_NR_SIGNAL_CHECK = "oplus.software.radio.smart5g_nr_signal_check";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_NR_S_DROP_CTRL = "oplus.software.radio.smart5g_nrscell_drop_ctrl";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_RTT_CHECK = "oplus.software.radio.smart5g_rtt_check";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_RTT_CHECK_SA = "oplus.software.radio.smart5g_rtt_check_sa";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_RX_FALLBACK = "oplus.software.radio.smart5g_rx_fallback";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_SA_ENABLED = "oplus.software.radio.smart5g_sa_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_SA_ENVIRONMENT_GAME_LTE_PREFER = "oplus.software.radio.smart5g_sa_environment_game_lte_prefer";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_SILENCE_MODE = "oplus.software.radio.smart5g_silence_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_SPEEDBASED_NR_S_DROP = "oplus.software.radio.smart5g_speedbased_nrscell_drop";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_SPEED_CONTROL = "oplus.software.radio.smart5g_speed_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_SP_REGION_DISABLE = "oplus.software.radio.smart5g_sp_region_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_THERMAL_NR_DEPRIO = "oplus.software.radio.smart5g_thermal_nr_deprio";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_THERMAL_NR_S_DROP = "oplus.software.radio.smart5g_thermal_nrscell_drop";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART5G_UL_THROTTLE = "oplus.software.radio.smart5g_ul_throttle";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMARTSA_DEFAULT_OFF_RUS_ON = "oplus.software.radio.smart_sa_off_rus_on";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART_COMMU = "oplus.software.radio.smart_commu";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART_NETWORK_SELECTION = "oplus.software.radio.smart_network_selection";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART_OPLUS_TEMPERATURE_CONTROL_SUPPORT = "oplus.software.display.smart_color_temperature_control_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART_RECOVERY_OFF = "oplus.software.radio.smart_recovery_off";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART_SIDEBAR = "oplus.software.smart_sidebar";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMART_WIFICALLING = "oplus.software.radio.smart_wificalling";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMS_7BIT16BIT = "oplus.software.radio.sms_7bit_16bit";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SMS_FIND_PHONE = "oplus.software.radio.find_phone";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SNAPSHOT_GSA_SUPPORT = "oplus.software.startingwindow.gsa.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SOS_SPECIAL_FUNCTION = "oplus.software.sos_special_function";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SPEECH_ASSIST_FOR_BREENO = "oplus.software.speech_assist_for_breeno";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SPEECH_ASSIST_FOR_GOOGLE = "oplus.software.speech_assist_for_google";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SPEED_CHARGE_MODE = "oplus.software.speed_charge_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SPLICE_NAVIGATION_GESTURE = "oplus.software.splice.navigation_gesture";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SPL_LIMIT_SUPPORT = "oplus.software.audio.spl_limit_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SPN_AS_DISPLAYNAME = "oplus.software.radio.spn.as.displayname";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SPRING_OVER_SCROLLER_SUPPORT = "oplus.software.view.spring_over_scroller";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SRGAME_SUPPORT = "oplus.software.display.game.sr_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SR_SUPPORT = "oplus.software.video.sr_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_STARTUP_STATISTICS_UPLOAD = "oplus.software.startup_statistics_upload";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_STARTUP_STRATEGY_RESTRICT = "oplus.software.startup_strategy_restrict";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_STIR = "oplus.software.radio.stir";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_STREAM_IDENTIFY = "oplus.software.radio.stream_identify";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_STS_AUTO_ANSWER_SUPPORT = "oplus.software.radio.sts_auto_answer_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_STS_CAMERA_SERVICE = "oplus.software.radio.subsystem_camera_service";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_STS_MIPC_SUPPORT = "oplus.software.radio.sts_mipc_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUBINFO_MCCMNC_ADVANCE = "oplus.software.radio.subinfo_mccmnc_advance";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUBWAY_SLOW_RECOVERY = "oplus.software.radio.subway_slow_recovery";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPER_SLEEP = "oplus.software.super_sleep";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPER_VOLUME = "oplus.software.audio.super_volume";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPER_VOLUME_3X = "oplus.software.audio.super_volume_3x";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPER_VOLUME_CALL_EARPIECE = "oplus.software.audio.super_volume_call_earpiece";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPER_VOLUME_CALL_EARPIECE_DISABLE = "oplus.software.audio.super_volume_call_earpiece_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_ABNORMAL = "oplus.software.support_abnormal";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_CHINA_ROAMING = "oplus.software.radio.china_roaming";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_DUAL_NR = "oplus.software.radio.support_dual_nr";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_FCC_SAR_REGION = "oplus.software.radio.support_fcc_sar";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_FLEXIBLE_ACTIVITY = "oplus.software.display.flexible.activity.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_FLUID_ENTRY = "oplus.software.support_fluid_entry";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_PAYJOY = "oplus.software.payjoy_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_R16 = "oplus.software.radio.support_r16";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_SIM_DISPLAY_NAME = "oplus.software.radio.support_sim_display_name";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_SIM_LOCK_STATE = "oplus.software.radio.support_sim_lock_state";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_SN220U = "oplus.software.radio.esim_support_sn220u";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_SNC = "oplus.sncontent.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SUPPORT_VONR_PLUS = "oplus.software.radio.support_vonr_plus";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SURROUND_EFFECT_SUPPORT = "oplus.software.effect.surround_effect_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SURROUND_RECORD_SUPPORT = "oplus.software.video.surround_record_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SVN_UPDATE = "oplus.software.radio.svn_update";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_SWITCH_ECT = "oplus.software.radio.switch_ect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TABLET = "oplus.hardware.type.tablet";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TCP_CONGEST_CONTROL = "oplus.software.radio.tcp_congest_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TCP_SYN_TIMEOUT_TUNING = "oplus.software.radio.tcp_syn_timeout_tuning";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TELEPHONY_DISABLED_KEY_LOG = "oplus.software.radio.disabled_key_log";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TELEPHONY_DISABLED_RUS = "oplus.software.radio.disabled_rus";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_THERMAL_BOOSTV2_SUPPORT = "oplus.software.use_thermal_boostV2";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TP_EDGE_MISTOUCH_PREVENTION = "oplus.software.tp.edge_mistouch_prevention";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TURN_ON_PAYJOY = "oplus.software.payjoy_service";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TVCONNECT_SUPPORT = "oplus.software.tvconnect.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TV_VIDEOCALL_SUPPORT = "oplus.software.tv_videocall.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_TYPEC_OTG_SUPPORT = "oplus.typecotg.connection.menu.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UID_PUR_STATS = "oplus.software.radio.uid_pur_stats";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_ULTIMATE_FIVE_G_SUPPORT = "oplus.software.radio.ultimate_five_g_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UL_PRIORITY_POLICY_FOREGROUND = "oplus.software.radio.ul_priority_policy_foreground";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UL_PRIORITY_POLICY_ONE_VS_ONE = "oplus.software.radio.ul_priority_policy_one_vs_one";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UNDERLIGHTSENSOR_SUPPORT = "oplus.software.display.underlightsensor_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UNKNOWN_SOURCE_APP_INSTALL = "oplus.software.unknown_source_app_install";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UPGRADEUI = "oplus.software.display.eyeprotect_with_upgrade_ui";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USB_ADB_OCAR_CONFIG = "oplus.software.usb.adb_ocar";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USB_SCENES_GAMES_OP_CONFIG = "oplus.software.usb.scenes.optimize_config";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USING_LEGACY_SIMLOCK = "oplus.software.radio.using_legacy_simlock";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USS_DATA_ERROR_MESSAGE = "oplus.software.radio.uss_data_error_message";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USS_DEVICE = "oplus.software.carrier.uss_device";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USS_ROAMING_ALERT = "oplus.software.radio.uss_roaming_alert";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USS_ROAMING_REDUCTION = "oplus.software.radio.uss_roaming_reduction";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USS_ROAMING_SUPPORT = "oplus.software.radio.uss_roaming_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_5G_UC = "oplus.software.carrier.ust_5g_uc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_APP_STATE_BROADCAST = "oplus.software.carrier.ust_app_state_broadcast";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_CARRIER_CONFIG = "oplus.software.radio.ust_carrier_config";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_DATA_ALWAYS_ON = "oplus.software.radio.ust_data_always_on";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_DATA_DEVICE_REPORTING = "oplus.software.radio.ust_data_device_reporting";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_DATA_ROAMING_POPUP = "oplus.software.radio.ust_data_roaming_popup";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_DEVICE = "oplus.software.carrier.ust_device";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_ECHOLOCATE = "oplus.software.radio.ust_echolocate";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_NONE_SIM_ECC = "oplus.software.radio.ust_none_sim_ecc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_SIMLOCK = "oplus.software.radio.ust_simlock";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_SMART_WIFICALLING = "oplus.software.radio.ust_smart_wificalling";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_STIR = "oplus.software.radio.ust_stir";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_TOAST = "oplus.software.radio.ust_toast";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_UNIFY_DEVICE = "oplus.software.carrier.ust_unify_device";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_USKU_DEVICE = "oplus.software.carrier.ust_usku_device";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UST_VT_CONTROLLER = "oplus.software.radio.ust_vt_controller";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USV_EMERGENCY_CALL = "oplus.software.radio.usv_emergency_call";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USV_OMA_APN = "oplus.software.radio.usv_ota_apn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USV_PCO = "oplus.software.radio.usv_pco";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USV_UA = "oplus.software.radio.usv_user_agent";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USV_UWICON = "oplus.software.radio.usv_uw_icon";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_USV_VOWIFI_CONTROLLER = "oplus.software.radio.usv_vowifi_controller";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UXICON_EXP = "oplus.software.uxicon_exp";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_UXICON_WHITE_SWAN_SUPPORT = "oplus.software.uxicon.whiteswan.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VERIFY_CODE_ENABLE = "oplus.software.inputmethod.verify_code_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIBRATION_INTENSITY_IME = "oplus.software.vibration_intensity_ime";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIBRATION_KEYBOARD_CONVERT = "oplus.software.vibration_keyboard_convert";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIBRATOR_FIFO_VIBRATE_SUPPORT = "oplus.software.vibrator_fifo";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIBRATOR_LUXUNVIBRATOR_SUPPORT = "oplus.software.vibrator_luxunvibrator";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIBRATOR_MINIMUMTACTILE_SUPPORT = "oplus.software.vibrator_minimumtactile";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIBRATOR_OPLUSVIBRATOR_INTERFACE_SUPPORT = "oplus.hardware.vibrator_oplus_v1";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIBRATOR_STYLESWITCH_SUPPORT = "oplus.hardware.vibrator_style_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIBRATOR_XLINEAR_SUPPORT = "oplus.hardware.vibrator_xlinear_type";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VICE_CARD_GAME_MODE = "oplus.software.radio.vice_card_game_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIDEO_RM_MEMC_SUPPORT = "oplus.software.video.rm_memc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIEW_EXTRACT = "oplus.software.view.viewextract";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VIP_PRELOAD = "oplus.software.vip_preload";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VOICE_DTMF = "oplus.software.radio.voice_dtmf";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VOICE_WAKEUP_3WORDS_SUPPORT = "oplus.software.audio.voice_wakeup_3words_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VOICE_WAKEUP_SUPPORT = "oplus.software.audio.voice_wakeup_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VONR_CAPABILITY_RECOVERY = "oplus.software.radio.vonr_capability_recovery";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VONR_CITY_CONTROL = "oplus.software.radio.vonr_city_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VONR_SWITCH_ENABLED = "oplus.software.radio.vonr_switch_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VOWIFI_CITY_CONTROL = "oplus.software.radio.vowifi_city_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_VT_SLOWSTART = "oplus.software.radio.vt_slowstart_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WAKE_UP_AROUSE_SUPPORT = "oplus.software.wake_up_arouse_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WALLET_QUICK_LAUNCH = "oplus.software.wallet_quick_launch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WALLPAPER_CARRIER_CLARO = "oplus.software.wallpaper_carrier_claro";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WALLPAPER_CARRIER_CUSTOM = "oplus.software.wallpaper_carrier_custom";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WALLPAPER_CARRIER_TELCEL = "oplus.software.wallpaper_carrier_telcel";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WALLPAPER_CARRIER_TIM = "oplus.software.wallpaper_carrier_tim";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WFC_DISABLE_NRSA = "oplus.software.radio.wfc_disable_nrsa";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_5G160M_SAP = "oplus.software.wlan.5g_160M_softap";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_5G160M_STATION_FOR_PHONE_CLONE = "oplus.software.wlan.5g_160M_station_for_phone_clone";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_6G320M_P2P = "oplus.software.wlan.6g_320M_p2p";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_ANT_SWAP = "oplus.software.wlan.ant_swap";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_AP_GC_CONNECT = "oplus.software.wlan.ap_gc_connect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_ATPC = "oplus.software.wlan.atpc";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_ATT_HOTSPOT = "oplus.software.wlan.att_hotspot";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_AUTORECONNECT = "oplus.software.wlan.auto_reconnect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_BAR = "oplus.software.wlan.bar";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_BTC_SUPPORT_FDD_2X2 = "oplus.software.wlan_btc_support_fdd_2x2";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_CONNECTION_SELFCURE = "oplus.software.wlan.connection_selfcure";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_CONNECTION_SELFCURE_V3 = "oplus.software.wlan.connection_selfcure_v3";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_DATA_CONTROL = "oplus.software.radio.wifi_data_control";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_DBS = "oplus.software.wlan.dbs";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_DISABLE_SAP_WiFi6 = "oplus.software.wlan.disable_sap_wifi6";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_DLNA_DETECT_ENABLE = "oplus.software.wlan.dlna_detect_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_DPD = "oplus.software.wlan.dpd";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_DUALSTA = "oplus.software.wlan.dual_sta";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_FDD = "oplus.software.wlan.fdd_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_FDD_V3 = "oplus.software.wlan.fdd_v3_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_FIXED_RATE = "oplus.software.wlan.fixedrate";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_FORBIDDEN_6GHZ = "oplus.software.wlan.forbidden_6GHz";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_HYBRID = "oplus.software.wlan.hybrid_mode";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_INTERCONNECT_SCENE_DISABLE = "oplus.software.wlan_interconnect_scene_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_IPV6OPTMIZE = "oplus.software.wlan.ipv6_optimize";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_LOG_EUEX = "oplus.software.wlan.log_euex";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_LOW_LATENCY = "oplus.software.wlan.low_latency";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_LOW_LATENCY_V2 = "oplus.software.wlan.low_latency_v2";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_NET_SHARE = "oplus.software.wlan.net_share";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_NULL_DATA_TO_CTS = "oplus.software.wlan.null_data_to_cts";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_OJD_DISABLE = "oplus.software.wlan_ojd_disable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PASSPOINT_ATT = "oplus.software.wlan.passpoint_attmex";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PASSPOINT_JIO = "oplus.software.wlan.passpoint_jio";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PASSPOINT_VERIZON = "oplus.software.wlan.passpoint_verizon";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PKAT = "oplus.software.wlan.pkat";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_BOUYGUE = "oplus.software.wlan.operator_preset_bouygue";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_KDDI = "oplus.software.wlan.operator_preset_kddi";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_O2 = "oplus.software.wlan.o2_preset";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_RAKUTEN = "oplus.software.wlan.operator_preset_rakuten";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_SINGTEL = "oplus.software.wlan.operator_preset_singtel";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_SWISSCOM = "oplus.software.wlan.operator_preset_swisscom";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_THAILAND = "oplus.software.wlan.operator_preset_thailand";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_TMOBILE = "oplus.software.wlan.operator_preset_tmobile";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_TURKCELL = "oplus.software.wlan.operator_preset_turkcell";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_VERIZON = "oplus.software.wlan.verizon_preset";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_PRESET_VODAFONE = "oplus.software.wlan.vodafone_preset";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_ROUTERBOOST = "oplus.software.wlan.routerboost";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_RSSI_ISSUE_TRACKER = "oplus.software.wlan.rssi_issue_tracker";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SAP_RANDOM_SSID = "oplus.software.wlan.softap_random_ssid";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SCAN_SET_BAND = "oplus.software.wlan.scan.set_band";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SEAMLESS_SWITCH = "oplus.software.wlan.seamless_switch";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SGDETECT = "oplus.software.wlan.sg_detect";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SMARTGEAR = "oplus.software.wlan.smart_gear";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SMART_ANTENNA = "oplus.software.wlan.smart_antenna";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SMART_MCC_ENABLE = "oplus.software.wlan_smart_mcc_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SNIFFER_CAPTURE_WITH_UDP = "oplus.software.wlan.sniffer_capture_with_udp";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_SOFTAP_NSS1X1 = "oplus.software.wlan.softap_nss1X1";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_USS_PASSPOINT = "oplus.software.wlan.uss_passpoint";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_UST_HOTSPOT_SHOW = "oplus.software.wlan.ust_hotspot_show";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_UST_PASSPOINT = "oplus.software.wlan.ust_passpoint";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_US_TMOBILE_URL_CUSTOMIZED = "oplus.software.wlan.us_tmobile_url_customized";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WIFI_WIFI6 = "oplus.software.wlan.wifi6";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WINDOW_ANIM_LIGHT = "oplus.software.windowanimaiton.light";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WINDOW_ANIM_LOW_END_CONFIG = "oplus.software.windowanimaiton.low.end.config";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WINDOW_BRACKET = "oplus.software.windowmode.bracket";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WINDOW_COMPACT = "oplus.software.windowmode.compact";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WINDOW_COMPACT_LITE = "oplus.software.windowmode.compact.lite";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WLAN_ASSISTANT = "oplus.software.wlan.assistant";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WLAN_IOTCONNECT_OFF = "oplus.software.wlan.iotconnect.off";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WLAN_SLA = "oplus.software.wlan.sla";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WLAN_TRAFFIC_LIMIT = "oplus.software.wlan.traffic_limit";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WLB = "oplus.software.wlb";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_WNR_SUPPORT = "oplus.software.video.wnr_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_XBXB_SUPPORT = "oplus.software.audio.voice_wakeup_xbxb_support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_XBXB_U_PREVIEW = "oplus.software.audio.voice_wakeup_android_u_preview";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FEATURE_Z_AXIS_LMVIBRATOR_SUPPORT = "oplus.software.vibrator_z_axis";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FFEATURE_CUSTOMER_SDCARD_WIPEDATA = "oplus.software.sdcard_wipedata";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String FFEATURE_CUSTOMER_WIFISAR = "oplus.software.wlan.company_customer_sar";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String INTELLIGENT_COLOR_TEMPERATURE = "oplus.software.display.proximity_sensor_support_for_intelligent_color_temperature";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OH_AOD_SWITCH_DISABLE = "oplus.software.disable_aod_open_double_tap_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_CALL_RECOVERY_DISABLED = "oplus.software.radio.call_recovery_disabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_CLEAR_USER_WALLPAPER = "oplus.software.wallpaper.clear_user_wallpaper";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_CMCC_NETWORK_SLICING = "oplus.software.radio.cmcc_network_slicing";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_COLOR_SPLIT_FEATURE = "oplus.software.color_split_feature";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_CRAZY_QUERY_CHECKER = "oplus.software.radio.crazy_query_checker";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_DARK_MODE_STYLE_ENHANCE_MASK = "oplus.software.dark_mode_style_enhance_mask";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_DARK_MODE_STYLE_GENTLE_MASK = "oplus.software.dark_mode_style_gentle_mask";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_DARK_MODE_STYLE_MODERATE_MASK = "oplus.software.dark_mode_style_moderate_mask";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_DARK_MODE_STYLE_MODIFY = "oplus.software.dark_mode_style";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_FLEXIBLE_SUB_DISPLAY_SUPPORT = "oplus.software.flexible.subdisplay.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_FOLD_STATUS_OPT = "oplus.software.radio.fold_status";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_INCREASE_TCP_SYNC_RETRIES = "oplus.software.radio.increase_tcp_sync_retries";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_JANK_TRACKER = "oplus.software.jank_tracker";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_KEEP_SA_NSA = "oplus.software.radio.keep_sa_nsa";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_KEEP_SWIPEUP_GESTURES = "com.android.systemui.keep_swipup_gestures";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_NEWSOUNDRECORD_OS12_1_FUNCTION = "oplus.software.newsoundrecord.os12_1_function";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_NHS_REG_TRACKER_4_0_ENABLED = "oplus.software.radio.nhs_reg_tracker_4.0_enabled";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_NWPOWER_MEASUREMENT_FROZEN = "oplus.software.radio.nwpower_measurement_frozen_switch_enable";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_OOS_SCAN_SCREEN_ON = "oplus.software.oos_scan_screen_on";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_PACKETSTUDIO_SUPPORT = "oplus.software.pocketstudio.support";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_POWERKEY_SHORT_PRESS_SHUTDOWN = "oplus.software.short_press_powerkey_shutdown";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_POWERKEY_SHUTDOWN = "oplus.software.long_press_powerkey_shutdown";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_SCREENSHOT_TOUCH_STYLUS_MODEL = "oplus.software.screenshot.touch_stylus_module";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_SCROLL_ANIMATION_OPT = "oplus.software.scroll.animation_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_SIDO_ELEVATOR = "oplus.software.radio.sido_elevator";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_SIDO_IMS_CAUSED_SA_BACKOFF_OPT = "oplus.software.radio.sido_ims_caused_sa_backoff_opt";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_SMART5G_THERMAL_DEPRIO = "oplus.software.radio.smart5g_thermal_deprio";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_STARTING_WINDOW_OPTIMIZE_STARTUP = "oplus.software.starting_window_startup_booster";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_TASKBAR_SUPPORT_T_COMPAT = "oplus.software.support_t_solution_taskbar_upgrade_compat";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_UST_ADB_SET_SVN = "oplus.software.carrier.ust_adb_set_svn";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_UST_UA = "oplus.software.radio.ust_user_agent";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_VIRTUALMODEM_DATA_ONLY = "oplus.software.radio.virtualmodem_data_only";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String OPLUS_FEATURE_WEATHER_EFFECT_OGG = "oplus.software.weather.effect_egg";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String SERVICE_RESTART_DURATION_LOW = "oplus.software.service.restrat_duration_low";

    @OplusFeature(OplusFeature.OplusFeatureType.READONLY_FEATURE)
    public static final String UNFOLD_WIDTH_SMALL_HIGH = "oplus.software.feature_unfold_width_small_high";
}
