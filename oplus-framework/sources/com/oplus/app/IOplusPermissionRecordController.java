package com.oplus.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusPermissionRecordController extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.IOplusPermissionRecordController";

    void notifyPermissionRecordInfo(String[] strArr, String[] strArr2, long[] jArr, int[] iArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusPermissionRecordController {
        @Override // com.oplus.app.IOplusPermissionRecordController
        public void notifyPermissionRecordInfo(String[] packageNameList, String[] permissionNameList, long[] timeList, int[] resultList) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusPermissionRecordController {
        static final int TRANSACTION_notifyPermissionRecordInfo = 1;

        public Stub() {
            attachInterface(this, IOplusPermissionRecordController.DESCRIPTOR);
        }

        public static IOplusPermissionRecordController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusPermissionRecordController.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusPermissionRecordController)) {
                return (IOplusPermissionRecordController) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "notifyPermissionRecordInfo";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(IOplusPermissionRecordController.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusPermissionRecordController.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String[] _arg0 = data.createStringArray();
                            String[] _arg1 = data.createStringArray();
                            long[] _arg2 = data.createLongArray();
                            int[] _arg3 = data.createIntArray();
                            data.enforceNoDataAvail();
                            notifyPermissionRecordInfo(_arg0, _arg1, _arg2, _arg3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusPermissionRecordController {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusPermissionRecordController.DESCRIPTOR;
            }

            @Override // com.oplus.app.IOplusPermissionRecordController
            public void notifyPermissionRecordInfo(String[] packageNameList, String[] permissionNameList, long[] timeList, int[] resultList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusPermissionRecordController.DESCRIPTOR);
                    _data.writeStringArray(packageNameList);
                    _data.writeStringArray(permissionNameList);
                    _data.writeLongArray(timeList);
                    _data.writeIntArray(resultList);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
