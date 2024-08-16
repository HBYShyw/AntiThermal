package com.oplus.heimdall;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.heimdall.ICrashListener;
import java.util.List;

/* loaded from: classes.dex */
public interface ICrashService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.heimdall.ICrashService";

    boolean addListener(String str, String str2, ICrashListener iCrashListener, List<String> list) throws RemoteException;

    boolean removeListener(String str, String str2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICrashService {
        @Override // com.oplus.heimdall.ICrashService
        public boolean addListener(String callerPackageName, String packageName, ICrashListener listener, List<String> traceContents) throws RemoteException {
            return false;
        }

        @Override // com.oplus.heimdall.ICrashService
        public boolean removeListener(String callerPackageName, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICrashService {
        static final int TRANSACTION_addListener = 1;
        static final int TRANSACTION_removeListener = 2;

        public Stub() {
            attachInterface(this, ICrashService.DESCRIPTOR);
        }

        public static ICrashService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICrashService.DESCRIPTOR);
            if (iin != null && (iin instanceof ICrashService)) {
                return (ICrashService) iin;
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
                    return "addListener";
                case 2:
                    return "removeListener";
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
                data.enforceInterface(ICrashService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICrashService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            String _arg1 = data.readString();
                            ICrashListener _arg2 = ICrashListener.Stub.asInterface(data.readStrongBinder());
                            List<String> _arg3 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            boolean _result = addListener(_arg0, _arg1, _arg2, _arg3);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result2 = removeListener(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICrashService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICrashService.DESCRIPTOR;
            }

            @Override // com.oplus.heimdall.ICrashService
            public boolean addListener(String callerPackageName, String packageName, ICrashListener listener, List<String> traceContents) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrashService.DESCRIPTOR);
                    _data.writeString(callerPackageName);
                    _data.writeString(packageName);
                    _data.writeStrongInterface(listener);
                    _data.writeStringList(traceContents);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.heimdall.ICrashService
            public boolean removeListener(String callerPackageName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrashService.DESCRIPTOR);
                    _data.writeString(callerPackageName);
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
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
            return 1;
        }
    }
}
