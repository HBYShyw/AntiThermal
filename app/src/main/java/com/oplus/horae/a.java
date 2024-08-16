package com.oplus.horae;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* compiled from: IThermalStatusListener.java */
/* loaded from: classes.dex */
public interface a extends IInterface {

    /* compiled from: IThermalStatusListener.java */
    /* renamed from: com.oplus.horae.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static abstract class AbstractBinderC0018a extends Binder implements a {
        private static final String DESCRIPTOR = "com.oplus.thermalcontrol.IThermalStatusListener";
        static final int TRANSACTION_NOTIFY_COMPONENT_HEAT = 6;
        static final int TRANSACTION_NOTIFY_TSENSOR_TEMP = 7;
        static final int TRANSACTION_empty1 = 1;
        static final int TRANSACTION_empty2 = 2;
        static final int TRANSACTION_notifyAmbientThermal = 5;
        static final int TRANSACTION_notifyThermalBroadCast = 4;
        static final int TRANSACTION_notifyThermalStatus = 3;

        public AbstractBinderC0018a() {
            attachInterface(this, "com.oplus.thermalcontrol.IThermalStatusListener");
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
                        parcel.enforceInterface("com.oplus.thermalcontrol.IThermalStatusListener");
                        empty1();
                        parcel2.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface("com.oplus.thermalcontrol.IThermalStatusListener");
                        empty2();
                        parcel2.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface("com.oplus.thermalcontrol.IThermalStatusListener");
                        notifyThermalStatus(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface("com.oplus.thermalcontrol.IThermalStatusListener");
                        notifyThermalBroadCast(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface("com.oplus.thermalcontrol.IThermalStatusListener");
                        notifyAmbientThermal(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface("com.oplus.thermalcontrol.IThermalStatusListener");
                        notifyThermalSource(parcel.readInt(), parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface("com.oplus.thermalcontrol.IThermalStatusListener");
                        notifyTsensorTemp(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    default:
                        return super.onTransact(i10, parcel, parcel2, i11);
                }
            }
            parcel2.writeString("com.oplus.thermalcontrol.IThermalStatusListener");
            return true;
        }
    }

    void empty1();

    void empty2();

    void notifyAmbientThermal(int i10);

    void notifyThermalBroadCast(int i10, int i11);

    void notifyThermalSource(int i10, int i11, String str);

    void notifyThermalStatus(int i10);

    void notifyTsensorTemp(int i10);
}
