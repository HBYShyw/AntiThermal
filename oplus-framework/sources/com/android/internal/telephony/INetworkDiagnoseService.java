package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.SignalStrength;

/* loaded from: classes.dex */
public interface INetworkDiagnoseService extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.INetworkDiagnoseService";

    SignalStrength getOrigSignalStrength(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements INetworkDiagnoseService {
        @Override // com.android.internal.telephony.INetworkDiagnoseService
        public SignalStrength getOrigSignalStrength(int subId) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements INetworkDiagnoseService {
        static final int TRANSACTION_getOrigSignalStrength = 1;

        public Stub() {
            attachInterface(this, INetworkDiagnoseService.DESCRIPTOR);
        }

        public static INetworkDiagnoseService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(INetworkDiagnoseService.DESCRIPTOR);
            if (iin != null && (iin instanceof INetworkDiagnoseService)) {
                return (INetworkDiagnoseService) iin;
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
                    return "getOrigSignalStrength";
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
                data.enforceInterface(INetworkDiagnoseService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(INetworkDiagnoseService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            SignalStrength _result = getOrigSignalStrength(_arg0);
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements INetworkDiagnoseService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return INetworkDiagnoseService.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.INetworkDiagnoseService
            public SignalStrength getOrigSignalStrength(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INetworkDiagnoseService.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    SignalStrength _result = (SignalStrength) _reply.readTypedObject(SignalStrength.CREATOR);
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
