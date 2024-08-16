package com.oplus.nec;

import android.content.Context;
import android.content.Intent;
import android.net.OplusNetworkingControlManager;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.OplusTelephonyIntents;
import com.oplus.nec.IOplusNecService;
import com.oplus.network.OplusNetworkStackManager;
import com.oplus.oms.split.splitrequest.SplitPathManager;

/* loaded from: classes.dex */
public final class OplusNecManager implements IOplusNecManager {
    public static final int CALL_EVENT_CALL_DROP = 9005;
    public static final int CALL_EVENT_CALL_FAIL = 9004;
    public static final int CALL_EVENT_NO_ERR = 9001;
    public static final int CALL_EVENT_TELEPHONY_LOGIC_ERR = 9003;
    public static final int CALL_EVENT_UNKNOWN_ERR = 9002;
    public static final boolean DBG = true;
    public static final String EVENT_ID = "evt_id";
    public static final String LOG_TAG = "OplusNecManager";
    public static final String NEC_ACTION = "com.oplus.telephony.action.ACTION_REPORT_NEC";
    public static final String NEC_BROADCAST_PERMISSION = "oplus.permission.OPLUS_COMPONENT_SAFE";
    public static final String NEC_DATA = "nec_data";
    public static final int NEC_EVENT_ANR_STAT = 8030;
    public static final int NEC_EVENT_CALL_ERROR = 8107;
    public static final int NEC_EVENT_CALL_INSTANCE = 8117;
    public static final int NEC_EVENT_CDMA_RESET_ACTIVE_TIME = 8102;
    public static final int NEC_EVENT_DATA_CALL_IP_TYPE = 8007;
    public static final int NEC_EVENT_DATA_CONNECT = 8002;
    public static final int NEC_EVENT_DATA_CONNECT_RET = 8003;
    public static final int NEC_EVENT_DATA_DISCONNECT = 8004;
    public static final int NEC_EVENT_DATA_DISCONNECT_RET = 8005;
    public static final int NEC_EVENT_DATA_ENABLE_CHANGE = 8021;
    public static final int NEC_EVENT_DATA_LIMIT_STATE = 8022;
    public static final int NEC_EVENT_DATA_LOST_REASON = 8023;
    public static final int NEC_EVENT_DATA_ONLINE_MEETING = 8024;
    public static final int NEC_EVENT_DISPATCHED_SMS_ID = 8088;
    public static final int NEC_EVENT_DISPATCHED_SMS_MO_ID = 8089;
    public static final int NEC_EVENT_FASTRECOVERY = 8050;
    public static final int NEC_EVENT_GAME_DELAY_ERROR = 8015;
    public static final int NEC_EVENT_HANG_UP_DELAY_TIME = 8101;
    public static final int NEC_EVENT_IMS_CALL_STATE_CHANGED = 8106;
    public static final int NEC_EVENT_IMS_KEYLOG = 8109;
    public static final int NEC_EVENT_IMS_REG_FAIL_KEYLOG = 8116;
    public static final int NEC_EVENT_IMS_STATE_CHANGED = 8105;
    public static final int NEC_EVENT_IMS_VOLTE_VOPS_QUEREY = 8108;
    private static final int NEC_EVENT_NETWORK_DIAGNOSIS = 8122;
    public static final int NEC_EVENT_NO_DATA_FLOW_ERROR = 8011;
    public static final int NEC_EVENT_NO_DATA_FLOW_RECOVERY_ERROR = 8012;
    public static final int NEC_EVENT_NO_DATA_ICON_ERROR = 8008;
    public static final int NEC_EVENT_NWDIAG_SERVICE_INITED = 8006;
    public static final int NEC_EVENT_OLK_KEYLOG = 8119;
    public static final int NEC_EVENT_OOS_CHANGED = 8001;
    public static final int NEC_EVENT_PRECISE_CALL_STATE_CHANGED = 8103;
    public static final int NEC_EVENT_PREF_NETWORK_MODE_CHANGED = 8018;
    public static final int NEC_EVENT_REG_INFO_CHANGED = 8017;
    public static final int NEC_EVENT_SCAN_QR_CODE_STATS = 8031;
    public static final int NEC_EVENT_SIGNALSTRENGTH_CHANGED = 8000;
    public static final int NEC_EVENT_SLOW_DATA_FLOW_ERROR = 8013;
    public static final int NEC_EVENT_SLOW_DATA_FLOW_RECOVERY = 8014;
    public static final int NEC_EVENT_SRVCC_STATE_CHANGED = 8104;
    private static final long NO_DATA_ICON_SAME_CAUSE_DURATION = 600000;
    public static final int SEND_VIDEO_STUTTER_TO_MOBILE = 983223;
    private static int SIM_COUNT = 0;
    public static final String SLOT_ID = "slot_id";
    public static final String SRV_NAME = "oplus_nec";
    public static final int VIDEO_EVENT_LAG_END = 101;
    public static final int VIDEO_EVENT_LAG_START = 100;
    public static final int VIDEO_EVENT_OPT_NOTIFY = 103;
    public static final int VIDEO_EVENT_VIDEO_CALL_START = 104;

