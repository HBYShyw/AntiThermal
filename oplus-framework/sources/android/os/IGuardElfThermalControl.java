package android.os;

/* loaded from: classes.dex */
public interface IGuardElfThermalControl extends IInterface {
    public static final String DESCRIPTOR = "android.os.IGuardElfThermalControl";

    int getBattPPSChgIng() throws RemoteException;

    int getBattPPSChgPower() throws RemoteException;

    float getBeginDecimal() throws RemoteException;

    int getBmsHeatingStatus() throws RemoteException;

    int getChargerTechnology() throws RemoteException;

    int getCustomSelectChgMode() throws RemoteException;

    float getEndDecimal() throws RemoteException;

    int getPsyBatteryHmac() throws RemoteException;

    int getPsyBatteryNotify() throws RemoteException;

    int getPsyBatteryRm() throws RemoteException;

    int getPsyChargeTech() throws RemoteException;

    int getPsyOtgOnline() throws RemoteException;

    String getQuickModeGain() throws RemoteException;

    String getReserveSocDebug() throws RemoteException;

    int getSmartChgMode() throws RemoteException;

    int getUIsohValue() throws RemoteException;

    int getWiredOtgOnline() throws RemoteException;

    int getWirelessAdapterPower() throws RemoteException;

    int getWirelessPenPresent() throws RemoteException;

    String getWirelessTXEnable() throws RemoteException;

    int getWirelessUserSleepMode() throws RemoteException;

    boolean isCameraOn() throws RemoteException;

    int nightstandby(int i) throws RemoteException;

    int setBatteryLogPush(String str) throws RemoteException;

    void setChargeLevel(String str, String str2) throws RemoteException;

    int setChargerCycle(String str) throws RemoteException;

    int setChgOlcConfig(String str) throws RemoteException;

    int setChgRusConfig(String str) throws RemoteException;

    int setCustomSelectChgMode(int i, boolean z) throws RemoteException;

    void setPsyMmiChgEn(String str) throws RemoteException;

    int setPsySlowChgEnable(int i, int i2, int i3) throws RemoteException;

    int setReserveSocDebug(String str) throws RemoteException;

    int setSmartChgMode(String str) throws RemoteException;

    int setSmartCoolDown(int i, int i2, String str) throws RemoteException;

    int setSuperEnduranceCount(String str) throws RemoteException;

    int setSuperEnduranceStatus(String str) throws RemoteException;

    void setWirelessPenSoc(String str) throws RemoteException;

    void setWirelessTXEnable(String str) throws RemoteException;

    void setWirelessUserSleepMode(String str) throws RemoteException;

