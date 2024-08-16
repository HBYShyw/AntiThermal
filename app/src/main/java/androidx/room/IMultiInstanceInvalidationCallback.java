package androidx.room;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* compiled from: IMultiInstanceInvalidationCallback.java */
/* renamed from: androidx.room.b, reason: use source file name */
/* loaded from: classes.dex */
public interface IMultiInstanceInvalidationCallback extends IInterface {

    /* compiled from: IMultiInstanceInvalidationCallback.java */
    /* renamed from: androidx.room.b$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements IMultiInstanceInvalidationCallback {

        /* compiled from: IMultiInstanceInvalidationCallback.java */
        /* renamed from: androidx.room.b$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0008a implements IMultiInstanceInvalidationCallback {

            /* renamed from: a, reason: collision with root package name */
            private IBinder f3842a;

            C0008a(IBinder iBinder) {
                this.f3842a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f3842a;
            }

            @Override // androidx.room.IMultiInstanceInvalidationCallback
            public void c(String[] strArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("androidx.room.IMultiInstanceInvalidationCallback");
                    obtain.writeStringArray(strArr);
                    this.f3842a.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "androidx.room.IMultiInstanceInvalidationCallback");
        }

        public static IMultiInstanceInvalidationCallback z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("androidx.room.IMultiInstanceInvalidationCallback");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IMultiInstanceInvalidationCallback)) {
                return (IMultiInstanceInvalidationCallback) queryLocalInterface;
            }
            return new C0008a(iBinder);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
            if (i10 == 1) {
                parcel.enforceInterface("androidx.room.IMultiInstanceInvalidationCallback");
                c(parcel.createStringArray());
                return true;
            }
            if (i10 != 1598968902) {
                return super.onTransact(i10, parcel, parcel2, i11);
            }
            parcel2.writeString("androidx.room.IMultiInstanceInvalidationCallback");
            return true;
        }
    }

    void c(String[] strArr);
}
