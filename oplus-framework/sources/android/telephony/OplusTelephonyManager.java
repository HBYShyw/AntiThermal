package android.telephony;

import android.app.PendingIntent;
import android.compat.Compatibility;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.OplusTelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SeempLog;
import com.android.ims.ImsManager;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.IOplusTelephonyExt;
import com.android.internal.telephony.IOplusTelephonyExtCallback;
import com.android.internal.telephony.IOplusTelephonyInternalExt;
import com.android.internal.telephony.IPlmnCarrierConfigLoader;
import com.android.internal.telephony.ISms;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.OplusFeature;
import com.oplus.gwsd.GwsdListener;
import com.oplus.gwsd.IGwsdService;
import com.oplus.telephony.IOemHookCallback;
import com.oplus.virtualcomm.IVirtualCommService;
import com.oplus.virtualcomm.VirtualCommServiceState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class OplusTelephonyManager {
    public static final int APP_FAM_3GPP = 1;
    public static final int APP_FAM_3GPP2 = 2;
    public static final int APP_FAM_NONE = 0;
    public static final String AUTO_NR_MODE_KEY = "autoNrMode";
    public static final String BUNDLE_CONTENT = "content";
    public static final int CAPABILITY_INTERNAL_THERMAL = 2;
    public static final int CAPABILITY_RECOVERY = 1;
    public static final int CAPABILITY_SMART_FIVEG = 3;
    public static final int CAPABILITY_THERMAL = 0;
    public static final int CARD_NOT_PRESENT = -2;
    public static final int CARD_TYPE_CB = 5;
    public static final int CARD_TYPE_CM = 2;
    public static final int CARD_TYPE_CSIM = 4;
    public static final int CARD_TYPE_CT = 1;
    public static final int CARD_TYPE_CU = 3;
    public static final int CARD_TYPE_NONE = 0;
    public static final int CARD_TYPE_OTHER = 4;
    public static final int CARD_TYPE_RUIM = 8;
    public static final int CARD_TYPE_SIM = 1;
    public static final int CARD_TYPE_TEST = 9;
    public static final int CARD_TYPE_UNKNOWN = -1;
    public static final int CARD_TYPE_USIM = 2;
    public static final String CT_AUTO_IMS_REG_PACKAGE = "com.oppo.ctautoregist";
    public static final int DEVICE_TYPE_HARDWARE = 0;
    public static final int DEVICE_TYPE_SOFTWARE = 1;
    public static final String DUAL_LTE_SWITCH = "oplus_customize_dual_lte_switch";
    public static final String DUAL_LTE_SWITCH_DEFAULT = "oplus_customize_dual_lte_switch_default";
    public static final int EVENT_CALL_BASE = 4000;
    public static final int EVENT_CALL_CLEAN_ALL_CONN = 4008;
    public static final int EVENT_CALL_CLEAN_CONN = 4007;
    public static final int EVENT_CALL_CRSTYPE_UPDATE = 4016;
    public static final int EVENT_CALL_END_CALLTRACKER_STATE = 4010;
    public static final int EVENT_CALL_GET_AUTO_ANSWER = 4004;
    public static final int EVENT_CALL_GET_CURRENT_CALLS = 4006;
    public static final int EVENT_CALL_GET_ECC_LIST = 4002;
    public static final int EVENT_CALL_GET_VONR_STATE = 4013;
    public static final int EVENT_CALL_GET_VONR_VISIBLE = 4014;
    public static final int EVENT_CALL_MARK_DISCONNECTING_STATE = 4011;
    public static final int EVENT_CALL_PROGRESS_INFO = 4017;
    public static final int EVENT_CALL_SET_AUTO_ANSWER = 4005;
    public static final int EVENT_CALL_SET_ECC_LIST = 4003;
    public static final int EVENT_CALL_SET_VONR_STATE = 4012;
    public static final int EVENT_CALL_UPDATE_CALLTRACKER_STATE = 4009;
    public static final int EVENT_CALL_UPDATE_VOLTE_FR2 = 4001;
    public static final int EVENT_CALL_VONR_VISIBLE_CHANGED = 4015;
    public static final int EVENT_COMMON_BASE = 1000;
    public static final int EVENT_COMMON_MATCH_UNLOCK = 1002;
    public static final int EVENT_COMMON_OEM_RIL_REQUEST = 1001;
    public static final int EVENT_DATA_ACTION_APN_RESTORE = 3010;
    public static final int EVENT_DATA_ACTION_APN_TO_NET = 3012;
    public static final int EVENT_DATA_ACTION_AUTO_PLMN = 3007;
    public static final int EVENT_DATA_ACTION_RETRY_PDN = 3008;
    public static final int EVENT_DATA_BASE = 3000;
    public static final int EVENT_DATA_CHECK_APN_MANUAL_ADD = 3009;
    public static final int EVENT_DATA_CHECK_APN_WAP = 3011;
    public static final int EVENT_DATA_CHECK_DNS = 3001;
    public static final int EVENT_DATA_CHECK_HTTP = 3002;
    public static final int EVENT_DATA_CHECK_PDN = 3005;
    public static final int EVENT_DATA_CHECK_SIGNAL = 3004;
    public static final int EVENT_DATA_CHECK_SIM = 3003;
    public static final int EVENT_DATA_CHECK_VPN = 3006;
    public static final int EVENT_DATA_GAME_BATTLE_SCENE = 3017;
    public static final int EVENT_DATA_IS_MULTIPLE_TIMEZONE = 3016;
    public static final int EVENT_DATA_REPORT_GAME_LATENCY = 3013;
    public static final int EVENT_DATA_REPORT_GAME_LEVEL = 3014;
    public static final int EVENT_DATA_SET_GEO_IP_TIMEZONE = 3015;
    public static final int EVENT_DETECT_RIL_STATE = 4020;
    public static final int EVENT_DETECT_RIL_STATE_DONE = 4023;
    public static final int EVENT_DUAL_DATA_RECOMMENDATION = 3034;
    public static final int EVENT_DUAL_DATA_SUPPORT = 3033;
    public static final int EVENT_EDMFAPP_URC_CALL_DATA_INFO = 4024;
    public static final int EVENT_GET_GAME_CLOSE_SA_KPI = 6049;
    public static final int EVENT_GET_MCC = 6038;
    public static final int EVENT_GET_MOTION_STATE = 7001;
    public static final int EVENT_GET_PENDING_REQUEST = 4018;
    public static final int EVENT_GET_PENDING_REQUEST_DONE = 4019;
    public static final int EVENT_GET_REGION_LOCK_SETTING_DATA = 6054;
    public static final int EVENT_GET_REGION_LOCK_STATUS = 6048;
    public static final int EVENT_GET_SIMLOCK_CATEGORY_DATA = 6042;
    public static final int EVENT_GET_SIMLOCK_FUSE_STATUS = 6043;
    public static final int EVENT_GET_SIMLOCK_RSU_MODE = 6044;
    public static final int EVENT_GET_SIMLOCK_STATUS = 6041;
    public static final int EVENT_GET_WIFI_CELL_KPI = 6037;
    public static final int EVENT_GMAE_LATENCY_REPORT = 3022;
    public static final int EVENT_HANDLE_GAME_INFO = 3023;
    public static final int EVENT_HANDLE_GAME_SWITCH_INFO = 3024;
    public static final int EVENT_IMS_REG_TIMER_CHANGED = 3027;
    public static final int EVENT_IS_CLOASE_SA_REASON = 3021;
    public static final String EVENT_KEY = "event";
    public static final int EVENT_MAX = 8000;
    public static final int EVENT_OP_GET_ANT_LOCK = 5019;
    public static final int EVENT_OP_GET_SAR_DSI = 5024;
    public static final int EVENT_OP_GET_SAR_MCC = 5022;
    public static final int EVENT_OP_GET_SAR_OTA_STATE = 5027;
    public static final int EVENT_OP_GET_SAR_STATE = 5028;
    public static final int EVENT_OP_NVBK_MISC_INFO = 5032;
    public static final int EVENT_OP_NVBK_MODEM_NV_CHECK = 5031;
    public static final int EVENT_OP_NVBK_NV_BACKUP = 5033;
    public static final int EVENT_OP_NVBK_NV_RESTORE = 5034;
    public static final int EVENT_OP_NVBK_READ_ADRC_CONFIG = 5036;
    public static final int EVENT_OP_NVBK_WRITE_ADRC_CONFIG = 5035;
    public static final int EVENT_OP_SET_ANT_LOCK = 5020;
    public static final int EVENT_OP_SET_NR_BAND_AND_CELL = 5029;
    public static final int EVENT_OP_SET_NR_BAND_ONLY = 5030;
    public static final int EVENT_OP_SET_SAR_DSI = 5023;
    public static final int EVENT_OP_SET_SAR_MCC = 5021;
    public static final int EVENT_OP_SET_SAR_OTA_STATE = 5026;
    public static final int EVENT_OP_SET_SAR_SENSOR_STATE = 5025;
    public static final int EVENT_OP_TEST_CARD_ON_OFF = 5037;
    public static final int EVENT_PIN_PUK_RETRY_UPDATE = 6030;
    public static final int EVENT_POWER_BASE = 7000;
    public static final int EVENT_POWER_HEARTBEAT_LOCAL_RELEASE = 7002;
    public static final int EVENT_REG_BASE = 2000;
    public static final int EVENT_REG_GET_5G_SIGNAL = 2003;
    public static final int EVENT_REG_GET_LTE_CA_STATE = 2010;
    public static final int EVENT_REG_GET_NR_MODE = 2009;
    public static final int EVENT_REG_GET_RADIO_INFO = 2002;
    public static final int EVENT_REG_GET_RADIO_ON = 2007;
    public static final int EVENT_REG_GET_REGION_NETLOCK_STATE_INFO = 2011;
    public static final int EVENT_REG_GET_REGION_NETLOCK_TEST_INFO = 2013;
    public static final int EVENT_REG_IS_CAPABILITY_SWITCH = 2015;
    public static final int EVENT_REG_LTE_CA_STATE = 2006;
    public static final int EVENT_REG_OEM_COMMON_REQ = 2004;
    public static final int EVENT_REG_SET_CELL_INFO_LIST_RATE = 2001;
    public static final int EVENT_REG_SET_NR_MODE = 2008;
    public static final int EVENT_REG_SET_REGION_NETLOCK_STATE_INFO = 2012;
    public static final int EVENT_REG_SET_REGION_NETLOCK_TEST_INFO = 2014;
    public static final int EVENT_REG_UPDATE_PPLMN_LIST = 2005;
    public static final int EVENT_RELEASE_DUAL_PS = 3032;
    public static final int EVENT_REPORT_GAME_STATE = 3030;
    public static final int EVENT_REPORT_NETWORK_LEVEL = 3029;
    public static final int EVENT_REQUEST_DUAL_PS = 3031;
    public static final int EVENT_RESET_MODEM = 4022;
    public static final int EVENT_RESET_QCOM_IMS_REG_TIMER_DONE = 3028;
    public static final int EVENT_RESET_RIL = 4021;
    public static final int EVENT_RF_BASE = 5000;
    public static final int EVENT_RF_GET_ASDIV_STATE = 5015;
    public static final int EVENT_RF_GET_BAND_MODE = 5002;
    public static final int EVENT_RF_GET_LAB_ANTPOS = 5017;
    public static final int EVENT_RF_GET_RFFE_DEV_INFO = 5011;
    public static final int EVENT_RF_GET_TX_POWER = 5018;
    public static final int EVENT_RF_GET_TX_RX_INFO = 5006;
    public static final int EVENT_RF_LOCK_GSM_ARFCN = 5008;
    public static final int EVENT_RF_LOCK_LTE_CELL = 5009;
    public static final int EVENT_RF_NOTICE_UPDATE_VOLTE_FR = 5010;
    public static final int EVENT_RF_QUERY_BAND_MODE = 5003;
    public static final int EVENT_RF_RFFE_CMD = 5014;
    public static final int EVENT_RF_SET_BAND_MODE = 5001;
    public static final int EVENT_RF_SET_FILTER_ARFCN = 5007;
    public static final int EVENT_RF_SET_LAB_ANTPOS = 5016;
    public static final int EVENT_RF_SET_MODEM_GPIO = 5005;
    public static final int EVENT_RF_SET_NV_PROCESS_CMD = 5004;
    public static final int EVENT_RF_SET_SAR_RF_STATE_V2 = 5012;
    public static final int EVENT_RF_SET_SAR_RF_STATE_V3 = 5013;
    public static final int EVENT_SETUP_DATA_COMPLETE = 3019;
    public static final int EVENT_SETUP_DATA_COMPLETED_DDSSWITCH = 3025;
    public static final int EVENT_SETUP_DATA_COMPLETE_FOR_APN_RECOVERY = 3020;
    public static final int EVENT_SET_REGION_LOCK_SETTING_DATA = 6055;
    public static final int EVENT_SET_SIM_AIRPLANE_MODE = 6045;
    public static final int EVENT_SET_SMART_5G_STATUS_ID = 6036;
    public static final int EVENT_SET_VICE_CARD_GAME_MODE = 6035;
    public static final int EVENT_SIM_BASE = 6000;
    public static final int EVENT_SIM_GET_ESIM_GPIO = 6032;
    public static final int EVENT_SIM_GET_HOTSWAP_STATE = 6005;
    public static final int EVENT_SIM_GET_IF_TEST_SIM = 6001;
    public static final int EVENT_SIM_GET_IMPI = 6003;
    public static final int EVENT_SIM_GET_IMS_TYPE = 6002;
    public static final int EVENT_SIM_GET_OPERATOR_SWITCH_ENABLE = 6006;
    public static final int EVENT_SIM_GET_OP_ID = 6004;
    public static final int EVENT_SIM_GET_SIMLOCK_ACTIVATE_TIME = 6026;
    public static final int EVENT_SIM_GET_SIMLOCK_COMBO_TYPE = 6011;
    public static final int EVENT_SIM_GET_SIMLOCK_CURRENTRETRY = 6013;
    public static final int EVENT_SIM_GET_SIMLOCK_FACTORY_RESET_TIME = 6020;
    public static final int EVENT_SIM_GET_SIMLOCK_FEATURE = 6022;
    public static final int EVENT_SIM_GET_SIMLOCK_FEESTATE = 6015;
    public static final int EVENT_SIM_GET_SIMLOCK_IS_REGION_VIETNAM = 6018;
    public static final int EVENT_SIM_GET_SIMLOCK_LOCKDEVICE = 6028;
    public static final int EVENT_SIM_GET_SIMLOCK_LOCKMARK = 6021;
    public static final int EVENT_SIM_GET_SIMLOCK_LOCKTYPE = 6009;
    public static final int EVENT_SIM_GET_SIMLOCK_MAXRETRY = 6012;
    public static final int EVENT_SIM_GET_SIMLOCK_OPERATOR = 6008;
    public static final int EVENT_SIM_GET_SIMLOCK_POPUP_TYPE = 6010;
    public static final int EVENT_SIM_GET_SIMLOCK_SIM1_STATE = 6016;
    public static final int EVENT_SIM_GET_SIMLOCK_SIM2_STATE = 6017;
    public static final int EVENT_SIM_GET_VSIM_ID = 6007;
    public static final int EVENT_SIM_SET_ESIM_GPIO = 6033;
    public static final int EVENT_SIM_SET_POWER = 6034;
    public static final int EVENT_SIM_SET_SIMLOCK_ACCUMULATED_TIME = 6024;
    public static final int EVENT_SIM_SET_SIMLOCK_ACTIVATE_TIME = 6025;
    public static final int EVENT_SIM_SET_SIMLOCK_FACTORY_RESET_TIME = 6019;
    public static final int EVENT_SIM_SET_SIMLOCK_FEESTATE = 6014;
    public static final int EVENT_SIM_SET_SIMLOCK_KDDI_SIMLOCK_FILE = 6031;
    public static final int EVENT_SIM_SET_SIMLOCK_LOCK = 6029;
    public static final int EVENT_SIM_SET_SIMLOCK_UNLOCK = 6023;
    public static final int EVENT_SIM_SET_VSIM_ID = 6027;
    public static final int EVENT_SIM_SET_VSIM_STATUS = 6039;
    public static final int EVENT_SMART5G_THERMAL_CONTROL = 6040;
    public static final int EVENT_START_TIMER_FOR_SETUP_DATA = 3018;
    public static final int EVENT_UNLOCK_REGION_LOCK = 6053;
    public static final int EVENT_UPDATA_NETWORK_SCORE = 3026;
    public static final int EVENT_UPDATE_REGION_LOCK_BLOB = 6046;
    public static final int EVENT_UPDATE_REGION_LOCK_KEY = 6052;
    public static final int EVENT_UPDATE_REGION_LOCK_STATUS = 6047;
    public static final int FAIL_RETRY = -1;
    public static final int FAIL_RETRY_NO_NEED = -2;
    private static final String GAME_SPACE_MODE_INGAME = "1";
    private static final String GAME_SPACE_MODE_SAVEGAME = "debug_gamemode_savegame";
    private static final String GAME_SPACE_MODE_VALUE = "debug_gamemode_value";
    private static final long GET_TARGET_SDK_VERSION_CODE_CHANGE_EXT = 145147529;
    public static final int HIGH_PRIORITY_SA_SILENCE = 1;
    public static final int INVALID_STATE = -1;
    public static final String IS_MULTIPLE_TIME_ZONE = "ismultipletimezone";
    public static final int LOW_PRIORITY_SA_SILENCE = 0;
    public static final int NETWORK_CLASS_2_G = 1;
    public static final int NETWORK_CLASS_3_G = 2;
    public static final int NETWORK_CLASS_4_G = 3;
    public static final int NETWORK_CLASS_5_G = 4;
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    public static final int NOT_PROVISIONED = 0;
    public static final String NR_MODE_CHANGED_ALLOW = "NrModeChangedAllow";
    private static final String OEM_REGSERVICE = "com.coloros.regservice";
    public static final String PACKAGE_KEY = "package";
    public static final int PLATFORM_TYPE_MTK = 2;
    public static final int PLATFORM_TYPE_NONE = 0;
    public static final int PLATFORM_TYPE_QCOM = 1;
    private static final String PLMN_CARRIER_CONFIG_SERVICE = "plmn_carrier_config";
    public static final int PROVISIONED = 1;
    public static final String RESULT_DATA = "hookdata";
    public static final String RESULT_KEY = "result";
    public static final String SA_PRE_KEY = "saPreEnabled";
    public static final String SERVICE_NAME = "oplus_telephony_ext";
    private static final String TAG = "OplusTelephonyManager";
    public static final String TIME_ZONE = "timezone";
    public static final String VC_SERVICE_NAME = "IVirtualComm";
    private static final int sPriorityHigh = 3;
    private static final int sPriorityLow = 0;
    private static final int sValiddityPeriodlow = 5;
    private static final int sValidityPeriodHight = 635040;
    private IBinder mBinder;
    private final Context mContext;
    private IOplusTelephonyExtCallback mTelephonyExtCallback;
    private static final boolean isQcomPlatform = SystemProperties.get("ro.boot.hardware", "unknow").toLowerCase().startsWith("qcom");
    private static final boolean isMTKPlatform = SystemProperties.get("ro.boot.hardware", "unknow").toLowerCase().startsWith("mt");
    private static OplusTelephonyManager sInstance = null;
    private boolean mNeedCallback = false;
    private ArrayList<ITelephonyExtCallback> mTelephonyExtCbList = new ArrayList<>();
    IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: android.telephony.OplusTelephonyManager$$ExternalSyntheticLambda0
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            OplusTelephonyManager.this.lambda$new$0();
        }
    };

    /* loaded from: classes.dex */
    public interface ITelephonyExtCallback {
        void onTelephonyEventReport(int i, int i2, Bundle bundle);

        void onbinderDied();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface SubscriptionResolverResult {
        void onFailure();

        void onSuccess(int i);
    }

    public static OplusTelephonyManager getInstance(Context context) {
        OplusTelephonyManager oplusTelephonyManager;
        synchronized (OplusTelephonyManager.class) {
            if (sInstance == null) {
                sInstance = new OplusTelephonyManager(context);
            }
            oplusTelephonyManager = sInstance;
        }
        return oplusTelephonyManager;
    }

    /* loaded from: classes.dex */
    public static class OemHookCallback {
        public void onAtCmdResp(int slotId, long token, String atCmd) {
            OplusTelephonyManager.log("OemHookCallback Not override onAtCmdResp");
        }

        public void onAtUrcInd(int slotId, String atCmd) {
            OplusTelephonyManager.log("OemHookCallback Not override onAtUrcInd");
        }

        public void onError(String e) {
            OplusTelephonyManager.log("OemHookCallback onError:" + e);
        }
    }

    public OplusTelephonyManager(Context context) {
        Context appContext = context.getApplicationContext();
        if (appContext != null) {
            if (Objects.equals(context.getFeatureId(), appContext.getFeatureId())) {
                this.mContext = appContext;
                return;
            } else {
                this.mContext = appContext.createFeatureContext(context.getFeatureId());
                return;
            }
        }
        this.mContext = context;
    }

    public static boolean isValidEvent(int event) {
        return event > 1000 && event < 8000;
    }

    public static int getRespMsgId(Message msg) {
        Bundle bundle = msg.getData();
        if (bundle != null) {
            return bundle.getInt("what");
        }
        return -1;
    }

    @Deprecated
    public Bundle requestForTelephonyEvent(int slotId, int eventId, Bundle data) {
        log("requestForTelephonyEvent.slotId:" + slotId + ", eventId:" + eventId);
        if (!SubscriptionManager.isValidSlotIndex(slotId) || !isValidEvent(eventId)) {
            return null;
        }
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                if (data == null) {
                    data = new Bundle();
                }
                data.putString("package", getOpPackageName());
                return telephonyExt.requestForTelephonyEvent(slotId, eventId, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public void registerCallback(String packageName, IOplusTelephonyExtCallback callback) {
        log("registerCallback:" + callback + ", package:" + packageName);
        try {
            if (!getOpPackageName().equals(packageName)) {
                log("registerCallback : packageName was not matched");
                return;
            }
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.registerCallback(packageName, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterCallback(IOplusTelephonyExtCallback callback) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.unRegisterCallback(callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOpPackageName() {
        Context context = this.mContext;
        if (context != null) {
            return context.getOpPackageName();
        }
        return "";
    }

    public static int getProductPlatform() {
        if (isQcomPlatform) {
            return 1;
        }
        if (isMTKPlatform) {
            return 2;
        }
        return 0;
    }

    public static boolean isMTKPlatform() {
        return isMTKPlatform;
    }

    public static boolean isQcomPlatform() {
        return isQcomPlatform;
    }

    public static void log(String string) {
        Rlog.d(TAG, string);
    }

    public int getCardType(int slotId) {
        if (!SubscriptionManager.isValidPhoneId(slotId)) {
            return -1;
        }
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -1;
            }
            int cardType = telephonyExt.getCardType(slotId);
            return cardType;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getSoftSimCardSlotId() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getSoftSimCardSlotId();
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean getIfTestSim(int slotId) {
        String[] MCCMNC_LIST = {"00101", "001001"};
        List<String> mccmncList = new ArrayList<>(Arrays.asList(MCCMNC_LIST));
        String mccmnc = TelephonyManager.getDefault().getSimOperatorNumericForPhone(slotId);
        log("getIfTestSim" + mccmnc);
        if (!TextUtils.isEmpty(mccmnc) && mccmncList.contains(mccmnc)) {
            return true;
        }
        return false;
    }

    public String getImsType(int slotId) {
        Bundle mBundle = requestForTelephonyEvent(slotId, EVENT_SIM_GET_IMS_TYPE, null);
        if (mBundle == null) {
            return null;
        }
        try {
            String result = mBundle.getString("result");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getImpi(int slotId) {
        Bundle mBundle = requestForTelephonyEvent(slotId, EVENT_SIM_GET_IMPI, null);
        if (mBundle == null) {
            return null;
        }
        try {
            String result = mBundle.getString("result");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getOpId(int slotId) {
        Bundle mBundle = requestForTelephonyEvent(slotId, EVENT_SIM_GET_OP_ID, null);
        if (mBundle == null) {
            return null;
        }
        try {
            String result = mBundle.getString("result");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHotswapState(int slotId) {
        Bundle mBundle = requestForTelephonyEvent(slotId, EVENT_SIM_GET_HOTSWAP_STATE, null);
        if (mBundle == null) {
            return null;
        }
        try {
            String result = mBundle.getString("result");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getOperatorSwitchEnable(int slotId) {
        Bundle mBundle = requestForTelephonyEvent(slotId, EVENT_SIM_GET_OPERATOR_SWITCH_ENABLE, null);
        if (mBundle != null) {
            return mBundle.getBoolean("result");
        }
        return false;
    }

    public int getVsimId() {
        Bundle mBundle = requestForTelephonyEvent(0, EVENT_SIM_GET_VSIM_ID, null);
        if (mBundle != null) {
            return mBundle.getInt("result");
        }
        return -1;
    }

    public int setVsimId(int slotId) {
        Bundle contentBundle = new Bundle();
        contentBundle.putInt(BUNDLE_CONTENT, slotId);
        Bundle mBundle = requestForTelephonyEvent(0, EVENT_SIM_SET_VSIM_ID, contentBundle);
        if (mBundle == null) {
            return -1;
        }
        try {
            int result = mBundle.getInt("result");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int setVsimStatus(int status) {
        Bundle contentBundle = new Bundle();
        contentBundle.putInt(BUNDLE_CONTENT, status);
        Bundle mBundle = requestForTelephonyEvent(0, EVENT_SIM_SET_VSIM_STATUS, contentBundle);
        if (mBundle == null) {
            return -1;
        }
        try {
            int result = mBundle.getInt("result");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int setSmat5gThermalControl(boolean status) {
        Bundle contentBundle = new Bundle();
        contentBundle.putBoolean(BUNDLE_CONTENT, status);
        Bundle bundle = requestForTelephonyEvent(0, EVENT_SMART5G_THERMAL_CONTROL, contentBundle);
        if (bundle == null) {
            return -1;
        }
        int result = bundle.getInt("result");
        return result;
    }

    public int setSmart5gStatus(boolean status) {
        Bundle contentBundle = new Bundle();
        contentBundle.putBoolean(BUNDLE_CONTENT, status);
        Bundle mBundle = requestForTelephonyEvent(0, EVENT_SET_SMART_5G_STATUS_ID, contentBundle);
        log("Smart5gStatus = " + status);
        if (mBundle == null) {
            return -1;
        }
        try {
            int result = mBundle.getInt("result");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int setDisplayNumberExt(String number, int subId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.setDisplayNumberExt(number, subId);
            }
            return -1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Deprecated
    public String getMcc(int slotId) {
        String pMcc = getCellIdentityOperator(slotId);
        if (pMcc != null && pMcc.length() >= 3) {
            return pMcc.substring(0, 3);
        }
        return null;
    }

    public String getCellIdentityOperator(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                log("getCellIdentityOperator");
                return telephonyExt.getCellIdentityOperator(slotId);
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getOperatorNumericForData(int subId) {
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            return null;
        }
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return null;
            }
            int phoneId = SubscriptionManager.getPhoneId(subId);
            if (!SubscriptionManager.isValidPhoneId(phoneId)) {
                return null;
            }
            String result = telephonyExt.getOperatorNumericForData(phoneId);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startMobileDataHongbaoPolicy(int time1, int time2, String value1, String value2) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.startMobileDataHongbaoPolicy(time1, time2, value1, value2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int[] calculateLengthOem(String messageBody, boolean use7bitOnly, int subId, int encodingType) {
        return OplusSmsMessage.calculateLengthOem(messageBody, use7bitOnly, subId, encodingType);
    }

    public ArrayList<String> divideMessageOem(String text, int encodingType, int subId) {
        if (OplusFeature.OPLUS_FEATURE_SMS_7BIT16BIT && (encodingType == 1 || encodingType == 3)) {
            return divideMessageOemInner(text, subId, encodingType);
        }
        return getSmsManagerForSubscriber(subId).divideMessage(text);
    }

    private ArrayList<String> divideMessageOemInner(String text, int subId, int encodingType) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }
        try {
            ArrayList<String> ret = OplusSmsMessage.oemFragmentText(text, subId, encodingType);
            if (ret != null) {
                log("divideMessageOem,subId=" + subId + " ret.size()=" + ret.size() + " encodingType=" + encodingType);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return SmsMessage.fragmentText(text, subId);
        }
    }

    public void sendMultipartTextMessageOem(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, int priority, boolean expectMore, int validityPeriod, int encodingType, int subId) {
        StringBuilder append = new StringBuilder().append("encodingType=").append(encodingType).append(", paltform=");
        boolean z = isQcomPlatform;
        log(append.append(z).toString());
        if (OplusFeature.OPLUS_FEATURE_SMS_7BIT16BIT && (encodingType == 1 || encodingType == 3)) {
            if (z) {
                sendMultipartTextMessageInternalOem(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true, priority, expectMore, validityPeriod, encodingType, subId);
                return;
            } else if (isMTKPlatform) {
                sendMultipartTextMessageInternalOem(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, false, priority, expectMore, validityPeriod, encodingType, subId);
                return;
            } else {
                log("sendMultipartTextMessageOem, unkonw platform");
                return;
            }
        }
        getSmsManagerForSubscriber(subId).sendMultipartTextMessage(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, priority, expectMore, validityPeriod);
    }

    private void sendMultipartTextMessageInternalOem(final String destinationAddress, final String scAddress, final List<String> parts, final List<PendingIntent> sentIntents, final List<PendingIntent> deliveryIntents, final boolean persistMessage, int priority, final boolean expectMore, int validityPeriod, final int encodingType, int subId) {
        PendingIntent deliveryIntent;
        List<PendingIntent> list;
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (parts == null || parts.size() < 1) {
            throw new IllegalArgumentException("Invalid message body");
        }
        int priority2 = (priority < 0 || priority > 3) ? -1 : priority;
        int validityPeriod2 = (validityPeriod < 5 || validityPeriod > sValidityPeriodHight) ? -1 : validityPeriod;
        if (parts.size() > 1) {
            final int finalPriority = priority2;
            final int finalValidity = validityPeriod2;
            if (persistMessage) {
                resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.OplusTelephonyManager.1
                    @Override // android.telephony.OplusTelephonyManager.SubscriptionResolverResult
                    public void onSuccess(int subId2) {
                        try {
                            IOplusTelephonyExt telephonyExt = OplusTelephonyManager.this.getIOplusTelephonyExt();
                            if (telephonyExt != null) {
                                telephonyExt.sendMultipartTextForSubscriberWithOptionsOem(subId2, null, destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage, finalPriority, expectMore, finalValidity, encodingType);
                            }
                        } catch (RemoteException e) {
                            Log.e(OplusTelephonyManager.TAG, "sendMultipartTextMessageInternal: Couldn't send SMS - " + e.getMessage());
                            OplusTelephonyManager.notifySmsError((List<PendingIntent>) sentIntents, 31);
                        }
                    }

                    @Override // android.telephony.OplusTelephonyManager.SubscriptionResolverResult
                    public void onFailure() {
                        OplusTelephonyManager.notifySmsError((List<PendingIntent>) sentIntents, 32);
                    }
                }, subId);
                return;
            }
            try {
                IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
                if (telephonyExt != null) {
                    list = sentIntents;
                    try {
                        telephonyExt.sendMultipartTextForSubscriberWithOptionsOem(subId, null, destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage, finalPriority, expectMore, finalValidity, encodingType);
                    } catch (RemoteException e) {
                        e = e;
                        Log.e(TAG, "sendMultipartTextMessageInternal (no persist): Couldn't send SMS - " + e.getMessage());
                        notifySmsError(list, 31);
                    }
                }
            } catch (RemoteException e2) {
                e = e2;
                list = sentIntents;
            }
        } else {
            PendingIntent sentIntent = null;
            if (sentIntents != null && sentIntents.size() > 0) {
                sentIntent = sentIntents.get(0);
            }
            if (deliveryIntents != null && deliveryIntents.size() > 0) {
                PendingIntent deliveryIntent2 = deliveryIntents.get(0);
                deliveryIntent = deliveryIntent2;
            } else {
                deliveryIntent = null;
            }
            sendTextMessageInternalOem(destinationAddress, scAddress, parts.get(0), sentIntent, deliveryIntent, persistMessage, priority2, expectMore, validityPeriod2, encodingType, subId);
        }
    }

    private void sendTextMessageInternalOem(final String destinationAddress, final String scAddress, final String text, final PendingIntent sentIntent, final PendingIntent deliveryIntent, final boolean persistMessage, int priority, final boolean expectMore, int validityPeriod, final int encodingType, int subId) {
        int priority2;
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        }
        if (priority >= 0 && priority <= 3) {
            priority2 = priority;
        } else {
            priority2 = -1;
        }
        final int finalPriority = priority2;
        final int finalValidity = (validityPeriod < 5 || validityPeriod > sValidityPeriodHight) ? -1 : validityPeriod;
        if (persistMessage) {
            resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.OplusTelephonyManager.2
                @Override // android.telephony.OplusTelephonyManager.SubscriptionResolverResult
                public void onSuccess(int subId2) {
                    try {
                        IOplusTelephonyExt telephonyExt = OplusTelephonyManager.this.getIOplusTelephonyExt();
                        if (telephonyExt != null) {
                            telephonyExt.sendTextForSubscriberWithOptionsOem(subId2, null, destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage, finalPriority, expectMore, finalValidity, encodingType);
                        }
                    } catch (RemoteException e) {
                        Log.e(OplusTelephonyManager.TAG, "sendTextMessageInternal: Couldn't send SMS, exception - " + e.getMessage());
                        OplusTelephonyManager.notifySmsError(sentIntent, 31);
                    }
                }

                @Override // android.telephony.OplusTelephonyManager.SubscriptionResolverResult
                public void onFailure() {
                    OplusTelephonyManager.notifySmsError(sentIntent, 32);
                }
            }, subId);
            return;
        }
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.sendTextForSubscriberWithOptionsOem(subId, null, destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage, finalPriority, expectMore, finalValidity, encodingType);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "sendTextMessageInternal(no persist): Couldn't send SMS, exception - " + e.getMessage());
            notifySmsError(sentIntent, 31);
        }
    }

    private void resolveSubscriptionForOperation(final SubscriptionResolverResult resolverResult, int subId) {
        boolean isSmsSimPickActivityNeeded = false;
        try {
            ISms iSms = getISmsService();
            if (iSms != null) {
                isSmsSimPickActivityNeeded = iSms.isSmsSimPickActivityNeeded(subId);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "resolveSubscriptionForOperation", ex);
        }
        if (isSmsSimPickActivityNeeded) {
            Log.d(TAG, "resolveSubscriptionForOperation isSmsSimPickActivityNeeded is true for calling package. ");
            try {
                ITelephony binder = getITelephony();
                IIntegerConsumer.Stub stub = new IIntegerConsumer.Stub() { // from class: android.telephony.OplusTelephonyManager.3
                    public void accept(int subId2) {
                        OplusTelephonyManager.this.sendResolverResult(resolverResult, subId2, true);
                    }
                };
                if (binder != null) {
                    binder.enqueueSmsPickResult((String) null, (String) null, stub);
                    return;
                }
                return;
            } catch (Exception ex2) {
                Log.e(TAG, "Unable to launch activity", ex2);
                sendResolverResult(resolverResult, subId, true);
                return;
            }
        }
        sendResolverResult(resolverResult, subId, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResolverResult(SubscriptionResolverResult resolverResult, int subId, boolean pickActivityShown) {
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            resolverResult.onSuccess(subId);
        } else if (!Compatibility.isChangeEnabled(GET_TARGET_SDK_VERSION_CODE_CHANGE_EXT) && !pickActivityShown) {
            resolverResult.onSuccess(subId);
        } else {
            resolverResult.onFailure();
        }
    }

    private static ISms getISmsService() {
        return ISms.Stub.asInterface(TelephonyFrameworkInitializer.getTelephonyServiceManager().getSmsServiceRegisterer().get());
    }

    private static ITelephony getITelephony() {
        ITelephony binder = ITelephony.Stub.asInterface(TelephonyFrameworkInitializer.getTelephonyServiceManager().getTelephonyServiceRegisterer().get());
        if (binder == null) {
            throw new RuntimeException("Could not find Telephony Service.");
        }
        return binder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifySmsError(PendingIntent pendingIntent, int error) {
        if (pendingIntent != null) {
            try {
                pendingIntent.send(error);
            } catch (PendingIntent.CanceledException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifySmsError(List<PendingIntent> pendingIntents, int error) {
        if (pendingIntents != null) {
            for (PendingIntent pendingIntent : pendingIntents) {
                notifySmsError(pendingIntent, error);
            }
        }
    }

    public static SmsManager getSmsManagerForSubscriber(int subId) {
        return SmsManager.getSmsManagerForSubscriptionId(subId);
    }

    public void sendRecoveryRequest(int slotIndex, int action) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.sendRecoveryRequest(slotIndex, action);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setViceCardGameMode(boolean enabled, String gamePkgName, String value) {
        if (gamePkgName == null) {
            Rlog.e(TAG, "gamePkgName cannot be null");
            return;
        }
        if (value == null) {
            Rlog.e(TAG, "init when value is null");
            value = "";
        }
        try {
            if (!OplusFeature.OPLUS_FEATURE_VICE_CARD_GAME_MODE) {
                Rlog.e(TAG, "OPLUS_FEATURE_VICE_CARD_GAME_MODE: false");
                return;
            }
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setViceCardGameMode(enabled, gamePkgName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPreferPhoneId(int phoneId) {
        try {
            if (!OplusFeature.OPLUS_FEATURE_DDS_SWITCH) {
                Rlog.e(TAG, "dds switch is disabled");
                return;
            }
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setPreferDdsSwitchPhoneId(phoneId);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setPreferPhoneId with error:" + e);
        }
    }

    public int getPreferPhoneId() {
        try {
        } catch (RemoteException e) {
            Rlog.e(TAG, "setPreferPhoneId with error:" + e);
        }
        if (!OplusFeature.OPLUS_FEATURE_DDS_SWITCH) {
            Rlog.e(TAG, "dds switch is disabled");
            return -1;
        }
        IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
        if (telephonyExt != null) {
            return telephonyExt.getPreferDdsSwitchPhoneId();
        }
        return -1;
    }

    public long getMtMmsTime(int phoneId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getReceiveMmsTime(phoneId);
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getMtMmsTime with error:" + e);
            return 0L;
        }
    }

    public long getMtSmsTime() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getReceiveSmsTime();
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getMtSmsTime with error:" + e);
            return 0L;
        }
    }

    public long getMtSmsTimebyPhoneId(int phoneId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getReceiveSmsTimebyPhoneId(phoneId);
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getMtSmsTimebyPhoneId with error:" + e);
            return 0L;
        }
    }

    public long getMoSmsTime() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getSendSmsTime();
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getMoSmsTime with error:" + e);
            return 0L;
        }
    }

    public int getMoSmsMapSize() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getSendSmsMapSize();
            }
            return 0;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getMoSmsMapSize with error:" + e);
            return 0;
        }
    }

    public long getReceiveMmsTime(int phoneId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getReceiveMmsTime(phoneId);
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getReceiveMmsTime with error:" + e);
            return 0L;
        }
    }

    public long getReceiveSmsTime() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getReceiveSmsTime();
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getReceiveSmsTime with error:" + e);
            return 0L;
        }
    }

    public long getReceiveSmsTimebyPhoneId(int phoneId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getReceiveSmsTimebyPhoneId(phoneId);
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getReceiveSmsTimebyPhoneId with error:" + e);
            return 0L;
        }
    }

    public long getSendSmsTime() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getSendSmsTime();
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getSendSmsTime with error:" + e);
            return 0L;
        }
    }

    public int getSendSmsMapSize() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getSendSmsMapSize();
            }
            return 0;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getSendSmsMapSize with error:" + e);
            return 0;
        }
    }

    public boolean isDataConnectivityPossible(int phoneId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.isDataConnectivityPossible(phoneId);
            }
            return false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "isDataConnectivityPossible with error:" + e);
            return false;
        }
    }

    public void sendDdsSwitchEvent() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.sendDdsSwitchEvent();
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "sendDdsSwitchEvent with error:" + e);
        }
    }

    public boolean isInNsa() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.isInNsa();
            }
            return false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "isInNsa with error:" + e);
            return false;
        }
    }

    public int[] getNsaSignalStrength() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getNsaSignalStrength();
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "getNsaSignalStrength with error:" + e);
        }
        return new int[]{0, 0};
    }

    public int[] getSignalInfoForGame(int rat, int phoneId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                SignalStrength signalStrength = telephonyExt.getOrigSignalStrength(phoneId);
                List<CellSignalStrength> cellSignalList = signalStrength.getCellSignalStrengths();
                for (int i = 0; i < cellSignalList.size(); i++) {
                    if (rat == 1 && (cellSignalList.get(i) instanceof CellSignalStrengthLte)) {
                        int rsrp = ((CellSignalStrengthLte) cellSignalList.get(i)).getRsrp();
                        int snr = ((CellSignalStrengthLte) cellSignalList.get(i)).getRssnr();
                        return new int[]{rsrp, snr};
                    }
                    if (rat == 3 && (cellSignalList.get(i) instanceof CellSignalStrengthNr)) {
                        int ssrsrp = ((CellSignalStrengthNr) cellSignalList.get(i)).getSsRsrp();
                        int sssinr = ((CellSignalStrengthNr) cellSignalList.get(i)).getSsSinr();
                        return new int[]{ssrsrp, sssinr};
                    }
                }
                return null;
            }
            return null;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getSignalInfoForGame with error:" + e);
            return null;
        }
    }

    public boolean updateIfGameFeatureBackOffSa() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.isBackOffSaEnabled();
            }
            return false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "updateIfGameFeatureBackOffSa with error:" + e);
            return false;
        }
    }

    public void triggerImsReregStatus(int phoneId, int action) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.triggerImsReregStatus(phoneId, action);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "triggerImsReregStatus with error:" + e);
        }
    }

    public void resetQcomImsRegTimer(int phoneId, int regTimer) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.resetImsRegTimer(phoneId, regTimer);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "resetQcomImsRegTimer with error:" + e);
        }
    }

    public void registerForImsRegTime(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.registerForImsRegTime(slotId);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "registerForImsRegTime with error:" + e);
        }
    }

    public void unregisterForImsRegTime(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.unregisterForImsRegTime(slotId);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "unregisterForImsRegTime with error:" + e);
        }
    }

    public long getActionExecuteTime(int slotIndex, int action) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return 0L;
            }
            long lastActionTime = telephonyExt.getActionExecuteTime(slotIndex, action);
            return lastActionTime;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public int getLastAction(int slotIndex) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getLastAction(slotIndex);
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void reportGameInfo(String gameInfo) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.reportGameInfo(gameInfo);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String reportGameErrorCauseToCenter() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.reportGameErrorCauseToCenter();
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public void setGameErrorCauseToCenter(String gameErrorCause) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setGameErrorCauseToCenter(gameErrorCause);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getCurrentDelayLevel() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getCurrentDelayLevel();
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public void setCurrentDelayLevel(int delayLevel) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setCurrentDelayLevel(delayLevel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPreferDdsSwitchPhoneId(int phoneId) {
        try {
            if (!OplusFeature.OPLUS_FEATURE_DDS_SWITCH) {
                Rlog.e(TAG, "dds switch is disabled");
                return;
            }
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setPreferDdsSwitchPhoneId(phoneId);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setPreferDdsSwitchPhoneId with error:" + e);
        }
    }

    public int getPreferDdsSwitchPhoneId() {
        try {
        } catch (RemoteException e) {
            Rlog.e(TAG, "getPreferDdsSwitchPhoneId with error:" + e);
        }
        if (!OplusFeature.OPLUS_FEATURE_DDS_SWITCH) {
            Rlog.e(TAG, "dds switch is disabled");
            return -1;
        }
        IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
        if (telephonyExt != null) {
            return telephonyExt.getPreferDdsSwitchPhoneId();
        }
        return -1;
    }

    public int[] getOrigSignalInfo(int rat, int phoneId) {
        IOplusTelephonyExt telephonyExt;
        try {
            telephonyExt = getIOplusTelephonyExt();
        } catch (RemoteException e) {
            Rlog.e(TAG, "getOrigSignalInfo with error:" + e);
        }
        if (telephonyExt == null || rat == 0) {
            return null;
        }
        if (rat == 2) {
            return telephonyExt.getNsaSignalStrength();
        }
        SignalStrength signalStrength = telephonyExt.getOrigSignalStrength(phoneId);
        List<CellSignalStrength> cellSignalList = signalStrength.getCellSignalStrengths();
        for (int i = 0; i < cellSignalList.size(); i++) {
            if (rat == 1 && (cellSignalList.get(i) instanceof CellSignalStrengthLte)) {
                int rsrp = ((CellSignalStrengthLte) cellSignalList.get(i)).getRsrp();
                int snr = ((CellSignalStrengthLte) cellSignalList.get(i)).getRssnr();
                return new int[]{rsrp, snr};
            }
            if (rat == 3 && (cellSignalList.get(i) instanceof CellSignalStrengthNr)) {
                int ssrsrp = ((CellSignalStrengthNr) cellSignalList.get(i)).getSsRsrp();
                int sssinr = ((CellSignalStrengthNr) cellSignalList.get(i)).getSsSinr();
                return new int[]{ssrsrp, sssinr};
            }
        }
        return null;
    }

    public boolean isBackOffSaEnabled() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.isBackOffSaEnabled();
            }
            return false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "isBackOffSaEnabled with error:" + e);
            return false;
        }
    }

    public void resetImsRegTimer(int phoneId, int regTimer) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.resetImsRegTimer(phoneId, regTimer);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "resetImsRegTimer with error:" + e);
        }
    }

    public void reportNetWorkLatency(Context context, String info) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (checkRevokeByGame(context) && telephonyExt != null) {
                telephonyExt.reportNetWorkLatency(info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void reportNetWorkLevel(Context context, String info) {
        try {
            if (!OplusFeature.OPLUS_FEATURE_LOG_GAME_ERR) {
                return;
            }
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (checkRevokeByGame(context) && telephonyExt != null) {
                telephonyExt.reportNetWorkLevel(info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void reportGameEnterOrLeave(Context context, int gameUid, String gamePackageName, boolean enter) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (checkRevokeByGame(context) && telephonyExt != null) {
                telephonyExt.reportGameEnterOrLeave(gameUid, gamePackageName, enter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkRevokeByGame(Context context) {
        boolean res = false;
        if (context != null) {
            ApplicationInfo pkgInfo = context.getApplicationInfo();
            if (pkgInfo != null && "com.oplus.cosa".equals(pkgInfo.packageName) && context.checkCallingOrSelfPermission("oplus.permission.OPLUS_COMPONENT_SAFE") == 0) {
                res = true;
            }
            if (!res) {
                Rlog.e(TAG, "checkRevokeByGame false,pkgInfo=" + pkgInfo);
            }
            return res;
        }
        Rlog.e(TAG, "checkRevokeByGame, err");
        return false;
    }

    public boolean isImsUseEnabled(int soltId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.isImsUseEnabled(soltId);
        } catch (Exception ex) {
            ex.printStackTrace();
            Rlog.e(TAG, "exception in isImsUseEnabled, so return false");
            return false;
        }
    }

    public boolean isImsValid(int soltId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.isImsValid(soltId);
        } catch (Exception ex) {
            ex.printStackTrace();
            Rlog.e(TAG, "exception in isImsValid, so return false");
            return false;
        }
    }

    public int getCurrentTransport(int soltId, int apnType) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -1;
            }
            return telephonyExt.getCurrentTransport(soltId, apnType);
        } catch (Exception ex) {
            ex.printStackTrace();
            Rlog.e(TAG, "exception in getCurrentTransport, so return invalid");
            return -1;
        }
    }

    public String getSelectConfig(int soltId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return null;
            }
            return telephonyExt.getSelectConfig(soltId);
        } catch (Exception ex) {
            ex.printStackTrace();
            Rlog.e(TAG, "exception in getSelectConfig, so return invalid");
            return null;
        }
    }

    public boolean setSelectConfig(int soltId, String configId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.setSelectConfig(soltId, configId);
        } catch (Exception ex) {
            ex.printStackTrace();
            Rlog.e(TAG, "exception in setSelectConfig, so return invalid");
            return false;
        }
    }

    public String[] getLteCdmaImsi(int phoneid) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getLteCdmaImsi(phoneid);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Rlog.e(TAG, "getLteCdmaImsi, err");
        return null;
    }

    public boolean isRequestingMms() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.isRequestingMms();
            }
            return false;
        } catch (RemoteException e) {
            Rlog.e(TAG, "isRequestingMms with error:" + e);
            return false;
        }
    }

    public long getReleaseMmsNetworkTime() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getReleaseMmsNetworkTime();
            }
            return 0L;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getReleaseMmsNetworkTime with error:" + e);
            return 0L;
        }
    }

    public String getWifiCellKpi() {
        try {
            Bundle mBundle = requestForTelephonyEvent(0, EVENT_GET_WIFI_CELL_KPI, null);
            if (mBundle == null) {
                return "";
            }
            String result = mBundle.getString("result");
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getFullIccId(int phoneId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getFullIccId(phoneId);
            }
            return null;
        } catch (Exception e) {
            Rlog.e(TAG, "error get full iccid", e);
            return null;
        }
    }

    public void changeEsimStatus(int newState) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.changeEsimStatus(newState);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getNrModeToCheck(int phoneId, Bundle bundle) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.getNrModeToCheck(phoneId, bundle);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkInvokeByCtReg(Context context) {
        if (context != null) {
            ApplicationInfo pkgInfo = context.getApplicationInfo();
            Rlog.e(TAG, "checkInvokeByCtReg : pkg = " + pkgInfo.packageName);
            if (CT_AUTO_IMS_REG_PACKAGE.length() <= 0 || !CT_AUTO_IMS_REG_PACKAGE.equals(pkgInfo.packageName)) {
                if ((OEM_REGSERVICE.length() > 0 && OEM_REGSERVICE.equals(pkgInfo.packageName)) || "com.oplus.ctautoregist".equals(pkgInfo.packageName) || "com.oplus.regservice".equals(pkgInfo.packageName)) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public Bundle getCellInfo(Context context, int slotId) {
        if (!checkInvokeByCtReg(context)) {
            return null;
        }
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getCellInfo(slotId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IOplusTelephonyExt getIOplusTelephonyExt() {
        return IOplusTelephonyExt.Stub.asInterface(ServiceManager.getService(SERVICE_NAME));
    }

    public boolean getRoamingReduction() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean roamingReduction = telephonyExt.getRoamingReduction();
            return roamingReduction;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getFiveGUperLayerIndAvailable(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -3;
            }
            int mUpperLayerIndInfo = telephonyExt.getFiveGUperLayerIndAvailable(slotId);
            return mUpperLayerIndInfo;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -3;
        }
    }

    public int getNrBearerAllocation(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -3;
            }
            int mBearerStatus = telephonyExt.getNrBearerAllocation(slotId);
            return mBearerStatus;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -3;
        }
    }

    public boolean getNrConnect() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean connect = telephonyExt.getNrConnect();
            return connect;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean getIsNrAvailable() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean isNrAvailable = telephonyExt.getIsNrAvailable();
            return isNrAvailable;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean getHasNrSecondaryServingCell() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean hasNrSecondaryServingCell = telephonyExt.getHasNrSecondaryServingCell();
            return hasNrSecondaryServingCell;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getPreferredDefaultNetMode(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            Log.d(TAG, "getPreferredDefaultNetMode call successful");
            if (telephonyExt == null) {
                return -1;
            }
            int networkMode = telephonyExt.getPreferredDefaultNetMode(slotId);
            return networkMode;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public int getNewPreferredNetworkMode(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            Log.d(TAG, "getNewPreferredNetworkMode call successful");
            if (telephonyExt == null) {
                return -1;
            }
            int networkMode = telephonyExt.getNewPreferredNetworkMode(slotId);
            return networkMode;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public String getNetworkConfig(String key) {
        Rlog.d(TAG, "getNetworkConfig key=" + key);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getNetworkConfig(key);
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public boolean prioritizeDefaultDataSubscription(boolean enable) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean result = telephonyExt.prioritizeDefaultDataSubscription(enable);
            Rlog.d(TAG, "prioritizeDefaultDataSubscription result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "enableCellularDataPrio fail: " + e.getMessage());
            return false;
        }
    }

    public boolean enableCellularDataPrio(String packageName) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean result = telephonyExt.enableCellularDataPrio(packageName);
            Rlog.d(TAG, "enableCellularDataPrio result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "enableCellularDataPrio fail: " + e.getMessage());
            return false;
        }
    }

    public boolean disableCellularDataPrio(String packageName) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean result = telephonyExt.disableCellularDataPrio(packageName);
            Rlog.d(TAG, "disableCellularDataPrio result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "disableCellularDataPrio fail: " + e.getMessage());
            return false;
        }
    }

    public boolean setDataPrioLevel(long rat, long uplink, long downlink) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean result = telephonyExt.setDataPrioLevel(rat, uplink, downlink);
            Rlog.d(TAG, "setDataPrioLevel result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "setDataPrioLevel fail: " + e.getMessage());
            return false;
        }
    }

    public boolean isInDelayOOSState(int phoneId) {
        Rlog.d(TAG, "isInDelayOOSState" + phoneId);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.isInDelayOOSState(phoneId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTelephonyPowerState() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getTelephonyPowerState();
            }
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public double getTelephonyPowerLost() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getTelephonyPowerLost();
            }
            return -1.0d;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1.0d;
        }
    }

    public long[] getModemTxTime() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getModemTxTime();
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean isAlreadyUpdated() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.isAlreadyUpdated();
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean getDataCallListAction(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.getDataCallListAction(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean disableEndc(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.disableEndc(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean setAllowedNetworkTypesBitmap(int slotId, int networkTypeBitmap) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.setAllowedNetworkTypesBitmap(slotId, networkTypeBitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean setPreferredNetworkType(int slotId, int networkType) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.setPreferredNetworkType(slotId, networkType);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getPreferredNetworkType(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -1;
            }
            return telephonyExt.getPreferredNetworkType(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean cleanupConnections(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.cleanupConnections(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getSaMode(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -1;
            }
            return telephonyExt.getSaMode(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean setSaMode(int slotId, int mode) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.setSaMode(slotId, mode);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean psDetachAttachAction(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.psDetachAttachAction(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isApnInException(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.isApnInException(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean cleanApnState(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.cleanApnState(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean lockCellAction(int slotId, String cmdStr) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.lockCellAction(slotId, cmdStr);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean radioPower(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.radioPower(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean setCellBlackList(int type, long cellid) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.setCellBlackList(type, cellid);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delCellBlacklist(int type, long cellid) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.delCellBlacklist(type, cellid);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean clearCellBlacklist(int type) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.clearCellBlacklist(type);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean resetRadioSmooth(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.resetRadioSmooth(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isApnEnabled(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.isApnEnabled(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getRealNrState(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return 0;
            }
            return telephonyExt.getRealNrState(slotId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public boolean reregisterNetwork(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.reregisterNetwork(slotId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setSaLtePingpongState(int slotId, int isLteNrPingPong) {
        log("setSaLtePingpongState.slotId:" + slotId + ", isLteNrPingPong:" + isLteNrPingPong);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.setSaLtePingpongState(slotId, isLteNrPingPong);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Messenger getRemoteMessenger() throws RemoteException {
        IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
        if (telephonyExt != null) {
            return new Messenger(telephonyExt.getRemoteMessenger());
        }
        return null;
    }

    public void setVoNrState(int phoneId, boolean enabled, boolean isFromUi, Message onComplete) {
        Rlog.e(TAG, "setVoNrState : " + onComplete);
        Handler target = onComplete.getTarget();
        Messenger client = new Messenger(target);
        try {
            Messenger remoteMger = getRemoteMessenger();
            if (remoteMger != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("what", onComplete.what);
                bundle.putInt("phoneId", phoneId);
                bundle.putBoolean("enabled", enabled);
                bundle.putBoolean("isFromUi", isFromUi);
                bundle.putAll(onComplete.getData());
                Message msgToRemote = target.obtainMessage(EVENT_CALL_SET_VONR_STATE);
                msgToRemote.setData(bundle);
                msgToRemote.replyTo = client;
                remoteMger.send(msgToRemote);
            } else {
                Rlog.e(TAG, "setVoNrState error");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void setVoNrState(int phoneId, boolean enabled, Message onComplete) {
        setVoNrState(phoneId, enabled, true, onComplete);
    }

    public void getVoNrState(int phoneId, boolean isFromUi, Message onComplete) {
        Rlog.e(TAG, "getVoNrState : " + onComplete);
        Handler target = onComplete.getTarget();
        Messenger client = new Messenger(target);
        try {
            Messenger remoteMger = getRemoteMessenger();
            if (remoteMger != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("what", onComplete.what);
                bundle.putInt("phoneId", phoneId);
                bundle.putBoolean("isFromUi", isFromUi);
                Message msgToRemote = target.obtainMessage(EVENT_CALL_GET_VONR_STATE);
                msgToRemote.setData(bundle);
                msgToRemote.replyTo = client;
                remoteMger.send(msgToRemote);
            } else {
                Rlog.e(TAG, "getVoNrState error");
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void getVoNrState(int phoneId, Message onComplete) {
        getVoNrState(phoneId, true, onComplete);
    }

    public boolean getVoNrVisible(int phoneId) {
        Rlog.e(TAG, "getVoNrVisible : on " + phoneId);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getVoNrVisible(phoneId);
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean isNotInGameMode(Context context) {
        if (context == null) {
            log("isNotInGameMode context is null, return true");
            return true;
        }
        String gameModeValue = Settings.Global.getString(context.getContentResolver(), GAME_SPACE_MODE_VALUE);
        if (TextUtils.isEmpty(gameModeValue) || !"1".equals(gameModeValue)) {
            Rlog.d(TAG, "isNotInGameMode not in GameMode");
            return true;
        }
        if (TextUtils.isEmpty(Settings.Global.getString(context.getContentResolver(), GAME_SPACE_MODE_SAVEGAME))) {
            Rlog.d(TAG, "isNotInGameMode SystemProperties savedgame is null");
            return true;
        }
        return false;
    }

    public int getUserSaMode() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -1;
            }
            return telephonyExt.getUserSaMode();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public int getSubId(int slotIndex) {
        Rlog.e(TAG, "getSubId for: " + slotIndex);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getSubId(slotIndex);
            }
            return -1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public void enableUiccApplications(int subId, boolean enabled) {
        Rlog.e(TAG, "enableUiccApplications for: " + subId + " enabled:" + enabled);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.enableUiccApplications(subId, enabled);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean canSupSlotSaSupport() {
        Rlog.e(TAG, "canSupSlotSaSupport ...");
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.canSupSlotSaSupport();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public void setSupSlotSaSupport(boolean support) {
        Rlog.e(TAG, "setSupSlotSaSupport : support = " + support);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setSupSlotSaSupport(support);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean getSupSlotSaSupport() {
        Rlog.e(TAG, "getSupSlotSaSupport ...");
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getSupSlotSaSupport();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public boolean getTempDdsSwitch() {
        Rlog.e(TAG, "getTempDdsSwitch ...");
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getTempDdsSwitch();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public void setNrModeChangedAllow(boolean enable) {
        Rlog.e(TAG, "setNrModeChangedAllow : enable = " + enable);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setNrModeChangedAllow(enable);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean getNrModeChangedAllow() {
        Rlog.e(TAG, "getNrModeChangedAllow ...");
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getNrModeChangedAllow();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    public Bundle getNrModeChangedEvent() {
        Rlog.e(TAG, "getNrModeChangedInfo ...");
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getNrModeChangedEvent();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Bundle();
    }

    public boolean connect(boolean needCallback) {
        Rlog.d(TAG, "connect " + needCallback);
        this.mBinder = ServiceManager.getService(SERVICE_NAME);
        this.mNeedCallback = needCallback;
        if (isBinderAlive()) {
            if (this.mNeedCallback) {
                if (this.mTelephonyExtCallback == null) {
                    this.mTelephonyExtCallback = new IOplusTelephonyExtCallback.Stub() { // from class: android.telephony.OplusTelephonyManager.4
                        @Override // com.android.internal.telephony.IOplusTelephonyExtCallback
                        public void onTelephonyEventReport(int slotId, int eventId, Bundle data) {
                            synchronized (OplusTelephonyManager.this.mTelephonyExtCbList) {
                                Iterator it = OplusTelephonyManager.this.mTelephonyExtCbList.iterator();
                                while (it.hasNext()) {
                                    ITelephonyExtCallback cb = (ITelephonyExtCallback) it.next();
                                    cb.onTelephonyEventReport(slotId, eventId, data);
                                }
                            }
                        }
                    };
                }
                registerCallback(this.mContext.getOpPackageName(), this.mTelephonyExtCallback);
            }
            try {
                this.mBinder.linkToDeath(this.mDeathRecipient, 0);
                Rlog.d(TAG, "linkTo " + this.mBinder);
            } catch (RemoteException e) {
                Rlog.e(TAG, "failed to link binder: " + e);
            }
        }
        return isBinderAlive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        Log.e(TAG, "DeathRecipient triggered, binder died.");
        notifyBinderDeath();
    }

    public void registerCallbackExternal(ITelephonyExtCallback cb) {
        synchronized (this.mTelephonyExtCbList) {
            if (!this.mTelephonyExtCbList.contains(cb)) {
                this.mTelephonyExtCbList.add(cb);
            }
        }
    }

    public void unregisterCallbackExternal(ITelephonyExtCallback cb) {
        synchronized (this.mTelephonyExtCbList) {
            if (this.mTelephonyExtCbList.contains(cb)) {
                this.mTelephonyExtCbList.remove(cb);
            }
        }
    }

    public boolean isBinderAlive() {
        IBinder iBinder = this.mBinder;
        return iBinder != null && iBinder.isBinderAlive();
    }

    private void notifyBinderDeath() {
        synchronized (this.mTelephonyExtCbList) {
            Iterator<ITelephonyExtCallback> it = this.mTelephonyExtCbList.iterator();
            while (it.hasNext()) {
                ITelephonyExtCallback cb = it.next();
                cb.onbinderDied();
            }
        }
        IBinder iBinder = this.mBinder;
        if (iBinder != null) {
            iBinder.unlinkToDeath(this.mDeathRecipient, 0);
        }
        this.mBinder = null;
    }

    public void addGeoFenceEventCallBack(OplusGeoFenceEventCallBack cb) {
        Rlog.e(TAG, "addGeoFenceEventCallBack cb = " + cb);
        if (cb == null) {
            Rlog.e(TAG, "addGeoFenceEventCallBack cb cant be null");
            return;
        }
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                cb.linkToDeath(telephonyExt);
                IOplusGeoFenceEventCallBack binder = cb.getGeoFenceEventCbBinder();
                telephonyExt.addGeoFenceEventCallBack(binder);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "addGeoFenceEventCallBack: " + ex.getMessage());
        }
    }

    public void removeGeoFenceEventCallBack(OplusGeoFenceEventCallBack cb) {
        Rlog.e(TAG, "removeGeoFenceEventCallBack cb = " + cb);
        if (cb == null) {
            Rlog.e(TAG, "removeGeoFenceEventCallBack cb cant be null");
            return;
        }
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                cb.unlinkToDeath();
                IOplusGeoFenceEventCallBack binder = cb.getGeoFenceEventCbBinder();
                telephonyExt.removeGeoFenceEventCallBack(binder);
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "removeGeoFenceEventCallBack: " + ex.getMessage());
        }
    }

    public List<String> getEcholocateMetrics(int index) {
        Rlog.e(TAG, "getEcholocateMetrics ...");
        List<String> echolocateMetrics = new ArrayList<>();
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getEcholocateMetrics(index);
            }
            return echolocateMetrics;
        } catch (Exception ex) {
            ex.printStackTrace();
            Rlog.e(TAG, "getEcholocateMetrics fail: " + ex.getMessage());
            return echolocateMetrics;
        }
    }

    private void enforceOplusPhoneState(String message) {
        this.mContext.enforceCallingOrSelfPermission("com.oplus.permission.safe.PHONE", message);
    }

    public boolean isVolteEnabledByPlatform(int phoneId) {
        enforceOplusPhoneState("isVolteEnabledByPlatform");
        return ImsManager.getInstance(this.mContext, phoneId).isVolteEnabledByPlatform();
    }

    public boolean oplusGetIccLockEnabled(int slotId) {
        int[] subIds;
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null && (subIds = SubscriptionManager.getSubId(slotId)) != null && subIds.length != 0) {
                return telephony.isIccLockEnabled(subIds[0]);
            }
            return false;
        } catch (Exception ex) {
            Rlog.e(TAG, "isIccLockEnabled fail: " + ex.getMessage());
        }
        return false;
    }

    public boolean isTestCard(int slotId) {
        return getCardType(slotId) == 9;
    }

    public String getIccCardType(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getIccCardType(slotId);
            }
            return null;
        } catch (Exception ex) {
            Rlog.e(TAG, "getIccCardType fail: " + ex.getMessage());
            return null;
        }
    }

    public int getSubState(int i) {
        int i2 = -1;
        try {
            List availableSubscriptionInfoList = SubscriptionManager.from(this.mContext).getAvailableSubscriptionInfoList();
            if (availableSubscriptionInfoList != null) {
                Iterator it = availableSubscriptionInfoList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SubscriptionInfo subscriptionInfo = (SubscriptionInfo) it.next();
                    if (subscriptionInfo.getSubscriptionId() == i) {
                        i2 = subscriptionInfo.areUiccApplicationsEnabled() ? 1 : 0;
                        break;
                    }
                }
            } else {
                i2 = -2;
            }
            Rlog.d(TAG, "getSubState--subId :" + i + " state: " + i2);
            return i2;
        } catch (Exception e) {
            Rlog.e(TAG, "oplusGetIccId fail: " + e.getMessage());
            return -1;
        }
    }

    public boolean isVtEnabledByPlatform(int phoneId) {
        enforceOplusPhoneState("isVtEnabledByPlatform");
        return ImsManager.getInstance(this.mContext, phoneId).isVtEnabledByPlatform();
    }

    public boolean isWfcEnabledByPlatform(int phoneId) {
        enforceOplusPhoneState("isWfcEnabledByPlatform");
        return ImsManager.getInstance(this.mContext, phoneId).isWfcEnabledByPlatform();
    }

    public boolean isUriFileExist(String vUri) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.isUriFileExist(vUri);
            }
            return false;
        } catch (Exception ex) {
            Rlog.e(TAG, "isUriFileExist fail: " + ex.getMessage());
            return false;
        }
    }

    public String getScAddressGemini(int slotId) {
        int[] subIds = SubscriptionManager.getSubId(slotId);
        try {
            ISms iSms = getISmsService();
            if (subIds == null || subIds.length < 1 || iSms == null) {
                return null;
            }
            String smsc = iSms.getSmscAddressFromIccEfForSubscriber(subIds[0], (String) null);
            return smsc;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setScAddressGemini(int slotId, String smsc) {
        int[] subIds = SubscriptionManager.getSubId(slotId);
        try {
            ISms iSms = getISmsService();
            if (subIds == null || subIds.length < 1 || iSms == null || !iSms.setSmscAddressOnIccEfForSubscriber(smsc, subIds[0], (String) null)) {
                Rlog.d(TAG, "setScAddress fail");
            } else {
                Rlog.d(TAG, "setScAddress successful");
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setDataRoamingEnabled(int slotId, boolean enable) {
        try {
            int[] subId = SubscriptionManager.getSubId(slotId);
            ITelephony binder = getITelephony();
            if (subId != null && subId.length > 0 && binder != null) {
                binder.setDataRoamingEnabled(subId[0], enable);
            }
        } catch (Exception ex) {
            Rlog.e(TAG, "setDataRoamingEnabled fail: " + ex.getMessage());
        }
    }

    public void setDualLteEnabled(boolean z) {
        enforceOplusPhoneState("setDualLteEnabled");
        Rlog.d(TAG, "setDualLteEnabled enable" + z);
        Settings.Global.putInt(this.mContext.getContentResolver(), DUAL_LTE_SWITCH, z ? 1 : 0);
    }

    public boolean isDualLteEnabled() {
        enforceOplusPhoneState("isDualLteEnabled");
        this.mContext.getPackageManager();
        int defaultValue = Settings.Global.getInt(this.mContext.getContentResolver(), DUAL_LTE_SWITCH_DEFAULT, 0);
        int value = Settings.Global.getInt(this.mContext.getContentResolver(), DUAL_LTE_SWITCH, defaultValue);
        boolean enable = value != 0;
        Rlog.d(TAG, "isDualLteEnabled defaultValue = " + defaultValue + " value = " + value + " enable = " + enable);
        return enable;
    }

    public String getOemSpn(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getOemSpn(slotId);
            }
            return null;
        } catch (Exception ex) {
            Rlog.e(TAG, "getOemSpn fail: " + ex.getMessage());
            return null;
        }
    }

    public CellLocation getCellLocation(int slotId) {
        SeempLog.record(49);
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                Rlog.d(TAG, "getCellLocation returning null because telephonyExt is null");
                return null;
            }
            CellIdentity cellIdentity = telephonyExt.getCellIdentity(slotId, this.mContext.getOpPackageName(), this.mContext.getAttributionTag());
            CellLocation cl = null;
            if (cellIdentity != null) {
                cl = cellIdentity.asCellLocation();
            }
            if (cl != null && !cl.isEmpty()) {
                return cl;
            }
            Rlog.d(TAG, "getCellLocation returning null because CellLocation is empty or phone type doesn't match CellLocation type");
            return null;
        } catch (RemoteException ex) {
            Rlog.d(TAG, "getCellLocation returning null due to RemoteException " + ex);
            return null;
        }
    }

    public boolean setNrMode(int slotId, int mode, String caller) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.setNrMode(slotId, mode, caller);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setSaMode: " + e.getMessage());
            return false;
        }
    }

    public boolean set5gIconDelayTimer(int slotId, int delayTimer) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.set5gIconDelayTimer(slotId, delayTimer);
        } catch (RemoteException e) {
            Rlog.e(TAG, "set5gIconDelayTimer: " + e.getMessage());
            return false;
        }
    }

    public boolean setNwCongestionCfg(int slotId, int cmd, byte[] data, int datalen) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            telephonyExt.setNwCongestionCfg(slotId, cmd, data, datalen);
            return true;
        } catch (RemoteException e) {
            Rlog.e(TAG, "setNwCongestionCfg: " + e.getMessage());
            return false;
        }
    }

    public int getPreferSubId() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -1;
            }
            return telephonyExt.getPreferSubId();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    private IPlmnCarrierConfigLoader getIPlmnCarrierConfigLoader() {
        return IPlmnCarrierConfigLoader.Stub.asInterface(ServiceManager.getService(PLMN_CARRIER_CONFIG_SERVICE));
    }

    public PersistableBundle getPlmnConfigForSlotId(int slotId) {
        String callingPackage = this.mContext.getOpPackageName();
        Rlog.d(TAG, "getConfigForslot: " + slotId + ", " + callingPackage);
        try {
            IPlmnCarrierConfigLoader plmnCarrierConfigLoader = getIPlmnCarrierConfigLoader();
            if (plmnCarrierConfigLoader != null) {
                return plmnCarrierConfigLoader.getConfigForSlotId(slotId, callingPackage);
            }
            return null;
        } catch (RemoteException ex) {
            Rlog.e(TAG, "getPlmnConfigForSlotId returning null due to RemoteException " + ex);
            return null;
        }
    }

    public boolean registerTelephonyCallbackExt(int slotId, Executor executor, TelephonyCallbackExt callback) {
        return registerTelephonyCallbackExt(slotId, false, false, executor, callback);
    }

    public boolean registerTelephonyCallbackExt(int slotId, boolean renounceFineLocationAccess, boolean renounceCoarseLocationAccess, Executor executor, TelephonyCallbackExt callback) {
        Context context = this.mContext;
        if (context == null || executor == null || callback == null) {
            Rlog.e(TAG, "mContext, callback or executor must be non-null");
            return false;
        }
        TelephonyRegistryManagerExt telephonyRegistryManagerExt = (TelephonyRegistryManagerExt) context.getSystemService(TelephonyRegistryManagerExt.TELEPHONY_REGISTRY_EXT);
        if (telephonyRegistryManagerExt != null) {
            return telephonyRegistryManagerExt.registerTelephonyCallbackExt(renounceFineLocationAccess, renounceCoarseLocationAccess, executor, slotId, getOpPackageName(), this.mContext.getAttributionTag(), callback, getIOplusTelephonyExt() != null);
        }
        Rlog.e(TAG, "registerTelephonyCallbackExt failed");
        return false;
    }

    public boolean unregisterTelephonyCallbackExt(int slotId, TelephonyCallbackExt callback) {
        if (this.mContext == null || callback == null || callback.callback == null) {
            Rlog.e(TAG, "mContext, callback must be non-null");
            return false;
        }
        TelephonyRegistryManagerExt telephonyRegistryManagerExt = (TelephonyRegistryManagerExt) this.mContext.getSystemService(TelephonyRegistryManagerExt.TELEPHONY_REGISTRY_EXT);
        if (telephonyRegistryManagerExt != null) {
            return telephonyRegistryManagerExt.unregisterTelephonyCallbackExt(slotId, getOpPackageName(), this.mContext.getAttributionTag(), callback, getIOplusTelephonyExt() != null);
        }
        Rlog.e(TAG, "unregisterTelephonyCallbackExt failed");
        return false;
    }

    public boolean rsrpBackoff(int phoneId, String plmn, int arfch, int pci, int offset) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean result = telephonyExt.rsrpBackoff(phoneId, plmn, arfch, pci, offset);
            Rlog.d(TAG, "rsrpBackoff result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "rsrpBackoff fail: " + e.getMessage());
            return false;
        }
    }

    public boolean resetRsrpBackoff(int phoneId, String plmn, int arfch, int pci) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean result = telephonyExt.resetRsrpBackoff(phoneId, plmn, arfch, pci);
            Rlog.d(TAG, "resetRsrpBackoff result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "resetRsrpBackoff fail: " + e.getMessage());
            return false;
        }
    }

    public boolean barCell(int phoneId, int rat, int arfch, int pci, long barTime) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean result = telephonyExt.barCell(phoneId, rat, arfch, pci, barTime);
            Rlog.d(TAG, "barCell result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "barCell fail: " + e.getMessage());
            return false;
        }
    }

    public boolean resetBarCell(int phoneId, int rat, int arfch, int pci) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean result = telephonyExt.resetBarCell(phoneId, rat, arfch, pci);
            Rlog.d(TAG, "resetBarCell result: " + result);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "resetBarCell fail: " + e.getMessage());
            return false;
        }
    }

    public SignalStrength getOrigSignalStrength(int phoneId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return null;
            }
            return telephonyExt.getOrigSignalStrength(phoneId);
        } catch (RemoteException e) {
            Rlog.e(TAG, "getOrigSignalStrength fail: " + e.getMessage());
            return null;
        }
    }

    private IVirtualCommService getIVirtualCommService() {
        return IVirtualCommService.Stub.asInterface(ServiceManager.getService(VC_SERVICE_NAME));
    }

    public boolean isVirtualCommSupport() {
        try {
            IVirtualCommService virtualCommService = getIVirtualCommService();
            if (virtualCommService == null) {
                return false;
            }
            boolean result = virtualCommService.isVirtualCommSupport();
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "isVirtualCommSupport fail: " + e.getMessage());
            return false;
        }
    }

    public boolean hasVirtualCommCapability(int queryType, int capability) {
        try {
            IVirtualCommService virtualCommService = getIVirtualCommService();
            if (virtualCommService == null) {
                return false;
            }
            boolean result = virtualCommService.hasVirtualCommCapability(queryType, capability);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "isFeatureEnable fail: " + e.getMessage());
            return false;
        }
    }

    public VirtualCommServiceState getVirtualCommState() {
        try {
            IVirtualCommService virtualCommService = getIVirtualCommService();
            if (virtualCommService == null) {
                return null;
            }
            VirtualCommServiceState result = virtualCommService.getVirtualCommState();
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getVirtualcommDeviceType fail: " + e.getMessage());
            return null;
        }
    }

    public int getVirtualcommDeviceType() {
        try {
            IVirtualCommService virtualCommService = getIVirtualCommService();
            if (virtualCommService == null) {
                return 0;
            }
            int result = virtualCommService.getVirtualcommDeviceType();
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getVirtualcommDeviceType fail: " + e.getMessage());
            return 0;
        }
    }

    public int enableVirtualcomm(boolean enabled) {
        Rlog.d(TAG, "enableVirtualcomm: " + enabled);
        try {
            IVirtualCommService virtualCommService = getIVirtualCommService();
            if (virtualCommService == null) {
                return -1;
            }
            int result = virtualCommService.enableVirtualcomm(enabled);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "enableVirtualcomm fail: " + e.getMessage());
            return -1;
        }
    }

    public int enableWifiP2PByVirtualcomm(boolean enabled) {
        try {
            IVirtualCommService virtualCommService = getIVirtualCommService();
            if (virtualCommService == null) {
                return -1;
            }
            int result = virtualCommService.enableWifiP2P(enabled);
            return result;
        } catch (RemoteException e) {
            Rlog.e(TAG, "enableWifiP2P fail: " + e.getMessage());
            return -1;
        }
    }

    public boolean isCallSharedSupport() {
        boolean ret = isVirtualCommSupport() && !OplusFeature.OPLUS_FEATURE_RADIO_VIRTUALMODEM_DATA_ONLY;
        Rlog.d(TAG, "isCallSharedSupport:" + ret);
        return ret;
    }

    public boolean isSmsSharedSupport() {
        boolean ret = isVirtualCommSupport() && !OplusFeature.OPLUS_FEATURE_RADIO_VIRTUALMODEM_DATA_ONLY;
        Rlog.d(TAG, "isSmsSharedSupport:" + ret);
        return ret;
    }

    public boolean isDataSharedSupport() {
        boolean ret = isVirtualCommSupport();
        Rlog.d(TAG, "isDataSharedSupport:" + ret);
        return ret;
    }

    public boolean isSupport700M(int type) {
        if ((type == 0 || type == 1) && !OplusFeature.OPLUS_FEATURE_NOT_SUPPORT_N28) {
            return true;
        }
        return false;
    }

    public boolean isSupportCarrierAggregation() {
        if (OplusFeature.OPLUS_FEATURE_NOT_SUPPORT_NRCA) {
            return false;
        }
        return true;
    }

    public boolean isSupportForeignRoaming() {
        if (!OplusFeature.OPLUS_FEATURE_SA_BAND_CFG && !OplusFeature.OPLUS_FEATURE_SUPPORT_CHINA_ROAMING) {
            return false;
        }
        return true;
    }

    public boolean isSupportR165G() {
        if (!OplusFeature.OPLUS_FEATURE_SUPPORT_R16) {
            return false;
        }
        return true;
    }

    public void simProfileRefresh(int slotId, boolean fileChange, int[] fileList) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.simProfileRefresh(slotId, fileChange, fileList);
                Rlog.d(TAG, "simProfileRefresh " + slotId);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "simProfileRefresh fail: " + e.getMessage());
        }
    }

    public boolean updateSmartDdsSwitch(boolean changeNext) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            boolean ret = telephonyExt.updateSmartDdsSwitch(changeNext);
            Rlog.d(TAG, "updateSmartDdsSwitch " + changeNext + " return: " + ret);
            return ret;
        } catch (RemoteException e) {
            Rlog.e(TAG, "updateSmartDdsSwitch fail: " + e.getMessage());
            return false;
        }
    }

    public int getDataNetworkInternetCid(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -1;
            }
            int cid = telephonyExt.getDataNetworkInternetCid(slotId);
            Rlog.d(TAG, "getDataNetworkInternetCid " + slotId + " return: " + cid);
            return cid;
        } catch (RemoteException e) {
            Rlog.e(TAG, "updateSmartDdsSwitch fail: " + e.getMessage());
            return -1;
        }
    }

    public boolean setGameAccelerateEnable(boolean enabled) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.setGameAccelerateEnable(enabled);
        } catch (RemoteException e) {
            Rlog.e(TAG, "setGameAccelerateEnable fail: " + e.getMessage());
            return false;
        }
    }

    public boolean isGameAccelerateEnable() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return false;
            }
            return telephonyExt.isGameAccelerateEnable();
        } catch (RemoteException e) {
            Rlog.e(TAG, "isGameAccelerateEnable fail: " + e.getMessage());
            return false;
        }
    }

    public boolean isGameLinkBoostFeatureEnable() {
        if (OplusFeature.OPLUS_FEATURE_GAME_ACCELERATE) {
            return true;
        }
        return false;
    }

    public int getSimLockStateForRSU(int phoneId) {
        try {
            IOplusTelephonyInternalExt telephony = getIOplusTelephonyInternalEx();
            if (telephony == null) {
                return -1;
            }
            return telephony.getSimLockStateForRSU(phoneId);
        } catch (RemoteException e) {
            return -2;
        } catch (NullPointerException e2) {
            return -2;
        }
    }

    public int getIccAppFamily(int slotId) {
        try {
            IOplusTelephonyInternalExt telephony = getIOplusTelephonyInternalEx();
            if (telephony == null) {
                return 0;
            }
            return telephony.getIccAppFamily(slotId);
        } catch (RemoteException e) {
            return 0;
        } catch (NullPointerException e2) {
            return 0;
        }
    }

    public void registerAtUrcInd(int slotId, Executor executor, OemHookCallback cb) {
        try {
            IOplusTelephonyInternalExt telephony = getIOplusTelephonyInternalEx();
            if (telephony == null) {
                Rlog.e(TAG, "registerAtUrcInd service is null");
            } else {
                telephony.registerAtUrcInd(slotId, new AnonymousClass5(executor, cb));
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "exception " + ex);
        }
    }

    /* renamed from: android.telephony.OplusTelephonyManager$5, reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass5 extends IOemHookCallback.Stub {
        final /* synthetic */ OemHookCallback val$cb;
        final /* synthetic */ Executor val$executor;

        AnonymousClass5(Executor executor, OemHookCallback oemHookCallback) {
            this.val$executor = executor;
            this.val$cb = oemHookCallback;
        }

        @Override // com.oplus.telephony.IOemHookCallback
        public void onAtCmdResp(int slotId, long token, String atCmd) {
        }

        @Override // com.oplus.telephony.IOemHookCallback
        public void onAtUrcInd(final int slotId, final String atCmd) {
            Executor executor = this.val$executor;
            final OemHookCallback oemHookCallback = this.val$cb;
            executor.execute(new Runnable() { // from class: android.telephony.OplusTelephonyManager$5$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    OplusTelephonyManager.OemHookCallback.this.onAtUrcInd(slotId, atCmd);
                }
            });
        }

        @Override // com.oplus.telephony.IOemHookCallback
        public void onError(final String e) {
            Executor executor = this.val$executor;
            final OemHookCallback oemHookCallback = this.val$cb;
            executor.execute(new Runnable() { // from class: android.telephony.OplusTelephonyManager$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OplusTelephonyManager.OemHookCallback.this.onError(e);
                }
            });
        }
    }

    public void registerAtUrcInd(final int slotId, final Handler h, final int eventId) {
        try {
            IOplusTelephonyInternalExt telephony = getIOplusTelephonyInternalEx();
            if (telephony == null) {
                Rlog.e(TAG, "registerAtUrcInd service is null");
            } else {
                telephony.registerAtUrcInd(slotId, new IOemHookCallback.Stub() { // from class: android.telephony.OplusTelephonyManager.6
                    @Override // com.oplus.telephony.IOemHookCallback
                    public void onAtCmdResp(int slotId2, long token, String atCmd) {
                    }

                    @Override // com.oplus.telephony.IOemHookCallback
                    public void onAtUrcInd(int slotId2, String atCmd) {
                        Handler handler = h;
                        if (handler != null) {
                            Message m = handler.obtainMessage(eventId, slotId2, 0, atCmd);
                            h.sendMessage(m);
                        } else {
                            Rlog.e(OplusTelephonyManager.TAG, "registerAtUrcInd onAtUrcInd h == null");
                        }
                    }

                    @Override // com.oplus.telephony.IOemHookCallback
                    public void onError(String e) {
                        if (h != null) {
                            Exception except = new Exception(e);
                            Message m = h.obtainMessage(eventId, slotId, 0, except);
                            h.sendMessage(m);
                            return;
                        }
                        Rlog.e(OplusTelephonyManager.TAG, "registerAtUrcInd onError h == null");
                    }
                });
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "exception " + ex);
        }
    }

    public void sendAtCmd(int slotId, long token, String atCmd, Executor executor, OemHookCallback cb) {
        try {
            IOplusTelephonyInternalExt telephony = getIOplusTelephonyInternalEx();
            if (telephony == null) {
                Rlog.e(TAG, "sendAtCmd service is null");
            } else {
                telephony.sendAtCmd(slotId, token, atCmd, new AnonymousClass7(executor, cb));
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "exception " + ex);
        }
    }

    /* renamed from: android.telephony.OplusTelephonyManager$7, reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass7 extends IOemHookCallback.Stub {
        final /* synthetic */ OemHookCallback val$cb;
        final /* synthetic */ Executor val$executor;

        AnonymousClass7(Executor executor, OemHookCallback oemHookCallback) {
            this.val$executor = executor;
            this.val$cb = oemHookCallback;
        }

        @Override // com.oplus.telephony.IOemHookCallback
        public void onAtCmdResp(final int slotId, final long token, final String atCmd) {
            Executor executor = this.val$executor;
            final OemHookCallback oemHookCallback = this.val$cb;
            executor.execute(new Runnable() { // from class: android.telephony.OplusTelephonyManager$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OplusTelephonyManager.OemHookCallback.this.onAtCmdResp(slotId, token, atCmd);
                }
            });
        }

        @Override // com.oplus.telephony.IOemHookCallback
        public void onAtUrcInd(int slotId, String atCmd) {
        }

        @Override // com.oplus.telephony.IOemHookCallback
        public void onError(final String e) {
            Executor executor = this.val$executor;
            final OemHookCallback oemHookCallback = this.val$cb;
            executor.execute(new Runnable() { // from class: android.telephony.OplusTelephonyManager$7$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    OplusTelephonyManager.OemHookCallback.this.onError(e);
                }
            });
        }
    }

    public void sendAtCmd(final int slotId, final long token, String atCmd, final Handler h, final int eventId) {
        try {
            IOplusTelephonyInternalExt telephony = getIOplusTelephonyInternalEx();
            if (telephony == null) {
                Rlog.e(TAG, "sendAtCmd service is null");
            } else {
                telephony.sendAtCmd(slotId, token, atCmd, new IOemHookCallback.Stub() { // from class: android.telephony.OplusTelephonyManager.8
                    @Override // com.oplus.telephony.IOemHookCallback
                    public void onAtCmdResp(int slotId2, long token2, String atCmd2) {
                        Rlog.d(OplusTelephonyManager.TAG, "sendAtCmd onAtCmdResp token=" + token2);
                        if (h != null) {
                            ArrayList<Object> ret = new ArrayList<>();
                            ret.add(Integer.valueOf(slotId2));
                            ret.add(Long.valueOf(token2));
                            ret.add(atCmd2);
                            Message m = h.obtainMessage(eventId, ret);
                            h.sendMessage(m);
                            return;
                        }
                        Rlog.e(OplusTelephonyManager.TAG, "sendAtCmd onAtCmdResp h == null");
                    }

                    @Override // com.oplus.telephony.IOemHookCallback
                    public void onAtUrcInd(int slotId2, String atCmd2) {
                    }

                    @Override // com.oplus.telephony.IOemHookCallback
                    public void onError(String e) {
                        Rlog.d(OplusTelephonyManager.TAG, "sendAtCmd onError " + e);
                        if (h != null) {
                            Object except = new Exception(e);
                            ArrayList<Object> ret = new ArrayList<>();
                            ret.add(Integer.valueOf(slotId));
                            ret.add(Long.valueOf(token));
                            ret.add(except);
                            Message m = h.obtainMessage(eventId, ret);
                            h.sendMessage(m);
                            return;
                        }
                        Rlog.e(OplusTelephonyManager.TAG, "sendAtCmd onError h == null");
                    }
                });
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "exception " + ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int getPropertyValueInt(String str, int i) {
        String str2 = TAG;
        int i2 = -1;
        try {
            IOplusTelephonyInternalExt iOplusTelephonyInternalEx = getIOplusTelephonyInternalEx();
            if (iOplusTelephonyInternalEx == null) {
                Rlog.e(TAG, "telephonyExt service is null");
                str2 = str2;
            } else {
                int propertyValueInt = iOplusTelephonyInternalEx.getPropertyValueInt(str, i);
                i2 = propertyValueInt;
                str2 = propertyValueInt;
            }
        } catch (RemoteException e) {
            Rlog.e(str2, "exception " + e);
        }
        return i2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean getPropertyValueBool(String str, boolean z) {
        String str2 = TAG;
        boolean z2 = z;
        try {
            IOplusTelephonyInternalExt iOplusTelephonyInternalEx = getIOplusTelephonyInternalEx();
            if (iOplusTelephonyInternalEx == null) {
                Rlog.e(TAG, "telephonyExt service is null");
                str2 = str2;
            } else {
                boolean propertyValueBool = iOplusTelephonyInternalEx.getPropertyValueBool(str, z);
                z2 = propertyValueBool;
                str2 = propertyValueBool;
            }
        } catch (RemoteException e) {
            Rlog.e(str2, "exception " + e);
        }
        return z2;
    }

    public String getPropertyValueString(String property, String def) {
        String str = TAG;
        String ret = def;
        try {
            IOplusTelephonyInternalExt telephonyExt = getIOplusTelephonyInternalEx();
            if (telephonyExt == null) {
                Rlog.e(TAG, "telephonyExt service is null");
            } else {
                str = telephonyExt.getPropertyValueString(property, def);
                ret = str;
            }
        } catch (RemoteException ex) {
            Rlog.e(str, "exception " + ex);
        }
        return ret;
    }

    private IOplusTelephonyInternalExt getIOplusTelephonyInternalEx() {
        return IOplusTelephonyInternalExt.Stub.asInterface(ServiceManager.getService("oplus_telephony_internal_ext"));
    }

    public void addGwsdListener(GwsdListener listener) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        addGwsdListener(defDataPhoneId, listener);
    }

    public void addGwsdListener(int phoneId, GwsdListener listener) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.addListener(phoneId, listener.callback);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void removeGwsdListener() {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        removeGwsdListener(defDataPhoneId);
    }

    public void removeGwsdListener(int phoneId) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.removeListener(phoneId);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setGwsdEnabled(boolean action) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        setGwsdEnabled(defDataPhoneId, action);
    }

    public void setGwsdEnabled(int phoneId, boolean action) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setUserModeEnabled(phoneId, action);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setGwsdAutoRejectEnabled(boolean action) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        setGwsdAutoRejectEnabled(defDataPhoneId, action);
    }

    public void setGwsdAutoRejectEnabled(int phoneId, boolean action) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setAutoRejectModeEnabled(phoneId, action);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void syncGwsdInfo(boolean userEnable, boolean autoReject) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        syncGwsdInfo(defDataPhoneId, userEnable, autoReject);
    }

    public void syncGwsdInfo(int phoneId, boolean userEnable, boolean autoReject) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.syncGwsdInfo(phoneId, userEnable, autoReject);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setCallValidTimer(int timer) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        setCallValidTimer(defDataPhoneId, timer);
    }

    public void setCallValidTimer(int phoneId, int timer) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setCallValidTimer(phoneId, timer);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setIgnoreSameNumberInterval(int internal) {
        int defDataPhoneId = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
        setIgnoreSameNumberInterval(defDataPhoneId, internal);
    }

    public void setIgnoreSameNumberInterval(int phoneId, int internal) {
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setIgnoreSameNumberInterval(phoneId, internal);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void setGwsdDualSimEnabled(boolean action) {
        Rlog.d(TAG, "setGwsdDualSimEnabled: " + action);
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                iGwsdService.setGwsdDualSimEnabled(action);
            }
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public boolean isDataAvailableForGwsdDualSim(boolean gwsdDualSimStatus) {
        Rlog.d(TAG, "isDataAvailableForGwsdDualSim");
        try {
            IGwsdService iGwsdService = IGwsdService.Stub.asInterface(ServiceManager.getService("gwsd"));
            if (iGwsdService != null) {
                return iGwsdService.isDataAvailableForGwsdDualSim(gwsdDualSimStatus);
            }
            return false;
        } catch (Exception e) {
            Rlog.e(TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    public boolean setSaSilenceMode(int soltId, boolean enable, int module, int priority, String event) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.setSaSilenceMode(soltId, enable, module, priority, event);
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return 1;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return 2;
            case 13:
            case 18:
            case 19:
                return 3;
            case 20:
                return 4;
            default:
                return 0;
        }
    }

    public int getDualPsEnableState() {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return 0;
            }
            Rlog.d(TAG, getOpPackageName() + " getDualPsEnableState");
            int dualPsState = telephonyExt.getDualPsEnableState();
            return dualPsState;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getDualPsEnableState fail: " + e.getMessage());
            return 0;
        }
    }

    public void requestDualPsNetwork(String pkgName) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                String callingPkn = getOpPackageName();
                Rlog.d(TAG, callingPkn + " requestDualPsNetwork");
                telephonyExt.requestDualPsNetwork(callingPkn);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "requestDualPsNetwork fail: " + e.getMessage());
        }
    }

    public void releaseDualPsNetwork(String pkgName) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                String callingPkn = getOpPackageName();
                Rlog.d(TAG, callingPkn + " releaseDualPsNetwork ");
                telephonyExt.releaseDualPsNetwork(callingPkn);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "releaseDualPsNetwork fail: " + e.getMessage());
        }
    }

    public int isPrimaryCellularNetwork(Network network) {
        if (network == null) {
            Rlog.d(TAG, "isPrimaryCellular network == null");
            return -1;
        }
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt == null) {
                return -1;
            }
            int isPrimary = telephonyExt.isPrimaryCellularNetwork(network);
            return isPrimary;
        } catch (RemoteException e) {
            Rlog.e(TAG, "isPrimaryCellularNetwork fail: " + e.getMessage());
            return -1;
        }
    }

    public void setDualDataUserPreference(boolean enable) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setDualDataUserPreference(enable);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setDualDataUserPreference fail: " + e.getMessage());
        }
    }

    public boolean getSaPrioState(int slotId) {
        boolean nrpriostate = true;
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                nrpriostate = telephonyExt.getSaPrioState(slotId);
                Rlog.d(TAG, "getSaPrioState:" + nrpriostate);
                return nrpriostate;
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "getSaPrioState fail: " + e.getMessage());
        }
        Rlog.d(TAG, "getSaPrioState:" + nrpriostate);
        return nrpriostate;
    }

    public void setSimlockOfflineLock(int slotId, boolean enabled, int time, int[] remind, long serverTimestamp) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                telephonyExt.setSimlockOfflineLock(slotId, enabled, time, remind, serverTimestamp);
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setSimlockOfflineLock fail: " + e.getMessage());
        }
    }

    public int[] getSimlockOfflineLock(int slotId) {
        try {
            IOplusTelephonyExt telephonyExt = getIOplusTelephonyExt();
            if (telephonyExt != null) {
                return telephonyExt.getSimlockOfflineLock(slotId);
            }
            return null;
        } catch (RemoteException e) {
            Rlog.e(TAG, "getSimlockOfflineLock fail: " + e.getMessage());
            return null;
        }
    }
}
