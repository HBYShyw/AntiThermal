package com.oplus.quicksettings;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.quicksettings.Tile;
import com.oplus.quicksettings.IOplusTileService;

/* loaded from: classes.dex */
public interface IOplusTileManagerService extends IInterface {
    public static final String DESCRIPTOR = "com.oplus.quicksettings.IOplusTileManagerService";

    Tile getOplusTile(ComponentName componentName) throws RemoteException;

    void registerOplusTileService(IOplusTileService iOplusTileService) throws RemoteException;

    void setDeathRecipientToken(IBinder iBinder, ComponentName componentName) throws RemoteException;

    void updateOplusTile(Tile tile, ComponentName componentName) throws RemoteException;

    void updateRequest(Bundle bundle, ComponentName componentName) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusTileManagerService {
        @Override // com.oplus.quicksettings.IOplusTileManagerService
        public void registerOplusTileService(IOplusTileService oplusTileService) throws RemoteException {
        }

        @Override // com.oplus.quicksettings.IOplusTileManagerService
        public Tile getOplusTile(ComponentName name) throws RemoteException {
            return null;
        }

        @Override // com.oplus.quicksettings.IOplusTileManagerService
        public void updateOplusTile(Tile tile, ComponentName name) throws RemoteException {
        }

        @Override // com.oplus.quicksettings.IOplusTileManagerService
        public void updateRequest(Bundle bundle, ComponentName name) throws RemoteException {
        }

        @Override // com.oplus.quicksettings.IOplusTileManagerService
        public void setDeathRecipientToken(IBinder binder, ComponentName name) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusTileManagerService {
        static final int TRANSACTION_getOplusTile = 2;
        static final int TRANSACTION_registerOplusTileService = 1;
        static final int TRANSACTION_setDeathRecipientToken = 5;
        static final int TRANSACTION_updateOplusTile = 3;
        static final int TRANSACTION_updateRequest = 4;

        public Stub() {
            attachInterface(this, IOplusTileManagerService.DESCRIPTOR);
        }

        public static IOplusTileManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusTileManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusTileManagerService)) {
                return (IOplusTileManagerService) iin;
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
                    return "registerOplusTileService";
                case 2:
                    return "getOplusTile";
                case 3:
                    return "updateOplusTile";
                case 4:
                    return "updateRequest";
                case 5:
                    return "setDeathRecipientToken";
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
                data.enforceInterface(IOplusTileManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusTileManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            IOplusTileService _arg0 = IOplusTileService.Stub.asInterface(data.readStrongBinder());
                            data.enforceNoDataAvail();
                            registerOplusTileService(_arg0);
                            return true;
                        case 2:
                            ComponentName _arg02 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            Tile _result = getOplusTile(_arg02);
                            reply.writeNoException();
                            reply.writeTypedObject(_result, 1);
                            return true;
                        case 3:
                            Tile _arg03 = (Tile) data.readTypedObject(Tile.CREATOR);
                            ComponentName _arg1 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            updateOplusTile(_arg03, _arg1);
                            return true;
                        case 4:
                            Bundle _arg04 = (Bundle) data.readTypedObject(Bundle.CREATOR);
                            ComponentName _arg12 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            updateRequest(_arg04, _arg12);
                            return true;
                        case 5:
                            IBinder _arg05 = data.readStrongBinder();
                            ComponentName _arg13 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            setDeathRecipientToken(_arg05, _arg13);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusTileManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusTileManagerService.DESCRIPTOR;
            }

            @Override // com.oplus.quicksettings.IOplusTileManagerService
            public void registerOplusTileService(IOplusTileService oplusTileService) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTileManagerService.DESCRIPTOR);
                    _data.writeStrongInterface(oplusTileService);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.quicksettings.IOplusTileManagerService
            public Tile getOplusTile(ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusTileManagerService.DESCRIPTOR);
                    _data.writeTypedObject(name, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    Tile _result = (Tile) _reply.readTypedObject(Tile.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.oplus.quicksettings.IOplusTileManagerService
            public void updateOplusTile(Tile tile, ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTileManagerService.DESCRIPTOR);
                    _data.writeTypedObject(tile, 0);
                    _data.writeTypedObject(name, 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.quicksettings.IOplusTileManagerService
            public void updateRequest(Bundle bundle, ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTileManagerService.DESCRIPTOR);
                    _data.writeTypedObject(bundle, 0);
                    _data.writeTypedObject(name, 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.oplus.quicksettings.IOplusTileManagerService
            public void setDeathRecipientToken(IBinder binder, ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(IOplusTileManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeTypedObject(name, 0);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public int getMaxTransactionId() {
            return 4;
        }
    }
}
