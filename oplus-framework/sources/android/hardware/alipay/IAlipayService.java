package android.hardware.alipay;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IAlipayService extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.alipay.IAlipayService";

    byte[] alipayInvokeCommand(byte[] bArr) throws RemoteException;

    int cancel(String str) throws RemoteException;

    String getDeviceModel() throws RemoteException;

    int getFingerprintIconDiameter() throws RemoteException;

    int getFingerprintIconExternalCircleXY(String str) throws RemoteException;

    int getSupportBIOTypes() throws RemoteException;

    int getSupportIFAAVersion() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IAlipayService {
        @Override // android.hardware.alipay.IAlipayService
        public byte[] alipayInvokeCommand(byte[] inbuf) throws RemoteException {
            return null;
        }

        @Override // android.hardware.alipay.IAlipayService
        public int cancel(String reqId) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.alipay.IAlipayService
        public int getSupportBIOTypes() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.alipay.IAlipayService
        public int getSupportIFAAVersion() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.alipay.IAlipayService
        public String getDeviceModel() throws RemoteException {
            return null;
        }

        @Override // android.hardware.alipay.IAlipayService
        public int getFingerprintIconDiameter() throws RemoteException {
            return 0;
        }

        @Override // android.hardware.alipay.IAlipayService
        public int getFingerprintIconExternalCircleXY(String coord) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAlipayService {
        static final int TRANSACTION_alipayInvokeCommand = 1;
        static final int TRANSACTION_cancel = 2;
        static final int TRANSACTION_getDeviceModel = 5;
        static final int TRANSACTION_getFingerprintIconDiameter = 6;
        static final int TRANSACTION_getFingerprintIconExternalCircleXY = 7;
        static final int TRANSACTION_getSupportBIOTypes = 3;
        static final int TRANSACTION_getSupportIFAAVersion = 4;

        public Stub() {
            attachInterface(this, IAlipayService.DESCRIPTOR);
        }

        public static IAlipayService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAlipayService.DESCRIPTOR);
            if (iin != null && (iin instanceof IAlipayService)) {
                return (IAlipayService) iin;
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
                    return "alipayInvokeCommand";
                case 2:
                    return "cancel";
                case 3:
                    return "getSupportBIOTypes";
                case 4:
                    return "getSupportIFAAVersion";
                case 5:
                    return "getDeviceModel";
                case 6:
                    return "getFingerprintIconDiameter";
                case 7:
                    return "getFingerprintIconExternalCircleXY";
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
                data.enforceInterface(IAlipayService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IAlipayService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            byte[] _arg0 = data.createByteArray();
                            data.enforceNoDataAvail();
                            byte[] _result = alipayInvokeCommand(_arg0);
                            reply.writeNoException();
                            reply.writeByteArray(_result);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            data.enforceNoDataAvail();
                            int _result2 = cancel(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            int _result3 = getSupportBIOTypes();
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            int _result4 = getSupportIFAAVersion();
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 5:
                            String _result5 = getDeviceModel();
                            reply.writeNoException();
                            reply.writeString(_result5);
                            return true;
                        case 6:
                            int _result6 = getFingerprintIconDiameter();
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 7:
                            String _arg03 = data.readString();
                            data.enforceNoDataAvail();
                            int _result7 = getFingerprintIconExternalCircleXY(_arg03);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IAlipayService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAlipayService.DESCRIPTOR;
            }

            @Override // android.hardware.alipay.IAlipayService
            public byte[] alipayInvokeCommand(byte[] inbuf) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAlipayService.DESCRIPTOR);
                    _data.writeByteArray(inbuf);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.alipay.IAlipayService
            public int cancel(String reqId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAlipayService.DESCRIPTOR);
                    _data.writeString(reqId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.alipay.IAlipayService
            public int getSupportBIOTypes() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAlipayService.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.alipay.IAlipayService
            public int getSupportIFAAVersion() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAlipayService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.alipay.IAlipayService
            public String getDeviceModel() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAlipayService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.alipay.IAlipayService
            public int getFingerprintIconDiameter() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAlipayService.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.alipay.IAlipayService
            public int getFingerprintIconExternalCircleXY(String coord) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAlipayService.DESCRIPTOR);
                    _data.writeString(coord);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
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
