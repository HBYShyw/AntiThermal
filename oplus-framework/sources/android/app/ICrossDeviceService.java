package android.app;

import android.app.IProximityService;
import android.app.IRemoteTaskCallback;
import android.app.IRfcommOemManager;
import android.app.ITaskChangeListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.InputEvent;
import android.view.Surface;
import java.util.List;

/* loaded from: classes.dex */
public interface ICrossDeviceService extends IInterface {
    public static final String DESCRIPTOR = "android.app.ICrossDeviceService";

    void addRemoteTaskCallback(IRemoteTaskCallback iRemoteTaskCallback) throws RemoteException;

    boolean getPermissionGranted(int i) throws RemoteException;

    IProximityService getProximityService() throws RemoteException;

    IRfcommOemManager getRfcommOemManager() throws RemoteException;

    int getThermalStatus() throws RemoteException;

    String getTopRunningPackageName() throws RemoteException;

    void injectInputEvent(InputEvent inputEvent, int i) throws RemoteException;

    boolean isResumableFromOverheat() throws RemoteException;

    void registerTaskChangeListener(ITaskChangeListener iTaskChangeListener) throws RemoteException;

    void removeRemoteTaskCallback(IRemoteTaskCallback iRemoteTaskCallback) throws RemoteException;

    void setPermissionGranted(int i) throws RemoteException;

    void setSecurityToken(String str) throws RemoteException;

    void unregisterTaskChangeListener(ITaskChangeListener iTaskChangeListener) throws RemoteException;

    void updateBackgroundActivityList(List<String> list) throws RemoteException;

    void updateDisplayInfo(int i, int i2, int i3) throws RemoteException;

    void updateSurface(String str, Surface surface) throws RemoteException;

