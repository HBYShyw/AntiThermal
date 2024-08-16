package com.aiunit.aon.utils;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.aiunit.aon.utils.core.FaceInfo;

/* loaded from: classes.dex */
public interface IAONEventListener extends IInterface {
    public static final String DESCRIPTOR = "com.aiunit.aon.utils.IAONEventListener";

    void onEvent(int i, int i2) throws RemoteException;

    void onEventParam(int i, int i2, FaceInfo faceInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IAONEventListener {
        @Override // com.aiunit.aon.utils.IAONEventListener
        public void onEvent(int eventType, int event) throws RemoteException {
        }

        @Override // com.aiunit.aon.utils.IAONEventListener
        public void onEventParam(int eventType, int event, FaceInfo faceInfo) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAONEventListener {
        static final int TRANSACTION_onEvent = 1;
        static final int TRANSACTION_onEventParam = 2;

        public Stub() {
            attachInterface(this, IAONEventListener.DESCRIPTOR);
        }

        public static IAONEventListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAONEventListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IAONEventListener)) {
                return (IAONEventListener) iin;
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
                    return "onEvent";
                case 2:
                    return "onEventParam";
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
                data.enforceInterface(IAONEventListener.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IAONEventListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onEvent(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            FaceInfo _arg2 = (FaceInfo) data.readTypedObject(FaceInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onEventParam(_arg02, _arg12, _arg2);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg2, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IAONEventListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAONEventListener.DESCRIPTOR;
            }

            @Override // com.aiunit.aon.utils.IAONEventListener
            public void onEvent(int eventType, int event) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONEventListener.DESCRIPTOR);
                    _data.writeInt(eventType);
                    _data.writeInt(event);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONEventListener
            public void onEventParam(int eventType, int event, FaceInfo faceInfo) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONEventListener.DESCRIPTOR);
                    _data.writeInt(eventType);
                    _data.writeInt(event);
                    _data.writeTypedObject(faceInfo, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        faceInfo.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 1;
        }
    }
}
