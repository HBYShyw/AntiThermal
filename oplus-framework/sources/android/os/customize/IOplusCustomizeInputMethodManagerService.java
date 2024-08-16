package android.os.customize;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusCustomizeInputMethodManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeInputMethodManagerService";

    boolean clearDefaultInputMethod() throws RemoteException;

    String getDefaultInputMethod() throws RemoteException;

    boolean setDefaultInputMethod(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeInputMethodManagerService {
        @Override // android.os.customize.IOplusCustomizeInputMethodManagerService
        public boolean setDefaultInputMethod(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeInputMethodManagerService
        public String getDefaultInputMethod() throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeInputMethodManagerService
        public boolean clearDefaultInputMethod() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeInputMethodManagerService {
        static final int TRANSACTION_clearDefaultInputMethod = 3;
        static final int TRANSACTION_getDefaultInputMethod = 2;
        static final int TRANSACTION_setDefaultInputMethod = 1;

        public Stub() {
            attachInterface(this, IOplusCustomizeInputMethodManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeInputMethodManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeInputMethodManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeInputMethodManagerService)) {
                return (IOplusCustomizeInputMethodManagerService) iin;
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
                    return "setDefaultInputMethod";
                case 2:
                    return "getDefaultInputMethod";
                case 3:
                    return "clearDefaultInputMethod";
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
                data.enforceInterface(IOplusCustomizeInputMethodManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeInputMethodManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result = setDefaultInputMethod(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _result2 = getDefaultInputMethod();
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 3:
                            boolean _result3 = clearDefaultInputMethod();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeInputMethodManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeInputMethodManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeInputMethodManagerService
            public boolean setDefaultInputMethod(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeInputMethodManagerService.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeInputMethodManagerService
            public String getDefaultInputMethod() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeInputMethodManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeInputMethodManagerService
            public boolean clearDefaultInputMethod() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeInputMethodManagerService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
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
            return 2;
        }
    }
}
