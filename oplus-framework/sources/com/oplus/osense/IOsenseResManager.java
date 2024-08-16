package com.oplus.osense;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.osense.eventinfo.EventConfig;
import com.oplus.osense.eventinfo.IOsenseEventCallback;
import com.oplus.osense.info.OsenseCtrlDataRequest;
import com.oplus.osense.info.OsenseNotifyRequest;

/* loaded from: classes.dex */
public interface IOsenseResManager extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.osense.IOsenseResManager";

    void athenaReqSceneAction(String str, Bundle bundle) throws RemoteException;

    void notifyProcessTerminate(int[] iArr, String str) throws RemoteException;

    void notifyProcessTerminateFinish(IOsenseEventCallback iOsenseEventCallback) throws RemoteException;

    void osenseClrCtrlData(String str) throws RemoteException;

    void osenseClrSceneAction(String str, long j) throws RemoteException;

    int osenseGetModeStatus(String str, int i) throws RemoteException;

    long[] osenseGetPerfLimit(String str) throws RemoteException;

    void osenseSetCtrlData(String str, OsenseCtrlDataRequest osenseCtrlDataRequest) throws RemoteException;

    void osenseSetNotification(String str, OsenseNotifyRequest osenseNotifyRequest) throws RemoteException;

    void osenseSetSceneAction(Bundle bundle) throws RemoteException;

    int registerEventCallback(IOsenseEventCallback iOsenseEventCallback, EventConfig eventConfig) throws RemoteException;

    void requestSceneAction(Bundle bundle) throws RemoteException;

    int unregisterEventCallback(IOsenseEventCallback iOsenseEventCallback) throws RemoteException;

    int unregisterEventCallbackWithConfig(IOsenseEventCallback iOsenseEventCallback, EventConfig eventConfig) throws RemoteException;

    void updateConfig(String str, Bundle bundle) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOsenseResManager {
        @Override // com.oplus.osense.IOsenseResManager
        public void athenaReqSceneAction(String featureType, Bundle bundle) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void updateConfig(String type, Bundle bundle) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void osenseSetSceneAction(Bundle bundle) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void osenseClrSceneAction(String identity, long handle) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void osenseSetNotification(String identity, OsenseNotifyRequest request) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void osenseSetCtrlData(String identity, OsenseCtrlDataRequest request) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void osenseClrCtrlData(String identity) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public int osenseGetModeStatus(String identity, int mode) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.osense.IOsenseResManager
        public long[] osenseGetPerfLimit(String identity) throws RemoteException {
            return null;
        }

        @Override // com.oplus.osense.IOsenseResManager
        public int registerEventCallback(IOsenseEventCallback callback, EventConfig eventConfig) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.osense.IOsenseResManager
        public int unregisterEventCallbackWithConfig(IOsenseEventCallback callback, EventConfig eventConfig) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.osense.IOsenseResManager
        public int unregisterEventCallback(IOsenseEventCallback callback) throws RemoteException {
            return 0;
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void notifyProcessTerminate(int[] pids, String reason) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void notifyProcessTerminateFinish(IOsenseEventCallback callback) throws RemoteException {
        }

        @Override // com.oplus.osense.IOsenseResManager
        public void requestSceneAction(Bundle bundle) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOsenseResManager {
        static final int TRANSACTION_athenaReqSceneAction = 1;
        static final int TRANSACTION_notifyProcessTerminate = 13;
        static final int TRANSACTION_notifyProcessTerminateFinish = 14;
        static final int TRANSACTION_osenseClrCtrlData = 7;
        static final int TRANSACTION_osenseClrSceneAction = 4;
        static final int TRANSACTION_osenseGetModeStatus = 8;
        static final int TRANSACTION_osenseGetPerfLimit = 9;
        static final int TRANSACTION_osenseSetCtrlData = 6;
        static final int TRANSACTION_osenseSetNotification = 5;
        static final int TRANSACTION_osenseSetSceneAction = 3;
        static final int TRANSACTION_registerEventCallback = 10;
        static final int TRANSACTION_requestSceneAction = 15;
        static final int TRANSACTION_unregisterEventCallback = 12;
        static final int TRANSACTION_unregisterEventCallbackWithConfig = 11;
        static final int TRANSACTION_updateConfig = 2;

        public Stub() {
            attachInterface(this, IOsenseResManager.DESCRIPTOR);
        }

        public static IOsenseResManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOsenseResManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOsenseResManager)) {
                return (IOsenseResManager) iin;
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
                    return "athenaReqSceneAction";
                case 2:
                    return "updateConfig";
                case 3:
                    return "osenseSetSceneAction";
                case 4:
                    return "osenseClrSceneAction";
                case 5:
                    return "osenseSetNotification";
                case 6:
                    return "osenseSetCtrlData";
                case 7:
                    return "osenseClrCtrlData";
                case 8:
                    return "osenseGetModeStatus";
                case 9:
                    return "osenseGetPerfLimit";
                case 10:
                    return "registerEventCallback";
                case 11:
                    return "unregisterEventCallbackWithConfig";
                case 12:
                    return "unregisterEventCallback";
                case 13:
                    return "notifyProcessTerminate";
                case 14:
                    return "notifyProcessTerminateFinish";
                case 15:
                    return "requestSceneAction";
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
                data.enforceInterface(IOsenseResManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOsenseResManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _arg0 = data.readString();
                            Bundle _arg1 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            athenaReqSceneAction(_arg0, _arg1);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            Bundle _arg12 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            updateConfig(_arg02, _arg12);
                            return true;
                        case 3:
                            Bundle _arg03 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            osenseSetSceneAction(_arg03);
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            long _arg13 = data.readLong();
                            data.enforceNoDataAvail();
                            osenseClrSceneAction(_arg04, _arg13);
                            return true;
                        case 5:
                            String _arg05 = data.readString();
                            OsenseNotifyRequest _arg14 = (OsenseNotifyRequest) data.readTypedObject(OsenseNotifyRequest.CREATOR);
                            data.enforceNoDataAvail();
                            osenseSetNotification(_arg05, _arg14);
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            OsenseCtrlDataRequest _arg15 = (OsenseCtrlDataRequest) data.readTypedObject(OsenseCtrlDataRequest.CREATOR);
                            data.enforceNoDataAvail();
                            osenseSetCtrlData(_arg06, _arg15);
                            return true;
                        case 7:
                            String _arg07 = data.readString();
                            data.enforceNoDataAvail();
                            osenseClrCtrlData(_arg07);
                            return true;
                        case 8:
                            String _arg08 = data.readString();
                            int _arg16 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result = osenseGetModeStatus(_arg08, _arg16);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 9:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            long[] _result2 = osenseGetPerfLimit(_arg09);
                            reply.writeNoException();
                            reply.writeLongArray(_result2);
                            return true;
                        case 10:
                            IOsenseEventCallback _arg010 = IOsenseEventCallback.Stub.asInterface(data.readStrongBinder());
                            EventConfig _arg17 = (EventConfig) data.readTypedObject(EventConfig.CREATOR);
                            data.enforceNoDataAvail();
                            int _result3 = registerEventCallback(_arg010, _arg17);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 11:
                            IOsenseEventCallback _arg011 = IOsenseEventCallback.Stub.asInterface(data.readStrongBinder());
                            EventConfig _arg18 = (EventConfig) data.readTypedObject(EventConfig.CREATOR);
                            data.enforceNoDataAvail();
                            int _result4 = unregisterEventCallbackWithConfig(_arg011, _arg18);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 12:
                            IOsenseEventCallback _arg012 = IOsenseEventCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            int _result5 = unregisterEventCallback(_arg012);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 13:
                            int[] _arg013 = data.createIntArray();
                            String _arg19 = data.readString();
                            data.enforceNoDataAvail();
                            notifyProcessTerminate(_arg013, _arg19);
                            reply.writeNoException();
                            return true;
                        case 14:
                            IOsenseEventCallback _arg014 = IOsenseEventCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            notifyProcessTerminateFinish(_arg014);
                            reply.writeNoException();
                            return true;
                        case 15:
                            Bundle _arg015 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            requestSceneAction(_arg015);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOsenseResManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOsenseResManager.DESCRIPTOR;
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void athenaReqSceneAction(String featureType, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeString(featureType);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void updateConfig(String type, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeString(type);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void osenseSetSceneAction(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void osenseClrSceneAction(String identity, long handle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeLong(handle);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void osenseSetNotification(String identity, OsenseNotifyRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeTypedObject(request, 0);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void osenseSetCtrlData(String identity, OsenseCtrlDataRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeTypedObject(request, 0);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void osenseClrCtrlData(String identity) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeString(identity);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public int osenseGetModeStatus(String identity, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeString(identity);
                    _data.writeInt(mode);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public long[] osenseGetPerfLimit(String identity) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeString(identity);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    long[] _result = _reply.createLongArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public int registerEventCallback(IOsenseEventCallback callback, EventConfig eventConfig) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    _data.writeTypedObject(eventConfig, 0);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public int unregisterEventCallbackWithConfig(IOsenseEventCallback callback, EventConfig eventConfig) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    _data.writeTypedObject(eventConfig, 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public int unregisterEventCallback(IOsenseEventCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void notifyProcessTerminate(int[] pids, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    _data.writeString(reason);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void notifyProcessTerminateFinish(IOsenseEventCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.osense.IOsenseResManager
            public void requestSceneAction(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOsenseResManager.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 14;
        }
    }
}
