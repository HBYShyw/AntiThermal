package com.oplus.internal.telephony;

import android.app.ActivityThread;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Message;
import android.os.Process;
import android.os.SystemProperties;
import android.os.customize.OplusCustomizeConnectivityManager;
import android.telephony.OplusTelephonyManager;
import android.telephony.Rlog;
import com.android.internal.telephony.IOplusCallManager;
import com.android.internal.telephony.IOplusGsmCdmaPhone;
import com.android.internal.telephony.IOplusNetworkManager;
import com.android.internal.telephony.IOplusPhone;
import com.android.internal.telephony.IOplusTelephonyExtCallback;
import com.android.internal.telephony.OplusTelephonyFactory;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.uicc.IOplusSIMRecords;
import com.oplus.internal.telephony.IccCardConstantsExt;
import com.oplus.internal.telephony.gsm.NetworkInfoWithAcT;

/* loaded from: classes.dex */
public class OplusPhoneInterFaceExt {
    public static final String ICCTYPE_CSIM = "CSIM";
    public static final String ICCTYPE_ISIM = "ISIM";
    public static final String ICCTYPE_RUIM = "RUIM";
    public static final String ICCTYPE_SIM = "SIM";
    public static final String ICCTYPE_UNKNOWN = "UNKNOWN";
    public static final String ICCTYPE_USIM = "USIM";
    private static final int INVALID = -1;
    private static final String PROPERTY_SIM_SLOT_LOCK_POLICY = "vendor.gsm.sim.slot.lock.policy";
    private static final String PROPERTY_SIM_SLOT_LOCK_STATE = "vendor.gsm.sim.slot.lock.state";
    private static final String PROPERTY_SML_MODE = "ro.vendor.sim_me_lock_mode";
    public static final String SIM_STATE_ESSENTIAL_RECORDS_LOADED = "ESSENTIAL_LOADED";
    private static final String TAG = "OplusPhoneInterFaceExt";
    private Context mContext;
    private boolean mIsSmlLockMode = SystemProperties.get(PROPERTY_SML_MODE, "").equals(OplusCustomizeConnectivityManager.WLAN_POLICY_STRING_OFF);
    private static OplusPhoneInterFaceExt sInstance = null;
    public static final String[] PROPERTY_RIL_CT3G = {"vendor.gsm.ril.ct3g", "vendor.gsm.ril.ct3g.2", "vendor.gsm.ril.ct3g.3", "vendor.gsm.ril.ct3g.4"};
    private static final String[] PROPERTY_SIM_SLOT_LOCK_CARD_VALID = {"vendor.gsm.sim.slot.lock.card.valid", "vendor.gsm.sim.slot.lock.card.valid.2", "vendor.gsm.sim.slot.lock.card.valid.3", "vendor.gsm.sim.slot.lock.card.valid.4"};
    private static final String[] PROPERTY_SIM_SLOT_LOCK_SERVICE_CAPABILITY = {"vendor.gsm.sim.slot.lock.service.capability", "vendor.gsm.sim.slot.lock.service.capability.2", "vendor.gsm.sim.slot.lock.service.capability.3", "vendor.gsm.sim.slot.lock.service.capability.4"};
    private static final String[] PROPERTY_RIL_CDMA_CARD_TYPE = {"vendor.ril.cdma.card.type.1", "vendor.ril.cdma.card.type.2", "vendor.ril.cdma.card.type.3", "vendor.ril.cdma.card.type.4"};
    private static final String[] PROPERTY_RIL_FULL_UICC_TYPE = {"vendor.gsm.ril.fulluicctype", "vendor.gsm.ril.fulluicctype.2", "vendor.gsm.ril.fulluicctype.3", "vendor.gsm.ril.fulluicctype.4"};

    private OplusPhoneInterFaceExt() {
        if (this.mContext == null) {
            this.mContext = ActivityThread.currentApplication().getApplicationContext();
        }
    }

    public OplusPhoneInterFaceExt(Context context) {
        this.mContext = context;
    }

    public static OplusPhoneInterFaceExt getDefault() {
        OplusPhoneInterFaceExt oplusPhoneInterFaceExt;
        synchronized (OplusPhoneInterFaceExt.class) {
            if (sInstance == null) {
                sInstance = new OplusPhoneInterFaceExt();
            }
            oplusPhoneInterFaceExt = sInstance;
        }
        return oplusPhoneInterFaceExt;
    }

