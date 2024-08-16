package android.os;

import com.oplus.os.IOplusScreenStatusListener;

/* loaded from: classes.dex */
public interface IOplusPowerManager extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusPowerManager";

    void disableScreenStayAwakeOfApp(boolean z, int i) throws RemoteException;

    int getDefaultBrightness() throws RemoteException;

    int getDefaultScreenBrightnessSetting() throws RemoteException;

    boolean getDisplayAodStatus() throws RemoteException;

    float[] getDisplaysBrightnessByNit(float f) throws RemoteException;

    int getMaxBrightness() throws RemoteException;

    int getMaximumScreenBrightnessSetting() throws RemoteException;

    int getMinBrightness() throws RemoteException;

    long getMinScreenOffTimeout() throws RemoteException;

    int getMinimumScreenBrightnessSetting() throws RemoteException;

    boolean isScreenStayAwake() throws RemoteException;

    void registerScreenStatusListener(IOplusScreenStatusListener iOplusScreenStatusListener) throws RemoteException;

    void setFlashing(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    boolean setMinScreenOffTimeout(long j) throws RemoteException;

    void unregisterScreenStatusListener(IOplusScreenStatusListener iOplusScreenStatusListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusPowerManager {
        @Override // android.os.IOplusPowerManager
        public void registerScreenStatusListener(IOplusScreenStatusListener listener) throws RemoteException {
        }

        @Override // android.os.IOplusPowerManager
        public void unregisterScreenStatusListener(IOplusScreenStatusListener listener) throws RemoteException {
        }

        @Override // android.os.IOplusPowerManager
        public boolean setMinScreenOffTimeout(long timeout) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusPowerManager
        public long getMinScreenOffTimeout() throws RemoteException {
            return 0L;
        }

        @Override // android.os.IOplusPowerManager
        public boolean isScreenStayAwake() throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusPowerManager
        public void disableScreenStayAwakeOfApp(boolean disable, int uid) throws RemoteException {
        }

        @Override // android.os.IOplusPowerManager
        public boolean getDisplayAodStatus() throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusPowerManager
        public int getMinBrightness() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusPowerManager
        public int getMaxBrightness() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusPowerManager
        public int getDefaultBrightness() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusPowerManager
        public float[] getDisplaysBrightnessByNit(float nit) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusPowerManager
        public int getMinimumScreenBrightnessSetting() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusPowerManager
        public int getMaximumScreenBrightnessSetting() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusPowerManager
        public int getDefaultScreenBrightnessSetting() throws RemoteException {
            return 0;
        }

        @Override // android.os.IOplusPowerManager
        public void setFlashing(int type, int color, int onMS, int offMS, int mode) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusPowerManager {
        static final int TRANSACTION_disableScreenStayAwakeOfApp = 6;
        static final int TRANSACTION_getDefaultBrightness = 10;
        static final int TRANSACTION_getDefaultScreenBrightnessSetting = 14;
        static final int TRANSACTION_getDisplayAodStatus = 7;
        static final int TRANSACTION_getDisplaysBrightnessByNit = 11;
        static final int TRANSACTION_getMaxBrightness = 9;
        static final int TRANSACTION_getMaximumScreenBrightnessSetting = 13;
        static final int TRANSACTION_getMinBrightness = 8;
        static final int TRANSACTION_getMinScreenOffTimeout = 4;
        static final int TRANSACTION_getMinimumScreenBrightnessSetting = 12;
        static final int TRANSACTION_isScreenStayAwake = 5;
        static final int TRANSACTION_registerScreenStatusListener = 1;
        static final int TRANSACTION_setFlashing = 15;
        static final int TRANSACTION_setMinScreenOffTimeout = 3;
        static final int TRANSACTION_unregisterScreenStatusListener = 2;

        public Stub() {
            attachInterface(this, IOplusPowerManager.DESCRIPTOR);
        }

        public static IOplusPowerManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusPowerManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusPowerManager)) {
                return (IOplusPowerManager) iin;
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
                    return "registerScreenStatusListener";
                case 2:
                    return "unregisterScreenStatusListener";
                case 3:
                    return "setMinScreenOffTimeout";
                case 4:
                    return "getMinScreenOffTimeout";
                case 5:
                    return "isScreenStayAwake";
                case 6:
                    return "disableScreenStayAwakeOfApp";
                case 7:
                    return "getDisplayAodStatus";
                case 8:
                    return "getMinBrightness";
                case 9:
                    return "getMaxBrightness";
                case 10:
                    return "getDefaultBrightness";
                case 11:
                    return "getDisplaysBrightnessByNit";
                case 12:
                    return "getMinimumScreenBrightnessSetting";
                case 13:
                    return "getMaximumScreenBrightnessSetting";
                case 14:
                    return "getDefaultScreenBrightnessSetting";
                case 15:
                    return "setFlashing";
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
                data.enforceInterface(IOplusPowerManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusPowerManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusScreenStatusListener _arg0 = IOplusScreenStatusListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerScreenStatusListener(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            IOplusScreenStatusListener _arg02 = IOplusScreenStatusListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterScreenStatusListener(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            long _arg03 = data.readLong();
                            data.enforceNoDataAvail();
                            boolean _result = setMinScreenOffTimeout(_arg03);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 4:
                            long _result2 = getMinScreenOffTimeout();
                            reply.writeNoException();
                            reply.writeLong(_result2);
                            return true;
                        case 5:
                            boolean _result3 = isScreenStayAwake();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 6:
                            boolean _arg04 = data.readBoolean();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            disableScreenStayAwakeOfApp(_arg04, _arg1);
                            reply.writeNoException();
                            return true;
                        case 7:
                            boolean _result4 = getDisplayAodStatus();
                            reply.writeNoException();
                            reply.writeBoolean(_result4);
                            return true;
                        case 8:
                            int _result5 = getMinBrightness();
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 9:
                            int _result6 = getMaxBrightness();
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 10:
                            int _result7 = getDefaultBrightness();
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 11:
                            float _arg05 = data.readFloat();
                            data.enforceNoDataAvail();
                            float[] _result8 = getDisplaysBrightnessByNit(_arg05);
                            reply.writeNoException();
                            reply.writeFloatArray(_result8);
                            return true;
                        case 12:
                            int _result9 = getMinimumScreenBrightnessSetting();
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 13:
                            int _result10 = getMaximumScreenBrightnessSetting();
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 14:
                            int _result11 = getDefaultScreenBrightnessSetting();
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 15:
                            int _arg06 = data.readInt();
                            int _arg12 = data.readInt();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            int _arg4 = data.readInt();
                            data.enforceNoDataAvail();
                            setFlashing(_arg06, _arg12, _arg2, _arg3, _arg4);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusPowerManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusPowerManager.DESCRIPTOR;
            }

            @Override // android.os.IOplusPowerManager
            public void registerScreenStatusListener(IOplusScreenStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public void unregisterScreenStatusListener(IOplusScreenStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public boolean setMinScreenOffTimeout(long timeout) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    _data.writeLong(timeout);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public long getMinScreenOffTimeout() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public boolean isScreenStayAwake() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public void disableScreenStayAwakeOfApp(boolean disable, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    _data.writeBoolean(disable);
                    _data.writeInt(uid);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public boolean getDisplayAodStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public int getMinBrightness() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public int getMaxBrightness() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public int getDefaultBrightness() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public float[] getDisplaysBrightnessByNit(float nit) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    _data.writeFloat(nit);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    float[] _result = _reply.createFloatArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public int getMinimumScreenBrightnessSetting() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public int getMaximumScreenBrightnessSetting() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public int getDefaultScreenBrightnessSetting() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusPowerManager
            public void setFlashing(int type, int color, int onMS, int offMS, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusPowerManager.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(color);
                    _data.writeInt(onMS);
                    _data.writeInt(offMS);
                    _data.writeInt(mode);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 14;
        }
    }
}
