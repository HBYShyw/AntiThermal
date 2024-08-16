package com.oplus.remote.policy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* compiled from: IRemoteClientCallback.java */
/* renamed from: com.oplus.remote.policy.a, reason: use source file name */
/* loaded from: classes2.dex */
public interface IRemoteClientCallback extends IInterface {

    /* compiled from: IRemoteClientCallback.java */
    /* renamed from: com.oplus.remote.policy.a$a */
    /* loaded from: classes2.dex */
    public static abstract class a extends Binder implements IRemoteClientCallback {

        /* JADX INFO: Access modifiers changed from: private */
        /* compiled from: IRemoteClientCallback.java */
        /* renamed from: com.oplus.remote.policy.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static class C0028a implements IRemoteClientCallback {

            /* renamed from: b, reason: collision with root package name */
            public static IRemoteClientCallback f10464b;

            /* renamed from: a, reason: collision with root package name */
            private IBinder f10465a;

            C0028a(IBinder iBinder) {
                this.f10465a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f10465a;
            }

            @Override // com.oplus.remote.policy.IRemoteClientCallback
            public void d(int i10) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.remote.policy.IRemoteClientCallback");
                    obtain.writeInt(i10);
                    if (!this.f10465a.transact(1, obtain, obtain2, 0) && a.A() != null) {
                        a.A().d(i10);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IRemoteClientCallback A() {
            return C0028a.f10464b;
        }

        public static IRemoteClientCallback z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.oplus.remote.policy.IRemoteClientCallback");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IRemoteClientCallback)) {
                return (IRemoteClientCallback) queryLocalInterface;
            }
            return new C0028a(iBinder);
        }
    }

    void d(int i10);
}
