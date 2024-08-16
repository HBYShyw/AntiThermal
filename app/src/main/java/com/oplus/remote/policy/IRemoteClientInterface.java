package com.oplus.remote.policy;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.oplus.remote.policy.IRemoteClientCallback;

/* compiled from: IRemoteClientInterface.java */
/* renamed from: com.oplus.remote.policy.b, reason: use source file name */
/* loaded from: classes2.dex */
public interface IRemoteClientInterface extends IInterface {

    /* compiled from: IRemoteClientInterface.java */
    /* renamed from: com.oplus.remote.policy.b$a */
    /* loaded from: classes2.dex */
    public static abstract class a extends Binder implements IRemoteClientInterface {
        public a() {
            attachInterface(this, "com.oplus.remote.policy.IRemoteClientInterface");
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
                        parcel.enforceInterface("com.oplus.remote.policy.IRemoteClientInterface");
                        int w10 = w();
                        parcel2.writeNoException();
                        parcel2.writeInt(w10);
                        return true;
                    case 2:
                        parcel.enforceInterface("com.oplus.remote.policy.IRemoteClientInterface");
                        long y4 = y();
                        parcel2.writeNoException();
                        parcel2.writeLong(y4);
                        return true;
                    case 3:
                        parcel.enforceInterface("com.oplus.remote.policy.IRemoteClientInterface");
                        a(IRemoteClientCallback.a.z(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface("com.oplus.remote.policy.IRemoteClientInterface");
                        r(IRemoteClientCallback.a.z(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface("com.oplus.remote.policy.IRemoteClientInterface");
                        int h10 = h();
                        parcel2.writeNoException();
                        parcel2.writeInt(h10);
                        return true;
                    case 6:
                        parcel.enforceInterface("com.oplus.remote.policy.IRemoteClientInterface");
                        Bundle p10 = p();
                        parcel2.writeNoException();
                        if (p10 != null) {
                            parcel2.writeInt(1);
                            p10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        parcel.enforceInterface("com.oplus.remote.policy.IRemoteClientInterface");
                        int o10 = o();
                        parcel2.writeNoException();
                        parcel2.writeInt(o10);
                        return true;
                    default:
                        return super.onTransact(i10, parcel, parcel2, i11);
                }
            }
            parcel2.writeString("com.oplus.remote.policy.IRemoteClientInterface");
            return true;
        }
    }

    void a(IRemoteClientCallback iRemoteClientCallback);

    int h();

    int o();

    Bundle p();

    void r(IRemoteClientCallback iRemoteClientCallback);

    int w();

    long y();
}