    @Deprecated
    public static final int VIDEO_EVENT_VIDEO_START = 100;
    public static final int VIDEO_EVENT_VIDEO_STOP = 102;
    private static OplusNecManager sInstance;
    public Context mContext;
    private int[] mDataNetworkType;
    private int[] mDataRegState;
    private String mLastDataFlowReasons = null;
    private long mLastDataFlowReasonsTime = 0;
    private IOplusNecService mNecService;
    private int[] mVoiceNetworkType;
    private int[] mVoiceRegState;
    private static final int SIM_NUM = TelephonyManager.getDefault().getPhoneCount();
    private static long mNoDataIconSameCauseDuration = 600000;

    public static OplusNecManager getInstance(Context c) {
        OplusNecManager oplusNecManager;
        synchronized (OplusNecManager.class) {
            if (sInstance == null) {
                sInstance = new OplusNecManager(c);
            }
            oplusNecManager = sInstance;
        }
        return oplusNecManager;
    }

    protected OplusNecManager(Context context) {
        int i = SIM_NUM;
        this.mVoiceRegState = new int[i];
        this.mVoiceNetworkType = new int[i];
        this.mDataRegState = new int[i];
        this.mDataNetworkType = new int[i];
        this.mContext = context;
        SIM_COUNT = ((TelephonyManager) context.getSystemService("phone")).getPhoneCount();
        this.mNecService = IOplusNecService.Stub.asInterface(ServiceManager.getService(SRV_NAME));
    }

    public void notifyNwDiagnoseInitComplete() {
        necLog("notifyNwDiagnoseInitComplete...");
        Intent intent = new Intent(NEC_ACTION);
        intent.putExtra(EVENT_ID, NEC_EVENT_NWDIAG_SERVICE_INITED);
        Context context = this.mContext;
        if (context != null) {
            context.sendBroadcast(intent, "oplus.permission.OPLUS_COMPONENT_SAFE");
        }
    }

    public void broadcastNecEvent(int slotId, int eventId, Bundle data) {
        if (this.mContext == null || !isValidSlotId(slotId)) {
            return;
        }
        Intent intent = new Intent(NEC_ACTION);
        intent.putExtra(SLOT_ID, slotId);
        intent.putExtra(EVENT_ID, eventId);
        intent.putExtra(NEC_DATA, data);
        Context context = this.mContext;
        if (context != null) {
            context.sendBroadcast(intent, "oplus.permission.OPLUS_COMPONENT_SAFE");
        }
    }

    public void broadcastServiceStateChanged(boolean oos, int slotId) {
        necLog("broadcastServiceStateChanged slotId:" + slotId + "oos:" + oos);
    }

    public boolean isApnSupported(String type) {
        if (SplitPathManager.DEFAULT.equals(type) || "mms".equals(type) || "ims".equals(type)) {
            return true;
        }
        return false;
    }

