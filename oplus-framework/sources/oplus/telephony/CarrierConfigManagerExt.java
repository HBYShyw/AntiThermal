package oplus.telephony;

import android.os.PersistableBundle;

/* loaded from: classes.dex */
public class CarrierConfigManagerExt {
    public static final int DEFAUT_IMS_VIDEO_CONFERENCE_SIZE_LIMIT = 5;
    public static final int DEFAUT_WFC_GET_CONFIDENCE_LEVEL = 68;
    public static final int DEFAUT_WFC_LOCATION_RESPONSE_TIMEOUT = 55000;
    public static final String KEY_ALLOW_CALL_WHEN_OUT_OF_SERVICE_BOOL = "mtk_allow_call_when_oos_bool";
    public static final String KEY_ALLOW_ONE_VIDEO_CALL_ONLY_BOOL = "mtk_allow_one_video_call_only_bool";
    public static final String KEY_CARRIER_IGNORE_CLIR_WHEN_ECC_BOOL = "mtk_ignore_clir_when_ecc";
    public static final String KEY_CARRIER_NEED_SHOW_ROAMING_ICON = "mtk_key_carrier_need_show_roaming_icon";
    public static final String KEY_CARRIER_SS_QUERY_DELAY_TIME = "mtk_carrier_ss_query_delay_time";
    public static final String KEY_CARRIER_SUPPORTS_MULTIANCHOR_CONFERENCE = "carrier_supports_multianchor_conference";
    public static final String KEY_CARRIER_SWITCH_WFC_MODE_REQUIRED_BOOL = "mtk_carrier_switch_wfc_mode_required_bool";
    public static final String KEY_CARRIER_USE_IMS_FIRST_FOR_CALL_BOOL = "mtk_use_ims_first_for_call_bool";
    public static final String KEY_CDMA_CW_CF_ENABLED_BOOL = "cdma_cw_cf_enabled_bool";
    public static final String KEY_DISABLE_VT_OVER_WIFI_BOOL = "mtk_disable_vt_over_wifi_bool";
    public static final String KEY_DISABLE_WFC_AFTER_AUTH_FAIL = "mtk_carrier_disable_wfc_after_auth_fail_bool";
    public static final String KEY_DISALLOW_OUTGOING_CALLS_DURING_CONFERENCE_BOOL = "mtk_disallow_outgoing_calls_during_conference_bool";
    public static final String KEY_DISALLOW_OUTGOING_CALLS_DURING_VT_OR_VO_BOOL = "mtk_disallow_outgoing_calls_during_video_or_voice_call_bool";
    public static final String KEY_EMC_RTT_GUARD_TIMER_BOOL = "mtk_emc_rtt_guard_timer_bool";
    public static final String KEY_IGNORE_DATA_ROAMING_FOR_VIDEO_CALLS = "mtk_ignore_data_roaming_for_video_calls";
    public static final String KEY_IMS_NO_CONF_REQ_AFTER_MAX_CONNECTION_BOOL = "mtk_no_merge_req_after_max_connection";
    public static final String KEY_IMS_VIDEO_CONFERENCE_SIZE_LIMIT_INT = "mtk_ims_video_conference_size_limit_int";
    public static final String KEY_INTER_ROAMING_EXCEPTION_LIST_STRS = "carrier_international_roaming_exception_list_strings";
    public static final String KEY_MT_RTT_WITHOUT_PRECONDITION_BOOL = "mtk_mt_rtt_without_precondition_bool";
    public static final String KEY_OTASP_CALL_NUMBER_STRING = "mtk_key_otasp_call_number_string";
    public static final String KEY_RESUME_HOLD_CALL_AFTER_ACTIVE_CALL_END_BY_REMOTE = "mtk_resume_hold_call_after_active_call_end_by_remote";
    public static final String KEY_ROAMING_BAR_GUARD_BOOL = "mtk_key_roaming_bar_guard_bool";
    public static final String KEY_RTT_AUDIO_INDICATION_SUPPORTED_BOOL = "mtk_rtt_audio_indication_supported_bool";
    public static final String KEY_RTT_VIDEO_SWITCH_SUPPORTED_BOOL = "mtk_rtt_video_switch_supported_bool";
    public static final String KEY_SEND_NETWORK_COVERAGE_LOST = "mtk_send_network_coverage_lost";
    public static final String KEY_SHOW_MERGE_ON_PARTICIPANT = "mtk_show_merge_button_on_conference_participant";
    public static final String KEY_SHOW_TOAST_WHEN_CONFERENCE_FULL_BOOL = "mtk_show_toast_when_conference_full";
    public static final String KEY_SINGLE_CALL_AFTER_CONFERENCE_SRVCC = "mtk_single_call_after_conference_srvcc";
    public static final String KEY_SPECIAL_CONSTRAINT_FOR_IMS_VIDEO_CONFERENCE = "mtk_special_constraint_for_ims_video_conference";
    public static final String KEY_SUPPORT_VT_SS_BOOL = "mtk_support_vt_ss_bool";
    public static final String KEY_VT_DOWNGRADE_IN_BAD_BITRATE = "mtk_key_vt_downgrade_in_bad_bitrate";
    public static final String KEY_VT_OVER_WIFI_CHECK_DATA_ENABLE_BOOL = "mtk_vt_over_wifi_check_data_enable_bool";
    public static final String KEY_VT_OVER_WIFI_CHECK_VOLTE_ENABLE_BOOL = "mtk_vt_over_wifi_check_volte_enable_bool";
    public static final String KEY_VT_OVER_WIFI_CHECK_WFC_ENABLE_BOOL = "mtk_vt_over_wifi_check_wfc_enable_bool";
    public static final String KEY_WFC_GET_CONFIDENCE_LEVEL = "mtk_carrier_wfc_get_confidence_level";
    public static final String KEY_WFC_GET_LOCATION_ALWAYS = "mtk_carrier_wfc_get_location_always";
    public static final String KEY_WFC_LOCATION_RESPONSE_TIMEOUT = "mtk_carrier_wfc_location_response_timeout";
    public static final String KEY_WFC_MODE_DOMESTIC_ROMAING_TO_HOME = "mtk_carrier_wfc_mode_domestic_roaming_to_home";
    public static final String KEY_WOS_SUPPORT_WFC_IN_FLIGHTMODE = "wos_flight_mode_support_bool";

