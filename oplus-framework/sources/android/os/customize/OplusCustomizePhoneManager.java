package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizePhoneManagerService;
import android.util.Slog;

/* loaded from: classes.dex */
public class OplusCustomizePhoneManager {
    private static final String SERVICE_NAME = "OplusCustomizePhoneManagerService";
    private static final String TAG = "OplusCustomizePhoneManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizePhoneManager sInstance;
    private IOplusCustomizePhoneManagerService mOplusCustomizePhoneManagerService;

    private OplusCustomizePhoneManager() {
        getOplusCustomizePhoneManagerService();
    }

    public static final OplusCustomizePhoneManager getInstance(Context context) {
        OplusCustomizePhoneManager oplusCustomizePhoneManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizePhoneManager();
                }
                oplusCustomizePhoneManager = sInstance;
            }
            return oplusCustomizePhoneManager;
        }
        return sInstance;
    }

    private IOplusCustomizePhoneManagerService getOplusCustomizePhoneManagerService() {
        IOplusCustomizePhoneManagerService iOplusCustomizePhoneManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizePhoneManagerService == null) {
                this.mOplusCustomizePhoneManagerService = IOplusCustomizePhoneManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizePhoneManagerService = this.mOplusCustomizePhoneManagerService;
        }
        return iOplusCustomizePhoneManagerService;
    }

    public boolean propSetNonEmergencyCallDisabled(boolean disable) {
        try {
            return getOplusCustomizePhoneManagerService().propSetNonEmergencyCallDisabled(disable);
        } catch (RemoteException e) {
            Slog.e(TAG, "propSetNonEmergencyCallDisabled Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "propSetNonEmergencyCallDisabled Error" + e2);
            return false;
        }
    }

    public boolean getPropSetNonEmergencyCallDisabled() {
        try {
            return getOplusCustomizePhoneManagerService().getPropSetNonEmergencyCallDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "getPropSetNonEmergencyCallDisabled Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "getPropSetNonEmergencyCallDisabled Error" + e2);
            return false;
        }
    }

    public boolean propSetCallForwardSettingDisabled(boolean disable) {
        try {
            return getOplusCustomizePhoneManagerService().propSetCallForwardSettingDisabled(disable);
        } catch (RemoteException e) {
            Slog.e(TAG, "propSetForwardCallSettingDisabled Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "propSetForwardCallSettingDisabled Error" + e2);
            return false;
        }
    }

    public boolean isCallForwardSettingDisabled() {
        try {
            return getOplusCustomizePhoneManagerService().isCallForwardSettingDisabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isCallForwardSettingDisabled Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isCallForwardSettingDisabled Error" + e2);
            return false;
        }
    }

    public boolean propEnablePhoneCallLimit(boolean enable, boolean isOutgoing) {
        try {
            return getOplusCustomizePhoneManagerService().propEnablePhoneCallLimit(enable, isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "propEnablePhoneCallLimit Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "propEnablePhoneCallLimit Error" + e2);
            return false;
        }
    }

    public boolean isEnablePhoneCallLimit(boolean isOutgoing) {
        try {
            return getOplusCustomizePhoneManagerService().isEnablePhoneCallLimit(isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "isEnablePhoneCallLimit Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isEnablePhoneCallLimit Error" + e2);
            return false;
        }
    }

    public boolean propSetPhoneCallLimitation(boolean isOutgoing, int limitation) {
        try {
            return getOplusCustomizePhoneManagerService().propSetPhoneCallLimitation(isOutgoing, limitation);
        } catch (RemoteException e) {
            Slog.e(TAG, "propSetPhoneCallLimitation Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "propSetPhoneCallLimitation Error" + e2);
            return false;
        }
    }

    public int propGetPhoneCallLimitation(boolean isOutgoing) {
        try {
            return getOplusCustomizePhoneManagerService().propGetPhoneCallLimitation(isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "propGetPhoneCallLimitation Error");
            return 0;
        } catch (Exception e2) {
            Slog.e(TAG, "propGetPhoneCallLimitation Error" + e2);
            return 0;
        }
    }

    public boolean propRemoveCallLimitation(boolean isOutgoing) {
        try {
            return getOplusCustomizePhoneManagerService().propRemoveCallLimitation(isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "propRemoveCallLimitation Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "propRemoveCallLimitation Error" + e2);
            return false;
        }
    }

    public boolean propSetCallLimitTime(boolean isOutgoing, int dateType) {
        try {
            return getOplusCustomizePhoneManagerService().propSetCallLimitTime(isOutgoing, dateType);
        } catch (RemoteException e) {
            Slog.e(TAG, "propSetCallLimitTime Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "propSetCallLimitTime Error" + e2);
            return false;
        }
    }

    public boolean setSlot1SmsLimitation(ComponentName admin, boolean isOutgoing, int dateType, int limitNumber) {
        try {
            return getOplusCustomizePhoneManagerService().setSlotOneSmsLimitation(admin, isOutgoing, dateType, limitNumber);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSlot1SmsLimitation Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot1SmsLimitation Error" + e2);
            return false;
        }
    }

    public boolean setSlot2SmsLimitation(ComponentName admin, boolean isOutgoing, int dateType, int limitNumber) {
        try {
            return getOplusCustomizePhoneManagerService().setSlotTwoSmsLimitation(admin, isOutgoing, dateType, limitNumber);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSlot2SmsLimitation Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot2SmsLimitation Error" + e2);
            return false;
        }
    }

    public long propGetSms1LimitationTime(boolean isOutgoing) {
        try {
            return getOplusCustomizePhoneManagerService().propGetSms1LimitationTime(isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "propGetSms1LimitationTime Error");
            return 0L;
        } catch (Exception e2) {
            Slog.e(TAG, "propGetSms1LimitationTime Error" + e2);
            return 0L;
        }
    }

    public long propGetSms2LimitationTime(boolean isOutgoing) {
        try {
            return getOplusCustomizePhoneManagerService().propGetSms2LimitationTime(isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "propGetSms2LimitationTime Error");
            return 0L;
        } catch (Exception e2) {
            Slog.e(TAG, "propGetSms2LimitationTime Error" + e2);
            return 0L;
        }
    }

    public void setSlot1SmsSendDisabled(ComponentName admin, boolean openswitch) {
        try {
            getOplusCustomizePhoneManagerService().setSlot1SmsSendDisabled(admin, openswitch);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSlot1SmsSendDisabled Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot1SmsSendDisabled Error" + e2);
        }
    }

    public void setSlot1SmsReceiveDisabled(ComponentName admin, boolean openswitch) {
        try {
            getOplusCustomizePhoneManagerService().setSlot1SmsReceiveDisabled(admin, openswitch);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSlot1SmsReceiveDisabled Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot1SmsReceiveDisabled Error" + e2);
        }
    }

    public void setSlot2SmsSendDisabled(ComponentName admin, boolean openswitch) {
        try {
            getOplusCustomizePhoneManagerService().setSlot2SmsSendDisabled(admin, openswitch);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSlot2SmsSendDisabled Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot2SmsSendDisabled Error" + e2);
        }
    }

    public void setSlot2SmsReceiveDisabled(ComponentName admin, boolean openswitch) {
        try {
            getOplusCustomizePhoneManagerService().setSlot2SmsReceiveDisabled(admin, openswitch);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSlot2SmsReceiveDisabled Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot2SmsReceiveDisabled Error" + e2);
        }
    }

    public void setSlot1SmsLimitation(ComponentName admin, int limitNumber) {
        try {
            getOplusCustomizePhoneManagerService().setSlot1SmsLimitation(admin, limitNumber);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSlot1SmsLimitation Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot1SmsLimitation Error" + e2);
        }
    }

    public void setSlot2SmsLimitation(ComponentName admin, int limitNumber) {
        try {
            getOplusCustomizePhoneManagerService().setSlot2SmsLimitation(admin, limitNumber);
        } catch (RemoteException e) {
            Slog.e(TAG, "setSlot2SmsLimitation Error");
        } catch (Exception e2) {
            Slog.e(TAG, "setSlot2SmsLimitation Error" + e2);
        }
    }

    public void removeSmsLimitation(ComponentName admin) {
        try {
            getOplusCustomizePhoneManagerService().removeSmsLimitation(admin);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeSmsLimitation Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeSmsLimitation Error" + e2);
        }
    }

    public int getSlot1SmsLimitation(ComponentName admin, boolean isOutgoing) {
        try {
            int result = getOplusCustomizePhoneManagerService().getSlot1SmsLimitation(admin, isOutgoing);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getSlot1SmsLimitation Error");
            return -1;
        } catch (Exception e2) {
            Slog.e(TAG, "getSlot1SmsLimitation Error" + e2);
            return -1;
        }
    }

    public int getSlot2SmsLimitation(ComponentName admin, boolean isOutgoing) {
        try {
            int result = getOplusCustomizePhoneManagerService().getSlot2SmsLimitation(admin, isOutgoing);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getSlot2SmsLimitation Error");
            return -1;
        } catch (Exception e2) {
            Slog.e(TAG, "getSlot2SmsLimitation Error" + e2);
            return -1;
        }
    }

    public void removeSlot1SmsLimitation(ComponentName admin, boolean isOutgoing) {
        try {
            getOplusCustomizePhoneManagerService().removeSlot1SmsLimitation(admin, isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeSlot1SmsLimitation Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeSlot1SmsLimitation Error" + e2);
        }
    }

    public void removeSlot2SmsLimitation(ComponentName admin, boolean isOutgoing) {
        try {
            getOplusCustomizePhoneManagerService().removeSlot2SmsLimitation(admin, isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "removeSlot2SmsLimitation Error");
        } catch (Exception e2) {
            Slog.e(TAG, "removeSlot2SmsLimitation Error" + e2);
        }
    }

    public String getSlot1SmsReceiveDisabled() {
        try {
            IOplusCustomizePhoneManagerService iOplusCustomizePhoneManagerService = this.mOplusCustomizePhoneManagerService;
            if (iOplusCustomizePhoneManagerService == null) {
                return "1";
            }
            String result = iOplusCustomizePhoneManagerService.getSlot1SmsReceiveDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getSlot1SmsReceiveDisabled error");
            return "1";
        }
    }

    public String getSlot2SmsReceiveDisabled() {
        try {
            IOplusCustomizePhoneManagerService iOplusCustomizePhoneManagerService = this.mOplusCustomizePhoneManagerService;
            if (iOplusCustomizePhoneManagerService == null) {
                return "1";
            }
            String result = iOplusCustomizePhoneManagerService.getSlot2SmsReceiveDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getSlot2SmsReceiveDisabled error");
            return "1";
        }
    }

    public String getSlot1SmsSendDisabled() {
        try {
            IOplusCustomizePhoneManagerService iOplusCustomizePhoneManagerService = this.mOplusCustomizePhoneManagerService;
            if (iOplusCustomizePhoneManagerService == null) {
                return "1";
            }
            String result = iOplusCustomizePhoneManagerService.getSlot1SmsSendDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getSlot1SmsSendDisabled error");
            return "1";
        }
    }

    public String getSlot2SmsSendDisabled() {
        try {
            IOplusCustomizePhoneManagerService iOplusCustomizePhoneManagerService = this.mOplusCustomizePhoneManagerService;
            if (iOplusCustomizePhoneManagerService == null) {
                return "1";
            }
            String result = iOplusCustomizePhoneManagerService.getSlot2SmsSendDisabled();
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getSlot2SmsSendDisabled error");
            return "1";
        }
    }

    public int showSlot1SmsTimes(boolean isOutgoing) {
        try {
            IOplusCustomizePhoneManagerService iOplusCustomizePhoneManagerService = this.mOplusCustomizePhoneManagerService;
            if (iOplusCustomizePhoneManagerService == null) {
                return -1;
            }
            int result = iOplusCustomizePhoneManagerService.showSlot1SmsTimes(isOutgoing);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "showSlot1SmsTimes error");
            return -1;
        }
    }

    public void storeSlot1SmsTimes(String times, boolean isOutgoing) {
        try {
            getOplusCustomizePhoneManagerService().storeSlot1SmsTimes(times, isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "getSlot2SmsSendDisabled Error");
        } catch (Exception e2) {
            Slog.e(TAG, "getSlot2SmsSendDisabled Error" + e2);
        }
    }

    public int showSlot2SmsTimes(boolean isOutgoing) {
        try {
            IOplusCustomizePhoneManagerService iOplusCustomizePhoneManagerService = this.mOplusCustomizePhoneManagerService;
            if (iOplusCustomizePhoneManagerService == null) {
                return -1;
            }
            int result = iOplusCustomizePhoneManagerService.showSlot2SmsTimes(isOutgoing);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "showSlot2SmsTimes error");
            return -1;
        }
    }

    public void storeSlot2SmsTimes(String times, boolean isOutgoing) {
        try {
            getOplusCustomizePhoneManagerService().storeSlot2SmsTimes(times, isOutgoing);
        } catch (RemoteException e) {
            Slog.e(TAG, "getSlot2SmsSendDisabled Error");
        } catch (Exception e2) {
            Slog.e(TAG, "getSlot2SmsSendDisabled Error" + e2);
        }
    }

    public void endCall(ComponentName admin) {
        try {
            IOplusCustomizePhoneManagerService service = getOplusCustomizePhoneManagerService();
            if (service != null) {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service:" + service);
                service.endCall(admin);
            } else {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service is null");
            }
        } catch (RemoteException e) {
            Slog.i(TAG, "endCall error!");
            e.printStackTrace();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setVoiceIncomingDisabledforSlot1(ComponentName componentName, boolean z) {
        String str = TAG;
        boolean z2 = false;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService, service:" + oplusCustomizePhoneManagerService);
                boolean voiceIncomingDisabledforSlot1 = oplusCustomizePhoneManagerService.setVoiceIncomingDisabledforSlot1(componentName, z);
                z2 = voiceIncomingDisabledforSlot1;
                str = voiceIncomingDisabledforSlot1;
            } else {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService service is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "setVoiceIncomingDisabledforSlot1 error!");
            e.printStackTrace();
        }
        return z2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setVoiceOutgoingDisabledforSlot1(ComponentName componentName, boolean z) {
        String str = TAG;
        boolean z2 = false;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService, service:" + oplusCustomizePhoneManagerService);
                boolean voiceOutgoingDisabledforSlot1 = oplusCustomizePhoneManagerService.setVoiceOutgoingDisabledforSlot1(componentName, z);
                z2 = voiceOutgoingDisabledforSlot1;
                str = voiceOutgoingDisabledforSlot1;
            } else {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService service is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "setVoiceIncomingDisabledforSlot1 error!");
            e.printStackTrace();
        }
        return z2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setVoiceIncomingDisabledforSlot2(ComponentName componentName, boolean z) {
        String str = TAG;
        boolean z2 = false;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService, service:" + oplusCustomizePhoneManagerService);
                boolean voiceIncomingDisabledforSlot2 = oplusCustomizePhoneManagerService.setVoiceIncomingDisabledforSlot2(componentName, z);
                z2 = voiceIncomingDisabledforSlot2;
                str = voiceIncomingDisabledforSlot2;
            } else {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService service is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "setVoiceIncomingDisabledforSlot2 error!");
            e.printStackTrace();
        }
        return z2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setVoiceOutgoingDisabledforSlot2(ComponentName componentName, boolean z) {
        String str = TAG;
        boolean z2 = false;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService, service:" + oplusCustomizePhoneManagerService);
                boolean voiceOutgoingDisabledforSlot2 = oplusCustomizePhoneManagerService.setVoiceOutgoingDisabledforSlot2(componentName, z);
                z2 = voiceOutgoingDisabledforSlot2;
                str = voiceOutgoingDisabledforSlot2;
            } else {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService service is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "setVoiceOutgoingDisabledforSlot2 error!");
            e.printStackTrace();
        }
        return z2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setRoamingCallDisabled(ComponentName componentName, boolean z) {
        String str = TAG;
        boolean z2 = false;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService, service:" + oplusCustomizePhoneManagerService);
                boolean roamingCallDisabled = oplusCustomizePhoneManagerService.setRoamingCallDisabled(componentName, z);
                z2 = roamingCallDisabled;
                str = roamingCallDisabled;
            } else {
                Slog.d(TAG, "IOplusCustomizePhoneManagerService service is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "setRoamingCallDisabled error!");
            e.printStackTrace();
        }
        return z2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isRoamingCallDisabled(ComponentName componentName) {
        String str = TAG;
        boolean z = false;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service:" + oplusCustomizePhoneManagerService);
                boolean isRoamingCallDisabled = oplusCustomizePhoneManagerService.isRoamingCallDisabled(componentName);
                z = isRoamingCallDisabled;
                str = isRoamingCallDisabled;
            } else {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService manager is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "isRoamingCallDisabled error : RemoteException");
        }
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean setIncomingThirdCallDisabled(ComponentName componentName, boolean z) {
        String str = TAG;
        boolean z2 = false;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service:" + oplusCustomizePhoneManagerService);
                boolean incomingThirdCallDisabled = oplusCustomizePhoneManagerService.setIncomingThirdCallDisabled(componentName, z);
                z2 = incomingThirdCallDisabled;
                str = incomingThirdCallDisabled;
            } else {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "setIncomingThirdCallDisabled error!");
            e.printStackTrace();
        }
        return z2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isInComingThirdCallDisabled(ComponentName componentName) {
        String str = TAG;
        boolean z = false;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service:" + oplusCustomizePhoneManagerService);
                boolean isInComingThirdCallDisabled = oplusCustomizePhoneManagerService.isInComingThirdCallDisabled(componentName);
                z = isInComingThirdCallDisabled;
                str = isInComingThirdCallDisabled;
            } else {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "isInComingThirdCallDisabled error!");
            e.printStackTrace();
        } catch (Exception e2) {
            Slog.e(str, "isInComingThirdCallDisabled Error");
            e2.printStackTrace();
        }
        return z;
    }

    public boolean setDefaultVoiceCard(ComponentName componentName, int slotId, Message response) {
        boolean value = false;
        try {
            IOplusCustomizePhoneManagerService service = getOplusCustomizePhoneManagerService();
            if (service != null) {
                Slog.d(TAG, "mdm service IDeviceRestrictionManager service:" + service);
                Bundle result = service.setDefaultVoiceCard(componentName, slotId);
                if (result != null) {
                    response.setData(result);
                    value = result.getBoolean("RESULT");
                    Slog.d(TAG, "mdm setDefaultVoiceCard result: " + result.getBoolean("RESULT") + ", exception : " + result.getString("EXCEPTION"));
                } else {
                    Bundle result2 = new Bundle();
                    result2.putBoolean("RESULT", false);
                    result2.putString("EXCEPTION", "GENERIC_FAILURE");
                    response.setData(result2);
                }
            } else {
                Slog.d(TAG, "mdm service IDeviceRestrictionManager manager is null");
            }
        } catch (RemoteException e) {
            Slog.i(TAG, "setDefaultVoiceCard RemoteException error!");
            Bundle result3 = new Bundle();
            result3.putBoolean("RESULT", false);
            result3.putString("EXCEPTION", "GENERIC_FAILURE");
            response.setData(result3);
        }
        return value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int getDefaultVoiceCard(ComponentName componentName) {
        String str = TAG;
        int i = -1;
        try {
            IOplusCustomizePhoneManagerService oplusCustomizePhoneManagerService = getOplusCustomizePhoneManagerService();
            if (oplusCustomizePhoneManagerService != null) {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service:" + oplusCustomizePhoneManagerService);
                int defaultVoiceCard = oplusCustomizePhoneManagerService.getDefaultVoiceCard(componentName);
                i = defaultVoiceCard;
                str = defaultVoiceCard;
            } else {
                Slog.d(TAG, "mdm service IOplusCustomizePhoneManagerService service is null");
                str = str;
            }
        } catch (RemoteException e) {
            Slog.i(str, "getDefaultVoiceCard RemoteException error!");
        }
        return i;
    }

    public void setSlotTwoDisabled(boolean disabled) {
        try {
            getOplusCustomizePhoneManagerService().setSlotTwoDisabled(disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setSlotTwoDisabled Error" + e);
        }
    }

    public boolean isSlotTwoDisabled() {
        try {
            return getOplusCustomizePhoneManagerService().isSlotTwoDisabled();
        } catch (Exception e) {
            Slog.e(TAG, "isSlotTwoDisabled Error" + e);
            return false;
        }
    }

    public void setSlotDisabled(ComponentName componentName, int slotId, boolean disabled) {
        try {
            getOplusCustomizePhoneManagerService().setSlotDisabled(componentName, slotId, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setSlotDisabled Error" + e);
        }
    }

    public boolean isSlotDisabled(ComponentName componentName, int slotId) {
        try {
            return getOplusCustomizePhoneManagerService().isSlotDisabled(componentName, slotId);
        } catch (Exception e) {
            Slog.e(TAG, "isSlotDisabled Error" + e);
            return false;
        }
    }

    public void answerRingingCall() {
        try {
            getOplusCustomizePhoneManagerService().answerRingingCall();
        } catch (RemoteException e) {
            Slog.e(TAG, "answerRingingCall Error");
        } catch (Exception e2) {
            Slog.e(TAG, "answerRingingCall Error" + e2);
        }
    }

    public void addPreciseCallStateChangedCallback(IOplusPreciseCallStateChangedInnerCallback callback) {
        Slog.i(TAG, "addPreciseCallStateChangedCallback");
        try {
            if (callback != null) {
                getOplusCustomizePhoneManagerService().addPreciseCallStateChangedCallback(callback);
            } else {
                Slog.d(TAG, "mdm addPreciseCallStateChangedCallback callback is null");
            }
        } catch (Throwable e) {
            Slog.e(TAG, "addPreciseCallStateChangedCallback: " + e);
        }
    }

    public void removePreciseCallStateChangedCallback(IOplusPreciseCallStateChangedInnerCallback callback) {
        Slog.i(TAG, "removePreciseCallStateChangedCallback");
        try {
            if (callback != null) {
                getOplusCustomizePhoneManagerService().removePreciseCallStateChangedCallback(callback);
            } else {
                Slog.d(TAG, "mdm removePreciseCallStateChangedCallback callback is null");
            }
        } catch (Throwable e) {
            Slog.e(TAG, "removePreciseCallStateChangedCallback: " + e);
        }
    }
}
