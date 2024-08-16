package android.telephony;

import android.os.PersistableBundle;
import android.util.Log;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import oplus.telephony.CarrierConfigManagerExt;

/* loaded from: classes.dex */
public class CarrierConfigManagerExtImpl implements ICarrierConfigManagerExt {
    private static final String LOG_TAG = "CarrierConfigManagerExtImpl";

    public CarrierConfigManagerExtImpl() {
    }

    public CarrierConfigManagerExtImpl(Object base) {
    }

    public void putDefault(PersistableBundle defaults) {
        if (defaults == null) {
            Log.d(LOG_TAG, "setPersistableBundleDefault null");
            return;
        }
        defaults.putBoolean("carrier_volte_fr1_bool", false);
        defaults.putIntArray("carrier_volte_fr1_int_array", new int[]{-999, -999, -999});
        defaults.putBoolean("support_1x_incall_mmi", false);
        defaults.putBoolean("config_oplus_dual_lte_available_bool", true);
        defaults.putStringArray("carrier_metered_apn_types_strings", new String[]{SplitPathManager.DEFAULT, "dun", "supl"});
        defaults.putStringArray("call_presentation_mapping_string_array", null);
        defaults.putBoolean("carrier_show_ims_conference_list_without_cep_config", false);
        defaults.putBoolean("wfc_disable_nrsa_config", false);
        defaults.putInt("carrier_support_bwp", -1);
        defaults.putInt("carrier_support_nrca", -1);
        defaults.putInt("dpmFdDelayIdleTime", 0);
        defaults.putInt("dpmFdScreenOnIdleTime", 15);
        defaults.putInt("dpmFdmScreenOffIdleTime", 3);
        defaults.putInt("dpmFdTetheringIdleTime", 15);
        defaults.putBoolean("force_build_mms_over_wifi_apns", false);
        defaults.putBoolean("config_oplus_erlvt_mt_off_bool", true);
        defaults.putInt("carrier_customized_nricon_config", -1);
        defaults.putBoolean("carrier_oplus_esm_reject", false);
        defaults.putBoolean("mtk_handle_dialing_data_during_call_bool", true);
        defaults.putBoolean("mtk_data_fdn_supported_bool", false);
        defaults.putStringArray("mtk_skip_data_stall_apn_list_strings", new String[]{""});
        defaults.putBoolean("mtk_volte_iot_firewall_enable_bool", false);
        defaults.putIntArray("mtk_skip_roaming_data_on_iwlan_apn_types_int_array", new int[0]);
        defaults.putBoolean("mtk_domestic_roaming_enabled_only_by_mobile_data_setting", false);
        defaults.putStringArray("mtk_domestic_roaming_enabled_only_by_mobile_data_setting_check_nw_plmn", null);
        defaults.putBoolean("mtk_intl_roaming_enabled_only_by_roaming_data_setting", false);
        defaults.putInt("mtk_unique_settings_for_domestic_and_intl_roaming", 0);
        defaults.putInt("mtk_test_sim_mtu_value_int", 0);
        CarrierConfigManagerExt.putDefault(defaults);
        defaults.putBoolean("carrier_vonr_backoff", true);
        defaults.putBoolean("carrier_sa_backoff", true);
        defaults.putInt("carrier_vonr_call_fail_threshold", 1);
        defaults.putLong("carrier_diasble_vonr_timer", 0L);
        defaults.putInt("carrier_sa_call_fail_threshold", 2);
        defaults.putLong("carrier_diasble_sa_timer", 180000L);
        defaults.putBoolean("carrier_config_handle_euisinfo_after_active_bool", false);
        defaults.putBoolean("cs_sms_in_ciwlan_only_mode", false);
        defaults.putBoolean("oplus_cfu_enabled_bool", false);
    }
}
