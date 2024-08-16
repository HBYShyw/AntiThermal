package com.android.server.profcollect;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.server.profcollect.IProviderStatusCallback;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IProfCollectd extends IInterface {
    public static final String DESCRIPTOR = "com.android.server.profcollect.IProfCollectd";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Default implements IProfCollectd {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public String get_supported_provider() throws RemoteException {
            return null;
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void process() throws RemoteException {
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void registerProviderStatusCallback(IProviderStatusCallback iProviderStatusCallback) throws RemoteException {
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public String report(int i) throws RemoteException {
            return null;
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void schedule() throws RemoteException {
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void terminate() throws RemoteException {
        }

        @Override // com.android.server.profcollect.IProfCollectd
        public void trace_once(String str) throws RemoteException {
        }
    }

    String get_supported_provider() throws RemoteException;

    void process() throws RemoteException;

    void registerProviderStatusCallback(IProviderStatusCallback iProviderStatusCallback) throws RemoteException;

    String report(int i) throws RemoteException;

    void schedule() throws RemoteException;

    void terminate() throws RemoteException;

    void trace_once(String str) throws RemoteException;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static abstract class Stub extends Binder implements IProfCollectd {
        static final int TRANSACTION_get_supported_provider = 6;
        static final int TRANSACTION_process = 4;
        static final int TRANSACTION_registerProviderStatusCallback = 7;
        static final int TRANSACTION_report = 5;
        static final int TRANSACTION_schedule = 1;
        static final int TRANSACTION_terminate = 2;
        static final int TRANSACTION_trace_once = 3;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IProfCollectd.DESCRIPTOR);
        }

        public static IProfCollectd asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IProfCollectd.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IProfCollectd)) {
                return (IProfCollectd) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IProfCollectd.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IProfCollectd.DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    schedule();
                    parcel2.writeNoException();
                    return true;
                case 2:
                    terminate();
                    parcel2.writeNoException();
                    return true;
                case 3:
                    String readString = parcel.readString();
                    parcel.enforceNoDataAvail();
                    trace_once(readString);
                    parcel2.writeNoException();
                    return true;
                case 4:
                    process();
                    parcel2.writeNoException();
                    return true;
                case 5:
                    int readInt = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    String report = report(readInt);
                    parcel2.writeNoException();
                    parcel2.writeString(report);
                    return true;
                case 6:
                    String str = get_supported_provider();
                    parcel2.writeNoException();
                    parcel2.writeString(str);
                    return true;
                case 7:
                    IProviderStatusCallback asInterface = IProviderStatusCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    registerProviderStatusCallback(asInterface);
                    parcel2.writeNoException();
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        private static class Proxy implements IProfCollectd {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IProfCollectd.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void schedule() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IProfCollectd.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void terminate() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IProfCollectd.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void trace_once(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IProfCollectd.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void process() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IProfCollectd.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public String report(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IProfCollectd.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public String get_supported_provider() throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IProfCollectd.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.server.profcollect.IProfCollectd
            public void registerProviderStatusCallback(IProviderStatusCallback iProviderStatusCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain(asBinder());
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IProfCollectd.DESCRIPTOR);
                    obtain.writeStrongInterface(iProviderStatusCallback);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
