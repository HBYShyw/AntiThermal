package com.oplus.ovoicemanager.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.oplus.ovoicemanager.service.IOVoiceSkillService;

/* compiled from: ISkillListener.java */
/* renamed from: com.oplus.ovoicemanager.service.c, reason: use source file name */
/* loaded from: classes.dex */
public interface ISkillListener extends IInterface {

    /* compiled from: ISkillListener.java */
    /* renamed from: com.oplus.ovoicemanager.service.c$a */
    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements ISkillListener {
        private static final String DESCRIPTOR = "com.oplus.ovoicemanager.service.ISkillListener";
        static final int TRANSACTION_onActionExecution = 2;
        static final int TRANSACTION_onCancel = 1;
        static final int TRANSACTION_onValueChanged = 3;
        static final int TRANSACTION_startAction = 4;

        /* compiled from: ISkillListener.java */
        /* renamed from: com.oplus.ovoicemanager.service.c$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0021a implements ISkillListener {

            /* renamed from: b, reason: collision with root package name */
            public static ISkillListener f9955b;

            /* renamed from: a, reason: collision with root package name */
            private IBinder f9956a;

            C0021a(IBinder iBinder) {
                this.f9956a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f9956a;
            }
        }

        public a() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISkillListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof ISkillListener)) {
                return (ISkillListener) queryLocalInterface;
            }
            return new C0021a(iBinder);
        }

        public static ISkillListener getDefaultImpl() {
            return C0021a.f9955b;
        }

        public static boolean setDefaultImpl(ISkillListener iSkillListener) {
            if (C0021a.f9955b != null || iSkillListener == null) {
                return false;
            }
            C0021a.f9955b = iSkillListener;
            return true;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i10, Parcel parcel, Parcel parcel2, int i11) {
            if (i10 == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onCancel(parcel.readString());
                parcel2.writeNoException();
                return true;
            }
            if (i10 == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onActionExecution(parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                return true;
            }
            if (i10 == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                onValueChanged(parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                return true;
            }
            if (i10 != 4) {
                if (i10 != 1598968902) {
                    return super.onTransact(i10, parcel, parcel2, i11);
                }
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            parcel.enforceInterface(DESCRIPTOR);
            startAction(parcel.readInt() != 0 ? ActionRequest.CREATOR.createFromParcel(parcel) : null, IOVoiceSkillService.a.z(parcel.readStrongBinder()));
            parcel2.writeNoException();
            return true;
        }
    }

    void onActionExecution(String str, String str2, String str3);

    void onCancel(String str);

    void onValueChanged(String str, String str2);

    void startAction(ActionRequest actionRequest, IOVoiceSkillService iOVoiceSkillService);
}
