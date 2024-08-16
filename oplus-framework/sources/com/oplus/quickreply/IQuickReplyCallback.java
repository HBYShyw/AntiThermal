package com.oplus.quickreply;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IQuickReplyCallback extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.quickreply.IQuickReplyCallback";

    void onFinishBindingQuickReplyService(boolean z) throws RemoteException;

    void onFinishSendingMsg(boolean z) throws RemoteException;

    void onFinishUnbindingQuickReplyService() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IQuickReplyCallback {
        @Override // com.oplus.quickreply.IQuickReplyCallback
        public void onFinishBindingQuickReplyService(boolean sucess) throws RemoteException {
        }

        @Override // com.oplus.quickreply.IQuickReplyCallback
        public void onFinishSendingMsg(boolean sucess) throws RemoteException {
        }

        @Override // com.oplus.quickreply.IQuickReplyCallback
        public void onFinishUnbindingQuickReplyService() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IQuickReplyCallback {
        static final int TRANSACTION_onFinishBindingQuickReplyService = 1;
        static final int TRANSACTION_onFinishSendingMsg = 2;
        static final int TRANSACTION_onFinishUnbindingQuickReplyService = 3;

        public Stub() {
            attachInterface(this, IQuickReplyCallback.DESCRIPTOR);
        }

        public static IQuickReplyCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IQuickReplyCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IQuickReplyCallback)) {
                return (IQuickReplyCallback) iin;
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
                    return "onFinishBindingQuickReplyService";
                case 2:
                    return "onFinishSendingMsg";
                case 3:
                    return "onFinishUnbindingQuickReplyService";
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
                data.enforceInterface(IQuickReplyCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IQuickReplyCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onFinishBindingQuickReplyService(_arg0);
                            return true;
                        case 2:
                            boolean _arg02 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onFinishSendingMsg(_arg02);
                            return true;
                        case 3:
                            onFinishUnbindingQuickReplyService();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IQuickReplyCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IQuickReplyCallback.DESCRIPTOR;
            }

            @Override // com.oplus.quickreply.IQuickReplyCallback
            public void onFinishBindingQuickReplyService(boolean sucess) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IQuickReplyCallback.DESCRIPTOR);
                    _data.writeBoolean(sucess);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.quickreply.IQuickReplyCallback
            public void onFinishSendingMsg(boolean sucess) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IQuickReplyCallback.DESCRIPTOR);
                    _data.writeBoolean(sucess);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.quickreply.IQuickReplyCallback
            public void onFinishUnbindingQuickReplyService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IQuickReplyCallback.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
