package com.android.internal.telephony.nrNetwork;

/* loaded from: classes.dex */
public interface OplusNrModeConstant {
    public static final String AT_CMD_QUERY_5G = "AT+E5GOPT?";
    public static final String AT_CMD_QUERY_5G_RESP = "+E5GOPT:";
    public static final String AT_CMD_QUERY_RAT_ORDER = "AT+ERAT?";
    public static final String AT_CMD_QUERY_RAT_RESP = "+ERAT:";
    public static final String AT_CMD_SET_5G = "AT+E5GOPT=";
    public static final String AT_CMD_SET_PREFER_RAT = "AT+EPRATL=";
    public static final int AUTO_NR_MODE = 1;
    public static final String CARRIER_CONFIG_AUTO_MODE = "carrier_oplus_auto_nr_mode";
    public static final String CARRIER_CONFIG_AUTO_MODE_SUB = "carrier_oplus_auto_nr_mode_sub";
    public static final String CARRIER_CONFIG_NR_SA_PRE = "carrier_oplus_nr_sa_prefer";
    public static final int INVALID_TAC = -1;
    public static final String KEY_ACQ_RAT_PREFER = "acq_rat_prefer";
    public static final String KEY_ACQ_RAT_PREFER_SUB = "acq_rat_prefer_sub";
    public static final String KEY_CMCC_DIS_SMARTSA_CITY_LIST = "cmcc_city_list";
    public static final String KEY_CMCC_DIS_SMARTSA_PRO_LIST = "cmcc_pro_list";
    public static final String KEY_CMCC_SA_CITY_LIST = "cmcc_sa_city_list";
    public static final String KEY_CMCC_SA_PRO_LIST = "cmcc_sa_pro_list";
    public static final String KEY_CT_DIS_SMARTSA_CITY_LIST = "ct_city_list";
    public static final String KEY_CT_DIS_SMARTSA_PRO_LIST = "ct_pro_list";
    public static final String KEY_CT_SA_CITY_LIST = "ct_sa_city_list";
    public static final String KEY_CT_SA_PRO_LIST = "ct_sa_pro_list";
    public static final String KEY_CU_DIS_SMARTSA_CITY_LIST = "cu_city_list";
    public static final String KEY_CU_DIS_SMARTSA_PRO_LIST = "cu_pro_list";
    public static final String KEY_CU_SA_CITY_LIST = "cu_sa_city_list";
    public static final String KEY_CU_SA_PRO_LIST = "cu_sa_pro_list";
    public static final String KEY_OP_SA_LIST = "operator_sa_list";
    public static final String KEY_RUS_NR_MODE = "rus_nr_mode";
    public static final String KEY_RUS_NR_MODE_CFG = "key_rus_nr_mode_cfg";
    public static final String KEY_RUS_NR_MODE_SUB = "rus_nr_mode_sub";
    public static final String KEY_RUS_RAT_CTRL = "rat_control";
    public static final String KEY_RUS_SA_PRE = "rus_sa_pre";
    public static final String KEY_RUS_SA_SWITCH_CFG = "key_rus_sa_switch_cfg";
    public static final String KEY_SA_PREFERED_CITY = "sa_prefered_city";
    public static final String KEY_SIM_STATE = "simstate";
    public static final String KEY_SUB_SLOT_SUPPORT_SA = "sub_slot_support_sa";
    public static final String KEY_USER_AUTO_MODE = "user_auto_mode";
    public static final String KEY_USER_NR_MODE = "user_nr_mode";
    public static final String KEY_USER_NR_MODE_SUB = "user_nr_mode_sub";
    public static final float LOC_ACCURACY = 50.0f;
    public static final long LONG_lOC_PERIOD = 7200000;
    public static final int MTK_E5GOPT_OPTION_DEFAULT = 1;
    public static final int MTK_NSA_MODE = 5;
    public static final int MTK_NSA_SA_MODE = 7;
    public static final int MTK_PREFER_RAT_LTE = 1;
    public static final int MTK_PREFER_RAT_NONE = 0;
    public static final int MTK_PREFER_RAT_NR = 2;
    public static final int MTK_SA_MODE = 3;
    public static final int NON_AUTO_NR_MODE = -1;
    public static final int NR5G_MODE_AUTO = 4;
    public static final int NR5G_MODE_INVALID = -1;
    public static final int NR5G_MODE_NSA_ONLY = 1;
    public static final int NR5G_MODE_NSA_PRE = 0;
    public static final int NR5G_MODE_SA_ONLY = 2;
    public static final int NR5G_MODE_SA_PRE = 3;
    public static final int PHONE_0 = 0;
    public static final int PHONE_1 = 1;
    public static final int RAT_CONTROL_DISABLED = 0;
    public static final int RAT_CONTROL_ENABLED = 1;
    public static final String RAT_PREFER_LTE = "4,4,2,1,128";
    public static final String RAT_PREFER_NR = "4,128,4,2,1";
    public static final int SET_NR_MODE_DDS_CHANGE = 3;
    public static final int SET_NR_MODE_LOC_CHANGE = 2;
    public static final int SET_NR_MODE_TEST_CARD = 1;
    public static final int SET_NR_MODE_USER = 0;
    public static final long SHORT_lOC_PERIOD = 1000;
    public static final long TAC_UPDATE_PERIOD = 600000;
    public static final int TIME_DELAY_TO_UPDATE_NR_MODE = 0;

    /* loaded from: classes.dex */
    public enum CfgType {
        NONE,
        CITY,
        PROVINCE
    }

    /* loaded from: classes.dex */
    public enum SimType {
        SIM_TYPE_OTHER,
        SIM_TYPE_TEST,
        SIM_TYPE_CMCC,
        SIM_TYPE_CU,
        SIM_TYPE_CT,
        SIM_TYPE_CB,
        SIM_TYPE_JIO
    }
}
