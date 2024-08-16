package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IPlmnCarrierConfigLoader extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.IPlmnCarrierConfigLoader";

    PersistableBundle getConfigForSlotId(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPlmnCarrierConfigLoader {
        @Override // com.android.internal.telephony.IPlmnCarrierConfigLoader
        public PersistableBundle getConfigForSlotId(int slotId, String callingPackage) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPlmnCarrierConfigLoader {
        static final int TRANSACTION_getConfigForSlotId = 1;

        public Stub() {
            attachInterface(this, IPlmnCarrierConfigLoader.DESCRIPTOR);
        }

        public static IPlmnCarrierConfigLoader asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IPlmnCarrierConfigLoader.DESCRIPTOR);
            if (iin != null && (iin instanceof IPlmnCarrierConfigLoader)) {
                return (IPlmnCarrierConfigLoader) iin;
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
                    return "getConfigForSlotId";
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
                data.enforceInterface(IPlmnCarrierConfigLoader.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IPlmnCarrierConfigLoader.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            PersistableBundle _result = getConfigForSlotId(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IPlmnCarrierConfigLoader {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IPlmnCarrierConfigLoader.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IPlmnCarrierConfigLoader
            public PersistableBundle getConfigForSlotId(int slotId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPlmnCarrierConfigLoader.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    PersistableBundle _result = (PersistableBundle) _reply.readTypedObject(PersistableBundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
