package com.oplus.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;

/* loaded from: classes.dex */
public interface ICarrierConfigService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.location.ICarrierConfigService";

    void getCarrierConfig(String str, ResultReceiver resultReceiver) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICarrierConfigService {
        @Override // com.oplus.location.ICarrierConfigService
        public void getCarrierConfig(String mccMnc, ResultReceiver result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICarrierConfigService {
        static final int TRANSACTION_getCarrierConfig = 1;

        public Stub() {
            attachInterface(this, ICarrierConfigService.DESCRIPTOR);
        }

        public static ICarrierConfigService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICarrierConfigService.DESCRIPTOR);
            if (iin != null && (iin instanceof ICarrierConfigService)) {
                return (ICarrierConfigService) iin;
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
                    return "getCarrierConfig";
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
                data.enforceInterface(ICarrierConfigService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICarrierConfigService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            ResultReceiver _arg1 = (ResultReceiver) data.readTypedObject(ResultReceiver.CREATOR);
                            data.enforceNoDataAvail();
                            getCarrierConfig(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICarrierConfigService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICarrierConfigService.DESCRIPTOR;
            }

            @Override // com.oplus.location.ICarrierConfigService
            public void getCarrierConfig(String mccMnc, ResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(ICarrierConfigService.DESCRIPTOR);
                    _data.writeString(mccMnc);
                    _data.writeTypedObject(result, 0);
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
