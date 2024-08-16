package vendor.oplus.hardware.gameopt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import vendor.oplus.hardware.gameopt.IGameCallback;

/* loaded from: classes.dex */
public interface IGameOptHalService extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$gameopt$IGameOptHalService".replace('$', '.');
    public static final String HASH = "48394e9cdbb0aee7539e513a52c9a972cf2f3fc7";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void notifyGameInfo(int i, String str) throws RemoteException;

    void notifySFBufferProduced(int i, long j) throws RemoteException;

    int notifyTGPAfps(int i) throws RemoteException;

    int setCallback(IGameCallback iGameCallback) throws RemoteException;

    void setKeyThread(String str) throws RemoteException;

    int unsetCallback(IGameCallback iGameCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IGameOptHalService {
        @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
        public void notifyGameInfo(int type, String jsonData) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
        public void notifySFBufferProduced(int bufferNum, long timeStamp) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
        public int notifyTGPAfps(int tFps) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
        public int setCallback(IGameCallback callback) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
        public void setKeyThread(String config) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
        public int unsetCallback(IGameCallback callback) throws RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGameOptHalService {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_notifyGameInfo = 1;
        static final int TRANSACTION_notifySFBufferProduced = 2;
        static final int TRANSACTION_notifyTGPAfps = 3;
        static final int TRANSACTION_setCallback = 4;
        static final int TRANSACTION_setKeyThread = 5;
        static final int TRANSACTION_unsetCallback = 6;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IGameOptHalService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGameOptHalService)) {
                return (IGameOptHalService) iin;
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
                    return "notifyGameInfo";
                case 2:
                    return "notifySFBufferProduced";
                case 3:
                    return "notifyTGPAfps";
                case 4:
                    return "setCallback";
                case 5:
                    return "setKeyThread";
                case 6:
                    return "unsetCallback";
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
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            notifyGameInfo(_arg0, _arg1);
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            long _arg12 = data.readLong();
                            data.enforceNoDataAvail();
                            notifySFBufferProduced(_arg02, _arg12);
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result = notifyTGPAfps(_arg03);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 4:
                            IGameCallback _arg04 = IGameCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result2 = setCallback(_arg04);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            data.enforceNoDataAvail();
                            setKeyThread(_arg05);
                            return true;
                        case 6:
                            IGameCallback _arg06 = IGameCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result3 = unsetCallback(_arg06);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IGameOptHalService {
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

            @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
            public void notifyGameInfo(int type, String jsonData) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(jsonData);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method notifyGameInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
            public void notifySFBufferProduced(int bufferNum, long timeStamp) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(bufferNum);
                    _data.writeLong(timeStamp);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method notifySFBufferProduced is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
            public int notifyTGPAfps(int tFps) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(tFps);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method notifyTGPAfps is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
            public int setCallback(IGameCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method setCallback is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
            public void setKeyThread(String config) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(config);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method setKeyThread is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
            public int unsetCallback(IGameCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new RemoteException("Method unsetCallback is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
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

            @Override // vendor.oplus.hardware.gameopt.IGameOptHalService
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
