package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IImsRilInd extends IInterface {
    public static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsRilInd";

    void onImsRilInd(int i, int i2, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IImsRilInd {
        @Override // android.telephony.ims.aidl.IImsRilInd
        public void onImsRilInd(int phoneId, int eventId, Bundle bundle) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IImsRilInd {
        static final int TRANSACTION_onImsRilInd = 1;

        public Stub() {
            attachInterface(this, IImsRilInd.DESCRIPTOR);
        }

        public static IImsRilInd asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IImsRilInd.DESCRIPTOR);
            if (iin != null && (iin instanceof IImsRilInd)) {
                return (IImsRilInd) iin;
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
                    return "onImsRilInd";
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
                data.enforceInterface(IImsRilInd.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IImsRilInd.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            Bundle _arg2 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            onImsRilInd(_arg0, _arg1, _arg2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IImsRilInd {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IImsRilInd.DESCRIPTOR;
            }

            @Override // android.telephony.ims.aidl.IImsRilInd
            public void onImsRilInd(int phoneId, int eventId, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IImsRilInd.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(eventId);
                    _data.writeTypedObject(bundle, 0);
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
