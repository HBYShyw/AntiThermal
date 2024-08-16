package vendor.oplus.hardware.urcc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.oplus.hardware.urcc.IUrccCallback;

/* loaded from: classes.dex */
public interface IUrcc extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$urcc$IUrcc".replace('$', '.');
    public static final String HASH = "cae43655eaf0af39cbb6d273c5e2206eebe33a10";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    int setRelatedSysInfo(int i, byte[] bArr) throws RemoteException;

    int uahNotifyExt(int i, int i2, int[] iArr) throws RemoteException;

    void uahResCtlRequestBypass(UrccRequestParcel urccRequestParcel) throws RemoteException;

    void urccInit() throws RemoteException;

    String urccPropertyGet(String str) throws RemoteException;

    int urccPropertySet(String str, String str2) throws RemoteException;

    int urccResCtlRelease(int i) throws RemoteException;

    int urccResCtlRequest(UrccRequestParcel urccRequestParcel) throws RemoteException;

    int urccResListeningRegister(UrccRequestParcel urccRequestParcel, IUrccCallback iUrccCallback) throws RemoteException;

    int urccResListeningUnRegister(int i) throws RemoteException;

    UrccRequestData[] urccResStateRequest(UrccRequestParcel urccRequestParcel) throws RemoteException;

    int urccRuleCtl(int i, int i2, UrccRequestData[] urccRequestDataArr) throws RemoteException;

    int urccThermalListeningRegister(int[] iArr, IUrccCallback iUrccCallback) throws RemoteException;

    int urccThermalListeningUnRegister(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IUrcc {
        @Override // vendor.oplus.hardware.urcc.IUrcc
        public void urccInit() throws RemoteException {
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int urccResCtlRequest(UrccRequestParcel mUrccRequestParcel) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int urccResCtlRelease(int mhandle) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public UrccRequestData[] urccResStateRequest(UrccRequestParcel mUrccRequestParcel) throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int urccResListeningRegister(UrccRequestParcel mUrccRequestParcel, IUrccCallback urccCallback) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int urccResListeningUnRegister(int mhandle) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public String urccPropertyGet(String name) throws RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int urccPropertySet(String name, String value) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int urccThermalListeningRegister(int[] types, IUrccCallback urccCallback) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int urccThermalListeningUnRegister(int mhandle) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int uahNotifyExt(int src, int type, int[] args) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int setRelatedSysInfo(int cmd, byte[] info) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int urccRuleCtl(int ruleId, int status, UrccRequestData[] ruleData) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public void uahResCtlRequestBypass(UrccRequestParcel mRequestParcel) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.urcc.IUrcc
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IUrcc {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_setRelatedSysInfo = 12;
        static final int TRANSACTION_uahNotifyExt = 11;
        static final int TRANSACTION_uahResCtlRequestBypass = 14;
        static final int TRANSACTION_urccInit = 1;
        static final int TRANSACTION_urccPropertyGet = 7;
        static final int TRANSACTION_urccPropertySet = 8;
        static final int TRANSACTION_urccResCtlRelease = 3;
        static final int TRANSACTION_urccResCtlRequest = 2;
        static final int TRANSACTION_urccResListeningRegister = 5;
        static final int TRANSACTION_urccResListeningUnRegister = 6;
        static final int TRANSACTION_urccResStateRequest = 4;
        static final int TRANSACTION_urccRuleCtl = 13;
        static final int TRANSACTION_urccThermalListeningRegister = 9;
        static final int TRANSACTION_urccThermalListeningUnRegister = 10;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IUrcc asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IUrcc)) {
                return (IUrcc) iin;
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
                    return "urccInit";
                case 2:
                    return "urccResCtlRequest";
                case 3:
                    return "urccResCtlRelease";
                case 4:
                    return "urccResStateRequest";
                case 5:
                    return "urccResListeningRegister";
                case 6:
                    return "urccResListeningUnRegister";
                case 7:
                    return "urccPropertyGet";
                case 8:
                    return "urccPropertySet";
                case 9:
                    return "urccThermalListeningRegister";
                case 10:
                    return "urccThermalListeningUnRegister";
                case 11:
                    return "uahNotifyExt";
                case 12:
                    return "setRelatedSysInfo";
                case 13:
                    return "urccRuleCtl";
                case 14:
                    return "uahResCtlRequestBypass";
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    return "getInterfaceHash";
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    return "getInterfaceVersion";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code >= 1 && code <= TRANSACTION_getInterfaceVersion) {
                data.enforceInterface(descriptor);
            }
            switch (code) {
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    reply.writeNoException();
                    reply.writeString(getInterfaceHash());
                    return true;
                case TRANSACTION_getInterfaceVersion /* 16777215 */:
                    reply.writeNoException();
                    reply.writeInt(getInterfaceVersion());
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            urccInit();
                            reply.writeNoException();
                            return true;
                        case 2:
                            UrccRequestParcel _arg0 = (UrccRequestParcel) data.readTypedObject(UrccRequestParcel.CREATOR);
                            data.enforceNoDataAvail();
                            int _result = urccResCtlRequest(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 3:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result2 = urccResCtlRelease(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 4:
                            UrccRequestParcel _arg03 = (UrccRequestParcel) data.readTypedObject(UrccRequestParcel.CREATOR);
                            data.enforceNoDataAvail();
                            UrccRequestData[] _result3 = urccResStateRequest(_arg03);
                            reply.writeNoException();
                            reply.writeTypedArray(_result3, 1);
                            return true;
                        case 5:
                            UrccRequestParcel _arg04 = (UrccRequestParcel) data.readTypedObject(UrccRequestParcel.CREATOR);
                            IUrccCallback _arg1 = IUrccCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result4 = urccResListeningRegister(_arg04, _arg1);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 6:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result5 = urccResListeningUnRegister(_arg05);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 7:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            String _result6 = urccPropertyGet(_arg06);
                            reply.writeNoException();
                            reply.writeString(_result6);
                            return true;
                        case 8:
                            String _arg07 = data.readString();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            int _result7 = urccPropertySet(_arg07, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 9:
                            int[] _arg08 = data.createIntArray();
                            IUrccCallback _arg13 = IUrccCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result8 = urccThermalListeningRegister(_arg08, _arg13);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 10:
                            int _arg09 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result9 = urccThermalListeningUnRegister(_arg09);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 11:
                            int _arg010 = data.readInt();
                            int _arg14 = data.readInt();
                            int[] _arg2 = data.createIntArray();
                            data.enforceNoDataAvail();
                            int _result10 = uahNotifyExt(_arg010, _arg14, _arg2);
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 12:
                            int _arg011 = data.readInt();
                            byte[] _arg15 = data.createByteArray();
                            data.enforceNoDataAvail();
                            int _result11 = setRelatedSysInfo(_arg011, _arg15);
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 13:
                            int _arg012 = data.readInt();
                            int _arg16 = data.readInt();
                            UrccRequestData[] _arg22 = (UrccRequestData[]) data.createTypedArray(UrccRequestData.CREATOR);
                            data.enforceNoDataAvail();
                            int _result12 = urccRuleCtl(_arg012, _arg16, _arg22);
                            reply.writeNoException();
                            reply.writeInt(_result12);
                            return true;
                        case 14:
                            UrccRequestParcel _arg013 = (UrccRequestParcel) data.readTypedObject(UrccRequestParcel.CREATOR);
                            data.enforceNoDataAvail();
                            uahResCtlRequestBypass(_arg013);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IUrcc {
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public void urccInit() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccInit is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int urccResCtlRequest(UrccRequestParcel mUrccRequestParcel) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(mUrccRequestParcel, 0);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccResCtlRequest is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int urccResCtlRelease(int mhandle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(mhandle);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccResCtlRelease is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public UrccRequestData[] urccResStateRequest(UrccRequestParcel mUrccRequestParcel) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(mUrccRequestParcel, 0);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccResStateRequest is unimplemented.");
                    }
                    _reply.readException();
                    UrccRequestData[] _result = (UrccRequestData[]) _reply.createTypedArray(UrccRequestData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int urccResListeningRegister(UrccRequestParcel mUrccRequestParcel, IUrccCallback urccCallback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(mUrccRequestParcel, 0);
                    _data.writeStrongInterface(urccCallback);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccResListeningRegister is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int urccResListeningUnRegister(int mhandle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(mhandle);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccResListeningUnRegister is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public String urccPropertyGet(String name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccPropertyGet is unimplemented.");
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int urccPropertySet(String name, String value) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccPropertySet is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int urccThermalListeningRegister(int[] types, IUrccCallback urccCallback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeIntArray(types);
                    _data.writeStrongInterface(urccCallback);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccThermalListeningRegister is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int urccThermalListeningUnRegister(int mhandle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(mhandle);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccThermalListeningUnRegister is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int uahNotifyExt(int src, int type, int[] args) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(src);
                    _data.writeInt(type);
                    _data.writeIntArray(args);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method uahNotifyExt is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int setRelatedSysInfo(int cmd, byte[] info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeByteArray(info);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method setRelatedSysInfo is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int urccRuleCtl(int ruleId, int status, UrccRequestData[] ruleData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(ruleId);
                    _data.writeInt(status);
                    _data.writeTypedArray(ruleData, 0);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method urccRuleCtl is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public void uahResCtlRequestBypass(UrccRequestParcel mRequestParcel) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(mRequestParcel, 0);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method uahResCtlRequestBypass is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public int getInterfaceVersion() throws RemoteException {
                if (this.mCachedVersion == -1) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceVersion, data, reply, 0);
                        reply.readException();
                        this.mCachedVersion = reply.readInt();
                    } finally {
                        reply.recycle();
                        data.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // vendor.oplus.hardware.urcc.IUrcc
            public synchronized String getInterfaceHash() throws RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    Parcel data = Parcel.obtain(asBinder());
                    Parcel reply = Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
                        reply.readException();
                        this.mCachedHash = reply.readString();
                        reply.recycle();
                        data.recycle();
                    } catch (Throwable th) {
                        reply.recycle();
                        data.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }
    }
}