    public static void putDefault(PersistableBundle sDefaults) {
        sDefaults.putBoolean(KEY_ALLOW_ONE_VIDEO_CALL_ONLY_BOOL, false);
        sDefaults.putStringArray(KEY_INTER_ROAMING_EXCEPTION_LIST_STRS, new String[]{""});
        sDefaults.putBoolean(KEY_SUPPORT_VT_SS_BOOL, false);
        sDefaults.putInt(KEY_CARRIER_SS_QUERY_DELAY_TIME, 0);
        sDefaults.putBoolean(KEY_ROAMING_BAR_GUARD_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_SWITCH_WFC_MODE_REQUIRED_BOOL, false);
        sDefaults.putBoolean(KEY_SINGLE_CALL_AFTER_CONFERENCE_SRVCC, true);
        sDefaults.putBoolean(KEY_CARRIER_USE_IMS_FIRST_FOR_CALL_BOOL, false);
        sDefaults.putBoolean(KEY_CARRIER_IGNORE_CLIR_WHEN_ECC_BOOL, true);
        sDefaults.putBoolean(KEY_WOS_SUPPORT_WFC_IN_FLIGHTMODE, true);
        sDefaults.putBoolean(KEY_WFC_MODE_DOMESTIC_ROMAING_TO_HOME, false);
        sDefaults.putBoolean(KEY_WFC_GET_LOCATION_ALWAYS, false);
        sDefaults.putInt(KEY_WFC_GET_CONFIDENCE_LEVEL, 68);
        sDefaults.putInt(KEY_WFC_LOCATION_RESPONSE_TIMEOUT, DEFAUT_WFC_LOCATION_RESPONSE_TIMEOUT);
        sDefaults.putBoolean(KEY_DISABLE_WFC_AFTER_AUTH_FAIL, false);
        sDefaults.putBoolean(KEY_SHOW_TOAST_WHEN_CONFERENCE_FULL_BOOL, true);
        sDefaults.putBoolean(KEY_IMS_NO_CONF_REQ_AFTER_MAX_CONNECTION_BOOL, true);
        sDefaults.putBoolean(KEY_CARRIER_NEED_SHOW_ROAMING_ICON, true);
        sDefaults.putBoolean(KEY_DISABLE_VT_OVER_WIFI_BOOL, false);
        sDefaults.putBoolean(KEY_RESUME_HOLD_CALL_AFTER_ACTIVE_CALL_END_BY_REMOTE, false);
        sDefaults.putBoolean(KEY_IGNORE_DATA_ROAMING_FOR_VIDEO_CALLS, false);
        sDefaults.putInt(KEY_IMS_VIDEO_CONFERENCE_SIZE_LIMIT_INT, 5);
        sDefaults.putBoolean(KEY_SPECIAL_CONSTRAINT_FOR_IMS_VIDEO_CONFERENCE, false);
        sDefaults.putBoolean(KEY_RTT_VIDEO_SWITCH_SUPPORTED_BOOL, true);
        sDefaults.putBoolean(KEY_EMC_RTT_GUARD_TIMER_BOOL, false);
        sDefaults.putBoolean(KEY_MT_RTT_WITHOUT_PRECONDITION_BOOL, false);
        sDefaults.putBoolean(KEY_RTT_AUDIO_INDICATION_SUPPORTED_BOOL, false);
        sDefaults.putBoolean(KEY_VT_OVER_WIFI_CHECK_DATA_ENABLE_BOOL, false);
        sDefaults.putBoolean(KEY_VT_OVER_WIFI_CHECK_WFC_ENABLE_BOOL, true);
        sDefaults.putBoolean(KEY_VT_OVER_WIFI_CHECK_VOLTE_ENABLE_BOOL, false);
        sDefaults.putBoolean(KEY_DISALLOW_OUTGOING_CALLS_DURING_CONFERENCE_BOOL, false);
        sDefaults.putString(KEY_OTASP_CALL_NUMBER_STRING, "");
        sDefaults.putBoolean(KEY_SHOW_MERGE_ON_PARTICIPANT, false);
        sDefaults.putBoolean(KEY_VT_DOWNGRADE_IN_BAD_BITRATE, false);
        sDefaults.putBoolean(KEY_SEND_NETWORK_COVERAGE_LOST, false);
        sDefaults.putBoolean(KEY_ALLOW_CALL_WHEN_OUT_OF_SERVICE_BOOL, false);
    }
}
