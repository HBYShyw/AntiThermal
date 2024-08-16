package com.oplus.thermalcontrol;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes2.dex */
public interface IThermalStatusListener extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.thermalcontrol.IThermalStatusListener";

    /* loaded from: classes2.dex */
    public static class Default implements IThermalStatusListener {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.oplus.thermalcontrol.IThermalStatusListener
        public void empty1() {
        }

        @Override // com.oplus.thermalcontrol.IThermalStatusListener
        public void empty2() {
        }

        @Override // com.oplus.thermalcontrol.IThermalStatusListener
        public void notifyThermalBroadCast(int i10, int i11) {
        }

        @Override // com.oplus.thermalcontrol.IThermalStatusListener
        public void notifyThermalSource(int i10, int i11, String str) {
        }

        @Override // com.oplus.thermalcontrol.IThermalStatusListener
        public void notifyThermalStatus(int i10) {
        }

        @Override // com.oplus.thermalcontrol.IThermalStatusListener
        public void notifyTsensorTemp(int i10) {
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IThermalStatusListener {
        static final int TRANSACTION_empty1 = 1;
        static final int TRANSACTION_empty2 = 2;
        static final int TRANSACTION_notifyThermalBroadCast = 4;
        static final int TRANSACTION_notifyThermalSource = 5;
        static final int TRANSACTION_notifyThermalStatus = 3;
        static final int TRANSACTION_notifyTsensorTemp = 6;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IThermalStatusListener {
            public static IThermalStatusListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.oplus.thermalcontrol.IThermalStatusListener
            public void empty1() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IThermalStatusListener.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().empty1();
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.thermalcontrol.IThermalStatusListener
            public void empty2() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IThermalStatusListener.DESCRIPTOR);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().empty2();
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return IThermalStatusListener.DESCRIPTOR;
            }

            @Override // com.oplus.thermalcontrol.IThermalStatusListener
            public void notifyThermalBroadCast(int i10, int i11) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IThermalStatusListener.DESCRIPTOR);
                    obtain.writeInt(i10);
                    obtain.writeInt(i11);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyThermalBroadCast(i10, i11);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.thermalcontrol.IThermalStatusListener
            public void notifyThermalSource(int i10, int i11, String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IThermalStatusListener.DESCRIPTOR);
                    obtain.writeInt(i10);
                    obtain.writeInt(i11);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyThermalSource(i10, i11, str);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.thermalcontrol.IThermalStatusListener
            public void notifyThermalStatus(int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IThermalStatusListener.DESCRIPTOR);
                    obtain.writeInt(i10);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyThermalStatus(i10);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.thermalcontrol.IThermalStatusListener
            public void notifyTsensorTemp(int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IThermalStatusListener.DESCRIPTOR);
                    obtain.writeInt(i10);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyTsensorTemp(i10);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, IThermalStatusListener.DESCRIPTOR);
        }

        public static IThermalStatusListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IThermalStatusListener.DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IThermalStatusListener)) {
                return (IThermalStatusListener) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        public static IThermalStatusListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IThermalStatusListener iThermalStatusListener) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (iThermalStatusListener == null) {
                return false;
            }
            Proxy.sDefaultImpl = iThermalStatusListener;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
            if (i10 != 1598968902) {
                switch (i10) {
                    case 1:
                        parcel.enforceInterface(IThermalStatusListener.DESCRIPTOR);
                        empty1();
                        parcel2.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(IThermalStatusListener.DESCRIPTOR);
                        empty2();
                        parcel2.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(IThermalStatusListener.DESCRIPTOR);
                        notifyThermalStatus(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(IThermalStatusListener.DESCRIPTOR);
                        notifyThermalBroadCast(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(IThermalStatusListener.DESCRIPTOR);
                        notifyThermalSource(parcel.readInt(), parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(IThermalStatusListener.DESCRIPTOR);
                        notifyTsensorTemp(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    default:
                        return super.onTransact(i10, parcel, parcel2, i11);
                }
            }
            parcel2.writeString(IThermalStatusListener.DESCRIPTOR);
            return true;
        }
    }

    void empty1();

    void empty2();

    void notifyThermalBroadCast(int i10, int i11);

    void notifyThermalSource(int i10, int i11, String str);

    void notifyThermalStatus(int i10);

    void notifyTsensorTemp(int i10);
}
