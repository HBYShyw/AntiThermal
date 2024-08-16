package android.engineer;

import android.engineer.IEngineerCommandCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IEngineerRemoteService extends IInterface {
    public static final String DESCRIPTOR = "android.engineer.IEngineerRemoteService";

    void sendCommand(IEngineerCommand iEngineerCommand, IEngineerCommandCallback iEngineerCommandCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IEngineerRemoteService {
        @Override // android.engineer.IEngineerRemoteService
        public void sendCommand(IEngineerCommand command, IEngineerCommandCallback callback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IEngineerRemoteService {
        static final int TRANSACTION_sendCommand = 1;

        public Stub() {
            attachInterface(this, IEngineerRemoteService.DESCRIPTOR);
        }

        public static IEngineerRemoteService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IEngineerRemoteService.DESCRIPTOR);
            if (iin != null && (iin instanceof IEngineerRemoteService)) {
                return (IEngineerRemoteService) iin;
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
                    return "sendCommand";
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
                data.enforceInterface(IEngineerRemoteService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IEngineerRemoteService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IEngineerCommand _arg0 = (IEngineerCommand) data.readTypedObject(IEngineerCommand.CREATOR);
                            IEngineerCommandCallback _arg1 = IEngineerCommandCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            sendCommand(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IEngineerRemoteService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IEngineerRemoteService.DESCRIPTOR;
            }

            @Override // android.engineer.IEngineerRemoteService
            public void sendCommand(IEngineerCommand command, IEngineerCommandCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IEngineerRemoteService.DESCRIPTOR);
                    _data.writeTypedObject(command, 0);
                    _data.writeStrongInterface(callback);
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