    public boolean isTestCard(int slotId) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Process.myPid(), Process.myUid(), getCallingPackage(), (String) null, "isTestCard")) {
            return false;
        }
        if (Process.myUid() != 1001) {
            throw new SecurityException("isTestCard : Package " + getCallingPackage() + " does not belong to phone");
        }
        OplusTelephonyFactory.getInstance();
        IOplusPhone tmpPhone = OplusTelephonyFactory.getFeatureFromCache(slotId, IOplusPhone.DEFAULT);
        if (tmpPhone != null) {
            return tmpPhone.isTestCard();
        }
        return false;
    }

    public void getPreferedOperatorList(int phoneId, Message onComplete) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Process.myPid(), Process.myUid(), getCallingPackage(), (String) null, "getPreferedOperatorList")) {
            AsyncResult.forMessage(onComplete, (Object) null, new Exception("not permissions"));
            onComplete.sendToTarget();
        }
        if (Process.myUid() != 1001) {
            throw new SecurityException("getPreferedOperatorList : Package " + getCallingPackage() + " does not belong to phone");
        }
        Rlog.d(TAG, "getPreferedOperatorList enter");
        IOplusSIMRecords interfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusSIMRecords.DEFAULT);
        interfaceImpl.getPreferedOperatorList(onComplete);
    }

    public void setPOLEntry(int phoneId, NetworkInfoWithAcT networkWithAct, Message onComplete) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Process.myPid(), Process.myUid(), getCallingPackage(), (String) null, "setPOLEntry")) {
            AsyncResult.forMessage(onComplete, (Object) null, new Exception("not permissions"));
            onComplete.sendToTarget();
        }
        if (Process.myUid() != 1001) {
            throw new SecurityException("setPOLEntry : Package " + getCallingPackage() + " does not belong to phone");
        }
        IOplusSIMRecords interfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusSIMRecords.DEFAULT);
        String plmn = networkWithAct.getOperatorNumeric();
        int act = networkWithAct.getAccessTechnology();
        int priority = networkWithAct.getPriority();
        interfaceImpl.setPOLEntry(plmn, act, priority, onComplete);
    }

    public String getIccCardType(int phoneId) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Process.myPid(), Process.myUid(), getCallingPackage(), (String) null, "getIccCardType")) {
            return "";
        }
        if (Process.myUid() != 1001) {
            throw new SecurityException("getIccCardType : Package " + getCallingPackage() + " does not belong to phone");
        }
        IOplusGsmCdmaPhone interfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusGsmCdmaPhone.DEFAULT);
        return interfaceImpl.getIccCardType();
    }

    public int getEsimGpio(int phoneId) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Process.myPid(), Process.myUid(), getCallingPackage(), (String) null, "getEsimGpio")) {
            return -1;
        }
        if (Process.myUid() != 1001) {
            throw new SecurityException("getEsimGpio : Package " + getCallingPackage() + " does not belong to phone");
        }
        IOplusPhone mInterfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusPhone.DEFAULT);
        return mInterfaceImpl.getEsimGpio();
    }

    public int setEsimGpio(int phoneId, int operation) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Process.myPid(), Process.myUid(), getCallingPackage(), (String) null, "setEsimGpio")) {
            return -1;
        }
        if (Process.myUid() != 1001) {
            throw new SecurityException("setEsimGpio : Package " + getCallingPackage() + " does not belong to phone");
        }
        IOplusPhone mInterfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusPhone.DEFAULT);
        return mInterfaceImpl.setEsimGpio(operation);
    }

    public int setHotswap(int phoneId) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Process.myPid(), Process.myUid(), getCallingPackage(), (String) null, "setHotswap")) {
            return -1;
        }
        if (Process.myUid() != 1001) {
            throw new SecurityException("setHotswap : Package " + getCallingPackage() + " does not belong to phone");
        }
        IOplusPhone mInterfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusPhone.DEFAULT);
        return mInterfaceImpl.setHotswap();
    }

    public int setUimPower(int phoneId, int operation) {
        if (!TelephonyPermissions.checkReadPhoneStateOnAnyActiveSub(this.mContext, Process.myPid(), Process.myUid(), getCallingPackage(), (String) null, "setUimPower")) {
            return -1;
        }
        if (Process.myUid() != 1001) {
            throw new SecurityException("setUimPower : Package " + getCallingPackage() + " does not belong to phone");
        }
        IOplusPhone mInterfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusPhone.DEFAULT);
        return mInterfaceImpl.setUimPower(operation);
    }

    public String[] getLteCdmaImsi(int phoneId) {
        if (Process.myUid() != 1001) {
            throw new SecurityException("getLteCdmaImsi : Package " + getCallingPackage() + " does not belong to phone");
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0) {
            Rlog.e(TAG, "READ_PRIVILEGED_PHONE_STATE. permission needed");
            return null;
        }
        IOplusPhone mInterfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusPhone.DEFAULT);
        return mInterfaceImpl.getLteCdmaImsi(phoneId);
    }

    public boolean isCapabilitySwitching() {
        return ProxyController.getInstance().getProxyControllerWrapper().isCapabilitySwitching();
    }

    public int getSimLockPolicy() {
        if (this.mIsSmlLockMode) {
            int policy = SystemProperties.getInt(PROPERTY_SIM_SLOT_LOCK_POLICY, -1);
            Rlog.d(TAG, "getSimLockPolicy: " + policy);
            return policy;
        }
        return 0;
    }

    public int getShouldServiceCapability(int slotId) {
        if (this.mIsSmlLockMode) {
            if (slotId >= 0) {
                String[] strArr = PROPERTY_SIM_SLOT_LOCK_SERVICE_CAPABILITY;
                if (slotId < strArr.length) {
                    int capability = SystemProperties.getInt(strArr[slotId], -1);
                    logd("getShouldServiceCapability: " + capability + ",slotId: " + slotId);
                    return capability;
                }
            }
            loge("getShouldServiceCapability: invalid slotId: " + slotId);
            return 4;
        }
        return 0;
    }

    public IccCardConstantsExt.CardType getCdmaCardType(int slotId) {
        if (slotId < 0 || slotId >= PROPERTY_RIL_CT3G.length) {
            Rlog.e(TAG, "getCdmaCardType: invalid slotId " + slotId);
            return null;
        }
        IccCardConstantsExt.CardType mCdmaCardType = IccCardConstantsExt.CardType.UNKNOW_CARD;
        String result = SystemProperties.get(PROPERTY_RIL_CDMA_CARD_TYPE[slotId], "");
        if (!result.equals("")) {
            int cardtype = Integer.parseInt(result);
            mCdmaCardType = IccCardConstantsExt.CardType.getCardTypeFromInt(cardtype);
        }
        logd("getCdmaCardType slotId: " + slotId + " result: " + result + "  mCdmaCardType: " + mCdmaCardType);
        return mCdmaCardType;
    }

    public String[] getSupportCardType(int slotId) {
        String[] values = null;
        if (slotId >= 0) {
            String[] strArr = PROPERTY_RIL_FULL_UICC_TYPE;
            if (slotId < strArr.length) {
                String prop = SystemProperties.get(strArr[slotId], "");
                if (!prop.equals("") && prop.length() > 0) {
                    values = prop.split(",");
                }
                loge("getSupportCardType slotId " + slotId + ", prop value= " + prop + ", size= " + (values != null ? values.length : 0));
                return values;
            }
        }
        loge("getSupportCardType: invalid slotId " + slotId);
        return null;
    }

    public int checkValidCard(int slotId) {
        if (this.mIsSmlLockMode) {
            if (slotId >= 0) {
                String[] strArr = PROPERTY_SIM_SLOT_LOCK_CARD_VALID;
                if (slotId < strArr.length) {
                    int validCard = SystemProperties.getInt(strArr[slotId], -1);
                    Rlog.d(TAG, "checkValidCard: " + validCard + ",slotId: " + slotId);
                    return validCard;
                }
            }
            Rlog.e(TAG, "checkValidCard: invalid slotId " + slotId);
            return 2;
        }
        return 0;
    }

    public int getSimLockState() {
        if (this.mIsSmlLockMode) {
            int lockState = SystemProperties.getInt(PROPERTY_SIM_SLOT_LOCK_STATE, -1);
            Rlog.d(TAG, "getSimLockState: " + lockState);
            return lockState;
        }
        return 1;
    }

    public void triggerModeSwitchByEcc(int phoneId, int mode, Message response) {
        if (Process.myUid() != 1001) {
            throw new SecurityException("triggerModeSwitchByEcc : Package " + getCallingPackage() + " does not belong to phone");
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0) {
            Rlog.e(TAG, "READ_PRIVILEGED_PHONE_STATE. permission needed");
            return;
        }
        IOplusCallManager impl = OplusTelephonyFactory.getInstance().getFeature(IOplusCallManager.DEFAULT, new Object[0]);
        if (impl != null) {
            impl.triggerModeSwitchByEcc(phoneId, mode, response);
        }
    }

    public void setEccMode(int phoneId, String number, int enable, int airplaneMode, int imsReg, Message result) {
        if (Process.myUid() != 1001) {
            throw new SecurityException("setEccMode : Package " + getCallingPackage() + " does not belong to phone");
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0) {
            Rlog.e(TAG, "READ_PRIVILEGED_PHONE_STATE. permission needed");
            return;
        }
        IOplusCallManager impl = OplusTelephonyFactory.getInstance().getFeature(IOplusCallManager.DEFAULT, new Object[0]);
        if (impl != null) {
            impl.setEccMode(phoneId, number, enable, airplaneMode, imsReg, result);
        }
    }

    public void getCurrentPOLList(int phoneId, Message result) {
        if (Process.myUid() != 1001) {
            throw new SecurityException("getCurrentPOLList : Package " + getCallingPackage() + " does not belong to phone");
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0) {
            Rlog.e(TAG, "READ_PRIVILEGED_PHONE_STATE. permission needed");
            return;
        }
        IOplusNetworkManager impl = OplusTelephonyFactory.getInstance().getFeature(IOplusNetworkManager.DEFAULT, new Object[0]);
        if (impl != null) {
            impl.getCurrentPOLList(phoneId, result);
        }
    }

    public void getPOLCapability(int phoneId, Message result) {
        if (Process.myUid() != 1001) {
            throw new SecurityException("getPOLCapability : Package " + getCallingPackage() + " does not belong to phone");
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0) {
            Rlog.e(TAG, "READ_PRIVILEGED_PHONE_STATE. permission needed");
            return;
        }
        IOplusNetworkManager impl = OplusTelephonyFactory.getInstance().getFeature(IOplusNetworkManager.DEFAULT, new Object[0]);
        if (impl != null) {
            impl.getPOLCapability(phoneId, result);
        }
    }

    public void setPOLEntry(int phoneId, int index, String numeric, int nAct, Message result) {
        if (Process.myUid() != 1001) {
            throw new SecurityException("setPOLEntry : Package " + getCallingPackage() + " does not belong to phone");
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") != 0) {
            Rlog.e(TAG, "READ_PRIVILEGED_PHONE_STATE. permission needed");
            return;
        }
        IOplusNetworkManager impl = OplusTelephonyFactory.getInstance().getFeature(IOplusNetworkManager.DEFAULT, new Object[0]);
        if (impl != null) {
            impl.setPOLEntry(phoneId, index, numeric, nAct, result);
        }
    }

    public void registerExtCallback(String packageName, OplusTelephonyExtCallbackBase callback) {
        if (callback == null) {
            Rlog.e(TAG, "callback is null");
        } else {
            OplusTelephonyManager.getInstance(this.mContext).registerCallback(packageName, (IOplusTelephonyExtCallback) callback.getBinder());
        }
    }

    public String getCallingPackage() {
        return this.mContext.getPackageManager().getPackagesForUid(Process.myUid())[0];
    }

    public static void exitScbm(int phoneId) {
        IOplusPhone mInterfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusPhone.DEFAULT);
        mInterfaceImpl.exitScbm();
    }

    public static boolean isInScbm(int phoneId) {
        IOplusPhone mInterfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusPhone.DEFAULT);
        return mInterfaceImpl.isInScbm();
    }

    public static boolean isExitScbmFeatureSupported(int phoneId) {
        IOplusPhone mInterfaceImpl = OplusTelephonyFactory.getFeatureFromCache(phoneId, IOplusPhone.DEFAULT);
        return mInterfaceImpl.isExitScbmFeatureSupported();
    }

    public static boolean isOutgoingImsVoiceAllowed(Phone phone) {
        return phone.isOutgoingImsVoiceAllowed();
    }

    private static void logd(String msg) {
        Rlog.d(TAG, msg);
    }

    private static void loge(String msg) {
        Rlog.e(TAG, msg);
    }
}
