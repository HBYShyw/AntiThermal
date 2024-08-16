package android.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusWallpaperManager extends IInterface {
    public static final String DESCRIPTOR = "android.app.IOplusWallpaperManager";

    WallpaperInfo getFoldWallpaperInfo(int i, int i2) throws RemoteException;

    boolean isWallpaperSupportBackup(int i, int i2) throws RemoteException;

    boolean rebindWallpaperComponent(ComponentName componentName, int i) throws RemoteException;

    void setFoldWallpaperComponentChecked(ComponentName componentName, String str, int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusWallpaperManager {
        @Override // android.app.IOplusWallpaperManager
        public void setFoldWallpaperComponentChecked(ComponentName name, String callingPackage, int userId, int which) throws RemoteException {
        }

        @Override // android.app.IOplusWallpaperManager
        public WallpaperInfo getFoldWallpaperInfo(int userId, int which) throws RemoteException {
            return null;
        }

        @Override // android.app.IOplusWallpaperManager
        public boolean rebindWallpaperComponent(ComponentName name, int physicalId) throws RemoteException {
            return false;
        }

        @Override // android.app.IOplusWallpaperManager
        public boolean isWallpaperSupportBackup(int userId, int which) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusWallpaperManager {
        static final int TRANSACTION_getFoldWallpaperInfo = 2;
        static final int TRANSACTION_isWallpaperSupportBackup = 4;
        static final int TRANSACTION_rebindWallpaperComponent = 3;
        static final int TRANSACTION_setFoldWallpaperComponentChecked = 1;

        public Stub() {
            attachInterface(this, IOplusWallpaperManager.DESCRIPTOR);
        }

        public static IOplusWallpaperManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusWallpaperManager.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusWallpaperManager)) {
                return (IOplusWallpaperManager) iin;
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
                    return "setFoldWallpaperComponentChecked";
                case 2:
                    return "getFoldWallpaperInfo";
                case 3:
                    return "rebindWallpaperComponent";
                case 4:
                    return "isWallpaperSupportBackup";
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
                data.enforceInterface(IOplusWallpaperManager.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusWallpaperManager.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            ComponentName _arg0 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            String _arg1 = data.readString();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            data.enforceNoDataAvail();
                            setFoldWallpaperComponentChecked(_arg0, _arg1, _arg2, _arg3);
                            reply.writeNoException();
                            return true;
                        case 2:
                            int _arg02 = data.readInt();
                            int _arg12 = data.readInt();
                            data.enforceNoDataAvail();
                            WallpaperInfo _result = getFoldWallpaperInfo(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 3:
                            ComponentName _arg03 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            int _arg13 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result2 = rebindWallpaperComponent(_arg03, _arg13);
                            reply.writeNoException();
                            reply.writeBoolean(_result2);
                            return true;
                        case 4:
                            int _arg04 = data.readInt();
                            int _arg14 = data.readInt();
                            data.enforceNoDataAvail();
                            boolean _result3 = isWallpaperSupportBackup(_arg04, _arg14);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusWallpaperManager {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusWallpaperManager.DESCRIPTOR;
            }

            @Override // android.app.IOplusWallpaperManager
            public void setFoldWallpaperComponentChecked(ComponentName name, String callingPackage, int userId, int which) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWallpaperManager.DESCRIPTOR);
                    _data.writeTypedObject(name, 0);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    _data.writeInt(which);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusWallpaperManager
            public WallpaperInfo getFoldWallpaperInfo(int userId, int which) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWallpaperManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(which);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    WallpaperInfo _result = (WallpaperInfo) _reply.readTypedObject(WallpaperInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusWallpaperManager
            public boolean rebindWallpaperComponent(ComponentName name, int physicalId) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWallpaperManager.DESCRIPTOR);
                    _data.writeTypedObject(name, 0);
                    _data.writeInt(physicalId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IOplusWallpaperManager
            public boolean isWallpaperSupportBackup(int userId, int which) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusWallpaperManager.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(which);
                    this.mRemote.transact(4, _data, _reply, 0);
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
            return 3;
        }
    }
}
