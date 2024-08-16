package com.oplus.quickreply;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IQuickReplyManagerService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.quickreply.IQuickReplyManagerService";

    void onFinishMessage(boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IQuickReplyManagerService {
        @Override // com.oplus.quickreply.IQuickReplyManagerService
        public void onFinishMessage(boolean sucess) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IQuickReplyManagerService {
        static final int TRANSACTION_onFinishMessage = 1;

        public Stub() {
            attachInterface(this, IQuickReplyManagerService.DESCRIPTOR);
        }

        public static IQuickReplyManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IQuickReplyManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IQuickReplyManagerService)) {
                return (IQuickReplyManagerService) iin;
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
                    return "onFinishMessage";
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
                data.enforceInterface(IQuickReplyManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IQuickReplyManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onFinishMessage(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IQuickReplyManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IQuickReplyManagerService.DESCRIPTOR;
            }

            @Override // com.oplus.quickreply.IQuickReplyManagerService
            public void onFinishMessage(boolean sucess) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IQuickReplyManagerService.DESCRIPTOR);
                    _data.writeBoolean(sucess);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
