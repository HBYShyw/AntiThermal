package com.oplus.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.app.IOplusFreezeInfoListener;
import com.oplus.app.IOplusProtectConnection;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusHansFreezeManager extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.app.IOplusHansFreezeManager";

    void cancelFrozenDelay(int i) throws RemoteException;

    List<OplusFreezeInfo> getFreezeInfo(String str) throws RemoteException;

    long getFrozenDelayTime(int i) throws RemoteException;

    List<String> getFrozenPackageList() throws RemoteException;

    boolean registerFreezeInfoListener(String str, IOplusFreezeInfoListener iOplusFreezeInfoListener) throws RemoteException;

    void report(int i, int i2, String str, String str2) throws RemoteException;

    void requestFrozenDelay(int i, String str, long j, String str2, IOplusProtectConnection iOplusProtectConnection) throws RemoteException;

    boolean unregisterFreezeInfoListener(String str, IOplusFreezeInfoListener iOplusFreezeInfoListener) throws RemoteException;

    boolean updateAppCardProvider(String str, List<OplusAppCardProviderInfo> list, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusHansFreezeManager {
        @Override // com.oplus.app.IOplusHansFreezeManager
        public boolean registerFreezeInfoListener(String callerPkg, IOplusFreezeInfoListener listener) throws RemoteException {
            return false;
        }

        @Override // com.oplus.app.IOplusHansFreezeManager
        public boolean unregisterFreezeInfoListener(String callerPkg, IOplusFreezeInfoListener listener) throws RemoteException {
            return false;
        }

        @Override // com.oplus.app.IOplusHansFreezeManager
        public List<OplusFreezeInfo> getFreezeInfo(String callerPkg) throws RemoteException {
            return null;
        }

        @Override // com.oplus.app.IOplusHansFreezeManager
        public List<String> getFrozenPackageList() throws RemoteException {
            return null;
        }

        @Override // com.oplus.app.IOplusHansFreezeManager
        public void report(int status, int pid, String installStage, String installerPkgName) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusHansFreezeManager
        public void requestFrozenDelay(int uid, String packageName, long timeout, String reason, IOplusProtectConnection protectConnection) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusHansFreezeManager
        public void cancelFrozenDelay(int uid) throws RemoteException {
        }

        @Override // com.oplus.app.IOplusHansFreezeManager
        public long getFrozenDelayTime(int uid) throws RemoteException {
            return 0L;
        }

        @Override // com.oplus.app.IOplusHansFreezeManager
        public boolean updateAppCardProvider(String callerPackageName, List<OplusAppCardProviderInfo> visibleAppCardList, int requestId) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusHansFreezeManager {
        static final int TRANSACTION_cancelFrozenDelay = 7;
        static final int TRANSACTION_getFreezeInfo = 3;
        static final int TRANSACTION_getFrozenDelayTime = 8;
        static final int TRANSACTION_getFrozenPackageList = 4;
        static final int TRANSACTION_registerFreezeInfoListener = 1;
        static final int TRANSACTION_report = 5;
        static final int TRANSACTION_requestFrozenDelay = 6;
        static final int TRANSACTION_unregisterFreezeInfoListener = 2;
        static final int TRANSACTION_updateAppCardProvider = 9;

        public Stub() {
            attachInterface(this, IOplusHansFreezeManager.DESCRIPTOR);
        }

        public static IOplusHansFreezeManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusHansFreezeManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusHansFreezeManager)) {
                return (IOplusHansFreezeManager) iin;
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
                    return "registerFreezeInfoListener";
                case 2:
                    return "unregisterFreezeInfoListener";
                case 3:
                    return "getFreezeInfo";
                case 4:
                    return "getFrozenPackageList";
                case 5:
                    return "report";
                case 6:
                    return "requestFrozenDelay";
                case 7:
                    return "cancelFrozenDelay";
                case 8:
                    return "getFrozenDelayTime";
                case 9:
                    return "updateAppCardProvider";
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
                data.enforceInterface(IOplusHansFreezeManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusHansFreezeManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            IOplusFreezeInfoListener _arg1 = IOplusFreezeInfoListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result = registerFreezeInfoListener(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            IOplusFreezeInfoListener _arg12 = IOplusFreezeInfoListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result2 = unregisterFreezeInfoListener(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            List<OplusFreezeInfo> _result3 = getFreezeInfo(_arg03);
                            reply.writeNoException();
                            reply.writeTypedList(_result3, 1);
                            return true;
                        case 4:
                            List<String> _result4 = getFrozenPackageList();
                            reply.writeNoException();
                            reply.writeStringList(_result4);
                            return true;
                        case 5:
                            int _arg04 = data.readInt();
                            int _arg13 = data.readInt();
                            String _arg2 = data.readString();
                            String _arg3 = data.readString();
                            data.enforceNoDataAvail();
                            report(_arg04, _arg13, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        case 6:
                            int _arg05 = data.readInt();
                            String _arg14 = data.readString();
                            long _arg22 = data.readLong();
                            String _arg32 = data.readString();
                            IOplusProtectConnection _arg4 = IOplusProtectConnection.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            requestFrozenDelay(_arg05, _arg14, _arg22, _arg32, _arg4);
                            reply.writeNoException();
                            return true;
                        case 7:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            cancelFrozenDelay(_arg06);
                            reply.writeNoException();
                            return true;
                        case 8:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            long _result5 = getFrozenDelayTime(_arg07);
                            reply.writeNoException();
                            reply.writeLong(_result5);
                            return true;
                        case 9:
                            String _arg08 = data.readString();
                            List<OplusAppCardProviderInfo> _arg15 = data.createTypedArrayList(OplusAppCardProviderInfo.CREATOR);
                            int _arg23 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result6 = updateAppCardProvider(_arg08, _arg15, _arg23);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusHansFreezeManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusHansFreezeManager.DESCRIPTOR;
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public boolean registerFreezeInfoListener(String callerPkg, IOplusFreezeInfoListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public boolean unregisterFreezeInfoListener(String callerPkg, IOplusFreezeInfoListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public List<OplusFreezeInfo> getFreezeInfo(String callerPkg) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    _data.writeString(callerPkg);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    List<OplusFreezeInfo> _result = _reply.createTypedArrayList(OplusFreezeInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public List<String> getFrozenPackageList() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public void report(int status, int pid, String installStage, String installerPkgName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(pid);
                    _data.writeString(installStage);
                    _data.writeString(installerPkgName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public void requestFrozenDelay(int uid, String packageName, long timeout, String reason, IOplusProtectConnection protectConnection) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeLong(timeout);
                    _data.writeString(reason);
                    _data.writeStrongInterface(protectConnection);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public void cancelFrozenDelay(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public long getFrozenDelayTime(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.app.IOplusHansFreezeManager
            public boolean updateAppCardProvider(String callerPackageName, List<OplusAppCardProviderInfo> visibleAppCardList, int requestId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusHansFreezeManager.DESCRIPTOR);
                    _data.writeString(callerPackageName);
                    _data.writeTypedList(visibleAppCardList, 0);
                    _data.writeInt(requestId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 8;
        }
    }
}
