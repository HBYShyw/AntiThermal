package com.oplus.neuron;

import android.content.ContentValues;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.neuron.INeuronSystemEventListener;
import java.util.List;

/* loaded from: classes.dex */
public interface INeuronSystemService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.neuron.INeuronSystemService";

    void enableRecommendedApps(boolean z, List<String> list) throws RemoteException;

    List<String> getRecommendedApps(int i) throws RemoteException;

    void publishEvent(int i, ContentValues contentValues) throws RemoteException;

    void registerEventListener(INeuronSystemEventListener iNeuronSystemEventListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements INeuronSystemService {
        @Override // com.oplus.neuron.INeuronSystemService
        public void publishEvent(int type, ContentValues contentValues) throws RemoteException {
        }

        @Override // com.oplus.neuron.INeuronSystemService
        public void registerEventListener(INeuronSystemEventListener listener) throws RemoteException {
        }

        @Override // com.oplus.neuron.INeuronSystemService
        public void enableRecommendedApps(boolean enable, List<String> pkgs) throws RemoteException {
        }

        @Override // com.oplus.neuron.INeuronSystemService
        public List<String> getRecommendedApps(int topK) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements INeuronSystemService {
        static final int TRANSACTION_enableRecommendedApps = 3;
        static final int TRANSACTION_getRecommendedApps = 4;
        static final int TRANSACTION_publishEvent = 1;
        static final int TRANSACTION_registerEventListener = 2;

        public Stub() {
            attachInterface(this, INeuronSystemService.DESCRIPTOR);
        }

        public static INeuronSystemService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(INeuronSystemService.DESCRIPTOR);
            if (iin != null && (iin instanceof INeuronSystemService)) {
                return (INeuronSystemService) iin;
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
                    return "publishEvent";
                case 2:
                    return "registerEventListener";
                case 3:
                    return "enableRecommendedApps";
                case 4:
                    return "getRecommendedApps";
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
                data.enforceInterface(INeuronSystemService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(INeuronSystemService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            ContentValues _arg1 = (ContentValues) data.readTypedObject(ContentValues.CREATOR);
                            data.enforceNoDataAvail();
                            publishEvent(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            INeuronSystemEventListener _arg02 = INeuronSystemEventListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerEventListener(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            boolean _arg03 = data.readBoolean();
                            List<String> _arg12 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            enableRecommendedApps(_arg03, _arg12);
                            reply.writeNoException();
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            List<String> _result = getRecommendedApps(_arg04);
                            reply.writeNoException();
                            reply.writeStringList(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INeuronSystemService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return INeuronSystemService.DESCRIPTOR;
            }

            @Override // com.oplus.neuron.INeuronSystemService
            public void publishEvent(int type, ContentValues contentValues) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INeuronSystemService.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(contentValues, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.neuron.INeuronSystemService
            public void registerEventListener(INeuronSystemEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INeuronSystemService.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.neuron.INeuronSystemService
            public void enableRecommendedApps(boolean enable, List<String> pkgs) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INeuronSystemService.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    _data.writeStringList(pkgs);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.neuron.INeuronSystemService
            public List<String> getRecommendedApps(int topK) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INeuronSystemService.DESCRIPTOR);
                    _data.writeInt(topK);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 3;
        }
    }
}
