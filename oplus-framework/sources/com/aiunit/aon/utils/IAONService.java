package com.aiunit.aon.utils;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.oplus.Telephony;
import android.view.Surface;
import com.aiunit.aon.utils.IAONEventListener;
import com.aiunit.aon.utils.core.SensorBmp;
import java.util.List;

/* loaded from: classes.dex */
public interface IAONService extends IInterface {
    public static final String DESCRIPTOR = "com.aiunit.aon.utils.IAONService";

    int enroll(int i, String str, Surface surface) throws RemoteException;

    int getEnrolledFaceCount(int i, String str) throws RemoteException;

    String getServerState() throws RemoteException;

    String getVersionInfo() throws RemoteException;

    int notifyAON(int i, Bundle bundle) throws RemoteException;

    int process(List<SensorBmp> list) throws RemoteException;

    int registerListener(IAONEventListener iAONEventListener, int i) throws RemoteException;

    int remove(int i, String str) throws RemoteException;

    int setGestureCourseState(int i) throws RemoteException;

    int start(int i) throws RemoteException;

    int stop(int i) throws RemoteException;

    int unRegisterListener(IAONEventListener iAONEventListener, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IAONService {
        @Override // com.aiunit.aon.utils.IAONService
        public String getServerState() throws RemoteException {
            return null;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public String getVersionInfo() throws RemoteException {
            return null;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int start(int AonCmdType) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int stop(int AonCmdType) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int registerListener(IAONEventListener listener, int AonCmdType) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int unRegisterListener(IAONEventListener listener, int AonCmdType) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int setGestureCourseState(int state) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int getEnrolledFaceCount(int userId, String opPackageName) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int enroll(int userId, String opPackageName, Surface previewSurface) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int remove(int userId, String opPackageName) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int notifyAON(int type, Bundle bundle) throws RemoteException {
            return 0;
        }

        @Override // com.aiunit.aon.utils.IAONService
        public int process(List<SensorBmp> sensorBmps) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAONService {
        static final int TRANSACTION_enroll = 9;
        static final int TRANSACTION_getEnrolledFaceCount = 8;
        static final int TRANSACTION_getServerState = 1;
        static final int TRANSACTION_getVersionInfo = 2;
        static final int TRANSACTION_notifyAON = 11;
        static final int TRANSACTION_process = 12;
        static final int TRANSACTION_registerListener = 5;
        static final int TRANSACTION_remove = 10;
        static final int TRANSACTION_setGestureCourseState = 7;
        static final int TRANSACTION_start = 3;
        static final int TRANSACTION_stop = 4;
        static final int TRANSACTION_unRegisterListener = 6;

        public Stub() {
            attachInterface(this, IAONService.DESCRIPTOR);
        }

        public static IAONService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAONService.DESCRIPTOR);
            if (iin != null && (iin instanceof IAONService)) {
                return (IAONService) iin;
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
                    return "getServerState";
                case 2:
                    return "getVersionInfo";
                case 3:
                    return Telephony.BaseMmsColumns.START;
                case 4:
                    return "stop";
                case 5:
                    return "registerListener";
                case 6:
                    return "unRegisterListener";
                case 7:
                    return "setGestureCourseState";
                case 8:
                    return "getEnrolledFaceCount";
                case 9:
                    return "enroll";
                case 10:
                    return "remove";
                case 11:
                    return "notifyAON";
                case 12:
                    return "process";
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
                data.enforceInterface(IAONService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IAONService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            String _result = getServerState();
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 2:
                            String _result2 = getVersionInfo();
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 3:
                            int _arg0 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result3 = start(_arg0);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 4:
                            int _arg02 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result4 = stop(_arg02);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 5:
                            IAONEventListener _arg03 = IAONEventListener.Stub.asInterface(data.readStrongBinder());
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result5 = registerListener(_arg03, _arg1);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 6:
                            IAONEventListener _arg04 = IAONEventListener.Stub.asInterface(data.readStrongBinder());
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result6 = unRegisterListener(_arg04, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 7:
                            int _arg05 = data.readInt();
                            data.enforceNoDataAvail();
                            int _result7 = setGestureCourseState(_arg05);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 8:
                            int _arg06 = data.readInt();
                            String _arg13 = data.readString();
                            data.enforceNoDataAvail();
                            int _result8 = getEnrolledFaceCount(_arg06, _arg13);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 9:
                            int _arg07 = data.readInt();
                            String _arg14 = data.readString();
                            Surface _arg2 = (Surface) data.readTypedObject(Surface.CREATOR);
                            data.enforceNoDataAvail();
                            int _result9 = enroll(_arg07, _arg14, _arg2);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 10:
                            int _arg08 = data.readInt();
                            String _arg15 = data.readString();
                            data.enforceNoDataAvail();
                            int _result10 = remove(_arg08, _arg15);
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 11:
                            int _arg09 = data.readInt();
                            Bundle _arg16 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            data.enforceNoDataAvail();
                            int _result11 = notifyAON(_arg09, _arg16);
                            reply.writeNoException();
                            reply.writeInt(_result11);
                            return true;
                        case 12:
                            List<SensorBmp> _arg010 = data.createTypedArrayList(SensorBmp.CREATOR);
                            data.enforceNoDataAvail();
                            int _result12 = process(_arg010);
                            reply.writeNoException();
                            reply.writeInt(_result12);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IAONService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAONService.DESCRIPTOR;
            }

            @Override // com.aiunit.aon.utils.IAONService
            public String getServerState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public String getVersionInfo() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int start(int AonCmdType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeInt(AonCmdType);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int stop(int AonCmdType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeInt(AonCmdType);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int registerListener(IAONEventListener listener, int AonCmdType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    _data.writeInt(AonCmdType);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int unRegisterListener(IAONEventListener listener, int AonCmdType) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    _data.writeInt(AonCmdType);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int setGestureCourseState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int getEnrolledFaceCount(int userId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int enroll(int userId, String opPackageName, Surface previewSurface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(opPackageName);
                    _data.writeTypedObject(previewSurface, 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int remove(int userId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int notifyAON(int type, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeTypedObject(bundle, 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.aiunit.aon.utils.IAONService
            public int process(List<SensorBmp> sensorBmps) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAONService.DESCRIPTOR);
                    _data.writeTypedList(sensorBmps, 0);
                    this.mRemote.transact(12, _data, _reply, 0);
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
            return 11;
        }
    }
}
