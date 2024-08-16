package android.os.customize;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusCustomizeContactManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeContactManagerService";

    int getContactBlockPattern() throws RemoteException;

    int getContactMatchPattern() throws RemoteException;

    int getContactNumberHideMode() throws RemoteException;

    int getContactNumberMaskEnable() throws RemoteException;

    int getContactOutgoOrIncomePattern() throws RemoteException;

    boolean isContactBlackListEnable() throws RemoteException;

    boolean isForbidCallLogEnable() throws RemoteException;

    boolean setContactBlackListEnable(boolean z) throws RemoteException;

    boolean setContactBlockPattern(int i) throws RemoteException;

    boolean setContactMatchPattern(int i) throws RemoteException;

    boolean setContactNumberHideMode(int i) throws RemoteException;

    boolean setContactNumberMaskEnable(int i) throws RemoteException;

    boolean setContactOutgoOrIncomePattern(int i) throws RemoteException;

    boolean setForbidCallLogEnable(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeContactManagerService {
        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean setContactBlackListEnable(boolean enable) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean isContactBlackListEnable() throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean setContactBlockPattern(int blockPattern) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public int getContactBlockPattern() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean setContactMatchPattern(int matchPattern) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public int getContactMatchPattern() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean setContactOutgoOrIncomePattern(int outgoOrIncomePattern) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public int getContactOutgoOrIncomePattern() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean setContactNumberHideMode(int mode) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public int getContactNumberHideMode() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean setContactNumberMaskEnable(int switcher) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public int getContactNumberMaskEnable() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean setForbidCallLogEnable(int forbid) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeContactManagerService
        public boolean isForbidCallLogEnable() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeContactManagerService {
        static final int TRANSACTION_getContactBlockPattern = 4;
        static final int TRANSACTION_getContactMatchPattern = 6;
        static final int TRANSACTION_getContactNumberHideMode = 10;
        static final int TRANSACTION_getContactNumberMaskEnable = 12;
        static final int TRANSACTION_getContactOutgoOrIncomePattern = 8;
        static final int TRANSACTION_isContactBlackListEnable = 2;
        static final int TRANSACTION_isForbidCallLogEnable = 14;
        static final int TRANSACTION_setContactBlackListEnable = 1;
        static final int TRANSACTION_setContactBlockPattern = 3;
        static final int TRANSACTION_setContactMatchPattern = 5;
        static final int TRANSACTION_setContactNumberHideMode = 9;
        static final int TRANSACTION_setContactNumberMaskEnable = 11;
        static final int TRANSACTION_setContactOutgoOrIncomePattern = 7;
        static final int TRANSACTION_setForbidCallLogEnable = 13;

        public Stub() {
            attachInterface(this, IOplusCustomizeContactManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeContactManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeContactManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeContactManagerService)) {
                return (IOplusCustomizeContactManagerService) iin;
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
                    return "setContactBlackListEnable";
                case 2:
                    return "isContactBlackListEnable";
                case 3:
                    return "setContactBlockPattern";
                case 4:
                    return "getContactBlockPattern";
                case 5:
                    return "setContactMatchPattern";
                case 6:
                    return "getContactMatchPattern";
                case 7:
                    return "setContactOutgoOrIncomePattern";
                case 8:
                    return "getContactOutgoOrIncomePattern";
                case 9:
                    return "setContactNumberHideMode";
                case 10:
                    return "getContactNumberHideMode";
                case 11:
                    return "setContactNumberMaskEnable";
                case 12:
                    return "getContactNumberMaskEnable";
                case 13:
                    return "setForbidCallLogEnable";
                case 14:
                    return "isForbidCallLogEnable";
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
                data.enforceInterface(IOplusCustomizeContactManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            boolean _arg0 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result = setContactBlackListEnable(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            boolean _result2 = isContactBlackListEnable();
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 3:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result3 = setContactBlockPattern(_arg02);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            int _result4 = getContactBlockPattern();
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 5:
                            int _arg03 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result5 = setContactMatchPattern(_arg03);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 6:
                            int _result6 = getContactMatchPattern();
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 7:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result7 = setContactOutgoOrIncomePattern(_arg04);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 8:
                            int _result8 = getContactOutgoOrIncomePattern();
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 9:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result9 = setContactNumberHideMode(_arg05);
                            reply.writeNoException();
                            reply.writeBoolean(_result9);
                            return true;
                        case 10:
                            int _result10 = getContactNumberHideMode();
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 11:
                            int _arg06 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result11 = setContactNumberMaskEnable(_arg06);
                            reply.writeNoException();
                            reply.writeBoolean(_result11);
                            return true;
                        case 12:
                            int _result12 = getContactNumberMaskEnable();
                            reply.writeNoException();
                            reply.writeInt(_result12);
                            return true;
                        case 13:
                            int _arg07 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result13 = setForbidCallLogEnable(_arg07);
                            reply.writeNoException();
                            reply.writeBoolean(_result13);
                            return true;
                        case 14:
                            boolean _result14 = isForbidCallLogEnable();
                            reply.writeNoException();
                            reply.writeBoolean(_result14);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeContactManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeContactManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean setContactBlackListEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    _data.writeBoolean(enable);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean isContactBlackListEnable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean setContactBlockPattern(int blockPattern) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    _data.writeInt(blockPattern);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public int getContactBlockPattern() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean setContactMatchPattern(int matchPattern) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    _data.writeInt(matchPattern);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public int getContactMatchPattern() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean setContactOutgoOrIncomePattern(int outgoOrIncomePattern) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    _data.writeInt(outgoOrIncomePattern);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public int getContactOutgoOrIncomePattern() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean setContactNumberHideMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public int getContactNumberHideMode() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean setContactNumberMaskEnable(int switcher) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    _data.writeInt(switcher);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public int getContactNumberMaskEnable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean setForbidCallLogEnable(int forbid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    _data.writeInt(forbid);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeContactManagerService
            public boolean isForbidCallLogEnable() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeContactManagerService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
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
            return 13;
        }
    }
}
