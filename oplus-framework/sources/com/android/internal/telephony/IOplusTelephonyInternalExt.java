package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.telephony.IOemHookCallback;

/* loaded from: classes.dex */
public interface IOplusTelephonyInternalExt extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.IOplusTelephonyInternalExt";

    void enableEndc(int i, boolean z) throws RemoteException;

    int getIccAppFamily(int i) throws RemoteException;

    boolean getPropertyValueBool(String str, boolean z) throws RemoteException;

    int getPropertyValueInt(String str, int i) throws RemoteException;

    String getPropertyValueString(String str, String str2) throws RemoteException;

    int getSimLockStateForRSU(int i) throws RemoteException;

    void registerAtUrcInd(int i, IOemHookCallback iOemHookCallback) throws RemoteException;

    void sendAtCmd(int i, long j, String str, IOemHookCallback iOemHookCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusTelephonyInternalExt {
        @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
        public void registerAtUrcInd(int slotId, IOemHookCallback cb) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
        public void sendAtCmd(int slotId, long token, String atCmd, IOemHookCallback cb) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
        public int getIccAppFamily(int slotId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
        public int getSimLockStateForRSU(int phoneId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
        public void enableEndc(int slotId, boolean enable) throws RemoteException {
        }

        @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
        public int getPropertyValueInt(String property, int def) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
        public boolean getPropertyValueBool(String property, boolean def) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
        public String getPropertyValueString(String property, String def) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusTelephonyInternalExt {
        static final int TRANSACTION_enableEndc = 2002;
        static final int TRANSACTION_getIccAppFamily = 1003;
        static final int TRANSACTION_getPropertyValueBool = 2004;
        static final int TRANSACTION_getPropertyValueInt = 2003;
        static final int TRANSACTION_getPropertyValueString = 2005;
        static final int TRANSACTION_getSimLockStateForRSU = 1004;
        static final int TRANSACTION_registerAtUrcInd = 1001;
        static final int TRANSACTION_sendAtCmd = 1002;

        public Stub() {
            attachInterface(this, IOplusTelephonyInternalExt.DESCRIPTOR);
        }

        public static IOplusTelephonyInternalExt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusTelephonyInternalExt.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusTelephonyInternalExt)) {
                return (IOplusTelephonyInternalExt) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1001:
                    return "registerAtUrcInd";
                case 1002:
                    return "sendAtCmd";
                case 1003:
                    return "getIccAppFamily";
                case 1004:
                    return "getSimLockStateForRSU";
                case 2002:
                    return "enableEndc";
                case 2003:
                    return "getPropertyValueInt";
                case 2004:
                    return "getPropertyValueBool";
                case 2005:
                    return "getPropertyValueString";
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
                data.enforceInterface(IOplusTelephonyInternalExt.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusTelephonyInternalExt.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1001:
                            int _arg0 = data.readInt();
                            IOemHookCallback _arg1 = IOemHookCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerAtUrcInd(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 1002:
                            int _arg02 = data.readInt();
                            long _arg12 = data.readLong();
                            String _arg2 = data.readString();
                            IOemHookCallback _arg3 = IOemHookCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            sendAtCmd(_arg02, _arg12, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        case 1003:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result = getIccAppFamily(_arg03);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 1004:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result2 = getSimLockStateForRSU(_arg04);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 2002:
                            int _arg05 = data.readInt();
                            boolean _arg13 = data.readBoolean();
                            data.enforceNoDataAvail();
                            enableEndc(_arg05, _arg13);
                            reply.writeNoException();
                            return true;
                        case 2003:
                            String _arg06 = data.readString();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result3 = getPropertyValueInt(_arg06, _arg14);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 2004:
                            String _arg07 = data.readString();
                            boolean _arg15 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result4 = getPropertyValueBool(_arg07, _arg15);
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 2005:
                            String _arg08 = data.readString();
                            String _arg16 = data.readString();
                            data.enforceNoDataAvail();
                            String _result5 = getPropertyValueString(_arg08, _arg16);
                            reply.writeNoException();
                            reply.writeString(_result5);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusTelephonyInternalExt {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusTelephonyInternalExt.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
            public void registerAtUrcInd(int slotId, IOemHookCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyInternalExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(1001, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
            public void sendAtCmd(int slotId, long token, String atCmd, IOemHookCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyInternalExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeLong(token);
                    _data.writeString(atCmd);
                    _data.writeStrongInterface(cb);
                    this.mRemote.transact(1002, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
            public int getIccAppFamily(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyInternalExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    this.mRemote.transact(1003, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
            public int getSimLockStateForRSU(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyInternalExt.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(1004, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
            public void enableEndc(int slotId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyInternalExt.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(2002, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
            public int getPropertyValueInt(String property, int def) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyInternalExt.DESCRIPTOR);
                    _data.writeString(property);
                    _data.writeInt(def);
                    this.mRemote.transact(2003, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
            public boolean getPropertyValueBool(String property, boolean def) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyInternalExt.DESCRIPTOR);
                    _data.writeString(property);
                    _data.writeBoolean(def);
                    this.mRemote.transact(2004, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IOplusTelephonyInternalExt
            public String getPropertyValueString(String property, String def) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTelephonyInternalExt.DESCRIPTOR);
                    _data.writeString(property);
                    _data.writeString(def);
                    this.mRemote.transact(2005, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2004;
        }
    }
}
