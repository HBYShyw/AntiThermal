package com.oplus.eap;

import android.app.ApplicationExitInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SharedMemory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IExceptionMonitorService extends IInterface {
    public static final String DESCRIPTOR = "IExceptionMonitorService";

    void initExceptionIdList(List<ExceptionIdentification> list) throws RemoteException;

    void initExceptionIdMap(Map map) throws RemoteException;

    List<ExceptionIdentification> initExitInfoIdList() throws RemoteException;

    void initExitInfoIdMap(Map map) throws RemoteException;

    ExceptionIdentification onAppException(SharedMemory sharedMemory) throws RemoteException;

    ExceptionIdentification onAppExit(ApplicationExitInfo applicationExitInfo) throws RemoteException;

    void onExceptionIdAdjust(ExceptionIdentification exceptionIdentification) throws RemoteException;

    void onUploadExitInfoList() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IExceptionMonitorService {
        @Override // com.oplus.eap.IExceptionMonitorService
        public ExceptionIdentification onAppException(SharedMemory data) throws RemoteException {
            return null;
        }

        @Override // com.oplus.eap.IExceptionMonitorService
        public ExceptionIdentification onAppExceptionBundle(Bundle data) throws RemoteException {
            return null;
        }

        @Override // com.oplus.eap.IExceptionMonitorService
        public void onExceptionIdAdjust(ExceptionIdentification exceptionId) throws RemoteException {
        }

        @Override // com.oplus.eap.IExceptionMonitorService
        public void initExceptionIdList(List<ExceptionIdentification> list) throws RemoteException {
        }

        @Override // com.oplus.eap.IExceptionMonitorService
        public void initExceptionIdMap(Map map) throws RemoteException {
        }

        @Override // com.oplus.eap.IExceptionMonitorService
        public ExceptionIdentification onAppExit(ApplicationExitInfo data) throws RemoteException {
            return null;
        }

        @Override // com.oplus.eap.IExceptionMonitorService
        public void onUploadExitInfoList() throws RemoteException {
        }

        @Override // com.oplus.eap.IExceptionMonitorService
        public List<ExceptionIdentification> initExitInfoIdList() throws RemoteException {
            return null;
        }

        @Override // com.oplus.eap.IExceptionMonitorService
        public void initExitInfoIdMap(Map map) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IExceptionMonitorService {
        static final int TRANSACTION_GETVERSION = 10;
        static final int TRANSACTION_INITEXCEPTIONIDLIST = 3;
        static final int TRANSACTION_INITEXCEPTIONIDMAP = 4;
        static final int TRANSACTION_INITEXITINFOIDLIST = 7;
        static final int TRANSACTION_INITEXITINFOIDMAP = 8;
        static final int TRANSACTION_ONAPPEXCEPTION = 1;
        static final int TRANSACTION_ONAPPEXCEPTIONBUNDLE = 9;
        static final int TRANSACTION_ONAPPEXIT = 5;
        static final int TRANSACTION_ONEXCEPTIONIDADJUST = 2;
        static final int TRANSACTION_ONUPLOADEXITINFOLIST = 6;

        public Stub() {
            attachInterface(this, IExceptionMonitorService.DESCRIPTOR);
        }

        public static IExceptionMonitorService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IExceptionMonitorService.DESCRIPTOR);
            if (iin != null && (iin instanceof IExceptionMonitorService)) {
                return (IExceptionMonitorService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    return transOnAppException(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 2:
                    return transOnExceptionIdAdjust(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 3:
                    return transInitExceptionIdList(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 4:
                    return transInitExceptionIdMap(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 5:
                    return transOnAppExit(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 6:
                    return transOnUploadExitInfoList(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 7:
                    return transinitExitInfoIdList(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 8:
                    return transInitExitInfoIdMap(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 9:
                    return transOnAppExceptionBundle(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 10:
                    return transGetVersion(data, reply, IExceptionMonitorService.DESCRIPTOR);
                case 1598968902:
                    reply.writeString(IExceptionMonitorService.DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private boolean transGetVersion(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            data.enforceInterface(descriptor);
            int result = getVersion();
            reply.writeNoException();
            reply.writeInt(result);
            return true;
        }

        private boolean transOnAppExceptionBundle(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            data.enforceInterface(descriptor);
            Bundle arg0 = new Bundle();
            if (data.readInt() != 0) {
                arg0 = (Bundle) Bundle.CREATOR.createFromParcel(data);
            }
            ExceptionIdentification result = onAppExceptionBundle(arg0);
            reply.writeNoException();
            if (result != null) {
                reply.writeInt(1);
                result.writeToParcel(reply, 1);
            } else {
                reply.writeInt(0);
            }
            return true;
        }

        private boolean transInitExitInfoIdMap(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            data.enforceInterface(descriptor);
            new HashMap();
            ClassLoader cl = getClass().getClassLoader();
            Map arg0 = data.readHashMap(cl);
            initExitInfoIdMap(arg0);
            reply.writeNoException();
            return true;
        }

        private boolean transinitExitInfoIdList(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            data.enforceInterface(descriptor);
            List<ExceptionIdentification> result = initExitInfoIdList();
            reply.writeNoException();
            reply.writeTypedList(result);
            return true;
        }

        private boolean transOnUploadExitInfoList(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            data.enforceInterface(descriptor);
            onUploadExitInfoList();
            reply.writeNoException();
            return true;
        }

        private boolean transOnAppExit(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            ApplicationExitInfo arg0;
            data.enforceInterface(descriptor);
            new ApplicationExitInfo();
            if (data.readInt() != 0) {
                arg0 = (ApplicationExitInfo) ApplicationExitInfo.CREATOR.createFromParcel(data);
            } else {
                arg0 = null;
            }
            ExceptionIdentification result = onAppExit(arg0);
            reply.writeNoException();
            if (result != null) {
                reply.writeInt(1);
                result.writeToParcel(reply, 1);
            } else {
                reply.writeInt(0);
            }
            return true;
        }

        private boolean transInitExceptionIdMap(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            data.enforceInterface(descriptor);
            new HashMap();
            ClassLoader cl = getClass().getClassLoader();
            Map arg0 = data.readHashMap(cl);
            initExceptionIdMap(arg0);
            reply.writeNoException();
            return true;
        }

        private boolean transInitExceptionIdList(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            data.enforceInterface(descriptor);
            new ArrayList();
            List<ExceptionIdentification> arg0 = data.createTypedArrayList(ExceptionIdentification.CREATOR);
            initExceptionIdList(arg0);
            reply.writeNoException();
            return true;
        }

        private boolean transOnAppException(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            data.enforceInterface(descriptor);
            SharedMemory arg0 = null;
            if (data.readInt() != 0) {
                arg0 = (SharedMemory) SharedMemory.CREATOR.createFromParcel(data);
            }
            ExceptionIdentification result = onAppException(arg0);
            reply.writeNoException();
            if (result != null) {
                reply.writeInt(1);
                result.writeToParcel(reply, 1);
            } else {
                reply.writeInt(0);
            }
            return true;
        }

        private boolean transOnExceptionIdAdjust(Parcel data, Parcel reply, String descriptor) throws RemoteException {
            ExceptionIdentification arg0;
            data.enforceInterface(descriptor);
            if (data.readInt() != 0) {
                ExceptionIdentification arg02 = ExceptionIdentification.CREATOR.createFromParcel(data);
                arg0 = arg02;
            } else {
                arg0 = null;
            }
            onExceptionIdAdjust(arg0);
            reply.writeNoException();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IExceptionMonitorService {
            private static IExceptionMonitorService sDefaultImpl;
            private IBinder mRemote;

            private Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IExceptionMonitorService.DESCRIPTOR;
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public ExceptionIdentification onAppException(SharedMemory smdata) throws RemoteException {
                ExceptionIdentification result;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    if (smdata != null) {
                        data.writeInt(1);
                        smdata.writeToParcel(data, 0);
                    } else {
                        data.writeInt(0);
                    }
                    boolean status = this.mRemote.transact(1, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onAppException(smdata);
                    }
                    reply.readException();
                    if (reply.readInt() != 0) {
                        result = ExceptionIdentification.CREATOR.createFromParcel(reply);
                    } else {
                        result = null;
                    }
                    return result;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public ExceptionIdentification onAppExceptionBundle(Bundle bdata) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    if (bdata != null) {
                        data.writeInt(1);
                        bdata.writeToParcel(data, 0);
                    } else {
                        data.writeInt(0);
                    }
                    boolean status = this.mRemote.transact(9, data, reply, 1);
                    reply.readException();
                    if (!status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onAppExceptionBundle(bdata);
                    }
                    reply.recycle();
                    data.recycle();
                    return null;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public int getVersion() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    boolean status = this.mRemote.transact(10, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVersion();
                    }
                    reply.readException();
                    int result = reply.readInt();
                    return result;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public void onExceptionIdAdjust(ExceptionIdentification exceptionId) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    if (exceptionId != null) {
                        data.writeInt(1);
                        exceptionId.writeToParcel(data, 0);
                    } else {
                        data.writeInt(0);
                    }
                    boolean status = this.mRemote.transact(2, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onExceptionIdAdjust(exceptionId);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public void initExceptionIdList(List<ExceptionIdentification> list) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    data.writeTypedList(list);
                    boolean status = this.mRemote.transact(3, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initExceptionIdList(list);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public void initExceptionIdMap(Map map) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    data.writeMap(map);
                    boolean status = this.mRemote.transact(4, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initExceptionIdMap(map);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public ExceptionIdentification onAppExit(ApplicationExitInfo aedata) throws RemoteException {
                ExceptionIdentification result;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    if (aedata != null) {
                        data.writeInt(1);
                        aedata.writeToParcel(data, 0);
                    } else {
                        data.writeInt(0);
                    }
                    boolean status = this.mRemote.transact(5, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onAppExit(aedata);
                    }
                    reply.readException();
                    if (reply.readInt() != 0) {
                        result = ExceptionIdentification.CREATOR.createFromParcel(reply);
                    } else {
                        result = null;
                    }
                    return result;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public void onUploadExitInfoList() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    boolean status = this.mRemote.transact(6, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onUploadExitInfoList();
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public List<ExceptionIdentification> initExitInfoIdList() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                new ArrayList();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    boolean status = this.mRemote.transact(7, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().initExitInfoIdList();
                    }
                    reply.readException();
                    List<ExceptionIdentification> result = reply.createTypedArrayList(ExceptionIdentification.CREATOR);
                    return result;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override // com.oplus.eap.IExceptionMonitorService
            public void initExitInfoIdMap(Map map) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(IExceptionMonitorService.DESCRIPTOR);
                    data.writeMap(map);
                    boolean status = this.mRemote.transact(8, data, reply, 0);
                    if (!status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initExitInfoIdMap(map);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IExceptionMonitorService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IExceptionMonitorService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    default ExceptionIdentification onAppExceptionBundle(Bundle data) throws RemoteException {
        return null;
    }

    default int getVersion() throws RemoteException {
        return 0;
    }
}