    int setWlsThirdPartitionInfo(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IGuardElfThermalControl {
        @Override // android.os.IGuardElfThermalControl
        public void setChargeLevel(String data, String name) throws RemoteException {
        }

        @Override // android.os.IGuardElfThermalControl
        public boolean isCameraOn() throws RemoteException {
            return false;
        }

        @Override // android.os.IGuardElfThermalControl
        public float getBeginDecimal() throws RemoteException {
            return 0.0f;
        }

        @Override // android.os.IGuardElfThermalControl
        public float getEndDecimal() throws RemoteException {
            return 0.0f;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getPsyOtgOnline() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getPsyBatteryHmac() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getChargerTechnology() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getWirelessPenPresent() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public void setWirelessPenSoc(String data) throws RemoteException {
        }

        @Override // android.os.IGuardElfThermalControl
        public void setWirelessTXEnable(String data) throws RemoteException {
        }

        @Override // android.os.IGuardElfThermalControl
        public String getWirelessTXEnable() throws RemoteException {
            return null;
        }

        @Override // android.os.IGuardElfThermalControl
        public void setWirelessUserSleepMode(String data) throws RemoteException {
        }

        @Override // android.os.IGuardElfThermalControl
        public int getWirelessUserSleepMode() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public void setPsyMmiChgEn(String data) throws RemoteException {
        }

        @Override // android.os.IGuardElfThermalControl
        public int getPsyBatteryRm() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getWiredOtgOnline() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getPsyBatteryNotify() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getWirelessAdapterPower() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getSmartChgMode() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setSmartChgMode(String data) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getBattPPSChgIng() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getBattPPSChgPower() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getCustomSelectChgMode() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setCustomSelectChgMode(int mode, boolean enable) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getPsyChargeTech() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setSmartCoolDown(int coolDown, int normalCoolDown, String pkgName) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public String getQuickModeGain() throws RemoteException {
            return null;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getBmsHeatingStatus() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int getUIsohValue() throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public String getReserveSocDebug() throws RemoteException {
            return null;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setReserveSocDebug(String data) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setWlsThirdPartitionInfo(String data) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int nightstandby(int status) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setChargerCycle(String data) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setChgOlcConfig(String data) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setSuperEnduranceStatus(String status) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setSuperEnduranceCount(String count) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setPsySlowChgEnable(int percent, int wattage, int status) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setBatteryLogPush(String data) throws RemoteException {
            return 0;
        }

        @Override // android.os.IGuardElfThermalControl
        public int setChgRusConfig(String data) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGuardElfThermalControl {
        static final int TRANSACTION_getBattPPSChgIng = 21;
        static final int TRANSACTION_getBattPPSChgPower = 22;
        static final int TRANSACTION_getBeginDecimal = 3;
        static final int TRANSACTION_getBmsHeatingStatus = 28;
        static final int TRANSACTION_getChargerTechnology = 7;
        static final int TRANSACTION_getCustomSelectChgMode = 23;
        static final int TRANSACTION_getEndDecimal = 4;
        static final int TRANSACTION_getPsyBatteryHmac = 6;
        static final int TRANSACTION_getPsyBatteryNotify = 17;
        static final int TRANSACTION_getPsyBatteryRm = 15;
        static final int TRANSACTION_getPsyChargeTech = 25;
        static final int TRANSACTION_getPsyOtgOnline = 5;
        static final int TRANSACTION_getQuickModeGain = 27;
        static final int TRANSACTION_getReserveSocDebug = 30;
        static final int TRANSACTION_getSmartChgMode = 19;
        static final int TRANSACTION_getUIsohValue = 29;
        static final int TRANSACTION_getWiredOtgOnline = 16;
        static final int TRANSACTION_getWirelessAdapterPower = 18;
        static final int TRANSACTION_getWirelessPenPresent = 8;
        static final int TRANSACTION_getWirelessTXEnable = 11;
        static final int TRANSACTION_getWirelessUserSleepMode = 13;
        static final int TRANSACTION_isCameraOn = 2;
        static final int TRANSACTION_nightstandby = 33;
        static final int TRANSACTION_setBatteryLogPush = 39;
        static final int TRANSACTION_setChargeLevel = 1;
        static final int TRANSACTION_setChargerCycle = 34;
        static final int TRANSACTION_setChgOlcConfig = 35;
        static final int TRANSACTION_setChgRusConfig = 40;
        static final int TRANSACTION_setCustomSelectChgMode = 24;
        static final int TRANSACTION_setPsyMmiChgEn = 14;
        static final int TRANSACTION_setPsySlowChgEnable = 38;
        static final int TRANSACTION_setReserveSocDebug = 31;
        static final int TRANSACTION_setSmartChgMode = 20;
        static final int TRANSACTION_setSmartCoolDown = 26;
        static final int TRANSACTION_setSuperEnduranceCount = 37;
        static final int TRANSACTION_setSuperEnduranceStatus = 36;
        static final int TRANSACTION_setWirelessPenSoc = 9;
        static final int TRANSACTION_setWirelessTXEnable = 10;
        static final int TRANSACTION_setWirelessUserSleepMode = 12;
        static final int TRANSACTION_setWlsThirdPartitionInfo = 32;

        public Stub() {
            attachInterface(this, IGuardElfThermalControl.DESCRIPTOR);
        }

        public static IGuardElfThermalControl asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IGuardElfThermalControl.DESCRIPTOR);
            if (iin != null && (iin instanceof IGuardElfThermalControl)) {
                return (IGuardElfThermalControl) iin;
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
                    return "setChargeLevel";
                case 2:
                    return "isCameraOn";
                case 3:
                    return "getBeginDecimal";
                case 4:
                    return "getEndDecimal";
                case 5:
                    return "getPsyOtgOnline";
                case 6:
                    return "getPsyBatteryHmac";
                case 7:
                    return "getChargerTechnology";
                case 8:
                    return "getWirelessPenPresent";
                case 9:
                    return "setWirelessPenSoc";
                case 10:
                    return "setWirelessTXEnable";
                case 11:
                    return "getWirelessTXEnable";
                case 12:
                    return "setWirelessUserSleepMode";
                case 13:
                    return "getWirelessUserSleepMode";
                case 14:
                    return "setPsyMmiChgEn";
                case 15:
                    return "getPsyBatteryRm";
                case 16:
                    return "getWiredOtgOnline";
                case 17:
                    return "getPsyBatteryNotify";
                case 18:
                    return "getWirelessAdapterPower";
                case 19:
                    return "getSmartChgMode";
                case 20:
                    return "setSmartChgMode";
                case 21:
                    return "getBattPPSChgIng";
                case 22:
                    return "getBattPPSChgPower";
                case 23:
                    return "getCustomSelectChgMode";
                case 24:
                    return "setCustomSelectChgMode";
                case 25:
                    return "getPsyChargeTech";
                case 26:
                    return "setSmartCoolDown";
                case 27:
                    return "getQuickModeGain";
                case 28:
                    return "getBmsHeatingStatus";
                case 29:
                    return "getUIsohValue";
                case 30:
                    return "getReserveSocDebug";
                case 31:
                    return "setReserveSocDebug";
                case 32:
                    return "setWlsThirdPartitionInfo";
                case 33:
                    return "nightstandby";
                case 34:
                    return "setChargerCycle";
                case 35:
                    return "setChgOlcConfig";
                case 36:
                    return "setSuperEnduranceStatus";
                case 37:
                    return "setSuperEnduranceCount";
                case 38:
                    return "setPsySlowChgEnable";
                case 39:
                    return "setBatteryLogPush";
                case 40:
                    return "setChgRusConfig";
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
                data.enforceInterface(IGuardElfThermalControl.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IGuardElfThermalControl.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            setChargeLevel(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            boolean _result = isCameraOn();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 3:
                            float _result2 = getBeginDecimal();
                            reply.writeNoException();
                            reply.writeFloat(_result2);
                            return true;
                        case 4:
                            float _result3 = getEndDecimal();
                            reply.writeNoException();
                            reply.writeFloat(_result3);
                            return true;
                        case 5:
                            int _result4 = getPsyOtgOnline();
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 6:
                            int _result5 = getPsyBatteryHmac();
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 7:
                            int _result6 = getChargerTechnology();
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 8:
                            int _result7 = getWirelessPenPresent();
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 9:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            setWirelessPenSoc(_arg02);
                            reply.writeNoException();
                            return true;
                        case 10:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            setWirelessTXEnable(_arg03);
                            reply.writeNoException();
                            return true;
                        case 11:
                            String _result8 = getWirelessTXEnable();
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 12:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            setWirelessUserSleepMode(_arg04);
                            reply.writeNoException();
                            return true;
                        case 13:
                            int _result9 = getWirelessUserSleepMode();
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 14:
                            String _arg05 = data.readString();
                            data.enforceNoDataAvail();
                            setPsyMmiChgEn(_arg05);
                            reply.writeNoException();
                            return true;
                        case 15:
                            int _result10 = getPsyBatteryRm();
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 16:
                            int _result11 = getWiredOtgOnline();
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 17:
                            int _result12 = getPsyBatteryNotify();
                            reply.writeNoException();
                            reply.writeInt(_result12);
                            return true;
                        case 18:
                            int _result13 = getWirelessAdapterPower();
                            reply.writeNoException();
                            reply.writeInt(_result13);
                            return true;
                        case 19:
                            int _result14 = getSmartChgMode();
                            reply.writeNoException();
                            reply.writeInt(_result14);
                            return true;
                        case 20:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            int _result15 = setSmartChgMode(_arg06);
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 21:
                            int _result16 = getBattPPSChgIng();
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 22:
                            int _result17 = getBattPPSChgPower();
                            reply.writeNoException();
                            reply.writeInt(_result17);
                            return true;
                        case 23:
                            int _result18 = getCustomSelectChgMode();
                            reply.writeNoException();
                            reply.writeInt(_result18);
                            return true;
                        case 24:
                            int _arg07 = data.readInt();
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            int _result19 = setCustomSelectChgMode(_arg07, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result19);
                            return true;
                        case 25:
                            int _result20 = getPsyChargeTech();
                            reply.writeNoException();
                            reply.writeInt(_result20);
                            return true;
                        case 26:
                            int _arg08 = data.readInt();
                            int _arg13 = data.readInt();
                            String _arg2 = data.readString();
                            data.enforceNoDataAvail();
                            int _result21 = setSmartCoolDown(_arg08, _arg13, _arg2);
                            reply.writeNoException();
                            reply.writeInt(_result21);
                            return true;
                        case 27:
                            String _result22 = getQuickModeGain();
                            reply.writeNoException();
                            reply.writeString(_result22);
                            return true;
                        case 28:
                            int _result23 = getBmsHeatingStatus();
                            reply.writeNoException();
                            reply.writeInt(_result23);
                            return true;
                        case 29:
                            int _result24 = getUIsohValue();
                            reply.writeNoException();
                            reply.writeInt(_result24);
                            return true;
                        case 30:
                            String _result25 = getReserveSocDebug();
                            reply.writeNoException();
                            reply.writeString(_result25);
                            return true;
                        case 31:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            int _result26 = setReserveSocDebug(_arg09);
                            reply.writeNoException();
                            reply.writeInt(_result26);
                            return true;
                        case 32:
                            String _arg010 = data.readString();
                            data.enforceNoDataAvail();
                            int _result27 = setWlsThirdPartitionInfo(_arg010);
                            reply.writeNoException();
                            reply.writeInt(_result27);
                            return true;
                        case 33:
                            int _arg011 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result28 = nightstandby(_arg011);
                            reply.writeNoException();
                            reply.writeInt(_result28);
                            return true;
                        case 34:
                            String _arg012 = data.readString();
                            data.enforceNoDataAvail();
                            int _result29 = setChargerCycle(_arg012);
                            reply.writeNoException();
                            reply.writeInt(_result29);
                            return true;
                        case 35:
                            String _arg013 = data.readString();
                            data.enforceNoDataAvail();
                            int _result30 = setChgOlcConfig(_arg013);
                            reply.writeNoException();
                            reply.writeInt(_result30);
                            return true;
                        case 36:
                            String _arg014 = data.readString();
                            data.enforceNoDataAvail();
                            int _result31 = setSuperEnduranceStatus(_arg014);
                            reply.writeNoException();
                            reply.writeInt(_result31);
                            return true;
                        case 37:
                            String _arg015 = data.readString();
                            data.enforceNoDataAvail();
                            int _result32 = setSuperEnduranceCount(_arg015);
                            reply.writeNoException();
                            reply.writeInt(_result32);
                            return true;
                        case 38:
                            int _arg016 = data.readInt();
                            int _arg14 = data.readInt();
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result33 = setPsySlowChgEnable(_arg016, _arg14, _arg22);
                            reply.writeNoException();
                            reply.writeInt(_result33);
                            return true;
                        case 39:
                            String _arg017 = data.readString();
                            data.enforceNoDataAvail();
                            int _result34 = setBatteryLogPush(_arg017);
                            reply.writeNoException();
                            reply.writeInt(_result34);
                            return true;
                        case 40:
                            String _arg018 = data.readString();
                            data.enforceNoDataAvail();
                            int _result35 = setChgRusConfig(_arg018);
                            reply.writeNoException();
                            reply.writeInt(_result35);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IGuardElfThermalControl {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IGuardElfThermalControl.DESCRIPTOR;
            }

            @Override // android.os.IGuardElfThermalControl
            public void setChargeLevel(String data, String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    _data.writeString(name);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public boolean isCameraOn() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public float getBeginDecimal() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public float getEndDecimal() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getPsyOtgOnline() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getPsyBatteryHmac() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getChargerTechnology() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getWirelessPenPresent() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public void setWirelessPenSoc(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public void setWirelessTXEnable(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public String getWirelessTXEnable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public void setWirelessUserSleepMode(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getWirelessUserSleepMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public void setPsyMmiChgEn(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getPsyBatteryRm() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getWiredOtgOnline() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getPsyBatteryNotify() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getWirelessAdapterPower() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getSmartChgMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setSmartChgMode(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getBattPPSChgIng() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getBattPPSChgPower() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getCustomSelectChgMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setCustomSelectChgMode(int mode, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getPsyChargeTech() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setSmartCoolDown(int coolDown, int normalCoolDown, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeInt(coolDown);
                    _data.writeInt(normalCoolDown);
                    _data.writeString(pkgName);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public String getQuickModeGain() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getBmsHeatingStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int getUIsohValue() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public String getReserveSocDebug() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setReserveSocDebug(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setWlsThirdPartitionInfo(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int nightstandby(int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setChargerCycle(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setChgOlcConfig(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setSuperEnduranceStatus(String status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(status);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setSuperEnduranceCount(String count) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(count);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setPsySlowChgEnable(int percent, int wattage, int status) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeInt(percent);
                    _data.writeInt(wattage);
                    _data.writeInt(status);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setBatteryLogPush(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IGuardElfThermalControl
            public int setChgRusConfig(String data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGuardElfThermalControl.DESCRIPTOR);
                    _data.writeString(data);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 39;
        }
    }
}
