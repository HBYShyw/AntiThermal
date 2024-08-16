package com.oplus.ovoicemanager.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* compiled from: IOVoiceSkillService.java */
/* renamed from: com.oplus.ovoicemanager.service.b, reason: use source file name */
/* loaded from: classes.dex */
public interface IOVoiceSkillService extends IInterface {

    /* compiled from: IOVoiceSkillService.java */
    /* renamed from: com.oplus.ovoicemanager.service.b$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements IOVoiceSkillService {

        /* JADX INFO: Access modifiers changed from: private */
        /* compiled from: IOVoiceSkillService.java */
        /* renamed from: com.oplus.ovoicemanager.service.b$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static class C0020a implements IOVoiceSkillService {

            /* renamed from: b, reason: collision with root package name */
            public static IOVoiceSkillService f9953b;

            /* renamed from: a, reason: collision with root package name */
            private IBinder f9954a;

            C0020a(IBinder iBinder) {
                this.f9954a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f9954a;
            }

            @Override // com.oplus.ovoicemanager.service.IOVoiceSkillService
            public boolean b(String str, String str2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.ovoicemanager.service.IOVoiceSkillService");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (!this.f9954a.transact(5, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().b(str, str2);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.ovoicemanager.service.IOVoiceSkillService
            public boolean j(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.ovoicemanager.service.IOVoiceSkillService");
                    obtain.writeString(str);
                    if (!this.f9954a.transact(6, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().j(str);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.ovoicemanager.service.IOVoiceSkillService
            public boolean k(String str, ISkillListener iSkillListener) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.ovoicemanager.service.IOVoiceSkillService");
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iSkillListener != null ? iSkillListener.asBinder() : null);
                    if (!this.f9954a.transact(1, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().k(str, iSkillListener);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.ovoicemanager.service.IOVoiceSkillService
            public boolean n(String str, int i10, String str2) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.ovoicemanager.service.IOVoiceSkillService");
                    obtain.writeString(str);
                    obtain.writeInt(i10);
                    obtain.writeString(str2);
                    if (!this.f9954a.transact(3, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().n(str, i10, str2);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.oplus.ovoicemanager.service.IOVoiceSkillService
            public boolean u(String str, String str2, ISkillListener iSkillListener) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.oplus.ovoicemanager.service.IOVoiceSkillService");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeStrongBinder(iSkillListener != null ? iSkillListener.asBinder() : null);
                    if (!this.f9954a.transact(4, obtain, obtain2, 0) && a.A() != null) {
                        return a.A().u(str, str2, iSkillListener);
                    }
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IOVoiceSkillService A() {
            return C0020a.f9953b;
        }

        public static IOVoiceSkillService z(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.oplus.ovoicemanager.service.IOVoiceSkillService");
            if (queryLocalInterface != null && (queryLocalInterface instanceof IOVoiceSkillService)) {
                return (IOVoiceSkillService) queryLocalInterface;
            }
            return new C0020a(iBinder);
        }
    }

    boolean b(String str, String str2);

    boolean j(String str);

    boolean k(String str, ISkillListener iSkillListener);

    boolean n(String str, int i10, String str2);

    boolean u(String str, String str2, ISkillListener iSkillListener);
}
