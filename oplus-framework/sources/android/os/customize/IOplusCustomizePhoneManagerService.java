package android.os.customize;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.customize.IOplusPreciseCallStateChangedInnerCallback;

/* loaded from: classes.dex */
public interface IOplusCustomizePhoneManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizePhoneManagerService";

    void addPreciseCallStateChangedCallback(IOplusPreciseCallStateChangedInnerCallback iOplusPreciseCallStateChangedInnerCallback) throws RemoteException;

    void answerRingingCall() throws RemoteException;

    void endCall(ComponentName componentName) throws RemoteException;

    int getDefaultVoiceCard(ComponentName componentName) throws RemoteException;

    boolean getPropSetNonEmergencyCallDisabled() throws RemoteException;

    int getSlot1SmsLimitation(ComponentName componentName, boolean z) throws RemoteException;

    String getSlot1SmsReceiveDisabled() throws RemoteException;

    String getSlot1SmsSendDisabled() throws RemoteException;

    int getSlot2SmsLimitation(ComponentName componentName, boolean z) throws RemoteException;

    String getSlot2SmsReceiveDisabled() throws RemoteException;

    String getSlot2SmsSendDisabled() throws RemoteException;

    boolean isCallForwardSettingDisabled() throws RemoteException;

    boolean isEnablePhoneCallLimit(boolean z) throws RemoteException;

    boolean isInComingThirdCallDisabled(ComponentName componentName) throws RemoteException;

    boolean isRoamingCallDisabled(ComponentName componentName) throws RemoteException;

    boolean isSlotDisabled(ComponentName componentName, int i) throws RemoteException;

    boolean isSlotTwoDisabled() throws RemoteException;

    boolean propEnablePhoneCallLimit(boolean z, boolean z2) throws RemoteException;

    int propGetPhoneCallLimitation(boolean z) throws RemoteException;

    long propGetSms1LimitationTime(boolean z) throws RemoteException;

    long propGetSms2LimitationTime(boolean z) throws RemoteException;

    boolean propRemoveCallLimitation(boolean z) throws RemoteException;

    boolean propSetCallForwardSettingDisabled(boolean z) throws RemoteException;

    boolean propSetCallLimitTime(boolean z, int i) throws RemoteException;

    boolean propSetNonEmergencyCallDisabled(boolean z) throws RemoteException;

    boolean propSetPhoneCallLimitation(boolean z, int i) throws RemoteException;

    void removePreciseCallStateChangedCallback(IOplusPreciseCallStateChangedInnerCallback iOplusPreciseCallStateChangedInnerCallback) throws RemoteException;

    void removeSlot1SmsLimitation(ComponentName componentName, boolean z) throws RemoteException;

    void removeSlot2SmsLimitation(ComponentName componentName, boolean z) throws RemoteException;

    void removeSmsLimitation(ComponentName componentName) throws RemoteException;

    Bundle setDefaultVoiceCard(ComponentName componentName, int i) throws RemoteException;

    boolean setIncomingThirdCallDisabled(ComponentName componentName, boolean z) throws RemoteException;

    boolean setRoamingCallDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setSlot1SmsLimitation(ComponentName componentName, int i) throws RemoteException;

    void setSlot1SmsReceiveDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setSlot1SmsSendDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setSlot2SmsLimitation(ComponentName componentName, int i) throws RemoteException;

    void setSlot2SmsReceiveDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setSlot2SmsSendDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setSlotDisabled(ComponentName componentName, int i, boolean z) throws RemoteException;

    boolean setSlotOneSmsLimitation(ComponentName componentName, boolean z, int i, int i2) throws RemoteException;

    void setSlotTwoDisabled(boolean z) throws RemoteException;

    boolean setSlotTwoSmsLimitation(ComponentName componentName, boolean z, int i, int i2) throws RemoteException;

    boolean setVoiceIncomingDisabledforSlot1(ComponentName componentName, boolean z) throws RemoteException;

    boolean setVoiceIncomingDisabledforSlot2(ComponentName componentName, boolean z) throws RemoteException;

    boolean setVoiceOutgoingDisabledforSlot1(ComponentName componentName, boolean z) throws RemoteException;

    boolean setVoiceOutgoingDisabledforSlot2(ComponentName componentName, boolean z) throws RemoteException;

    int showSlot1SmsTimes(boolean z) throws RemoteException;

    int showSlot2SmsTimes(boolean z) throws RemoteException;

    void storeSlot1SmsTimes(String str, boolean z) throws RemoteException;

    void storeSlot2SmsTimes(String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizePhoneManagerService {
        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean propSetNonEmergencyCallDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean getPropSetNonEmergencyCallDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean propSetCallForwardSettingDisabled(boolean disable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean isCallForwardSettingDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean propEnablePhoneCallLimit(boolean isEnable, boolean isOutgoing) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean isEnablePhoneCallLimit(boolean isOutgoing) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean propSetPhoneCallLimitation(boolean isOutgoing, int limitNumber) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public int propGetPhoneCallLimitation(boolean isOutgoing) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean propRemoveCallLimitation(boolean isOutgoing) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean propSetCallLimitTime(boolean isOutgoing, int dateType) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void setSlot1SmsSendDisabled(ComponentName admin, boolean openswitch) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void setSlot1SmsReceiveDisabled(ComponentName admin, boolean openswitch) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void setSlot2SmsSendDisabled(ComponentName admin, boolean openswitch) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void setSlot2SmsReceiveDisabled(ComponentName admin, boolean openswitch) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void setSlot1SmsLimitation(ComponentName admin, int limitNumber) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void setSlot2SmsLimitation(ComponentName admin, int limitNumber) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void removeSmsLimitation(ComponentName admin) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public int getSlot1SmsLimitation(ComponentName admin, boolean isOutgoing) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public int getSlot2SmsLimitation(ComponentName admin, boolean isOutgoing) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void removeSlot1SmsLimitation(ComponentName admin, boolean isOutgoing) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void removeSlot2SmsLimitation(ComponentName admin, boolean isOutgoing) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public String getSlot1SmsReceiveDisabled() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public String getSlot2SmsReceiveDisabled() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public String getSlot1SmsSendDisabled() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public String getSlot2SmsSendDisabled() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public int showSlot1SmsTimes(boolean isOutgoing) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void storeSlot1SmsTimes(String times, boolean isOutgoing) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public int showSlot2SmsTimes(boolean isOutgoing) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void storeSlot2SmsTimes(String times, boolean isOutgoing) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void endCall(ComponentName admin) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean setVoiceIncomingDisabledforSlot1(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean setVoiceOutgoingDisabledforSlot1(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean setVoiceIncomingDisabledforSlot2(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean setVoiceOutgoingDisabledforSlot2(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean setRoamingCallDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean isRoamingCallDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean setIncomingThirdCallDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean isInComingThirdCallDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public Bundle setDefaultVoiceCard(ComponentName componentName, int slotId) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public int getDefaultVoiceCard(ComponentName componentName) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void setSlotTwoDisabled(boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean isSlotTwoDisabled() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void setSlotDisabled(ComponentName admin, int slotId, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean isSlotDisabled(ComponentName admin, int slotId) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void answerRingingCall() throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public long propGetSms2LimitationTime(boolean isOutgoing) throws RemoteException {
            return 0L;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public long propGetSms1LimitationTime(boolean isOutgoing) throws RemoteException {
            return 0L;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean setSlotOneSmsLimitation(ComponentName componentName, boolean isOutgoing, int dateType, int limitNumber) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public boolean setSlotTwoSmsLimitation(ComponentName componentName, boolean isOutgoing, int dateType, int limitNumber) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void addPreciseCallStateChangedCallback(IOplusPreciseCallStateChangedInnerCallback callback) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizePhoneManagerService
        public void removePreciseCallStateChangedCallback(IOplusPreciseCallStateChangedInnerCallback callback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizePhoneManagerService {
        static final int TRANSACTION_addPreciseCallStateChangedCallback = 50;
        static final int TRANSACTION_answerRingingCall = 45;
        static final int TRANSACTION_endCall = 30;
        static final int TRANSACTION_getDefaultVoiceCard = 40;
        static final int TRANSACTION_getPropSetNonEmergencyCallDisabled = 2;
        static final int TRANSACTION_getSlot1SmsLimitation = 18;
        static final int TRANSACTION_getSlot1SmsReceiveDisabled = 22;
        static final int TRANSACTION_getSlot1SmsSendDisabled = 24;
        static final int TRANSACTION_getSlot2SmsLimitation = 19;
        static final int TRANSACTION_getSlot2SmsReceiveDisabled = 23;
        static final int TRANSACTION_getSlot2SmsSendDisabled = 25;
        static final int TRANSACTION_isCallForwardSettingDisabled = 4;
        static final int TRANSACTION_isEnablePhoneCallLimit = 6;
        static final int TRANSACTION_isInComingThirdCallDisabled = 38;
        static final int TRANSACTION_isRoamingCallDisabled = 36;
        static final int TRANSACTION_isSlotDisabled = 44;
        static final int TRANSACTION_isSlotTwoDisabled = 42;
        static final int TRANSACTION_propEnablePhoneCallLimit = 5;
        static final int TRANSACTION_propGetPhoneCallLimitation = 8;
        static final int TRANSACTION_propGetSms1LimitationTime = 47;
        static final int TRANSACTION_propGetSms2LimitationTime = 46;
        static final int TRANSACTION_propRemoveCallLimitation = 9;
        static final int TRANSACTION_propSetCallForwardSettingDisabled = 3;
        static final int TRANSACTION_propSetCallLimitTime = 10;
        static final int TRANSACTION_propSetNonEmergencyCallDisabled = 1;
        static final int TRANSACTION_propSetPhoneCallLimitation = 7;
        static final int TRANSACTION_removePreciseCallStateChangedCallback = 51;
        static final int TRANSACTION_removeSlot1SmsLimitation = 20;
        static final int TRANSACTION_removeSlot2SmsLimitation = 21;
        static final int TRANSACTION_removeSmsLimitation = 17;
        static final int TRANSACTION_setDefaultVoiceCard = 39;
        static final int TRANSACTION_setIncomingThirdCallDisabled = 37;
        static final int TRANSACTION_setRoamingCallDisabled = 35;
        static final int TRANSACTION_setSlot1SmsLimitation = 15;
        static final int TRANSACTION_setSlot1SmsReceiveDisabled = 12;
        static final int TRANSACTION_setSlot1SmsSendDisabled = 11;
        static final int TRANSACTION_setSlot2SmsLimitation = 16;
        static final int TRANSACTION_setSlot2SmsReceiveDisabled = 14;
        static final int TRANSACTION_setSlot2SmsSendDisabled = 13;
        static final int TRANSACTION_setSlotDisabled = 43;
        static final int TRANSACTION_setSlotOneSmsLimitation = 48;
        static final int TRANSACTION_setSlotTwoDisabled = 41;
        static final int TRANSACTION_setSlotTwoSmsLimitation = 49;
        static final int TRANSACTION_setVoiceIncomingDisabledforSlot1 = 31;
        static final int TRANSACTION_setVoiceIncomingDisabledforSlot2 = 33;
        static final int TRANSACTION_setVoiceOutgoingDisabledforSlot1 = 32;
        static final int TRANSACTION_setVoiceOutgoingDisabledforSlot2 = 34;
        static final int TRANSACTION_showSlot1SmsTimes = 26;
        static final int TRANSACTION_showSlot2SmsTimes = 28;
        static final int TRANSACTION_storeSlot1SmsTimes = 27;
        static final int TRANSACTION_storeSlot2SmsTimes = 29;

        public Stub() {
            attachInterface(this, IOplusCustomizePhoneManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizePhoneManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizePhoneManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizePhoneManagerService)) {
                return (IOplusCustomizePhoneManagerService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "propSetNonEmergencyCallDisabled";
                case 2:
                    return "getPropSetNonEmergencyCallDisabled";
                case 3:
                    return "propSetCallForwardSettingDisabled";
                case 4:
                    return "isCallForwardSettingDisabled";
                case 5:
                    return "propEnablePhoneCallLimit";
                case 6:
                    return "isEnablePhoneCallLimit";
                case 7:
                    return "propSetPhoneCallLimitation";
                case 8:
                    return "propGetPhoneCallLimitation";
                case 9:
                    return "propRemoveCallLimitation";
                case 10:
                    return "propSetCallLimitTime";
                case 11:
                    return "setSlot1SmsSendDisabled";
                case 12:
                    return "setSlot1SmsReceiveDisabled";
                case 13:
                    return "setSlot2SmsSendDisabled";
                case 14:
                    return "setSlot2SmsReceiveDisabled";
                case 15:
                    return "setSlot1SmsLimitation";
                case 16:
                    return "setSlot2SmsLimitation";
                case 17:
                    return "removeSmsLimitation";
                case 18:
                    return "getSlot1SmsLimitation";
                case 19:
                    return "getSlot2SmsLimitation";
                case 20:
                    return "removeSlot1SmsLimitation";
                case 21:
                    return "removeSlot2SmsLimitation";
                case 22:
                    return "getSlot1SmsReceiveDisabled";
                case 23:
                    return "getSlot2SmsReceiveDisabled";
                case 24:
                    return "getSlot1SmsSendDisabled";
                case 25:
                    return "getSlot2SmsSendDisabled";
                case 26:
                    return "showSlot1SmsTimes";
                case 27:
                    return "storeSlot1SmsTimes";
                case 28:
                    return "showSlot2SmsTimes";
                case 29:
                    return "storeSlot2SmsTimes";
                case 30:
                    return "endCall";
                case 31:
                    return "setVoiceIncomingDisabledforSlot1";
                case 32:
                    return "setVoiceOutgoingDisabledforSlot1";
                case 33:
                    return "setVoiceIncomingDisabledforSlot2";
                case 34:
                    return "setVoiceOutgoingDisabledforSlot2";
                case 35:
                    return "setRoamingCallDisabled";
                case 36:
                    return "isRoamingCallDisabled";
                case 37:
                    return "setIncomingThirdCallDisabled";
                case 38:
                    return "isInComingThirdCallDisabled";
                case 39:
                    return "setDefaultVoiceCard";
                case 40:
                    return "getDefaultVoiceCard";
                case 41:
                    return "setSlotTwoDisabled";
                case 42:
                    return "isSlotTwoDisabled";
                case 43:
                    return "setSlotDisabled";
                case 44:
                    return "isSlotDisabled";
                case 45:
                    return "answerRingingCall";
                case 46:
                    return "propGetSms2LimitationTime";
                case 47:
                    return "propGetSms1LimitationTime";
                case 48:
                    return "setSlotOneSmsLimitation";
                case 49:
                    return "setSlotTwoSmsLimitation";
                case 50:
                    return "addPreciseCallStateChangedCallback";
                case 51:
                    return "removePreciseCallStateChangedCallback";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusCustomizePhoneManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result = propSetNonEmergencyCallDisabled(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            boolean _result2 = getPropSetNonEmergencyCallDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result3 = propSetCallForwardSettingDisabled(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            boolean _result4 = isCallForwardSettingDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 5:
                            boolean _arg03 = data.readBoolean();
                            boolean _arg1 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result5 = propEnablePhoneCallLimit(_arg03, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            boolean _arg04 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result6 = isEnablePhoneCallLimit(_arg04);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 7:
                            boolean _arg05 = data.readBoolean();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result7 = propSetPhoneCallLimitation(_arg05, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 8:
                            boolean _arg06 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result8 = propGetPhoneCallLimitation(_arg06);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 9:
                            boolean _arg07 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result9 = propRemoveCallLimitation(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 10:
                            boolean _arg08 = data.readBoolean();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result10 = propSetCallLimitTime(_arg08, _arg13);
                            reply.writeNoException();
                            reply.writeBoolean(_result10);
                            return true;
                        case 11:
                            ComponentName _arg09 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg14 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSlot1SmsSendDisabled(_arg09, _arg14);
                            reply.writeNoException();
                            return true;
                        case 12:
                            ComponentName _arg010 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg15 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSlot1SmsReceiveDisabled(_arg010, _arg15);
                            reply.writeNoException();
                            return true;
                        case 13:
                            ComponentName _arg011 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg16 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSlot2SmsSendDisabled(_arg011, _arg16);
                            reply.writeNoException();
                            return true;
                        case 14:
                            ComponentName _arg012 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg17 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSlot2SmsReceiveDisabled(_arg012, _arg17);
                            reply.writeNoException();
                            return true;
                        case 15:
                            ComponentName _arg013 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg18 = data.readInt();
                            data.enforceNoDataAvail();
                            setSlot1SmsLimitation(_arg013, _arg18);
                            reply.writeNoException();
                            return true;
                        case 16:
                            ComponentName _arg014 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg19 = data.readInt();
                            data.enforceNoDataAvail();
                            setSlot2SmsLimitation(_arg014, _arg19);
                            reply.writeNoException();
                            return true;
                        case 17:
                            ComponentName _arg015 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            removeSmsLimitation(_arg015);
                            reply.writeNoException();
                            return true;
                        case 18:
                            ComponentName _arg016 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg110 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result11 = getSlot1SmsLimitation(_arg016, _arg110);
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 19:
                            ComponentName _arg017 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg111 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result12 = getSlot2SmsLimitation(_arg017, _arg111);
                            reply.writeNoException();
                            reply.writeInt(_result12);
                            return true;
                        case 20:
                            ComponentName _arg018 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg112 = data.readBoolean();
                            data.enforceNoDataAvail();
                            removeSlot1SmsLimitation(_arg018, _arg112);
                            reply.writeNoException();
                            return true;
                        case 21:
                            ComponentName _arg019 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg113 = data.readBoolean();
                            data.enforceNoDataAvail();
                            removeSlot2SmsLimitation(_arg019, _arg113);
                            reply.writeNoException();
                            return true;
                        case 22:
                            String _result13 = getSlot1SmsReceiveDisabled();
                            reply.writeNoException();
                            reply.writeString(_result13);
                            return true;
                        case 23:
                            String _result14 = getSlot2SmsReceiveDisabled();
                            reply.writeNoException();
                            reply.writeString(_result14);
                            return true;
                        case 24:
                            String _result15 = getSlot1SmsSendDisabled();
                            reply.writeNoException();
                            reply.writeString(_result15);
                            return true;
                        case 25:
                            String _result16 = getSlot2SmsSendDisabled();
                            reply.writeNoException();
                            reply.writeString(_result16);
                            return true;
                        case 26:
                            boolean _arg020 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result17 = showSlot1SmsTimes(_arg020);
                            reply.writeNoException();
                            reply.writeInt(_result17);
                            return true;
                        case 27:
                            String _arg021 = data.readString();
                            boolean _arg114 = data.readBoolean();
                            data.enforceNoDataAvail();
                            storeSlot1SmsTimes(_arg021, _arg114);
                            reply.writeNoException();
                            return true;
                        case 28:
                            boolean _arg022 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result18 = showSlot2SmsTimes(_arg022);
                            reply.writeNoException();
                            reply.writeInt(_result18);
                            return true;
                        case 29:
                            String _arg023 = data.readString();
                            boolean _arg115 = data.readBoolean();
                            data.enforceNoDataAvail();
                            storeSlot2SmsTimes(_arg023, _arg115);
                            reply.writeNoException();
                            return true;
                        case 30:
                            ComponentName _arg024 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            endCall(_arg024);
                            reply.writeNoException();
                            return true;
                        case 31:
                            ComponentName _arg025 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg116 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result19 = setVoiceIncomingDisabledforSlot1(_arg025, _arg116);
                            reply.writeNoException();
                            reply.writeBoolean(_result19);
                            return true;
                        case 32:
                            ComponentName _arg026 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg117 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result20 = setVoiceOutgoingDisabledforSlot1(_arg026, _arg117);
                            reply.writeNoException();
                            reply.writeBoolean(_result20);
                            return true;
                        case 33:
                            ComponentName _arg027 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg118 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result21 = setVoiceIncomingDisabledforSlot2(_arg027, _arg118);
                            reply.writeNoException();
                            reply.writeBoolean(_result21);
                            return true;
                        case 34:
                            ComponentName _arg028 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg119 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result22 = setVoiceOutgoingDisabledforSlot2(_arg028, _arg119);
                            reply.writeNoException();
                            reply.writeBoolean(_result22);
                            return true;
                        case 35:
                            ComponentName _arg029 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg120 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result23 = setRoamingCallDisabled(_arg029, _arg120);
                            reply.writeNoException();
                            reply.writeBoolean(_result23);
                            return true;
                        case 36:
                            ComponentName _arg030 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result24 = isRoamingCallDisabled(_arg030);
                            reply.writeNoException();
                            reply.writeBoolean(_result24);
                            return true;
                        case 37:
                            ComponentName _arg031 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg121 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result25 = setIncomingThirdCallDisabled(_arg031, _arg121);
                            reply.writeNoException();
                            reply.writeBoolean(_result25);
                            return true;
                        case 38:
                            ComponentName _arg032 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result26 = isInComingThirdCallDisabled(_arg032);
                            reply.writeNoException();
                            reply.writeBoolean(_result26);
                            return true;
                        case 39:
                            ComponentName _arg033 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg122 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result27 = setDefaultVoiceCard(_arg033, _arg122);
                            reply.writeNoException();
                            reply.writeTypedObject(_result27, 1);
                            return true;
                        case 40:
                            ComponentName _arg034 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result28 = getDefaultVoiceCard(_arg034);
                            reply.writeNoException();
                            reply.writeInt(_result28);
                            return true;
                        case 41:
                            boolean _arg035 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSlotTwoDisabled(_arg035);
                            reply.writeNoException();
                            return true;
                        case 42:
                            boolean _result29 = isSlotTwoDisabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result29);
                            return true;
                        case 43:
                            ComponentName _arg036 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg123 = data.readInt();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setSlotDisabled(_arg036, _arg123, _arg2);
                            reply.writeNoException();
                            return true;
                        case 44:
                            ComponentName _arg037 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg124 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result30 = isSlotDisabled(_arg037, _arg124);
                            reply.writeNoException();
                            reply.writeBoolean(_result30);
                            return true;
                        case 45:
                            answerRingingCall();
                            reply.writeNoException();
                            return true;
                        case 46:
                            boolean _arg038 = data.readBoolean();
                            data.enforceNoDataAvail();
                            long _result31 = propGetSms2LimitationTime(_arg038);
                            reply.writeNoException();
                            reply.writeLong(_result31);
                            return true;
                        case 47:
                            boolean _arg039 = data.readBoolean();
                            data.enforceNoDataAvail();
                            long _result32 = propGetSms1LimitationTime(_arg039);
                            reply.writeNoException();
                            reply.writeLong(_result32);
                            return true;
                        case 48:
                            ComponentName _arg040 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg125 = data.readBoolean();
                            int _arg22 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result33 = setSlotOneSmsLimitation(_arg040, _arg125, _arg22, _arg3);
                            reply.writeNoException();
                            reply.writeBoolean(_result33);
                            return true;
                        case 49:
                            ComponentName _arg041 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg126 = data.readBoolean();
                            int _arg23 = data.readInt();
                            int _arg32 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result34 = setSlotTwoSmsLimitation(_arg041, _arg126, _arg23, _arg32);
                            reply.writeNoException();
                            reply.writeBoolean(_result34);
                            return true;
                        case 50:
                            IOplusPreciseCallStateChangedInnerCallback _arg042 = IOplusPreciseCallStateChangedInnerCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            addPreciseCallStateChangedCallback(_arg042);
                            reply.writeNoException();
                            return true;
                        case 51:
                            IOplusPreciseCallStateChangedInnerCallback _arg043 = IOplusPreciseCallStateChangedInnerCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            removePreciseCallStateChangedCallback(_arg043);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizePhoneManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizePhoneManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean propSetNonEmergencyCallDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean getPropSetNonEmergencyCallDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean propSetCallForwardSettingDisabled(boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean isCallForwardSettingDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean propEnablePhoneCallLimit(boolean isEnable, boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isEnable);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean isEnablePhoneCallLimit(boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean propSetPhoneCallLimitation(boolean isOutgoing, int limitNumber) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    _data.writeInt(limitNumber);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public int propGetPhoneCallLimitation(boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean propRemoveCallLimitation(boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean propSetCallLimitTime(boolean isOutgoing, int dateType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    _data.writeInt(dateType);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void setSlot1SmsSendDisabled(ComponentName admin, boolean openswitch) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(openswitch);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void setSlot1SmsReceiveDisabled(ComponentName admin, boolean openswitch) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(openswitch);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void setSlot2SmsSendDisabled(ComponentName admin, boolean openswitch) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(openswitch);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void setSlot2SmsReceiveDisabled(ComponentName admin, boolean openswitch) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(openswitch);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void setSlot1SmsLimitation(ComponentName admin, int limitNumber) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(limitNumber);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void setSlot2SmsLimitation(ComponentName admin, int limitNumber) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(limitNumber);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void removeSmsLimitation(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public int getSlot1SmsLimitation(ComponentName admin, boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public int getSlot2SmsLimitation(ComponentName admin, boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void removeSlot1SmsLimitation(ComponentName admin, boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void removeSlot2SmsLimitation(ComponentName admin, boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public String getSlot1SmsReceiveDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public String getSlot2SmsReceiveDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public String getSlot1SmsSendDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public String getSlot2SmsSendDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public int showSlot1SmsTimes(boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void storeSlot1SmsTimes(String times, boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeString(times);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public int showSlot2SmsTimes(boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void storeSlot2SmsTimes(String times, boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeString(times);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void endCall(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean setVoiceIncomingDisabledforSlot1(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean setVoiceOutgoingDisabledforSlot1(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean setVoiceIncomingDisabledforSlot2(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean setVoiceOutgoingDisabledforSlot2(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean setRoamingCallDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean isRoamingCallDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean setIncomingThirdCallDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean isInComingThirdCallDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public Bundle setDefaultVoiceCard(ComponentName componentName, int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeInt(slotId);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public int getDefaultVoiceCard(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void setSlotTwoDisabled(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean isSlotTwoDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void setSlotDisabled(ComponentName admin, int slotId, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(slotId);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean isSlotDisabled(ComponentName admin, int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeInt(slotId);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void answerRingingCall() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public long propGetSms2LimitationTime(boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public long propGetSms1LimitationTime(boolean isOutgoing) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeBoolean(isOutgoing);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean setSlotOneSmsLimitation(ComponentName componentName, boolean isOutgoing, int dateType, int limitNumber) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeBoolean(isOutgoing);
                    _data.writeInt(dateType);
                    _data.writeInt(limitNumber);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public boolean setSlotTwoSmsLimitation(ComponentName componentName, boolean isOutgoing, int dateType, int limitNumber) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeBoolean(isOutgoing);
                    _data.writeInt(dateType);
                    _data.writeInt(limitNumber);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void addPreciseCallStateChangedCallback(IOplusPreciseCallStateChangedInnerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizePhoneManagerService
            public void removePreciseCallStateChangedCallback(IOplusPreciseCallStateChangedInnerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizePhoneManagerService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 50;
        }
    }
}
