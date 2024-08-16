package com.oplus.ovoicemanager.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* compiled from: IBinderPool.java */
/* renamed from: com.oplus.ovoicemanager.service.a, reason: use source file name */
/* loaded from: classes.dex */
public interface IBinderPool extends IInterface {

    /* compiled from: IBinderPool.java */
    /* renamed from: com.oplus.ovoicemanager.service.a$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements IBinderPool {

        /* JADX INFO: Access modifiers changed from: private */
        /* compiled from: IBinderPool.java */
        /* renamed from: com.oplus.ovoicemanager.service.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static class C0019a implements IBinderPool {

            /* renamed from: b, reason: collision with root package name */
            public static IBinderPool f9951b;

            /* renamed from: a, reason: collision with root package name */
            private IBinder f9952a;

            C0019a(IBinder iBinder) {
                this.f9952a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f9952a;
            }

            @Override // com.oplus.ovoicemanager.service.IBinderPool
            public IBinder g(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.ovoicemanager.service.IBinderPool");
                    obtain.writeString(str);
                    if (!this.f9952a.transact(1, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().g(str);
                    }
                    obtain2.readException();
                    return obtain2.readStrongBinder();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IBinderPool A() {
            return C0019a.f9951b;
        }

        public static IBinderPool z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.oplus.ovoicemanager.service.IBinderPool");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IBinderPool)) {
                return (IBinderPool) queryLocalInterface;
            }
            return new C0019a(iBinder);
        }
    }

    IBinder g(String str);
}
