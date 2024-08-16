package o;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

/* compiled from: IUnusedAppRestrictionsBackportCallback.java */
/* renamed from: o.a, reason: use source file name */
/* loaded from: classes.dex */
public interface IUnusedAppRestrictionsBackportCallback extends IInterface {

    /* compiled from: IUnusedAppRestrictionsBackportCallback.java */
    /* renamed from: o.a$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements IUnusedAppRestrictionsBackportCallback {

        /* compiled from: IUnusedAppRestrictionsBackportCallback.java */
        /* renamed from: o.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0085a implements IUnusedAppRestrictionsBackportCallback {

            /* renamed from: a, reason: collision with root package name */
            private IBinder f16091a;

            C0085a(IBinder iBinder) {
                this.f16091a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f16091a;
            }
        }

        public static IUnusedAppRestrictionsBackportCallback z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IUnusedAppRestrictionsBackportCallback)) {
                return (IUnusedAppRestrictionsBackportCallback) queryLocalInterface;
            }
            return new C0085a(iBinder);
        }
    }
}
