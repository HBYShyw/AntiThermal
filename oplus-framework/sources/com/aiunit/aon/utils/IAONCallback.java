package com.aiunit.aon.utils;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.aiunit.aon.utils.core.CommentResult;
import com.aiunit.aon.utils.core.ErrorResult;
import com.aiunit.aon.utils.core.InfoResult;

/* loaded from: classes.dex */
public interface IAONCallback extends IInterface {
    public static final String DESCRIPTOR = "com.aiunit.aon.utils.IAONCallback";

    String getRequestID() throws RemoteException;

    void onDetectedError(ErrorResult errorResult) throws RemoteException;

    void onDetectedInfo(InfoResult infoResult) throws RemoteException;

    void onDetectedResult(CommentResult commentResult) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IAONCallback {
        @Override // com.aiunit.aon.utils.IAONCallback
        public String getRequestID() throws RemoteException {
            return null;
        }

        @Override // com.aiunit.aon.utils.IAONCallback
        public void onDetectedError(ErrorResult errorResult) throws RemoteException {
        }

        @Override // com.aiunit.aon.utils.IAONCallback
        public void onDetectedInfo(InfoResult infoResult) throws RemoteException {
        }

        @Override // com.aiunit.aon.utils.IAONCallback
        public void onDetectedResult(CommentResult commentResult) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAONCallback {
        static final int TRANSACTION_getRequestID = 1;
        static final int TRANSACTION_onDetectedError = 2;
        static final int TRANSACTION_onDetectedInfo = 3;
        static final int TRANSACTION_onDetectedResult = 4;

        public Stub() {
            attachInterface(this, IAONCallback.DESCRIPTOR);
        }

        public static IAONCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAONCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IAONCallback)) {
                return (IAONCallback) iin;
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
                    return "getRequestID";
                case 2:
                    return "onDetectedError";
                case 3:
                    return "onDetectedInfo";
                case 4:
                    return "onDetectedResult";
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
                data.enforceInterface(IAONCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IAONCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _result = getRequestID();
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 2:
                            ErrorResult _arg0 = (ErrorResult) data.readTypedObject(ErrorResult.CREATOR);
                            data.enforceNoDataAvail();
                            onDetectedError(_arg0);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg0, 1);
                            return true;
                        case 3:
                            InfoResult _arg02 = (InfoResult) data.readTypedObject(InfoResult.CREATOR);
                            data.enforceNoDataAvail();
                            onDetectedInfo(_arg02);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg02, 1);
                            return true;
                        case 4:
                            CommentResult _arg03 = (CommentResult) data.readTypedObject(CommentResult.CREATOR);
                            data.enforceNoDataAvail();
                            onDetectedResult(_arg03);
                            reply.writeNoException();
                            reply.writeTypedObject(_arg03, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IAONCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAONCallback.DESCRIPTOR;
            }

            @Override // com.aiunit.aon.utils.IAONCallback
            public String getRequestID() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONCallback.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONCallback
            public void onDetectedError(ErrorResult errorResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONCallback.DESCRIPTOR);
                    _data.writeTypedObject(errorResult, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        errorResult.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONCallback
            public void onDetectedInfo(InfoResult infoResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONCallback.DESCRIPTOR);
                    _data.writeTypedObject(infoResult, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        infoResult.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONCallback
            public void onDetectedResult(CommentResult commentResult) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONCallback.DESCRIPTOR);
                    _data.writeTypedObject(commentResult, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        commentResult.readFromParcel(_reply);
                    }
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