    public void broadcastDataConnect(int slotId, String type) {
        if (type == null || !isApnSupported(type)) {
            Log.e(LOG_TAG, "broadcastDataConnect paras is null ");
            return;
        }
        if (slotId < 0 || slotId >= SIM_COUNT) {
            Log.e(LOG_TAG, "invalid subId: " + slotId);
            return;
        }
        Bundle data = new Bundle();
        data.putString("type", type);
        broadcastNecEvent(slotId, NEC_EVENT_DATA_CONNECT, data);
    }

    public void broadcastOlkKeylog(int slotId, Bundle data) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastOlkKeylog invalid slotId ");
        } else {
            broadcastNecEvent(slotId, NEC_EVENT_OLK_KEYLOG, data);
        }
    }

    public void broadcastDataConnectResult(int slotId, String type, boolean success) {
        if (type == null || !isApnSupported(type)) {
            Log.e(LOG_TAG, "broadcastDataConnected paras is null ");
            return;
        }
        if (slotId < 0 || slotId >= SIM_COUNT) {
            Log.e(LOG_TAG, "invalid subId: " + slotId);
            return;
        }
        Bundle data = new Bundle();
        data.putString("type", type);
        data.putBoolean("success", success);
        broadcastNecEvent(slotId, NEC_EVENT_DATA_CONNECT_RET, data);
    }

    public void broadcastDataDisconnect(int slotId, String type) {
        if (type == null || !isApnSupported(type)) {
            Log.e(LOG_TAG, "broadcastDataDisconnect paras is null ");
            return;
        }
        if (slotId < 0 || slotId >= SIM_COUNT) {
            Log.e(LOG_TAG, "invalid subId: " + slotId);
            return;
        }
        Bundle data = new Bundle();
        data.putString("type", type);
        broadcastNecEvent(slotId, NEC_EVENT_DATA_DISCONNECT, data);
    }

    public void broadcastDataDisconnectComplete(int slotId, String type) {
        if (type == null || !isApnSupported(type)) {
            Log.e(LOG_TAG, "broadcastDataDisconnectComplete paras is null ");
            return;
        }
        if (slotId < 0 || slotId >= SIM_COUNT) {
            Log.e(LOG_TAG, "invalid subId: " + slotId);
            return;
        }
        Bundle data = new Bundle();
        data.putString("type", type);
        broadcastNecEvent(slotId, NEC_EVENT_DATA_DISCONNECT_RET, data);
    }

    public void broadcastDataCallInternetProtocolType(int slotId, int protocol) {
        if (slotId < 0 || slotId >= SIM_COUNT) {
            Log.e(LOG_TAG, "invalid subId: " + slotId);
            return;
        }
        Bundle data = new Bundle();
        data.putInt("protocol", protocol);
        broadcastNecEvent(slotId, NEC_EVENT_DATA_CALL_IP_TYPE, data);
    }

    public void broadcastNoDataIconError(int slotId, int errorcode, int protocol, String cause) {
        StringBuilder stringBuilder = new StringBuilder();
        if (slotId < 0 || slotId >= SIM_NUM) {
            stringBuilder.append("invalid subId: ");
            stringBuilder.append(slotId);
            Log.e(LOG_TAG, stringBuilder.toString());
        } else {
            Bundle data = new Bundle();
            data.putInt("errorcode", errorcode);
            data.putInt("protocol", protocol);
            data.putString("cause", cause);
            broadcastNecEvent(slotId, NEC_EVENT_NO_DATA_ICON_ERROR, data);
        }
    }

    public void broadcastNoDataFlowError(int slotId, int errorcode, String mNoDataFlowReason) {
        StringBuilder stringBuilder = new StringBuilder();
        if (slotId < 0 || slotId >= SIM_NUM) {
            stringBuilder.append("invalid subId: ");
            stringBuilder.append(slotId);
            Log.e(LOG_TAG, stringBuilder.toString());
            return;
        }
        if (this.mLastDataFlowReasons != null && mNoDataFlowReason != null) {
            long dataDuration = SystemClock.elapsedRealtime() - this.mLastDataFlowReasonsTime;
            if (this.mLastDataFlowReasons.equals(mNoDataFlowReason) && dataDuration > 0 && dataDuration < mNoDataIconSameCauseDuration) {
                Log.e(LOG_TAG, "same mNoDataFlowReason return");
                return;
            }
        }
        this.mLastDataFlowReasonsTime = SystemClock.elapsedRealtime();
        this.mLastDataFlowReasons = mNoDataFlowReason;
        Bundle data = new Bundle();
        data.putInt("errorcode", errorcode);
        data.putString("mNoDataFlowReason", mNoDataFlowReason);
        broadcastNecEvent(slotId, NEC_EVENT_NO_DATA_FLOW_ERROR, data);
    }

    public void broadcastNoDataFlowRecoveryError(int slotId, int errorcode, String recovery) {
        StringBuilder stringBuilder = new StringBuilder();
        if (slotId < 0 || slotId >= SIM_NUM) {
            stringBuilder.append("invalid subId: ");
            stringBuilder.append(slotId);
            Log.e(LOG_TAG, stringBuilder.toString());
        } else {
            Bundle data = new Bundle();
            data.putInt("errorcode", errorcode);
            data.putString("recovery", recovery);
            broadcastNecEvent(slotId, NEC_EVENT_NO_DATA_FLOW_RECOVERY_ERROR, data);
        }
    }

    public void broadcastFastRecoveryEvent(int slotId, int errorcode, String event) {
        Log.d(LOG_TAG, "broadcastFastRecoveryEvent subId: " + slotId);
        Bundle data = new Bundle();
        data.putInt("errorcode", errorcode);
        data.putString("event", event);
        broadcastNecEvent(slotId, NEC_EVENT_FASTRECOVERY, data);
    }

    public void broadcastSlowDataFlowError(int slotId, int errorcode, String score) {
        if (errorcode == 983223) {
            Log.d(LOG_TAG, "broadcastSlowDataFlowError " + slotId + "," + errorcode + "," + score);
            try {
                String[] split = score.split("#");
                if (split.length != 4) {
                    Log.e(LOG_TAG, "parse failed!" + score);
                    return;
                }
                int code = Integer.parseInt(split[1]);
                OplusNetworkStackManager stackManager = OplusNetworkStackManager.getInstance(this.mContext);
                switch (code) {
                    case 100:
                        stackManager.videoFrameLag(true);
                        break;
                    case 101:
                        stackManager.videoFrameLag(false);
                        break;
                    case 102:
                        stackManager.videoStop();
                        break;
                    case 103:
                        Log.d(LOG_TAG, "opt result:" + score);
                        break;
                    case 104:
                        stackManager.videoStart();
                        break;
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "parse failed!" + e.getMessage(), e);
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (slotId < 0 || slotId >= SIM_NUM) {
            stringBuilder.append("invalid subId: ");
            stringBuilder.append(slotId);
            Log.e(LOG_TAG, stringBuilder.toString());
        } else {
            Bundle data = new Bundle();
            data.putInt("errorcode", errorcode);
            data.putString("score", score);
            broadcastNecEvent(slotId, NEC_EVENT_SLOW_DATA_FLOW_ERROR, data);
        }
    }

    public void broadcastSlowDataFlowRecovery(int slotId, int errorcode, String score) {
        StringBuilder stringBuilder = new StringBuilder();
        if (slotId < 0 || slotId >= SIM_NUM) {
            stringBuilder.append("invalid subId: ");
            stringBuilder.append(slotId);
            Log.e(LOG_TAG, stringBuilder.toString());
        } else {
            Bundle data = new Bundle();
            data.putInt("errorcode", errorcode);
            data.putString("score", score);
            broadcastNecEvent(slotId, NEC_EVENT_SLOW_DATA_FLOW_RECOVERY, data);
        }
    }

    public void broadcastGameLargeDelayError(int slotId, int errorcode, String gameError) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastGameLargeDelayError invalid slotId ");
            return;
        }
        Bundle data = new Bundle();
        data.putInt("errorcode", errorcode);
        data.putString("gameError", gameError);
        broadcastNecEvent(slotId, NEC_EVENT_GAME_DELAY_ERROR, data);
    }

    public void broadcastLimitState(int slotId, boolean limitState) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastLimitState invalid slotId ");
            return;
        }
        Bundle data = new Bundle();
        data.putBoolean("limitState", limitState);
        broadcastNecEvent(slotId, NEC_EVENT_DATA_LIMIT_STATE, data);
    }

    public void broadcastScanQrCodeStats(int slotId, int errorcode, String QrSuccess) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastScanQrCodeStats invalid slotId ");
            return;
        }
        Bundle data = new Bundle();
        data.putInt("errorcode", errorcode);
        data.putString("QrResult", QrSuccess);
        Log.d(LOG_TAG, "QrResult " + QrSuccess);
        broadcastNecEvent(slotId, NEC_EVENT_SCAN_QR_CODE_STATS, data);
    }

    public void broadcastPreferredNetworkMode(int slotId, int preferredMode) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastPreferredNetworkMode invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastPreferredNetworkMode ok ,slotId: " + slotId + "preferredMode: " + preferredMode);
        Bundle data = new Bundle();
        data.putInt("preferredMode", preferredMode);
        broadcastNecEvent(slotId, NEC_EVENT_PREF_NETWORK_MODE_CHANGED, data);
    }

    public void broadcastDataEnabledChanged(int slotId, boolean enabled) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastDataEnabledChanged invalid slotId ");
            return;
        }
        Bundle data = new Bundle();
        data.putBoolean("enabled", enabled);
        broadcastNecEvent(slotId, NEC_EVENT_DATA_ENABLE_CHANGE, data);
    }

    public void broadcastLostConnectionReason(int slotId, int errorcode, int lostReason) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastLostConnectionReason invalid slotId ");
            return;
        }
        Bundle data = new Bundle();
        data.putInt("errorcode", errorcode);
        data.putInt("lostReason", lostReason);
        broadcastNecEvent(slotId, NEC_EVENT_DATA_LOST_REASON, data);
    }

    public void broadcastAnrEventStat(int slotId, int errorcode, String anrEventStat) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastLostConnectionReason invalid slotId ");
            return;
        }
        Bundle data = new Bundle();
        data.putInt("errorcode", errorcode);
        data.putString("anrEventStat", anrEventStat);
        broadcastNecEvent(slotId, NEC_EVENT_ANR_STAT, data);
    }

    public void broadcastHangUpDelayTimer(int slotId, long millis, int csOrIms) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastHangUpDelayTimer invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastHangUpDelayTimer ok " + slotId + " " + millis + " " + csOrIms);
        Bundle data = new Bundle();
        data.putLong("millis", millis);
        data.putInt("csOrIms", csOrIms);
        broadcastNecEvent(slotId, NEC_EVENT_HANG_UP_DELAY_TIME, data);
    }

    public void broadcastCdmaResetActiveTimer(int slotId, int networkType) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastCdmaResetActiveTimer invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastCdmaResetActiveTimer ok " + slotId + "networkType:" + networkType);
        Bundle data = new Bundle();
        data.putInt(OplusNetworkingControlManager.EXTRA_NETWORK_TYPE, networkType);
        broadcastNecEvent(slotId, NEC_EVENT_CDMA_RESET_ACTIVE_TIME, data);
    }

    public void broadcastPreciseCallStateChanged(int slotId, int ring, int foreground, int background, int cause, int preciseCause, int disconnectState) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastPreciseCallStateChanged invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastPreciseCallStateChanged ok " + slotId + " " + ring + " " + foreground + " " + background + " " + cause + " " + preciseCause);
        Bundle data = new Bundle();
        data.putInt("ring", ring);
        data.putInt("foreground", foreground);
        data.putInt("background", background);
        data.putInt("cause", cause);
        data.putInt("preciseCause", preciseCause);
        data.putInt("disconnectState", disconnectState);
        broadcastNecEvent(slotId, NEC_EVENT_PRECISE_CALL_STATE_CHANGED, data);
    }

    public void broadcastSrvccStateChanged(int slotId, int srvccState) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastSrvccStateChanged invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastSrvccStateChanged ok " + slotId + " " + srvccState);
        Bundle data = new Bundle();
        data.putInt("srvccState", srvccState);
        broadcastNecEvent(slotId, NEC_EVENT_SRVCC_STATE_CHANGED, data);
    }

    public void broadcastCallError(int slotId, int event, int cause, int preciseCause, String desc, boolean isImsCall, boolean isIncoming) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastCallError invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastCallError  slotId:" + slotId + " ,event:" + event + ",desc: " + desc + ",cause: " + cause + ",preciseCause:" + preciseCause);
        Bundle data = new Bundle();
        data.putInt(OplusTelephonyIntents.EXTRA_SLOT_ID, slotId);
        data.putInt("cause", cause);
        data.putInt("preciseCause", preciseCause);
        data.putInt("event", event);
        data.putString("desc", desc);
        data.putBoolean("isImsCall", isImsCall);
        data.putBoolean("isIncoming", isIncoming);
        broadcastNecEvent(slotId, NEC_EVENT_CALL_ERROR, data);
    }

    public void broadcastVolteCallKeylog(int slotId, int event, String desc) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastVolteCallKeylog invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastVolteCallKeylog  slotId:" + slotId + " ,event:" + event + ", desc:" + desc);
        Bundle data = new Bundle();
        data.putInt(OplusTelephonyIntents.EXTRA_SLOT_ID, slotId);
        data.putInt("event", event);
        data.putString("desc", desc);
        broadcastNecEvent(slotId, NEC_EVENT_IMS_KEYLOG, data);
    }

    public void broadcastImsRegisterState(int slotId, boolean imsRegisterState) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastImsRegisterState invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastImsRegisterState ok " + slotId + " " + imsRegisterState);
        Bundle data = new Bundle();
        data.putBoolean("imsRegisterState", imsRegisterState);
        broadcastNecEvent(slotId, NEC_EVENT_IMS_STATE_CHANGED, data);
    }

    public void broadcastVolteVopsOrSettingChanged(int slotId, int event, boolean isVolteEnabled) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastVolteVopsOrSettingChanged invalid slotId ");
            return;
        }
        Log.d(LOG_TAG, "broadcastVolteVopsOrSettingChanged  slotId:" + slotId + " ,event:" + event + ", isVolteEnabled:" + isVolteEnabled);
        Bundle data = new Bundle();
        data.putInt(OplusTelephonyIntents.EXTRA_SLOT_ID, slotId);
        data.putInt("event", event);
        data.putBoolean("volteEnabled", isVolteEnabled);
        broadcastNecEvent(slotId, NEC_EVENT_IMS_VOLTE_VOPS_QUEREY, data);
    }

    public void broadcastRegInfoChanged(int voiceRegState, int voiceNetworkType, int dataRegState, int dataNetworkType, int slotId) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastRegInfoChanged invalid slotId ");
            return;
        }
        int[] iArr = this.mVoiceRegState;
        if (voiceRegState == iArr[slotId] && voiceNetworkType == this.mVoiceNetworkType[slotId] && dataRegState == this.mDataRegState[slotId] && dataNetworkType == this.mDataNetworkType[slotId]) {
            return;
        }
        iArr[slotId] = voiceRegState;
        this.mVoiceNetworkType[slotId] = voiceNetworkType;
        this.mDataRegState[slotId] = dataRegState;
        this.mDataNetworkType[slotId] = dataNetworkType;
        Bundle data = new Bundle();
        data.putInt("voiceRegState", voiceRegState);
        data.putInt("voiceNetworkType", voiceNetworkType);
        data.putInt("dataRegState", dataRegState);
        data.putInt("dataNetworkType", dataNetworkType);
        broadcastNecEvent(slotId, NEC_EVENT_REG_INFO_CHANGED, data);
    }

    public void broadcastImsNetworkStateChanged(int phoneId, int errorCode, String errorMessage, int regState, int imsRat) {
        if (!isValidSlotId(phoneId)) {
            Log.e(LOG_TAG, "broadcastRegInfoChanged invalid mPhoneId ");
            return;
        }
        Bundle data = new Bundle();
        data.putInt(OplusTelephonyIntents.EXTRA_SLOT_ID, phoneId);
        data.putInt("errorCode", errorCode);
        data.putString("errorMessage", errorMessage);
        data.putInt("regState", regState);
        data.putInt("imsRat", imsRat);
        broadcastNecEvent(phoneId, NEC_EVENT_IMS_REG_FAIL_KEYLOG, data);
    }

    public void broadcastScreenShare(int phoneId) {
        if (!isValidSlotId(phoneId)) {
            Log.e(LOG_TAG, "broadcastScreenShare invalid mPhoneId ");
            return;
        }
        Log.d(LOG_TAG, "broadcast screenShare");
        Bundle data = new Bundle();
        data.putString("instantType", "screenShare");
        broadcastNecEvent(phoneId, NEC_EVENT_CALL_INSTANCE, data);
    }

    public void broadcastScreenTouch(int phoneId, int screenTouchCount, int screenTouchSuccCount) {
        if (!isValidSlotId(phoneId)) {
            Log.e(LOG_TAG, "broadcastScreenTouch invalid mPhoneId ");
            return;
        }
        Log.d(LOG_TAG, "broadcast screenTouch ,screenTouchCount = " + screenTouchCount + ",screenTouchSuccCount = " + screenTouchSuccCount);
        Bundle data = new Bundle();
        data.putString("instantType", "screenTouch");
        data.putInt("screenTouchCount", screenTouchCount);
        data.putInt("screenTouchSuccCount", screenTouchSuccCount);
        broadcastNecEvent(phoneId, NEC_EVENT_CALL_INSTANCE, data);
    }

    public void broadcastOnlineMeeting(int slotId, int errorcode, String record) {
        StringBuilder stringBuilder = new StringBuilder();
        if (slotId < 0 || slotId >= SIM_NUM) {
            stringBuilder.append("invalid subId: ");
            stringBuilder.append(slotId);
            Log.e(LOG_TAG, stringBuilder.toString());
        } else {
            Bundle data = new Bundle();
            data.putString("record", record);
            broadcastNecEvent(slotId, NEC_EVENT_DATA_ONLINE_MEETING, data);
        }
    }

    public void broadcastNetworkDiagnosis(int slotId, Bundle data) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastNetworkDiagnosis invalid slotId ");
        } else {
            broadcastNecEvent(slotId, NEC_EVENT_NETWORK_DIAGNOSIS, data);
        }
    }

    public void broadcastDispatchedSmsId(int slotId, int errorCode, long messageId, boolean isWapPush, boolean isClass0, String mPackageName) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastDispatchedSmsId invalid phoneId");
            return;
        }
        Bundle data = new Bundle();
        data.putInt(OplusTelephonyIntents.EXTRA_SLOT_ID, slotId);
        data.putInt("errorCode", errorCode);
        data.putLong("messageId", messageId);
        data.putBoolean("isWapPush", isWapPush);
        data.putBoolean("isClass0", isClass0);
        data.putString("dispatchToPackage", mPackageName);
        broadcastNecEvent(slotId, NEC_EVENT_DISPATCHED_SMS_ID, data);
    }

    public void broadcastDispatchedMoSmsId(int slotId, int eventId, String networkType, int rat, int errorType, int errorCode, String success) {
        if (!isValidSlotId(slotId)) {
            Log.e(LOG_TAG, "broadcastDispatchedMoSmsId invalid phoneId！");
            return;
        }
        Bundle data = new Bundle();
        data.putInt(OplusTelephonyIntents.EXTRA_SLOT_ID, slotId);
        data.putInt("eventId", eventId);
        data.putString(OplusNetworkingControlManager.EXTRA_NETWORK_TYPE, networkType);
        data.putInt("rat", rat);
        data.putInt("errorType", errorType);
        data.putInt("errorCode", errorCode);
        data.putString("success", success);
        broadcastNecEvent(slotId, NEC_EVENT_DISPATCHED_SMS_MO_ID, data);
    }

    private boolean isValidSlotId(int slotId) {
        if (slotId < 0 || slotId > SIM_COUNT) {
            return false;
        }
        return true;
    }

    private static void necLog(String s) {
        Log.d(LOG_TAG, s);
    }
}
