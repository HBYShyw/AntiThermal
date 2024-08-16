package o;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import o.IUnusedAppRestrictionsBackportCallback;

/* compiled from: IUnusedAppRestrictionsBackportService.java */
/* renamed from: o.b, reason: use source file name */
/* loaded from: classes.dex */
public interface IUnusedAppRestrictionsBackportService extends IInterface {

    /* compiled from: IUnusedAppRestrictionsBackportService.java */
    /* renamed from: o.b$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements IUnusedAppRestrictionsBackportService {
        public a() {
            attachInterface(this, "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
            if (i10 >= 1 && i10 <= 16777215) {
                parcel.enforceInterface("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
            }
            if (i10 == 1598968902) {
                parcel2.writeString("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService");
                return true;
            }
            if (i10 != 1) {
                return super.onTransact(i10, parcel, parcel2, i11);
            }
            l(IUnusedAppRestrictionsBackportCallback.a.z(parcel.readStrongBinder()));
            return true;
        }
    }

    void l(IUnusedAppRestrictionsBackportCallback iUnusedAppRestrictionsBackportCallback);
}
