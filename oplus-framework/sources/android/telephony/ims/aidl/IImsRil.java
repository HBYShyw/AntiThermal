package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsRilInd;
import com.oplus.ims.IImsExt;

/* loaded from: classes.dex */
public interface IImsRil extends IInterface {
    public static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsRil";

    void commonReqToIms(int i, int i2, Message message) throws RemoteException;

    IImsExt getIImsExtBinder() throws RemoteException;

    void registerIndication(IImsRilInd iImsRilInd) throws RemoteException;

    void unRegisterIndication(IImsRilInd iImsRilInd) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IImsRil {
        @Override // android.telephony.ims.aidl.IImsRil
        public void commonReqToIms(int phoneId, int requestId, Message result) throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IImsRil
        public void registerIndication(IImsRilInd ind) throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IImsRil
        public void unRegisterIndication(IImsRilInd ind) throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IImsRil
        public IImsExt getIImsExtBinder() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IImsRil {
        static final int TRANSACTION_commonReqToIms = 1;
        static final int TRANSACTION_getIImsExtBinder = 4;
        static final int TRANSACTION_registerIndication = 2;
        static final int TRANSACTION_unRegisterIndication = 3;

        public Stub() {
            attachInterface(this, IImsRil.DESCRIPTOR);
        }

        public static IImsRil asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IImsRil.DESCRIPTOR);
            if (iin != null && (iin instanceof IImsRil)) {
                return (IImsRil) iin;
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
                    return "commonReqToIms";
                case 2:
                    return "registerIndication";
                case 3:
                    return "unRegisterIndication";
                case 4:
                    return "getIImsExtBinder";
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
                data.enforceInterface(IImsRil.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IImsRil.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            int _arg1 = data.readInt();
                            Message _arg2 = (Message) data.readTypedObject(Message.CREATOR);
                            data.enforceNoDataAvail();
                            commonReqToIms(_arg0, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 2:
                            IImsRilInd _arg02 = IImsRilInd.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerIndication(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            IImsRilInd _arg03 = IImsRilInd.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unRegisterIndication(_arg03);
                            reply.writeNoException();
                            return true;
                        case 4:
                            IImsExt _result = getIImsExtBinder();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IImsRil {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IImsRil.DESCRIPTOR;
            }

            @Override // android.telephony.ims.aidl.IImsRil
            public void commonReqToIms(int phoneId, int requestId, Message result) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IImsRil.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(requestId);
                    _data.writeTypedObject(result, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsRil
            public void registerIndication(IImsRilInd ind) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IImsRil.DESCRIPTOR);
                    _data.writeStrongInterface(ind);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsRil
            public void unRegisterIndication(IImsRilInd ind) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IImsRil.DESCRIPTOR);
                    _data.writeStrongInterface(ind);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsRil
            public IImsExt getIImsExtBinder() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IImsRil.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    IImsExt _result = IImsExt.Stub.asInterface(_reply.readStrongBinder());
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
