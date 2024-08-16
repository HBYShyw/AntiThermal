package com.oplus.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusLockPatternUtils extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.os.IOplusLockPatternUtils";

    byte[] generateDerivedPassword(byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteException;

    Map getDerivedPasswordInfo(byte[] bArr, byte[] bArr2) throws RemoteException;

    byte[] getPublicKey() throws RemoteException;

    byte[] getVersionInfo() throws RemoteException;

    void notifySrpLockVerify(byte[] bArr) throws RemoteException;

    void registSrpCredential() throws RemoteException;

    void unRegistSrpCredential() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusLockPatternUtils {
        @Override // com.oplus.os.IOplusLockPatternUtils
        public byte[] getVersionInfo() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusLockPatternUtils
        public void notifySrpLockVerify(byte[] eSalt) throws RemoteException {
        }

        @Override // com.oplus.os.IOplusLockPatternUtils
        public void registSrpCredential() throws RemoteException {
        }

        @Override // com.oplus.os.IOplusLockPatternUtils
        public void unRegistSrpCredential() throws RemoteException {
        }

        @Override // com.oplus.os.IOplusLockPatternUtils
        public Map getDerivedPasswordInfo(byte[] pubkeyForPassword, byte[] pubkeyForSalt) throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusLockPatternUtils
        public byte[] getPublicKey() throws RemoteException {
            return null;
        }

        @Override // com.oplus.os.IOplusLockPatternUtils
        public byte[] generateDerivedPassword(byte[] pubKey, byte[] ePassword, byte[] eSalt) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusLockPatternUtils {
        static final int TRANSACTION_generateDerivedPassword = 7;
        static final int TRANSACTION_getDerivedPasswordInfo = 5;
        static final int TRANSACTION_getPublicKey = 6;
        static final int TRANSACTION_getVersionInfo = 1;
        static final int TRANSACTION_notifySrpLockVerify = 2;
        static final int TRANSACTION_registSrpCredential = 3;
        static final int TRANSACTION_unRegistSrpCredential = 4;

        public Stub() {
            attachInterface(this, IOplusLockPatternUtils.DESCRIPTOR);
        }

        public static IOplusLockPatternUtils asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusLockPatternUtils.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusLockPatternUtils)) {
                return (IOplusLockPatternUtils) iin;
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
                    return "getVersionInfo";
                case 2:
                    return "notifySrpLockVerify";
                case 3:
                    return "registSrpCredential";
                case 4:
                    return "unRegistSrpCredential";
                case 5:
                    return "getDerivedPasswordInfo";
                case 6:
                    return "getPublicKey";
                case 7:
                    return "generateDerivedPassword";
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
                data.enforceInterface(IOplusLockPatternUtils.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusLockPatternUtils.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            byte[] _result = getVersionInfo();
                            reply.writeNoException();
                            reply.writeByteArray(_result);
                            return true;
                        case 2:
                            byte[] _arg0 = data.createByteArray();
                            data.enforceNoDataAvail();
                            notifySrpLockVerify(_arg0);
                            reply.writeNoException();
                            return true;
                        case 3:
                            registSrpCredential();
                            return true;
                        case 4:
                            unRegistSrpCredential();
                            return true;
                        case 5:
                            byte[] _arg02 = data.createByteArray();
                            byte[] _arg1 = data.createByteArray();
                            data.enforceNoDataAvail();
                            Map _result2 = getDerivedPasswordInfo(_arg02, _arg1);
                            reply.writeNoException();
                            reply.writeMap(_result2);
                            return true;
                        case 6:
                            byte[] _result3 = getPublicKey();
                            reply.writeNoException();
                            reply.writeByteArray(_result3);
                            return true;
                        case 7:
                            byte[] _arg03 = data.createByteArray();
                            byte[] _arg12 = data.createByteArray();
                            byte[] _arg2 = data.createByteArray();
                            data.enforceNoDataAvail();
                            byte[] _result4 = generateDerivedPassword(_arg03, _arg12, _arg2);
                            reply.writeNoException();
                            reply.writeByteArray(_result4);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusLockPatternUtils {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusLockPatternUtils.DESCRIPTOR;
            }

            @Override // com.oplus.os.IOplusLockPatternUtils
            public byte[] getVersionInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLockPatternUtils.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusLockPatternUtils
            public void notifySrpLockVerify(byte[] eSalt) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLockPatternUtils.DESCRIPTOR);
                    _data.writeByteArray(eSalt);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusLockPatternUtils
            public void registSrpCredential() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusLockPatternUtils.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusLockPatternUtils
            public void unRegistSrpCredential() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusLockPatternUtils.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusLockPatternUtils
            public Map getDerivedPasswordInfo(byte[] pubkeyForPassword, byte[] pubkeyForSalt) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLockPatternUtils.DESCRIPTOR);
                    _data.writeByteArray(pubkeyForPassword);
                    _data.writeByteArray(pubkeyForSalt);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusLockPatternUtils
            public byte[] getPublicKey() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLockPatternUtils.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.os.IOplusLockPatternUtils
            public byte[] generateDerivedPassword(byte[] pubKey, byte[] ePassword, byte[] eSalt) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusLockPatternUtils.DESCRIPTOR);
                    _data.writeByteArray(pubKey);
                    _data.writeByteArray(ePassword);
                    _data.writeByteArray(eSalt);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
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
