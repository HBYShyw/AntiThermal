package oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import oplus.os.IDefragTaskListener;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDefrag extends IInterface {
    public static final String DESCRIPTOR = "oplus.os.IDefrag";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IDefrag {
        @Override // oplus.os.IDefrag
        public void abortDefragFiles(IDefragTaskListener iDefragTaskListener) throws RemoteException {
        }

        @Override // oplus.os.IDefrag
        public void abortScan(IDefragTaskListener iDefragTaskListener) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // oplus.os.IDefrag
        public void defragFiles(int i, IDefragTaskListener iDefragTaskListener) throws RemoteException {
        }

        @Override // oplus.os.IDefrag
        public void startScan(IDefragTaskListener iDefragTaskListener) throws RemoteException {
        }
    }

    void abortDefragFiles(IDefragTaskListener iDefragTaskListener) throws RemoteException;

    void abortScan(IDefragTaskListener iDefragTaskListener) throws RemoteException;

    void defragFiles(int i, IDefragTaskListener iDefragTaskListener) throws RemoteException;

    void startScan(IDefragTaskListener iDefragTaskListener) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IDefrag {
        static final int TRANSACTION_abortDefragFiles = 2;
        static final int TRANSACTION_abortScan = 4;
        static final int TRANSACTION_defragFiles = 1;
        static final int TRANSACTION_startScan = 3;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IDefrag.DESCRIPTOR);
        }

        public static IDefrag asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IDefrag.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IDefrag)) {
                return (IDefrag) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IDefrag.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IDefrag.DESCRIPTOR);
                return true;
            }
            if (i == 1) {
                int readInt = parcel.readInt();
                IDefragTaskListener asInterface = IDefragTaskListener.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                defragFiles(readInt, asInterface);
            } else if (i == 2) {
                IDefragTaskListener asInterface2 = IDefragTaskListener.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                abortDefragFiles(asInterface2);
            } else if (i == 3) {
                IDefragTaskListener asInterface3 = IDefragTaskListener.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                startScan(asInterface3);
            } else if (i == 4) {
                IDefragTaskListener asInterface4 = IDefragTaskListener.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                abortScan(asInterface4);
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IDefrag {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IDefrag.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // oplus.os.IDefrag
            public void defragFiles(int i, IDefragTaskListener iDefragTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IDefrag.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iDefragTaskListener);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // oplus.os.IDefrag
            public void abortDefragFiles(IDefragTaskListener iDefragTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IDefrag.DESCRIPTOR);
                    obtain.writeStrongInterface(iDefragTaskListener);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // oplus.os.IDefrag
            public void startScan(IDefragTaskListener iDefragTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IDefrag.DESCRIPTOR);
                    obtain.writeStrongInterface(iDefragTaskListener);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // oplus.os.IDefrag
            public void abortScan(IDefragTaskListener iDefragTaskListener) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IDefrag.DESCRIPTOR);
                    obtain.writeStrongInterface(iDefragTaskListener);
                    this.mRemote.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
