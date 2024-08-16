package com.oplus.hardware.devicecase;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.hardware.devicecase.IOplusDeviceCaseStateCallback;
import com.oplus.network.OlkConstants;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusDeviceCaseManager extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.hardware.devicecase.IOplusDeviceCaseManager";

    List<Rect> getViewPorts() throws RemoteException;

    boolean isEnabled() throws RemoteException;

    boolean isSupported() throws RemoteException;

    void registerCallback(IOplusDeviceCaseStateCallback iOplusDeviceCaseStateCallback) throws RemoteException;

    void unregisterCallback(IOplusDeviceCaseStateCallback iOplusDeviceCaseStateCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusDeviceCaseManager {
        @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
        public boolean isSupported() throws RemoteException {
            return false;
        }

        @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
        public boolean isEnabled() throws RemoteException {
            return false;
        }

        @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
        public List<Rect> getViewPorts() throws RemoteException {
            return null;
        }

        @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
        public void registerCallback(IOplusDeviceCaseStateCallback callback) throws RemoteException {
        }

        @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
        public void unregisterCallback(IOplusDeviceCaseStateCallback callback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusDeviceCaseManager {
        static final int TRANSACTION_getViewPorts = 3;
        static final int TRANSACTION_isEnabled = 2;
        static final int TRANSACTION_isSupported = 1;
        static final int TRANSACTION_registerCallback = 4;
        static final int TRANSACTION_unregisterCallback = 5;

        public Stub() {
            attachInterface(this, IOplusDeviceCaseManager.DESCRIPTOR);
        }

        public static IOplusDeviceCaseManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusDeviceCaseManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusDeviceCaseManager)) {
                return (IOplusDeviceCaseManager) iin;
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
                    return "isSupported";
                case 2:
                    return "isEnabled";
                case 3:
                    return "getViewPorts";
                case 4:
                    return OlkConstants.FUN_REGISTER_CALLBACK;
                case 5:
                    return "unregisterCallback";
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
                data.enforceInterface(IOplusDeviceCaseManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusDeviceCaseManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _result = isSupported();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            boolean _result2 = isEnabled();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            List<Rect> _result3 = getViewPorts();
                            reply.writeNoException();
                            reply.writeTypedList(_result3, 1);
                            return true;
                        case 4:
                            IOplusDeviceCaseStateCallback _arg0 = IOplusDeviceCaseStateCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerCallback(_arg0);
                            reply.writeNoException();
                            return true;
                        case 5:
                            IOplusDeviceCaseStateCallback _arg02 = IOplusDeviceCaseStateCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterCallback(_arg02);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusDeviceCaseManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusDeviceCaseManager.DESCRIPTOR;
            }

            @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
            public boolean isSupported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDeviceCaseManager.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
            public boolean isEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDeviceCaseManager.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
            public List<Rect> getViewPorts() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDeviceCaseManager.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    List<Rect> _result = _reply.createTypedArrayList(Rect.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
            public void registerCallback(IOplusDeviceCaseStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDeviceCaseManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.hardware.devicecase.IOplusDeviceCaseManager
            public void unregisterCallback(IOplusDeviceCaseStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusDeviceCaseManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 4;
        }
    }
}
