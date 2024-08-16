package com.android.internal.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusBatteryStatsService extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.os.IOplusBatteryStatsService";

    UidSipper[] getUidSipper(int[] iArr, long j, int i, boolean z, boolean z2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusBatteryStatsService {
        @Override // com.android.internal.os.IOplusBatteryStatsService
        public UidSipper[] getUidSipper(int[] uid, long elapsedRealtimeMs, int which, boolean updateCpu, boolean updateModem) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusBatteryStatsService {
        static final int TRANSACTION_getUidSipper = 1;

        public Stub() {
            attachInterface(this, IOplusBatteryStatsService.DESCRIPTOR);
        }

        public static IOplusBatteryStatsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusBatteryStatsService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusBatteryStatsService)) {
                return (IOplusBatteryStatsService) iin;
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
                    return "getUidSipper";
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
                data.enforceInterface(IOplusBatteryStatsService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusBatteryStatsService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int[] _arg0 = data.createIntArray();
                            long _arg1 = data.readLong();
                            int _arg2 = data.readInt();
                            boolean _arg3 = data.readBoolean();
                            boolean _arg4 = data.readBoolean();
                            data.enforceNoDataAvail();
                            UidSipper[] _result = getUidSipper(_arg0, _arg1, _arg2, _arg3, _arg4);
                            reply.writeNoException();
                            reply.writeTypedArray(_result, 1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusBatteryStatsService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusBatteryStatsService.DESCRIPTOR;
            }

            @Override // com.android.internal.os.IOplusBatteryStatsService
            public UidSipper[] getUidSipper(int[] uid, long elapsedRealtimeMs, int which, boolean updateCpu, boolean updateModem) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusBatteryStatsService.DESCRIPTOR);
                    _data.writeIntArray(uid);
                    _data.writeLong(elapsedRealtimeMs);
                    _data.writeInt(which);
                    _data.writeBoolean(updateCpu);
                    _data.writeBoolean(updateModem);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    UidSipper[] _result = (UidSipper[]) _reply.createTypedArray(UidSipper.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 0;
        }
    }
}
