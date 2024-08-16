package u4;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.List;

/* compiled from: IRemoteGuardElfInterface.java */
/* renamed from: u4.a, reason: use source file name */
/* loaded from: classes.dex */
public interface IRemoteGuardElfInterface extends IInterface {

    /* compiled from: IRemoteGuardElfInterface.java */
    /* renamed from: u4.a$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements IRemoteGuardElfInterface {

        /* JADX INFO: Access modifiers changed from: private */
        /* compiled from: IRemoteGuardElfInterface.java */
        /* renamed from: u4.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static class C0107a implements IRemoteGuardElfInterface {

            /* renamed from: b, reason: collision with root package name */
            public static IRemoteGuardElfInterface f18855b;

            /* renamed from: a, reason: collision with root package name */
            private IBinder f18856a;

            C0107a(IBinder iBinder) {
                this.f18856a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f18856a;
            }

            @Override // u4.IRemoteGuardElfInterface
            public void e(String str, int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.athena.policy.battery.IRemoteGuardElfInterface");
                    obtain.writeString(str);
                    obtain.writeInt(i10);
                    if (!this.f18856a.transact(8, obtain, obtain2, 0) && a.A() != null) {
                        a.A().e(str, i10);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // u4.IRemoteGuardElfInterface
            public boolean q(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.athena.policy.battery.IRemoteGuardElfInterface");
                    obtain.writeString(str);
                    if (!this.f18856a.transact(3, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().q(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // u4.IRemoteGuardElfInterface
            public List<String> t(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.athena.policy.battery.IRemoteGuardElfInterface");
                    obtain.writeString(str);
                    if (!this.f18856a.transact(2, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().t(str);
                    }
                    obtain2.readException();
                    return obtain2.createStringArrayList();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // u4.IRemoteGuardElfInterface
            public void x() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.athena.policy.battery.IRemoteGuardElfInterface");
                    if (!this.f18856a.transact(9, obtain, obtain2, 0) && a.A() != null) {
                        a.A().x();
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IRemoteGuardElfInterface A() {
            return C0107a.f18855b;
        }

        public static IRemoteGuardElfInterface z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.oplus.athena.policy.battery.IRemoteGuardElfInterface");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IRemoteGuardElfInterface)) {
                return (IRemoteGuardElfInterface) queryLocalInterface;
            }
            return new C0107a(iBinder);
        }
    }

    void e(String str, int i10);

    boolean q(String str);

    List<String> t(String str);

    void x();
}
