package com.oplus.network.heartbeat;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.heartbeat.IHeartbeatListener;

/* loaded from: classes.dex */
public interface IHeartbeat extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.network.heartbeat.IHeartbeat";

    String establishHeartbeat(HeartbeatSettings heartbeatSettings, IHeartbeatListener iHeartbeatListener) throws RemoteException;

    boolean isHeartbeatAvailabel() throws RemoteException;

    boolean isHeartbeatDynamicCycleEnabled() throws RemoteException;

    void pauseHeartbeat(String str) throws RemoteException;

    void resumeHeartbeat(String str) throws RemoteException;

    void stopHeartbeat(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IHeartbeat {
        @Override // com.oplus.network.heartbeat.IHeartbeat
        public boolean isHeartbeatAvailabel() throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.heartbeat.IHeartbeat
        public boolean isHeartbeatDynamicCycleEnabled() throws RemoteException {
            return false;
        }

        @Override // com.oplus.network.heartbeat.IHeartbeat
        public String establishHeartbeat(HeartbeatSettings request, IHeartbeatListener callback) throws RemoteException {
            return null;
        }

        @Override // com.oplus.network.heartbeat.IHeartbeat
        public void stopHeartbeat(String proxyKey) throws RemoteException {
        }

        @Override // com.oplus.network.heartbeat.IHeartbeat
        public void pauseHeartbeat(String proxyKey) throws RemoteException {
        }

        @Override // com.oplus.network.heartbeat.IHeartbeat
        public void resumeHeartbeat(String proxyKey) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IHeartbeat {
        static final int TRANSACTION_establishHeartbeat = 3;
        static final int TRANSACTION_isHeartbeatAvailabel = 1;
        static final int TRANSACTION_isHeartbeatDynamicCycleEnabled = 2;
        static final int TRANSACTION_pauseHeartbeat = 5;
        static final int TRANSACTION_resumeHeartbeat = 6;
        static final int TRANSACTION_stopHeartbeat = 4;

        public Stub() {
            attachInterface(this, IHeartbeat.DESCRIPTOR);
        }

        public static IHeartbeat asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IHeartbeat.DESCRIPTOR);
            if (iin != null && (iin instanceof IHeartbeat)) {
                return (IHeartbeat) iin;
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
                    return "isHeartbeatAvailabel";
                case 2:
                    return "isHeartbeatDynamicCycleEnabled";
                case 3:
                    return "establishHeartbeat";
                case 4:
                    return "stopHeartbeat";
                case 5:
                    return "pauseHeartbeat";
                case 6:
                    return "resumeHeartbeat";
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
                data.enforceInterface(IHeartbeat.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IHeartbeat.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _result = isHeartbeatAvailabel();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            boolean _result2 = isHeartbeatDynamicCycleEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            HeartbeatSettings _arg0 = (HeartbeatSettings) data.readTypedObject(HeartbeatSettings.CREATOR);
                            IHeartbeatListener _arg1 = IHeartbeatListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            String _result3 = establishHeartbeat(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        case 4:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            stopHeartbeat(_arg02);
                            reply.writeNoException();
                            return true;
                        case 5:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            pauseHeartbeat(_arg03);
                            reply.writeNoException();
                            return true;
                        case 6:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            resumeHeartbeat(_arg04);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IHeartbeat {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IHeartbeat.DESCRIPTOR;
            }

            @Override // com.oplus.network.heartbeat.IHeartbeat
            public boolean isHeartbeatAvailabel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeartbeat.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.heartbeat.IHeartbeat
            public boolean isHeartbeatDynamicCycleEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeartbeat.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.heartbeat.IHeartbeat
            public String establishHeartbeat(HeartbeatSettings request, IHeartbeatListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeartbeat.DESCRIPTOR);
                    _data.writeTypedObject(request, 0);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.heartbeat.IHeartbeat
            public void stopHeartbeat(String proxyKey) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeartbeat.DESCRIPTOR);
                    _data.writeString(proxyKey);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.heartbeat.IHeartbeat
            public void pauseHeartbeat(String proxyKey) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeartbeat.DESCRIPTOR);
                    _data.writeString(proxyKey);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.network.heartbeat.IHeartbeat
            public void resumeHeartbeat(String proxyKey) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHeartbeat.DESCRIPTOR);
                    _data.writeString(proxyKey);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 5;
        }
    }
}
