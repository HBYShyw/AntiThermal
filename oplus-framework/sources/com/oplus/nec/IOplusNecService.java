package com.oplus.nec;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.nec.IOnNecEventListener;

/* loaded from: classes.dex */
public interface IOplusNecService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.nec.IOplusNecService";

    void addNecEventListener(String str, IOnNecEventListener iOnNecEventListener) throws RemoteException;

    void clearCellAppsRttRecord() throws RemoteException;

    void clearCellDnsRecord() throws RemoteException;

    void clearCellHttpRecord() throws RemoteException;

    void clearCellNetTotalRecord() throws RemoteException;

    void clearCellTcpRecord() throws RemoteException;

    void clearWlanAppsRttRecord() throws RemoteException;

    void clearWlanDnsRecord() throws RemoteException;

    void clearWlanHttpRecord() throws RemoteException;

    void clearWlanPowerRecord() throws RemoteException;

    void clearWlanTcpRecord() throws RemoteException;

    String getCellAppsRttRecord() throws RemoteException;

    String getCellDnsRecord() throws RemoteException;

    String getCellHttpRecord() throws RemoteException;

    String getCellNetTotalRecord() throws RemoteException;

    String getCellTcpRecord() throws RemoteException;

    String getWlanAppsRttRecord() throws RemoteException;

    String getWlanDnsRecord() throws RemoteException;

    String getWlanHttpRecord() throws RemoteException;

    String getWlanPowerRecord() throws RemoteException;

    String getWlanTcpRecord() throws RemoteException;

    String onCollectPwrStatistic(boolean z) throws RemoteException;

    void onStandbyStart(boolean z) throws RemoteException;

    void removeNecEventListener(String str, IOnNecEventListener iOnNecEventListener) throws RemoteException;

    void reportNecEvent(int i, int i2, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusNecService {
        @Override // com.oplus.nec.IOplusNecService
        public void addNecEventListener(String callingPackage, IOnNecEventListener callback) throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void removeNecEventListener(String pkgForDebug, IOnNecEventListener callback) throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void reportNecEvent(int slotId, int eventId, Bundle data) throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getCellDnsRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getWlanDnsRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getCellHttpRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getWlanHttpRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getCellTcpRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getWlanTcpRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getCellAppsRttRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getWlanAppsRttRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getCellNetTotalRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearCellDnsRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearWlanDnsRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearCellHttpRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearWlanHttpRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearCellTcpRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearWlanTcpRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearCellAppsRttRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearWlanAppsRttRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public String getWlanPowerRecord() throws RemoteException {
            return null;
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearCellNetTotalRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void clearWlanPowerRecord() throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public void onStandbyStart(boolean disabled) throws RemoteException {
        }

        @Override // com.oplus.nec.IOplusNecService
        public String onCollectPwrStatistic(boolean cleanup) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusNecService {
        static final int TRANSACTION_addNecEventListener = 1;
        static final int TRANSACTION_clearCellAppsRttRecord = 19;
        static final int TRANSACTION_clearCellDnsRecord = 13;
        static final int TRANSACTION_clearCellHttpRecord = 15;
        static final int TRANSACTION_clearCellNetTotalRecord = 22;
        static final int TRANSACTION_clearCellTcpRecord = 17;
        static final int TRANSACTION_clearWlanAppsRttRecord = 20;
        static final int TRANSACTION_clearWlanDnsRecord = 14;
        static final int TRANSACTION_clearWlanHttpRecord = 16;
        static final int TRANSACTION_clearWlanPowerRecord = 23;
        static final int TRANSACTION_clearWlanTcpRecord = 18;
        static final int TRANSACTION_getCellAppsRttRecord = 10;
        static final int TRANSACTION_getCellDnsRecord = 4;
        static final int TRANSACTION_getCellHttpRecord = 6;
        static final int TRANSACTION_getCellNetTotalRecord = 12;
        static final int TRANSACTION_getCellTcpRecord = 8;
        static final int TRANSACTION_getWlanAppsRttRecord = 11;
        static final int TRANSACTION_getWlanDnsRecord = 5;
        static final int TRANSACTION_getWlanHttpRecord = 7;
        static final int TRANSACTION_getWlanPowerRecord = 21;
        static final int TRANSACTION_getWlanTcpRecord = 9;
        static final int TRANSACTION_onCollectPwrStatistic = 25;
        static final int TRANSACTION_onStandbyStart = 24;
        static final int TRANSACTION_removeNecEventListener = 2;
        static final int TRANSACTION_reportNecEvent = 3;

        public Stub() {
            attachInterface(this, IOplusNecService.DESCRIPTOR);
        }

        public static IOplusNecService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusNecService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusNecService)) {
                return (IOplusNecService) iin;
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
                    return "addNecEventListener";
                case 2:
                    return "removeNecEventListener";
                case 3:
                    return "reportNecEvent";
                case 4:
                    return "getCellDnsRecord";
                case 5:
                    return "getWlanDnsRecord";
                case 6:
                    return "getCellHttpRecord";
                case 7:
                    return "getWlanHttpRecord";
                case 8:
                    return "getCellTcpRecord";
                case 9:
                    return "getWlanTcpRecord";
                case 10:
                    return "getCellAppsRttRecord";
                case 11:
                    return "getWlanAppsRttRecord";
                case 12:
                    return "getCellNetTotalRecord";
                case 13:
                    return "clearCellDnsRecord";
                case 14:
                    return "clearWlanDnsRecord";
                case 15:
                    return "clearCellHttpRecord";
                case 16:
                    return "clearWlanHttpRecord";
                case 17:
                    return "clearCellTcpRecord";
                case 18:
                    return "clearWlanTcpRecord";
                case 19:
                    return "clearCellAppsRttRecord";
                case 20:
                    return "clearWlanAppsRttRecord";
                case 21:
                    return "getWlanPowerRecord";
                case 22:
                    return "clearCellNetTotalRecord";
                case 23:
                    return "clearWlanPowerRecord";
                case 24:
                    return "onStandbyStart";
                case 25:
                    return "onCollectPwrStatistic";
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
                data.enforceInterface(IOplusNecService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusNecService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            IOnNecEventListener _arg1 = IOnNecEventListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            addNecEventListener(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            IOnNecEventListener _arg12 = IOnNecEventListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            removeNecEventListener(_arg02, _arg12);
                            reply.writeNoException();
                            return true;
                        case 3:
                            int _arg03 = data.readInt();
                            int _arg13 = data.readInt();
                            Bundle _arg2 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            reportNecEvent(_arg03, _arg13, _arg2);
                            reply.writeNoException();
                            return true;
                        case 4:
                            String _result = getCellDnsRecord();
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 5:
                            String _result2 = getWlanDnsRecord();
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 6:
                            String _result3 = getCellHttpRecord();
                            reply.writeNoException();
                            reply.writeString(_result3);
                            return true;
                        case 7:
                            String _result4 = getWlanHttpRecord();
                            reply.writeNoException();
                            reply.writeString(_result4);
                            return true;
                        case 8:
                            String _result5 = getCellTcpRecord();
                            reply.writeNoException();
                            reply.writeString(_result5);
                            return true;
                        case 9:
                            String _result6 = getWlanTcpRecord();
                            reply.writeNoException();
                            reply.writeString(_result6);
                            return true;
                        case 10:
                            String _result7 = getCellAppsRttRecord();
                            reply.writeNoException();
                            reply.writeString(_result7);
                            return true;
                        case 11:
                            String _result8 = getWlanAppsRttRecord();
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 12:
                            String _result9 = getCellNetTotalRecord();
                            reply.writeNoException();
                            reply.writeString(_result9);
                            return true;
                        case 13:
                            clearCellDnsRecord();
                            reply.writeNoException();
                            return true;
                        case 14:
                            clearWlanDnsRecord();
                            reply.writeNoException();
                            return true;
                        case 15:
                            clearCellHttpRecord();
                            reply.writeNoException();
                            return true;
                        case 16:
                            clearWlanHttpRecord();
                            reply.writeNoException();
                            return true;
                        case 17:
                            clearCellTcpRecord();
                            reply.writeNoException();
                            return true;
                        case 18:
                            clearWlanTcpRecord();
                            reply.writeNoException();
                            return true;
                        case 19:
                            clearCellAppsRttRecord();
                            reply.writeNoException();
                            return true;
                        case 20:
                            clearWlanAppsRttRecord();
                            reply.writeNoException();
                            return true;
                        case 21:
                            String _result10 = getWlanPowerRecord();
                            reply.writeNoException();
                            reply.writeString(_result10);
                            return true;
                        case 22:
                            clearCellNetTotalRecord();
                            reply.writeNoException();
                            return true;
                        case 23:
                            clearWlanPowerRecord();
                            reply.writeNoException();
                            return true;
                        case 24:
                            boolean _arg04 = data.readBoolean();
                            data.enforceNoDataAvail();
                            onStandbyStart(_arg04);
                            reply.writeNoException();
                            return true;
                        case 25:
                            boolean _arg05 = data.readBoolean();
                            data.enforceNoDataAvail();
                            String _result11 = onCollectPwrStatistic(_arg05);
                            reply.writeNoException();
                            reply.writeString(_result11);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusNecService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusNecService.DESCRIPTOR;
            }

            @Override // com.oplus.nec.IOplusNecService
            public void addNecEventListener(String callingPackage, IOnNecEventListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void removeNecEventListener(String pkgForDebug, IOnNecEventListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    _data.writeString(pkgForDebug);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void reportNecEvent(int slotId, int eventId, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(eventId);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getCellDnsRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getWlanDnsRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getCellHttpRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getWlanHttpRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getCellTcpRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getWlanTcpRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getCellAppsRttRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getWlanAppsRttRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getCellNetTotalRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearCellDnsRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearWlanDnsRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearCellHttpRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearWlanHttpRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearCellTcpRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearWlanTcpRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearCellAppsRttRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearWlanAppsRttRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String getWlanPowerRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearCellNetTotalRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void clearWlanPowerRecord() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public void onStandbyStart(boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.nec.IOplusNecService
            public String onCollectPwrStatistic(boolean cleanup) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusNecService.DESCRIPTOR);
                    _data.writeBoolean(cleanup);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 24;
        }
    }
}
