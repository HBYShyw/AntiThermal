package android.os.customize;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeVpnManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.customize.IOplusCustomizeVpnManagerService";

    boolean deleteVpnProfile(ComponentName componentName, String str) throws RemoteException;

    int disestablishVpnConnection(ComponentName componentName) throws RemoteException;

    String getAlwaysOnVpnPackage(ComponentName componentName) throws RemoteException;

    boolean getVpnAlwaysOnPersis(String str) throws RemoteException;

    List<String> getVpnList(ComponentName componentName) throws RemoteException;

    int getVpnServiceState() throws RemoteException;

    boolean isVpnDisabled(ComponentName componentName) throws RemoteException;

    boolean setAlwaysOnVpnPackage(ComponentName componentName, String str, boolean z) throws RemoteException;

    void setVpnAlwaysOnPersis(boolean z) throws RemoteException;

    void setVpnDisabled(ComponentName componentName, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IOplusCustomizeVpnManagerService {
        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public int getVpnServiceState() throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public List<String> getVpnList(ComponentName componentName) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public boolean deleteVpnProfile(ComponentName componentName, String key) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public int disestablishVpnConnection(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public void setVpnDisabled(ComponentName admin, boolean disabled) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public boolean isVpnDisabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public boolean setAlwaysOnVpnPackage(ComponentName admin, String vpnPackage, boolean lockdown) throws RemoteException {
            return false;
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public String getAlwaysOnVpnPackage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public void setVpnAlwaysOnPersis(boolean lockdown) throws RemoteException {
        }

        @Override // android.os.customize.IOplusCustomizeVpnManagerService
        public boolean getVpnAlwaysOnPersis(String defval) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOplusCustomizeVpnManagerService {
        static final int TRANSACTION_deleteVpnProfile = 3;
        static final int TRANSACTION_disestablishVpnConnection = 4;
        static final int TRANSACTION_getAlwaysOnVpnPackage = 8;
        static final int TRANSACTION_getVpnAlwaysOnPersis = 10;
        static final int TRANSACTION_getVpnList = 2;
        static final int TRANSACTION_getVpnServiceState = 1;
        static final int TRANSACTION_isVpnDisabled = 6;
        static final int TRANSACTION_setAlwaysOnVpnPackage = 7;
        static final int TRANSACTION_setVpnAlwaysOnPersis = 9;
        static final int TRANSACTION_setVpnDisabled = 5;

        public Stub() {
            attachInterface(this, IOplusCustomizeVpnManagerService.DESCRIPTOR);
        }

        public static IOplusCustomizeVpnManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IOplusCustomizeVpnManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IOplusCustomizeVpnManagerService)) {
                return (IOplusCustomizeVpnManagerService) iin;
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
                    return "getVpnServiceState";
                case 2:
                    return "getVpnList";
                case 3:
                    return "deleteVpnProfile";
                case 4:
                    return "disestablishVpnConnection";
                case 5:
                    return "setVpnDisabled";
                case 6:
                    return "isVpnDisabled";
                case 7:
                    return "setAlwaysOnVpnPackage";
                case 8:
                    return "getAlwaysOnVpnPackage";
                case 9:
                    return "setVpnAlwaysOnPersis";
                case 10:
                    return "getVpnAlwaysOnPersis";
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
                data.enforceInterface(IOplusCustomizeVpnManagerService.DESCRIPTOR);
            }
            switch (code) {
                case 1598968902:
                    reply.writeString(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            int _result = getVpnServiceState();
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            ComponentName _arg0 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            List<String> _result2 = getVpnList(_arg0);
                            reply.writeNoException();
                            reply.writeStringList(_result2);
                            return true;
                        case 3:
                            ComponentName _arg02 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            String _arg1 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result3 = deleteVpnProfile(_arg02, _arg1);
                            reply.writeNoException();
                            reply.writeBoolean(_result3);
                            return true;
                        case 4:
                            ComponentName _arg03 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            int _result4 = disestablishVpnConnection(_arg03);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 5:
                            ComponentName _arg04 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            boolean _arg12 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setVpnDisabled(_arg04, _arg12);
                            reply.writeNoException();
                            return true;
                        case 6:
                            ComponentName _arg05 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            boolean _result5 = isVpnDisabled(_arg05);
                            reply.writeNoException();
                            reply.writeBoolean(_result5);
                            return true;
                        case 7:
                            ComponentName _arg06 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            String _arg13 = data.readString();
                            boolean _arg2 = data.readBoolean();
                            data.enforceNoDataAvail();
                            boolean _result6 = setAlwaysOnVpnPackage(_arg06, _arg13, _arg2);
                            reply.writeNoException();
                            reply.writeBoolean(_result6);
                            return true;
                        case 8:
                            ComponentName _arg07 = (ComponentName) data.readTypedObject(ComponentName.CREATOR);
                            data.enforceNoDataAvail();
                            String _result7 = getAlwaysOnVpnPackage(_arg07);
                            reply.writeNoException();
                            reply.writeString(_result7);
                            return true;
                        case 9:
                            boolean _arg08 = data.readBoolean();
                            data.enforceNoDataAvail();
                            setVpnAlwaysOnPersis(_arg08);
                            reply.writeNoException();
                            return true;
                        case 10:
                            String _arg09 = data.readString();
                            data.enforceNoDataAvail();
                            boolean _result8 = getVpnAlwaysOnPersis(_arg09);
                            reply.writeNoException();
                            reply.writeBoolean(_result8);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOplusCustomizeVpnManagerService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IOplusCustomizeVpnManagerService.DESCRIPTOR;
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public int getVpnServiceState() throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public List<String> getVpnList(ComponentName componentName) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public boolean deleteVpnProfile(ComponentName componentName, String key) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeTypedObject(componentName, 0);
                    _data.writeString(key);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public int disestablishVpnConnection(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public void setVpnDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeBoolean(disabled);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public boolean isVpnDisabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public boolean setAlwaysOnVpnPackage(ComponentName admin, String vpnPackage, boolean lockdown) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    _data.writeString(vpnPackage);
                    _data.writeBoolean(lockdown);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public String getAlwaysOnVpnPackage(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeTypedObject(admin, 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public void setVpnAlwaysOnPersis(boolean lockdown) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeBoolean(lockdown);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.customize.IOplusCustomizeVpnManagerService
            public boolean getVpnAlwaysOnPersis(String defval) throws RemoteException {
                Parcel _data = Parcel.obtain(asBinder());
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IOplusCustomizeVpnManagerService.DESCRIPTOR);
                    _data.writeString(defval);
                    this.mRemote.transact(10, _data, _reply, 0);
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
            return 9;
        }
    }
}
