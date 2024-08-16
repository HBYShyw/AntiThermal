package android.telephony;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telecom.Log;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import com.android.internal.telecom.ITelecomService;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.SmsApplication;
import com.oplus.internal.telephony.OplusPhoneInterFaceExt;
import java.util.List;

/* loaded from: classes.dex */
public class OplusOSTelephonyManager {
    private static final int CDMA = 72;
    private static final int EVDO = 10288;
    private static final int GSM = 32771;
    private static final int HS = 17280;
    private static final int LTE = 266240;
    private static final int NR = 524288;
    public static final int RAF_1xRTT = 64;
    public static final int RAF_EDGE = 2;
    public static final int RAF_EHRPD = 8192;
    public static final int RAF_EVDO_0 = 16;
    public static final int RAF_EVDO_A = 32;
    public static final int RAF_EVDO_B = 2048;
    public static final int RAF_GPRS = 1;
    public static final int RAF_GSM = 32768;
    public static final int RAF_HSDPA = 128;
    public static final int RAF_HSPA = 512;
    public static final int RAF_HSPAP = 16384;
    public static final int RAF_HSUPA = 256;
    public static final int RAF_IS95A = 8;
    public static final int RAF_IS95B = 8;
    public static final int RAF_LTE = 4096;
    public static final int RAF_LTE_CA = 262144;
    public static final int RAF_NR = 524288;
    public static final int RAF_TD_SCDMA = 65536;
    public static final int RAF_UMTS = 4;
    public static final int RAF_UNKNOWN = 0;
    private static final String TAG = "OplusOSTelephonyManager";
    private static final int WCDMA = 17284;
    private static OplusOSTelephonyManager sInstance;
    private CarrierConfigManager mCarrierConfigManager;
    private Context mContext;
    private OplusTelephonyManager mOplusTelephonyManager;
    private TelephonyManager mTelephonyManager;
    private static boolean isQcomGeminiSupport = false;
    private static boolean isMtkGeminiSupport = false;
    private static String vDescriptor = IOplusOSTelephony.OPLUS_SINGLE_CARD_DESCRIPTOR;
    private boolean mIsExpVersion = false;
    private boolean mIsDualLteSupported = true;

    public static OplusOSTelephonyManager getDefault(Context context) {
        OplusOSTelephonyManager oplusOSTelephonyManager;
        synchronized (OplusOSTelephonyManager.class) {
            if (sInstance == null) {
                sInstance = new OplusOSTelephonyManager(context);
            }
            oplusOSTelephonyManager = sInstance;
        }
        return oplusOSTelephonyManager;
    }

    public OplusOSTelephonyManager(Context context) {
        this.mContext = context;
        this.mTelephonyManager = TelephonyManager.from(context);
        this.mOplusTelephonyManager = OplusTelephonyManager.getInstance(context);
        initRemoteService();
    }

