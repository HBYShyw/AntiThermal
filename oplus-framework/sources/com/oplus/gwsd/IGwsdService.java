package com.oplus.gwsd;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.gwsd.IGwsdListener;

/* loaded from: classes.dex */
public interface IGwsdService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.gwsd.IGwsdService";

    void addListener(int i, IGwsdListener iGwsdListener) throws RemoteException;

    boolean isDataAvailableForGwsdDualSim(boolean z) throws RemoteException;

    void removeListener(int i) throws RemoteException;

    void setAutoRejectModeEnabled(int i, boolean z) throws RemoteException;

    void setCallValidTimer(int i, int i2) throws RemoteException;

    void setGwsdDualSimEnabled(boolean z) throws RemoteException;

    void setIgnoreSameNumberInterval(int i, int i2) throws RemoteException;

    void setUserModeEnabled(int i, boolean z) throws RemoteException;

    void syncGwsdInfo(int i, boolean z, boolean z2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IGwsdService {
        @Override // com.oplus.gwsd.IGwsdService
        public void addListener(int phoneId, IGwsdListener listener) throws RemoteException {
        }

        @Override // com.oplus.gwsd.IGwsdService
        public void removeListener(int phoneId) throws RemoteException {
        }

        @Override // com.oplus.gwsd.IGwsdService
        public void setUserModeEnabled(int phoneId, boolean action) throws RemoteException {
        }

        @Override // com.oplus.gwsd.IGwsdService
        public void setAutoRejectModeEnabled(int phoneId, boolean action) throws RemoteException {
        }

        @Override // com.oplus.gwsd.IGwsdService
        public void syncGwsdInfo(int phoneId, boolean userEnable, boolean autoReject) throws RemoteException {
        }

        @Override // com.oplus.gwsd.IGwsdService
        public void setCallValidTimer(int phoneId, int timer) throws RemoteException {
        }

        @Override // com.oplus.gwsd.IGwsdService
        public void setIgnoreSameNumberInterval(int phoneId, int interna) throws RemoteException {
        }

        @Override // com.oplus.gwsd.IGwsdService
        public void setGwsdDualSimEnabled(boolean action) throws RemoteException {
        }

        @Override // com.oplus.gwsd.IGwsdService
        public boolean isDataAvailableForGwsdDualSim(boolean gwsdDualSimStatus) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGwsdService {
        static final int TRANSACTION_addListener = 1;
        static final int TRANSACTION_isDataAvailableForGwsdDualSim = 9;
        static final int TRANSACTION_removeListener = 2;
        static final int TRANSACTION_setAutoRejectModeEnabled = 4;
        static final int TRANSACTION_setCallValidTimer = 6;
        static final int TRANSACTION_setGwsdDualSimEnabled = 8;
        static final int TRANSACTION_setIgnoreSameNumberInterval = 7;
        static final int TRANSACTION_setUserModeEnabled = 3;
        static final int TRANSACTION_syncGwsdInfo = 5;

        public Stub() {
            attachInterface(this, IGwsdService.DESCRIPTOR);
        }

        public static IGwsdService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IGwsdService.DESCRIPTOR);
            if (iin != null && (iin instanceof IGwsdService)) {
                return (IGwsdService) iin;
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
                case 3:
                    return "setUserModeEnabled";
                case 4:
                    return "setAutoRejectModeEnabled";
                case 5:
                    return "syncGwsdInfo";
                case 6:
                    return "setCallValidTimer";
                case 7:
                    return "setIgnoreSameNumberInterval";
                case 8:
                    return "setGwsdDualSimEnabled";
                case 9:
                    return "isDataAvailableForGwsdDualSim";
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
                data.enforceInterface(IGwsdService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IGwsdService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _arg0 = data.readInt();
                            IGwsdListener _arg1 = IGwsdListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            addListener(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            removeListener(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setUserModeEnabled(_arg03, _arg12);
                            reply.writeNoException();
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            boolean _arg13 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setAutoRejectModeEnabled(_arg04, _arg13);
                            reply.writeNoException();
                            return true;
                        case 5:
                            int _arg05 = data.readInt();
                            boolean _arg14 = data.readBoolean();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            syncGwsdInfo(_arg05, _arg14, _arg2);
                            reply.writeNoException();
                            return true;
                        case 6:
                            int _arg06 = data.readInt();
                            int _arg15 = data.readInt();
                            data.enforceNoDataAvail();
                            setCallValidTimer(_arg06, _arg15);
                            reply.writeNoException();
                            return true;
                        case 7:
                            int _arg07 = data.readInt();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            setIgnoreSameNumberInterval(_arg07, _arg16);
                            reply.writeNoException();
                            return true;
                        case 8:
                            boolean _arg08 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setGwsdDualSimEnabled(_arg08);
                            reply.writeNoException();
                            return true;
                        case 9:
                            boolean _arg09 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result = isDataAvailableForGwsdDualSim(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IGwsdService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IGwsdService.DESCRIPTOR;
            }

            @Override // com.oplus.gwsd.IGwsdService
            public void addListener(int phoneId, IGwsdListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.gwsd.IGwsdService
            public void removeListener(int phoneId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.gwsd.IGwsdService
            public void setUserModeEnabled(int phoneId, boolean action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(action);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.gwsd.IGwsdService
            public void setAutoRejectModeEnabled(int phoneId, boolean action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(action);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.gwsd.IGwsdService
            public void syncGwsdInfo(int phoneId, boolean userEnable, boolean autoReject) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeBoolean(userEnable);
                    _data.writeBoolean(autoReject);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.gwsd.IGwsdService
            public void setCallValidTimer(int phoneId, int timer) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(timer);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.gwsd.IGwsdService
            public void setIgnoreSameNumberInterval(int phoneId, int interna) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(interna);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.gwsd.IGwsdService
            public void setGwsdDualSimEnabled(boolean action) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeBoolean(action);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.gwsd.IGwsdService
            public boolean isDataAvailableForGwsdDualSim(boolean gwsdDualSimStatus) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGwsdService.DESCRIPTOR);
                    _data.writeBoolean(gwsdDualSimStatus);
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
