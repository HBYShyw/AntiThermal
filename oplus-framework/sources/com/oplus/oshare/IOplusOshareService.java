package com.oplus.oshare;

import android.bluetooth.OplusBluetoothMonitor;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.network.OlkConstants;
import com.oplus.oshare.IOplusOshareCallback;

/* loaded from: classes.dex */
public interface IOplusOshareService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.oshare.IOplusOshareService";

    void cancelTask(OplusOshareDevice oplusOshareDevice) throws RemoteException;

    boolean isSendOn() throws RemoteException;

    void pause() throws RemoteException;

    void registerCallback(IOplusOshareCallback iOplusOshareCallback) throws RemoteException;

    void resume() throws RemoteException;

    void scan() throws RemoteException;

    void sendData(Intent intent, OplusOshareDevice oplusOshareDevice) throws RemoteException;

    void stop() throws RemoteException;

    void switchSend(boolean z) throws RemoteException;

    void unregisterCallback(IOplusOshareCallback iOplusOshareCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusOshareService {
        @Override // com.oplus.oshare.IOplusOshareService
        public boolean isSendOn() throws RemoteException {
            return false;
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void switchSend(boolean isOn) throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void scan() throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void registerCallback(IOplusOshareCallback callback) throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void unregisterCallback(IOplusOshareCallback callback) throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void sendData(Intent intent, OplusOshareDevice target) throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void cancelTask(OplusOshareDevice device) throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void pause() throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void resume() throws RemoteException {
        }

        @Override // com.oplus.oshare.IOplusOshareService
        public void stop() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusOshareService {
        static final int TRANSACTION_cancelTask = 7;
        static final int TRANSACTION_isSendOn = 1;
        static final int TRANSACTION_pause = 8;
        static final int TRANSACTION_registerCallback = 4;
        static final int TRANSACTION_resume = 9;
        static final int TRANSACTION_scan = 3;
        static final int TRANSACTION_sendData = 6;
        static final int TRANSACTION_stop = 10;
        static final int TRANSACTION_switchSend = 2;
        static final int TRANSACTION_unregisterCallback = 5;

        public Stub() {
            attachInterface(this, IOplusOshareService.DESCRIPTOR);
        }

        public static IOplusOshareService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusOshareService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusOshareService)) {
                return (IOplusOshareService) iin;
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
                    return "isSendOn";
                case 2:
                    return "switchSend";
                case 3:
                    return OplusBluetoothMonitor.SCAN_MONIT_EVENT;
                case 4:
                    return OlkConstants.FUN_REGISTER_CALLBACK;
                case 5:
                    return "unregisterCallback";
                case 6:
                    return "sendData";
                case 7:
                    return "cancelTask";
                case 8:
                    return "pause";
                case 9:
                    return "resume";
                case 10:
                    return "stop";
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
                data.enforceInterface(IOplusOshareService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusOshareService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _result = isSendOn();
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            switchSend(_arg0);
                            reply.writeNoException();
                            return true;
                        case 3:
                            scan();
                            reply.writeNoException();
                            return true;
                        case 4:
                            IOplusOshareCallback _arg02 = IOplusOshareCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerCallback(_arg02);
                            reply.writeNoException();
                            return true;
                        case 5:
                            IOplusOshareCallback _arg03 = IOplusOshareCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterCallback(_arg03);
                            reply.writeNoException();
                            return true;
                        case 6:
                            Intent _arg04 = (Intent) data.readTypedObject(Intent.CREATOR);
                            OplusOshareDevice _arg1 = (OplusOshareDevice) data.readTypedObject(OplusOshareDevice.CREATOR);
                            data.enforceNoDataAvail();
                            sendData(_arg04, _arg1);
                            reply.writeNoException();
                            return true;
                        case 7:
                            OplusOshareDevice _arg05 = (OplusOshareDevice) data.readTypedObject(OplusOshareDevice.CREATOR);
                            data.enforceNoDataAvail();
                            cancelTask(_arg05);
                            reply.writeNoException();
                            return true;
                        case 8:
                            pause();
                            reply.writeNoException();
                            return true;
                        case 9:
                            resume();
                            reply.writeNoException();
                            return true;
                        case 10:
                            stop();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusOshareService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusOshareService.DESCRIPTOR;
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public boolean isSendOn() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void switchSend(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    _data.writeBoolean(isOn);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void scan() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void registerCallback(IOplusOshareCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void unregisterCallback(IOplusOshareCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void sendData(Intent intent, OplusOshareDevice target) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeTypedObject(target, 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void cancelTask(OplusOshareDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    _data.writeTypedObject(device, 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void pause() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void resume() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.oshare.IOplusOshareService
            public void stop() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusOshareService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 9;
        }
    }
}
