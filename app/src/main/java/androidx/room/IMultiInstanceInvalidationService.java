package androidx.room;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import androidx.room.IMultiInstanceInvalidationCallback;

/* compiled from: IMultiInstanceInvalidationService.java */
/* renamed from: androidx.room.c, reason: use source file name */
/* loaded from: classes.dex */
public interface IMultiInstanceInvalidationService extends IInterface {

    /* compiled from: IMultiInstanceInvalidationService.java */
    /* renamed from: androidx.room.c$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements IMultiInstanceInvalidationService {

        /* compiled from: IMultiInstanceInvalidationService.java */
        /* renamed from: androidx.room.c$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0009a implements IMultiInstanceInvalidationService {

            /* renamed from: a, reason: collision with root package name */
            private IBinder f3843a;

            C0009a(IBinder iBinder) {
                this.f3843a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f3843a;
            }

            @Override // androidx.room.IMultiInstanceInvalidationService
            public int f(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("androidx.room.IMultiInstanceInvalidationService");
                    obtain.writeStrongBinder(iMultiInstanceInvalidationCallback != null ? iMultiInstanceInvalidationCallback.asBinder() : null);
                    obtain.writeString(str);
                    this.f3843a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // androidx.room.IMultiInstanceInvalidationService
            public void s(int i10, String[] strArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("androidx.room.IMultiInstanceInvalidationService");
                    obtain.writeInt(i10);
                    obtain.writeStringArray(strArr);
                    this.f3843a.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // androidx.room.IMultiInstanceInvalidationService
            public void v(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("androidx.room.IMultiInstanceInvalidationService");
                    obtain.writeStrongBinder(iMultiInstanceInvalidationCallback != null ? iMultiInstanceInvalidationCallback.asBinder() : null);
                    obtain.writeInt(i10);
                    this.f3843a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "androidx.room.IMultiInstanceInvalidationService");
        }

        public static IMultiInstanceInvalidationService z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("androidx.room.IMultiInstanceInvalidationService");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IMultiInstanceInvalidationService)) {
                return (IMultiInstanceInvalidationService) queryLocalInterface;
            }
            return new C0009a(iBinder);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
            if (i10 == 1) {
                parcel.enforceInterface("androidx.room.IMultiInstanceInvalidationService");
                int f10 = f(IMultiInstanceInvalidationCallback.a.z(parcel.readStrongBinder()), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeInt(f10);
                return true;
            }
            if (i10 == 2) {
                parcel.enforceInterface("androidx.room.IMultiInstanceInvalidationService");
                v(IMultiInstanceInvalidationCallback.a.z(parcel.readStrongBinder()), parcel.readInt());
                parcel2.writeNoException();
                return true;
            }
            if (i10 == 3) {
                parcel.enforceInterface("androidx.room.IMultiInstanceInvalidationService");
                s(parcel.readInt(), parcel.createStringArray());
                return true;
            }
            if (i10 != 1598968902) {
                return super.onTransact(i10, parcel, parcel2, i11);
            }
            parcel2.writeString("androidx.room.IMultiInstanceInvalidationService");
            return true;
        }
    }

    int f(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, String str);

    void s(int i10, String[] strArr);

    void v(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, int i10);
}
