package android.os;

import com.oplus.onetrace.entities.TaskInfo;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusTraceCallBack extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusTraceCallBack";

    void onDataChanged(SharedMemory sharedMemory, int i) throws RemoteException;

    void onDataListChanged(SharedMemory[] sharedMemoryArr, String str) throws RemoteException;

    void onProcessReused(List<TaskInfo> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusTraceCallBack {
        @Override // android.os.IOplusTraceCallBack
        public void onDataChanged(SharedMemory data, int contentSize) throws RemoteException {
        }

        @Override // android.os.IOplusTraceCallBack
        public void onProcessReused(List<TaskInfo> tasks) throws RemoteException {
        }

        @Override // android.os.IOplusTraceCallBack
        public void onDataListChanged(SharedMemory[] dataArray, String cookie) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusTraceCallBack {
        static final int TRANSACTION_onDataChanged = 1;
        static final int TRANSACTION_onDataListChanged = 3;
        static final int TRANSACTION_onProcessReused = 2;

        public Stub() {
            attachInterface(this, IOplusTraceCallBack.DESCRIPTOR);
        }

        public static IOplusTraceCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusTraceCallBack.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusTraceCallBack)) {
                return (IOplusTraceCallBack) iin;
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
                    return "onDataChanged";
                case 2:
                    return "onProcessReused";
                case 3:
                    return "onDataListChanged";
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
                data.enforceInterface(IOplusTraceCallBack.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusTraceCallBack.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            SharedMemory _arg0 = (SharedMemory) data.readTypedObject(SharedMemory.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            onDataChanged(_arg0, _arg1);
                            return true;
                        case 2:
                            List<TaskInfo> _arg02 = data.createTypedArrayList(TaskInfo.CREATOR);
                            data.enforceNoDataAvail();
                            onProcessReused(_arg02);
                            return true;
                        case 3:
                            SharedMemory[] _arg03 = (SharedMemory[]) data.createTypedArray(SharedMemory.CREATOR);
                            String _arg12 = data.readString();
                            data.enforceNoDataAvail();
                            onDataListChanged(_arg03, _arg12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusTraceCallBack {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusTraceCallBack.DESCRIPTOR;
            }

            @Override // android.os.IOplusTraceCallBack
            public void onDataChanged(SharedMemory data, int contentSize) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceCallBack.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    _data.writeInt(contentSize);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceCallBack
            public void onProcessReused(List<TaskInfo> tasks) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceCallBack.DESCRIPTOR);
                    _data.writeTypedList(tasks, 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceCallBack
            public void onDataListChanged(SharedMemory[] dataArray, String cookie) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceCallBack.DESCRIPTOR);
                    _data.writeTypedArray(dataArray, 0);
                    _data.writeString(cookie);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 2;
        }
    }
}
