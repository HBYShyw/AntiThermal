package android.secrecy;

import android.content.pm.ActivityInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.secrecy.ISecrecyServiceReceiver;

/* loaded from: classes.dex */
public interface ISecrecyService extends IInterface {
    public static final String DESCRIPTOR = "android.secrecy.ISecrecyService";

    byte[] generateCipherFromKey(int i) throws RemoteException;

    String generateTokenFromKey() throws RemoteException;

    boolean getSecrecyState(int i) throws RemoteException;

    boolean isInEncryptedAppList(ActivityInfo activityInfo, String str, int i, int i2) throws RemoteException;

    boolean isKeyImported() throws RemoteException;

    boolean isSecrecySupport() throws RemoteException;

    boolean registerSecrecyServiceReceiver(ISecrecyServiceReceiver iSecrecyServiceReceiver) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ISecrecyService {
        @Override // android.secrecy.ISecrecyService
        public boolean getSecrecyState(int type) throws RemoteException {
            return false;
        }

        @Override // android.secrecy.ISecrecyService
        public String generateTokenFromKey() throws RemoteException {
            return null;
        }

        @Override // android.secrecy.ISecrecyService
        public boolean isKeyImported() throws RemoteException {
            return false;
        }

        @Override // android.secrecy.ISecrecyService
        public byte[] generateCipherFromKey(int cipherLength) throws RemoteException {
            return null;
        }

        @Override // android.secrecy.ISecrecyService
        public boolean registerSecrecyServiceReceiver(ISecrecyServiceReceiver receiver) throws RemoteException {
            return false;
        }

        @Override // android.secrecy.ISecrecyService
        public boolean isSecrecySupport() throws RemoteException {
            return false;
        }

        @Override // android.secrecy.ISecrecyService
        public boolean isInEncryptedAppList(ActivityInfo info, String callingPackage, int callingUid, int callingPid) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISecrecyService {
        static final int TRANSACTION_generateCipherFromKey = 4;
        static final int TRANSACTION_generateTokenFromKey = 2;
        static final int TRANSACTION_getSecrecyState = 1;
        static final int TRANSACTION_isInEncryptedAppList = 7;
        static final int TRANSACTION_isKeyImported = 3;
        static final int TRANSACTION_isSecrecySupport = 6;
        static final int TRANSACTION_registerSecrecyServiceReceiver = 5;

        public Stub() {
            attachInterface(this, ISecrecyService.DESCRIPTOR);
        }

        public static ISecrecyService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ISecrecyService.DESCRIPTOR);
            if (iin != null && (iin instanceof ISecrecyService)) {
                return (ISecrecyService) iin;
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
                    return "getSecrecyState";
                case 2:
                    return "generateTokenFromKey";
                case 3:
                    return "isKeyImported";
                case 4:
                    return "generateCipherFromKey";
                case 5:
                    return "registerSecrecyServiceReceiver";
                case 6:
                    return "isSecrecySupport";
                case 7:
                    return "isInEncryptedAppList";
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
                data.enforceInterface(ISecrecyService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ISecrecyService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result = getSecrecyState(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            String _result2 = generateTokenFromKey();
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 3:
                            boolean _result3 = isKeyImported();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            byte[] _result4 = generateCipherFromKey(_arg02);
                            reply.writeNoException();
                            reply.writeByteArray(_result4);
                            return true;
                        case 5:
                            ISecrecyServiceReceiver _arg03 = ISecrecyServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result5 = registerSecrecyServiceReceiver(_arg03);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            boolean _result6 = isSecrecySupport();
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 7:
                            ActivityInfo _arg04 = (ActivityInfo) data.readTypedObject(ActivityInfo.CREATOR);
                            String _arg1 = data.readString();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result7 = isInEncryptedAppList(_arg04, _arg1, _arg2, _arg3);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ISecrecyService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISecrecyService.DESCRIPTOR;
            }

            @Override // android.secrecy.ISecrecyService
            public boolean getSecrecyState(int type) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecrecyService.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.secrecy.ISecrecyService
            public String generateTokenFromKey() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecrecyService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.secrecy.ISecrecyService
            public boolean isKeyImported() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecrecyService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.secrecy.ISecrecyService
            public byte[] generateCipherFromKey(int cipherLength) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecrecyService.DESCRIPTOR);
                    _data.writeInt(cipherLength);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.secrecy.ISecrecyService
            public boolean registerSecrecyServiceReceiver(ISecrecyServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecrecyService.DESCRIPTOR);
                    _data.writeStrongInterface(receiver);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.secrecy.ISecrecyService
            public boolean isSecrecySupport() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecrecyService.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.secrecy.ISecrecyService
            public boolean isInEncryptedAppList(ActivityInfo info, String callingPackage, int callingUid, int callingPid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISecrecyService.DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    _data.writeString(callingPackage);
                    _data.writeInt(callingUid);
                    _data.writeInt(callingPid);
                    this.mRemote.transact(7, _data, _reply, 0);
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
            return 6;
        }
    }
}
