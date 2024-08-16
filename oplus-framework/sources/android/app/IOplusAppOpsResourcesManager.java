package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusAppOpsResourcesManager extends IInterface {
    public static final String DESCRIPTOR = "android.app.IOplusAppOpsResourcesManager";

    List readCustomizedAppOps(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusAppOpsResourcesManager {
        @Override // android.app.IOplusAppOpsResourcesManager
        public List readCustomizedAppOps(int opcode) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusAppOpsResourcesManager {
        static final int TRANSACTION_readCustomizedAppOps = 1;

        public Stub() {
            attachInterface(this, IOplusAppOpsResourcesManager.DESCRIPTOR);
        }

        public static IOplusAppOpsResourcesManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusAppOpsResourcesManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusAppOpsResourcesManager)) {
                return (IOplusAppOpsResourcesManager) iin;
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
                    return "readCustomizedAppOps";
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
                data.enforceInterface(IOplusAppOpsResourcesManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusAppOpsResourcesManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            List _result = readCustomizedAppOps(_arg0);
                            reply.writeNoException();
                            reply.writeList(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusAppOpsResourcesManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusAppOpsResourcesManager.DESCRIPTOR;
            }

            @Override // android.app.IOplusAppOpsResourcesManager
            public List readCustomizedAppOps(int opcode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusAppOpsResourcesManager.DESCRIPTOR);
                    _data.writeInt(opcode);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
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
