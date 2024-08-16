package vendor.oplus.hardware.gameopt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IGameCallback extends IInterface {
    public static final String DESCRIPTOR = "vendor$oplus$hardware$gameopt$IGameCallback".replace('$', '.');
    public static final String HASH = "48394e9cdbb0aee7539e513a52c9a972cf2f3fc7";
    public static final int VERSION = 1;

    String getInterfaceHash() throws RemoteException;

    int getInterfaceVersion() throws RemoteException;

    void onBigDataReport(String str) throws RemoteException;

    void onCommonCall(String str, int i) throws RemoteException;

    void onFrameLimit(String str) throws RemoteException;

    void onFrameProduce(String str) throws RemoteException;

    void onInputCollect(String str) throws RemoteException;

    void onNotify(String str) throws RemoteException;

    void onNotifyTemp(String str) throws RemoteException;

    void onReadTop(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IGameCallback {
        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onNotify(String info) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onReadTop(String info) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onFrameLimit(String info) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onBigDataReport(String info) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onNotifyTemp(String info) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onFrameProduce(String info) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onInputCollect(String info) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public void onCommonCall(String info, int type) throws RemoteException {
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.gameopt.IGameCallback
        public String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGameCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onBigDataReport = 4;
        static final int TRANSACTION_onCommonCall = 8;
        static final int TRANSACTION_onFrameLimit = 3;
        static final int TRANSACTION_onFrameProduce = 6;
        static final int TRANSACTION_onInputCollect = 7;
        static final int TRANSACTION_onNotify = 1;
        static final int TRANSACTION_onNotifyTemp = 5;
        static final int TRANSACTION_onReadTop = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static IGameCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGameCallback)) {
                return (IGameCallback) iin;
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
                    return "onNotify";
                case 2:
                    return "onReadTop";
                case 3:
                    return "onFrameLimit";
                case 4:
                    return "onBigDataReport";
                case 5:
                    return "onNotifyTemp";
                case 6:
                    return "onFrameProduce";
                case 7:
                    return "onInputCollect";
                case 8:
                    return "onCommonCall";
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
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            onNotify(_arg0);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            onReadTop(_arg02);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            onFrameLimit(_arg03);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            onBigDataReport(_arg04);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            data.enforceNoDataAvail();
                            onNotifyTemp(_arg05);
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            data.enforceNoDataAvail();
                            onFrameProduce(_arg06);
                            return true;
                        case 7:
                            String _arg07 = data.readString();
                            data.enforceNoDataAvail();
                            onInputCollect(_arg07);
                            return true;
                        case 8:
                            String _arg08 = data.readString();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onCommonCall(_arg08, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IGameCallback {
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

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
            public void onNotify(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onNotify is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
            public void onReadTop(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onReadTop is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
            public void onFrameLimit(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onFrameLimit is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
            public void onBigDataReport(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onBigDataReport is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
            public void onNotifyTemp(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onNotifyTemp is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
            public void onFrameProduce(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onFrameProduce is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
            public void onInputCollect(String info) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onInputCollect is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
            public void onCommonCall(String info, int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(info);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new RemoteException("Method onCommonCall is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
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

            @Override // vendor.oplus.hardware.gameopt.IGameCallback
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
