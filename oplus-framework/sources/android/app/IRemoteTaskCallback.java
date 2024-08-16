package android.app;

import android.app.IServiceRemoteTaskInstance;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IRemoteTaskCallback extends IInterface {
    public static final String DESCRIPTOR = "android.app.IRemoteTaskCallback";

    void activateRemoteTask(IServiceRemoteTaskInstance iServiceRemoteTaskInstance) throws RemoteException;

    void notifyClientDied() throws RemoteException;

    void notifyDeviceAvailabilityStateChanged(int i) throws RemoteException;

    void notifyDisplaySwitched(IServiceRemoteTaskInstance iServiceRemoteTaskInstance, Intent intent, int i) throws RemoteException;

    void notifyDuplicateRemoteTask(Intent intent, String str, String str2, int i) throws RemoteException;

    void notifyRemoteShowingSecuredContentChanged(IServiceRemoteTaskInstance iServiceRemoteTaskInstance, boolean z) throws RemoteException;

    void notifyRemoteTaskCreationFailed(String str, int i) throws RemoteException;

    void notifyRemoteTaskEmptyUUIDetected(Intent intent) throws RemoteException;

    void notifyRemoteTaskRemoved(String str, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IRemoteTaskCallback {
        @Override // android.app.IRemoteTaskCallback
        public void activateRemoteTask(IServiceRemoteTaskInstance remoteTaskInstance) throws RemoteException {
        }

        @Override // android.app.IRemoteTaskCallback
        public void notifyRemoteTaskRemoved(String uuid, int reason) throws RemoteException {
        }

        @Override // android.app.IRemoteTaskCallback
        public void notifyDisplaySwitched(IServiceRemoteTaskInstance handler, Intent intent, int interceptReason) throws RemoteException {
        }

        @Override // android.app.IRemoteTaskCallback
        public void notifyDeviceAvailabilityStateChanged(int deviceAvailabilityState) throws RemoteException {
        }

        @Override // android.app.IRemoteTaskCallback
        public void notifyRemoteTaskEmptyUUIDetected(Intent intent) throws RemoteException {
        }

        @Override // android.app.IRemoteTaskCallback
        public void notifyRemoteTaskCreationFailed(String uuid, int reason) throws RemoteException {
        }

        @Override // android.app.IRemoteTaskCallback
        public void notifyRemoteShowingSecuredContentChanged(IServiceRemoteTaskInstance instance, boolean isShowingSecuredContent) throws RemoteException {
        }

        @Override // android.app.IRemoteTaskCallback
        public void notifyDuplicateRemoteTask(Intent intent, String needFocusUuid, String interceptUuid, int reason) throws RemoteException {
        }

        @Override // android.app.IRemoteTaskCallback
        public void notifyClientDied() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IRemoteTaskCallback {
        static final int TRANSACTION_activateRemoteTask = 1;
        static final int TRANSACTION_notifyClientDied = 9;
        static final int TRANSACTION_notifyDeviceAvailabilityStateChanged = 4;
        static final int TRANSACTION_notifyDisplaySwitched = 3;
        static final int TRANSACTION_notifyDuplicateRemoteTask = 8;
        static final int TRANSACTION_notifyRemoteShowingSecuredContentChanged = 7;
        static final int TRANSACTION_notifyRemoteTaskCreationFailed = 6;
        static final int TRANSACTION_notifyRemoteTaskEmptyUUIDetected = 5;
        static final int TRANSACTION_notifyRemoteTaskRemoved = 2;

        public Stub() {
            attachInterface(this, IRemoteTaskCallback.DESCRIPTOR);
        }

        public static IRemoteTaskCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IRemoteTaskCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IRemoteTaskCallback)) {
                return (IRemoteTaskCallback) iin;
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
                    return "activateRemoteTask";
                case 2:
                    return "notifyRemoteTaskRemoved";
                case 3:
                    return "notifyDisplaySwitched";
                case 4:
                    return "notifyDeviceAvailabilityStateChanged";
                case 5:
                    return "notifyRemoteTaskEmptyUUIDetected";
                case 6:
                    return "notifyRemoteTaskCreationFailed";
                case 7:
                    return "notifyRemoteShowingSecuredContentChanged";
                case 8:
                    return "notifyDuplicateRemoteTask";
                case 9:
                    return "notifyClientDied";
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
                data.enforceInterface(IRemoteTaskCallback.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IRemoteTaskCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IServiceRemoteTaskInstance _arg0 = IServiceRemoteTaskInstance.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            activateRemoteTask(_arg0);
                            return true;
                        case 2:
                            String _arg02 = data.readString();
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyRemoteTaskRemoved(_arg02, _arg1);
                            return true;
                        case 3:
                            IServiceRemoteTaskInstance _arg03 = IServiceRemoteTaskInstance.Stub.asInterface(data.readStrongBinder());
                            Intent _arg12 = (Intent) data.readTypedObject(Intent.CREATOR);
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyDisplaySwitched(_arg03, _arg12, _arg2);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyDeviceAvailabilityStateChanged(_arg04);
                            return true;
                        case 5:
                            Intent _arg05 = (Intent) data.readTypedObject(Intent.CREATOR);
                            data.enforceNoDataAvail();
                            notifyRemoteTaskEmptyUUIDetected(_arg05);
                            return true;
                        case 6:
                            String _arg06 = data.readString();
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyRemoteTaskCreationFailed(_arg06, _arg13);
                            return true;
                        case 7:
                            IServiceRemoteTaskInstance _arg07 = IServiceRemoteTaskInstance.Stub.asInterface(data.readStrongBinder());
                            boolean _arg14 = data.readBoolean();
                            data.enforceNoDataAvail();
                            notifyRemoteShowingSecuredContentChanged(_arg07, _arg14);
                            return true;
                        case 8:
                            Intent _arg08 = (Intent) data.readTypedObject(Intent.CREATOR);
                            String _arg15 = data.readString();
                            String _arg22 = data.readString();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            notifyDuplicateRemoteTask(_arg08, _arg15, _arg22, _arg3);
                            return true;
                        case 9:
                            notifyClientDied();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRemoteTaskCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IRemoteTaskCallback.DESCRIPTOR;
            }

            @Override // android.app.IRemoteTaskCallback
            public void activateRemoteTask(IServiceRemoteTaskInstance remoteTaskInstance) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    _data.writeStrongInterface(remoteTaskInstance);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IRemoteTaskCallback
            public void notifyRemoteTaskRemoved(String uuid, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(reason);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IRemoteTaskCallback
            public void notifyDisplaySwitched(IServiceRemoteTaskInstance handler, Intent intent, int interceptReason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    _data.writeStrongInterface(handler);
                    _data.writeTypedObject(intent, 0);
                    _data.writeInt(interceptReason);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IRemoteTaskCallback
            public void notifyDeviceAvailabilityStateChanged(int deviceAvailabilityState) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    _data.writeInt(deviceAvailabilityState);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IRemoteTaskCallback
            public void notifyRemoteTaskEmptyUUIDetected(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IRemoteTaskCallback
            public void notifyRemoteTaskCreationFailed(String uuid, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(reason);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IRemoteTaskCallback
            public void notifyRemoteShowingSecuredContentChanged(IServiceRemoteTaskInstance instance, boolean isShowingSecuredContent) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    _data.writeStrongInterface(instance);
                    _data.writeBoolean(isShowingSecuredContent);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IRemoteTaskCallback
            public void notifyDuplicateRemoteTask(Intent intent, String needFocusUuid, String interceptUuid, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    _data.writeTypedObject(intent, 0);
                    _data.writeString(needFocusUuid);
                    _data.writeString(interceptUuid);
                    _data.writeInt(reason);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IRemoteTaskCallback
            public void notifyClientDied() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IRemoteTaskCallback.DESCRIPTOR);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 8;
        }
    }
}
