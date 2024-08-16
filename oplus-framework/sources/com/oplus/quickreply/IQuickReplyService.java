package com.oplus.quickreply;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.quickreply.IQuickReplyManagerService;

/* loaded from: classes.dex */
public interface IQuickReplyService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.quickreply.IQuickReplyService";

    void onSendMessage(String str) throws RemoteException;

    void onSetManagerServiceCallback(IQuickReplyManagerService iQuickReplyManagerService) throws RemoteException;

    void openAccessibilityService() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IQuickReplyService {
        @Override // com.oplus.quickreply.IQuickReplyService
        public void openAccessibilityService() throws RemoteException {
        }

        @Override // com.oplus.quickreply.IQuickReplyService
        public void onSendMessage(String message) throws RemoteException {
        }

        @Override // com.oplus.quickreply.IQuickReplyService
        public void onSetManagerServiceCallback(IQuickReplyManagerService callback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IQuickReplyService {
        static final int TRANSACTION_onSendMessage = 2;
        static final int TRANSACTION_onSetManagerServiceCallback = 3;
        static final int TRANSACTION_openAccessibilityService = 1;

        public Stub() {
            attachInterface(this, IQuickReplyService.DESCRIPTOR);
        }

        public static IQuickReplyService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IQuickReplyService.DESCRIPTOR);
            if (iin != null && (iin instanceof IQuickReplyService)) {
                return (IQuickReplyService) iin;
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
                    return "openAccessibilityService";
                case 2:
                    return "onSendMessage";
                case 3:
                    return "onSetManagerServiceCallback";
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
                data.enforceInterface(IQuickReplyService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IQuickReplyService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            openAccessibilityService();
                            return true;
                        case 2:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            onSendMessage(_arg0);
                            return true;
                        case 3:
                            IQuickReplyManagerService _arg02 = IQuickReplyManagerService.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            onSetManagerServiceCallback(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IQuickReplyService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IQuickReplyService.DESCRIPTOR;
            }

            @Override // com.oplus.quickreply.IQuickReplyService
            public void openAccessibilityService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IQuickReplyService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.quickreply.IQuickReplyService
            public void onSendMessage(String message) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IQuickReplyService.DESCRIPTOR);
                    _data.writeString(message);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.quickreply.IQuickReplyService
            public void onSetManagerServiceCallback(IQuickReplyManagerService callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IQuickReplyService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
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
