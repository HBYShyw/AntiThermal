package android.os;

import android.os.IOplusFilterListener;
import android.os.IOplusTraceCallBack;
import com.oplus.onetrace.entities.TaskInfo;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IOplusTraceService extends IInterface {
    public static final String DESCRIPTOR = "android.os.IOplusTraceService";

    void callUpdateContentFilter(int i, IOplusFilterListener iOplusFilterListener) throws RemoteException;

    boolean flushTraceBuffer() throws RemoteException;

    List<TaskInfo> getProcessTree() throws RemoteException;

    void handleShmemAsync(SharedMemory sharedMemory, int i, int i2, long j) throws RemoteException;

    void handleShmemWithMapAsync(SharedMemory sharedMemory, int i, int i2, String str, ThreadMap threadMap, long j) throws RemoteException;

    void handleTraceShmemBuffer(SharedMemory sharedMemory, int i) throws RemoteException;

    SharedMemory obtainMemoryCache(String str, int i, IOplusFilterListener iOplusFilterListener) throws RemoteException;

    SharedMemory obtainSharedMemory(String str) throws RemoteException;

    boolean registerCallBack(IOplusTraceCallBack iOplusTraceCallBack) throws RemoteException;

    boolean registerCallBack2(IOplusTraceCallBack iOplusTraceCallBack, int i) throws RemoteException;

    long registerSharedMemory(SharedMemory sharedMemory) throws RemoteException;

    void unregisterCallBack(IOplusTraceCallBack iOplusTraceCallBack) throws RemoteException;

    void unregisterCallBack2(IOplusTraceCallBack iOplusTraceCallBack, boolean z, String str) throws RemoteException;

    void updateContentFilterList(Map map) throws RemoteException;

    void updateProcessWhitelist(String[] strArr) throws RemoteException;

    void uploadProcessTree(int i, String str, Map map) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusTraceService {
        @Override // android.os.IOplusTraceService
        public boolean registerCallBack(IOplusTraceCallBack callBack) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusTraceService
        public void unregisterCallBack(IOplusTraceCallBack callBack) throws RemoteException {
        }

        @Override // android.os.IOplusTraceService
        public void handleTraceShmemBuffer(SharedMemory data, int contentSize) throws RemoteException {
        }

        @Override // android.os.IOplusTraceService
        public SharedMemory obtainSharedMemory(String processName) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusTraceService
        public boolean flushTraceBuffer() throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusTraceService
        public void updateProcessWhitelist(String[] processNameList) throws RemoteException {
        }

        @Override // android.os.IOplusTraceService
        public SharedMemory obtainMemoryCache(String processName, int filterHashCode, IOplusFilterListener listener) throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusTraceService
        public void updateContentFilterList(Map contentFilter) throws RemoteException {
        }

        @Override // android.os.IOplusTraceService
        public void uploadProcessTree(int pid, String processName, Map threadMap) throws RemoteException {
        }

        @Override // android.os.IOplusTraceService
        public List<TaskInfo> getProcessTree() throws RemoteException {
            return null;
        }

        @Override // android.os.IOplusTraceService
        public void callUpdateContentFilter(int filterHashCode, IOplusFilterListener listener) throws RemoteException {
        }

        @Override // android.os.IOplusTraceService
        public void handleShmemWithMapAsync(SharedMemory data, int contentSize, int pid, String processName, ThreadMap threadMap, long token) throws RemoteException {
        }

        @Override // android.os.IOplusTraceService
        public void handleShmemAsync(SharedMemory data, int contentSize, int pid, long token) throws RemoteException {
        }

        @Override // android.os.IOplusTraceService
        public long registerSharedMemory(SharedMemory data) throws RemoteException {
            return 0L;
        }

        @Override // android.os.IOplusTraceService
        public boolean registerCallBack2(IOplusTraceCallBack callBack, int flag) throws RemoteException {
            return false;
        }

        @Override // android.os.IOplusTraceService
        public void unregisterCallBack2(IOplusTraceCallBack callBack, boolean forceFlush, String cookie) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusTraceService {
        static final int TRANSACTION_callUpdateContentFilter = 11;
        static final int TRANSACTION_flushTraceBuffer = 5;
        static final int TRANSACTION_getProcessTree = 10;
        static final int TRANSACTION_handleShmemAsync = 13;
        static final int TRANSACTION_handleShmemWithMapAsync = 12;
        static final int TRANSACTION_handleTraceShmemBuffer = 3;
        static final int TRANSACTION_obtainMemoryCache = 7;
        static final int TRANSACTION_obtainSharedMemory = 4;
        static final int TRANSACTION_registerCallBack = 1;
        static final int TRANSACTION_registerCallBack2 = 15;
        static final int TRANSACTION_registerSharedMemory = 14;
        static final int TRANSACTION_unregisterCallBack = 2;
        static final int TRANSACTION_unregisterCallBack2 = 16;
        static final int TRANSACTION_updateContentFilterList = 8;
        static final int TRANSACTION_updateProcessWhitelist = 6;
        static final int TRANSACTION_uploadProcessTree = 9;

        public Stub() {
            attachInterface(this, IOplusTraceService.DESCRIPTOR);
        }

        public static IOplusTraceService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusTraceService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusTraceService)) {
                return (IOplusTraceService) iin;
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
                    return "registerCallBack";
                case 2:
                    return "unregisterCallBack";
                case 3:
                    return "handleTraceShmemBuffer";
                case 4:
                    return "obtainSharedMemory";
                case 5:
                    return "flushTraceBuffer";
                case 6:
                    return "updateProcessWhitelist";
                case 7:
                    return "obtainMemoryCache";
                case 8:
                    return "updateContentFilterList";
                case 9:
                    return "uploadProcessTree";
                case 10:
                    return "getProcessTree";
                case 11:
                    return "callUpdateContentFilter";
                case 12:
                    return "handleShmemWithMapAsync";
                case 13:
                    return "handleShmemAsync";
                case 14:
                    return "registerSharedMemory";
                case 15:
                    return "registerCallBack2";
                case 16:
                    return "unregisterCallBack2";
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
                data.enforceInterface(IOplusTraceService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusTraceService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusTraceCallBack _arg0 = IOplusTraceCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            boolean _result = registerCallBack(_arg0);
                            reply.writeNoException();
                            reply.writeBoolean(_result);
                            return true;
                        case 2:
                            IOplusTraceCallBack _arg02 = IOplusTraceCallBack.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterCallBack(_arg02);
                            reply.writeNoException();
                            return true;
                        case 3:
                            SharedMemory _arg03 = (SharedMemory) data.readTypedObject(SharedMemory.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            handleTraceShmemBuffer(_arg03, _arg1);
                            reply.writeNoException();
                            return true;
                        case 4:
                            String _arg04 = data.readString();
                            data.enforceNoDataAvail();
                            SharedMemory _result2 = obtainSharedMemory(_arg04);
                            reply.writeNoException();
                            reply.writeTypedObject(_result2, 1);
                            return true;
                        case 5:
                            boolean _result3 = flushTraceBuffer();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 6:
                            String[] _arg05 = data.createStringArray();
                            data.enforceNoDataAvail();
                            updateProcessWhitelist(_arg05);
                            return true;
                        case 7:
                            String _arg06 = data.readString();
                            int _arg12 = data.readInt();
                            IOplusFilterListener _arg2 = IOplusFilterListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            SharedMemory _result4 = obtainMemoryCache(_arg06, _arg12, _arg2);
                            reply.writeNoException();
                            reply.writeTypedObject(_result4, 1);
                            return true;
                        case 8:
                            ClassLoader cl = getClass().getClassLoader();
                            Map _arg07 = data.readHashMap(cl);
                            data.enforceNoDataAvail();
                            updateContentFilterList(_arg07);
                            return true;
                        case 9:
                            int _arg08 = data.readInt();
                            String _arg13 = data.readString();
                            ClassLoader cl2 = getClass().getClassLoader();
                            Map _arg22 = data.readHashMap(cl2);
                            data.enforceNoDataAvail();
                            uploadProcessTree(_arg08, _arg13, _arg22);
                            return true;
                        case 10:
                            List<TaskInfo> _result5 = getProcessTree();
                            reply.writeNoException();
                            reply.writeTypedList(_result5, 1);
                            return true;
                        case 11:
                            int _arg09 = data.readInt();
                            IOplusFilterListener _arg14 = IOplusFilterListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            callUpdateContentFilter(_arg09, _arg14);
                            return true;
                        case 12:
                            SharedMemory _arg010 = (SharedMemory) data.readTypedObject(SharedMemory.CREATOR);
                            int _arg15 = data.readInt();
                            int _arg23 = data.readInt();
                            String _arg3 = data.readString();
                            ThreadMap _arg4 = (ThreadMap) data.readTypedObject(ThreadMap.CREATOR);
                            long _arg5 = data.readLong();
                            data.enforceNoDataAvail();
                            handleShmemWithMapAsync(_arg010, _arg15, _arg23, _arg3, _arg4, _arg5);
                            return true;
                        case 13:
                            SharedMemory _arg011 = (SharedMemory) data.readTypedObject(SharedMemory.CREATOR);
                            int _arg16 = data.readInt();
                            int _arg24 = data.readInt();
                            long _arg32 = data.readLong();
                            data.enforceNoDataAvail();
                            handleShmemAsync(_arg011, _arg16, _arg24, _arg32);
                            return true;
                        case 14:
                            SharedMemory _arg012 = (SharedMemory) data.readTypedObject(SharedMemory.CREATOR);
                            data.enforceNoDataAvail();
                            long _result6 = registerSharedMemory(_arg012);
                            reply.writeNoException();
                            reply.writeLong(_result6);
                            return true;
                        case 15:
                            IOplusTraceCallBack _arg013 = IOplusTraceCallBack.Stub.asInterface(data.readStrongBinder());
                            int _arg17 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result7 = registerCallBack2(_arg013, _arg17);
                            reply.writeNoException();
                            reply.writeBoolean(_result7);
                            return true;
                        case 16:
                            IOplusTraceCallBack _arg014 = IOplusTraceCallBack.Stub.asInterface(data.readStrongBinder());
                            boolean _arg18 = data.readBoolean();
                            String _arg25 = data.readString();
                            data.enforceNoDataAvail();
                            unregisterCallBack2(_arg014, _arg18, _arg25);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusTraceService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusTraceService.DESCRIPTOR;
            }

            @Override // android.os.IOplusTraceService
            public boolean registerCallBack(IOplusTraceCallBack callBack) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void unregisterCallBack(IOplusTraceCallBack callBack) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void handleTraceShmemBuffer(SharedMemory data, int contentSize) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    _data.writeInt(contentSize);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public SharedMemory obtainSharedMemory(String processName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeString(processName);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    SharedMemory _result = (SharedMemory) _reply.readTypedObject(SharedMemory.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public boolean flushTraceBuffer() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void updateProcessWhitelist(String[] processNameList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeStringArray(processNameList);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public SharedMemory obtainMemoryCache(String processName, int filterHashCode, IOplusFilterListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(filterHashCode);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    SharedMemory _result = (SharedMemory) _reply.readTypedObject(SharedMemory.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void updateContentFilterList(Map contentFilter) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeMap(contentFilter);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void uploadProcessTree(int pid, String processName, Map threadMap) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeString(processName);
                    _data.writeMap(threadMap);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public List<TaskInfo> getProcessTree() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    List<TaskInfo> _result = _reply.createTypedArrayList(TaskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void callUpdateContentFilter(int filterHashCode, IOplusFilterListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeInt(filterHashCode);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void handleShmemWithMapAsync(SharedMemory data, int contentSize, int pid, String processName, ThreadMap threadMap, long token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    _data.writeInt(contentSize);
                    _data.writeInt(pid);
                    _data.writeString(processName);
                    _data.writeTypedObject(threadMap, 0);
                    _data.writeLong(token);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void handleShmemAsync(SharedMemory data, int contentSize, int pid, long token) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    _data.writeInt(contentSize);
                    _data.writeInt(pid);
                    _data.writeLong(token);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public long registerSharedMemory(SharedMemory data) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public boolean registerCallBack2(IOplusTraceCallBack callBack, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    _data.writeInt(flag);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IOplusTraceService
            public void unregisterCallBack2(IOplusTraceCallBack callBack, boolean forceFlush, String cookie) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTraceService.DESCRIPTOR);
                    _data.writeStrongInterface(callBack);
                    _data.writeBoolean(forceFlush);
                    _data.writeString(cookie);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 15;
        }
    }
}