    void wakeUp(long j) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ICrossDeviceService {
        @Override // android.app.ICrossDeviceService
        public void injectInputEvent(InputEvent event, int injectInputEventModeAsync) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public String getTopRunningPackageName() throws RemoteException {
            return null;
        }

        @Override // android.app.ICrossDeviceService
        public void wakeUp(long time) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public void addRemoteTaskCallback(IRemoteTaskCallback callback) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public void removeRemoteTaskCallback(IRemoteTaskCallback callback) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public void updateBackgroundActivityList(List<String> activityList) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public void updateSurface(String uuid, Surface surface) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public void updateDisplayInfo(int width, int height, int densityDpi) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public int getThermalStatus() throws RemoteException {
            return 0;
        }

        @Override // android.app.ICrossDeviceService
        public boolean isResumableFromOverheat() throws RemoteException {
            return false;
        }

        @Override // android.app.ICrossDeviceService
        public IRfcommOemManager getRfcommOemManager() throws RemoteException {
            return null;
        }

        @Override // android.app.ICrossDeviceService
        public void setPermissionGranted(int uid) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public boolean getPermissionGranted(int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.ICrossDeviceService
        public void setSecurityToken(String securityToken) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public void registerTaskChangeListener(ITaskChangeListener listener) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public void unregisterTaskChangeListener(ITaskChangeListener listener) throws RemoteException {
        }

        @Override // android.app.ICrossDeviceService
        public IProximityService getProximityService() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ICrossDeviceService {
        static final int TRANSACTION_addRemoteTaskCallback = 4;
        static final int TRANSACTION_getPermissionGranted = 13;
        static final int TRANSACTION_getProximityService = 17;
        static final int TRANSACTION_getRfcommOemManager = 11;
        static final int TRANSACTION_getThermalStatus = 9;
        static final int TRANSACTION_getTopRunningPackageName = 2;
        static final int TRANSACTION_injectInputEvent = 1;
        static final int TRANSACTION_isResumableFromOverheat = 10;
        static final int TRANSACTION_registerTaskChangeListener = 15;
        static final int TRANSACTION_removeRemoteTaskCallback = 5;
        static final int TRANSACTION_setPermissionGranted = 12;
        static final int TRANSACTION_setSecurityToken = 14;
        static final int TRANSACTION_unregisterTaskChangeListener = 16;
        static final int TRANSACTION_updateBackgroundActivityList = 6;
        static final int TRANSACTION_updateDisplayInfo = 8;
        static final int TRANSACTION_updateSurface = 7;
        static final int TRANSACTION_wakeUp = 3;

        public Stub() {
            attachInterface(this, ICrossDeviceService.DESCRIPTOR);
        }

        public static ICrossDeviceService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ICrossDeviceService.DESCRIPTOR);
            if (iin != null && (iin instanceof ICrossDeviceService)) {
                return (ICrossDeviceService) iin;
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
                    return "injectInputEvent";
                case 2:
                    return "getTopRunningPackageName";
                case 3:
                    return "wakeUp";
                case 4:
                    return "addRemoteTaskCallback";
                case 5:
                    return "removeRemoteTaskCallback";
                case 6:
                    return "updateBackgroundActivityList";
                case 7:
                    return "updateSurface";
                case 8:
                    return "updateDisplayInfo";
                case 9:
                    return "getThermalStatus";
                case 10:
                    return "isResumableFromOverheat";
                case 11:
                    return "getRfcommOemManager";
                case 12:
                    return "setPermissionGranted";
                case 13:
                    return "getPermissionGranted";
                case 14:
                    return "setSecurityToken";
                case 15:
                    return "registerTaskChangeListener";
                case 16:
                    return "unregisterTaskChangeListener";
                case 17:
                    return "getProximityService";
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
                data.enforceInterface(ICrossDeviceService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(ICrossDeviceService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            InputEvent _arg0 = (InputEvent) data.readTypedObject(InputEvent.CREATOR);
                            int _arg1 = data.readInt();
                            data.enforceNoDataAvail();
                            injectInputEvent(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            String _result = getTopRunningPackageName();
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 3:
                            long _arg02 = data.readLong();
                            data.enforceNoDataAvail();
                            wakeUp(_arg02);
                            reply.writeNoException();
                            return true;
                        case 4:
                            IRemoteTaskCallback _arg03 = IRemoteTaskCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            addRemoteTaskCallback(_arg03);
                            reply.writeNoException();
                            return true;
                        case 5:
                            IRemoteTaskCallback _arg04 = IRemoteTaskCallback.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            removeRemoteTaskCallback(_arg04);
                            reply.writeNoException();
                            return true;
                        case 6:
                            List<String> _arg05 = data.createStringArrayList();
                            data.enforceNoDataAvail();
                            updateBackgroundActivityList(_arg05);
                            reply.writeNoException();
                            return true;
                        case 7:
                            String _arg06 = data.readString();
                            Surface _arg12 = (Surface) data.readTypedObject(Surface.CREATOR);
                            data.enforceNoDataAvail();
                            updateSurface(_arg06, _arg12);
                            reply.writeNoException();
                            return true;
                        case 8:
                            int _arg07 = data.readInt();
                            int _arg13 = data.readInt();
                            int _arg2 = data.readInt();
                            data.enforceNoDataAvail();
                            updateDisplayInfo(_arg07, _arg13, _arg2);
                            reply.writeNoException();
                            return true;
                        case 9:
                            int _result2 = getThermalStatus();
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 10:
                            boolean _result3 = isResumableFromOverheat();
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 11:
                            IRfcommOemManager _result4 = getRfcommOemManager();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result4);
                            return true;
                        case 12:
                            int _arg08 = data.readInt();
                            data.enforceNoDataAvail();
                            setPermissionGranted(_arg08);
                            reply.writeNoException();
                            return true;
                        case 13:
                            int _arg09 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result5 = getPermissionGranted(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 14:
                            String _arg010 = data.readString();
                            data.enforceNoDataAvail();
                            setSecurityToken(_arg010);
                            reply.writeNoException();
                            return true;
                        case 15:
                            ITaskChangeListener _arg011 = ITaskChangeListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerTaskChangeListener(_arg011);
                            reply.writeNoException();
                            return true;
                        case 16:
                            ITaskChangeListener _arg012 = ITaskChangeListener.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            unregisterTaskChangeListener(_arg012);
                            reply.writeNoException();
                            return true;
                        case 17:
                            IProximityService _result6 = getProximityService();
                            reply.writeNoException();
                            reply.writeStrongInterface(_result6);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ICrossDeviceService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ICrossDeviceService.DESCRIPTOR;
            }

            @Override // android.app.ICrossDeviceService
            public void injectInputEvent(InputEvent event, int injectInputEventModeAsync) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeTypedObject(event, 0);
                    _data.writeInt(injectInputEventModeAsync);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public String getTopRunningPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void wakeUp(long time) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeLong(time);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void addRemoteTaskCallback(IRemoteTaskCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void removeRemoteTaskCallback(IRemoteTaskCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void updateBackgroundActivityList(List<String> activityList) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeStringList(activityList);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void updateSurface(String uuid, Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeTypedObject(surface, 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void updateDisplayInfo(int width, int height, int densityDpi) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(densityDpi);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public int getThermalStatus() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public boolean isResumableFromOverheat() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public IRfcommOemManager getRfcommOemManager() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    IRfcommOemManager _result = IRfcommOemManager.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void setPermissionGranted(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public boolean getPermissionGranted(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void setSecurityToken(String securityToken) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeString(securityToken);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void registerTaskChangeListener(ITaskChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public void unregisterTaskChangeListener(ITaskChangeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.ICrossDeviceService
            public IProximityService getProximityService() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ICrossDeviceService.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    IProximityService _result = IProximityService.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 16;
        }
    }
}