    private void initRemoteService() {
        isQcomGeminiSupport = true;
        isMtkGeminiSupport = false;
        vDescriptor = IOplusOSTelephony.OPLUS_SINGLE_CARD_DESCRIPTOR;
        this.mCarrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager != null) {
            this.mIsExpVersion = packageManager.hasSystemFeature("oplus.version.exp");
        }
    }

    @Deprecated
    public int oplusGetQcomActiveSubscriptionsCount() {
        Context context = this.mContext;
        if (context != null) {
            return SubscriptionManager.from(context).getActiveSubscriptionInfoCount();
        }
        return 0;
    }

    public String getSubscriberIdGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return null;
            }
            String vRet = this.mTelephonyManager.getSubscriberId(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport) {
            return null;
        }
        String vRet2 = this.mTelephonyManager.getSubscriberId();
        return vRet2;
    }

    @Deprecated
    public int getCallStateGemini(int slotID) {
        int[] subId;
        if (isQcomGeminiSupport) {
            int[] subId2 = SubscriptionManager.getSubId(slotID);
            if (subId2 == null || subId2.length <= 0) {
                return 0;
            }
            int vRet = this.mTelephonyManager.getCallState(subId2[0]);
            return vRet;
        }
        if (isMtkGeminiSupport || slotID != 0 || (subId = SubscriptionManager.getSubId(slotID)) == null || subId.length <= 0) {
            return 0;
        }
        int vRet2 = this.mTelephonyManager.getCallState(subId[0]);
        return vRet2;
    }

    public String getVoiceMailNumberGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return null;
            }
            String vRet = this.mTelephonyManager.getVoiceMailNumber(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport) {
            return null;
        }
        String vRet2 = this.mTelephonyManager.getVoiceMailNumber();
        return vRet2;
    }

    public String getLine1NumberGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return null;
            }
            String vRet = this.mTelephonyManager.getLine1Number(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport) {
            return null;
        }
        String vRet2 = this.mTelephonyManager.getLine1Number();
        return vRet2;
    }

    @Deprecated
    public int getSimStateGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int vRet = TelephonyManager.getDefault().getSimState(slotID);
            return vRet;
        }
        if (isMtkGeminiSupport || slotID != 0) {
            return -1;
        }
        int vRet2 = TelephonyManager.getDefault().getSimState();
        return vRet2;
    }

    public boolean hasIccCardGemini(int slotID) {
        if (isQcomGeminiSupport) {
            boolean vRet = TelephonyManager.getDefault().hasIccCard(slotID);
            return vRet;
        }
        if (isMtkGeminiSupport || slotID != 0) {
            return false;
        }
        boolean vRet2 = TelephonyManager.getDefault().hasIccCard();
        return vRet2;
    }

    public int getNetworkTypeGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return 0;
            }
            int vRet = this.mTelephonyManager.getNetworkType(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport) {
            return 0;
        }
        int vRet2 = this.mTelephonyManager.getNetworkType();
        return vRet2;
    }

    public boolean isNetworkRoamingGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return false;
            }
            boolean vRet = TelephonyManager.getDefault().isNetworkRoaming(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport || slotID != 0) {
            return false;
        }
        boolean vRet2 = TelephonyManager.getDefault().isNetworkRoaming();
        return vRet2;
    }

    public String getDeviceIdGemini(int slotID) {
        if (isQcomGeminiSupport) {
            String vRet = this.mTelephonyManager.getDeviceId(slotID);
            return vRet;
        }
        if (isMtkGeminiSupport) {
            return null;
        }
        this.mTelephonyManager.getDeviceId();
        return null;
    }

    public void listenGemini(Context context, PhoneStateListener listener, int events, int slotID) {
        TelephonyManager telephonyManager = null;
        int[] subIds = SubscriptionManager.getSubId(slotID);
        if (subIds != null && subIds.length > 0) {
            telephonyManager = new TelephonyManager(context, subIds[0]);
        }
        if (telephonyManager != null) {
            telephonyManager.listen(listener, events);
        } else {
            log("listenGemini ERROR!");
        }
    }

    @Deprecated
    public boolean isSimInsert(int slotID) {
        if (isQcomGeminiSupport) {
            boolean vRet = hasIccCardGemini(slotID);
            return vRet;
        }
        if (isMtkGeminiSupport || slotID != 0) {
            return false;
        }
        boolean vRet2 = hasIccCardGemini(slotID);
        return vRet2;
    }

    @Deprecated
    public String oplusGetIccCardTypeGemini(int slotID) {
        if (!isQcomGeminiSupport) {
            return "";
        }
        try {
            String vRet = getIccCardTypeGemini(slotID);
            return vRet;
        } catch (Exception e) {
            return "";
        }
    }

    public String getNetworkOperatorGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return "";
            }
            String vRet = TelephonyManager.getDefault().getNetworkOperator(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport || slotID != 0) {
            return "";
        }
        String vRet2 = TelephonyManager.getDefault().getNetworkOperator();
        return vRet2;
    }

    public String getSimOperatorGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return "";
            }
            String vRet = TelephonyManager.getDefault().getSimOperator(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport || slotID != 0) {
            return "";
        }
        String vRet2 = TelephonyManager.getDefault().getSimOperator();
        return vRet2;
    }

    public int getVoiceNetworkTypeGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return 0;
            }
            int vRet = this.mTelephonyManager.getVoiceNetworkType(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport) {
            return 0;
        }
        int vRet2 = this.mTelephonyManager.getVoiceNetworkType();
        return vRet2;
    }

    public int getCurrentPhoneTypeGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return 0;
            }
            int vRet = TelephonyManager.getDefault().getCurrentPhoneType(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport || slotID != 0) {
            return 0;
        }
        int vRet2 = TelephonyManager.getDefault().getCurrentPhoneType();
        return vRet2;
    }

    public String getSimSerialNumberGemini(int slotID) {
        if (isQcomGeminiSupport) {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId == null || subId.length <= 0) {
                return "";
            }
            String vRet = this.mTelephonyManager.getSimSerialNumber(subId[0]);
            return vRet;
        }
        if (isMtkGeminiSupport) {
            return "";
        }
        String vRet2 = this.mTelephonyManager.getSimSerialNumber();
        return vRet2;
    }

    private ITelephony getITelephony() {
        return ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
    }

    private ITelecomService getTelecomService() {
        return ITelecomService.Stub.asInterface(ServiceManager.getService("telecom"));
    }

    @Deprecated
    public boolean endCallGemini(int slotID) {
        try {
            return getTelecomService().endCall(this.mContext.getPackageName());
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    @Deprecated
    public void answerRingingCallGemini(int subscription) {
        try {
            if (isQcomGeminiSupport) {
                getTelecomService().acceptRingingCall(this.mContext.getPackageName());
            } else if (!isMtkGeminiSupport) {
                getTelecomService().acceptRingingCall(this.mContext.getPackageName());
            }
        } catch (RemoteException e) {
        } catch (NullPointerException e2) {
        }
    }

    @Deprecated
    public boolean isRingingGemini(int slotID, String callingPackage) {
        try {
            return this.mTelephonyManager.isRinging();
        } catch (NullPointerException e) {
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    @Deprecated
    public boolean isIdleGemini(int slotID, String callingPackage) {
        try {
            return this.mTelephonyManager.isIdle();
        } catch (NullPointerException e) {
            return true;
        } catch (Exception e2) {
            return true;
        }
    }

    @Deprecated
    public boolean isOffhookGemini(int slotID, String callingPackage) {
        try {
            return this.mTelephonyManager.isOffhook();
        } catch (NullPointerException e) {
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    @Deprecated
    public void silenceRingerGemini(int slotID, String callingPackage) {
        try {
            getTelecomService().silenceRinger(callingPackage);
        } catch (RemoteException e) {
            Log.w(TAG, "Error calling ITelecomService#silenceRinger", new Object[]{e});
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public boolean showInCallScreenGemini(boolean showDialpad, String callingPackage, String callingFeatureId) {
        try {
            getTelecomService().showInCallScreen(showDialpad, callingPackage, callingFeatureId);
            return true;
        } catch (RemoteException e) {
            Log.w(TAG, "Error calling ITelecomService#showInCallScreen", new Object[]{e});
            return false;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean supplyPuk(String puk, String pin, int slotID) {
        int[] subId;
        try {
            if (!isQcomGeminiSupport || (subId = SubscriptionManager.getSubId(slotID)) == null || subId.length <= 0) {
                return false;
            }
            boolean vRet = getITelephony().supplyPukForSubscriber(subId[0], puk, pin);
            return vRet;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public boolean supplyPin(String pin, int slotID) {
        int[] subId;
        try {
            if (!isQcomGeminiSupport || (subId = SubscriptionManager.getSubId(slotID)) == null || subId.length <= 0) {
                return false;
            }
            boolean vRet = getITelephony().supplyPinForSubscriber(subId[0], pin);
            return vRet;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public int[] supplyPukReportResult(String puk, String pin, int slotID) {
        int[] subId;
        try {
            if (!isQcomGeminiSupport || (subId = SubscriptionManager.getSubId(slotID)) == null) {
                return null;
            }
            return getITelephony().supplyPukReportResultForSubscriber(subId[0], puk, pin);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public int[] supplyPinReportResult(String pin, int slotID) {
        int[] subId;
        try {
            if (!isQcomGeminiSupport || (subId = SubscriptionManager.getSubId(slotID)) == null) {
                return null;
            }
            return getITelephony().supplyPinReportResultForSubscriber(subId[0], pin);
        } catch (RemoteException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    public boolean oplusIsSubActive(int subscription) {
        int vCardState = getSimStateGemini(subscription);
        if (vCardState == 5) {
            return true;
        }
        return false;
    }

    @Deprecated
    public int oplusGetActiveSubscriptionsCount(Context mContext) {
        return SubscriptionManager.from(mContext).getActiveSubscriptionInfoCount();
    }

    @Deprecated
    public int oplusGetDefaultSubscription() {
        int vRet = SubscriptionManager.getDefaultSubscriptionId();
        return vRet;
    }

    public int oplusGetDataSubscription() {
        int vSUBID = SubscriptionManager.getDefaultDataSubscriptionId();
        int vSlotID = SubscriptionManager.getSlotIndex(vSUBID);
        return vSlotID;
    }

    public void oplusSetDataSubscription(Context mContext, int slotId) {
        int[] subId = SubscriptionManager.getSubId(slotId);
        if (subId != null && subId.length > 0) {
            SubscriptionManager.from(mContext).setDefaultDataSubId(subId[0]);
        }
    }

    private static IBinder getRemoteServiceBinder() {
        IBinder mRemote = ServiceManager.getService("phone");
        if (mRemote == null) {
            log("***********************************");
            log("OplusOSTelephonyManager is NULL !!!1");
            log("***********************************");
            return null;
        }
        return mRemote;
    }

    @Deprecated
    public int oplusGetSimIndicatorState(int subscription) {
        return 0;
    }

    public int oplusSetPreferredNetworkType(int slotID, int type) {
        try {
            int[] subId = SubscriptionManager.getSubId(slotID);
            if (subId != null) {
                int length = subId.length;
            }
        } catch (NullPointerException ex) {
            Log.w(TAG, "setPreferredNetworkType NPE", new Object[]{ex});
        }
        if (0 == 1) {
            return 0;
        }
        return -1;
    }

    @Deprecated
    public boolean oplusSetLine1Number(int subscription, String phoneNumber) {
        return this.mTelephonyManager.setLine1NumberForDisplay(subscription, null, phoneNumber);
    }

    @Deprecated
    public boolean oplusGetIccLockEnabled(int slotId) {
        return this.mOplusTelephonyManager.oplusGetIccLockEnabled(slotId);
    }

    public String oplusGetScAddressGemini(int subscription, int slotId) {
        return this.mOplusTelephonyManager.getScAddressGemini(subscription);
    }

    public void oplusSetScAddressGemini(int subscription, String scAddr, int simId) {
        this.mOplusTelephonyManager.setScAddressGemini(subscription, scAddr);
    }

    @Deprecated
    public String getIccCardTypeGemini(int slotId) {
        return this.mOplusTelephonyManager.getIccCardType(slotId);
    }

    @Deprecated
    public String oplusGetQcomImeiGemini(int subscription) {
        return null;
    }

    @Deprecated
    public String[] oplusGetQcomLTECDMAImei(int phoneId) {
        return this.mOplusTelephonyManager.getLteCdmaImsi(phoneId);
    }

    @Deprecated
    public boolean oplusIsWhiteSIMCard(int slotId) {
        return this.mOplusTelephonyManager.isTestCard(slotId);
    }

    @Deprecated
    public String oplusGetMeid(int subscription) {
        return null;
    }

    public boolean isUriFileExist(String vUri) {
        return this.mOplusTelephonyManager.isUriFileExist(vUri);
    }

    @Deprecated
    public CellLocation getCellLocation(int slotId) {
        return this.mOplusTelephonyManager.getCellLocation(slotId);
    }

    private static CellLocation newCellLocationFromBundle(Bundle bundle, String cardType) {
        int phoneType = bundle.getInt("type", 0);
        if (OplusPhoneInterFaceExt.ICCTYPE_CSIM.equals(cardType) || OplusPhoneInterFaceExt.ICCTYPE_RUIM.equals(cardType) || phoneType == 2) {
            return new CdmaCellLocation(bundle);
        }
        return new GsmCellLocation(bundle);
    }

    @Deprecated
    public static int oplusgetActiveSubInfoCount(Context context) {
        return SubscriptionManager.from(context).getActiveSubscriptionInfoCount();
    }

    @Deprecated
    public static int oplusgetPhoneId(Context context, int subId) {
        return SubscriptionManager.getPhoneId(subId);
    }

    public static int oplusgetSubId(Context context, int slotId) {
        int[] subId = SubscriptionManager.getSubId(slotId);
        if (subId != null && subId.length > 0) {
            int vRetSubId = subId[0];
            return vRetSubId;
        }
        return -1000;
    }

    public static int oplusgetSlotId(Context context, int subId) {
        return SubscriptionManager.getSlotIndex(subId);
    }

    @Deprecated
    public static int oplusgetOnDemandDataSubId(Context context) {
        return -1;
    }

    @Deprecated
    public static int oplusgetSubState(Context context, int subId) {
        return -1;
    }

    public static boolean oplusisValidPhoneId(Context context, int phoneId) {
        return SubscriptionManager.isValidPhoneId(phoneId);
    }

    public static boolean oplusisValidSlotId(Context context, int slotId) {
        return SubscriptionManager.isValidSlotIndex(slotId);
    }

    public static boolean oplusisValidSubId(Context context, int subId) {
        return SubscriptionManager.isValidSubscriptionId(subId);
    }

    public static int oplusgetDefaultDataPhoneId(Context context) {
        SubscriptionManager.from(context);
        return SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
    }

    public static int oplusgetDefaultDataSubId(Context context) {
        int vRet = SubscriptionManager.getDefaultDataSubscriptionId();
        return vRet;
    }

    public static int oplusgetDefaultSmsPhoneId(Context context) {
        SubscriptionManager.from(context);
        int vRet = SubscriptionManager.getPhoneId(SubscriptionManager.getDefaultSmsSubscriptionId());
        return vRet;
    }

    public static int oplusgetDefaultSmsSubId(Context context) {
        int vRet = SubscriptionManager.getDefaultSmsSubscriptionId();
        return vRet;
    }

    @Deprecated
    public static int oplusgetDefaultSubId(Context context) {
        int vRet = SubscriptionManager.getDefaultSubscriptionId();
        return vRet;
    }

    public String getNetworkCountryIso(int subId) {
        return TelephonyManager.getDefault().getNetworkCountryIso(subId);
    }

    public boolean handlePinMmiForSubscriber(int subId, String dialString) {
        try {
            return getITelephony().handlePinMmiForSubscriber(subId, dialString);
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    @Deprecated
    public String getIccOperatorNumeric(int subId) {
        return this.mOplusTelephonyManager.getOperatorNumericForData(subId);
    }

    public static void setDefaultApplication(String packageName, Context context) {
        SmsApplication.setDefaultApplication(packageName, context);
    }

    private static void log(String msg) {
        Log.d(TAG, msg, new Object[0]);
    }

    @Deprecated
    public boolean oplusIsQcomSubActive(int slotId) {
        int[] subId = SubscriptionManager.getSubId(slotId);
        return (subId == null || subId.length == 0 || getSubState(subId[0]) != 1) ? false : true;
    }

    @Deprecated
    public boolean isRingingGemini(int slotID) {
        Context context = this.mContext;
        if (context != null) {
            return isRingingGemini(slotID, context.getOpPackageName());
        }
        return false;
    }

    @Deprecated
    public void silenceRingerGemini(int slotID) {
        Context context = this.mContext;
        if (context != null) {
            silenceRingerGemini(slotID, context.getOpPackageName());
        }
    }

    public void listenGemini(PhoneStateListener listener, int events, int slotID) {
        Context context = this.mContext;
        if (context != null) {
            listenGemini(context, listener, events, slotID);
        }
    }

    @Deprecated
    public boolean isIdleGemini(int slotId) {
        Context context = this.mContext;
        if (context != null) {
            return isIdleGemini(slotId, context.getOpPackageName());
        }
        return true;
    }

    @Deprecated
    public boolean isOplusHasSoftSimCard() {
        return oplusGetSoftSimCardSlotId() >= 0;
    }

    @Deprecated
    public int oplusGetSoftSimCardSlotId() {
        return this.mOplusTelephonyManager.getSoftSimCardSlotId();
    }

    public void oplusSetDataRoamingEnabled(int slotId, boolean enable) {
        this.mOplusTelephonyManager.setDataRoamingEnabled(slotId, enable);
    }

    @Deprecated
    public boolean oplusIsImsRegistered(Context context, int slotId) {
        int[] subId = SubscriptionManager.getSubId(slotId);
        if (subId == null || subId.length == 0) {
            return false;
        }
        return this.mTelephonyManager.isImsRegistered(subId[0]);
    }

    public String oplusGetPlmnOverride(String operatorNumic, ServiceState ss) {
        return OplusTelephonyFunction.oplusGetPlmnOverride(this.mContext, operatorNumic, ss);
    }

    @Deprecated
    public String oplusGetOemSpn(int slotId) {
        return this.mOplusTelephonyManager.getOemSpn(slotId);
    }

    @Deprecated
    public boolean oplusMvnoMatches(int phoneId, int family, String mvnoType, String mvnoMatchData) {
        return false;
    }

    public boolean isOplusSingleSimCard() {
        return OplusTelephonyFunction.oplusGetSingleSimCard();
    }

    @Deprecated
    public boolean oplusIsVolteEnabledByPlatform(Context context, int phoneId) {
        return this.mOplusTelephonyManager.isVolteEnabledByPlatform(phoneId);
    }

    @Deprecated
    public boolean oplusIsVtEnabledByPlatform(Context context, int phoneId) {
        return this.mOplusTelephonyManager.isVtEnabledByPlatform(phoneId);
    }

    @Deprecated
    public boolean oplusIsWfcEnabledByPlatform(Context context, int phoneId) {
        return this.mOplusTelephonyManager.isWfcEnabledByPlatform(phoneId);
    }

    @Deprecated
    public String oplusGetIccId(int slotId) {
        int[] subId = SubscriptionManager.getSubId(slotId);
        if (subId == null || subId.length == 0) {
            return null;
        }
        return this.mTelephonyManager.getSimSerialNumber(subId[0]);
    }

    public void setDualLteEnabled(boolean enable) {
        IBinder remoteServiceBinder = getRemoteServiceBinder();
        if (remoteServiceBinder == null) {
            log("setDualLteEnabled remoteServiceBinder is null, return!");
            return;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            try {
                _data.writeInterfaceToken(vDescriptor);
                _data.writeInt(enable ? 1 : 0);
                remoteServiceBinder.transact(IOplusOSTelephony.OPLUS_SET_DUAL_LTE_ENABLED, _data, _reply, 0);
                _reply.readException();
            } catch (Exception e) {
                log("setDualLteEnabled ERROR !!! " + e);
            }
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    @Deprecated
    public boolean isDualLteEnabled() {
        return this.mOplusTelephonyManager.isDualLteEnabled();
    }

    public boolean isDualLteSupportedByPlatform() {
        if (this.mIsExpVersion) {
            boolean defaultValue = CarrierConfigManager.getDefaultConfig().getBoolean("config_oplus_dual_lte_available_bool");
            if (defaultValue) {
                this.mIsDualLteSupported = getBooleanCarrierConfig("config_oplus_dual_lte_available_bool", 0) && getBooleanCarrierConfig("config_oplus_dual_lte_available_bool", 1);
            } else {
                if (!getBooleanCarrierConfig("config_oplus_dual_lte_available_bool", 0) && !getBooleanCarrierConfig("config_oplus_dual_lte_available_bool", 1)) {
                    r2 = false;
                }
                this.mIsDualLteSupported = r2;
            }
        }
        log("isDualLteSupportedByPlatform mIsDualLteSupported = " + this.mIsDualLteSupported);
        return this.mIsDualLteSupported;
    }

    public boolean getBooleanCarrierConfig(String key, int phoneId) {
        if (TextUtils.isEmpty(key)) {
            log("getBooleanCarrierConfig return false for key is null!");
            return false;
        }
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length == 0) {
            return false;
        }
        log("getBooleanCarrierConfig: phoneId=" + phoneId + " subId=" + subIds[0] + " key = " + key);
        PersistableBundle b = null;
        CarrierConfigManager carrierConfigManager = this.mCarrierConfigManager;
        if (carrierConfigManager != null) {
            b = carrierConfigManager.getConfigForSubId(subIds[0]);
        }
        if (b != null) {
            return b.getBoolean(key);
        }
        return CarrierConfigManager.getDefaultConfig().getBoolean(key);
    }

    public List<SubscriptionInfo> getSelectableSubscriptionInfoList() {
        SubscriptionManager sm = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        return sm.getSelectableSubscriptionInfoList();
    }

    public static int getNetworkTypeFromRaf(int raf) {
        switch (getAdjustedRaf(raf)) {
            case 72:
                return 5;
            case EVDO /* 10288 */:
                return 6;
            case 10360:
                return 4;
            case WCDMA /* 17284 */:
                return 2;
            case GSM /* 32771 */:
                return 1;
            case 50055:
                return 0;
            case 50127:
            case 60415:
                return 7;
            case 65536:
                return 13;
            case 82820:
                return 14;
            case 98307:
                return 16;
            case 115591:
                return 18;
            case 125951:
                return 21;
            case LTE /* 266240 */:
                return 11;
            case 276600:
                return 8;
            case 283524:
                return 12;
            case 316295:
                return 9;
            case 316367:
            case 392191:
                return 22;
            case 326655:
                return 10;
            case 331776:
                return 15;
            case 349060:
                return 19;
            case 364547:
                return 17;
            case 381831:
                return 20;
            case 524288:
                return 23;
            case 790528:
                return 24;
            case 800888:
                return 25;
            case 807812:
                return 28;
            case 840583:
                return 26;
            case 850943:
                return 27;
            case 856064:
                return 29;
            case 873348:
                return 31;
            case 888835:
                return 30;
            case 906119:
                return 32;
            case 916479:
                return 33;
            default:
                return RILConstants.PREFERRED_NETWORK_MODE;
        }
    }

    private static int getAdjustedRaf(int raf) {
        int raf2 = (raf & GSM) > 0 ? GSM | raf : raf;
        int raf3 = (raf2 & WCDMA) > 0 ? raf2 | WCDMA : raf2;
        int raf4 = (raf3 & 72) > 0 ? raf3 | 72 : raf3;
        int raf5 = (raf4 & EVDO) > 0 ? raf4 | EVDO : raf4;
        int raf6 = (raf5 & LTE) > 0 ? LTE | raf5 : raf5;
        return (raf6 & 524288) > 0 ? 524288 | raf6 : raf6;
    }

    public static boolean getBooleanCarrierConfig(Context context, String key, int phoneId) {
        if (context == null || TextUtils.isEmpty(key)) {
            log("getBooleanCarrierConfig return false for context is null or key is null!");
            return false;
        }
        CarrierConfigManager configManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length == 0) {
            return false;
        }
        log("getBooleanCarrierConfig: phoneId=" + phoneId + " subId=" + subIds[0] + " key = " + key);
        PersistableBundle b = null;
        if (configManager != null) {
            b = configManager.getConfigForSubId(subIds[0]);
        }
        if (b != null) {
            return b.getBoolean(key);
        }
        return CarrierConfigManager.getDefaultConfig().getBoolean(key);
    }

    @Deprecated
    public void activateSubId(int subId) {
        SubscriptionManager.from(this.mContext).setUiccApplicationsEnabled(subId, true);
    }

    @Deprecated
    public void deactivateSubId(int subId) {
        SubscriptionManager.from(this.mContext).setUiccApplicationsEnabled(subId, false);
    }

    @Deprecated
    public int getSubState(int subId) {
        return this.mOplusTelephonyManager.getSubState(subId);
    }
}
