package com.android.internal.app;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ILMServiceManager extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.app.ILMServiceManager";

    boolean enableBoost(int i, int i2, int i3, int i4) throws RemoteException;

    boolean enableMobileBoost() throws RemoteException;

    String getLuckyMoneyInfo(int i) throws RemoteException;

    Bundle getModeData(int i, int i2, int i3) throws RemoteException;

    Bundle getModeEnableInfo(int i, int i2) throws RemoteException;

    Bundle getSwitchInfo() throws RemoteException;

    boolean inDebugMode() throws RemoteException;

    boolean isInitialized() throws RemoteException;

    void writeDCS(Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ILMServiceManager {
        @Override // com.android.internal.app.ILMServiceManager
        public Bundle getModeEnableInfo(int type, int versionCode) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.ILMServiceManager
        public String getLuckyMoneyInfo(int type) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.ILMServiceManager
        public boolean enableBoost(int pid, int uid, int timeout, int code) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.app.ILMServiceManager
        public Bundle getModeData(int type, int versionCode, int defaultValue) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.ILMServiceManager
        public Bundle getSwitchInfo() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.ILMServiceManager
        public boolean enableMobileBoost() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.app.ILMServiceManager
        public void writeDCS(Bundle data) throws RemoteException {
        }

        @Override // com.android.internal.app.ILMServiceManager
        public boolean isInitialized() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.app.ILMServiceManager
        public boolean inDebugMode() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ILMServiceManager {
        static final int TRANSACTION_enableBoost = 3;
        static final int TRANSACTION_enableMobileBoost = 6;
        static final int TRANSACTION_getLuckyMoneyInfo = 2;
        static final int TRANSACTION_getModeData = 4;
        static final int TRANSACTION_getModeEnableInfo = 1;
        static final int TRANSACTION_getSwitchInfo = 5;
        static final int TRANSACTION_inDebugMode = 9;
        static final int TRANSACTION_isInitialized = 8;
        static final int TRANSACTION_writeDCS = 7;

        public Stub() {
            attachInterface(this, ILMServiceManager.DESCRIPTOR);
        }

        public static ILMServiceManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ILMServiceManager.DESCRIPTOR);
            if (iin != null && (iin instanceof ILMServiceManager)) {
                return (ILMServiceManager) iin;
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
                    return "getModeEnableInfo";
                case 2:
                    return "getLuckyMoneyInfo";
                case 3:
                    return "enableBoost";
                case 4:
                    return "getModeData";
                case 5:
                    return "getSwitchInfo";
                case 6:
                    return "enableMobileBoost";
                case 7:
                    return "writeDCS";
                case 8:
                    return "isInitialized";
                case 9:
                    return "inDebugMode";
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
                data.enforceInterface(ILMServiceManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ILMServiceManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result = getModeEnableInfo(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            String _result2 = getLuckyMoneyInfo(_arg02);
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg12 = data.readInt();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result3 = enableBoost(_arg03, _arg12, _arg2, _arg3);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg13 = data.readInt();
                            int _arg22 = data.readInt();
                            data.enforceNoDataAvail();
                            Bundle _result4 = getModeData(_arg04, _arg13, _arg22);
                            reply.writeNoException();
                            reply.writeTypedObject(_result4, 1);
                            return true;
                        case 5:
                            Bundle _result5 = getSwitchInfo();
                            reply.writeNoException();
                            reply.writeTypedObject(_result5, 1);
                            return true;
                        case 6:
                            boolean _result6 = enableMobileBoost();
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 7:
                            Bundle _arg05 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            writeDCS(_arg05);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg05, 1);
                            return true;
                        case 8:
                            boolean _result7 = isInitialized();
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 9:
                            boolean _result8 = inDebugMode();
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ILMServiceManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ILMServiceManager.DESCRIPTOR;
            }

            @Override // com.android.internal.app.ILMServiceManager
            public Bundle getModeEnableInfo(int type, int versionCode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(versionCode);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ILMServiceManager
            public String getLuckyMoneyInfo(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ILMServiceManager
            public boolean enableBoost(int pid, int uid, int timeout, int code) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeInt(timeout);
                    _data.writeInt(code);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ILMServiceManager
            public Bundle getModeData(int type, int versionCode, int defaultValue) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(versionCode);
                    _data.writeInt(defaultValue);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ILMServiceManager
            public Bundle getSwitchInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    Bundle _result = (Bundle) _reply.readTypedObject(Bundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ILMServiceManager
            public boolean enableMobileBoost() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ILMServiceManager
            public void writeDCS(Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        data.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ILMServiceManager
            public boolean isInitialized() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ILMServiceManager
            public boolean inDebugMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ILMServiceManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 8;
        }
    }
}
