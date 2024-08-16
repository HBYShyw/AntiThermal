package oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IDefragTaskListener extends IInterface {
    public static final String DESCRIPTOR = "oplus.os.IDefragTaskListener";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Default implements IDefragTaskListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // oplus.os.IDefragTaskListener
        public void onFinished(int i, PersistableBundle persistableBundle) throws RemoteException {
        }

        @Override // oplus.os.IDefragTaskListener
        public void onStatus(int i, PersistableBundle persistableBundle) throws RemoteException {
        }
    }

    void onFinished(int i, PersistableBundle persistableBundle) throws RemoteException;

    void onStatus(int i, PersistableBundle persistableBundle) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static abstract class Stub extends Binder implements IDefragTaskListener {
        static final int TRANSACTION_onFinished = 2;
        static final int TRANSACTION_onStatus = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IDefragTaskListener.DESCRIPTOR);
        }

        public static IDefragTaskListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IDefragTaskListener.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IDefragTaskListener)) {
                return (IDefragTaskListener) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IDefragTaskListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IDefragTaskListener.DESCRIPTOR);
                return true;
            }
            if (i == 1) {
                int readInt = parcel.readInt();
                PersistableBundle persistableBundle = (PersistableBundle) parcel.readTypedObject(PersistableBundle.CREATOR);
                parcel.enforceNoDataAvail();
                onStatus(readInt, persistableBundle);
            } else if (i == 2) {
                int readInt2 = parcel.readInt();
                PersistableBundle persistableBundle2 = (PersistableBundle) parcel.readTypedObject(PersistableBundle.CREATOR);
                parcel.enforceNoDataAvail();
                onFinished(readInt2, persistableBundle2);
            } else {
                return super.onTransact(i, parcel, parcel2, i2);
            }
            return true;
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
        private static class Proxy implements IDefragTaskListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IDefragTaskListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // oplus.os.IDefragTaskListener
            public void onStatus(int i, PersistableBundle persistableBundle) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IDefragTaskListener.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(persistableBundle, 0);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // oplus.os.IDefragTaskListener
            public void onFinished(int i, PersistableBundle persistableBundle) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                try {
                    obtain.writeInterfaceToken(IDefragTaskListener.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(persistableBundle, 0);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
