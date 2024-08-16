package m7;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.pantanal.oassist.base.OAssistInput;
import com.oplus.pantanal.oassist.base.OAssistOutput;

/* compiled from: OAssistContract.java */
/* renamed from: m7.a, reason: use source file name */
/* loaded from: classes.dex */
public interface OAssistContract extends IInterface {

    /* compiled from: OAssistContract.java */
    /* renamed from: m7.a$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements OAssistContract {

        /* compiled from: OAssistContract.java */
        /* renamed from: m7.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0074a implements OAssistContract {

            /* renamed from: a, reason: collision with root package name */
            private IBinder f14931a;

            C0074a(IBinder iBinder) {
                this.f14931a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f14931a;
            }

            @Override // m7.OAssistContract
            public OAssistOutput i(OAssistInput oAssistInput) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.pantanal.oassist.base.OAssistContract");
                    b.d(obtain, oAssistInput, 0);
                    this.f14931a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return (OAssistOutput) b.c(obtain2, OAssistOutput.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "com.oplus.pantanal.oassist.base.OAssistContract");
        }

        public static OAssistContract z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.oplus.pantanal.oassist.base.OAssistContract");
            if (queryLocalInterface != null && (queryLocalInterface instanceof OAssistContract)) {
                return (OAssistContract) queryLocalInterface;
            }
            return new C0074a(iBinder);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
            if (i10 >= 1 && i10 <= 16777215) {
                parcel.enforceInterface("com.oplus.pantanal.oassist.base.OAssistContract");
            }
            if (i10 == 1598968902) {
                parcel2.writeString("com.oplus.pantanal.oassist.base.OAssistContract");
                return true;
            }
            if (i10 == 1) {
                int m10 = m((OAssistInput) b.c(parcel, OAssistInput.CREATOR));
                parcel2.writeNoException();
                parcel2.writeInt(m10);
            } else {
                if (i10 != 2) {
                    return super.onTransact(i10, parcel, parcel2, i11);
                }
                OAssistOutput i12 = i((OAssistInput) b.c(parcel, OAssistInput.CREATOR));
                parcel2.writeNoException();
                b.d(parcel2, i12, 1);
            }
            return true;
        }
    }

    /* compiled from: OAssistContract.java */
    /* renamed from: m7.a$b */
    /* loaded from: classes.dex */
    public static class b {
        /* JADX INFO: Access modifiers changed from: private */
        public static <T> T c(Parcel parcel, Parcelable.Creator<T> creator) {
            if (parcel.readInt() != 0) {
                return creator.createFromParcel(parcel);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static <T extends Parcelable> void d(Parcel parcel, T t7, int i10) {
            if (t7 != null) {
                parcel.writeInt(1);
                t7.writeToParcel(parcel, i10);
            } else {
                parcel.writeInt(0);
            }
        }
    }

    OAssistOutput i(OAssistInput oAssistInput);

    int m(OAssistInput oAssistInput);
}
