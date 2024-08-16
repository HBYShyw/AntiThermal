package com.oplus.deepthinker.sdk.app.aidl.eventfountain;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes.dex */
public interface IEventQueryListener extends IInterface {

    /* loaded from: classes.dex */
    public static class Default implements IEventQueryListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
        public void onFailure(int i10) {
        }

        @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
        public void onSuccess(DeviceEventResult deviceEventResult) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IEventQueryListener {
        private static final String DESCRIPTOR = "com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener";
        static final int TRANSACTION_onFailure = 2;
        static final int TRANSACTION_onSuccess = 1;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IEventQueryListener {
            public static IEventQueryListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
            public void onFailure(int i10) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i10);
                    if (this.mRemote.transact(2, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onFailure(i10);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener
            public void onSuccess(DeviceEventResult deviceEventResult) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (deviceEventResult != null) {
                        obtain.writeInt(1);
                        deviceEventResult.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, null, 1) || Stub.getDefaultImpl() == null) {
                        return;
                    }
                    Stub.getDefaultImpl().onSuccess(deviceEventResult);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEventQueryListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IEventQueryListener)) {
                return (IEventQueryListener) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public static IEventQueryListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IEventQueryListener iEventQueryListener) {
            if (Proxy.sDefaultImpl != null || iEventQueryListener == null) {
                return false;
            }
            Proxy.sDefaultImpl = iEventQueryListener;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
            if (i10 == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onSuccess(parcel.readInt() != 0 ? DeviceEventResult.CREATOR.createFromParcel(parcel) : null);
                return true;
            }
            if (i10 == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onFailure(parcel.readInt());
                return true;
            }
            if (i10 != 1598968902) {
                return super.onTransact(i10, parcel, parcel2, i11);
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }
    }

    void onFailure(int i10);

    void onSuccess(DeviceEventResult deviceEventResult);
}
